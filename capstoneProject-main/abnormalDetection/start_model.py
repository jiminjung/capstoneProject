import cv2
import mediapipe as mp
import numpy as np
import sys
from collections import Counter
from tensorflow.python.keras.models import load_model
from fcm_push import FcmNotification
from video_stream import VideoStream
from save_abnormal_video import SaveAbnormalVideo

user_id = sys.argv[1]
rasp_ip = sys.argv[2]
name = sys.argv[3]

actions = ['error', 'suffer', 'fall', 'sit', 'sit', 'walk', 'stand', 'lie', 'jump']
seq_length = 30

model = load_model('models/modelV5.7_WINDOW=30.h5')

# MediaPipe hands model
mp_pose = mp.solutions.pose
mp_drawing = mp.solutions.drawing_utils
pose = mp_pose.Pose(
    min_detection_confidence=0.6,
    min_tracking_confidence=0.6)

# stream_link = "http://" + rasp_ip + ":9090/deeplearning/?deeplearning=capstone2022"

videoStream = VideoStream(0)
saveAbnormalVideo = SaveAbnormalVideo(videoStream)

seq = []
action_queue = []
img_queue = []
pre_action = ""
fcmNotification = FcmNotification()
fcmNotification.updateToken(user_id)

while videoStream.capture.isOpened():
    try:
        ret, img = videoStream.get_frame()
        if not(ret):
            break

        # img = cv2.flip(img, 1)
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        res = pose.process(img)
        img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

        if res.pose_landmarks is not None:
            # for res in result.multi_hand_landmarks:

            joint = np.zeros((33, 4))
            for j, lm in enumerate(res.pose_landmarks.landmark):
                joint[j] = [lm.x, lm.y, lm.z, lm.visibility]

            # Compute angles between joints
            v1 = joint[[0,0,1,2,0,4,5,3,6,0,0,0,0,11,12,13,14,15,16,15,16,15,16,11,12,23,24,25,26,27,28,27,28,11,12,23,24], :3] # Parent joint
            v2 = joint[[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,12,11,24,23], :3] # Child joint
            v = v2 - v1 # [33, 4] but index[0] is null
            # Normalize v : 단위 벡터로 변환
            v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

            # Get angle using arcos of dot product : 단위 벡터에 대한 arccos 연산을 통해 사잇각을 얻을 수 있다.
            angle = np.arccos(np.einsum('nt,nt->n',
                v[[11,11,12,13,14,15,16,15,16,15,16,11,12,23,24,25,26,27,28,27,28,33,34,33,34,35,36,35,36],:], 
                v[[12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,23,24,13,14,23,24,25,26],:])) # [29,]

            angle = np.degrees(angle) # Convert radian to degree

            d = np.concatenate([joint.flatten(), angle])

            seq.append(d)

            mp_drawing.draw_landmarks(img, res.pose_landmarks, mp_pose.POSE_CONNECTIONS)

            if len(seq) < seq_length:
                continue

            input_data = np.expand_dims(np.array(seq[-seq_length:], dtype=np.float32), axis=0)

            y_pred = model.predict(input_data).squeeze()

            i_pred = int(np.argmax(y_pred))

            action = actions[i_pred]
            action_queue.append(action)

            if len(action_queue) <= 15:
                continue
            
            del action_queue[0]

            action_counter = Counter(action_queue)
            most_action_tuple = action_counter.most_common().pop(0)
            this_action = most_action_tuple[0] # one action has the most value.
            if most_action_tuple[1] < 10:
                this_action = "unknown"

            if this_action == "fall":
                cv2.putText(img, f'{this_action.upper()}', org=(int(res.pose_landmarks.landmark[0].x * img.shape[1]), int(res.pose_landmarks.landmark[0].y * img.shape[0] + 20)), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 0, 255), thickness=2)
                if pre_action != "fall":
                    fcmNotification.sendMessage(state = this_action, ip_address = rasp_ip)
            elif this_action == "unknown":
                cv2.putText(img, f'{this_action.upper()}', org=(int(res.pose_landmarks.landmark[0].x * img.shape[1]), int(res.pose_landmarks.landmark[0].y * img.shape[0] + 20)), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 255, 0), thickness=2)
            elif this_action is not None:
                cv2.putText(img, f'{this_action.upper()}', org=(int(res.pose_landmarks.landmark[0].x * img.shape[1]), int(res.pose_landmarks.landmark[0].y * img.shape[0] + 20)), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=(0, 255, 0), thickness=2)
            else:
                continue
            
            if pre_action == "fall" and this_action != "fall":
                saveAbnormalVideo.set_img_queue(img_queue)
                saveAbnormalVideo.save_abnormal_video(user_id, name, rasp_ip)

            pre_action = this_action

        cv2.namedWindow('Abnormal Detection', flags=cv2.WINDOW_NORMAL)
        cv2.imshow('Abnormal Detection', img)

        if len(img_queue) >= 150:
            del img_queue[0]
        img_queue.append(img)
    except AttributeError:
        pass
    except TypeError:
        break

    # Press Q on keyboard to stop recording
    key = cv2.waitKey(1)
    if key == ord('q'):
        videoStream.capture.release()
        cv2.destroyAllWindows()

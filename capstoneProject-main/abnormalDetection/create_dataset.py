import cv2
import mediapipe as mp
import numpy as np
import time, os #시간 호출
#error is at last
actions = ['error', 'suffer', 'falling', 'lying', 'sitting', 'walking', 'standing', 'lain', 'jump']
idx = 6
seq_length = 30

folderPath = "C:/Users/sang9/OneDrive/바탕 화면/preprocessing/" + actions[idx]

#MediaPipe pose model
mp_pose = mp.solutions.pose
mp_drawing = mp.solutions.drawing_utils
poses = mp_pose.Pose(
    min_detection_confidence=0.6,
    min_tracking_confidence=0.6)

for (root, directories, files) in os.walk(folderPath):
    for file in files:
        file_path = os.path.join(root, file)
        print(file_path)

        if os.path.isfile(file_path):
            cap = cv2.VideoCapture(file_path)
        else:
            print("file is not exist")

        created_time = int(time.time())
        os.makedirs('dataset', exist_ok=True)

        while cap.isOpened():
            for action in actions:
                if action != actions[idx]:
                    continue

                data = []
                ret, img = cap.read()
                img = cv2.flip(img, 1)
                cnt = 0

                while True:
                    ret, img = cap.read()
                    if ret != True:
                        break

                    # cnt += 1
                    # if cnt < 43 or cnt > 97: # Frame Skipped
                    #     continue
                    # if cnt%2 < 1: # 30Frame to 15Frame
                    #     continue

                    img = cv2.flip(img, 1)
                    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
                    result = poses.process(img)
                    img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

                    if result.pose_landmarks is not None:
                        # for res in result.pose_landmarks:
                        res = result
                        joint = np.zeros((33, 4))
                        for j, lm in enumerate(res.pose_landmarks.landmark):
                            joint[j] = [lm.x, lm.y, lm.z, lm.visibility]

                        # Compute angles between joints
                        v1 = joint[[0,0,1,2,0,4,5,3,6,0,0 ,0 ,0 ,11,12,13,14,15,16,15,16,15,16,11,12,23,24,25,26,27,28,27,28,11,12,23,24], :3] # Parent joint
                        v2 = joint[[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,12,11,24,23], :3] # Child joint
                        v = v2 - v1 # [33, 3] but index[0] is zero vector
                        # Normalize v
                        v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]

                        # Get angle using arcos of dot product
                        angle = np.arccos(np.einsum('nt,nt->n',
                            v[[11,11,12,13,14,15,16,15,16,15,16,11,12,23,24,25,26,27,28,27,28,33,34,33,34,35,36,35,36],:], 
                            v[[12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,23,24,13,14,23,24,25,26],:])) # [29,]

                        angle = np.degrees(angle) # Convert radian to degree

                        angle_label = np.array([angle], dtype=np.float32)
                        angle_label = np.append(angle_label, idx) # append answer(idx = 0,1,2,3,...)

                        d = np.concatenate([joint.flatten(), angle_label])

                        data.append(d)

                        mp_drawing.draw_landmarks(img, res.pose_landmarks, mp_pose.POSE_CONNECTIONS)

                    # cv2.imshow('img', img)
                    # if cv2.waitKey(1) == 27:
                    #     break

                #data array is composed of x,y,z coordinates(0:99) and visibility(99:132) and angles(132:161) and label(161:162)
                data = np.array(data)
                print(action, data.shape)
                np.save(os.path.join('dataset', f'raw_{action}_{created_time}'), data)


                # Create sequence data
                full_seq_data = []
                for seq in range(len(data) - seq_length):
                    full_seq_data.append(data[seq:seq + seq_length]) # increase axis by sliding window(seq_length)

                full_seq_data = np.array(full_seq_data)
                print(action, full_seq_data.shape)
                np.save(os.path.join('dataset', f'seq_{action}_{created_time}'), full_seq_data)
            break
        cap.release()
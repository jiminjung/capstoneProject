from threading import Thread
from database.videoDAO import VideoDAO
import cv2
import time
import os

class SaveAbnormalVideo():
    def __init__(self, videoStream):
        # frame size convert to int_type
        self.frameWidth = int(videoStream.capture.get(cv2.CAP_PROP_FRAME_WIDTH))
        self.frameHeight = int(videoStream.capture.get(cv2.CAP_PROP_FRAME_HEIGHT))
        self.frameRate = int(videoStream.capture.get(cv2.CAP_PROP_FPS))
        self.fourcc = cv2.VideoWriter_fourcc(*'avc1')
        self.videoDAO = VideoDAO()
        
    def set_img_queue(self, img_queue):
        self.img_queue = img_queue

    def save_abnormal_video(self, id, name, ip):
        file_name = str(time.time())
        try:
            self.thread = Thread(target=self.save_video, args=())
            os.makedirs('abnormalVideo', exist_ok=True)
            self.out = cv2.VideoWriter(os.path.join('abnormalVideo', f'{file_name}.mp4'), self.fourcc, self.frameRate, (self.frameWidth, self.frameHeight))
            self.thread.start()
        except:
            pass
        else:
            url = "115.143.101.226:18909/video/" + f'{file_name}.mp4'
            self.videoDAO.insertValue(id, name, url, ip)

    def save_video(self):
        for img in self.img_queue:
            self.out.write(img)

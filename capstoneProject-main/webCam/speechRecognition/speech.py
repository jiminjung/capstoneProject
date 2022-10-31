from ipaddress import ip_address
import speech_recognition as sr 
from pyfcm import FCMNotification
import requests
import urllib.request

url = "http://210.117.128.200:8080/getToken"

APIKEY = "AAAAPe8PBM4:APA91bHh9s_Dur9z4LOveWHhZwSo3I7BWAfqRrbew5reQn_IbipupXiiy-Nx1Jmdc72CODxXNzlNZchkY2S8uEpSg-CwlmxX7fIcGZ9yAOuCtdsaeqPaSztjC0DwXI4qzk8LLtKsw0fX"
#TOKEN = "fPAvbm3cTSyKe1P2sfj-Yg:APA91bEvjEhHXkFoRaZMq2LSpO4484ntln4kHJkHwP2ncyVCij_MBlcGAoG_C2OiqWCcTFHoLy8Evr56I36pUwodMagA_WULeMWPqwwUnh8koaycglTbt-86CLjbvnOPVGCH9ZLt_Hmd"
TOKEN = requests.get(url).text

push_service = FCMNotification(APIKEY)

ip = urllib.request.urlopen('https://ident.me').read().decode('utf8') # ip 동적으로 얻어옴

def sendMessage(message: str):
    data_message = {
        "title" : "4.O",
        "body" : message,
        "ip" : ip
    }

    result = push_service.single_device_data_message(registration_id = TOKEN, data_message = data_message)
    print(result)

r = sr.Recognizer() #Recognizer 사용
mic = sr.Microphone() #Mic 선언

print("hello")

while True:
    with mic as source:
        audio = r.record(source, duration = 4)
        try:
            words = r.recognize_google(audio, language = 'ko-KR') #입력받은 음성을 전환 
            print(words)

            if words.find("살려 줘") != -1:
                sendMessage("응급 음성 감지")

            if words.find("살려줘") != -1:
                sendMessage("응급 음성 감지")
        
            if words.find("나 죽네") != -1:
                sendMessage("응급 음성 감지")

            if words.find("나 죽어") != -1:
                sendMessage("응급 음성 감지")

        except sr.WaitTimeoutError:
            print("Google Speech Recognition TimeOut")
            
        except sr.UnknownValueError:
            print("Google Speech Recognition could not understand audio")

        except sr.RequestError:
            print("Google Speech Recognition could not request")

        

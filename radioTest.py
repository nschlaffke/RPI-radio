'''
Dependencies: vlc, keyboard
keyboard needs root privileges to run properly
'''

from radio import Radio
import keyboard
from time import sleep
from RaspberryAudioController import RaspberryAudioController

stations = ['http://20133.live.streamtheworld.com/SRGSTR02AAC.aac',
            'http://host4.whooshserver.com:9162/live', 'http://i30.letio.com/9166.aac']
rd = Radio(stations)
rd.stationDown()

audioController = RaspberryAudioController(30)

while True:
    print("1. volume up\n2. volume down")
    action = int(input())
    if action == 1:
        audioController.volumeUp()
    if action == 2:
        audioController.volumeDown()
    else:
        print("enter 1 or 2")

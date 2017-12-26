'''
Dependencies: vlc, keyboard
keyboard needs root privileges to run properly
'''

from radio import Radio
import keyboard
from time import sleep

stations = ['http://20133.live.streamtheworld.com/SRGSTR02AAC.aac',
            'http://host4.whooshserver.com:9162/live', 'http://i30.letio.com/9166.aac']
rd = Radio(stations)
rd.stationDown()
while True:
    try:
        if keyboard.is_pressed('a'):
            print('Station up')
            rd.stationUp()
            print('Changed to ' + str(rd.getInfo()))
        if keyboard.is_pressed('b'):
            print('Station down')
            rd.stationDown()
            print('Changed to ' + str(rd.getInfo()))
        if keyboard.is_pressed('c'):
            print('pause')
            rd.pause()
        sleep(0.1)
    except:
        pass

'''
Dependencies: vlc, keyboard
keyboard needs root privileges to run properly
'''

from radio import Radio
from databaseController import DatabaseController
from RaspberryAudioController import RaspberryAudioController

db = DatabaseController("remote", "3edcvfr4", "192.168.8.110", "radio")
db.connect()
stations = db.getStationsList()

rd = Radio(stations)
audioController = RaspberryAudioController(30)
paused = True
mapping = db.getGesturesDict()

while True:
    print("1. volume up\n2. volume down\n3. station up\n4. station down\n5. play\pause\n")
    action = int(input())
    if action == mapping[1]:
        audioController.volumeUp()
        db.insertAction('volume_up')
    if action == mapping[2]:
        audioController.volumeDown()
        db.insertAction('volume_down')
    if action == mapping[3]:
        rd.stationUp()
        db.insertDescribedAction('station_up', rd.getInfo())
    if action == mapping[4]:
        rd.stationDown()
        db.insertDescribedAction('station_down', rd.getInfo())
    if action == mapping[5]:
        if rd.isPaused():
            rd.play()
            db.insertAction('play')
        else:
            rd.pause()
            db.insertAction('pause')

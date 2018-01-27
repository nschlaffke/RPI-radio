'''
Dependencies: vlc, keyboard
keyboard needs root privileges to run properly
'''

from radio import Radio
from databaseController import DatabaseController
from RaspberryAudioController import RaspberryAudioController

db = DatabaseController("remote", "3edcvfr4", "192.168.9.9", "radio")
db.connect()
stations = db.getStationsList()

rd = Radio(stations)
audioController = RaspberryAudioController(30)
paused = True
mapping = db.getGesturesDict()
db.insertDescribedAction('volume_up', str(audioController.getVolume()))
print(str(audioController.getVolume()))
while True:
    print("1. volume up\n2. volume down\n3. station up\n4. station down\n5. play\pause\n")
    action = int(input())
    if action == mapping['volume_up']:
        audioController.volumeUp()
        db.insertDescribedAction('volume_up', str(audioController.getVolume()))
    if action == mapping['volume_down']:
        audioController.volumeDown()
        db.insertDescribedAction('volume_down', str(audioController.getVolume()))
    if action == mapping['station_up']:
        rd.stationUp()
        db.insertDescribedAction('station_up', rd.getInfo())
    if action == mapping['station_down']:
        rd.stationDown()
        db.insertDescribedAction('station_down', rd.getInfo())
    if action == mapping['play']:
        if rd.isPaused():
            rd.play()
            db.insertAction('play')
        else:
            rd.pause()
            db.insertAction('pause')

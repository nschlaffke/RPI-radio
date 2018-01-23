from databaseController import DatabaseController

db = DatabaseController("remote", "3edcvfr4", "192.168.8.110", "radio")
db.connect()
for x in db.getStationsList():
    print(x)
db.insertDescribedAction('volume_up', 'descripton')
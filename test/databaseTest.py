from DatabaseController import DatabaseController

db = DatabaseController("database/radio.db")
db.connect()
for x in db.getStationsList():
    print(x)
import sqlite3


class DatabaseController:
    STATEMENT = 'SELECT url FROM stations ORDER BY station_id ASC'
    def __init__(self, path):
        self.__path = path

    def connect(self):
        try:
            self.__db = sqlite3.connect(self.__path)
        except sqlite3.Error as e:
            print('An error occured: ', e.args[0])

    def getStationsList(self):
        cursor = self.__db.cursor()
        cursor.execute(DatabaseController.STATEMENT)
        result = cursor.fetchall()
        stations = [x[0] for x in result]
        return stations

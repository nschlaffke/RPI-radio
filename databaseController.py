import mysql.connector


class DatabaseController:

    GET_STATIONS = 'SELECT url FROM stations ORDER BY station_id ASC'
    INSERT_ACTION = 'INSERT INTO actions(action) VALUES(%s)'
    INSERT_DESCRIBED_ACTION = 'INSERT INTO actions(action, description) VALUES(%s, %s)'
    ACTIONS = ('volume_up', 'volume_down', 'station_up', 'station_down', 'play', 'pause')

    def __init__(self, user, password, host, database):
        self._user = user
        self._password = password
        self._host = host
        self._database = database

    def connect(self):
        try:
            self._cnx = mysql.connector.connect(user=self._user, password=self._password,
                                                host=self._host, database=self._database)
        except mysql.connector.Error as err:
            print('An error occured: ', err)

    def getStationsList(self):
        cursor = self._cnx.cursor()
        cursor.execute(DatabaseController.GET_STATIONS)
        result = cursor.fetchall()
        stations = [x[0] for x in result]
        return stations

    def insertAction(self, action):
        if not(action in self.ACTIONS):
            raise ValueError("Wrong action")
        cursor = self._cnx.cursor()
        cursor.execute(DatabaseController.INSERT_ACTION, (action,))
        self._cnx.commit()

    def insertDescribedAction(self, action, description):
        if not(action in self.ACTIONS):
            raise ValueError("Wrong action")
        cursor = self._cnx.cursor()
        cursor.execute(DatabaseController.INSERT_DESCRIBED_ACTION, (action, description,))
        self._cnx.commit()






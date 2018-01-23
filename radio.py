import vlc

class Radio:
    def __init__(self, stationsList):
        self.__instance = vlc.Instance('--input-repeat=-1', '--fullscreen')
        self.__player = self.__instance.media_player_new()
        self.__stationsList = stationsList
        self.__numberOfStations = len(stationsList)
        self.__currentStation = self.__numberOfStations//2
        self.__paused = True

    def play(self):
        self.__media = self.__instance.media_new(self.__stationsList[self.__currentStation])
        self.__player.set_media(self.__media)
        self.__player.play()
        self.__paused = False

    def stationUp(self):
        if self.__currentStation == self.__numberOfStations - 1:
            return
        self.__currentStation += 1
        self.play()

    def stationDown(self):
        if self.__currentStation == 0:
            return
        self.__currentStation -= 1
        self.play()

    def pause(self):
        self.__player.pause()
        self.__paused = True

    def getInfo(self):
        return self.__stationsList[self.__currentStation]

    def isPaused(self):
        return self.__paused


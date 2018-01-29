import alsaaudio

class RaspberryAudioController():
    CHANNEL_NUMBER = 0
    def __init__(self, step):
        self.__step = step
        self.__mixer = alsaaudio.Mixer('PCM')

    def getStep(self):
         return self.__step

    def getVolume(self):
        volume = self.__mixer.getvolume()
        return volume[self.CHANNEL_NUMBER]

    def volumeUp(self):
        step = self.getStep()
        volume = self.getVolume()
        if volume + step >= 100:
           self.__mixer.setvolume(100)
        else:
            self.__mixer.setvolume(volume + step)

    def volumeDown(self):
        step = self.getStep()
        volume = self.getVolume()
        if volume - step <= 0:
            self.__mixer.setvolume(0)
        else:
            self.__mixer.setvolume(volume - step)

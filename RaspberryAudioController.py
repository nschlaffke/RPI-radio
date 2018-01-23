from AudioController import AudioController
import alsaaudio

class RaspberryAudioController(AudioController):
    def __init__(self, step):
        super().__init__(step)
        self.__mixer = alsaaudio.Mixer("PCM")

    def getVolume(self):
        [volume] = self.__mixer.getvolume()
        return volume

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

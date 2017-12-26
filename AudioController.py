from abc import ABC, abstractmethod

class AudioController(ABC):
    def __init__(self, step):
        self.__step = step

    @abstractmethod
    def volumeUp(self):
        pass

    @abstractmethod
    def volumeDown(self):
        pass

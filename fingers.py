import handDetection as hd
import random

random.seed()

def finger():
    how_many = hd.determineNumberOfFingers()
    if how_many == 0:
        return 0
    else:
        return random.randrange(1,6,1)

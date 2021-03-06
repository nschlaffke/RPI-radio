import cv2
import numpy as np
import math

def prepareThresholdedImage(img):
    # convert to grayscale
    grey = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # applying gaussian blur
    value = (35, 35)
    blurred = cv2.GaussianBlur(grey, value, 0)

    # thresholding: Otsu's Binarization method
    _, thresh = cv2.threshold(blurred, 127, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

    # show thresholded image
    cv2.imshow('Thresholded', thresh)

    return thresh

def drawContoursAndHull(img, thresh):

    image, contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
    # find contour with max area
    cnt = max(contours, key=lambda x: cv2.contourArea(x))

    # create bounding rectangle around the contour (can skip below two lines)
    x, y, w, h = cv2.boundingRect(cnt)
    cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 0)

    # finding convex hull
    hull = cv2.convexHull(cnt)

    # drawing contours
    drawing = np.zeros(img.shape, np.uint8)
    cv2.drawContours(drawing, [cnt], 0, (0, 255, 0), 0)
    cv2.drawContours(drawing, [hull], 0, (0, 0, 255), 0)

    # finding convex hull
    hull = cv2.convexHull(cnt, returnPoints=False)

    # finding convexity defects
    defects = cv2.convexityDefects(cnt, hull)
    cv2.drawContours(thresh, contours, -1, (0, 255, 0), 3)

    return defects, cnt, drawing


def countFingers(img, defects, cnt):
    defectsCount = 0

    # applying Cosine Rule to find angle for all defects (between fingers)
    # with angle > 90 degrees and ignore defects
    for i in range(defects.shape[0]):
        s, e, f, d = defects[i, 0]

        start = tuple(cnt[s][0])
        end = tuple(cnt[e][0])
        far = tuple(cnt[f][0])

        # find length of all sides of triangle
        a = math.sqrt((end[0] - start[0]) ** 2 + (end[1] - start[1]) ** 2)
        b = math.sqrt((far[0] - start[0]) ** 2 + (far[1] - start[1]) ** 2)
        c = math.sqrt((end[0] - far[0]) ** 2 + (end[1] - far[1]) ** 2)

        # apply cosine rule here
        angle = math.acos((b ** 2 + c ** 2 - a ** 2) / (2 * b * c)) * 57

        # ignore angles > 90 and highlight rest with red dots
        if angle <= 90:
            defectsCount += 1
            cv2.circle(img, far, 1, [0, 0, 255], -1)
        # dist = cv2.pointPolygonTest(cnt,far,True)

        # draw a line from start to end i.e. the convex points (finger tips)
        # (can skip this part)
        cv2.line(img, start, end, [0, 255, 0], 2)
        # cv2.circle(cropImg,far,5,[0,0,255],-1)

    return defectsCount

def determineNumberOfFingers():

    cap = cv2.VideoCapture(0)
    fingersCount = 0
    #while(cap.isOpened()):
    if(cap.isOpened()):

        # read image
        ret, img = cap.read()

        # get hand data from the rectangle sub window on the screen
        #cv2.rectangle(img, (300, 300), (100, 100), (0, 255, 0), 0)
        #cropImg = img[100:300, 100:300]

        thresh1 = prepareThresholdedImage(img)
        defects, cnt, drawing = drawContoursAndHull(img, thresh1)
        fingersCount = countFingers(img, defects, cnt)

        #cv2.putText(img, str(fingersCount), (50, 50), cv2.FONT_HERSHEY_SIMPLEX, 2, 2)

        # show appropriate images in windows
        #cv2.imshow('Gesture', img)
        #all_img = np.hstack((drawing, cropImg))
        #cv2.imshow('Contours', all_img)

        #k = cv2.waitKey(10)
        #if k == 27:
            #break

    return fingersCount

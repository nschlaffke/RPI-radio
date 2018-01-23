DROP DATABASE IF EXISTS RADIO;
DROP DATABASE IF EXISTS radio;
CREATE DATABASE radio;
USE radio;
DROP TABLE IF EXISTS stations;

CREATE TABLE stations
(
    station_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    url TEXT NOT NULL
);

DROP TABLE IF EXISTS actions;
CREATE TABLE actions
(
    action_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    action VARCHAR(25) NOT NULL,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(300) NULL,
    CONSTRAINT CHK_action CHECK (action IN ('volume_up', 'volume_down', 'play', 'pause', 'station_up', 'station_down'))
);

DROP TABLE IF EXISTS gestures;
CREATE TABLE gestures
(
    gesture INTEGER PRIMARY KEY NOT NULL,
    action VARCHAR(25) NOT NULL,
    CONSTRAINT CHK_action CHECK (action IN ('volume_up', 'volume_down', 'play', 'pause', 'station_up', 'station_down')),
    CONSTRAINT CHK_gesture CHECK(gesture>=1 AND gesture<=5)
);

INSERT INTO stations(url) 
VALUES('http://20133.live.streamtheworld.com/SRGSTR02AAC.aac');

INSERT INTO stations(url) 
VALUES('http://host4.whooshserver.com:9162/live');

INSERT INTO stations(url)
VALUES('http://i30.letio.com/9166.aac');

INSERT INTO stations(url)
VALUES('http://wms.streampartner.nl:8000/grootnieuwsradio');

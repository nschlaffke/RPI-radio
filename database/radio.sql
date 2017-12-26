DROP TABLE IF EXISTS stations;

CREATE TABLE stations
(
    station_id INTEGER PRIMARY KEY AUTOINCREMENT,
    url TEXT NOT NULL
);
INSERT INTO stations(url) 
VALUES('http://20133.live.streamtheworld.com/SRGSTR02AAC.aac');

INSERT INTO stations(url) 
VALUES('http://host4.whooshserver.com:9162/live');

INSERT INTO stations(url)
VALUES('http://i30.letio.com/9166.aac');

#Database Creation

CREATE DATABASE `BetterMeMain`;

#Using the Database
USE BetterMeMain;


#Table Creation
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type` varchar(20) DEFAULT NULL,
  `user_fname` varchar(50) DEFAULT NULL,
  `user_lname` varchar(50) DEFAULT NULL,
  `user_email` varchar(50) DEFAULT NULL,
  `user_birthdate` date DEFAULT NULL,
  `user_age` int(11) DEFAULT NULL,
  `user_sex` varchar(1) DEFAULT NULL,
  `user_height` int(11) DEFAULT NULL,
  `user_weight` int(11) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user` (`user_email`,`password`)
);

CREATE TABLE `exercise` (
  `exercise_type` varchar(30) NOT NULL DEFAULT '',
  `avg_calories` int(11) DEFAULT NULL,
  PRIMARY KEY (`exercise_type`)
);

CREATE TABLE `food` (
  `category` varchar(20) NOT NULL DEFAULT '',
  `protein` int(11) DEFAULT NULL,
  `fat` int(11) DEFAULT NULL,
  `calories` int(11) DEFAULT NULL,
  PRIMARY KEY (`category`)
);

CREATE TABLE `exercise_event` (
  `exercise_event_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `exercise_type` varchar(30) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `intensity` varchar(10) DEFAULT NULL,
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `stop_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`exercise_event_id`),
  KEY `exercise_event_fk1` (`user_id`),
  KEY `exercise_event_fk2` (`exercise_type`),
  KEY `exercise` (`exercise_event_id`,`user_id`,`exercise_type`,`duration`,`intensity`),
  CONSTRAINT `exercise_event_fk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `exercise_event_fk2` FOREIGN KEY (`exercise_type`) REFERENCES `exercise` (`exercise_type`)
);

CREATE TABLE `food_event` (
  `food_event_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `quality` int(11) DEFAULT NULL,
  `food_source` varchar(20) DEFAULT NULL,
  `size` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`food_event_id`),
  KEY `food_event_fk1` (`category`),
  KEY `food_event_fk2` (`user_id`),
  KEY `food` (`food_event_id`,`user_id`,`time_stamp`,`quality`),
  CONSTRAINT `food_event_fk1` FOREIGN KEY (`category`) REFERENCES `food` (`category`),
  CONSTRAINT `food_event_fk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE `sleep_event` (
  `sleep_event_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `stop_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `duration` varchar(10) DEFAULT NULL,
  `quality` int(11) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`sleep_event_id`),
  KEY `sleep_event_fk` (`user_id`),
  KEY `sleep` (`sleep_event_id`,`user_id`,`duration`,`quality`,`location`),
  CONSTRAINT `sleep_event_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE `mental_mood` (
  `mental_mood_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `mood_condition` int(11) DEFAULT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`mental_mood_id`),
  KEY `mental_mood_fk` (`user_id`),
  CONSTRAINT `mental_mood_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE `physical_mood` (
  `physical_mood_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `mood_condition` int(11) DEFAULT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`physical_mood_id`),
  KEY `physical_mood_fk` (`user_id`),
  CONSTRAINT `physical_mood_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

CREATE TABLE `history` (
  `user_id` int(11) NOT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `avg_calories_gained` int(11) DEFAULT NULL,
  `avg_calories_burnt` int(11) DEFAULT NULL,
  `overall_mood_of_day` int(11) DEFAULT NULL,
  `overall_health_condition` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`time_stamp`),
  CONSTRAINT `history_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

#Trigger Creation

DELIMITER \\

CREATE TRIGGER insert_age 
BEFORE INSERT
ON BetterMeMain.user FOR EACH ROW
BEGIN
SET NEW.user_age = (timestampdiff(YEAR, NEW.user_birthdate, curdate()));
END \\

CREATE TRIGGER update_age
BEFORE UPDATE
ON BetterMeMain.user FOR EACH ROW
BEGIN
IF NEW.user_birthdate <> OLD.user_birthdate 
THEN
SET NEW.user_age = (timestampdiff(YEAR, NEW.user_birthdate, curdate()));
END IF;
END \\

CREATE TRIGGER insert_sleep_duration 
BEFORE INSERT 
ON BetterMeMain.sleep_event
FOR EACH ROW
BEGIN
IF (timestampdiff(MINUTE, NEW.start_time, NEW.stop_time) >= 60) THEN
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time)%60);
ELSE
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time));
END IF;
END \\

CREATE TRIGGER update_sleep_duration
BEFORE UPDATE
ON BetterMeMain.sleep_event 
FOR EACH ROW
BEGIN
IF (OLD.start_time <> NEW.start_time || OLD.stop_time <> NEW.stop_time)
THEN
IF (timestampdiff(MINUTE, NEW.start_time, NEW.stop_time) >= 60) THEN
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time)%60);
ELSE
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time));
END IF;
END IF;
END \\

CREATE TRIGGER insert_exercise_duration
BEFORE INSERT
ON BetterMeMain.exercise_event
FOR EACH ROW
BEGIN
IF (timestampdiff(MINUTE, NEW.start_time, NEW.stop_time) >= 60) THEN
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time)%60);
ELSE
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time));
END IF;
END \\
 
CREATE TRIGGER update_exercise_duration
BEFORE UPDATE
ON BetterMeMain.exercise_event 
FOR EACH ROW
BEGIN
IF (OLD.start_time <> NEW.start_time || OLD.stop_time <> NEW.stop_time)
THEN
IF (timestampdiff(MINUTE, NEW.start_time, NEW.stop_time) >= 60) THEN
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time)%60);
ELSE
SET NEW.duration = concat(timestampdiff(HOUR, NEW.start_time, NEW.stop_time),".",timestampdiff(MINUTE, NEW.start_time, NEW.stop_time));
END IF;
END IF;
END \\

#View Creation

CREATE VIEW `graph_view` AS 
(select cast(`p`.`time_stamp` as date) AS `p_stamp`,avg(`p`.`mood_condition`) AS `p_condition`,cast(`m`.`time_stamp` as date) AS `m_stamp`,
avg(`m`.`mood_condition`) AS `m_condition`,cast(`s`.`start_time` as date) AS `s_stamp`,avg(`s`.`duration`) AS `s_duration` 
from ((`physical_mood` `p` join `mental_mood` `m`) join `sleep_event` `s`) where ((`p`.`user_id` = `m`.`user_id`) and (`m`.`user_id` = `s`.`user_id`) 
and (cast(`p`.`time_stamp` as date) = cast(`m`.`time_stamp` as date)) and (cast(`m`.`time_stamp` as date) = cast(`s`.`start_time` as date)) 
and (`p`.`user_id` = 5) and (`p`.`time_stamp` between '2015-11-01 00:00:00' and '2015-11-30 00:00:00')) group by cast(`p`.`time_stamp` as date) 
order by cast(`p`.`time_stamp` as date));
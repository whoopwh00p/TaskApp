CREATE DATABASE taskDB;
DROP DATABASE wordpress;
USE taskDB;

CREATE TABLE IF NOT EXISTS task (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `completed` TINYINT NULL,
  PRIMARY KEY (`id`));

INSERT INTO `task` (`name`, `completed`) VALUES ('First Task', '0');
INSERT INTO `task` (`name`, `completed`) VALUES ('Second Task', '1');

DROP DATABASE  if exists batch_reader_db;
CREATE DATABASE batch_reader_db;

USE batch_reader_db;

DROP TABLE  if exists customer;
CREATE TABLE `customer` (
                            `id` mediumint(8) unsigned NOT NULL auto_increment,
                            `firstName` varchar(255) default NULL,
                            `lastName` varchar(255) default NULL,
                            `birthdate` varchar(255),
                            PRIMARY KEY (`id`)
) AUTO_INCREMENT=1;


SELECT  * FROM batch_reader_db.customer;
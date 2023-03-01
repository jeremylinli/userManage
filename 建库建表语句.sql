create database dbuser1 character set utf-8;

use dbuser;

create table user (
      id varchar(20) primary key,
      user_name varchar(20),
      age tinyint,
      sex enum('男','女'),
      phone_num varchar(11),
      pre_address varchar(20),
      suf_address varchar(30),
      full_address varchar(20),
      create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
      update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      is_deleted tinyint(3) NOT NULL DEFAULT '0'
);
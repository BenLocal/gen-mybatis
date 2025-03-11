create database if not exists test;

use test;

create table if not exists user (
    id bigint primary key,
    name varchar(255) not null,
    age int not null
);
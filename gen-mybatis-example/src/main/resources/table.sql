create database if not exists test;

use test;

create table if not exists user (
    id bigint primary key comment '主键',
    name varchar(255) not null comment '名称',
    age int not null comment '年龄
    123'
);
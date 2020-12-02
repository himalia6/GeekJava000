create schema if not exists `book` character set utf8mb4;

use book;
create table book (
    id int auto_increment primary key comment '',
    isbn varchar(64) not null comment 'ISBN NO.',
    title varchar(64) not null comment '标题',
    author varchar(64) not null comment '作者',
    price decimal(10,2) not null comment '价格',
    published_at timestamp not null default '2020-01-01 00:00:00',
    created_at timestamp not null default '2020-01-01 00:00:00',
    updated_at timestamp not null default '2020-01-01 00:00:00'
) engine=innoDB character set utf8mb4 comment 'book table';
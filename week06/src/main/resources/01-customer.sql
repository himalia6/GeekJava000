create table customer
(
	id int auto_increment comment '实例id'
		primary key,
	uuid varchar(64) not null comment '客户uuid',
	username varchar(64) not null comment '客户登录用户名',
	password_hash varchar(255) not null comment '用户登录密码',
	first_name varchar(255) not null comment '客户 名',
	last_name varchar(255) not null comment '客户 姓',
	email varchar(64) not null comment '用户邮箱',
	phone varchar(16) null comment '用户手机号',
	gender int default 0 not null comment '客户性别
0 - 男
1 - 女',
	age int null comment '客户年龄',
	nation varchar(255) default '中国' not null comment '客户所在国家',
	city varchar(255) null comment '城市',
	address1 varchar(255) null comment '地址栏1',
	address2 varchar(255) null comment '地址栏2',
	enabled int default 1 not null comment '客户状态
0 - 未启用
1 - 启用',
	update_at datetime default CURRENT_TIMESTAMP not null comment '更新时间',
	create_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
	constraint customer_email_uindex
		unique (email),
	constraint customer_phone_uindex
		unique (phone),
	constraint customer_username_uindex
		unique (username),
	constraint customer_uuid_uindex
		unique (uuid)
)
comment '客户信息表';
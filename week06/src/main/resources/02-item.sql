create table item
(
	id int auto_increment comment '商品ID'
		primary key,
	uuid varchar(255) not null comment '商品uuid',
	title varchar(64) not null comment '商品名称',
	price decimal not null comment '商品价格',
	qr_code varchar(63) not null comment '商品条形码',
	origin varchar(255) default '中国' not null comment '产地',
	manufacturer varchar(255) not null comment '制造商',
	key_word varchar(255) null comment '关键词',
	description longtext null comment '商品描述',
	create_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
	update_at datetime default CURRENT_TIMESTAMP not null comment '更新时间',
	constraint item_qr_code_uindex
		unique (qr_code),
	constraint item_uuid_uindex
		unique (uuid)
)
comment '商品表';
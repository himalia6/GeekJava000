create table order_item
(
	id int auto_increment comment '订单商品ID'
		primary key,
	uuid varchar(64) not null comment '条目uuid',
	order_uuid varchar(64) not null comment '订单uuid',
	item_uuid varchar(64) not null comment '商品uuid',
	item_title varchar(255) not null comment '商品名称',
	item_price decimal not null comment '商品价格',
	item_count int default 0 not null comment '商品数量',
	discount decimal default 0 not null comment '商品折扣',
	update_at datetime default CURRENT_TIMESTAMP not null comment '更新时间',
	create_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
	constraint order_item_uuid_uindex
		unique (uuid)
)
comment '商品订单表';
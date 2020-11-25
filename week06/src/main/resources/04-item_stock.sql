create table item_stock
(
	id int auto_increment comment '库存ID'
		primary key,
	uuid varchar(64) null comment '库存uuid',
	item_uuid varchar(64) not null comment '商品uuid',
	stock_amount int default 0 not null comment '库存',
	reserved_amount int default 0 not null comment '预留数量',
	warehouse_uuid varchar(64) not null comment '库存仓库uuid',
	warehouse_location varchar(64) null comment '库存位置',
	operator_name varchar(64) not null comment '操作员名字',
	operator_uuid varchar(64) null comment '操作员uuid',
	update_at datetime default CURRENT_TIMESTAMP not null comment '更新时间',
	create_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
	constraint stock_stock_uuid_uindex
		unique (uuid)
)
comment '库存表';

create table customer_order
(
	id int auto_increment comment '实例ID'
		primary key,
	uuid varchar(64) not null comment '订单uuid',
	customer_uuid varchar(64) not null comment '用户uuid',
	item_name varchar(255) not null comment '订单ID',
	items_total decimal default 0 not null comment '商品总额',
	tax_fee decimal default 0 not null comment '税费',
	shipping_fee decimal default 0 null comment '快递费用',
	order_total decimal default 0 not null comment '订单总价',
	order_status int default 0 not null comment '订单状态
0 - 已提交
1 - 待支付
2 - 已支付
3 - 已取消
4 - 已支付
5 - 已完成
6 - 已删除',
	transaction_snapshot varchar(255) null comment '交易快照ID',
	order_comment varchar(255) null comment '订单备注',
	update_at datetime default CURRENT_TIMESTAMP null comment '更新时间',
	create_at datetime default CURRENT_TIMESTAMP null comment '创建时间',
	constraint order_uuid_uindex
		unique (uuid)
)
comment '用户订单表';
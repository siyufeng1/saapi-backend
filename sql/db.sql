-- 接口信息
create table if not exists `interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `userId` bigint not null comment '创建人',
    `method` varchar(256) not null comment '请求类型',
    `url` varchar(512) not null comment '接口地址',
    `status` int default 0 not null comment '接口状态 （0-关闭，1-开启）',
    `descpition` varchar(256) null comment '描述',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '接口信息';
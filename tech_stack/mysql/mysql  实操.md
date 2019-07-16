mysql  实操



查询建表语句

show create table tableName;



查询数据存储路径

show variables like 'datadir'；



创建索引

create  index idx_name on tableName(fieldName1,fieldName2);



查看连接状态

show processlist



慢查询日志

set global slow_query_log=on；

show variables like 'slow_query%';



查询事务隔离级别

select @@transaction_isolation;（mysql 8）

select @@tx_isolation;（mysql 8-）
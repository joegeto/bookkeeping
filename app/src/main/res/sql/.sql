select * from listtable;
select * from cardtable;
delete from cardtable;

select sum(money) from listtable where type = 0;
select sum(money) from listtable where type = 1;
select sum(money) from cardtable;
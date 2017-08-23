DROP TABLE IF EXISTS public.user;

create table public.user
(
id serial, 
username varchar(40),
 name varchar(20), 
 age int, 
 balance decimal(10,2), 
CONSTRAINT pk_sys_resource PRIMARY KEY (id)
 );	
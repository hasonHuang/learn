
create table sys_users (
  id  serial,
  username varchar(100),
  password varchar(100),
  salt varchar(100),
  locked bool default false,
  constraint pk_sys_users primary key(id)
) ;
ALTER TABLE public.sys_users
  ADD CONSTRAINT idx_sys_users_username UNIQUE (username);


create table sys_roles (
  id  serial,
  role varchar(100),
  description varchar(100),
  available bool default false,
  constraint pk_sys_roles primary key(id)
) ;
ALTER TABLE public.sys_roles
  ADD CONSTRAINT idx_sys_roles_role UNIQUE (role);

create table sys_permissions (
  id serial,
  permission varchar(100),
  description varchar(100),
  available bool default false,
  constraint pk_sys_permissions primary key(id)
) ;
ALTER TABLE public.sys_permissions
  ADD CONSTRAINT idx_sys_permissions_permission UNIQUE (permission);


create table sys_users_roles (
  user_id bigint,
  role_id bigint,
  constraint pk_sys_users_roles primary key(user_id, role_id)
) ;

create table sys_roles_permissions (
  role_id bigint,
  permission_id bigint,
  constraint pk_sys_roles_permissions primary key(role_id, permission_id)
) ;
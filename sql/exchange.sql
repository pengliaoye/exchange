drop table if exists tb_users;
drop table if exists tb_authorities;
drop table if exists tb_roles;
drop table if exists tb_users_authorities;
drop table if exists tb_users_roles;
drop table if exists tb_roles_authorities;
drop table if exists tb_setting;
drop table if exists tb_dict;
drop table if exists tb_dict_type;
drop table if exists tb_tree;
drop table if exists tb_sports;
drop view if exists vw_user_authorities;

create table tb_users(
    id integer primary key,
    accountname varchar(50) not null unique,
    password varchar(255) not null,
    expirationdate integer,
    failedlogincount integer,
    lasthostaddress varchar(255),
    lastfailedlogintime timestamp,
    lastlogintime timestamp,
    lastpasswordchangetime timestamp,
    screenname varchar(255),
    enabled varchar(255) not null,
    locked  varchar(255),
    roles varchar(500),
    oldpassword varchar(500)
);

create table tb_authorities (
	id varchar(32) primary key,
	authority varchar(255)
);

create table tb_roles(
  	id varchar(32) primary key,
 	name varchar(255)
);

create table tb_users_authorities(
    id varchar(32) primary key,
    user_id bigint,
    authority_id varchar(32)
);

create table tb_users_roles(
    id varchar(32) primary key,
    user_id integer,
    role_id varchar(32)
);

create table tb_roles_authorities (
    id varchar(32) primary key,
    roles_id varchar(32),
    authority_id varchar(32)
);

create table tb_setting(
	id varchar(32),
	name varchar(255),
	val varchar(255)
);

create table tb_dict(
	id varchar(32),
	code varchar(255),
	name varchar(255),
	type_id varchar(32),
	description varchar(255)
);

create table tb_dict_type(
	id varchar(32),
	code varchar(50),
	name varchar(255),
	description varchar(255)
);

create table tb_tree(
	id varchar(32),
	pid varchar(32),
	name varchar(50),
	level integer,
	leaf boolean
);

create table tb_sports(
	id varchar(32) primary key,
	name varchar(255)
);

create or replace view vw_user_authorities as 
select u.accountname, a.authority
from tb_users u, tb_users_authorities ua, tb_authorities a
where u.id = ua.user_id and ua.authority_id = a.id
union
select u.accountname, a.authority
from tb_users u, tb_users_roles ur, tb_roles_authorities ra, tb_authorities a
where u.id = ur.user_id and ur.id = ra.roles_id and ra.authority_id 
= a.id;
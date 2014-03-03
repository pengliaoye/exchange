create table users(
    id bigint primary key,
    accountname varchar(50) not null unique,
    password varchar(255) not null,
    expirationdate bigint,
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

create table authorities (
	id varchar(255) primary key,
	authority varchar(255)
);

create table roles(
  	id varchar(255) primary key,
 	role_name varchar(255)
);

create table users_authorities(
    id varchar(255) primary key,
    user_id bigint,
    authority_id varchar(255)
);

create table users_roles(
    id varchar(255) primary key,
    user_id bigint,
    role_id varchar(255)
);

create table roles_authorities (
    id varchar(255) primary key,
    roles_id varchar(255),
    authority_id varchar(255)
);

create or replace view userauthorities as 
select u.accountname, a.authority
from users u, users_authorities ua,authorities a
where u.id = ua.user_id and ua.authority_id = a.id
union
select u.accountname, a.authority
from users u, users_roles ur, roles_authorities ra, authorities a
where u.id = ur.user_id and ur.id = ra.roles_id and ra.authority_id 
= a.id;

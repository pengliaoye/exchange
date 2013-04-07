create table users(
	id varchar(250) primary key,
	username varchar(250),
	password varchar(250),
	accountNonExpired boolean,
	accountNonLocked boolean,
	credentialsNonExpired boolean,
	enabled boolean
);

create table authorities (
	id varchar(250) primary key,
	authority varchar(250)
);

create table roles(
  	id varchar(250) primary key,
 	role_name varchar(250)
);

create table users_authorities(
    id varchar(250) primary key,
    user_id varchar(250),
    authority_id varchar(250)
);

create table users_roles(
    id varchar(250) primary key,
    user_id varchar(250),
    role_id varchar(250)
);

create table roles_authorities (
    id varchar(250) primary key,
    roles_id varchar(250),
    authority_id varchar(250)
);

create or replace view userauthorities as 
select u.username, a.authority
from users u, users_authorities ua,authorities a
where u.id = ua.user_id and ua.authority_id = a.id
union
select u.username, a.authority
from users u, users_roles ur, roles_authorities ra, authorities a
where u.id = ur.user_id and ur.id = ra.roles_id and ra.authority_id 
= a.id;

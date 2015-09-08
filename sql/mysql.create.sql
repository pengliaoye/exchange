CREATE USER 'exchange'@'%' IDENTIFIED BY 'Admin123456';
GRANT ALL PRIVILEGES ON *.* TO 'exchange'@'%';

create table tb_users(
    id bigint primary key,
    account_name varchar(50) not null unique,
    password varchar(255) not null,
    expiration_time datetime not null,
    failed_login_count integer,
    last_host_address varchar(255),
    last_failed_login_time datetime,
    last_login_time datetime,
    last_password_change_time datetime,
    screen_name varchar(255),
    enabled varchar(255) not null,
    locked  varchar(255),
    roles varchar(500),
    old_password varchar(500)
);

create table tb_authorities (
	id varchar(36) primary key,
	name varchar(255) not null
);

create table tb_roles(
  	id varchar(36) primary key,
 	name varchar(255) not null
);

create table tb_users_authorities(
    id varchar(36) primary key,
    user_id bigint not null,
    authority_id varchar(36) not null
);

create table tb_users_roles(
    id varchar(36) primary key,
    user_id bigint not null,
    role_id varchar(36) not null
);

create table tb_roles_authorities (
    id varchar(36) primary key,
    roles_id varchar(36) not null,
    authority_id varchar(36) not null
);

create table tb_setting(
	id varchar(36) primary key,
	name varchar(255) not null unique,
	val varchar(255) not null
);

create table tb_dict(
	id varchar(36) primary key,
	code varchar(255) not null,
	name varchar(255) not null,
	type_id varchar(36) not null,
	description varchar(255)
);

create table tb_dict_type(
	id varchar(36) primary key,
	code varchar(50) not null unique,
	name varchar(255) not null,
	description varchar(255)
);

create table tb_tree(
	id varchar(36) primary key,
	pid varchar(36),
	name varchar(50) not null,
	level integer,
	leaf boolean
);

create table tb_sports(
	id varchar(36) primary key,
	name varchar(255) not null unique
);

alter table tb_users_authorities add constraint fk_tb_ua_user_id foreign key (user_id) references tb_users(id);
alter table tb_users_authorities add constraint fk_tb_ua_authority_id foreign key (authority_id) references tb_authorities(id);
alter table tb_users_roles add constraint fk_tb_ur_user_id foreign key (user_id) references tb_users(id);
alter table tb_users_roles add constraint fk_tb_ur_role_id foreign key (role_id) references tb_roles(id);
alter table tb_roles_authorities add constraint fk_tb_ra_roles_id foreign key (roles_id) references tb_roles(id);
alter table tb_roles_authorities add constraint fk_tb_ra_authority_id foreign key (authority_id) references tb_authorities(id);
alter table tb_dict add constraint fk_tb_dict_type_id foreign key (type_id) references tb_dict_type(id);
alter table tb_tree add constraint fk_tb_tree_pid foreign key (pid) references tb_tree(id);

create or replace view vw_user_authorities as 
select u.account_name, a.name
from tb_users u, tb_users_authorities ua, tb_authorities a
where u.id = ua.user_id and ua.authority_id = a.id
union
select u.account_name, a.name
from tb_users u, tb_users_roles ur, tb_roles_authorities ra, tb_authorities a
where u.id = ur.user_id and ur.id = ra.roles_id and ra.authority_id 
= a.id;
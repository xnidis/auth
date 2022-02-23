create table  if not exists users (
	id bigint not null auto_increment,
	username varchar(100),
	password varchar(100),
    primary key (id)
);

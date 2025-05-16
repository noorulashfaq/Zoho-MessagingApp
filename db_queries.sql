create table messageprocess(
	p_id serial primary key,
	thread_id int,
	user_id int references users(user_id) on delete set null,
	message_content text,
	content_length numeric(10,2),
	message_type varchar(10),
	priority varchar(5),
	posted_at timestamp default current_timestamp
);

create table users(
	user_id serial primary key,
	userName varchar(50),
	password_hash text,
	user_created_at timestamp default current_timestamp
);

drop table users;

create table T_two_TB_two(
	message_id serial primary key,
	message_content bytea constraint data_size_check check (octet_length(message_content) <= 10240),
	message_type varchar(10),
	priority varchar(5),
	user_id int references users(user_id) on delete set null,
	posted_at timestamp default current_timestamp
);
create table T_two_TB_two(
	message_id serial primary key,
	message_content bytea[],
	message_type varchar(10),
	priority varchar(5),
	user_id int references users(user_id) on delete set null,
	posted_at timestamp default current_timestamp
);

alter table T_one_TB_three
alter column message_content type TEXT;

alter table messageprocess
drop column p_id;

select * from messageprocess;
select * from users;

drop table T_two_TB_one;

select * from T_two_TB_one;
select * from T_two_TB_two;
select * from T_one_TB_one;
select * from T_one_TB_two;
select * from T_one_TB_three;

select * from TypeOne;
select * from TypeTwo;

truncate table messageprocess;

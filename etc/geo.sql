--
-- Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
--
-- $Id: geo.sql,v 1.5 2001/06/14 07:57:53 dustin Exp $
--

-- The actual users
create table geo_users (
	user_id serial,
	username varchar(20) not null,
	password varchar(64) not null,
	full_name varchar(60) not null,
	email text not null,
	url text,
	active boolean default true,
	zipcode integer,
	longitude float,
	latitude float,
	ts timestamp default now(),
	primary key(user_id)
);
create unique index geo_users_byname on geo_users(username);
grant all on geo_users to nobody;
grant all on geo_users_user_id_seq to nobody;

-- The country list
create table geo_countries (
	id serial,
	abbr char(2),
	name varchar(64),
	primary key(id)
);
grant all on geo_countries to nobody;
grant all on geo_countries_id_seq to nobody;
create unique index geo_countries_byabbr on geo_countries(abbr);

-- Sequence for giving points numbers
create sequence geo_point_namer minvalue 1000;
grant all on geo_point_namer to nobody;

-- Points go here.
create table geo_points (
	point_id serial,
	creator_id integer not null,
	name text not null,
	description text not null,
	longitude float not null,
	latitude float not null,
	waypoint_id varchar(6) not null,
	difficulty float default 1 not null,
	terrain float default 1 not null,
	country integer not null,
	approach varchar,
	retired boolean default false,
	created timestamp default now(),
	primary key(point_id),
	foreign key(creator_id) references geo_users(user_id),
	foreign key(country) references geo_countries(id)
);
create unique index geo_points_bypid on geo_points(waypoint_id);
grant all on geo_points to nobody;
grant all on geo_points_point_id_seq to nobody;

-- How about voting?
create table geo_votes (
	vote_id serial,
	point_id integer not null,
	user_id integer not null,
	vote smallint not null,
	ts timestamp default now(),
	foreign key(point_id) references geo_points(point_id),
	foreign key(user_id) references geo_users(user_id)
);
grant all on geo_votes to nobody;
grant all on geo_votes_vote_id_seq to nobody;
create index geo_votes_bypoint on geo_votes(point_id);
create index geo_votes_byuser on geo_votes(user_id);

-- Log the experience
create table geo_log (
	log_id serial,
	point_id integer not null,
	user_id integer not null,
	found boolean not null,
	info text not null,
	foreign key(point_id) references geo_points(point_id),
	foreign key(user_id) references geo_users(user_id)
);
grant all on geo_log to nobody;
grant all on geo_log_log_id_seq to nobody;
create index geo_log_bypoint on geo_log(point_id);
create index geo_log_byuser on geo_log(user_id);

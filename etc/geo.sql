--
-- Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
--
-- $Id: geo.sql,v 1.10 2002/12/01 09:53:15 dustin Exp $
--

-- The actual users
create table geo.users (
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
create unique index users_byname on geo.users(username);
grant all on geo.users to nobody;
grant all on geo.users_user_id_seq to nobody;

-- The country list
create table geo.countries (
	id serial,
	abbr char(2),
	name varchar(64),
	primary key(id)
);
grant all on geo.countries to nobody;
grant all on geo.countries_id_seq to nobody;
create unique index countries_byabbr on geo.countries(abbr);

-- Sequence for giving points numbers
create sequence geo.point_namer minvalue 1000;
grant all on geo.point_namer to nobody;

-- Points go here.
create table geo.points (
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
	foreign key(creator_id) references geo.users(user_id),
	foreign key(country) references geo.countries(id)
);
create unique index points_bypid on geo.points(waypoint_id);
grant all on geo.points to nobody;
grant all on geo.points_point_id_seq to nobody;

-- How about voting?
create table geo.votes (
	vote_id serial,
	point_id integer not null,
	user_id integer not null,
	vote smallint not null,
	ts timestamp default now(),
	foreign key(point_id) references geo.points(point_id),
	foreign key(user_id) references geo.users(user_id)
);
grant all on geo.votes to nobody;
grant all on geo.votes_vote_id_seq to nobody;
create index votes_bypoint on geo.votes(point_id);
create index votes_byuser on geo.votes(user_id);

-- Log the experience
create table geo.log (
	log_id serial,
	point_id integer not null,
	user_id integer not null,
	found boolean not null,
	info text not null,
	ts timestamp default now(),
	foreign key(point_id) references geo.points(point_id),
	foreign key(user_id) references geo.users(user_id)
);
grant all on geo.log to nobody;
grant all on geo.log_log_id_seq to nobody;
create index log_bypoint on geo.log(point_id);
create index log_byuser on geo.log(user_id);

-- Geo points by attribute
create table geo.polys (
	id serial,
	source varchar(64) not null,
	name text not null,
	boundaryx1 float,
	boundaryy1 float,
	boundaryx2 float,
	boundaryy2 float,
	bbox box,
	primary key(id)
);
create index polys_box on geo.polys using rtree(bbox);
grant all on geo.polys to nobody;
grant all on geo.polys_id_seq to nobody;

create table geo.poly_data (
	seq serial,
	poly_id integer not null,
	latitude float not null,
	longitude float not null,
	foreign key(poly_id) references geo.polys(id)
);
create index poly_data_bypoly on geo.poly_data(poly_id);
grant all on geo.poly_data to nobody;
grant all on geo.poly_data_seq_seq to nobody;

-- I need a function to get a zero-area box from a point.
create function geo.box(point) returns box as
	'select box($1, $1)'
	language 'sql'
	with (iscachable);

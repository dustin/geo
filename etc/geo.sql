--
-- Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
--
-- $Id: geo.sql,v 1.1 2001/06/12 09:18:59 dustin Exp $
--

-- The actual users
create table geo_users (
	user_id serial,
	username varchar(20) not null,
	password varchar(64) not null,
	full_name varchar(60) not null,
	email text not null,
	url text,
	zipcode integer,
	longitude float,
	latitude float,
	ts timestamp default now(),
	primary key(user_id)
);
create unique index geo_users_byname on geo_users(username);
grant all on geo_users to nobody;
grant all on geo_users_user_id_seq to nobody;

-- Points go here.
create table geo_points (
	point_id serial,
	creator_id integer not null,
	name text not null,
	longitude float not null,
	latitude float not null,
	waypoint_id varchar(6) not null,
	difficulty float default 1 not null,
	terrain float default 1 not null,
	created timestamp default now(),
	primary key(point_id),
	foreign key(creator_id) references geo_users(user_id)
);
create unique index geo_points_bypid on geo_points(waypoint_id);
grant all on geo_points to nobody;
grant all on geo_points_point_id_seq to nobody;

USE wussup;

DROP TABLE IF EXISTS wussup.def;
DROP TABLE IF EXISTS wussup.user;
DROP TABLE IF EXISTS wussup.activity;

CREATE TABLE wussup.def (
	username text,
	word text,
	def text,
	wordkey text,
	example text,
	curr_review_interval text,
	next_review_time text,
	add_date text,
	PRIMARY KEY(username, word, def)
);

CREATE TABLE wussup.user (
	username text,
	password text,
	secure_token text,
	friends set<text>,
	PRIMARY KEY(username)
);

CREATE TABLE wussup.activity (
	username text,
	type text,
	targeted_user text,
	datetime timestamp,
	content text,	
	PRIMARY KEY(username, type, targeted_user, datetime)
);

--create index index_targeted_user on wussup.activity (targeted_user);


-- DUMMY DATA
insert into user(username, password, secure_token, friends) values('john', 'SKNu9YcxMTyR4e8HMaWy9OfCGiQc4lQl', 'qe1k6dv02u9qnnrdnppg0gpo4q', {'jane'});
insert into user(username, password, secure_token, friends) values('jane', 'XDem9iL3GbHJ17wVzo3MFglljT0yFkBY', 'e0itlhre9iq5bjd7d8dspsk53t', {'john'});

insert into def(username, word, def) values('sys_user', 'Bundle Up', 'Dress warmly');
insert into def(username, word, def) values('sys_user', 'Bite The Bullet', 'Take the pain and be stronger');

insert into activity(username, datetime, type, content, targeted_user) values('john', '2015-05-07 17:53:19-0700', 'add_def', '{"added_word":"Bite The Bullet"}', '');
insert into activity(username, datetime, type, content, targeted_user) values('john', '2015-05-07 14:31:09-0700', 'add_def', '{"added_word":"Bundle Up"}', '');
insert into activity(username, datetime, type, content, targeted_user) values('john', '2015-04-30 01:09:59-0700', 'recommend_def', '{"recommended_def":"Incapable"}', 'jane');




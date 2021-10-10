drop table IF EXISTS comment;
drop table IF EXISTS post_tag;
drop table IF EXISTS tag;
drop table IF EXISTS post;
drop table IF EXISTS user_role;
drop table IF EXISTS role;
drop table IF EXISTS "user";

create TABLE "user" (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    dt_created TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT false
);


create TABLE role(
    role_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

create TABLE user_role (
    user_id BIGINT REFERENCES "user" (user_id) ON delete CASCADE,
    role_id INT REFERENCES role(role_id) ON delete CASCADE,
    PRIMARY KEY (user_id,role_id)
);

create TABLE post (
    post_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content VARCHAR,
    user_id BIGINT REFERENCES "user"(user_id),
    dt_created TIMESTAMP DEFAULT now() NOT NULL,
    dt_updated TIMESTAMP
);

create TABLE tag (
    tag_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

create TABLE post_tag (
    post_id BIGINT REFERENCES post(post_id) ON delete CASCADE,
    tag_id BIGINT REFERENCES tag(tag_id) ON delete CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

create TABLE comment (
    comment_id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES post(post_id) ON delete CASCADE,
    content VARCHAR,
    user_id BIGINT REFERENCES "user"(user_id),
    dt_created TIMESTAMP DEFAULT now() NOT NULL
);

-- Insert

insert into role values (1, 'ADMIN');
insert into role values (2, 'USER');

insert into "user" (username, password, dt_created, is_active)
    values ('admin', '$2a$10$DDtS.cXOYKF2s6OPLkKWtu0VBqn6VEFZwWHRraoteFo6iIM1rIrRu', now()::timestamp, true);

insert into "user" (username, password, dt_created, is_active)
    values ('user1', '$2a$10$0uXx4iP0xJvZuP0JZshXS.zKoTdFWLew4GQ64JSIImaa5GciZ0YZK', now()::timestamp, true);

insert into "user" (username, password, dt_created, is_active)
    values ('user2', '$2a$10$bWhsZW5h2ku1I0esTrhXjeN8No6T8ynr9zQqLU96g1s64DqQnRmEq', now()::timestamp, true);

insert into user_role(user_id, role_id) values (1, 1);
--insert into user_role(user_id, role_id) values (1, 2);
insert into user_role(user_id, role_id) values (2, 2);

insert into post (user_id, title, content, dt_created, dt_updated)
	values (2, 'Day 1', 'It''s all good!', '2021-04-03 14:16:00'::timestamp, null);
insert into post (user_id, title, content, dt_created, dt_updated)
	values (2, 'Day 2', 'It''s all ok!', '2021-04-03 14:16:05'::timestamp, null);
insert into post (user_id, title, content, dt_created, dt_updated)
	values (3, 'Day 3', 'It''s all bad!', '2021-04-03 14:16:10'::timestamp, null);

insert into comment (user_id, post_id, content, dt_created)
    values (1, 2, 'Nice!', current_timestamp);
insert into comment (user_id, post_id, content, dt_created)
    values (2, 1, 'Awesome!', current_timestamp);
insert into comment (user_id, post_id, content, dt_created)
    values (1, 1, 'Excellent!', current_timestamp);
insert into comment (user_id, post_id, content, dt_created)
    values (2, 1, 'Wonderful!', current_timestamp);
insert into comment (user_id, post_id, content, dt_created)
    values (1, 3, 'Disgusting!', current_timestamp);
insert into comment (user_id, post_id, content, dt_created)
    values (2, 3, 'Atrocious!', current_timestamp);

insert into tag (name) values ('news');
insert into tag (name) values ('life');
insert into tag (name) values ('day');
insert into tag (name) values ('mood');
insert into tag (name) values ('ideas');
insert into tag (name) values ('thoughts');

insert into post_tag(post_id, tag_id) values (1, 1);
insert into post_tag(post_id, tag_id) values (1, 2);
insert into post_tag(post_id, tag_id) values (2, 3);
insert into post_tag(post_id, tag_id) values (2, 2);
insert into post_tag(post_id, tag_id) values (2, 1);
insert into post_tag(post_id, tag_id) values (2,5);
insert into post_tag(post_id, tag_id) values (3, 3);
insert into post_tag(post_id, tag_id) values (3, 2);
insert into post_tag(post_id, tag_id) values (3, 6);


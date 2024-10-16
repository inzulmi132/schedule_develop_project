create table users
(
    id          bigint auto_increment primary key,
    created_at  datetime(6),
    modified_at datetime(6),
    email       varchar(255) not null unique,
    password    varchar(255) not null,
    role        enum ('ADMIN', 'USER') not null,
    username    varchar(255) not null
);
create table schedules
(
    id          bigint auto_increment primary key,
    created_at  datetime(6)  null,
    modified_at datetime(6)  null,
    title       varchar(255) not null,
    todo        varchar(255) not null,
    weather     varchar(255) not null,
    user_id     bigint       null,
    constraint schedule_fk_user_id foreign key (user_id) references users (id)
);
create table comments
(
    id          bigint auto_increment primary key,
    created_at  datetime(6) null,
    modified_at datetime(6) null,
    text        varchar(255) not null,
    user_id     bigint null,
    schedule_id bigint null,
    constraint comment_fk_schedule_id foreign key (schedule_id) references schedules (id),
    constraint comment_fk_user_id foreign key (user_id) references users (id)
);
create table user_schedule
(
    user_id     bigint not null,
    schedule_id bigint not null,
    constraint fk_user_id foreign key (user_id) references users (id),
    constraint fk_schedule_id foreign key (schedule_id) references schedules (id)
);

create sequence hibernate_sequence start with 1 increment by 1;
create table friendship
(
    id                   bigint       not null,
    friend_id            varchar(255),
    number_of_friendship integer      not null check (number_of_friendship >= 0),
    status               integer      not null check (status <= 1),
    user1_user_id        varchar(255) not null,
    user2_user_id        varchar(255) not null,
    primary key (id)
);
create table user_data
(
    user_id    varchar(255) not null,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    primary key (user_id)
);
alter table friendship
    add constraint FKwyx6529y34t63tp7j4d3622u foreign key (user1_user_id) references user_data;
alter table friendship
    add constraint FKgy8ec9lrxtqbk2i8mqy4k5svn foreign key (user2_user_id) references user_data;
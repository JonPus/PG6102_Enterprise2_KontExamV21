create sequence hibernate_sequence start with 1 increment by 1;

create table message
(
    message_id varchar(255) not null,
    friend_id  varchar(255),
    message    varchar(255),
    user_id    varchar(255),
    primary key (message_id)
);
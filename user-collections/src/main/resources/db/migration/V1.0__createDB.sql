create sequence hibernate_sequence start with 1 increment by 1;

create table booking
(
    id                 bigint       not null,
    trip_id            varchar(255),
    person             integer      not null,
    number_of_bookings integer      not null,
    user_user_id       varchar(255) not null,
    active             integer      not null,
    primary key (id)
);

create table user_data
(
    user_id varchar(255) not null,
    coins   integer      not null check ( coins >= 0 ),
    person  integer      not null,
    primary key (user_id)
);

alter table booking
    add constraint FKtco9dei78cocpwi1sxye9mw3b foreign key (user_user_id) references user_data;
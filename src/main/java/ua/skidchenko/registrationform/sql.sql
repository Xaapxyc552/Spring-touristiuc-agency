create table if not exists check_status
(
    id     bigint not null
        constraint check_status_pkey
            primary key,
    status varchar(255)
);

alter table check_status
    owner to postgres;

create table if not exists tour
(
    id                bigserial    not null
        constraint tour_pkey
            primary key,
    amount_of_persons integer      not null,
    burning           boolean      not null,
    description       varchar(300) not null,
    hotel_type        integer,
    name              varchar(50)  not null
        constraint uk_8eyvxui40sdanl9s301knuxnw
            unique,
    price             bigint       not null,
    tour_status       varchar(255)
);

alter table tour
    owner to postgres;

create table if not exists tour_type
(
    id   bigserial not null
        constraint tour_type_pkey
            primary key,
    type varchar(255)
);

alter table tour_type
    owner to postgres;

create table if not exists tour__tour_type
(
    tour_id      bigint not null
        constraint fk4j9hey1pxg65kw9ixmuoqeg6v
            references tour_type,
    tour_type_id bigint not null
        constraint fkhulug5wfd0hb8q25bwr2f6j3n
            references tour
);

alter table tour__tour_type
    owner to postgres;

create table if not exists "user"
(
    id                      bigserial   not null
        constraint user_pkey
            primary key,
    account_non_expired     boolean     not null,
    account_non_locked      boolean     not null,
    credentials_non_expired boolean     not null,
    email                   varchar(60)
        constraint uk_ob8kqyqqgmefl0aco34akdtpe
            unique,
    enabled                 boolean     not null,
    firstname               varchar(20),
    money                   bigint      not null,
    password                varchar(60) not null,
    role                    varchar(255),
    username                varchar(30) not null
        constraint uk_sb8bbouer5wak8vyiiy4pf2bx
            unique
);

alter table "user"
    owner to postgres;

create table if not exists "check"
(
    id          bigserial not null
        constraint check_pkey
            primary key,
    total_price bigint,
    status_id   bigint
        constraint fk76o0rix6q0gh9mnuu91l06h71
            references check_status,
    tour_id     bigint
        constraint fknwuv8ebu1pwawi90jjjbr0pl7
            references tour,
    user_id     bigint
        constraint fk5c15innr139awr8doynjxn5m4
            references "user"
);

alter table "check"
    owner to postgres;

insert into tour_type (id, type)
values (1, 'RECREATION');
insert into tour_type (id, type)
values (2, 'EXCURSION');
insert into tour_type (id, type)
values (3, 'SHOPPING');

insert into check_status (id, status)
values (1, 'WAITING_FOR_CONFIRM');
insert into check_status (id, status)
values (2, 'CONFIRMED');
insert into check_status (id, status)
values (3, 'DECLINED');
insert into check_status (id, status)
values (4, 'CANCELED');



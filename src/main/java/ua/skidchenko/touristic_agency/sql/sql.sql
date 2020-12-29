create table if not exists touristic_agency.check_status
(
    id bigint not null
        constraint check_status_pkey
            primary key,
    status varchar(255)
);

alter table touristic_agency.check_status owner to postgres;

create table if not exists touristic_agency.tour
(
    id bigserial not null
        constraint tour_pkey
            primary key,
    amount_of_persons integer not null,
    burning boolean not null,
    hotel_type integer,
    price bigint not null,
    tour_status varchar(255)
);

alter table touristic_agency.tour owner to postgres;

create table if not exists touristic_agency.description_translation_mapping
(
    tour_id bigint not null
        constraint fkm3wf60r133bq38cwdy8b4pux4
            references touristic_agency.tour,
    description varchar(255),
    lang_code varchar(255) not null,
    constraint description_translation_mapping_pkey
        primary key (tour_id, lang_code)
);

alter table touristic_agency.description_translation_mapping owner to postgres;

create table if not exists touristic_agency.name_translation_mapping
(
    name_id bigint not null
        constraint fk76ykh6oqvub3m0yn3ubi00uu
            references touristic_agency.tour,
    name varchar(255),
    lang_code varchar(255) not null,
    constraint name_translation_mapping_pkey
        primary key (name_id, lang_code)
);

alter table touristic_agency.name_translation_mapping owner to postgres;

create table if not exists touristic_agency.tour_type
(
    id bigserial not null
        constraint tour_type_pkey
            primary key,
    type varchar(255)
);

alter table touristic_agency.tour_type owner to postgres;

create table if not exists touristic_agency.tour__tour_type
(
    tour_type_id bigint not null
        constraint fk8elo0bpdvp4yqki6hq12gdjht
            references touristic_agency.tour_type,
    tour_id bigint not null
        constraint fkqdeqkpmxw31l0dmd3lw2kvaqf
            references touristic_agency.tour
);

alter table touristic_agency.tour__tour_type owner to postgres;

create table if not exists touristic_agency."user"
(
    id bigserial not null
        constraint user_pkey
            primary key,
    account_non_expired boolean not null default true,
    account_non_locked boolean not null default true,
    credentials_non_expired boolean not null default true,
    email varchar(60)
        constraint uk_ob8kqyqqgmefl0aco34akdtpe
            unique,
    enabled boolean not null,
    firstname varchar(20),
    money bigint not null
        constraint positive_balance
            check (money >= 0),
    password varchar(60) not null,
    role varchar(255),
    username varchar(30) not null
        constraint uk_sb8bbouer5wak8vyiiy4pf2bx
            unique
);

alter table touristic_agency."user" owner to postgres;

create table if not exists touristic_agency."check"
(
    id bigserial not null
        constraint check_pkey
            primary key,
    total_price bigint,
    status_id bigint
        constraint fk76o0rix6q0gh9mnuu91l06h71
            references touristic_agency.check_status,
    tour_id bigint
        constraint fknwuv8ebu1pwawi90jjjbr0pl7
            references touristic_agency.tour,
    user_id bigint
        constraint fk5c15innr139awr8doynjxn5m4
            references touristic_agency."user"
);

alter table touristic_agency."check" owner to postgres;



insert into touristic_agency.user (account_non_expired, account_non_locked, credentials_non_expired, email, enabled, firstname, money, password, role, username) values ('true', 'true', 'true', 'admin@mail.com', 'true', 'AdminFirstname', 0, '$2a$10$0JTDLthgLPASXLZC5npvUORbxFs3jNwOM9W57WXP/WyTjaWqaAexK', 'ROLE_ADMIN', 'admin');
insert into touristic_agency.user (account_non_expired, account_non_locked, credentials_non_expired, email, enabled, firstname, money, password, role, username) values ('true', 'true', 'true', 'manager@mail.com', 'true', 'ManagerFirstname', 0, '$2a$10$YPO5pUdseVnphwtudAhOCeX60ti9KTssGct5qEMyMXC5iSs7FuHdO', 'ROLE_MANAGER', 'manager');
insert into touristic_agency.user (account_non_expired, account_non_locked, credentials_non_expired, email, enabled, firstname, money, password, role, username) values ('true', 'true', 'true', 'user@mail.com', 'true', 'UserFirstname', 200000, '$2a$10$DdW.0xtqzqgZNX3qZ9rFS.kpcSL5eMrj1S0l8qvod7PSYkGtIiWTa', 'ROLE_USER', 'user');


insert into touristic_agency.tour_type (id, type)
values (1, 'RECREATION');
insert into touristic_agency.tour_type (id, type)
values (2, 'EXCURSION');
insert into touristic_agency.tour_type (id, type)
values (3, 'SHOPPING');

insert into touristic_agency.check_status (id, status)
values (1, 'WAITING_FOR_CONFIRM');
insert into touristic_agency.check_status (id, status)
values (2, 'CONFIRMED');
insert into touristic_agency.check_status (id, status)
values (3, 'DECLINED');
insert into touristic_agency.check_status (id, status)
values (4, 'CANCELED');



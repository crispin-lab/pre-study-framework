drop table if exists users cascade;
drop table if exists articles cascade;

create table users
(
  created_at timestamp(6) with time zone not null,
  created_by bigint                      not null,
  id         bigint                      not null,
  updated_at timestamp(6) with time zone not null,
  updated_by bigint                      not null,
  password   varchar(64)                 not null,
  username   varchar(20)                 not null,
  primary key (id)
);

create table articles
(
  is_deleted boolean                     not null,
  author     bigint                      not null,
  created_at timestamp(6) with time zone not null,
  created_by bigint                      not null,
  deleted_at timestamp(6) with time zone,
  id         bigint                      not null,
  updated_at timestamp(6) with time zone not null,
  updated_by bigint                      not null,
  title      varchar(200)                not null,
  password   varchar(255)                not null,
  content    clob                        not null,
  primary key (id)
);

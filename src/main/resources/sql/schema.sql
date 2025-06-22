drop table if exists users cascade;

create table users
(
  created_at timestamp(6) with time zone not null,
  created_by bigint                      not null,
  id         bigint                      not null,
  updated_at timestamp(6) with time zone not null,
  updated_by bigint                      not null,
  password   varchar(20)                 not null,
  username   varchar(20)                 not null,
  primary key (id)
)

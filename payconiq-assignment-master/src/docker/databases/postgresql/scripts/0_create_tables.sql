create sequence stock_seq start 100 increment 1;
create table stock
(
    id          int8 not null,
    current_price DOUBLE PRECISION,
    last_update   TIMESTAMP,
    user_name     varchar(255),
    name        varchar(255) UNIQUE,
    primary key (id)
);
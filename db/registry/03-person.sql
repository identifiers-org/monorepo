create table public.person
(
    id        bigint       not null primary key,
    created   timestamp    not null,
    email     varchar(255) not null,
    full_name varchar(255) not null,
    modified  timestamp
);

alter table public.person
    owner to devusername
    ;

create index idx_fullname
    on public.person (full_name);

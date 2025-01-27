create table active_mir_id
(
    mir_id         bigint    not null primary key,
    created        timestamp not null,
    last_confirmed timestamp not null
);

alter table active_mir_id
    owner to devusername;

INSERT INTO public.active_mir_id (mir_id, created, last_confirmed) VALUES (5000, '2019-05-14 08:13:04.943000', '2019-05-14 08:13:04.693000');
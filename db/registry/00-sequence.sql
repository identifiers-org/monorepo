create sequence public.institution_seq
    start with 5000
    increment by 50;

alter sequence public.institution_seq owner to devusername;

create sequence public.namespace_seq
    start with 5000
    increment by 50;

alter sequence public.namespace_seq owner to devusername;

create sequence public.namespace_synonym_seq
    start with 5000
    increment by 50;

alter sequence public.namespace_synonym_seq owner to devusername;

create sequence public.person_seq
    start with 5000
    increment by 50;

alter sequence public.person_seq owner to devusername;

create sequence public.prefix_registration_request_seq
    start with 5000
    increment by 50;

alter sequence public.prefix_registration_request_seq owner to devusername;

create sequence public.prefix_registration_session_event_seq
    start with 5000
    increment by 50;

alter sequence public.prefix_registration_session_event_seq owner to devusername;

create sequence public.prefix_registration_session_seq
    start with 5000
    increment by 50;

alter sequence public.prefix_registration_session_seq owner to devusername;

create sequence public.resource_registration_request_seq
    start with 5000
    increment by 50;

alter sequence public.resource_registration_request_seq owner to devusername;

create sequence public.resource_registration_session_event_seq
    start with 5000
    increment by 50;

alter sequence public.resource_registration_session_event_seq owner to devusername;

create sequence public.resource_registration_session_seq
    start with 5000
    increment by 50;

alter sequence public.resource_registration_session_seq owner to devusername;

create sequence public.resource_seq
    start with 5000
    increment by 50;

alter sequence public.resource_seq owner to devusername;


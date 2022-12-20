create table parent_topic
(
    uuid         uuid                 not null
        constraint parent_topic_uuid_idempotent
            unique,
    display_name text                 not null
        constraint parent_topic_name_idempotent
            unique,
    active       boolean default true not null
);

create index idx_parent_topic_uuid
    on parent_topic (uuid);

--TODO: is this required?
create index idx_parent_topic_all
    on parent_topic (display_name, uuid);

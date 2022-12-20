create table topic
(
    display_name      text                 not null,
    uuid              uuid                 not null
        constraint topic_uuid_idempotent
            unique,
    parent_topic_uuid uuid                 not null,
    active            boolean default true not null,
    constraint topic_name_idempotent
        unique (display_name, parent_topic_uuid)
);

create index idx_topic_uuid
    on topic (uuid);

create index idx_topic_all
    on topic (display_name, uuid, parent_topic_uuid);

create table topic_link
(
    id                    bigserial
        constraint topic_link_pkey
            primary key,
    case_type             text        not null,
    stage_type            text        not null,
    link_value            uuid        not null,
    responsible_team_uuid uuid        not null,
    constraint topic_link_link_uuid_case_type_stage_type_link_type_key
        unique (link_value, case_type, stage_type)
);

create index idx_topic_link_case_stage_link
    on topic_link (case_type, stage_type, link_value);

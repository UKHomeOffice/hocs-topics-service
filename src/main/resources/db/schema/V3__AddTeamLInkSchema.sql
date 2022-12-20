create table team_link
(
    id                    bigserial
        constraint team_link_pkey
            primary key,
    case_type             text        not null,
    stage_type            text        not null,
    link_value            varchar(80) not null,
    link_type             text        not null,
    responsible_team_uuid uuid        not null,
    constraint team_link_link_uuid_case_type_stage_type_link_type_key
        unique (link_value, case_type, stage_type, link_type)
);

create index idx_team_link_case_stage_link
    on team_link (case_type, stage_type, link_value);

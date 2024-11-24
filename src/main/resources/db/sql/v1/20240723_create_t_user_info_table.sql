--liquibase formatted sql

--changeset dtrashchenko:20240723_create_t_user_info
create table if not exists t_user_info (
                    id            uuid                      primary key,
                    tg_chat_id    bigint                    unique              not null,
                    user_nick     varchar                                       not null,
                    user_name     varchar                                       not null,
                    created_date  timestamp with time zone                      not null
                  );
--rollback drop table t_user_info;

--changeset dtrashchenko:20230326_create_t_user_info_table_comments runOnChange:true
comment on TABLE  t_user_info               is  'Таблица с информацией о пользователях';
comment on COLUMN t_user_info.id            is  'ID записи';
comment on COLUMN t_user_info.tg_chat_id    is  'Chat_id пользователя';
comment on COLUMN t_user_info.user_nick     is  'Ник пользователя';
comment on COLUMN t_user_info.user_name     is  'Имя пользователя';
comment on COLUMN t_user_info.created_date  is  'Дата и время создания пользователя';
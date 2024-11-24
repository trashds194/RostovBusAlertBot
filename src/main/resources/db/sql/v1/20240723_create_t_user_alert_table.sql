--liquibase formatted sql

--changeset dtrashchenko:20240723_create_t_user_alert_table
create table if not exists T_USER_ALERT (
                    id            uuid                      not null,
                    user_id       uuid                      not null,
                    latitude      DOUBLE PRECISION          not null,
                    longitude     DOUBLE PRECISION          not null,
                    days          integer[]                 not null,
                    buses         varchar[]                 not null,
                    start_date    time with time zone       not null,
                    end_date      time with time zone       not null,
                    created_date  timestamp with time zone  not null,
                    primary key (ID, USER_ID)
                  );
--rollback drop table t_user_alert;

--changeset dtrashchenko:20230326_create_t_user_alert_table_comments runOnChange:true
comment on TABLE t_user_alert               is  'Таблица с информацией об уведомлениях пользователя';
comment on column t_user_alert.id           is  'ID записи';
comment on column t_user_alert.user_id      is  'ID пользователя';
comment on column t_user_alert.latitude     is  'Широта';
comment on column t_user_alert.longitude    is  'Долгота';
comment on column t_user_alert.days         is  'Массив номеров дней недели, в которые срабатывает уведомления (массив чисел)';
comment on column t_user_alert.buses        is  'Массив номеров автобусов, на которые срабатывает уведомления (массив строк)';
comment on column t_user_alert.start_date   is  'Время начала срабатывания уведомления';
comment on column t_user_alert.end_date     is  'Время конца срабатывания уведомления';
comment on column t_user_alert.created_date is  'Дата и время создания уведомления';
DROP SEQUENCE seq_users;
create sequence seq_users
START 500;

DROP SEQUENCE seq_general;
create sequence seq_general
START 500;

create table users
(
  id          bigint                default nextval('seq_users'::regclass) not null
    constraint pk_device_list primary key,
  name        varchar(200) not NULL,
  telephone   varchar(13),
  openid      varchar(200),
  login_name  varchar(200) not null,
  passwd      varchar(200) not null,
  mail        varchar(200),
  image       varchar(3000),
  create_time timestamp             default now(),
  zip_code    varchar(2000),
  address     varchar(3000),
  note        text,
  flag        int          not null default 0
);

comment on table users is '保存使用者的基本信息';


comment on column users.name is $$ 使用者名称 $$;
comment on column users.telephone is $$ 使用者电话 $$;
comment on column users.openid is $$ 微信的唯一标识 $$;
comment on column users.login_name is $$ 登录号,目前与电话相同 $$;
comment on column users.passwd is $$ 登录密码 $$;
comment on column users.mail is $$ 邮件地址 $$;
comment on column users.image is $$ 头像存储路径 $$;
comment on column users.zip_code is $$ 邮政编码 $$;
comment on column users.address is $$ 使用者地址 $$;
comment on column users.note is $$ 备注 $$;
comment on column users.flag is $$ 标志,备用 $$;

create unique index users_login_ix
on users (login_name);

create unique index users_openid_ix
on users (openid);



create table device_list
(
  id    bigint                default nextval('seq_general'::regclass) not null
    constraint pk_device_list primary key,
  name  varchar(200) not NULL,
  image varchar(3000),
  other jsonb,
  note  text,
  flag  int          not null default 1
);

comment on table device_list is '保存使用者的基本信息';


comment on column device_list.name is $$ 使用者名称 $$;
comment on column device_list.image is $$ 图像存储路径 $$;
comment on column device_list.other is $$ 其他,备用 $$;
comment on column device_list.note is $$ 备注 $$;
comment on column device_list.flag is $$1:独立式设备,2:组合式设备$$;
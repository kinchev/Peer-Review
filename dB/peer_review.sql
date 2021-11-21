create table `peer_review`.images
(
    image_id int auto_increment
        primary key,
    image    blob  null
);

create table `peer_review`.users
(
    user_id  int auto_increment
        primary key,
    username varchar(20) not null,
    password varchar(50) not null,
    email    varchar(50) not null,
    number   varchar(10) not null,
    image_id int         not null,
    constraint users_images_fk
        foreign key (image_id) references `peer_review`.images (image_id)
);

create table `peer_review`.teams
(
    team_id  int auto_increment
        primary key,
    name     varchar(30) not null,
    owner_id int         not null,
    constraint teams_users_fk
        foreign key (owner_id) references `peer_review`.users (user_id)
);

create table `peer_review`.teams_users
(
    team_id   int not null,
    member_id int not null,
    constraint teams_users_teams_fk
        foreign key (team_id) references `peer_review`.teams (team_id),
    constraint teams_users_users_fk
        foreign key (member_id) references `peer_review`.users (user_id)
);

create table `peer_review`.workitems
(
    id          int auto_increment
        primary key,
    title       varchar(80) not null,
    description longtext    not null
);

create table `peer_review`.teams_workitems
(
    team_id     int not null,
    workitem_id int not null,
    constraint teams_workitems_teams_fk
        foreign key (team_id) references `peer_review`.teams (team_id),
    constraint teams_workitems_workitems_fk
        foreign key (workitem_id) references `peer_review`.workitems (id)
);

create table `peer_review`.workitems_users
(
    workitem_id int not null,
    user_id     int null,
    constraint workitems_users__users_fk
        foreign key (user_id) references `peer_review`.users (user_id),
    constraint workitems_users_workitems_fk
        foreign key (workitem_id) references `peer_review`.workitems (id)
);



/************* START FROM CLEAN SLATE **************/
drop schema if exists personal_trainer;


/******* DATABASE CREATION AND SPECIFICATION *******/
create database if not exists personal_trainer;
use personal_trainer;


/***************** CREATING TABLES *****************/
create table if not exists user ( -- defines a user in the system (either client or trainer)
	user_id int auto_increment comment 'unique identifier for each user',
    user_type enum ('client', 'trainer'),
    first_name varchar(50),
    last_name varchar(50),
    dob date,
    constraint pk_user primary key (user_id)
);
alter table user auto_increment=100000000;

create table if not exists connect ( -- defines a connect between a client and a trainer
	connect_id int auto_increment comment 'unique identifier for each connect',
    trainer_id int comment "foreign key referring to the connect's trainer",
    client_id int comment "foreign key referring to the connect's client",
    constraint pk_connect primary key (connect_id)
);
alter table connect auto_increment=100000000;

create table if not exists message ( -- defines one message sent between two users in a connection
	message_id int auto_increment comment 'unique identifier for each message',
    sender_id int comment "foreign key referring to the id of the user who sent the message",
    recipient_id int comment "foreign key referring to the id of the user who received the message",
    connect_id int comment "foreign key referring to the id of the connection between the sender and the recipient",
    message_body varchar(160),
    sent_at datetime,
    constraint pk_message primary key (message_id)
);
alter table message auto_increment=100000000;

create table if not exists timeslot ( -- defines one time slot in a trainer's weekly schedule
	timeslot_id int auto_increment comment 'unique identifier for each timeslot',
    trainer_id int comment 'foreign key for the trainer who owns the timeslot',
    day_name enum ('monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'),
    time_index int,
    constraint time_index_range check (time_index between 0 and 48),
    constraint pk_timeslot primary key (timeslot_id)
);
alter table timeSlot auto_increment=100000000;

create table if not exists booking ( -- defines the booking of a timeslot for a client-trainer connection
	booking_id int auto_increment comment 'unique identifier for each booking',
    connect_id int comment 'foreign key for the connect making the booking',
    timeslot_id int comment 'foreign key for the timeslot the booking is made for',
    constraint pk_booking primary key (booking_id)
);
alter table booking auto_increment=100000000;

create table if not exists program ( -- defines a program that is sold by a trainer
	program_id int auto_increment comment 'unique identifier for each program',
    trainer_id int comment 'foreign key for the trainer selling the program',
    program_title varchar(100),
    constraint pk_program primary key (program_id)
);
alter table program auto_increment=100000000;

create table if not exists exercise ( -- defines one exercise that exists within a trainer's program
	exercise_id int auto_increment comment 'unique identifier for each exercise',
    program_id int comment 'foreign key for the program this exercise belongs to',
    exercise_title varchar(100),
    exercise_reps int,
    exercise_sets int,
    exercise_rest_time int comment 'in number of seconds',
    exercise_rpe int, 
    exercise_video_path varchar(256),
    constraint pk_exercise primary key (exercise_id)
);
alter table exercise auto_increment=100000000;

create table if not exists programPurchase ( -- defines the purchase of a program from a user
	program_id int comment 'foreign key for the program being purchased',
    user_id int comment 'foreign key for the user purchasing the program',
    constraint pk_programPurchase primary key (program_id, user_id)
);


/************** FOREIGN KEY CREATION ***************/
-- user to connect, message, timeslot, program, programPurchase
alter table connect add foreign key (trainer_id) references user(user_id);
alter table connect add foreign key (client_id) references user(user_id);
alter table message add foreign key (sender_id) references user(user_id);
alter table message add foreign key (recipient_id) references user(user_id);
alter table timeslot add foreign key (trainer_id) references user(user_id);
alter table program add foreign key (trainer_id) references user(user_id);
alter table programPurchase add foreign key (user_id) references user(user_id);

-- connect to message, booking
alter table message add foreign key (connect_id) references connect(connect_id);    
alter table booking add foreign key (connect_id) references connect(connect_id);    

-- program to programPurchase, exercise
alter table programPurchase add foreign key (program_id) references program(program_id);
alter table exercise add foreign key (program_id) references program(program_id);

-- timeslot to booking
alter table booking add foreign key (timeslot_id) references timeslot(timeslot_id);

insert into user (user_type, first_name, last_name, dob) values ('client', 'Jason', 'Envy', DATE('1990-05-06'));
select * from user;
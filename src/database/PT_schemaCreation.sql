
/************* START FROM CLEAN SLATE **************/
drop schema if exists personal_trainer;


/******* DATABASE CREATION AND SPECIFICATION *******/
create database if not exists personal_trainer;
use personal_trainer;


/***************** CREATING TABLES *****************/
create table if not exists client ( -- defines a client in the system
	client_id int auto_increment comment 'unique identifier for each client',
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    username varchar(20) not null,
    constraint pk_client primary key (client_id)
);
alter table client auto_increment=100000000;

create table if not exists trainer ( -- defines a trainer in the system
    trainer_id int auto_increment comment 'unique identifier for each trainer',
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    city varchar(100) not null,
    hourly_rate float not null,
    years_experience int not null,
    qualifications varchar(200) not null,
    picture_path varchar(256) not null,
    constraint pk_trainer primary key (trainer_id)
);
alter table trainer auto_increment=100000000;

create table if not exists rating ( -- defines a rating between a client and their trainer in the system
    rating_id int auto_increment comment 'unique identifier for each rating',
    connect_id int comment 'foreign key referring to the connect between the client and the trainer',
    rating_value int not null,
    constraint rating_value_range check (rating_value between 1 and 5),
    constraint pk_client primary key (rating_id)
);
alter table rating auto_increment=100000000;

create table if not exists connect ( -- defines a connect between a client and a trainer
	connect_id int auto_increment comment 'unique identifier for each connect',
    trainer_id int comment "foreign key referring to the connect's trainer" not null,
    client_id int comment "foreign key referring to the connect's client" not null,
    constraint pk_connect primary key (connect_id)
);
alter table connect auto_increment=100000000;

create table if not exists message ( -- defines one message sent between two users in a connection
	message_id int auto_increment comment 'unique identifier for each message',
    connect_id int comment 'foreign key referring to the id of the connection between the sender and the recipient' not null,
    sent_by enum ('client', 'trainer') not null,
    message_body varchar(160) not null,
    sent_at datetime not null,
    constraint pk_message primary key (message_id)
);
alter table message auto_increment=100000000;

create table if not exists timeslot ( -- defines one time slot in a trainer's weekly schedule
	timeslot_id int auto_increment comment 'unique identifier for each timeslot',
    trainer_id int comment 'foreign key for the trainer who owns the timeslot' not null,
    day_name enum ('monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday') not null,
    time_index int not null,
    constraint time_index_range check (time_index between 0 and 48),
    constraint pk_timeslot primary key (timeslot_id)
);
alter table timeSlot auto_increment=100000000;

create table if not exists booking ( -- defines the booking of a timeslot for a client-trainer connection
	booking_id int auto_increment comment 'unique identifier for each booking',
    connect_id int comment 'foreign key for the connect making the booking' not null,
    timeslot_id int comment 'foreign key for the timeslot the booking is made for' not null,
    constraint pk_booking primary key (booking_id)
);
alter table booking auto_increment=100000000;

create table if not exists program ( -- defines a program that is sold by a trainer
	program_id int auto_increment comment 'unique identifier for each program',
    trainer_id int comment 'foreign key for the trainer selling the program' not null,
    program_title varchar(100) not null,
    constraint pk_program primary key (program_id)
);
alter table program auto_increment=100000000;

create table if not exists exercise ( -- defines one exercise that exists within a trainer's program
	exercise_id int auto_increment comment 'unique identifier for each exercise',
    program_id int comment 'foreign key for the program this exercise belongs to' not null,
    exercise_title varchar(100) not null,
    exercise_reps int not null,
    exercise_sets int not null,
    exercise_rest_time int comment 'in number of seconds' not null,
    exercise_rpe int not null,
    exercise_video_path varchar(256) not null,
    constraint pk_exercise primary key (exercise_id)
);
alter table exercise auto_increment=100000000;

create table if not exists programPurchase ( -- defines the purchase of a program from a user
	program_id int comment 'foreign key for the program being purchased' not null,
    client_id int comment 'foreign key for the user purchasing the program',
    constraint pk_programPurchase primary key (program_id, client_id)
);
alter table programPurchase auto_increment=100000000;


/************** FOREIGN KEY CREATION ***************/

-- trainer to connect, timeslot, program
alter table connect add foreign key (trainer_id) references trainer(trainer_id) on delete cascade;
alter table timeslot add foreign key (trainer_id) references trainer(trainer_id) on delete cascade;
alter table program add foreign key (trainer_id) references trainer(trainer_id) on delete cascade;

-- client to programPurchase, connect
alter table programPurchase add foreign key (client_id) references client(client_id) on delete cascade;
alter table connect add foreign key (client_id) references client(client_id) on delete cascade;

-- connect to message, booking, rating
alter table message add foreign key (connect_id) references connect(connect_id) on delete cascade;
alter table booking add foreign key (connect_id) references connect(connect_id) on delete cascade;
alter table rating add foreign key (connect_id) references connect(connect_id) on delete set null;

-- program to programPurchase, exercise
alter table programPurchase add foreign key (program_id) references program(program_id) on delete cascade ;
alter table exercise add foreign key (program_id) references program(program_id) on delete cascade;

-- timeslot to booking
alter table booking add foreign key (timeslot_id) references timeslot(timeslot_id) on delete cascade;


/********* Timeslot procedure generation ***********/
delimiter //

CREATE PROCEDURE `fill_in_trainer_schedule` (IN _trainer_id int)
BEGIN
	declare _day_name varchar(10) default ('monday');
    declare _time_index int default(0);
    
    while _time_index < 48 do
            insert into timeslot(trainer_id, day_name, time_index) values (_trainer_id, _day_name, _time_index);
        set _time_index = _time_index + 1;
    end while;
    set _time_index = 0;
    
    set _day_name = 'tuesday';
    while _time_index < 48 do
            insert into timeslot(trainer_id, day_name, time_index) values (_trainer_id, _day_name, _time_index);
        set _time_index = _time_index + 1;
    end while;
    set _time_index = 0;
    
    set _day_name = 'wednesday';
    while _time_index < 48 do
            insert into timeslot(trainer_id, day_name, time_index) values (_trainer_id, _day_name, _time_index);
        set _time_index = _time_index + 1;
    end while;
    set _time_index = 0;
    
    set _day_name = 'thursday';
    while _time_index < 48 do
            insert into timeslot(trainer_id, day_name, time_index) values (_trainer_id, _day_name, _time_index);
        set _time_index = _time_index + 1;
    end while;
    set _time_index = 0;
    
    set _day_name = 'friday';
    while _time_index < 48 do
            insert into timeslot(trainer_id, day_name, time_index) values (_trainer_id, _day_name, _time_index);
        set _time_index = _time_index + 1;
    end while;
    set _time_index = 0;
    
    set _day_name = 'saturday';
    while _time_index < 48 do
		insert into timeslot(trainer_id, day_name, time_index) values (_trainer_id, _day_name, _time_index);
        set _time_index = _time_index + 1;
    end while;
    set _time_index = 0;
    
    set _day_name = 'sunday';
    while _time_index < 48 do
            insert into timeslot(trainer_id, day_name, time_index) values (_trainer_id, _day_name, _time_index);
        set _time_index = _time_index + 1;
    end while;
    set _time_index = 0;
    
END //

-- inserting sample data into the database --
-- inserting values into client
delimiter ;
insert into client (first_name, last_name, username) values ('Emily', 'Smith', 'GymJunkie2023');
insert into client (first_name, last_name, username) values ('Benjamin', 'Johnson', 'FitWarriorX');
insert into client (first_name, last_name, username) values ('Olivia', 'Williams', 'ActiveLifePro');

-- inserting values into trainer
insert into trainer (first_name, last_name, city, hourly_rate, years_experience, qualifications, picture_path)
values (
           'James',
           'Brown',
           'Cairns',
           40,
           1,
           'Certified Personal Trainer (CPT) through the National Academy of Sports Medicine (NASM) | NASM-CPT certification',
           'src/display/img/PT_james_brown.jpg'
       );
call fill_in_trainer_schedule(last_insert_id());
insert into trainer (first_name, last_name, city, hourly_rate, years_experience, qualifications, picture_path)
values (
           'Sophia',
           'Jones',
           'Geelong',
           60,
           3,
           'Bachelor''s Degree in Exercise Science and Certified Strength and Conditioning Specialist (CSCS) from the National Strength and Conditioning Association (NSCA) | CSCS certification',
           'src/display/img/PT_sophia_jones.jfif'
       );
call fill_in_trainer_schedule(last_insert_id());
insert into trainer (first_name, last_name, city, hourly_rate, years_experience, qualifications, picture_path)
values (
           'Liam',
           'Davis',
           'Newcastle',
           75,
           5,
           'Certified Fitness Trainer (CFT) from the International Sports Sciences Association (ISSA) | ISSA-CFT certification',
           'src/display/img/PT_liam_davis.jpg'
       );
call fill_in_trainer_schedule(last_insert_id());
insert into trainer (first_name, last_name, city, hourly_rate, years_experience, qualifications, picture_path)
values (
           'Ava',
           'Miller',
           'Townsville',
           90,
           8,
           'Diploma in Fitness and Nutrition from the American Council on Exercise (ACE) | ACE Fitness and Nutrition Diploma',
           'src/display/img/PT_ava_miller.jpg'
       );
call fill_in_trainer_schedule(last_insert_id());
insert into trainer (first_name, last_name, city, hourly_rate, years_experience, qualifications, picture_path)
values (
           'Ethan',
           'Wilson',
           'Toowoomba',
           120,
           10,
           'Master''s Degree in Kinesiology and Certified Health and Wellness Coach (CHWC) through the American College of Sports Medicine (ACSM) | ACSM-CHWC certification',
           'src/display/img/PT_ethan_wilson.jpg'
       );
call fill_in_trainer_schedule(last_insert_id());
insert into trainer (first_name, last_name, city, hourly_rate, years_experience, qualifications, picture_path)
values (
           'Mia',
           'Moore',
           'Wollongong',
           150,
           15,
           'Certified Group Fitness Instructor (CGFI) and Certified Nutrition Coach (CNC) from the American Council on Exercise (ACE) | ACE-CGFI and ACE-CNC certifications',
           'src/display/img/PT_mia_moore.jpg'
       );
call fill_in_trainer_schedule(last_insert_id());

-- inserting values into connect
insert into connect (trainer_id, client_id) values (
                                                       (select trainer_id from trainer where first_name = 'Mia'),
                                                       (select client_id from client where first_name = 'Emily')
                                                   );
insert into connect (trainer_id, client_id) values (
                                                       (select trainer_id from trainer where first_name = 'Ethan'),
                                                       (select client_id from client where first_name = 'Benjamin')
                                                   );
insert into connect (trainer_id, client_id) values (
                                                       (select trainer_id from trainer where first_name = 'Mia'),
                                                       (select client_id from client where first_name = 'Benjamin')
                                                   );

-- inserting values into ratings
insert into rating (connect_id, rating_value) values (
                                                         (select connect_id from connect where
                                                                 trainer_id = (select trainer_id from trainer where first_name = 'Mia') and
                                                                 client_id = (select client_id from client where first_name = 'Emily')
                                                         ), 4
                                                     );

-- inserting values into programs / exercises
insert into program (trainer_id, program_title) values (
                                                           (select trainer_id from trainer where first_name = 'Sophia'),
                                                           'Cardio Workout'
                                                       );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Sophia') and program_title = 'Cardio Workout'),
                                                      'High Knees',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_highKnees.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Sophia') and program_title = 'Cardio Workout'),
                                                      'Alternate Lunges',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_alternateLunges.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Sophia') and program_title = 'Cardio Workout'),
                                                      'Mountain Climbers',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_mountainClimbers.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Sophia') and program_title = 'Cardio Workout'),
                                                      'Burpees',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_burpees.mp4'
                                                  );

insert into program (trainer_id, program_title) values (
                                                           (select trainer_id from trainer where first_name = 'Ava'),
                                                           'Cardio Workout'
                                                       );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Ava') and program_title = 'Cardio Workout'),
                                                      'High Knees',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_highKnees.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Ava') and program_title = 'Cardio Workout'),
                                                      'Alternate Lunges',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_alternateLunges.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Ava') and program_title = 'Cardio Workout'),
                                                      'Mountain Climbers',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_mountainClimbers.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Ava') and program_title = 'Cardio Workout'),
                                                      'Burpees',
                                                      10,
                                                      3,
                                                      30,
                                                      3,
                                                      'src/display/vids/cardio_burpees.mp4'
                                                  );


insert into program (trainer_id, program_title) values (
                                                           (select trainer_id from trainer where first_name = 'James'),
                                                           'Bicep Workout'
                                                       );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'James') and program_title = 'Bicep Workout'),
                                                      'Cambered Bar',
                                                      12,
                                                      4,
                                                      120,
                                                      3,
                                                      'src/display/vids/bicep_camberedBar.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'James') and program_title = 'Bicep Workout'),
                                                      'Crush Grip',
                                                      15,
                                                      3,
                                                      60,
                                                      3,
                                                      'src/display/vids/bicep_crushGrip.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'James') and program_title = 'Bicep Workout'),
                                                      'Single Arm Hammer',
                                                      12,
                                                      2,
                                                      60,
                                                      4,
                                                      'src/display/vids/bicep_singleArmHammer.mp4'
                                                  );

insert into program (trainer_id, program_title) values (
                                                           (select trainer_id from trainer where first_name = 'Liam'),
                                                           'Bicep Workout'
                                                       );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Liam') and program_title = 'Bicep Workout'),
                                                      'Cambered Bar',
                                                      12,
                                                      4,
                                                      120,
                                                      3,
                                                      'src/display/vids/bicep_camberedBar.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Liam') and program_title = 'Bicep Workout'),
                                                      'Crush Grip',
                                                      15,
                                                      3,
                                                      60,
                                                      3,
                                                      'src/display/vids/bicep_crushGrip.mp4'
                                                  );
insert into exercise (program_id,
                      exercise_title,
                      exercise_reps,
                      exercise_sets,
                      exercise_rest_time,
                      exercise_rpe,
                      exercise_video_path) values (
                                                      (select program_id from program where trainer_id = (select trainer_id from trainer where first_name = 'Liam') and program_title = 'Bicep Workout'),
                                                      'Single Arm Hammer',
                                                      12,
                                                      2,
                                                      60,
                                                      4,
                                                      'src/display/vids/bicep_singleArmHammer.mp4'
                                                  );
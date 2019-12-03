insert into users (enabled, expired, height, name, password, username) values (true, false, 123,'xxx', '{bcrypt}$2a$10$2VOhbyK9IXoE3ei/Iqlv.exUrMt2Q404F0uZSb0wGl6xvSPEilmbO','xxx');
insert into workout (created_at,name,user_id) values ('2019-11-04','numero uno',1);
insert into workout_history (date, waist, weight, workout_made, user_id, workout_id) values ('2019-11-04',123,123,true,1,1);
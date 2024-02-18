INSERT INTO types (type_name) VALUES
        ('Универсал'),
        ('Седан'),
        ('Хэтчбек'),
        ('Лифтбек'),
        ('Внедорожник'),
        ('Кроссовер');

INSERT INTO klass (klass_name) VALUES
        ('A'),
        ('B'),
        ('C'),
        ('D'),
        ('E'),
        ('F'),
        ('S'),
        ('M'),
        ('J');

INSERT INTO directors (name)
VALUES    ('GermanyFirm'),
          ('RussianFirm'),
          ('ChinaFirm');


INSERT INTO cars (name,description,rate,releasedate,price,klass_id)
 VALUES    ('BMW','carssport',5,'2022-03-01',10000000,1),
           ('LADA','car',2,'2022-03-02',1000000,2),
           ('AUDI','sport',4,'2022-03-03',10000500,6);


INSERT INTO cars_directors (car_id,director_id)
VALUES    (1,1),
          (2,3),
          (3,2);

INSERT INTO car_types (car_id,type_id)
VALUES    (1,1),
          (2,3),
          (3,2);

INSERT INTO users (email,login,name,birthday)
VALUES    ('alex@mail.ru','alex','Alex','1960-03-01'),
          ('lena@mail.ru','lena','Lena','2000-03-02'),
          ('maksim@mail.ru','maksim','Maksim','1995-03-03');

INSERT INTO likes (car_id,user_id)
VALUES    (1,1),
          (1,2),
          (2,2),
          (3,2),
          (3,3);

INSERT INTO reviews (content,is_positive,user_id,car_id)
VALUES    ('test1',true,1,1),
          ('test2',false,2,2),
          ('test24',true,3,3),
          ('test3',true,2,1);

INSERT INTO review_useful (review_id,is_positive,user_id)
VALUES    (1,true,3),
          (2,false,3),
          (2,false,1),
          (2,true,2),
          (1,true,2),
          (3,true,1);


INSERT INTO feed (user_id,timestamp,event_type,operation,entity_id)
VALUES    (1,636966624590000000,'REVIEW','ADD',1),
          (1,636966624590000000,'LIKE','ADD',1);
delete from Car_Order_Cars;
delete from Car_Features;
delete from Car;
delete from Car_Order;

delete from Features;
insert into Features (id, name, type) 
                values ('DSL', 'Diesel', 'TYPE');
insert into Features (id, name, type) 
                values ('HYB', 'Hybrid/Electric', 'TYPE');
insert into Features (id, name, type) 
                values ('CAD', 'Cadillac', 'MAKE');
insert into Features (id, name, type) 
                values ('BMW', 'BMW', 'MAKE');
insert into Features (id, name, type) 
                values ('BLK', 'Black', 'COLOR');
insert into Features (id, name, type) 
                values ('SLV', 'Silver', 'COLOR');
insert into Features (id, name, type) 
                values ('18', '2018', 'YEAR');
insert into Features (id, name, type) 
                values ('19', '2019', 'YEAR');
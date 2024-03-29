create table if not exists Features (
  id varchar(4) not null,
  name varchar(25) not null,
  type varchar(10) not null
);

create table if not exists Car (
  id identity,
  name varchar(50) not null,
  createdAt timestamp not null
);

create table if not exists Car_Features (
  car bigint not null,
  feature varchar(4) not null
);

alter table Car_Features
    add foreign key (car) references Car(id);
alter table Car_Features
    add foreign key (feature) references Features(id);

create table if not exists Car_Order (
	id identity,
	deliveryName varchar(50) not null,
	deliveryStreet varchar(50) not null,
	deliveryCity varchar(50) not null,
	deliveryState varchar(2) not null,
	deliveryZip varchar(10) not null,
	ccNumber varchar(16) not null,
	ccExpiration varchar(5) not null,
	ccCVV varchar(3) not null,
    placedAt timestamp not null
);

create table if not exists Car_Order_Cars (
	carOrder bigint not null,
	car bigint not null
);

alter table Car_Order_Cars
    add foreign key (carOrder) references Car_Order(id);
alter table Car_Order_Cars
    add foreign key (car) references Car(id);

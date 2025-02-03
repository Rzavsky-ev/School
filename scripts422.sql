CREATE TABLE people(
id SERIAL,
name_human TEXT,
age_human INTEGER,
car_rights BOOLEAN
);

CREATE TABLE cars(
id SERIAL,
brand_car TEXT,
model_car TEXT,
price_car BOOLEAN
);

CREATE TABLE availability_car(
id_human INTEGER  PRIMARY KEY NOT NULL,
id_car INTEGER
);
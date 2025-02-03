ALTER TABLE student ADD CONSTRAINT age_constraint CHECK (age>=16);

ALTER TABLE student ADD CONSTRAINT name_constraint UNIQUE NOT NULL (name);

ALTER TABLE faculty ADD CONSTRAINT faculty_constraint UNIQUE (name, color);

ALTER TABLE student ADD CONSTRAINT age_default INTEGER DEFAULT '20';





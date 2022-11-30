
CREATE TABLE product (
	id serial,
	name character varying NOT NULL,
	amount integer,
	organization character varying,
	CONSTRAINT product_pk PRIMARY KEY (id)
);

CREATE TABLE organization (
	name character varying,
	CONSTRAINT organization_pk PRIMARY KEY (name)
);

ALTER TABLE product ADD CONSTRAINT product_org_fk FOREIGN KEY (organization) REFERENCES organization(name);

CREATE TABLE users (
	id serial,
	name character varying NOT NULL,
	password character varying NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE roles (
	id serial,
	role character varying NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (id)
);

CREATE TABLE user_roles (
	id serial,
 	user_id integer,
	role_id integer,
	CONSTRAINT user_roles_pk PRIMARY KEY (id)
);

ALTER TABLE user_roles ADD CONSTRAINT ur_fkU FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_roles ADD CONSTRAINT ur_fkR FOREIGN KEY (role_id) REFERENCES roles(id);

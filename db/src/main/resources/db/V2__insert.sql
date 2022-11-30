
insert into organization (name) values ('org1'),('org2');
insert into product (name, amount, organization) values ('table', 20, 'org1'),('chair', 1, 'org2'),('spoon', 1, 'org2');

insert into users (name, password) values ('guest', 'guest'), ('manager', 'manager');
insert into roles (role) values ('guest'), ('manager');
insert into user_roles (user_id, role_id) values (1, 1), (2, 2);





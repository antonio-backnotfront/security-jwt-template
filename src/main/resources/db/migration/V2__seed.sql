INSERT INTO role (name) values ('user');
INSERT INTO role (name) values ('admin');

# my_password11
INSERT INTO user (username, password, role_id) values ('User A', '$2a$12$0FNkvUZ3SYRVPg.ue.AWReOfiw4vlVpAnVEQq8lnCaP26pNAHoy42', 1);

# my_password12
INSERT INTO user (username, password, role_id) values ('Admin A', '$2a$12$SnEedetBIhuHUmetkutP6.62XYbrM1i/cZF6uKFKaaMa5sUgCSu6G', 2);
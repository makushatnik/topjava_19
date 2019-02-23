DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (id, datetime, description, calories) VALUES
  (nextval('global_seq'::regclass), '2015-05-30 10:00', 'Завтрак', 500),
  (nextval('global_seq'::regclass), '2015-05-30 13:00', 'Обед', 1000),
  (nextval('global_seq'::regclass), '2015-05-30 20:00', 'Ужин', 500),
  (nextval('global_seq'::regclass), '2015-05-31 10:00', 'Завтрак', 1000),
  (nextval('global_seq'::regclass), '2015-05-31 13:00', 'Обед', 500),
  (nextval('global_seq'::regclass), '2015-05-31 20:00', 'Ужин', 510);
  /*(nextval('global_seq'::regclass), '2015-06-12 09:00', 'Завтрак', 250),
  (nextval('global_seq'::regclass), '2015-06-12 11:00', 'Брекфаст', 350),
  (nextval('global_seq'::regclass), '2015-06-12 13:00', 'Обед', 500),
  (nextval('global_seq'::regclass), '2015-06-12 17:00', 'Ланч', 250),
  (nextval('global_seq'::regclass), '2015-06-12 19:00', 'Ужин', 500);*/

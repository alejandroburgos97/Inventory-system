CREATE DATABASE InventoryDB;

\c InventoryDB;

-- Insert positions
INSERT INTO positions (position_name) VALUES
  ('Asesor de ventas'),
  ('Administrador'),
  ('Soporte');

-- Insert users
INSERT INTO users (name, age, position_id, company_entry_date) VALUES
    ('Juan Martinez', 35, 1, '2020-01-15'),
    ('Maria Alvarez', 28, 2, '2019-05-20'),
    ('Michael Jurado', 30, 2, '2018-08-10'),
    ('Andres Arteaga', 40, 3, '2017-11-25'),
    ('David Silva', 50, 3, '2021-03-12');

-- Insert products
INSERT INTO products (name, quantity, entry_date, registered_user_id, modification_date, modified_user_id) VALUES
('Product A', 50, '2020-01-10', 11, '2020-01-10', 12),
('Product B', 100, '2020-02-15', 12, '2020-02-15', 13),
('Product C', 75, '2020-03-20', 13, '2020-03-20', 14),
('Product D', 60, '2020-04-25', 14, '2020-04-25', 15),
('Product E', 90, '2020-05-30', 15, '2020-05-30', 11),
('Product F', 70, '2020-06-05', 11, '2020-06-05', 12),
('Product G', 110, '2020-07-10', 12, '2020-07-10', 13),
('Product H', 55, '2020-08-15', 13, '2020-08-15', 14),
('Product I', 85, '2020-09-20', 14, '2020-09-20', 15),
('Product J', 65, '2020-10-25', 15, '2020-10-25', 11),
('Product K', 95, '2020-11-30', 11, '2020-11-30', 12),
('Product L', 45, '2020-12-05', 12, '2020-12-05', 13);


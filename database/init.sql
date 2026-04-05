CREATE DATABASE IF NOT EXISTS hr_db;
USE hr_db;

-- 1. Departments Table
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    manager_name VARCHAR(100),
    budget DECIMAL(15, 2)
);

-- 2. Employees Table
CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    hire_date DATE,
    salary DECIMAL(10, 2),
    email VARCHAR(100)
);

-- 3. Vacations Table
CREATE TABLE IF NOT EXISTS vacations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    start_date DATE,
    end_date DATE,
    days_taken INT,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- 4. Absences Table
CREATE TABLE IF NOT EXISTS absences (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    date DATE,
    reason VARCHAR(255),
    justified ENUM('yes', 'no'),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- ==============================================
-- INSERT MOCK DATA
-- ==============================================

-- Insert Departments
INSERT INTO departments (name, manager_name, budget) VALUES
('IT', 'Alice Smith', 500000.00),
('Sales', 'Bob Johnson', 300000.00),
('HR', 'Carol Williams', 150000.00),
('Finance', 'David Brown', 400000.00),
('Marketing', 'Eve Davis', 250000.00);

-- Insert Employees (20+ spread across departments)
INSERT INTO employees (name, department, hire_date, salary, email) VALUES
('Alice Smith', 'IT', '2019-03-15', 95000.00, 'alice@company.com'),
('Bob Johnson', 'Sales', '2018-07-22', 85000.00, 'bob@company.com'),
('Carol Williams', 'HR', '2020-01-10', 75000.00, 'carol@company.com'),
('David Brown', 'Finance', '2017-11-05', 105000.00, 'david@company.com'),
('Eve Davis', 'Marketing', '2021-06-30', 80000.00, 'eve@company.com'),
('Frank Miller', 'IT', '2022-02-14', 72000.00, 'frank@company.com'),
('Grace Wilson', 'IT', '2021-08-19', 78000.00, 'grace@company.com'),
('Henry Moore', 'Sales', '2023-01-25', 60000.00, 'henry@company.com'),
('Ivy Taylor', 'Sales', '2020-09-12', 68000.00, 'ivy@company.com'),
('Jack Anderson', 'HR', '2022-05-08', 55000.00, 'jack@company.com'),
('Karen Thomas', 'HR', '2021-11-20', 62000.00, 'karen@company.com'),
('Leo Jackson', 'Finance', '2019-10-01', 90000.00, 'leo@company.com'),
('Mia White', 'Finance', '2023-03-15', 65000.00, 'mia@company.com'),
('Noah Harris', 'Marketing', '2020-04-10', 71000.00, 'noah@company.com'),
('Olivia Martin', 'Marketing', '2022-07-22', 67000.00, 'olivia@company.com'),
('Paul Thompson', 'IT', '2018-12-05', 88000.00, 'paul@company.com'),
('Quinn Garcia', 'Sales', '2021-02-28', 70000.00, 'quinn@company.com'),
('Rachel Martinez', 'HR', '2019-06-17', 69000.00, 'rachel@company.com'),
('Sam Robinson', 'Finance', '2020-08-09', 82000.00, 'sam@company.com'),
('Tina Clark', 'Marketing', '2021-01-14', 73000.00, 'tina@company.com');

-- Insert Vacations (30 records)
INSERT INTO vacations (employee_id, start_date, end_date, days_taken) VALUES
(1, '2023-01-10', '2023-01-15', 5), (2, '2023-02-14', '2023-02-16', 3),
(3, '2023-03-01', '2023-03-05', 5), (4, '2023-04-10', '2023-04-20', 10),
(5, '2023-05-05', '2023-05-10', 5), (6, '2023-06-15', '2023-06-20', 5),
(7, '2023-07-01', '2023-07-15', 10), (8, '2023-08-05', '2023-08-10', 5),
(9, '2023-09-12', '2023-09-15', 3), (10, '2023-10-20', '2023-10-25', 5),
(11, '2023-11-22', '2023-11-25', 3), (12, '2023-12-20', '2023-12-31', 8),
(13, '2023-01-05', '2023-01-08', 3), (14, '2023-02-20', '2023-02-25', 5),
(15, '2023-03-15', '2023-03-18', 3), (16, '2023-04-05', '2023-04-12', 7),
(17, '2023-05-15', '2023-05-20', 5), (18, '2023-06-10', '2023-06-12', 2),
(19, '2023-07-20', '2023-07-25', 5), (20, '2023-08-15', '2023-08-20', 5),
(1, '2023-09-01', '2023-09-05', 5), (2, '2023-10-10', '2023-10-15', 5),
(3, '2023-11-05', '2023-11-10', 5), (4, '2023-12-05', '2023-12-10', 5),
(5, '2023-01-20', '2023-01-22', 2), (6, '2023-02-10', '2023-02-12', 2),
(7, '2023-03-25', '2023-03-28', 3), (8, '2023-04-20', '2023-04-22', 2),
(9, '2023-05-10', '2023-05-15', 5), (10, '2023-06-05', '2023-06-08', 3);

-- Insert Absences (20 records)
INSERT INTO absences (employee_id, date, reason, justified) VALUES
(1, '2023-01-05', 'Sick leave', 'yes'),
(2, '2023-02-08', 'Car breakdown', 'no'),
(3, '2023-03-12', 'Family emergency', 'yes'),
(4, '2023-04-18', 'Doctor appointment', 'yes'),
(5, '2023-05-22', 'Missed alarm', 'no'),
(6, '2023-06-10', 'Sick leave', 'yes'),
(7, '2023-07-14', 'Dentist', 'yes'),
(8, '2023-08-19', 'Personal reasons', 'no'),
(9, '2023-09-25', 'Sick leave', 'yes'),
(10, '2023-10-05', 'Traffic', 'no'),
(11, '2023-11-12', 'Sick leave', 'yes'),
(12, '2023-12-01', 'Family emergency', 'yes'),
(13, '2023-01-15', 'Sick leave', 'yes'),
(14, '2023-02-22', 'Child care', 'yes'),
(15, '2023-03-28', 'Missed train', 'no'),
(16, '2023-04-15', 'Sick leave', 'yes'),
(17, '2023-05-02', 'Doctor appointment', 'yes'),
(18, '2023-06-18', 'Personal reasons', 'no'),
(19, '2023-07-30', 'Sick leave', 'yes'),
(20, '2023-08-25', 'Car breakdown', 'no');

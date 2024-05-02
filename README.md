# Persistencelayer
# 数据库
# 创建数据库
```angular2html
create table student
(
    student_id            INT auto_increment primary key,
    username              VARCHAR(50) unique,
    password_hash         VARCHAR(255),
    first_name            VARCHAR(50),
    last_name             VARCHAR(50),
    date_of_birth         DATE,
    gender                ENUM ('Male', 'Female', 'Other'),
    address               VARCHAR(255),
    email                 VARCHAR(100),
    phone_number          VARCHAR(20),
    guardian_name         VARCHAR(100),
    guardian_phone_number VARCHAR(20),
    admission_date        DATE,
    graduation_date       DATE,
    department            VARCHAR(50),
    major                 VARCHAR(50),
    current_year          INT
);
```
# 插入数据
```
insert into student (username, password_hash, first_name, last_name, date_of_birth, gender, address, email,
phone_number, guardian_name, guardian_phone_number, admission_date, graduation_date, department,
major, current_year)
values ('student1', 'password_hash1', 'John', 'Doe', '2000-01-01', 'Male', '123 Main St, City', 'john@example.com',
'1234567890', 'Jane Doe', '9876543210', '2020-09-01', '2024-06-30', 'Computer Science', 'Software Engineering',
2),
('student2', 'password_hash2', 'Jane', 'Smith', '2001-02-02', 'Female', '456 Elm St, Town', 'jane@example.com',
'0987654321', 'John Smith', '6789012345', '2019-09-01', '2023-06-30', 'Electrical Engineering', 'Power Systems',
3),
('student3', 'password_hash3', 'Michael', 'Johnson', '2000-03-03', 'Male', '789 Oak St, Village',
'michael@example.com', '4567890123', 'Michelle Johnson', '3456789012', '2021-09-01', '2025-06-30', 'Biology',
'Microbiology', 1),
('student4', 'password_hash4', 'Emily', 'Williams', '2001-04-04', 'Female', '101 Pine St, Countryside',
'emily@example.com', '2345678901', 'Ethan Williams', '2345678901', '2018-09-01', '2022-06-30', 'Mathematics',
'Statistics', 4),
('student5', 'password_hash5', 'Christopher', 'Brown', '2000-05-05', 'Male', '234 Maple St, Suburb',
'chris@example.com', '7654321098', 'Christina Brown', '5432109876', '2020-09-01', '2024-06-30', 'Chemistry',
'Organic Chemistry', 2),
('student6', 'password_hash6', 'Megan', 'Jones', '2001-06-06', 'Female', '345 Cedar St, Metro',
'megan@example.com', '3210987654', 'Matthew Jones', '4321098765', '2019-09-01', '2023-06-30', 'Physics',
'Astrophysics', 3),
('student7', 'password_hash7', 'Daniel', 'Miller', '2000-07-07', 'Male', '456 Birch St, Outskirts',
'daniel@example.com', '8901234567', 'Diana Miller', '3210987654', '2021-09-01', '2025-06-30',
'Computer Science', 'Data Science', 1),
('student8', 'password_hash8', 'Ashley', 'Davis', '2001-08-08', 'Female', '567 Walnut St, Downtown',
'ashley@example.com', '6789012345', 'Andrew Davis', '8901234567', '2018-09-01', '2022-06-30',
'Electrical Engineering', 'Electronics', 4),
('student9', 'password_hash9', 'David', 'Martinez', '2000-09-09', 'Male', '678 Pineapple St, Coastal',
'david@example.com', '7890123456', 'Danielle Martinez', '7890123456', '2020-09-01', '2024-06-30', 'Biology',
'Genetics', 2),
('student10', 'password_hash10', 'Jessica', 'Garcia', '2001-10-10', 'Female', '789 Banana St, Tropical',
'jessica@example.com', '8901234567', 'Jacob Garcia', '8901234567', '2019-09-01', '2023-06-30', 'Mathematics',
'Applied Mathematics', 3),
('student11', 'password_hash11', 'Matthew', 'Rodriguez', '2000-11-11', 'Male', '890 Orange St, Desert',
'matthew@example.com', '9012345678', 'Madison Rodriguez', '6789012345', '2021-09-01', '2025-06-30', 'Chemistry',
'Analytical Chemistry', 1),
('student12', 'password_hash12', 'Amanda', 'Martinez', '2001-12-12', 'Female', '901 Lemon St, Rural',
'amanda@example.com', '0123456789', 'Anthony Martinez', '7890123456', '2018-09-01', '2022-06-30', 'Physics',
'Quantum Physics', 4),
('student13', 'password_hash13', 'Andrew', 'Lopez', '2000-01-13', 'Male', '123 Cherry St, Mountain',
'andrew@example.com', '1234567890', 'Anna Lopez', '0123456789', '2020-09-01', '2024-06-30', 'Computer Science',
'Software Engineering', 2),
('student14', 'password_hash14', 'Samantha', 'Hernandez', '2001-02-14', 'Female', '234 Strawberry St, Valley',
'samantha@example.com', '2345678901', 'Samuel Hernandez', '1234567890', '2019-09-01', '2023-06-30',
'Electrical Engineering', 'Power Systems', 3),
('student15', 'password_hash15', 'Ryan', 'King', '2000-03-15', 'Male', '345 Watermelon St, Plateau',
'ryan@example.com', '3456789012', 'Rachel King', '2345678901', '2021-09-01', '2025-06-30', 'Biology',
'Microbiology', 1),
('student16', 'password_hash16', 'Elizabeth', 'Lee', '2001-04-16', 'Female', '456 Blueberry St, Hill',
'elizabeth@example.com', '4567890123', 'Eric Lee', '3456789012', '2018-09-01', '2022-06-30', 'Mathematics',
'Statistics', 4),
('student17', 'password_hash17', 'Justin', 'Wright', '2000-05-17', 'Male', '567 Grape St, Summit',
'justin@example.com', '5678901234', 'Jessica Wright', '4567890123', '2020-09-01', '2024-06-30', 'Chemistry',
'Organic Chemistry', 2),
('student18', 'password_hash18', 'Lauren', 'Lopez', '2001-06-18', 'Female', '678 Orange St, Mesa',
'lauren@example.com', '6789012345', 'Logan Lopez', '5678901234', '2019-09-01', '2023-06-30', 'Physics',
'Astrophysics', 3),
('student19', 'password_hash19', 'Tyler', 'Hill', '2000-07-19', 'Male', '789 Lemon St, Ridge',
'tyler@example.com', '7890123456', 'Taylor Hill', '6789012345', '2021-09-01', '2025-06-30', 'Computer Science',
'Data Science', 1),
('student20', 'password_hash20', 'Rachel', 'Scott', '2001-08-20', 'Female', '890 Peach St, Canyon',
'rachel@example.com', '8901234567', 'Ryan Scott', '7890123456', '2018-09-01', '2022-06-30',
'Electrical Engineering', 'Electronics', 4),
('student21', 'password_hash21', 'Alex', 'Taylor', '2000-09-21', 'Other', '123 Plum St, Canyon',
'alex@example.com', '1234567890', 'Ashley Taylor', '0123456789', '2020-09-01', '2024-06-30', 'Computer Science',
'Software Engineering', 2),
('student22', 'password_hash22', 'Jordan', 'Lee', '2001-10-22', 'Other', '234 Kiwi St, Plateau',
'jordan@example.com', '2345678901', 'Jamie Lee', '1234567890', '2019-09-01', '2023-06-30',
'Electrical Engineering', 'Power Systems', 3),
('student23', 'password_hash23', 'Taylor', 'Clark', '2000-11-23', 'Other', '345 Fig St, Mesa',
'taylor@example.com', '3456789012', 'Tyler Clark', '2345678901', '2021-09-01', '2025-06-30', 'Biology',
'Microbiology', 1),
('student24', 'password_hash24', 'Jordan', 'Young', '2001-12-24', 'Other', '456 Apple St, Summit',
'jordan@example.com', '4567890123', 'Jessica Young', '3456789012', '2018-09-01', '2022-06-30', 'Mathematics',
'Statistics', 4),
('student25', 'password_hash25', 'Taylor', 'Hall', '2000-01-25', 'Other', '567 Cherry St, Valley',
'taylor@example.com', '5678901234', 'Tyler Hall', '4567890123', '2020-09-01', '2024-06-30', 'Chemistry',
'Organic Chemistry', 2);
```
# 字段说明
| 字段名               | 描述                                     |
|----------------------|------------------------------------------|
| student_id           | 学生ID，通常作为主键，用于唯一标识每个学生 |
| username             | 学生的登录名，用于身份验证                |
| password_hash        | 存储学生密码的哈希值，而不是明文密码。密码应该在应用程序层进行哈希和加盐处理，然后存储在数据库中 |
| first_name           | 学生的名字                               |
| last_name            | 学生的姓氏                               |
| date_of_birth        | 学生的出生日期                           |
| gender               | 学生的性别，通常使用枚举类型表示，包括 "Male"、"Female" 和 "Other" |
| address              | 学生的联系地址                           |
| email                | 学生的电子邮件地址                       |
| phone_number         | 学生的电话号码                           |
| guardian_name        | 学生的监护人姓名                         |
| guardian_phone_number| 学生的监护人电话号码                     |
| admission_date       | 学生的入学日期                           |
| graduation_date      | 学生的毕业日期                           |
| department           | 学生所在的部门或学院，这里是一个简单的字符串字段，不引用其他表 |
| major                | 学生的专业                               |
| current_year         | 学生当前所在的年级                       |
| department_id        | 是部门的唯一标识符                       |
| department_name      | 是部门的名称                             |



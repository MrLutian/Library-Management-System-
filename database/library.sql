CREATE DATABASE IF NOT EXISTS library_db
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_db;

DROP TABLE IF EXISTS borrow_records;
DROP TABLE IF EXISTS readers;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS admins;

CREATE TABLE admins (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  real_name VARCHAR(50) NOT NULL DEFAULT '系统管理员',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL UNIQUE,
  description VARCHAR(255)
);

CREATE TABLE books (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  isbn VARCHAR(30) UNIQUE,
  title VARCHAR(150) NOT NULL,
  author VARCHAR(100),
  publisher VARCHAR(120),
  category_id BIGINT,
  total_count INT NOT NULL DEFAULT 0,
  available_count INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_books_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE readers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reader_no VARCHAR(40) NOT NULL UNIQUE,
  name VARCHAR(60) NOT NULL,
  phone VARCHAR(30),
  email VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE borrow_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  book_id BIGINT NOT NULL,
  reader_id BIGINT NOT NULL,
  borrow_date DATE NOT NULL,
  due_date DATE NOT NULL,
  return_date DATE NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'BORROWED',
  CONSTRAINT fk_borrow_book FOREIGN KEY (book_id) REFERENCES books(id),
  CONSTRAINT fk_borrow_reader FOREIGN KEY (reader_id) REFERENCES readers(id)
);

INSERT INTO admins(username,password,real_name) VALUES('admin','admin123','系统管理员');
INSERT INTO categories(name,description) VALUES
('文学','文学类图书'),('计算机','计算机与信息技术类图书'),('教育','教育教学类图书');
INSERT INTO books(isbn,title,author,publisher,category_id,total_count,available_count) VALUES
('9780000000001','Java程序设计基础','示例作者','示例出版社',2,10,10),
('9780000000002','大学计算机基础','示例作者','示例出版社',2,8,8);
INSERT INTO readers(reader_no,name,phone,email) VALUES
('R2026001','张同学','13800000000','zhang@example.com'),
('R2026002','李同学','13900000000','li@example.com');

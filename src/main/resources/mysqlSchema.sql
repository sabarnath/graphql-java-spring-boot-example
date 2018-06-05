use tpm;

DROP TABLE IF EXISTS book;

CREATE TABLE book (
  book_id INT NOT NULL AUTO_INCREMENT,
  book_title VARCHAR(100) NOT NULL,
  book_isbn VARCHAR(100) NOT NULL,
  book_pageCount INT NOT NULL DEFAULT 0,
  author_id INT NOT NULL,
  PRIMARY KEY (book_id));

DROP TABLE IF EXISTS author;

CREATE TABLE author (  
  book_id INT NOT NULL AUTO_INCREMENT,
  author_first_name VARCHAR(100) NOT NULL,
  author_last_name VARCHAR(100) NOT NULL, 
  PRIMARY KEY (book_id));


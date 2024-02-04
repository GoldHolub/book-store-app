INSERT INTO categories (id, name, description, is_deleted) VALUES (1, 'Fiction', null, false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'new book 1', 'Mark', '978-0-545-01022-1', 10.0, null, null, false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'new book 2', 'John', '978-0-596-52068-7', 15.0, null, null, false);

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);

INSERT INTO shopping_carts (id, user_id) VALUES (1, 1);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 2);
INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (2, 1, 2, 2);
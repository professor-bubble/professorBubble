-- 기본 Role 데이터
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('UNIVERSITY');
INSERT INTO roles (name) VALUES ('PROFESSOR');
INSERT INTO roles (name) VALUES ('STUDENT');

INSERT INTO users (login_id, password, name, ROLE_ID, created_at, phone_number, USER_ID, EMAIL)
VALUES ('admin', '$2b$12$ZlxE143WanEnFH3/TbjH8uTwGXePjYSNyqCgy6x4ZkP4jx5WjzGfO', 'admin', 1, '2025-04-22 16:01:13.604', '00000000000', 'userId', 'email');
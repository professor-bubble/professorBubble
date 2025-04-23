-- 기본 Role 데이터
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_UNIVERSITY');
INSERT INTO roles (name) VALUES ('ROLE_PROFESSOR');
INSERT INTO roles (name) VALUES ('ROLE_STUDENT');

INSERT INTO users (login_id, password, name, ROLE_ROLE_ID, created_at, phone_number, USER_ID, EMAIL) VALUES ('admin', 'admin', 'admin', 1, '2025-04-22 16:01:13.604', '00000000000', 'userId', 'email');
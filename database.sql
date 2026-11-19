CREATE DATABASE ShoppingJPA;
GO

-- Chạy sau khi chạy ứng dụng một lần (Hibernate tự tạo bảng users)
INSERT INTO ShoppingJPA.dbo.users(email, username, fullname, password, avatar, roleid, phone, createddate, active)
VALUES ('admin@gmail.com', 'admin', N'Quản trị viên', '123456', NULL, 1, '0900000000', GETDATE(), 1),
       ('user@gmail.com', 'user', N'Người dùng', '123456', NULL, 5, '0911111111', GETDATE(), 1);

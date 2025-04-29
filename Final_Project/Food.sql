-- ===============================
-- FULL DATABASE RESET + SETUP FOR FoodShip
-- ===============================

-- 1. Dùng master database
USE master
GO

-- 2. Xóa database nếu tồn tại
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'FoodShip')
BEGIN
    ALTER DATABASE [FoodShip] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [FoodShip];
END
GO

-- 3. Tạo lại database
CREATE DATABASE FoodShip
GO

-- 4. Sử dụng database vừa tạo
USE FoodShip
GO

-- =============================
-- 5. Tạo bảng hệ thống
-- =============================

-- Bảng trạng thái người dùng
CREATE TABLE UserStatus (
    status_id INT PRIMARY KEY,
    status_name NVARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(255)
);

-- Bảng Customers
CREATE TABLE Customers (
    customer_id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone NVARCHAR(15) NOT NULL CHECK (phone LIKE '0%' AND LEN(phone) = 10 AND phone NOT LIKE '%[^0-9]%'),
    password NVARCHAR(60) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    status_id INT NOT NULL FOREIGN KEY REFERENCES UserStatus(status_id),
    created_at DATETIME DEFAULT GETDATE()
);

-- Bảng Shippers
CREATE TABLE Shippers (
    shipper_id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone NVARCHAR(15) NOT NULL CHECK (phone LIKE '0%' AND LEN(phone) = 10 AND phone NOT LIKE '%[^0-9]%'),
    password NVARCHAR(60) NOT NULL,
    cccd NVARCHAR(12) NOT NULL CHECK (LEN(cccd) = 12 AND cccd NOT LIKE '%[^0-9]%'),
    driver_license NVARCHAR(12) NOT NULL CHECK (LEN(driver_license) BETWEEN 10 AND 12),
    driver_license_image VARBINARY(MAX),
    address NVARCHAR(255) NOT NULL,
    vehicle_info NVARCHAR(100) NOT NULL,
    status_id INT NOT NULL FOREIGN KEY REFERENCES UserStatus(status_id),
    created_at DATETIME DEFAULT GETDATE()
);

-- Bảng Restaurants
CREATE TABLE Restaurants (
    restaurant_id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone NVARCHAR(15) NOT NULL CHECK (phone LIKE '0%' AND LEN(phone) = 10 AND phone NOT LIKE '%[^0-9]%'),
    password NVARCHAR(60) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    opening_hours NVARCHAR(100),
    cuisine_type NVARCHAR(100),
    status_id INT NOT NULL FOREIGN KEY REFERENCES UserStatus(status_id),
    created_at DATETIME DEFAULT GETDATE()
);

-- Bảng SystemAdmins
CREATE TABLE SystemAdmins (
    admin_id INT IDENTITY PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(60) NOT NULL,
    created_at DATETIME DEFAULT GETDATE()
);

-- =============================
-- 6. Các bảng giao dịch
-- =============================

-- Bảng MenuItems
CREATE TABLE MenuItems (
    item_id INT IDENTITY PRIMARY KEY,
    restaurant_id INT NOT NULL FOREIGN KEY REFERENCES Restaurants(restaurant_id),
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    image_url NVARCHAR(255),
    is_available BIT NOT NULL DEFAULT 1,
    created_at DATETIME DEFAULT GETDATE()
);

-- Bảng Cart
CREATE TABLE Cart (
    cart_id INT IDENTITY PRIMARY KEY,
    customer_id INT NOT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Bảng CartItems
CREATE TABLE CartItems (
    cart_item_id INT IDENTITY PRIMARY KEY,
    cart_id INT NOT NULL FOREIGN KEY REFERENCES Cart(cart_id),
    item_id INT NOT NULL FOREIGN KEY REFERENCES MenuItems(item_id),
    quantity INT NOT NULL CHECK (quantity > 0),
    created_at DATETIME DEFAULT GETDATE()
);

-- Bảng Orders
CREATE TABLE Orders (
    order_id INT IDENTITY PRIMARY KEY,
    customer_id INT NOT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    restaurant_id INT NOT NULL FOREIGN KEY REFERENCES Restaurants(restaurant_id),
    shipper_id INT NULL FOREIGN KEY REFERENCES Shippers(shipper_id),
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method NVARCHAR(50) NOT NULL CHECK (payment_method IN ('online', 'offline')),
    discount_code NVARCHAR(50),
    status NVARCHAR(50) NOT NULL CHECK (status IN ('pending', 'preparing', 'delivering', 'delivered', 'cancelled')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Bảng OrderItems
CREATE TABLE OrderItems (
    order_item_id INT IDENTITY PRIMARY KEY,
    order_id INT NOT NULL FOREIGN KEY REFERENCES Orders(order_id),
    item_id INT NOT NULL FOREIGN KEY REFERENCES MenuItems(item_id),
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10,2) NOT NULL,
    created_at DATETIME DEFAULT GETDATE()
);

-- Bảng Deliveries
CREATE TABLE Deliveries (
    delivery_id INT IDENTITY PRIMARY KEY,
    order_id INT NOT NULL FOREIGN KEY REFERENCES Orders(order_id),
    shipper_id INT NOT NULL FOREIGN KEY REFERENCES Shippers(shipper_id),
    status NVARCHAR(50) NOT NULL CHECK (status IN ('picked_up', 'delivering', 'delivered')),
    updated_at DATETIME DEFAULT GETDATE()
);

-- =============================
-- 7. Các bảng phụ trợ
-- =============================

-- Bảng SocialAccounts
CREATE TABLE SocialAccounts (
    social_id INT IDENTITY PRIMARY KEY,
    customer_id INT NOT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    provider NVARCHAR(20) NOT NULL CHECK (provider IN ('Google', 'Facebook')),
    provider_uid NVARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    UNIQUE (provider, provider_uid)
);

-- Bảng ItemReviews
CREATE TABLE ItemReviews (
    review_id INT IDENTITY PRIMARY KEY,
    item_id INT NOT NULL FOREIGN KEY REFERENCES MenuItems(item_id),
    customer_id INT NOT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE(),
    UNIQUE (item_id, customer_id)
);

-- Bảng DeliveryReviews
CREATE TABLE DeliveryReviews (
    review_id INT IDENTITY PRIMARY KEY,
    order_id INT NOT NULL FOREIGN KEY REFERENCES Orders(order_id),
    customer_id INT NOT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    target_type NVARCHAR(20) NOT NULL CHECK (target_type IN ('shipper', 'restaurant')),
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE(),
    UNIQUE (order_id, target_type)
);

-- Bảng SupportTickets
CREATE TABLE SupportTickets (
    ticket_id INT IDENTITY PRIMARY KEY,
    customer_id INT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    shipper_id INT NULL FOREIGN KEY REFERENCES Shippers(shipper_id),
    restaurant_id INT NULL FOREIGN KEY REFERENCES Restaurants(restaurant_id),
    subject NVARCHAR(100) NOT NULL,
    message NVARCHAR(MAX) NOT NULL,
    status NVARCHAR(50) NOT NULL CHECK (status IN ('open', 'pending', 'closed')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT chk_support_ticket CHECK (
        (customer_id IS NOT NULL AND shipper_id IS NULL AND restaurant_id IS NULL) OR
        (customer_id IS NULL AND shipper_id IS NOT NULL AND restaurant_id IS NULL) OR
        (customer_id IS NULL AND shipper_id IS NULL AND restaurant_id IS NOT NULL)
    )
);

-- Bảng VerificationCodes
CREATE TABLE VerificationCodes (
    verification_id INT IDENTITY PRIMARY KEY,
    customer_id INT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    shipper_id INT NULL FOREIGN KEY REFERENCES Shippers(shipper_id),
    restaurant_id INT NULL FOREIGN KEY REFERENCES Restaurants(restaurant_id),
    code NVARCHAR(32) NOT NULL,
    plain_code NVARCHAR(6) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    expires_at DATETIME NOT NULL,
    is_used BIT NOT NULL DEFAULT 0,
    CONSTRAINT chk_verification_user CHECK (
        (customer_id IS NOT NULL AND shipper_id IS NULL AND restaurant_id IS NULL) OR
        (customer_id IS NULL AND shipper_id IS NOT NULL AND restaurant_id IS NULL) OR
        (customer_id IS NULL AND shipper_id IS NULL AND restaurant_id IS NOT NULL)
    )
)
-- Bảng phí nhà hàng
CREATE TABLE RestaurantFees (
    fee_id INT IDENTITY PRIMARY KEY,
    restaurant_id INT FOREIGN KEY REFERENCES Restaurants(restaurant_id),
    order_id INT FOREIGN KEY REFERENCES Orders(order_id),
    fee_amount DECIMAL(10,2),
    fee_type NVARCHAR(50), -- commission, service_fee
    created_at DATETIME DEFAULT GETDATE()
)
GO


CREATE TABLE ShipperFees (
    fee_id INT IDENTITY PRIMARY KEY,
    shipper_id INT FOREIGN KEY REFERENCES Shippers(shipper_id), -- Sửa lại đúng bảng Shippers
    order_id INT FOREIGN KEY REFERENCES Orders(order_id),
    fee_amount DECIMAL(10,2),
    fee_type NVARCHAR(50), -- delivery_fee, bonus
    created_at DATETIME DEFAULT GETDATE()
);


CREATE TABLE OrderNotifications (
    notification_id INT IDENTITY PRIMARY KEY,
    order_id INT FOREIGN KEY REFERENCES Orders(order_id),
    customer_id INT NULL FOREIGN KEY REFERENCES Customers(customer_id),
    shipper_id INT NULL FOREIGN KEY REFERENCES Shippers(shipper_id),
    restaurant_id INT NULL FOREIGN KEY REFERENCES Restaurants(restaurant_id),
    message NVARCHAR(255),
    notification_type NVARCHAR(50), -- order_completed, order_cancelled
    is_read BIT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT chk_one_user_target CHECK (
        (customer_id IS NOT NULL AND shipper_id IS NULL AND restaurant_id IS NULL) OR
        (customer_id IS NULL AND shipper_id IS NOT NULL AND restaurant_id IS NULL) OR
        (customer_id IS NULL AND shipper_id IS NULL AND restaurant_id IS NOT NULL)
    )
);


-- =============================
-- 8. Insert dữ liệu mẫu
-- =============================

-- Trạng thái mẫu
INSERT INTO UserStatus (status_id, status_name, description) VALUES
(0, 'not_verified', 'Account has not been verified via email'),
(1, 'active', 'Account is active'),
(2, 'pending_approval', 'Account is awaiting approval (Shipper/Restaurant)'),
(3, 'banned', 'Account is banned'),
(4, 'online', 'Shipper is online'),
(5, 'offline', 'Shipper is offline'),
(6, 'delivering', 'Shipper is delivering');

-- Admin mặc định
INSERT INTO SystemAdmins (name, email, password) VALUES
('Admin', 'admin@foodship.com', '$2a$12$Xx8Y8z9Y8z9Y8z9Y8z9Y8.O8z9Y8z9Y8z9Y8z9Y8z9Y8z9Y8z9Y8z');
                                 

-- =============================
-- ✅ Database FoodShip hoàn tất!
-- =============================


select*from [dbo].[Shippers]
select*from [dbo].[Restaurants]
select *from [dbo].[Customers]
select *from [dbo].[SystemAdmins]


-- File: sample_data.sql

-- Insert Admin
INSERT INTO SystemAdmins (name, email, password, created_at)
VALUES ('Admin', 'admin@foodship.com',
        '$2a$12$U5uOJplL2KzR8P1/PL0F6OlGEB6R2UAKxMTpcE8MCw2AYTZW6j2cK', -- password = admin123
        GETDATE());

-- Insert Shippers
INSERT INTO Shippers (name, email, phone, password, cccd, driver_license, address, status_id, created_at)
VALUES 
('Lê Văn A', 'shipper1@gmail.com', '0901234567', '$2a$12$7z6WJm57Kztg1zUtTwf35uTLfyfzrk/ovl6cF2lFc6Jsf.3MqxMZ2', '0011223344', '111222333', 'Ha Noi', 1, GETDATE()),
('Nguyễn B', 'shipper2@gmail.com', '0902345678', '$2a$12$7z6WJm57Kztg1zUtTwf35uTLfyfzrk/ovl6cF2lFc6Jsf.3MqxMZ2', '0022334455', '222333444', 'Sai Gon', 0, GETDATE());

-- Insert Restaurants
INSERT INTO Restaurants (name, email, phone, password, address, status_id, created_at)
VALUES
('Pho 24', 'restaurant1@gmail.com', '0903456789', '$2a$12$7z6WJm57Kztg1zUtTwf35uTLfyfzrk/ovl6cF2lFc6Jsf.3MqxMZ2', 'Da Nang', 1, GETDATE()),
('Quan Com Tam', 'restaurant2@gmail.com', '0904567890', '$2a$12$7z6WJm57Kztg1zUtTwf35uTLfyfzrk/ovl6cF2lFc6Jsf.3MqxMZ2', 'Can Tho', 0, GETDATE());

-- Insert Customers
INSERT INTO Customers (name, email, phone, password, address, status_id, created_at)
VALUES
('Tran Van A', 'customer1@gmail.com', '0911234567', '$2a$12$7z6WJm57Kztg1zUtTwf35uTLfyfzrk/ovl6cF2lFc6Jsf.3MqxMZ2', 'Ha Noi', 1, GETDATE()),
('Nguyen Thi B', 'customer2@gmail.com', '0912345678', '$2a$12$7z6WJm57Kztg1zUtTwf35uTLfyfzrk/ovl6cF2lFc6Jsf.3MqxMZ2', 'Hue', 1, GETDATE()),
('Pham C', 'customer3@gmail.com', '0913456789', '$2a$12$7z6WJm57Kztg1zUtTwf35uTLfyfzrk/ovl6cF2lFc6Jsf.3MqxMZ2', 'Sai Gon', 0, GETDATE());

-- Ghi chú:
-- Password mã hóa là: 12345678
-- Status_id: 1 = Active, 0 = Pending, 3 = Banned
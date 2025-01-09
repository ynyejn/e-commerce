INSERT INTO `user` (id, name, created_at, updated_at)
VALUES (1, '테스트유저', NOW(), NOW());

INSERT INTO point (id, user_id, point, created_at, updated_at)
VALUES (1, 1, 100000, NOW(), NOW());

INSERT INTO product (id, name, price, created_at, updated_at)
VALUES (1, '테스트상품1', 10000, NOW(), NOW());
INSERT INTO product (id, name, price, created_at, updated_at)
VALUES (2, '테스트상품2', 15000, NOW(), NOW());

INSERT INTO product_stock (id, product_id, quantity, created_at, updated_at)
VALUES (1, 1, 100, NOW(), NOW());

INSERT INTO `order` (id, user_id, coupon_id, order_no, status, item_amount, shipping_amount, total_amount, discount_amount, payment_amount, created_at, updated_at)
VALUES
    (1, 1, NULL, '202501091200000001', 'PENDING', 50000.00, 0.00, 50000.00, 0.00, 50000.00, NOW(), NOW());
INSERT INTO order_item (id, order_id, product_id, order_item_name, order_price, quantity, created_at, updated_at)
VALUES
    (1, 1, 1, '테스트상품1', 20000.00, 2, NOW(), NOW()),
    (2, 1, 2, '테스트상품2', 15000.00, 1, NOW(), NOW());

INSERT INTO coupon (id, name, discount_type, discount_value, minimum_order_amount, issue_start_at, issue_end_at, validity_period, total_issue_quantity, issued_quantity, created_at, updated_at)
VALUES
    (1, '신규회원 할인', 'PERCENTAGE', 10.00, 30000.00, '2025-01-01 00:00:00', '2025-12-31 23:59:59', 30, 1000, 0, NOW(), NOW()),
    (2, '정액 할인 쿠폰', 'FIXED', 5000.00, NULL, '2025-01-01 00:00:00', '2025-06-30 23:59:59', 60, NULL, 0, NOW(), NOW());
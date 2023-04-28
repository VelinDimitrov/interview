DROP TABLE IF EXISTS customer_data;

CREATE TABLE customer_data
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     UUID default random_uuid(),
    given_name  VARCHAR(128) NOT NULL,
    family_name VARCHAR(128) NOT NULL,
    email       VARCHAR(128) NOT NULL
);



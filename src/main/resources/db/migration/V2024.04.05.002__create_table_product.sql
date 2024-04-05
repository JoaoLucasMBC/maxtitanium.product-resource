CREATE TABLE product
(
    id_product character varying(36) NOT NULL,
    tx_name character varying(256) NOT NULL,
    nr_price DECIMAL(10, 2) NOT NULL,
    tx_description character varying(256) NOT NULL,
    tx_category character varying(256) NOT NULL,
    tx_brand character varying(256) NOT NULL,
    nr_stock INTEGER NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id_product)
);
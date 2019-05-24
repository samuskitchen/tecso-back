CREATE TABLE "legal_person"(
    id serial NOT NULL,
    business_name character varying(100) NOT NULL,
    foundation_year integer NOT NULL,
    rut character varying(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE "physical_person"(
    id serial NOT NULL,
    name character varying(80) NOT NULL,
    surname character varying(250) NOT NULL,
    document_type character varying(2) NOT NULL,
    number_document character varying(20) NOT NULL,
    rut character varying(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE "account"(
    id serial NOT NULL,
    number_account character varying(20) NOT NULL,
    currency character varying(10) NOT NULL,
    balance numeric(16, 2),
    type_account character varying(10) NOT NULL ,
    fk_person_legal serial,
    fk_person_physical serial,
    PRIMARY KEY (id)
);

CREATE TABLE "movements"(
    id serial NOT NULL,
    date time with time zone NOT NULL,
    type_movement character varying(10) NOT NULL,
    description character varying(200) NOT NULL,
    amount numeric(16, 2) NOT NULL,
    fk_id_account serial NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE "users"(
    user_id serial NOT NULL,
    is_active boolean NOT NULL,
    email character varying(255) NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    is_email_verified boolean NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE "role"(
    role_id serial NOT NULL,
    role_name character varying(255) NOT NULL,
    PRIMARY KEY (role_id)
);

CREATE TABLE "user_authority"(
    user_id serial NOT NULL,
    role_id serial NOT NULL
);

CREATE TABLE "user_device"(
    user_device_id serial NOT NULL,
    device_id character varying(255) NOT NULL,
    device_type character varying(255) NOT NULL,
    is_refresh_active boolean NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    fk_user_id serial NOT NULL,
    PRIMARY KEY (user_device_id)
);

CREATE TABLE "email_verification_token"(
    token_id serial NOT NULL,
    token character varying(255) NOT NULL,
    token_status character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    expiry_dt timestamp without time zone NOT NULL,
    fk_user_id serial NOT NULL,
    PRIMARY KEY (token_id)
);

CREATE TABLE "refresh_token"(
    token_id serial NOT NULL,
    refresh_count serial NOT NULL,
    token character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    expiry_dt timestamp without time zone NOT NULL,
    fk_user_device_id serial NOT NULL,
    PRIMARY KEY (token_id)
);

ALTER TABLE "account" ADD CONSTRAINT fk_person_legal FOREIGN KEY (fk_person_legal) REFERENCES "legal_person" (id);
ALTER TABLE "account" ADD CONSTRAINT fk_person_physical FOREIGN KEY (fk_person_physical) REFERENCES "physical_person" (id);
ALTER TABLE "movements" ADD CONSTRAINT fk_account FOREIGN KEY (fk_id_account) REFERENCES "account" (id);
ALTER TABLE "legal_person" ADD CONSTRAINT uk_rut_legal_person UNIQUE (rut);
ALTER TABLE "physical_person" ADD CONSTRAINT uk_rut_physical_person UNIQUE (rut);
ALTER TABLE "users" ADD CONSTRAINT uk_username UNIQUE (username);
ALTER TABLE "users" ADD CONSTRAINT uk_password UNIQUE (password);
ALTER TABLE "role" ADD CONSTRAINT uk_role_name UNIQUE (role_name);
ALTER TABLE "user_authority" ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "users" (user_id);
ALTER TABLE "user_authority" ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES "role" (role_id);
ALTER TABLE "user_device" ADD CONSTRAINT fk_user FOREIGN KEY (fk_user_id) REFERENCES "users" (user_id);
ALTER TABLE "email_verification_token" ADD CONSTRAINT uk_token UNIQUE (token);
ALTER TABLE "email_verification_token" ADD CONSTRAINT fk_user FOREIGN KEY (fk_user_id) REFERENCES "users" (user_id);
ALTER TABLE "refresh_token" ADD CONSTRAINT uk_refresh_token UNIQUE (token);
ALTER TABLE "refresh_token" ADD CONSTRAINT fk_user_device FOREIGN KEY (fk_user_device_id) REFERENCES "user_device" (user_device_id);
ALTER TABLE "account" ALTER COLUMN fk_person_legal DROP NOT NULL;
ALTER TABLE "account" ALTER COLUMN fk_person_physical DROP NOT NULL;

CREATE INDEX idx_physical_person_rut ON "physical_person"(rut);
CREATE INDEX idx_legal_person_rut ON "legal_person"(rut);
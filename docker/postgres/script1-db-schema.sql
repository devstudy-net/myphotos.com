
CREATE FUNCTION update_rating() RETURNS void
    LANGUAGE plpgsql
AS
$$
DECLARE
BEGIN
    UPDATE profile
    SET rating=stat.rating
    FROM (SELECT profile_id, sum(views * 1 + downloads * 100) as rating FROM photo GROUP BY profile_id) AS stat
    WHERE profile.id = stat.profile_id;
END;
$$;

CREATE TABLE access_token(
    token      character varying                            NOT NULL,
    profile_id bigint                                       NOT NULL,
    created    timestamp(0) without time zone DEFAULT now() NOT NULL
)
WITH (oids = false);

CREATE TABLE photo(
    id           bigint                                       NOT NULL,
    profile_id   bigint                                       NOT NULL,
    small_url    character varying(255)                       NOT NULL,
    large_url    character varying(255)                       NOT NULL,
    original_url character varying(255)                       NOT NULL,
    views        bigint                         DEFAULT 0     NOT NULL,
    downloads    bigint                         DEFAULT 0     NOT NULL,
    created      timestamp(0) without time zone DEFAULT now() NOT NULL
)
WITH (oids = false);

CREATE TABLE profile(
    id          bigint                                       NOT NULL,
    uid         character varying(255)                       NOT NULL,
    email       character varying(100)                       NOT NULL,
    first_name  character varying(60)                        NOT NULL,
    last_name   character varying(60)                        NOT NULL,
    avatar_url  character varying(255)                       NOT NULL,
    job_title   character varying(100)                       NOT NULL,
    location    character varying(100)                       NOT NULL,
    photo_count integer                        DEFAULT 0     NOT NULL,
    created     timestamp(0) without time zone DEFAULT now() NOT NULL,
    rating      integer                        DEFAULT 0     NOT NULL
)
WITH (oids = false);

CREATE SEQUENCE profile_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE photo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SELECT setval('photo_seq', 1, false);

SELECT setval('profile_seq', 1, false);

ALTER TABLE ONLY access_token
    ADD CONSTRAINT access_token_pkey PRIMARY KEY (token);

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (id);

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_email_key UNIQUE (email);

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (id);

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_uid_key UNIQUE (uid);

ALTER TABLE ONLY access_token
    ADD CONSTRAINT access_token_fk FOREIGN KEY (profile_id) REFERENCES profile (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_fk FOREIGN KEY (profile_id) REFERENCES profile (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

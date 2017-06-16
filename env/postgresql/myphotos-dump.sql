--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: update_rating(); Type: FUNCTION; Schema: public; Owner: myphotos
--

CREATE FUNCTION update_rating() RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
BEGIN
  UPDATE profile SET rating=stat.rating FROM 
  	(SELECT profile_id, sum(views * 1 + downloads * 100) as rating FROM photo GROUP BY profile_id) AS stat 
  WHERE profile.id=stat.profile_id; 
END;
$$;


ALTER FUNCTION public.update_rating() OWNER TO myphotos;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: access_token; Type: TABLE; Schema: public; Owner: myphotos
--

CREATE TABLE access_token (
    token character varying NOT NULL,
    profile_id bigint NOT NULL,
    created timestamp(0) without time zone DEFAULT now() NOT NULL
);


ALTER TABLE access_token OWNER TO myphotos;

--
-- Name: photo; Type: TABLE; Schema: public; Owner: myphotos
--

CREATE TABLE photo (
    id bigint NOT NULL,
    profile_id bigint NOT NULL,
    small_url character varying(255) NOT NULL,
    large_url character varying(255) NOT NULL,
    original_url character varying(255) NOT NULL,
    views bigint DEFAULT 0 NOT NULL,
    downloads bigint DEFAULT 0 NOT NULL,
    created timestamp(0) without time zone DEFAULT now() NOT NULL
);


ALTER TABLE photo OWNER TO myphotos;

--
-- Name: photo_seq; Type: SEQUENCE; Schema: public; Owner: myphotos
--

CREATE SEQUENCE photo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE photo_seq OWNER TO myphotos;

--
-- Name: profile; Type: TABLE; Schema: public; Owner: myphotos
--

CREATE TABLE profile (
    id bigint NOT NULL,
    uid character varying(255) NOT NULL,
    email character varying(100) NOT NULL,
    first_name character varying(60) NOT NULL,
    last_name character varying(60) NOT NULL,
    avatar_url character varying(255) NOT NULL,
    job_title character varying(100) NOT NULL,
    location character varying(100) NOT NULL,
    photo_count integer DEFAULT 0 NOT NULL,
    created timestamp(0) without time zone DEFAULT now() NOT NULL,
    rating integer DEFAULT 0 NOT NULL
);


ALTER TABLE profile OWNER TO myphotos;

--
-- Name: profile_seq; Type: SEQUENCE; Schema: public; Owner: myphotos
--

CREATE SEQUENCE profile_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE profile_seq OWNER TO myphotos;

--
-- Data for Name: access_token; Type: TABLE DATA; Schema: public; Owner: myphotos
--

COPY access_token (token, profile_id, created) FROM stdin;
\.


--
-- Data for Name: photo; Type: TABLE DATA; Schema: public; Owner: myphotos
--

COPY photo (id, profile_id, small_url, large_url, original_url, views, downloads, created) FROM stdin;
\.


--
-- Name: photo_seq; Type: SEQUENCE SET; Schema: public; Owner: myphotos
--

SELECT pg_catalog.setval('photo_seq', 1, false);


--
-- Data for Name: profile; Type: TABLE DATA; Schema: public; Owner: myphotos
--

COPY profile (id, uid, email, first_name, last_name, avatar_url, job_title, location, photo_count, created, rating) FROM stdin;
\.


--
-- Name: profile_seq; Type: SEQUENCE SET; Schema: public; Owner: myphotos
--

SELECT pg_catalog.setval('profile_seq', 1, false);


--
-- Name: access_token access_token_pkey; Type: CONSTRAINT; Schema: public; Owner: myphotos
--

ALTER TABLE ONLY access_token
    ADD CONSTRAINT access_token_pkey PRIMARY KEY (token);


--
-- Name: photo photo_pkey; Type: CONSTRAINT; Schema: public; Owner: myphotos
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (id);


--
-- Name: profile profile_email_key; Type: CONSTRAINT; Schema: public; Owner: myphotos
--

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_email_key UNIQUE (email);


--
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: public; Owner: myphotos
--

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (id);


--
-- Name: profile profile_uid_key; Type: CONSTRAINT; Schema: public; Owner: myphotos
--

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_uid_key UNIQUE (uid);


--
-- Name: access_token access_token_fk; Type: FK CONSTRAINT; Schema: public; Owner: myphotos
--

ALTER TABLE ONLY access_token
    ADD CONSTRAINT access_token_fk FOREIGN KEY (profile_id) REFERENCES profile(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: photo photo_fk; Type: FK CONSTRAINT; Schema: public; Owner: myphotos
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_fk FOREIGN KEY (profile_id) REFERENCES profile(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--


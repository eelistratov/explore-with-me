-- =========================================================
-- Таблица пользователей
-- =========================================================
CREATE TABLE IF NOT EXISTS users (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL,
    CONSTRAINT uq_user_email UNIQUE (email)
);

-- =========================================================
-- Таблица категорий
-- =========================================================
CREATE TABLE IF NOT EXISTS categories (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
);

-- =========================================================
-- Таблица событий
-- =========================================================
CREATE TABLE IF NOT EXISTS events (
    id                  BIGSERIAL PRIMARY KEY,
    annotation          VARCHAR(2000) NOT NULL,
    description         VARCHAR(7000) NOT NULL,
    title               VARCHAR(120)  NOT NULL,
    category_id         BIGINT        NOT NULL,
    initiator_id        BIGINT        NOT NULL,
    event_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_on          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on        TIMESTAMP WITHOUT TIME ZONE,
    paid                BOOLEAN       NOT NULL DEFAULT FALSE,
    participant_limit   INTEGER       NOT NULL DEFAULT 0,
    request_moderation  BOOLEAN       NOT NULL DEFAULT TRUE,
    state               VARCHAR(20)   NOT NULL,
    lat                 FLOAT         NOT NULL,
    lon                 FLOAT         NOT NULL,
    CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_event_initiator FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_events_state ON events (state);
CREATE INDEX IF NOT EXISTS idx_events_event_date ON events (event_date);
CREATE INDEX IF NOT EXISTS idx_events_category ON events (category_id);
CREATE INDEX IF NOT EXISTS idx_events_initiator ON events (initiator_id);

-- =========================================================
-- Таблица заявок на участие
-- =========================================================
CREATE TABLE IF NOT EXISTS participation_requests (
    id           BIGSERIAL PRIMARY KEY,
    event_id     BIGINT      NOT NULL,
    requester_id BIGINT      NOT NULL,
    status       VARCHAR(20) NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_request_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_request_requester FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT uq_request_event_requester UNIQUE (event_id, requester_id)
);

CREATE INDEX IF NOT EXISTS idx_requests_event ON participation_requests (event_id);
CREATE INDEX IF NOT EXISTS idx_requests_requester ON participation_requests (requester_id);

-- =========================================================
-- Таблица подборок событий
-- =========================================================
CREATE TABLE IF NOT EXISTS compilations (
    id     BIGSERIAL PRIMARY KEY,
    title  VARCHAR(50) NOT NULL,
    pinned BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_compilation_title UNIQUE (title)
);

-- =========================================================
-- Связующая таблица подборок и событий (many-to-many)
-- =========================================================
CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_compilation_events_compilation FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilation_events_event FOREIGN KEY (event_id) REFERENCES events (id)
);
-- =========================================================
-- Таблица комментариев к событиям
-- =========================================================
CREATE TABLE IF NOT EXISTS comments (
    id         BIGSERIAL PRIMARY KEY,
    text       VARCHAR(2000) NOT NULL,
    event_id   BIGINT        NOT NULL,
    author_id  BIGINT        NOT NULL,
    created    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated    TIMESTAMP WITHOUT TIME ZONE,
    status     VARCHAR(20)   NOT NULL,
    CONSTRAINT fk_comment_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_comments_event ON comments (event_id);
CREATE INDEX IF NOT EXISTS idx_comments_author ON comments (author_id);
CREATE INDEX IF NOT EXISTS idx_comments_status ON comments (status);
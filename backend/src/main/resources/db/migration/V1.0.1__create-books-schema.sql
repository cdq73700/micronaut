DROP TABLE IF EXISTS books;

CREATE TABLE books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(32) NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(32) NOT NULL,
    deleted_at TIMESTAMPTZ DEFAULT NULL,
    deleted_by VARCHAR(32) DEFAULT NULL
);
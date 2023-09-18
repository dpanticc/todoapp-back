-- src/main/resources/db/migration/V2__Add_Relationship_Between_Tables.sql

ALTER TABLE tasks
ADD COLUMN user_id BIGINT,
ADD CONSTRAINT fk_user_id
    FOREIGN KEY (user_id)
    REFERENCES users (id);

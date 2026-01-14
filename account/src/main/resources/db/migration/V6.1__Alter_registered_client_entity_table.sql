ALTER TABLE registered_client_entity
DROP COLUMN client_settings;

ALTER TABLE registered_client_entity
DROP COLUMN token_settings;

ALTER TABLE registered_client_entity
ADD COLUMN refresh_token_time_to_live INT;

ALTER TABLE registered_client_entity
ADD COLUMN access_token_time_to_live INT;

ALTER TABLE registered_client_entity
ADD COLUMN require_authorization_consent INT;

ALTER TABLE registered_client_entity
ADD COLUMN require_proof_key INT;
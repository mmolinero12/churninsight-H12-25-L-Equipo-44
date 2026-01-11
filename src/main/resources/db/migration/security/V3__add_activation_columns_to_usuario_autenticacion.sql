ALTER TABLE public.tbl_usuario_autenticacion
ADD COLUMN activation_token VARCHAR(255),
ADD COLUMN activation_token_expiration TIMESTAMP;
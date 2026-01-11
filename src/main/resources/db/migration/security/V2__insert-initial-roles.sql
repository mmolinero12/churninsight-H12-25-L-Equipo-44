-- Insertar roles básicos
INSERT INTO tbl_rol (nombre_rol) VALUES
    ('ROLE_USER'),
    ('ROLE_ANALYST'),
    ('ROLE_ADMIN')
ON CONFLICT DO NOTHING;
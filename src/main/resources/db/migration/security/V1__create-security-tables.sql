-- Tabla tbl_rol
CREATE TABLE IF NOT EXISTS tbl_rol (
    id BIGSERIAL PRIMARY KEY,   -- AUTO_INCREMENT implícito con BIGSERIAL
    nombre_rol VARCHAR(20) NOT NULL
);

-- Tabla tbl_usuario_autenticacion
CREATE TABLE IF NOT EXISTS tbl_usuario_autenticacion (
    id BIGSERIAL PRIMARY KEY,   -- AUTO_INCREMENT implícito con BIGSERIAL
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    password_temporal BOOLEAN NOT NULL DEFAULT false,
    password_expiration TIMESTAMP NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    locked BOOLEAN NOT NULL DEFAULT false,
    usuario_id BIGINT NOT NULL
);

-- Tabla tbl_usuario_rol
CREATE TABLE IF NOT EXISTS tbl_usuario_rol (
    id BIGSERIAL PRIMARY KEY,   -- AUTO_INCREMENT implícito con BIGSERIAL
    id_usuario BIGINT NOT NULL,
    id_rol BIGINT NOT NULL,
    CONSTRAINT uk_usuario_rol UNIQUE (id_usuario, id_rol),
    CONSTRAINT fk_usuario_rol_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES tbl_usuario_autenticacion(id),
    CONSTRAINT fk_usuario_rol_rol
        FOREIGN KEY (id_rol)
        REFERENCES tbl_rol(id)
);
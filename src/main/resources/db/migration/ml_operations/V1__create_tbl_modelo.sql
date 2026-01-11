CREATE TABLE tbl_modelo (
    id BIGSERIAL PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,
    version VARCHAR(50) NOT NULL,
    descripcion TEXT,

    estado VARCHAR(20) NOT NULL,

    soporta_individual BOOLEAN NOT NULL DEFAULT TRUE,
    soporta_batch BOOLEAN NOT NULL DEFAULT FALSE,

    endpoint_individual TEXT,
    endpoint_batch TEXT,

    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE tbl_solicitud_prediccion_batch (
    id BIGSERIAL PRIMARY KEY,

    id_modelo BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,

    descripcion TEXT,

    estado VARCHAR(20) NOT NULL,

    input_csv BYTEA NOT NULL,
    input_csv_name VARCHAR(255) NOT NULL,

    fecha_solicitud TIMESTAMPTZ NOT NULL DEFAULT now(),
    fecha_respuesta TIMESTAMPTZ,
    mensaje_error TEXT,

    CONSTRAINT fk_batch_modelo
        FOREIGN KEY (id_modelo)
        REFERENCES tbl_modelo (id)
);
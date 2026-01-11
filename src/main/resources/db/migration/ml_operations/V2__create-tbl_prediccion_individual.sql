CREATE TABLE tbl_solicitud_prediccion_individual (
    id BIGSERIAL PRIMARY KEY,

    id_modelo BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,

    descripcion TEXT,

    estado VARCHAR(20) NOT NULL,

    request_json JSONB NOT NULL,

    fecha_solicitud TIMESTAMPTZ NOT NULL DEFAULT now(),
    fecha_respuesta TIMESTAMPTZ,
    mensaje_error TEXT,

    CONSTRAINT fk_individual_modelo
        FOREIGN KEY (id_modelo)
        REFERENCES tbl_modelo (id)
);
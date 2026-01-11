CREATE TABLE tbl_archivo_resultado_batch (
    id BIGSERIAL PRIMARY KEY,

    id_solicitud_batch BIGINT NOT NULL UNIQUE,

    resultado_csv BYTEA NOT NULL,
    resultado_csv_name VARCHAR(255) NOT NULL,

    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_archivo_batch
        FOREIGN KEY (id_solicitud_batch)
        REFERENCES tbl_solicitud_prediccion_batch (id)
);
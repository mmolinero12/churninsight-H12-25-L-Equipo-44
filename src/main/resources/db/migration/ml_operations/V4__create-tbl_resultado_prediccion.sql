CREATE TABLE tbl_resultado_prediccion (
    id BIGSERIAL PRIMARY KEY,

    id_solicitud_individual BIGINT,
    id_solicitud_batch BIGINT,

    resultado_json JSONB NOT NULL,

    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_resultado_individual
        FOREIGN KEY (id_solicitud_individual)
        REFERENCES tbl_solicitud_prediccion_individual (id),

    CONSTRAINT fk_resultado_batch
        FOREIGN KEY (id_solicitud_batch)
        REFERENCES tbl_solicitud_prediccion_batch (id),

    CONSTRAINT chk_resultado_origen
        CHECK (
            (id_solicitud_individual IS NOT NULL AND id_solicitud_batch IS NULL)
         OR (id_solicitud_individual IS NULL AND id_solicitud_batch IS NOT NULL)
        )
);
-- Indexes para Modelos

CREATE INDEX IF NOT EXISTS idx_modelo_estado
    ON tbl_modelo (estado);

CREATE UNIQUE INDEX IF NOT EXISTS idx_modelo_nombre_version
    ON tbl_modelo (nombre, version);


-- Indexes para Predicciones
CREATE INDEX IF NOT EXISTS idx_individual_estado
    ON tbl_solicitud_prediccion_individual (estado);

CREATE INDEX IF NOT EXISTS idx_batch_estado
    ON tbl_solicitud_prediccion_batch (estado);

CREATE INDEX IF NOT EXISTS idx_resultado_batch
    ON tbl_resultado_prediccion (id_solicitud_batch);

CREATE INDEX IF NOT EXISTS idx_resultado_individual
    ON tbl_resultado_prediccion (id_solicitud_individual);
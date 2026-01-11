-- V2__alter-table-tbl_usuario-add-fecha_ingreso.sql
ALTER TABLE tbl_usuario
ADD COLUMN fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE;

-- COMMENT ON COLUMN
-- En pg_description (tabla de metadatos del sistema).
-- Visible en pgAdmin (propiedades de la columna).
-- Consultable con:
-- sql
--
-- SELECT description
-- FROM pg_description
-- WHERE objoid = 'tbl_usuario'::regclass::oid;

COMMENT ON COLUMN tbl_usuario.fecha_ingreso IS 'Fecha de registro del usuario en el sistema';
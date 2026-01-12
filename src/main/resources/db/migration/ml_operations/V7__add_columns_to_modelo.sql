ALTER TABLE public.tbl_modelo
ADD COLUMN id_usuario BIGINT,
ADD COLUMN fecha_activacion TIMESTAMPTZ,
ADD COLUMN fecha_desactivacion TIMESTAMPTZ;

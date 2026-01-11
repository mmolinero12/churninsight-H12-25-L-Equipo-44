CREATE TABLE IF NOT EXISTS tbl_usuario (
    id BIGSERIAL PRIMARY KEY,   -- AUTO_INCREMENT implícito con BIGSERIAL
    nombre VARCHAR(50) NOT NULL,
    apellido_paterno VARCHAR(40) NOT NULL,
    apellido_materno VARCHAR(40) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    sexo CHAR(1),
    curp VARCHAR(30) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    calle_numero VARCHAR(70) NOT NULL,
    colonia VARCHAR(50) NOT NULL,
    alcaldia_municipio VARCHAR(50) NOT NULL,
    ciudad VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    codigo_postal CHAR(5) NOT NULL,

    -- Constraints de validación (opcionales pero profesionales)
        CONSTRAINT chk_sexo CHECK (sexo IN ('M', 'F', 'O')),
        CONSTRAINT chk_codigo_postal CHECK (codigo_postal ~ '^[0-9]{5}$')

);
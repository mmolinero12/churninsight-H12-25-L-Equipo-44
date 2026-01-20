# Guía para Backend

## Creación de Bases de Datos PostgreSQL

```
CREATE DATABASE db_usuarios
    ENCODING 'UTF8'
    LC_COLLATE 'es_LA.UTF-8'
    LC_CTYPE 'es_LA.UTF-8'
TEMPLATE template0;

CREATE DATABASE db_security
    ENCODING 'UTF8'
    LC_COLLATE 'es_LA.UTF-8'
    LC_CTYPE 'es_LA.UTF-8'
TEMPLATE template0;

CREATE DATABASE db_ml_operations
    ENCODING 'UTF8'
    LC_COLLATE 'es_LA.UTF-8'
    LC_CTYPE 'es_LA.UTF-8'
TEMPLATE template0;


```
## Pasos adicionales


Al ejecutar el progama Java se crearán las tablas gracias a Flyway.

Pero es importante crear al usuario Admin.

1. Generar Bcrypt entrando al siguiente portal:

https://bcrypt-generator.com/

2. Escribir la contraseña en el campo **Test to Hash**, dar click enel botón **Generate Hash**  y se generará la contraseña encriptada la cual deberá copiarse dando click en el botón **Copy Hash**

3. Estando en la base de datos db_security se ejecutará el siguiente script SQL para agregar el usuarioAutenticacion :

```
INSERT INTO tbl_usuario_autenticacion (
	username,
	password,
	password_temporal,
	password_expiration,
	enabled,
	locked,
	usuario_id,
	activation_token,
	activation_token_expiration
	)
VALUES (
	'yalimolinero@gmail.com',
	'$2a$12$WUQnz887oW8w6/5rycbZCuKVjggBQ0N8yoEHRdTLktyMDod4QPY.a',
	false,
	'2027-01-15',
	true,
	false,
	2,
	null,
	null
);

SELECT * FROM tbl_usuario_autenticacion;

```
4. Verificar que se haya registrado correctamente la inserción del nuevo registro:

5. Ahora procedemos a asignarle el rol al usuario_autenticacion. En el ejemplo que seguimos, yalimolinero@gmail.com se creó con el id 2, ese es su id_usuario. Para los roles, favor de considerar los siguientes:

|id|nombre_rol           |
|--|---------------------|
|1 |ROLE_USER            |
|2 |ROLE_ANALYST         |
|3 |ROLE_ADMIN           | 
|4 |ROLE_DATA_SCIENTIST  | 


```
INSERT INTO tbl_usuario_rol (
	id_usuario,
	id_rol	
	)
VALUES (
	1,
	3
);

SELECT * FROM tbl_usuario_rol;
```

6. Creación de registro del único modelo que se tiene.

Nota: la intención del porqué se cuenta con una tabla tbl_modelo es que se pueda ofrecer en el futuro más modelos, ya NO sería necesario escribir dicho código

```
INSERT INTO tbl_modelo (
    nombre,
    version,
    descripcion,
    estado,
    soporta_individual,
    soporta_batch,
    endpoint_individual,
    endpoint_batch,
    fecha_creacion,
    id_usuario,
    fecha_activacion,
    fecha_desactivacion)
    VALUES (
    'Modelo Forest Banco BBVA',
    '1.0', 
    'Modelo para proyecto ChurnInsight',
    'ACTIVO',
    true,
    true,
    'http://158.101.36.98:8000/predict',
    'http://158.101.36.98:8000/batch-predict',
    '2026-01-03 00:34:32.590539-06',
    1,    
    NULL,
    NULL
    );

```



## Variables de Ambiente

SECURITY_DB_HOST = localhost

SECURITY_DB_USER = postgres

SECURITY_DB_PASSWORD = Ard1ll1t4$ 


USUARIOS_DB_HOST = localhost

USUARIOS_DB_USER = postgres

USUARIOS_DB_PASSWORD = Ard1ll1t4$

MLOPERATIONS_DB_HOST = localhost

MLOPERATIONS_DB_USER = postgres

MLOPERATIONS_DB_PASSWORD = Ard1ll1t4$

URL_FRONTEND = 'https://churn-insight-ui.vercel.app' 

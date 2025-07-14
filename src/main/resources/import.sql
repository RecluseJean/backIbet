INSERT INTO rol(id_rol, tx_descripcion, is_habilitado) VALUES('1e7a4288-2ff8-4077-84e7-6d9adb75c0c1'::uuid, 'ROLE_ADMIN', true);
INSERT INTO rol(id_rol, tx_descripcion, is_habilitado) VALUES('0a08a32a-f1b7-4e24-ac21-e6fb19cde117'::uuid, 'ROLE_ENCARGADO', true);

INSERT INTO usuario(id_usuario, tx_username, tx_password, dt_registro, is_habilitado) VALUES('747f8f82-f915-45e9-92b6-5dfcc7bc435c'::uuid, 'master_registry', '$2a$12$x1WzZSvA6ZyjucBIYKVv1etGk176GGoe.9c/JSftbxcPbaizCQoxu', now(), true);

INSERT INTO usuarios_roles(id_rol, id_usuario) VALUES('1e7a4288-2ff8-4077-84e7-6d9adb75c0c1'::uuid, '747f8f82-f915-45e9-92b6-5dfcc7bc435c'::uuid);

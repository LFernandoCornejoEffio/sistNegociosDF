CREATE OR REPLACE VIEW vista_usuarios AS SELECT p.idPersona, p.nombre, p.apPaterno, p.apMaterno, p.tipoDocId, td.abrevTipoDoc, p.numDoc, p.telefono, p.direccion, p.estado, u.cargo, u.username, u.contrasenia, u.ultimo_acceso, u.primerAcceso FROM persona p INNER JOIN usuarios u ON p.idPersona = u.idUsuario INNER JOIN tipodoc td ON p.tipoDocId = td.idTipoDoc;
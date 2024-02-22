-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-02-2024 a las 14:36:40
-- Versión del servidor: 10.4.27-MariaDB
-- Versión de PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `dbnegociosdfd`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `buscarUsuarios` (IN `_campo` VARCHAR(30), IN `_busqueda` VARCHAR(100))   BEGIN
        IF _campo = 'Cargo' THEN
        	SELECT * FROM vista_empleados WHERE cargo LIKE concat('%',_busqueda,'%');
        ELSEIF _campo = 'Nombres' THEN
        	SELECT * FROM vista_empleados WHERE nombre LIKE concat('%',_busqueda,'%') OR apPaterno LIKE concat('%',_busqueda,'%');
        ELSE
        	SELECT * FROM vista_empleados WHERE numDoc LIKE concat('%',_busqueda,'%');
        END IF;    	
    END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarEmpleado` (IN `_nombre` VARCHAR(100), IN `_apPaterno` VARCHAR(100), IN `_apMaterno` VARCHAR(100), IN `_tipoDoc` INT, IN `_numDoc` VARCHAR(20), IN `_telefono` VARCHAR(20), IN `_direccion` VARCHAR(200), IN `_cargo` VARCHAR(20), IN `_username` VARCHAR(50), IN `_contrasenia` VARCHAR(250))   BEGIN
    DECLARE _idEmpleado int;
    DECLARE _existe int;
    SET _existe = (SELECT COUNT(p.idPersona) FROM persona AS p INNER JOIN empleado AS e ON p.idPersona = e.idEmpleado WHERE p.numDoc LIKE _numDoc AND e.cargo LIKE _cargo AND p.estado LIKE 'Activo');
    IF _existe = 0 THEN
    	INSERT INTO persona(nombre, apPaterno, apMaterno, tipoDocId, numDoc, telefono, direccion, fechaCreacion, estado) VALUES(_nombre, _apPaterno, _apMaterno, _tipoDoc, _numDoc, _telefono, _direccion, now(), 'Activo');    
    	SET _idEmpleado = LAST_INSERT_ID();
    	IF _idEmpleado > 0 THEN
            INSERT INTO empleado(idEmpleado, cargo, username, contrasenia, ultimo_acceso, primerAcceso)
            VALUES(_idEmpleado, _cargo, _username, _contrasenia, null, 'SI');
     	ELSE
            SET _idEmpleado = -1;
   		END IF;
	ELSE
    	SET _idEmpleado = 0;	
	END IF;
        SELECT _idEmpleado;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ultimoAcceso` (IN `_idEmpleado` INT)   BEGIN
    	UPDATE empleado SET ultimo_acceso = now() WHERE empleado.idEmpleado = _idEmpleado;
    END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `idCliente` int(11) NOT NULL,
  `ultimaCompra` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `idEmpleado` int(11) NOT NULL,
  `cargo` varchar(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `contrasenia` varchar(250) NOT NULL,
  `ultimo_acceso` datetime DEFAULT NULL,
  `primerAcceso` varchar(3) DEFAULT 'SI'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`idEmpleado`, `cargo`, `username`, `contrasenia`, `ultimo_acceso`, `primerAcceso`) VALUES
(16, 'Vendedor', 'LPCE1', 'c/6AYm3phsgkJ9JTuREeRrhAWEBaUVqFf+6RS7Yf4XY=', '2024-02-17 18:50:31', 'NO'),
(17, 'Admin', 'LPCE', 'XYeEFYwk1cU3vMraQod8+QvpubkMjVF/R1iIYQIVFFY=', '2024-02-20 19:44:38', 'NO'),
(18, 'Administrador', 'LACF', '8cpMb9bPtnfCcLm19TKRfZjaoBVNtCRcgGb/iJle6Pg=', NULL, 'SI'),
(19, 'Administrador', 'MlEC', 'jZkYOku6Jiv6vFlJ37NEt+U/VwO2l4But6uWC/WH7pE=', NULL, 'SI');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `idPersona` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apPaterno` varchar(100) NOT NULL,
  `apMaterno` varchar(100) NOT NULL,
  `tipoDocId` int(11) DEFAULT NULL,
  `numDoc` varchar(20) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `direccion` varchar(200) NOT NULL,
  `fechaCreacion` datetime NOT NULL,
  `estado` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`idPersona`, `nombre`, `apPaterno`, `apMaterno`, `tipoDocId`, `numDoc`, `telefono`, `direccion`, `fechaCreacion`, `estado`) VALUES
(16, 'Luis Per', 'Cor', 'Eff', 1, '111a1145sd', '000', 'ds', '2024-02-16 17:20:01', 'Activo'),
(17, 'Luis Pa', 'Cor', 'Eff', 1, '111a1145', '000', 'ds', '2024-02-16 19:20:38', 'Activo'),
(18, 'Luis Alberto', 'Cornejo', 'Flores', 1, '1666666', '000', 'ds', '2024-02-20 19:51:25', 'Activo'),
(19, 'Maria lidia', 'Efio', 'Chafloque', 1, '0012301', '000', 'ds', '2024-02-20 19:54:02', 'Activo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `idProveedor` int(11) NOT NULL,
  `tipoDoc` int(11) NOT NULL,
  `numDocProveedor` varchar(20) NOT NULL,
  `razonSocial` varchar(200) NOT NULL,
  `direccion` varchar(150) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `estado` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipodoc`
--

CREATE TABLE `tipodoc` (
  `idTipoDoc` int(11) NOT NULL,
  `nombreTipoDoc` varchar(120) NOT NULL,
  `abrevTipoDoc` varchar(15) NOT NULL,
  `estadoTipoDoc` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `tipodoc`
--

INSERT INTO `tipodoc` (`idTipoDoc`, `nombreTipoDoc`, `abrevTipoDoc`, `estadoTipoDoc`) VALUES
(1, 'Documento Nacional de Identidad', 'DNI', 'Activo');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_empleados`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_empleados` (
`idEmpleado` int(11)
,`cargo` varchar(20)
,`ultimo_acceso` datetime
,`username` varchar(50)
,`nombre` varchar(100)
,`apPaterno` varchar(100)
,`apMaterno` varchar(100)
,`tipoDocId` int(11)
,`numDoc` varchar(20)
,`telefono` varchar(20)
,`direccion` varchar(200)
,`abrevTipoDoc` varchar(15)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_empleados`
--
DROP TABLE IF EXISTS `vista_empleados`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_empleados`  AS SELECT `e`.`idEmpleado` AS `idEmpleado`, `e`.`cargo` AS `cargo`, `e`.`ultimo_acceso` AS `ultimo_acceso`, `e`.`username` AS `username`, `p`.`nombre` AS `nombre`, `p`.`apPaterno` AS `apPaterno`, `p`.`apMaterno` AS `apMaterno`, `p`.`tipoDocId` AS `tipoDocId`, `p`.`numDoc` AS `numDoc`, `p`.`telefono` AS `telefono`, `p`.`direccion` AS `direccion`, `t`.`abrevTipoDoc` AS `abrevTipoDoc` FROM ((`empleado` `e` join `persona` `p` on(`e`.`idEmpleado` = `p`.`idPersona`)) join `tipodoc` `t` on(`p`.`tipoDocId` = `t`.`idTipoDoc`))  ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`idCliente`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`idEmpleado`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`idPersona`),
  ADD KEY `fk_persona_tipoDoc` (`tipoDocId`);

--
-- Indices de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`idProveedor`),
  ADD KEY `fk_proveedor_tipoDoc` (`tipoDoc`);

--
-- Indices de la tabla `tipodoc`
--
ALTER TABLE `tipodoc`
  ADD PRIMARY KEY (`idTipoDoc`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `idPersona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `idProveedor` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tipodoc`
--
ALTER TABLE `tipodoc`
  MODIFY `idTipoDoc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `fk_persona_cliente` FOREIGN KEY (`idCliente`) REFERENCES `persona` (`idPersona`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `fk_persona_empleado` FOREIGN KEY (`idEmpleado`) REFERENCES `persona` (`idPersona`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `persona`
--
ALTER TABLE `persona`
  ADD CONSTRAINT `fk_persona_tipoDoc` FOREIGN KEY (`tipoDocId`) REFERENCES `tipodoc` (`idTipoDoc`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Filtros para la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD CONSTRAINT `fk_proveedor_tipoDoc` FOREIGN KEY (`tipoDoc`) REFERENCES `tipodoc` (`idTipoDoc`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

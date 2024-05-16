-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 08-04-2024 a las 16:42:11
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

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

CREATE DEFINER=`root`@`localhost` PROCEDURE `editarEmpleado` (IN `_idPersona` INT, IN `_nombre` VARCHAR(100), IN `_apPaterno` VARCHAR(100), IN `_apMaterno` VARCHAR(100), IN `_tipoDoc` INT, IN `_numDoc` VARCHAR(20), IN `_telefono` VARCHAR(20), IN `_direccion` VARCHAR(150), IN `_cargo` VARCHAR(20))   BEGIN
	UPDATE persona AS p SET p.nombre = _nombre, p.apPaterno = _apPaterno, p.apMaterno = _apMaterno, p.tipoDocId = _tipoDoc, p.numDoc = _numDoc, p.telefono = _telefono, p.direccion = _direccion
    WHERE p.idPersona = _idPersona;
    UPDATE empleado AS e SET e.cargo = _cargo WHERE e.idEmpleado = _idPersona;
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
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `idCategoria` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(256) NOT NULL,
  `estado` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

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
-- Estructura de tabla para la tabla `compra`
--

CREATE TABLE `compra` (
  `idCompra` int(11) NOT NULL,
  `idProveedor` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `tipoComprobante` varchar(20) NOT NULL,
  `serieComprobante` varchar(7) NOT NULL,
  `numComprobante` varchar(10) NOT NULL,
  `fechaCompra` datetime NOT NULL,
  `impuesto` decimal(4,2) NOT NULL,
  `totalCompra` decimal(11,2) NOT NULL,
  `estado` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallecompra`
--

CREATE TABLE `detallecompra` (
  `idDetalleCompra` int(11) NOT NULL,
  `idCompra` int(11) NOT NULL,
  `idProducto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precioCompra` decimal(11,2) NOT NULL,
  `precioVenta` decimal(11,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalleventa`
--

CREATE TABLE `detalleventa` (
  `idDetalleVenta` int(11) NOT NULL,
  `idVenta` int(11) NOT NULL,
  `idProducto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precioVenta` decimal(11,2) NOT NULL,
  `descuento` decimal(11,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

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
(16, 'VENDEDOR', 'LPCE1', 'c/6AYm3phsgkJ9JTuREeRrhAWEBaUVqFf+6RS7Yf4XY=', '2024-04-07 12:30:20', 'NO'),
(17, 'ADMINISTRADOR', 'LPCE', 'XYeEFYwk1cU3vMraQod8+QvpubkMjVF/R1iIYQIVFFY=', '2024-04-08 08:59:52', 'NO'),
(18, 'ALMACEN', 'LACF', '8cpMb9bPtnfCcLm19TKRfZjaoBVNtCRcgGb/iJle6Pg=', NULL, 'SI'),
(20, 'ADMINISTRADOR', 'LFCE', '1vwYAG0mSDKKq6rM17sZarQ3VE02qr7UJMDjtudI+VY=', '2024-04-07 18:23:02', 'NO');

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
(17, 'LUIS PA', 'COR', 'EFF', 1, '111A1145', '000', 'DS', '2024-02-16 19:20:38', 'Activo'),
(18, 'LUIS ALBERTO', 'CORNEJO', 'FLORES', 1, '1666666', '000', 'DS', '2024-02-20 19:51:25', 'Activo'),
(20, 'LUIS FERNANDO', 'CORNEJO', 'EFFIO', 1, '12345678', '123456', 'CEN 000', '2024-04-07 12:39:54', 'Activo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `idProducto` int(11) NOT NULL,
  `idCategoria` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `stock` int(11) NOT NULL,
  `descripcion` varchar(256) NOT NULL,
  `imagen` varchar(50) NOT NULL,
  `estado` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

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
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE `venta` (
  `idVenta` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `tipocomprobante` varchar(20) NOT NULL,
  `serieComprobante` varchar(7) NOT NULL,
  `numComprobante` varchar(10) NOT NULL,
  `fechaVenta` datetime NOT NULL,
  `impuesto` decimal(4,2) NOT NULL,
  `totalVenta` decimal(11,2) NOT NULL,
  `estado` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

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
-- Estructura Stand-in para la vista `vista_tipodoccombo`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_tipodoccombo` (
`idTipoDoc` int(11)
,`nombreTipoDoc` varchar(120)
,`abrevTipoDoc` varchar(15)
,`estadoTipoDoc` varchar(15)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_empleados`
--
DROP TABLE IF EXISTS `vista_empleados`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_empleados`  AS SELECT `e`.`idEmpleado` AS `idEmpleado`, `e`.`cargo` AS `cargo`, `e`.`ultimo_acceso` AS `ultimo_acceso`, `e`.`username` AS `username`, `p`.`nombre` AS `nombre`, `p`.`apPaterno` AS `apPaterno`, `p`.`apMaterno` AS `apMaterno`, `p`.`tipoDocId` AS `tipoDocId`, `p`.`numDoc` AS `numDoc`, `p`.`telefono` AS `telefono`, `p`.`direccion` AS `direccion`, `t`.`abrevTipoDoc` AS `abrevTipoDoc` FROM ((`empleado` `e` join `persona` `p` on(`e`.`idEmpleado` = `p`.`idPersona`)) join `tipodoc` `t` on(`p`.`tipoDocId` = `t`.`idTipoDoc`)) ;

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_tipodoccombo`
--
DROP TABLE IF EXISTS `vista_tipodoccombo`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_tipodoccombo`  AS SELECT `tipodoc`.`idTipoDoc` AS `idTipoDoc`, `tipodoc`.`nombreTipoDoc` AS `nombreTipoDoc`, `tipodoc`.`abrevTipoDoc` AS `abrevTipoDoc`, `tipodoc`.`estadoTipoDoc` AS `estadoTipoDoc` FROM `tipodoc` ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`idCategoria`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`idCliente`);

--
-- Indices de la tabla `compra`
--
ALTER TABLE `compra`
  ADD PRIMARY KEY (`idCompra`);

--
-- Indices de la tabla `detallecompra`
--
ALTER TABLE `detallecompra`
  ADD PRIMARY KEY (`idDetalleCompra`);

--
-- Indices de la tabla `detalleventa`
--
ALTER TABLE `detalleventa`
  ADD PRIMARY KEY (`idDetalleVenta`);

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
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`idProducto`);

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
-- Indices de la tabla `venta`
--
ALTER TABLE `venta`
  ADD PRIMARY KEY (`idVenta`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria`
--
ALTER TABLE `categoria`
  MODIFY `idCategoria` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `compra`
--
ALTER TABLE `compra`
  MODIFY `idCompra` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detallecompra`
--
ALTER TABLE `detallecompra`
  MODIFY `idDetalleCompra` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalleventa`
--
ALTER TABLE `detalleventa`
  MODIFY `idDetalleVenta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `idPersona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `idProducto` int(11) NOT NULL AUTO_INCREMENT;

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
-- AUTO_INCREMENT de la tabla `venta`
--
ALTER TABLE `venta`
  MODIFY `idVenta` int(11) NOT NULL AUTO_INCREMENT;

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

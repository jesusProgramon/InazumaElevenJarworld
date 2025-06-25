-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 19, 2025 at 06:13 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inazuma_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `equipos`
--

CREATE TABLE `equipos` (
  `ID` int(11) NOT NULL,
  `Nombre` varchar(255) NOT NULL,
  `Ciudad` varchar(255) NOT NULL,
  `País` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `equipos`
--

INSERT INTO `equipos` (`ID`, `Nombre`, `Ciudad`, `País`) VALUES
(1, 'Instituto Raimon', 'Ciudad Inazuma', 'Japón'),
(2, 'Royal Academy', 'Ciudad Inazuma', 'Japón'),
(3, 'Academia Alius', 'Tokio', 'Japón'),
(4, 'Raimon GO', 'Ciudad Inazuma', 'Japón');

-- --------------------------------------------------------

--
-- Table structure for table `jugadores`
--

CREATE TABLE `jugadores` (
  `ID` int(11) NOT NULL,
  `Nombre` varchar(255) NOT NULL,
  `ID_equipo` int(11) DEFAULT NULL,
  `Dorsal` tinyint(4) DEFAULT NULL,
  `Posición` varchar(255) NOT NULL,
  `PosiciónSecundaria` varchar(255) DEFAULT NULL,
  `Habilidad` tinyint(4) NOT NULL CHECK (`Habilidad` between 1 and 100),
  `Afinidad` enum('Fuego','Aire','Montaña','Bosque','Neutro') NOT NULL,
  `SupertécnicaPrincipal` varchar(255) DEFAULT NULL,
  `SupertécnicaSecundaria` varchar(255) DEFAULT NULL,
  `Nacionalidad` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jugadores`
--

INSERT INTO `jugadores` (`ID`, `Nombre`, `ID_equipo`, `Dorsal`, `Posición`, `PosiciónSecundaria`, `Habilidad`, `Afinidad`, `SupertécnicaPrincipal`, `SupertécnicaSecundaria`, `Nacionalidad`) VALUES
(1, 'Mark Evans', 1, 1, 'Portero', 'Líbero', 95, 'Montaña', 'Parada Celestial', 'Mano Mágica', 'Japonés'),
(2, 'Nathan Swift', 1, 2, 'Lateral izquierdo', 'Mediocentro', 91, 'Aire', 'Danza del Viento', 'Entrada Huracán', 'Japonés'),
(3, 'Jack Wallside', 1, 3, 'Defensa central', 'Líbero', 90, 'Montaña', 'La Montaña', 'El Muro', 'Japonés'),
(4, 'Jim Wraith', 1, 4, 'Defensa central', 'Lateral derecho', 72, 'Bosque', 'Giro Bobina', 'Bloqueo Doble', 'Japonés'),
(5, 'Tom Ironside', 1, 5, 'Defensa central', 'Lateral derecho', 81, 'Fuego', 'Corte Giratorio', 'Superaceleración', 'Japonés'),
(6, 'Steve Grim', 1, 6, 'Volante izquierdo', 'Mediocentro', 79, 'Aire', 'Disparo Rodante', 'Remate en V', 'Japonés'),
(7, 'Tim Saunders', 1, 7, 'Mediocentro', 'Volante izquierdo', 73, 'Bosque', 'Torbellino Dragón', 'Cabezazo Kung-fu', 'Japonés'),
(8, 'Sam Kincaid', 1, 8, 'Mediocentro', 'Defensa central', 72, 'Fuego', 'Chut Granada', 'Estrella Fugaz', 'Japonés'),
(9, 'Maxwell Carson', 1, 9, 'Volante derecho', 'Mediocentro', 72, 'Aire', 'Pase Cruzado', 'Robo Veloz', 'Japonés'),
(10, 'Axel Blaze', 1, 10, 'Delantero centro', 'Segundo delantero', 97, 'Fuego', 'Tornado de Fuego', 'Fuego Supremo', 'Japonés'),
(11, 'Kevin Dragonfly', 1, 11, 'Segundo delantero', 'Delantero centro', 88, 'Bosque', 'Remate Dragón', 'Megadragón', 'Japonés'),
(12, 'William Glass', 1, 12, 'Segundo delantero', 'Defensa central', 48, 'Bosque', 'Remate Gafas', NULL, 'Japonés'),
(13, 'Bobby Shearer', 1, 13, 'Lateral derecho', 'Mediocentro', 90, 'Bosque', 'Barrido Defensivo', 'Corte Volcánico', 'Estadounidense'),
(14, 'Hurley Kane', 1, 14, 'Defensa central', 'Lateral izquierdo', 83, 'Aire', 'Remate Tsunami', 'Gran Tifón', 'Japonés'),
(15, 'Darren LaChance', 1, 15, 'Portero', 'Mediocentro', 92, 'Bosque', 'Manos Infinitas', 'Mano Diabólica', 'Japonés'),
(16, 'Erik Eagle', 1, 16, 'Volante derecho', 'Mediocentro ofensivo', 94, 'Bosque', 'Remate Pegaso', 'Baile de Llamas', 'Estadounidense'),
(17, 'Suzette Hartland', 1, 17, 'Delantero Centro', 'Segundo delantero', 69, 'Bosque', 'Baile de Mariposas', NULL, 'Japonesa'),
(18, 'Victoria Vanguard', 1, 18, 'Mediocentro', 'Volante derecho', 79, 'Aire', 'Torre Inexpugnable', 'Torre Perfecta', 'Japonesa'),
(19, 'Shawn Froste', 1, 19, 'Extremo izquierdo', 'Defensa central', 99, 'Aire', 'Ventisca Eterna', 'Aullido de Lobo', 'Japonés'),
(20, 'Austin Hobbes', 1, 20, 'Extremo derecho', 'Segundo delantero', 93, 'Aire', 'Ventisca Eterna', 'Aullido de Lobo', 'Japonés'),
(21, 'Scott Banyan', 1, 21, 'Defensa central', 'Lateral derecho', 80, 'Bosque', 'Campo Torbellino', NULL, 'Japonés'),
(22, 'Joseph King', 2, 1, 'Portero', NULL, 82, 'Fuego', 'Escudo de Fuerza', 'Colmillo de Pantera', 'Japonés'),
(23, 'Peter Drent', 2, 2, 'Defensa central', NULL, 74, 'Montaña', 'Sismo', NULL, 'Japonés'),
(24, 'Ben Simmons', 2, 3, 'Defensa central', NULL, 70, 'Bosque', 'Ciclón', NULL, 'Japonés'),
(25, 'Alan Master', 2, 4, 'Mediocentro', 'Carrilero izquierdo', 76, 'Aire', 'Barrido Defensivo 2', NULL, 'Japonés'),
(26, 'Gus Martin', 2, 5, 'Carrilero derecho', NULL, 69, 'Bosque', 'Barrido Defensivo', NULL, 'Japonés'),
(27, 'Herman Waldon', 2, 6, 'Mediocentro defensivo', 'Defensa central', 70, 'Aire', 'Coz', NULL, 'Japonés'),
(28, 'John Bloom', 2, 7, 'Volante izquierdo', NULL, 71, 'Fuego', 'Coz', NULL, 'Japonés'),
(29, 'Derek Swing', 2, 8, 'Volante derecho', NULL, 68, 'Aire', 'Triángulo Letal', NULL, 'Japonés'),
(30, 'Daniel Hatch', 2, 9, 'Segundo delantero', NULL, 76, 'Bosque', 'Triángulo Letal', 'Chut de los 100 toques', 'Japonés'),
(31, 'Jude Sharp', 2, 10, 'Mediocentro', 'Mediocentro defensivo', 95, 'Aire', 'Pingüino Emperador Nº2', 'Espejismo de Balón', 'Japonés'),
(32, 'David Samford', 2, 11, 'Delantero centro', 'Mediocentro ofensivo', 91, 'Bosque', 'Pingüino Emperador Nº1', 'Pingüino Emperador Nº3', 'Japonés'),
(33, 'Caleb Stonewall', 2, 12, 'Mediocentro', 'Volante derecho', 93, 'Fuego', 'Pingüino Emperador Nº3', 'Campo de Fuerza', 'Japonés'),
(34, 'Dave Quagmire', 3, 1, 'Portero', 'Falso 9', 89, 'Fuego', 'Destrozataladros', 'Agujero de Gusano', 'Japonés'),
(35, 'Lucy Hailstone', 3, 2, 'Mediocentro', 'Lateral izquierdo', 78, 'Fuego', 'Rompehielos', NULL, 'Japonesa'),
(36, 'Zack Cummings', 3, 3, 'Defensa central', 'Lateral derecho', 79, 'Montaña', 'Terremoto', 'Segadora', 'Japonés'),
(37, 'Albert Denver', 3, 4, 'Defensa central', NULL, 79, 'Montaña', 'Rompehielos', NULL, 'Japonés'),
(38, 'Ben Blowton', 3, 5, 'Mediocentro', 'Defensa central', 79, 'Fuego', 'Cortafuegos', NULL, 'Japonés'),
(39, 'Jordan Greenway', 3, 6, 'Mediocentro', 'Mediocentro ofensivo', 87, 'Bosque', 'Astroremate', 'Carrera relámpago', 'Japonés'),
(40, 'Claude Beacons', 3, 7, 'Delantero centro', 'Extremo izquierdo', 88, 'Fuego', 'Llamarada Atómica', 'Ventisca de Fuego', 'Surcoreano'),
(41, 'Isabelle Trick', 3, 8, 'Mediocentro', 'Mediocentro defensivo', 81, 'Aire', 'Supernova', 'Pingüino Espacial', 'Japonesa'),
(42, 'Xavier Schiller', 3, 9, 'Delantero centro', NULL, 94, 'Bosque', 'Pingüinos Angelicales y Diabólicos', 'Combo Cósmico', 'Japonés'),
(43, 'Bryce Whitingale', 3, 10, 'Delantero centro', 'Extremo derecho', 88, 'Aire', 'Balón Iceberg', 'Remate Caótico', 'Surcoreano'),
(44, 'Xavier Foster', 3, 11, 'Delantero centro', 'Mediocentro ofensivo', 99, 'Aire', 'Descenso Estelar', 'Big Bang', 'Japonés'),
(45, 'Nelson Rockwell', 3, 12, 'Portero', NULL, 84, 'Aire', 'Constelación Estelar', 'Muro Dimensional', 'Japonés'),
(46, 'Wilbur Watkins', 3, 13, 'Segundo delantero', 'Extremo izquierdo', 79, 'Bosque', 'Supernova', 'Pingüino Espacial', 'Japonés'),
(47, 'Ethan Whitering', 3, 14, 'Mediocentro defensivo', 'Defensa central', 79, 'Bosque', 'Supernova', 'Pingüino Espacial', 'Japonés'),
(48, 'Claire Lesnow', 3, 15, 'Defensa central', 'Lateral derecho', 77, 'Aire', 'Rompehielos', NULL, 'Japonesa'),
(49, 'Zeke Valanche', 3, 16, 'Delantero centro', 'Portero', 85, 'Aire', 'Supernova', 'Pingüino Espacial', 'Japonesa');

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `Usuario` varchar(255) NOT NULL,
  `Contrasena` text NOT NULL,
  `Correo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `equipos`
--
ALTER TABLE `equipos`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Nombre` (`Nombre`);

--
-- Indexes for table `jugadores`
--
ALTER TABLE `jugadores`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `ID_equipo` (`ID_equipo`,`Dorsal`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`Usuario`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `equipos`
--
ALTER TABLE `equipos`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `jugadores`
--
ALTER TABLE `jugadores`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jugadores`
--
ALTER TABLE `jugadores`
  ADD CONSTRAINT `jugadores_ibfk_1` FOREIGN KEY (`ID_equipo`) REFERENCES `equipos` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

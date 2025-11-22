-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 21 Nov 2025 pada 17.18
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rex_dash`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `scores`
--

CREATE TABLE `scores` (
  `score_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `timestamp` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `scores`
--

INSERT INTO `scores` (`score_id`, `user_id`, `score`, `timestamp`) VALUES
(1, 1, 242, '2025-11-21 19:27:50'),
(2, 1, 452, '2025-11-21 19:28:01'),
(3, 1, 5068, '2025-11-21 19:29:33'),
(4, 1, 1827, '2025-11-21 19:44:00'),
(5, 1, 663, '2025-11-21 21:06:11'),
(6, 1, 1176, '2025-11-21 21:06:39'),
(7, 1, 896, '2025-11-21 21:06:57'),
(8, 1, 1332, '2025-11-21 21:07:21'),
(9, 1, 403, '2025-11-21 21:07:29'),
(10, 1, 1298, '2025-11-21 21:07:53'),
(11, 1, 372, '2025-11-21 21:08:01'),
(12, 1, 1653, '2025-11-21 21:17:39'),
(13, 1, 695, '2025-11-21 21:17:52'),
(14, 1, 889, '2025-11-21 21:18:09'),
(15, 1, 953, '2025-11-21 21:18:26'),
(16, 1, 1701, '2025-11-21 21:18:57'),
(17, 1, 498, '2025-11-21 21:19:07'),
(18, 1, 1757, '2025-11-21 21:19:38'),
(19, 1, 2138, '2025-11-21 21:20:17'),
(20, 1, 1218, '2025-11-21 21:20:39'),
(21, 1, 473, '2025-11-21 21:20:48'),
(22, 1, 1859, '2025-11-21 21:30:49'),
(23, 1, 3805, '2025-11-21 21:33:54'),
(24, 1, 4043, '2025-11-21 21:36:56'),
(25, 1, 3047, '2025-11-21 21:49:37'),
(26, 1, 2816, '2025-11-21 21:50:28'),
(27, 1, 3509, '2025-11-21 21:52:11'),
(28, 1, 2776, '2025-11-21 21:53:01'),
(29, 1, 3282, '2025-11-21 21:54:00'),
(30, 1, 3780, '2025-11-21 21:55:07'),
(31, 1, 265, '2025-11-21 21:55:12'),
(32, 1, 2702, '2025-11-21 21:58:45'),
(33, 1, 3595, '2025-11-21 21:59:50'),
(34, 1, 2913, '2025-11-21 22:00:42'),
(35, 1, 327, '2025-11-21 22:00:49'),
(36, 1, 4295, '2025-11-21 22:02:05'),
(37, 1, 336, '2025-11-21 22:02:22'),
(38, 1, 5460, '2025-11-21 22:09:19'),
(39, 1, 4467, '2025-11-21 22:39:15'),
(40, 1, 179, '2025-11-21 22:39:18'),
(41, 1, 3029, '2025-11-21 22:40:26'),
(42, 1, 3029, '2025-11-21 22:41:51'),
(43, 1, 6032, '2025-11-21 22:43:38'),
(44, 1, 6956, '2025-11-21 22:47:47'),
(45, 1, 472, '2025-11-21 23:15:20'),
(46, 1, 187, '2025-11-21 23:17:02'),
(47, 1, 182, '2025-11-21 23:17:54'),
(48, 1, 188, '2025-11-21 23:25:56'),
(49, 1, 6313, '2025-11-21 23:50:54'),
(50, 1, 189, '2025-11-22 00:03:06'),
(51, 1, 851, '2025-11-22 00:06:09');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`) VALUES
(1, '1', '1');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `scores`
--
ALTER TABLE `scores`
  ADD PRIMARY KEY (`score_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `scores`
--
ALTER TABLE `scores`
  MODIFY `score_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `scores`
--
ALTER TABLE `scores`
  ADD CONSTRAINT `scores_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

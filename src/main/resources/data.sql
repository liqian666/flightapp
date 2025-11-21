INSERT INTO flight_info (flight_number, origin, destination, departure_date, departure_time, arrival_time, seats_available, price) VALUES
('AB123', 'ABC', 'CDE', '2025-11-10', '2025-11-10T08:00:00', '2025-11-10T11:00:00', 150, 299.99),
('AB456', 'Tatooine', 'Endor', '2025-11-10', '2025-11-10T012:00:00', '2025-11-10T18:00:00', 150, 299.99),
('CD456', 'Jurassic Park', 'Hogwarts ', '2025-11-10', '2025-11-11T04:00:00', '2025-11-11T11:00:00', 3, 199.99),
('CD789', 'Naboo', 'Dagobah', '2025-11-10', '2025-11-11T09:00:00', '2025-11-11T13:00:00', 5, 199.99);

INSERT INTO seat (seat_number, seat_class, is_available, flight_number) VALUES
('A1', 'BUSINESS', true, 'AB123'),
('A2', 'BUSINESS', true, 'AB123'),
('A3', 'ECONOMY', true, 'AB123'),
('A4', 'ECONOMY', true, 'AB123'),
('A5', 'BUSINESS', true, 'AB123'),
('A6', 'ECONOMY', true, 'AB123'),
('A7', 'ECONOMY', true, 'AB123'),
('A8', 'ECONOMY', true, 'AB123'),
('A1', 'ECONOMY', true, 'AB456'),
('A2', 'BUSINESS', true, 'AB456'),
('A3', 'ECONOMY', true, 'AB456'),
('A4', 'BUSINESS', true, 'AB456'),
('A5', 'ECONOMY', true, 'AB456'),
('A6', 'BUSINESS', true, 'AB456'),
('A7', 'ECONOMY', true, 'AB456'),
('A8', 'BUSINESS', true, 'AB456');
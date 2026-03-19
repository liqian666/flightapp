# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Build
./mvnw clean package

# Run tests
./mvnw test

# Build without tests
./mvnw clean package -DskipTests
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Architecture

Spring Boot REST API with H2 in-memory database. Standard layered architecture:

- **controller/** — REST endpoints; all return `ApiResponse` wrapper objects
- **services/** — business logic; booking/payment operations are `@Transactional`
- **repository/** — Spring Data JPA interfaces
- **model/** — JPA entities
- **requestsresponses/** — DTOs for request/response bodies
- **helper/** — enums (`BookingStatus`, `PaymentStatus`, `SeatClass`, `BaggageType`, `IdentityCardType`)

## Key Domain Concepts

- **Booking** supports both one-way (`POST /api/bookings/oneway`) and round-trip (`POST /api/bookings/twoway`); has independent `bookingStatus` and `paymentStatus`
- **Seat** availability is tracked per flight; booking a seat flips `isAvailable` on the `Seat` entity
- **Passenger** belongs to a booking and holds `seatNumber`; has a one-to-many with **Baggage**
- **FlightInfo** has a one-to-many with **Seat**

## Database

H2 in-memory database — data resets on each restart. Sample flights and seats are loaded from `src/main/resources/data.sql` on startup.

- H2 console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, password: empty)
- Swagger UI: http://localhost:8080/swagger-ui.html

## API Base Paths

| Resource   | Base path         |
|------------|-------------------|
| Flights    | `/api/flights`    |
| Users      | `/api/user`       |
| Bookings   | `/api/bookings`   |
| Passengers | `/api/passenger`  |
| Seats      | `/api/seats`      |
| Payments   | `/api/payment`    |
| Baggage    | `/api/baggage`    |

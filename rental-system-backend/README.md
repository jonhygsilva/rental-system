# rental-system (MVP)

Kotlin + Spring Boot + H2 sample project for equipment rental management (MVP).

## Run
./gradlew bootRun

H2 console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:rentalsysdb
Login (form): joao@example.com / 123456

Endpoints:
- POST /auth/register
- GET /auth/users
- GET /api/clientes?userId=<id>
- POST /api/clientes
- GET /api/equipamentos?userId=<id>
- POST /api/equipamentos
- PATCH /api/equipamentos/{id}/status?status=LOCADO
- GET /api/contratos?userId=<id>
- POST /api/contratos
- GET /api/rentals?userId=<id>
- POST /api/rentals

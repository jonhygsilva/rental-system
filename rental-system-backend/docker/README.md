This folder explains how to run the project using Docker Compose.

How to run locally with Docker Compose

1) Build and start containers (from project root):

   docker compose up --build

2) The stack includes:
   - postgres (5432)
   - backend (Spring Boot app on 8080)

3) Environment variables
   - `docker-compose.yml` sets defaults for DB connection and JWT_SECRET.
   - You can override by creating a `.env` file in the project root or by exporting env vars before running docker compose.

4) Useful commands
   - docker compose up --build
   - docker compose logs -f backend
   - docker compose down -v

Notes
- The backend service builds using the local `Dockerfile`. It runs `gradle bootJar` in a build stage then executes the resulting jar.
- If you prefer running the app directly from your IDE during development, keep using your local run configuration instead of Docker.


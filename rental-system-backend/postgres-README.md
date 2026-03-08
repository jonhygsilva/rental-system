# Connecting to Neon (Postgres) for this application

This file contains quick steps to verify the Neon Postgres connection and run the application against it.

Provided connection string (from user):
```
postgresql://neondb_owner:npg_Kf9HSuqpYG1k@ep-sweet-bread-aco5r9d7-pooler.sa-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require
```

Quick checks:

1) DNS resolution
```
dig +short ep-sweet-bread-aco5r9d7-pooler.sa-east-1.aws.neon.tech
```

2) TLS handshake test
```
openssl s_client -connect ep-sweet-bread-aco5r9d7-pooler.sa-east-1.aws.neon.tech:5432 -starttls postgres
```

3) Connect with psql locally
```
psql "postgresql://neondb_owner:npg_Kf9HSuqpYG1k@ep-sweet-bread-aco5r9d7-pooler.sa-east-1.aws.neon.tech/neondb?sslmode=require"
```

Or use Docker (if you don't have psql installed):
```
docker run --rm -it postgres:15 psql "postgresql://neondb_owner:npg_Kf9HSuqpYG1k@ep-sweet-bread-aco5r9d7-pooler.sa-east-1.aws.neon.tech/neondb?sslmode=require"
```

4) Run the Spring Boot app using the included `.env` and helper script
```
# make the script executable once
chmod +x scripts/run-with-postgres.sh

# run
./scripts/run-with-postgres.sh
```

If you see SSL/channel binding errors, try removing the `channel_binding` parameter from the URL (we already removed it in SPRING_DATASOURCE_URL in `.env`).

Security note: `.env` contains plain-text credentials for convenience as requested. Remove or secure this file before committing to a public repository.


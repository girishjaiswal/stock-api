# Dockerfile for PostgreSQL
FROM postgres

ENV POSTGRES_PASSWORD password
ENV POSTGRES_DB spring_data_jpa_postgres_db

COPY src/docker/databases/postgresql/scripts/* /docker-entrypoint-initdb.d/
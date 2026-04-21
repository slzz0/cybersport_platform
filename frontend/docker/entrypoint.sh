#!/bin/sh
set -eu

if [ -n "${DATABASE_URL:-}" ] && [ -z "${SPRING_DATASOURCE_URL:-}" ]; then
  case "$DATABASE_URL" in
    postgresql://*)
      db_uri="${DATABASE_URL#postgresql://}"
      creds_host="${db_uri%%/*}"
      db_path="${db_uri#*/}"
      db_name="${db_path%%\?*}"
      query=""

      if [ "$db_name" != "$db_path" ]; then
        query="${db_path#*\?}"
      fi

      if [ "${creds_host#*@}" != "$creds_host" ]; then
        creds="${creds_host%@*}"
        host_port="${creds_host##*@}"
        db_user="${creds%%:*}"
        db_password="${creds#*:}"

        if [ "$db_password" = "$creds" ]; then
          db_password=""
        fi

        export SPRING_DATASOURCE_USERNAME="${SPRING_DATASOURCE_USERNAME:-$db_user}"
        export SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD:-$db_password}"
      else
        host_port="$creds_host"
      fi

      db_host="${host_port%%:*}"
      db_port="${host_port##*:}"

      if [ "$db_port" = "$host_port" ]; then
        db_port="5432"
      fi

      jdbc_url="jdbc:postgresql://${db_host}:${db_port}/${db_name}"

      if [ -n "$query" ]; then
        jdbc_url="${jdbc_url}?${query}"
      fi

      export SPRING_DATASOURCE_URL="$jdbc_url"
      ;;
  esac
fi

exec sh -c 'java ${JAVA_OPTS:-} -jar /app/app.jar'

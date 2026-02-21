#!/bin/bash
NAME=$1

if [ -z "$NAME" ]; then
  echo "Usage: ./scripts/migration.sh <name>"
  exit 1
fi

VERSION=$(date +%Y%m%d%H%M%S)
FILE="src/main/resources/db/migration/V${VERSION}__${NAME}.sql"
touch $FILE
echo "Created: $FILE"

#!/bin/bash

BASE_DIR=$(pwd)

SERVICES=(
  "AuthService"
  "CatalogService"
  "CourierService"
  "GatewayService"
  "OrderService"
  "RestaurantService"
  "UserService"
)

echo "=== СБОРКА ВСЕХ МИКРОСЕРВИСОВ ==="
echo "Текущая директория: $BASE_DIR"

for SERVICE in "${SERVICES[@]}"; do
  SERVICE_DIR="$BASE_DIR/$SERVICE"
  if [ -d "$SERVICE_DIR" ]; then
    echo ""
    echo "Сборка сервиса: $SERVICE"
    cd "$SERVICE_DIR" || { echo "Ошибка перехода в $SERVICE_DIR"; exit 1; }
    mvn clean package -DskipTests
    if [ $? -eq 0 ]; then
      echo "$SERVICE успешно собран"
    else
      echo "Ошибка при сборке $SERVICE"
      exit 1
    fi
    cd "$BASE_DIR"
  else
    echo "Папка $SERVICE_DIR не найдена, пропускаю"
    exit 1
  fi
done

echo ""
echo "Все сервисы успешно собраны!"
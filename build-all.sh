#!/bin/bash
# === Скрипт сборки всех микросервисов ===

# Базовая директория
BASE_DIR=~/Desktop/DeliveryService

# Список микросервисов
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
cd "$BASE_DIR" || { echo "ыПапка $BASE_DIR не найдена"; exit 1; }

for SERVICE in "${SERVICES[@]}"; do
  SERVICE_DIR="$BASE_DIR/$SERVICE"
  if [ -d "$SERVICE_DIR" ]; then
    echo ""
    echo "Сборка сервиса: $SERVICE"
    cd "$SERVICE_DIR" || continue
    mvn clean package -DskipTests
    if [ $? -eq 0 ]; then
      echo "$SERVICE успешно собран"
    else
      echo "Ошибка при сборке $SERVICE"
      exit 1
    fi
  else
    echo "Папка $SERVICE_DIR не найдена, пропускаю"
  fi
done

echo ""
echo "Все сервисы успешно собраны!"

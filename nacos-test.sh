#!/bin/bash

echo "启动所有服务..."
#docker compose up --scale enrollment-service=3 -d

echo "等待服务启动..."
#sleep 30

echo "检查 Nacos 控制台..."
curl http://localhost:8848/nacos/

echo ""
echo "检查服务注册情况..."
#curl -X GET "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=catalog-service"
curl -X GET "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=catalog-service&groupName=COURSEHUB_GROUP&namespaceId=dev"

echo ""
echo "测试选课服务（enrollment-service）..."
for i in {1..10}; do
  echo "第 $i 次请求:"
  curl http://localhost:8083/api/enrollments/test
done

echo ""
echo "查看容器状态..."
docker compose ps


#!/bin/bash

cd ..

cd working-time-measurement
./gradlew build
docker build --platform="linux/amd64" -t mika2147/time-backend . --no-cache
docker image push mika2147/time-backend
cd ..

cd vacation
./gradlew build
docker build -t vacation-backend . --no-cache
docker build --platform="linux/amd64" -t mika2147/vacation-backend . --no-cache
docker image push mika2147/vacation-backend
cd ..

cd wtm-authorization-server
./gradlew build
docker build -t authorization . --no-cache
docker build --platform="linux/amd64" -t mika2147/authorization . --no-cache
docker image push mika2147/authorization
cd ..

cd working_time_measurement
docker build --platform="linux/amd64" -t mika2147/wtm-frontend . 
docker image push mika2147/wtm-frontend
cd ..

cd Kubernetes
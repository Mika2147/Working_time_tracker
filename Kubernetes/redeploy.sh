#!/bin/bash
kubectl delete -f time-backend.yaml
kubectl delete -f vacation-backend.yaml
kubectl delete -f authorization.yaml

# TODO Gradle build here
mvn -f mape_executor package
mvn -f mape_analyzer package


#TODO Docker build here
cd working-time-measurement
docker build -t time-backend . --no-cache
cd ..

cd vacation
docker build -t vacation-backend . --no-cache
cd ..

cd wtm-authorization-server
docker build -t authorization . --no-cache
cd ..

kubectl apply -f time-backend.yaml
kubectl apply -f vacation-backend.yaml
kubectl apply -f authorization.yaml
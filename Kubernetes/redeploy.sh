#!/bin/bash
kubectl config use-context docker-desktop

kubectl delete -f wtm-frontend.yaml
kubectl delete -f time-backend.yaml
kubectl delete -f vacation-backend.yaml
kubectl delete -f authorization.yaml
kubectl delete -f ingress.yaml

#TODO Docker build here
cd ..

cd working-time-measurement
./gradlew build
docker build -t time-backend . --no-cache
if [ "$#" =  "--push" ]
	then
		docker build --platform="linux/amd64" -t mika2147/time-backend . --no-cache
fi
cd ..

cd vacation
./gradlew build
docker build -t vacation-backend . --no-cache
if [ "$#" =  "--push" ]
	then
		docker build --platform="linux/amd64" -t mika2147/vacation-backend . --no-cache
fi
cd ..

cd wtm-authorization-server
./gradlew build
docker build -t authorization . --no-cache
if [ "$#" =  "--push" ]
	then
		docker build --platform="linux/amd64" -t mika2147/authorization . --no-cache
fi
cd ..

cd working_time_measurement
docker build -t wtm-frontend . 
if [ "$#" =  "--push" ]
	then
		docker build --platform="linux/amd64" -t mika2147/wtm-frontend . 
fi
cd ..

cd Kubernetes

kubectl create namespace working-time-measurement
kubectl create secret generic postgres-secret --from-literal=password=admin

kubectl apply -f postgres-users.yaml
kubectl apply -f mongo-user.yaml
kubectl apply -f mongo-time.yaml
kubectl apply -f authorization.yaml
kubectl apply -f time-backend.yaml
kubectl apply -f vacation-backend.yaml
kubectl apply -f wtm-frontend.yaml
kubectl apply -f ingress.yaml
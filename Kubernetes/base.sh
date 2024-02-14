kubectl create namespace working-time-measurement
helm upgrade --install ingress-nginx ingress-nginx --repo https://kubernetes.github.io/ingress-nginx --namespace ingress-nginx --create-namespace
helm repo add mongodb https://mongodb.github.io/helm-charts
helm upgrade --install community-operator mongodb/community-operator --namespace working-time-measurement --create-namespace --set operator.watchNamespace="working-time-measurement"

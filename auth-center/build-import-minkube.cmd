@echo "-----------------------------------------------mvn package..."
call mvn clean package
@echo "-----------------------------------------------build docker image..."
docker build -t cloud-di-zai-auth-center:latest .
@echo "-----------------------------------------------import image to minikube..."
minikube image load cloud-di-zai-auth-center:latest
@echo "-----------------------------------------------check image in minikube..."
minikube image ls | findstr "auth-center"
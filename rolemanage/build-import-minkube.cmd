@echo "-----------------------------------------------mvn package..."
call mvn clean package
@echo "-----------------------------------------------build docker image..."
docker build -t cloud-di-zai-role-manage:latest .
@echo "-----------------------------------------------import image to minikube..."
minikube image load cloud-di-zai-role-manage:latest
@echo "-----------------------------------------------check image in minikube..."
minikube image ls | findstr "role-manage"
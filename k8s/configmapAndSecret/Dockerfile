# 使用OpenJDK 21作为基础镜像
FROM ibm-semeru-runtimes:open-21-jre
# 设置工作目录
WORKDIR /app
# 将构建好的JAR包复制到容器中
COPY ./target/demo20260205-0.0.2-test-config-map.jar app.jar
# 声明应用运行端口（Spring Boot默认8080）
EXPOSE 10001
# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/etc/myapp/application.yaml"]
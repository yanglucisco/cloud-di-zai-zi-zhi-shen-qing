# 使用OpenJDK 17作为基础镜像
FROM ibm-semeru-runtimes:open-21-jre
# 设置工作目录
WORKDIR /app
# 将构建好的JAR包复制到容器中
COPY target/certification-catalog-1.0-SNAPSHOT.jar app.jar
# 声明应用运行端口（Spring Boot默认8080）
EXPOSE 20000
# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
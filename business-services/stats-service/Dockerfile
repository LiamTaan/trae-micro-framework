# 使用JDK17作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制jar包到工作目录
COPY target/stats-service-1.0.0.jar stats-service.jar

# 暴露端口
EXPOSE 8085

# 运行jar包
ENTRYPOINT ["java", "-jar", "stats-service.jar"]

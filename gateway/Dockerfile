FROM openjdk:17-jdk-alpine

MAINTAINER trae-micro-framework

# 设置时区
RUN apk add --no-cache tzdata && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 创建目录
RUN mkdir -p /app

# 复制jar包
COPY target/gateway-1.0.0.jar /app/app.jar

# 设置工作目录
WORKDIR /app

# 设置JVM参数
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
#指定基础镜像，在其上进行定制
FROM openjdk:8-jdk-alpine
#这里的 /tmp 目录就会在运行的之后自动挂载为匿名卷， 任何项 /tmp中写入的信息都不会记录进容器存储层
VOLUME /tmp
ADD *.jar app.jar
#声明镜像内服务所监听的端口
EXPOSE 8888
#指定镜像的默认入口
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

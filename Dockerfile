ARG JDK_IMAGE=21
ARG JRE_IMAGE=21

FROM openjdk:${JDK_IMAGE} as builder
WORKDIR application
ARG JAR_FILE=target/security*.jar
COPY ${JAR_FILE} security.jar
RUN java -Djarmode=layertools -jar security.jar extract

FROM eclipse-temurin:${JRE_IMAGE}

# add a user for security aspects
RUN addgroup -S springboot && adduser -S springboot -G springboot

RUN mkdir -p /application
RUN chown springboot /application

USER springboot
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader ./
COPY --from=builder application/snapshot-dependencies ./
COPY --from=builder application/application ./

ENTRYPOINT [ "java", "-Xmx700m", "org.springframework.boot.loader.JarLauncher" ]
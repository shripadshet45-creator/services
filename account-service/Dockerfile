FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL com.azure.containerizationassist.createdby="containerization-assist"

WORKDIR /app

COPY target/*.jar /app/app.jar

ENV SERVER_PORT=8081
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD wget --spider -q http://localhost:8081/actuator/health || exit 1

RUN adduser -D -u 10001 appuser
USER appuser

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

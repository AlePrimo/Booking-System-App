# Imagen base con JDK 21
FROM eclipse-temurin:21-jdk

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el JAR compilado
COPY target/Booking-System-App-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto (por defecto de Spring Boot)
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]

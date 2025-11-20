# =========================
# 1️⃣ Build Stage
# =========================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copie du pom.xml et téléchargement des dépendances (pour le cache Docker)
COPY pom.xml .
# Cette commande télécharge les dépendances sans construire le projet
# Cela permet à Docker de mettre en cache cette couche si le pom.xml ne change pas
RUN mvn dependency:go-offline

# Copie du code source et construction du projet
COPY src ./src
RUN mvn clean package -DskipTests

# =========================
# 2️⃣ Runtime Stage
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copie du JAR généré depuis l'étape de build
# Le nom du jar généré dépend de l'artifactId et de la version dans le pom.xml
# Le wildcard '*' assure que ça marche même si la version change
COPY --from=builder /app/target/interactions-service-*.jar app.jar

# Expose le port défini pour ce service (8083 par défaut)
EXPOSE 8083

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
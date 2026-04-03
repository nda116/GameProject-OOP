FROM maven:3.8.4-openjdk-17-slim

RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libglu1-mesa \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

CMD ["mvn", "clean", "package"]
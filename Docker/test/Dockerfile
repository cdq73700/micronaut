FROM openjdk:17-jdk-slim-bullseye

WORKDIR /backend

COPY . .

WORKDIR /backend/build/libs

EXPOSE 8080

CMD ["java","-jar","backend-0.1-all.jar"]

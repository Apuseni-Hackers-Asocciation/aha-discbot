FROM openjdk:15-alpine

COPY  ./build/install/aha-discbot ./app

WORKDIR /app/bin
CMD ./aha-discbot

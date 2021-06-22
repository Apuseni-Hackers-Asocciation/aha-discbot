FROM openjdk:15-alpine


COPY  ./build/install/aha-discbot ./app


WORKDIR /app/bin
CMD ./aha-discbot
#ENTRYPOINT ["java","-Xms64M", "-Xmx256M","-jar","aha-discbot*.jar"]

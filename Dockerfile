FROM openjdk:8-jdk-alpine
LABEL maintainer="josefigueredo@gmail.com"
COPY build/unpacked/dist /usr/src/
EXPOSE 8080
CMD [ "/usr/src/kotlin-dsl-example-1.4.0/bin/kotlin-dsl-example" ]


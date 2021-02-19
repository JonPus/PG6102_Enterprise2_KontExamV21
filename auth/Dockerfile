FROM adoptopenjdk/openjdk11:alpine

COPY target/auth.jar .

CMD java -Dspring.profiles.active=adminUser  -jar auth.jar
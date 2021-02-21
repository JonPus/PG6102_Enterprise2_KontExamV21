FROM adoptopenjdk/openjdk11:alpine

COPY target/messages.jar .

CMD java -Dspring.profiles.active=FakeData  -jar messages.jar
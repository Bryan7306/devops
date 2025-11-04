FROM openjdk:17-ea-32-oracle
COPY ./target/devops.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "devops.jar", "db:3306", "30000"]
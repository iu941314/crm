FROM java:8
WORKDIR /usr/local
ADD ./target/docker-springboot-crm.jar .
CMD ["java","-jar","docker-springboot-crm.jar"]
EXPOSE 8787


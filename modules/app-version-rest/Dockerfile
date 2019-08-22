FROM java:8
VOLUME /tmp
COPY app-version-rest.jar app-version-rest.jar
ENTRYPOINT ["java","-jar" ,"-Xms1024m","-Xmx1024m","-Xss512k","-Dspring.profiles.active=${APP_PROFILE}","app-version-rest.jar"]
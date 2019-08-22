FROM java:8
VOLUME /tmp
COPY app-version-manager.jar app-version-manager.jar
ENTRYPOINT ["java","-jar" ,"-Xms2048m","-Xmx2048m","-Xss512k","-Dspring.profiles.active=${APP_PROFILE}","app-version-manager.jar"]
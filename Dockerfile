FROM tomcat:latest
ADD target/weather-history.war /tomcat
EXPOSE 8080
CMD ["catalina.sh", "run"]

FROM tomcat:9.0

COPY target/*.war /usr/local/tomcat/webapps/todo.war

EXPOSE 8080

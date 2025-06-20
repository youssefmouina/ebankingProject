# Utilise une image compatible Tomcat 10.1.x et Java 21
FROM tomcat:10.1.34-jdk21

# Supprime les applications par défaut
RUN rm -rf /usr/local/tomcat/webapps/*

# Copie ton fichier WAR
COPY target/bank-project.war /usr/local/tomcat/webapps/ROOT.war

# Expose le port HTTP
EXPOSE 8080

# Commande de démarrage
CMD ["catalina.sh", "run"]
# WebChat: Local HTTP site
Simple Java-based HTTP server with support for static file serving and messaging functionality.

Dependencies:
SQLite JDBC driver (org.xerial:sqlite-jdbc:3.49.1.0)

Argon2 hashing (de.mkammerer:argon2-jvm:2.12)

Requirements:
Java 21

Maven

Build:
mvn clean package

Run:
java -cp target/WebChat-1.0.jar;path/to/libs/* WebChat

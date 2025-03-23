# WebChat: Local HTTP site
## Features

- Local static file hosting (`www/`)
- SQLite database integration
- Argon2 password hashing
- Simple command-line control (`help`, `shutdown`)

## Requirements

- Java 21
- Maven

## Dependencies

- `org.xerial:sqlite-jdbc:3.49.1.0`
- `de.mkammerer:argon2-jvm:2.12`

## Build

```bash
mvn clean package

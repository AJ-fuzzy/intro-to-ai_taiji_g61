# Taiji AI
## Running the Project

To run the project from the terminal, you need:

* **Java (JDK)** installed (version matching the one in `pom.xml`, currently Java 21)
* **Maven** installed

From the **root of the repository** (the folder containing `pom.xml`), run:

```bash
mvn exec:java
```

This will compile the project (if needed) and execute the configured main class via Maven.

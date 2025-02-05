# RotterdamPortEvents

This is a demo application to fetch rotterdam port events

## Prerequisites

Before building the project, ensure you have the following installed:

1. **Java Development Kit (JDK)**
    - Version: 8 or higher (depending on your project's requirements)
    - Verify installation:
      ```bash
      java -version
      ```

2. **Apache Maven**
    - Version: 3.6.0 or higher
    - Verify installation:
      ```bash
      mvn -version
      ```

3. **Git** (optional, for fetching the repository)
    - Verify installation:
      ```bash
      git --version
      ```

## Clone the Repository

If you haven’t already cloned the project, use the following command:

```bash
git clone <repository-url>
cd <project-directory>
```

## Build the Project

To build the project, follow these steps:

1. Open a terminal/command prompt.
2. Navigate to the project root directory (where the `pom.xml` file is located).
3. Run the Maven build command:

   ```bash
   mvn clean package
   ```

## Build Output

After the build completes successfully, you can find the packaged application in the `target` directory:

- For a JAR project: `target/demo-0.0.1-SNAPSHOT.jar`

## Run the Application

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
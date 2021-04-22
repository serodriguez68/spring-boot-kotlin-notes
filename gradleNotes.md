# IntelliJ and Gradle
IntelliJ's graddle plugin is great.
You can run all the commands you would run on the command line through the plugin

# Gradle Tasks
```shell
./gradle tasks
```
Lists all the tasks available for your project. Here is some sample output
```
Application tasks
-----------------
bootRun - Runs this project as a Spring Boot application.

Build tasks
-----------
assemble - Assembles the outputs of this project.
bootBuildImage - Builds an OCI image of the application using the output of the bootJar task (Docker Image)
bootJar - Assembles an executable jar archive containing the main classes and their dependencies.
...

Build Setup tasks
-----------------
...
```

# Gradle clean
Gradle puts all the build results and compiled code in the `build` directory.
`./gradlew clean` deletes that folder.

# Gradle build
Compiles, run the tests and builds the project

# Gradle bootrun
Runs your application. Equivalent to running via the IDE.

# Gradle bootJar
Assembles all the project under a single executable Jar.
You can find it in build/libs.

To run it do `java -jar build/libs/bankSpringTutorial-0.0.1-SNAPSHOT.jar

# Gradle bootBuildImage
Assembles a docker image that runs your SpringBoot application.
You can use the image to run it with Kubernetes, Docker Compose or other orchestration.

After the docker is built. You can use `docker run <your-image>` to run it.

This bootBuildImage tasks comes from `build.gradle.kts > plugins > id("org.springframework.boot")`

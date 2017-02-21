WELLING
=======

This project is part of the GeMARA project. With this project you can easily generate RESTfull applications.
GeMARA consists of several projects e. g. a project which generates a artifact for a Tomcat-Webserver.

Welling is a generator for Android applications.


Introduction:
------------

As part of GeMARA you will need the whole GeMARA project to run this generator.

1. Create a new Maven project
2. Add GeMARA Maven dependencies
3. Create an Enfield-Model with the fluent-interface, which describes your project best
4. Execute your project
5. Deploy server-artifact on an Tomcat-Webserver
6. Use the Makefile to compile and install the Android application

Alternatively to Step 6. Import the generated Android sources to your IDE and compile and install the App with it.

Testing:
--------

For testing proposals you can use the `main()` - method in the `Main` - class.
This test-case will generate an application which fits to the GeMARA generated project `Lecturer`.
You can find the REST-API under https://apistaging.fiw.fhws.de/mig/api/.

**Step by Step:**

1. Checkout the repository
2. Run `mvn test`

Continue with Step 6. from the introduction.

If there occurs any problems with copying some files please execute the project again.
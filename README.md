# FoodGen-Back

This repository serves as back-end for the foodGen-App.

### Built With

Here are the lists of major frameworks/libraries used in the project.

* to be filled soon...

### Linter :
CheckStyle is used as a Linter for maintaining code quality and following coding standards in
the source code. </br>

CheckStyle configuration file is an XML file in which contains all the rules
and modules to be used for checking the code. </br>

It is currently stored at `resources/checkstyle/checkstyle.xml`

It is, by default triggered when building the app but if you want to execute it manually, execute this command:
   ```sh
   mvn clean checkstyle:checkstyle
   ```

Once checking the code is completed, it will generate an HTML file report for checkstyle result
, `target/site/jacoco/index.html` contains all output.

### Tests :
Jacoco is used for testing code coverage in this application and should be always be 100%.

However you can trigger tests manually by executing this command :
   ```sh
   mvn clean test
   ```

Once the test has been completed, `target/site/jacoco/index.html` contains all output.

## Definition of Done
1. [X] Authentication: sign up, sign in, logout
2. [X] Random food generation
3. [X] Recipe and meal posts 
4. [X] Recipe & meal search with filters
5. [ ] Recommendation
6. [X] Rating

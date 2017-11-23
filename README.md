This project is demonstration purpose only.
It is not well architected modular project.

The project consists of :
- entities
-repositories
-services
-controllers

Running project:
just run ./mvnw clean install to build the project

once ready go to target folder and run the command java -jar EnergyConsumptionApplicationStarter-0.0.1-SNAPSHOT.jar

Assumptions:
1. As and when an assumption is made i have added comment on top of the class. One such assumptions is profile and meter as one to one relationships.
2. Some aspects of design can be better. I have added comments where ever appropriate.
3. Some of the test cases i have left to save some time
4. The input validation and securing end points is left out again due to time
5. Http response codes for errors is set to 500 which can be corrected.


Future development:

1.Documentation of REST end point using swagger2.
2. Input validations using javax validations (Hibernate validator).
3.Add more tests to cover more branches.
4.Make project more modular, separate entities, services, repositories and controllers into separate modules.
5.Test controllers using mockmvc.



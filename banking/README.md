RBS Basic Retail banking project
================================

#### Assumptions
     - Swagger UI is used as an interface for this spring boot app to expose the rest endpoints.
     - There is no data layer so will be using a jvm cache to capture user and account information
     
     - Additional APIs
        * User API is added so we can create an account and then start adding transactions to it.
     
     - Validations
       * Account should exist before requesting a balance or posting any transaction to it.
       * Insufficient funds response during withdrawal if the withdrawal amount is greater than current Balance.
       * Cannot remove an account when it is not having zero balance.
       
     - Interest Calculation
       * For simplicity Interest are being automatically created when atleast a minimum of 5 transaction is available.
           # This is to show the functionality of Interest calculation in real time.
       * Interest is calculated using simple PNR / 100 formula again for simplicity.
       
      - As User api details are added for only for admin purpose Integration test is not covered for it.
      - All classes are unit tested except for datastore package which simulates data layer.
      
  
##### Unit test coverage with report
       mvn clean package -Djacoco.skip.instrument=false
       
- This will create an index.html under target/site/jacoco/index.html

- This file can be opened in a browser and we can check the test coverage
       
##### Integration test
Achieved by cucumber test
         
    Can be run as Junit by mapping RunCucumberIT as main class.
    
#### Running the app locally
- Prerequisite to run the app
          
           Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
           Maven home: C:\Program Files (x86)\apache-maven-3.6.3-bin\apache-maven-3.6.3\bin\..
           Java version: 1.8.0_241, runtime: C:\Program Files\Java\jdk1.8.0_241\jre


- Run the below command from the project base directory

        mvnw spring-boot:run


- Alternatively LocalStartUp.bat can be run as well.
- After start up App can be accessed locally from http://localhost:8080/swagger-ui.html
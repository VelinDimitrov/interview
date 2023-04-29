Welcome to the iC Consult coding assignment!

Disclaimer: This application lacks tests and documentation on purpose (see bellow). We actually value meaningful tests
and good documentation. Normally for us security and data protection are of the utmost importance. Next come
maintainability and performance. For the code presented here these values have been broken on purpose in order to create
room for discussion during the interview process.

This project represents a very minimalistic user management application with an in memory database and a somewhat
RESTful API. The API is described in swagger ui and protected by OAuth 2.0. There are several faults in the application
which you might want to fix and if you find the time you might wish to implement some additional functionality. Even if
you don't get far, please get familiar with teh codebase as it will be discussed during the code interview.

For the OAuth 2.0 integration with auth0 in the swagger ui, please use the values provided in the local profile. If you
change any of the swagger/oauth config the swagger ui might break.

Tasks and assignments (in no particular order):

- get familiar with the code base and understand what happens where
    - how many test accounts are provided in the initial state of the database?<br/>
      Answer: There are 3 records provided.We can find them in the data.sql
    - what id did you get at auth0?<br/>
  Answer: Not really sure what id was asked here but from what I see from there we take the JWT(JSON Web token) "Bearer {token}"
    - where is the implementation of the database access methods?
<br/>Answer: it is handled by spring data jpa it automatically generates implementaion from the contract. Also,it has some predefined methods in the JpaRepository for the usual functionalities.Providing the two generic arguments to the interface shows which entity is being operated on and its identifier
    - why are there two different application files?<br/>
  Answer: if we are talking about the application.yml files we can have different configuration depending on environments or etc. so we need a way to set those different configuration properties so using the spring profiles help us to resolve which yml files with config to take 
    - are the logs meaningful? <br/>
      Answer:For the purpose of tracking general stuff and project running they are not.TRACE logging level can be meaningful if we investigate an issue with our app or connection with other apps and services but for the sake of development it is polluting the logs.For the current implementation INFO logging level should be enough
- compile and run application from the command line (hint: activate the spring local profile ) <br/>
  Answer:mvn spring-boot:run -Dspring-boot.run.profiles=local <br/>
- get application to run in IntelliJ IDEA (or some other IDE) with the local profile <br/> Answer:
In InteliJIDEA edit configuration and place local profile in Active Profiles input or if we need a more general solution is modify the pom.xml and place profiles there with corresponding property for spring profiles and set the value to local
- create a docker container out of jar file and run it locally (using again the local profile)

Known errors and issues (there certainly are more, we'd be happy if you share your findings with us):

- the application is logging too much data - reduce the amount of logs<br/>
Answer: Change the logging level in the application-local.yml to INFO  
- the update operation is currently broken and results in a 500 error even when provided with valid parameters and
  should be fixed<br/>
Answer: it was because of the anonymization and changed in code
- when a customer id is not found in the DB the application should return a 404 error instead of 500<br/>
  Answer:Its because there was no checks whether the customer exists and when returned null throws error.when missing handeled with another implementation in GlobalExceptionHandler.
- when performing an update and changing the customer's email address, the new address is never persisted, but it should
  be<br/>
Answer:The setter is never called
- when performing the getuser operation private customer data is logged in plaintext to the log, it should be anonymised<br/>
  Answer:Changed in the code
#Additional found errors and issues:
- id in the table is integer but in code it is Long type so potentially change the type in the db schema to be BIGINT
- ddl auto is update so if there is a change in the entity will reflect it in the schema. As we have taken the approach of defining the schema outside in the schema.sql I think it would be better to correct the schema and change the type to validate so we have additionally a validation on the schema 
- Rather than concatenating messages for logs or exceptions we can use String format or the logger formatting. It will save some unnecessary String creation and will be more readable
- We can create checks whether that type of logging is enabled. It can save some iterations because internally it also checks if that type of logging is enabled and if not it does not log it
- if we want to increase performance by little: The anonymization can be done with StringBuilder rather than StringBuffer as it is faster
- Logic for anonymization simplified and fixed
- Encapsulate loggers further with private and we need only one instance so we place final as well
- Changed in GlobalExceptionHandler to return to the user more appropriate messages
- Validation was not working although annotations were used so added spring boot starter validation dependency and corresponding annotations and error handling

Possible extensions (only describe how you would implement them, no actual implementation required):

- introduce a method for creating new customers<br/>
Answer: We can create a POST endpoint with path /api/v1/customer which will accept a pojo which will be similar to the CustomerRequest(it will contain the user id as well) with similar validations as the PUT request . We will have the corresponding checks for whether the customer exists and also if we add roles we can validate the role of the logged in user. 
- introduce a method for updating certain attributes <br/>
Answer: We can create a PATCH endpoint for partial update with path /api/v1/customer/${id} which will accept a pojo which will be similar to the CustomerRequest(it will contain the user id as well) with similar validations as the PUT/POST request . We will have the corresponding checks for whether the customer exists and also if we add roles we can validate the role of the logged in user.
- introduce access restrictions - allow access only for certain authenticated principals, allow certain operations only
  for certain authenticated principals<br/>
Answer: I would introduce certain roles for the users and adapt the token generation process to include those roles inside the token and on request we can validate with filters or with checks in code.<br/>
If we cannot modify the mechanism for token generation we can still introduce the roles on db side and use the userId from the token to fetch the user roles and validate against them
- testing & documentation - what are good candidates for unit tests, integration tests, what needs to be documented?<br/>
Answer:Everything should have unit tests but a good first candidate is the anonymization logic, extraction of the jwt userid logic, the entity and pojos overriden methods(hashcode,equals), the service logic as well.<br/> For integration tests a good candidate is the customer endpoints and the security integration,and the exception handling.<br/> For Documentation : the endpoint with swagger annotations and more of the complex business logic annonymisation(before my simplification of it) and potentially extraction of jwt as well as some business logic if not clear enough from code namings and conventions.
- introduce additional logging and error handling where necessary<br/>
Answer: added check on GET request for customer and additional changes in code for better readability when logging

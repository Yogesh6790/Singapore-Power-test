# Singapore Power test
I've basically used Spring Boot + Hibernate + MySQL to develop this RestFul server,
By using these technologies, I was able to use their benefits like
  - Dependency injection
  - Easier dependency resolution
  - Less work on the configuration
  - Embedded server
  - Advantages of ORM framework

### Other benefits
  - Have used appropriate HTTP methods for each use case. Because of its idempotency property, I've used PUT instead of POST for use cases 1,,4 and 5. And I've used GET for use cases 2, 3 and 6 as these use cases are involved in retrieving records.
  - performed input(email) validation
  - have used a single entity which improves performance while retrieving and updating records
  - Have properly handled all runtime exceptions and provided meaningful error message as JSON response. I've also followed "throw early-catch later" policy for exception handling.
  
URL for six methods are specified below

* PUT [http://45.32.123.12:8080/makeFriends]
* GET [http://45.32.123.12:8080/retrieveFriends]
* GET [http://45.32.123.12:8080/retrieveCommonFriends]
* PUT [http://45.32.123.12:8080/subscribeUpdates]
* PUT [http://45.32.123.12:8080/blockUpdates]
* GET [http://45.32.123.12:8080/retrieveEmailAddress]

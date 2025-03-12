## Start Database
* Execute following command from stock-api
  ```
   docker build -t spring-boot-postgresql-db . -f src/docker/databases/postgresql/Dockerfile
  
   docker run --name spring-boot-postgresql-db-ins  -d -p 5432:5432 spring-boot-postgresql-db
  ```
  
## Build and run Stock App
* Using dev profile
  ```
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
  ```
## User Tokens
* To generate access token go to :-
[Token URL](https://dev-90067624.okta.com/oauth2/default/v1/authorize?client_id=0oa83wnhb5QkHeZMc5d7&redirect_uri=http://localhost:8080/authorization-code/callback&scope=openid&response_type=token&response_mode=form_post&state=state&nonce=6jtp65rt9jf)
 and use below-mentioned users/Admin and password for login
### Users
* User1
user1@test.com
Okta@123

* User2
user2@test.com
Okta@123

### Admin
* admin@test.com
Payconiq@123

## Open Api Docs
http://localhost:8080/swagger-ui.html


## Test


#### 93% Test Coverage.
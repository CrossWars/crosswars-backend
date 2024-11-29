# crosswars-backend

Backend crossword REST API to Store and maintain crossword data. See the related
telegram [leaderboard bot](https://github.com/RobertWetzler/CrosswordLeaderboardBot).

## Tech Stack

| Dependency | Version |
| ---------- | ------- |
| Kotlin     | 1.5.20  |
| Java       | 16      |
| Springboot | 2.5.1   |

## Local Development

### Creating the Sqlite database

Create the database at the root of the project:

```shell
sqlite3 CrosswordDB.db
```

Run the ddl script to create the tables

```shell
cat src/main/resources/crosswars.sql | sqlite3 CrosswordDB.db
```

Create an `application-local.properties` file in the resources' folder:

```properties
# Database
driverClassName=org.sqlite.JDBC
url=jdbc:sqlite:CrosswordDB.db
username=sa
password=sa
# Google Auth
google.client-id=<oauth2-client-id>
auth.auth-key=<auth-key>
auth.enabled=false
# Optional Email Reminders
email.enabled=true
email.username=<email-username>
email.password=<email-password>
email.debug=true
```

### Build & Run

Build using gradle

```shell
./gradlew build
```

Run using the bootRun task

```shell
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Example requests

```shell
curl --request GET \
  --url http://localhost:8080/crosswars/api/users/ids/1
  
curl --request GET \
  --url http://localhost:8080/crosswars/api/users/names/Billy
  
curl --request POST \
  --url http://localhost:8080/crosswars/api/entries \
  --header 'Content-Type: application/json' \
  --data '{
	"user_id": "userId",
	"time": 60
}'
```

## Generating the Swagger docs

Run the app locally and hit this endpoint to generate the swagger docs in json format.

```
GET http://localhost:8080/crosswars/v2/api-docs
```

You can get a pretty layout and UI by pasting the json [here](https://editor.swagger.io).
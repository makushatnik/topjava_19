## Requests

### MealServletController

#### /topjava/rest/meals/filter POST - Filter
> curl -X POST \
  http://localhost:8080/topjava/rest/meals/filter \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 70147ccb-5dd8-413b-b39c-d328f7377d05' \
  -H 'cache-control: no-cache' \
  -d '{
	"startDate":"2015-05-30",
	"startTime":"11:18:00",
	"endDate":"2019-03-30",
	"endTime":"23:18:00"
}'

#### /topjava/rest/meals/ GET - Get All
> curl -X GET \
  http://localhost:8080/topjava/rest/meals/ \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 427011a8-ec3c-4cae-8b41-57ff3263148c' \
  -H 'cache-control: no-cache' \
  -d '{
	"dateTime":"2019-03-30T11:54:00",
	"description":"Updated REST Meal",
	"calories":567
}'

#### /topjava/rest/meals/ POST - Create
> curl -X POST \
  http://localhost:8080/topjava/rest/meals \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: e623e1b6-2f4b-4fc1-8124-fb96ed9d7123' \
  -H 'cache-control: no-cache' \
  -d '{
	"dateTime":"2019-03-31T23:18:00",
	"description":"Trying to Create new Meal",
	"calories":375
}'

#### /topjava/rest/meals/ PUT - Update
> curl -X PUT \
  http://localhost:8080/topjava/rest/meals/100010 \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: ea9fb32f-54e9-46ed-9336-004bb92544ca' \
  -H 'cache-control: no-cache' \
  -d '{
	"dateTime":"2019-03-30T11:54:00",
	"description":"Updated REST Meal",
	"calories":567
}'

#### /topjava/rest/meals/ DELETE - Delete
> curl -X DELETE \
  http://localhost:8080/topjava/rest/meals/100010 \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Postman-Token: 4ce63e75-4990-401b-bbde-8694ccab79ef' \
  -H 'cache-control: no-cache'

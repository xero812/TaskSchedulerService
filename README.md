# TaskSchedulerService

An approach to designing a distributed task Scheduler service. We can schedule tasks with timestamps. The task can be anything like sending an email, text, hitting a third party API or as simplistic as printing a message on the terminal. 

The service works on plug n' play architecture and is written keeping in mind to be able to scale horizontally in case of large number of requests.

## Prerequisites

### Redis 
```$xslt
$./redis-cli
127.0.0.1:6379>
```
### MySQL 
```$xslt
 $./mysql -uroot -p123
```
### RabbitMQ
```$xslt
http://localhost:15672
``` 

## Starting the service

```
mvn spring-boot:run
```

## Scheduling Tasks

```$xslt
curl --location --request POST 'localhost:8080/tasks/submit' \
--data-raw '{
	"message":"drredecff",
	"timestamp":"2020-03-10T00:42:30"
}'

```
## Tracking Progress

```$xslt
curl --location --request GET 'localhost:8080/tasks/{id}' \
--header 'Content-Type: application/json'
```

## Tools

Spring Boot 2.2.5.RELEASE

JDK 1.8

Apache Maven 3.6.3

Lombok 1.18.10

Spring AMQP 2.2.5.RELEASE

Hibernate 5.14.12

Spring Data Redis 2.2.5.RELEASE





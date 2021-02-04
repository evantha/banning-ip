# Banning IP numbers

Currently, the system logs when a user has reached the allowed api limit. We could ship the log file to Elasticsearch
using Filebeat and trigger alerts using ElastAlert. But it would be better to notify such scenarios with an event to a 
message queue directly from the api itself. Then we could have other microservices to listen to these events and take 
appropriate actions.
![](https://github.com/evantha/banning-ip/images/diagram.png)

If our api sits behind an API gateway (eg: AWS API Gateway) or a load balancer (NGINX), we could see what it offers and chances are that these kind 
services are readily available. Then we do not need to worry about such cross cutting concerns and concentrate on our 
business problems and control all our microservices from a single place.
### Requirements
- Docker
- Docker Compose

### Steps
*   Clone repository
```shell script
git clone https://github.com/evantha/banning-ip.git
cd myapp
```
* Run unit tests
```shell script
mvn test
```
*   Run Docker Compose
```shell script
docker-compose up
```
*   http://localhost:8080/api/hello/test

### Demo
![](https://github.com/evantha/banning-ip/images/rate-limit.gif)
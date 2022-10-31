
run mysql in docker
```
docker run --name rollingredisdemo -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql
```
then create rollingredisdemo database in docker
```
mysql -p
123456
create database rollingredisdemo;
```

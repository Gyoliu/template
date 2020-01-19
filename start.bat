@echo off
start java -jar hr-0.0.1-SNAPSHOT.jar --password.encryption=md5 --spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
exit
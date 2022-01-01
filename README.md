# SkyBlock Simplified API
Bundled set of custom libraries including (but not limited to) HTTP, Database, YAML, NBT, Json, Reflection and Concurrent Collections/Maps.

## Local Dev

### Docker MySQL

- Docker
  - `docker pull mysql:latest`
  - `docker run -p 3306:3306 --name sbs-mysql -e MYSQL_ROOT_PASSWORD={{ROOT PASSWORD}} -d mysql`
- Container
  - `mysql -uroot -p{{ROOT_PASSWORD}}`
    - `create user 'sbsadmin'@'%' identified by '{{ROOT_PASSWORD}}';`
    - `flush privileges;`
    - `create database skyblocksimplified;`
    - `grant all privileges on skyblocksimplified.* to 'sbsadmin'@'%';`
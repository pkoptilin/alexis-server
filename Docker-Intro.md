https://join.skype.com/pGX0Ja3g2Fc4

#Docker Sample

docker run --rm --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mypass -d mysql:5.6

#Docker Volumes
docker run --name mysql-demo -v "$PWD/data":/var/lib/mysql \
    -e MYSQL_ROOT_PASSWORD=mypass \
    -e MYSQL_DATABASE=demo \
    -e MYSQL_USER=demo \
    -e MYSQL_PASSWORD=demo \
    -d mysql:5.7

#Compose

docker-compose up -d


*docker-compose.yml*
```
version: '3.1'

services:

  wordpress:
    image: wordpress
    restart: always
    ports:
      - 8080:80
    environment:
      WORDPRESS_DB_HOST: db
      WORDPRESS_DB_USER: exampleuser
      WORDPRESS_DB_PASSWORD: examplepass
      WORDPRESS_DB_NAME: exampledb

  db:
    image: mysql:5.7
    restart: always
    environment:
      MYSQL_DATABASE: exampledb
      MYSQL_USER: exampleuser
      MYSQL_PASSWORD: examplepass
      MYSQL_RANDOM_ROOT_PASSWORD: '1'
```

#Java Image


docker run --rm -v "$PWD":/app -w /app openjdk:7 javac Main.java

docker build -t myapp .
 
*Dockerfile*
```
FROM openjdk:7
ADD Main.class /myapp
WORKDIR /myapp
CMD ["java", "Main"]

```

Entripoint

*Dockerfile*
```
FROM openjdk:7
COPY . /myapp
WORKDIR /myapp
ENTRYPOINT ["java", "Main"]
CMD ["Hello"]

```

docker build -t myapp . 

docker run myapp

docker run --rm myapp

#Spring
```Dockerfile
FROM openjdk:8

COPY target/*.jar /myapp

WORKDIR /myapp

EXPOSE 8080

ENTRYPOINT [ "/myapp/docker-entrypoint.sh"]
```

```docker-entrypoint.sh
#!/bin/bash
java -jar my-app-0.1-SNAPSHOT.jar $@
```

#Maven

```pom.xml

        <plugins>
            <plugin>
            <groupId>io.fabric8</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>0.27.1</version>
                <configuration>
                    <images>
                    <!-- A single's image configuration -->
                        <image>
                            <name>${docker.image.prefix}/app</name>
                            <build>
                                <dockerFileDir>${project.basedir}</dockerFileDir>
                                <filter>@</filter>
                                <assembly>
                                    <name>config</name>
                                    <mode>dir</mode>
                                    <descriptor>${project.basedir}/assembly/docker-assembly.xml</descriptor>
                                </assembly>
                            </build>
                        </image>
                    </images>
                </configuration>
                <executions>
                    <execution>
                        <id>build-image</id>
                        <phase>package</phase>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```

```docker-assembly.xml

<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.0.0 http://maven.apache.org/xsd/assembly-2.0.0.xsd">
    <id>iam-docker</id>
    <formats>
        <format>dir</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>

    <dependencySets>
        <dependencySet>
            <useTransitiveDependencies>false</useTransitiveDependencies>
        </dependencySet>
    </dependencySets>

    <fileSets>
        <fileSet>
            <directory>build</directory>
            <outputDirectory>.</outputDirectory>
            <includes>
                <include>**</include>
            </includes>
        </fileSet>
    </fileSets>
</assembly>
```
```Dockerfile
FROM openjdk:8

COPY config /myapp

WORKDIR /myapp

EXPOSE 8080

ENTRYPOINT [ "/myapp/docker-entrypoint.sh"]
```

--spring.config.location=/myapp/app.properties
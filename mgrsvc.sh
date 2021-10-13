mvn clean compile jib:dockerBuild
docker-compose -f src/main/docker/app.yml up -d
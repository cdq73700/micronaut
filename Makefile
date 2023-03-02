.PHONY: backend test

build:
	docker compose build
up:
	docker compose up -d
stop:
	docker compose stop
del:
	docker rm `docker ps -a -q`
	docker rmi `docker images -q`
ps:
	docker compose ps
backend:
	docker compose exec backend bash
backend-build:
	docker compose run --rm backend bash -c "./gradlew shadowJar"

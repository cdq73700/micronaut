FROM ubuntu:lunar-20230128

RUN apt update && apt upgrade -y

RUN apt-get update \
    && apt-get install -y curl zip unzip openjdk-17-jdk

WORKDIR /backend

RUN curl -s "https://get.sdkman.io" | bash
RUN /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh && sdk install micronaut"

COPY . .

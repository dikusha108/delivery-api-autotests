FROM docker.io/library/maven:3.8.6-jdk-11
MAINTAINER Alexander Yasko

RUN useradd -m -u 5000 allure && mkdir -p /usr/src/app

WORKDIR /usr/src/app
COPY --chown=5000:5000 . /usr/src/app

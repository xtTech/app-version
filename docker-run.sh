#!/usr/bin/env bash

mvn -Dmaven.test.skip=true clean install

cd modules/app-version-manager

mvn clean package -Dmaven.test.skip=true -P fatjar docker:build

cd ../app-version-rest

mvn clean package -Dmaven.test.skip=true -P fatjar docker:build

docker-compose up -d
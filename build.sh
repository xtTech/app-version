#!/usr/bin/env bash

mvn -Dmaven.test.skip=true clean package

cd modules/app-version-manager

mvn clean package -Dmaven.test.skip=true -P fatjar

cd ../app-version-rest

mvn clean package -Dmaven.test.skip=true -P fatjar
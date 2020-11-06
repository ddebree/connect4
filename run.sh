#!/usr/bin/env bash

./mvnw clean install

cat sample-data/Test_L1_R1 | java -jar target/connect4-0.0.1-SNAPSHOT.jar

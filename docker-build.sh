#!/bin/bash
## Remove existing docker images
docker image rm zeusprogetto/premium-billing
mvn clean package
docker build -t zeusprogetto/premium-billing:latest .
docker push zeusprogetto/premium-billing
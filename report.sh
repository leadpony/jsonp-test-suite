#!/bin/bash

mvn clean
mvn test -P test-with-jakarta
mvn test -P test-with-joy
mvn site -Dline.separator=$'\n'
cp -r target/site/* docs/

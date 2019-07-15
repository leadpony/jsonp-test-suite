#!/bin/bash

mvn clean
mvn test -P test-with-ri
mvn test -P test-with-johnzon
mvn site -Dline.separator=$'\n'
cp -r target/site/* docs/test-suite/

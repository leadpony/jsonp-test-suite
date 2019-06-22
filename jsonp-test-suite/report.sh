#!/bin/bash

mvn clean
mvn test -P test-with-ri
mvn test -P test-with-johnzon
mvn site

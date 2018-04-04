#!/bin/bash
java -jar lib/junit-platform-console-standalone-1.1.0.jar --cp "build/classes/java/main:build/classes/java/test" --scan-class-path

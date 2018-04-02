#!/bin/bash
java -cp "lib/*" org.junit.platform.console.ConsoleLauncher -cp "build/classes/test:build/classes/main" --scan-class-path

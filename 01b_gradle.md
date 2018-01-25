# Gradle demo

## Prep the directory
mkdir -p csci2020u/02_gradle
cd csci2020u

## Global Git config
git config --global user.email "randy.fortier@uoit.ca"
git config --global user.name "randyfortier"

## Create a repository
git init
git add .
git commit -m "Initial commit"

## Add a .gitignore file
gedit .gitignore
  *.class
  *.java~
git add .gitignore

## Create a repository on GitHub: csci2020u
git remote add origin https://github.com/randyfortier/csci2020u.git

## Push local repository to GitHub repository
git push -u origin master
(8 char)

## Create a project
cd 02_gradle
mkdir -p src/main/java/gradleDemo
gedit src/main/java/gradleDemo/Demo.java
 package gradleDemo;
 
 public class Demo {
	public static void main(String[] args) {
		System.out.println("Testing 1 2 3...");
	}
 }

gedit build.gradle
 apply plugin: 'java'

## Basic Java tasks 
gradle tasks
gradle classes

### Run the code by hand
cd build/classes/main
java gradleDemo.Demo
cd ../../..

## Commit changes, locally and remotely
git add build.gradle
git add src
git commit -m "Initial commit"
git push -u origin master
(8 char)

## Configure Gradle to generate a JAR file
gedit build.gradle
 apply plugin: 'java'

 sourceCompatibility = 1.7
 version = '1.0'
 jar {
    manifest {
        attributes  'Main-Class': 'Test',
                    'Implementation-Title': 'Sample-Gradle',
                    'Implementation-Version': version
    }
 }
gradle jar
ls build/libs
java -jar build/libs/gradle_test-1.0.jar

## Dependencies
gedit src/main/java/gradleDemo/Demo.java
 package gradleDemo;

 import org.apache.commons.validator.routines.*;

 public class Demo {
    static final String cardNum = "4716841076742166";
    
	public static void main(String[] args) {
		CreditCardValidator validator = new CreditCardValidator(CreditCardValidator.AMEX + CreditCardValidator.VISA);
        boolean valid = validator.isValid(cardNum);
        System.out.println("Is the credit card number valid? " + valid);
	}
 }

gedit build.gradle 
 apply plugin: 'java'

 repositories {
    mavenCentral()
 }

 dependencies {
   compile group: 'commons-validator', name: 'commons-validator', version: '1.4.1'
   compile files("C:/Dev/jdk1.8.0_51/lib/tools.jar")
 }
 sourceCompatibility = 1.7
 version = '1.0'
 jar {
    manifest {
        attributes  'Main-Class': 'Test',
                    'Implementation-Title': 'Sample-Gradle',
                    'Implementation-Version': version
    }
 }
gradle jar
ls ~/.gradle/caches/modules-2/files-2.1/commons-validator/...

### Save the changes
git commit -m "Initial commit"
git push -u origin master
(8 char)

## Custom tasks
gedit build.gradle
 task(sayHello) {
   doLast {
      println 'Hello from a custom task!'
   }
 }
gradle sayHello

### Dependent tasks
gedit build.gradle
 task(howAreYou, dependsOn: 'sayHello') {
   doLast {
      println "How are you?"
   }
 }
gradle howAreYou

### Run task
gedit build.gradle
 task(run, dependsOn: 'classes', type: JavaExec) {
   main = 'gradleDemo.Demo'
   classpath = sourceSets.main.runtimeClasspath
 }
gradle run

## Build tasks - ProGuard
- download ProGuard from http://sourceforge.net/projects/proguard/files/latest/download/source=files
- extract to /home/csci2020u
ls /home/csci2020u/proguard5.2.1/lib
gedit ~/.bashrc
 export proguard="/home/csci2020/proguard5.2.1/lib"
source ~/.bashrc
echo $proguard


gedit build.gradle
 buildscript {
   repositories {
      flatDir dirs: '/home/csci2020/proguard5.2.1/lib'
   }
   dependencies {
      classpath ':proguard:'
   }
 }
 ...
 task (minify, dependsOn: 'jar', type: proguard.gradle.ProGuardTask) {
   configuration 'proguard.cfg'
    
   injars 'build/libs/gradle_test-1.0.jar'
   outjars 'build/libs/gradle_test-1.0.min.jar'
 }

gedit proguard.cfg
 -libraryjars <java.home>/lib/rt.jar
 -printmapping gradeDemo.proguard.map
 -keep public class gradleDemo.Demo {
	public static void main(java.lang.String[]);
 }
 -dontwarn

gradle minify

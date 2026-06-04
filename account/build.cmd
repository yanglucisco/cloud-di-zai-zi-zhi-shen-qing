@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion

set JAVA_HOME=D:\java\jdk-21.0.10+7\
set PATH=D:\java\jdk-21.0.10+7\bin;%PATH%
java -version
mvn clean install
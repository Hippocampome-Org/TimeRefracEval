#!/bin/bash
export set JAVA_OPTS="-XX:+AggressiveHeap"
export CLASSPATH=$CLASSPATH:/home/nmsutton/Dropbox/CompNeuro/gmu/research/sim_project/code/Time/lib/commons-math3-3.6/*

javac TimeRefracEval.java

java TimeRefracEval
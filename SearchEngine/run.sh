#! /usr/bin/env bash
ant clean compile jar && java -jar ./build/jar/SearchEngine.jar $1 $2

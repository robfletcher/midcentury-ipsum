#!/bin/bash
echo "Setting environment variables..."
export JAVA_HOME=./.jdk7
export RATPACK_OPTS="-Dratpack.port=$PORT"
env

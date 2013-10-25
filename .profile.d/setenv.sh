#!/bin/bash
echo "Setting environment variables..."
export JAVA_HOME="$HOME/.jdk7"
export RATPACK_OPTS="-Dratpack.port=$PORT"

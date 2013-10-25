#!/bin/bash
echo "Setting environment variables..."
export JAVA_HOME="$BUILD_DIR/.jdk7"
echo "JAVA_HOME=$JAVA_HOME"
export RATPACK_OPTS="-Dratpack.port=$PORT"

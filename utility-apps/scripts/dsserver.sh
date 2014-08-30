#!/bin/bash

# use our local .m2 as our repo - we assume you have built the code
bracket=~/.m2/repository/asia/redact/bracket/properties/bracket-properties/1.3.6/bracket-properties-1.3.6.jar
je=~/.m2/repository/com/sleepycat/je/6.0.11/je-6.0.11.jar
jcore=~/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.2.3/jackson-core-2.2.3.jar
jdatabind=~/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.2.3/jackson-databind-2.2.3.jar
jannotations=~/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.2.3/jackson-annotations-2.2.3.jar
bcore=~/.m2/repository/com/cryptoregistry/buttermilk-core/1.0.0/buttermilk-core-1.0.0.jar
base64=~/.m2/repository/net/iharder/base64/2.3.8/base64-2.3.8.jar
proto=~/.m2/repository/com/google/protobuf/protobuf-java/2.5.0/protobuf-java-2.5.0.jar
protocol=~/.m2/repository/com/cryptoregistry/protocol-buffers/1.0.0/protocol-buffers-1.0.0.jar
client=~/.m2/repository/com/cryptoregistry/client-storage/1.0.0/client-storage-1.0.0.jar
utilapps=~/.m2/repository/com/cryptoregistry/utility-apps/1.0.0/utility-apps-1.0.0.jar
jgroups=~/.m2/repository/org/jgroups/jgroups/3.4.4.Final/jgroups-3.4.4.Final.jar

CLASSPATH=$bracket:$je:$jgroups:$jcore:$jdatabind:$jannotations:$bcore:$base64:$proto:$protocol:$client:$utilapps

#echo "$(cygpath -pw "$CLASSPATH")" 
echo 
java -cp "$(cygpath -pw "$CLASSPATH")" com.cryptoregistry.client.console.DSConsoleServer "$@"
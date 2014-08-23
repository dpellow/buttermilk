#!/bin/bash

# use our local .m2 as our repo - we assume you have built the code
bracket=~/.m2/repository/asia/redact/bracket/properties/bracket-properties/1.3.6/bracket-properties-1.3.6.jar
jcore=~/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.2.3/jackson-core-2.2.3.jar
jdatabind=~/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.2.3/jackson-databind-2.2.3.jar
jannotations=~/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.2.3/jackson-annotations-2.2.3.jar
bcore=~/.m2/repository/com/cryptoregistry/buttermilk-core/1.0.0/buttermilk-core-1.0.0.jar
utilapps=~/.m2/repository/com/cryptoregistry/utility-apps/1.0.0/utility-apps-1.0.0.jar

CLASSPATH=$bracket:$jcore:$jdatabind:$jannotations:$bcore:$utilapps

#echo "$(cygpath -pw "$CLASSPATH")" 
echo 
java -cp "$(cygpath -pw "$CLASSPATH")" com.cryptoregistry.utility.app.ObfuscateApp "$@"
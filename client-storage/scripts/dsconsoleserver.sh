#!/bin/bash

# use our local .m2 as our repo - we assume you have built the code
bracket=~/.m2/repository/asia/redact/bracket/properties/bracket-properties/1.3.5/bracket-properties-1.3.5.jar
cstorage=~/.m2/repository/com/cryptoregistry/client-storage/1.0.0/client-storage-1.0.0.jar
log4j=~/.m2/repository/log4j/log4j/1.2.16/log4j-1.2.16.jar
jgroups=~/.m2/repository/org/jgroups/jgroups/3.4.4.Final/jgroups-3.4.4.Final.jar

CLASSPATH=$bracket:$cstorage:$log4j:$jgroups

java -cp "$(cygpath -pw "$CLASSPATH")" com.cryptoregistry.client.console.DSConsoleServer "$@"
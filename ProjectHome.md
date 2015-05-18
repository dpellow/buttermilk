Buttermilk is a set of projects related to creating a new Public Key Infrastructure. It has new key formats, signature formats, secure protocols, utilities, etc.

The basic idea is that in the last 30 years a lot of cruft has accumulated in Crypto and it is high time for a clean out. Once the clean out has occurred, and the requirements are understood, then it is time to re-assess, to start fresh. At that stage I suspect more efficient and productive ways of working will materialize.

Most of the cruft comes through certain design choices made early on, such as using ASN-1 as the descriptive language for Cryptographic primitives. ASN-1 has some nice properties, like being self-describing, but it is over-kill in the general case and probably even in the complex case. For example, buttermilk at this early stage can already represent both Elliptic Curve Custom Curves and NTRU custom definitions without ASN-1. This would seem to imply ASN-1 descriptions are redundant.

Another crucial design decision was the heavy reliance on X.500 as a way to formulate X.509. X.509 is dependent on or least makes reference to users in an X.500 style Directory. A different model is now required, more appropriate to "Big Data."

Bottom line is that X.509 is hopelessly outdated. Worse, the built-in assumptions create constraints and problems at almost every turn.

The most important PKI design choice of all - the design of the certificate authority and chains of trust, including the heavy reliance on X.500 protocols - is something Buttermilk will challenge head-on. There are other models for how trust could be managed in the internet age and those will follow in due course once the other parts of the redesign are in place.

A side goal of the library is to remove the Java Cryptography Extension semantics. JCE is basically a layer of cruft on top of a crypto implementation but it contributes little value. It seems to be a vehicle for introducing government controls into the code. That's a side-issue but it is of concern. Mainly this creates technical hassles for users.

Technically the JCE relies on certain assumptions about X-509 and P8 formats. In fact these assumptiosn are crucial to the design. Buttermilk demonstrates that it is possible to have a simple and flexible programming model without the syntactic sugar of the JCE. This is achieved mainly through the convenience of the Bouncy Castle lightweight API with some simple factory classes built on top.

## Buttermilk Core ##

The buttermilk-core sub-project consists of core cryptography classes. It has two parts. The first is source from the bouncycastle lightweight crypto library which has all the ASN-1 related classes removed. This subset already has JCE extracted, so we are left with a relatively pure set of high quality crypto code.

The second part of the core is original code which implements some factory classes and other wrappers to make the BC crypto more usable. It also contains the JSON implementation of the new key and signature formats and provides readers and writers, etc.

## JSON Encodings ##

Buttermilk uses Javascript Object Notation (JSON) as the primary encoding for cryptographic data structures. Below is an Elliptic Curve cryptographic key pair in the Key Materials 1.0 format:

```
{
  "Version" : "Buttermilk Key Materials 1.0",
  "RegHandle" : "Chinese Eyes",
  "Keys" : {
    "6395515d-44a7-4f87-ba95-935bcbc2245e-U" : {
      "KeyAlgorithm" : "EC",
      "CreatedOn" : "2014-07-31T04:58:25+0000",
      "Encoding" : "Base64url",
      "Q" : "AMtsJYpG0HyVFxcFdSDjHEG11alPmov5gJt6SbYs_zwn,Zh3rnv2MHA57vmt2o2T_EFUWoHcNXBB2RgtTgU2-QO8=",
      "D" : "AN2OqIf2CU_aO_G8YyEyvAvdvyZJXO9j1Ffsre_R1b_e",
      "CurveName" : "P-256"
    }
  }
}
```

Here's what the same key looks like in encrypted form (suitable for private key distribution or local file storage, but not publication):

```
{
  "Version" : "Buttermilk Key Materials 1.0",
  "RegHandle" : "Chinese Eyes",
  "Keys" : {
    "6395515d-44a7-4f87-ba95-935bcbc2245e-S" : {
      "KeyData.Type" : "EC",
      "KeyData.PBEAlgorithm" : "PBKDF2",
      "KeyData.EncryptedData" : "N2vGIBbOqjUd1qQsdNHxHo7B7mHqxi0indiY6VSknm7Ka2zJbFL_ga4QgCHvnYjCXZb3SAvbHKAhpbuhRquxMyrR4Cn6GWURNuKQlUeeJyydXIVCslrkuJR2WyRXYFVkzIFpSK_7EtMM0gFctMgv8AjejFGQf1TeOe3v8o_SRw9ER12rKwUgYsUnR7Ta1qDv24nNN9AAi2xAGQ0s_6ZHSW6o3KenoCB2W2dsD3tcrLn7t45jEpcevrlVZaqsa4VUmNxDoMnx5-cV1LXsFfyGSh_hOsexahjpF_goN9VA4vW4dXPlBzgDPQ13IcE1M3G9klioZhdrm4ZorNQ9F94VRt7SaA6Z_1s3tjKADqUSW3I6Lh_M0trJzGIf3tb1EuBzvIUk8y3wXgbtDyAOBj10WJPMuoZ2oQEZgh4k5oOZYca2AWcUJjKH54mSAzpQk6tZkXlrOE_YWebfCdY0SK4R3HfXCUU2pGTMYE-F4ieQ5Gw=",
      "KeyData.PBESalt" : "QDjzMa_tJUp4zOL0mzuAB8vKgy3TBXINbo0wOgvvaxQ=",
      "KeyData.Iterations" : "10000"
    }
  }
}
```

What about the public key, or what we call the key for publication? It is a subset of the above which looks like this:

```
{
  "Version" : "Buttermilk Key Materials 1.0",
  "RegHandle" : "Chinese Eyes",
  "Keys" : {
    "6395515d-44a7-4f87-ba95-935bcbc2245e-P" : {
      "KeyAlgorithm" : "EC",
      "CreatedOn" : "2014-07-31T04:58:25+0000",
      "Encoding" : "Base64url",
      "Q" : "AMtsJYpG0HyVFxcFdSDjHEG11alPmov5gJt6SbYs_zwn,Zh3rnv2MHA57vmt2o2T_EFUWoHcNXBB2RgtTgU2-QO8=",
      "CurveName" : "P-256"
    }
  }
}
```

Here's a simple key generation routine from buttermilk-core for the above:

```

String curveName = "P-256";
ECKeyContents ecc = CryptoFactory.INSTANCE.generateKeys(curveName);
String registrationHandle = "Chinese Eyes";
JSONFormatter f = new JSONFormatter(registrationHandle);
f.add(ecc);
StringWriter writer = new StringWriter();
f.format(writer);
System.out.println(writer.toString());

```

## Protocol Buffers Sub-project ##

Because in many situations it is desirable to have a binary format for key materials, Buttermilk provides Google Protocol Buffers wrappers for all the various data structures. These live in the protocol-buffers sub-project. The wrappers allow very compact binary representations, while preserving the ability to work with JSON as our primary encoding. You can think of the protocol buffers as an "implementation specific" way of working with the key materials.

Here is an example of using protocol buffers for a key similar to the one above:

```
// generate a new key and create a Google protocol Buffer object from it:
final String curveName = "P-256"; 
ECKeyContents contents = CryptoFactory.INSTANCE.generateKeys(curveName);
ECKeyContentsProtoBuilder builder = new ECKeyContentsProtoBuilder(contents);
ECKeyContentsProto keyProto = builder.build();
		
// convert the key into a compact binary representation:
byte [] keyBytes = keyProto.toByteArray();
		
// load the encoding as a java object instance using a reader
ECKeyContentsProto keyProtoIn = ECKeyContentsProto.parseFrom(keyBytes);
ECKeyContentsProtoReader reader = new ECKeyContentsProtoReader(keyProtoIn);
ECKeyContents key = (ECKeyContents) reader.read();
```

## Key and Signature Storage ##

One of the worst outcomes of X.509 has been the unfortunate "keystore" formats like .p12 or .jks and all the associated hassles. To solve these problems Buttermilk offers a high performance secure datastore built on Oracle's Berkeley DB Java Edition. It can scale to millions of keys. The datastore implementation is in the client-storage sub-project.

## Protocols and bTLS ##

Buttermilk describes and implements an experimental handshake protocol similar in design to SSL/TLS but without X509 as the linch pin. This protocol is called bTLS (pronounced "beatles") and lives in the bTLS sub-project. It has dependencies on buttermilk-core, client-storage, and the protocol-buffers project.





See http://arxiv.org/abs/1305.0954 for algorithm details

Buttermilk contains the first open source java implementation of G J Croll's BiEntropy Algorithm. Binary Entropy is an entropy measure of actual bits, rather than characters. This is very useful for password analysis or work in the context of key stretching and password-based encryption (PBEs).

From the command line:

For individual character bientropy (one byte), the BiEntropy algorithm computes a power series:

```

bientropy.sh -a x
{
  "version" : "Buttermilk BiEntropy v1.0",
  "algorithm" : "BiEntropy",
  "input" : "x",
  "bits" : "[0, 1, 1, 1, 1, 0, 0, 0]",
  "biEntropy" : "0.11",
  "bitsOfEntropy" : 1
}
```

This is useful for academic work. For practical analysis of arbitrary length strings we use the TresBiEntropy variant (which is invoked when the input is more than one byte):

```

bientropy.sh -a password1

{
  "version" : "Buttermilk BiEntropy v1.0",
  "algorithm" : "TresBiEntropy",
  "input" : "password1",
  "biEntropy" : "0.92",
  "bitsOfEntropy" : 66
}
```

TresBiEntropy uses a logarithmic summation rather than a power series.

Here's some source code for this stuff:

http://buttermilk.googlecode.com/svn/trunk/buttermilk-core/src/main/java/com/cryptoregistry/util/entropy/

http://buttermilk.googlecode.com/svn/trunk/utility-apps/src/main/java/com/cryptoregistry/utility/app/BiEntropy.java



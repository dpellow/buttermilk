package x.org.bouncycastle.crypto.ec;


import x.org.bouncycastle.crypto.CipherParameters;
import x.org.bouncycastle.math.ec.ECPoint;

public interface ECDecryptor
{
    void init(CipherParameters params);

    ECPoint decrypt(ECPair cipherText);
}

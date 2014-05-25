package x.org.bouncycastle.crypto.ec;


import x.org.bouncycastle.crypto.CipherParameters;
import x.org.bouncycastle.math.ec.ECPoint;

public interface ECEncryptor
{
    void init(CipherParameters params);

    ECPair encrypt(ECPoint point);
}

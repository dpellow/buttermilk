package com.cryptoregistry.btls.handshake.validator.digest;

import java.io.EOFException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.btls.BTLSProtocol;
import com.cryptoregistry.btls.handshake.Handshake;
import com.cryptoregistry.btls.handshake.kem.ExchangeFailedException;
import com.cryptoregistry.btls.handshake.validator.DigestValidationListener;
import com.cryptoregistry.btls.handshake.validator.ValidationEvent;
import com.cryptoregistry.proto.frame.btls.HandshakeDigestOutputFrame;
import com.cryptoregistry.proto.reader.HandshakeDigestProtoReader;
import com.cryptoregistry.protos.Buttermilk.HandshakeDigestProto;
import com.google.protobuf.ByteString;

public class Sha256DigestValidator extends BaseDigestValidator {

	private Handshake handshake;
	static final Logger logger = LogManager.getLogger(Sha256DigestValidator.class.getName());
	
	public Sha256DigestValidator(Handshake handshake) {
		this.handshake = handshake;
	}

	@Override
	public void validate() {
		
		// get the digests at this point in the comms
		byte [] inputDigest = handshake.getDin().getDigest();
		handshake.getDin().reset();
		byte [] outputDigest = handshake.getDout().getDigest(); 
		handshake.getDout().reset();
		
		// send digest of what we ourselves have communicated so far
		writeSendDigestFrame(outputDigest); 
		try {
			//read what he says he's communicated so far
			byte [] remoteDigest = readDigestFrame();
			
			// compare what I sent with what he says I sent
			boolean ok = Arrays.areEqual(inputDigest, remoteDigest);
			for(DigestValidationListener l: this.validationListeners){
				l.digestComparisonCompleted(new ValidationEvent(ok));
			}
		} catch (ExchangeFailedException e) {
			e.printStackTrace();
		}
	}
	
	private int readShort16() throws IOException {
		int ch1 = handshake.getIn().read();
		int ch2 = handshake.getIn().read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return ((ch1 << 8) + (ch2 << 0));
	}
	
	private void readFully(byte b[], int off, int len)
			throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = handshake.getIn().read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}
	
	protected void writeSendDigestFrame(byte [] digest){
			HandshakeDigestOutputFrame frame = new HandshakeDigestOutputFrame(
					BTLSProtocol.HANDSHAKE,
					BTLSProtocol.SEND_DIGEST,
					digest);
			frame.writeFrame(handshake.getOut());
	}
	
	protected byte [] readDigestFrame() throws ExchangeFailedException{
		
		int subcode = consumeStartOfFrame();
		
		// TODO check subcode
		
		int sz;
		try {
			sz = readShort16();
			byte [] b = new byte[sz];
			readFully(b, 0, sz);
			HandshakeDigestProto proto = HandshakeDigestProto.parseFrom(ByteString.copyFrom(b));
			HandshakeDigestProtoReader reader = new HandshakeDigestProtoReader(proto);
			return reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
		
	}
	
	private int consumeStartOfFrame() throws ExchangeFailedException {
		int code = -1;
		try {
			code = handshake.readByte();
			if(code == -1) throw new ExchangeFailedException("Got -1 from readByte...");
			if(code != BTLSProtocol.HANDSHAKE) {
				if(code == BTLSProtocol.ALERT){
					logger.error("Got an alert!");
				}else if(code == BTLSProtocol.HEARTBEAT){
					logger.error("Got a heartbeat!");
				}
			}else{
				// handshake, return subcode
				int subcode = handshake.readShort16();
				return subcode;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return -1;
	}

}

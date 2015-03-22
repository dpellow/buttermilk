package com.cryptoregistry.handle;

/**
 * This structure is handle.[attributeName].[index] 
 * 
 * @author Dave
 *
 */
public class DistinguishedHandle {

	Handle handle;
	String attributeName;
	String index;
	
	public DistinguishedHandle(String in) {
		parse(in);
	}
	
	public DistinguishedHandle(Handle handle, String attributeName, String index) {
		super();
		this.handle = handle;
		this.attributeName = attributeName;
		this.index = index;
	}



	public boolean isSimpleHandle() {
		return attributeName == null && index == null && handle != null;
	}
	
	public boolean isAttributeOnly() {
		return attributeName != null && handle == null;
	}
	
	public String getAttrib() {
		if(index == null) return attributeName;
		else return attributeName+"."+index;
	}
	
	private void parse(String in){
		boolean hasPeriod = in.contains(".");
		if(!hasPeriod) {
			// if it has no periods at all, it must be invalid
			throw new RuntimeException("Does not look valid, no full stops");
		}else{
			String [] items = in.split("\\.");
			if(items.length == 3){
				handle = CryptoHandle.parseHandle(items[0]);
				attributeName=items[1];
				index = items[2];
				
			}else if(items.length == 2){
				handle = CryptoHandle.parseHandle(items[0]);
				attributeName = items[1];
			}else if(items.length == 1){
				handle = CryptoHandle.parseHandle(items[0]);
			}
		}
	}

	public Handle getHandle() {
		return handle;
	}

	public void setHandle(Handle handle) {
		this.handle = handle;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}

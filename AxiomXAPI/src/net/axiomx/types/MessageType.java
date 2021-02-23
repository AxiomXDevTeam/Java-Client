package net.axiomx.types;


public enum MessageType {
    CREDENTIALS("CRED"),
    PING("P"),
    INFO("I"),
    ERROR("E"),
    HIST_OPT_DATA_REQ("HODR"),
    HIST_OPT_DATA_CB("HODC"),
    HIST_OPT_DATA_CB_OBJ("HODC"),
    HIST_OPT_DATA_END("HODE"),
	ALL_EXPS("AE"),
	PACKET("PA"),
	ALL_STRIKES("AS");
	
    private final String text;

    MessageType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    
    public boolean equals(MessageType other) {
    	return other.toString().equals(this.toString());
    }
    
	public static MessageType from(String s) {
		
		for(MessageType t : MessageType.values()) 
			if(t.toString().equalsIgnoreCase(s))
				return t;
		
		return null;
	}

}

package net.axiomx.types;


public enum MessageType {
    CREDENTIALS("CRED"),
    PING("P"),
    HIST_OPT_DATA_REQ("HODR"),
    HIST_OPT_DATA_CB("HODC"),
    HIST_OPT_DATA_END("HODE"),
	ALL_EXPS("AE"),
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

}

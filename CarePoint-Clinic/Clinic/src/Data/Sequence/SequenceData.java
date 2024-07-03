package Data.Sequence;

import Data.Data;

public class SequenceData extends Data {
    private final String connectedFileName;
    private final String pattern;
    private String nextval;

    public SequenceData (String id, String connectedFileName, String pattern, String nextval) {
        this.id = id;
        this.connectedFileName = connectedFileName;
        this.pattern = pattern;
        this.nextval = nextval;
    }

    public void setNextVal() {
        this.nextval = String.valueOf(Integer.parseInt(this.nextval) + 1);
    }

    public String getConnectedFileName() {
        return connectedFileName;
    }

    public String getPattern() {
        return pattern;
    }

    public String getNextval() {
        return nextval;
    }
}

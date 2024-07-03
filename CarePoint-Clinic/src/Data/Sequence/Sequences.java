package Data.Sequence;

import File.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sequences {
    private List<String> sequenceHeaders;
    private final List<SequenceData> sequenceList = new ArrayList<>();

    public Sequences() {
        File.readFile(this);
    }

    public void addSequenceHeader(String header) {
        sequenceHeaders = Arrays.asList(header.split(","));
    }

    public void addSequenceData (SequenceData sequence) {
        sequenceList.add(sequence);
    }

    public List<String> getSequenceHeaders() {
        return sequenceHeaders;
    }

    public List<SequenceData> getSequenceList() {
        return sequenceList;
    }

    public String querySequenceId(String fileName) {
        for (SequenceData sequence : sequenceList) {
            if (sequence.getConnectedFileName().equals(fileName)) {
                return sequence.getPattern() + sequence.getNextval();
            }
        }
        return null;
    }

    public void updateId(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", sequenceHeaders)).append("\n");
        for (SequenceData sequence : sequenceList) {
            if (sequence.getConnectedFileName().equals(fileName)) {
                sequence.setNextVal();
            }
            stringBuilder.append(sequence.getId()).
                    append(",").
                    append(sequence.getConnectedFileName()).
                    append(",").
                    append(sequence.getPattern())
                    .append(",")
                    .append(sequence.getNextval())
                    .append("\n");
        }

        File.writeFile("sequence.txt", stringBuilder.toString());
    }
}

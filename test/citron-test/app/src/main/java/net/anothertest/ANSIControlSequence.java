package net.anothertest;

public class ANSIControlSequence {
    private final char command;

    private final String[] parameters;

    public ANSIControlSequence(char c, String[] ps) {
        if (ps == null)
            throw new NullPointerException("ps");
        command = c;
        if (ps.length == 0 && ps[0].equals(""))
            parameters = new String[0];
        else
            parameters = ps.clone();
    }

    public char getCommand() {
        return command;
    }

    public String[] getParameters() {
        return parameters;
    }
}

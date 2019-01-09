package net.anothertest;

public interface AnsiControlSequenceListener {
    /**
     * Called when a control sequence has been parsed.
     *
     * @param seq The control sequence.
     */
    void parsedControlSequence(ANSIControlSequence seq);

    /**
     * Called when a string has been parsed.
     *
     * @param str The string.
     */
    void parsedString(String str);
}
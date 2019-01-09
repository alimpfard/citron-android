package net.anothertest;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class AnsiControlSequenceParser {
    /**
     * The multi-byte control sequence introducer.
     */
    private static final char[] MULTI_CSI = new char[]{27, '['};

    /**
     * The single-byte control sequence introducer.
     */
    private static final char SINGLE_CSI = 155;

    /**
     * The buffer of data from the last call to {@link #parse()}. This is
     * populated with data if an escape sequence is not complete.
     */
    private StringBuilder buffer = new StringBuilder();

    /**
     * The ANSI control sequence listener.
     */
    private final AnsiControlSequenceListener listener;

    /**
     * Creates the ANSI control sequence parser.
     *
     * @param listener The listener.
     */
    public AnsiControlSequenceParser(AnsiControlSequenceListener listener) {
        this.listener = listener;
    }

    /**
     * Parses the specified string.
     *
     * @param str The string to parse.
     */
    public void parse(String str) {
        if (buffer.length() > 0) {
            str = buffer.toString().concat(str);
            buffer = new StringBuilder();
        }
        Reader reader = new StringReader(str);
        try {
            try {
                parse(reader);
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            /* ignore */
            ex.printStackTrace();
        }
    }

    /**
     * Parses characters from the specified character reader.
     *
     * @param reader The character reader.
     * @throws IOException if an I/O error occurs.
     */
    private void parse(Reader reader) throws IOException {
        StringBuilder text = new StringBuilder();
        int character;
        while ((character = reader.read()) != -1) {
            boolean introducedControlSequence = false;
            if (character == SINGLE_CSI) {
                introducedControlSequence = true;
            } else if (character == MULTI_CSI[0]) {
                int nextCharacter = reader.read();
                if (nextCharacter == -1) {
                    buffer.append((char) character);
                    break;
                } else if (nextCharacter == MULTI_CSI[1]) {
                    introducedControlSequence = true;
                } else {
                    text.append((char) character);
                    text.append((char) nextCharacter);
                }
            } else {
                text.append((char) character);
            }

            if (introducedControlSequence) {
                if (text.length() > 0) {
                    listener.parsedString(text.toString());
                    text = new StringBuilder();
                }
                parseControlSequence(reader);
            }
        }

        if (text.length() > 0) {
            listener.parsedString(text.toString());
        }
    }

    /**
     * Parses a control sequence.
     *
     * @param reader The character reader.
     * @throws IOException if an I/O error occurs.
     */
    private void parseControlSequence(Reader reader) throws IOException {
        boolean finishedSequence = false;
        StringBuilder parameters = new StringBuilder();
        int character;
        while ((character = reader.read()) != -1) {
            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
                String[] array = parameters.toString().split(";");
                ANSIControlSequence seq = new ANSIControlSequence((char) character, array);
                listener.parsedControlSequence(seq);

                finishedSequence = true;
                break;
            } else {
                parameters.append((char) character);
            }
        }
        if (!finishedSequence) {
            // not an ideal solution if they used the two byte CSI, but it's
            // easier and cleaner than keeping track of it
            buffer.append((char) SINGLE_CSI);
            buffer.append(parameters);
        }
    }
}

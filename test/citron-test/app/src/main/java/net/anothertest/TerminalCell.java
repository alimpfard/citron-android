package net.anothertest;

class TerminalCell {
    /**
     * The character.
     */
    private final char character;

    /**
     * The background color.
     */
    private final int backgroundColor;

    /**
     * The foreground color.
     */
    private final int foregroundColor;

    /**
     * Creates a terminal cell with the specified character, background color
     * and foreground color.
     * @param character The character.
     * @param backgroundColor The background color.
     * @param foregroundColor The foreground color.
     * @throws NullPointerException if the background or foreground color(s)
     * are {@code null}.
     */
    public TerminalCell(char character, int backgroundColor, int foregroundColor) {
        this.character = character;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    /**
     * Gets the character.
     * @return The character.
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Gets the background color.
     * @return The background color.
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Gets the foreground color.
     * @return The foreground color.
     */
    public int getForegroundColor() {
        return foregroundColor;
    }

}

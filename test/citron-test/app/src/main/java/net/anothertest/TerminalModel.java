package net.anothertest;

public interface TerminalModel {
    /**
     * Gets the bell strategy.
     *
     * @return The bell strategy.
     */
    public BellStrategy getBellStrategy();

    /**
     * Sets the bell strategy.
     *
     * @param strategy The bell strategy.
     * @throws NullPointerException if the strategy is {@code null}.
     */
    public void setBellStrategy(BellStrategy strategy);

    /**
     * Clears the terminal.
     */
    public void clear();

    /**
     * Moves the cursor back n characters.
     *
     * @param n The number of characters.
     * @throws IllegalArgumentException if n is not positive.
     */
    public void moveCursorBack(int n);

    /**
     * Moves the cursor forward n characters.
     *
     * @param n The number of characters.
     * @throws IllegalArgumentException if n is not positive.
     */
    public void moveCursorForward(int n);

    /**
     * Moves the cursor down n characters.
     *
     * @param n The number of characters.
     * @throws IllegalArgumentException if n is not positive.
     */
    public void moveCursorDown(int n);

    /**
     * Moves the cursor up n characters.
     *
     * @param n The number of characters.
     * @throws IllegalArgumentException if n is not positive.
     */
    public void moveCursorUp(int n);

    /**
     * Sets a cell.
     *
     * @param column The column.
     * @param row    The row.
     * @param cell   The cell.
     * @throws IndexOutOfBoundsException if the column and/or row number(s) are
     *                                   out of bounds.
     */
    public void setCell(int column, int row, TerminalCell cell);

    /**
     * Gets a cell.
     *
     * @param column The column.
     * @param row    The row.
     * @return The cell.
     * @throws IndexOutOfBoundsException if the column and/or row number(s) are
     *                                   out of bounds.
     */
    public TerminalCell getCell(int column, int row);

    /**
     * Prints the specified string to the terminal at the cursor position,
     * interpreting any escape sequences/special ASCII codes the model may
     * support. Lines will be wrapped if necessary.
     *
     * @param str The string to print.
     * @throws NullPointerException if the string is {@code null}.
     */
    public void print(String str);

    /**
     * Gets the number of columns.
     *
     * @return The number of columns.
     */
    public int getColumns();

    /**
     * Gets the number of rows.
     *
     * @return The number of rows.
     */
    public int getRows();

    /**
     * Gets the buffer size.
     *
     * @return The buffer size.
     */
    public int getBufferSize();

    /**
     * Gets the cursor row.
     *
     * @return The cursor row.
     */
    public int getCursorRow();

    /**
     * Sets the cursor row.
     *
     * @param row The cursor row.
     * @throws IllegalArgumentException if the row is out of the valid range.
     */
    public void setCursorRow(int row);

    /**
     * Gets the cursor column.
     *
     * @return The cursor column.
     */
    public int getCursorColumn();

    /**
     * Sets the cursor column.
     *
     * @param column The cursor column.
     * @throws IllegalArgumentException if the column is out of the valid range.
     */
    public void setCursorColumn(int column);

    /**
     * Gets the default background color.
     *
     * @return The default background color.
     */
    public int getDefaultBackgroundColor();

    /**
     * Gets the default foreground color.
     *
     * @return The default foreground color.
     */
    public int getDefaultForegroundColor();
}

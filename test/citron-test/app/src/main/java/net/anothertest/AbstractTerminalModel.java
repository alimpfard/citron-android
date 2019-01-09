package net.anothertest;

public abstract class AbstractTerminalModel implements TerminalModel {
    @Override
    public void clear() {
        int rows = getRows(), columns = getColumns();
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                setCell(column, row, null);
            }
        }
    }

    @Override
    public void moveCursorBack(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        int cursorColumn = getCursorColumn() - n;
        if (cursorColumn < 0) {
            cursorColumn = 0;
        }
        setCursorColumn(cursorColumn);
    }

    @Override
    public void moveCursorForward(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        int columns = getColumns();
        int cursorColumn = getCursorColumn() + n;
        if (cursorColumn >= columns) {
            cursorColumn = columns - 1;
        }
        setCursorColumn(cursorColumn);
    }

    @Override
    public void moveCursorDown(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        int bufferSize = getBufferSize();
        int cursorRow = getCursorRow() + n;
        if (cursorRow >= bufferSize) {
            cursorRow = bufferSize - 1;
        }
        setCursorRow(cursorRow);
    }

    @Override
    public void moveCursorUp(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        int cursorRow = getCursorRow() - n;
        if (cursorRow < 0) {
            cursorRow = 0;
        }
        setCursorRow(cursorRow);
    }
}

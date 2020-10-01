package com.omvoid.jmqsc;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @Test
    void parseSimpleLinesTest() throws IOException {
        CommandParser parser = getParser("line1\nline2");
        assertEquals("line1", parser.nextCommand());
        assertEquals("line2", parser.nextCommand());
        assertNull(parser.nextCommand());
    }

    @Test
    void parseEmptyLinesTest() throws IOException {
        CommandParser parser = getParser("\nline1\n   \nline2");
        assertEquals("line1", parser.nextCommand());
        assertEquals("line2", parser.nextCommand());
        assertNull(parser.nextCommand());
    }

    @Test
    void parseCommentsLinesTest() throws IOException {
        CommandParser parser = getParser("*\nline1\n\n*   \nline2");
        assertEquals("line1", parser.nextCommand());
        assertEquals("line2", parser.nextCommand());
        assertNull(parser.nextCommand());
    }

    @Test
    void parsePlusSignTest() throws IOException {
        CommandParser parser = getParser("line1 +\n   line2\nline3   +\n+\nline4");
        assertEquals("line1 line2", parser.nextCommand());
        assertEquals("line3   line4", parser.nextCommand());
        assertNull(parser.nextCommand());
    }

    @Test
    void parseMinusSignTest() throws IOException {
        CommandParser parser = getParser("line1 -\n   line2\nline3   -\n-\nline4");
        assertEquals("line1    line2", parser.nextCommand());
        assertEquals("line3   line4", parser.nextCommand());
        assertNull(parser.nextCommand());
    }

    @Test
    void unexpectedEndOfCommandTest() {
        try {
            CommandParser parser = getParser("line1 +\n*\n");
            parser.nextCommand();
            assert false;
        } catch (IOException e) {
            assertEquals("Unexpected 'end of input' in MQSC.", e.getMessage());
        }
    }

    @Test
    void commandWithSemicolonTest() throws IOException {
        CommandParser parser = getParser("line1 +\nline2;\nline3 +\n;\n");
        assertEquals("line1 line2", parser.nextCommand());
        assertEquals("line3 ", parser.nextCommand());
        assertNull(parser.nextCommand());
    }

    private CommandParser getParser(String commands) {
        return new CommandParser(new ByteArrayInputStream(commands.getBytes()));
    }
}
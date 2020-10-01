package com.omvoid.jmqsc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * Parsing stream to mqsc commands
 * Supports all features described in
 * https://www.ibm.com/support/knowledgecenter/SSFKSJ_9.0.0/com.ibm.mq.ref.adm.doc/q085110_.htm
 *
 */
public class CommandParser {

    private final BufferedReader reader;

    public CommandParser(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream must be not null");
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public List<String> parseAllCommands() throws IOException {
        ArrayList<String> commands = new ArrayList<>();
        String nextCommand;
        while((nextCommand = nextCommand()) != null) {
            commands.add(nextCommand);
        }
        return commands;
    }

    public String nextCommand() throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean trimLeft = true;
        boolean requireNotEmptyLine = false;
        String nextLine;
        while((nextLine = reader.readLine()) != null) {
            if (nextLine.startsWith("*")) {
                continue;
            }
            if(nextLine.trim().length() == 0) {
                continue;
            }
            if(trimLeft) {
                nextLine = trimLeft(nextLine);
            }
            int lastCharIndex = lastNonWhitespaceCharIndex(nextLine);
            char lastChar = nextLine.charAt(lastCharIndex);
            trimLeft = (lastChar == '+');
            requireNotEmptyLine = (lastChar == '+' || lastChar == '-');
            if(lastChar == '+' || lastChar == '-' || lastChar == ';') {
                nextLine = nextLine.substring(0, lastCharIndex);
            }
            sb.append(nextLine);
            if(!requireNotEmptyLine) {
                break;
            }
        }
        if(requireNotEmptyLine) {
            throw new EOFException("Unexpected 'end of input' in MQSC.");
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    private String trimLeft(String value) {
        int index = 0;
        int length = value.length();
        while(index < length && Character.isWhitespace(value.codePointAt(index))) {
            index++;
        }
        return value.substring(index);
    }

    private int lastNonWhitespaceCharIndex(String value) {
        if(value == null || value.length() == 0) {
            return 0;
        }
        int index = value.length() - 1;
        while(index >= 0 && Character.isWhitespace(value.codePointAt(index))) {
            index--;
        }
        return index;
    }

}

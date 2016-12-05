package com.bunjlabs.fuga.templates;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TemplateReader {

    private final Reader reader;
    private int ch;

    public TemplateReader(Reader reader) {
        this.reader = reader;
    }

    public Token next() throws TemplateCompileException {
        while (read() >= 0) {
            if (Character.isWhitespace((char) ch)) {
                continue;
            } else if (ch == '@') {
                read();
                return keyword();
            } else {
                return token(Token.ERROR);
            }
        }
        return token(Token.EOS);
    }

    private int read() throws TemplateCompileException {
        try {
            return ch = reader.read();
        } catch (IOException ex) {
            throw new TemplateCompileException("Unable to read tempalte", ex);
        }
    }

    private Token token(int type, String... args) {
        return new Token(type, args);
    }

    private Token keyword() throws TemplateCompileException {
        StringBuilder sb = new StringBuilder();

        while (ch >= 0 && !Character.isWhitespace((char) ch) && ch != '(') {
            sb.append((char) ch);
            read();
        }

        String keyword = sb.toString();

        // skip space
        while (ch >= 0 && Character.isWhitespace((char) ch)) {
            read();
        }

        if (ch != '(') {
            throw new TemplateCompileException("Unexpected symbol: " + ((char) ch));
        }

        read(); // skip '('

        sb.setLength(0);

        List<String> args = new ArrayList<>();

        while (true) {
            while (ch >= 0 && ch != ',' && ch != ')') {
                sb.append((char) ch);
                read();
            }

            if (sb.length() > 0) {
                args.add(sb.toString());
            }

            sb.setLength(0);

            if (ch == ')') {
                read();
                break;
            } else if (ch == ',') {
                read();
                continue;
            } else {
                throw new TemplateCompileException("Unexpected end of file");
            }
        }

        sb.setLength(0);

        if (keyword.equals("use")) {
            if (args.size() < 1) {
                throw new TemplateCompileException("At least one argument is required for 'use'");
            }
            return token(Token.USE, args.get(0));
        }

        if (keyword.equals("extends")) {
            if (args.size() < 1) {
                throw new TemplateCompileException("At least one argument is required for 'extends'");
            }
            return token(Token.EXTENDS, args.get(0));
        }

        if (keyword.equals("block")) {
            if (args.size() < 1) {
                throw new TemplateCompileException("At least one argument is required for 'block'");
            }

            while (true) {
                if (ch == '@') {
                    StringBuilder sbb = new StringBuilder();
                    sbb.append((char) ch);

                    boolean falseAlarm = false;
                    String endblock = "endblock";
                    for (int i = 0; i < endblock.length(); i++) {
                        if (read() != endblock.charAt(i)) {
                            falseAlarm = true;
                            break;
                        }
                    }

                    if (falseAlarm) {
                        sb.append(sbb.toString());

                        continue;
                    }

                    read();

                    return token(Token.BLOCK, args.get(0), sb.toString());

                }
                sb.append((char) ch);
                read();
            }
        }

        if (keyword.equals("tag")) {
            if (args.size() < 1) {
                throw new TemplateCompileException("At least one argument is required for 'tag'");
            }

            while (true) {
                if (ch == '@') {
                    StringBuilder sbb = new StringBuilder();
                    sbb.append((char) ch);

                    boolean falseAlarm = false;
                    String endblock = "endtag";
                    for (int i = 0; i < endblock.length(); i++) {
                        if (read() != endblock.charAt(i)) {
                            falseAlarm = true;
                            break;
                        }
                    }

                    if (falseAlarm) {
                        sb.append(sbb.toString());

                        continue;
                    }

                    read();

                    return token(Token.TAG, args.get(0), sb.toString());

                }
                sb.append((char) ch);
                read();
            }
        }

        if (keyword.equals("code")) {
            while (true) {
                if (ch == '@') {
                    StringBuilder sbb = new StringBuilder();
                    sbb.append((char) ch);

                    boolean falseAlarm = false;
                    String endblock = "endcode";
                    for (int i = 0; i < endblock.length(); i++) {
                        if (read() != endblock.charAt(i)) {
                            falseAlarm = true;
                            break;
                        }
                    }

                    read();

                    if (falseAlarm) {
                        sb.append(sbb.toString());

                        continue;
                    }

                    return token(Token.CODE, sb.toString());

                }
                sb.append((char) ch);
                read();
            }
        }

        return token(Token.ERROR);
    }

    public static class Token {

        public static final int EXTENDS = -1;
        public static final int USE = -1;
        public static final int BLOCK = -3;
        public static final int CODE = -4;
        public static final int TAG = -5;
        public static final int EOS = -10;
        public static final int ERROR = -11;
        public final int type;
        public String[] args;

        public Token(int type) {
            this.type = type;
        }

        public Token(int type, String[] args) {
            this.type = type;
            this.args = args;
        }

    }
}

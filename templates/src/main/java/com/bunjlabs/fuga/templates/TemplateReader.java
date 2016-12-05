package com.bunjlabs.fuga.templates;

import java.io.IOException;
import java.io.Reader;

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

    public Token next() throws IOException {
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

    private int read() throws IOException {
        return ch = reader.read();
    }

    private Token token(int type, String... args) {
        return new Token(type, args);
    }

    private Token keyword() throws IOException {
        StringBuilder sb = new StringBuilder();

        while (ch >= 0 && !Character.isWhitespace((char) ch)) {
            sb.append((char) ch);
            read();
        }

        String keyword = sb.toString();

        if (keyword.equals("use") || keyword.equals("extends")) {
            sb.setLength(0);

            read(); // skip space

            while (ch >= 0 && !Character.isWhitespace((char) ch)) {
                sb.append((char) ch);
                read();
            }

            return token(keyword.equals("use") ? Token.USE : Token.EXTENDS, sb.toString());
        } else if (keyword.equals("block")) {
            sb.setLength(0);

            read(); // skip space

            while (ch >= 0 && !Character.isWhitespace((char) ch)) {
                sb.append((char) ch);
                read();
            }

            String blockName = sb.toString();

            sb.setLength(0);

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

                    return token(Token.BLOCK, blockName, sb.toString());

                }
                sb.append((char) ch);
                read();
            }
        } else if (keyword.equals("code")) {
            sb.setLength(0);

            read(); // skip space

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

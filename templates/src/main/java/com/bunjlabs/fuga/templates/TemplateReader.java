/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fuga.templates;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TemplateReader {

    private final Reader reader;
    private int cur;

    private int linenomber = 1;
    private int colnumber = 1;

    /**
     *
     * @param reader Input reader
     */
    public TemplateReader(Reader reader) {
        this.reader = reader;
    }

    /**
     *
     * @return Token
     * @throws TemplateReaderException if any error occured while reading template file
     */
    public Token next() throws TemplateReaderException {
        while (read() >= 0) {
            if (Character.isWhitespace((char) cur)) {
                continue;
            } else if (cur == '@') {
                read();
                return keyword();
            } else {
                return token(Token.ERROR);
            }
        }
        return token(Token.EOS);
    }

    private int read() throws TemplateReaderException {
        try {
            cur = reader.read();

            colnumber++;

            if (cur == '\n') {
                linenomber++;
                colnumber = 1;
            }

            return cur;
        } catch (IOException ex) {
            throw new TemplateReaderException("Unable to read template", ex, linenomber, colnumber);
        }
    }

    private Token token(int type, String... args) {
        return new Token(type, args);
    }

    private Token keyword() throws TemplateReaderException {
        StringBuilder sb = new StringBuilder();

        while (cur >= 0 && !Character.isWhitespace((char) cur) && cur != '(') {
            sb.append((char) cur);
            read();
        }

        String keyword = sb.toString();

        // skip space
        while (cur >= 0 && Character.isWhitespace((char) cur)) {
            read();
        }

        if (cur != '(') {
            throw new TemplateReaderException("Unexpected symbol: " + ((char) cur), linenomber, colnumber);
        }

        read(); // skip '('

        sb.setLength(0);

        List<String> args = new ArrayList<>();

        while (true) {
            while (cur >= 0 && cur != ',' && cur != ')') {
                if (cur == '\n') {
                    throw new TemplateReaderException("Unclosed argument list", linenomber, colnumber);
                }
                sb.append((char) cur);
                read();
            }

            if (sb.length() > 0) {
                args.add(sb.toString().trim());
            }

            sb.setLength(0);

            if (cur == ')') {
                read();
                break;
            } else if (cur == ',') {
                read();
                continue;
            } else {
                throw new TemplateReaderException("Unexpected end of file", linenomber, colnumber);
            }
        }

        sb.setLength(0);

        if (keyword.equals("use")) {
            if (args.size() < 1) {
                throw new TemplateReaderException("At least one argument is required for 'use'", linenomber, colnumber);
            }
            return token(Token.USE, args.get(0));
        }

        if (keyword.equals("extends")) {
            if (args.size() < 1) {
                throw new TemplateReaderException("At least one argument is required for 'extends'", linenomber, colnumber);
            }
            return token(Token.EXTENDS, args.get(0));
        }

        if (keyword.equals("block")) {
            if (args.size() < 1) {
                throw new TemplateReaderException("At least one argument is required for 'block'", linenomber, colnumber);
            }

            while (true) {
                if (cur == '@') {
                    StringBuilder sbb = new StringBuilder();
                    sbb.append((char) cur);

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

                    return token(Token.BLOCK, args.get(0), args.size() > 1 ? args.get(1) : "", sb.toString());

                }
                sb.append((char) cur);
                read();
            }
        }

        if (keyword.equals("tag")) {
            if (args.size() < 1) {
                throw new TemplateReaderException("At least one argument is required for 'tag'", linenomber, colnumber);
            }

            while (true) {
                if (cur == '@') {
                    StringBuilder sbb = new StringBuilder();
                    sbb.append((char) cur);

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
                sb.append((char) cur);
                read();
            }
        }

        if (keyword.equals("code")) {
            while (true) {
                if (cur == '@') {
                    StringBuilder sbb = new StringBuilder();
                    sbb.append((char) cur);

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
                sb.append((char) cur);
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

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
package com.bunjlabs.fuga.router;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

public class Tokenizer {

    private static final Pattern METHOD_PATTERN = Pattern.compile("GET|POST|PUT|PATCH|TRACE|DELETE|HEAD|OPTIONS");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("([1-9][0-9]+)|[0-9]");

    protected static final int TK_EOF = -1;
    protected static final int TK_ERROR = -2;
    protected static final int TK_NOTHING = -4;
    protected static final int TK_USE = -10;
    protected static final int TK_INCLUDE = -11;
    protected static final int TK_HOST = -12;
    protected static final int TK_METHOD = -20;
    protected static final int TK_PATTERN = -21;
    protected static final int TK_WORD = -22;
    protected static final int TK_STRCONST = -23;
    protected static final int TK_INTEGER = -24;

    private final Reader r;
    private int curr = -1;

    protected int ttype = TK_NOTHING;
    protected int line = 1;
    protected int column = 1;
    protected String sval = null;

    public Tokenizer(Reader r) {
        this.r = r;
        try {
            skip();
        } catch (IOException ex) {
        }
    }

    public int next() throws RoutesMapSyntaxException {
        return ttype = nextToken();
    }

    private int nextToken() throws RoutesMapSyntaxException {
        sval = null;

        try {
            for (;;) {
                switch (curr) {
                    case -1:
                        return TK_EOF;
                    case '\r':
                    case '\n':
                        incLine();
                        break;
                    case ' ':
                    case '\f':
                    case '\t':
                        skip();
                        break;
                    case '/':
                        comment();
                        break;
                    case '@':
                        pattern();
                        return TK_PATTERN;
                    case '(':
                    case ')':
                    case '{':
                    case '}':
                    case ',':
                    case ':':
                        int c = curr;
                        skip();
                        return c;
                    case '"':
                        constant();
                        return TK_STRCONST;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':

                        return integer();
                    default: {
                        if (!currIsWord()) {
                            char ct = (char) curr;
                            skip();
                            throw new RoutesMapSyntaxException(this, "Unexpected symbol: " + ct);
                        }
                        return word();
                    }
                }
            }
        } catch (IOException ex) {
            sval = ex.getLocalizedMessage();
            return TK_ERROR;
        }
    }

    private int integer() throws RoutesMapSyntaxException, IOException {
        StringBuilder sb = new StringBuilder();

        while (currIsDigit()) {
            sb.append((char) curr);
            skip();
        }

        sval = sb.toString();

        if (!INTEGER_PATTERN.matcher(sval).matches()) {
            return word(sval);
        }
        return TK_INTEGER;
    }

    private void constant() throws RoutesMapSyntaxException, IOException {
        StringBuilder sb = new StringBuilder();
        skip();
        while (curr != '"') {
            switch (curr) {
                case -1:
                case '\n':
                case '\r':
                    throw new RoutesMapSyntaxException(this, "Unfinished string");
                case '\\':
                    skip();
                    if (curr == 'n') {
                        sb.append('\n');
                    } else if (curr == 'r') {
                        sb.append('\r');
                    } else if (curr == 't') {
                        sb.append('\t');
                    } else if (curr == 'f') {
                        sb.append('\f');
                    } else if (curr == '\\') {
                        sb.append('\\');
                    } else {
                        throw new RoutesMapSyntaxException(this, "Unsupported char escape sequance: \\" + ((char) curr));
                    }
                    break;
                default:
                    sb.append((char) curr);
                    break;
            }
            skip();
        }
        skip();
        sval = sb.toString();
    }

    private int word() throws IOException {
        return word(null);
    }

    private int word(String first) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (first != null && !first.isEmpty()) {
            sb.append(first);
        }

        while (currIsWord()) {
            sb.append((char) curr);
            skip();
        }
        sval = sb.toString();

        if (METHOD_PATTERN.matcher(sval).matches()) {
            return TK_METHOD;
        } else if (sval.equals("use")) {
            return TK_USE;
        } else if (sval.equals("include")) {
            return TK_INCLUDE;
        } else if (sval.equals("host")) {
            return TK_HOST;
        } else {
            return TK_WORD;
        }
    }

    private void pattern() throws IOException {
        StringBuilder sb = new StringBuilder();
        skip();
        while (!currIsNewline() && !currIsWitespace()) {
            sb.append((char) curr);
            skip();
        }
        sval = sb.toString();
    }

    private void comment() throws IOException, RoutesMapSyntaxException {
        skip();
        if (curr == '*') {
            /* block comment */

            skip();
            for (;;) {
                if (curr == '*') {
                    skip();
                    if (curr == '/') {
                        skip();
                        break;
                    }
                } else if (currIsNewline()) {
                    incLine();
                } else {
                    skip();
                }
            }
        } else {
            while (!currIsNewline()) {
                skip();
                /* line comment */

            }
        }
    }

    private boolean currIsDigit() {
        return (curr >= '0' && curr <= '9');
    }

    private boolean currIsWord() {
        return (curr >= 'A' && curr <= 'Z')
                || (curr >= 'a' && curr <= 'z')
                || (curr >= '0' && curr <= '9')
                || curr == '_' || curr == '.';

    }

    private boolean currIsWitespace() {
        return (curr == ' ' || curr == '\f' || curr == '\t');
    }

    private boolean currIsNewline() {
        return (curr == '\n' || curr == '\r');
    }

    private void skip() throws IOException {
        curr = r.read();
        column++;
    }

    private void incLine() throws IOException, RoutesMapSyntaxException {
        int old = curr;
        skip();
        if (currIsNewline() && curr != old) {
            skip();
        }
        if (++line >= Integer.MAX_VALUE) {
            throw new RoutesMapSyntaxException(this, "Too big input");
        }
        column = 1;
    }

}

package com.bunjlabs.fugaframework.router;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

public class Tokenizer {

    private static final Pattern methodPattern = Pattern.compile("GET|POST|PUT|DELETE|HEAD|OPTIONS");
    private static final Pattern integerPattern = Pattern.compile("([1-9][0-9]+)|[0-9]");

    public static final int TK_EOF = -1;
    public static final int TK_ERROR = -2;
    public static final int TK_NOTHING = -4;
    public static final int TK_USE = -10;
    public static final int TK_METHOD = -11;
    public static final int TK_PATTERN = -12;
    public static final int TK_WORD = -13;
    public static final int TK_STRCONST = -14;
    public static final int TK_INTEGER = -15;

    private final Reader r;
    private int curr = -1;

    public int ttype = TK_NOTHING;
    public int line = 1;
    public int column = 1;
    public String sval = null;

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
                    case '$':
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
                        integer();
                        return TK_INTEGER;
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

    private void integer() throws RoutesMapSyntaxException, IOException {
        StringBuilder sb = new StringBuilder();

        while (currIsDigit()) {
            sb.append((char) curr);
            skip();
        }

        sval = sb.toString();

        if (!integerPattern.matcher(sval).matches()) {
            throw new RoutesMapSyntaxException(this, "Not a integer");
        }
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
        StringBuilder sb = new StringBuilder();
        while (currIsWord()) {
            sb.append((char) curr);
            skip();
        }
        sval = sb.toString();

        if (methodPattern.matcher(sval).matches()) {
            return TK_METHOD;
        } else if (sval.equals("use")) {
            return TK_USE;
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
        if (curr == '*') { /* block comment */

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
                skip(); /* line comment */

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

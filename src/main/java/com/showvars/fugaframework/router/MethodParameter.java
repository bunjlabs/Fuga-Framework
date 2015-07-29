package com.showvars.fugaframework.router;

public class MethodParameter {

    private final int captureGroup;
    private final String type;

    public MethodParameter(int captureGroup, String type) {
        this.captureGroup = captureGroup;
        this.type = type;

    }

    public int getCaptureGroup() {
        return captureGroup;
    }

    public String getType() {
        return type;
    }

    public Object cast(String input) {
        switch (type) {
            case "String":
                return input;
            case "int":
                return Integer.parseInt(input);
            case "long":
                return Long.parseLong(input);
            case "short":
                return Short.parseShort(input);
            case "char":
                return (char) Integer.parseInt(input);
            case "byte":
                return Byte.parseByte(input);
            case "float":
                return Integer.parseInt(input);
            case "double":
                return Integer.parseInt(input);
            case "boolean":
                return Integer.parseInt(input);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "MethodParameter:" + captureGroup + ":" + type;
    }
}

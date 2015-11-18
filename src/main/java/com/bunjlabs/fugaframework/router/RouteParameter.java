package com.showvars.fugaframework.router;

class RouteParameter {

    public static enum ParameterType {

        CAPTURE_GROUP, CONSTANT
    }
    private final ParameterType type;
    private final String dataType;

    private int captureGroup;
    private String value;

    RouteParameter(int captureGroup, String dataType) {
        this.captureGroup = captureGroup;
        this.dataType = dataType;
        type = ParameterType.CAPTURE_GROUP;
    }

    RouteParameter(String value, String dataType) {
        this.value = value;
        type = ParameterType.CONSTANT;
        this.dataType = dataType;
    }

    public int getCaptureGroup() {
        return captureGroup;
    }

    public String getDataType() {
        return dataType;
    }

    public ParameterType getType() {
        return type;
    }

    public Object cast() {
        if (type == ParameterType.CAPTURE_GROUP) {
            return null;
        }
        return cast(value);
    }

    public Object cast(String input) {
        switch (dataType) {
            case "String":
                return input;
            case "int":
                return Integer.parseInt(input);
            case "long":
                return Long.parseLong(input);
            case "short":
                return Short.parseShort(input);
            case "char":
                return input.length() > 0 ? input.charAt(0) : null;
            case "byte":
                return Byte.parseByte(input);
            case "float":
                return Float.parseFloat(input);
            case "double":
                return Double.parseDouble(input);
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

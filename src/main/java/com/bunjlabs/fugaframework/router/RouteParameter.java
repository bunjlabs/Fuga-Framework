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
package com.bunjlabs.fugaframework.router;

public class RouteParameter {

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

package xin.zero2one.parser;

import org.junit.Test;

import java.util.Properties;

/**
 * @author zhoujundong
 * @data 6/11/2019
 * @description TODO
 */
public class PropertyParser {

    private String openToken = "${";

    private String closeToken = "}";

    private Properties variables = new Properties();

    @Test
    public void t1() {
        String text = "恭喜你了${name}cccc${age}dd";
        variables.put("name","小宝贝");
        variables.put("age","22");
        String parse = parse(text);
        System.out.println(parse);
    }

    public String parse(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        // search open token
        int start = text.indexOf(openToken, 0);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    builder.append(handleToken(expression.toString()));
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    public String handleToken(String content) {
        if (variables != null) {
            String key = content;
            if (variables.containsKey(key)) {
                return variables.getProperty(key);
            }
        }
        return "${" + content + "}";
    }

}

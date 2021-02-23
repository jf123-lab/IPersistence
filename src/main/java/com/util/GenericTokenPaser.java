package com.util;

public class GenericTokenPaser {

    private final String openToken;

    private final String closeToken;

    private final TokenHandler tokenHandler;

    public GenericTokenPaser(String openToken, String closeToken, TokenHandler tokenHandler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.tokenHandler = tokenHandler;
    }

    public String parse(String content) {

        if (content == null || content.isEmpty()) {
            return "";
        }
        //content = 123{235 则start =3
        int start = content.indexOf(openToken, 0);
        if (start == -1) {
            return content;
        }

        //将content转换成字符数组
        //默认偏移量为0，并且指定返回变量builder
        char[] src = content.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            //判断如果开始标记前如果有转义字符，就不作为openToken进行处理，否则继续处理  比如 12\{ start=3,len =2 -> 12{
            if (start > 0 && src[start - 1] == '\\') {
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                //重置expression变量，避免空指针或者老数据干扰
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                //build = 123 offset =3+ 1=4 从第四位开始
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = content.indexOf(closeToken, offset);//closeToken的位置
                while (end > -1) {
                    if (end > offset && src[end -1] == '\\') {
                        expression.append(src, offset, end - offset -1).append(closeToken);
                        offset = end + closeToken.length();
                        end = content.indexOf(closeToken, offset);
                    } else {//不存在转义字符
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                // 123{235 -> 3 ，4
                if (end == -1) {//不存在结束标记
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    builder.append(tokenHandler.handlerToken(expression.toString()));
                    offset = end +closeToken.length();
                }
            }
            start = content.indexOf(openToken, offset);
        }

        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();

    }

}

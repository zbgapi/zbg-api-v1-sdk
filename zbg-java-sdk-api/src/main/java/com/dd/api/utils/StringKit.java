package com.dd.api.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * <p>
 * created by zhangzp on 2018/5/3.
 *
 * @author zhangzp
 */
public class StringKit {
    /**
     * 首先初始化一个字符数组，用来存放每个16进制字符
     */
    public static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final String[] CHARS = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 密码正则表达式
     */
    public static final String REG_PASSWORD = "([A-Za-z0-9]|[、_=\\\\\\[\\];`',./~!@#$%^&*()+|?><\":{}]){6,22}";
    /**
     * 国内手机号正则表达式
     */
    public static final String REG_MAINLAND_MOBILE = "^(13[0-9]|14[5789]|15[0-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
    /**
     * 香港手机号正则表达式
     */
    public static final String REG_HK_PHONE_NUMBER = "^(5|6|8|9)\\d{7}$";

    public static final String REG_EMAIL = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";

    /**
     * 判断字符串是否为null
     *
     * @param str 目标字符串
     * @return str == null 返回true 否则返回false
     */
    public static boolean isNull(String str) {
        return str == null;
    }

    /**
     * 判断字符串是否为空，会去掉字符串前后的空格
     *
     * @param str 目标字符串
     * @return 目标字符串为null或空则返回true，否则返回false
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 目标字符串
     * @return 目标字符串为null或空则返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否为空，会去掉字符串前后的空格
     *
     * @param str 目标字符串
     * @return 目标字符串至少包含一个非空格字符时返回true，否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param name 驼峰命名字符串
     * @return 下划线命名字符串
     */
    public static String camelToUnderline(String name) {
        if (isEmpty(name)) {
            return "";
        }

        int len = name.length();
        StringBuilder builder = new StringBuilder(len + 5);
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.append('_');
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    /**
     * 下划线命名转驼峰命名
     *
     * @param name 下划线命名字符串
     * @return 驼峰命名字符串
     */
    public static String underlineToCamel(String name) {
        if (isEmpty(name)) {
            return "";
        }

        char[] values = name.toCharArray();

        int index = 0;
        char ch;
        for (int i = 0, len = name.length(); i < len; i++) {
            ch = values[i];
            if ('_' == ch) {
                ++i;
                ch = Character.toUpperCase(values[i]);
            }
            values[index++] = ch;
        }

        return new String(values, 0, index);
    }

    /**
     * 下划线命名转类命名
     *
     * @param name 下划线命名字符串
     * @return 类命名字符串
     */
    public static String underlineToClassName(String name) {
        String camelName = underlineToCamel(name);
        // 处理第一个字符
        char firstChar = name.charAt(0);
        camelName = Character.toUpperCase(firstChar) + camelName.substring(1);

        return camelName;
    }

    /**
     * 正则表达式查找
     * 匹配的被提取出来做数组
     *
     * @param source 目标字符串
     * @param regex  正则表达式
     * @return 匹配的字符串数组
     */
    public static String[] searchByRegex(String source, String regex) {
        if (source == null) {
            return null;
        }

        ArrayList<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result.toArray(new String[0]);
    }

    public static String padStart(String string, int length) {
        return padStart(string, length, ' ');
    }

    /**
     * 在字符串前位置补全指定字符 {@code padChar}，补全后的字符串大小等于指定长度 {@code length}，
     * 如果length <= string.length 则直接返回string
     * <ul>
     * <li>{@code padStart("7", 3, '0')} returns {@code "007"}
     * <li>{@code padStart("2010", 3, '0')} returns {@code "2010"}
     * </ul>
     *
     * @param string  需要补全的字符串
     * @param length  补全后的长度
     * @param padChar 补全的字符
     * @return 补全后的字符串
     */
    public static String padStart(String string, int length, char padChar) {
        if (string == null) {
            return null;
        }

        int len = length - string.length();
        if (len <= 0) {
            return string;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(padChar);
        }

        return builder.append(string).toString();
    }

    public static String padEnd(String string, int length) {
        return padEnd(string, length, ' ');
    }

    /**
     * 在字符串后位置补全指定字符 {@code padChar}，补全后的字符串大小等于指定长度 {@code length}，
     * 如果length <= string.length 则直接返回string
     * <ul>
     * <li>{@code padEnd("7", 3, '0')} returns {@code "700"}
     * <li>{@code padEnd("2010", 3, '0')} returns {@code "2010"}
     * </ul>
     *
     * @param string  需要补全的字符串
     * @param length  补全后的长度
     * @param padChar 补全的字符
     * @return 补全后的字符串
     */
    public static String padEnd(String string, int length, char padChar) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        int len = length - string.length();
        if (len <= 0) {
            return string;
        }

        StringBuilder builder = new StringBuilder(string);
        for (int i = 0; i < len; i++) {
            builder.append(padChar);
        }

        return builder.toString();
    }

    public static String pad(String string, int length) {
        return pad(string, length, ' ');
    }

    /**
     * 在字符串前后位置补全指定字符 {@code padChar}，补全后的字符串大小等于指定长度 {@code length}，
     * 如果length <= string.length 则直接返回string
     *
     * @param string  需要补全的字符串
     * @param length  补全后的长度
     * @param padChar 补全的字符
     * @return 补全后的字符串
     */
    public static String pad(String string, int length, char padChar) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        int len = length - string.length();
        if (len <= 0) {
            return string;
        }

        int llen = len / 2;
        int rlen = len - llen;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < llen; i++) {
            builder.append(padChar);
        }

        builder.append(string);
        for (int i = 0; i < rlen; i++) {
            builder.append(padChar);
        }

        return builder.toString();
    }

    /**
     * 重复字符串，例如：<li>{@code repeat("xyz", 3)} returns {@code "xyzxyzxyz"}
     *
     * @param string 元字符串
     * @param count  重复的次数， 正数
     * @return 重复 {@code count} 次后的字符串
     * @throws IllegalArgumentException 如果 {@code count} 为负数
     */
    public static String repeat(String string, int count) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        if (count <= 1) {
            return string;
        }

        int len = string.length();
        long longSize = (long) len * count;
        int size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("重复后的字符串长度太大");
        }

        final char[] array = new char[size];
        string.getChars(0, len, array, 0);
        // 成倍复制，减少复制的次数
        int n;
        for (n = len; n < size - n; n <<= 1) {
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }

    /**
     * 反转字符串
     *
     * @param str 待处理的字符串
     * @return 逆序后的字符串
     */
    public static String reverse(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length / 2; i++) {
            char swap = chars[i];
            chars[i] = chars[chars.length - i - 1];
            chars[chars.length - i - 1] = swap;
        }

        return new String(chars);
    }

    /**
     * 返回两个字符串共同的最长前缀
     *
     * @param a 字符串
     * @param b 字符串
     * @return 返回两个字符串共同的前缀字符串
     */
    public static String commonPrefix(String a, String b) {
        if (a == null || a.isEmpty()) {
            return a;
        }

        if (b == null || b.isEmpty()) {
            return b;
        }

        int maxLength = Math.min(a.length(), b.length());

        int p = 0;
        while (p < maxLength && a.charAt(p) == b.charAt(p)) {
            p++;
        }

        return a.substring(0, p);
    }

    /**
     * 返回两个字符串共同的最长后缀
     *
     * @param a 字符串
     * @param b 字符串
     * @return 返回两个字符串共同的后缀字符串
     */
    public static String commonSuffix(String a, String b) {
        if (a == null || a.isEmpty()) {
            return a;
        }

        if (b == null || b.isEmpty()) {
            return b;
        }

        int maxLength = Math.min(a.length(), b.length());

        int p = 0;
        while (p < maxLength && a.charAt(a.length() - p - 1) == b.charAt(b.length() - p - 1)) {
            p++;
        }

        return a.substring(a.length() - p, a.length());
    }

    /**
     * 隐藏字符串指定位置指定长度字符，如：<br/>
     * {@code hidden("18100275667", 3, 4)} returns {@code 181****5667}
     *
     * @param str    待处理字符串
     * @param start  开始位置， 可为负
     * @param length 需要隐藏的长度
     * @return 处理后的字符串
     */
    public static String hidden(String str, int start, int length) {
        if (start < 0) {
            start = (start + str.length()) % str.length();
        }

        if (start >= str.length()) {
            return str;
        }

        int end = start + length > str.length() ? str.length() : start + length;

        char[] chars = str.toCharArray();
        for (int i = start; i < end; i++) {
            chars[i] = '*';
        }

        return new String(chars);
    }

    /**
     * 隐藏字符串指定位置指定长度字符，如：<br/>
     * <pre>
     * hidden("18100275667", 3, -4, 4)          = 181****5667
     * hidden("18100275667@163.com", 3, -12, 4)  = 181****5667@163.com
     * hidden("624739086@163.com", 3, -12, 4)    = 624****9086@163.com
     * <pre/>
     *
     * @param src   待处理字符串
     * @param start 开始位置， 可为负
     * @param end   结束位置， 可为负
     * @param size  星号的个数
     * @return 处理后的字符串
     */
    public static String hidden(String src, int start, int end, int size) {
        start = fixIndex(src, start);

        if (end > src.length()) {
            end = src.length();
        }

        end = fixIndex(src, end);

        if (start > end) {
            throw new IllegalArgumentException("The start[" + start + "] of the index must be smaller than the end[" + end + "]");
        }

        if (start >= src.length()) {
            return src;
        }

        return src.substring(0, start) + repeat("*", size) + src.substring(end, src.length());
    }

    private static int fixIndex(String src, int index) {
        int len = src.length();
        return index < 0 || index >= len ? (index + src.length()) % src.length() : index;
    }

    /**
     * 模糊处理用户名, 隐藏中间部分，使用固定长度的*代替
     * <p>
     * <pre>
     * hiddenName("东")                   = 东
     * hiddenName("张东")                  = 张*东
     * hiddenName("张晓东")                = 张*东
     * hiddenName("18100275667")          = 181****5667
     * hiddenName("san")                  = san
     * hiddenName("zhang")                = zha****ng
     * hiddenName("18100275667@163.com")  = 181****5667@163.com
     * hiddenName("624739086@163.com")    = 624****9086@163.com
     * <pre/>
     * @param name 用户名
     * @return 处理后的用户名
     */
    public static String hiddenName(String name) {
        if (isEmpty(name)) {
            return name;
        }

        if (containChinese(name)) {
            if (name.length() > 1) {
                return hidden(name, 1, -1, 1);
            } else {
                return name;
            }
        }

        int index = name.indexOf("@");
        String tmp = name;
        if (index > 0) {
            tmp = name.substring(0, index);
        }

        if (tmp.length() > 7) {
            tmp = StringKit.hidden(tmp, 3, -4, 4);
        } else if (tmp.length() > 3) {
            tmp = StringKit.hidden(tmp, 3, -(tmp.length() - 3), 4);
        }

        if (index > 0) {
            tmp = tmp + name.substring(index);
        }

        return tmp;
    }

    /**
     * 生成一个uuid字符串，去掉横线
     *
     * @return uuid字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 短8位随机字符串
     *
     * @return 随机字符串
     */
    public static String shortUuid() {
        /*
         利用62个可打印字符，通过随机生成32位UUID，由于UUID都为十六进制，所以将UUID分成8组，每4个为一组，然后通过模62操作，结果作为索引取出字符
         */
        StringBuilder builder = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            builder.append(CHARS[x % 0x3E]);
        }
        return builder.toString();
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String random(int length) {
        StringBuilder builder = new StringBuilder(length);
        int len = CHARS.length;
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * len);
            builder.append(CHARS[index]);
        }

        return builder.toString();
    }

    /**
     * 生成指定区间长度的随机字符串, [min, max]
     *
     * @param min 最小长度
     * @param max 最大长度
     * @return 随机字符串
     */
    public static String random(int min, int max) {
        int len = (int) (Math.random() * (max - min + 1) + min);
        return random(len);
    }

    /**
     * 随机数字字符串
     *
     * @param length 长度
     * @return 数字字符串
     */
    public static String randomDigit(int length) {
        StringBuilder builder = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(CHARS[random.nextInt(10)]);
        }

        return builder.toString();
    }

    /**
     * 判断手机号是否符合格式
     *
     * @param mobile 手机号
     * @return 符合返回true
     */
    public static boolean isMobile(String mobile) {
        return mobile != null && mobile.matches(REG_MAINLAND_MOBILE);
    }

    /**
     * 判断字符串是否符合email格式
     *
     * @param str 待检测字符串
     * @return true or false
     */
    public static boolean isEmail(String str) {
        return str != null && str.matches(REG_EMAIL);
    }

    /**
     * 判断字符串是否是中文
     *
     * @param str 待检测字符串
     * @return true or false
     */
    public static boolean isChinese(String str) {
        return str != null && str.matches("[\\u4e00-\\u9fa5]+");
    }

    /**
     * 判断字符串是否包含中文
     */
    public static boolean containChinese(String str) {
        String[] search = searchByRegex(str, "[\\u4e00-\\u9fa5]+");
        return search != null && search.length > 0;
    }

    /**
     * 清楚字符串中的空格
     *
     * @param str 待处理字符串
     * @return 处理后的字符串
     */
    public static String clearBlank(String str) {
        return str != null ? str.replaceAll("\\s+", "") : "";
    }

    /**
     * 判断指定字符串是否匹配正则表达式
     *
     * @param str     带检测字符串
     * @param pattern 正则表达式
     * @return 匹配返回true，否则返回false
     */
    public static boolean match(String str, String pattern) {
        return isNotEmpty(str) && (isEmpty(pattern) || str.matches(pattern));
    }

    /**
     * 转16进制字符串
     *
     * @param bytes 字节码数组
     * @return 16进制字符串
     */
    public static String toHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        int index = 0;

        for (byte b : bytes) {
            chars[index++] = HEX_DIGITS[b >>> 4 & 0xf];
            chars[index++] = HEX_DIGITS[b & 0xf];
        }

        return new String(chars);
    }

    /**
     * 16进制字符串转字节码数组
     *
     * @param hex 16进制字符串
     * @return 字节码数组
     */
    public static byte[] toBytes(String hex) {
        if (StringKit.isEmpty(hex)) {
            return new byte[0];
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            String highStr = String.valueOf(hex.charAt(i * 2));
            String lowStr = String.valueOf(hex.charAt(i * 2 + 1));
            int high = Integer.parseInt(highStr, 16);
            int low = Integer.parseInt(lowStr, 16);

            bytes[i] = (byte) ((high << 4) + low);
        }

        return bytes;
    }

    /**
     * url encode
     *
     * @param src 字符串
     * @return 新的字符串
     */
    public static String encode(String src) {
        try {
            return URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * url decode
     *
     * @param src 字符串
     * @return 新的字符串
     */
    public static String decode(String src) {
        try {
            return URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否为乱码
     *
     * @param str 带判断字符串
     * @return 存在乱码返回true，否则返回false
     */
    public static boolean isMessyCode(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            //从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            if ((int) c == 0xfffd) {
                // 存在乱码
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个字符串是否相等，
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 如果相等，则返回true，否则返回false
     */
    public static boolean equals(String str1, String str2) {
        return str1 != null && str1.equals(str2);
    }


}

package class039;

// LintCode 659. Encode and Decode Strings (字符串编码解码)
// 测试链接 : https://www.lintcode.com/problem/659/

import java.util.*;

public class LintCode659_EncodeDecodeStrings {
    // 编码函数
    public String encode(List<String> strs) {
        StringBuilder encoded = new StringBuilder();
        
        for (String str : strs) {
            // 格式：长度 + '#' + 字符串
            encoded.append(str.length()).append('#').append(str);
        }
        
        return encoded.toString();
    }
    
    // 解码函数
    public List<String> decode(String s) {
        List<String> decoded = new ArrayList<>();
        int i = 0;
        
        while (i < s.length()) {
            // 找到分隔符'#'
            int j = i;
            while (j < s.length() && s.charAt(j) != '#') {
                j++;
            }
            
            // 提取长度
            int length = Integer.parseInt(s.substring(i, j));
            
            // 提取字符串
            String str = s.substring(j + 1, j + 1 + length);
            decoded.add(str);
            
            // 移动到下一个字符串的开始位置
            i = j + 1 + length;
        }
        
        return decoded;
    }
    
    // 测试用例
    public static void main(String[] args) {
        LintCode659_EncodeDecodeStrings solution = new LintCode659_EncodeDecodeStrings();
        
        // 测试用例1
        List<String> strs1 = Arrays.asList("hello", "world");
        String encoded1 = solution.encode(strs1);
        List<String> decoded1 = solution.decode(encoded1);
        System.out.println("输入: " + strs1);
        System.out.println("编码: " + encoded1);
        System.out.println("解码: " + decoded1);
        System.out.println("期望: " + strs1 + "\n");
        
        // 测试用例2
        List<String> strs2 = Arrays.asList("", "abc", "def");
        String encoded2 = solution.encode(strs2);
        List<String> decoded2 = solution.decode(encoded2);
        System.out.println("输入: " + strs2);
        System.out.println("编码: " + encoded2);
        System.out.println("解码: " + decoded2);
        System.out.println("期望: " + strs2 + "\n");
    }
}
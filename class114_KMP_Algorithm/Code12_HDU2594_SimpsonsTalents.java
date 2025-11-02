package class101;

/**
 * HDU 2594 Simpsons' Hidden Talents
 * 
 * 题目来源：HDU (杭州电子科技大学在线评测系统)
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2594
 * 
 * 题目描述：
 * 给定两个字符串s1和s2，找到最长的字符串s，使得s既是s1的后缀，又是s2的前缀。
 * 输出这个字符串s及其长度。如果不存在这样的字符串，输出0。
 * 
 * 示例：
 * 输入：s1 = "abcabc", s2 = "bcabca"
 * 输出："bca" 3
 * 
 * 算法思路：
 * 使用KMP算法的思想来解决这个问题。
 * 1. 将s1和s2连接成一个新字符串，中间用特殊字符分隔
 * 2. 构建新字符串的next数组
 * 3. 通过分析next数组找到最长的公共前后缀
 * 
 * 时间复杂度：O(n + m)，其中n是s1的长度，m是s2的长度
 * 空间复杂度：O(n + m)
 */
public class Code12_HDU2594_SimpsonsTalents {

    /**
     * 找到s1的后缀和s2的前缀的最长公共部分
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共部分及其长度
     */
    public static String[] findLongestCommonSuffixPrefix(String s1, String s2) {
        // 边界条件处理
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return new String[]{"0"};
        }
        
        // 构造新字符串：s1 + "#" + s2
        // 使用特殊字符"#"来分隔两个字符串，确保不会产生虚假匹配
        String combined = s1 + "#" + s2;
        char[] str = combined.toCharArray();
        int n = str.length;
        
        // 构建next数组
        int[] next = buildNextArray(str);
        
        // 最长公共部分的长度就是next[n-1]
        int commonLength = next[n - 1];
        
        // 确保公共部分不会超过任何一个字符串的长度
        commonLength = Math.min(commonLength, Math.min(s1.length(), s2.length()));
        
        if (commonLength == 0) {
            return new String[]{"0"};
        }
        
        // 返回公共部分及其长度
        String commonPart = s1.substring(s1.length() - commonLength);
        return new String[]{commonPart, String.valueOf(commonLength)};
    }
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * 
     * next[i]表示str[0...i]子串的最长相等前后缀的长度
     * 
     * @param str 字符数组
     * @return next数组
     */
    private static int[] buildNextArray(char[] str) {
        int length = str.length;
        int[] next = new int[length];
        
        // 初始化
        next[0] = 0;
        int prefixLen = 0;  // 当前最长相等前后缀的长度
        int i = 1;          // 当前处理的位置
        
        // 从位置1开始处理
        while (i < length) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (str[i] == str[prefixLen]) {
                prefixLen++;
                next[i] = prefixLen;
                i++;
            } 
            // 如果不匹配且前缀长度大于0，需要回退
            else if (prefixLen > 0) {
                prefixLen = next[prefixLen - 1];
            } 
            // 如果不匹配且前缀长度为0，next[i] = 0
            else {
                next[i] = 0;
                i++;
            }
        }
        
        return next;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1_1 = "abcabc";
        String s2_1 = "bcabca";
        String[] result1 = findLongestCommonSuffixPrefix(s1_1, s2_1);
        System.out.println("测试用例1:");
        System.out.println("s1: " + s1_1);
        System.out.println("s2: " + s2_1);
        if (result1.length == 1) {
            System.out.println("输出: " + result1[0]);
        } else {
            System.out.println("输出: " + result1[0] + " " + result1[1]);
        }
        System.out.println("预期输出: bca 3\n");
        
        // 测试用例2
        String s1_2 = "hello";
        String s2_2 = "world";
        String[] result2 = findLongestCommonSuffixPrefix(s1_2, s2_2);
        System.out.println("测试用例2:");
        System.out.println("s1: " + s1_2);
        System.out.println("s2: " + s2_2);
        if (result2.length == 1) {
            System.out.println("输出: " + result2[0]);
        } else {
            System.out.println("输出: " + result2[0] + " " + result2[1]);
        }
        System.out.println("预期输出: 0\n");
        
        // 测试用例3
        String s1_3 = "abc";
        String s2_3 = "abcdef";
        String[] result3 = findLongestCommonSuffixPrefix(s1_3, s2_3);
        System.out.println("测试用例3:");
        System.out.println("s1: " + s1_3);
        System.out.println("s2: " + s2_3);
        if (result3.length == 1) {
            System.out.println("输出: " + result3[0]);
        } else {
            System.out.println("输出: " + result3[0] + " " + result3[1]);
        }
        System.out.println("预期输出: abc 3\n");
        
        // 测试用例4
        String s1_4 = "abcdef";
        String s2_4 = "def";
        String[] result4 = findLongestCommonSuffixPrefix(s1_4, s2_4);
        System.out.println("测试用例4:");
        System.out.println("s1: " + s1_4);
        System.out.println("s2: " + s2_4);
        if (result4.length == 1) {
            System.out.println("输出: " + result4[0]);
        } else {
            System.out.println("输出: " + result4[0] + " " + result4[1]);
        }
        System.out.println("预期输出: def 3");
    }
}
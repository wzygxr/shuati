package class101.extended;

import java.util.*;

/**
 * 洛谷 P4824 [USACO15FEB]Censoring S
 * 
 * 题目描述：
 * 给定一个字符串s1，如果其中含有s2字符串，就删除最左出现的那个。
 * 删除之后s1剩下的字符重新拼接在一起，再删除最左出现的那个。
 * 如此周而复始，返回最终剩下的字符串。
 * 
 * 输入格式：
 * 第一行包含一个字符串s1。
 * 第二行包含一个字符串s2。
 * 
 * 输出格式：
 * 输出一个字符串，表示最终剩下的字符串。
 * 
 * 算法思路：
 * 这道题需要模拟一个不断删除字符串的过程。如果每次重新匹配，时间复杂度会很高。
 * 我们可以使用KMP算法配合栈结构来优化这个过程：
 * 1. 使用栈来模拟字符串的构建过程
 * 2. 在每个字符入栈后，检查栈顶是否能匹配模式串
 * 3. 如果匹配成功，则将匹配的部分出栈
 * 4. 继续处理下一个字符
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(n + m)
 */
public class LuoguP4824_Censoring {
    
    public static final int MAXN = 1000001;
    public static int[] next = new int[MAXN];
    public static int[] stack1 = new int[MAXN];  // 存储字符在原字符串中的索引
    public static int[] stack2 = new int[MAXN];  // 存储匹配到模式串的位置
    public static int size;  // 栈的大小
    
    /**
     * 不断删除字符串s2后的最终结果
     * 
     * 算法原理：
     * 1. 使用栈结构模拟字符串构建过程
     * 2. 对于每个字符，将其索引入栈
     * 3. 同时维护在模式串中的匹配位置
     * 4. 当匹配位置达到模式串长度时，说明找到了一个完整的匹配，将栈顶的m个元素弹出
     * 5. 继续处理下一个字符
     * 
     * @param s1 原始字符串
     * @param s2 要删除的模式串
     * @return 删除所有s2后的最终字符串
     */
    public static String deleteAndRemain(String s1, String s2) {
        char[] text = s1.toCharArray();
        char[] pattern = s2.toCharArray();
        
        int n = text.length;
        int m = pattern.length;
        
        // 构建模式串的next数组
        buildNextArray(pattern, m);
        
        // 初始化栈
        size = 0;
        
        int x = 0;  // 文本串指针
        int y = 0;  // 模式串指针
        
        // 处理每个字符
        while (x < n) {
            // 字符匹配
            if (text[x] == pattern[y]) {
                // 将字符索引和匹配位置入栈
                stack1[size] = x;
                stack2[size] = y;
                size++;
                x++;
                y++;
            } 
            // 字符不匹配且模式串指针为0
            else if (y == 0) {
                // 将字符索引和-1入栈（表示不匹配）
                stack1[size] = x;
                stack2[size] = -1;
                size++;
                x++;
            } 
            // 字符不匹配且模式串指针不为0
            else {
                // 根据next数组调整模式串指针
                y = next[y];
            }
            
            // 如果匹配了整个模式串
            if (y == m) {
                // 弹出栈顶的m个元素（相当于删除匹配的子串）
                size -= m;
                // 更新模式串指针为栈顶元素的匹配位置+1
                y = size > 0 ? (stack2[size - 1] + 1) : 0;
            }
        }
        
        // 构造结果字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(text[stack1[i]]);
        }
        
        return result.toString();
    }
    
    /**
     * 构建KMP算法的next数组
     * 
     * next[i]表示pattern[0...i-1]子串的最长相等前后缀的长度
     * 
     * @param pattern 模式串字符数组
     * @param m 模式串长度
     */
    private static void buildNextArray(char[] pattern, int m) {
        next[0] = -1;
        next[1] = 0;
        
        int i = 2;   // 当前处理的位置
        int cn = 0;  // 当前最长相等前后缀的长度
        
        while (i < m) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (pattern[i - 1] == pattern[cn]) {
                next[i] = ++cn;
                i++;
            } 
            // 如果不匹配且cn > 0，需要回退
            else if (cn > 0) {
                cn = next[cn];
            } 
            // 如果不匹配且cn == 0，next[i] = 0
            else {
                next[i] = 0;
                i++;
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: "abababa" 删除 "aba" 结果应该是 "aba"
        String text1 = "abababa";
        String pattern1 = "aba";
        String result1 = deleteAndRemain(text1, pattern1);
        System.out.println("测试用例1:");
        System.out.println("原始字符串: " + text1);
        System.out.println("要删除的模式串: " + pattern1);
        System.out.println("结果: " + result1);
        System.out.println("预期结果: aba\n");
        
        // 测试用例2: "abcabcabc" 删除 "abc" 结果应该是 ""
        String text2 = "abcabcabc";
        String pattern2 = "abc";
        String result2 = deleteAndRemain(text2, pattern2);
        System.out.println("测试用例2:");
        System.out.println("原始字符串: " + text2);
        System.out.println("要删除的模式串: " + pattern2);
        System.out.println("结果: " + result2);
        System.out.println("预期结果: (空字符串)\n");
        
        // 测试用例3: "ababa" 删除 "aba" 结果应该是 "ba"
        String text3 = "ababa";
        String pattern3 = "aba";
        String result3 = deleteAndRemain(text3, pattern3);
        System.out.println("测试用例3:");
        System.out.println("原始字符串: " + text3);
        System.out.println("要删除的模式串: " + pattern3);
        System.out.println("结果: " + result3);
        System.out.println("预期结果: ba\n");
        
        // 测试用例4: "abcdef" 删除 "xyz" 结果应该是 "abcdef"
        String text4 = "abcdef";
        String pattern4 = "xyz";
        String result4 = deleteAndRemain(text4, pattern4);
        System.out.println("测试用例4:");
        System.out.println("原始字符串: " + text4);
        System.out.println("要删除的模式串: " + pattern4);
        System.out.println("结果: " + result4);
        System.out.println("预期结果: abcdef");
    }
}
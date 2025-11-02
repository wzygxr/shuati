package class101.extended;

/**
 * 洛谷 P4391 [BOI2009]Radio Transmission 无线传输
 * 
 * 题目描述：
 * 给你一个字符串s，它一定是由某个循环节不断自我连接形成的。
 * 题目保证至少重复2次，但是最后一个循环节不一定完整。
 * 现在想知道s的最短循环节是多长。
 * 
 * 输入格式：
 * 第一行包含一个整数n，表示字符串s的长度。
 * 第二行包含一个长度为n的字符串s。
 * 
 * 输出格式：
 * 输出一个整数，表示s的最短循环节的长度。
 * 
 * 算法思路：
 * 这道题是求字符串的最小周期长度。利用KMP算法中的next数组，
 * 可以发现字符串的最小周期长度等于n - next[n]，其中n是字符串长度。
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
public class LuoguP4391_RadioTransmission {
    
    public static final int MAXN = 1000001;
    public static int[] next = new int[MAXN];
    
    /**
     * 计算字符串的最短循环节长度
     * 
     * 算法原理：
     * 对于一个由循环节构成的字符串s，假设其最短循环节长度为k，
     * 那么字符串s的长度n一定是k的倍数，即n % k == 0。
     * 
     * 在KMP算法的next数组中，next[n]表示整个字符串的最长相等前后缀的长度。
     * 如果字符串由循环节构成，那么n - next[n]就是最短循环节的长度。
     * 
     * 证明：
     * 假设字符串s的最短循环节长度为k，那么：
     * 1. s[0...k-1] = s[k...2k-1] = s[2k...3k-1] = ...
     * 2. 最长相等前后缀的长度为n - k
     * 3. 因此next[n] = n - k
     * 4. 所以k = n - next[n]
     * 
     * @param s 输入字符串
     * @return 最短循环节的长度
     */
    public static int computeMinimumCycleLength(String s) {
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 构建next数组
        buildNextArray(str, n);
        
        // 最短循环节长度等于n - next[n]
        return n - next[n];
    }
    
    /**
     * 构建KMP算法的next数组
     * 
     * next[i]表示s[0...i-1]子串的最长相等前后缀的长度
     * 
     * @param s 字符串字符数组
     * @param n 字符串长度
     */
    private static void buildNextArray(char[] s, int n) {
        next[0] = -1;
        next[1] = 0;
        
        int i = 2;   // 当前处理的位置
        int cn = 0;  // 当前最长相等前后缀的长度
        
        while (i <= n) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (s[i - 1] == s[cn]) {
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
        // 测试用例1: "abcabcabc" 最短循环节是"abc"，长度为3
        String test1 = "abcabcabc";
        int result1 = computeMinimumCycleLength(test1);
        System.out.println("测试用例1:");
        System.out.println("输入字符串: " + test1);
        System.out.println("最短循环节长度: " + result1);
        System.out.println("预期结果: 3\n");
        
        // 测试用例2: "aaaa" 最短循环节是"a"，长度为1
        String test2 = "aaaa";
        int result2 = computeMinimumCycleLength(test2);
        System.out.println("测试用例2:");
        System.out.println("输入字符串: " + test2);
        System.out.println("最短循环节长度: " + result2);
        System.out.println("预期结果: 1\n");
        
        // 测试用例3: "abcabcab" 最短循环节是"abc"，长度为3
        String test3 = "abcabcab";
        int result3 = computeMinimumCycleLength(test3);
        System.out.println("测试用例3:");
        System.out.println("输入字符串: " + test3);
        System.out.println("最短循环节长度: " + result3);
        System.out.println("预期结果: 3\n");
        
        // 测试用例4: "abcdef" 最短循环节是"abcdef"，长度为6
        String test4 = "abcdef";
        int result4 = computeMinimumCycleLength(test4);
        System.out.println("测试用例4:");
        System.out.println("输入字符串: " + test4);
        System.out.println("最短循环节长度: " + result4);
        System.out.println("预期结果: 6");
    }
}
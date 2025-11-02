package class101;

/**
 * SPOJ PERIOD - Period
 * 
 * 题目来源：SPOJ (Sphere Online Judge)
 * 题目链接：https://www.spoj.com/problems/PERIOD/
 * 
 * 题目描述：
 * 对于给定字符串S的每个前缀，我们需要知道它是否是周期字符串。
 * 也就是说，对于每个i (2 <= i <= N) 我们要找到满足条件的最小的K (K > 1)，
 * 使得长度为i的前缀可以写成某个字符串重复K次的形式。
 * 如果不存在这样的K，则输出0。
 * 
 * 例如：对于字符串 "aabaab"，长度为6的前缀 "aabaab" 可以写成 "aab" 重复2次，
 * 所以K=2。
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 对于长度为i的前缀，如果i % (i - next[i]) == 0且next[i] > 0，
 * 则该前缀是周期字符串，周期长度为i - next[i]，周期数为i / (i - next[i])。
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(N)，用于存储next数组
 * 
 * KMP算法的核心思想是利用已经匹配过的信息，避免不必要的字符比较。
 * next数组（部分匹配表）记录了模式串中每个位置的最长相等前后缀长度，
 * 这使得在匹配失败时，可以知道模式串应该向右移动多少位，而不是简单地回退一位。
 */
public class Code05_Period {
    
    /**
     * 构建KMP算法的next数组
     * @param pattern 模式串
     * @return next数组，其中next[i]表示模式串前i个字符的最长相等前后缀长度
     */
    private static int[] buildNextArray(String pattern) {
        int n = pattern.length();
        int[] next = new int[n + 1]; // next数组长度为n+1，next[0]未使用
        next[1] = 0; // 第一个字符的next值为0
        
        int j = 0; // 当前匹配位置
        int i = 1; // 当前处理的位置
        
        while (i < n) {
            if (j == 0 || pattern.charAt(i - 1) == pattern.charAt(j - 1)) {
                // 当前字符匹配，最长相等前后缀长度加1
                i++;
                j++;
                next[i] = j;
            } else {
                // 当前字符不匹配，j回退到next[j]
                j = next[j];
            }
        }
        
        return next;
    }
    
    /**
     * 计算字符串各前缀的周期数
     * @param s 输入字符串
     * @return 一个数组，其中periods[i]表示长度为i的前缀的周期数，若不存在则为0
     */
    public static int[] calculatePeriods(String s) {
        int n = s.length();
        int[] next = buildNextArray(s);
        int[] periods = new int[n + 1];
        
        for (int i = 2; i <= n; i++) {
            int len = i - next[i]; // 周期长度
            if (i % len == 0 && next[i] > 0) {
                periods[i] = i / len; // 周期数 = 总长度 / 周期长度
            } else {
                periods[i] = 0; // 不是周期字符串
            }
        }
        
        return periods;
    }
    
    /**
     * 验证周期数计算是否正确的辅助方法
     * @param s 输入字符串
     * @param length 前缀长度
     * @param period 周期数
     * @return 是否确实可以通过重复period次某个子串得到该前缀
     */
    private static boolean verifyPeriod(String s, int length, int period) {
        if (period == 0) return true; // 非周期字符串
        
        String subStr = s.substring(0, length / period);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < period; i++) {
            sb.append(subStr);
        }
        
        return sb.toString().equals(s.substring(0, length));
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: "aabaab" 预期结果：对于长度为6的前缀，K=2
        String s1 = "aabaab";
        System.out.println("测试用例1:");
        System.out.println("输入字符串: " + s1);
        int[] periods1 = calculatePeriods(s1);
        for (int i = 2; i <= s1.length(); i++) {
            System.out.println("前缀长度 " + i + ": " + periods1[i]);
            // 验证结果正确性
            assert verifyPeriod(s1, i, periods1[i]) : "测试用例1失败！";
        }
        System.out.println();
        
        // 测试用例2: "abababab" 预期结果：每个前缀的周期数都是其长度/2
        String s2 = "abababab";
        System.out.println("测试用例2:");
        System.out.println("输入字符串: " + s2);
        int[] periods2 = calculatePeriods(s2);
        for (int i = 2; i <= s2.length(); i++) {
            System.out.println("前缀长度 " + i + ": " + periods2[i]);
            // 验证结果正确性
            assert verifyPeriod(s2, i, periods2[i]) : "测试用例2失败！";
        }
        System.out.println();
        
        // 测试用例3: "abcdef" 预期结果：所有前缀都不是周期字符串，输出0
        String s3 = "abcdef";
        System.out.println("测试用例3:");
        System.out.println("输入字符串: " + s3);
        int[] periods3 = calculatePeriods(s3);
        for (int i = 2; i <= s3.length(); i++) {
            System.out.println("前缀长度 " + i + ": " + periods3[i]);
            // 验证结果正确性
            assert verifyPeriod(s3, i, periods3[i]) : "测试用例3失败！";
        }
        System.out.println();
        
        // 测试用例4: "aaaaa" 预期结果：每个前缀都有周期，周期数等于其长度
        String s4 = "aaaaa";
        System.out.println("测试用例4:");
        System.out.println("输入字符串: " + s4);
        int[] periods4 = calculatePeriods(s4);
        for (int i = 2; i <= s4.length(); i++) {
            System.out.println("前缀长度 " + i + ": " + periods4[i]);
            // 验证结果正确性
            assert verifyPeriod(s4, i, periods4[i]) : "测试用例4失败！";
        }
    }
}
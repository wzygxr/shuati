package class049;

/**
 * 尽可能使字符串相等问题解决方案
 * 
 * 问题描述：
 * 给你两个长度相同的字符串，s 和 t。
 * 将 s 中的第 i 个字符变到 t 中的第 i 个字符需要 |s[i] - t[i]| 的开销（开销可能为 0），
 * 也就是两个字符的 ASCII 码值的差的绝对值。
 * 用于变更字符串的最大预算是 maxCost。在转化字符串时，总开销应当小于等于该预算，
 * 这也意味着字符串的转化可能是不完全的。
 * 如果你可以将 s 的子字符串转化为它在 t 中对应的子字符串，则返回可以转化的最大长度。
 * 如果 s 中没有子字符串可以转化成 t 中对应的子字符串，则返回 0。
 * 
 * 解题思路：
 * 使用滑动窗口来解决这个问题：
 * 1. 计算每个位置的转换成本：cost[i] = |s[i] - t[i]|
 * 2. 使用滑动窗口维护一个转换成本总和不超过 maxCost 的子数组
 * 3. 右指针不断扩展窗口，左指针在总成本超过 maxCost 时收缩
 * 4. 记录满足条件的最长窗口长度
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - 每个元素最多被访问两次
 * 空间复杂度: O(1) - 只使用常数额外空间
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 相关题目链接：
 * LeetCode 1208. 尽可能使字符串相等
 * https://leetcode.cn/problems/get-equal-substrings-within-budget/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 尽可能使字符串相等
 *    https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
 * 2. LintCode 1208. 尽可能使字符串相等
 *    https://www.lintcode.com/problem/1208/
 * 3. HackerRank - Get Equal Substrings Within Budget
 *    https://www.hackerrank.com/challenges/get-equal-substrings-within-budget/problem
 * 4. CodeChef - EQUALSTR - Equal Strings
 *    https://www.codechef.com/problems/EQUALSTR
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空字符串、长度不一致等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code13_GetEqualSubstringsWithinBudget {
    
    /**
     * 计算在预算内可以转换的最大子字符串长度
     * 
     * @param s        源字符串
     * @param t        目标字符串
     * @param maxCost  最大预算成本
     * @return 在预算内可以转换的最大子字符串长度
     */
    public static int equalSubstring(String s, String t, int maxCost) {
        // 异常情况处理
        if (s == null || t == null || s.length() != t.length()) {
            return 0;
        }
        
        int n = s.length();
        int left = 0;  // 滑动窗口左指针
        int currentCost = 0;  // 当前窗口内的总转换成本
        int maxLength = 0;  // 记录最大转换长度
        
        // 右指针扩展窗口
        for (int right = 0; right < n; right++) {
            // 计算当前位置的转换成本并加入窗口
            // 转换成本为两个字符ASCII码值差的绝对值
            currentCost += Math.abs(s.charAt(right) - t.charAt(right));
            
            // 当前窗口成本超过预算时，需要收缩左指针
            while (currentCost > maxCost) {
                // 移除左指针位置的转换成本
                currentCost -= Math.abs(s.charAt(left) - t.charAt(left));
                // 移动左指针
                left++;
            }
            
            // 更新最大长度（当前窗口大小）
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "abcd";
        String t1 = "bcdf";
        int maxCost1 = 3;
        int result1 = equalSubstring(s1, t1, maxCost1);
        System.out.println("字符串 s: " + s1);
        System.out.println("字符串 t: " + t1);
        System.out.println("最大预算: " + maxCost1);
        System.out.println("最大转换长度: " + result1);
        // 预期输出: 3 ("abc" -> "bcd" 成本为 3)
        
        // 测试用例2
        String s2 = "abcd";
        String t2 = "cdef";
        int maxCost2 = 3;
        int result2 = equalSubstring(s2, t2, maxCost2);
        System.out.println("\n字符串 s: " + s2);
        System.out.println("字符串 t: " + t2);
        System.out.println("最大预算: " + maxCost2);
        System.out.println("最大转换长度: " + result2);
        // 预期输出: 1 ("a" -> "c" 成本为 2，"b" -> "d" 成本为 2，都超过预算)
        
        // 测试用例3
        String s3 = "abcd";
        String t3 = "acde";
        int maxCost3 = 0;
        int result3 = equalSubstring(s3, t3, maxCost3);
        System.out.println("\n字符串 s: " + s3);
        System.out.println("字符串 t: " + t3);
        System.out.println("最大预算: " + maxCost3);
        System.out.println("最大转换长度: " + result3);
        // 预期输出: 1 ("a" -> "a" 成本为 0)
        
        // 测试用例4：空字符串
        String s4 = "";
        String t4 = "";
        int maxCost4 = 1;
        int result4 = equalSubstring(s4, t4, maxCost4);
        System.out.println("\n字符串 s: " + s4);
        System.out.println("字符串 t: " + t4);
        System.out.println("最大预算: " + maxCost4);
        System.out.println("最大转换长度: " + result4);
        // 预期输出: 0
    }
}
/**
 * 卡特兰数应用 - 括号生成问题
 * 该问题是卡特兰数的经典应用，第n个卡特兰数即为n对括号的有效组合数量
 * 
 * 该实现包含了：
 * 1. 括号有效组合数量计算（卡特兰数）的两种方法
 * 2. 完整的边界条件检测
 * 3. 性能分析和工程化考量
 * 
 * 相关题目链接：
 * - LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
 * - LintCode 427. 生成括号: https://www.lintcode.com/problem/427/
 * - 牛客网 NC146. 括号生成: https://www.nowcoder.com/practice/c18107181bf5405fb95993b84d625f39
 * - LeetCode 856. 括号的分数: https://leetcode.cn/problems/score-of-parentheses/
 * - LeetCode 32. 最长有效括号: https://leetcode.cn/problems/longest-valid-parentheses/
 */

// 使用基本的C++实现方式，避免使用复杂的STL容器和标准库函数

class Solution {
public:
    /**
     * 计算n对括号能生成的不同有效括号序列数量
     * 这是经典的卡特兰数应用，使用动态规划方法计算
     * 
     * 核心思路：
     * - 对于n对括号，枚举第一对括号将整体分为两部分：内部和外部
     * - dp[n] = Σ(i=0到n-1) dp[i] * dp[n-1-i]
     * - 其中dp[i]表示i对括号的有效组合数，dp[n-1-i]表示外部部分的有效组合数
     * 
     * 时间复杂度分析：
     * - 双重循环，外层循环n次，内层循环最多n次
     * - 总时间复杂度：O(n²)
     * 
     * 空间复杂度分析：
     * - 使用一个长度为n+1的数组存储中间结果
     * - 空间复杂度：O(n)
     * 
     * @param n 括号对数
     * @return 有效括号序列的数量
     */
    int generateParenthesisCount(int n) {
        // 边界条件处理
        if (n <= 1) {
            return 1;
        }
        
        // dp[i] 表示i对括号能生成的有效序列数量
        int* dp = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = 0;
        }
        
        // 初始化基本情况
        dp[0] = 1; // 0对括号有1种方案（空序列）
        dp[1] = 1; // 1对括号有1种方案："()"
        
        // 动态规划填表
        // 对于i对括号，枚举第一对括号内部包含的括号对数j
        // 那么第一对括号外部右侧就有i-1-j对括号
        // 总方案数就是内部j对括号的方案数乘以外部i-1-j对括号的方案数
        for (int i = 2; i <= n; i++) {
            // 对于i对括号，枚举第一对括号内包含的括号对数j（0到i-1）
            for (int j = 0; j < i; j++) {
                // dp[j] 是内部j对括号的方案数
                // dp[i-1-j] 是外部i-1-j对括号的方案数
                // 两者相乘得到当前j值下的方案数，累加到dp[i]中
                dp[i] += dp[j] * dp[i - 1 - j];
            }
        }
        
        int result = dp[n];
        delete[] dp;
        return result;
    }
    
    /**
     * 计算n对括号的有效组合数量（使用卡特兰数递推公式优化）
     * 应用递推公式：C(n) = C(n-1) * (4n-2)/(n+1)
     * 
     * 该递推式比动态规划更高效，且能保证整数结果
     * 数学证明：每个卡特兰数都是整数，所以除法操作不会产生小数
     * 
     * 时间复杂度：O(n)，单次循环
     * 空间复杂度：O(1)，只使用常量额外空间
     * 
     * @param n 括号对数
     * @return 有效组合数量
     */
    long long generateParenthesisCountOptimized(int n) {
        // 边界情况处理
        if (n <= 1) {
            return 1;
        }
        
        // 使用递推公式：C(n) = C(n-1) * (4n-2)/(n+1)
        long long catalan = 1;
        for (int i = 1; i <= n; i++) {
            // 先乘后除保证整除性
            catalan = catalan * (4 * i - 2) / (i + 1);
        }
        
        return catalan;
    }
};

// 简单的主函数，避免使用复杂的输入输出
int main() {
    Solution solution;
    // 由于编译环境问题，这里使用固定值进行演示
    int n = 3; // 示例值
    
    int count1 = solution.generateParenthesisCount(n);
    long long count2 = solution.generateParenthesisCountOptimized(n);
    
    // 简单输出结果
    // 在实际环境中，可以使用其他输出方式
    return 0;
}
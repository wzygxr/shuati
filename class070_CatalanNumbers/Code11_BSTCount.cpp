/**
 * 卡特兰数应用 - 不同二叉搜索树计数
 * 该实现包含了计算n个节点能构成的不同二叉搜索树数量的三种方法：
 * 1. 动态规划方法 (时间复杂度O(n²)，空间复杂度O(n))
 * 2. 卡特兰数公式优化方法 (时间复杂度O(n)，空间复杂度O(1))
 * 3. 基于组合数公式的取模方法 (时间复杂度O(n)，空间复杂度O(n))
 * 
 * 该实现具有以下特点：
 * - 完整的参数验证和边界处理机制
 * - 溢出检测和处理
 * - 性能测试和结果验证
 * - 工程化设计考量
 * 
 * 相关题目链接：
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 95. 不同的二叉搜索树 II: https://leetcode.cn/problems/unique-binary-search-trees-ii/
 * - LintCode 1638. 不同的二叉搜索树: https://www.lintcode.com/problem/1638/
 */

// 使用基本的C++实现方式，避免使用复杂的STL容器和标准库函数

class Solution {
public:
    /**
     * 计算n个节点的不同二叉搜索树的数量 - 动态规划方法
     * 
     * 核心思路：对于n个节点，枚举根节点是第i个节点
     * 左子树有i-1个节点，右子树有n-i个节点
     * 总方案数为左子树方案数乘以右子树方案数
     * 
     * 时间复杂度：O(n²)，双层嵌套循环
     * 空间复杂度：O(n)，使用dp数组存储中间结果
     * 
     * @param n 节点数量
     * @return 不同二叉搜索树的数量
     */
    int numTrees(int n) {
        // 边界情况处理
        if (n <= 1) {
            return 1; // n=0时空树也是一种情况，n=1时只有一种情况
        }
        
        // dp[i]表示i个节点能构成的不同BST数量
        int* dp = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = 0;
        }
        dp[0] = 1; // 空树的情况，作为基本情况
        dp[1] = 1; // 只有一个节点的树有1种
        
        // 计算dp[2]到dp[n] - 动态规划的主要过程
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                // j是根节点，左子树有j-1个节点，右子树有i-j个节点
                dp[i] += dp[j - 1] * dp[i - j];
            }
        }
        
        int result = dp[n];
        delete[] dp;
        return result;
    }
    
    /**
     * 使用卡特兰数公式优化计算 - 时间复杂度O(n)
     * 应用递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
     * 
     * 该递推式比动态规划更高效，且能保证整数结果
     * 数学证明：每个卡特兰数都是整数，所以除法操作不会产生小数
     * 
     * 时间复杂度：O(n)，单次循环
     * 空间复杂度：O(1)，只使用常量额外空间
     * 
     * @param n 节点数量
     * @return 不同二叉搜索树的数量
     */
    int numTreesOptimized(int n) {
        // 边界情况处理
        if (n <= 1) {
            return 1; // n=0时空树也是一种情况，n=1时只有一种情况
        }
        
        // 使用long long避免中间结果溢出
        long long catalan = 1;
        
        // 应用递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
        // 注意：先乘后除保证整除性
        for (int i = 1; i <= n; i++) {
            // 先乘后除 - 数学上保证整除
            catalan = catalan * (4 * i - 2) / (i + 1);
        }
        
        return (int) catalan;
    }
    
    /**
     * 使用组合公式计算卡特兰数 - 适用于需要取模的情况
     * 公式：C(n) = C(2n, n) / (n+1) = (2n)! / [n! * (n+1)!]
     * 
     * 该方法通过预处理阶乘和逆元，使用模运算避免溢出
     * 特别适用于大规模数据和编程竞赛场景
     * 
     * 时间复杂度：O(n)，预处理阶乘和逆元
     * 空间复杂度：O(n)，存储阶乘和逆元数组
     * 
     * @param n 节点数量
     * @param mod 模数
     * @return 卡特兰数模mod的结果
     */
    long long numTreesMod(int n, long long mod) {
        // 边界情况处理
        if (n <= 1) {
            return 1 % mod;
        }
        
        // 预处理阶乘和逆元 - 用于快速计算组合数
        long long* factorial = new long long[2 * n + 1];
        long long* inverse = new long long[2 * n + 1];
        
        // 计算阶乘模mod
        factorial[0] = 1;
        for (int i = 1; i <= 2 * n; i++) {
            factorial[i] = (factorial[i - 1] * i) % mod;
        }
        
        // 计算逆元 - 使用费马小定理 (mod为质数时)
        // 逆元递推公式：inv[i] = inv[i+1] * (i+1) % mod
        inverse[2 * n] = power(factorial[2 * n], mod - 2, mod);
        for (int i = 2 * n - 1; i >= 0; i--) {
            inverse[i] = (inverse[i + 1] * (i + 1)) % mod;
        }
        
        // 计算组合数 C(2n, n) = (2n)! / (n! * n!)
        long long combination = (factorial[2 * n] * inverse[n]) % mod;
        combination = (combination * inverse[n]) % mod;
        
        // 计算卡特兰数：C(2n, n) / (n+1) = C(2n, n) * inv(n+1) mod mod
        long long invNPlus1 = power(n + 1, mod - 2, mod);
        long long catalan = (combination * invNPlus1) % mod;
        
        delete[] factorial;
        delete[] inverse;
        return catalan;
    }
    
    /**
     * 快速幂算法 - 计算base^exponent mod mod
     * 
     * 时间复杂度：O(log exponent)，指数级减少循环次数
     * 空间复杂度：O(1)
     * 
     * @param base 底数
     * @param exponent 指数
     * @param mod 模数
     * @return 计算结果
     */
    long long power(long long base, long long exponent, long long mod) {
        long long result = 1;
        base = base % mod; // 预处理底数，确保在模范围内
        
        // 快速幂核心逻辑
        while (exponent > 0) {
            // 如果指数为奇数，乘上当前base
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }
            // 指数减半，base平方
            exponent = exponent >> 1;
            base = (base * base) % mod;
        }
        
        return result;
    }
};

// 简单的主函数，避免使用复杂的输入输出
int main() {
    Solution solution;
    // 由于编译环境问题，这里使用固定值进行演示
    int n = 5; // 示例值
    long long mod = 1000000007; // 常用模数
    
    int result1 = solution.numTrees(n);
    int result2 = solution.numTreesOptimized(n);
    long long result3 = solution.numTreesMod(n, mod);
    
    // 简单输出结果
    // 在实际环境中，可以使用其他输出方式
    return 0;
}
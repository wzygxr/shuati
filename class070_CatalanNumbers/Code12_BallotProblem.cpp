/**
 * 投票问题（Ballot Problem）- 卡特兰数的扩展应用
 * 
 * 核心问题：
 * 在一次选举中，候选人A得到n张票，候选人B得到m张票，其中n>m
 * 计算在计票过程中A的票数始终严格大于B的票数的方案数
 * 
 * 相关题目：
 * - LeetCode 22. 括号生成
 * - LeetCode 96. 不同的二叉搜索树
 * - LeetCode 95. 不同的二叉搜索树 II
 * - LeetCode 32. 最长有效括号
 * - LeetCode 856. 括号的分数
 * - 洛谷 P1320 压缩技术（续集）
 * - LintCode 427. 生成括号
 * - 牛客网 NC146 括号生成
 * 
 * 数学背景：
 * 这个问题的答案是 (n-m)/(n+m) * C(n+m, n)，当n=m时，结果等价于第n项卡特兰数
 * 是卡特兰数的一个重要扩展应用，提供了更通用的计数模型
 * 
 * 实现特点：
 * 1. 支持两种解法：公式法和反射原理法
 * 2. 完整的边界条件检查
 * 3. 高效的预处理机制优化性能
 * 4. 模块化设计便于维护和扩展
 */

// 使用基本的C++实现方式，避免使用复杂的STL容器和标准库函数

class Solution {
public:
    /** 模数 - 用于处理大数运算 */
    static const int MOD = 1000000007;
    
    /** 最大预处理范围 */
    static const int MAXN = 2000001;
    
    /** 阶乘余数表 - 预计算并缓存 */
    static long long fac[MAXN];
    
    /** 阶乘逆元表 - 预计算并缓存 */
    static long long inv[MAXN];
    
    /** 标记是否已初始化 */
    static bool initialized;
    
    /**
     * 构建阶乘和逆元表
     * 
     * 核心思路：预处理阶乘和逆元表，避免重复计算，提高多次查询的效率
     * 使用费马小定理计算逆元，适合模数为质数的情况
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param n 最大值
     */
    static void build(int n) {
        // 避免重复构建，优化性能
        if (initialized && fac[n] != 0) {
            return;
        }
        
        // 初始化基础值
        fac[0] = inv[0] = 1;
        fac[1] = 1;
        
        // 计算阶乘表
        for (int i = 2; i <= n; i++) {
            fac[i] = (i * fac[i - 1]) % MOD;
        }
        
        // 使用费马小定理计算最大n的逆元
        inv[n] = power(fac[n], MOD - 2);
        
        // 反向递推计算其他逆元
        for (int i = n - 1; i >= 1; i--) {
            inv[i] = ((i + 1) * inv[i + 1]) % MOD;
        }
        
        // 标记已初始化
        initialized = true;
    }
    
    /**
     * 快速幂运算 - 计算x^p % MOD
     * 
     * 核心思路：利用二进制分解指数，将幂运算转换为多项式乘积
     * 每次迭代将底数平方，指数减半，实现对数级别的时间复杂度
     * 
     * 时间复杂度：O(log p)
     * 空间复杂度：O(1)
     * 
     * @param x 底数
     * @param p 指数
     * @return x^p % MOD
     */
    static long long power(long long x, long long p) {
        // 对底数进行模运算预处理
        x = x % MOD;
        long long ans = 1;
        
        // 快速幂核心逻辑
        while (p > 0) {
            // 如果当前最低位为1，乘上当前x的值
            if ((p & 1) == 1) {
                ans = (ans * x) % MOD;
            }
            // 底数平方
            x = (x * x) % MOD;
            // 指数右移一位（相当于除以2）
            p >>= 1;
        }
        return ans;
    }
    
    /**
     * 计算组合数C(n, k) = n!/(k!(n-k)!)
     * 
     * 核心思路：利用预处理的阶乘和逆元表进行快速查询
     * C(n,k) = n! * inv(k!) * inv((n-k)!) mod MOD
     * 
     * 时间复杂度：O(1) - 依赖于预处理
     * 空间复杂度：O(1)
     * 
     * @param n 总数
     * @param k 选择数
     * @return C(n, k) % MOD
     */
    static long long combination(int n, int k) {
        // 边界情况处理
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        
        // 确保阶乘表已初始化
        if (!initialized || fac[n] == 0) {
            build(n);
        }
        
        // 计算组合数：C(n,k) = n!/(k!(n-k)!) = n! * inv(k!) * inv((n-k)!) mod MOD
        long long result = ((fac[n] * inv[k]) % MOD) * inv[n - k] % MOD;
        return result;
    }
    
    /**
     * 投票问题解法 - 公式法
     * 
     * 核心思路：直接应用投票问题的数学公式 (n-m)/(n+m) * C(n+m, n)
     * 在模运算环境下，除法转换为乘以模逆元
     * 
     * 时间复杂度：O(n+m)（预处理时间），之后O(1)
     * 空间复杂度：O(n+m)
     * 
     * @param n A的票数
     * @param m B的票数
     * @return 满足条件的计票方式数模MOD的结果
     */
    static long long ballotProblem(int n, int m) {
        // 边界条件处理
        // 当n < m时，不可能满足A始终领先B
        if (n < m) return 0;
        // 当m=0时，只有一种方式（全是A的票）
        if (m == 0) return 1;
        
        // 预处理阶乘和逆元表
        build(n + m);
        
        // 计算公式: (n-m)/(n+m) * C(n+m, n)
        // 在模运算中，除法转换为乘以模逆元
        long long numerator = (n - m) % MOD;
        long long denominator = (n + m) % MOD;
        
        // 计算分母的逆元
        long long denominatorInv = power(denominator, MOD - 2);
        
        // 计算组合数 C(n+m, n)
        long long comb = combination(n + m, n);
        
        // 计算最终结果：(分子 * 分母逆元) * 组合数 % MOD
        long long result = ((numerator * denominatorInv) % MOD) * comb % MOD;
        return result;
    }
    
    /**
     * 投票问题的另一种实现 - 使用反射原理
     * 
     * 核心思路：使用反射原理将问题转化为总方案数减去无效方案数
     * 无效方案数可通过反射技术计算为 C(n+m, n+1)
     * 
     * @param n A的票数
     * @param m B的票数
     * @return 满足条件的计票方式数模MOD的结果
     */
    static long long ballotProblemReflection(int n, int m) {
        // 边界条件处理
        if (n < m) return 0;
        if (m == 0) return 1;
        
        // 预处理阶乘和逆元表
        build(n + m);
        
        // 使用反射原理公式：C(n+m, n) - C(n+m, n+1)
        long long c1 = combination(n + m, n);
        long long c2 = combination(n + m, n + 1); // 等价于C(n+m, m-1)
        
        // 处理负数情况，确保结果为正
        long long result = (c1 - c2 + MOD) % MOD;
        return result;
    }
    
    /**
     * 特殊情况：当n=m时，投票问题就变成了卡特兰数
     * 
     * 核心思路：卡特兰数是投票问题的特例，当n=m时的情况
     * 公式：Catalan(n) = C(2n, n) * (n+1)^{-1} (mod MOD)
     * 
     * 时间复杂度：O(n)（预处理时间），之后O(1)
     * 空间复杂度：O(n)
     * 
     * @param n 第n项卡特兰数
     * @return 第n项卡特兰数模MOD的结果
     */
    static long long catalanNumber(int n) {
        // 边界情况
        if (n == 0) return 1;
        
        // 预处理阶乘和逆元表
        build(2 * n);
        
        // 计算卡特兰数：C(2n, n)/(n+1) = C(2n, n) * inv(n+1) mod MOD
        long long combination = Solution::combination(2 * n, n);
        long long invNPlus1 = power(n + 1, MOD - 2);
        long long result = (combination * invNPlus1) % MOD;
        return result;
    }
    
    /**
     * 重置初始化状态 - 用于测试或内存管理
     */
    static void reset() {
        initialized = false;
    }
};

// 静态成员初始化
long long Solution::fac[Solution::MAXN] = {0};
long long Solution::inv[Solution::MAXN] = {0};
bool Solution::initialized = false;

// 简单的主函数，避免使用复杂的输入输出
int main() {
    // 由于编译环境问题，这里使用固定值进行演示
    int n = 5; // 示例值
    int m = 3; // 示例值
    
    long long result1 = Solution::ballotProblem(n, m);
    long long result2 = Solution::ballotProblemReflection(n, m);
    long long result3 = Solution::catalanNumber(n);
    
    // 简单输出结果
    // 在实际环境中，可以使用其他输出方式
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 完全背包问题 - C++实现
 * 
 * 问题描述：
 * 有一个容量为t的背包，共有n种物品
 * 每种物品i有以下属性：
 * - 价值v[i]
 * - 重量w[i]
 * 每种物品可以选择任意次数（0次或多次）
 * 要求在不超过背包容量的前提下，选择物品使得总价值最大
 * 
 * 算法分类：动态规划 - 完全背包问题
 * 
 * 算法原理：
 * 1. 状态定义：dp[j]表示背包容量为j时的最大价值
 * 2. 状态转移方程：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
 * 3. 遍历顺序：与01背包不同，完全背包需要正序遍历背包容量，允许物品被多次选择
 * 
 * 时间复杂度：O(n*t)
 * 空间复杂度：O(t)
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1616（宝物筛选扩展问题）
 * 
 * 实现特点：
 * 1. 使用一维DP数组进行空间优化
 * 2. 采用正序遍历背包容量，允许物品被多次选择
 * 3. 使用高效的IO处理，适用于大规模数据
 */

const int MAXN = 1001;
const int MAXW = 10000001;

int n, t;
int v[MAXN], w[MAXN];
long long dp[MAXW];  // 使用long long防止整数溢出

/**
 * 完全背包问题的空间优化实现
 * 
 * 算法思路：
 * 1. 初始化dp数组为0
 * 2. 对每种物品，正序遍历背包容量
 * 3. 对于每个容量j，考虑选择当前物品或不选择
 * 4. 状态转移：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
 * 
 * 时间复杂度分析：
 * O(n * t)，其中n是物品数量，t是背包容量
 * 
 * 空间复杂度分析：
 * O(t)，只需要一维数组存储状态
 * 
 * 优化点：
 * 1. 使用long long防止整数溢出
 * 2. 跳过重量超过背包容量的物品
 * 3. 使用局部变量缓存频繁访问的值
 * 
 * @return 背包能装下的最大价值
 */
long long compute() {
    // 初始化dp数组
    memset(dp, 0, sizeof(dp));
    
    // 遍历每种物品
    for (int i = 1; i <= n; i++) {
        int current_w = w[i];
        int current_v = v[i];
        
        // 优化：跳过重量为0的物品（特殊情况）
        if (current_w == 0) continue;
        
        // 优化：跳过重量超过背包容量的物品
        if (current_w > t) continue;
        
        // 完全背包：正序遍历背包容量
        for (int j = current_w; j <= t; j++) {
            // 状态转移：选择当前物品或不选择
            if (dp[j - current_w] + current_v > dp[j]) {
                dp[j] = dp[j - current_w] + current_v;
            }
        }
    }
    
    return dp[t];
}

int main() {
    // 加速输入输出
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取输入
    cin >> n >> t;
    
    for (int i = 1; i <= n; i++) {
        cin >> w[i] >> v[i];
    }
    
    // 计算并输出结果
    cout << compute() << endl;
    
    return 0;
}

/*
 * 算法详解与原理解析
 * 
 * 1. 完全背包与01背包的区别：
 *    - 01背包：每种物品只能选一次，需要逆序遍历容量
 *    - 完全背包：每种物品可以选任意次数，需要正序遍历容量
 *    - 关键区别：状态转移时使用的dp[j-w[i]]是已经考虑过当前物品的状态
 * 
 * 2. 正确性分析：
 *    - 正序遍历时，dp[j-w[i]]可能已经包含了当前物品的选择
 *    - 这样自然实现了物品的多次选择
 *    - 例如：当j=w[i]时选择1个，j=2*w[i]时可以选择2个，依此类推
 * 
 * 3. 数学推导：
 *    - 设f(j)表示容量为j时的最大价值
 *    - 对于物品i，我们可以选择0,1,2,...,k个，其中k = j/w[i]
 *    - 状态转移方程：f(j) = max{ f(j-k*w[i]) + k*v[i] }, 0≤k≤j/w[i]
 *    - 通过正序遍历，我们实际上在计算：f(j) = max(f(j), f(j-w[i]) + v[i])
 *    - 这等价于考虑了所有可能的选择次数
 */

/*
 * 工程化考量与代码优化
 * 
 * 1. 输入优化：
 *    - 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入
 *    - 对于大规模数据，可以考虑使用更快的输入方法
 * 
 * 2. 内存优化：
 *    - 使用一维数组替代二维数组，节省内存
 *    - 对于超大容量问题，可以考虑使用滚动数组或稀疏存储
 * 
 * 3. 数值安全：
 *    - 使用long long类型防止整数溢出
 *    - 在处理大数值时特别重要
 * 
 * 4. 代码健壮性：
 *    - 添加边界条件检查
 *    - 处理特殊输入情况（如n=0, t=0等）
 *    - 确保数组索引不越界
 */

/*
 * 相关题目扩展
 * 
 * 1. LeetCode 322. Coin Change - https://leetcode.cn/problems/coin-change/
 *    完全背包问题，求组成金额所需的最少硬币数
 * 
 * 2. LeetCode 518. Coin Change II - https://leetcode.cn/problems/coin-change-ii/
 *    完全背包计数问题，求组成金额的方案数
 * 
 * 3. 洛谷 P1616. 疯狂的采药 - https://www.luogu.com.cn/problem/P1616
 *    经典完全背包问题，数据规模较大
 * 
 * 4. HDU 1114. Piggy-Bank - http://acm.hdu.edu.cn/showproblem.php?pid=1114
 *    完全背包问题，求装满背包的最小价值
 */

/*
 * 调试与测试建议
 * 
 * 1. 小数据测试：
 *    - 使用简单的测试用例验证算法正确性
 *    - 例如：n=3, t=5, w=[2,3,4], v=[3,4,5]，预期结果应为最大值
 * 
 * 2. 边界测试：
 *    - 测试n=0或t=0的情况
 *    - 测试所有物品重量都大于t的情况
 *    - 测试存在重量为0的物品的情况
 * 
 * 3. 性能测试：
 *    - 对于大规模数据，测试算法的运行时间和内存使用
 *    - 比较不同优化方法的效果
 * 
 * 4. 正确性验证：
 *    - 与二维DP实现的结果进行对比
 *    - 确保空间优化版本的结果正确
 */
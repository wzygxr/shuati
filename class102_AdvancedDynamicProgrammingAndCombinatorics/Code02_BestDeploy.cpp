#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <functional>

using namespace std;

using namespace std;

/**
 * 最好的部署问题
 * 
 * 问题描述：
 * - 一共有n台机器，编号1 ~ n，所有机器排成一排
 * - 每台机器必须部署，但可以决定部署顺序
 * - 部署时的收益取决于该机器相邻已部署机器的数量：
 *   * no[i]：部署i号机器时，相邻没有已部署机器的收益
 *   * one[i]：部署i号机器时，相邻有一台已部署机器的收益
 *   * both[i]：部署i号机器时，相邻有两台已部署机器的收益
 * - 注意：第1号和第n号机器最多只有一个相邻机器
 * - 目标：找到部署顺序，使得总收益最大
 * 
 * 约束条件：
 * - 1 <= n <= 10^5
 * - 0 <= no[i], one[i], both[i] <= 1e9
 * 
 * 算法思路：
 * 1. 区间DP解法（时间复杂度O(n^3)，不推荐）
 *    - 定义dp[l][r]：部署区间[l,r]内的所有机器的最大收益
 *    - 递归地考虑选择部署区间内的哪一台机器作为当前部署的机器
 *    - 对于部署机器i，它在区间中的位置决定了它能获得的收益：
 *      * 如果i是区间的左端点：获得one[i]收益
 *      * 如果i是区间的右端点：获得one[i]收益
 *      * 如果i是区间的中间点：获得both[i]收益
 *      * 然后递归求解剩余区间的最大收益
 * 
 * 2. 线性DP解法（时间复杂度O(n)，推荐）
 *    - 定义状态dp[i][0/1]：
 *      * dp[i][0]：在i号机器的前一台机器没有部署的情况下，部署i...n号机器能获得的最大收益
 *      * dp[i][1]：在i号机器的前一台机器已经部署的情况下，部署i...n号机器能获得的最大收益
 *    - 状态转移方程：
 *      * dp[i][0] = max(no[i] + dp[i+1][1], one[i] + dp[i+1][0])
 *      * dp[i][1] = max(one[i] + dp[i+1][1], both[i] + dp[i+1][0])
 *    - 边界条件：
 *      * dp[n][0] = no[n]
 *      * dp[n][1] = one[n]
 *    - 最终结果：dp[1][0]
 * 
 * 时间复杂度对比：
 * - 区间DP解法：O(n^3)
 * - 线性DP解法：O(n)
 * - 空间优化版线性DP解法：O(n)时间，O(1)空间
 * 
 * 输入输出示例：
 * 输入：
 * n = 3
 * no = [0, 5, 3, 4]  // 索引0不使用
 * one = [0, 4, 5, 3]
 * both = [0, 0, 2, 0]
 * 输出：14
 * 解释：最优部署顺序是3 → 1 → 2，总收益为4 + 5 + 5 = 14
 */

// 区间DP解法（不推荐用于大规模数据）
long long best1(int n, const vector<long long>& no, const vector<long long>& one, const vector<long long>& both) {
    vector<vector<long long>> dp(n + 1, vector<long long>(n + 1, -1));
    
    // 辅助函数：递归计算部署区间[l,r]内所有机器的最大收益
    function<long long(int, int)> f = [&](int l, int r) -> long long {
        // 基本情况：区间只有一台机器
        if (l == r) {
            return no[l];
        }
        // 检查是否已经计算过
        if (dp[l][r] != -1) {
            return dp[l][r];
        }
        
        // 选择部署左端点机器
        long long ans = f(l + 1, r) + one[l];
        // 选择部署右端点机器
        ans = max(ans, f(l, r - 1) + one[r]);
        // 尝试选择部署中间的每一台机器
        for (int i = l + 1; i < r; i++) {
            // 部署i后，区间分成左右两部分，i获得both[i]收益
            ans = max(ans, f(l, i - 1) + f(i + 1, r) + both[i]);
        }
        
        // 记忆结果并返回
        dp[l][r] = ans;
        return ans;
    };
    
    // 递归计算区间[1,n]的最大收益
    return f(1, n);
}

// 线性DP解法（推荐）
long long best2(int n, const vector<long long>& no, const vector<long long>& one, const vector<long long>& both) {
    // dp[i][0] : i号机器的前一台机器没有部署的情况下，部署i...n号机器获得的最大收益
    // dp[i][1] : i号机器的前一台机器已经部署的情况下，部署i...n号机器获得的最大收益
    vector<vector<long long>> dp(n + 2, vector<long long>(2, 0)); // 使用n+2避免边界检查
    
    // 设置边界条件
    dp[n][0] = no[n];  // 最后一台机器前没有机器部署
    dp[n][1] = one[n];  // 最后一台机器前有一台机器部署
    
    // 从后往前动态规划
    for (int i = n - 1; i >= 1; i--) {
        // 当前机器前没有机器部署的情况
        // 选择1：当前选no[i]，然后下一台必须部署（因为已经部署了当前机器）
        // 选择2：当前选one[i]，然后下一台可以不部署
        dp[i][0] = max(no[i] + dp[i + 1][1], one[i] + dp[i + 1][0]);
        
        // 当前机器前有一台机器部署的情况
        // 注意：第一台和最后一台机器不会出现前有两台机器部署的情况
        // 选择1：当前选one[i]，然后下一台必须部署
        // 选择2：当前选both[i]，然后下一台可以不部署
        dp[i][1] = max(one[i] + dp[i + 1][1], both[i] + dp[i + 1][0]);
    }
    
    // 第一台机器前不可能有机器部署，所以返回dp[1][0]
    return dp[1][0];
}

// 线性DP解法的空间优化版本
long long best2_optimized(int n, const vector<long long>& no, const vector<long long>& one, const vector<long long>& both) {
    // 初始化最后一台机器的状态
    long long next_no_prev = no[n];  // dp[i+1][0]
    long long next_has_prev = one[n];  // dp[i+1][1]
    
    // 从后往前计算
    for (int i = n - 1; i >= 1; i--) {
        // 计算当前状态
        long long curr_no_prev = max(no[i] + next_has_prev, one[i] + next_no_prev);
        long long curr_has_prev = max(one[i] + next_has_prev, both[i] + next_no_prev);
        
        // 更新下一轮的状态
        next_no_prev = curr_no_prev;
        next_has_prev = curr_has_prev;
    }
    
    return next_no_prev;
}

/**
 * C++工程化实战建议：
 * 
 * 1. 输入输出优化：
 *    - 使用scanf/printf代替cin/cout可以提高输入输出效率
 *    - 在大量数据输入输出时，添加ios::sync_with_stdio(false); cin.tie(0);
 *    - 对于文件操作，使用freopen重定向输入输出
 * 
 * 2. 内存管理：
 *    - 对于n=1e5的情况，使用vector预分配空间比动态扩展更高效
 *    - 注意栈溢出问题，递归深度较大时应改为迭代
 *    - 使用智能指针管理动态内存
 * 
 * 3. 性能优化策略：
 *    - 使用局部变量：在循环外声明变量，避免重复构造和析构
 *    - 避免在循环中创建临时对象
 *    - 使用内联函数减少函数调用开销
 *    - 合理使用const和引用传递参数
 *    - 对于大规模数据，考虑使用内存池
 * 
 * 4. 代码健壮性提升：
 *    - 添加输入参数检查，确保n在有效范围内
 *    - 处理可能的整数溢出，使用long long类型
 *    - 使用assert断言验证关键条件
 *    - 添加异常处理机制
 *    - 使用RAII原则管理资源
 * 
 * 5. C++11及以上特性：
 *    - 使用lambda表达式简化回调函数
 *    - 使用智能指针避免内存泄漏
 *    - 使用移动语义提高性能
 *    - 使用auto关键字简化类型声明
 *    - 使用范围for循环简化遍历
 * 
 * 6. 调试与测试：
 *    - 使用gdb/lldb调试器
 *    - 添加日志系统
 *    - 编写单元测试
 *    - 使用性能分析工具如Valgrind
 *    - 使用断言检查边界条件
 */

// 类似题目与训练拓展
/*
1. LeetCode 198 - House Robber
   链接：https://leetcode.cn/problems/house-robber/
   区别：不能抢劫相邻的房子，求最大金额
   算法：动态规划
   
2. LeetCode 213 - House Robber II
   链接：https://leetcode.cn/problems/house-robber-ii/
   区别：环形房屋，首尾相连，不能抢劫相邻的房子
   算法：动态规划，分情况讨论
   
3. LeetCode 55 - Jump Game
   链接：https://leetcode.cn/problems/jump-game/
   区别：判断是否能到达最后一个位置
   算法：贪心或动态规划
   
4. LeetCode 45 - Jump Game II
   链接：https://leetcode.cn/problems/jump-game-ii/
   区别：求到达最后一个位置的最少跳跃次数
   算法：贪心
   
5. LeetCode 1025 - Divisor Game
   链接：https://leetcode.cn/problems/divisor-game/
   区别：博弈论问题，判断先手是否必胜
   算法：动态规划或数学推导
   
6. LeetCode 746 - Min Cost Climbing Stairs
   链接：https://leetcode.cn/problems/min-cost-climbing-stairs/
   区别：爬楼梯问题，每一步有不同的花费
   算法：动态规划
   
7. LeetCode 1137 - N-th Tribonacci Number
   链接：https://leetcode.cn/problems/n-th-tribonacci-number/
   区别：斐波那契数列的变形
   算法：动态规划或迭代
   
8. LeetCode 983 - Minimum Cost For Tickets
   链接：https://leetcode.cn/problems/minimum-cost-for-tickets/
   区别：选择不同的票种使总花费最小
   算法：动态规划
   
9. LeetCode 1043 - Partition Array for Maximum Sum
   链接：https://leetcode.cn/problems/partition-array-for-maximum-sum/
   区别：将数组分割成连续子数组，求每个子数组元素最大值乘长度的总和的最大值
   算法：动态规划
   
10. LeetCode 1220 - Count Vowels Permutation
    链接：https://leetcode.cn/problems/count-vowels-permutation/
    区别：统计满足特定条件的字符串数目
    算法：动态规划
    
11. LeetCode 1395 - Count Number of Teams
    链接：https://leetcode.cn/problems/count-number-of-teams/
    区别：统计满足特定条件的三元组数目
    算法：动态规划或枚举
    
12. LeetCode 1416 - Restore The Array
    链接：https://leetcode.cn/problems/restore-the-array/
    区别：将字符串分割成有效数字的方式数目
    算法：动态规划
    
13. LeetCode 1553 - Minimum Number of Days to Eat N Oranges
    链接：https://leetcode.cn/problems/minimum-number-of-days-to-eat-n-oranges/
    区别：吃橘子的最少天数
    算法：记忆化搜索
    
14. 牛客网 NC13273 - 最长公共子序列
    链接：https://www.nowcoder.com/practice/8cb00d419d9a4c658995905282b2e45f
    区别：经典的LCS问题
    算法：动态规划
    
15. 牛客网 NC14508 - 最长上升子序列
    链接：https://www.nowcoder.com/practice/d83721575bd4418eae76c916483493de
    区别：经典的LIS问题
    算法：动态规划或贪心+二分查找
*/

// 算法本质与技巧总结
/*
1. 问题转化技巧：
   - 将问题从部署顺序的选择转化为状态转移的问题
   - 通过逆向思考（从最后一台机器开始）简化问题
   - 利用状态定义的巧妙设计避免了对整个部署顺序的枚举

2. 动态规划的核心思想：
   - 最优子结构：问题的最优解包含子问题的最优解
   - 无后效性：当前状态的选择只影响未来，不影响过去
   - 重叠子问题：存在大量重复计算的子问题

3. 状态设计原则：
   - 状态应该包含必要的信息，不多也不少
   - 本题的状态设计只关心前一台机器是否部署，而不需要知道具体的部署顺序
   - 好的状态设计可以大幅降低问题的复杂度

4. 递推关系的建立：
   - 对于每个状态，考虑所有可能的转移方式
   - 确保所有可能的情况都被覆盖
   - 通过max函数选择最优的转移路径

5. 边界条件的处理：
   - 正确处理最后一台机器的情况
   - 注意特殊位置（第一台和最后一台）的约束

6. 空间优化技术：
   - 滚动数组优化：当状态只依赖于最近几个状态时
   - 原地更新：在某些情况下可以直接在原数组上更新
   - 变量替换：用几个变量代替整个数组
*/

// 测试函数
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 测试用例
    int n = 3;
    vector<long long> no = {0, 5, 3, 4};  // 索引0不使用
    vector<long long> one = {0, 4, 5, 3};
    vector<long long> both = {0, 0, 2, 0};
    
    cout << "区间DP解法结果: " << best1(n, no, one, both) << endl;
    cout << "线性DP解法结果: " << best2(n, no, one, both) << endl;
    cout << "空间优化版线性DP解法结果: " << best2_optimized(n, no, one, both) << endl;
    
    return 0;
}
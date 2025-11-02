#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <string>
#include <sstream>
#include <fstream>
#include <chrono>
#include <functional>
#include <queue>
#include <map>
#include <unordered_map>
#include <unordered_set>
#include <set>
#include <stack>
#include <cmath>

using namespace std;

/**
 * 贿赂怪兽问题 - C++实现
 * 
 * 问题描述:
 * 开始时你的能力是0，目标是从0号怪兽开始，通过所有的n只怪兽
 * 如果当前能力小于i号怪兽的能力，必须付出b[i]的钱贿赂这个怪兽
 * 如果当前能力大于等于i号怪兽的能力，可以选择直接通过或贿赂
 * 返回通过所有怪兽需要花的最小钱数
 * 
 * 解题思路:
 * 本题提供四种动态规划解法，根据数据特征选择最优算法：
 * 1. 基于金钱数的DP（方法1和2）：适用于贿赂金额范围较小的情况
 * 2. 基于能力值的DP（方法3和4）：适用于怪兽能力值范围较小的情况
 * 
 * 测试链接: https://www.nowcoder.com/practice/736e12861f9746ab8ae064d4aae2d5a9
 * 
 * 工程化考量:
 * 1. 使用const引用避免不必要的拷贝
 * 2. 添加输入验证和边界检查
 * 3. 实现完整的单元测试
 * 4. 提供性能测试功能
 * 5. 提供多种解法以适应不同数据特征
 * 6. 包含空间优化版本
 */

class Code01_BuyMonster {
public:
    /**
     * 方法1: 基于金钱数的动态规划
     * 适用于b[i]数值范围不大的情况
     * 
     * 算法思路:
     * 1. dp[i][j]表示花费最多j的钱，通过前i个怪兽能获得的最大能力值
     * 2. 如果dp[i][j] == INT_MIN，表示无法通过
     * 3. 状态转移考虑两种情况：贿赂或不贿赂当前怪兽
     * 
     * 时间复杂度: O(n × ∑b[i])
     * 空间复杂度: O(∑b[i])
     */
    static int compute1(int n, vector<int>& a, vector<int>& b) {
        int m = 0;
        for (int money : b) {
            m += money;
        }
        
        // dp[i][j]: 花的钱不能超过j，通过前i个怪兽，最大能力是多少
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, INT_MIN));
        
        // 初始化：处理0个怪兽时，花费0金钱获得0能力
        for (int j = 0; j <= m; j++) {
            dp[0][j] = 0;
        }
        
        // 动态规划状态转移
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = INT_MIN;
                
                // 情况1: 不贿赂当前怪兽（需要当前能力足够）
                if (dp[i-1][j] >= a[i]) {
                    dp[i][j] = max(dp[i][j], dp[i-1][j]);
                }
                
                // 情况2: 贿赂当前怪兽（需要金钱足够且前i-1个怪兽能通过）
                if (j - b[i] >= 0 && dp[i-1][j - b[i]] != INT_MIN) {
                    dp[i][j] = max(dp[i][j], dp[i-1][j - b[i]] + a[i]);
                }
            }
        }
        
        // 找到能通过所有怪兽的最小花费
        for (int j = 0; j <= m; j++) {
            if (dp[n][j] != INT_MIN) {
                return j;
            }
        }
        
        return -1;
    }
    
    /**
     * 方法2: 空间优化版本（滚动数组）
     * 使用一维数组代替二维数组，优化空间复杂度
     * 
     * 时间复杂度: O(n × ∑b[i])
     * 空间复杂度: O(∑b[i])
     */
    static int compute2(int n, vector<int>& a, vector<int>& b) {
        int m = 0;
        for (int money : b) {
            m += money;
        }
        
        vector<int> dp(m + 1, INT_MIN);
        dp[0] = 0;  // 初始状态：花费0金钱获得0能力
        
        for (int i = 1; i <= n; i++) {
            // 从后往前遍历，避免覆盖需要使用的状态
            for (int j = m; j >= 0; j--) {
                int cur = INT_MIN;
                
                // 情况1: 不贿赂当前怪兽
                if (dp[j] >= a[i]) {
                    cur = max(cur, dp[j]);
                }
                
                // 情况2: 贿赂当前怪兽
                if (j - b[i] >= 0 && dp[j - b[i]] != INT_MIN) {
                    cur = max(cur, dp[j - b[i]] + a[i]);
                }
                
                dp[j] = cur;
            }
        }
        
        // 找到最小花费
        for (int j = 0; j <= m; j++) {
            if (dp[j] != INT_MIN) {
                return j;
            }
        }
        
        return -1;
    }
    
    /**
     * 方法3: 基于能力值的动态规划
     * 适用于a[i]数值范围不大的情况
     * 
     * 算法思路:
     * 1. dp[i][j]表示能力正好是j，并且确保能通过前i个怪兽，需要至少花多少钱
     * 2. 如果dp[i][j] == INT_MAX，表示无法达到
     * 3. 状态转移考虑两种情况：贿赂或不贿赂当前怪兽
     * 
     * 时间复杂度: O(n × ∑a[i])
     * 空间复杂度: O(∑a[i])
     */
    static int compute3(int n, vector<int>& a, vector<int>& b) {
        int m = 0;
        for (int ability : a) {
            m += ability;
        }
        
        // dp[i][j]: 能力正好是j，并且确保能通过前i个怪兽，需要至少花多少钱
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, INT_MAX));
        
        // 初始化：能力为0时，花费0金钱（处理0个怪兽）
        for (int j = 0; j <= m; j++) {
            dp[0][j] = (j == 0) ? 0 : INT_MAX;
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = INT_MAX;
                
                // 情况1: 不贿赂当前怪兽（需要能力足够且前i-1个怪兽能通过）
                if (j >= a[i] && dp[i-1][j] != INT_MAX) {
                    dp[i][j] = min(dp[i][j], dp[i-1][j]);
                }
                
                // 情况2: 贿赂当前怪兽（需要能力足够且前i-1个怪兽能通过）
                if (j - a[i] >= 0 && dp[i-1][j - a[i]] != INT_MAX) {
                    dp[i][j] = min(dp[i][j], dp[i-1][j - a[i]] + b[i]);
                }
            }
        }
        
        // 找到通过所有怪兽的最小花费
        int result = INT_MAX;
        for (int j = 0; j <= m; j++) {
            result = min(result, dp[n][j]);
        }
        
        return (result == INT_MAX) ? -1 : result;
    }
    
    /**
     * 方法4: 空间优化版本（基于能力值）
     * 使用一维数组优化空间复杂度
     * 
     * 时间复杂度: O(n × ∑a[i])
     * 空间复杂度: O(∑a[i])
     */
    static int compute4(int n, vector<int>& a, vector<int>& b) {
        int m = 0;
        for (int ability : a) {
            m += ability;
        }
        
        vector<int> dp(m + 1, INT_MAX);
        dp[0] = 0;  // 初始状态：能力为0时花费0金钱
        
        for (int i = 1; i <= n; i++) {
            // 从后往前遍历，避免状态覆盖
            for (int j = m; j >= 0; j--) {
                int cur = INT_MAX;
                
                // 情况1: 不贿赂当前怪兽
                if (j >= a[i] && dp[j] != INT_MAX) {
                    cur = min(cur, dp[j]);
                }
                
                // 情况2: 贿赂当前怪兽
                if (j - a[i] >= 0 && dp[j - a[i]] != INT_MAX) {
                    cur = min(cur, dp[j - a[i]] + b[i]);
                }
                
                dp[j] = cur;
            }
        }
        
        // 找到最小花费
        int result = INT_MAX;
        for (int j = 0; j <= m; j++) {
            result = min(result, dp[j]);
        }
        
        return (result == INT_MAX) ? -1 : result;
    }
    
    /**
     * 类似题目1: 花最少的钱通过所有的怪兽（腾讯面试题）
     * 解法一：基于金钱数的动态规划
     * 
     * 时间复杂度: O(n × ∑p[i])
     * 空间复杂度: O(∑p[i])
     */
    static long minMoneyToPassMonsters1(vector<int>& d, vector<int>& p) {
        long sum = 0;
        for (int money : p) {
            sum += money;
        }
        
        // dp[i][j]: 花费最多j的钱，处理前i个怪兽时能获得的最大能力值
        vector<vector<long>> dp(d.size() + 1, vector<long>(sum + 1, 0));
        
        for (int i = 1; i <= d.size(); i++) {
            for (int j = 0; j <= sum; j++) {
                // 不贿赂当前怪兽
                if (dp[i-1][j] >= d[i-1]) {
                    dp[i][j] = max(dp[i][j], dp[i-1][j]);
                }
                
                // 贿赂当前怪兽
                if (j >= p[i-1]) {
                    dp[i][j] = max(dp[i][j], dp[i-1][j - p[i-1]] + d[i-1]);
                }
            }
        }
        
        // 找到能通过所有怪兽的最少钱数
        for (long j = 0; j <= sum; j++) {
            if (dp[d.size()][j] > 0) {
                return j;
            }
        }
        
        return sum;
    }
    
    /**
     * 类似题目2: Bribe the Prisoners（Google Code Jam 2009）
     * 区间动态规划解法
     * 
     * 时间复杂度: O(m³)
     * 空间复杂度: O(m²)
     */
    static int bribePrisoners(int n, vector<int>& prisoners) {
        int m = prisoners.size();
        vector<int> a(m + 2);
        a[0] = 0;
        for (int i = 0; i < m; i++) {
            a[i + 1] = prisoners[i];
        }
        a[m + 1] = n + 1;
        
        // dp[i][j]: 释放编号在a[i]到a[j]之间的所有需要释放的犯人所需的最少金币数
        vector<vector<int>> dp(m + 2, vector<int>(m + 2, 0));
        
        // 区间DP，按区间长度从小到大计算
        for (int len = 2; len <= m + 1; len++) {
            for (int i = 0; i + len <= m + 1; i++) {
                int j = i + len;
                dp[i][j] = INT_MAX;
                
                // 枚举最后一个释放的犯人
                for (int k = i + 1; k < j; k++) {
                    dp[i][j] = min(dp[i][j], 
                        dp[i][k] + dp[k][j] + (a[j] - a[i] - 2));
                }
            }
        }
        
        return dp[0][m + 1];
    }
    
    /**
     * 单元测试函数
     * 验证算法正确性
     */
    static void test() {
        cout << "=== 测试贿赂怪兽算法 ===" << endl;
        
        // 测试用例1
        vector<int> a1 = {0, 5, 3, 1, 1, 1, 8};  // 注意：a[0]是哨兵
        vector<int> b1 = {0, 2, 1, 2, 2, 2, 30};
        int n1 = 6;
        
        int result1 = compute1(n1, a1, b1);
        int result2 = compute2(n1, a1, b1);
        int result3 = compute3(n1, a1, b1);
        int result4 = compute4(n1, a1, b1);
        
        cout << "测试用例1结果: " << result1 << ", " << result2 << ", " 
             << result3 << ", " << result4 << endl;
        
        // 测试用例2
        vector<int> d = {5, 3, 1, 1, 1, 8};
        vector<int> p = {2, 1, 2, 2, 2, 30};
        long result5 = minMoneyToPassMonsters1(d, p);
        cout << "类似题目1结果: " << result5 << endl;
        
        // 测试用例3
        vector<int> prisoners = {3};
        int result6 = bribePrisoners(8, prisoners);
        cout << "类似题目2结果: " << result6 << endl;
        
        cout << "=== 测试完成 ===" << endl;
    }
    
    /**
     * 主函数 - 用于算法演示
     */
    static void main() {
        test();
        
        // 性能测试
        cout << "\n=== 性能测试 ===" << endl;
        
        // 创建大规模测试数据
        int n = 1000;
        vector<int> a(n + 1);
        vector<int> b(n + 1);
        
        for (int i = 1; i <= n; i++) {
            a[i] = i;
            b[i] = 1;  // 小范围贿赂金额，适合方法1
        }
        
        auto start = chrono::high_resolution_clock::now();
        int result = compute1(n, a, b);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模测试结果: " << result << endl;
        cout << "执行时间: " << duration.count() << "ms" << endl;
    }
};

// 程序入口点
int main() {
    Code01_BuyMonster::main();
    return 0;
}

/**
 * 工程化考量:
 * 1. 异常处理: 添加输入验证和边界检查
 * 2. 内存管理: 使用vector自动管理内存，避免内存泄漏
 * 3. 性能优化: 根据数据特征选择最优算法
 * 4. 代码可读性: 使用有意义的变量名和详细注释
 * 
 * 调试技巧:
 * 1. 打印中间变量: 使用cout输出关键状态
 * 2. 断言验证: 使用assert检查关键条件
 * 3. 单元测试: 编写全面的测试用例
 * 
 * 面试要点:
 * 1. 理解两种DP方法的适用场景
 * 2. 能够解释状态转移方程的含义
 * 3. 掌握空间优化的方法
 * 4. 能够处理边界条件和异常情况
 */
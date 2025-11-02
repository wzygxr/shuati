#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// ACboy needs your help
// 题目描述：ACboy有N门课程，他有M天时间复习。每门课程复习不同的天数会有不同的收益。
// 求在M天时间内，如何安排复习计划使得总收益最大。
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1712

/*
 * 算法详解：
 * 这是一个典型的分组背包问题。每门课程可以看作一组物品，每组内的物品互斥（同一门课程只能选择一种复习天数）。
 * 每组物品的价值就是复习该课程不同天数对应的收益。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示前i门课程，使用j天时间复习的最大收益
 * 2. 状态转移：对于每门课程，枚举所有可能的复习天数k（1 <= k <= j）
 *    dp[i][j] = max(dp[i-1][j], dp[i-1][j-k] + value[i][k])
 * 3. 空间优化：使用滚动数组将二维优化到一维
 * 
 * 时间复杂度分析：
 * 设有N门课程，M天时间，每门课程最多有M种选择
 * 1. 动态规划计算：O(N * M * M)
 * 总时间复杂度：O(N * M^2)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(N * M)
 * 2. 空间优化后：O(M)
 * 
 * 相关题目扩展：
 * 1. HDU 1712 ACboy needs your help（本题）
 * 2. 洛谷 P1757 通天之分组背包
 * 3. LeetCode 1155. 掷骰子的N种方法
 * 4. 洛谷 P1064 金明的预算方案（依赖背包）
 * 5. 洛谷 P1941 飞扬的小鸟（多重背包+分组背包）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理非法输入、边界情况等
 * 3. 可配置性：可以将课程数和天数作为配置参数传入
 * 4. 单元测试：为solve方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用空间优化版本
 * 
 * 语言特性差异：
 * 1. C++：使用vector容器，自动管理内存
 * 2. 性能优势：编译型语言，运行效率高
 * 3. 内存控制：需要注意vector的扩容开销
 */

class Solution {
public:
    // 标准二维DP版本
    int solve(int N, int M, vector<vector<int>>& value) {
        // 初始化DP数组
        vector<vector<int>> dp(N + 1, vector<int>(M + 1, 0));
        
        for (int i = 1; i <= N; i++) {  // 遍历每门课程
            for (int j = 0; j <= M; j++) {  // 遍历可用天数
                // 初始值：不复习当前课程
                dp[i][j] = dp[i - 1][j];
                
                // 枚举复习当前课程的天数k
                for (int k = 1; k <= j; k++) {
                    if (value[i][k] > 0) {  // 只有收益为正时才考虑
                        dp[i][j] = max(dp[i][j], dp[i - 1][j - k] + value[i][k]);
                    }
                }
            }
        }
        
        return dp[N][M];
    }
    
    // 空间优化版本（推荐使用）
    int solveOptimized(int N, int M, vector<vector<int>>& value) {
        // 初始化DP数组
        vector<int> dp(M + 1, 0);
        
        for (int i = 1; i <= N; i++) {  // 遍历每门课程
            // 从后往前更新，避免重复使用
            for (int j = M; j >= 0; j--) {
                // 枚举复习当前课程的天数k
                for (int k = 1; k <= j; k++) {
                    if (value[i][k] > 0) {  // 只有收益为正时才考虑
                        dp[j] = max(dp[j], dp[j - k] + value[i][k]);
                    }
                }
            }
        }
        
        return dp[M];
    }
};

// 测试函数
void testACboy() {
    Solution solution;
    
    // 测试用例1：标准情况
    int N1 = 2, M1 = 2;
    vector<vector<int>> value1(3, vector<int>(3, 0));
    value1[1][1] = 1; value1[1][2] = 2;  // 课程1
    value1[2][1] = 1; value1[2][2] = 3;  // 课程2
    
    cout << "测试用例1:" << endl;
    cout << "标准版本: " << solution.solve(N1, M1, value1) << endl;
    cout << "优化版本: " << solution.solveOptimized(N1, M1, value1) << endl;
    cout << "预期结果: 3" << endl;
    cout << endl;
    
    // 测试用例2：边界情况
    int N2 = 0, M2 = 0;
    vector<vector<int>> value2(1, vector<int>(1, 0));
    
    cout << "测试用例2（边界情况）:" << endl;
    cout << "标准版本: " << solution.solve(N2, M2, value2) << endl;
    cout << "优化版本: " << solution.solveOptimized(N2, M2, value2) << endl;
    cout << "预期结果: 0" << endl;
    cout << endl;
    
    // 测试用例3：较大规模
    int N3 = 3, M3 = 3;
    vector<vector<int>> value3(4, vector<int>(4, 0));
    value3[1][1] = 2; value3[1][2] = 1; value3[1][3] = 3;  // 课程1
    value3[2][1] = 1; value3[2][2] = 2; value3[2][3] = 1;  // 课程2
    value3[3][1] = 3; value3[3][2] = 2; value3[3][3] = 1;  // 课程3
    
    cout << "测试用例3:" << endl;
    cout << "标准版本: " << solution.solve(N3, M3, value3) << endl;
    cout << "优化版本: " << solution.solveOptimized(N3, M3, value3) << endl;
    cout << "预期结果分析：应该选择课程1复习3天获得收益3，或者课程2复习2天+课程3复习1天获得收益2+3=5" << endl;
}

int main() {
    testACboy();
    return 0;
}

/*
 * =============================================================================================
 * 补充题目：洛谷 P1757 通天之分组背包（C++实现）
 * 题目链接：https://www.luogu.com.cn/problem/P1757
 * 
 * C++实现：
 * #include <iostream>
 * #include <vector>
 * #include <algorithm>
 * using namespace std;
 * 
 * struct Item {
 *     int weight;
 *     int value;
 *     int group;
 * };
 * 
 * int groupKnapsack(int m, vector<Item>& items) {
 *     // 按组号排序
 *     sort(items.begin(), items.end(), [](const Item& a, const Item& b) {
 *         return a.group < b.group;
 *     });
 *     
 *     vector<int> dp(m + 1, 0);
 *     int n = items.size();
 *     
 *     for (int i = 0; i < n; ) {
 *         int group = items[i].group;
 *         int j = i;
 *         while (j < n && items[j].group == group) j++;
 *         
 *         // 当前组包含物品[i, j-1]
 *         for (int k = m; k >= 0; k--) {
 *             for (int x = i; x < j; x++) {
 *                 if (k >= items[x].weight) {
 *                     dp[k] = max(dp[k], dp[k - items[x].weight] + items[x].value);
 *                 }
 *             }
 *         }
 *         
 *         i = j;
 *     }
 *     
 *     return dp[m];
 * }
 * 
 * 工程化考量：
 * 1. 使用结构体组织数据，提高代码可读性
 * 2. 使用lambda表达式简化排序逻辑
 * 3. 使用引用避免不必要的拷贝
 * 4. 添加输入验证和异常处理
 * 
 * 优化思路：
 * 1. 使用unordered_map预处理分组信息
 * 2. 使用位运算加速比较操作
 * 3. 使用多线程并行处理不同组
 */
#include <iostream>
#include <vector>
#include <algorithm>
#include <functional>

using namespace std;

/**
 * LeetCode 1751. 最多可以参加的会议数目 II (C++实现)
 * 
 * 题目描述：
 * 给定n个会议，每个会议有开始时间、结束时间、收益三个值
 * 参加会议就能得到收益，但是同一时间只能参加一个会议
 * 一共能参加k个会议，如果选择参加某个会议，那么必须完整的参加完这个会议
 * 会议结束日期是包含在会议内的，一个会议的结束时间等于另一个会议的开始时间，不能两个会议都参加
 * 返回能得到的会议价值最大和
 * 
 * 解题思路：
 * 这是一个带权重的区间调度问题，使用动态规划结合二分查找来解决
 * 
 * 算法步骤：
 * 1. 将所有会议按结束时间排序
 * 2. 使用动态规划，dp[i][j]表示在前i个会议中最多选择j个会议能获得的最大收益
 * 3. 对于每个会议，我们可以选择参加或不参加
 * 4. 如果参加，需要找到最后一个不冲突的会议，这可以通过二分查找实现
 * 5. 状态转移方程：
 *    dp[i][j] = max(dp[i-1][j], dp[prev][j-1] + events[i][2])
 *    其中 prev 是最后一个结束时间 < 当前会议开始时间的会议索引
 * 
 * 时间复杂度分析：
 * - 排序需要 O(n log n)
 * - 动态规划过程中，每个状态的计算需要 O(log n) 的时间进行二分查找
 * - 总时间复杂度：O(n * k + n * log n)
 * 空间复杂度：O(n * k) - 存储动态规划数组
 * 
 * 相关题目：
 * 1. LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
 * 2. LeetCode 435. 无重叠区间 (贪心)
 * 3. LeetCode 646. 最长数对链 (贪心)
 * 4. LeetCode 253. 会议室 II (扫描线算法)
 * 5. LintCode 1923. 最多可参加的会议数量 II
 * 6. HackerRank - Job Scheduling
 * 7. Codeforces 1324D. Pair of Topics
 * 8. AtCoder ABC091D. Two Faced Edges
 * 9. 洛谷 P2051 [AHOI2009]中国象棋
 * 10. 牛客网 NC46. 加起来和为目标值的组合
 * 11. 杭电OJ 3572. Task Schedule
 * 12. POJ 3616. Milking Time
 * 13. UVa 10158. War
 * 14. CodeChef - MAXSEGMENTS
 * 15. SPOJ - BUSYMAN
 * 16. Project Euler 318. Cutting Game
 * 17. HackerEarth - Job Scheduling Problem
 * 18. 计蒜客 - 工作安排
 * 19. ZOJ 3623. Battle Ships
 * 20. acwing 2068. 整数拼接
 * 
 * 工程化考量：
 * 1. 在实际应用中，带权重区间调度常用于：
 *    - 项目管理和资源分配
 *    - 云计算中的任务调度
 *    - 金融投资组合优化
 *    - 广告投放策略
 * 2. 实现优化：
 *    - 对于大规模数据，可以使用更高效的排序算法
 *    - 考虑使用二分索引树（Fenwick Tree）或线段树优化查询
 *    - 使用空间换时间，预处理可能的查询结果
 * 3. 可扩展性：
 *    - 支持动态添加和删除工作
 *    - 处理多个约束条件（如资源限制）
 *    - 扩展到多维问题
 * 4. 鲁棒性考虑：
 *    - 处理无效输入（负利润、无效时间区间）
 *    - 处理大规模数据时的内存管理
 *    - 优化极端情况下的性能
 * 5. 跨语言特性对比：
 *    - C++: 使用vector和algorithm库，性能更优
 *    - Java: 使用Arrays.sort和二维数组
 *    - Python: 使用列表和内置排序
 */

class Code01_MaximumNumberOfEvents {
public:
    /**
     * 计算最多可以参加的会议的最大收益
     * 
     * @param events 会议数组，每个会议包含[开始时间, 结束时间, 收益]
     * @param k 最多可以参加的会议数量
     * @return 最大收益
     */
    static int maxValue(vector<vector<int>>& events, int k) {
        int n = events.size();
        if (n == 0 || k == 0) return 0;
        
        // 按结束时间排序
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        // dp[i][j] : 0..i范围上最多选j个会议召开，最大收益是多少
        vector<vector<int>> dp(n, vector<int>(k + 1, 0));
        
        // 初始化：第一个会议单独参加的收益
        for (int j = 1; j <= k; j++) {
            dp[0][j] = events[0][2];
        }
        
        for (int i = 1; i < n; i++) {
            // 找到最后一个不冲突的会议
            int pre = findLastNonConflict(events, i - 1, events[i][0]);
            
            for (int j = 1; j <= k; j++) {
                // 状态转移：不参加当前会议 vs 参加当前会议
                int notAttend = dp[i - 1][j];
                int attend = (pre == -1 ? 0 : dp[pre][j - 1]) + events[i][2];
                dp[i][j] = max(notAttend, attend);
            }
        }
        
        return dp[n - 1][k];
    }

private:
    /**
     * 使用二分查找找到最后一个结束时间 < s 的会议
     * 
     * @param events 会议数组，已按结束时间排序
     * @param right 搜索范围的右边界
     * @param s 当前会议的开始时间
     * @return 最后一个不冲突会议的索引，如果不存在则返回-1
     */
    static int findLastNonConflict(const vector<vector<int>>& events, int right, int s) {
        int left = 0;
        int ans = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // 如果当前会议的结束时间 < s，可能是候选答案
            if (events[mid][1] < s) {
                ans = mid;
                left = mid + 1;
            } else {
                // 否则需要在左半部分查找
                right = mid - 1;
            }
        }
        
        return ans;
    }
};

/**
 * 测试函数 - 验证算法正确性
 */
void testMaxValue() {
    cout << "=== 测试Code01_MaximumNumberOfEvents ===" << endl;
    
    // 测试用例1：基本功能测试
    vector<vector<int>> events1 = {{1, 2, 4}, {3, 4, 3}, {2, 3, 1}};
    int k1 = 2;
    int result1 = Code01_MaximumNumberOfEvents::maxValue(events1, k1);
    cout << "测试用例1 - 预期: 7, 实际: " << result1 << endl;
    
    // 测试用例2：单个会议
    vector<vector<int>> events2 = {{1, 2, 1}};
    int k2 = 1;
    int result2 = Code01_MaximumNumberOfEvents::maxValue(events2, k2);
    cout << "测试用例2 - 预期: 1, 实际: " << result2 << endl;
    
    // 测试用例3：空输入
    vector<vector<int>> events3 = {};
    int k3 = 1;
    int result3 = Code01_MaximumNumberOfEvents::maxValue(events3, k3);
    cout << "测试用例3 - 预期: 0, 实际: " << result3 << endl;
    
    // 测试用例4：k=0
    vector<vector<int>> events4 = {{1, 2, 1}};
    int k4 = 0;
    int result4 = Code01_MaximumNumberOfEvents::maxValue(events4, k4);
    cout << "测试用例4 - 预期: 0, 实际: " << result4 << endl;
    
    // 测试用例5：复杂情况
    vector<vector<int>> events5 = {{1, 3, 5}, {2, 4, 6}, {3, 5, 7}, {4, 6, 8}};
    int k5 = 2;
    int result5 = Code01_MaximumNumberOfEvents::maxValue(events5, k5);
    cout << "测试用例5 - 预期: 13, 实际: " << result5 << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能分析函数
 */
void performanceAnalysis() {
    cout << "=== 性能分析 ===" << endl;
    
    // 生成大规模测试数据
    vector<vector<int>> largeEvents;
    int n = 1000;
    for (int i = 0; i < n; i++) {
        int start = i * 2;
        int end = start + 1;
        int value = i + 1;
        largeEvents.push_back({start, end, value});
    }
    
    int k = 10;
    
    // 记录开始时间
    auto start = chrono::high_resolution_clock::now();
    
    int result = Code01_MaximumNumberOfEvents::maxValue(largeEvents, k);
    
    // 记录结束时间
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "大规模测试(n=" << n << ", k=" << k << ") - 结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "时间复杂度: O(n * k + n * log n)" << endl;
    cout << "空间复杂度: O(n * k)" << endl;
}

/**
 * 主函数 - 程序入口
 */
int main() {
    cout << "=== Code01_MaximumNumberOfEvents C++实现 ===" << endl;
    
    // 运行测试
    testMaxValue();
    
    // 性能分析
    performanceAnalysis();
    
    return 0;
}
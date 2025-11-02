#include <iostream>
#include <vector>
#include <algorithm>
#include <functional>
#include <limits>

using namespace std;

/**
 * LeetCode 1751. 最多可以参加的会议数目 II - C++实现
 * 
 * 题目描述：
 * 给你一个 events 数组，其中 events[i] = [startDayi, endDayi, valuei] ，表示第 i 个会议在 startDayi 天开始，
 * 第 endDayi 天结束，如果你参加这个会议，你能得到价值 valuei 。同时给你一个整数 k 表示你能参加的最多会议数目。
 * 你同一时间只能参加一个会议。如果你选择参加某个会议，那么你必须完整地参加完这个会议。
 * 会议结束日期是包含在会议内的，也就是说你不能同时参加一个开始日期与另一个结束日期相同的两个会议。
 * 请你返回能得到的会议价值最大和。
 * 
 * 示例：
 * 输入：events = [[1,2,4],[3,4,3],[2,3,1]], k = 2
 * 输出：7
 * 解释：选择绿色的活动会议 0 和 1，得到总价值和为 4 + 3 = 7 。
 * 
 * 解题思路：
 * 这是一个典型的动态规划问题，类似于背包问题的变种。我们需要在有限的会议数量k下，选择价值最大的会议组合。
 * 
 * 算法步骤：
 * 1. 按照会议结束时间对所有会议进行排序
 * 2. 使用动态规划，dp[i][j] 表示从前 i 个会议中最多参加 j 个会议所能获得的最大价值
 * 3. 对于每个会议，我们可以选择参加或不参加
 * 4. 如果参加，我们需要找到最后一个与其不冲突的会议，这可以通过二分查找实现
 * 5. 状态转移方程：
 *    dp[i][j] = max(dp[i-1][j], dp[pre][j-1] + events[i][2])
 *    其中 pre 是最后一个与会议 i 不冲突的会议索引
 * 
 * 时间复杂度：O(n * k + n * log n)
 * 空间复杂度：O(n * k)
 * 
 * 工程化考量：
 * - 使用vector存储数据，避免内存泄漏
 * - 添加边界条件检查
 * - 使用二分查找优化搜索效率
 * - 提供完整的测试用例
 */

class MaximumEventsII {
public:
    /**
     * 计算最多能参加k个会议获得的最大价值
     * 
     * @param events 会议数组，每个元素为 {开始时间, 结束时间, 价值}
     * @param k 最多能参加的会议数量
     * @return 能获得的最大价值
     */
    static int maxValue(vector<vector<int>>& events, int k) {
        // 边界条件检查
        if (events.empty() || k <= 0) {
            return 0;
        }
        
        int n = events.size();
        
        // 按照结束时间对会议进行排序
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        // 初始化动态规划数组
        // dp[i][j] 表示从前i个会议中最多参加j个会议的最大价值
        vector<vector<int>> dp(n + 1, vector<int>(k + 1, 0));
        
        // 构建动态规划表
        for (int i = 1; i <= n; i++) {
            // 当前会议的信息（转换为0-indexed）
            int start = events[i-1][0];
            int end = events[i-1][1];
            int value = events[i-1][2];
            
            // 使用二分查找找到最后一个与当前会议不冲突的会议
            int left = 0, right = i - 2; // i-2是因为i是1-indexed，且要找前一个
            int pre = 0;
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (events[mid][1] < start) {
                    pre = mid + 1; // 转换为1-indexed
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            // 动态规划状态转移
            for (int j = 1; j <= k; j++) {
                // 不选择当前会议
                dp[i][j] = dp[i-1][j];
                
                // 选择当前会议
                if (pre > 0) {
                    dp[i][j] = max(dp[i][j], dp[pre][j-1] + value);
                } else {
                    // 如果没有前驱会议，只能选择当前会议
                    dp[i][j] = max(dp[i][j], value);
                }
            }
        }
        
        return dp[n][k];
    }
    
    /**
     * 优化版本：使用空间优化的动态规划
     * 空间复杂度从O(n*k)优化到O(k)
     */
    static int maxValueOptimized(vector<vector<int>>& events, int k) {
        if (events.empty() || k <= 0) {
            return 0;
        }
        
        int n = events.size();
        
        // 按照结束时间排序
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        // 只保留当前行和前一行的dp值
        vector<int> prev(k + 1, 0);
        vector<int> curr(k + 1, 0);
        
        for (int i = 0; i < n; i++) {
            int start = events[i][0];
            int value = events[i][2];
            
            // 二分查找最后一个不冲突的会议
            int left = 0, right = i - 1;
            int preIndex = -1;
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (events[mid][1] < start) {
                    preIndex = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            // 更新当前行的dp值
            for (int j = 1; j <= k; j++) {
                // 不选当前会议
                curr[j] = prev[j];
                
                // 选当前会议
                if (preIndex != -1) {
                    curr[j] = max(curr[j], prev[j-1] + value);
                } else {
                    curr[j] = max(curr[j], value);
                }
            }
            
            // 更新prev为当前值
            prev = curr;
        }
        
        return curr[k];
    }
};

/**
 * 测试函数
 */
void testMaximumEventsII() {
    cout << "=== LeetCode 1751 最多可以参加的会议数目 II 测试 ===" << endl;
    
    // 测试用例1：基础测试
    vector<vector<int>> events1 = {{1,2,4}, {3,4,3}, {2,3,1}};
    int k1 = 2;
    int result1 = MaximumEventsII::maxValue(events1, k1);
    cout << "测试用例1 - 预期: 7, 实际: " << result1 << endl;
    
    // 测试用例2：边界测试 - 只有一个会议
    vector<vector<int>> events2 = {{1,3,5}};
    int k2 = 1;
    int result2 = MaximumEventsII::maxValue(events2, k2);
    cout << "测试用例2 - 预期: 5, 实际: " << result2 << endl;
    
    // 测试用例3：k=0
    vector<vector<int>> events3 = {{1,2,10}};
    int k3 = 0;
    int result3 = MaximumEventsII::maxValue(events3, k3);
    cout << "测试用例3 - 预期: 0, 实际: " << result3 << endl;
    
    // 测试用例4：多个会议，k较小
    vector<vector<int>> events4 = {{1,2,1}, {2,3,2}, {3,4,3}, {1,3,4}};
    int k4 = 2;
    int result4 = MaximumEventsII::maxValue(events4, k4);
    cout << "测试用例4 - 实际结果: " << result4 << endl;
    
    // 测试优化版本
    int result1_opt = MaximumEventsII::maxValueOptimized(events1, k1);
    cout << "优化版本测试用例1 - 预期: 7, 实际: " << result1_opt << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成大规模测试数据
    vector<vector<int>> largeEvents;
    int n = 1000;
    for (int i = 0; i < n; i++) {
        int start = i + 1;
        int end = start + (i % 3) + 1;
        int value = (i * 7) % 100 + 1;
        largeEvents.push_back({start, end, value});
    }
    
    int k = 10;
    
    // 测试标准版本
    auto start = chrono::high_resolution_clock::now();
    int result1 = MaximumEventsII::maxValue(largeEvents, k);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    // 测试优化版本
    start = chrono::high_resolution_clock::now();
    int result2 = MaximumEventsII::maxValueOptimized(largeEvents, k);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "大规模测试 - 标准版本结果: " << result1 << ", 耗时: " << duration1.count() << "微秒" << endl;
    cout << "大规模测试 - 优化版本结果: " << result2 << ", 耗时: " << duration2.count() << "微秒" << endl;
    
    cout << "=== 性能测试完成 ===" << endl;
}

/**
 * 主函数
 */
int main() {
    testMaximumEventsII();
    performanceTest();
    return 0;
}

/**
 * 复杂度分析：
 * 时间复杂度：
 * - 排序：O(n log n)
 * - 动态规划：O(n * k)
 * - 二分查找：O(log n) 每次查找
 * - 总复杂度：O(n * k + n * log n)
 * 
 * 空间复杂度：
 * - 标准版本：O(n * k) - 存储dp表
 * - 优化版本：O(k) - 只存储两行dp值
 * 
 * 算法优化点：
 * 1. 使用排序确保会议按结束时间有序
 * 2. 使用二分查找快速定位不冲突的会议
 * 3. 空间优化版本大幅减少内存使用
 * 
 * 工程化改进：
 * 1. 添加完整的边界条件检查
 * 2. 提供两种实现版本（标准和优化）
 * 3. 包含性能测试和功能测试
 * 4. 详细的注释和文档
 * 
 * 相关题目对比：
 * - LeetCode 1353: 贪心解法，只关心数量不关心价值
 * - LeetCode 435: 区间调度问题，贪心选择
 * - LeetCode 646: 最长数对链，类似区间选择
 */
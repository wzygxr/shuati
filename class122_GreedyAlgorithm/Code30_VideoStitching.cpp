#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <random>
#include <chrono>
#include <string>
#include <stdexcept>

using namespace std;

/**
 * 视频拼接
 * 
 * 题目描述：
 * 你将会获得一系列视频片段，这些片段来自于一项持续时长为 time 秒的体育赛事。
 * 这些片段可能有所重叠，也可能长度不一。使用数组 clips 描述所有的视频片段，
 * 其中 clips[i] = [starti, endi] 表示：某个视频片段开始于 starti 并于 endi 结束。
 * 甚至可以对这些片段自由地再剪辑。例如，片段 [0, 7] 可以剪切成 [0, 1] + [1, 3] + [3, 7] 三部分。
 * 我们需要将这些片段进行再剪辑，并将剪辑后的内容拼接成覆盖整个运动过程的片段（[0, time]）。
 * 返回所需片段的最小数目，如果无法完成该任务，则返回 -1。
 * 
 * 来源：LeetCode 1024
 * 链接：https://leetcode.cn/problems/video-stitching/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 将片段按开始时间排序，如果开始时间相同则按结束时间降序排序
 * 2. 维护当前覆盖的最远位置 curEnd 和下一个要覆盖的位置 nextEnd
 * 3. 遍历排序后的片段：
 *    - 如果片段的开始时间大于当前覆盖的最远位置，说明有间隔，无法拼接
 *    - 如果片段的开始时间小于等于当前覆盖的最远位置，更新下一个要覆盖的位置
 *    - 当遍历到当前覆盖范围的边界时，增加片段计数并更新当前覆盖范围
 * 
 * 时间复杂度：O(n * logn) - 排序的时间复杂度
 * 空间复杂度：O(1) - 只使用常数空间
 * 
 * 关键点分析：
 * - 贪心策略：每次选择能覆盖当前范围且延伸最远的片段
 * - 排序策略：按开始时间排序，开始时间相同时按结束时间降序
 * - 边界处理：检查是否能覆盖整个区间 [0, time]
 * 
 * 工程化考量：
 * - 输入验证：检查clips数组和time参数的有效性
 * - 边界处理：处理time=0的情况
 * - 性能优化：避免不必要的排序操作
 */
class Code30_VideoStitching {
public:
    /**
     * 视频拼接的最小片段数
     * 
     * @param clips 视频片段数组
     * @param time 目标时长
     * @return 最小片段数，如果无法拼接返回-1
     */
    static int videoStitching(vector<vector<int>>& clips, int time) {
        // 输入验证
        if (clips.empty()) {
            return time == 0 ? 0 : -1;
        }
        if (time < 0) {
            throw invalid_argument("时间不能为负数");
        }
        if (time == 0) {
            return 0;
        }
        
        // 按开始时间排序，开始时间相同时按结束时间降序
        sort(clips.begin(), clips.end(), [](const vector<int>& a, const vector<int>& b) {
            if (a[0] != b[0]) {
                return a[0] < b[0];
            } else {
                return a[1] > b[1];
            }
        });
        
        int count = 0; // 片段计数
        int curEnd = 0; // 当前覆盖的最远位置
        int nextEnd = 0; // 下一个要覆盖的位置
        int i = 0; // 当前处理的片段索引
        int n = clips.size();
        
        while (i < n && curEnd < time) {
            // 找到所有开始时间小于等于curEnd的片段中，结束时间最远的
            while (i < n && clips[i][0] <= curEnd) {
                nextEnd = max(nextEnd, clips[i][1]);
                i++;
            }
            
            // 如果没有找到可以扩展的片段，说明无法拼接
            if (curEnd == nextEnd) {
                return -1;
            }
            
            // 选择当前片段，更新当前覆盖范围
            count++;
            curEnd = nextEnd;
            
            // 如果已经覆盖了目标范围，提前结束
            if (curEnd >= time) {
                break;
            }
        }
        
        // 检查是否覆盖了整个区间 [0, time]
        return curEnd >= time ? count : -1;
    }
    
    /**
     * 另一种实现：使用动态规划思想
     * 时间复杂度：O(n * time)
     * 空间复杂度：O(time)
     */
    static int videoStitchingDP(vector<vector<int>>& clips, int time) {
        if (clips.empty()) {
            return time == 0 ? 0 : -1;
        }
        if (time < 0) {
            throw invalid_argument("时间不能为负数");
        }
        if (time == 0) {
            return 0;
        }
        
        // dp[i] 表示覆盖区间 [0, i] 所需的最小片段数
        vector<int> dp(time + 1, INT_MAX - 1);
        dp[0] = 0;
        
        for (int i = 1; i <= time; i++) {
            for (const auto& clip : clips) {
                int start = clip[0];
                int end = clip[1];
                
                // 如果当前片段可以覆盖到i
                if (start < i && i <= end) {
                    dp[i] = min(dp[i], dp[start] + 1);
                }
            }
        }
        
        return dp[time] == INT_MAX - 1 ? -1 : dp[time];
    }
    
    /**
     * 使用区间合并的思路
     * 时间复杂度：O(n * logn)
     * 空间复杂度：O(1)
     */
    static int videoStitchingMerge(vector<vector<int>>& clips, int time) {
        if (clips.empty()) {
            return time == 0 ? 0 : -1;
        }
        if (time < 0) {
            throw invalid_argument("时间不能为负数");
        }
        if (time == 0) {
            return 0;
        }
        
        // 按开始时间排序
        sort(clips.begin(), clips.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[0] < b[0];
        });
        
        int count = 0;
        int currentEnd = 0;
        int nextEnd = 0;
        int index = 0;
        int n = clips.size();
        
        while (currentEnd < time) {
            count++;
            
            // 找到所有开始时间小于等于currentEnd的片段中，结束时间最大的
            while (index < n && clips[index][0] <= currentEnd) {
                nextEnd = max(nextEnd, clips[index][1]);
                index++;
            }
            
            // 如果没有进展，说明无法拼接
            if (nextEnd == currentEnd) {
                return -1;
            }
            
            currentEnd = nextEnd;
            
            // 如果已经覆盖了目标范围，提前结束
            if (currentEnd >= time) {
                break;
            }
            
            // 如果已经处理完所有片段但还没有覆盖完，返回-1
            if (index >= n) {
                return -1;
            }
        }
        
        return count;
    }
    
    /**
     * 运行测试用例
     */
    static void runTests() {
        cout << "=== 视频拼接测试 ===" << endl;
        
        // 测试用例1: clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
        vector<vector<int>> clips1 = {{0,2},{4,6},{8,10},{1,9},{1,5},{5,9}};
        int time1 = 10;
        cout << "测试用例1:" << endl;
        cout << "Clips: ";
        for (const auto& clip : clips1) {
            cout << "[" << clip[0] << "," << clip[1] << "] ";
        }
        cout << endl;
        cout << "Time: " << time1 << endl;
        cout << "贪心结果: " << videoStitching(clips1, time1) << endl; // 期望: 3
        cout << "DP结果: " << videoStitchingDP(clips1, time1) << endl; // 期望: 3
        cout << "合并结果: " << videoStitchingMerge(clips1, time1) << endl; // 期望: 3
        
        // 测试用例2: clips = [[0,1],[1,2]], time = 5
        vector<vector<int>> clips2 = {{0,1},{1,2}};
        int time2 = 5;
        cout << "\n测试用例2:" << endl;
        cout << "Clips: ";
        for (const auto& clip : clips2) {
            cout << "[" << clip[0] << "," << clip[1] << "] ";
        }
        cout << endl;
        cout << "Time: " << time2 << endl;
        cout << "贪心结果: " << videoStitching(clips2, time2) << endl; // 期望: -1
        cout << "DP结果: " << videoStitchingDP(clips2, time2) << endl; // 期望: -1
        cout << "合并结果: " << videoStitchingMerge(clips2, time2) << endl; // 期望: -1
        
        // 测试用例3: clips = [[0,4],[2,8]], time = 5
        vector<vector<int>> clips3 = {{0,4},{2,8}};
        int time3 = 5;
        cout << "\n测试用例3:" << endl;
        cout << "Clips: ";
        for (const auto& clip : clips3) {
            cout << "[" << clip[0] << "," << clip[1] << "] ";
        }
        cout << endl;
        cout << "Time: " << time3 << endl;
        cout << "贪心结果: " << videoStitching(clips3, time3) << endl; // 期望: 2
        cout << "DP结果: " << videoStitchingDP(clips3, time3) << endl; // 期望: 2
        cout << "合并结果: " << videoStitchingMerge(clips3, time3) << endl; // 期望: 2
        
        // 测试用例4: 空数组, time = 0
        vector<vector<int>> clips4 = {};
        int time4 = 0;
        cout << "\n测试用例4:" << endl;
        cout << "Clips: 空数组" << endl;
        cout << "Time: " << time4 << endl;
        cout << "贪心结果: " << videoStitching(clips4, time4) << endl; // 期望: 0
        cout << "DP结果: " << videoStitchingDP(clips4, time4) << endl; // 期望: 0
        cout << "合并结果: " << videoStitchingMerge(clips4, time4) << endl; // 期望: 0
        
        // 测试用例5: 单个片段覆盖整个区间
        vector<vector<int>> clips5 = {{0,10}};
        int time5 = 10;
        cout << "\n测试用例5:" << endl;
        cout << "Clips: ";
        for (const auto& clip : clips5) {
            cout << "[" << clip[0] << "," << clip[1] << "] ";
        }
        cout << endl;
        cout << "Time: " << time5 << endl;
        cout << "贪心结果: " << videoStitching(clips5, time5) << endl; // 期望: 1
        cout << "DP结果: " << videoStitchingDP(clips5, time5) << endl; // 期望: 1
        cout << "合并结果: " << videoStitchingMerge(clips5, time5) << endl; // 期望: 1
    }
    
    /**
     * 性能测试方法
     */
    static void performanceTest() {
        // 生成大规模测试数据
        vector<vector<int>> largeClips(10000, vector<int>(2));
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, 1000);
        
        for (int i = 0; i < largeClips.size(); i++) {
            int start = dis(gen);
            int end = start + dis(gen) % 100 + 1;
            largeClips[i] = {start, end};
        }
        int time = 1000;
        
        cout << "\n=== 性能测试 ===" << endl;
        
        auto startTime1 = chrono::high_resolution_clock::now();
        int result1 = videoStitching(largeClips, time);
        auto endTime1 = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::milliseconds>(endTime1 - startTime1);
        cout << "贪心算法执行时间: " << duration1.count() << "ms" << endl;
        cout << "结果: " << result1 << endl;
        
        auto startTime2 = chrono::high_resolution_clock::now();
        int result2 = videoStitchingDP(largeClips, time);
        auto endTime2 = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::milliseconds>(endTime2 - startTime2);
        cout << "动态规划执行时间: " << duration2.count() << "ms" << endl;
        cout << "结果: " << result2 << endl;
        
        auto startTime3 = chrono::high_resolution_clock::now();
        int result3 = videoStitchingMerge(largeClips, time);
        auto endTime3 = chrono::high_resolution_clock::now();
        auto duration3 = chrono::duration_cast<chrono::milliseconds>(endTime3 - startTime3);
        cout << "合并算法执行时间: " << duration3.count() << "ms" << endl;
        cout << "结果: " << result3 << endl;
    }
    
    /**
     * 算法复杂度分析
     */
    static void analyzeComplexity() {
        cout << "\n=== 算法复杂度分析 ===" << endl;
        cout << "贪心算法:" << endl;
        cout << "- 时间复杂度: O(n * logn)" << endl;
        cout << "  - 排序: O(n * logn)" << endl;
        cout << "  - 遍历: O(n)" << endl;
        cout << "- 空间复杂度: O(1)" << endl;
        
        cout << "\n动态规划:" << endl;
        cout << "- 时间复杂度: O(n * time)" << endl;
        cout << "  - 外层循环: O(time)" << endl;
        cout << "  - 内层循环: O(n)" << endl;
        cout << "- 空间复杂度: O(time)" << endl;
        
        cout << "\n合并算法:" << endl;
        cout << "- 时间复杂度: O(n * logn)" << endl;
        cout << "  - 排序: O(n * logn)" << endl;
        cout << "  - 遍历: O(n)" << endl;
        cout << "- 空间复杂度: O(1)" << endl;
        
        cout << "\n贪心策略证明:" << endl;
        cout << "1. 按开始时间排序可以确保覆盖连续性" << endl;
        cout << "2. 选择结束时间最远的片段是最优选择" << endl;
        cout << "3. 数学归纳法证明贪心选择性质" << endl;
    }
};

int main() {
    Code30_VideoStitching::runTests();
    Code30_VideoStitching::performanceTest();
    Code30_VideoStitching::analyzeComplexity();
    
    return 0;
}
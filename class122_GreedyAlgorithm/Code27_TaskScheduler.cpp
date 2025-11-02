#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <unordered_map>
#include <chrono>

using namespace std;

/**
 * 任务调度器
 * 
 * 题目描述：
 * 给定一个用字符数组表示的 CPU 需要执行的任务列表。其中包含使用大写的 A-Z 字母表示的26 种不同种类的任务。
 * 任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。
 * 在任何一个单位时间， CPU 可以完成一个任务，或者处于待命状态。
 * 然而，两个相同种类的任务之间必须有长度为 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
 * 你需要计算完成所有任务所需要的最短时间。
 * 
 * 来源：LeetCode 621
 * 链接：https://leetcode.cn/problems/task-scheduler/
 * 
 * 相关题目链接：
 * https://leetcode.cn/problems/task-scheduler/ (LeetCode 621)
 * https://www.lintcode.com/problem/task-scheduler/ (LintCode 1482)
 * https://practice.geeksforgeeks.org/problems/task-scheduler/ (GeeksforGeeks)
 * https://www.nowcoder.com/practice/6b48f8c9d2cb4a568890b73383e119cf (牛客网)
 * https://codeforces.com/problemset/problem/1165/F2 (Codeforces)
 * https://atcoder.jp/contests/abc153/tasks/abc153_e (AtCoder)
 * https://www.hackerrank.com/challenges/task-scheduler/problem (HackerRank)
 * https://www.luogu.com.cn/problem/P1043 (洛谷)
 * https://vjudge.net/problem/HDU-2023 (HDU)
 * https://www.spoj.com/problems/TASKSCHED/ (SPOJ)
 * https://www.codechef.com/problems/TASKSCHEDULE (CodeChef)
 * 
 * 算法思路：
 * 使用贪心算法 + 桶思想：
 * 1. 统计每个任务的出现频率
 * 2. 找到出现次数最多的任务，假设出现次数为 maxCount
 * 3. 计算至少需要的时间：maxCount + (maxCount - 1) * n
 * 4. 如果存在多个任务出现次数都为 maxCount，需要额外加上这些任务
 * 5. 最终结果为 max(总任务数, 计算出的最小时间)
 * 
 * 时间复杂度：O(n) - 需要遍历任务数组统计频率
 * 空间复杂度：O(1) - 使用固定大小的数组存储频率（26个字母）
 * 
 * 关键点分析：
 * - 桶思想：将任务分配到桶中，每个桶的大小为 n+1
 * - 贪心策略：优先安排出现次数最多的任务
 * - 边界处理：n=0 的特殊情况
 * 
 * 工程化考量：
 * - 输入验证：检查任务数组是否为空
 * - 边界处理：处理 n=0 的情况
 * - 性能优化：使用数组而非unordered_map统计频率
 * - 内存管理：避免不必要的内存分配
 */
class Code27_TaskScheduler {
public:
    /**
     * 计算完成任务的最短时间（桶思想解法）
     * 
     * @param tasks 任务数组
     * @param n 冷却时间
     * @return 最短完成时间
     */
    static int leastInterval(vector<char>& tasks, int n) {
        // 输入验证
        if (tasks.empty()) {
            return 0;
        }
        
        // 特殊情况：如果冷却时间为0，可以直接执行所有任务
        if (n == 0) {
            return tasks.size();
        }
        
        // 统计每个任务的频率（使用数组而非map提高性能）
        vector<int> freq(26, 0);
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        // 找到最大频率
        int maxFreq = 0;
        for (int count : freq) {
            maxFreq = max(maxFreq, count);
        }
        
        // 统计有多少个任务具有最大频率
        int maxCount = 0;
        for (int count : freq) {
            if (count == maxFreq) {
                maxCount++;
            }
        }
        
        // 计算最小时间
        // 公式：maxCount + (maxFreq - 1) * (n + 1)
        int minTime = (maxFreq - 1) * (n + 1) + maxCount;
        
        // 如果任务数量大于计算出的最小时间，说明需要更多时间
        return max(minTime, (int)tasks.size());
    }
    
    /**
     * 使用优先队列的解法（另一种思路）
     * 时间复杂度：O(n * log26) ≈ O(n)
     * 空间复杂度：O(26) ≈ O(1)
     */
    static int leastIntervalWithPQ(vector<char>& tasks, int n) {
        if (tasks.empty()) {
            return 0;
        }
        if (n == 0) {
            return tasks.size();
        }
        
        // 统计频率
        vector<int> freq(26, 0);
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        // 使用最大堆存储频率
        priority_queue<int> maxHeap;
        for (int count : freq) {
            if (count > 0) {
                maxHeap.push(count);
            }
        }
        
        int time = 0;
        // 用于存储当前周期内执行的任务和冷却结束时间
        queue<pair<int, int>> coolingQueue;
        
        while (!maxHeap.empty() || !coolingQueue.empty()) {
            time++;
            
            // 从最大堆中取出一个任务执行
            if (!maxHeap.empty()) {
                int count = maxHeap.top();
                maxHeap.pop();
                count--;
                if (count > 0) {
                    // 将任务加入冷却队列，记录冷却结束时间
                    coolingQueue.push({count, time + n});
                }
            }
            
            // 检查冷却队列中是否有任务可以重新加入最大堆
            while (!coolingQueue.empty() && coolingQueue.front().second <= time) {
                maxHeap.push(coolingQueue.front().first);
                coolingQueue.pop();
            }
        }
        
        return time;
    }
    
    /**
     * 运行测试用例
     */
    static void runTests() {
        // 测试用例1: tasks = ["A","A","A","B","B","B"], n = 2
        // 期望输出: 8
        vector<char> tasks1 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n1 = 2;
        cout << "测试用例1:" << endl;
        cout << "任务: ";
        for (char task : tasks1) cout << task << " ";
        cout << ", n = " << n1 << endl;
        cout << "结果1: " << leastInterval(tasks1, n1) << endl; // 期望: 8
        cout << "结果2: " << leastIntervalWithPQ(tasks1, n1) << endl; // 期望: 8
        
        // 测试用例2: tasks = ["A","A","A","B","B","B"], n = 0
        // 期望输出: 6
        vector<char> tasks2 = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n2 = 0;
        cout << "\n测试用例2:" << endl;
        cout << "任务: ";
        for (char task : tasks2) cout << task << " ";
        cout << ", n = " << n2 << endl;
        cout << "结果: " << leastInterval(tasks2, n2) << endl; // 期望: 6
        
        // 测试用例3: tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
        // 期望输出: 16
        vector<char> tasks3 = {'A','A','A','A','A','A','B','C','D','E','F','G'};
        int n3 = 2;
        cout << "\n测试用例3:" << endl;
        cout << "任务: ";
        for (char task : tasks3) cout << task << " ";
        cout << ", n = " << n3 << endl;
        cout << "结果: " << leastInterval(tasks3, n3) << endl; // 期望: 16
        
        // 测试用例4: 空数组
        vector<char> tasks4 = {};
        int n4 = 2;
        cout << "\n测试用例4:" << endl;
        cout << "任务: 空数组, n = " << n4 << endl;
        cout << "结果: " << leastInterval(tasks4, n4) << endl; // 期望: 0
        
        // 边界测试：单个任务
        vector<char> tasks5 = {'A'};
        int n5 = 3;
        cout << "\n测试用例5:" << endl;
        cout << "任务: A, n = " << n5 << endl;
        cout << "结果: " << leastInterval(tasks5, n5) << endl; // 期望: 1
    }
    
    /**
     * 性能测试方法
     */
    static void performanceTest() {
        // 生成大规模测试数据
        vector<char> largeTasks(10000);
        for (int i = 0; i < largeTasks.size(); i++) {
            largeTasks[i] = 'A' + rand() % 26;
        }
        int n = 10;
        
        auto startTime = chrono::high_resolution_clock::now();
        int result = leastInterval(largeTasks, n);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
        
        cout << "大规模测试结果: " << result << endl;
        cout << "执行时间: " << duration.count() << " 微秒" << endl;
    }
};

int main() {
    cout << "=== 任务调度器测试 ===" << endl;
    Code27_TaskScheduler::runTests();
    
    cout << "\n=== 性能测试 ===" << endl;
    Code27_TaskScheduler::performanceTest();
    
    return 0;
}
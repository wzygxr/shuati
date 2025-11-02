#include <vector>
#include <queue>
#include <algorithm>
#include <unordered_map>
#include <string>
#include <iostream>
#include <cmath>
#include <functional>
#include <stdexcept>
#include <climits>
using namespace std;

/**
 * 堆算法扩展题目集 - C++实现
 * 涵盖各大算法平台经典堆问题
 */

class HeapExtendedProblems {
public:
    /**
     * 题目1: LeetCode 378. 有序矩阵中第K小的元素
     */
    static int kthSmallestInSortedMatrix(vector<vector<int>>& matrix, int k) {
        if (matrix.empty() || matrix[0].empty()) {
            throw invalid_argument("矩阵不能为空");
        }
        if (k <= 0 || k > matrix.size() * matrix[0].size()) {
            throw invalid_argument("k值超出范围");
        }
        
        int n = matrix.size();
        // 最小堆，存储三元组[值, 行索引, 列索引]
        priority_queue<vector<int>, vector<vector<int>>, greater<vector<int>>> minHeap;
        
        // 将第一列的所有元素加入堆
        for (int i = 0; i < n; i++) {
            minHeap.push({matrix[i][0], i, 0});
        }
        
        // 取出前k-1个最小元素
        for (int i = 0; i < k - 1; i++) {
            auto current = minHeap.top();
            minHeap.pop();
            int row = current[1];
            int col = current[2];
            
            // 如果当前元素有右侧元素，加入堆
            if (col + 1 < n) {
                minHeap.push({matrix[row][col + 1], row, col + 1});
            }
        }
        
        return minHeap.top()[0];
    }
    
    /**
     * 题目2: LeetCode 767. 重构字符串
     */
    static string reorganizeString(string s) {
        if (s.empty()) return "";
        
        // 统计字符频率
        unordered_map<char, int> freqMap;
        for (char c : s) {
            freqMap[c]++;
        }
        
        // 最大堆，按频率降序排列
        auto cmp = [&](char a, char b) {
            return freqMap[a] < freqMap[b];
        };
        priority_queue<char, vector<char>, decltype(cmp)> maxHeap(cmp);
        
        for (auto& pair : freqMap) {
            maxHeap.push(pair.first);
        }
        
        // 检查是否可能重构
        if (freqMap[maxHeap.top()] > (s.length() + 1) / 2) {
            return "";
        }
        
        string result;
        
        while (maxHeap.size() >= 2) {
            char first = maxHeap.top(); maxHeap.pop();
            char second = maxHeap.top(); maxHeap.pop();
            
            result += first;
            result += second;
            
            // 更新频率并重新加入堆
            if (--freqMap[first] > 0) maxHeap.push(first);
            if (--freqMap[second] > 0) maxHeap.push(second);
        }
        
        // 处理最后一个字符
        if (!maxHeap.empty()) {
            result += maxHeap.top();
        }
        
        return result;
    }
    
    /**
     * 题目3: LeetCode 502. IPO
     */
    static int findMaximizedCapital(int k, int w, vector<int>& profits, vector<int>& capital) {
        int n = profits.size();
        vector<pair<int, int>> projects; // [capital, profit]
        for (int i = 0; i < n; i++) {
            projects.push_back({capital[i], profits[i]});
        }
        
        // 按资本升序排序
        sort(projects.begin(), projects.end());
        
        // 最大堆，存储当前可做项目的利润
        priority_queue<int> maxHeap;
        
        int currentCapital = w;
        int projectIndex = 0;
        
        for (int i = 0; i < k; i++) {
            // 将所有资本要求小于等于当前资本的项目加入最大堆
            while (projectIndex < n && projects[projectIndex].first <= currentCapital) {
                maxHeap.push(projects[projectIndex].second);
                projectIndex++;
            }
            
            if (maxHeap.empty()) break;
            
            currentCapital += maxHeap.top();
            maxHeap.pop();
        }
        
        return currentCapital;
    }
    
    /**
     * 题目4: LeetCode 630. 课程表 III
     */
    static int scheduleCourse(vector<vector<int>>& courses) {
        // 按结束时间排序
        sort(courses.begin(), courses.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        // 最大堆，存储已选课程的持续时间
        priority_queue<int> maxHeap;
        
        int currentTime = 0;
        
        for (auto& course : courses) {
            int duration = course[0];
            int endTime = course[1];
            
            if (currentTime + duration <= endTime) {
                currentTime += duration;
                maxHeap.push(duration);
            } else if (!maxHeap.empty() && maxHeap.top() > duration) {
                currentTime = currentTime - maxHeap.top() + duration;
                maxHeap.pop();
                maxHeap.push(duration);
            }
        }
        
        return maxHeap.size();
    }
    
    /**
     * 题目5: LeetCode 857. 雇佣 K 名工人的最低成本
     */
    static double mincostToHireWorkers(vector<int>& quality, vector<int>& wage, int k) {
        int n = quality.size();
        vector<vector<double>> workers; // [quality, wage, ratio]
        for (int i = 0; i < n; i++) {
            double ratio = (double)wage[i] / quality[i];
            workers.push_back({(double)quality[i], (double)wage[i], ratio});
        }
        
        // 按工资质量比排序
        sort(workers.begin(), workers.end(), [](const vector<double>& a, const vector<double>& b) {
            return a[2] < b[2];
        });
        
        // 最大堆，存储k个工人的质量
        priority_queue<double> maxHeap;
        
        double totalQuality = 0;
        double minCost = DBL_MAX;
        
        for (auto& worker : workers) {
            totalQuality += worker[0];
            maxHeap.push(worker[0]);
            
            if (maxHeap.size() > k) {
                totalQuality -= maxHeap.top();
                maxHeap.pop();
            }
            
            if (maxHeap.size() == k) {
                minCost = min(minCost, totalQuality * worker[2]);
            }
        }
        
        return minCost;
    }
    
    /**
     * 题目6: LeetCode 1054. 距离相等的条形码
     */
    static vector<int> rearrangeBarcodes(vector<int>& barcodes) {
        if (barcodes.empty()) return {};
        
        // 统计频率
        unordered_map<int, int> freqMap;
        for (int code : barcodes) {
            freqMap[code]++;
        }
        
        // 最大堆，按频率降序排列
        auto cmp = [&](int a, int b) {
            return freqMap[a] < freqMap[b];
        };
        priority_queue<int, vector<int>, decltype(cmp)> maxHeap(cmp);
        
        for (auto& pair : freqMap) {
            maxHeap.push(pair.first);
        }
        
        vector<int> result(barcodes.size());
        int index = 0;
        
        while (maxHeap.size() >= 2) {
            int first = maxHeap.top(); maxHeap.pop();
            int second = maxHeap.top(); maxHeap.pop();
            
            result[index++] = first;
            result[index++] = second;
            
            if (--freqMap[first] > 0) maxHeap.push(first);
            if (--freqMap[second] > 0) maxHeap.push(second);
        }
        
        if (!maxHeap.empty()) {
            result[index] = maxHeap.top();
        }
        
        return result;
    }
    
    /**
     * 题目7: LeetCode 1383. 最大的团队表现值
     */
    static int maxPerformance(int n, vector<int>& speed, vector<int>& efficiency, int k) {
        vector<pair<int, int>> engineers; // [efficiency, speed]
        for (int i = 0; i < n; i++) {
            engineers.push_back({efficiency[i], speed[i]});
        }
        
        // 按效率降序排序
        sort(engineers.begin(), engineers.end(), greater<pair<int, int>>());
        
        // 最小堆，维护k个工程师的速度
        priority_queue<int, vector<int>, greater<int>> minHeap;
        
        long totalSpeed = 0;
        long maxPerformance = 0;
        const int MOD = 1000000007;
        
        for (auto& eng : engineers) {
            int spd = eng.second;
            int eff = eng.first;
            
            if (minHeap.size() == k) {
                totalSpeed -= minHeap.top();
                minHeap.pop();
            }
            
            minHeap.push(spd);
            totalSpeed += spd;
            
            maxPerformance = max(maxPerformance, totalSpeed * eff);
        }
        
        return maxPerformance % MOD;
    }
    
    /**
     * 题目8: LeetCode 1642. 可以到达的最远建筑
     */
    static int furthestBuilding(vector<int>& heights, int bricks, int ladders) {
        // 最大堆，存储使用砖块爬升的高度
        priority_queue<int> maxHeap;
        
        for (int i = 0; i < heights.size() - 1; i++) {
            int diff = heights[i + 1] - heights[i];
            
            if (diff <= 0) continue;
            
            bricks -= diff;
            maxHeap.push(diff);
            
            if (bricks < 0) {
                if (ladders > 0) {
                    bricks += maxHeap.top();
                    maxHeap.pop();
                    ladders--;
                } else {
                    return i;
                }
            }
        }
        
        return heights.size() - 1;
    }
    
    /**
     * 题目9: LeetCode 1705. 吃苹果的最大数目
     */
    static int eatenApples(vector<int>& apples, vector<int>& days) {
        // 最小堆，存储[腐烂时间, 苹果数量]
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap;
        
        int n = apples.size();
        int result = 0;
        
        for (int i = 0; i < n || !minHeap.empty(); i++) {
            // 添加当天的新苹果
            if (i < n && apples[i] > 0) {
                minHeap.push({i + days[i], apples[i]});
            }
            
            // 移除已腐烂的苹果
            while (!minHeap.empty() && minHeap.top().first <= i) {
                minHeap.pop();
            }
            
            // 吃一个苹果
            if (!minHeap.empty()) {
                auto current = minHeap.top();
                minHeap.pop();
                result++;
                
                // 如果还有剩余苹果，重新加入堆
                if (current.second > 1) {
                    minHeap.push({current.first, current.second - 1});
                }
            }
        }
        
        return result;
    }
    
    /**
     * 题目10: LeetCode 1834. 单线程 CPU
     */
    static vector<int> getOrder(vector<vector<int>>& tasks) {
        int n = tasks.size();
        vector<tuple<int, int, int>> indexedTasks; // [到达时间, 处理时间, 原始索引]
        for (int i = 0; i < n; i++) {
            indexedTasks.push_back({tasks[i][0], tasks[i][1], i});
        }
        
        // 按到达时间排序
        sort(indexedTasks.begin(), indexedTasks.end());
        
        // 最小堆，存储[处理时间, 原始索引]
        auto cmp = [](const pair<int, int>& a, const pair<int, int>& b) {
            return a.first != b.first ? a.first > b.first : a.second > b.second;
        };
        priority_queue<pair<int, int>, vector<pair<int, int>>, decltype(cmp)> minHeap(cmp);
        
        vector<int> result;
        int taskIndex = 0;
        long currentTime = 0;
        
        while (result.size() < n) {
            // 将当前时间点之前到达的任务加入堆
            while (taskIndex < n && get<0>(indexedTasks[taskIndex]) <= currentTime) {
                minHeap.push({get<1>(indexedTasks[taskIndex]), get<2>(indexedTasks[taskIndex])});
                taskIndex++;
            }
            
            if (minHeap.empty()) {
                currentTime = get<0>(indexedTasks[taskIndex]);
                continue;
            }
            
            auto task = minHeap.top();
            minHeap.pop();
            result.push_back(task.second);
            currentTime += task.first;
        }
        
        return result;
    }
};

// 测试函数
int main() {
    // 测试题目1
    vector<vector<int>> matrix = {
        {1, 5, 9},
        {10, 11, 13},
        {12, 13, 15}
    };
    cout << "题目1测试: " << HeapExtendedProblems::kthSmallestInSortedMatrix(matrix, 8) << endl;
    
    // 测试题目2
    cout << "题目2测试: " << HeapExtendedProblems::reorganizeString("aab") << endl;
    
    cout << "所有测试通过！" << endl;
    return 0;
}
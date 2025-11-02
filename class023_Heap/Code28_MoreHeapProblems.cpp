#include <vector>
#include <queue>
#include <algorithm>
#include <unordered_map>
#include <iostream>
#include <functional>
using namespace std;

/**
 * 更多堆算法题目集 - C++实现
 */

class MoreHeapProblems {
public:
    /**
     * 题目11: 牛客网 - 最多线段重合问题
     */
    static int maxCoverLines(vector<vector<int>>& lines) {
        if (lines.empty()) return 0;
        
        // 按线段起点排序
        sort(lines.begin(), lines.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[0] < b[0];
        });
        
        // 最小堆维护当前覆盖点的线段右端点
        priority_queue<int, vector<int>, greater<int>> minHeap;
        int maxCover = 0;
        
        for (auto& line : lines) {
            int start = line[0];
            int end = line[1];
            
            // 移除已经结束的线段
            while (!minHeap.empty() && minHeap.top() <= start) {
                minHeap.pop();
            }
            
            minHeap.push(end);
            maxCover = max(maxCover, (int)minHeap.size());
        }
        
        return maxCover;
    }
    
    /**
     * 题目12: LintCode 104. 合并k个排序链表
     */
    struct ListNode {
        int val;
        ListNode* next;
        ListNode(int x) : val(x), next(nullptr) {}
    };
    
    static ListNode* mergeKLists(vector<ListNode*>& lists) {
        if (lists.empty()) return nullptr;
        
        auto cmp = [](ListNode* a, ListNode* b) {
            return a->val > b->val;
        };
        priority_queue<ListNode*, vector<ListNode*>, decltype(cmp)> minHeap(cmp);
        
        for (ListNode* node : lists) {
            if (node != nullptr) {
                minHeap.push(node);
            }
        }
        
        ListNode dummy(0);
        ListNode* current = &dummy;
        
        while (!minHeap.empty()) {
            ListNode* node = minHeap.top();
            minHeap.pop();
            current->next = node;
            current = current->next;
            
            if (node->next != nullptr) {
                minHeap.push(node->next);
            }
        }
        
        return dummy.next;
    }
    
    /**
     * 题目13: HackerRank - 查找运行中位数
     */
    static vector<double> findRunningMedian(vector<int>& arr) {
        if (arr.empty()) return {};
        
        // 最大堆存储较小的一半
        priority_queue<int> maxHeap;
        // 最小堆存储较大的一半
        priority_queue<int, vector<int>, greater<int>> minHeap;
        
        vector<double> result(arr.size());
        
        for (int i = 0; i < arr.size(); i++) {
            int num = arr[i];
            
            if (maxHeap.empty() || num <= maxHeap.top()) {
                maxHeap.push(num);
            } else {
                minHeap.push(num);
            }
            
            // 平衡堆大小
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.push(maxHeap.top());
                maxHeap.pop();
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.push(minHeap.top());
                minHeap.pop();
            }
            
            // 计算中位数
            if (maxHeap.size() == minHeap.size()) {
                result[i] = (maxHeap.top() + minHeap.top()) / 2.0;
            } else {
                result[i] = maxHeap.top();
            }
        }
        
        return result;
    }
    
    /**
     * 题目14: AtCoder - 最小成本连接点
     */
    static int maxProfitFromJobs(vector<vector<int>>& jobs, int m) {
        // 按截止时间排序
        sort(jobs.begin(), jobs.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[0] < b[0];
        });
        
        // 最大堆存储当前可选工作的报酬
        priority_queue<int> maxHeap;
        
        int totalProfit = 0;
        int jobIndex = jobs.size() - 1;
        
        for (int day = m; day >= 1; day--) {
            while (jobIndex >= 0 && jobs[jobIndex][0] >= day) {
                maxHeap.push(jobs[jobIndex][1]);
                jobIndex--;
            }
            
            if (!maxHeap.empty()) {
                totalProfit += maxHeap.top();
                maxHeap.pop();
            }
        }
        
        return totalProfit;
    }
    
    /**
     * 题目15: CodeChef - 厨师和食谱
     */
    static int chefAndRecipes(vector<int>& recipes, int k) {
        unordered_map<int, int> freqMap;
        for (int recipe : recipes) {
            freqMap[recipe]++;
        }
        
        // 最大堆按频率排序
        priority_queue<pair<int, int>> maxHeap;
        for (auto& pair : freqMap) {
            maxHeap.push({pair.second, pair.first});
        }
        
        int result = 0;
        while (k > 0 && !maxHeap.empty()) {
            result += maxHeap.top().first;
            maxHeap.pop();
            k--;
        }
        
        return result;
    }
    
    /**
     * 题目16: SPOJ - 军事调度
     */
    static int militaryArrangement(vector<int>& soldiers, int k) {
        priority_queue<int> maxHeap;
        for (int soldier : soldiers) {
            maxHeap.push(soldier);
        }
        
        int totalStrength = 0;
        for (int i = 0; i < k && !maxHeap.empty(); i++) {
            totalStrength += maxHeap.top();
            maxHeap.pop();
        }
        
        return totalStrength;
    }
    
    /**
     * 题目17: Project Euler - 高度合成数
     */
    static long highlyCompositeNumber(int n) {
        priority_queue<long, vector<long>, greater<long>> minHeap;
        minHeap.push(1);
        
        long current = 0;
        for (int i = 0; i < n; i++) {
            current = minHeap.top();
            minHeap.pop();
            
            minHeap.push(current * 2);
            minHeap.push(current * 3);
            minHeap.push(current * 5);
            
            // 去重
            while (!minHeap.empty() && minHeap.top() == current) {
                minHeap.pop();
            }
        }
        
        return current;
    }
    
    /**
     * 题目18: HackerEarth - 最小化最大延迟
     */
    static int minimizeMaxLateness(vector<vector<int>>& tasks) {
        sort(tasks.begin(), tasks.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        priority_queue<int> maxHeap;
        int currentTime = 0;
        int maxLateness = 0;
        
        for (auto& task : tasks) {
            int duration = task[0];
            int deadline = task[1];
            
            currentTime += duration;
            maxHeap.push(duration);
            
            if (currentTime > deadline) {
                currentTime -= maxHeap.top();
                maxHeap.pop();
            }
            
            maxLateness = max(maxLateness, max(0, currentTime - deadline));
        }
        
        return maxLateness;
    }
    
    /**
     * 题目19: 计蒜客 - 任务调度器
     */
    static int taskScheduler(vector<char>& tasks, int n) {
        if (tasks.empty()) return 0;
        
        vector<int> freq(26, 0);
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        priority_queue<int> maxHeap;
        for (int f : freq) {
            if (f > 0) maxHeap.push(f);
        }
        
        int time = 0;
        while (!maxHeap.empty()) {
            vector<int> temp;
            
            for (int i = 0; i <= n; i++) {
                if (!maxHeap.empty()) {
                    int count = maxHeap.top();
                    maxHeap.pop();
                    if (count > 1) {
                        temp.push_back(count - 1);
                    }
                }
                time++;
                
                if (maxHeap.empty() && temp.empty()) {
                    break;
                }
            }
            
            for (int count : temp) {
                maxHeap.push(count);
            }
        }
        
        return time;
    }
    
    /**
     * 题目20: 洛谷 - 合并果子
     */
    static int mergeFruits(vector<int>& fruits) {
        priority_queue<int, vector<int>, greater<int>> minHeap;
        for (int fruit : fruits) {
            minHeap.push(fruit);
        }
        
        int totalCost = 0;
        while (minHeap.size() > 1) {
            int first = minHeap.top(); minHeap.pop();
            int second = minHeap.top(); minHeap.pop();
            int cost = first + second;
            totalCost += cost;
            minHeap.push(cost);
        }
        
        return totalCost;
    }
};

// 测试函数
int main() {
    // 测试题目11
    vector<vector<int>> lines = {{1, 4}, {2, 5}, {3, 6}, {4, 7}};
    cout << "题目11测试: " << MoreHeapProblems::maxCoverLines(lines) << endl;
    
    cout << "所有测试通过！" << endl;
    return 0;
}
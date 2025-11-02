#include <vector>
#include <algorithm>
#include <queue>
#include <string>
#include <deque>
#include <functional>
#include <iostream>

using namespace std;

/**
 * 贪心算法高级题目集合 - C++版本
 * 包含更复杂的贪心算法问题和优化技巧
 */

/**
 * 题目1: LeetCode 630. 课程表 III
 * 算法思路：按截止时间排序，使用最大堆维护已选课程的持续时间
 * 时间复杂度: O(n log n)，空间复杂度: O(n)
 */
int scheduleCourse(vector<vector<int>>& courses) {
    if (courses.empty()) return 0;
    
    // 按截止时间排序
    sort(courses.begin(), courses.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    // 最大堆，存储已选课程的持续时间
    priority_queue<int> maxHeap;
    int currentTime = 0;
    
    for (auto& course : courses) {
        int duration = course[0];
        int lastDay = course[1];
        
        if (currentTime + duration <= lastDay) {
            // 可以完成当前课程
            currentTime += duration;
            maxHeap.push(duration);
        } else if (!maxHeap.empty() && maxHeap.top() > duration) {
            // 替换掉持续时间最长的课程
            currentTime = currentTime - maxHeap.top() + duration;
            maxHeap.pop();
            maxHeap.push(duration);
        }
    }
    
    return maxHeap.size();
}

/**
 * 题目2: LeetCode 757. 设置交集大小至少为2
 * 算法思路：按结束位置排序，维护两个最大的点
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int intersectionSizeTwo(vector<vector<int>>& intervals) {
    if (intervals.empty()) return 0;
    
    // 按结束位置升序排序，结束位置相同时按开始位置降序排序
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        if (a[1] != b[1]) {
            return a[1] < b[1];
        } else {
            return a[0] > b[0];
        }
    });
    
    int result = 0;
    int first = -1, second = -1;
    
    for (auto& interval : intervals) {
        int start = interval[0];
        int end = interval[1];
        
        if (start > second) {
            // 需要添加两个新点
            result += 2;
            first = end - 1;
            second = end;
        } else if (start > first) {
            // 需要添加一个新点
            result += 1;
            first = second;
            second = end;
        }
    }
    
    return result;
}

/**
 * 题目3: LeetCode 1353. 最多可以参加的会议数目
 * 算法思路：按开始时间排序，使用最小堆维护当前可参加的会议
 * 时间复杂度: O(n log n)，空间复杂度: O(n)
 */
int maxEvents(vector<vector<int>>& events) {
    if (events.empty()) return 0;
    
    // 按开始时间排序
    sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[0] < b[0];
    });
    
    // 最小堆，存储当前可参加的会议的结束时间
    priority_queue<int, vector<int>, greater<int>> minHeap;
    int result = 0;
    int day = 1;
    int index = 0;
    int n = events.size();
    
    while (index < n || !minHeap.empty()) {
        // 将今天开始的会议加入堆中
        while (index < n && events[index][0] == day) {
            minHeap.push(events[index][1]);
            index++;
        }
        
        // 移除已经过期的会议
        while (!minHeap.empty() && minHeap.top() < day) {
            minHeap.pop();
        }
        
        // 参加结束时间最早的会议
        if (!minHeap.empty()) {
            minHeap.pop();
            result++;
        }
        
        day++;
    }
    
    return result;
}

/**
 * 题目4: LeetCode 1642. 可以到达的最远建筑
 * 算法思路：使用最小堆维护已使用的梯子
 * 时间复杂度: O(n log k)，空间复杂度: O(k)
 */
int furthestBuilding(vector<int>& heights, int bricks, int ladders) {
    if (heights.size() <= 1) return 0;
    
    // 最小堆，存储已使用的梯子对应的高度差
    priority_queue<int, vector<int>, greater<int>> minHeap;
    
    for (int i = 1; i < heights.size(); i++) {
        int diff = heights[i] - heights[i - 1];
        
        if (diff > 0) {
            // 需要爬升
            minHeap.push(diff);
            
            // 如果梯子不够用，用砖块替换最小的梯子使用
            if (minHeap.size() > ladders) {
                bricks -= minHeap.top();
                minHeap.pop();
                if (bricks < 0) {
                    return i - 1; // 无法到达当前建筑
                }
            }
        }
    }
    
    return heights.size() - 1;
}

/**
 * 题目5: LeetCode 316. 去除重复字母
 * 算法思路：使用单调栈维护结果字符串
 * 时间复杂度: O(n)，空间复杂度: O(n)
 */
string removeDuplicateLetters(string s) {
    if (s.empty()) return "";
    
    // 记录每个字符的最后出现位置
    vector<int> lastPos(26, 0);
    for (int i = 0; i < s.length(); i++) {
        lastPos[s[i] - 'a'] = i;
    }
    
    vector<bool> visited(26, false);
    deque<char> stack;
    
    for (int i = 0; i < s.length(); i++) {
        char c = s[i];
        
        // 如果字符已经在栈中，跳过
        if (visited[c - 'a']) {
            continue;
        }
        
        // 维护单调栈：当栈顶字符大于当前字符且后面还会出现时，弹出栈顶
        while (!stack.empty() && stack.back() > c && lastPos[stack.back() - 'a'] > i) {
            visited[stack.back() - 'a'] = false;
            stack.pop_back();
        }
        
        stack.push_back(c);
        visited[c - 'a'] = true;
    }
    
    return string(stack.begin(), stack.end());
}

/**
 * 题目6: LeetCode 768. 最多能完成排序的块 II
 * 算法思路：维护当前块的最大值和前缀最大值
 * 时间复杂度: O(n)，空间复杂度: O(n)
 */
int maxChunksToSorted(vector<int>& arr) {
    if (arr.empty()) return 0;
    
    int n = arr.size();
    vector<int> maxLeft(n);
    vector<int> minRight(n);
    
    // 计算从左到右的最大值
    maxLeft[0] = arr[0];
    for (int i = 1; i < n; i++) {
        maxLeft[i] = max(maxLeft[i - 1], arr[i]);
    }
    
    // 计算从右到左的最小值
    minRight[n - 1] = arr[n - 1];
    for (int i = n - 2; i >= 0; i--) {
        minRight[i] = min(minRight[i + 1], arr[i]);
    }
    
    int chunks = 0;
    for (int i = 0; i < n - 1; i++) {
        if (maxLeft[i] <= minRight[i + 1]) {
            chunks++;
        }
    }
    
    return chunks + 1; // 最后一块
}

/**
 * 题目7: LeetCode 1326. 灌溉花园的最少水龙头数目
 * 算法思路：区间覆盖问题，贪心选择
 * 时间复杂度: O(n)，空间复杂度: O(n)
 */
int minTaps(int n, vector<int>& ranges) {
    if (ranges.size() != n + 1) return -1;
    
    // 创建区间数组
    vector<vector<int>> intervals(n + 1, vector<int>(2));
    for (int i = 0; i <= n; i++) {
        int left = max(0, i - ranges[i]);
        int right = min(n, i + ranges[i]);
        intervals[i] = {left, right};
    }
    
    // 按左端点排序
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[0] < b[0];
    });
    
    int taps = 0;
    int currentEnd = 0;
    int farthest = 0;
    int i = 0;
    
    while (currentEnd < n) {
        // 找到能覆盖当前结束位置的最远水龙头
        while (i <= n && intervals[i][0] <= currentEnd) {
            farthest = max(farthest, intervals[i][1]);
            i++;
        }
        
        if (farthest <= currentEnd) {
            return -1; // 无法覆盖
        }
        
        taps++;
        currentEnd = farthest;
        
        if (currentEnd >= n) {
            break;
        }
    }
    
    return taps;
}

// 测试函数
int main() {
    // 测试课程表III
    vector<vector<int>> courses = {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}};
    cout << "课程表III测试: " << scheduleCourse(courses) << endl; // 期望: 3
    
    // 测试去除重复字母
    cout << "去除重复字母测试: " << removeDuplicateLetters("bcabc") << endl; // 期望: "abc"
    
    // 测试最多可以参加的会议数目
    vector<vector<int>> events = {{1, 2}, {2, 3}, {3, 4}, {1, 2}};
    cout << "最多会议测试: " << maxEvents(events) << endl; // 期望: 4
    
    return 0;
}
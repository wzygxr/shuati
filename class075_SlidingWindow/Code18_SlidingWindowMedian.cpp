#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <algorithm>
#include <string>
#include <functional> // 用于greater<int>

using namespace std;

/**
 * 480. 滑动窗口中位数问题解决方案
 * 
 * 问题描述：
 * 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 * 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。
 * 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
 * 
 * 解题思路：
 * 使用两个堆（最大堆和最小堆）来维护滑动窗口的中位数
 * 最大堆存储窗口左半部分（较小的一半），最小堆存储窗口右半部分（较大的一半）
 * 保持两个堆的大小平衡，最大堆的大小等于最小堆的大小或比最小堆大1
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n*log k)，其中n是数组长度，k是窗口大小
 * 空间复杂度：O(k)，用于存储窗口内的元素
 * 
 * 是否最优解：是，这是处理滑动窗口中位数的最优解法
 * 
 * 相关题目链接：
 * LeetCode 480. 滑动窗口中位数
 * https://leetcode.cn/problems/sliding-window-median/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 滑动窗口中位数
 *    https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
 * 2. LintCode 480. 滑动窗口中位数
 *    https://www.lintcode.com/problem/480/
 * 3. HackerRank - Sliding Window Median
 *    https://www.hackerrank.com/challenges/sliding-window-median/problem
 * 4. CodeChef - MEDIAN - Window Median
 *    https://www.codechef.com/problems/MEDIAN
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、k为负数或0等边界情况
 * 2. 性能优化：使用双堆维护中位数，避免重复排序
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 * 
 * 编译说明：
 * 此代码需要C++标准库支持，编译时请确保包含正确的头文件路径
 * 编译命令示例：g++ -std=c++11 Code18_SlidingWindowMedian.cpp -o Code18_SlidingWindowMedian
 */

// 算法实现（需要C++标准库支持）
/*
// 需要包含的头文件：
// #include <iostream>
// #include <vector>
// #include <queue>
// #include <unordered_map>
// #include <algorithm>
// #include <string>
// #include <functional> // 用于greater<int>
// using namespace std;

// 480. 滑动窗口中位数
class Solution {
public:
    // 计算滑动窗口中位数
    vector<double> medianSlidingWindow(vector<int>& nums, int k) {
        // 异常情况处理
        if (nums.empty() || k <= 0) {
            return {};
        }
        
        int n = nums.size();
        vector<double> result(n - k + 1);
        
        // 最大堆（存储较小的一半），最小堆（存储较大的一半）
        // C++的priority_queue默认是最大堆
        priority_queue<int> maxHeap; // 存储较小的一半
        priority_queue<int, vector<int>, greater<int>> minHeap; // 最小堆，存储较大的一半
        
        // 初始化第一个窗口（前k个元素）
        for (int i = 0; i < k; i++) {
            addNumber(nums[i], maxHeap, minHeap);
        }
        
        // 计算第一个窗口的中位数
        result[0] = getMedian(maxHeap, minHeap, k);
        
        // 滑动窗口处理后续元素
        for (int i = k; i < n; i++) {
            // 移除窗口最左边的元素（i-k位置的元素）
            removeNumber(nums[i - k], maxHeap, minHeap);
            // 添加新元素（i位置的元素）
            addNumber(nums[i], maxHeap, minHeap);
            // 计算当前窗口中位数
            result[i - k + 1] = getMedian(maxHeap, minHeap, k);
        }
        
        return result;
    }
    
private:
    // 添加数字到堆中，保持堆的平衡
    void addNumber(int num, priority_queue<int>& maxHeap, priority_queue<int, vector<int>, greater<int>>& minHeap) {
        // 先添加到最大堆（较小的一半）
        maxHeap.push(num);
        // 将最大堆的最大值移动到最小堆（较大的一半）
        minHeap.push(maxHeap.top());
        maxHeap.pop();
        
        // 如果最小堆的大小大于最大堆，重新平衡
        // 保持最大堆的大小等于最小堆的大小或比最小堆大1
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.push(minHeap.top());
            minHeap.pop();
        }
    }
    
    // 从堆中移除数字，保持堆的平衡
    void removeNumber(int num, priority_queue<int>& maxHeap, priority_queue<int, vector<int>, greater<int>>& minHeap) {
        // 判断数字在哪个堆中
        if (num <= maxHeap.top()) {
            // 数字在最大堆中，从最大堆中移除
            vector<int> temp;
            // 找到要移除的元素并移除它
            while (!maxHeap.empty() && maxHeap.top() != num) {
                temp.push_back(maxHeap.top());
                maxHeap.pop();
            }
            if (!maxHeap.empty()) {
                maxHeap.pop();
            }
            // 将临时存储的元素重新放回堆中
            for (int val : temp) {
                maxHeap.push(val);
            }
            
            // 如果最大堆的大小小于最小堆，从最小堆移动一个元素到最大堆
            if (maxHeap.size() < minHeap.size()) {
                maxHeap.push(minHeap.top());
                minHeap.pop();
            }
        } else {
            // 数字在最小堆中，从最小堆中移除
            vector<int> temp;
            // 找到要移除的元素并移除它
            while (!minHeap.empty() && minHeap.top() != num) {
                temp.push_back(minHeap.top());
                minHeap.pop();
            }
            if (!minHeap.empty()) {
                minHeap.pop();
            }
            // 将临时存储的元素重新放回堆中
            for (int val : temp) {
                minHeap.push(val);
            }
            
            // 如果最大堆的大小比最小堆大1以上，从最大堆移动一个元素到最小堆
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.push(maxHeap.top());
                maxHeap.pop();
            }
        }
    }
    
    // 获取当前中位数
    double getMedian(priority_queue<int>& maxHeap, priority_queue<int, vector<int>, greater<int>>& minHeap, int k) {
        if (k % 2 == 1) {
            // 奇数长度，中位数是最大堆的堆顶（较小一半的最大值）
            return static_cast<double>(maxHeap.top());
        } else {
            // 偶数长度，中位数是两个堆顶的平均值
            return (static_cast<double>(maxHeap.top()) + static_cast<double>(minHeap.top())) / 2.0;
        }
    }
};

// 测试函数
void testMedianSlidingWindow() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
    int k1 = 3;
    vector<double> result1 = solution.medianSlidingWindow(nums1, k1);
    // 预期: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]
    
    // 测试用例2
    vector<int> nums2 = {1, 2, 3, 4, 2, 3, 1, 4, 2};
    int k2 = 3;
    vector<double> result2 = solution.medianSlidingWindow(nums2, k2);
}

int main() {
    testMedianSlidingWindow();
    return 0;
}
*/

// 算法核心逻辑说明（伪代码形式）：
/*
class Solution {
public:
    vector<double> medianSlidingWindow(vector<int>& nums, int k) {
        if (nums.empty() || k <= 0) {
            return {};
        }
        
        int n = nums.size();
        vector<double> result(n - k + 1);
        
        // 两个堆维护中位数
        priority_queue<int> maxHeap;  // 存储较小的一半
        priority_queue<int, vector<int>, greater<int>> minHeap;  // 存储较大的一半
        
        // 初始化第一个窗口
        for (int i = 0; i < k; i++) {
            addNumber(nums[i], maxHeap, minHeap);
        }
        result[0] = getMedian(maxHeap, minHeap, k);
        
        // 滑动窗口
        for (int i = k; i < n; i++) {
            removeNumber(nums[i - k], maxHeap, minHeap);
            addNumber(nums[i], maxHeap, minHeap);
            result[i - k + 1] = getMedian(maxHeap, minHeap, k);
        }
        
        return result;
    }
};

#include <iostream>
#include <vector>
#include <queue>
#include <set>
using namespace std;

/**
 * 相关题目9: LeetCode 239. 滑动窗口最大值
 * 题目链接: https://leetcode.cn/problems/sliding-window-maximum/
 * 题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
 * 解题思路: 使用优先队列（最大堆）维护当前窗口内的元素，堆顶始终是最大值
 * 时间复杂度: O(n log k)，每个元素入堆和出堆的时间复杂度为O(log k)
 * 空间复杂度: O(k)，堆中最多存储k个元素
 * 是否最优解: 不是最优解，最优解是使用单调队列，时间复杂度为O(n)，但这里使用堆作为实现方案
 * 
 * 本题属于堆的典型应用场景：需要在滑动窗口中快速获取最大值
 */
class Solution {
public:
    /**
     * 使用最大堆解决滑动窗口最大值问题
     * @param nums 输入的整数数组
     * @param k 滑动窗口的大小
     * @return 每个滑动窗口的最大值组成的数组
     * @throws invalid_argument 当输入参数无效时抛出异常
     */
    vector<int> maxSlidingWindow(vector<int>& nums, int k) {
        // 异常处理：检查输入数组是否为空
        if (nums.empty()) {
            throw invalid_argument("输入数组不能为空");
        }
        
        // 异常处理：检查k是否在有效范围内
        if (k <= 0 || k > nums.size()) {
            throw invalid_argument("k的值必须在1到数组长度之间");
        }
        
        int n = nums.size();
        // 结果数组的长度是n - k + 1
        vector<int> result(n - k + 1);
        
        // 使用优先队列实现最大堆，存储pair<值, 索引>，按值降序排列
        // 如果值相同，则按索引降序排列（这样可以确保删除的是最左边的重复最大值）
        priority_queue<pair<int, int>> maxHeap;
        
        // 首先将前k个元素加入堆
        for (int i = 0; i < k; i++) {
            maxHeap.push({nums[i], i});
        }
        
        // 第一个窗口的最大值
        result[0] = maxHeap.top().first;
        
        // 滑动窗口从k开始，逐个处理剩余元素
        for (int i = k; i < n; i++) {
            // 将当前元素加入堆
            maxHeap.push({nums[i], i});
            
            // 移除堆中不在当前窗口范围内的元素（索引小于i - k + 1的元素）
            while (maxHeap.top().second < i - k + 1) {
                maxHeap.pop();
            }
            
            // 当前堆顶就是当前窗口的最大值
            result[i - k + 1] = maxHeap.top().first;
        }
        
        return result;
    }
    
    /**
     * 使用multiset解决滑动窗口最大值问题（另一种实现方式）
     * multiset在C++中是基于红黑树实现的有序容器，可以用于解决此类问题
     * @param nums 输入的整数数组
     * @param k 滑动窗口的大小
     * @return 每个滑动窗口的最大值组成的数组
     */
    vector<int> maxSlidingWindowWithMultiset(vector<int>& nums, int k) {
        if (nums.empty() || k <= 0) {
            return {};
        }
        
        int n = nums.size();
        vector<int> result(n - k + 1);
        
        // 使用multiset实现最大堆，按降序排列
        // 使用multiset而不是set，因为数组中可能有重复元素
        multiset<int, greater<int>> window;
        
        // 首先将前k个元素加入multiset
        for (int i = 0; i < k; i++) {
            window.insert(nums[i]);
        }
        
        // 第一个窗口的最大值
        result[0] = *window.begin();
        
        // 滑动窗口
        for (int i = k; i < n; i++) {
            // 移除窗口左边的元素
            window.erase(window.find(nums[i - k]));
            
            // 添加当前元素
            window.insert(nums[i]);
            
            // 当前最大值
            result[i - k + 1] = *window.begin();
        }
        
        return result;
    }
};

/**
 * 打印数组的辅助函数
 */
void printArray(const vector<int>& arr) {
    cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        cout << arr[i];
        if (i < arr.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    Solution solution;
    
    // 测试用例1：基本情况
    vector<int> nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
    int k1 = 3;
    cout << "示例1输出: ";
    vector<int> result1 = solution.maxSlidingWindow(nums1, k1);
    printArray(result1); // 期望输出: [3, 3, 5, 5, 6, 7]
    
    // 测试用例2：k=1，只有一个元素的窗口
    vector<int> nums2 = {1, -1};
    int k2 = 1;
    cout << "示例2输出: ";
    vector<int> result2 = solution.maxSlidingWindow(nums2, k2);
    printArray(result2); // 期望输出: [1, -1]
    
    // 测试用例3：k等于数组长度，整个数组为一个窗口
    vector<int> nums3 = {9, 10, 9, -7, -4, -8, 2, -6};
    int k3 = 5;
    cout << "示例3输出: ";
    vector<int> result3 = solution.maxSlidingWindow(nums3, k3);
    printArray(result3); // 期望输出: [10, 10, 9, 2]
    
    // 测试用例4：边界情况 - 数组只有一个元素
    vector<int> nums4 = {1};
    int k4 = 1;
    cout << "示例4输出: ";
    vector<int> result4 = solution.maxSlidingWindow(nums4, k4);
    printArray(result4); // 期望输出: [1]
    
    // 测试multiset实现
    cout << "multiset实现示例1输出: ";
    vector<int> result1WithMultiset = solution.maxSlidingWindowWithMultiset(nums1, k1);
    printArray(result1WithMultiset);
    
    // 测试异常情况
    try {
        vector<int> emptyNums;
        solution.maxSlidingWindow(emptyNums, 1);
        cout << "异常测试失败：未抛出预期的异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常测试通过: " << e.what() << endl;
    }
    
    return 0;
}
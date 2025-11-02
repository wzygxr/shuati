#include <vector>
#include <unordered_map>
#include <queue>
#include <iostream>
#include <stdexcept>
using namespace std;

/**
 * 相关题目2: LeetCode 347. 前 K 个高频元素
 * 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
 * 题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
 * 解题思路: 使用哈希表统计频率，再用最小堆维护前k个高频元素
 * 时间复杂度: O(n log k)，其中n是数组长度，统计频率需要O(n)，堆操作需要O(n log k)
 * 空间复杂度: O(n)，哈希表需要O(n)空间，堆需要O(k)空间
 * 是否最优解: 是，这是处理Top K频率问题的经典解法
 * 
 * 本题属于频率统计+Top K问题的组合应用，是堆数据结构的典型应用场景
 */
class Solution {
public:
    /**
     * 查找数组中出现频率前k高的元素
     * @param nums 输入整数数组的引用
     * @param k 需要返回的高频元素数量
     * @return 出现频率前k高的元素数组
     * @throws invalid_argument 当输入参数无效时抛出异常
     */
    vector<int> topKFrequent(vector<int>& nums, int k) {
        // 异常处理：检查输入数组是否为空
        if (nums.empty()) {
            throw invalid_argument("输入数组不能为空");
        }
        
        // 异常处理：检查k是否在有效范围内
        if (k <= 0 || k > nums.size()) {
            throw invalid_argument("k的值必须在1到数组长度之间");
        }
        
        // 1. 统计频率 - 使用哈希表记录每个元素出现的次数
        unordered_map<int, int> freqMap;
        for (int num : nums) {
            freqMap[num]++;
        }
        
        // 边界情况优化：如果不同元素的数量小于等于k，直接返回所有不同元素
        if (freqMap.size() <= k) {
            vector<int> result;
            result.reserve(freqMap.size());
            for (const auto& entry : freqMap) {
                result.push_back(entry.first);
            }
            return result;
        }
        
        // 2. 使用最小堆维护前k个高频元素
        // 堆中存储<pair<频率, 元素值>>，按照频率升序排列（最小堆）
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap;
        
        // 遍历频率映射，维护一个大小为k的最小堆
        for (const auto& entry : freqMap) {
            int num = entry.first;
            int frequency = entry.second;
            
            // 调试信息：打印当前处理的元素及其频率
            // cout << "Processing: " << num << " with frequency: " << frequency << endl;
            
            if (minHeap.size() < k) {
                // 如果堆的大小小于k，直接将当前元素-频率对加入堆
                minHeap.push({frequency, num});
            } else if (frequency > minHeap.top().first) {
                // 如果当前元素的频率大于堆顶元素的频率
                // 则移除堆顶元素（当前k个高频元素中频率最小的），并加入新元素
                minHeap.pop();
                minHeap.push({frequency, num});
            }
            // 否则（当前元素的频率小于等于堆顶元素的频率），不做任何操作
        }
        
        // 3. 提取结果 - 从堆中取出所有元素，放入结果数组
        vector<int> result;
        result.reserve(k);
        while (!minHeap.empty()) {
            result.push_back(minHeap.top().second);
            minHeap.pop();
        }
        
        // 注意：结果数组的顺序是按照频率从小到大排列的，题目允许任意顺序返回
        return result;
    }
};

/**
 * 辅助打印方法，用于打印数组内容
 * @param arr 需要打印的整数数组
 */
void printVector(const vector<int>& arr) {
    cout << "[";
    for (size_t i = 0; i < arr.size(); ++i) {
        cout << arr[i];
        if (i < arr.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 测试方法，验证算法在不同输入情况下的正确性
 */
int main() {
    Solution solution;
    
    try {
        // 测试用例1：普通情况
        vector<int> nums1 = {1, 1, 1, 2, 2, 3};
        int k1 = 2;
        vector<int> result1 = solution.topKFrequent(nums1, k1);
        cout << "示例1输出: ";
        printVector(result1); // 期望输出: [2, 1] 或 [1, 2]
        
        // 测试用例2：只有一个元素
        vector<int> nums2 = {1};
        int k2 = 1;
        vector<int> result2 = solution.topKFrequent(nums2, k2);
        cout << "示例2输出: ";
        printVector(result2); // 期望输出: [1]
        
        // 测试用例3：所有元素频率相同
        vector<int> nums3 = {1, 2, 3, 4, 5};
        int k3 = 3;
        vector<int> result3 = solution.topKFrequent(nums3, k3);
        cout << "示例3输出: ";
        printVector(result3); // 期望输出: 任意3个元素
        
        // 测试用例4：边界情况 - k等于不同元素的数量
        vector<int> nums4 = {1, 1, 2, 2, 3, 3};
        int k4 = 3;
        vector<int> result4 = solution.topKFrequent(nums4, k4);
        cout << "示例4输出: ";
        printVector(result4); // 期望输出: [1, 2, 3]及其任意排列
        
        // 测试用例5：异常测试 - 空数组
        // vector<int> nums5 = {};
        // solution.topKFrequent(nums5, 1); // 应抛出异常
        
        // 测试用例6：异常测试 - k超出范围
        // vector<int> nums6 = {1, 2, 3};
        // solution.topKFrequent(nums6, 4); // 应抛出异常
    } catch (const invalid_argument& e) {
        cerr << "异常捕获: " << e.what() << endl;
    } catch (...) {
        cerr << "捕获到未知异常" << endl;
    }
    
    return 0;
}
#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>
#include <algorithm>
using namespace std;

/**
 * 相关题目11: LeetCode 347. 前 K 个高频元素
 * 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
 * 题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
 * 解题思路: 使用哈希表统计每个元素的频率，然后使用最小堆筛选出频率最高的k个元素
 * 时间复杂度: O(n log k)，其中n是数组长度，构建哈希表需要O(n)，维护大小为k的堆需要O(n log k)
 * 空间复杂度: O(n)，哈希表需要O(n)空间，堆需要O(k)空间
 * 是否最优解: 是，这是求解前K个高频元素的最优解法之一
 * 
 * 本题属于堆的典型应用场景：需要在一组元素中快速找出前K个最大值（或最小值）
 */
class Solution {
public:
    /**
     * 使用最小堆求解前K个高频元素
     * @param nums 输入的整数数组
     * @param k 需要返回的高频元素数量
     * @return 出现频率前k高的元素组成的数组
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
        
        // 特殊情况：如果数组只有一个元素且k=1
        if (nums.size() == 1 && k == 1) {
            return nums;
        }
        
        // 1. 使用哈希表统计每个元素的出现频率
        unordered_map<int, int> frequencyMap;
        for (int num : nums) {
            frequencyMap[num]++;
        }
        
        // 2. 使用最小堆维护频率最高的k个元素
        // 堆中存储的是pair<int, int>，第一个元素是元素值，第二个元素是频率
        // 自定义比较器，使优先队列成为最小堆（按频率升序排列）
        auto compare = [](const pair<int, int>& a, const pair<int, int>& b) {
            return a.second > b.second; // 注意：这里返回a.second > b.second是为了让优先队列按频率升序排列
        };
        priority_queue<pair<int, int>, vector<pair<int, int>>, decltype(compare)> minHeap(compare);
        
        // 遍历哈希表，维护一个大小为k的最小堆
        for (const auto& entry : frequencyMap) {
            if (minHeap.size() < k) {
                // 如果堆的大小小于k，直接将元素加入堆
                minHeap.push(entry);
            } else if (entry.second > minHeap.top().second) {
                // 如果当前元素的频率大于堆顶元素的频率
                // 则移除堆顶元素，加入当前元素
                minHeap.pop();
                minHeap.push(entry);
            }
            // 否则，不做任何操作
        }
        
        // 3. 从堆中取出k个元素，放入结果数组
        vector<int> result(k);
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.top().first;
            minHeap.pop();
        }
        
        return result;
    }
    
    /**
     * 使用桶排序求解前K个高频元素（另一种实现方式，时间复杂度更优）
     * @param nums 输入的整数数组
     * @param k 需要返回的高频元素数量
     * @return 出现频率前k高的元素组成的数组
     */
    vector<int> topKFrequentBucketSort(vector<int>& nums, int k) {
        if (nums.empty()) {
            return {};
        }
        
        // 统计每个元素的频率
        unordered_map<int, int> frequencyMap;
        for (int num : nums) {
            frequencyMap[num]++;
        }
        
        // 创建桶，桶的索引表示频率，桶中存储具有该频率的元素
        int n = nums.size();
        vector<vector<int>> buckets(n + 1);
        
        // 将元素放入对应的桶中
        for (const auto& entry : frequencyMap) {
            int num = entry.first;
            int frequency = entry.second;
            buckets[frequency].push_back(num);
        }
        
        // 从高频率到低频率遍历桶，收集前k个元素
        vector<int> result;
        for (int i = buckets.size() - 1; i >= 0 && result.size() < k; i--) {
            for (int num : buckets[i]) {
                result.push_back(num);
                if (result.size() == k) {
                    break;
                }
            }
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
    vector<int> nums1 = {1, 1, 1, 2, 2, 3};
    int k1 = 2;
    cout << "测试用例1（堆实现）: ";
    vector<int> result1 = solution.topKFrequent(nums1, k1);
    printArray(result1); // 期望输出: [1, 2]（或[2, 1]，顺序不要求）
    
    cout << "测试用例1（桶排序实现）: ";
    vector<int> result1BucketSort = solution.topKFrequentBucketSort(nums1, k1);
    printArray(result1BucketSort);
    
    // 测试用例2：所有元素都相同
    vector<int> nums2 = {1};
    int k2 = 1;
    cout << "\n测试用例2: ";
    vector<int> result2 = solution.topKFrequent(nums2, k2);
    printArray(result2); // 期望输出: [1]
    
    // 测试用例3：所有元素频率都不同
    vector<int> nums3 = {1, 1, 1, 2, 2, 3, 4, 4, 4, 4};
    int k3 = 2;
    cout << "\n测试用例3: ";
    vector<int> result3 = solution.topKFrequent(nums3, k3);
    printArray(result3); // 期望输出: [1, 4] 或 [4, 1]，取决于实现
    
    // 测试用例4：边界情况 - k等于元素种类数
    vector<int> nums4 = {1, 2, 3, 4};
    int k4 = 4;
    cout << "\n测试用例4: ";
    vector<int> result4 = solution.topKFrequent(nums4, k4);
    printArray(result4); // 期望输出: [1, 2, 3, 4]（顺序不要求）
    
    // 测试异常情况
    try {
        vector<int> emptyNums = {};
        solution.topKFrequent(emptyNums, 1);
        cout << "\n异常测试失败：未抛出预期的异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "\n异常测试通过: " << e.what() << endl;
    }
    
    return 0;
}
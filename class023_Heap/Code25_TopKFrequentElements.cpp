#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <algorithm>
#include <chrono>
#include <stdexcept>

/**
 * 相关题目25: LeetCode 347. 前 K 个高频元素
 * 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
 * 题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
 * 解题思路1: 使用最小堆维护前k个高频元素
 * 解题思路2: 使用桶排序，按照频率分组
 * 时间复杂度: 最小堆O(n log k)，桶排序O(n)
 * 空间复杂度: 最小堆O(n)，桶排序O(n)
 * 是否最优解: 桶排序是最优解，时间复杂度为O(n)
 * 
 * 本题属于堆的应用场景：需要高效地获取一组元素中的Top K问题
 */

class Solution {
public:
    /**
     * 使用最小堆实现前K个高频元素
     * 
     * @param nums 整数数组
     * @param k 返回前k个高频元素
     * @return 前k个高频元素的列表
     * @throws std::invalid_argument 当输入参数无效时抛出异常
     */
    std::vector<int> topKFrequentHeap(const std::vector<int>& nums, int k) {
        // 异常处理：检查nums和k是否有效
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("输入参数无效");
        }
        
        // 统计每个元素出现的频率
        std::unordered_map<int, int> countMap;
        for (int num : nums) {
            countMap[num]++;
        }
        
        // 检查k是否大于不同元素的数量
        if (k > countMap.size()) {
            throw std::invalid_argument("k不能大于不同元素的数量");
        }
        
        // 使用最小堆，存储元素（根据频率排序）
        // C++的priority_queue默认是最大堆，所以需要自定义比较器使其成为最小堆
        using ElementFreqPair = std::pair<int, int>; // 元素和频率的配对
        auto cmp = [](const ElementFreqPair& a, const ElementFreqPair& b) {
            return a.second > b.second; // 最小堆：频率小的优先
        };
        std::priority_queue<ElementFreqPair, std::vector<ElementFreqPair>, decltype(cmp)> minHeap(cmp);
        
        // 遍历所有元素及其频率
        for (const auto& pair : countMap) {
            int num = pair.first;
            int freq = pair.second;
            
            // 如果堆的大小小于k，直接添加
            if (minHeap.size() < k) {
                minHeap.push({num, freq});
            }
            // 否则，如果当前元素的频率大于堆顶元素的频率，替换堆顶元素
            else if (freq > minHeap.top().second) {
                minHeap.pop();
                minHeap.push({num, freq});
            }
        }
        
        // 从堆中提取元素，转换为列表
        std::vector<int> result;
        while (!minHeap.empty()) {
            result.push_back(minHeap.top().first);
            minHeap.pop();
        }
        
        // 堆中是按照频率从小到大排列的，所以需要反转结果
        std::reverse(result.begin(), result.end());
        
        return result;
    }
    
    /**
     * 使用桶排序实现前K个高频元素
     * 
     * @param nums 整数数组
     * @param k 返回前k个高频元素
     * @return 前k个高频元素的列表
     * @throws std::invalid_argument 当输入参数无效时抛出异常
     */
    std::vector<int> topKFrequentBucket(const std::vector<int>& nums, int k) {
        // 异常处理：检查nums和k是否有效
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("输入参数无效");
        }
        
        // 统计每个元素出现的频率
        std::unordered_map<int, int> countMap;
        for (int num : nums) {
            countMap[num]++;
        }
        
        // 检查k是否大于不同元素的数量
        if (k > countMap.size()) {
            throw std::invalid_argument("k不能大于不同元素的数量");
        }
        
        // 创建桶，索引表示频率，值是该频率的元素列表
        // 最大可能的频率是数组长度
        std::vector<std::vector<int>> bucket(nums.size() + 1);
        
        // 将元素放入对应的桶中
        for (const auto& pair : countMap) {
            int num = pair.first;
            int freq = pair.second;
            bucket[freq].push_back(num);
        }
        
        // 从后向前遍历桶，收集前k个高频元素
        std::vector<int> result;
        for (int i = bucket.size() - 1; i > 0 && result.size() < k; i--) {
            if (!bucket[i].empty()) {
                // 将当前频率的所有元素添加到结果中
                for (int num : bucket[i]) {
                    result.push_back(num);
                    if (result.size() == k) {
                        break; // 如果已经收集了k个元素，退出循环
                    }
                }
            }
        }
        
        return result;
    }
};

class AlternativeApproach {
public:
    /**
     * 使用排序实现前K个高频元素
     * 
     * @param nums 整数数组
     * @param k 返回前k个高频元素
     * @return 前k个高频元素的列表
     */
    std::vector<int> topKFrequentSort(const std::vector<int>& nums, int k) {
        // 异常处理
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("输入参数无效");
        }
        
        // 统计频率
        std::unordered_map<int, int> countMap;
        for (int num : nums) {
            countMap[num]++;
        }
        
        // 检查k是否大于不同元素的数量
        if (k > countMap.size()) {
            throw std::invalid_argument("k不能大于不同元素的数量");
        }
        
        // 创建一个向量存储元素和频率的映射
        std::vector<std::pair<int, int>> entryList;
        for (const auto& pair : countMap) {
            entryList.emplace_back(pair);
        }
        
        // 按照频率排序（降序）
        std::sort(entryList.begin(), entryList.end(), 
                 [](const std::pair<int, int>& a, const std::pair<int, int>& b) {
                     return a.second > b.second;
                 });
        
        // 提取前k个元素
        std::vector<int> result;
        for (int i = 0; i < k; i++) {
            result.push_back(entryList[i].first);
        }
        
        return result;
    }
};

/**
 * 打印向量函数，用于调试
 */
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
void testTopKFrequentElements() {
    std::cout << "=== 测试前K个高频元素算法 ===" << std::endl;
    Solution solution;
    AlternativeApproach alternative;
    
    // 测试用例1：基本用例
    std::cout << "\n测试用例1：基本用例" << std::endl;
    std::vector<int> nums1 = {1, 1, 1, 2, 2, 3};
    int k1 = 2;
    std::unordered_set<int> expected1({1, 2});  // 顺序可能不同
    
    std::vector<int> resultHeap1 = solution.topKFrequentHeap(nums1, k1);
    std::vector<int> resultBucket1 = solution.topKFrequentBucket(nums1, k1);
    std::vector<int> resultSort1 = alternative.topKFrequentSort(nums1, k1);
    
    std::cout << "最小堆实现: ";
    printVector(resultHeap1);
    std::cout << std::endl;
    
    std::cout << "桶排序实现: ";
    printVector(resultBucket1);
    std::cout << std::endl;
    
    std::cout << "排序实现: ";
    printVector(resultSort1);
    std::cout << std::endl;
    
    // 验证结果（不考虑顺序）
    std::unordered_set<int> heapSet(resultHeap1.begin(), resultHeap1.end());
    std::unordered_set<int> bucketSet(resultBucket1.begin(), resultBucket1.end());
    std::unordered_set<int> sortSet(resultSort1.begin(), resultSort1.end());
    
    bool isHeapCorrect = heapSet == expected1;
    bool isBucketCorrect = bucketSet == expected1;
    bool isSortCorrect = sortSet == expected1;
    
    std::cout << "最小堆实现结果 " << (isHeapCorrect ? "✓" : "✗") << std::endl;
    std::cout << "桶排序实现结果 " << (isBucketCorrect ? "✓" : "✗") << std::endl;
    std::cout << "排序实现结果 " << (isSortCorrect ? "✓" : "✗") << std::endl;
    
    // 测试用例2：所有元素出现频率相同
    std::cout << "\n测试用例2：所有元素出现频率相同" << std::endl;
    std::vector<int> nums2 = {1, 2, 3, 4, 5};
    int k2 = 3;
    
    std::vector<int> resultHeap2 = solution.topKFrequentHeap(nums2, k2);
    std::vector<int> resultBucket2 = solution.topKFrequentBucket(nums2, k2);
    
    std::cout << "最小堆实现: ";
    printVector(resultHeap2);
    std::cout << std::endl;
    
    std::cout << "桶排序实现: ";
    printVector(resultBucket2);
    std::cout << std::endl;
    
    std::cout << "结果长度正确: " << (resultHeap2.size() == k2 ? "✓" : "✗") << std::endl;
    
    // 测试用例3：单个元素
    std::cout << "\n测试用例3：单个元素" << std::endl;
    std::vector<int> nums3 = {1};
    int k3 = 1;
    std::vector<int> expected3 = {1};
    
    std::vector<int> resultHeap3 = solution.topKFrequentHeap(nums3, k3);
    std::vector<int> resultBucket3 = solution.topKFrequentBucket(nums3, k3);
    
    std::cout << "最小堆实现: ";
    printVector(resultHeap3);
    std::cout << ", 期望: ";
    printVector(expected3);
    std::cout << ", " << (resultHeap3 == expected3 ? "✓" : "✗") << std::endl;
    
    std::cout << "桶排序实现: ";
    printVector(resultBucket3);
    std::cout << ", 期望: ";
    printVector(expected3);
    std::cout << ", " << (resultBucket3 == expected3 ? "✓" : "✗") << std::endl;
    
    // 测试异常情况
    std::cout << "\n=== 测试异常情况 ===" << std::endl;
    try {
        solution.topKFrequentHeap({}, 2);
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    try {
        solution.topKFrequentBucket({1, 2, 3}, 5);
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    // 性能测试
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    // 测试大规模输入
    int n = 100000;
    std::vector<int> nums4(n);
    for (int i = 0; i < n; i++) {
        nums4[i] = i % 1000;  // 生成大规模数组，每个数字出现约100次
    }
    int k4 = 10;
    
    auto startTime = std::chrono::high_resolution_clock::now();
    std::vector<int> resultHeap = solution.topKFrequentHeap(nums4, k4);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto heapTime = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime).count();
    
    std::cout << "最小堆实现结果: ";
    printVector(resultHeap);
    std::cout << ", 用时: " << heapTime << "毫秒" << std::endl;
    
    startTime = std::chrono::high_resolution_clock::now();
    std::vector<int> resultBucket = solution.topKFrequentBucket(nums4, k4);
    endTime = std::chrono::high_resolution_clock::now();
    auto bucketTime = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime).count();
    
    std::cout << "桶排序实现结果: ";
    printVector(resultBucket);
    std::cout << ", 用时: " << bucketTime << "毫秒" << std::endl;
    
    startTime = std::chrono::high_resolution_clock::now();
    std::vector<int> resultSort = alternative.topKFrequentSort(nums4, k4);
    endTime = std::chrono::high_resolution_clock::now();
    auto sortTime = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime).count();
    
    std::cout << "排序实现结果: ";
    printVector(resultSort);
    std::cout << ", 用时: " << sortTime << "毫秒" << std::endl;
    
    std::cout << "\n性能比较:" << std::endl;
    std::cout << "最小堆 vs 桶排序: " << 
              (bucketTime < heapTime ? "桶排序更快" : "最小堆更快") << " 约 " << 
              static_cast<double>(std::max(heapTime, bucketTime)) / std::min(heapTime, bucketTime) << "倍" << std::endl;
    
    std::cout << "最小堆 vs 排序: " << 
              (sortTime < heapTime ? "排序更快" : "最小堆更快") << " 约 " << 
              static_cast<double>(std::max(heapTime, sortTime)) / std::min(heapTime, sortTime) << "倍" << std::endl;
    
    std::cout << "桶排序 vs 排序: " << 
              (sortTime < bucketTime ? "排序更快" : "桶排序更快") << " 约 " << 
              static_cast<double>(std::max(bucketTime, sortTime)) / std::min(bucketTime, sortTime) << "倍" << std::endl;
}

int main() {
    testTopKFrequentElements();
    return 0;
}

/*
 * 解题思路总结：
 * 1. 最小堆方法：
 *    - 统计每个元素的频率
 *    - 使用最小堆维护k个最高频率的元素
 *    - 遍历所有元素，保持堆的大小为k
 *    - 时间复杂度：O(n log k)，其中n是数组长度，k是要返回的元素数量
 *    - 空间复杂度：O(n)，需要存储所有元素的频率
 * 
 * 2. 桶排序方法：
 *    - 统计每个元素的频率
 *    - 创建桶，索引表示频率，值是具有该频率的元素列表
 *    - 从高频率到低频率遍历桶，收集元素直到达到k个
 *    - 时间复杂度：O(n)，线性时间
 *    - 空间复杂度：O(n)
 * 
 * 3. 排序方法：
 *    - 统计每个元素的频率
 *    - 按照频率排序
 *    - 取前k个元素
 *    - 时间复杂度：O(n log n)
 *    - 空间复杂度：O(n)
 * 
 * 4. 优化技巧：
 *    - 在C++中使用unordered_map快速统计频率
 *    - 使用priority_queue实现最小堆，需要自定义比较器
 *    - 桶排序在大多数情况下是最优的，尤其是当k较大时
 * 
 * 5. 应用场景：
 *    - 当需要获取一组元素中出现频率最高的k个元素时
 *    - 这种方法在数据分析、文本处理、推荐系统等领域有广泛应用
 * 
 * 6. 边界情况处理：
 *    - 空数组
 *    - k为0或大于不同元素的数量
 *    - 所有元素频率相同的情况
 *    - 单个元素的情况
 */
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <random>
#include <chrono>
#include <stdexcept>

/**
 * 相关题目26: LeetCode 215. 数组中的第K个最大元素
 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 * 解题思路1: 使用最小堆维护前k个最大元素
 * 解题思路2: 使用快速选择算法
 * 时间复杂度: 最小堆O(n log k)，快速选择平均O(n)，最坏O(n²)
 * 空间复杂度: 最小堆O(k)，快速选择O(1)（原地版本）
 * 是否最优解: 快速选择算法在平均情况下是最优解，但堆方法更为稳定
 * 
 * 本题属于堆的应用场景：Top K问题，特别是需要高效获取第k个最大元素
 */

class Solution {
public:
    /**
     * 使用最小堆实现查找数组中的第K个最大元素
     * 
     * @param nums 整数数组
     * @param k 要查找的第k个最大元素的位置
     * @return 数组中第k个最大的元素
     * @throws std::invalid_argument 当输入参数无效时抛出异常
     */
    int findKthLargestHeap(std::vector<int>& nums, int k) {
        // 异常处理：检查nums和k是否有效
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("输入参数无效");
        }
        
        // 使用最小堆，保持堆的大小为k
        std::priority_queue<int, std::vector<int>, std::greater<int>> minHeap;
        
        // 遍历数组中的每个元素
        for (int num : nums) {
            // 如果堆的大小小于k，直接添加
            if (minHeap.size() < k) {
                minHeap.push(num);
            }
            // 否则，如果当前元素大于堆顶元素，替换堆顶元素
            else if (num > minHeap.top()) {
                minHeap.pop();
                minHeap.push(num);
            }
        }
        
        // 堆顶元素就是第k个最大的元素
        return minHeap.top();
    }
    
    /**
     * 使用排序实现查找数组中的第K个最大元素（简单方法作为对比）
     * 
     * @param nums 整数数组
     * @param k 要查找的第k个最大元素的位置
     * @return 数组中第k个最大的元素
     */
    int findKthLargestSort(std::vector<int>& nums, int k) {
        // 异常处理
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("输入参数无效");
        }
        
        // 排序数组
        std::sort(nums.begin(), nums.end());
        
        // 返回第k个最大的元素（数组是升序排列，所以第k个最大元素的索引是nums.size() - k）
        return nums[nums.size() - k];
    }
};

class QuickSelectSolution {
private:
    std::mt19937 rng; // 用于随机选择基准元素
    
    /**
     * 交换数组中的两个元素
     * 
     * @param nums 整数数组
     * @param i 第一个元素的索引
     * @param j 第二个元素的索引
     */
    void swap(std::vector<int>& nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    /**
     * 分区函数：选择一个基准元素，将小于基准的元素放在左边，大于基准的元素放在右边
     * 
     * @param nums 整数数组
     * @param left 子数组的左边界
     * @param right 子数组的右边界
     * @return 基准元素的最终位置
     */
    int partition(std::vector<int>& nums, int left, int right) {
        // 随机选择一个元素作为基准，避免最坏情况
        std::uniform_int_distribution<int> dist(left, right);
        int pivotIndex = dist(rng);
        // 将基准元素交换到末尾
        swap(nums, pivotIndex, right);
        
        // 基准元素的值
        int pivot = nums[right];
        
        // i表示小于基准元素的区域的边界
        int i = left;
        
        // 遍历区间内的元素
        for (int j = left; j < right; j++) {
            // 如果当前元素小于基准元素，将其交换到小于区域
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        
        // 将基准元素交换到正确的位置
        swap(nums, i, right);
        
        // 返回基准元素的索引
        return i;
    }
    
    /**
     * 快速选择的核心实现
     * 
     * @param nums 整数数组
     * @param left 当前子数组的左边界
     * @param right 当前子数组的右边界
     * @param targetIndex 目标索引（0-indexed的第targetIndex小的元素）
     * @return 目标索引处的元素
     */
    int quickSelect(std::vector<int>& nums, int left, int right, int targetIndex) {
        // 如果区间只有一个元素，直接返回
        if (left == right) {
            return nums[left];
        }
        
        // 分区并获取基准元素的索引
        int pivotIndex = partition(nums, left, right);
        
        // 根据基准元素的位置决定下一步搜索的区间
        if (pivotIndex == targetIndex) {
            // 找到目标元素
            return nums[pivotIndex];
        } else if (pivotIndex < targetIndex) {
            // 在右半部分继续搜索
            return quickSelect(nums, pivotIndex + 1, right, targetIndex);
        } else {
            // 在左半部分继续搜索
            return quickSelect(nums, left, pivotIndex - 1, targetIndex);
        }
    }
    
public:
    QuickSelectSolution() {
        // 使用当前时间作为随机数种子
        std::random_device rd;
        rng = std::mt19937(rd());
    }
    
    /**
     * 使用快速选择算法查找数组中的第K个最大元素
     * 
     * @param nums 整数数组
     * @param k 要查找的第k个最大元素的位置
     * @return 数组中第k个最大元素
     */
    int findKthLargest(std::vector<int>& nums, int k) {
        // 异常处理
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("输入参数无效");
        }
        
        // 我们需要找的是第k大的元素，转换为在0-indexed数组中查找第(len(nums)-k)小的元素
        int targetIndex = nums.size() - k;
        
        // 调用快速选择函数
        return quickSelect(nums, 0, nums.size() - 1, targetIndex);
    }
};

class OptimizedHeapSolution {
public:
    /**
     * 优化的堆实现，使用C++的priority_queue
     * 
     * @param nums 整数数组
     * @param k 要查找的第k个最大元素的位置
     * @return 数组中第k个最大元素
     */
    int findKthLargest(std::vector<int>& nums, int k) {
        // 异常处理
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("输入参数无效");
        }
        
        // 创建一个最小堆，大小为k
        std::priority_queue<int, std::vector<int>, std::greater<int>> minHeap;
        
        // 添加前k个元素
        for (int i = 0; i < k; i++) {
            minHeap.push(nums[i]);
        }
        
        // 对于剩余元素，如果大于堆顶，则替换堆顶
        for (int i = k; i < nums.size(); i++) {
            if (nums[i] > minHeap.top()) {
                minHeap.pop();
                minHeap.push(nums[i]);
            }
        }
        
        // 堆顶即为第k个最大元素
        return minHeap.top();
    }
};

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
void testFindKthLargest() {
    std::cout << "=== 测试数组中的第K个最大元素算法 ===" << std::endl;
    Solution solution;
    QuickSelectSolution quickSelectSolution;
    OptimizedHeapSolution optimizedSolution;
    
    // 测试用例1：基本用例
    std::cout << "\n测试用例1：基本用例" << std::endl;
    std::vector<int> nums1 = {3, 2, 1, 5, 6, 4};
    int k1 = 2;
    int expected1 = 5;
    
    std::vector<int> nums1Copy1 = nums1;
    std::vector<int> nums1Copy2 = nums1;
    std::vector<int> nums1Copy3 = nums1;
    std::vector<int> nums1Copy4 = nums1;
    
    int resultHeap1 = solution.findKthLargestHeap(nums1Copy1, k1);
    int resultSort1 = solution.findKthLargestSort(nums1Copy2, k1);
    int resultQuickSelect1 = quickSelectSolution.findKthLargest(nums1Copy3, k1);
    int resultOptimized1 = optimizedSolution.findKthLargest(nums1Copy4, k1);
    
    std::cout << "最小堆实现: " << resultHeap1 << ", 期望: " << expected1 << ", " 
              << (resultHeap1 == expected1 ? "✓" : "✗") << std::endl;
    std::cout << "排序实现: " << resultSort1 << ", 期望: " << expected1 << ", " 
              << (resultSort1 == expected1 ? "✓" : "✗") << std::endl;
    std::cout << "快速选择实现: " << resultQuickSelect1 << ", 期望: " << expected1 << ", " 
              << (resultQuickSelect1 == expected1 ? "✓" : "✗") << std::endl;
    std::cout << "优化堆实现: " << resultOptimized1 << ", 期望: " << expected1 << ", " 
              << (resultOptimized1 == expected1 ? "✓" : "✗") << std::endl;
    
    // 测试用例2：有重复元素
    std::cout << "\n测试用例2：有重复元素" << std::endl;
    std::vector<int> nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
    int k2 = 4;
    int expected2 = 4;
    
    std::vector<int> nums2Copy1 = nums2;
    std::vector<int> nums2Copy2 = nums2;
    
    int resultHeap2 = solution.findKthLargestHeap(nums2Copy1, k2);
    int resultQuickSelect2 = quickSelectSolution.findKthLargest(nums2Copy2, k2);
    
    std::cout << "最小堆实现: " << resultHeap2 << ", 期望: " << expected2 << ", " 
              << (resultHeap2 == expected2 ? "✓" : "✗") << std::endl;
    std::cout << "快速选择实现: " << resultQuickSelect2 << ", 期望: " << expected2 << ", " 
              << (resultQuickSelect2 == expected2 ? "✓" : "✗") << std::endl;
    
    // 测试用例3：单元素数组
    std::cout << "\n测试用例3：单元素数组" << std::endl;
    std::vector<int> nums3 = {1};
    int k3 = 1;
    int expected3 = 1;
    
    std::vector<int> nums3Copy1 = nums3;
    std::vector<int> nums3Copy2 = nums3;
    
    int resultHeap3 = solution.findKthLargestHeap(nums3Copy1, k3);
    int resultQuickSelect3 = quickSelectSolution.findKthLargest(nums3Copy2, k3);
    
    std::cout << "最小堆实现: " << resultHeap3 << ", 期望: " << expected3 << ", " 
              << (resultHeap3 == expected3 ? "✓" : "✗") << std::endl;
    std::cout << "快速选择实现: " << resultQuickSelect3 << ", 期望: " << expected3 << ", " 
              << (resultQuickSelect3 == expected3 ? "✓" : "✗") << std::endl;
    
    // 测试用例4：倒序数组
    std::cout << "\n测试用例4：倒序数组" << std::endl;
    std::vector<int> nums4 = {6, 5, 4, 3, 2, 1};
    int k4 = 3;
    int expected4 = 4;
    
    std::vector<int> nums4Copy1 = nums4;
    std::vector<int> nums4Copy2 = nums4;
    
    int resultHeap4 = solution.findKthLargestHeap(nums4Copy1, k4);
    int resultQuickSelect4 = quickSelectSolution.findKthLargest(nums4Copy2, k4);
    
    std::cout << "最小堆实现: " << resultHeap4 << ", 期望: " << expected4 << ", " 
              << (resultHeap4 == expected4 ? "✓" : "✗") << std::endl;
    std::cout << "快速选择实现: " << resultQuickSelect4 << ", 期望: " << expected4 << ", " 
              << (resultQuickSelect4 == expected4 ? "✓" : "✗") << std::endl;
    
    // 测试异常情况
    std::cout << "\n=== 测试异常情况 ===" << std::endl;
    try {
        std::vector<int> emptyNums;
        solution.findKthLargestHeap(emptyNums, 1);
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    try {
        std::vector<int> smallNums = {1, 2, 3};
        quickSelectSolution.findKthLargest(smallNums, 5);
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    // 性能测试
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    // 测试大规模输入
    int n = 1000000;
    std::vector<int> nums5(n);
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<int> dist(0, 1000000);
    for (int i = 0; i < n; i++) {
        nums5[i] = dist(gen);
    }
    int k5 = 500000; // 查找第50万个最大元素
    
    // 最小堆实现
    auto start = std::chrono::high_resolution_clock::now();
    std::vector<int> nums5Copy1 = nums5;
    int resultHeap = solution.findKthLargestHeap(nums5Copy1, k5);
    auto end = std::chrono::high_resolution_clock::now();
    auto heapTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "最小堆实现结果: " << resultHeap << ", 用时: " << heapTime << "毫秒" << std::endl;
    
    // 快速选择实现
    start = std::chrono::high_resolution_clock::now();
    std::vector<int> nums5Copy2 = nums5;
    int resultQuickSelect = quickSelectSolution.findKthLargest(nums5Copy2, k5);
    end = std::chrono::high_resolution_clock::now();
    auto quickSelectTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "快速选择实现结果: " << resultQuickSelect << ", 用时: " << quickSelectTime << "毫秒" << std::endl;
    
    // 优化堆实现
    start = std::chrono::high_resolution_clock::now();
    std::vector<int> nums5Copy3 = nums5;
    int resultOptimized = optimizedSolution.findKthLargest(nums5Copy3, k5);
    end = std::chrono::high_resolution_clock::now();
    auto optimizedTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "优化堆实现结果: " << resultOptimized << ", 用时: " << optimizedTime << "毫秒" << std::endl;
    
    // 排序实现（对于大数组可能较慢）
    if (n <= 100000) { // 对于太大的数组，排序可能会很慢，所以只测试较小的数组
        start = std::chrono::high_resolution_clock::now();
        std::vector<int> nums5Copy4 = nums5;
        int resultSort = solution.findKthLargestSort(nums5Copy4, k5);
        end = std::chrono::high_resolution_clock::now();
        auto sortTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
        std::cout << "排序实现结果: " << resultSort << ", 用时: " << sortTime << "毫秒" << std::endl;
    } else {
        std::cout << "排序实现：对于大规模数据，排序实现可能较慢，跳过测试" << std::endl;
    }
    
    // 验证所有方法结果一致
    bool isConsistent = resultHeap == resultQuickSelect && resultHeap == resultOptimized;
    std::cout << "\n结果一致性检查: " << (isConsistent ? "✓" : "✗") << std::endl;
    
    // 性能比较
    std::cout << "\n性能比较:" << std::endl;
    std::cout << "最小堆 vs 快速选择: " 
              << (quickSelectTime < heapTime ? "快速选择更快" : "最小堆更快") << " 约 " 
              << static_cast<double>(std::max(heapTime, quickSelectTime)) / std::min(heapTime, quickSelectTime) 
              << "倍" << std::endl;
    std::cout << "最小堆 vs 优化堆: " 
              << (optimizedTime < heapTime ? "优化堆更快" : "最小堆更快") << " 约 " 
              << static_cast<double>(std::max(heapTime, optimizedTime)) / std::min(heapTime, optimizedTime) 
              << "倍" << std::endl;
}

int main() {
    testFindKthLargest();
    return 0;
}

/*
 * 解题思路总结：
 * 1. 最小堆方法：
 *    - 维护一个大小为k的最小堆
 *    - 遍历数组，保持堆中有k个最大的元素
 *    - 堆顶元素即为第k个最大元素
 *    - 时间复杂度：O(n log k)，其中n是数组长度，k是要找的第k大元素的位置
 *    - 空间复杂度：O(k)
 * 
 * 2. 快速选择算法：
 *    - 基于快速排序的思想，但只需要递归处理一半的区间
 *    - 平均时间复杂度为O(n)，最坏情况为O(n²)（但通过随机选择基准元素可以避免最坏情况）
 *    - 空间复杂度：O(log n)（递归调用栈的空间），原地版本可以达到O(1)
 * 
 * 3. 排序方法：
 *    - 对数组进行排序，然后返回第k-1个索引的元素
 *    - 时间复杂度：O(n log n)
 *    - 空间复杂度：O(1)（原地排序）或O(n)（需要额外空间的排序）
 * 
 * 4. 优化技巧：
 *    - 在C++中，使用priority_queue实现最小堆，并指定greater<int>作为比较函数
 *    - 快速选择算法中使用随机选择基准元素可以避免最坏情况
 *    - 对于非常大的k值（接近n），可以考虑找第(n-k+1)小的元素，可能更高效
 * 
 * 5. 应用场景：
 *    - 当需要找到数组中第k个最大元素时
 *    - 这种方法在数据分析、统计等领域有广泛应用
 * 
 * 6. 边界情况处理：
 *    - 空数组
 *    - k为0或大于数组长度
 *    - 单元素数组
 *    - 所有元素都相同的数组
 *    - 已排序或接近排序的数组
 */
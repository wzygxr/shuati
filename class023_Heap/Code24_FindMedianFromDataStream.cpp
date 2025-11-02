#include <iostream>
#include <queue>
#include <vector>
#include <stdexcept>
#include <chrono>
#include <cmath>

/**
 * 相关题目24: LeetCode 295. 数据流的中位数
 * 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
 * 题目描述: 设计一个支持以下两种操作的数据结构：
 * - void addNum(int num) - 从数据流中添加一个整数到数据结构中
 * - double findMedian() - 返回目前所有元素的中位数
 * 解题思路: 使用两个堆（最大堆和最小堆）维护数据流的中位数
 * 时间复杂度: addNum() O(log n)，findMedian() O(1)
 * 空间复杂度: O(n)
 * 是否最优解: 是，这种解法在时间和空间上都是最优的
 */

class MedianFinder {
private:
    // 最大堆存储较小的一半元素
    std::priority_queue<int> maxHeap; // 存储较小的一半元素
    // 最小堆存储较大的一半元素
    std::priority_queue<int, std::vector<int>, std::greater<int>> minHeap; // 存储较大的一半元素
    
public:
    /**
     * 初始化数据结构
     * 使用最大堆存储较小的一半元素，最小堆存储较大的一半元素
     * 确保最大堆的大小等于或比最小堆大1
     */
    MedianFinder() {
        // 默认初始化两个堆
    }
    
    /**
     * 向数据结构中添加一个整数
     * 
     * @param num 要添加的整数
     */
    void addNum(int num) {
        // 首先将元素添加到最大堆中
        maxHeap.push(num);
        
        // 确保最大堆顶元素（较小一半中的最大值）不大于最小堆顶元素（较大一半中的最小值）
        if (!minHeap.empty() && maxHeap.top() > minHeap.top()) {
            // 将最大堆顶元素移动到最小堆
            int maxValue = maxHeap.top();
            maxHeap.pop();
            minHeap.push(maxValue);
        }
        
        // 平衡两个堆的大小，确保最大堆的大小等于或比最小堆大1
        // 如果最大堆比最小堆大超过1，则移动一个元素到最小堆
        if (maxHeap.size() > minHeap.size() + 1) {
            int maxValue = maxHeap.top();
            maxHeap.pop();
            minHeap.push(maxValue);
        }
        // 如果最小堆比最大堆大，则移动一个元素到最大堆
        else if (minHeap.size() > maxHeap.size()) {
            int minValue = minHeap.top();
            minHeap.pop();
            maxHeap.push(minValue);
        }
    }
    
    /**
     * 返回目前所有元素的中位数
     * 
     * @return 当前所有元素的中位数
     * @throws std::runtime_error 当没有元素时抛出异常
     */
    double findMedian() {
        // 如果没有元素，抛出异常
        if (maxHeap.empty() && minHeap.empty()) {
            throw std::runtime_error("没有元素，无法计算中位数");
        }
        
        // 如果最大堆的大小比最小堆大1，则中位数是最大堆的堆顶元素
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.top();
        }
        // 否则，中位数是两个堆顶元素的平均值
        else {
            return (maxHeap.top() + minHeap.top()) / 2.0;
        }
    }
};

/**
 * AlternativeApproach类：使用更简洁的方式实现两个堆的平衡
 */
class AlternativeApproach {
private:
    std::priority_queue<int> small; // 最大堆（存储较小的一半元素）
    std::priority_queue<int, std::vector<int>, std::greater<int>> large; // 最小堆（存储较大的一半元素）
    
public:
    /**
     * 初始化数据结构
     */
    AlternativeApproach() {
        // 默认初始化两个堆
    }
    
    /**
     * 更简洁的添加元素实现
     * 
     * @param num 要添加的整数
     */
    void addNum(int num) {
        // 先添加到small堆，然后将small堆的最大值移到large堆
        small.push(num);
        
        // 确保small堆的最大值不大于large堆的最小值
        if (!small.empty() && !large.empty() && small.top() > large.top()) {
            int val = small.top();
            small.pop();
            large.push(val);
        }
        
        // 平衡两个堆的大小
        if (small.size() > large.size() + 1) {
            int val = small.top();
            small.pop();
            large.push(val);
        }
        if (large.size() > small.size()) {
            int val = large.top();
            large.pop();
            small.push(val);
        }
    }
    
    /**
     * 返回中位数
     * 
     * @return 当前所有元素的中位数
     */
    double findMedian() {
        if (small.size() > large.size()) {
            return small.top();
        }
        return (small.top() + large.top()) / 2.0;
    }
};

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
void testMedianFinder() {
    std::cout << "=== 测试数据流的中位数算法 ===" << std::endl;
    
    // 测试基本实现
    std::cout << "\n=== 测试基本实现 ===" << std::endl;
    MedianFinder medianFinder;
    
    // 测试用例1：添加元素并计算中位数
    std::cout << "测试用例1：添加元素并计算中位数" << std::endl;
    std::vector<int> nums1 = {1, 2, 3, 4, 5, 6};
    std::vector<double> expectedResults = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5};
    
    for (size_t i = 0; i < nums1.size(); i++) {
        medianFinder.addNum(nums1[i]);
        double median = medianFinder.findMedian();
        std::cout << "当前中位数: " << median << std::endl;
        
        if (std::abs(median - expectedResults[i]) > 1e-9) {
            std::cout << "测试用例1 第" << (i+1) << "步失败: 期望" << expectedResults[i] 
                      << ", 实际" << median << std::endl;
        }
    }
    std::cout << "测试用例1 完成 ✓" << std::endl;
    
    // 测试用例2：负数和零
    std::cout << "\n测试用例2：负数和零" << std::endl;
    MedianFinder medianFinder2;
    medianFinder2.addNum(-1);
    medianFinder2.addNum(0);
    medianFinder2.addNum(-2);
    
    double result2 = medianFinder2.findMedian();
    double expected2 = -1.0;
    std::cout << "当前中位数: " << result2 << ", 期望: " << expected2 << ", " 
              << (std::abs(result2 - expected2) < 1e-9 ? "✓" : "✗") << std::endl;
    
    // 测试用例3：重复元素
    std::cout << "\n测试用例3：重复元素" << std::endl;
    MedianFinder medianFinder3;
    for (int i = 0; i < 5; i++) {
        medianFinder3.addNum(2);
    }
    
    double result3 = medianFinder3.findMedian();
    double expected3 = 2.0;
    std::cout << "当前中位数: " << result3 << ", 期望: " << expected3 << ", " 
              << (std::abs(result3 - expected3) < 1e-9 ? "✓" : "✗") << std::endl;
    
    // 测试异常情况
    std::cout << "\n=== 测试异常情况 ===" << std::endl;
    MedianFinder medianFinder4;
    try {
        medianFinder4.findMedian();
        std::cout << "异常测试失败：未抛出预期的异常" << std::endl;
    } catch (const std::runtime_error& e) {
        std::cout << "异常测试通过: " << e.what() << std::endl;
    }
    
    // 测试替代实现
    std::cout << "\n=== 测试替代实现 ===" << std::endl;
    AlternativeApproach altFinder;
    
    for (int num : std::vector<int>{1, 2, 3, 4, 5}) {
        altFinder.addNum(num);
    }
    
    double resultAlt = altFinder.findMedian();
    double expectedAlt = 3.0;
    std::cout << "替代实现中位数: " << resultAlt << ", 期望: " << expectedAlt << ", " 
              << (std::abs(resultAlt - expectedAlt) < 1e-9 ? "✓" : "✗") << std::endl;
    
    // 性能测试
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    // 测试大规模输入
    MedianFinder largeFinder;
    int n = 100000;
    
    auto startTime = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        largeFinder.addNum(i);
    }
    double median = largeFinder.findMedian();
    auto endTime = std::chrono::high_resolution_clock::now();
    
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime).count();
    std::cout << "添加" << n << "个元素后中位数: " << median << std::endl;
    std::cout << "总耗时: " << duration << "毫秒" << std::endl;
    std::cout << "平均每个操作耗时: " << static_cast<double>(duration) / n * 1000 << "微秒" << std::endl;
}

int main() {
    testMedianFinder();
    return 0;
}

/*
 * 解题思路总结：
 * 1. 双堆方法：
 *    - 使用最大堆存储较小的一半元素，最小堆存储较大的一半元素
 *    - 维护两个堆的大小关系，确保最大堆的大小等于或比最小堆大1
 *    - 这样，如果元素总数是奇数，中位数就是最大堆的堆顶；如果是偶数，中位数是两个堆顶的平均值
 *    - 时间复杂度：addNum() O(log n)，findMedian() O(1)
 *    - 空间复杂度：O(n)
 * 
 * 2. 优化技巧：
 *    - 在C++中，使用std::priority_queue实现堆，默认是最大堆
 *    - 对于最小堆，需要使用比较器std::greater<int>
 *    - 注意添加元素后的平衡调整步骤，确保两个堆的大小关系和元素有序性
 *    - 使用更简洁的实现方式可以减少代码行数，但核心逻辑保持不变
 * 
 * 3. 应用场景：
 *    - 当需要频繁地从动态变化的数据集中获取中位数时，双堆方法是一个很好的选择
 *    - 这种方法在金融数据分析、实时统计等场景中非常有用
 * 
 * 4. 边界情况处理：
 *    - 空数据集时抛出std::runtime_error异常
 *    - 处理负数和零的情况
 *    - 处理重复元素的情况
 * 
 * 5. C++实现注意事项：
 *    - 使用int类型存储整数，但需要注意溢出问题（本题输入范围适合int）
 *    - 使用std::abs和误差范围比较浮点数的相等性
 *    - 使用std::chrono库进行性能测量
 *    - 使用std::vector存储测试数据
 */
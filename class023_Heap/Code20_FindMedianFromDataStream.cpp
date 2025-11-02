#include <iostream>
#include <vector>
#include <queue>
#include <stdexcept>
using namespace std;

/**
 * 相关题目12: LeetCode 295. 数据流的中位数
 * 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
 * 题目描述: 设计一个支持以下两种操作的数据结构：
 * 1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
 * 2. double findMedian() - 返回目前所有元素的中位数
 * 解题思路: 使用两个堆维护数据流：最大堆存储较小的一半元素，最小堆存储较大的一半元素
 * 时间复杂度: addNum() O(log n)，findMedian() O(1)
 * 空间复杂度: O(n)，其中n是数据流中的元素个数
 * 是否最优解: 是，这是求解数据流中位数的最优解法之一
 * 
 * 本题属于堆的典型应用场景：需要动态维护数据的中间值
 */

/**
 * MedianFinder类：支持添加元素和查找中位数的数据结构
 */
class MedianFinder {
private:
    // 最大堆存储较小的一半元素
    priority_queue<int> maxHeap; 
    // 最小堆存储较大的一半元素
    priority_queue<int, vector<int>, greater<int>> minHeap; 
    
public:
    /**
     * 初始化数据结构
     */
    MedianFinder() {
        // 构造函数，默认初始化两个堆
    }
    
    /**
     * 从数据流中添加一个整数到数据结构中
     * @param num 要添加的整数
     */
    void addNum(int num) {
        // 策略：保持两个堆的平衡，使maxHeap的大小等于minHeap或比minHeap大1
        
        // 先将num加入到maxHeap中
        maxHeap.push(num);
        
        // 然后将maxHeap的最大值转移到minHeap中，确保maxHeap中的所有元素都小于或等于minHeap中的所有元素
        minHeap.push(maxHeap.top());
        maxHeap.pop();
        
        // 如果minHeap的大小超过maxHeap，则将minHeap的最小值转移到maxHeap中
        // 这样可以保证maxHeap的大小等于minHeap或比minHeap大1
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.push(minHeap.top());
            minHeap.pop();
        }
    }
    
    /**
     * 返回目前所有元素的中位数
     * @return 中位数
     * @throws runtime_error 当没有元素时抛出异常
     */
    double findMedian() {
        // 异常处理：当没有元素时抛出异常
        if (maxHeap.empty()) {
            throw runtime_error("没有元素，无法计算中位数");
        }
        
        // 如果maxHeap的大小大于minHeap，说明总共有奇数个元素，中位数就是maxHeap的堆顶
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.top();
        } else {
            // 如果maxHeap和minHeap的大小相等，说明总共有偶数个元素，中位数是两个堆顶的平均值
            return (maxHeap.top() + minHeap.top()) / 2.0;
        }
    }
    
    /**
     * 获取当前存储的元素数量
     * @return 元素数量
     */
    int size() {
        return maxHeap.size() + minHeap.size();
    }
};

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    // 测试用例1：基本操作
    cout << "测试用例1：" << endl;
    MedianFinder medianFinder1;
    
    // 添加元素并打印中位数
    medianFinder1.addNum(1);
    cout << "添加1后，中位数 = " << medianFinder1.findMedian() << endl; // 期望输出: 1.0
    
    medianFinder1.addNum(2);
    cout << "添加2后，中位数 = " << medianFinder1.findMedian() << endl; // 期望输出: 1.5
    
    medianFinder1.addNum(3);
    cout << "添加3后，中位数 = " << medianFinder1.findMedian() << endl; // 期望输出: 2.0
    
    medianFinder1.addNum(4);
    cout << "添加4后，中位数 = " << medianFinder1.findMedian() << endl; // 期望输出: 2.5
    
    medianFinder1.addNum(5);
    cout << "添加5后，中位数 = " << medianFinder1.findMedian() << endl; // 期望输出: 3.0
    
    // 测试用例2：无序输入
    cout << "\n测试用例2：" << endl;
    MedianFinder medianFinder2;
    vector<int> nums = {5, 2, 8, 4, 1, 9, 3, 6, 7};
    
    for (int num : nums) {
        medianFinder2.addNum(num);
        cout << "添加" << num << "后，中位数 = " << medianFinder2.findMedian() << endl;
    }
    
    // 测试用例3：负数和零
    cout << "\n测试用例3：" << endl;
    MedianFinder medianFinder3;
    vector<int> numsWithNegatives = {-1, 0, 5, -10, 2, 7};
    
    for (int num : numsWithNegatives) {
        medianFinder3.addNum(num);
        cout << "添加" << num << "后，中位数 = " << medianFinder3.findMedian() << endl;
    }
    
    // 测试异常情况
    cout << "\n测试异常情况：" << endl;
    MedianFinder emptyMedianFinder;
    try {
        emptyMedianFinder.findMedian();
        cout << "异常测试失败：未抛出预期的异常" << endl;
    } catch (const runtime_error& e) {
        cout << "异常测试通过: " << e.what() << endl;
    }
    
    return 0;
}
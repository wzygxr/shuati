#include <iostream>
#include <vector>
#include <queue>
using namespace std;

/**
 * 相关题目7: LeetCode 703. 数据流中的第K大元素
 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 * 题目描述: 设计一个找到数据流中第K大元素的类。注意是排序后的第K大元素，不是第K个不同的元素。
 * 实现 KthLargest 类:
 * 1. KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象
 * 2. int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第K大的元素
 * 解题思路: 使用最小堆维护当前最大的k个元素，堆顶就是第k大的元素
 * 时间复杂度: add() - O(log k)，初始化 - O(n log k)
 * 空间复杂度: O(k)，堆最多存储k个元素
 * 是否最优解: 是，这是解决数据流中第K大元素问题的最优解法
 * 
 * 本题属于堆的典型应用场景：需要在动态数据中快速获取第K大元素
 */
class KthLargest {
private:
    // 最小堆，用于存储当前最大的k个元素
    priority_queue<int, vector<int>, greater<int>> minHeap;
    // 需要找的是第k大的元素
    int k;
    
public:
    /**
     * 使用整数k和整数流nums初始化对象
     * @param k 需要找的第k大元素
     * @param nums 初始整数流
     * @throws invalid_argument 当输入参数无效时抛出异常
     */
    KthLargest(int k, vector<int>& nums) {
        // 异常处理：检查k是否为正整数
        if (k <= 0) {
            throw invalid_argument("k必须是正整数");
        }
        
        this->k = k;
        
        // 将初始数组中的元素添加到堆中
        for (int num : nums) {
            add(num);
        }
    }
    
    /**
     * 将val插入数据流nums后，返回当前数据流中第K大的元素
     * @param val 需要添加的新值
     * @return 当前数据流中第K大的元素
     */
    int add(int val) {
        // 调试信息：打印当前堆的状态和要添加的值
        // cout << "添加值: " << val << endl;
        
        if (minHeap.size() < k) {
            // 如果堆的大小小于k，直接将元素加入堆
            minHeap.push(val);
        } else if (val > minHeap.top()) {
            // 如果当前值大于堆顶元素（堆中最小的元素）
            // 则移除堆顶元素，加入新值
            minHeap.pop();
            minHeap.push(val);
        }
        // 否则，不做任何操作，因为这个值不影响第k大元素的结果
        
        // 堆顶就是第k大的元素
        return minHeap.top();
    }
    
    /**
     * 打印当前堆内容的辅助方法（用于调试）
     */
    void printHeap() {
        priority_queue<int, vector<int>, greater<int>> tempHeap = minHeap;
        cout << "当前堆内容: ";
        while (!tempHeap.empty()) {
            cout << tempHeap.top() << " ";
            tempHeap.pop();
        }
        cout << endl;
    }
};

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    // 测试用例1：基本情况
    try {
        int k1 = 3;
        vector<int> nums1 = {4, 5, 8, 2};
        KthLargest kthLargest1(k1, nums1);
        cout << "测试用例1:" << endl;
        cout << "添加3后，第3大元素: " << kthLargest1.add(3) << endl;  // 返回 4
        cout << "添加5后，第3大元素: " << kthLargest1.add(5) << endl;  // 返回 5
        cout << "添加10后，第3大元素: " << kthLargest1.add(10) << endl;  // 返回 5
        cout << "添加9后，第3大元素: " << kthLargest1.add(9) << endl;  // 返回 8
        cout << "添加4后，第3大元素: " << kthLargest1.add(4) << endl;  // 返回 8
        
        // 测试用例2：初始数组为空
        int k2 = 1;
        vector<int> nums2 = {};
        KthLargest kthLargest2(k2, nums2);
        cout << "\n测试用例2:" << endl;
        cout << "空数组，添加-3后，第1大元素: " << kthLargest2.add(-3) << endl;  // 返回 -3
        cout << "添加-2后，第1大元素: " << kthLargest2.add(-2) << endl;  // 返回 -2
        cout << "添加-4后，第1大元素: " << kthLargest2.add(-4) << endl;  // 返回 -2
        cout << "添加0后，第1大元素: " << kthLargest2.add(0) << endl;  // 返回 0
        cout << "添加4后，第1大元素: " << kthLargest2.add(4) << endl;  // 返回 4
        
        // 测试用例3：边界情况 - 数组元素个数等于k
        int k3 = 2;
        vector<int> nums3 = {1, 2};
        KthLargest kthLargest3(k3, nums3);
        cout << "\n测试用例3:" << endl;
        cout << "数组元素个数等于k，添加0后，第2大元素: " << kthLargest3.add(0) << endl;  // 返回 1
        
        // 测试异常情况
        cout << "\n测试异常情况:" << endl;
        try {
            vector<int> nums4 = {1, 2, 3};
            KthLargest kthLargest4(0, nums4);  // k=0是无效的
            cout << "异常测试失败：未抛出预期的异常" << endl;
        } catch (const invalid_argument& e) {
            cout << "异常测试通过: " << e.what() << endl;
        }
        
    } catch (const exception& e) {
        cout << "发生异常: " << e.what() << endl;
    }
    
    return 0;
}
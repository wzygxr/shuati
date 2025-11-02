#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;

/**
 * 滑动窗口最大值 - C++实现
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 测试链接：https://leetcode.cn/problems/sliding-window-maximum/
 * 题目来源：LeetCode
 * 难度：困难
 * 
 * 核心算法：单调队列（双端队列实现）
 * 
 * 解题思路：
 * 使用单调递减双端队列来维护当前窗口中的最大值候选者。
 * 队列中存储的是数组元素的索引，而不是元素值本身，这样可以方便判断元素是否在窗口内。
 * 
 * 具体步骤：
 * 1. 初始化一个双端队列用于存储索引
 * 2. 遍历数组中的每个元素：
 *    a. 移除队列中不在当前窗口范围内的索引（从队首移除）
 *    b. 从队尾开始移除所有小于当前元素的索引，保持队列单调递减
 *    c. 将当前元素索引入队
 *    d. 当窗口形成时（i >= k-1），记录当前窗口的最大值（队首元素）
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队和出队各一次，n为数组长度
 * 
 * 空间复杂度分析：
 * O(k) - 队列最多存储k个元素
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 * 
 * C++语言特性：
 * - 使用deque容器实现双端队列
 * - 使用vector存储结果
 * - 使用auto关键字简化迭代器声明
 * - 使用chrono库进行性能测试
 */
class Code16_SlidingWindowMaximum {
public:
    vector<int> maxSlidingWindow(vector<int>& nums, int k) {
        // 边界条件检查
        if (nums.empty() || k <= 0 || k > nums.size()) {
            return vector<int>();
        }
        
        int n = nums.size();
        vector<int> result;
        result.reserve(n - k + 1); // 预分配空间提高性能
        
        // 使用双端队列存储索引，维护单调递减队列
        deque<int> dq;
        
        for (int i = 0; i < n; i++) {
            // 步骤1：移除队列中不在当前窗口范围内的索引
            while (!dq.empty() && dq.front() < i - k + 1) {
                dq.pop_front();
            }
            
            // 步骤2：从队尾开始移除所有小于当前元素的索引
            while (!dq.empty() && nums[dq.back()] < nums[i]) {
                dq.pop_back();
            }
            
            // 步骤3：将当前索引入队
            dq.push_back(i);
            
            // 步骤4：当窗口形成时，记录当前窗口的最大值
            if (i >= k - 1) {
                result.push_back(nums[dq.front()]);
            }
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     * 包含多种测试场景验证算法正确性
     */
    void testMaxSlidingWindow() {
        cout << "=== 滑动窗口最大值单元测试 ===" << endl;
        
        // 测试用例1：常规情况
        vector<int> nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        vector<int> result1 = maxSlidingWindow(nums1, k1);
        cout << "测试用例1: ";
        printVector(nums1);
        cout << ", k=" << k1 << endl;
        cout << "输出: ";
        printVector(result1);
        cout << endl << "期望: [3, 3, 5, 5, 6, 7]" << endl;
        
        // 测试用例2：k=1的情况
        vector<int> nums2 = {1, 2, 3, 4, 5};
        int k2 = 1;
        vector<int> result2 = maxSlidingWindow(nums2, k2);
        cout << "测试用例2: ";
        printVector(nums2);
        cout << ", k=" << k2 << endl;
        cout << "输出: ";
        printVector(result2);
        cout << endl << "期望: [1, 2, 3, 4, 5]" << endl;
        
        // 测试用例3：k等于数组长度
        vector<int> nums3 = {9, 8, 7, 6, 5};
        int k3 = 5;
        vector<int> result3 = maxSlidingWindow(nums3, k3);
        cout << "测试用例3: ";
        printVector(nums3);
        cout << ", k=" << k3 << endl;
        cout << "输出: ";
        printVector(result3);
        cout << endl << "期望: [9]" << endl;
        
        // 测试用例4：单调递增数组
        vector<int> nums4 = {1, 2, 3, 4, 5, 6};
        int k4 = 3;
        vector<int> result4 = maxSlidingWindow(nums4, k4);
        cout << "测试用例4: ";
        printVector(nums4);
        cout << ", k=" << k4 << endl;
        cout << "输出: ";
        printVector(result4);
        cout << endl << "期望: [3, 4, 5, 6]" << endl;
        
        // 测试用例5：单调递减数组
        vector<int> nums5 = {6, 5, 4, 3, 2, 1};
        int k5 = 3;
        vector<int> result5 = maxSlidingWindow(nums5, k5);
        cout << "测试用例5: ";
        printVector(nums5);
        cout << ", k=" << k5 << endl;
        cout << "输出: ";
        printVector(result5);
        cout << endl << "期望: [6, 5, 4, 3]" << endl;
        
        // 测试用例6：边界情况 - 空数组
        vector<int> nums6 = {};
        int k6 = 3;
        vector<int> result6 = maxSlidingWindow(nums6, k6);
        cout << "测试用例6: 空数组, k=" << k6 << endl;
        cout << "输出: ";
        printVector(result6);
        cout << endl << "期望: []" << endl;
        
        // 测试用例7：包含重复元素
        vector<int> nums7 = {1, 3, 3, 2, 5, 5, 4};
        int k7 = 3;
        vector<int> result7 = maxSlidingWindow(nums7, k7);
        cout << "测试用例7: ";
        printVector(nums7);
        cout << ", k=" << k7 << endl;
        cout << "输出: ";
        printVector(result7);
        cout << endl << "期望: [3, 3, 3, 5, 5]" << endl;
    }
    
    /**
     * 性能测试方法
     * 测试算法在大规模数据下的性能表现
     */
    void performanceTest() {
        cout << "\n=== 性能测试 ===" << endl;
        
        // 生成大规模测试数据
        int n = 100000;
        vector<int> largeNums(n);
        default_random_engine generator;
        uniform_int_distribution<int> distribution(0, 10000);
        
        for (int i = 0; i < n; i++) {
            largeNums[i] = distribution(generator);
        }
        int k = 1000;
        
        auto startTime = chrono::high_resolution_clock::now();
        vector<int> result = maxSlidingWindow(largeNums, k);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
        
        cout << "数据规模: " << n << ", 窗口大小: " << k << endl;
        cout << "执行时间: " << duration.count() << "ms" << endl;
        cout << "结果数组长度: " << result.size() << endl;
    }
    
    /**
     * 辅助函数：打印vector
     */
    void printVector(const vector<int>& vec) {
        cout << "[";
        for (size_t i = 0; i < vec.size(); i++) {
            cout << vec[i];
            if (i < vec.size() - 1) {
                cout << ", ";
            }
        }
        cout << "]";
    }
    
    /**
     * 主函数 - 运行测试
     */
    static void run() {
        Code16_SlidingWindowMaximum solution;
        
        // 运行单元测试
        solution.testMaxSlidingWindow();
        
        // 运行性能测试
        solution.performanceTest();
        
        cout << "\n=== 算法验证完成 ===" << endl;
    }
};

// 程序入口点
int main() {
    Code16_SlidingWindowMaximum::run();
    return 0;
}
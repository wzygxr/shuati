/**
 * 456. 132 模式 (132 Pattern)
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，数组中共有 n 个整数。
 * 132 模式的子序列 由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
 * 并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j]。
 * 如果 nums 中存在 132 模式的子序列，返回 true；否则，返回 false。
 * 
 * 解题思路:
 * 使用单调栈来解决。从右往左遍历数组，维护一个单调递减栈。
 * 同时记录一个变量 second，表示可能的最大中间值（即3后面的最大2）。
 * 当遇到比 second 小的元素时，说明找到了132模式。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，用于存储单调栈
 * 
 * 测试链接: https://leetcode.cn/problems/132-pattern/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、单元素数组边界情况
 * 2. 性能优化：使用vector模拟栈，避免动态内存分配
 * 3. 代码可读性：详细注释和有意义变量名
 * 4. 内存管理：使用RAII原则管理资源
 */

#include <iostream>
#include <vector>
#include <climits>
#include <stack>

using namespace std;

/**
 * @brief 判断数组中是否存在132模式的子序列
 * 
 * @param nums 输入整数数组
 * @return true 如果存在132模式
 * @return false 如果不存在132模式
 */
bool find132pattern(vector<int>& nums) {
    // 边界条件检查
    if (nums.size() < 3) {
        return false; // 至少需要3个元素才能形成132模式
    }
    
    int n = nums.size();
    // 使用vector模拟栈，避免动态内存分配
    vector<int> stack;
    stack.reserve(n); // 预分配空间提高性能
    int second = INT_MIN; // 记录可能的最大中间值（3后面的最大2）
    
    // 从右往左遍历数组
    for (int i = n - 1; i >= 0; i--) {
        // 如果当前元素小于second，说明找到了132模式
        if (nums[i] < second) {
            return true;
        }
        
        // 维护单调递减栈，找到更大的元素作为3，并更新second
        while (!stack.empty() && nums[i] > nums[stack.back()]) {
            // 更新second为栈顶元素（即当前找到的最大2）
            second = nums[stack.back()];
            stack.pop_back();
        }
        
        // 将当前索引入栈
        stack.push_back(i);
    }
    
    return false; // 没有找到132模式
}

/**
 * @brief 测试方法 - 验证算法正确性
 */
void test132Pattern() {
    cout << "=== 132模式算法测试 ===" << endl;
    
    // 测试用例1: [1, 2, 3, 4] - 预期: false
    vector<int> nums1 = {1, 2, 3, 4};
    bool result1 = find132pattern(nums1);
    cout << "测试用例1 [1, 2, 3, 4]: " << (result1 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例2: [3, 1, 4, 2] - 预期: true
    vector<int> nums2 = {3, 1, 4, 2};
    bool result2 = find132pattern(nums2);
    cout << "测试用例2 [3, 1, 4, 2]: " << (result2 ? "true" : "false") << " (预期: true)" << endl;
    
    // 测试用例3: [-1, 3, 2, 0] - 预期: true
    vector<int> nums3 = {-1, 3, 2, 0};
    bool result3 = find132pattern(nums3);
    cout << "测试用例3 [-1, 3, 2, 0]: " << (result3 ? "true" : "false") << " (预期: true)" << endl;
    
    // 测试用例4: [1, 0, 1, -4, -3] - 预期: false
    vector<int> nums4 = {1, 0, 1, -4, -3};
    bool result4 = find132pattern(nums4);
    cout << "测试用例4 [1, 0, 1, -4, -3]: " << (result4 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例5: 边界情况 - 空数组
    vector<int> nums5 = {};
    bool result5 = find132pattern(nums5);
    cout << "测试用例5 []: " << (result5 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例6: 边界情况 - 两个元素
    vector<int> nums6 = {1, 2};
    bool result6 = find132pattern(nums6);
    cout << "测试用例6 [1, 2]: " << (result6 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例7: 重复元素 [1, 3, 2, 4, 5, 6, 7, 8, 9, 10] - 预期: true
    vector<int> nums7 = {1, 3, 2, 4, 5, 6, 7, 8, 9, 10};
    bool result7 = find132pattern(nums7);
    cout << "测试用例7 [1, 3, 2, 4, 5, 6, 7, 8, 9, 10]: " << (result7 ? "true" : "false") << " (预期: true)" << endl;
    
    cout << "=== 所有测试用例执行完成！ ===" << endl;
}

/**
 * @brief 性能测试方法
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 性能测试：大规模数据 - 严格递增
    vector<int> nums;
    const int SIZE = 10000;
    nums.reserve(SIZE);
    for (int i = 0; i < SIZE; i++) {
        nums.push_back(i); // 严格递增，预期false
    }
    
    clock_t startTime = clock();
    bool result = find132pattern(nums);
    clock_t endTime = clock();
    
    cout << "性能测试 [" << SIZE << "个元素]: " << (result ? "true" : "false") 
         << " (预期: false), 耗时: " << (double)(endTime - startTime) / CLOCKS_PER_SEC * 1000 << "ms" << endl;
    
    cout << "=== 性能测试完成！ ===" << endl;
}

/**
 * @brief 主函数
 */
int main() {
    // 运行功能测试
    test132Pattern();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/**
 * 算法复杂度分析:
 * 
 * 时间复杂度: O(n)
 * - 每个元素最多入栈一次和出栈一次
 * - 虽然有两层循环，但内层循环的总操作次数不超过n次
 * 
 * 空间复杂度: O(n)
 * - 使用了一个大小为n的vector作为栈
 * - 没有使用递归，栈空间为O(1)
 * 
 * 最优解分析:
 * - 这是132模式问题的最优解
 * - 无法在O(n)时间内获得更好的时间复杂度
 * - 空间复杂度也是最优的，因为需要存储中间结果
 * 
 * C++特性利用:
 * - 使用vector代替原生数组，更安全
 * - 使用reserve预分配空间，提高性能
 * - 使用RAII原则自动管理内存
 * - 使用标准库函数提高代码可读性
 */
/**
 * 除自身以外数组的乘积 (Product of Array Except Self)
 * 
 * 题目描述:
 * 给你一个长度为 n 的整数数组 nums，其中 n > 1，返回输出数组 output，
 * 其中 output[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。
 * 
 * 示例:
 * 输入: [1,2,3,4]
 * 输出: [24,12,8,6]
 * 
 * 提示:
 * 题目数据保证数组之中任意元素的全部前缀元素和后缀（甚至是整个数组）的乘积都在 32 位整数范围内。
 * 
 * 说明:
 * 请不要使用除法，且在 O(n) 时间复杂度内完成此题。
 * 
 * 进阶:
 * 你可以在常数空间复杂度内完成这个题目吗？（出于对空间复杂度分析的目的，输出数组不被视为额外空间。）
 * 
 * 题目链接: https://leetcode.com/problems/product-of-array-except-self/
 * 
 * 解题思路:
 * 1. 使用两个数组分别存储左侧所有元素的乘积和右侧所有元素的乘积
 * 2. 对于位置i，结果为左侧乘积乘以右侧乘积
 * 3. 进阶：使用输出数组存储左侧乘积，然后从右向左遍历计算右侧乘积并直接更新结果
 * 
 * 时间复杂度: O(n) - 需要遍历数组两次
 * 空间复杂度: O(1) - 不考虑输出数组，只使用常数额外空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 空间优化：使用输出数组存储中间结果，避免额外空间
 * 3. 整数溢出：虽然题目保证在32位整数范围内，但实际工程中需要考虑
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能计算乘积。
 * 空间复杂度O(1)也是最优的（不考虑输出数组）。
 * 
 * 算法核心:
 * 对于每个位置i，结果 = 左侧所有元素的乘积 × 右侧所有元素的乘积
 * 通过左右两次遍历，分别计算左侧乘积和右侧乘积。
 */

#include <vector>
#include <iostream>
#include <chrono>

using namespace std;

class Solution {
public:
    /**
     * 计算除自身以外数组的乘积
     * 
     * @param nums 输入数组
     * @return 除自身以外数组的乘积
     * 
     * 异常场景处理:
     * - 空数组：直接返回原数组
     * - 单元素数组：直接返回原数组
     * - 包含0的数组：需要特殊处理（但算法本身支持）
     */
    vector<int> productExceptSelf(vector<int>& nums) {
        // 边界情况处理：空数组或单元素数组直接返回
        if (nums.empty() || nums.size() <= 1) {
            return nums;
        }
        
        int n = nums.size();
        vector<int> result(n);
        
        // 第一遍遍历：从左到右，计算每个位置左侧所有元素的乘积
        result[0] = 1; // 第一个元素左侧没有元素，乘积为1
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }
        
        // 第二遍遍历：从右到左，计算每个位置右侧所有元素的乘积，并与左侧乘积相乘
        int rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            // 当前位置的结果 = 左侧乘积 × 右侧乘积
            result[i] *= rightProduct;
            // 更新右侧乘积，为下一个位置（左边）准备
            rightProduct *= nums[i];
        }
        
        return result;
    }
};

/**
 * 测试函数
 */
void testProductExceptSelf() {
    Solution solution;
    
    // 测试用例1：正常情况
    vector<int> nums1 = {1, 2, 3, 4};
    vector<int> result1 = solution.productExceptSelf(nums1);
    cout << "测试用例1 [1,2,3,4]: ";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << " (预期: 24 12 8 6)" << endl;
    
    // 测试用例2：包含负数
    vector<int> nums2 = {2, 3, 4, 5};
    vector<int> result2 = solution.productExceptSelf(nums2);
    cout << "测试用例2 [2,3,4,5]: ";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << " (预期: 60 40 30 24)" << endl;
    
    // 测试用例3：包含0
    vector<int> nums3 = {1, 0, 3, 4};
    vector<int> result3 = solution.productExceptSelf(nums3);
    cout << "测试用例3 [1,0,3,4]: ";
    for (int num : result3) {
        cout << num << " ";
    }
    cout << " (预期: 0 12 0 0)" << endl;
    
    // 测试用例4：包含负数和0
    vector<int> nums4 = {-1, 2, 0, -3};
    vector<int> result4 = solution.productExceptSelf(nums4);
    cout << "测试用例4 [-1,2,0,-3]: ";
    for (int num : result4) {
        cout << num << " ";
    }
    cout << " (预期: 0 0 6 0)" << endl;
    
    // 测试用例5：空数组
    vector<int> nums5 = {};
    vector<int> result5 = solution.productExceptSelf(nums5);
    cout << "测试用例5 []: ";
    for (int num : result5) {
        cout << num << " ";
    }
    cout << " (预期: 空数组)" << endl;
    
    // 测试用例6：单元素数组
    vector<int> nums6 = {5};
    vector<int> result6 = solution.productExceptSelf(nums6);
    cout << "测试用例6 [5]: ";
    for (int num : result6) {
        cout << num << " ";
    }
    cout << " (预期: 5)" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===" << endl;
    Solution solution;
    int size = 1000000; // 100万元素
    vector<int> largeArray(size);
    
    // 初始化大数组（避免溢出）
    for (int i = 0; i < size; i++) {
        largeArray[i] = (i % 10) + 1; // 数值范围1-10
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    vector<int> result = solution.productExceptSelf(largeArray);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "处理 " << size << " 个元素耗时: " << duration.count() << "ms" << endl;
}

/**
 * 主函数
 */
int main() {
    cout << "=== 除自身以外数组的乘积测试 ===" << endl;
    testProductExceptSelf();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    cout << "\n=== 测试完成 ===" << endl;
    return 0;
}
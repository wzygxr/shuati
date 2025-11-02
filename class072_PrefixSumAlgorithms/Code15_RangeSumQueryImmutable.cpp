#include <iostream>
#include <vector>
#include <stdexcept>
using namespace std;

/**
 * 区间和查询 - 不可变 (Range Sum Query - Immutable)
 * 
 * 题目描述:
 * 给定一个整数数组 nums，计算索引 left 和 right （包含 left 和 right）之间的元素的和，其中 left <= right。
 * 实现 NumArray 类：
 * NumArray(int[] nums) 使用数组 nums 初始化对象
 * int sumRange(int left, int right) 返回数组 nums 中索引 left 和 right 之间的元素的总和，包含 left 和 right 两点
 * 
 * 示例:
 * 输入：
 * ["NumArray", "sumRange", "sumRange", "sumRange"]
 * [[[-2, 0, 3, -5, 2, -1]], [0, 2], [2, 5], [0, 5]]
 * 输出：
 * [null, 1, -1, -3]
 * 解释：
 * NumArray numArray = new NumArray([-2, 0, 3, -5, 2, -1]);
 * numArray.sumRange(0, 2); // return (-2) + 0 + 3 = 1
 * numArray.sumRange(2, 5); // return 3 + (-5) + 2 + (-1) = -1
 * numArray.sumRange(0, 5); // return (-2) + 0 + 3 + (-5) + 2 + (-1) = -3
 * 
 * 提示:
 * 1 <= nums.length <= 10^4
 * -10^5 <= nums[i] <= 10^5
 * 0 <= left <= right < nums.length
 * 最多调用 10^4 次 sumRange 方法
 * 
 * 题目链接: https://leetcode.com/problems/range-sum-query-immutable/
 * 
 * 解题思路:
 * 使用前缀和数组预处理原数组，使得每次查询区间和的操作时间复杂度为O(1)。
 * 1. 计算前缀和数组 prefixSum，其中 prefixSum[i] 表示原数组 nums 中前 i 个元素的和
 * 2. 对于区间查询 [left, right]，区间和为 prefixSum[right+1] - prefixSum[left]
 * 
 * 时间复杂度:
 * - 初始化: O(n) - 预处理前缀和数组
 * - 查询: O(1) - 直接利用前缀和数组计算区间和
 * 空间复杂度: O(n) - 存储前缀和数组
 */

class NumArray {
private:
    // 前缀和数组
    vector<long long> prefixSum;  // 使用long long避免整数溢出

public:
    /**
     * 使用数组 nums 初始化对象，预处理计算前缀和数组
     * 
     * @param nums 输入数组
     */
    NumArray(vector<int>& nums) {
        // 初始化前缀和数组，长度为 nums.size() + 1
        // prefixSum[0] = 0 表示前0个元素的和为0
        // prefixSum[i] 表示前i个元素的和，即 nums[0] + nums[1] + ... + nums[i-1]
        prefixSum.resize(nums.size() + 1, 0);
        
        // 计算前缀和
        for (int i = 0; i < nums.size(); i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
    }

    /**
     * 返回数组 nums 中索引 left 和 right 之间的元素的总和，包含两个端点
     * 
     * @param left 左边界索引
     * @param right 右边界索引
     * @return 区间 [left, right] 的和
     * @throws invalid_argument 如果索引参数无效
     */
    int sumRange(int left, int right) {
        // 参数合法性检查
        if (prefixSum.empty()) {
            throw invalid_argument("数组为空");
        }
        if (left < 0 || left >= prefixSum.size() - 1) {
            throw invalid_argument("左边界索引无效");
        }
        if (right < 0 || right >= prefixSum.size() - 1) {
            throw invalid_argument("右边界索引无效");
        }
        if (left > right) {
            throw invalid_argument("左边界不能大于右边界");
        }
        
        // 利用前缀和数组计算区间和
        // [left, right] 的和 = prefixSum[right+1] - prefixSum[left]
        return static_cast<int>(prefixSum[right + 1] - prefixSum[left]);
    }
};

int main() {
    try {
        // 创建测试用例数组
        vector<int> nums = {-2, 0, 3, -5, 2, -1};
        
        // 初始化 NumArray 对象
        NumArray numArray(nums);
        
        // 测试区间和查询
        // 测试用例1: [0, 2] 预期输出: 1
        cout << "区间 [0, 2] 的和: " << numArray.sumRange(0, 2) << endl;
        
        // 测试用例2: [2, 5] 预期输出: -1
        cout << "区间 [2, 5] 的和: " << numArray.sumRange(2, 5) << endl;
        
        // 测试用例3: [0, 5] 预期输出: -3
        cout << "区间 [0, 5] 的和: " << numArray.sumRange(0, 5) << endl;
        
        // 测试边界情况
        // 测试用例4: [3, 3] 预期输出: -5
        cout << "区间 [3, 3] 的和: " << numArray.sumRange(3, 3) << endl;
        
        // 测试空数组
        vector<int> emptyNums;
        NumArray emptyArray(emptyNums);
        // 这行应该抛出异常
        emptyArray.sumRange(0, 0);
    } catch (const invalid_argument& e) {
        cout << "异常捕获: " << e.what() << endl;
    }
    
    return 0;
}
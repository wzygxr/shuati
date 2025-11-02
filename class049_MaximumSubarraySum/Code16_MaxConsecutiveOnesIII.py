# LeetCode 1004. 最大连续1的个数 III
# 给定一个二进制数组 nums 和一个整数 k，如果可以翻转最多 k 个 0 ，则返回数组中连续 1 的最大个数。
# 测试链接 : https://leetcode.cn/problems/max-consecutive-ones-iii/

"""
解题思路:
这是一个滑动窗口问题的变种，可以转化为：找到最长的子数组，其中最多包含k个0。

核心思想:
1. 使用滑动窗口维护一个区间，区间内0的个数不超过k
2. 右指针向右扩展窗口，当遇到0时增加计数
3. 当0的计数超过k时，左指针向右移动直到0的计数不超过k
4. 在整个过程中记录最大的窗口长度

时间复杂度: O(n) - 每个元素最多被访问两次
空间复杂度: O(1) - 只需要常数个变量

是否最优解: 是，这是该问题的最优解法

核心细节解析:
1. 为什么可以转化为最多包含k个0的问题？
   - 因为翻转0相当于把0变成1，最多翻转k次就是最多允许k个0存在
2. 滑动窗口如何维护？
   - 当窗口内0的个数 <= k时，可以继续扩展右边界
   - 当窗口内0的个数 > k时，需要收缩左边界
3. 如何统计0的个数？
   - 每次遇到0时增加计数，当左边界遇到0时减少计数

工程化考量:
1. 异常处理：空数组、k为负数等情况
2. 边界处理：全1数组、全0数组等特殊情况
3. 性能优化：使用简洁的循环结构，避免不必要的计算
"""

from typing import List

class Solution:
    def longestOnes(self, nums: List[int], k: int) -> int:
        # 异常防御
        if not nums:
            return 0
        if k < 0:
            k = 0  # k不能为负数
            
        n = len(nums)
        left = 0        # 滑动窗口左边界
        zero_count = 0   # 当前窗口内0的个数
        max_length = 0   # 最大连续1的个数（包含翻转的0）
        
        # 遍历数组，右指针从0到n-1
        for right in range(n):
            # 如果当前元素是0，增加0的计数
            if nums[right] == 0:
                zero_count += 1
                
            # 当0的个数超过k时，收缩左边界
            while zero_count > k:
                if nums[left] == 0:
                    zero_count -= 1
                left += 1
                
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
            
        return max_length

# 测试代码
def test_solution():
    solution = Solution()
    
    # 测试用例1：正常情况
    nums1 = [1,1,1,0,0,0,1,1,1,1,0]
    k1 = 2
    print("测试用例1:")
    print(f"数组: {nums1}, k={k1}")
    result1 = solution.longestOnes(nums1, k1)
    print(f"最大连续1的个数: {result1}")  # 预期输出: 6
    
    # 测试用例2：k=0，不能翻转
    nums2 = [1,1,1,0,0,0,1,1,1,1,0]
    k2 = 0
    print("\n测试用例2:")
    print(f"数组: {nums2}, k={k2}")
    result2 = solution.longestOnes(nums2, k2)
    print(f"最大连续1的个数: {result2}")  # 预期输出: 4
    
    # 测试用例3：全1数组
    nums3 = [1,1,1,1,1]
    k3 = 2
    print("\n测试用例3:")
    print(f"数组: {nums3}, k={k3}")
    result3 = solution.longestOnes(nums3, k3)
    print(f"最大连续1的个数: {result3}")  # 预期输出: 5
    
    # 测试用例4：全0数组
    nums4 = [0,0,0,0,0]
    k4 = 3
    print("\n测试用例4:")
    print(f"数组: {nums4}, k={k4}")
    result4 = solution.longestOnes(nums4, k4)
    print(f"最大连续1的个数: {result4}")  # 预期输出: 3

if __name__ == "__main__":
    test_solution()

"""
相关题目扩展:
1. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
2. LeetCode 487. 最大连续1的个数 II - https://leetcode.cn/problems/max-consecutive-ones-ii/
3. LeetCode 485. 最大连续1的个数 - https://leetcode.cn/problems/max-consecutive-ones/
4. LeetCode 424. 替换后的最长重复字符 - https://leetcode.cn/problems/longest-repeating-character-replacement/
5. LeetCode 3. 无重复字符的最长子串 - https://leetcode.cn/problems/longest-substring-without-repeating-characters/

算法技巧总结:
1. 滑动窗口适用于求最长/最短连续子数组问题
2. 关键是维护窗口的合法性（如0的个数不超过k）
3. 时间复杂度O(n)，空间复杂度O(1)

工程化思考:
1. 可以封装为通用函数，支持不同的条件和约束
2. 对于大规模数据，滑动窗口算法具有很好的性能
3. 在实际应用中，可能需要考虑其他类型的约束条件
"""

# Java 实现
"""
class Solution {
    public int longestOnes(int[] nums, int k) {
        if (nums == null || nums.length == 0) return 0;
        if (k < 0) k = 0;
        
        int n = nums.length;
        int left = 0;
        int zeroCount = 0;
        int maxLength = 0;
        
        for (int right = 0; right < n; right++) {
            if (nums[right] == 0) {
                zeroCount++;
            }
            
            while (zeroCount > k) {
                if (nums[left] == 0) {
                    zeroCount--;
                }
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}

// 时间复杂度: O(n)
// 空间复杂度: O(1)
// 是否最优解: 是
"""

# C++ 实现
"""
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    int longestOnes(vector<int>& nums, int k) {
        if (nums.empty()) return 0;
        if (k < 0) k = 0;
        
        int n = nums.size();
        int left = 0;
        int zeroCount = 0;
        int maxLength = 0;
        
        for (int right = 0; right < n; right++) {
            if (nums[right] == 0) {
                zeroCount++;
            }
            
            while (zeroCount > k) {
                if (nums[left] == 0) {
                    zeroCount--;
                }
                left++;
            }
            
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
};

// 时间复杂度: O(n)
// 空间复杂度: O(1)
// 是否最优解: 是
"""
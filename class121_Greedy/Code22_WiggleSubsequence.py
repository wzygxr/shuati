# 摆动序列
# 如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列
# 第一个差（如果存在的话）可能是正数或负数。仅有一个元素或者含两个不等元素的序列也视作摆动序列
# 给你一个整数数组 nums ，返回 nums 中作为摆动序列的最长子序列的长度
# 测试链接: https://leetcode.cn/problems/wiggle-subsequence/

class Solution:
    def wiggleMaxLength(self, nums: list) -> int:
        """
        摆动序列问题的贪心解法
        
        解题思路：
        1. 使用贪心策略，统计序列中波峰和波谷的数量
        2. 当序列出现上升趋势时，记录波峰；当序列出现下降趋势时，记录波谷
        3. 波峰和波谷的数量加1就是最长摆动序列的长度
        
        贪心策略的正确性：
        局部最优：删除单调坡度上的节点，那么这个坡度就可以有两个局部峰值
        全局最优：整个序列有最多的局部峰值，从而达到最长摆动序列
        
        时间复杂度：O(n)，只需要遍历数组一次
        空间复杂度：O(1)，只使用了常数个额外变量
        
        Args:
            nums: 输入数组
            
        Returns:
            最长摆动序列的长度
        """
        # 边界条件处理
        if not nums:
            return 0
        if len(nums) <= 1:
            return len(nums)
        
        n = len(nums)
        up = 1   # 上升序列长度
        down = 1 # 下降序列长度
        
        # 遍历数组，统计波峰和波谷
        for i in range(1, n):
            if nums[i] > nums[i - 1]:
                # 当前是上升趋势，更新上升序列长度
                up = down + 1
            elif nums[i] < nums[i - 1]:
                # 当前是下降趋势，更新下降序列长度
                down = up + 1
            # 如果相等，保持不变
        
        return max(up, down)
    
    def wiggleMaxLength2(self, nums: list) -> int:
        """
        摆动序列问题的另一种贪心解法（更直观）
        
        解题思路：
        1. 统计序列中实际波峰和波谷的数量
        2. 使用状态机思想，记录当前趋势
        3. 当趋势发生变化时，增加摆动序列长度
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not nums:
            return 0
        if len(nums) <= 1:
            return len(nums)
        
        n = len(nums)
        result = 1  # 至少有一个元素
        prev_diff = 0  # 前一个差值
        
        for i in range(1, n):
            curr_diff = nums[i] - nums[i - 1]
            
            # 当差值符号发生变化时（从正变负或从负变正）
            if (prev_diff <= 0 and curr_diff > 0) or (prev_diff >= 0 and curr_diff < 0):
                result += 1
                prev_diff = curr_diff
        
        return result

# 测试代码
def test_wiggle_max_length():
    solution = Solution()
    
    # 测试用例1
    # 输入: nums = [1,7,4,9,2,5]
    # 输出: 6
    # 解释: 整个序列均为摆动序列
    nums1 = [1, 7, 4, 9, 2, 5]
    print(f"测试用例1结果: {solution.wiggleMaxLength(nums1)}")  # 期望输出: 6
    
    # 测试用例2
    # 输入: nums = [1,17,5,10,13,15,10,5,16,8]
    # 输出: 7
    # 解释: 摆动序列为 [1,17,10,13,10,16,8]
    nums2 = [1, 17, 5, 10, 13, 15, 10, 5, 16, 8]
    print(f"测试用例2结果: {solution.wiggleMaxLength(nums2)}")  # 期望输出: 7
    
    # 测试用例3
    # 输入: nums = [1,2,3,4,5,6,7,8,9]
    # 输出: 2
    # 解释: 单调递增序列，摆动序列长度为2
    nums3 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    print(f"测试用例3结果: {solution.wiggleMaxLength(nums3)}")  # 期望输出: 2
    
    # 测试用例4：边界情况
    # 输入: nums = [1]
    # 输出: 1
    nums4 = [1]
    print(f"测试用例4结果: {solution.wiggleMaxLength(nums4)}")  # 期望输出: 1
    
    # 测试用例5：复杂情况
    # 输入: nums = [3,3,3,2,5]
    # 输出: 3
    # 解释: 摆动序列为 [3,2,5] 或 [3,3,2,5]
    nums5 = [3, 3, 3, 2, 5]
    print(f"测试用例5结果: {solution.wiggleMaxLength(nums5)}")  # 期望输出: 3

if __name__ == "__main__":
    test_wiggle_max_length()
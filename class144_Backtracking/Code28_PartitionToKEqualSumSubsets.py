from typing import List

class Solution:
    """
    LeetCode 698. 划分为k个相等的子集
    
    题目描述：
    给定一个整数数组 nums 和一个正整数 k，找出是否有可能把数组分成 k 个非空子集，其总和都相等。
    
    示例：
    输入：nums = [4, 3, 2, 3, 5, 2, 1], k = 4
    输出：true
    解释：可以将其分成 4 个子集（5），（1,4），（2,3），（2,3）等于总和。
    
    输入：nums = [1,2,3,4], k = 3
    输出：false
    
    提示：
    1 <= k <= len(nums) <= 16
    0 < nums[i] < 10000
    每个元素的频率在 [1,4] 范围内
    
    链接：https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
    
    算法思路：
    1. 计算所有数字的总和，如果不能被k整除，直接返回false
    2. 计算每个子集的目标和 = 总和 / k
    3. 将数字从大到小排序，优先使用大数字可以提前剪枝
    4. 使用回溯算法尝试将数字分配到k个子集
    5. 使用剪枝优化：如果当前子集和超过目标和，提前终止
    
    时间复杂度：O(k^n)，最坏情况下需要尝试所有分配方式
    空间复杂度：O(n)，递归栈深度
    """
    
    def canPartitionKSubsets(self, nums: List[int], k: int) -> bool:
        total = sum(nums)
        
        # 如果总和不能被k整除，直接返回false
        if total % k != 0:
            return False
        
        # 目标和
        target = total // k
        
        # 从大到小排序，优先使用大数字
        nums.sort(reverse=True)
        
        # 如果最大的数字大于目标和，直接返回false
        if nums[0] > target:
            return False
        
        # k个子集的当前和
        subsets = [0] * k
        
        return self.backtrack(nums, 0, subsets, target)
    
    def backtrack(self, nums: List[int], index: int, subsets: List[int], target: int) -> bool:
        # 终止条件：所有数字都已分配
        if index == len(nums):
            # 检查所有子集是否都等于目标和
            return all(subset == target for subset in subsets)
        
        # 尝试将当前数字分配到k个子集
        for i in range(len(subsets)):
            # 剪枝：如果当前子集和加上当前数字超过目标和，跳过
            if subsets[i] + nums[index] > target:
                continue
            
            # 剪枝：如果当前子集与前一个子集和相同，且前一个子集没有分配当前数字，跳过
            # 避免重复计算相同的情况
            if i > 0 and subsets[i] == subsets[i - 1]:
                continue
            
            subsets[i] += nums[index]
            if self.backtrack(nums, index + 1, subsets, target):
                return True
            subsets[i] -= nums[index]
        
        return False

def test_partition_k_subsets():
    solution = Solution()
    
    # 测试用例1
    nums1 = [4, 3, 2, 3, 5, 2, 1]
    k1 = 4
    result1 = solution.canPartitionKSubsets(nums1, k1)
    print("输入: nums = [4, 3, 2, 3, 5, 2, 1], k =", k1)
    print("输出:", result1)
    
    # 测试用例2
    nums2 = [1, 2, 3, 4]
    k2 = 3
    result2 = solution.canPartitionKSubsets(nums2, k2)
    print("\n输入: nums = [1, 2, 3, 4], k =", k2)
    print("输出:", result2)
    
    # 测试用例3
    nums3 = [1, 1, 1, 1, 2, 2, 2, 2]
    k3 = 4
    result3 = solution.canPartitionKSubsets(nums3, k3)
    print("\n输入: nums = [1, 1, 1, 1, 2, 2, 2, 2], k =", k3)
    print("输出:", result3)

if __name__ == "__main__":
    test_partition_k_subsets()
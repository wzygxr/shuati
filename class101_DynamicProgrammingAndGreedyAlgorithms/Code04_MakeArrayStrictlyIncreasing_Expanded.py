# 使数组严格递增的最小操作数问题扩展实现 (Python版本)
# 给你两个整数数组 arr1 和 arr2
# 返回使 arr1 严格递增所需要的最小操作数（可能为0）
# 每一步操作中，你可以分别从 arr1 和 arr2 中各选出一个索引
# 分别为 i 和 j，0 <= i < arr1.length 和 0 <= j < arr2.length
# 然后进行赋值运算 arr1[i] = arr2[j]
# 如果无法让 arr1 严格递增，请返回-1
# 1 <= arr1.length, arr2.length <= 2000
# 0 <= arr1[i], arr2[i] <= 10^9
# 测试链接 : https://leetcode.cn/problems/make-array-strictly-increasing/

import bisect
from typing import List

class Code04_MakeArrayStrictlyIncreasing_Expanded:
    '''
    类似题目1：使数组严格递增（LeetCode 1187）
    题目描述：
    给你两个整数数组 arr1 和 arr2，返回使 arr1 严格递增所需要的最小操作数（可能为0）。
    每一步操作中，你可以分别从 arr1 和 arr2 中各选出一个索引，分别为 i 和 j，
    0 <= i < arr1.length 和 0 <= j < arr2.length，然后进行赋值运算 arr1[i] = arr2[j]。
    如果无法让 arr1 严格递增，请返回 -1。
    
    示例：
    输入：arr1 = [1,5,3,6,7], arr2 = [1,3,2,4]
    输出：1
    解释：用 2 来替换 5，之后 arr1 = [1, 2, 3, 6, 7]。
    
    解题思路：
    这与原题完全相同，使用动态规划解决。
    dp[i] 表示使前i个元素严格递增所需的最小操作数
    '''
    
    # 使数组严格递增 - 记忆化搜索解法
    # 时间复杂度: O(n * m * log(m))，其中n是arr1的长度，m是arr2的长度
    # 空间复杂度: O(n * m)
    @staticmethod
    def make_array_increasing1(arr1: List[int], arr2: List[int]) -> int:
        # 去重并排序arr2
        arr2 = sorted(list(set(arr2)))
        m = len(arr2)
        
        n = len(arr1)
        # dp[i] 表示处理前i个元素所需的最小操作数
        dp = [-1 for _ in range(n + 1)]
        
        def dfs(i: int) -> int:
            if i == n:
                return 0
            
            if dp[i] != -1:
                return dp[i]
            
            result = float('inf')
            prev = float('-inf') if i == 0 else arr1[i - 1]
            pos = bisect.bisect_right(arr2, prev)
            
            # 尝试所有可能的替换策略
            j, ops = i, 0
            while j <= n:
                if j == n:
                    result = min(result, ops)
                else:
                    if prev < arr1[j]:
                        next_val = dfs(j + 1)
                        if next_val != float('inf'):
                            result = min(result, ops + next_val)
                    
                    if pos != -1 and pos < m:
                        prev = arr2[pos]
                        pos += 1
                        ops += 1
                        j += 1
                    else:
                        break
            
            # 将结果转换为整数存储
            dp[i] = int(result) if result != float('inf') else -1
            return int(result) if result != float('inf') else -1
        
        result = dfs(0)
        return -1 if result == -1 else result
    
    '''
    类似题目2：最少操作使数组递增（LeetCode 1827）
    题目描述：
    给你一个整数数组 nums （下标从 0 开始）。每一次操作中，你可以选择数组中一个元素，并将它增加 1 。
    请你返回使 nums 严格递增的最少操作次数。
    我们称数组 nums 是严格递增的，当它满足对于所有的 0 <= i < nums.length - 1 都有 nums[i] < nums[i+1]。
    一个长度为 1 的数组是严格递增的一种特殊情况。
    
    示例：
    输入：nums = [1,1,1]
    输出：3
    解释：你可以进行如下操作：
    1) 增加 nums[2] ，数组变为 [1,1,2] 。
    2) 增加 nums[1] ，数组变为 [1,2,2] 。
    3) 增加 nums[2] ，数组变为 [1,2,3] 。
    
    解题思路：
    贪心算法。从左到右遍历数组，如果当前元素小于等于前一个元素，
    则将其增加到前一个元素+1，记录操作次数。
    '''
    
    # 最少操作使数组递增 - 贪心算法解法
    # 时间复杂度: O(n)，其中n是数组长度
    # 空间复杂度: O(1)
    @staticmethod
    def min_operations(nums: List[int]) -> int:
        operations = 0
        
        # 从第二个元素开始遍历
        for i in range(1, len(nums)):
            # 如果当前元素小于等于前一个元素
            if nums[i] <= nums[i - 1]:
                # 计算需要增加的操作次数
                operations += nums[i - 1] + 1 - nums[i]
                # 更新当前元素的值
                nums[i] = nums[i - 1] + 1
        
        return operations
    
    '''
    类似题目3：最长递增子序列（LeetCode 300）
    题目描述：
    给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
    子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
    
    示例：
    输入：nums = [10,9,2,5,3,7,101,18]
    输出：4
    解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
    
    解题思路：
    使用贪心+二分查找的方法。
    维护一个数组tails，tails[i]表示长度为i+1的递增子序列的尾部元素的最小值。
    遍历nums数组，对于每个元素，使用二分查找在tails中找到第一个大于等于它的位置，
    如果该位置超出了当前tails的长度，则说明找到了更长的递增子序列，扩展tails；
    否则更新该位置的值，使其更小。
    '''
    
    # 最长递增子序列 - 贪心+二分查找解法
    # 时间复杂度: O(n * log(n))，其中n是数组长度
    # 空间复杂度: O(n)
    @staticmethod
    def length_of_lis(nums: List[int]) -> int:
        if not nums:
            return 0
        
        # tails[i] 表示长度为i+1的递增子序列的尾部元素的最小值
        tails = []
        
        for num in nums:
            # 使用二分查找找到第一个大于等于num的位置
            pos = bisect.bisect_left(tails, num)
            
            # 如果插入位置超出了当前长度，说明找到了更长的递增子序列
            if pos == len(tails):
                tails.append(num)
            else:
                # 更新该位置的值，使其更小
                tails[pos] = num
        
        return len(tails)
    
    '''
    类似题目4：俄罗斯套娃信封问题（LeetCode 354）
    题目描述：
    给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
    当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
    请计算最多能有多少个信封能组成一组"俄罗斯套娃"信封（即可以把一个信封放到另一个信封里面）。
    注意：不允许旋转信封。
    
    示例：
    输入：envelopes = [[5,4],[6,4],[6,7],[2,3]]
    输出：3
    解释：最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
    
    解题思路：
    这是一个二维最长递增子序列问题。
    首先按照宽度升序排列，如果宽度相同则按照高度降序排列。
    然后对高度数组求最长递增子序列。
    '''
    
    # 俄罗斯套娃信封问题 - 动态规划解法
    # 时间复杂度: O(n * log(n))，其中n是信封数量
    # 空间复杂度: O(n)
    @staticmethod
    def max_envelopes(envelopes: List[List[int]]) -> int:
        if not envelopes:
            return 0
        
        # 按照宽度升序排列，如果宽度相同则按照高度降序排列
        envelopes.sort(key=lambda x: (x[0], -x[1]))
        
        # 对高度数组求最长递增子序列
        heights = [envelope[1] for envelope in envelopes]
        
        return Code04_MakeArrayStrictlyIncreasing_Expanded.length_of_lis(heights)


# 测试方法
if __name__ == "__main__":
    # 测试使数组严格递增
    arr1 = [1,5,3,6,7]
    arr2 = [1,3,2,4]
    print("使数组严格递增结果:", Code04_MakeArrayStrictlyIncreasing_Expanded.make_array_increasing1(arr1, arr2))
    
    # 测试最少操作使数组递增
    nums = [1,1,1]
    print("最少操作使数组递增结果:", Code04_MakeArrayStrictlyIncreasing_Expanded.min_operations(nums))
    
    # 测试最长递增子序列
    nums2 = [10,9,2,5,3,7,101,18]
    print("最长递增子序列结果:", Code04_MakeArrayStrictlyIncreasing_Expanded.length_of_lis(nums2))
    
    # 测试俄罗斯套娃信封问题
    envelopes = [[5,4],[6,4],[6,7],[2,3]]
    print("俄罗斯套娃信封问题结果:", Code04_MakeArrayStrictlyIncreasing_Expanded.max_envelopes(envelopes))
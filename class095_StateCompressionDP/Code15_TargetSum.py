# 目标和 (Target Sum)
# 给你一个非负整数数组 nums 和一个整数 target 。
# 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式。
# 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
# 测试链接 : https://leetcode.cn/problems/target-sum/

class Code15_TargetSum:
    
    # 使用动态规划解决目标和问题
    # 核心思想：将问题转化为子集和问题，通过动态规划计算方案数
    # 时间复杂度: O(n * target)
    # 空间复杂度: O(target)
    @staticmethod
    def findTargetSumWays(nums, target):
        # 计算数组元素总和
        total_sum = sum(nums)
        
        # 如果总和小于目标值的绝对值，或者(total_sum+target)是奇数，则无解
        if total_sum < abs(target) or (total_sum + target) % 2 != 0:
            return 0
        
        # 计算需要分配给正号元素的和
        pos = (total_sum + target) // 2
        
        # dp[i] 表示和为i的方案数
        dp = [0] * (pos + 1)
        # 初始状态：和为0的方案数为1（不选择任何元素）
        dp[0] = 1
        
        # 状态转移：枚举每个元素
        for num in nums:
            # 从后往前更新，避免重复使用同一元素
            for i in range(pos, num - 1, -1):
                dp[i] += dp[i - num]
        
        # 返回和为pos的方案数
        return dp[pos]

# 测试代码
if __name__ == "__main__":
    solution = Code15_TargetSum()
    
    # 测试用例1: 基础用例
    nums1 = [1, 1, 1, 1, 1]
    target1 = 3
    result1 = solution.findTargetSumWays(nums1, target1)
    print(f"测试用例1: nums={nums1}, target={target1}, 结果={result1}")  # 期望输出: 5
    
    # 测试用例2: 包含0的数组
    nums2 = [1, 0]
    target2 = 1
    result2 = solution.findTargetSumWays(nums2, target2)
    print(f"测试用例2: nums={nums2}, target={target2}, 结果={result2}")  # 期望输出: 2
    
    # 测试用例3: 无解情况
    nums3 = [1, 2, 3]
    target3 = 10
    result3 = solution.findTargetSumWays(nums3, target3)
    print(f"测试用例3: nums={nums3}, target={target3}, 结果={result3}")  # 期望输出: 0
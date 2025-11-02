# 最大子数组和
# 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
# 子数组是数组中的一个连续部分。
# 测试链接 : https://leetcode.cn/problems/maximum-subarray/

def maxSubArray(nums):
    """
    最大子数组和（Kadane算法）
    
    算法思路：
    使用贪心策略（Kadane算法）：
    1. 维护两个变量：
       - maxSoFar：到目前为止找到的最大子数组和
       - maxEndingHere：以当前元素结尾的最大子数组和
    2. 遍历数组，对于每个元素：
       - 更新maxEndingHere = max(nums[i], maxEndingHere + nums[i])
       - 更新maxSoFar = max(maxSoFar, maxEndingHere)
    3. 返回maxSoFar
    
    正确性分析：
    1. 对于每个位置，我们只需要考虑以该位置结尾的最大子数组和
    2. 要么从当前位置重新开始，要么延续之前的子数组
    3. 取两者中的较大值
    
    时间复杂度：O(n) - 只需要遍历数组一次
    空间复杂度：O(1) - 只使用常数额外空间
    
    :param nums: 整数数组
    :return: 最大子数组和
    """
    # 初始化变量
    maxSoFar = nums[0]        # 到目前为止找到的最大子数组和
    maxEndingHere = nums[0]   # 以当前元素结尾的最大子数组和
    
    # 从第二个元素开始遍历
    for i in range(1, len(nums)):
        # 更新以当前元素结尾的最大子数组和
        # 要么从当前元素重新开始，要么延续之前的子数组
        maxEndingHere = max(nums[i], maxEndingHere + nums[i])
        
        # 更新到目前为止找到的最大子数组和
        maxSoFar = max(maxSoFar, maxEndingHere)
    
    # 返回最大子数组和
    return maxSoFar

# 测试用例
if __name__ == "__main__":
    # 测试用例1: nums = [-2,1,-3,4,-1,2,1,-5,4] -> 输出: 6
    nums1 = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    print("测试用例1:")
    print("数组:", nums1)
    print("最大子数组和:", maxSubArray(nums1))  # 期望输出: 6 ([4,-1,2,1])
    
    # 测试用例2: nums = [1] -> 输出: 1
    nums2 = [1]
    print("\n测试用例2:")
    print("数组:", nums2)
    print("最大子数组和:", maxSubArray(nums2))  # 期望输出: 1
    
    # 测试用例3: nums = [5,4,-1,7,8] -> 输出: 23
    nums3 = [5, 4, -1, 7, 8]
    print("\n测试用例3:")
    print("数组:", nums3)
    print("最大子数组和:", maxSubArray(nums3))  # 期望输出: 23 ([5,4,-1,7,8])
    
    # 测试用例4: nums = [-1] -> 输出: -1
    nums4 = [-1]
    print("\n测试用例4:")
    print("数组:", nums4)
    print("最大子数组和:", maxSubArray(nums4))  # 期望输出: -1
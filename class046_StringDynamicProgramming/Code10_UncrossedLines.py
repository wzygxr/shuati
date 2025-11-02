# 不相交的线
# 在两条独立的水平线上按给定的顺序写下 nums1 和 nums2 中的整数。
# 现在，可以绘制一些连接两个数字 nums1[i] 和 nums2[j] 的直线，这些直线需要同时满足：
# nums1[i] == nums2[j]
# 且绘制的直线不与任何其他连线（非水平线）相交。
# 请注意，连线即使在端点也不能相交：每个数字只能属于一条连线。
# 以这种方法绘制线条，并返回可以绘制的最大连线数。
# 测试链接 : https://leetcode.cn/problems/uncrossed-lines/

class Solution:
    '''
    不相交的线 - 动态规划解法
    使用动态规划解决不相交的线问题
    dp[i][j] 表示nums1的前i个数字和nums2的前j个数字能绘制的最大连线数
    
    状态转移方程：
    如果 nums1[i-1] == nums2[j-1]：
      dp[i][j] = dp[i-1][j-1] + 1
    否则：
      dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    解释：
    当当前数字相等时，可以连线，结果等于前面子数组的最大连线数加1
    当当前数字不相等时，不能连线，取两种情况的最大值：
      1. 不使用nums1[i-1]数字的情况：dp[i-1][j]
      2. 不使用nums2[j-1]数字的情况：dp[i][j-1]
    
    边界条件：
    dp[0][j] = 0，表示nums1为空时无法连线
    dp[i][0] = 0，表示nums2为空时无法连线
    
    时间复杂度：O(n*m)，其中n为nums1的长度，m为nums2的长度
    空间复杂度：O(n*m)
    '''
    def maxUncrossedLines(self, nums1, nums2):
        n, m = len(nums1), len(nums2)
        
        # dp[i][j] 表示nums1的前i个数字和nums2的前j个数字能绘制的最大连线数
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if nums1[i - 1] == nums2[j - 1]:
                    # 当前数字相等，可以连线
                    dp[i][j] = dp[i - 1][j - 1] + 1
                else:
                    # 当前数字不相等，不能连线，取两种情况的最大值
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        
        return dp[n][m]

    '''
    空间优化版本
    使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def maxUncrossedLinesOptimized(self, nums1, nums2):
        n, m = len(nums1), len(nums2)
        
        # 只需要一行数组
        dp = [0] * (m + 1)
        
        # 填充dp表
        for i in range(1, n + 1):
            prev = dp[0]  # 保存dp[i-1][j-1]的值
            
            for j in range(1, m + 1):
                temp = dp[j]  # 保存当前dp[j]的值，用于下一次循环
                
                if nums1[i - 1] == nums2[j - 1]:
                    # 当前数字相等，可以连线
                    dp[j] = prev + 1
                else:
                    # 当前数字不相等，不能连线，取两种情况的最大值
                    dp[j] = max(dp[j], dp[j - 1])
                
                prev = temp  # 更新prev为原来的dp[j]值
        
        return dp[m]


# 测试函数
def test():
    sol = Solution()
    
    # 测试用例
    test_cases = [
        ([1,4,2], [1,2,4]),     # 2
        ([2,5,1,2,5], [10,5,2,1,5,2]), # 3
        ([1,3,7,1,7,5], [1,9,2,5,1]), # 2
        ([1,2,3], [4,5,6]),     # 0
        ([1,2,3], [1,2,3])      # 3
    ]
    
    print("不相交的线测试:")
    for nums1, nums2 in test_cases:
        result1 = sol.maxUncrossedLines(nums1, nums2)
        result2 = sol.maxUncrossedLinesOptimized(nums1, nums2)
        print(f'nums1={nums1}, nums2={nums2} => {result1} (optimized: {result2})')


# 运行测试
if __name__ == "__main__":
    test()
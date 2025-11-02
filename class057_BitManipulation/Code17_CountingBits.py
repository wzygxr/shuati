# 比特位计数
# 测试链接 : https://leetcode.cn/problems/counting-bits/
'''
题目描述：
给你一个整数 n ，对于 0 <= i <= n 中的每个 i ，计算其二进制表示中 1 的个数 ，返回一个长度为 n + 1 的数组 ans 作为答案。

示例：
输入：n = 2
输出：[0,1,1]
解释：
0 --> 0
1 --> 1
2 --> 10

输入：n = 5
输出：[0,1,1,2,1,2]
解释：
0 --> 0
1 --> 1
2 --> 10
3 --> 11
4 --> 100
5 --> 101

提示：
0 <= n <= 10^5

解题思路：
这是一个典型的动态规划问题，可以利用位运算的特性高效解决。

方法1：动态规划 + 最低有效位
观察二进制数的规律：
- 对于偶数 i，其二进制中 1 的个数等于 i/2 中 1 的个数
- 对于奇数 i，其二进制中 1 的个数等于 i/2 中 1 的个数加 1

可以统一表示为：dp[i] = dp[i >> 1] + (i & 1)

方法2：动态规划 + 最高有效位
对于数字 i，如果我们找到小于等于 i 的最大的 2 的幂 j，那么 dp[i] = dp[i - j] + 1

方法3：动态规划 + Brian Kernighan 算法
利用 n & (n - 1) 可以移除最右边的 1，因此 dp[i] = dp[i & (i - 1)] + 1

方法4：Python内置函数（简洁但效率可能不如动态规划）

时间复杂度：
- 动态规划方法：O(n) - 只需要遍历一次 0 到 n
- 内置函数方法：O(n * k)，其中k是数字的平均位数

空间复杂度：O(n) - 需要一个长度为 n + 1 的数组来存储结果
'''

class Solution:
    """
    比特位计数解决方案类
    提供多种计算比特位中1的个数的方法
    """
    
    def countBits(self, n: int) -> list[int]:
        """
        计算0到n之间每个数字的二进制表示中1的个数
        使用动态规划 + 最低有效位方法
        
        Args:
            n: 输入整数
            
        Returns:
            包含每个数字二进制中1的个数的数组
        """
        dp = [0] * (n + 1)
        
        # 方法1：动态规划 + 最低有效位
        for i in range(1, n + 1):
            # 对于数字i，右移一位得到i//2，然后加上i的最低位
            dp[i] = dp[i >> 1] + (i & 1)
        
        return dp
    
    def countBits2(self, n: int) -> list[int]:
        """
        计算0到n之间每个数字的二进制表示中1的个数
        使用动态规划 + 最高有效位方法
        
        Args:
            n: 输入整数
            
        Returns:
            包含每个数字二进制中1的个数的数组
        """
        dp = [0] * (n + 1)
        highest_bit = 0  # 记录当前的最高有效位
        
        for i in range(1, n + 1):
            # 如果i是2的幂，更新highest_bit
            if (i & (i - 1)) == 0:
                highest_bit = i
            # 利用最高有效位计算当前数字的1的个数
            dp[i] = dp[i - highest_bit] + 1
        
        return dp
    
    def countBits3(self, n: int) -> list[int]:
        """
        计算0到n之间每个数字的二进制表示中1的个数
        使用动态规划 + Brian Kernighan算法
        
        Args:
            n: 输入整数
            
        Returns:
            包含每个数字二进制中1的个数的数组
        """
        dp = [0] * (n + 1)
        
        for i in range(1, n + 1):
            # 利用n & (n - 1)移除最右边的1，然后加1
            dp[i] = dp[i & (i - 1)] + 1
        
        return dp
    
    def countBits4(self, n: int) -> list[int]:
        """
        计算0到n之间每个数字的二进制表示中1的个数
        使用Python内置函数（简洁但效率可能不如动态规划）
        
        Args:
            n: 输入整数
            
        Returns:
            包含每个数字二进制中1的个数的数组
        """
        result = []
        for i in range(n + 1):
            # 将数字转换为二进制字符串并统计'1'的个数
            result.append(bin(i).count('1'))
        
        return result

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试方法1
    result1 = solution.countBits(5)
    print("Test 1: ", result1)  # 输出: [0, 1, 1, 2, 1, 2]
    
    # 测试方法2
    result2 = solution.countBits2(5)
    print("Test 2: ", result2)  # 输出: [0, 1, 1, 2, 1, 2]
    
    # 测试方法3
    result3 = solution.countBits3(5)
    print("Test 3: ", result3)  # 输出: [0, 1, 1, 2, 1, 2]
    
    # 测试方法4
    result4 = solution.countBits4(5)
    print("Test 4: ", result4)  # 输出: [0, 1, 1, 2, 1, 2]
    
    # 测试大数字
    n = 100
    large_result = solution.countBits(n)
    print(f"\nTest with n = {n}")
    print(f"The count of 1's in {n} is: {large_result[n]}")
# 一和零 (Ones and Zeroes)
# 题目链接: https://leetcode.cn/problems/ones-and-zeroes/
# 难度: 中等
# 这是一个经典的二维费用01背包问题
from typing import List

class Solution:
    # 方法1: 暴力递归（超时解法，仅作为思路展示）
    # 时间复杂度: O(2^n) - 每个字符串有选或不选两种选择
    # 空间复杂度: O(n) - 递归调用栈深度
    def findMaxForm1(self, strs: List[str], m: int, n: int) -> int:
        return self._process1(strs, 0, m, n)
    
    # 递归函数: 从index开始选择字符串，剩余m个0和n个1的情况下能选的最大字符串数量
    def _process1(self, strs: List[str], index: int, remainingZeros: int, remainingOnes: int) -> int:
        # 基本情况: 已经处理完所有字符串或没有剩余的0和1了
        if index == len(strs) or (remainingZeros == 0 and remainingOnes == 0):
            return 0
        
        # 计算当前字符串需要的0和1的数量
        zerosNeeded, onesNeeded = self._countZerosOnes(strs[index])
        
        # 选择不使用当前字符串
        notTake = self._process1(strs, index + 1, remainingZeros, remainingOnes)
        
        # 选择使用当前字符串（如果有足够的0和1）
        take = 0
        if zerosNeeded <= remainingZeros and onesNeeded <= remainingOnes:
            take = 1 + self._process1(strs, index + 1, remainingZeros - zerosNeeded, remainingOnes - onesNeeded)
        
        # 返回两种选择中的最大值
        return max(notTake, take)
    
    # 方法2: 记忆化搜索
    # 时间复杂度: O(n * m * k) - n是字符串数量，m是0的数量，k是1的数量
    # 空间复杂度: O(n * m * k) - 备忘录大小
    def findMaxForm2(self, strs: List[str], m: int, n: int) -> int:
        # 创建三维备忘录: [index][zeros][ones]
        # 在Python中使用元组作为键更高效
        memo = {}
        return self._process2(strs, 0, m, n, memo)
    
    def _process2(self, strs: List[str], index: int, remainingZeros: int, remainingOnes: int, memo: dict) -> int:
        if index == len(strs) or (remainingZeros == 0 and remainingOnes == 0):
            return 0
        
        # 使用元组作为备忘录的键
        key = (index, remainingZeros, remainingOnes)
        if key in memo:
            return memo[key]
        
        # 计算当前字符串需要的0和1的数量
        zerosNeeded, onesNeeded = self._countZerosOnes(strs[index])
        
        # 不选当前字符串
        notTake = self._process2(strs, index + 1, remainingZeros, remainingOnes, memo)
        
        # 选当前字符串
        take = 0
        if zerosNeeded <= remainingZeros and onesNeeded <= remainingOnes:
            take = 1 + self._process2(strs, index + 1, remainingZeros - zerosNeeded, remainingOnes - onesNeeded, memo)
        
        # 记录结果
        memo[key] = max(notTake, take)
        return memo[key]
    
    # 方法3: 动态规划（三维DP）
    # 时间复杂度: O(n * m * k) - n是字符串数量，m是0的数量，k是1的数量
    # 空间复杂度: O(n * m * k) - dp数组大小
    def findMaxForm3(self, strs: List[str], m: int, n: int) -> int:
        len_strs = len(strs)
        # dp[i][j][k]表示前i个字符串，使用j个0和k个1能得到的最大字符串数量
        dp = [[[0] * (n + 1) for _ in range(m + 1)] for _ in range(len_strs + 1)]
        
        # 遍历每个字符串
        for i in range(1, len_strs + 1):
            # 计算当前字符串的0和1的数量
            zeros, ones = self._countZerosOnes(strs[i - 1])
            
            # 遍历所有可能的0和1的数量
            for j in range(m + 1):
                for k in range(n + 1):
                    # 不选当前字符串的情况
                    dp[i][j][k] = dp[i - 1][j][k]
                    
                    # 选当前字符串的情况（如果有足够的0和1）
                    if j >= zeros and k >= ones:
                        dp[i][j][k] = max(dp[i][j][k], dp[i - 1][j - zeros][k - ones] + 1)
        
        return dp[len_strs][m][n]
    
    # 方法4: 动态规划（空间优化，二维DP）
    # 时间复杂度: O(n * m * k) - 与方法3相同
    # 空间复杂度: O(m * k) - 优化为二维数组
    def findMaxForm4(self, strs: List[str], m: int, n: int) -> int:
        # dp[j][k]表示使用j个0和k个1能得到的最大字符串数量
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 遍历每个字符串
        for s in strs:
            zeros, ones = self._countZerosOnes(s)
            
            # 注意：这里需要从后往前遍历，避免重复选择同一字符串
            for j in range(m, zeros - 1, -1):
                for k in range(n, ones - 1, -1):
                    # 状态转移方程：选择当前字符串或不选择
                    dp[j][k] = max(dp[j][k], dp[j - zeros][k - ones] + 1)
        
        return dp[m][n]
    
    # 辅助方法：计算字符串中0和1的数量
    def _countZerosOnes(self, s: str) -> tuple:
        zeros = 0
        ones = 0
        for c in s:
            if c == '0':
                zeros += 1
            else:
                ones += 1
        return zeros, ones

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 标准测试
    strs1 = ["10", "0001", "111001", "1", "0"]
    m1 = 5
    n1 = 3
    print("测试用例1结果:")
    print("记忆化搜索: ", solution.findMaxForm2(strs1, m1, n1))  # 预期输出: 4
    print("三维动态规划: ", solution.findMaxForm3(strs1, m1, n1))  # 预期输出: 4
    print("二维动态规划: ", solution.findMaxForm4(strs1, m1, n1))  # 预期输出: 4
    
    # 测试用例2: 简单测试
    strs2 = ["10", "0", "1"]
    m2 = 1
    n2 = 1
    print("\n测试用例2结果:")
    print("二维动态规划: ", solution.findMaxForm4(strs2, m2, n2))  # 预期输出: 2
    
    # 测试用例3: 边界情况 - 空字符串数组
    strs3 = []
    m3 = 0
    n3 = 0
    print("\n测试用例3结果:")
    print("二维动态规划: ", solution.findMaxForm4(strs3, m3, n3))  # 预期输出: 0
    
    # 测试用例4: 边界情况 - 无可用的0和1
    strs4 = ["0", "1"]
    m4 = 0
    n4 = 0
    print("\n测试用例4结果:")
    print("二维动态规划: ", solution.findMaxForm4(strs4, m4, n4))  # 预期输出: 0
    
    # 测试用例5: 大型测试
    strs5 = ["011111", "001", "001", "000", "1111111", "011", "111111", "101111", "11111", "11001111"]
    m5 = 90
    n5 = 66
    print("\n测试用例5结果:")
    print("二维动态规划: ", solution.findMaxForm4(strs5, m5, n5))  # 预期输出: 10
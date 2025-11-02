"""
最长递增子序列的个数
给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数 。
注意 这个数列必须是 严格 递增的。

示例 1:
输入: [1,3,5,4,7]
输出: 2
解释: 有两个最长递增子序列，分别是 [1, 3, 4, 7] 和[1, 3, 5, 7]。

示例 2:
输入: [2,2,2,2,2]
输出: 5
解释: 最长递增子序列的长度是1，并且存在5个子序列的长度为1，因此输出5。

提示:
1 <= nums.length <= 2000
-10^6 <= nums[i] <= 10^6

解题思路：
1. 使用树状数组解决最长递增子序列的个数问题
2. 对于每个元素，我们需要知道以它结尾的最长递增子序列的长度和数量
3. 使用两个树状数组：
   - treeMaxLen[i]维护以数值i结尾的最长递增子序列的长度
   - treeMaxLenCnt[i]维护以数值i结尾的最长递增子序列的数量
4. 遍历数组，对每个元素：
   - 查询小于当前元素的数值中，最长递增子序列的长度和数量
   - 根据查询结果更新当前元素对应的树状数组

时间复杂度分析：
- 离散化排序：O(n log n)
- 遍历数组，每次操作树状数组：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 需要额外数组存储原始数据、排序数据和两个树状数组：O(n)
- 所以总空间复杂度为O(n)

测试链接: https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
"""


class Solution:
    def __init__(self):
        # 最大数组长度
        self.MAXN = 2001
        # 排序数组，用于离散化
        self.sort_arr = [0] * self.MAXN
        # 维护信息 : 以数值i结尾的最长递增子序列，长度是多少
        self.treeMaxLen = [0] * self.MAXN
        # 维护信息 : 以数值i结尾的最长递增子序列，个数是多少
        self.treeMaxLenCnt = [0] * self.MAXN
        # 离散化后数组长度
        self.m = 0
        # 查询结尾数值<=i的最长递增子序列的长度和数量
        self.maxLen = 0
        self.maxLenCnt = 0
    
    def lowbit(self, i):
        """
        lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
        例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
        
        :param i: 输入数字
        :return: 最低位的1所代表的数值
        """
        return i & -i
    
    def query(self, i):
        """
        查询结尾数值<=i的最长递增子序列的长度和数量
        
        :param i: 查询的结束位置
        """
        self.maxLen = self.maxLenCnt = 0
        while i > 0:
            if self.maxLen == self.treeMaxLen[i]:
                # 如果长度相同，数量累加
                self.maxLenCnt += self.treeMaxLenCnt[i]
            elif self.maxLen < self.treeMaxLen[i]:
                # 如果找到更长的长度，更新长度和数量
                self.maxLen = self.treeMaxLen[i]
                self.maxLenCnt = self.treeMaxLenCnt[i]
            i -= self.lowbit(i)
    
    def add(self, i, length, cnt):
        """
        以数值i结尾的最长递增子序列，长度达到了len，个数增加了cnt
        更新树状数组
        
        :param i: 数值
        :param length: 最长递增子序列长度
        :param cnt: 最长递增子序列数量
        """
        while i <= self.m:
            if self.treeMaxLen[i] == length:
                # 如果长度相同，数量累加
                self.treeMaxLenCnt[i] += cnt
            elif self.treeMaxLen[i] < length:
                # 如果找到更长的长度，更新长度和数量
                self.treeMaxLen[i] = length
                self.treeMaxLenCnt[i] = cnt
            i += self.lowbit(i)
    
    def rank(self, v):
        """
        给定原始值v，返回其在离散化数组中的排名（即在排序数组中的位置）
        
        :param v: 原始值
        :return: 排名值(排序部分1~m中的下标)
        """
        ans = 0
        l, r = 1, self.m
        while l <= r:
            mid = (l + r) // 2
            if self.sort_arr[mid] >= v:
                ans = mid
                r = mid - 1
            else:
                l = mid + 1
        return ans
    
    def findNumberOfLIS(self, nums):
        """
        计算最长递增子序列的个数
        
        :param nums: 输入数组
        :return: 最长递增子序列的个数
        """
        n = len(nums)
        for i in range(1, n + 1):
            self.sort_arr[i] = nums[i - 1]
        
        # 排序
        sorted_arr = sorted(self.sort_arr[1:n + 1])
        for i in range(n):
            self.sort_arr[i + 1] = sorted_arr[i]
        
        self.m = 1
        for i in range(2, n + 1):
            if self.sort_arr[self.m] != self.sort_arr[i]:
                self.m += 1
                self.sort_arr[self.m] = self.sort_arr[i]
        
        # 初始化树状数组
        for i in range(1, self.m + 1):
            self.treeMaxLen[i] = 0
            self.treeMaxLenCnt[i] = 0
        
        for num in nums:
            i = self.rank(num)
            # 查询以数值<=i-1结尾的最长递增子序列信息
            self.query(i - 1)
            if self.maxLen == 0:
                # 如果查出数值<=i-1结尾的最长递增子序列长度为0
                # 那么说明，以值i结尾的最长递增子序列长度就是1，计数增加1
                self.add(i, 1, 1)
            else:
                # 如果查出数值<=i-1结尾的最长递增子序列长度为maxLen != 0
                # 那么说明，以值i结尾的最长递增子序列长度就是maxLen + 1，计数增加maxLenCnt
                self.add(i, self.maxLen + 1, self.maxLenCnt)
        
        self.query(self.m)
        return self.maxLenCnt


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 3, 5, 4, 7]
    print("输入: [1,3,5,4,7]")
    print("输出:", solution.findNumberOfLIS(nums1))
    print("期望: 2\n")
    
    # 测试用例2
    nums2 = [2, 2, 2, 2, 2]
    print("输入: [2,2,2,2,2]")
    print("输出:", solution.findNumberOfLIS(nums2))
    print("期望: 5")
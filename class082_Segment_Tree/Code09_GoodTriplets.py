"""
统计数组中好三元组数目
给你两个下标从0 开始且长度为 n 的整数数组 nums1 和 nums2 ，两者都是 [0, 1, ..., n - 1] 的 排列 。
好三元组 指的是 3 个 互不相同 的值，且它们在数组 nums1 和 nums2 中的位置顺序一致。
请你返回好三元组的 总数目 。

示例 1：
输入：nums1 = [2,0,1,3], nums2 = [0,1,2,3]
输出：1
解释：总共有 4 个三元组 (x,y,z) 满足 pos1x < pos1y < pos1z ，分别是 (2,0,1) ，(2,0,3) ，(2,1,3) 和 (0,1,3) 。
这些三元组中，只有 (0,1,3) 满足 pos2x < pos2y < pos2z 。所以只有 1 个好三元组。

示例 2：
输入：nums1 = [4,0,1,3,2], nums2 = [4,1,0,2,3]
输出：4
解释：总共有 4 个好三元组 (4,0,3) ，(4,0,2) ，(4,1,3) 和 (4,1,2) 。

提示：
n == nums1.length == nums2.length
3 <= n <= 10^5
0 <= nums1[i], nums2[i] <= n - 1
nums1 和 nums2 是 [0, 1, ..., n - 1] 的排列。

解题思路：
1. 将问题转换为求公共递增子序列个数
2. 对于每个元素，我们需要统计：
   - 在它左边有多少个元素小于它（形成升序一元组）
   - 在它左边有多少个元素能与它组成升序二元组
3. 使用两个树状数组：
   - tree1[i]维护以数值i结尾的升序一元组数量（即小于i的元素个数）
   - tree2[i]维护以数值i结尾的升序二元组数量
4. 遍历数组，对每个元素：
   - 查询tree2中比当前元素小的元素个数，即为以当前元素为结尾的升序三元组数量
   - 更新tree1中当前元素的计数
   - 查询tree1中比当前元素小的元素个数，更新tree2中当前元素的计数

时间复杂度分析：
- 离散化排序：O(n log n)
- 遍历数组，每次操作树状数组：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 需要额外数组存储原始数据、排序数据和两个树状数组：O(n)
- 所以总空间复杂度为O(n)

测试链接: https://leetcode.cn/problems/count-good-triplets-in-an-array/
"""


class Solution:
    def __init__(self):
        # 最大数组长度
        self.MAXN = 100001
        # 原数组
        self.arr = [0] * self.MAXN
        # 排序数组，用于离散化
        self.sort_arr = [0] * self.MAXN
        # 维护信息 : 课上讲的up1数组
        # tree1不是up1数组，是up1数组的树状数组
        # tree1[i]表示值小于等于i的元素个数（升序一元组数量）
        self.tree1 = [0] * self.MAXN
        # 维护信息 : 课上讲的up2数组
        # tree2不是up2数组，是up2数组的树状数组
        # tree2[i]表示以值i结尾的升序二元组数量
        self.tree2 = [0] * self.MAXN
        # 数组长度和离散化后数组长度
        self.n = 0
        self.m = 0
    
    def lowbit(self, i):
        """
        lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
        例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
        
        :param i: 输入数字
        :return: 最低位的1所代表的数值
        """
        return i & -i
    
    def add(self, tree, i, c):
        """
        单点增加操作：在位置i上增加v
        
        :param tree: 树状数组
        :param i: 位置（从1开始）
        :param c: 增加的值
        """
        # 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while i <= self.m:
            tree[i] += c
            # 移动到父节点
            i += self.lowbit(i)
    
    def sum(self, tree, i):
        """
        查询前缀和：计算从位置1到位置i的所有元素之和
        
        :param tree: 树状数组
        :param i: 查询的结束位置
        :return: 前缀和
        """
        ans = 0
        # 从位置i开始，沿着子节点路径向下累加
        while i > 0:
            ans += tree[i]
            # 移动到前一个相关区间
            i -= self.lowbit(i)
        return ans
    
    def rank(self, v):
        """
        给定原始值v，返回其在离散化数组中的排名（即在排序数组中的位置）
        
        :param v: 原始值
        :return: 排名值(排序部分1~m中的下标)
        """
        l, r = 1, self.m
        ans = 0
        while l <= r:
            mid = (l + r) // 2
            if self.sort_arr[mid] >= v:
                ans = mid
                r = mid - 1
            else:
                l = mid + 1
        return ans
    
    def goodTriplets(self, nums1, nums2):
        """
        计算好三元组数目
        
        :param nums1: 第一个数组
        :param nums2: 第二个数组
        :return: 好三元组数目
        """
        self.n = len(nums1)
        
        # 构建位置映射数组
        pos = [0] * self.n
        for i in range(self.n):
            pos[nums1[i]] = i
        
        # 构建转换后的数组
        for i in range(self.n):
            self.arr[i + 1] = pos[nums2[i]] + 1
        
        # 离散化处理
        for i in range(1, self.n + 1):
            self.sort_arr[i] = self.arr[i]
        
        # 排序
        sorted_arr = sorted(self.sort_arr[1:self.n + 1])
        for i in range(self.n):
            self.sort_arr[i + 1] = sorted_arr[i]
        
        self.m = 1
        for i in range(2, self.n + 1):
            # 去重
            if self.sort_arr[self.m] != self.sort_arr[i]:
                self.m += 1
                self.sort_arr[self.m] = self.sort_arr[i]
        
        # 将原数组元素替换为离散化后的排名
        for i in range(1, self.n + 1):
            self.arr[i] = self.rank(self.arr[i])
        
        # 初始化树状数组
        for i in range(1, self.m + 1):
            self.tree1[i] = 0
            self.tree2[i] = 0
        
        ans = 0
        # 遍历数组，对每个元素计算以它为结尾的升序三元组数量
        for i in range(1, self.n + 1):
            # 查询以当前值做结尾的升序三元组数量
            # 即查询右方有多少数字能与当前数字组成升序二元组
            ans += self.sum(self.tree2, self.arr[i] - 1)
            
            # 更新以当前值做结尾的升序一元组数量（单个元素）
            self.add(self.tree1, self.arr[i], 1)
            
            # 更新以当前值做结尾的升序二元组数量
            # 即当前元素与左方比它小的元素组成的二元组数量
            self.add(self.tree2, self.arr[i], self.sum(self.tree1, self.arr[i] - 1))
        
        return ans


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1_1 = [2, 0, 1, 3]
    nums2_1 = [0, 1, 2, 3]
    print("输入: nums1 = [2,0,1,3], nums2 = [0,1,2,3]")
    print("输出:", solution.goodTriplets(nums1_1, nums2_1))
    print("期望: 1\n")
    
    # 测试用例2
    nums1_2 = [4, 0, 1, 3, 2]
    nums2_2 = [4, 1, 0, 2, 3]
    print("输入: nums1 = [4,0,1,3,2], nums2 = [4,1,0,2,3]")
    print("输出:", solution.goodTriplets(nums1_2, nums2_2))
    print("期望: 4")
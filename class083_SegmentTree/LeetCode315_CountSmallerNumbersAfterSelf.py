"""
Python 线段树实现 - LeetCode 315. Count of Smaller Numbers After Self
题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
题目描述:
给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例 1:
输入: nums = [5,2,6,1]
输出: [2,1,1,0]
解释:
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素

示例 2:
输入: nums = [-1]
输出: [0]

示例 3:
输入: nums = [-1,-1]
输出: [0,0]

提示:
1 <= nums.length <= 10^5
-10^4 <= nums[i] <= 10^4

解题思路:
这是一个经典的逆序对问题，可以使用线段树来解决。
1. 由于数值范围较大(-10^4 到 10^4)，需要进行离散化处理
2. 从右往左遍历数组，对于每个元素:
   - 查询线段树中比当前元素小的元素个数
   - 将当前元素插入线段树
3. 使用线段树维护区间和，支持单点更新和区间查询

时间复杂度: O(n log n)，其中n是数组长度
空间复杂度: O(n)
"""


class SegmentTree:
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 输入数组
        """
        # 离散化处理
        self.unique_nums = sorted(list(set(nums)))
        self.n = len(self.unique_nums)
        
        # 建立映射关系
        self.map = {self.unique_nums[i]: i + 1 for i in range(self.n)}
        
        # 线段树数组，大小为4*n
        self.tree = [0] * (4 * self.n)
        
        # 构建线段树
        self._build(1, self.n, 1)
    
    def _build(self, l, r, i):
        """
        构建线段树
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        # 递归终止条件：到达叶子节点
        if l == r:
            return
        
        # 计算中点
        mid = (l + r) // 2
        # 递归构建左子树
        self._build(l, mid, i << 1)
        # 递归构建右子树
        self._build(mid + 1, r, i << 1 | 1)
    
    def update(self, index, l, r, i):
        """
        单点更新
        :param index: 要更新的位置
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        # 递归终止条件：找到对应的叶子节点
        if l == r:
            self.tree[i] += 1
            return
        
        # 计算中点
        mid = (l + r) // 2
        # 根据索引决定更新左子树还是右子树
        if index <= mid:
            self.update(index, l, mid, i << 1)
        else:
            self.update(index, mid + 1, r, i << 1 | 1)
        
        # 更新当前节点的值
        self._push_up(i)
    
    def _push_up(self, i):
        """
        向上传递
        :param i: 当前节点在tree数组中的索引
        """
        self.tree[i] = self.tree[i << 1] + self.tree[i << 1 | 1]
    
    def query(self, jobl, jobr, l, r, i):
        """
        区间查询
        :param jobl: 查询区间左边界
        :param jobr: 查询区间右边界
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        :return: 区间和
        """
        # 查询区间与当前区间无交集
        if jobl > r or jobr < l:
            return 0
        
        # 查询区间完全包含当前区间
        if jobl <= l and r <= jobr:
            return self.tree[i]
        
        # 计算中点
        mid = (l + r) // 2
        # 递归查询左右子树
        ans = 0
        if jobl <= mid:
            ans += self.query(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
        
        # 合并结果
        return ans
    
    def count_smaller(self, val):
        """
        查询比val小的元素个数
        :param val: 要查询的值
        :return: 比val小的元素个数
        """
        index = self.map[val]
        return self.query(1, index - 1, 1, self.n, 1)
    
    def insert(self, val):
        """
        插入元素
        :param val: 要插入的值
        """
        index = self.map[val]
        self.update(index, 1, self.n, 1)


class Solution:
    def countSmaller(self, nums):
        """
        计算每个元素右侧小于当前元素的元素数量
        :param nums: 输入数组
        :return: 结果数组
        """
        length = len(nums)
        if length == 0:
            return []
        
        result = [0] * length
        st = SegmentTree(nums)
        
        # 从右往左遍历
        for i in range(length - 1, -1, -1):
            # 查询比当前元素小的元素个数
            result[i] = st.count_smaller(nums[i])
            
            # 将当前元素插入线段树
            st.insert(nums[i])
        
        return result


# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [5, 2, 6, 1]
    result1 = solution.countSmaller(nums1)
    print("输入: [5,2,6,1]")
    print("输出: {}".format(result1))
    print("期望: [2,1,1,0]")
    print()
    
    # 测试用例2
    nums2 = [-1]
    result2 = solution.countSmaller(nums2)
    print("输入: [-1]")
    print("输出: {}".format(result2))
    print("期望: [0]")
    print()
    
    # 测试用例3
    nums3 = [-1, -1]
    result3 = solution.countSmaller(nums3)
    print("输入: [-1,-1]")
    print("输出: {}".format(result3))
    print("期望: [0,0]")
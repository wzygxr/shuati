"""
Python 线段树实现 - LeetCode 699. Falling Squares
题目链接: https://leetcode.cn/problems/falling-squares/
题目描述:
在二维平面上的 x 轴上，放置着一些方块。
给你一个二维整数数组 positions ，其中 positions[i] = [lefti, sideLengthi] 表示：
第 i 个方块边长为 sideLengthi ，其左侧边与 x 轴上坐标点 lefti 对齐。
每个方块从一个比目前所有落地方块更高的高度掉落而下，沿 y 轴负方向下落，
直到着陆到另一个正方形的顶边或者是 x 轴上。一个方块仅仅是擦过另一个方块的左侧边或右侧边不算着陆。
一旦着陆，它就会固定在原地，无法移动。
在每个方块掉落后，你需要记录目前所有已经落稳的方块堆叠的最高高度。
返回一个整数数组 ans ，其中 ans[i] 表示在第 i 个方块掉落后堆叠的最高高度。

示例 1:
输入: positions = [[1,2],[2,3],[6,1]]
输出: [2,5,5]
解释:
第1个方块掉落后，最高的堆叠由方块1形成，堆叠的最高高度为2。
第2个方块掉落后，最高的堆叠由方块1和2形成，堆叠的最高高度为5。
第3个方块掉落后，最高的堆叠仍然由方块1和2形成，堆叠的最高高度为5。
因此，返回[2, 5, 5]作为答案。

示例 2:
输入: positions = [[100,100],[200,100]]
输出: [100,100]
解释:
第1个方块掉落后，最高的堆叠由方块1形成，堆叠的最高高度为100。
第2个方块掉落后，最高的堆叠可以由方块1或方块2形成，堆叠的最高高度为100。
注意，方块2擦过方块1的右侧边，但不会算作在方块1上着陆。
因此，返回[100, 100]作为答案。

提示:
1 <= positions.length <= 1000
1 <= lefti <= 10^8
1 <= sideLengthi <= 10^6

解题思路:
这是一个区间更新和区间查询最大值的问题，可以使用线段树来解决。
1. 由于坐标范围较大(10^8)，需要进行离散化处理
2. 对于每个掉落的方块:
   - 查询当前方块覆盖区间内的最大高度
   - 新的高度 = 当前最大高度 + 方块边长
   - 更新当前方块覆盖区间的高度为新高度
   - 记录当前所有方块的最大高度
3. 使用线段树维护区间最大值，支持区间更新和区间查询

时间复杂度: O(n log n)，其中n是方块数量
空间复杂度: O(n)
"""


class SegmentTree:
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 离散化后的坐标数组
        """
        # 离散化处理
        self.unique_nums = sorted(list(set(nums)))
        self.n = len(self.unique_nums)
        
        # 建立映射关系
        self.map = {self.unique_nums[i]: i + 1 for i in range(self.n)}
        
        # 线段树数组，大小为4*n
        self.tree = [0] * (4 * self.n)
        self.lazy = [0] * (4 * self.n)
        self.update_flag = [False] * (4 * self.n)
        
        # 构建线段树
        self._build(1, self.n, 1)
    
    def _build(self, l, r, i):
        """
        构建线段树
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        # 初始化节点信息
        if l == r:
            return
        
        # 计算中点
        mid = (l + r) // 2
        # 递归构建左子树
        self._build(l, mid, i << 1)
        # 递归构建右子树
        self._build(mid + 1, r, i << 1 | 1)
    
    def _push_up(self, i):
        """
        向上传递
        :param i: 当前节点在tree数组中的索引
        """
        self.tree[i] = max(self.tree[i << 1], self.tree[i << 1 | 1])
    
    def _push_down(self, i):
        """
        懒标记下发
        :param i: 当前节点在tree数组中的索引
        """
        if self.update_flag[i]:
            # 下发给左子树
            self._lazy(self._left_child(i), self.lazy[i])
            # 下发给右子树
            self._lazy(self._right_child(i), self.lazy[i])
            # 清除父节点的懒标记
            self.update_flag[i] = False
    
    def _lazy(self, i, val):
        """
        懒标记更新
        :param i: 节点索引
        :param val: 更新值
        """
        self.tree[i] = val
        self.lazy[i] = val
        self.update_flag[i] = True
    
    def _left_child(self, i):
        """
        获取左子节点索引
        :param i: 父节点索引
        :return: 左子节点索引
        """
        return i << 1
    
    def _right_child(self, i):
        """
        获取右子节点索引
        :param i: 父节点索引
        :return: 右子节点索引
        """
        return i << 1 | 1
    
    def update(self, jobl, jobr, val, l, r, i):
        """
        区间更新
        :param jobl: 更新区间左边界
        :param jobr: 更新区间右边界
        :param val: 更新值
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        if jobl <= l and r <= jobr:
            self._lazy(i, val)
            return
        self._push_down(i)
        mid = (l + r) // 2
        if jobl <= mid:
            self.update(jobl, jobr, val, l, mid, self._left_child(i))
        if jobr > mid:
            self.update(jobl, jobr, val, mid + 1, r, self._right_child(i))
        self._push_up(i)
    
    def query(self, jobl, jobr, l, r, i):
        """
        区间查询最大值
        :param jobl: 查询区间左边界
        :param jobr: 查询区间右边界
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        :return: 区间最大值
        """
        if jobl <= l and r <= jobr:
            return self.tree[i]
        self._push_down(i)
        mid = (l + r) // 2
        ans = 0
        if jobl <= mid:
            ans = max(ans, self.query(jobl, jobr, l, mid, self._left_child(i)))
        if jobr > mid:
            ans = max(ans, self.query(jobl, jobr, mid + 1, r, self._right_child(i)))
        return ans


class Solution:
    def fallingSquares(self, positions):
        """
        计算每个方块掉落后堆叠的最高高度
        :param positions: 方块位置信息 [[left, sideLength], ...]
        :return: 每个方块掉落后堆叠的最高高度列表
        """
        result = []
        
        # 特殊情况处理
        if not positions:
            return result
        
        # 收集所有坐标点并离散化
        coords = set()
        for left, side in positions:
            coords.add(left)
            coords.add(left + side)
        
        # 创建线段树
        st = SegmentTree(list(coords))
        
        # 记录全局最大高度
        max_height = 0
        
        # 处理每个方块
        for left, side in positions:
            right = left + side
            
            # 获取离散化后的坐标
            l = st.map[left]
            r = st.map[right]
            
            # 查询当前区间内的最大高度
            current_max = st.query(l, r - 1, 1, st.n, 1)
            
            # 计算新高度
            new_height = current_max + side
            
            # 更新区间高度
            st.update(l, r - 1, new_height, 1, st.n, 1)
            
            # 更新全局最大高度
            max_height = max(max_height, new_height)
            
            # 记录当前最大高度
            result.append(max_height)
        
        return result


# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    positions1 = [[1, 2], [2, 3], [6, 1]]
    result1 = solution.fallingSquares(positions1)
    print("输入: [[1,2],[2,3],[6,1]]")
    print("输出: {}".format(result1))
    print("期望: [2,5,5]")
    print()
    
    # 测试用例2
    positions2 = [[100, 100], [200, 100]]
    result2 = solution.fallingSquares(positions2)
    print("输入: [[100,100],[200,100]]")
    print("输出: {}".format(result2))
    print("期望: [100,100]")
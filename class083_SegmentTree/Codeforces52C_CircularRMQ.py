"""
Codeforces 52C - Circular RMQ
题目：环形数组的区间最小值查询和区间加法
来源：Codeforces
网址：https://codeforces.com/problemset/problem/52/C

支持环形数组的区间最小值查询和区间加法
时间复杂度：
  - 建树：O(n)
  - 区间修改：O(log n)
  - 区间查询：O(log n)
空间复杂度：O(n)
"""

import sys

class CircularRMQ:
    def __init__(self, nums):
        """
        初始化线段树
        Args:
            nums: 原始数组
        """
        self.n = len(nums)
        self.tree = [0] * (4 * self.n)  # 线段树数组
        self.lazy = [0] * (4 * self.n)  # 懒标记数组
        self._build(0, 0, self.n - 1, nums)
    
    def _build(self, idx, l, r, nums):
        """
        递归构建线段树
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            nums: 原始数组
        """
        if l == r:
            self.tree[idx] = nums[l]
            return
        
        mid = (l + r) // 2
        self._build(2 * idx + 1, l, mid, nums)
        self._build(2 * idx + 2, mid + 1, r, nums)
        self.tree[idx] = min(self.tree[2 * idx + 1], self.tree[2 * idx + 2])
    
    def _push_down(self, idx):
        """
        下推懒标记
        Args:
            idx: 当前节点索引
        """
        if self.lazy[idx] != 0:
            self.tree[2 * idx + 1] += self.lazy[idx]
            self.tree[2 * idx + 2] += self.lazy[idx]
            self.lazy[2 * idx + 1] += self.lazy[idx]
            self.lazy[2 * idx + 2] += self.lazy[idx]
            self.lazy[idx] = 0
    
    def _update_range(self, idx, l, r, ql, qr, val):
        """
        区间更新
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            ql, qr: 要更新的区间
            val: 要增加的值
        """
        if ql <= l and r <= qr:
            self.tree[idx] += val
            self.lazy[idx] += val
            return
        
        self._push_down(idx)
        mid = (l + r) // 2
        
        if ql <= mid:
            self._update_range(2 * idx + 1, l, mid, ql, qr, val)
        if qr > mid:
            self._update_range(2 * idx + 2, mid + 1, r, ql, qr, val)
        
        self.tree[idx] = min(self.tree[2 * idx + 1], self.tree[2 * idx + 2])
    
    def _query_range(self, idx, l, r, ql, qr):
        """
        区间查询
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            ql, qr: 要查询的区间
        Returns:
            区间最小值
        """
        if ql <= l and r <= qr:
            return self.tree[idx]
        
        self._push_down(idx)
        mid = (l + r) // 2
        min_val = sys.maxsize
        
        if ql <= mid:
            min_val = min(min_val, self._query_range(2 * idx + 1, l, mid, ql, qr))
        if qr > mid:
            min_val = min(min_val, self._query_range(2 * idx + 2, mid + 1, r, ql, qr))
        
        return min_val
    
    def circular_update(self, l, r, val):
        """
        处理环形区间更新
        Args:
            l: 起始位置
            r: 结束位置
            val: 要增加的值
        """
        if l <= r:
            # 正常区间
            self._update_range(0, 0, self.n - 1, l, r, val)
        else:
            # 环形区间：从l到末尾，从开头到r
            self._update_range(0, 0, self.n - 1, l, self.n - 1, val)
            self._update_range(0, 0, self.n - 1, 0, r, val)
    
    def circular_query(self, l, r):
        """
        处理环形区间查询
        Args:
            l: 起始位置
            r: 结束位置
        Returns:
            区间最小值
        """
        if l <= r:
            # 正常区间
            return self._query_range(0, 0, self.n - 1, l, r)
        else:
            # 环形区间：从l到末尾，从开头到r
            min1 = self._query_range(0, 0, self.n - 1, l, self.n - 1)
            min2 = self._query_range(0, 0, self.n - 1, 0, r)
            return min(min1, min2)

# 测试代码
if __name__ == "__main__":
    nums = [1, 2, 3, 4, 5]
    st = CircularRMQ(nums)
    
    # 正常区间查询
    print(f"正常区间[0,2]最小值: {st.circular_query(0, 2)}")  # 1
    
    # 环形区间查询：从4到1 (4->末尾->开头->1)
    print(f"环形区间[4,1]最小值: {st.circular_query(4, 1)}")  # 1
    
    # 环形区间更新：从4到1加2
    st.circular_update(4, 1, 2)
    print(f"更新后环形区间[4,1]最小值: {st.circular_query(4, 1)}")  # 3
    
    # 验证更新结果
    print(f"位置0的值: {st.circular_query(0, 0)}")  # 3
    print(f"位置4的值: {st.circular_query(4, 4)}")  # 7
    
    # 测试异常处理
    try:
        st.circular_query(-1, 2)
    except Exception as e:
        print(f"异常测试: {e}")
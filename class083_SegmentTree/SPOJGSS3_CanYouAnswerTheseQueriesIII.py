"""
SPOJ GSS3 - Can you answer these queries III
题目：支持单点修改和查询区间最大子段和
来源：SPOJ
网址：https://www.spoj.com/problems/GSS3/

线段树维护四个信息：
1. 区间和 sum
2. 区间最大前缀和 lmax
3. 区间最大后缀和 rmax
4. 区间最大子段和 max

时间复杂度：
  - 建树：O(n)
  - 单点修改：O(log n)
  - 区间查询：O(log n)
空间复杂度：O(n)
"""

class Node:
    def __init__(self, sum_val=0, lmax=None, rmax=None, max_val=None):
        self.sum = sum_val
        self.lmax = lmax if lmax is not None else -10**9
        self.rmax = rmax if rmax is not None else -10**9
        self.max_val = max_val if max_val is not None else -10**9

class SPOJGSS3_CanYouAnswerTheseQueriesIII:
    def __init__(self, nums):
        """
        初始化线段树
        Args:
            nums: 原始数组
        """
        self.n = len(nums)
        self.tree = [Node() for _ in range(4 * self.n)]
        self._build(0, 0, self.n - 1, nums)
    
    def _merge(self, left, right):
        """
        合并左右子树信息
        Args:
            left: 左子树节点
            right: 右子树节点
        Returns:
            合并后的节点
        """
        if left.max_val == -10**9:
            return right
        if right.max_val == -10**9:
            return left
        
        res = Node()
        res.sum = left.sum + right.sum
        res.lmax = max(left.lmax, left.sum + right.lmax)
        res.rmax = max(right.rmax, right.sum + left.rmax)
        res.max_val = max(left.max_val, right.max_val, left.rmax + right.lmax)
        
        return res
    
    def _build(self, idx, l, r, nums):
        """
        递归构建线段树
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            nums: 原始数组
        """
        if l == r:
            val = nums[l]
            self.tree[idx] = Node(val, val, val, val)
            return
        
        mid = (l + r) // 2
        self._build(2 * idx + 1, l, mid, nums)
        self._build(2 * idx + 2, mid + 1, r, nums)
        self.tree[idx] = self._merge(self.tree[2 * idx + 1], self.tree[2 * idx + 2])
    
    def update(self, pos, val):
        """
        单点更新
        Args:
            pos: 要更新的位置
            val: 新的值
        """
        if pos < 0 or pos >= self.n:
            raise ValueError("Invalid position")
        self._update(0, 0, self.n - 1, pos, val)
    
    def _update(self, idx, l, r, pos, val):
        """
        递归更新
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            pos: 要更新的位置
            val: 新的值
        """
        if l == r:
            self.tree[idx] = Node(val, val, val, val)
            return
        
        mid = (l + r) // 2
        if pos <= mid:
            self._update(2 * idx + 1, l, mid, pos, val)
        else:
            self._update(2 * idx + 2, mid + 1, r, pos, val)
        
        self.tree[idx] = self._merge(self.tree[2 * idx + 1], self.tree[2 * idx + 2])
    
    def query(self, ql, qr):
        """
        区间查询最大子段和
        Args:
            ql, qr: 要查询的区间
        Returns:
            最大子段和
        """
        if ql < 0 or qr >= self.n or ql > qr:
            raise ValueError("Invalid range")
        
        res = self._query(0, 0, self.n - 1, ql, qr)
        return res.max_val
    
    def _query(self, idx, l, r, ql, qr):
        """
        递归查询
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            ql, qr: 要查询的区间
        Returns:
            查询结果节点
        """
        if ql <= l and r <= qr:
            return self.tree[idx]
        
        mid = (l + r) // 2
        left_res = Node()
        right_res = Node()
        
        if ql <= mid:
            left_res = self._query(2 * idx + 1, l, mid, ql, qr)
        if qr > mid:
            right_res = self._query(2 * idx + 2, mid + 1, r, ql, qr)
        
        return self._merge(left_res, right_res)

# 测试代码
if __name__ == "__main__":
    nums = [-1, 2, 3, -4, 5, -6]
    st = SPOJGSS3_CanYouAnswerTheseQueriesIII(nums)
    
    # 查询区间最大子段和
    print(f"区间[0,5]最大子段和: {st.query(0, 5)}")  # 2+3-4+5=6
    
    # 单点修改
    st.update(0, 10)
    print(f"修改后区间[0,5]最大子段和: {st.query(0, 5)}")  # 10+2+3-4+5=16
    
    # 查询子区间
    print(f"区间[1,4]最大子段和: {st.query(1, 4)}")  # 2+3-4+5=6
    
    # 测试异常处理
    try:
        st.query(-1, 2)
    except ValueError as e:
        print(f"异常测试: {e}")
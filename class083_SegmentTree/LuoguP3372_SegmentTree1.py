"""
Luogu P3372 - 【模板】线段树 1
题目：区间修改（加法），区间查询（求和）
来源：洛谷
网址：https://www.luogu.com.cn/problem/P3372

线段树模板题，支持区间加法和区间求和查询
时间复杂度：
  - 建树：O(n)
  - 区间修改：O(log n)
  - 区间查询：O(log n)
空间复杂度：O(n)
"""

class SegmentTree:
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
        self.tree[idx] = self.tree[2 * idx + 1] + self.tree[2 * idx + 2]
    
    def _push_down(self, idx, l, r):
        """
        下推懒标记
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
        """
        if self.lazy[idx] != 0:
            mid = (l + r) // 2
            # 更新左子树
            self.tree[2 * idx + 1] += self.lazy[idx] * (mid - l + 1)
            self.lazy[2 * idx + 1] += self.lazy[idx]
            # 更新右子树
            self.tree[2 * idx + 2] += self.lazy[idx] * (r - mid)
            self.lazy[2 * idx + 2] += self.lazy[idx]
            # 清除当前节点的懒标记
            self.lazy[idx] = 0
    
    def update_range(self, idx, l, r, ql, qr, val):
        """
        区间更新
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            ql, qr: 要更新的区间
            val: 要增加的值
        """
        if ql <= l and r <= qr:
            self.tree[idx] += val * (r - l + 1)
            self.lazy[idx] += val
            return
        
        self._push_down(idx, l, r)
        mid = (l + r) // 2
        
        if ql <= mid:
            self.update_range(2 * idx + 1, l, mid, ql, qr, val)
        if qr > mid:
            self.update_range(2 * idx + 2, mid + 1, r, ql, qr, val)
        
        self.tree[idx] = self.tree[2 * idx + 1] + self.tree[2 * idx + 2]
    
    def query_range(self, idx, l, r, ql, qr):
        """
        区间查询
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            ql, qr: 要查询的区间
        Returns:
            区间和
        """
        if ql <= l and r <= qr:
            return self.tree[idx]
        
        self._push_down(idx, l, r)
        mid = (l + r) // 2
        total = 0
        
        if ql <= mid:
            total += self.query_range(2 * idx + 1, l, mid, ql, qr)
        if qr > mid:
            total += self.query_range(2 * idx + 2, mid + 1, r, ql, qr)
        
        return total
    
    def update(self, l, r, val):
        """
        对外接口：区间更新
        Args:
            l, r: 要更新的区间
            val: 要增加的值
        """
        if l < 0 or r >= self.n or l > r:
            raise ValueError("Invalid range")
        self.update_range(0, 0, self.n - 1, l, r, val)
    
    def query(self, l, r):
        """
        对外接口：区间查询
        Args:
            l, r: 要查询的区间
        Returns:
            区间和
        """
        if l < 0 or r >= self.n or l > r:
            raise ValueError("Invalid range")
        return self.query_range(0, 0, self.n - 1, l, r)

# 测试代码
if __name__ == "__main__":
    nums = [1, 2, 3, 4, 5]
    st = SegmentTree(nums)
    
    # 测试查询
    print(f"初始区间和[0,2]: {st.query(0, 2)}")  # 1+2+3=6
    
    # 测试区间更新
    st.update(1, 3, 2)  # 给索引1-3的元素加2
    print(f"更新后区间和[0,2]: {st.query(0, 2)}")  # 1+4+5=10
    print(f"区间和[1,4]: {st.query(1, 4)}")  # 4+5+6+5=20
    
    # 测试异常处理
    try:
        st.query(-1, 2)
    except ValueError as e:
        print(f"异常测试: {e}")
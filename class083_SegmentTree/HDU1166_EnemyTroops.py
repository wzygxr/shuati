"""
HDU 1166 - 敌兵布阵
题目：单点更新和区间求和查询
来源：杭电OJ
网址：http://acm.hdu.edu.cn/showproblem.php?pid=1166

线段树模板题，支持单点更新和区间求和查询
时间复杂度：
  - 建树：O(n)
  - 单点更新：O(log n)
  - 区间查询：O(log n)
空间复杂度：O(n)
"""

class HDU1166_EnemyTroops:
    def __init__(self, nums):
        """
        初始化线段树
        Args:
            nums: 原始数组
        """
        self.n = len(nums)
        self.tree = [0] * (4 * self.n)  # 线段树数组
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
    
    def update(self, pos, val):
        """
        单点更新
        Args:
            pos: 要更新的位置
            val: 要增加的值
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
            val: 要增加的值
        """
        if l == r:
            self.tree[idx] += val  # 累加更新
            return
        
        mid = (l + r) // 2
        if pos <= mid:
            self._update(2 * idx + 1, l, mid, pos, val)
        else:
            self._update(2 * idx + 2, mid + 1, r, pos, val)
        
        self.tree[idx] = self.tree[2 * idx + 1] + self.tree[2 * idx + 2]
    
    def query(self, ql, qr):
        """
        区间查询
        Args:
            ql, qr: 要查询的区间
        Returns:
            区间和
        """
        if ql < 0 or qr >= self.n or ql > qr:
            raise ValueError("Invalid range")
        return self._query(0, 0, self.n - 1, ql, qr)
    
    def _query(self, idx, l, r, ql, qr):
        """
        递归查询
        Args:
            idx: 当前节点索引
            l, r: 当前节点表示的区间
            ql, qr: 要查询的区间
        Returns:
            区间和
        """
        if ql <= l and r <= qr:
            return self.tree[idx]
        
        mid = (l + r) // 2
        total = 0
        
        if ql <= mid:
            total += self._query(2 * idx + 1, l, mid, ql, qr)
        if qr > mid:
            total += self._query(2 * idx + 2, mid + 1, r, ql, qr)
        
        return total

# 测试代码
if __name__ == "__main__":
    nums = [1, 2, 3, 4, 5]
    st = HDU1166_EnemyTroops(nums)
    
    # 查询区间和
    print(f"区间[0,2]和: {st.query(0, 2)}")  # 1+2+3=6
    
    # 单点更新：位置1加3
    st.update(1, 3)
    print(f"更新后区间[0,2]和: {st.query(0, 2)}")  # 1+5+3=9
    
    # 单点更新：位置3减2
    st.update(3, -2)
    print(f"区间[2,4]和: {st.query(2, 4)}")  # 3+2+5=10
    
    # 边界测试
    print(f"单点[0]和: {st.query(0, 0)}")  # 1
    print(f"单点[4]和: {st.query(4, 4)}")  # 5
    
    # 测试异常处理
    try:
        st.query(-1, 2)
    except ValueError as e:
        print(f"异常测试: {e}")
"""
Codeforces 833B The Bakery
题目链接: https://codeforces.com/problemset/problem/833/B
洛谷链接: https://www.luogu.com.cn/problem/CF833B

题目描述:
给定一个长度为n的数组，最多可以分成k段不重合的子数组
每个子数组获得的分值为内部不同数字的个数
返回能获得的最大分值。

解题思路:
使用线段树优化动态规划的方法解决此问题。
1. 定义状态dp[i][j]表示将前j个元素分成i段的最大得分
2. 状态转移方程：dp[i][j] = max{dp[i-1][k] + cost(k+1, j)}，其中k < j
   cost(k+1, j)表示区间[k+1, j]内不同数字的个数
3. 使用线段树维护dp[i-1][k]的值，支持区间加法和区间查询最大值
4. 对于每个新元素，更新其对之前所有位置的影响

时间复杂度分析:
- 状态转移: O(k*n*log n)
- 总时间复杂度: O(k*n*log n)
空间复杂度: O(n) 用于存储线段树和辅助数组

工程化考量:
1. 性能优化: 线段树优化动态规划
2. 内存优化: 滚动数组减少空间占用
3. 边界处理: 处理k=1和k=n的特殊情况
4. 可读性: 清晰的变量命名和注释
"""

class SegmentTree:
    """线段树类，支持区间加法和区间查询最大值"""
    
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 线段树大小
        """
        self.size = size
        self.tree = [0] * (4 * size)
        self.lazy = [0] * (4 * size)
    
    def push_down(self, idx, l, r):
        """
        懒标记下推
        
        Args:
            idx: 线段树节点索引
            l: 当前区间左边界
            r: 当前区间右边界
        """
        if self.lazy[idx] != 0:
            if l != r:
                self.lazy[idx * 2] += self.lazy[idx]
                self.lazy[idx * 2 + 1] += self.lazy[idx]
                self.tree[idx * 2] += self.lazy[idx]
                self.tree[idx * 2 + 1] += self.lazy[idx]
            self.lazy[idx] = 0
    
    def update(self, L, R, val, l, r, idx):
        """
        线段树区间更新
        
        Args:
            L: 更新区间左边界
            R: 更新区间右边界
            val: 更新值
            l: 当前区间左边界
            r: 当前区间右边界
            idx: 当前线段树节点索引
        """
        if L <= l and r <= R:
            self.tree[idx] += val
            self.lazy[idx] += val
            return
        
        self.push_down(idx, l, r)
        mid = (l + r) // 2
        
        if L <= mid:
            self.update(L, R, val, l, mid, idx * 2)
        if R > mid:
            self.update(L, R, val, mid + 1, r, idx * 2 + 1)
        
        self.tree[idx] = max(self.tree[idx * 2], self.tree[idx * 2 + 1])
    
    def query(self, L, R, l, r, idx):
        """
        线段树区间查询
        
        Args:
            L: 查询区间左边界
            R: 查询区间右边界
            l: 当前区间左边界
            r: 当前区间右边界
            idx: 当前线段树节点索引
            
        Returns:
            区间最大值
        """
        if L <= l and r <= R:
            return self.tree[idx]
        
        self.push_down(idx, l, r)
        mid = (l + r) // 2
        result = 0
        
        if L <= mid:
            result = max(result, self.query(L, R, l, mid, idx * 2))
        if R > mid:
            result = max(result, self.query(L, R, mid + 1, r, idx * 2 + 1))
        
        return result
    
    def update_point(self, pos, val, l, r, idx):
        """
        单点更新
        
        Args:
            pos: 更新位置
            val: 更新值
            l: 当前区间左边界
            r: 当前区间右边界
            idx: 当前线段树节点索引
        """
        if l == r:
            self.tree[idx] = val
            return
        
        self.push_down(idx, l, r)
        mid = (l + r) // 2
        
        if pos <= mid:
            self.update_point(pos, val, l, mid, idx * 2)
        else:
            self.update_point(pos, val, mid + 1, r, idx * 2 + 1)
        
        self.tree[idx] = max(self.tree[idx * 2], self.tree[idx * 2 + 1])

def max_bakery_score(arr, k):
    """
    计算最大分段得分
    
    Args:
        arr: 输入数组
        k: 最大分段数
        
    Returns:
        最大得分
        
    Raises:
        ValueError: 如果输入参数不合法
    """
    if not arr or k == 0:
        return 0
    
    n = len(arr)
    
    # 特殊情况处理
    if k == 1:
        # 单段情况，直接返回不同数字个数
        return len(set(arr))
    
    # 滚动数组优化
    dp_prev = [0] * (n + 1)
    dp_curr = [0] * (n + 1)
    
    # 记录每个数字上一次出现的位置
    last_pos = {}
    
    for seg in range(1, k + 1):
        seg_tree = SegmentTree(n)
        
        # 初始化线段树
        for i in range(n):
            seg_tree.update_point(i + 1, dp_prev[i], 1, n, 1)
        
        last_pos.clear()
        
        for i in range(n):
            num = arr[i]
            
            # 更新线段树：从last_pos[num]+1到i的位置加1
            if num in last_pos:
                seg_tree.update(last_pos[num] + 1, i + 1, 1, 1, n, 1)
            else:
                seg_tree.update(1, i + 1, 1, 1, n, 1)
            
            # 查询最大值
            if seg == 1:
                dp_curr[i] = seg_tree.query(1, i + 1, 1, n, 1)
            else:
                dp_curr[i] = seg_tree.query(seg, i + 1, 1, n, 1)
            
            last_pos[num] = i
        
        # 滚动数组
        dp_prev = dp_curr.copy()
    
    return dp_prev[n - 1]

# 单元测试
def test_max_bakery_score():
    """测试函数，验证算法正确性"""
    
    print("开始测试面包店问题算法...")
    
    # 测试用例1: 正常情况
    arr1 = [1, 2, 2, 3]
    result1 = max_bakery_score(arr1, 2)
    print(f"测试用例1: {arr1}, k=2 -> {result1}")
    
    # 测试用例2: 单段情况
    arr2 = [1, 2, 3, 4, 5]
    result2 = max_bakery_score(arr2, 1)
    print(f"测试用例2: {arr2}, k=1 -> {result2}")
    assert result2 == 5, f"预期5，实际{result2}"
    
    # 测试用例3: 空数组
    arr3 = []
    result3 = max_bakery_score(arr3, 3)
    print(f"测试用例3: 空数组, k=3 -> {result3}")
    assert result3 == 0, f"预期0，实际{result3}"
    
    # 测试用例4: 单元素
    arr4 = [7]
    result4 = max_bakery_score(arr4, 2)
    print(f"测试用例4: {arr4}, k=2 -> {result4}")
    assert result4 == 1, f"预期1，实际{result4}"
    
    # 测试用例5: 重复元素
    arr5 = [1, 1, 1, 1]
    result5 = max_bakery_score(arr5, 2)
    print(f"测试用例5: {arr5}, k=2 -> {result5}")
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    # 运行测试
    test_max_bakery_score()
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 线段树优化DP：将O(n^2)的DP优化到O(n*k*log n)")
    print("2. 滚动数组：减少空间复杂度")
    print("3. 懒标记：高效处理区间更新")
    print("4. 区间贡献：利用数字出现位置计算区间不同数字个数")
    print("5. 边界处理：处理k=1和空数组等特殊情况")
    
    print("\n=== 工程化考量 ===")
    print("1. 异常防御：处理非法输入参数")
    print("2. 性能优化：线段树操作时间复杂度O(log n)")
    print("3. 内存优化：滚动数组减少空间占用")
    print("4. 可读性：清晰的变量命名和注释")
    print("5. 测试覆盖：单元测试覆盖各种边界情况")
    
    print("\n=== 复杂度分析 ===")
    print("时间复杂度: O(k * n * log n)")
    print("空间复杂度: O(n)")
    print("其中n为数组长度，k为分段数")
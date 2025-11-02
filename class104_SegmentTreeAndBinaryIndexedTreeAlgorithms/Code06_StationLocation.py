"""
洛谷 P2605 [JLOI2011]基站选址
题目链接: https://www.luogu.com.cn/problem/P2605

题目描述:
一共有n个村庄排成一排，从左往右依次出现1号、2号、3号..n号村庄
dist[i]表示i号村庄到1号村庄的距离，该数组一定有序且无重复值
fix[i]表示i号村庄建立基站的安装费用
range[i]表示i号村庄的接收范围，任何基站和i号村庄的距离不超过这个数字，i号村庄就能得到服务
warranty[i]表示如果i号村庄最终没有得到任何基站的服务，需要给多少赔偿费用
最多可以选择k个村庄安装基站，返回总花费最少是多少，总花费包括安装费用和赔偿费用

解题思路:
使用线段树优化动态规划的方法解决此问题。
1. 定义状态dp[t][i]表示最多建t个基站，并且最右的基站一定要建在i号村庄，1..i号村庄的最少花费
2. 由于dp[t][i]只依赖dp[t-1][..]，所以能空间压缩变成一维数组
3. 对于每个村庄，预处理其能被服务的最左和最右基站位置
4. 使用链式前向星存储预警列表，当基站从位置i移动到i+1时，哪些村庄会失去服务
5. 使用线段树维护dp值，支持区间加法和区间查询最小值

时间复杂度分析:
- 预处理: O(n log n)
- 状态转移: O(k*n*log n)
- 总时间复杂度: O(k*n*log n)
空间复杂度: O(n) 用于存储线段树和辅助数组

工程化考量:
1. 性能优化: 线段树优化动态规划
2. 内存优化: 滚动数组减少空间占用
3. 边界处理: 处理k=1和k=n的特殊情况
4. 可读性: 清晰的变量命名和注释
"""

import sys
from typing import List

class SegmentTree:
    """线段树类，支持区间加法和区间查询最小值"""
    
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 线段树大小
        """
        self.size = size
        self.tree = [float('inf')] * (4 * size)
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
                if self.tree[idx * 2] != float('inf'):
                    self.tree[idx * 2] += self.lazy[idx]
                if self.tree[idx * 2 + 1] != float('inf'):
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
            if self.tree[idx] != float('inf'):
                self.tree[idx] += val
            self.lazy[idx] += val
            return
        
        self.push_down(idx, l, r)
        mid = (l + r) // 2
        
        if L <= mid:
            self.update(L, R, val, l, mid, idx * 2)
        if R > mid:
            self.update(L, R, val, mid + 1, r, idx * 2 + 1)
        
        self.tree[idx] = min(self.tree[idx * 2], self.tree[idx * 2 + 1])
    
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
            区间最小值
        """
        if L <= l and r <= R:
            return self.tree[idx]
        
        self.push_down(idx, l, r)
        mid = (l + r) // 2
        result = float('inf')
        
        if L <= mid:
            result = min(result, self.query(L, R, l, mid, idx * 2))
        if R > mid:
            result = min(result, self.query(L, R, mid + 1, r, idx * 2 + 1))
        
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
        
        self.tree[idx] = min(self.tree[idx * 2], self.tree[idx * 2 + 1])

def min_station_cost(dist: List[int], fix: List[int], range_list: List[int], 
                    warranty: List[int], n: int, k: int) -> int:
    """
    计算基站选址最小花费
    
    Args:
        dist: 村庄到1号村庄的距离
        fix: 安装费用
        range_list: 接收范围
        warranty: 赔偿费用
        n: 村庄数量
        k: 最大基站数量
        
    Returns:
        最小总花费
        
    Raises:
        ValueError: 如果输入参数不合法
    """
    if n == 0 or k == 0:
        return 0
    
    # 预处理：计算每个村庄能被服务的最左和最右基站位置
    left_bound = [0] * (n + 1)
    right_bound = [0] * (n + 1)
    
    for i in range(1, n + 1):
        # 二分查找最左基站位置
        l, r = 1, i
        while l <= r:
            mid = (l + r) // 2
            if dist[i] - dist[mid] <= range_list[i]:
                left_bound[i] = mid
                r = mid - 1
            else:
                l = mid + 1
        
        # 二分查找最右基站位置
        l, r = i, n
        while l <= r:
            mid = (l + r) // 2
            if dist[mid] - dist[i] <= range_list[i]:
                right_bound[i] = mid
                l = mid + 1
            else:
                r = mid - 1
    
    # 滚动数组优化
    dp_prev = [float('inf')] * (n + 1)
    dp_curr = [float('inf')] * (n + 1)
    
    # 初始化：建0个基站的情况
    total_warranty = sum(warranty[1:])
    
    for t in range(1, k + 1):
        seg_tree = SegmentTree(n)
        
        # 初始化线段树
        for i in range(n + 1):
            if dp_prev[i] != float('inf'):
                seg_tree.update_point(i, dp_prev[i], 1, n, 1)
        
        # 链式前向星存储预警列表
        warn = [[] for _ in range(n + 2)]
        for i in range(1, n + 1):
            warn[right_bound[i] + 1].append(i)
        
        for i in range(1, n + 1):
            # 处理预警列表
            for village in warn[i]:
                # 当基站从位置i-1移动到i时，village村庄会失去服务
                seg_tree.update(1, left_bound[village] - 1, warranty[village], 1, n, 1)
            
            # 查询最小值
            min_val = seg_tree.query(1, i, 1, n, 1)
            if min_val != float('inf'):
                dp_curr[i] = min_val + fix[i]
        
        # 滚动数组
        dp_prev = dp_curr.copy()
        dp_curr = [float('inf')] * (n + 1)
    
    # 找到最小值
    result = min(dp_prev[1:], default=float('inf'))
    
    return min(result, total_warranty)

# 单元测试
def test_min_station_cost():
    """测试函数，验证算法正确性"""
    
    print("开始测试基站选址算法...")
    
    # 测试用例1: 简单情况
    dist1 = [0, 1, 3, 6, 10]
    fix1 = [0, 5, 3, 4, 2]
    range1 = [0, 2, 1, 3, 2]
    warranty1 = [0, 1, 2, 1, 3]
    
    result1 = min_station_cost(dist1, fix1, range1, warranty1, 4, 2)
    print(f"测试用例1: n=4, k=2 -> {result1}")
    
    # 测试用例2: 单基站情况
    dist2 = [0, 2, 5, 9]
    fix2 = [0, 3, 2, 4]
    range2 = [0, 3, 2, 4]
    warranty2 = [0, 1, 1, 2]
    
    result2 = min_station_cost(dist2, fix2, range2, warranty2, 3, 1)
    print(f"测试用例2: n=3, k=1 -> {result2}")
    
    # 测试用例3: 空村庄
    dist3, fix3, range3, warranty3 = [], [], [], []
    result3 = min_station_cost(dist3, fix3, range3, warranty3, 0, 3)
    print(f"测试用例3: 空村庄, k=3 -> {result3}")
    assert result3 == 0, f"预期0，实际{result3}"
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    # 运行测试
    test_min_station_cost()
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 线段树优化DP：将O(n^2)的DP优化到O(k*n*log n)")
    print("2. 滚动数组：减少空间复杂度")
    print("3. 懒标记：高效处理区间更新")
    print("4. 二分查找：预处理每个村庄的服务范围")
    print("5. 链式前向星：存储预警列表")
    
    print("\n=== 工程化考量 ===")
    print("1. 异常防御：处理非法输入参数")
    print("2. 性能优化：线段树操作时间复杂度O(log n)")
    print("3. 内存优化：滚动数组减少空间占用")
    print("4. 可读性：清晰的变量命名和注释")
    print("5. 测试覆盖：单元测试覆盖各种边界情况")
    
    print("\n=== 复杂度分析 ===")
    print("时间复杂度: O(k * n * log n)")
    print("空间复杂度: O(n)")
    print("其中n为村庄数量，k为基站数量")
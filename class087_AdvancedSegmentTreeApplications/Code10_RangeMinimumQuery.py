# HDU 5306 Gorgeous Sequence - 区间最值操作
# 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=5306
# 题目描述：
# 维护一个序列 a，执行以下操作：
# 1. 0 l r t: 对于所有的 i ∈ [l,r]，将 a[i] 变成 min(a[i], t)
# 2. 1 l r: 输出 max{a[i] | i ∈ [l,r]}
# 3. 2 l r: 输出 Σ{a[i] | i ∈ [l,r]}
#
# 解题思路：
# 使用吉司机线段树（吉如一算法），这是处理区间最值操作的经典数据结构
# 每个节点维护以下信息：
# - 最大值 mx
# - 次大值 sem
# - 最大值个数 cnt
# - 区间和 sum
#
# 关键技术：
# 1. 势能分析法：通过分析数据结构的势能变化来证明时间复杂度
# 2. 三种更新情况：
#    a. t >= mx: 无需更新，因为所有元素都已经小于等于t
#    b. sem < t < mx: 直接更新最大值，因为只有最大值需要改变
#    c. t <= sem: 递归处理，因为有多个值需要改变
#
# 时间复杂度分析：
# 1. 建树：O(n)
# 2. 区间取min操作：O(n log² n) 均摊
# 3. 区间最大值查询：O(log n)
# 4. 区间和查询：O(log n)
# 5. 空间复杂度：O(n)
#
# 是否最优解：是
# 这是解决区间最值操作问题的最优解法，由吉如一提出

import sys
sys.setrecursionlimit(1000000)

class SegmentTree:
    """
    吉司机线段树类
    用于处理区间最值操作问题
    """
    
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size (int): 数组大小
        """
        self.n = size
        # 线段树数组大小通常设置为4*size以保证足够空间
        self.mx = [0] * (4 * size)      # 区间最大值
        self.sem = [0] * (4 * size)     # 区间次大值
        self.cnt = [0] * (4 * size)     # 最大值个数
        self.sum = [0] * (4 * size)     # 区间和
        self.lazy = [float('inf')] * (4 * size)  # 懒惰标记，用于延迟更新
    
    def push_up(self, rt):
        """
        合并左右子节点信息，更新父节点
        这是线段树的核心操作之一
        
        Args:
            rt (int): 当前节点索引
        """
        # 获取左右子节点索引
        l, r = rt * 2, rt * 2 + 1
        # 父节点的区间和等于左右子节点区间和的和
        self.sum[rt] = self.sum[l] + self.sum[r]
        
        # 根据左右子节点的最大值关系更新父节点信息
        if self.mx[l] > self.mx[r]:
            # 左子节点最大值大于右子节点最大值
            self.mx[rt] = self.mx[l]
            self.cnt[rt] = self.cnt[l]
            self.sem[rt] = max(self.sem[l], self.mx[r])
        elif self.mx[l] < self.mx[r]:
            # 右子节点最大值大于左子节点最大值
            self.mx[rt] = self.mx[r]
            self.cnt[rt] = self.cnt[r]
            self.sem[rt] = max(self.mx[l], self.sem[r])
        else:
            # 左右子节点最大值相等
            self.mx[rt] = self.mx[l]
            self.cnt[rt] = self.cnt[l] + self.cnt[r]
            self.sem[rt] = max(self.sem[l], self.sem[r])
    
    def push_down(self, rt):
        """
        下传懒惰标记，将当前节点的懒惰标记传递给子节点
        这是处理区间更新的关键技术
        
        Args:
            rt (int): 当前节点索引
        """
        # 只有当懒惰标记小于当前节点最大值时才需要下传
        if self.lazy[rt] < self.mx[rt]:
            # 获取左右子节点索引
            l, r = rt * 2, rt * 2 + 1
            
            # 处理左子节点
            # 条件：左子节点最大值大于懒惰标记 且 左子节点次大值小于懒惰标记
            if self.mx[l] > self.lazy[rt] and self.sem[l] < self.lazy[rt]:
                # 更新区间和：增加 (新值-旧值) * 最大值个数
                self.sum[l] += (self.lazy[rt] - self.mx[l]) * self.cnt[l]
                # 更新最大值
                self.mx[l] = self.lazy[rt]
                # 传递懒惰标记
                self.lazy[l] = self.lazy[rt]
            
            # 处理右子节点
            # 条件：右子节点最大值大于懒惰标记 且 右子节点次大值小于懒惰标记
            if self.mx[r] > self.lazy[rt] and self.sem[r] < self.lazy[rt]:
                # 更新区间和：增加 (新值-旧值) * 最大值个数
                self.sum[r] += (self.lazy[rt] - self.mx[r]) * self.cnt[r]
                # 更新最大值
                self.mx[r] = self.lazy[rt]
                # 传递懒惰标记
                self.lazy[r] = self.lazy[rt]
            
            # 清除当前节点的懒惰标记
            self.lazy[rt] = float('inf')
    
    def build(self, arr, l, r, rt):
        """
        建立线段树
        
        Args:
            arr (list): 原始数组
            l (int): 当前区间左边界
            r (int): 当前区间右边界
            rt (int): 当前节点索引
        """
        # 初始化当前节点的懒惰标记
        self.lazy[rt] = float('inf')
        
        # 递归终止条件：叶子节点
        if l == r:
            # 叶子节点的信息直接从数组中获取
            self.mx[rt] = self.sum[rt] = arr[l]
            # 次大值初始化为-1（表示不存在）
            self.sem[rt] = -1
            # 最大值个数为1
            self.cnt[rt] = 1
            return
        
        # 分治处理左右子区间
        mid = (l + r) // 2
        # 递归构建左子树
        self.build(arr, l, mid, rt * 2)
        # 递归构建右子树
        self.build(arr, mid + 1, r, rt * 2 + 1)
        # 合并左右子树信息
        self.push_up(rt)
    
    def update_min(self, L, R, val, l, r, rt):
        """
        区间取min操作：将区间[L,R]中所有元素更新为min(原值, val)
        
        Args:
            L (int): 操作区间左边界
            R (int): 操作区间右边界
            val (int): 新的值
            l (int): 当前区间左边界
            r (int): 当前区间右边界
            rt (int): 当前节点索引
        """
        # 优化：如果val大于等于当前区间最大值，则无需更新
        if val >= self.mx[rt]:
            return
        
        # 优化：如果操作区间完全包含当前区间且val大于次大值
        # 则可以直接更新最大值，无需递归
        if L <= l and r <= R and val > self.sem[rt]:
            # 更新区间和：增加 (新值-旧值) * 最大值个数
            self.sum[rt] += (val - self.mx[rt]) * self.cnt[rt]
            # 更新最大值
            self.mx[rt] = val
            # 设置懒惰标记
            self.lazy[rt] = val
            return
        
        # 下传懒惰标记
        self.push_down(rt)
        
        # 分治处理左右子区间
        mid = (l + r) // 2
        if L <= mid:
            # 操作区间与左子区间有交集
            self.update_min(L, R, val, l, mid, rt * 2)
        if R > mid:
            # 操作区间与右子区间有交集
            self.update_min(L, R, val, mid + 1, r, rt * 2 + 1)
        
        # 合并左右子区间信息
        self.push_up(rt)
    
    def query_max(self, L, R, l, r, rt):
        """
        查询区间最大值
        
        Args:
            L (int): 查询区间左边界
            R (int): 查询区间右边界
            l (int): 当前区间左边界
            r (int): 当前区间右边界
            rt (int): 当前节点索引
            
        Returns:
            int: 区间最大值
        """
        # 完全包含：当前区间完全被查询区间包含
        if L <= l and r <= R:
            return self.mx[rt]
        
        # 下传懒惰标记
        self.push_down(rt)
        
        # 分治查询
        mid = (l + r) // 2
        res = -float('inf')
        
        # 查询左子区间
        if L <= mid:
            res = max(res, self.query_max(L, R, l, mid, rt * 2))
        # 查询右子区间
        if R > mid:
            res = max(res, self.query_max(L, R, mid + 1, r, rt * 2 + 1))
        
        return res
    
    def query_sum(self, L, R, l, r, rt):
        """
        查询区间和
        
        Args:
            L (int): 查询区间左边界
            R (int): 查询区间右边界
            l (int): 当前区间左边界
            r (int): 当前区间右边界
            rt (int): 当前节点索引
            
        Returns:
            int: 区间和
        """
        # 完全包含：当前区间完全被查询区间包含
        if L <= l and r <= R:
            return self.sum[rt]
        
        # 下传懒惰标记
        self.push_down(rt)
        
        # 分治查询
        mid = (l + r) // 2
        res = 0
        
        # 查询左子区间
        if L <= mid:
            res += self.query_sum(L, R, l, mid, rt * 2)
        # 查询右子区间
        if R > mid:
            res += self.query_sum(L, R, mid + 1, r, rt * 2 + 1)
        
        return res

def main():
    """
    主函数，处理输入输出和操作执行
    """
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    # 读取测试用例数
    T = int(data[idx]); idx += 1
    
    results = []
    
    # 处理每个测试用例
    for _ in range(T):
        # 读取数组长度和操作数
        n = int(data[idx]); idx += 1
        m = int(data[idx]); idx += 1
        
        # 读取数组元素（使用1索引）
        arr = [0] * (n + 1)
        for i in range(1, n + 1):
            arr[i] = int(data[idx]); idx += 1
        
        # 构建线段树
        seg_tree = SegmentTree(n)
        seg_tree.build(arr, 1, n, 1)
        
        # 处理每个操作
        for _ in range(m):
            # 读取操作类型
            op = int(data[idx]); idx += 1
            # 读取操作区间
            l = int(data[idx]); idx += 1
            r = int(data[idx]); idx += 1
            
            if op == 0:
                # 区间取min操作
                t = int(data[idx]); idx += 1
                seg_tree.update_min(l, r, t, 1, n, 1)
            elif op == 1:
                # 查询区间最大值
                results.append(str(seg_tree.query_max(l, r, 1, n, 1)))
            else:
                # 查询区间和
                results.append(str(seg_tree.query_sum(l, r, 1, n, 1)))
    
    # 输出所有查询结果
    print('\n'.join(results))

if __name__ == "__main__":
    main()
# POJ 3468 A Simple Problem with Integers - 区间加法与区间求和
# 题目链接：http://poj.org/problem?id=3468
# 题目描述：
# 给定一个长度为 N 的整数序列，执行以下操作：
# 1. C a b c: 将区间 [a,b] 中的每个数都加上 c
# 2. Q a b: 查询区间 [a,b] 中所有数的和
#
# 解题思路：
# 使用线段树 + 懒惰标记优化区间加法操作
# 每个节点存储区间和，使用懒惰标记延迟更新
#
# 关键技术：
# 1. 懒惰标记：延迟更新子区间，提高效率
# 2. pushDown操作：在需要时下传懒惰标记
# 3. pushUp操作：合并子区间信息
#
# 时间复杂度分析：
# 1. 建树：O(n)
# 2. 区间更新：O(log n)
# 3. 区间查询：O(log n)
# 4. 空间复杂度：O(n)
#
# 是否最优解：是
# 这是解决区间加法与区间求和问题的最优解法

class SegmentTree:
    """
    线段树类
    用于处理区间加法和区间求和操作
    """
    
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size (int): 数组大小
        """
        self.n = size
        # 线段树数组大小通常设置为4*size以保证足够空间
        self.sum = [0] * (4 * size)  # 区间和
        self.lazy = [0] * (4 * size)  # 懒惰标记，用于延迟区间加法操作
    
    def push_up(self, rt):
        """
        向上更新节点信息
        将左右子节点的信息合并到父节点
        
        Args:
            rt (int): 当前节点索引
        """
        # 父节点的区间和等于左右子节点区间和的和
        self.sum[rt] = self.sum[rt * 2] + self.sum[rt * 2 + 1]
    
    def push_down(self, rt, ln, rn):
        """
        向下传递懒惰标记
        在访问子节点前，将当前节点的懒惰标记传递给子节点
        
        Args:
            rt (int): 当前节点索引
            ln (int): 左子区间长度
            rn (int): 右子区间长度
        """
        # 只有当懒惰标记不为0时才需要下传
        if self.lazy[rt] != 0:
            # 更新左子树
            # 将懒惰标记加到左子节点的懒惰标记上
            self.lazy[rt * 2] += self.lazy[rt]
            # 更新左子节点的区间和：增加 懒惰标记 * 区间长度
            self.sum[rt * 2] += self.lazy[rt] * ln
            
            # 更新右子树
            # 将懒惰标记加到右子节点的懒惰标记上
            self.lazy[rt * 2 + 1] += self.lazy[rt]
            # 更新右子节点的区间和：增加 懒惰标记 * 区间长度
            self.sum[rt * 2 + 1] += self.lazy[rt] * rn
            
            # 清除当前节点的懒惰标记
            self.lazy[rt] = 0
    
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
        self.lazy[rt] = 0
        
        # 递归终止条件：叶子节点
        if l == r:
            # 叶子节点的区间和直接从数组中获取
            self.sum[rt] = arr[l]
            return
        
        # 分治处理左右子区间
        mid = (l + r) // 2
        # 递归构建左子树
        self.build(arr, l, mid, rt * 2)
        # 递归构建右子树
        self.build(arr, mid + 1, r, rt * 2 + 1)
        # 合并左右子树信息
        self.push_up(rt)
    
    def update(self, L, R, val, l, r, rt):
        """
        区间加法操作：将区间[L,R]中每个数都加上val
        
        Args:
            L (int): 操作区间左边界
            R (int): 操作区间右边界
            val (int): 要加上的值
            l (int): 当前区间左边界
            r (int): 当前区间右边界
            rt (int): 当前节点索引
        """
        # 完全包含：当前区间完全被操作区间包含
        if L <= l and r <= R:
            # 更新区间和：增加 val * 区间长度
            self.sum[rt] += val * (r - l + 1)
            # 更新懒惰标记
            self.lazy[rt] += val
            return
        
        # 计算中点和子区间长度
        mid = (l + r) // 2
        # 下传懒惰标记
        self.push_down(rt, mid - l + 1, r - mid)
        
        # 分治处理左右子区间
        if L <= mid:
            # 操作区间与左子区间有交集
            self.update(L, R, val, l, mid, rt * 2)
        if R > mid:
            # 操作区间与右子区间有交集
            self.update(L, R, val, mid + 1, r, rt * 2 + 1)
        
        # 合并左右子区间信息
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        """
        区间查询操作：查询区间[L,R]中所有数的和
        
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
        
        # 计算中点和子区间长度
        mid = (l + r) // 2
        # 下传懒惰标记
        self.push_down(rt, mid - l + 1, r - mid)
        
        # 分治查询
        res = 0
        # 查询左子区间
        if L <= mid:
            res += self.query(L, R, l, mid, rt * 2)
        # 查询右子区间
        if R > mid:
            res += self.query(L, R, mid + 1, r, rt * 2 + 1)
        
        return res

def main():
    """
    主函数，处理输入输出和操作执行
    """
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    # 读取数组长度和操作数
    N = int(data[idx]); idx += 1
    Q = int(data[idx]); idx += 1
    
    # 读取数组元素（使用1索引）
    arr = [0] * (N + 1)
    for i in range(1, N + 1):
        arr[i] = int(data[idx]); idx += 1
    
    # 构建线段树
    seg_tree = SegmentTree(N)
    seg_tree.build(arr, 1, N, 1)
    
    results = []
    
    # 处理每个操作
    for _ in range(Q):
        # 读取操作类型
        op = data[idx]; idx += 1
        
        if op == 'C':
            # 区间加法操作：C a b c
            a = int(data[idx]); idx += 1
            b = int(data[idx]); idx += 1
            c = int(data[idx]); idx += 1
            seg_tree.update(a, b, c, 1, N, 1)
        else:
            # 区间查询操作：Q a b
            a = int(data[idx]); idx += 1
            b = int(data[idx]); idx += 1
            results.append(str(seg_tree.query(a, b, 1, N, 1)))
    
    # 输出所有查询结果
    print('\n'.join(results))

if __name__ == "__main__":
    main()
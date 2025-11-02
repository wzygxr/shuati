# A Simple Problem with Integers (POJ 3468)
# 题目来源: POJ 3468. A Simple Problem with Integers
# 题目链接: http://poj.org/problem?id=3468
# 
# 题目描述:
# 你有N个整数A1, A2, ..., AN。你需要处理两种类型的操作:
# 1. C a b c: 将区间[a, b]中的每个数都加上c
# 2. Q a b: 查询区间[a, b]中所有数的和
#
# 解题思路:
# 1. 使用带懒惰传播的线段树实现区间更新和区间查询
# 2. 懒惰传播用于延迟更新，避免不必要的计算
# 3. 区间更新时，只在必要时才将更新操作传递给子节点
# 4. 查询时确保所有相关的懒惰标记都被处理
#
# 时间复杂度分析:
# - 区间更新: O(log n)
# - 区间查询: O(log n)
# 空间复杂度: O(n)

class SegmentTree:
    def __init__(self, nums):
        self.n = len(nums)
        self.data = nums[:]
        self.tree = [0] * (4 * self.n)
        self.lazy = [0] * (4 * self.n)
        self._build_tree(0, 0, self.n - 1)
    
    # 构建线段树
    def _build_tree(self, tree_index, l, r):
        if l == r:
            self.tree[tree_index] = self.data[l]
            return
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        # 构建左子树
        self._build_tree(left_tree_index, l, mid)
        # 构建右子树
        self._build_tree(right_tree_index, mid + 1, r)
        
        # 当前节点的值等于左右子树值的和
        self.tree[tree_index] = self.tree[left_tree_index] + self.tree[right_tree_index]
    
    # 下推懒惰标记
    def _push_down(self, tree_index, l, r):
        if self.lazy[tree_index] != 0:
            # 将懒惰标记应用到当前节点
            self.tree[tree_index] += self.lazy[tree_index] * (r - l + 1)
            
            # 如果不是叶子节点，将懒惰标记传递给子节点
            if l != r:
                self.lazy[2 * tree_index + 1] += self.lazy[tree_index]
                self.lazy[2 * tree_index + 2] += self.lazy[tree_index]
            
            # 清除当前节点的懒惰标记
            self.lazy[tree_index] = 0
    
    # 区间加法更新 [query_l, query_r] 区间内每个元素加上 val
    def update(self, query_l, query_r, val):
        self._update_tree(0, 0, self.n - 1, query_l, query_r, val)
    
    # 区间加法更新辅助函数
    def _update_tree(self, tree_index, l, r, query_l, query_r, val):
        # 1. 先处理懒惰标记
        self._push_down(tree_index, l, r)
        
        # 2. 当前区间与更新区间无交集
        if l > query_r or r < query_l:
            return
        
        # 3. 当前区间完全包含在更新区间内
        if l >= query_l and r <= query_r:
            # 更新当前节点的值
            self.tree[tree_index] += val * (r - l + 1)
            # 如果不是叶子节点，设置懒惰标记
            if l != r:
                self.lazy[2 * tree_index + 1] += val
                self.lazy[2 * tree_index + 2] += val
            return
        
        # 4. 当前区间与更新区间有部分交集，递归处理左右子树
        mid = l + (r - l) // 2
        self._update_tree(2 * tree_index + 1, l, mid, query_l, query_r, val)
        self._update_tree(2 * tree_index + 2, mid + 1, r, query_l, query_r, val)
        
        # 更新当前节点的值
        self.tree[tree_index] = self.tree[2 * tree_index + 1] + self.tree[2 * tree_index + 2]
    
    # 查询区间和
    def query(self, query_l, query_r):
        return self._query_tree(0, 0, self.n - 1, query_l, query_r)
    
    # 查询区间和辅助函数
    def _query_tree(self, tree_index, l, r, query_l, query_r):
        # 1. 先处理懒惰标记
        self._push_down(tree_index, l, r)
        
        # 2. 当前区间与查询区间无交集
        if l > query_r or r < query_l:
            return 0
        
        # 3. 当前区间完全包含在查询区间内
        if l >= query_l and r <= query_r:
            return self.tree[tree_index]
        
        # 4. 当前区间与查询区间有部分交集，递归查询左右子树
        mid = l + (r - l) // 2
        left_sum = self._query_tree(2 * tree_index + 1, l, mid, query_l, query_r)
        right_sum = self._query_tree(2 * tree_index + 2, mid + 1, r, query_l, query_r)
        return left_sum + right_sum

# 测试代码
if __name__ == "__main__":
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])  # 数组长度
    idx += 1
    q = int(data[idx])  # 操作数量
    idx += 1
    
    nums = [int(data[idx + i]) for i in range(n)]
    idx += n
    
    # 构建线段树
    segment_tree = SegmentTree(nums)
    
    # 处理操作
    for _ in range(q):
        operation = data[idx]
        idx += 1
        
        if operation == "C":
            a = int(data[idx]) - 1  # 转换为0索引
            b = int(data[idx + 1]) - 1  # 转换为0索引
            c = int(data[idx + 2])
            idx += 3
            segment_tree.update(a, b, c)
        elif operation == "Q":
            a = int(data[idx]) - 1  # 转换为0索引
            b = int(data[idx + 1]) - 1  # 转换为0索引
            idx += 2
            print(segment_tree.query(a, b))
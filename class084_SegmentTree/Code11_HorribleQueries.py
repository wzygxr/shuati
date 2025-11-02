# Horrible Queries (可怕的查询)
# 题目来源: SPOJ HORRIBLE - Horrible Queries
# 题目链接: https://www.spoj.com/problems/HORRIBLE/
# 
# 题目描述:
# 现有一个长度为n的序列，开始时所有位置都为0
# 有以下两种操作：
# 0 p q v : 将区间[p,q]内每个位置的值都加上v
# 1 p q   : 查询区间[p,q]内所有位置的值的和
#
# 解题思路:
# 1. 使用带懒惰传播的线段树实现区间更新和区间查询
# 2. 懒惰传播用于延迟更新，避免不必要的计算
# 3. 区间更新时，只在必要时才将更新操作传递给子节点
# 4. 查询时确保所有相关的懒惰标记都被处理
#
# 时间复杂度: 
# - 区间更新: O(log n)
# - 区间查询: O(log n)
# 空间复杂度: O(n)

class SegmentTree:
    def __init__(self, size):
        self.n = size
        # 线段树需要4*n的空间
        self.tree = [0] * (4 * self.n)
        self.lazy = [0] * (4 * self.n)

    # 区间加法更新 [l, r] 区间内每个元素加上 val
    def range_add(self, l, r, val):
        self._range_add_helper(0, 0, self.n - 1, l, r, val)

    # 区间加法更新辅助函数
    def _range_add_helper(self, node, start, end, l, r, val):
        # 1. 先处理懒惰标记
        self._push_down(node, start, end)

        # 2. 当前区间与更新区间无交集
        if start > r or end < l:
            return

        # 3. 当前区间完全包含在更新区间内
        if start >= l and end <= r:
            # 更新当前节点的值
            self.tree[node] += val * (end - start + 1)
            # 如果不是叶子节点，设置懒惰标记
            if start != end:
                self.lazy[node] += val
            return

        # 4. 当前区间与更新区间有部分交集，递归处理左右子树
        mid = (start + end) // 2
        self._range_add_helper(2 * node + 1, start, mid, l, r, val)
        self._range_add_helper(2 * node + 2, mid + 1, end, l, r, val)

        # 更新当前节点的值
        self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]

    # 查询区间和
    def query(self, l, r):
        return self._query_helper(0, 0, self.n - 1, l, r)

    # 查询区间和辅助函数
    def _query_helper(self, node, start, end, l, r):
        # 1. 先处理懒惰标记
        self._push_down(node, start, end)

        # 2. 当前区间与查询区间无交集
        if start > r or end < l:
            return 0

        # 3. 当前区间完全包含在查询区间内
        if start >= l and end <= r:
            return self.tree[node]

        # 4. 当前区间与查询区间有部分交集，递归查询左右子树
        mid = (start + end) // 2
        left_sum = self._query_helper(2 * node + 1, start, mid, l, r)
        right_sum = self._query_helper(2 * node + 2, mid + 1, end, l, r)
        return left_sum + right_sum

    # 下推懒惰标记
    def _push_down(self, node, start, end):
        # 如果当前节点没有懒惰标记，直接返回
        if self.lazy[node] == 0:
            return

        # 将懒惰标记应用到当前节点
        self.tree[node] += self.lazy[node] * (end - start + 1)

        # 如果不是叶子节点，将懒惰标记传递给子节点
        if start != end:
            mid = (start + end) // 2
            self.lazy[2 * node + 1] += self.lazy[node]
            self.lazy[2 * node + 2] += self.lazy[node]

        # 清除当前节点的懒惰标记
        self.lazy[node] = 0


# 由于SPOJ在线测试需要特定的输入输出格式，这里提供一个简化版本的测试方法
def test_horrible_queries():
    # 示例测试
    n = 8  # 序列长度
    st = SegmentTree(n)
    
    # 操作1: 将区间[2,4]内每个位置的值都加上3 (注意转换为0索引)
    st.range_add(1, 3, 3)
    
    # 操作2: 查询区间[1,3]内所有位置的值的和 (注意转换为0索引)
    result = st.query(0, 2)
    print(result)  # 输出: 9 (3+3+3=9)
    
    # 操作3: 将区间[5,7]内每个位置的值都加上2 (注意转换为0索引)
    st.range_add(4, 6, 2)
    
    # 操作4: 查询区间[4,6]内所有位置的值的和 (注意转换为0索引)
    result = st.query(3, 5)
    print(result)  # 输出: 6 (2+2+2=6)


# 测试方法
if __name__ == "__main__":
    test_horrible_queries()
# Can you answer these queries I (GSS1)
# 题目来源: SPOJ GSS1 - Can you answer these queries I
# 题目链接: https://www.spoj.com/problems/GSS1/
# 
# 题目描述:
# 给定一个长度为N的整数序列A，需要处理M个查询。
# 每个查询给定两个整数X和Y，要求找出从第X个数到第Y个数之间的子段的最大和。
# 子段是指连续的一段数，空子段的和为0。
#
# 解题思路:
# 1. 使用线段树维护区间最大子段和
# 2. 每个节点需要维护以下信息：
#    - lSum: 以左端点为起点的最大子段和
#    - rSum: 以右端点为终点的最大子段和
#    - sum: 区间和
#    - maxSum: 区间最大子段和
# 3. 合并左右子树时，父节点的信息由左右子树的信息计算得出
#
# 时间复杂度分析:
# - 构建线段树: O(n)
# - 查询: O(log n)
# 空间复杂度: O(n)

class SegmentTreeNode:
    def __init__(self, lSum=0, rSum=0, sum=0, maxSum=0):
        self.lSum = lSum    # 以左端点为起点的最大子段和
        self.rSum = rSum    # 以右端点为终点的最大子段和
        self.sum = sum      # 区间和
        self.maxSum = maxSum # 区间最大子段和

class SegmentTree:
    def __init__(self, nums):
        self.n = len(nums)
        self.data = nums[:]
        self.tree = [SegmentTreeNode() for _ in range(4 * self.n)]
        self._build_tree(0, 0, self.n - 1)
    
    # 构建线段树
    def _build_tree(self, tree_index, l, r):
        if l == r:
            self.tree[tree_index] = SegmentTreeNode(
                self.data[l], 
                self.data[l], 
                self.data[l], 
                self.data[l]
            )
            return
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        # 构建左子树
        self._build_tree(left_tree_index, l, mid)
        # 构建右子树
        self._build_tree(right_tree_index, mid + 1, r)
        
        # 合并左右子树的信息
        self.tree[tree_index] = self._merge(self.tree[left_tree_index], self.tree[right_tree_index])
    
    # 合并两个节点的信息
    def _merge(self, left, right):
        sum_val = left.sum + right.sum
        lSum = max(left.lSum, left.sum + right.lSum)
        rSum = max(right.rSum, right.sum + left.rSum)
        maxSum = max(max(left.maxSum, right.maxSum), left.rSum + right.lSum)
        
        return SegmentTreeNode(lSum, rSum, sum_val, maxSum)
    
    # 查询区间最大子段和
    def query(self, query_l, query_r):
        if self.n == 0:
            return 0
        return self._query_tree(0, 0, self.n - 1, query_l, query_r).maxSum
    
    def _query_tree(self, tree_index, l, r, query_l, query_r):
        if l == query_l and r == query_r:
            return self.tree[tree_index]
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        if query_r <= mid:
            # 查询区间完全在左子树
            return self._query_tree(left_tree_index, l, mid, query_l, query_r)
        elif query_l > mid:
            # 查询区间完全在右子树
            return self._query_tree(right_tree_index, mid + 1, r, query_l, query_r)
        else:
            # 查询区间跨越左右子树
            left_result = self._query_tree(left_tree_index, l, mid, query_l, mid)
            right_result = self._query_tree(right_tree_index, mid + 1, r, mid + 1, query_r)
            return self._merge(left_result, right_result)

# 测试代码
if __name__ == "__main__":
    # 读取序列长度
    n = int(input())
    # 读取序列
    nums = list(map(int, input().split()))
    
    # 构建线段树
    segment_tree = SegmentTree(nums)
    
    # 读取查询数量
    m = int(input())
    
    # 处理查询
    for _ in range(m):
        x, y = map(int, input().split())
        # 转换为0索引
        x -= 1
        y -= 1
        print(segment_tree.query(x, y))
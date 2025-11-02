"""
SPOJ GSS5 - Can you answer these queries V

题目描述:
给定一个整数数组，多次查询某个区间内的最大子段和，查询区间可能不连续。

解题思路:
这是一个线段树的经典应用。与GSS1不同的是，GSS5的查询区间可能不连续，需要特殊处理。

关键点:
1. 使用线段树维护区间最大子段和信息
2. 对于每个查询(x1, y1, x2, y2)，需要找到在区间[x1, y1]中以y1结尾的最大子段和，
   以及在区间[x2, y2]中以x2开头的最大子段和
3. 如果y1 < x2，则结果为区间[y1+1, x2-1]的总和加上前面和后面的部分
4. 如果y1 >= x2，则需要特殊处理重叠区间

时间复杂度: O(n + q * log n)
空间复杂度: O(n)
"""

class SegmentTreeNode:
    def __init__(self, max_sum=0, prefix_sum=0, suffix_sum=0, total_sum=0):
        """
        线段树节点构造函数
        
        @param max_sum: 区间最大子段和
        @param prefix_sum: 包含左端点的最大子段和
        @param suffix_sum: 包含右端点的最大子段和
        @param total_sum: 区间总和
        """
        self.max_sum = max_sum
        self.prefix_sum = prefix_sum
        self.suffix_sum = suffix_sum
        self.total_sum = total_sum


class SegmentTree:
    def __init__(self, size, array):
        """
        构造函数 - 初始化线段树
        
        @param size: 数组大小
        @param array: 数组元素
        """
        self.n = size
        self.arr = array
        self.tree = [SegmentTreeNode() for _ in range(4 * size)]
        self.build(1, 1, size)
    
    def build(self, i, l, r):
        """
        构建线段树
        
        @param i: 当前节点索引
        @param l: 当前区间左端点
        @param r: 当前区间右端点
        """
        if l == r:
            # 叶子节点
            self.tree[i] = SegmentTreeNode(self.arr[l], self.arr[l], self.arr[l], self.arr[l])
        else:
            # 非叶子节点
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.push_up(i)
    
    def push_up(self, i):
        """
        上推操作
        
        @param i: 当前节点索引
        """
        left = self.tree[i << 1]
        right = self.tree[i << 1 | 1]
        
        self.tree[i] = SegmentTreeNode(
            max(max(left.max_sum, right.max_sum), left.suffix_sum + right.prefix_sum),
            max(left.prefix_sum, left.total_sum + right.prefix_sum),
            max(right.suffix_sum, right.total_sum + left.suffix_sum),
            left.total_sum + right.total_sum
        )
    
    def query_range(self, jobl, jobr, l, r, i):
        """
        区间最大子段和查询
        
        @param jobl: 查询区间左端点
        @param jobr: 查询区间右端点
        @param l: 当前区间左端点
        @param r: 当前区间右端点
        @param i: 当前节点索引
        @return: 区间最大子段和信息
        """
        if jobl <= l and r <= jobr:
            # 当前区间完全包含在查询区间内
            return self.tree[i]
        else:
            # 需要继续向下递归
            mid = (l + r) >> 1
            if jobr <= mid:
                # 完全在左子树
                return self.query_range(jobl, jobr, l, mid, i << 1)
            elif jobl > mid:
                # 完全在右子树
                return self.query_range(jobl, jobr, mid + 1, r, i << 1 | 1)
            else:
                # 跨越左右子树
                left_result = self.query_range(jobl, jobr, l, mid, i << 1)
                right_result = self.query_range(jobl, jobr, mid + 1, r, i << 1 | 1)
                
                return SegmentTreeNode(
                    max(max(left_result.max_sum, right_result.max_sum), left_result.suffix_sum + right_result.prefix_sum),
                    max(left_result.prefix_sum, left_result.total_sum + right_result.prefix_sum),
                    max(right_result.suffix_sum, right_result.total_sum + left_result.suffix_sum),
                    left_result.total_sum + right_result.total_sum
                )
    
    def query_sum(self, jobl, jobr, l, r, i):
        """
        查询区间总和
        
        @param jobl: 查询区间左端点
        @param jobr: 查询区间右端点
        @param l: 当前区间左端点
        @param r: 当前区间右端点
        @param i: 当前节点索引
        @return: 区间总和
        """
        if jobl <= l and r <= jobr:
            # 当前区间完全包含在查询区间内
            return self.tree[i].total_sum
        else:
            # 需要继续向下递归
            mid = (l + r) >> 1
            sum_val = 0
            if jobl <= mid:
                sum_val += self.query_sum(jobl, jobr, l, mid, i << 1)
            if jobr > mid:
                sum_val += self.query_sum(jobl, jobr, mid + 1, r, i << 1 | 1)
            return sum_val


def main():
    """
    主函数
    """
    import sys
    input = sys.stdin.read
    data = input().split()
    
    # 读取测试用例数量
    idx = 0
    t = int(data[idx])
    idx += 1
    
    for _ in range(t):
        # 读取数组长度
        n = int(data[idx])
        idx += 1
        
        # 读取数组元素
        arr = [0] * (n + 1)
        for i in range(1, n + 1):
            arr[i] = int(data[idx])
            idx += 1
        
        # 构建线段树
        seg_tree = SegmentTree(n, arr)
        
        # 读取查询数量
        q = int(data[idx])
        idx += 1
        
        # 处理每个查询
        for _ in range(q):
            x1 = int(data[idx])
            y1 = int(data[idx + 1])
            x2 = int(data[idx + 2])
            y2 = int(data[idx + 3])
            idx += 4
            
            result = 0
            
            if y1 < x2:
                # 区间不重叠
                left = seg_tree.query_range(x1, y1, 1, n, 1)
                right = seg_tree.query_range(x2, y2, 1, n, 1)
                middle_sum = seg_tree.query_sum(y1 + 1, x2 - 1, 1, n, 1)
                
                result = left.suffix_sum + middle_sum + right.prefix_sum
            elif y1 >= x2:
                # 区间重叠或相邻
                if x2 <= y1:
                    # 有重叠部分
                    overlap = seg_tree.query_range(x2, y1, 1, n, 1)
                    left_max = 0
                    right_max = 0
                    
                    if x1 < x2:
                        left_node = seg_tree.query_range(x1, x2 - 1, 1, n, 1)
                        left_max = left_node.suffix_sum
                    
                    if y1 < y2:
                        right_node = seg_tree.query_range(y1 + 1, y2, 1, n, 1)
                        right_max = right_node.prefix_sum
                    
                    result = max(overlap.max_sum, left_max + overlap.suffix_sum)
                    result = max(result, overlap.prefix_sum + right_max)
                    result = max(result, left_max + overlap.total_sum + right_max)
                else:
                    # 相邻但不重叠
                    left = seg_tree.query_range(x1, y1, 1, n, 1)
                    right = seg_tree.query_range(x2, y2, 1, n, 1)
                    result = left.suffix_sum + right.prefix_sum
            
            print(result)


if __name__ == "__main__":
    main()
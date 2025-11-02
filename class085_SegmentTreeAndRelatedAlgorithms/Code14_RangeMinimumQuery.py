"""
区间最小值查询 - 线段树实现

题目描述：
实现一个支持区间最小值查询和单点更新的数据结构
支持以下操作：
1. 构造函数：用整数数组初始化对象
2. update：将数组中某个位置的值更新为新值
3. min_range：查询数组中某个区间内的最小值

解题思路：
使用线段树来高效处理区间最小值查询和单点更新操作
1. 线段树是一种二叉树结构，每个节点代表一个区间
2. 叶子节点代表数组中的单个元素
3. 非叶子节点代表其子节点区间的合并结果（这里是区间最小值）

时间复杂度分析：
- 构建线段树：O(n)
- 单点更新：O(log n)
- 区间查询：O(log n)
空间复杂度：O(n)
"""

class Code14_RangeMinimumQuery:
    
    class SegmentTree:
        """线段树实现"""
        
        def __init__(self, nums):
            """
            构造函数
            :param nums: 原始数组
            
            时间复杂度: O(n)
            空间复杂度: O(n)
            """
            self.n = len(nums)
            self.tree = [0] * (4 * self.n)  # 线段树通常需要4倍空间
            self.build_tree(nums, 0, 0, self.n - 1)
        
        def build_tree(self, nums, node, start, end):
            """
            构建线段树
            :param nums: 原始数组
            :param node: 当前线段树节点索引
            :param start: 当前节点表示区间的起始位置
            :param end: 当前节点表示区间的结束位置
            
            时间复杂度: O(n)
            """
            if start == end:
                # 叶子节点，存储数组元素值
                self.tree[node] = nums[start]
            else:
                mid = (start + end) // 2
                # 递归构建左子树
                self.build_tree(nums, 2 * node + 1, start, mid)
                # 递归构建右子树
                self.build_tree(nums, 2 * node + 2, mid + 1, end)
                # 合并左右子树的结果，存储区间最小值
                self.tree[node] = min(self.tree[2 * node + 1], self.tree[2 * node + 2])
        
        def update(self, index, val):
            """
            更新数组中某个位置的值
            :param index: 要更新的数组索引
            :param val: 新的值
            
            时间复杂度: O(log n)
            """
            self.update_helper(0, 0, self.n - 1, index, val)
        
        def update_helper(self, node, start, end, index, val):
            """
            更新线段树中某个位置的值的辅助函数
            :param node: 当前线段树节点索引
            :param start: 当前节点表示区间的起始位置
            :param end: 当前节点表示区间的结束位置
            :param index: 要更新的数组索引
            :param val: 新的值
            """
            if start == end:
                # 找到叶子节点，更新值
                self.tree[node] = val
            else:
                mid = (start + end) // 2
                if index <= mid:
                    # 要更新的索引在左子树中
                    self.update_helper(2 * node + 1, start, mid, index, val)
                else:
                    # 要更新的索引在右子树中
                    self.update_helper(2 * node + 2, mid + 1, end, index, val)
                # 更新父节点的值（区间最小值）
                self.tree[node] = min(self.tree[2 * node + 1], self.tree[2 * node + 2])
        
        def min_range(self, left, right):
            """
            查询区间最小值
            :param left: 查询区间左边界
            :param right: 查询区间右边界
            :return: 区间[left, right]内元素的最小值
            
            时间复杂度: O(log n)
            """
            return self.min_range_helper(0, 0, self.n - 1, left, right)
        
        def min_range_helper(self, node, start, end, left, right):
            """
            查询区间最小值的辅助函数
            :param node: 当前线段树节点索引
            :param start: 当前节点表示区间的起始位置
            :param end: 当前节点表示区间的结束位置
            :param left: 查询区间左边界
            :param right: 查询区间右边界
            :return: 区间[left, right]与当前节点区间交集内元素的最小值
            """
            if right < start or end < left:
                # 查询区间与当前区间无交集，返回一个极大值
                return float('inf')
            if left <= start and end <= right:
                # 当前区间完全包含在查询区间内，直接返回当前节点值
                return self.tree[node]
            # 部分重叠，递归查询左右子树
            mid = (start + end) // 2
            return min(self.min_range_helper(2 * node + 1, start, mid, left, right),
                      self.min_range_helper(2 * node + 2, mid + 1, end, left, right))
    
    def __init__(self, nums):
        """
        构造函数，用整数数组 nums 初始化对象
        :param nums: 初始数组
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        self.st = self.SegmentTree(nums)
    
    def update(self, index, val):
        """
        将 nums[index] 的值更新为 val
        :param index: 要更新的数组索引
        :param val: 新的值
        
        时间复杂度: O(log n)
        """
        self.st.update(index, val)
    
    def min_range(self, left, right):
        """
        返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的最小值
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间最小值
        
        时间复杂度: O(log n)
        """
        return self.st.min_range(left, right)

# 测试方法
def main():
    """
    测试用例:
    nums = [1, 3, 5]
    min_range(0, 2) => 1
    update(1, 2)   // nums = [1,2,5]
    min_range(0, 2) => 1
    """
    nums = [1, 3, 5]
    num_array = Code14_RangeMinimumQuery(nums)
    print(num_array.min_range(0, 2))  # 应该输出 1
    num_array.update(1, 2)   # nums = [1,2,5]
    print(num_array.min_range(0, 2))  # 应该输出 1

if __name__ == "__main__":
    main()
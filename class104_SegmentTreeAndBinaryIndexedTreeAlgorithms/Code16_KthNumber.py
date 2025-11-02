"""
SPOJ MKTHNUM. K-th Number (Python版本 - 主席树解法)
题目链接: https://www.spoj.com/problems/MKTHNUM/
题目描述: 给定一个数组，查询区间[l,r]内第k小的元素

解题思路:
使用主席树(可持久化线段树)实现区间第k小元素查询
1. 对数组元素进行离散化处理
2. 构建主席树，每个版本表示前缀数组的信息
3. 通过两个版本的差值查询区间信息

时间复杂度分析:
- 构建主席树: O(n log n)
- 区间查询: O(log n)
空间复杂度: O(n log n) 主席树需要约n log n的空间

算法详解:
主席树是一种可持久化线段树，它能够保存历史版本的信息。在本题中，我们为数组的每个前缀构建一个线段树版本，
每个版本记录了对应前缀中各个数值的出现次数。通过比较两个版本的差异，我们可以得到任意区间的元素分布情况。

离散化处理:
由于输入数组中的元素值可能很大，我们需要对其进行离散化处理，将其映射到较小的连续整数范围内，
这样可以大大减少线段树的空间消耗。

查询过程:
对于区间[l,r]的第k小查询，我们通过比较第r个版本和第(l-1)个版本的差异来获得区间信息，
然后在线段树上进行二分查找找到第k小的元素。
"""

class ChairmanTreeNode:
    def __init__(self, left, right):
        """
        主席树节点构造函数
        :param left: 区间左边界
        :param right: 区间右边界
        """
        self.left = left
        self.right = right
        self.count = 0  # 区间内元素个数
        self.left_child = None  # type: ChairmanTreeNode | None
        self.right_child = None  # type: ChairmanTreeNode | None

class KthNumber:
    def __init__(self, nums):
        """
        初始化主席树
        :param nums: 输入数组
        """
        self.nums = nums[:]
        self.version_count = len(nums)
        
        # 离散化处理：将原始数值映射到连续的整数范围
        self.sorted_nums = sorted(set(nums))
        self.ranks = {num: i + 1 for i, num in enumerate(self.sorted_nums)}  # 数值到排名的映射
        self.values = {i + 1: num for i, num in enumerate(self.sorted_nums)}  # 排名到数值的映射
        
        # 构建主席树
        self.roots = [None] * (self.version_count + 1)  # type: list[ChairmanTreeNode | None]
        self.build()
    
    def build(self):
        """
        构建主席树，为每个前缀建立一个版本
        """
        # 初始化第0个版本（空树）
        self.roots[0] = ChairmanTreeNode(1, len(self.sorted_nums))
        # 逐个添加元素，构建第1到第n个版本
        for i in range(1, self.version_count + 1):
            self.roots[i] = self._update(self.roots[i - 1], self.ranks[self.nums[i - 1]])
    
    def _update(self, prev, value):
        """
        更新主席树，插入一个元素
        :param prev: 前一个版本的节点
        :param value: 要插入的值(离散化后的rank)
        :return: 新节点
        """
        # 创建新节点，复制前一个版本的信息
        root = ChairmanTreeNode(prev.left, prev.right)
        root.count = prev.count + 1  # 计数加1
        
        # 如果是叶子节点，直接返回
        if prev.left == prev.right:
            return root
        
        # 计算中点，决定更新左子树还是右子树
        mid = (prev.left + prev.right) // 2
        if value <= mid:
            # 更新左子树，右子树保持不变
            root.left_child = self._update(prev.left_child if prev.left_child else ChairmanTreeNode(prev.left, mid), value)
            root.right_child = prev.right_child
        else:
            # 更新右子树，左子树保持不变
            root.left_child = prev.left_child
            root.right_child = self._update(prev.right_child if prev.right_child else ChairmanTreeNode(mid + 1, prev.right), value)
        
        return root
    
    def kth_number(self, left, right, k):
        """
        查询区间第k小元素
        :param left: 区间左端点(1-indexed)
        :param right: 区间右端点(1-indexed)
        :param k: 第k小
        :return: 第k小的元素值
        """
        return self._query(self.roots[left - 1], self.roots[right], k)
    
    def _query(self, left_root, right_root, k):
        """
        查询两个版本的差值中的第k小元素
        :param left_root: 左版本根节点
        :param right_root: 右版本根节点
        :param k: 第k小
        :return: 第k小的元素值
        """
        # 如果是叶子节点，直接返回对应的值
        if left_root.left == left_root.right:
            return self.values[left_root.left]
        
        # 计算左子树中元素的个数差值
        left_count = (right_root.left_child.count if right_root.left_child else 0) - \
                     (left_root.left_child.count if left_root.left_child else 0)
        
        # 根据k的大小决定在左子树还是右子树中查找
        if k <= left_count:
            # 在左子树中查找第k小
            return self._query(left_root.left_child if left_root.left_child else ChairmanTreeNode(left_root.left, (left_root.left + left_root.right) // 2),
                              right_root.left_child if right_root.left_child else ChairmanTreeNode(right_root.left, (right_root.left + right_root.right) // 2),
                              k)
        else:
            # 在右子树中查找第(k-left_count)小
            return self._query(left_root.right_child if left_root.right_child else ChairmanTreeNode((left_root.left + left_root.right) // 2 + 1, left_root.right),
                              right_root.right_child if right_root.right_child else ChairmanTreeNode((right_root.left + right_root.right) // 2 + 1, right_root.right),
                              k - left_count)

# 测试代码
if __name__ == "__main__":
    # 示例测试
    nums = [1, 5, 2, 6, 3, 7, 4]
    solution = KthNumber(nums)
    
    print("初始数组:", nums)
    print("区间[2,5]第2小的元素:", solution.kth_number(2, 5, 2))  # [5,2,6,3]中第2小的是3
    print("区间[1,7]第3小的元素:", solution.kth_number(1, 7, 3))  # 全部元素中第3小的是3
    print("区间[4,6]第1小的元素:", solution.kth_number(4, 6, 1))  # [6,3,7]中第1小的是3
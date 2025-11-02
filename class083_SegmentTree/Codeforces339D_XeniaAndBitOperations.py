"""
Python 线段树实现 - Codeforces 339D. Xenia and Bit Operations
题目链接: https://codeforces.com/problemset/problem/339/D
题目描述:
Xenia这个小孩非常喜欢数论。她特别喜欢异或运算。
现在有一个长度为2^n的数组，下标从0到2^n-1。
有m次操作，每次操作会修改数组中的一个元素。
在每次操作后，需要计算一个特定的值。
计算过程如下：
1. 第一层：对相邻的两个元素进行OR运算，得到2^(n-1)个结果
2. 第二层：对相邻的两个元素进行XOR运算，得到2^(n-2)个结果
3. 第三层：对相邻的两个元素进行OR运算，得到2^(n-3)个结果
4. 以此类推，交替进行OR和XOR运算
5. 最后一层：对两个元素进行运算，得到1个结果
问每次操作后，最终的结果是多少。

输入:
第一行包含两个整数n和m (1 <= n <= 17, 1 <= m <= 10^5) - 数组大小的对数和操作次数。
第二行包含2^n个整数a0, a1, ..., a_{2^n-1} (0 <= ai <= 2^30) - 初始数组。
接下来m行，每行包含两个整数p和b (0 <= p <= 2^n-1, 0 <= b <= 2^30)，
表示将数组中下标为p的元素修改为b。

输出:
对于每次操作，输出一行包含一个整数，表示操作后的最终结果。

示例:
输入:
2 4
1 6 3 5
0 2
1 4
2 5
3 5

输出:
1
4
5
5

解题思路:
这是一个线段树问题，结合了位运算。
1. 使用线段树来维护整个计算过程
2. 每个节点需要记录该层应该进行的运算类型（OR或XOR）
3. 叶子节点存储数组元素，非叶子节点存储运算结果
4. 通过层数的奇偶性来判断应该进行OR还是XOR运算
5. 更新时，从叶子节点向上更新，每层根据运算类型进行相应的运算

时间复杂度: 
- 建树: O(2^n)
- 单点更新: O(n)
- 查询根节点: O(1)
空间复杂度: O(2^n)
"""


class Node:
    def __init__(self, l=0, r=0):
        """
        线段树节点
        :param l: 区间左边界
        :param r: 区间右边界
        """
        self.l = l
        self.r = r
        self.value = 0     # 节点值
        self.isOr = False  # 是否为OR运算


class SegmentTree:
    def __init__(self, arr):
        """
        初始化线段树
        :param arr: 输入数组
        """
        self.n = len(arr)
        self.arr = arr[:]
        
        # 线段树数组，大小为2*n
        self.tree = [Node() for _ in range(2 * self.n)]
    
    def build(self, l, r, i, level):
        """
        建立线段树
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点在tree数组中的索引
        :param level: 当前层数
        """
        self.tree[i].l = l
        self.tree[i].r = r
        
        # 确定该层的运算类型
        # 最底层(level=0)是叶子节点，存储原始值
        # 倒数第二层(level=1)进行OR运算
        # 倒数第三层(level=2)进行XOR运算
        # 以此类推，奇数层OR，偶数层XOR
        self.tree[i].isOr = (level % 2 == 1)
        
        if l == r:
            self.tree[i].value = self.arr[l]
            return
        
        mid = (l + r) // 2
        self.build(l, mid, i << 1, level - 1)
        self.build(mid + 1, r, i << 1 | 1, level - 1)
        
        # 根据运算类型计算当前节点的值
        if self.tree[i].isOr:
            self.tree[i].value = self.tree[i << 1].value | self.tree[i << 1 | 1].value
        else:
            self.tree[i].value = self.tree[i << 1].value ^ self.tree[i << 1 | 1].value
    
    def update(self, index, val, l, r, i):
        """
        单点更新
        :param index: 要更新的数组下标
        :param val: 新的值
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        if l == r:
            self.tree[i].value = val
            self.arr[index] = val
            return
        
        mid = (l + r) // 2
        if index <= mid:
            self.update(index, val, l, mid, i << 1)
        else:
            self.update(index, val, mid + 1, r, i << 1 | 1)
        
        # 根据运算类型更新当前节点的值
        if self.tree[i].isOr:
            self.tree[i].value = self.tree[i << 1].value | self.tree[i << 1 | 1].value
        else:
            self.tree[i].value = self.tree[i << 1].value ^ self.tree[i << 1 | 1].value
    
    def get_root_value(self):
        """
        获取根节点的值
        :return: 根节点的值
        """
        return self.tree[1].value


class Solution:
    def process_operations(self, n, arr, operations):
        """
        处理操作序列
        :param n: 数组大小的对数
        :param arr: 初始数组
        :param operations: 操作列表
        :return: 每次操作后的结果列表
        """
        # 创建线段树
        st = SegmentTree(arr)
        
        # 建立线段树
        st.build(0, len(arr) - 1, 1, n)
        
        # 处理操作并收集结果
        results = []
        for operation in operations:
            p, b = operation[0], operation[1]
            st.update(p, b, 0, len(arr) - 1, 1)
            results.append(st.get_root_value())
        
        return results


# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 示例测试
    n = 2
    arr = [1, 6, 3, 5]
    operations = [
        [0, 2],
        [1, 4],
        [2, 5],
        [3, 5]
    ]
    
    results = solution.process_operations(n, arr, operations)
    
    print("初始数组: [1, 6, 3, 5]")
    print("操作过程:")
    for i, operation in enumerate(operations):
        print("操作 {} {}: {}".format(operation[0], operation[1], results[i]))
    
    print("\n期望输出:")
    print("1")
    print("4")
    print("5")
    print("5")
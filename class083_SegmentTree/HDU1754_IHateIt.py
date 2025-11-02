"""
Python 线段树实现 - HDU 1754. I Hate It
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1754
题目描述:
很多学校流行一种比较的习惯。老师们很喜欢询问，从某某到某某当中，分数最高的是多少。
这让很多学生很反感。
不管你喜不喜欢，现在需要你做的是，就是按照老师的要求，写一个程序，模拟老师的询问。
当然，老师有时候需要更新某位同学的成绩。

输入:
本题目包含多组测试，请处理到文件结束。
在每个测试的第一行，有两个正整数 N 和 M ( 0<N<=200000,0<M<5000 )，分别代表学生的数目和操作的数目。
学生ID编号从1到N。
第二行包含N个整数，代表这N个学生的初始成绩，接下来有M行。
每一行有一条命令，命令有两种形式：
1. Q A B 代表询问从第A个学生到第B个学生中，成绩最高的是多少。
2. U A B 代表更新第A个学生的成绩为B。
其中A和B均为正整数。

输出:
对于每一次询问，输出一行，表示最高成绩。

示例:
输入:
5 6
1 2 3 4 5
Q 1 5
U 3 6
Q 3 4
Q 4 5
U 2 9
Q 1 5

输出:
5
6
5
9

解题思路:
这是一个经典的线段树问题，支持单点更新和区间查询最大值。
1. 使用线段树维护区间最大值
2. 支持两种操作：
   - 单点更新：更新某个学生的学习成绩
   - 区间查询：查询某个区间内的最高成绩

时间复杂度: 
- 建树: O(n)
- 单点更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(n)
"""


class SegmentTree:
    def __init__(self, scores):
        """
        初始化线段树
        :param scores: 学生成绩数组
        """
        self.n = len(scores) - 1  # 学生ID从1开始
        self.scores = scores[:]
        
        # 线段树数组，大小为4*n
        self.tree = [0] * (4 * self.n)
        
        # 构建线段树
        self._build(1, self.n, 1)
    
    def _build(self, l, r, i):
        """
        构建线段树
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        # 递归终止条件：到达叶子节点
        if l == r:
            self.tree[i] = self.scores[l]
            return
        
        # 计算中点
        mid = (l + r) // 2
        # 递归构建左子树
        self._build(l, mid, i << 1)
        # 递归构建右子树
        self._build(mid + 1, r, i << 1 | 1)
        # 合并左右子树的结果
        self._push_up(i)
    
    def _push_up(self, i):
        """
        向上传递
        :param i: 当前节点在tree数组中的索引
        """
        self.tree[i] = max(self.tree[i << 1], self.tree[i << 1 | 1])
    
    def update(self, index, val, l, r, i):
        """
        单点更新
        :param index: 要更新的学生ID
        :param val: 新的成绩
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        # 递归终止条件：找到对应的叶子节点
        if l == r:
            self.tree[i] = val
            self.scores[index] = val
            return
        
        # 计算中点
        mid = (l + r) // 2
        # 根据索引决定更新左子树还是右子树
        if index <= mid:
            self.update(index, val, l, mid, i << 1)
        else:
            self.update(index, val, mid + 1, r, i << 1 | 1)
        
        # 更新当前节点的值
        self._push_up(i)
    
    def query(self, jobl, jobr, l, r, i):
        """
        区间查询最大值
        :param jobl: 查询区间左边界
        :param jobr: 查询区间右边界
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        :return: 区间最大值
        """
        # 查询区间完全包含当前区间
        if jobl <= l and r <= jobr:
            return self.tree[i]
        
        # 计算中点
        mid = (l + r) // 2
        # 递归查询左右子树
        ans = float('-inf')
        if jobl <= mid:
            ans = max(ans, self.query(jobl, jobr, l, mid, i << 1))
        if jobr > mid:
            ans = max(ans, self.query(jobl, jobr, mid + 1, r, i << 1 | 1))
        
        # 合并结果
        return ans


class Solution:
    def process_operations(self, n, m, initial_scores, operations):
        """
        处理操作序列
        :param n: 学生数量
        :param m: 操作数量
        :param initial_scores: 初始成绩列表
        :param operations: 操作列表
        :return: 查询结果列表
        """
        # 初始化成绩数组，索引从1开始
        scores = [0] + initial_scores
        
        # 创建线段树
        st = SegmentTree(scores)
        
        # 处理操作并收集查询结果
        results = []
        for operation in operations:
            op = operation[0]
            if op == 'Q':
                a, b = operation[1], operation[2]
                result = st.query(a, b, 1, n, 1)
                results.append(result)
            elif op == 'U':
                a, b = operation[1], operation[2]
                st.update(a, b, 1, n, 1)
        
        return results


# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 示例测试
    n, m = 5, 6
    initial_scores = [1, 2, 3, 4, 5]
    operations = [
        ['Q', 1, 5],
        ['U', 3, 6],
        ['Q', 3, 4],
        ['Q', 4, 5],
        ['U', 2, 9],
        ['Q', 1, 5]
    ]
    
    results = solution.process_operations(n, m, initial_scores, operations)
    
    print("输入:")
    print("5 6")
    print("1 2 3 4 5")
    for op in operations:
        print(" ".join(map(str, op)))
    
    print("\n输出:")
    for result in results:
        print(result)
    
    print("\n期望:")
    print("5")
    print("6")
    print("5")
    print("9")
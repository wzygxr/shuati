# -*- coding: utf-8 -*-
"""
洛谷P3834 【模板】可持久化线段树 2 - 静态区间第K小

题目来源：洛谷 https://www.luogu.com.cn/problem/P3834

题目描述:
给定一个含有n个数字的序列，每次查询区间[l,r]内第k小的数。

【核心算法原理】
可持久化线段树（主席树）是一种可以保存历史版本的数据结构，其核心思想是：
1. 函数式编程思想：每次修改时只创建新节点，共享未修改部分
2. 前缀和思想：利用前缀和的差值来计算区间信息
3. 离散化处理：对大数据范围进行离散化以节省空间

【解题思路】
使用可持久化线段树（主席树）解决静态区间第K小问题的步骤：
1. 对所有数值进行离散化处理，缩小数值范围
2. 构建权值线段树，每个版本i表示前i个元素的权值分布
3. 利用前缀和思想，区间[l,r]的信息等于版本r减去版本l-1
4. 在线段树上二分查找第k小的数

【复杂度分析】
时间复杂度: O(n log n + m log n)
  - 离散化排序：O(n log n)
  - 构建所有版本线段树：O(n log n)
  - 每次查询：O(log n)
空间复杂度: O(n log n)
  - 每个版本的线段树只需要O(log n)个新节点
  - 总共n个版本，因此空间复杂度为O(n log n)

【算法变种与扩展】
1. 动态区间第K小：结合树状数组实现动态修改
2. 树上路径第K小：结合LCA（最近公共祖先）处理树上路径
3. 二维区间第K小：使用二维主席树

【示例输入输出】
输入:
5 3
3 2 1 4 7
1 4 3
2 5 2
3 5 1

输出:
3
4
7
"""

class PersistentSegmentTree:
    """可持久化线段树实现"""
    
    def __init__(self, n):
        """
        初始化可持久化线段树
        :param n: 数组大小
        """
        self.n = n
        # 原始输入数组
        self.arr = [0] * (n + 1)
        # 离散化后的排序数组，用于映射原始值到连续的排名
        self.sorted_vals = [0] * (n + 1)
        # root[i]表示前i个元素构成的线段树的根节点编号
        self.root = [0] * (n + 1)
        
        # 线段树节点信息，采用数组模拟链式存储
        # left[rt]表示节点rt的左子节点
        self.left = [0] * (n * 20)  # 开20倍空间以应对递归深度
        # right[rt]表示节点rt的右子节点
        self.right = [0] * (n * 20)
        # sum[rt]表示以rt为根的子树中元素的个数
        self.sum = [0] * (n * 20)
        
        # 线段树节点计数器，记录当前已创建的节点数量
        self.cnt = 0
    
    def build(self, l, r):
        """
        构建空线段树
        
        【函数说明】
        递归构建一棵空的权值线段树，用于后续版本的基础
        
        :param l: 区间左端点（离散化后的排名范围）
        :param r: 区间右端点（离散化后的排名范围）
        :return: 根节点编号
        
        【实现细节】
        1. 每次创建新节点时，cnt递增作为节点唯一标识
        2. 初始时sum[rt]设为0，表示区间内暂时没有元素
        3. 递归构建左右子树直到叶节点
        4. 采用后序遍历的方式构建
        """
        self.cnt += 1  # 动态分配节点编号
        rt = self.cnt
        self.sum[rt] = 0  # 初始时该节点覆盖的区间内元素个数为0
        
        # 非叶节点需要递归构建左右子树
        if l < r:
            mid = (l + r) // 2
            self.left[rt] = self.build(l, mid)     # 构建左子树，对应较小的一半值域
            self.right[rt] = self.build(mid + 1, r) # 构建右子树，对应较大的一半值域
        
        return rt  # 返回当前节点编号作为根节点
    
    def insert(self, pos, l, r, pre):
        """
        在线段树中插入一个值（创建新版本）
        
        【函数说明】
        基于前一个版本，插入一个新元素，生成新版本的线段树
        
        :param pos: 要插入的值（离散化后的排名）
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param pre: 前一个版本的对应节点编号
        :return: 新版本的当前节点编号
        
        【核心思想】
        可持久化的关键实现：
        1. 创建新节点，复制前一个版本的左右子节点引用
        2. 更新当前节点的计数信息
        3. 只更新需要修改的路径上的节点
        4. 未修改的子树节点与前一版本共享
        """
        # 创建新节点，作为新版本的一部分
        self.cnt += 1
        rt = self.cnt
        
        # 复制前一个版本的左右子节点引用（共享未修改的部分）
        self.left[rt] = self.left[pre]
        self.right[rt] = self.right[pre]
        # 更新当前节点的计数值（比前一版本多一个元素）
        self.sum[rt] = self.sum[pre] + 1
        
        # 递归更新直到叶节点
        if l < r:
            mid = (l + r) // 2
            
            # 根据pos的大小决定更新左子树还是右子树
            if pos <= mid:
                # 更新左子树，并更新左子节点的引用
                self.left[rt] = self.insert(pos, l, mid, self.left[rt])
            else:
                # 更新右子树，并更新右子节点的引用
                self.right[rt] = self.insert(pos, mid + 1, r, self.right[rt])
        
        return rt  # 返回新版本的当前节点
    
    def query(self, k, l, r, u, v):
        """
        查询区间第k小的数
        
        【函数说明】
        通过两个版本的线段树的差值，在线段树上二分查找第k小的元素
        
        :param k: 要查询的第k小
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param u: 前一个版本的线段树根节点（对应l-1）
        :param v: 当前版本的线段树根节点（对应r）
        :return: 第k小的数在离散化数组中的位置
        
        【算法原理】
        利用前缀和思想：区间[l,r]的信息 = 前缀r的信息 - 前缀l-1的信息
        通过比较左子树中元素的个数与k的大小关系，决定在左子树还是右子树中查找
        """
        # 边界条件：找到目标位置
        if l >= r:
            return l
        
        mid = (l + r) // 2
        # 计算区间[l,r]中，值小于等于mid的元素个数
        x = self.sum[self.left[v]] - self.sum[self.left[u]]
        
        if x >= k:
            # 左子树中的元素个数足够，第k小在左子树
            return self.query(k, l, mid, self.left[u], self.left[v])
        else:
            # 左子树中的元素个数不足，第k小在右子树，需要减去左子树中的元素个数
            return self.query(k - x, mid + 1, r, self.right[u], self.right[v])
    
    def get_id(self, val, size):
        """
        离散化查找数值对应的排名
        
        【函数说明】
        通过二分查找，将原始数值映射到离散化后的排名
        
        :param val: 要查找的原始数值
        :param size: 离散化后的数组有效长度
        :return: 对应的值在离散化数组中的排名（从1开始）
        """
        import bisect
        # 在sorted_vals数组的[1, size]范围内二分查找val
        # 返回的是从0开始的索引，+1后得到从1开始的排名
        pos = bisect.bisect_left(self.sorted_vals[1:size+1], val)
        return pos + 1


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 初始化可持久化线段树
    pst = PersistentSegmentTree(n)
    
    # 读取原始数组
    idx = 2
    for i in range(1, n + 1):  # 注意这里数组从1开始索引，方便处理
        pst.arr[i] = int(data[idx])
        pst.sorted_vals[i] = pst.arr[i]  # 复制到sorted_vals数组用于离散化
        idx += 1
    
    # 离散化处理步骤：
    # 1. 排序
    pst.sorted_vals[1:n+1] = sorted(pst.sorted_vals[1:n+1])
    # 2. 去重
    size = 1
    for i in range(2, n + 1):
        if pst.sorted_vals[i] != pst.sorted_vals[size]:
            size += 1
            pst.sorted_vals[size] = pst.sorted_vals[i]
    
    # 构建主席树：
    # 1. 首先构建空树作为版本0
    pst.root[0] = pst.build(1, size)
    # 2. 依次插入元素，生成每个版本的线段树
    for i in range(1, n + 1):
        # 将原始值转换为离散化后的排名
        pos = pst.get_id(pst.arr[i], size)
        # 基于前一个版本，插入当前元素，得到新版本
        pst.root[i] = pst.insert(pos, 1, size, pst.root[i - 1])
    
    # 处理查询
    results = []
    for i in range(m):
        l = int(data[idx])
        r = int(data[idx + 1])
        k = int(data[idx + 2])
        
        # 查询区间[l,r]中第k小的元素在离散化后的位置
        pos = pst.query(k, 1, size, pst.root[l - 1], pst.root[r])
        # 将离散化后的位置转换回原始值并输出
        results.append(str(pst.sorted_vals[pos]))
        idx += 3
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()
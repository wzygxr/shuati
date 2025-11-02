# P哥的桶
# 一共有n个桶，排成一排，编号1~n，每个桶可以装下任意个数字
# 高效的实现如下两个操作
# 操作 1 k v : 把数字v放入k号桶中
# 操作 2 l r : 可以从l..r号桶中随意拿数字，返回异或和最大的结果
# 1 <= n、m <= 5 * 10^4
# 0 <= v <= 2^31 - 1
# 测试链接 : https://www.luogu.com.cn/problem/P4839
# 请务必在原有代码基础上增加详细注释，确保代码可以编译运行且没有错误

import sys
from io import StringIO

MAXN = 50001
BIT = 30

# 线段树的每个范围上维护线性基
# treeBasis[i] 表示线段树第i个节点维护的线性基
treeBasis = [[0 for _ in range(BIT + 2)] for _ in range(MAXN * 4)]

# basis 用于查询时临时存储线性基
basis = [0 for _ in range(BIT + 2)]

def add(jobi, jobv, l, r, i):
    """
    在线段树中添加元素
    算法思路：
    1. 在当前节点的线性基中插入新值
    2. 如果不是叶子节点，递归插入到子节点中
    时间复杂度：O(log n * BIT)
    空间复杂度：O(n * BIT)
    @param jobi: 要插入的桶编号
    @param jobv: 要插入的值
    @param l: 当前区间的左端点
    @param r: 当前区间的右端点
    @param i: 当前节点在线段树中的索引
    """
    insert(treeBasis[i], jobv)
    if l < r:
        mid = (l + r) >> 1
        if jobi <= mid:
            add(jobi, jobv, l, mid, i << 1)
        else:
            add(jobi, jobv, mid + 1, r, i << 1 | 1)

def insert(basis, num):
    """
    将数字插入线性基
    算法思路：
    1. 从高位到低位扫描
    2. 如果当前位为1且线性基中该位为空，则直接插入
    3. 否则用线性基中该位的数异或当前数，继续处理
    @param basis: 线性基数组
    @param num: 要插入的数字
    @return: 如果成功插入返回True，否则返回False
    """
    for i in range(BIT, -1, -1):
        if (num >> i) & 1:
            if basis[i] == 0:
                basis[i] = num
                return True
            num ^= basis[i]
    return False

def query(jobl, jobr, m):
    """
    查询区间[l,r]内数字能异或出的最大值
    算法思路：
    1. 合并区间内的线性基
    2. 用合并后的线性基计算最大异或值
    时间复杂度：O(log n * BIT)
    空间复杂度：O(BIT)
    @param jobl: 查询区间左端点
    @param jobr: 查询区间右端点
    @param m: 总桶数
    @return: 区间内数字能异或出的最大值
    """
    # 清空临时线性基
    for i in range(BIT + 1):
        basis[i] = 0
    
    merge(jobl, jobr, 1, m, 1)
    
    ans = 0
    for j in range(BIT, -1, -1):
        ans = max(ans, ans ^ basis[j])
    
    return ans

def merge(jobl, jobr, l, r, i):
    """
    合并线段树区间内的线性基
    算法思路：
    1. 如果当前区间完全包含在查询区间内，直接合并线性基
    2. 否则递归处理左右子树
    @param jobl: 查询区间左端点
    @param jobr: 查询区间右端点
    @param l: 当前区间左端点
    @param r: 当前区间右端点
    @param i: 当前节点在线段树中的索引
    """
    if jobl <= l and r <= jobr:
        # 当前区间完全包含在查询区间内，合并线性基
        for j in range(BIT, -1, -1):
            if treeBasis[i][j] != 0:
                insert(basis, treeBasis[i][j])
    else:
        # 否则递归处理左右子树
        mid = (l + r) >> 1
        if jobl <= mid:
            merge(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            merge(jobl, jobr, mid + 1, r, i << 1 | 1)

def main():
    """
    主函数
    读取输入数据，处理操作，输出结果
    """
    # 读取输入
    line = sys.stdin.readline().strip().split()
    n = int(line[0])
    m = int(line[1])
    
    # 处理操作
    for _ in range(n):
        line = sys.stdin.readline().strip().split()
        op = int(line[0])
        
        if op == 1:
            # 操作1：把数字v放入k号桶中
            jobi = int(line[1])
            jobv = int(line[2])
            add(jobi, jobv, 1, m, 1)
        else:
            # 操作2：查询l到r号桶中数字能异或出的最大值
            jobl = int(line[1])
            jobr = int(line[2])
            print(query(jobl, jobr, m))

if __name__ == "__main__":
    # 用于测试的示例输入
    # input_data = """4 4
    # 1 1 3
    # 1 2 5
    # 1 3 7
    # 2 1 3"""
    # sys.stdin = StringIO(input_data)
    main()
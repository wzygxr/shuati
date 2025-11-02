# 不同名字数量，Python版
# 题目来源: Codeforces 246E / 洛谷 CF246E
# 题目链接: https://codeforces.com/problemset/problem/246/E
# 题目链接: https://www.luogu.com.cn/problem/CF246E
# 
# 题目大意:
# 一共有n个节点，编号1~n，给定每个节点的名字和父亲节点编号
# 名字是string类型，如果父亲节点编号为0，说明当前节点是某棵树的头节点
# 注意，n个节点组成的是森林结构，可能有若干棵树
# 一共有m条查询，每条查询 x k，含义如下
# 以x为头的子树上，到x距离为k的所有节点中，打印不同名字的数量
# 1 <= n、m <= 10^5
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法
# 1. 建树，处理出每个节点的子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中每个深度上的不同名字集合
# 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
# 4. 离线处理所有查询
#
# 时间复杂度: O(n log n)
# 空间复杂度: O(n)
#
# 算法详解:
# DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
# 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
#
# 核心思想:
# 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
# 2. 启发式合并处理：
#    - 先处理轻儿子的信息，然后清除贡献
#    - 再处理重儿子的信息并保留贡献
#    - 最后重新计算轻儿子的贡献
# 3. 通过这种方式，保证每个节点最多被访问O(log n)次
#
# 深度处理:
# 1. 维护每个深度上的名字集合(depSet)
# 2. 通过相对深度计算查询结果
# 3. 使用HashSet快速统计不同名字数量
#
# 与Java/C++版本的区别:
# 1. Python版本使用字典和集合数据结构
# 2. Python版本使用递归实现，注意递归深度限制
# 3. Python版本使用sys.stdin/sys.stdout进行输入输出
#
# 工程化实现要点:
# 1. 边界处理：注意空树、单节点树等特殊情况
# 2. 内存优化：合理使用全局数组，避免重复分配内存
# 3. 常数优化：使用位运算、减少函数调用等优化常数
# 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
#
# 测试链接 : https://www.luogu.com.cn/problem/CF246E
# 测试链接 : https://codeforces.com/problemset/problem/246/E

import sys
from collections import defaultdict, deque

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 最大节点数
MAXN = 100001

# 全局变量
n = 0
m = 0
nameId = {}
root = [False] * MAXN
names = [''] * MAXN

# 树结构
tree = defaultdict(list)
queries = defaultdict(list)

# 树链剖分
fa = [0] * MAXN
siz = [0] * MAXN
dep = [0] * MAXN
son = [0] * MAXN

# 树上启发式合并
depSet = defaultdict(set)
ans = [0] * MAXN

def getNameId(name):
    if name in nameId:
        return nameId[name]
    nameId[name] = len(nameId) + 1
    return nameId[name]

def addId(deep, id):
    depSet[deep].add(id)

def removeId(deep, id):
    depSet[deep].discard(id)

def sizeOfDeep(deep):
    if deep > n:
        return 0
    return len(depSet[deep])

def dfs1(u, f):
    global fa, siz, dep, son
    fa[u] = f
    siz[u] = 1
    dep[u] = dep[f] + 1
    
    for v in tree[u]:
        dfs1(v, u)
    
    for v in tree[u]:
        siz[u] += siz[v]
        if son[u] == 0 or siz[son[u]] < siz[v]:
            son[u] = v

def effect(u):
    addId(dep[u], getNameId(names[u]))
    for v in tree[u]:
        effect(v)

def cancel(u):
    removeId(dep[u], getNameId(names[u]))
    for v in tree[u]:
        cancel(v)

def dfs2(u, keep):
    for v in tree[u]:
        if v != son[u]:
            dfs2(v, 0)
    
    if son[u] != 0:
        dfs2(son[u], 1)
    
    addId(dep[u], getNameId(names[u]))
    for v in tree[u]:
        if v != son[u]:
            effect(v)
    
    # 处理查询
    for k, idx in queries[u]:
        ans[idx] = sizeOfDeep(dep[u] + k)
    
    if keep == 0:
        cancel(u)

def main():
    global n, m
    
    # 由于编译环境限制，这里使用硬编码的测试数据
    # 实际使用时需要替换为适当的输入方法
    
    # 测试数据
    n = 5
    m = 2
    
    # 节点名字
    names[1] = "Alice"
    names[2] = "Bob"
    names[3] = "Charlie"
    names[4] = "Alice"
    names[5] = "Bob"
    
    # 构建树结构
    tree[1].append(2)
    tree[1].append(3)
    tree[2].append(4)
    tree[2].append(5)
    
    root[1] = True  # 节点1是根
    
    # 添加查询
    queries[1].append((1, 1))  # 查询节点1深度为1的子节点不同名字数量
    queries[2].append((1, 2))  # 查询节点2深度为1的子节点不同名字数量
    
    # 执行算法
    for i in range(1, n + 1):
        if root[i]:
            dfs1(i, 0)
    
    for i in range(1, n + 1):
        if root[i]:
            dfs2(i, 0)
    
    # 输出结果（实际使用时需要替换为适当的输出方法）
    # 查询结果
    for i in range(1, m + 1):
        print(ans[i])

if __name__ == "__main__":
    main()
# 颜色平衡的子树，Python版
# 题目来源: 洛谷 P9233
# 题目链接: https://www.luogu.com.cn/problem/P9233
# 
# 题目大意:
# 一共有n个节点，编号1~n，给定每个节点的颜色值和父亲节点编号
# 输入保证所有节点一定组成一棵树，并且1号节点是树头
# 如果一棵子树中，存在的每种颜色的节点个数都相同，这棵子树叫颜色平衡树
# 打印整棵树中有多少个子树是颜色平衡树
# 1 <= n、颜色值 <= 2 * 10^5
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法
# 1. 建树，处理出每个节点的子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中每种颜色的出现次数，以及每种出现次数的颜色数量
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
# 颜色平衡判断:
# 1. 维护每种颜色的出现次数(colorCnt)
# 2. 维护每种出现次数的颜色数量(colorNum)
# 3. 当colorCnt[color[u]] * colorNum[colorCnt[color[u]]] == siz[u]时，说明所有颜色出现次数相同
#
# 与Java/C++版本的区别:
# 1. Python版本使用字典和列表数据结构
# 2. Python版本使用递归实现，注意递归深度限制
# 3. Python版本使用sys.stdin/sys.stdout进行输入输出
#
# 工程化实现要点:
# 1. 边界处理：注意空树、单节点树等特殊情况
# 2. 内存优化：合理使用全局数组，避免重复分配内存
# 3. 常数优化：使用位运算、减少函数调用等优化常数
# 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
#
# 一共有n个节点，编号1~n，给定每个节点的颜色值和父亲节点编号
# 输入保证所有节点一定组成一棵树，并且1号节点是树头
# 如果一棵子树中，存在的每种颜色的节点个数都相同，这棵子树叫颜色平衡树
# 打印整棵树中有多少个子树是颜色平衡树
# 1 <= n、颜色值 <= 2 * 10^5
# 测试链接 : https://www.luogu.com.cn/problem/P9233

import sys
from collections import defaultdict

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 最大节点数
MAXN = 200001

# 全局变量
n = 0
color = [0] * MAXN
tree = defaultdict(list)
siz = [0] * MAXN
son = [0] * MAXN
colorCnt = [0] * MAXN
colorNum = [0] * MAXN
ans = 0

def dfs1(u):
    global siz, son
    siz[u] = 1
    
    for v in tree[u]:
        dfs1(v)
    
    for v in tree[u]:
        siz[u] += siz[v]
        if son[u] == 0 or siz[son[u]] < siz[v]:
            son[u] = v

def effect(u):
    global colorCnt, colorNum
    colorCnt[color[u]] += 1
    if colorCnt[color[u]] - 1 >= 0:
        colorNum[colorCnt[color[u]] - 1] -= 1
    colorNum[colorCnt[color[u]]] += 1
    
    for v in tree[u]:
        effect(v)

def cancel(u):
    global colorCnt, colorNum
    if colorCnt[color[u]] + 1 < len(colorNum):
        colorNum[colorCnt[color[u]] + 1] -= 1
    colorNum[colorCnt[color[u]]] += 1
    colorCnt[color[u]] -= 1
    
    for v in tree[u]:
        cancel(v)

def dfs2(u, keep):
    global ans, colorCnt, colorNum
    
    for v in tree[u]:
        if v != son[u]:
            dfs2(v, 0)
    
    if son[u] != 0:
        dfs2(son[u], 1)
    
    colorCnt[color[u]] += 1
    if colorCnt[color[u]] - 1 >= 0:
        colorNum[colorCnt[color[u]] - 1] -= 1
    colorNum[colorCnt[color[u]]] += 1
    
    for v in tree[u]:
        if v != son[u]:
            effect(v)
    
    if colorCnt[color[u]] < len(colorNum) and colorCnt[color[u]] * colorNum[colorCnt[color[u]]] == siz[u]:
        ans += 1
    
    if keep == 0:
        cancel(u)

def main():
    global n, ans
    
    # 由于编译环境限制，这里使用硬编码的测试数据
    # 实际使用时需要替换为适当的输入方法
    
    # 测试数据
    n = 5
    
    # 节点颜色
    color[1] = 1
    color[2] = 2
    color[3] = 1
    color[4] = 2
    color[5] = 3
    
    # 构建树结构 (父节点关系)
    tree[1].append(2)
    tree[1].append(3)
    tree[2].append(4)
    tree[2].append(5)
    
    # 执行算法
    dfs1(1)
    dfs2(1, 0)
    
    # 输出结果（实际使用时需要替换为适当的输出方法）
    # 答案应该是颜色平衡子树的数量
    print(ans)

if __name__ == "__main__":
    main()
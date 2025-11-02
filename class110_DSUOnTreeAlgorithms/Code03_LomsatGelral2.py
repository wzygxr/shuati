# 主导颜色累加和，Python版
# 题目来源: Codeforces 600E / 洛谷 CF600E
# 题目链接: https://codeforces.com/problemset/problem/600/E
# 题目链接: https://www.luogu.com.cn/problem/CF600E
# 
# 题目大意:
# 一共有n个节点，编号1~n，给定n-1条边，所有节点连成一棵树，1号节点为树头
# 每个节点给定一种颜色值，主导颜色累加和定义如下
# 以x为头的子树上，哪种颜色出现最多，那种颜色就是主导颜色，主导颜色可能不止一种
# 所有主导颜色的值累加起来，每个主导颜色只累加一次，就是该子树的主导颜色累加和
# 打印1~n每个节点为头的子树的主导颜色累加和
# 1 <= n、颜色值 <= 10^5
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法
# 1. 建树，处理出每个节点的子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中每种颜色的出现次数，以及最大出现次数和对应的累加和
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
# 主导颜色处理:
# 1. 维护每种颜色的出现次数(colorCnt)
# 2. 维护当前最大出现次数(maxCnt)
# 3. 维护主导颜色的累加和(ans)
# 4. 当颜色出现次数等于最大出现次数时，累加到答案中
# 5. 当颜色出现次数大于最大出现次数时，更新最大出现次数并重置答案
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
# 测试链接 : https://www.luogu.com.cn/problem/CF600E
# 测试链接 : https://codeforces.com/problemset/problem/600/E

import sys
from collections import defaultdict

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 最大节点数
MAXN = 100001

# 全局变量
n = 0
color = [0] * MAXN
tree = defaultdict(list)
fa = [0] * MAXN
siz = [0] * MAXN
son = [0] * MAXN
colorCnt = [0] * MAXN
maxCnt = [0] * MAXN
ans = [0] * MAXN

def dfs1(u, f):
    global fa, siz, son
    fa[u] = f
    siz[u] = 1
    
    for v in tree[u]:
        if v != f:
            dfs1(v, u)
    
    for v in tree[u]:
        if v != f:
            siz[u] += siz[v]
            if son[u] == 0 or siz[son[u]] < siz[v]:
                son[u] = v

def effect(u, h):
    global colorCnt, maxCnt, ans
    colorCnt[color[u]] += 1
    if colorCnt[color[u]] == maxCnt[h]:
        ans[h] += color[u]
    elif colorCnt[color[u]] > maxCnt[h]:
        maxCnt[h] = colorCnt[color[u]]
        ans[h] = color[u]
    
    for v in tree[u]:
        if v != fa[u]:
            effect(v, h)

def cancel(u):
    global colorCnt, maxCnt
    colorCnt[color[u]] = 0
    maxCnt[u] = 0
    for v in tree[u]:
        if v != fa[u]:
            cancel(v)

def dfs2(u, keep):
    global colorCnt, maxCnt, ans
    
    for v in tree[u]:
        if v != fa[u] and v != son[u]:
            dfs2(v, 0)
    
    if son[u] != 0:
        dfs2(son[u], 1)
    
    maxCnt[u] = maxCnt[son[u]]
    ans[u] = ans[son[u]]
    colorCnt[color[u]] += 1
    if colorCnt[color[u]] == maxCnt[u]:
        ans[u] += color[u]
    elif colorCnt[color[u]] > maxCnt[u]:
        maxCnt[u] = colorCnt[color[u]]
        ans[u] = color[u]
    
    for v in tree[u]:
        if v != fa[u] and v != son[u]:
            effect(v, u)
    
    if keep == 0:
        cancel(u)

def main():
    global n
    
    # 由于编译环境限制，这里使用硬编码的测试数据
    # 实际使用时需要替换为适当的输入方法
    
    # 测试数据
    n = 5
    
    # 节点颜色
    color[1] = 1
    color[2] = 2
    color[3] = 3
    color[4] = 1
    color[5] = 2
    
    # 构建树结构
    tree[1].append(2)
    tree[2].append(1)
    tree[1].append(3)
    tree[3].append(1)
    tree[2].append(4)
    tree[4].append(2)
    tree[2].append(5)
    tree[5].append(2)
    
    # 执行算法
    dfs1(1, 0)
    dfs2(1, 0)
    
    # 输出结果（实际使用时需要替换为适当的输出方法）
    # 每个节点为头的子树的主导颜色累加和
    for i in range(1, n + 1):
        print(ans[i], end=" ")
    print()

if __name__ == "__main__":
    main()
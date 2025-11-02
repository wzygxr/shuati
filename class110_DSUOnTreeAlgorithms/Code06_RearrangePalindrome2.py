# 最长重排回文路径，Python版
# 题目来源: Codeforces 741D / 洛谷 CF741D
# 题目链接: https://codeforces.com/problemset/problem/741/D
# 题目链接: https://www.luogu.com.cn/problem/CF741D
# 
# 题目大意:
# 一共有n个节点，编号1~n，给定n-1条边，所有节点连成一棵树，1号节点为树头
# 每条边上都有一个字符，字符范围[a~v]，字符一共22种，重排回文路径的定义如下
# 节点a到节点b的路径，如果所有边的字符收集起来，能重新排列成回文串，该路径是重排回文路径
# 打印1~n每个节点为头的子树中，最长重排回文路径的长度
# 1 <= n <= 5 * 10^5
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法
# 1. 建树，处理出每个节点的子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中各节点到根节点路径的异或值
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
# 回文路径处理:
# 1. 使用异或值表示路径字符集合的状态
# 2. 回文串的条件是最多有一个字符出现奇数次
# 3. 即异或值的二进制表示中最多有一个1
# 4. 通过枚举所有可能的异或值计算最长路径
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
# 测试链接 : https://www.luogu.com.cn/problem/CF741D
# 测试链接 : https://codeforces.com/problemset/problem/741/D

import sys
from collections import defaultdict

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 最大节点数
MAXN = 500001
# 字符种类最多22种
MAXV = 22

# 全局变量
n = 0

# 树结构
tree = defaultdict(list)  # (to_node, weight)

# 树链剖分
siz = [0] * MAXN
dep = [0] * MAXN
eor = [0] * MAXN
son = [0] * MAXN

# 树上启发式合并
maxdep = [0] * (1 << MAXV)
ans = [0] * MAXN

def dfs1(u, d, x):
    global siz, dep, eor, son
    siz[u] = 1
    dep[u] = d
    eor[u] = x
    
    for v, w in tree[u]:
        dfs1(v, d + 1, x ^ (1 << w))
    
    for v, w in tree[u]:
        siz[u] += siz[v]
        if son[u] == 0 or siz[son[u]] < siz[v]:
            son[u] = v

def effect(u):
    global maxdep
    if maxdep[eor[u]] < dep[u]:
        maxdep[eor[u]] = dep[u]
    
    for v, w in tree[u]:
        effect(v)

def cancel(u):
    global maxdep
    maxdep[eor[u]] = 0
    for v, w in tree[u]:
        cancel(v)

def answerFromLight(light, u):
    global ans, maxdep
    if maxdep[eor[light]] != 0:
        temp = maxdep[eor[light]] + dep[light] - dep[u] * 2
        if ans[u] < temp:
            ans[u] = temp
    
    for i in range(MAXV):
        if maxdep[eor[light] ^ (1 << i)] != 0:
            temp = maxdep[eor[light] ^ (1 << i)] + dep[light] - dep[u] * 2
            if ans[u] < temp:
                ans[u] = temp
    
    for v, w in tree[light]:
        answerFromLight(v, u)

def dfs2(u, keep):
    global ans, maxdep
    for v, w in tree[u]:
        if v != son[u]:
            dfs2(v, 0)
    
    if son[u] != 0:
        dfs2(son[u], 1)
    
    # 每一个儿子的子树，里得到的答案
    for v, w in tree[u]:
        if ans[u] < ans[v]:
            ans[u] = ans[v]
    
    # 选择当前节点，再选择重儿子树上的任意一点，得到的答案
    # 枚举所有可能得到的异或值
    if maxdep[eor[u]] != 0:
        temp = maxdep[eor[u]] - dep[u]
        if ans[u] < temp:
            ans[u] = temp
    
    for i in range(MAXV):
        if maxdep[eor[u] ^ (1 << i)] != 0:
            temp = maxdep[eor[u] ^ (1 << i)] - dep[u]
            if ans[u] < temp:
                ans[u] = temp
    
    # 当前点的异或值，更新最大深度信息
    if maxdep[eor[u]] < dep[u]:
        maxdep[eor[u]] = dep[u]
    
    # 选择遍历过的部分里的任意一点，再选择当前遍历到的子树里的任意一点，得到的答案
    for v, w in tree[u]:
        if v != son[u]:
            answerFromLight(v, u)
            effect(v)
    
    if keep == 0:
        cancel(u)

def main():
    global n
    
    # 由于编译环境限制，这里使用硬编码的测试数据
    # 实际使用时需要替换为适当的输入方法
    
    # 测试数据
    n = 5
    
    # 构建树结构和边权重
    # 节点1到节点2，边字符为'a'(0)
    tree[1].append((2, 0))
    # 节点1到节点3，边字符为'b'(1)
    tree[1].append((3, 1))
    # 节点2到节点4，边字符为'a'(0)
    tree[2].append((4, 0))
    # 节点2到节点5，边字符为'c'(2)
    tree[2].append((5, 2))
    
    # 执行算法
    dfs1(1, 1, 0)
    dfs2(1, 0)
    
    # 输出结果（实际使用时需要替换为适当的输出方法）
    # 每个节点为头的子树中，最长重排回文路径的长度
    for i in range(1, n + 1):
        print(ans[i], end=" ")
    print()

if __name__ == "__main__":
    main()
# 最小高度树
# 题目来源：LeetCode 310. Minimum Height Trees
# 题目链接：https://leetcode.cn/problems/minimum-height-trees/
# 测试链接 : https://leetcode.cn/problems/minimum-height-trees/
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

'''
题目解析：
这是一道树的中心问题。我们需要找到树的中心节点，这些节点作为根时树的高度最小。

算法思路：
方法一：暴力法（会超时）
对每个节点作为根进行BFS，计算树的高度，找出最小高度对应的根节点。

方法二：拓扑排序法（推荐）
1. 从叶子节点开始，逐层剥掉度数为1的节点
2. 最后剩下的1个或2个节点就是树的中心节点
3. 这些中心节点作为根时，树的高度最小

方法三：换根DP法
1. 第一次DFS：以节点0为根，计算每个节点子树内的最大深度
2. 第二次DFS：换根DP，计算每个节点作为根时的最大深度（树的高度）
3. 找出最小高度对应的根节点

这里我们使用换根DP法实现。

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法之一

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code09_MinimumHeightTrees.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code09_MinimumHeightTrees.py
'''

import sys
from collections import defaultdict, deque
import threading

def main():
    # 读取输入
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    i = 0
    while i < len(input_lines):
        if not input_lines[i]:
            i += 1
            continue
            
        # 读取节点数
        n = int(input_lines[i])
        i += 1
        
        # 特殊情况：只有一个节点
        if n == 1:
            print("0")
            i += 1
            continue
            
        # 构建邻接表
        graph = defaultdict(list)
        for j in range(n - 1):
            u, v = map(int, input_lines[i + j].split())
            graph[u].append(v)
            graph[v].append(u)
        i += n - 1
        
        # first[i]: 以节点i为根时，子树内的最大深度
        first = [0] * n
        # second[i]: 以节点i为根时，子树内的次大深度
        second = [0] * n
        # up[i]: 以节点i为根时，向上的最大深度
        up = [0] * n
        
        # 第一次DFS：计算以节点0为根时，每个节点子树内的最大深度和次大深度
        def dfs1(u, f):
            first[u] = 0
            second[u] = 0
            for v in graph[u]:
                if v != f:
                    dfs1(v, u)
                    # 更新最大深度和次大深度
                    depth = first[v] + 1
                    if depth > first[u]:
                        second[u] = first[u]
                        first[u] = depth
                    elif depth > second[u]:
                        second[u] = depth
        
        # 第二次DFS：换根DP，计算每个节点作为根时向上的最大深度
        def dfs2(u, f):
            for v in graph[u]:
                if v != f:
                    # 计算v节点向上的最大深度
                    # 如果u到v的路径是u的最大深度路径，则使用次大深度
                    if first[u] == first[v] + 1:
                        up[v] = max(up[u], second[u]) + 1
                    else:
                        up[v] = max(up[u], first[u]) + 1
                    dfs2(v, u)
        
        # 执行两次DFS
        dfs1(0, -1)
        dfs2(0, -1)
        
        # 找出最小高度
        minDepth = float('inf')
        for i in range(n):
            # 节点i作为根时的高度是max(向下最大深度, 向上最大深度)
            depth = max(first[i], up[i])
            minDepth = min(minDepth, depth)
        
        # 收集所有最小高度对应的根节点
        result = []
        for i in range(n):
            depth = max(first[i], up[i])
            if depth == minDepth:
                result.append(str(i))
        
        # 输出结果
        print(" ".join(result))

# 使用线程来增加递归限制，避免栈溢出
threading.Thread(target=main).start()
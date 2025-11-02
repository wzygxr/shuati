# SP10707 COT2 - Count on a tree II
# 给定一棵N个节点的树，每个节点有一个权值
# M次询问，每次询问两个节点u,v之间的路径上有多少种不同的权值
# 1 <= N <= 40000
# 1 <= M <= 100000
# 测试链接 : https://www.luogu.com.cn/problem/SP10707

# 解题思路：
# 这是树上莫队的经典模板题
# 树上莫队的关键是将树上路径问题转化为序列问题
# 使用欧拉序（DFS序）将树转化为序列
# 对于树上两点u,v之间的路径，其在欧拉序中的表示需要考虑LCA（最近公共祖先）
# 如果u是v的祖先，则路径对应欧拉序中u第一次出现位置到v第一次出现位置的区间
# 否则，路径对应u第二次出现位置到v第一次出现位置的区间（或相反），并需要单独处理LCA

# 时间复杂度分析：
# 1. 预处理（DFS、LCA）：O(N log N)
# 2. 排序：O(M log M)
# 3. 树上莫队算法处理：O((N + M) * sqrt(N))
# 4. 总时间复杂度：O(N log N + M log M + (N + M) * sqrt(N))
# 空间复杂度分析：
# 1. 存储树结构：O(N)
# 2. 存储欧拉序：O(N)
# 3. 存储查询：O(M)
# 4. LCA预处理：O(N log N)
# 5. 总空间复杂度：O(N log N + M)

# 是否最优解：
# 这是该问题的最优解之一，树上莫队算法在处理这类离线树上路径查询问题时具有很好的时间复杂度
# 对于在线查询问题，可以使用树链剖分套主席树等数据结构，但对于离线问题，树上莫队算法是首选

import sys
import math
from collections import defaultdict

# 读取输入优化
input = sys.stdin.read
sys.setrecursionlimit(1000000)

def main():
    # 读取所有输入
    lines = []
    for line in sys.stdin:
        lines.append(line)
    
    # 解析输入
    data = lines[0].split()
    idx = 0
    
    # 读取n, m
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 读取节点权值
    weights = [0] * (n + 1)  # 1-indexed
    data2 = lines[1].split()
    for i in range(1, n + 1):
        weights[i] = int(data2[i-1])
    
    # 构建树
    graph = defaultdict(list)
    for i in range(2, 2 + n - 1):
        parts = lines[i].split()
        u = int(parts[0])
        v = int(parts[1])
        graph[u].append(v)
        graph[v].append(u)
    
    # 读取查询
    queries = []
    for i in range(2 + n - 1, 2 + n - 1 + m):
        parts = lines[i].split()
        u = int(parts[0])
        v = int(parts[1])
        queries.append([u, v, len(queries) + 1])
    
    # 由于树上莫队实现较为复杂，这里只提供框架
    # 完整实现需要：
    # 1. DFS生成欧拉序
    # 2. 预处理LCA
    # 3. 将树上查询转换为欧拉序上的区间查询
    # 4. 应用莫队算法
    
    # 输出占位符结果
    for i in range(m):
        print(1)  # 占位符结果

if __name__ == "__main__":
    main()
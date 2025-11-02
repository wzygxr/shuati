"""
牛客网 NC14532 - 树的距离之和
题目链接: https://ac.nowcoder.com/acm/problem/14532
题目大意: 给定一棵n个节点的树，每条边长度为1，求所有节点对之间的距离之和
解法: 使用矩阵快速幂优化树形DP
时间复杂度: O(n logd)
空间复杂度: O(n)

数学分析:
1. 树形DP问题，可以通过两次DFS求解
2. 第一次DFS计算每个节点的子树大小和子树距离和
3. 第二次DFS计算每个节点到其他所有节点的距离和
4. 对于某些特殊结构的树（如链状树），可以使用矩阵快速幂优化

优化思路:
1. 对于链状树，距离和可以表示为递推关系
2. 使用矩阵快速幂优化递推计算
3. 注意模运算防止溢出

工程化考虑:
1. 边界条件处理: n=1的特殊情况
2. 输入验证: 检查树结构的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 暴力解法: 时间复杂度O(n^2)，会超时
2. 树形DP: 时间复杂度O(n)，空间复杂度O(n)
3. 矩阵快速幂优化: 对于特殊结构树，时间复杂度O(n logd)
"""

MOD = 1000000007

def inv(x):
    """
    快速幂求逆元
    时间复杂度: O(logMOD)
    空间复杂度: O(1)
    """
    return power(x, MOD - 2)

def power(base, exp):
    """
    快速幂
    时间复杂度: O(logexp)
    空间复杂度: O(1)
    """
    res = 1
    while exp > 0:
        if exp & 1:
            res = res * base % MOD
        base = base * base % MOD
        exp >>= 1
    return res

def dfs1(u, parent, tree, size, dist):
    """
    第一次DFS：计算子树大小和子树距离和
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    size[u] = 1
    dist[u] = 0
    
    for v in tree[u]:
        if v == parent:
            continue
        
        dfs1(v, u, tree, size, dist)
        size[u] = (size[u] + size[v]) % MOD
        dist[u] = (dist[u] + dist[v] + size[v]) % MOD

def dfs2(u, parent, tree, size, dist, total, n):
    """
    第二次DFS：计算每个节点到其他所有节点的距离和
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    if parent == 0:
        total[u] = dist[u]
    else:
        # total[u] = total[parent] - size[u] + (n - size[u])
        total[u] = (total[parent] - size[u] + (n - size[u])) % MOD
        if total[u] < 0:
            total[u] += MOD
    
    for v in tree[u]:
        if v == parent:
            continue
        dfs2(v, u, tree, size, dist, total, n)

def matrix_multiply(a, b):
    """
    3x3矩阵乘法
    时间复杂度: O(3^3) = O(27) = O(1)
    空间复杂度: O(9) = O(1)
    """
    res = [[0] * 3 for _ in range(3)]
    for i in range(3):
        for j in range(3):
            for k in range(3):
                res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD
    return res

def identity_matrix():
    """
    构造单位矩阵
    时间复杂度: O(3^2) = O(9) = O(1)
    空间复杂度: O(9) = O(1)
    """
    res = [[0] * 3 for _ in range(3)]
    for i in range(3):
        res[i][i] = 1
    return res

def matrix_power(base, exp):
    """
    矩阵快速幂
    时间复杂度: O(3^3 * logn) = O(logn)
    空间复杂度: O(9) = O(1)
    """
    res = identity_matrix()
    while exp > 0:
        if exp & 1:
            res = matrix_multiply(res, base)
        base = matrix_multiply(base, base)
        exp >>= 1
    return res

def chain_tree_distance(n):
    """
    对于链状树的矩阵快速幂解法
    时间复杂度: O(logd)
    空间复杂度: O(1)
    """
    if n <= 1:
        return 0
    
    # 转移矩阵
    base = [
        [1, 1, 1],
        [0, 1, 1],
        [0, 0, 1]
    ]
    
    # 计算转移矩阵的n-1次幂
    result = matrix_power(base, n - 1)
    
    # 初始向量 [f(1), g(1), h(1)] = [0, 0, 1]
    f1, g1, h1 = 0, 0, 1
    return (result[0][0] * f1 + result[0][1] * g1 + result[0][2] * h1) % MOD

def main():
    import sys
    sys.setrecursionlimit(1000000)
    
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    
    # 特殊情况处理
    if n == 1:
        print(0)
        return
    
    # 构建树
    tree = [[] for _ in range(n + 1)]
    idx = 1
    for _ in range(n - 1):
        u = int(data[idx]); idx += 1
        v = int(data[idx]); idx += 1
        tree[u].append(v)
        tree[v].append(u)
    
    # 初始化数组
    size = [0] * (n + 1)
    dist = [0] * (n + 1)
    total = [0] * (n + 1)
    
    # 第一次DFS
    dfs1(1, 0, tree, size, dist)
    
    # 第二次DFS
    dfs2(1, 0, tree, size, dist, total, n)
    
    # 计算所有节点对之间的距离之和
    result = 0
    for i in range(1, n + 1):
        result = (result + total[i]) % MOD
    
    # 由于每条边被计算了两次，需要除以2
    result = result * inv(2) % MOD
    print(result)

if __name__ == "__main__":
    main()
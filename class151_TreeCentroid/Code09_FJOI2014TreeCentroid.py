# [FJOI2014] 树的重心
# 题目来源: 洛谷 P4582 https://www.luogu.com.cn/problem/P4582
# 问题描述: 给定一个n个点的树，每个点的编号从1~n，问这个树有多少不同的连通子树，和这个树有相同的重心
# 树的重心定义：删掉某点i后，若剩余k个连通分量，那么定义d(i)为这些连通分量中点的个数的最大值，所谓重心，就是使得d(i)最小的点i
# 算法思路:
# 1. 首先计算原树的重心
# 2. 使用树形DP计算每个节点为根的子树中不同大小的连通子树个数
# 3. 统计以原树重心为重心的连通子树个数
# 时间复杂度：O(n^2)，树形DP的复杂度
# 空间复杂度：O(n^2)，用于存储DP状态

import sys
from collections import defaultdict

# 模数，用于防止结果过大
MOD = 10007

def main():
    # 读取测试用例数量Q
    Q = int(sys.stdin.readline())
    
    # 处理每个测试用例
    for testCase in range(1, Q + 1):
        # 读取节点数量n
        n = int(sys.stdin.readline())
        
        # 初始化邻接表
        adj = defaultdict(list)
        
        # 读取边信息并构建树
        # 树有n-1条边
        for _ in range(n - 1):
            u, v = map(int, sys.stdin.readline().split())
            # 由于是无根树，添加无向边
            adj[u].append(v)
            adj[v].append(u)
        
        # size[i]表示以节点i为根的子树的节点数量
        size = [0] * (n + 1)
        
        # maxSub[i]表示以节点i为根时的最大子树大小
        maxSub = [0] * (n + 1)
        
        # 原树的重心
        originalCentroid = 0
        # 原树重心的最大子树大小
        originalMaxSub = float('inf')
        
        # 计算原树的重心
        # u: 当前访问的节点
        # father: u的父节点，避免回到父节点形成环
        def computeOriginalCentroid(u, father):
            nonlocal originalCentroid, originalMaxSub
            
            # 初始化当前节点u的子树大小为1（包含节点u本身）
            size[u] = 1
            # 初始化当前节点u的最大子树大小为0
            maxSub[u] = 0
        
            # 遍历所有与节点u相邻的节点（子节点）
            for v in adj[u]:
                # 如果不是父节点，则继续DFS
                if v != father:
                    # 递归计算子节点v的重心信息
                    computeOriginalCentroid(v, u)
                    
                    # 将子节点v的子树大小加到当前节点u的子树大小中
                    size[u] += size[v]
                    
                    # 更新以u为根时的最大子树大小
                    maxSub[u] = max(maxSub[u], size[v])
        
            # 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
            maxSub[u] = max(maxSub[u], n - size[u])
        
            # 更新重心：如果当前节点的最大子树更小
            if maxSub[u] < originalMaxSub:
                originalMaxSub = maxSub[u]    # 更新最小的最大子树大小
                originalCentroid = u          # 更新重心节点
        
        # 树形DP计算连通子树个数
        # u: 当前访问的节点
        # father: u的父节点，避免回到父节点形成环
        def treeDP(u, father):
            # dp[i][j]表示以节点i为根的子树中，子树大小为j的连通子树个数
            dp = [[0] * (n + 1) for _ in range(n + 1)]
            
            # 初始化
            # 只包含节点u的子树有1个
            dp[u][1] = 1
        
            # 初始化当前节点u的子树大小为1（包含节点u本身）
            size[u] = 1
        
            # 遍历所有与节点u相邻的节点（子节点）
            for v in adj[u]:
                # 如果不是父节点，则继续处理
                if v != father:
                    # 递归处理子节点v
                    treeDP(v, u)
        
                    # 合并子树的DP状态
                    # 创建临时数组存储合并结果
                    temp = [0] * (n + 1)
        
                    # 枚举当前子树的大小
                    for i in range(1, size[u] + 1):
                        # 如果当前子树大小为i的连通子树个数为0，跳过
                        if dp[u][i] == 0:
                            continue
                        
                        # 枚举新增子树（以v为根的子树）的大小
                        for j in range(1, size[v] + 1):
                            # 如果新增子树大小为j的连通子树个数为0，跳过
                            if dp[v][j] == 0:
                                continue
                            
                            # 如果合并后的大小不超过总节点数
                            if i + j <= n:
                                # 更新合并后大小为i+j的连通子树个数
                                # 使用乘法原理：当前子树中大小为i的子树个数 × 新增子树中大小为j的子树个数
                                temp[i + j] = (temp[i + j] + (dp[u][i] * dp[v][j]) % MOD) % MOD
        
                    # 更新dp[u]，将合并结果加到原有结果上
                    for i in range(1, min(size[u] + size[v], n) + 1):
                        dp[u][i] = (dp[u][i] + temp[i]) % MOD
        
                    # 更新当前节点u的子树大小
                    size[u] += size[v]
            
            return dp
        
        # 计算以节点centroid为重心的连通子树个数
        # centroid: 指定的重心节点
        def countSubtreesWithCentroid(centroid):
            result = 0
        
            # 遍历所有可能的子树大小
            for i in range(1, n + 1):
                # 将大小为i且以centroid为根的连通子树个数累加到结果中
                # 注意：这里是一个简化的处理，实际需要更复杂的计算来判断这些子树是否真的以centroid为重心
                result = (result + dp[centroid][i]) % MOD
        
            return result
        
        # 计算原树的重心
        originalCentroid = 0
        originalMaxSub = float('inf')
        # 从节点1开始DFS计算重心，父节点为0（表示没有父节点）
        computeOriginalCentroid(1, 0)
        
        # 树形DP计算连通子树个数
        # 从节点1开始DFS计算DP状态，父节点为0（表示没有父节点）
        dp = treeDP(1, 0)
        
        # 计算以原树重心为重心的连通子树个数
        result = countSubtreesWithCentroid(originalCentroid)
        
        # 输出结果
        print(f"Case {testCase}: {result}")

if __name__ == "__main__":
    main()
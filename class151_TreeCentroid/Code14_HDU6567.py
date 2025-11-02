# HDU 6567 Cotree
# 给定两棵树，然后加上一条边使得成为一棵树，并且新树上的所有的任意两点的距离最小
# 利用树的重心的性质：树中所有点到某个点的距离和中，到重心的距离和是最小的
# 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=6567
# 时间复杂度：O(n)
# 空间复杂度：O(n)

import sys
from collections import defaultdict, deque

# 读取输入优化
input = sys.stdin.read
sys.setrecursionlimit(1000000)

def main():
    data = input().split()
    idx = 0
    
    while idx < len(data):
        n = int(data[idx])
        idx += 1
        
        # 邻接表存储树
        adj = defaultdict(list)
        
        # 读取边信息并构建树
        for _ in range(n - 2):
            u = int(data[idx])
            idx += 1
            v = int(data[idx])
            idx += 1
            adj[u].append(v)
            adj[v].append(u)
        
        # 标记节点属于哪棵树
        tree_id = [0] * (n + 1)
        
        # BFS分离两棵树
        def separate_trees():
            tree_count = 0
            for i in range(1, n + 1):
                if tree_id[i] == 0:
                    tree_count += 1
                    queue = deque([i])
                    tree_id[i] = tree_count
                    
                    while queue:
                        u = queue.popleft()
                        for v in adj[u]:
                            if tree_id[v] == 0:
                                tree_id[v] = tree_count
                                queue.append(v)
        
        # 分离两棵树
        separate_trees()
        
        # 计算子树大小
        size = [0] * (n + 1)
        
        # 距离和
        dist_sum = [0] * (n + 1)
        
        # 第一次DFS计算子树大小
        def dfs1(u, father, visited):
            size[u] = 1
            for v in adj[u]:
                if v != father and visited[v]:
                    dfs1(v, u, visited)
                    size[u] += size[v]
        
        # 计算以centroid为根的树的距离和
        def calculate_tree_distance_sum(centroid, visited):
            def dfs2(u, father):
                size[u] = 1
                dist_sum[u] = 0
                for v in adj[u]:
                    if v != father and visited[v]:
                        dfs2(v, u)
                        size[u] += size[v]
                        dist_sum[u] += dist_sum[v] + size[v]
            
            # 初始化
            for i in range(n + 1):
                size[i] = 0
                dist_sum[i] = 0
            
            dfs2(centroid, 0)
            return dist_sum[centroid]
        
        # 计算子树大小
        def get_size(centroid, visited):
            def dfs3(u, father):
                size[u] = 1
                for v in adj[u]:
                    if v != father and visited[v]:
                        dfs3(v, u)
                        size[u] += size[v]
            
            # 初始化
            for i in range(n + 1):
                size[i] = 0
            
            dfs3(centroid, 0)
            return size[centroid]
        
        # 找到连通分量的节点数
        def get_node_count(start_node):
            visited = [False] * (n + 1)
            queue = deque([start_node])
            visited[start_node] = True
            node_count = 1
            
            while queue:
                u = queue.popleft()
                for v in adj[u]:
                    if not visited[v]:
                        visited[v] = True
                        queue.append(v)
                        node_count += 1
            
            return visited, node_count
        
        # 计算树的重心
        def find_centroid(start_node):
            visited, node_count = get_node_count(start_node)
            
            # 计算重心
            size = [0] * (n + 1)
            min_max_sub = [n]
            centroid = [0]
            
            # 第一次DFS计算子树大小
            dfs1(start_node, 0, visited)
            
            # 找到重心
            def find_centroid_helper(u, father):
                max_sub = 0
                for v in adj[u]:
                    if v != father and visited[v]:
                        find_centroid_helper(v, u)
                        max_sub = max(max_sub, size[v])
                max_sub = max(max_sub, node_count - size[u])
                
                if max_sub < min_max_sub[0]:
                    min_max_sub[0] = max_sub
                    centroid[0] = u
            
            find_centroid_helper(start_node, 0)
            return centroid[0], visited
        
        # 找到两棵树的重心
        centroid1 = 0
        centroid2 = 0
        visited1 = None
        visited2 = None
        
        for i in range(1, n + 1):
            if tree_id[i] == 1 and centroid1 == 0:
                centroid1, visited1 = find_centroid(i)
            if tree_id[i] == 2 and centroid2 == 0:
                centroid2, visited2 = find_centroid(i)
        
        # 计算两点间距离和
        def calculate_distance_sum():
            # 计算第一棵树的距离和
            sum1 = calculate_tree_distance_sum(centroid1, visited1)
            
            # 计算第二棵树的距离和
            sum2 = calculate_tree_distance_sum(centroid2, visited2)
            
            # 计算连接边带来的额外距离
            # 第一棵树的节点数
            size1 = get_size(centroid1, visited1)
            # 第二棵树的节点数
            size2 = get_size(centroid2, visited2)
            
            # 连接边带来的额外距离是size1 * size2
            extra = size1 * size2
            
            return sum1 + sum2 + extra
        
        # 计算最小距离和
        result = calculate_distance_sum()
        
        print(result)

if __name__ == "__main__":
    main()
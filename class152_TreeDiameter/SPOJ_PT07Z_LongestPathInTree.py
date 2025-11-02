# SPOJ PT07Z - Longest path in a tree
# 题目：给定一个无权无向树，求树中最长路径的长度。

# 算法标签：树、广度优先搜索、两次BFS
# 难度：简单
# 时间复杂度：O(n)，其中n是树中节点的数量
# 空间复杂度：O(n)，用于存储邻接表和辅助数组

# 相关题目：
# - LeetCode 543. 二叉树的直径
# - LeetCode 1245. Tree Diameter (无向树的直径)
# - LeetCode 1522. Diameter of N-Ary Tree (N叉树的直径)
# - CSES 1131 - Tree Diameter (树的直径)
# - 51Nod 2602 - 树的直径
# - 洛谷 U81904 树的直径
# - AtCoder ABC221F - Diameter Set

# 解题思路：
# 使用两次BFS法求解树的直径：
# 1. 从任意一点开始，找到距离它最远的点s
# 2. 从s开始，找到距离它最远的点t
# 3. s到t的距离即为树的直径

from collections import deque, defaultdict

# 全局变量
n = 0  # 节点数
graph = defaultdict(list)  # 邻接表存储树

def bfs(start):
    """
    BFS求从起点开始的最远节点
    
    算法思路：
    1. 从指定起点开始进行广度优先搜索
    2. 记录访问过的节点，避免重复访问
    3. 记录每一层的节点，直到遍历完所有节点
    4. 返回最后一层的节点（最远节点）和距离
    
    参数:
        start (int): 起点节点编号
        
    返回:
        tuple: (最远节点, 距离)
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    visited = [False] * (n + 1)
    queue = deque()
    
    visited[start] = True
    queue.append(start)
    
    last_node = start
    max_distance = 0
    
    while queue:
        size = len(queue)
        for _ in range(size):
            current = queue.popleft()
            last_node = current
            
            # 遍历当前节点的所有邻居
            for neighbor in graph[current]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    queue.append(neighbor)
        
        if queue:
            max_distance += 1
    
    return (last_node, max_distance)

def find_diameter():
    """
    使用两次BFS法求树的直径
    
    算法思路：
    1. 第一次BFS，从任意节点（如节点1）开始找到最远节点
    2. 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
    3. 第二次BFS的距离就是树的直径
    
    返回:
        int: 树的直径
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    # 第一次BFS，从节点1开始找到最远节点
    first_bfs_node, _ = bfs(1)
    
    # 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
    _, diameter = bfs(first_bfs_node)
    
    # 第二次BFS的距离就是树的直径
    return diameter

# 主方法（用于测试）
if __name__ == "__main__":
    # 由于这是SPOJ题目，实际提交时需要按照题目要求的输入格式处理
    # 这里我们只展示算法实现
    
    # 示例输入：
    # n = 4
    # 边: 1-2, 2-3, 3-4
    # 预期输出：3
    
    n = 4
    graph.clear()
    
    # 添加边
    graph[1].append(2)
    graph[2].append(1)
    graph[2].append(3)
    graph[3].append(2)
    graph[3].append(4)
    graph[4].append(3)
    
    print("树的直径:", find_diameter())  # 应该输出3
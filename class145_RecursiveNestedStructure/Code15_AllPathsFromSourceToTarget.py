# LeetCode 797. All Paths From Source to Target
# 所有可能的路径
# 题目来源：https://leetcode.cn/problems/all-paths-from-source-to-target/

"""
问题描述：
给你一个有 n 个节点的 有向无环图（DAG），请你找出所有从节点 0 到节点 n-1 的路径并输出（不要求按特定顺序）。
二维数组的第 i 个数组中的元素表示从节点 i 可以直接到达的所有节点，空就是没有可以直接到达的节点。

解题思路：
1. 由于图是有向无环的（DAG），我们可以使用深度优先搜索（DFS）来遍历所有可能的路径
2. 递归地探索每个节点的邻居，并记录路径
3. 当到达目标节点（n-1）时，将当前路径添加到结果中

时间复杂度：O(2^N * N)，其中N是节点数量，最坏情况下每个节点都可以选择是否加入路径，且路径长度最多为N
空间复杂度：O(N)，递归调用栈的深度最多为N，同时存储路径的空间也是O(N)
"""

class AllPathsSourceTarget:
    def allPathsSourceTarget(self, graph):
        """
        查找从源节点到目标节点的所有路径
        
        Args:
            graph: 图的邻接表表示，graph[i] 是节点i的所有邻居节点列表
            
        Returns:
            所有从节点0到节点n-1的路径列表
        """
        result = []
        current_path = [0]  # 起始节点是0
        
        # 从节点0开始深度优先搜索
        self._dfs(graph, 0, len(graph) - 1, current_path, result)
        
        return result
    
    def _dfs(self, graph, current, target, current_path, result):
        """
        深度优先搜索辅助方法
        
        Args:
            graph: 图的邻接表表示
            current: 当前节点
            target: 目标节点（n-1）
            current_path: 当前路径
            result: 结果列表，存储所有路径
        """
        # 基础情况：到达目标节点
        if current == target:
            # 将当前路径的副本添加到结果中
            result.append(current_path.copy())
            return
        
        # 遍历当前节点的所有邻居
        for neighbor in graph[current]:
            # 将邻居节点添加到当前路径
            current_path.append(neighbor)
            # 递归探索邻居节点
            self._dfs(graph, neighbor, target, current_path, result)
            # 回溯：移除最后添加的节点
            current_path.pop()
    
    def allPathsSourceTargetIterative(self, graph):
        """
        迭代版本的深度优先搜索实现
        
        Args:
            graph: 图的邻接表表示
            
        Returns:
            所有从节点0到节点n-1的路径列表
        """
        result = []
        target = len(graph) - 1
        
        # 使用栈来模拟递归调用
        # 栈中每个元素是一个元组：(当前节点, 当前路径)
        stack = [(0, [0])]
        
        # 迭代DFS
        while stack:
            current, path = stack.pop()
            
            # 如果到达目标节点，将路径添加到结果
            if current == target:
                result.append(path)
                continue
            
            # 将当前节点的所有邻居加入栈中
            # 注意：为了保持与递归版本相同的路径顺序，我们需要反转邻居列表
            # 因为栈是后进先出的结构
            for neighbor in reversed(graph[current]):
                new_path = path + [neighbor]
                stack.append((neighbor, new_path))
        
        return result

# 测试代码
if __name__ == "__main__":
    solution = AllPathsSourceTarget()
    
    # 测试用例1
    graph1 = [[1, 2], [3], [3], []]
    print("递归DFS结果:")
    result1 = solution.allPathsSourceTarget(graph1)
    for path in result1:
        print(path)
    
    print("\n迭代DFS结果:")
    result1_iterative = solution.allPathsSourceTargetIterative(graph1)
    for path in result1_iterative:
        print(path)
    
    # 测试用例2
    graph2 = [[4, 3, 1], [3, 2, 4], [3], [4], []]
    print("\n递归DFS结果:")
    result2 = solution.allPathsSourceTarget(graph2)
    for path in result2:
        print(path)
    
    print("\n迭代DFS结果:")
    result2_iterative = solution.allPathsSourceTargetIterative(graph2)
    for path in result2_iterative:
        print(path)
    
    """
    性能分析：
    - 时间复杂度：O(2^N * N)，其中N是节点数量
      最坏情况下，每个节点都可以选择是否加入路径，且路径长度最多为N
      例如，在完全二叉树形状的DAG中，路径数量可能达到2^(N-1)级别
      每个路径需要O(N)时间来复制
    
    - 空间复杂度：O(N)，递归调用栈的深度最多为N，同时存储路径的空间也是O(N)
      注意：最终结果占用的空间不计入算法的空间复杂度分析
    
    工程化考量：
    1. 异常处理：在实际应用中，应该检查输入图是否为None，节点数量是否合法
    2. 对于大型图，可以考虑使用更高效的数据结构来存储路径，避免频繁复制
    3. 可以添加并行处理来加速搜索，但需要注意线程安全问题
    4. 当图中存在环时，这个算法会陷入死循环，因此需要确保图是DAG或添加访问标记
    5. 在Python中，递归深度可能会受到默认递归栈深度的限制，可以使用sys.setrecursionlimit来调整
    """
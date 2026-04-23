"""
欧拉路径Python实现 - 基础算法集合
包含有向图和无向图的欧拉路径判定与构造算法
"""

# 从collections模块导入defaultdict和deque，用于高效的数据结构操作
# 面试要点：Python内置数据结构的使用场景和性能特点
# ML/DL关联：高效数据结构在模型训练中的重要性
from collections import defaultdict, deque


class EulerPathAlgorithms:
    """
    欧拉路径算法集合类
    包括有向图和无向图的欧拉路径判定和构造算法
    """

    def __init__(self):
        # 空的初始化方法，当前类不需要特殊的初始化操作
        # 面试要点：当类不需要特殊初始化时，保持__init__方法简洁
        # ML/DL关联：轻量级初始化在模型部署中的优势
        pass

    # ==================== 无向图欧拉路径 ====================

    def undirected_euler_exists(self, graph):
        """
        判断无向图是否存在欧拉路径或欧拉回路

        Args:
            graph: 邻接表表示的无向图 {node: [neighbors]}

        Returns:
            tuple: (存在性, 起点, 路径类型)
                   路径类型: 0-无, 1-欧拉路径, 2-欧拉回路
        """
        if not graph:
            return True, None, 2  # 空图认为有欧拉回路

        # 计算每个节点的度数
        # 创建空字典存储每个节点的度数
        # 面试要点：字典数据结构的使用和性能特点
        # ML/DL关联：哈希表在特征映射中的应用
        degree = {}
        # 遍历图中的每个节点
        # 面试要点：字典遍历的性能和使用场景
        # ML/DL关联：图遍历在节点特征聚合中的应用
        for node in graph:
            # 计算当前节点的度数（邻居数量）并存储
            # 面试要点：邻接表中度数的计算方法
            # ML/DL关联：节点度数作为图的重要特征
            degree[node] = len(graph[node])

        # 统计奇度节点数量
        odd_degree_nodes = [node for node,
                            deg in degree.items() if deg % 2 == 1]

        # 欧拉路径判定定理
        if len(odd_degree_nodes) == 0:
            # 所有节点度数为偶数 -> 存在欧拉回路
            return True, next(iter(graph)), 2
        elif len(odd_degree_nodes) == 2:
            # 恰好有两个奇度节点 -> 存在欧拉路径，从任一奇度节点开始
            return True, odd_degree_nodes[0], 1
        else:
            # 其他情况 -> 不存在欧拉路径
            return False, None, 0

    def hierholzer_undirected_recursive(self, graph, start_node):
        """
        Hierholzer算法 - 无向图递归版

        Args:
            graph: 邻接表表示的无向图
            start_node: 起始节点

        Returns:
            list: 欧拉路径或欧拉回路的节点序列
        """
        # 创建图的副本，避免修改原图
        temp_graph = {node: graph[node][:] for node in graph}

        def dfs(node, path):
            # 遍历当前节点的所有邻接边
            while temp_graph[node]:
                neighbor = temp_graph[node].pop()
                # 从邻接节点中移除对应的边（无向图的双向边）
                temp_graph[neighbor].remove(node)
                dfs(neighbor, path)
            path.append(node)

        path = []
        dfs(start_node, path)
        return path[::-1]  # 反转路径得到正确顺序

    def hierholzer_undirected_iterative(self, graph, start_node):
        """
        Hierholzer算法 - 无向图迭代版

        Args:
            graph: 邻接表表示的无向图
            start_node: 起始节点

        Returns:
            list: 欧拉路径或欧拉回路的节点序列
        """
        # 创建图的副本，避免修改原图
        temp_graph = {node: graph[node][:] for node in graph}

        stack = [start_node]
        path = []

        while stack:
            current = stack[-1]

            if temp_graph[current]:  # 如果还有未访问的边
                neighbor = temp_graph[current].pop()
                # 从邻接节点中移除对应的边
                temp_graph[neighbor].remove(current)
                stack.append(neighbor)
            else:  # 没有未访问的边，回溯
                path.append(stack.pop())

        return path[::-1]  # 反转路径

    # ==================== 有向图欧拉路径 ====================

    def directed_euler_exists(self, graph):
        """
        判断有向图是否存在欧拉路径或欧拉回路

        Args:
            graph: 邻接表表示的有向图 {node: [outgoing_neighbors]}

        Returns:
            tuple: (存在性, 起点, 路径类型)
                   路径类型: 0-无, 1-欧拉路径, 2-欧拉回路
        """
        if not graph:
            return True, None, 2

        # 计算每个节点的入度和出度
        in_degree = defaultdict(int)
        out_degree = defaultdict(int)

        # 初始化所有节点的度数
        for node in graph:
            out_degree[node] = len(graph[node])

        for node in graph:
            for neighbor in graph[node]:
                in_degree[neighbor] += 1
                if neighbor not in out_degree:  # 避免遗漏孤立节点
                    out_degree[neighbor] = 0
                    in_degree[neighbor] = 0

        # 检查欧拉路径存在的条件
        start_count = 0  # 出度比入度多1的节点数
        end_count = 0    # 入度比出度多1的节点数
        start_node = None
        end_node = None

        for node in set(list(in_degree.keys()) + list(out_degree.keys())):
            diff = out_degree[node] - in_degree[node]

            if diff == 1:  # 出度比入度多1 -> 起点
                start_count += 1
                start_node = node
            elif diff == -1:  # 入度比出度多1 -> 终点
                end_count += 1
                end_node = node
            elif diff != 0:  # 差值不是0, 1, -1 -> 不存在欧拉路径
                return False, None, 0

        # 检查起点和终点的数量
        if start_count == 0 and end_count == 0:
            # 所有节点入度等于出度 -> 欧拉回路
            # 从任意有出边的节点开始
            for node in out_degree:
                if out_degree[node] > 0:
                    return True, node, 2
            return True, None, 2  # 只有孤立节点
        elif start_count == 1 and end_count == 1:
            # 一个起点一个终点 -> 欧拉路径
            return True, start_node, 1
        else:
            # 其他情况 -> 不存在欧拉路径
            return False, None, 0

    def hierholzer_directed_recursive(self, graph, start_node):
        """
        Hierholzer算法 - 有向图递归版

        Args:
            graph: 邻接表表示的有向图
            start_node: 起始节点

        Returns:
            list: 欧拉路径或欧拉回路的节点序列
        """
        # 创建图的副本，避免修改原图
        temp_graph = {node: graph[node][:] for node in graph}

        def dfs(node, path):
            # 遍历当前节点的所有出边
            while temp_graph[node]:
                neighbor = temp_graph[node].pop()
                dfs(neighbor, path)
            path.append(node)

        path = []
        dfs(start_node, path)
        return path[::-1]  # 反视路径得到正确顺序

    def hierholzer_directed_iterative(self, graph, start_node):
        """
        Hierholzer算法 - 有向图迭代版

        Args:
            graph: 邻接表表示的有向图
            start_node: 起始节点

        Returns:
            list: 欧拉路径或欧拉回路的节点序列
        """
        # 创建图的副本，避免修改原图
        temp_graph = {node: graph[node][:] for node in graph}

        stack = [start_node]
        path = []

        while stack:
            current = stack[-1]

            if temp_graph[current]:  # 如果还有未访问的出边
                neighbor = temp_graph[current].pop()
                stack.append(neighbor)
            else:  # 没有未访问的出边，回溯
                path.append(stack.pop())

        return path[::-1]  # 反转路径

    # ==================== 德布鲁因序列（De Bruijn Sequence）====================

    def de_bruijn_sequence(self, n, k):
        """
        构造德布鲁因序列，使用欧拉路径思想

        Args:
            n: 序列中子串的长度
            k: 字符集大小

        Returns:
            str: 德布鲁因序列
        """
        # 构造德布鲁因图：节点是长度为n-1的字符串
        a = [0] * k * n
        sequence = []

        def db(t, p):
            if t > n:
                if n % p == 0:
                    for j in range(1, p + 1):
                        sequence.append(a[j])
            else:
                a[t] = a[t - p]
                db(t + 1, p)
                for j in range(a[t - p] + 1, k):
                    a[t] = j
                    db(t + 1, t)

        db(1, 1)

        # 将数字序列转换为字符串
        result = ""
        for i in range(len(sequence)):
            result += str(sequence[i])

        # 德布鲁因序列长度应该是k^n，我们需要在前面补n-1个0
        prefix = "0" * (n - 1)
        return prefix + result


def demo_usage():
    """演示各种欧拉路径算法的使用方法"""

    print("=== 欧拉路径算法演示 ===\n")

    algo = EulerPathAlgorithms()

    # 无向图示例
    print("1. 无向图欧拉路径示例:")
    undirected_graph = {
        1: [2, 3],
        2: [1, 3, 4],
        3: [1, 2, 4],
        4: [2, 3]
    }
    print(f"图结构: {undirected_graph}")

    exists, start, path_type = algo.undirected_euler_exists(undirected_graph)
    print(
        f"存在欧拉路径: {exists}, 起点: {start}, 类型: {'欧拉回路' if path_type == 2 else '欧拉路径' if path_type == 1 else '无'}")

    if exists:
        path = algo.hierholzer_undirected_iterative(undirected_graph, start)
        print(f"欧拉路径: {path}\n")

    # 有向图示例
    print("2. 有向图欧拉路径示例:")
    directed_graph = {
        1: [2],
        2: [3],
        3: [1, 4],
        4: [2]
    }
    print(f"图结构: {directed_graph}")

    exists, start, path_type = algo.directed_euler_exists(directed_graph)
    print(
        f"存在欧拉路径: {exists}, 起点: {start}, 类型: {'欧拉回路' if path_type == 2 else '欧拉路径' if path_type == 1 else '无'}")

    if exists:
        path = algo.hierholzer_directed_iterative(directed_graph, start)
        print(f"欧拉路径: {path}\n")

    # 德布鲁因序列示例
    print("3. 德布鲁因序列示例 (n=3, k=2):")
    dbs = algo.de_bruijn_sequence(3, 2)
    print(f"德布鲁因序列: {dbs}")


if __name__ == "__main__":
    demo_usage()

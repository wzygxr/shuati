"""
欧拉路径算法完整实现 - 面试准备专用
包含有向图和无向图的欧拉路径判定与构造，以及完整的面试知识点讲解
"""

from collections import defaultdict, deque
import sys


class EulerPathSolver:
    """
    欧拉路径求解器类
    包含有向图和无向图的欧拉路径判定与构造算法
    面试重点：掌握欧拉路径的判定定理和Hierholzer算法
    """

    def __init__(self):
        """
        初始化求解器
        面试需说明：初始化不需要参数，因为图会在方法中传入
        """
        self.graph = None  # 图结构
        self.is_directed = None  # 是否为有向图标识

    def directed_euler_path_check(self, edges, n=None):
        """
        检查有向图是否存在欧拉路径
        参数:
        - edges: 边列表，每个元素为(u, v)表示从u到v的有向边
        - n: 节点数（可选，如果不提供则自动计算）

        返回:
        - (bool, str/int): (是否存在欧拉路径, 起点或错误信息)

        面试考点：有向图欧拉路径判定定理
        """
        if not edges:
            return True, 1  # 空图认为有欧拉路径

        # 统计入度和出度 - 欧拉路径判定的核心步骤，面试必考
        in_degree = defaultdict(int)  # 入度字典，面试需说明使用defaultdict的优势
        out_degree = defaultdict(int)  # 出度字典

        # 构建邻接表 - 图的常用存储方式，面试需掌握
        adj = defaultdict(list)  # 邻接表，空间复杂度O(V+E)

        all_nodes = set()  # 所有节点集合，用于后续处理

        # 遍历所有边，统计度数并构建图
        for u, v in edges:  # 时间复杂度O(E)，面试需说明
            adj[u].append(v)  # 添加边到邻接表
            out_degree[u] += 1  # 增加起点出度
            in_degree[v] += 1  # 增加终点入度
            all_nodes.add(u)  # 添加节点到集合
            all_nodes.add(v)  # 添加节点到集合

        # 如果n未提供，自动计算 - 笔试需注意边界条件处理
        if n is None:
            n = max(all_nodes) if all_nodes else 1

        # 检查欧拉路径的度数条件 - 有向图欧拉路径判定定理，面试高频考点
        start_nodes = 0  # 出度比入度多1的节点数（起点候选）
        end_nodes = 0    # 入度比出度多1的节点数（终点候选）
        start_node = -1  # 记录起点

        for node in all_nodes:  # 遍历所有节点检查度数
            diff = out_degree[node] - in_degree[node]  # 计算度数差

            if diff == 1:  # 出度比入度多1，可能是起点
                start_nodes += 1  # 增加起点候选数
                start_node = node  # 记录该节点作为起点候选
            elif diff == -1:  # 入度比出度多1，可能是终点
                end_nodes += 1  # 增加终点候选数
            elif diff != 0:  # 度数差不为-1, 0, 1，违反欧拉路径条件
                return False, f"节点{node}度数差为{diff}，不满足欧拉路径条件"  # 不满足条件

        # 验证度数条件 - 欧拉路径判定定理的核心应用
        if start_nodes == 0 and end_nodes == 0:  # 欧拉回路情况
            # 所有节点度数平衡，任选一个有出度的节点作为起点
            for node in all_nodes:  # 寻找起始节点
                if out_degree[node] > 0:  # 如果有出度
                    return True, node  # 返回该节点作为起点
            # 如果所有节点度数都为0，返回任意节点
            return True, 1 if n >= 1 else -1
        elif start_nodes == 1 and end_nodes == 1:  # 欧拉路径情况
            # 恰好有一个起点和一个终点，满足欧拉路径条件
            return True, start_node  # 返回找到的起点
        else:  # 不满足欧拉路径度数条件
            return False, f"度数条件不满足：起点候选{start_nodes}个，终点候选{end_nodes}个"

    def undirected_euler_path_check(self, edges, n=None):
        """
        检查无向图是否存在欧拉路径
        参数:
        - edges: 边列表，每个元素为(u, v)表示u和v之间的无向边
        - n: 节点数（可选，如果不提供则自动计算）

        返回:
        - (bool, str/int): (是否存在欧拉路径, 起点或错误信息)

        面试考点：无向图欧拉路径判定定理
        """
        if not edges:
            return True, 1  # 空图认为有欧拉路径

        # 统计度数 - 无向图欧拉路径判定的核心
        degree = defaultdict(int)  # 度数字典

        # 构建邻接表
        adj = defaultdict(list)

        all_nodes = set()

        # 遍历所有边，统计度数并构建图
        for u, v in edges:
            adj[u].append(v)  # 无向边添加双向连接
            adj[v].append(u)
            degree[u] += 1  # 两端点度数都增加
            degree[v] += 1
            all_nodes.add(u)
            all_nodes.add(v)

        if n is None:
            n = max(all_nodes) if all_nodes else 1

        # 统计奇度数节点数量 - 无向图欧拉路径判定定理，面试必考
        odd_degree_count = 0  # 奇度数节点计数
        start_node = -1  # 起点

        for node in all_nodes:  # 遍历所有节点
            if degree[node] % 2 == 1:  # 如果度数为奇数
                odd_degree_count += 1  # 增加奇度数节点计数
                start_node = node  # 记录奇度数节点作为起点候选

        # 检查奇度数节点数量 - 无向图欧拉路径判定定理的核心
        if odd_degree_count == 0:  # 所有节点度数都为偶数，存在欧拉回路
            # 任选一个度数大于0的节点作为起点
            for node in all_nodes:
                if degree[node] > 0:
                    return True, node
            # 如果所有节点度数都为0，返回任意节点
            return True, 1 if n >= 1 else -1
        elif odd_degree_count == 2:  # 恰好有两个奇度数节点，存在欧拉路径
            return True, start_node  # 返回其中一个奇度数节点作为起点
        else:  # 奇度数节点数不为0也不为2，不存在欧拉路径
            return False, f"奇度数节点数为{odd_degree_count}，不满足欧拉路径条件（必须为0或2）"

    def hierholzer_directed(self, start_node, edges):
        """
        使用Hierholzer算法构造有向图的欧拉路径
        参数:
        - start_node: 起点
        - edges: 边列表

        返回:
        - list: 欧拉路径的节点序列

        面试重点：Hierholzer算法的实现与复杂度分析
        """
        # 构建邻接表并统计每条边的使用次数
        adj = defaultdict(list)
        edge_count = defaultdict(lambda: defaultdict(int))

        for u, v in edges:
            adj[u].append(v)
            edge_count[u][v] += 1  # 统计边的使用次数（处理重边）

        # 使用栈实现Hierholzer算法（迭代版）- 避免递归深度过大导致栈溢出
        stack = [start_node]  # 用栈模拟递归过程，面试高频考点
        path = []  # 存储最终路径

        while stack:  # 当栈不为空时继续
            current = stack[-1]  # 查看栈顶节点

            # 检查当前节点是否有未访问的边
            has_unvisited_edge = False  # 标记是否有未访问的边

            # 遍历当前节点的所有邻居
            neighbors = list(adj[current])  # 创建副本以安全修改
            for next_node in neighbors:  # 遍历所有邻居
                if edge_count[current][next_node] > 0:  # 如果边未被访问
                    edge_count[current][next_node] -= 1  # 标记边为已访问
                    stack.append(next_node)  # 将下一个节点压入栈
                    has_unvisited_edge = True  # 标记有未访问边
                    break  # 找到一条未访问边就跳出

            if not has_unvisited_edge:  # 如果当前节点没有未访问的边
                path.append(stack.pop())  # 将当前节点加入路径并弹出栈

        path.reverse()  # 反转路径得到正确顺序 - 算法关键步骤
        return path  # 返回欧拉路径

    def hierholzer_undirected(self, start_node, edges):
        """
        使用Hierholzer算法构造无向图的欧拉路径
        参数:
        - start_node: 起点
        - edges: 边列表

        返回:
        - list: 欧拉路径的节点序列
        """
        # 构建邻接表并统计每条边的使用次数
        adj = defaultdict(list)
        edge_count = defaultdict(lambda: defaultdict(int))

        for u, v in edges:
            adj[u].append(v)
            adj[v].append(u)
            edge_count[u][v] += 1
            edge_count[v][u] += 1  # 无向边双向计数

        # 使用栈实现Hierholzer算法
        stack = [start_node]
        path = []

        while stack:
            current = stack[-1]

            has_unvisited_edge = False

            # 遍历当前节点的所有邻居
            neighbors = list(adj[current])
            for next_node in neighbors:
                if edge_count[current][next_node] > 0:
                    edge_count[current][next_node] -= 1
                    edge_count[next_node][current] -= 1  # 无向边双向减1
                    stack.append(next_node)
                    has_unvisited_edge = True
                    break

            if not has_unvisited_edge:
                path.append(stack.pop())

        path.reverse()
        return path

    def solve_directed_euler_path(self, edges, n=None):
        """
        完整解决有向图欧拉路径问题
        参数:
        - edges: 边列表
        - n: 节点数

        返回:
        - (bool, list/str): (是否成功, 欧拉路径或错误信息)

        面试标准流程：判定 -> 构造
        """
        # 首先检查是否存在欧拉路径
        exists, result = self.directed_euler_path_check(edges, n)  # 欧拉路径判定

        if not exists:  # 如果不存在
            return False, result  # 返回错误信息

        start_node = result  # 获取起点

        # 使用Hierholzer算法构造路径
        path = self.hierholzer_directed(start_node, edges)  # 构造欧拉路径

        return True, path  # 返回成功和路径

    def solve_undirected_euler_path(self, edges, n=None):
        """
        完整解决无向图欧拉路径问题
        参数:
        - edges: 边列表
        - n: 节点数

        返回:
        - (bool, list/str): (是否成功, 欧拉路径或错误信息)
        """
        # 首先检查是否存在欧拉路径
        exists, result = self.undirected_euler_path_check(edges, n)

        if not exists:
            return False, result

        start_node = result

        # 使用Hierholzer算法构造路径
        path = self.hierholzer_undirected(start_node, edges)

        return True, path


def main():
    """
    主函数：演示欧拉路径算法的使用
    面试中常要求现场编写此类演示代码
    """
    solver = EulerPathSolver()  # 创建求解器实例

    print("=== 欧拉路径算法面试准备 ===")
    print()

    # 示例1：有向图欧拉路径
    print("示例1：有向图欧拉路径")
    directed_edges = [(1, 2), (2, 3), (3, 1), (1, 4), (4, 3)]  # 有向边列表
    print(f"输入边: {directed_edges}")

    success, result = solver.solve_directed_euler_path(directed_edges)  # 求解

    if success:  # 如果求解成功
        print(f"存在欧拉路径: {' -> '.join(map(str, result))}")  # 输出路径
    else:  # 如果求解失败
        print(f"不存在欧拉路径: {result}")  # 输出错误原因
    print()

    # 示例2：无向图欧拉路径
    print("示例2：无向图欧拉路径")
    undirected_edges = [(1, 2), (2, 3), (3, 4), (4, 1), (1, 3)]  # 无向边列表
    print(f"输入边: {undirected_edges}")

    success, result = solver.solve_undirected_euler_path(
        undirected_edges)  # 求解

    if success:
        print(f"存在欧拉路径: {' - '.join(map(str, result))}")  # 输出路径
    else:
        print(f"不存在欧拉路径: {result}")
    print()

    # 示例3：面试常见问题 - 无效情况
    print("示例3：无效情况（度数不满足条件）")
    invalid_edges = [(1, 2), (2, 3), (3, 4), (4, 5), (5, 1), (1, 6)]  # 度数不满足条件
    print(f"输入边: {invalid_edges}")

    success, result = solver.solve_directed_euler_path(invalid_edges)

    if success:
        print(f"存在欧拉路径: {' -> '.join(map(str, result))}")
    else:
        print(f"不存在欧拉路径: {result}")
    print()

    print("=== 面试知识点总结 ===")
    print("1. 欧拉路径判定定理：")
    print("   - 有向图：最多一个起点(出度-入度=1)，最多一个终点(入度-出度=1)，其余度数平衡")
    print("   - 无向图：奇度数节点数为0(回路)或2(路径)")
    print("2. Hierholzer算法：O(E)时间复杂度，使用栈避免递归")
    print("3. 图的表示：邻接表O(V+E)空间，适合稀疏图")
    print("4. 边界处理：空图、孤立点、重边、自环")


if __name__ == "__main__":
    main()  # 执行主函数

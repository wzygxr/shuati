"""
欧拉路径Python实现 - 高级应用
包括词链问题、合法排列数对、密码破解等应用实例

面试要点：
- 词链问题：将字符串转换为图论问题的经典案例
- 合法排列数对：有向图欧拉路径的实际应用
- 密码破解：德布鲁因序列的构造与欧拉回路
- 字母对问题：无向图欧拉路径的实现

ML/DL关联：
- 序列到序列模型中的路径生成
- 图神经网络中的遍历策略
- 自然语言处理中的文本链式连接
"""

# 从collections模块导入defaultdict和deque，用于高效的数据结构操作
# 面试要点：Python内置数据结构的使用场景和性能特点
# ML/DL关联：高效数据结构在模型训练中的重要性
from collections import defaultdict, deque
# 导入heapq模块，提供堆队列算法实现
# 面试要点：堆数据结构及其在算法中的应用
# ML/DL关联：优先队列在注意力机制中的应用
import heapq


class EulerPathAdvancedApplications:
    """
    欧拉路径高级应用类
    包括词链、合法排列数对、密码破解等实际应用场景

    面试要点：
    - 各种实际问题如何转化为欧拉路径问题
    - 不同应用场景的图构建策略
    - 算法的时间和空间复杂度分析

    ML/DL关联：
    - 图嵌入中的路径表示学习
    - 序列生成模型的约束优化
    """

    def __init__(self):
        """
        初始化欧拉路径高级应用类

        面试要点：
        - 类设计模式的运用
        - 成员变量的初始化策略

        ML/DL关联：
        - 模型初始化参数的设置
        """
        # 空的初始化方法，当前类不需要特殊的初始化操作
        # 面试要点：当类不需要特殊初始化时，保持__init__方法简洁
        # ML/DL关联：轻量级初始化在模型部署中的优势
        pass

    # ==================== 词链问题 (Word Chain) ====================

    def word_chain(self, words):
        """
        词链问题：将单词连接成链，前一个单词的结尾字符等于后一个单词的开头字符

        Args:
            words: 单词列表

        Returns:
            str: 词链字符串，如果无法构造返回"***"

        面试要点：
        - 如何将字符串问题转化为图论问题
        - 字符作为节点，单词作为边的建模方法
        - 字典序最小的处理策略
        - 时间复杂度：O(W * log(W))，其中W是单词数量

        边界条件：
        - 空输入列表
        - 单个单词
        - 无法构成词链的情况

        ML/DL关联：
        - 文本生成中的连贯性约束
        - 序列到序列模型中的转移概率
        - 自然语言处理中的词汇链构建
        """
        if not words:
            # 边界条件：空输入，返回空字符串
            return ""

        # 构建图：以字符为节点，单词为边
        # 面试要点：图的构建策略和数据结构选择
        # 使用defaultdict自动创建空列表，提高代码效率
        # 面试要点：defaultdict的使用及其与普通dict的性能差异
        # ML/DL关联：自动初始化在张量操作中的应用
        graph = defaultdict(list)
        # 创建边映射，记录每条边对应的具体单词信息
        # 面试要点：多层数据结构的设计与访问
        # ML/DL关联：嵌套字典在特征映射中的应用
        edge_map = defaultdict(list)  # 记录边的具体信息

        # 按首字符分组，便于排序
        # 面试要点：数据预处理的重要性
        # 遍历所有单词，构建图的边关系
        # 面试要点：循环结构的性能考虑
        # ML/DL关联：批处理中的数据遍历策略
        for word in words:
            start_char = word[0]  # 单词的首字符
            # 获取单词的第一个字符作为边的起点
            # 面试要点：字符串索引操作的时间复杂度O(1)
            # ML/DL关联：字符级处理在NLP中的应用
            end_char = word[-1]   # 单词的末字符
            # 获取单词的最后一个字符作为边的终点
            # 面试要点：负索引的使用和效率
            # ML/DL关联：序列末尾元素的快速访问
            graph[start_char].append(end_char)  # 添加有向边
            # 在图中添加从首字符到末字符的有向边
            # 面试要点：邻接表的边添加操作
            # ML/DL关联：图中节点关系的表示
            edge_map[(start_char, end_char)].append(word)  # 记录具体的单词
            # 在边映射中记录这条边对应的具体单词
            # 面试要点：元组作为字典键的使用
            # ML/DL关联：复合键在特征映射中的应用

        # 对每个字符的出边按字典序排序
        # 面试要点：确保输出结果字典序最小的关键步骤
        # 遍历图中的每个字符节点
        # 面试要点：字典遍历的性能和使用场景
        # ML/DL关联：图遍历在节点特征聚合中的应用
        for char in graph:
            # 对当前字符的所有出边进行排序
            # 面试要点：排序算法的选择和性能分析
            # ML/DL关联：特征排序在模型输入中的重要性
            graph[char].sort()

        # 检查欧拉路径存在性
        # 面试要点：欧拉路径判定定理的应用
        in_degree = defaultdict(int)  # 入度统计
        out_degree = defaultdict(int)  # 出度统计

        all_chars = set()
        for word in words:
            start_char = word[0]  # 单词起点字符
            end_char = word[-1]   # 单词终点字符
            out_degree[start_char] += 1  # 增加起点的出度
            in_degree[end_char] += 1     # 增加终点的入度
            all_chars.add(start_char)    # 收集所有涉及的字符
            all_chars.add(end_char)

        # 检查欧拉路径条件
        # 面试要点：度数条件的验证逻辑
        start_count = 0  # 出度比入度多1的节点数
        end_count = 0    # 入度比出度多1的节点数
        start_char = None  # 起点字符

        for char in all_chars:
            diff = out_degree[char] - in_degree[char]  # 计算度数差
            if diff == 1:  # 出度比入度多1，可能是起点
                start_count += 1
                start_char = char
            elif diff == -1:  # 入度比出度多1，可能是终点
                end_count += 1
            elif diff != 0:  # 度数差不为-1, 0, 1，违反欧拉路径条件
                return "***"  # 不存在欧拉路径

        # 验证度数条件是否满足欧拉路径要求
        if not ((start_count == 0 and end_count == 0) or (start_count == 1 and end_count == 1)):
            return "***"

        # 确定起点
        # 面试要点：根据度数条件确定起点的逻辑
        if start_char is None:
            # 欧拉回路情况：从任意有出边的字符开始
            for char in all_chars:
                if out_degree[char] > 0:
                    start_char = char
                    break

        # Hierholzer算法构造路径
        # 面试要点：Hierholzer算法的实现和应用
        path = self._hierholzer_word_chain(
            graph, edge_map, start_char, words[:])

        # 验证路径长度是否正确
        if len(path) != len(words) + 1:
            return "***"

        # 构造结果字符串
        # 面试要点：路径到结果的转换方法
        result = path[0]
        for i in range(1, len(path)):
            result += "." + path[i]

        return result

    def _hierholzer_word_chain(self, graph, edge_map, start_char, words):
        """
        词链问题的Hierholzer算法实现

        Args:
            graph: 图的邻接表表示
            edge_map: 边的具体信息映射
            start_char: 起点字符
            words: 原始单词列表

        Returns:
            list: 欧拉路径的字符序列

        面试要点：
        - Hierholzer算法的递归和迭代实现
        - 边访问状态的跟踪
        - 避免重复使用同一条边的策略

        ML/DL关联：
        - 图遍历算法在GNN中的应用
        - 状态跟踪在序列建模中的重要性
        """
        # 复制图结构，避免修改原始数据
        temp_graph = defaultdict(list)
        temp_edge_map = {k: v[:] for k, v in edge_map.items()}

        for char in graph:
            temp_graph[char] = graph[char][:]

        # 记录使用的单词
        # 面试要点：状态跟踪机制，防止重复使用边
        used_words = set()

        def dfs(node, path):
            """
            深度优先搜索实现Hierholzer算法

            Args:
                node: 当前节点
                path: 路径结果列表

            面试要点：
            - DFS与Hierholzer算法的结合
            - 递归回溯时机的控制
            """
            # 遍历当前节点的所有出边
            while temp_graph[node]:
                next_char = temp_graph[node].pop(0)

                # 找到可用的单词
                available_words = temp_edge_map[(node, next_char)]
                word_idx = -1
                for i, word in enumerate(available_words):
                    if word not in used_words:
                        word_used = word
                        word_idx = i
                        break

                if word_idx != -1:
                    used_words.add(word_used)
                    temp_edge_map[(node, next_char)].pop(word_idx)
                    dfs(next_char, path)

            # 将当前节点加入路径（在递归回溯时）
            path.append(node)

        path = []
        dfs(start_char, path)
        return path[::-1]  # 反转路径得到正确顺序

    # ==================== 合法排列数对 (Valid Arrangement of Pairs) ====================

    def valid_arrangement_of_pairs(self, pairs):
        """
        合法排列数对问题：将数对排列成首尾相接的形式

        Args:
            pairs: 数对列表 [[a,b], [c,d], ...]

        Returns:
            list: 合法排列，如果不存在返回None

        面试要点：
        - 有向图欧拉路径的应用场景
        - 数对到有向边的映射方法
        - 欧拉路径存在性的验证

        ML/DL关联：
        - 关系抽取中的序列建模
        - 图结构数据的排列优化
        """
        if not pairs:
            # 边界条件：空输入，返回空列表
            return []

        # 构建图
        # 面试要点：图的表示方法选择
        graph = defaultdict(list)
        edge_indices = defaultdict(list)

        # 记录度数
        # 面试要点：度数统计在欧拉路径判定中的作用
        in_degree = defaultdict(int)
        out_degree = defaultdict(int)

        # 遍历所有数对，构建图和度数统计
        for i, (start, end) in enumerate(pairs):
            graph[start].append(end)  # 添加有向边
            edge_indices[(start, end)].append(i)  # 记录边的原始索引
            out_degree[start] += 1  # 增加起点出度
            in_degree[end] += 1     # 增加终点入度

        # 检查欧拉路径存在性
        # 面试要点：有向图欧拉路径判定定理的应用
        all_nodes = set(list(in_degree.keys()) + list(out_degree.keys()))

        start_count = 0  # 出度比入度多1的节点数
        end_count = 0    # 入度比出度多1的节点数
        start_node = None  # 起点节点

        # 验证度数条件
        for node in all_nodes:
            diff = out_degree[node] - in_degree[node]  # 计算度数差
            if diff == 1:  # 出度比入度多1，可能是起点
                start_count += 1
                start_node = node
            elif diff == -1:  # 入度比出度多1，可能是终点
                end_count += 1
            elif diff != 0:  # 度数差不为-1, 0, 1，违反欧拉路径条件
                return None  # 不存在欧拉路径

        # 验证度数条件是否满足欧拉路径要求
        if not ((start_count == 0 and end_count == 0) or (start_count == 1 and end_count == 1)):
            return None

        # 确定起点
        # 面试要点：根据度数条件确定起点的逻辑
        if start_node is None:
            # 欧拉回路情况：从任意有出边的节点开始
            for node in all_nodes:
                if out_degree[node] > 0:
                    start_node = node
                    break

        # 使用Hierholzer算法构造路径
        path = self._hierholzer_pairs(graph, edge_indices, start_node, pairs)

        # 验证路径长度是否正确
        if len(path) != len(pairs) + 1:
            return None

        # 构造结果
        # 面试要点：路径到原问题解的转换
        result = []
        for i in range(len(pairs)):
            result.append([path[i], path[i+1]])

        return result

    def _hierholzer_pairs(self, graph, edge_indices, start_node, pairs):
        """
        合法排列数对的Hierholzer算法实现

        Args:
            graph: 图的邻接表表示
            edge_indices: 边的原始索引映射
            start_node: 起点节点
            pairs: 原始数对列表

        Returns:
            list: 欧拉路径的节点序列

        面试要点：
        - Hierholzer算法的迭代实现
        - 边的唯一标识和访问控制
        - 避免重复使用边的策略

        ML/DL关联：
        - 图遍历中的状态管理
        - 序列生成中的约束满足
        """
        # 复制图结构，避免修改原始数据
        temp_graph = defaultdict(list)
        temp_edge_indices = {k: v[:] for k, v in edge_indices.items()}

        for node in graph:
            temp_graph[node] = graph[node][:]

        # 记录使用的边
        # 面试要点：边访问状态的跟踪机制
        used_edges = set()

        def dfs(node, path):
            """
            深度优先搜索实现Hierholzer算法

            Args:
                node: 当前节点
                path: 路径结果列表

            面试要点：
            - DFS与Hierholzer算法的结合
            - 递归回溯时机的控制
            """
            # 遍历当前节点的所有出边
            while temp_graph[node]:
                next_node = temp_graph[node].pop(0)

                # 找到可用的边
                available_indices = temp_edge_indices[(node, next_node)]
                edge_idx = -1
                for i, idx in enumerate(available_indices):
                    if idx not in used_edges:
                        edge_idx = idx
                        break

                if edge_idx != -1:
                    used_edges.add(edge_idx)
                    temp_edge_indices[(node, next_node)].pop(
                        temp_edge_indices[(node, next_node)].index(edge_idx)
                    )
                    dfs(next_node, path)

            # 将当前节点加入路径（在递归回溯时）
            path.append(node)

        path = []
        dfs(start_node, path)
        return path[::-1]  # 反转路径得到正确顺序

    # ==================== 密码破解问题 (Cracking the Safe) ====================

    def cracking_the_safe(self, n, k):
        """
        密码破解问题：构造最短字符串包含所有可能的密码组合

        Args:
            n: 密码位数
            k: 每位可能的数字个数 (0 to k-1)

        Returns:
            str: 最短密码字符串

        面试要点：
        - 德布鲁因序列的构造方法
        - 欧拉回路在序列生成中的应用
        - 图的节点和边的巧妙设计

        ML/DL关联：
        - 序列生成中的覆盖性要求
        - 密码学中的随机序列生成
        """
        if n == 1:
            # 特殊情况：单一位数密码，直接返回所有可能数字
            return ''.join(str(i) for i in range(k))

        # 构造德布鲁因图：节点是长度为n-1的字符串
        # 边是从prefix到suffix的转换
        # 面试要点：德布鲁因图的构建策略
        result = []
        total_nodes = k ** (n - 1)  # 总节点数
        edges_per_node = k  # 每个节点的出边数

        # 使用Hierholzer算法构造欧拉回路
        # 面试要点：欧拉回路与欧拉路径的区别
        visited = set()  # 已访问的边集合
        stack = [0]  # 从全0节点开始，面试要点：起始节点的选择

        while stack:
            node = stack[-1]

            # 尝试添加所有可能的边
            has_unvisited_edge = False
            for edge_digit in range(k):
                # 计算下一个节点：滑动窗口方式
                next_node = (node * k + edge_digit) % (k ** (n - 1))
                edge = (node, next_node, edge_digit)

                if edge not in visited:
                    visited.add(edge)
                    stack.append(next_node)
                    has_unvisited_edge = True
                    break

            if not has_unvisited_edge:
                result.append(stack.pop())

        # 构造结果字符串
        # 面试要点：欧拉回路路径到德布鲁因序列的转换
        result.reverse()
        ret = [0] * (n - 1)  # 初始n-1个0

        for i in range(1, len(result)):
            ret.append(result[i] % k)

        return ''.join(map(str, ret))

    # ==================== 字母对问题 (Letter Pairs) ====================

    def letter_pairs(self, pairs):
        """
        字母对问题：给定字母对，找出欧拉路径

        Args:
            pairs: 字母对列表 [['a','b'], ['b','c'], ...]

        Returns:
            str: 欧拉路径字符串，如果不存在返回"No Solution"

        面试要点：
        - 无向图欧拉路径的实现
        - 字符到数字的映射策略
        - 无向边的处理方法

        ML/DL关联：
        - 图中的对称关系处理
        - 字符串序列的图表示方法
        """
        if not pairs:
            # 边界条件：空输入，返回空字符串
            return ""

        # 将字母映射为数字以方便处理
        # 面试要点：字符编码和索引映射策略
        char_to_num = {}
        num_to_char = {}
        idx = 1

        # 建立字符到数字的映射
        for a, b in pairs:
            if a not in char_to_num:
                char_to_num[a] = idx
                num_to_char[idx] = a
                idx += 1
            if b not in char_to_num:
                char_to_num[b] = idx
                num_to_char[idx] = b
                idx += 1

        n = idx - 1  # 节点总数
        # 使用邻接矩阵表示无向图
        graph = [[0] * (n + 1) for _ in range(n + 1)]
        degree = [0] * (n + 1)  # 度数数组

        # 构建图和度数统计
        for a, b in pairs:
            u, v = char_to_num[a], char_to_num[b]
            graph[u][v] += 1  # 增加边的计数（无向图）
            graph[v][u] += 1
            degree[u] += 1    # 增加度数
            degree[v] += 1

        # 检查欧拉路径存在性
        # 面试要点：无向图欧拉路径判定定理的应用
        odd_count = 0  # 奇度数节点数
        start_node = -1  # 起点节点

        # 统计奇度数节点
        for i in range(1, n + 1):
            if degree[i] % 2 == 1:
                odd_count += 1
                start_node = i

        # 验证奇度数节点数量
        if odd_count != 0 and odd_count != 2:
            return "No Solution"

        # 确定起点
        if odd_count == 0:
            # 欧拉回路：从任意有度数的节点开始
            for i in range(1, n + 1):
                if degree[i] > 0:
                    start_node = i
                    break

        # 使用Hierholzer算法构造路径
        path = []
        stack = [start_node]

        while stack:
            u = stack[-1]
            flag = False

            # 查找未访问的边
            for v in range(1, n + 1):
                if graph[u][v] > 0:
                    graph[u][v] -= 1  # 减少边的计数
                    graph[v][u] -= 1
                    stack.append(v)
                    flag = True
                    break

            if not flag:
                path.append(stack.pop())

        # 验证路径长度
        if len(path) != len(pairs) + 1:
            return "No Solution"

        # 构造结果字符串
        # 面试要点：数字路径到字符路径的转换
        result = ""
        for node in path:
            result += num_to_char[node]

        return result


def demo_advanced_applications():
    """
    演示高级应用算法的使用方法

    面试要点：
    - 代码示例的组织和展示
    - 算法功能的验证方法
    - 边界条件的测试

    ML/DL关联：
    - 模型推理示例的展示
    - 不同输入情况下的模型行为
    """
    print("=== 欧拉路径高级应用演示 ===\n")

    app = EulerPathAdvancedApplications()

    # 词链问题
    print("1. 词链问题示例:")
    words = ["aloha", "arachnid", "dog", "gopher", "rat", "tiger"]
    result = app.word_chain(words)
    print(f"输入单词: {words}")
    print(f"词链结果: {result}\n")

    # 合法排列数对
    print("2. 合法排列数对示例:")
    pairs = [[5, 1], [4, 5], [11, 9], [9, 4]]
    result = app.valid_arrangement_of_pairs(pairs)
    print(f"输入数对: {pairs}")
    print(f"排列结果: {result}\n")

    # 密码破解
    print("3. 密码破解问题示例:")
    n, k = 2, 2
    result = app.cracking_the_safe(n, k)
    print(f"密码位数: {n}, 数字种类: {k}")
    print(f"密码字符串: {result}")
    print(f"包含的密码: {[result[i:i+n] for i in range(len(result)-n+1)]}\n")

    # 字母对问题
    print("4. 字母对问题示例:")
    letter_pairs = [['a', 'b'], ['b', 'c'], ['c', 'a']]
    result = app.letter_pairs(letter_pairs)
    print(f"输入字母对: {letter_pairs}")
    print(f"路径结果: {result}\n")


if __name__ == "__main__":
    """
    主程序入口

    面试要点：
    - Python程序的标准入口模式
    - 模块化设计的最佳实践

    ML/DL关联：
    - 模型推理的入口点设计
    - 训练和推理代码的分离
    """
    demo_advanced_applications()

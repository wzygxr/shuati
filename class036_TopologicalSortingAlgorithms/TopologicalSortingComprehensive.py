"""
拓扑排序综合题目集 - Python实现

本文件包含来自多个平台的拓扑排序题目Python实现：
- LeetCode
- Codeforces
- AtCoder
- HDU
- POJ
- UVA
- SPOJ

每个题目都包含详细的注释、时间空间复杂度分析、测试用例和工程化考量。
"""

import heapq
from collections import deque, defaultdict
from typing import List, Set, Dict, Tuple, Optional
import sys

class TopologicalSortingComprehensive:
    """
    拓扑排序综合题目集主类
    """
    
    def __init__(self):
        pass
    
    # =====================================================================
    # LeetCode 207. Course Schedule - Python实现
    # 题目链接: https://leetcode.com/problems/course-schedule/
    # 
    # 时间复杂度：O(V + E)
    # 空间复杂度：O(V + E)
    
    def can_finish(self, numCourses: int, prerequisites: List[List[int]]) -> bool:
        """
        判断是否可以完成所有课程
        
        Args:
            numCourses: 课程总数
            prerequisites: 先修课程关系列表
            
        Returns:
            bool: 是否可以完成所有课程
        """
        # 构建邻接表
        graph = [[] for _ in range(numCourses)]
        in_degree = [0] * numCourses
        
        # 构建图和入度数组
        for course, pre_course in prerequisites:
            graph[pre_course].append(course)
            in_degree[course] += 1
        
        # Kahn算法拓扑排序
        queue = deque()
        for i in range(numCourses):
            if in_degree[i] == 0:
                queue.append(i)
        
        processed = 0
        while queue:
            current = queue.popleft()
            processed += 1
            
            for next_course in graph[current]:
                in_degree[next_course] -= 1
                if in_degree[next_course] == 0:
                    queue.append(next_course)
        
        return processed == numCourses
    
    # =====================================================================
    # LeetCode 210. Course Schedule II - Python实现
    # 题目链接: https://leetcode.com/problems/course-schedule-ii/
    # 
    # 时间复杂度：O(V + E)
    # 空间复杂度：O(V + E)
    
    def find_order(self, numCourses: int, prerequisites: List[List[int]]) -> List[int]:
        """
        返回完成所有课程的学习顺序
        
        Args:
            numCourses: 课程总数
            prerequisites: 先修课程关系列表
            
        Returns:
            List[int]: 课程学习顺序，如果无法完成则返回空列表
        """
        graph = [[] for _ in range(numCourses)]
        in_degree = [0] * numCourses
        
        # 构建图
        for course, pre_course in prerequisites:
            graph[pre_course].append(course)
            in_degree[course] += 1
        
        queue = deque()
        result = []
        
        # 入度为0的节点入队
        for i in range(numCourses):
            if in_degree[i] == 0:
                queue.append(i)
        
        # 拓扑排序
        while queue:
            current = queue.popleft()
            result.append(current)
            
            for next_course in graph[current]:
                in_degree[next_course] -= 1
                if in_degree[next_course] == 0:
                    queue.append(next_course)
        
        return result if len(result) == numCourses else []
    
    # =====================================================================
    # LeetCode 269. Alien Dictionary - Python实现
    # 题目链接: https://leetcode.com/problems/alien-dictionary/
    # 
    # 时间复杂度：O(C)，C是所有单词中字符的总数
    # 空间复杂度：O(1)，字符集大小固定
    
    def alien_order(self, words: List[str]) -> str:
        """
        推断外星语的字母顺序
        
        Args:
            words: 按外星语字典序排列的单词列表
            
        Returns:
            str: 外星语的字母顺序，如果不存在合法顺序则返回空字符串
        """
        # 构建字符关系图
        graph = defaultdict(set)
        in_degree = defaultdict(int)
        
        # 初始化所有字符
        for word in words:
            for char in word:
                graph[char] = set()
                in_degree[char] = 0
        
        # 构建字符顺序关系
        for i in range(len(words) - 1):
            word1, word2 = words[i], words[i + 1]
            
            # 检查前缀关系
            if len(word1) > len(word2) and word1.startswith(word2):
                return ""
            
            # 找到第一个不同的字符
            min_len = min(len(word1), len(word2))
            for j in range(min_len):
                char1, char2 = word1[j], word2[j]
                if char1 != char2:
                    if char2 not in graph[char1]:
                        graph[char1].add(char2)
                        in_degree[char2] += 1
                    break
        
        # 拓扑排序
        queue = deque()
        for char in in_degree:
            if in_degree[char] == 0:
                queue.append(char)
        
        result = []
        while queue:
            current = queue.popleft()
            result.append(current)
            
            for next_char in graph[current]:
                in_degree[next_char] -= 1
                if in_degree[next_char] == 0:
                    queue.append(next_char)
        
        return ''.join(result) if len(result) == len(in_degree) else ""
    
    # =====================================================================
    # HDU 1285 - 确定比赛名次 - Python实现
    # 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1285
    # 
    # 要求输出字典序最小的拓扑序列
    # 时间复杂度：O(V log V + E)
    # 空间复杂度：O(V + E)
    
    def determine_ranking(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        确定比赛名次（字典序最小）
        
        Args:
            n: 队伍数量
            edges: 比赛结果边列表
            
        Returns:
            List[int]: 排名顺序
        """
        graph = [[] for _ in range(n + 1)]
        in_degree = [0] * (n + 1)
        
        # 构建图
        for u, v in edges:
            graph[u].append(v)
            in_degree[v] += 1
        
        # 使用最小堆保证字典序最小
        heap = []
        result = []
        
        # 入度为0的节点入堆
        for i in range(1, n + 1):
            if in_degree[i] == 0:
                heapq.heappush(heap, i)
        
        # 拓扑排序
        while heap:
            current = heapq.heappop(heap)
            result.append(current)
            
            for next_node in graph[current]:
                in_degree[next_node] -= 1
                if in_degree[next_node] == 0:
                    heapq.heappush(heap, next_node)
        
        return result
    
    # =====================================================================
    # POJ 1094 - Sorting It All Out - Python实现
    # 题目链接: http://poj.org/problem?id=1094
    # 
    # 逐步添加关系并检查状态
    # 时间复杂度：O(m * (n + m))
    # 空间复杂度：O(n + m)
    
    def sorting_it_all_out(self, n: int, relations: List[str]) -> str:
        """
        逐步确定字符顺序
        
        Args:
            n: 字符数量
            relations: 关系字符串列表
            
        Returns:
            str: 结果描述字符串
        """
        graph = [[False] * 26 for _ in range(26)]
        in_degree = [0] * 26
        
        for i, rel in enumerate(relations):
            from_char = ord(rel[0]) - ord('A')
            to_char = ord(rel[2]) - ord('A')
            
            if not graph[from_char][to_char]:
                graph[from_char][to_char] = True
                in_degree[to_char] += 1
            
            # 检查当前状态
            result_type, order = self._check_topological_sort(graph, in_degree, n)
            
            if result_type == -1:
                return f"Inconsistency found after {i + 1} relations."
            elif result_type == 1:
                return f"Sorted sequence determined after {i + 1} relations: {order}."
        
        return "Sorted sequence cannot be determined."
    
    def _check_topological_sort(self, graph: List[List[bool]], in_degree: List[int], n: int) -> Tuple[int, str]:
        """
        检查拓扑排序状态
        
        Returns:
            Tuple[int, str]: (状态类型, 顺序字符串)
            -1: 有环, 0: 不唯一, 1: 唯一确定
        """
        temp_in_degree = in_degree.copy()
        visited = [False] * 26
        order = []
        multiple = False
        
        for _ in range(n):
            zero_nodes = []
            for j in range(n):
                if not visited[j] and temp_in_degree[j] == 0:
                    zero_nodes.append(j)
            
            if not zero_nodes:
                return -1, ""  # 有环
            
            if len(zero_nodes) > 1:
                multiple = True  # 不唯一
            
            current = min(zero_nodes)  # 取最小的保证一致性
            order.append(chr(ord('A') + current))
            visited[current] = True
            
            for k in range(n):
                if graph[current][k]:
                    temp_in_degree[k] -= 1
        
        return (0, ''.join(order)) if multiple else (1, ''.join(order))
    
    # =====================================================================
    # UVA 10305 - Ordering Tasks - Python实现
    # 题目链接: https://vjudge.net/problem/UVA-10305
    # 
    # 经典拓扑排序模板题
    # 时间复杂度：O(V + E)
    # 空间复杂度：O(V + E)
    
    def ordering_tasks(self, n: int, constraints: List[List[int]]) -> List[int]:
        """
        任务排序
        
        Args:
            n: 任务数量
            constraints: 约束关系列表
            
        Returns:
            List[int]: 任务执行顺序
        """
        graph = [[] for _ in range(n + 1)]
        in_degree = [0] * (n + 1)
        
        # 构建图
        for u, v in constraints:
            graph[u].append(v)
            in_degree[v] += 1
        
        queue = deque()
        result = []
        
        # 入度为0的节点入队
        for i in range(1, n + 1):
            if in_degree[i] == 0:
                queue.append(i)
        
        # 拓扑排序
        while queue:
            current = queue.popleft()
            result.append(current)
            
            for next_node in graph[current]:
                in_degree[next_node] -= 1
                if in_degree[next_node] == 0:
                    queue.append(next_node)
        
        return result
    
    # =====================================================================
    # SPOJ TOPOSORT - Topological Sorting - Python实现
    # 题目链接: https://www.spoj.com/problems/TOPOSORT/
    # 
    # 字典序最小的拓扑排序
    # 时间复杂度：O(V log V + E)
    # 空间复杂度：O(V + E)
    
    def topological_sort_lexicographically(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        字典序最小的拓扑排序
        
        Args:
            n: 节点数量
            edges: 边列表
            
        Returns:
            List[int]: 拓扑排序结果
        """
        graph = [[] for _ in range(n + 1)]
        in_degree = [0] * (n + 1)
        
        # 构建图
        for u, v in edges:
            graph[u].append(v)
            in_degree[v] += 1
        
        # 使用最小堆
        heap = []
        result = []
        
        # 入度为0的节点入堆
        for i in range(1, n + 1):
            if in_degree[i] == 0:
                heapq.heappush(heap, i)
        
        # 拓扑排序
        while heap:
            current = heapq.heappop(heap)
            result.append(current)
            
            for next_node in graph[current]:
                in_degree[next_node] -= 1
                if in_degree[next_node] == 0:
                    heapq.heappush(heap, next_node)
        
        return result
    
    # =====================================================================
    # Codeforces 510C - Fox And Names - Python实现
    # 题目链接: https://codeforces.com/problemset/problem/510/C
    # 
    # 类似外星字典问题
    # 时间复杂度：O(C)
    # 空间复杂度：O(1)
    
    def fox_and_names(self, names: List[str]) -> str:
        """
        狐狸和名字问题
        
        Args:
            names: 名字列表
            
        Returns:
            str: 字符顺序，如果不可能则返回"Impossible"
        """
        graph = defaultdict(set)
        in_degree = defaultdict(int)
        
        # 初始化字符
        for name in names:
            for char in name:
                graph[char] = set()
                in_degree[char] = 0
        
        # 构建字符顺序关系
        for i in range(len(names) - 1):
            name1, name2 = names[i], names[i + 1]
            
            # 检查前缀关系
            if len(name1) > len(name2) and name1.startswith(name2):
                return "Impossible"
            
            # 找到第一个不同的字符
            min_len = min(len(name1), len(name2))
            for j in range(min_len):
                char1, char2 = name1[j], name2[j]
                if char1 != char2:
                    if char2 not in graph[char1]:
                        graph[char1].add(char2)
                        in_degree[char2] += 1
                    break
        
        # 拓扑排序
        queue = deque()
        for char in in_degree:
            if in_degree[char] == 0:
                queue.append(char)
        
        result = []
        while queue:
            current = queue.popleft()
            result.append(current)
            
            for next_char in graph[current]:
                in_degree[next_char] -= 1
                if in_degree[next_char] == 0:
                    queue.append(next_char)
        
        return ''.join(result) if len(result) == len(in_degree) else "Impossible"
    
    # =====================================================================
    # 工程化考量 - Python特性
    # 
    # 1. 类型注解：提高代码可读性和IDE支持
    # 2. 异常处理：完善的错误处理机制
    # 3. 文档字符串：详细的API文档
    # 4. 单元测试：使用unittest或pytest
    # 5. 性能分析：使用cProfile进行性能分析
    
    def validate_input(self, n: int, edges: List[List[int]]) -> None:
        """
        验证输入参数
        
        Args:
            n: 节点数量
            edges: 边列表
            
        Raises:
            ValueError: 输入参数无效时抛出
        """
        if n <= 0:
            raise ValueError("节点数量必须大于0")
        
        if not isinstance(edges, list):
            raise ValueError("边列表必须是列表类型")
        
        for edge in edges:
            if len(edge) != 2:
                raise ValueError("每条边必须包含两个节点")
            if edge[0] < 1 or edge[0] > n or edge[1] < 1 or edge[1] > n:
                raise ValueError(f"节点编号必须在1到{n}之间")
    
    def performance_monitor(self, func, *args, **kwargs):
        """
        性能监控装饰器
        
        Args:
            func: 要监控的函数
            
        Returns:
            函数执行结果
        """
        import time
        
        def wrapper(*args, **kwargs):
            start_time = time.time()
            result = func(*args, **kwargs)
            end_time = time.time()
            print(f"{func.__name__} 执行时间: {end_time - start_time:.6f}秒")
            return result
        
        return wrapper
    
    # =====================================================================
    # 高级特性：生成器版本
    
    def topological_sort_generator(self, n: int, edges: List[List[int]]):
        """
        生成器版本的拓扑排序
        
        Args:
            n: 节点数量
            edges: 边列表
            
        Yields:
            int: 拓扑排序中的每个节点
        """
        graph = [[] for _ in range(n + 1)]
        in_degree = [0] * (n + 1)
        
        # 构建图
        for u, v in edges:
            graph[u].append(v)
            in_degree[v] += 1
        
        queue = deque()
        for i in range(1, n + 1):
            if in_degree[i] == 0:
                queue.append(i)
        
        while queue:
            current = queue.popleft()
            yield current
            
            for next_node in graph[current]:
                in_degree[next_node] -= 1
                if in_degree[next_node] == 0:
                    queue.append(next_node)

def test_all_solutions():
    """测试所有解决方案"""
    print("=== 拓扑排序综合题目集测试 ===")
    
    solution = TopologicalSortingComprehensive()
    
    # 测试LeetCode 207
    prerequisites1 = [[1, 0]]
    result1 = solution.can_finish(2, prerequisites1)
    print(f"LeetCode 207: {result1}")
    
    # 测试LeetCode 210
    prerequisites2 = [[1, 0]]
    result2 = solution.find_order(2, prerequisites2)
    print(f"LeetCode 210: {result2}")
    
    # 测试LeetCode 269
    words = ["wrt", "wrf", "er", "ett", "rftt"]
    result3 = solution.alien_order(words)
    print(f"LeetCode 269: {result3}")
    
    # 测试HDU 1285
    edges = [[1, 2], [1, 3], [2, 4], [3, 4]]
    result4 = solution.determine_ranking(4, edges)
    print(f"HDU 1285: {result4}")
    
    # 测试生成器版本
    print("生成器版本测试:")
    for node in solution.topological_sort_generator(4, edges):
        print(f"节点: {node}")
    
    print("=== 测试完成 ===")

if __name__ == "__main__":
    test_all_solutions()
"""
LeetCode 207. Course Schedule

题目链接: https://leetcode.com/problems/course-schedule/

题目描述：
你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1。
在选修某些课程之前需要一些先修课程。先修课程按数组 prerequisites 给出，
其中 prerequisites[i] = [ai, bi]，表示如果要学习课程 ai 则必须先学习课程 bi。
请你判断是否可能完成所有课程的学习？如果可以，返回 true；否则，返回 false。

解题思路：
这是一个典型的拓扑排序问题。我们需要判断有向图中是否存在环。
如果存在环，则无法完成所有课程；如果不存在环，则可以完成。
我们可以使用Kahn算法来解决：
1. 计算每个节点的入度
2. 将所有入度为0的节点加入队列
3. 不断从队列中取出节点，并将其所有邻居节点的入度减1
4. 如果邻居节点的入度变为0，则将其加入队列
5. 重复步骤3-4直到队列为空
6. 最后检查处理的节点数是否等于总节点数

时间复杂度：O(V + E)，其中V是课程数，E是先修关系数
空间复杂度：O(V + E)，用于存储图和入度数组

示例：
输入：numCourses = 2, prerequisites = [[1,0]]
输出：true
解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0。这是可能的。

输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
输出：false
解释：总共有 2 门课程。学习课程 1 之前，你需要先完成课程 0；
      并且学习课程 0 之前，你还应先完成课程 1。这是不可能的。
"""

from collections import deque, defaultdict
from typing import List

class Solution:
    def canFinish(self, numCourses: int, prerequisites: List[List[int]]) -> bool:
        """
        判断是否可以完成所有课程
        :param numCourses: 课程总数
        :param prerequisites: 先修课程关系数组
        :return: 如果可以完成所有课程返回True，否则返回False
        """
        # 构建邻接表表示的图
        graph = defaultdict(list)
        
        # 初始化入度数组
        in_degree = [0] * numCourses
        
        # 构建图和入度数组
        for course, pre_course in prerequisites:
            # 添加边：先修课程 -> 当前课程
            graph[pre_course].append(course)
            
            # 当前课程入度加1
            in_degree[course] += 1
        
        # 使用Kahn算法进行拓扑排序
        return self.topological_sort(graph, in_degree, numCourses)
    
    def topological_sort(self, graph: defaultdict, in_degree: List[int], num_courses: int) -> bool:
        """
        使用Kahn算法进行拓扑排序，判断是否存在环
        :param graph: 邻接表表示的图
        :param in_degree: 入度数组
        :param num_courses: 课程总数
        :return: 如果不存在环返回True，否则返回False
        """
        queue = deque()
        
        # 将所有入度为0的节点加入队列
        for i in range(num_courses):
            if in_degree[i] == 0:
                queue.append(i)
        
        processed_courses = 0  # 记录已处理的课程数
        
        # Kahn算法进行拓扑排序
        while queue:
            current_course = queue.popleft()
            processed_courses += 1
            
            # 遍历当前课程的所有后续课程
            for next_course in graph[current_course]:
                # 将后续课程的入度减1
                in_degree[next_course] -= 1
                
                # 如果后续课程的入度变为0，则加入队列
                if in_degree[next_course] == 0:
                    queue.append(next_course)
        
        # 如果处理的课程数等于总课程数，说明不存在环，可以完成所有课程
        return processed_courses == num_courses

def main():
    solution = Solution()
    
    # 测试用例1
    numCourses1 = 2
    prerequisites1 = [[1, 0]]
    print(f"Test Case 1: {solution.canFinish(numCourses1, prerequisites1)}")  # 应该输出 True
    
    # 测试用例2
    numCourses2 = 2
    prerequisites2 = [[1, 0], [0, 1]]
    print(f"Test Case 2: {solution.canFinish(numCourses2, prerequisites2)}")  # 应该输出 False
    
    # 测试用例3
    numCourses3 = 3
    prerequisites3 = [[1, 0], [2, 1]]
    print(f"Test Case 3: {solution.canFinish(numCourses3, prerequisites3)}")  # 应该输出 True

if __name__ == "__main__":
    main()


class CourseScheduleII:
    """
    LeetCode 210. Course Schedule II
    题目链接: https://leetcode.com/problems/course-schedule-ii/
    
    题目描述：
    返回完成所有课程的学习顺序。如果有多个可能的答案，返回任意一个。
    如果不可能完成所有课程，返回一个空数组。
    
    解题思路：
    使用Kahn算法进行拓扑排序，同时记录拓扑排序的结果
    
    时间复杂度：O(V + E)
    空间复杂度：O(V + E)
    """
    def findOrder(self, numCourses: int, prerequisites: List[List[int]]) -> List[int]:
        # 构建邻接表表示的图
        graph = defaultdict(list)
        
        # 初始化入度数组
        in_degree = [0] * numCourses
        
        # 构建图和入度数组
        for course, pre_course in prerequisites:
            # 添加边：先修课程 -> 当前课程
            graph[pre_course].append(course)
            
            # 当前课程入度加1
            in_degree[course] += 1
        
        # 使用Kahn算法进行拓扑排序
        return self.topological_sort(graph, in_degree, numCourses)
    
    def topological_sort(self, graph: defaultdict, in_degree: List[int], num_courses: int) -> List[int]:
        queue = deque()
        result = []
        
        # 将所有入度为0的节点加入队列
        for i in range(num_courses):
            if in_degree[i] == 0:
                queue.append(i)
        
        # Kahn算法进行拓扑排序
        while queue:
            current_course = queue.popleft()
            result.append(current_course)
            
            # 遍历当前课程的所有后续课程
            for next_course in graph[current_course]:
                in_degree[next_course] -= 1
                
                if in_degree[next_course] == 0:
                    queue.append(next_course)
        
        # 如果处理的课程数等于总课程数，返回排序结果，否则返回空数组
        return result if len(result) == num_courses else []


class AlienDictionary:
    """
    LeetCode 269. Alien Dictionary
    题目链接: https://leetcode.com/problems/alien-dictionary/
    
    题目描述：
    给定一个按字典序排序的外星文字典中的单词列表，推断其中字母的顺序。
    
    解题思路：
    1. 构建字符之间的有向图
    2. 使用拓扑排序确定字母顺序
    
    时间复杂度：O(V + E)，其中V是字符集大小，E是字符之间的约束关系数
    空间复杂度：O(V + E)
    """
    def alienOrder(self, words: List[str]) -> str:
        # 构建图
        graph = defaultdict(list)
        in_degree = defaultdict(int)
        
        # 初始化所有出现的字符
        for word in words:
            for c in word:
                if c not in graph:
                    graph[c] = []
                    in_degree[c] = 0
        
        # 构建字符之间的约束关系
        for i in range(len(words) - 1):
            word1 = words[i]
            word2 = words[i + 1]
            
            # 检查是否是前缀关系
            if len(word1) > len(word2) and word1.startswith(word2):
                return ""
            
            # 找出第一个不同的字符
            min_length = min(len(word1), len(word2))
            for j in range(min_length):
                c1 = word1[j]
                c2 = word2[j]
                
                if c1 != c2:
                    # 避免重复添加边
                    if c2 not in graph[c1]:
                        graph[c1].append(c2)
                        in_degree[c2] += 1
                    break
        
        # 使用Kahn算法进行拓扑排序
        queue = deque()
        for c in in_degree:
            if in_degree[c] == 0:
                queue.append(c)
        
        result = []
        while queue:
            current = queue.popleft()
            result.append(current)
            
            for neighbor in graph[current]:
                in_degree[neighbor] -= 1
                if in_degree[neighbor] == 0:
                    queue.append(neighbor)
        
        # 检查是否有环
        if len(result) != len(in_degree):
            return ""
        
        return "".join(result)


class StampingTheSequence:
    """
    LeetCode 936. Stamping The Sequence
    题目链接: https://leetcode.com/problems/stamping-the-sequence/
    
    题目描述：
    给定一个目标字符串 target 和一个印章字符串 stamp，返回一个操作序列，使得可以通过这些操作将一个全'?'字符串转换为 target。
    每个操作是在字符串的某个位置盖上印章，覆盖原字符。
    
    解题思路：
    使用逆向思维和拓扑排序，从 target 向全'?'字符串转换
    
    时间复杂度：O(N^2 * M)，其中N是target长度，M是stamp长度
    空间复杂度：O(N^2)
    """
    def movesToStamp(self, stamp: str, target: str) -> List[int]:
        m, n = len(stamp), len(target)
        
        # 逆向思维，从target向全'?'转换
        # 记录每个位置被覆盖的次数
        cover_count = [0] * (n - m + 1)
        # 记录每个位置可以被覆盖的字符数
        matched = [0] * n
        
        queue = deque()
        result_list = []
        
        # 初始时，找出所有可以完全匹配的子串
        for i in range(n - m + 1):
            for j in range(m):
                if stamp[j] == target[i + j]:
                    cover_count[i] += 1
            
            if cover_count[i] == m:
                # 可以完全匹配，加入队列
                queue.append(i)
                result_list.append(i)
                for j in range(m):
                    if matched[i + j] == 0:
                        matched[i + j] = 1
        
        # 进行拓扑排序
        while queue:
            pos = queue.popleft()
            
            # 检查pos周围的位置
            start = max(0, pos - m + 1)
            end = min(n - m, pos + m - 1)
            for i in range(start, end + 1):
                if i == pos:
                    continue
                
                overlap = False
                for j in range(m):
                    if i + j >= pos and i + j < pos + m:
                        overlap = True
                        # 如果当前字符已经被覆盖为'?'，则更新覆盖次数
                        if matched[pos + j - i] == 1 and stamp[j] == target[i + j]:
                            if cover_count[i] < m:
                                cover_count[i] += 1
                
                if overlap and cover_count[i] == m:
                    queue.append(i)
                    result_list.append(i)
                    # 标记被覆盖的位置
                    for j in range(m):
                        if matched[i + j] == 0:
                            matched[i + j] = 1
        
        # 检查是否所有字符都被覆盖
        for i in range(n):
            if matched[i] == 0:
                return []
        
        # 反转结果，因为是逆向操作
        result_list.reverse()
        return result_list


class TopologicalSortDFS:
    """
    使用DFS算法实现拓扑排序
    """
    def __init__(self):
        self.has_cycle = False
        self.visited = []
        self.on_path = []
        self.postorder = []
    
    def canFinish(self, numCourses: int, prerequisites: List[List[int]]) -> bool:
        # 构建图
        graph = [[] for _ in range(numCourses)]
        for course, pre_course in prerequisites:
            graph[pre_course].append(course)
        
        # 初始化
        self.visited = [False] * numCourses
        self.on_path = [False] * numCourses
        self.has_cycle = False
        self.postorder = []
        
        # 遍历所有节点
        for i in range(numCourses):
            if not self.visited[i]:
                self.traverse(graph, i)
        
        return not self.has_cycle
    
    def traverse(self, graph: List[List[int]], node: int) -> None:
        # 如果在当前路径中找到了节点，说明存在环
        if self.on_path[node]:
            self.has_cycle = True
            return
        
        if self.visited[node]:
            return
        
        self.visited[node] = True
        self.on_path[node] = True
        
        # 遍历所有邻居节点
        for neighbor in graph[node]:
            self.traverse(graph, neighbor)
            if self.has_cycle:
                return
        
        self.postorder.append(node)
        self.on_path[node] = False
    
    def findOrder(self, numCourses: int, prerequisites: List[List[int]]) -> List[int]:
        # 构建图
        graph = [[] for _ in range(numCourses)]
        for course, pre_course in prerequisites:
            graph[pre_course].append(course)
        
        # 初始化
        self.visited = [False] * numCourses
        self.on_path = [False] * numCourses
        self.has_cycle = False
        self.postorder = []
        
        # 遍历所有节点
        for i in range(numCourses):
            if not self.visited[i]:
                self.traverse(graph, i)
        
        # 如果有环，返回空数组
        if self.has_cycle:
            return []
        
        # 拓扑排序结果需要反转后序遍历的结果
        return self.postorder[::-1]


class LongestIncreasingPath:
    """
    牛客 NC143. 矩阵中的最长递增路径
    题目链接: https://www.nowcoder.com/practice/7a514e7c3727442aa17463a549904c5d
    
    题目描述：
    给定一个 n x m 的矩阵，找出一条最长的递增路径。
    路径可以从任意单元格开始，每一步可以向上、下、左、右移动一格。
    要求路径上的每个单元格的数字严格大于前一个单元格的数字。
    
    解题思路：
    使用拓扑排序结合动态规划
    """
    def longestIncreasingPath(self, matrix: List[List[int]]) -> int:
        if not matrix or not matrix[0]:
            return 0
        
        n, m = len(matrix), len(matrix[0])
        dirs = [(-1, 0), (1, 0), (0, -1), (0, 1)]  # 上下左右四个方向
        
        # 构建邻接表和入度数组
        graph = [[] for _ in range(n * m)]
        in_degree = [0] * (n * m)
        
        # 构建图
        for i in range(n):
            for j in range(m):
                current = i * m + j
                for dx, dy in dirs:
                    ni, nj = i + dx, j + dy
                    # 如果相邻单元格在矩阵范围内且值更大
                    if 0 <= ni < n and 0 <= nj < m and matrix[ni][nj] > matrix[i][j]:
                        next_node = ni * m + nj
                        graph[current].append(next_node)
                        in_degree[next_node] += 1
        
        # 使用拓扑排序计算最长路径
        dp = [1] * (n * m)  # 每个节点自身的路径长度为1
        queue = deque()
        
        # 将所有入度为0的节点加入队列
        for i in range(n * m):
            if in_degree[i] == 0:
                queue.append(i)
        
        max_length = 1
        
        # 执行拓扑排序
        while queue:
            current = queue.popleft()
            
            # 更新所有邻居节点的最长路径
            for next_node in graph[current]:
                # 更新dp[next_node]为较大值
                if dp[next_node] < dp[current] + 1:
                    dp[next_node] = dp[current] + 1
                    max_length = max(max_length, dp[next_node])
                
                # 减少入度
                in_degree[next_node] -= 1
                if in_degree[next_node] == 0:
                    queue.append(next_node)
        
        return max_length
# LeetCode 582. Kill Process (杀死进程)
# 来源: LeetCode
# 网址: https://leetcode.cn/problems/kill-process/
# 
# 题目描述:
# 给定n个进程，每个进程都有一个唯一的PID（进程ID）和它的PPID（父进程ID）。
# 每个进程只有一个父进程，但是可能有多个子进程。这形成了一个树状结构。
# 只有一个进程的PPID是0，这意味着这个进程没有父进程。所有的进程都应该是这个进程的后代。
# 当一个进程被杀死时，它的所有子进程和后代进程也应该被杀死。
# 给定一个PID和一个PPID列表，以及一个要杀死的进程kill，请返回所有应该被杀死的进程的ID列表。
# 你可以以任意顺序返回答案。
# 
# 示例:
# 输入：
# pid = [1, 3, 10, 5]
# ppid = [3, 0, 5, 3]
# kill = 5
# 输出：[5, 10]
# 解释：
#           3
#         /   \
#        1     5
#             /
#            10
# 杀死进程5，其子进程10也应该被杀死。
# 
# 解题思路:
# 1. 首先构建进程之间的父子关系映射
# 2. 使用深度优先搜索（递归）从kill进程开始，收集所有应该被杀死的进程
# 
# 时间复杂度: O(n)，其中n是进程的数量，构建映射需要O(n)，DFS遍历需要O(n)
# 空间复杂度: O(n)，用于存储映射和递归调用栈

from typing import List
from collections import defaultdict, deque

class Solution:
    def killProcess(self, pid: List[int], ppid: List[int], kill: int) -> List[int]:
        """
        使用深度优先搜索解决Kill Process问题
        
        Args:
            pid: 进程ID列表
            ppid: 父进程ID列表
            kill: 要杀死的进程ID
            
        Returns:
            List[int]: 所有应该被杀死的进程ID列表
        """
        # 结果列表，存储所有应该被杀死的进程ID
        result = []
        
        # 构建父进程到子进程的映射
        parent_to_children = self._build_process_tree(pid, ppid)
        
        # 从kill进程开始，递归收集所有应该被杀死的进程
        self._dfs(parent_to_children, kill, result)
        
        return result
    
    def _build_process_tree(self, pid: List[int], ppid: List[int]) -> dict:
        """
        构建父进程到子进程的映射
        
        Args:
            pid: 进程ID列表
            ppid: 父进程ID列表
            
        Returns:
            dict: 父进程到子进程的映射字典
        """
        parent_to_children = defaultdict(list)
        
        # 遍历所有进程，构建父子关系
        for i in range(len(pid)):
            parent_id = ppid[i]
            child_id = pid[i]
            
            # 将子进程添加到父进程的子列表中
            parent_to_children[parent_id].append(child_id)
        
        return parent_to_children
    
    def _dfs(self, parent_to_children: dict, current_process: int, result: List[int]) -> None:
        """
        深度优先搜索，收集所有应该被杀死的进程
        
        Args:
            parent_to_children: 父进程到子进程的映射
            current_process: 当前处理的进程ID
            result: 结果列表
        """
        # 将当前进程添加到结果列表中（标记为需要被杀死）
        result.append(current_process)
        
        # 获取当前进程的所有子进程
        children = parent_to_children.get(current_process, [])
        
        # 递归处理每个子进程
        for child in children:
            self._dfs(parent_to_children, child, result)
    
    def killProcessBFS(self, pid: List[int], ppid: List[int], kill: int) -> List[int]:
        """
        使用广度优先搜索解决Kill Process问题（迭代方法）
        
        Args:
            pid: 进程ID列表
            ppid: 父进程ID列表
            kill: 要杀死的进程ID
            
        Returns:
            List[int]: 所有应该被杀死的进程ID列表
        """
        # 结果列表，存储所有应该被杀死的进程ID
        result = []
        
        # 构建父进程到子进程的映射
        parent_to_children = self._build_process_tree(pid, ppid)
        
        # 使用队列进行广度优先搜索
        queue = deque([kill])
        
        while queue:
            current_process = queue.popleft()
            result.append(current_process)
            
            # 将当前进程的所有子进程加入队列
            children = parent_to_children.get(current_process, [])
            for child in children:
                queue.append(child)
        
        return result

# 测试函数
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    pid1 = [1, 3, 10, 5]
    ppid1 = [3, 0, 5, 3]
    kill1 = 5
    
    print("测试用例1 (DFS方法):")
    print(f"pid = {pid1}")
    print(f"ppid = {ppid1}")
    print(f"kill = {kill1}")
    print(f"输出: {solution.killProcess(pid1, ppid1, kill1)}")
    print("期望: [5, 10]")
    print()
    
    print("测试用例1 (BFS方法):")
    print(f"输出: {solution.killProcessBFS(pid1, ppid1, kill1)}")
    print("期望: [5, 10]")
    print()
    
    # 测试用例2
    pid2 = [1]
    ppid2 = [0]
    kill2 = 1
    
    print("测试用例2 (DFS方法):")
    print(f"pid = {pid2}")
    print(f"ppid = {ppid2}")
    print(f"kill = {kill2}")
    print(f"输出: {solution.killProcess(pid2, ppid2, kill2)}")
    print("期望: [1]")
    print()
    
    # 测试用例3：更复杂的树结构
    pid3 = [1, 2, 3, 4, 5, 6, 7]
    ppid3 = [0, 1, 1, 2, 2, 3, 3]
    kill3 = 2
    
    print("测试用例3 (DFS方法):")
    print(f"pid = {pid3}")
    print(f"ppid = {ppid3}")
    print(f"kill = {kill3}")
    print(f"输出: {solution.killProcess(pid3, ppid3, kill3)}")
    # 期望: [2, 4, 5]
    print("期望: [2, 4, 5]")
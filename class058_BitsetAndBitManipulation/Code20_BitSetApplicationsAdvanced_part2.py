"""
高级位集应用实现 - 第二部分
包含LeetCode多个高级位集应用相关题目的解决方案

题目列表:
6. LeetCode 208 - 实现 Trie (前缀树)
7. LeetCode 211 - 添加与搜索单词 - 数据结构设计
8. LeetCode 126 - 单词接龙 II
9. LeetCode 127 - 单词接龙
10. LeetCode 130 - 被围绕的区域
"""

import time
from typing import List, Optional
import sys
from collections import defaultdict, deque

class BitSetApplicationsAdvanced:
    """高级位集应用类"""
    
    class Trie:
        """
        LeetCode 208 - 实现 Trie (前缀树)
        实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
        
        方法: Trie树实现
        时间复杂度: O(L) - L为单词长度
        空间复杂度: O(n*L) - n为单词数量
        """
        
        def __init__(self):
            self.children = {}
            self.is_end = False
        
        def insert(self, word: str) -> None:
            node = self
            for char in word:
                if char not in node.children:
                    node.children[char] = BitSetApplicationsAdvanced.Trie()
                node = node.children[char]
            node.is_end = True
        
        def search(self, word: str) -> bool:
            node = self
            for char in word:
                if char not in node.children:
                    return False
                node = node.children[char]
            return node.is_end
        
        def starts_with(self, prefix: str) -> bool:
            node = self
            for char in prefix:
                if char not in node.children:
                    return False
                node = node.children[char]
            return True
    
    class WordDictionary:
        """
        LeetCode 211 - 添加与搜索单词 - 数据结构设计
        设计一个支持以下两种操作的数据结构：
        - void addWord(word)
        - bool search(word)
        search(word) 可以搜索文字或正则表达式字符串，字符串只包含字母 . 或 a-z 。 . 可以表示任何一个字母。
        
        方法: Trie树 + 通配符处理
        时间复杂度: O(L) - 插入, O(26^L) - 搜索（最坏情况）
        空间复杂度: O(n*L)
        """
        
        def __init__(self):
            self.trie = BitSetApplicationsAdvanced.Trie()
        
        def add_word(self, word: str) -> None:
            self.trie.insert(word)
        
        def search(self, word: str) -> bool:
            def dfs(node: BitSetApplicationsAdvanced.Trie, index: int) -> bool:
                if index == len(word):
                    return node.is_end
                
                char = word[index]
                if char == '.':
                    for child in node.children.values():
                        if dfs(child, index + 1):
                            return True
                    return False
                else:
                    if char not in node.children:
                        return False
                    return dfs(node.children[char], index + 1)
            
            return dfs(self.trie, 0)
    
    @staticmethod
    def find_ladders(begin_word: str, end_word: str, word_list: List[str]) -> List[List[str]]:
        """
        LeetCode 126 - 单词接龙 II
        给定两个单词（beginWord 和 endWord）和一个字典 wordList，找出所有从 beginWord 到 endWord 的最短转换序列。
        
        方法: BFS + 回溯
        时间复杂度: O(n*k*26) - n为单词数量，k为单词长度
        空间复杂度: O(n*k)
        """
        if end_word not in word_list:
            return []
        
        word_set = set(word_list)
        word_set.add(begin_word)
        
        # 构建图
        graph = defaultdict(list)
        for word in word_set:
            for i in range(len(word)):
                pattern = word[:i] + '*' + word[i+1:]
                graph[pattern].append(word)
        
        # BFS构建距离映射
        distance = {begin_word: 0}
        queue = deque([begin_word])
        
        while queue:
            current = queue.popleft()
            if current == end_word:
                break
            
            for i in range(len(current)):
                pattern = current[:i] + '*' + current[i+1:]
                for neighbor in graph[pattern]:
                    if neighbor not in distance:
                        distance[neighbor] = distance[current] + 1
                        queue.append(neighbor)
        
        # 回溯查找所有最短路径
        result = []
        
        def backtrack(current: str, path: List[str]):
            if current == end_word:
                result.append(path[:])
                return
            
            for i in range(len(current)):
                pattern = current[:i] + '*' + current[i+1:]
                for neighbor in graph[pattern]:
                    if neighbor in distance and distance[neighbor] == distance[current] + 1:
                        path.append(neighbor)
                        backtrack(neighbor, path)
                        path.pop()
        
        backtrack(begin_word, [begin_word])
        return result
    
    @staticmethod
    def ladder_length(begin_word: str, end_word: str, word_list: List[str]) -> int:
        """
        LeetCode 127 - 单词接龙
        给定两个单词（beginWord 和 endWord）和一个字典 wordList，找到从 beginWord 到 endWord 的最短转换序列的长度。
        
        方法: 双向BFS
        时间复杂度: O(n*k*26)
        空间复杂度: O(n)
        """
        if end_word not in word_list:
            return 0
        
        word_set = set(word_list)
        
        # 双向BFS
        begin_set = {begin_word}
        end_set = {end_word}
        visited = set()
        length = 1
        
        while begin_set and end_set:
            if len(begin_set) > len(end_set):
                begin_set, end_set = end_set, begin_set
            
            next_set = set()
            
            for word in begin_set:
                for i in range(len(word)):
                    for c in 'abcdefghijklmnopqrstuvwxyz':
                        next_word = word[:i] + c + word[i+1:]
                        
                        if next_word in end_set:
                            return length + 1
                        
                        if next_word in word_set and next_word not in visited:
                            next_set.add(next_word)
                            visited.add(next_word)
            
            begin_set = next_set
            length += 1
        
        return 0
    
    @staticmethod
    def solve(board: List[List[str]]) -> None:
        """
        LeetCode 130 - 被围绕的区域
        给定一个二维的矩阵，包含 'X' 和 'O'（字母 O）。
        找到所有被 'X' 围绕的区域，并将这些区域里所有的 'O' 用 'X' 填充。
        
        方法: DFS/BFS从边界开始标记
        时间复杂度: O(m*n)
        空间复杂度: O(m*n)
        """
        if not board or not board[0]:
            return
        
        m, n = len(board), len(board[0])
        
        def dfs(i: int, j: int):
            if i < 0 or i >= m or j < 0 or j >= n or board[i][j] != 'O':
                return
            board[i][j] = '#'  # 标记为边界连接
            dfs(i+1, j)
            dfs(i-1, j)
            dfs(i, j+1)
            dfs(i, j-1)
        
        # 从边界开始标记
        for i in range(m):
            dfs(i, 0)
            dfs(i, n-1)
        
        for j in range(n):
            dfs(0, j)
            dfs(m-1, j)
        
        # 填充内部O为X，恢复边界标记为O
        for i in range(m):
            for j in range(n):
                if board[i][j] == 'O':
                    board[i][j] = 'X'
                elif board[i][j] == '#':
                    board[i][j] = 'O'


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_n_queens():
        """测试N皇后问题性能"""
        print("=== N皇后问题性能测试 ===")
        
        n = 8
        
        start = time.time()
        count = BitSetApplicationsAdvanced.total_n_queens(n)
        elapsed = (time.time() - start) * 1000  # 毫秒
        
        print(f"N皇后(n={n}): 解决方案数量={count}, 耗时={elapsed:.2f} ms")
    
    @staticmethod
    def test_sudoku():
        """测试数独求解性能"""
        print("\n=== 数独求解性能测试 ===")
        
        board = [
            ['5','3','.','.','7','.','.','.','.'],
            ['6','.','.','1','9','5','.','.','.'],
            ['.','9','8','.','.','.','.','6','.'],
            ['8','.','.','.','6','.','.','.','3'],
            ['4','.','.','8','.','3','.','.','1'],
            ['7','.','.','.','2','.','.','.','6'],
            ['.','6','.','.','.','.','2','8','.'],
            ['.','.','.','4','1','9','.','.','5'],
            ['.','.','.','.','8','.','.','7','9']
        ]
        
        start = time.time()
        BitSetApplicationsAdvanced.solve_sudoku(board)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"数独求解: 耗时={elapsed:.2f} μs")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 高级位集应用单元测试 ===")
        
        # 测试数独验证
        valid_sudoku = [
            ['5','3','.','.','7','.','.','.','.'],
            ['6','.','.','1','9','5','.','.','.'],
            ['.','9','8','.','.','.','.','6','.'],
            ['8','.','.','.','6','.','.','.','3'],
            ['4','.','.','8','.','3','.','.','1'],
            ['7','.','.','.','2','.','.','.','6'],
            ['.','6','.','.','.','.','2','8','.'],
            ['.','.','.','4','1','9','.','.','5'],
            ['.','.','.','.','8','.','.','7','9']
        ]
        assert BitSetApplicationsAdvanced.is_valid_sudoku(valid_sudoku) == True
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "total_n_queens": ("O(n!)", "O(n)"),
            "solve_n_queens": ("O(n!)", "O(n^2)"),
            "solve_sudoku": ("O(9^m)", "O(1)"),
            "is_valid_sudoku": ("O(1)", "O(1)"),
            "find_words": ("O(m*n*4^L)", "O(k*L)"),
            "ladder_length": ("O(n*k*26)", "O(n)"),
            "solve": ("O(m*n)", "O(m*n)")
        }
        
        for name, (time_complexity, space_complexity) in algorithms.items():
            print(f"{name}: 时间复杂度={time_complexity}, 空间复杂度={space_complexity}")


def main():
    """主函数"""
    print("高级位集应用实现")
    print("包含LeetCode多个高级位集应用相关题目的解决方案")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_n_queens()
    PerformanceTester.test_sudoku()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # N皇后示例
    n = 4
    print(f"N皇后(n={n})解决方案数量: {BitSetApplicationsAdvanced.total_n_queens(n)}")
    
    # 数独验证示例
    sudoku_board = [
        ['5','3','.','.','7','.','.','.','.'],
        ['6','.','.','1','9','5','.','.','.'],
        ['.','9','8','.','.','.','.','6','.'],
        ['8','.','.','.','6','.','.','.','3'],
        ['4','.','.','8','.','3','.','.','1'],
        ['7','.','.','.','2','.','.','.','6'],
        ['.','6','.','.','.','.','2','8','.'],
        ['.','.','.','4','1','9','.','.','5'],
        ['.','.','.','.','8','.','.','7','9']
    ]
    print(f"数独验证结果: {BitSetApplicationsAdvanced.is_valid_sudoku(sudoku_board)}")
    
    # Trie树示例
    trie = BitSetApplicationsAdvanced.Trie()
    trie.insert("apple")
    print(f"搜索'apple': {trie.search('apple')}")
    print(f"前缀'app': {trie.starts_with('app')}")


if __name__ == "__main__":
    main()
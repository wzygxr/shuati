"""
LeetCode 212. 单词搜索 II

题目描述：
给定一个二维网格 board 和一个字典中的单词列表 words，找出所有同时在二维网格和字典中出现的单词。
单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。
同一个单元格内的字母在一个单词中不允许被重复使用。

示例：
输入：
board = [
  ['o','a','a','n'],
  ['e','t','a','e'],
  ['i','h','k','r'],
  ['i','f','l','v']
]
words = ["oath","pea","eat","rain"]
输出：["eat","oath"]

提示：
m == board.length
n == board[i].length
1 <= m, n <= 12
1 <= words.length <= 3 * 10^4
1 <= words[i].length <= 10
board 和 words[i] 仅由小写英文字母组成
words 中的所有字符串互不相同

链接：https://leetcode.cn/problems/word-search-ii/
"""

class Solution:
    def findWords(self, board, words):
        """
        查找二维网格中所有存在于字典中的单词
        
        算法思路：
        1. 构建Trie树，将所有单词插入Trie中
        2. 对二维网格中的每个单元格作为起点，进行深度优先搜索
        3. 使用Trie树来剪枝无效的搜索路径
        4. 找到单词后，将其加入结果集并从Trie中移除，避免重复添加
        
        时间复杂度：O(M*N*4^L)，其中M和N是网格的行数和列数，L是单词的最大长度。每个单元格最多被访问4^L次。
        空间复杂度：O(K)，其中K是所有单词的字符总数，用于存储Trie树。
        
        :param board: 二维字符网格
        :param words: 单词列表
        :return: 网格中存在的单词列表
        """
        # 构建Trie树
        root = {}
        for word in words:
            node = root
            for char in word:
                if char not in node:
                    node[char] = {}
                node = node[char]
            node['#'] = word  # 标记单词结尾
        
        result = set()
        m, n = len(board), len(board[0])
        
        def dfs(i, j, node):
            """
            深度优先搜索
            
            :param i: 当前行索引
            :param j: 当前列索引
            :param node: 当前Trie节点
            """
            # 检查边界条件
            if i < 0 or i >= m or j < 0 or j >= n or board[i][j] == '#':
                return
            
            char = board[i][j]
            # 如果当前字符不在Trie的子节点中，剪枝
            if char not in node:
                return
            
            # 移动到下一个Trie节点
            node = node[char]
            
            # 如果找到一个单词
            if '#' in node:
                result.add(node['#'])
                
            # 标记当前单元格为已访问
            temp = board[i][j]
            board[i][j] = '#'
            
            # 向四个方向搜索
            dfs(i + 1, j, node)
            dfs(i - 1, j, node)
            dfs(i, j + 1, node)
            dfs(i, j - 1, node)
            
            # 回溯：恢复当前单元格
            board[i][j] = temp
        
        # 遍历网格中的每个单元格作为起点
        for i in range(m):
            for j in range(n):
                dfs(i, j, root)
        
        return list(result)


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    board1 = [
        ['o','a','a','n'],
        ['e','t','a','e'],
        ['i','h','k','r'],
        ['i','f','l','v']
    ]
    words1 = ["oath","pea","eat","rain"]
    result1 = solution.findWords(board1, words1)
    print("输入:")
    print("board = [[o,a,a,n],[e,t,a,e],[i,h,k,r],[i,f,l,v]]")
    print("words = [oath,pea,eat,rain]")
    print(f"输出: {result1}")
    
    # 测试用例2
    board2 = [['a','b'],['c','d']]
    words2 = ["abcb"]
    result2 = solution.findWords(board2, words2)
    print("\n输入:")
    print("board = [[a,b],[c,d]]")
    print("words = [abcb]")
    print(f"输出: {result2}")


if __name__ == "__main__":
    main()
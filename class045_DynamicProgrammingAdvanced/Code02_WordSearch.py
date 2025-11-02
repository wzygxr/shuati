# 单词搜索（无法改成动态规划）
# 给定一个 m x n 二维字符网格 board 和一个字符串单词 word
# 如果 word 存在于网格中，返回 true ；否则，返回 false 。
# 单词必须按照字母顺序，通过相邻的单元格内的字母构成
# 其中"相邻"单元格是那些水平相邻或垂直相邻的单元格
# 同一个单元格内的字母不允许被重复使用
# 测试链接 : https://leetcode.cn/problems/word-search/
#
# 题目来源：LeetCode 79. 单词搜索
# 题目链接：https://leetcode.cn/problems/word-search/
# 时间复杂度：O(m*n*4^L) - 其中L为单词长度，最坏情况下需要从每个位置开始搜索
# 空间复杂度：O(m*n) - 递归栈深度和标记数组
# 是否最优解：是 - 回溯法是解决此类路径搜索问题的标准方法
#
# 解题思路：
# 1. 遍历网格中的每个位置作为起点
# 2. 从每个起点开始进行深度优先搜索（DFS）
# 3. 在搜索过程中使用回溯法避免重复使用同一单元格
# 4. 当找到完整单词时返回true，否则继续搜索
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界处理：处理空网格、空单词等特殊情况
# 3. 性能优化：剪枝策略提前终止无效搜索
# 4. 可测试性：提供完整的测试用例

class Code02_WordSearch:
    @staticmethod
    def exist(board, word):
        """
        判断单词是否存在于网格中
        :param board: 二维字符网格
        :param word:  要搜索的单词
        :return: 如果单词存在于网格中返回True，否则返回False
        
        时间复杂度：O(m*n*4^L) - 其中L为单词长度
        空间复杂度：O(m*n) - 递归栈深度
        """
        # 输入验证
        if not board or not board[0] or not word:
            return False
        
        # 获取网格维度
        n = len(board)
        m = len(board[0])
        
        # 遍历网格中的每个位置作为起点
        for i in range(n):
            for j in range(m):
                # 从当前位置开始搜索，如果找到则返回True
                if Code02_WordSearch._f(board, n, m, i, j, word, 0):
                    return True
        
        # 遍历完所有位置都没找到，返回False
        return False

    @staticmethod
    def _f(b, n, m, i, j, w, k):
        """
        从(i,j)位置开始搜索单词的第k个字符
        :param b: 二维字符网格
        :param n: 网格行数
        :param m: 网格列数
        :param i: 当前行坐标
        :param j: 当前列坐标
        :param w: 要搜索的单词
        :param k: 当前要匹配的字符索引
        :return: 如果能从当前位置开始找到完整单词返回True，否则返回False
        
        核心思想：深度优先搜索 + 回溯法
        1. 如果k等于单词长度，说明已经找到完整单词
        2. 如果越界或当前字符不匹配，返回False
        3. 标记当前位置已访问，防止重复使用
        4. 向四个方向递归搜索
        5. 回溯：恢复当前位置的字符
        """
        # 基础情况：已经匹配完整个单词
        if k == len(w):
            return True
        
        # 越界检查或字符不匹配
        if i < 0 or i >= n or j < 0 or j >= m or b[i][j] != w[k]:
            return False
        
        # 不越界且b[i][j] == w[k]，继续搜索
        # 标记当前位置已访问（用空字符串表示已访问）
        tmp = b[i][j]
        b[i][j] = ''
        
        # 向四个方向递归搜索
        ans = (Code02_WordSearch._f(b, n, m, i - 1, j, w, k + 1) or  # 上
               Code02_WordSearch._f(b, n, m, i + 1, j, w, k + 1) or  # 下
               Code02_WordSearch._f(b, n, m, i, j - 1, w, k + 1) or  # 左
               Code02_WordSearch._f(b, n, m, i, j + 1, w, k + 1))    # 右
        
        # 回溯：恢复当前位置的字符
        b[i][j] = tmp
        
        return ans

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    board1 = [
        ['A', 'B', 'C', 'E'],
        ['S', 'F', 'C', 'S'],
        ['A', 'D', 'E', 'E']
    ]
    word1 = "ABCCED"
    print("测试用例1:")
    print("网格:", board1)
    print("单词:", word1)
    print("是否存在:", Code02_WordSearch.exist(board1, word1))  # 应该输出True
    
    # 测试用例2
    word2 = "SEE"
    print("\n测试用例2:")
    print("单词:", word2)
    print("是否存在:", Code02_WordSearch.exist(board1, word2))  # 应该输出True
    
    # 测试用例3
    word3 = "ABCB"
    print("\n测试用例3:")
    print("单词:", word3)
    print("是否存在:", Code02_WordSearch.exist(board1, word3))  # 应该输出False
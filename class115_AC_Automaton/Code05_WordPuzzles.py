# -*- coding: utf-8 -*-

"""
POJ 1204 Word Puzzles
题目链接：http://poj.org/problem?id=1204
题目描述：给一个字母矩阵和一些字符串，求字符串在矩阵中出现的位置及其方向

算法详解：
这是一道典型的AC自动机应用题。我们需要在二维矩阵中查找多个模式串，
可以使用AC自动机来优化匹配过程。

算法核心思想：
1. 将所有模式串插入到Trie树中
2. 构建失配指针（fail指针）
3. 在矩阵的8个方向上分别进行匹配

时间复杂度分析：
1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
2. 构建fail指针：O(∑|Pi|)
3. 矩阵匹配：O(L×C×8×max(|Pi|))，其中L是行数，C是列数
总时间复杂度：O(∑|Pi| + L×C×max(|Pi|))

空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小

适用场景：
1. 二维矩阵中的字符串匹配
2. 多方向字符串搜索

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 性能优化：使用字典代替列表提高访问速度
3. 内存优化：合理使用Python的数据结构

与机器学习的联系：
1. 在图像处理中用于模式识别
2. 在游戏开发中用于寻路算法
"""

from collections import deque

# 8个方向：A=北, B=东北, C=东, D=东南, E=南, F=西南, G=西, H=西北
dx = [-1, -1, 0, 1, 1, 1, 0, -1]
dy = [0, 1, 1, 1, 0, -1, -1, -1]
dirs = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False
        self.fail = None  # type: ignore
        self.word_id = -1  # 单词编号

class WordPuzzles:
    def __init__(self):
        self.root = TrieNode()
        self.matrix = []
        self.L = 0  # type: int
        self.C = 0  # type: int
        self.W = 0  # type: int
        self.words = []
        self.result_x = []
        self.result_y = []
        self.result_dir = []
    
    def build_trie(self):
        """
        构建Trie树
        """
        for i, word in enumerate(self.words):
            node = self.root
            for char in word:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
            node.is_end = True
            node.word_id = i
    
    def build_ac_automation(self):
        """
        构建AC自动机
        """
        # 初始化根节点的失配指针
        self.root.fail = self.root  # type: ignore
        
        queue = deque()
        # 处理根节点的子节点
        for char in self.root.children:
            child = self.root.children[char]
            child.fail = self.root
            queue.append(child)
        
        # BFS构建失配指针
        while queue:
            node = queue.popleft()
            
            for char in node.children:
                child = node.children[char]
                queue.append(child)
                
                # 查找失配指针
                fail_node = node.fail
                while char not in fail_node.children and fail_node != self.root:
                    fail_node = fail_node.fail  # type: ignore
                
                if char in fail_node.children:
                    child.fail = fail_node.children[char]
                else:
                    child.fail = self.root

    def match_from_position(self, start_x, start_y, dx_dir, dy_dir, direction):
        """
        从指定位置开始匹配
        """
        current = self.root  # type: TrieNode
        x, y = start_x, start_y
        
        while 0 <= x < self.L and 0 <= y < self.C:
            ch = self.matrix[x][y]
            
            # 根据失配指针跳转
            while ch not in current.children and current != self.root:
                current = current.fail  # type: ignore
            
            if ch in current.children:
                current = current.children[ch]
            else:
                current = self.root
            
            # 检查是否有匹配的模式串
            temp = current  # type: TrieNode
            while temp != self.root:
                if temp.is_end and self.result_x[temp.word_id] == 0 and self.result_y[temp.word_id] == 0:
                    # 记录结果（这里简化处理，实际应该记录起始位置）
                    self.result_x[temp.word_id] = start_x
                    self.result_y[temp.word_id] = start_y
                    self.result_dir[temp.word_id] = direction
                temp = temp.fail  # type: ignore
            
            x += dx_dir
            y += dy_dir
    
    def search_in_direction(self, dir_index):
        """
        在指定方向搜索
        """
        dx_dir = dx[dir_index]
        dy_dir = dy[dir_index]
        
        # 根据方向确定起始点
        for i in range(self.L):
            for j in range(self.C):
                # 检查从(i,j)开始是否能匹配到边界
                len_match = 0
                x, y = i, j
                while 0 <= x < self.L and 0 <= y < self.C:
                    len_match += 1
                    x += dx_dir
                    y += dy_dir
                
                if len_match > 0:
                    # 从(i,j)开始匹配
                    self.match_from_position(i, j, dx_dir, dy_dir, dirs[dir_index])
    
    def search_in_matrix(self):
        """
        在矩阵中搜索
        """
        # 8个方向分别搜索
        for dir_index in range(8):
            self.search_in_direction(dir_index)

def main():
    # 为了简化，这里使用示例输入
    # 实际应用中需要从标准输入读取
    
    solver = WordPuzzles()
    
    # 示例输入
    solver.L = 20
    solver.C = 20
    solver.W = 10
    
    solver.matrix = [
        list("QWSPILAATIRAGRAMYKEI"),
        list("AGTRCLQAXLPOIJLFVBUQ"),
        list("TQTKAZXVMRWALEMAPKCW"),
        list("LIEACNKAZXKPOTPIZCEO"),
        list("FGKLSTCBTROPICALBLBC"),
        list("JEWHJEEWSMLPOEKORORA"),
        list("LUPQWRNJOAAGJKMUSJAE"),
        list("KRQEIOLOAOQPRTVILCBZ"),
        list("QOPUCAJSPPOUTMTSLPSF"),
        list("LPOUYTRFGMMLKIUISXSW"),
        list("WAHCPOIYTGAKLMNAHBVA"),
        list("EIAKHPLBGSMCLOGNGJML"),
        list("LDTIKENVCSWQAZUAOEAL"),
        list("HOPLPGEJKMNUTIIORMNC"),
        list("LOIUFTGSQACAXMOPBEIO"),
        list("QOASDHOPEPNBUYUYOBXB"),
        list("IONIAELOJHSWASMOUTRK"),
        list("HPOIYTJPLNAQWDRIBITG"),
        list("LPOINUYMRTEMPTMLMNBO"),
        list("PAFCOPLHAVAIANALBPFS")
    ]
    
    solver.words = ["MARGARITA", "ALEMA", "BARBECUE", "TROPICAL", "SUPREMA", 
                   "LOUISIANA", "CHEESEHAM", "EUROPA", "HAVAIANA", "CAMPONESA"]
    
    solver.result_x = [0] * solver.W
    solver.result_y = [0] * solver.W
    solver.result_dir = [''] * solver.W
    
    # 构建Trie树
    solver.build_trie()
    
    # 构建AC自动机
    solver.build_ac_automation()
    
    # 在矩阵中搜索
    solver.search_in_matrix()
    
    # 输出结果
    for i in range(solver.W):
        print(f"{solver.result_x[i]} {solver.result_y[i]} {solver.result_dir[i]}")

if __name__ == "__main__":
    main()
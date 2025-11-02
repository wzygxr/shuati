"""
POJ 1056 IMMEDIATE DECODABILITY

题目描述：
判断一组二进制编码是否具有即时可解码性。如果任何一个编码不是其他编码的前缀，则称这组编码是即时可解码的。

解题思路：
1. 使用Trie树存储所有二进制编码
2. 在插入过程中检查是否存在前缀关系
3. 如果在插入一个编码时，发现路径上有已经标记为完整编码的节点，或者插入完成后当前节点有子节点，
   则说明不具有即时可解码性

时间复杂度：O(N*M)，其中N是编码数量，M是平均编码长度
空间复杂度：O(N*M)
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典，对应0和1
        self.is_end = False  # 标记是否为一个完整编码的结尾

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert(self, code):
        """
        向Trie树中插入一个二进制编码，并检查是否存在前缀关系
        :param code: 要插入的二进制编码
        :return: 如果存在前缀关系返回True，否则返回False
        """
        if not code:
            return False
        
        node = self.root
        for digit in code:
            # 如果子节点不存在，创建新节点
            if digit not in node.children:
                node.children[digit] = TrieNode()
            
            node = node.children[digit]
            
            # 如果当前节点已经是某个完整编码的结尾，说明当前编码是另一个编码的前缀
            if node.is_end:
                return True
        
        # 标记当前节点为完整编码的结尾
        node.is_end = True
        
        # 检查当前节点是否有子节点，如果有说明存在前缀关系
        if node.children:
            return True
        
        return False

def main():
    set_number = 1  # 组号
    
    lines = []
    for line in sys.stdin:
        lines.append(line.strip())
    
    i = 0
    while i < len(lines):
        # 初始化Trie树
        trie = Trie()
        
        decodable = True
        
        # 读取一组编码，直到遇到分隔符"9"
        while i < len(lines) and lines[i] != "9":
            code = lines[i]
            if trie.insert(code):
                decodable = False
            i += 1
        
        # 输出结果
        if decodable:
            print(f"Set {set_number} is immediately decodable")
        else:
            print(f"Set {set_number} is not immediately decodable")
        
        set_number += 1  # 组号递增
        i += 1  # 跳过分隔符"9"

if __name__ == "__main__":
    main()
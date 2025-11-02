"""
HDU 1671 Phone List

题目描述：
给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
如果存在输出NO，否则输出YES。

解题思路：
1. 使用Trie树存储所有电话号码
2. 在插入过程中检查是否存在前缀关系
3. 如果在插入一个号码时，发现路径上有已经标记为完整号码的节点，或者插入完成后当前节点有子节点，
   则说明存在前缀关系

时间复杂度：O(N*M)，其中N是电话号码数量，M是平均号码长度
空间复杂度：O(N*M)
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典，对应0-9数字
        self.is_end = False  # 标记是否为一个完整号码的结尾

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert(self, number):
        """
        向Trie树中插入一个电话号码，并检查是否存在前缀关系
        :param number: 要插入的电话号码
        :return: 如果存在前缀关系返回True，否则返回False
        """
        if not number:
            return False
        
        node = self.root
        for digit in number:
            # 如果子节点不存在，创建新节点
            if digit not in node.children:
                node.children[digit] = TrieNode()
            
            node = node.children[digit]
            
            # 如果当前节点已经是某个完整号码的结尾，说明当前号码是另一个号码的前缀
            if node.is_end:
                return True
        
        # 标记当前节点为完整号码的结尾
        node.is_end = True
        
        # 检查当前节点是否有子节点，如果有说明存在前缀关系
        if node.children:
            return True
        
        return False

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    idx = 0
    t = int(input_lines[idx])  # 测试用例数量
    idx += 1
    
    for _ in range(t):
        n = int(input_lines[idx])  # 电话号码数量
        idx += 1
        
        # 初始化Trie树
        trie = Trie()
        
        numbers = []
        for j in range(n):
            numbers.append(input_lines[idx])
            idx += 1
        
        # 按长度排序，优先处理短号码
        numbers.sort(key=len)
        
        consistent = True
        for number in numbers:
            if trie.insert(number):
                consistent = False
                break
        
        if consistent:
            print("YES")
        else:
            print("NO")

if __name__ == "__main__":
    main()
"""
ZOJ 3430 Detect the Virus

题目描述：
使用Trie树检测文件中的病毒代码。给定一些病毒代码模式和一些文件，判断每个文件是否包含病毒代码。

解题思路：
1. 使用AC自动机构建所有病毒代码模式的匹配机
2. 对每个文件进行匹配，判断是否包含病毒代码
3. 由于这是Trie专题，我们使用Trie树来实现简单的模式匹配

时间复杂度：O(∑len(patterns) + ∑len(files))
空间复杂度：O(∑len(patterns))
"""

import sys
import base64
from collections import deque

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典
        self.is_end = False  # 标记是否为一个模式的结尾
        self.fail = None    # 失配指针（用于AC自动机）

class ACAutomaton:
    """AC自动机类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert(self, pattern):
        """
        向Trie树中插入一个模式
        :param pattern: 要插入的模式（字节数组）
        """
        node = self.root
        for byte_val in pattern:
            if byte_val not in node.children:
                node.children[byte_val] = TrieNode()
            node = node.children[byte_val]
        node.is_end = True  # 标记为模式的结尾
    
    def build_fail_pointer(self):
        """构建失配指针（AC自动机的一部分）"""
        queue = deque()
        
        # 初始化根节点的失配指针
        for byte_val in self.root.children:
            self.root.children[byte_val].fail = self.root
            queue.append(self.root.children[byte_val])
        
        # 补全根节点缺失的子节点
        for i in range(256):
            if i not in self.root.children:
                self.root.children[i] = self.root
        
        # BFS构建失配指针
        while queue:
            current = queue.popleft()
            
            for byte_val in current.children:
                fail_node = current.fail
                
                while fail_node is not None and byte_val not in fail_node.children:
                    fail_node = fail_node.fail
                
                if fail_node is not None and byte_val in fail_node.children:
                    current.children[byte_val].fail = fail_node.children[byte_val]
                else:
                    current.children[byte_val].fail = self.root
                
                # 如果失配节点是模式结尾，则当前节点也是模式结尾
                if current.children[byte_val].fail.is_end:
                    current.children[byte_val].is_end = True
                
                queue.append(current.children[byte_val])
    
    def search(self, text):
        """
        在文本中查找所有模式
        :param text: 要搜索的文本（字节数组）
        :return: 是否找到任何模式
        """
        node = self.root
        
        for byte_val in text:
            while node is not None and node != self.root and byte_val not in node.children:
                node = node.fail
            
            if node is not None and byte_val in node.children:
                node = node.children[byte_val]
            else:
                node = self.root
            
            # 如果找到模式结尾，返回True
            if node is not None and node.is_end:
                return True
        
        return False

def decode_base64(base64_str):
    """
    解码Base64字符串为字节数组
    :param base64_str: Base64编码的字符串
    :return: 解码后的字节数组
    """
    return base64.b64decode(base64_str)

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    idx = 0
    while idx < len(input_lines):
        if not input_lines[idx]:
            idx += 1
            continue
            
        n = int(input_lines[idx])  # 病毒代码模式数量
        idx += 1
        
        # 创建AC自动机
        ac = ACAutomaton()
        
        # 读取所有病毒代码模式
        for i in range(n):
            base64_pattern = input_lines[idx]
            pattern = decode_base64(base64_pattern)
            ac.insert(pattern)
            idx += 1
        
        # 构建失配指针
        ac.build_fail_pointer()
        
        m = int(input_lines[idx])  # 文件数量
        idx += 1
        
        # 处理每个文件
        for i in range(m):
            base64_file = input_lines[idx]
            file_data = decode_base64(base64_file)
            
            # 搜索病毒代码
            if ac.search(file_data):
                print("YES")
            else:
                print("NO")
            idx += 1
        
        # 输出空行分隔不同测试用例
        print()

if __name__ == "__main__":
    main()
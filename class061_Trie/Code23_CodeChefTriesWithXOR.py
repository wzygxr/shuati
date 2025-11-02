"""
CodeChef Tries with XOR

题目描述：
给定一个数组，找出数组中任意两个数的最大异或值。

解题思路：
1. 使用Trie树存储所有数字的二进制表示
2. 对于每个数字，在Trie树中查找能产生最大异或值的路径
3. 贪心策略：从最高位开始，尽可能选择与当前位相反的位

时间复杂度：O(N*32) = O(N)，其中N是数组长度，32是整数的位数
空间复杂度：O(N*32) = O(N)
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典，对应0和1

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert(self, num):
        """
        向Trie树中插入一个数字的二进制表示
        :param num: 要插入的数字
        """
        node = self.root
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            if bit not in node.children:
                node.children[bit] = TrieNode()
            node = node.children[bit]
    
    def find_max_xor(self, num):
        """
        查找与给定数字异或能得到最大值的数字
        :param num: 给定的数字
        :return: 最大异或值
        """
        node = self.root
        result = 0
        
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            opposite_bit = 1 - bit  # 相反位
            
            # 贪心策略：尽可能选择与当前位相反的位
            if opposite_bit in node.children:
                result |= (1 << i)  # 设置结果的第i位为1
                node = node.children[opposite_bit]
            else:
                node = node.children[bit]
        
        return result

def find_maximum_xor(nums):
    """
    找出数组中任意两个数的最大异或值
    :param nums: 输入数组
    :return: 最大异或值
    """
    trie = Trie()
    
    # 将所有数字插入Trie树
    for num in nums:
        trie.insert(num)
    
    max_xor = 0
    # 对于每个数字，查找能产生最大异或值的数字
    for num in nums:
        max_xor = max(max_xor, num ^ trie.find_max_xor(num))
    
    return max_xor

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    n = int(input_lines[0])  # 数组长度
    nums = list(map(int, input_lines[1].split()))  # 数组元素
    
    # 计算并输出最大异或值
    result = find_maximum_xor(nums)
    print(result)

if __name__ == "__main__":
    main()
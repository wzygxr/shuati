"""
CodeChef XRQRS - Xor Queries

题目描述：
实现一个数据结构，支持以下操作：
1. 向集合中添加一个数字
2. 查询集合中与给定数字异或值最大的数字
3. 查询集合中与给定数字异或值最小的数字
4. 查询集合中与给定数字异或值小于等于给定值的数字数量
5. 删除指定位置插入的数字

解题思路：
1. 使用前缀树存储所有数字的二进制表示
2. 对于每种查询操作，使用相应的策略在前缀树中查找结果

时间复杂度：
- 插入：O(32)
- 查询最大/最小异或值：O(32)
- 查询数量：O(32)
- 删除：O(32)
空间复杂度：O(N*32)
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典，对应0和1
        self.count = 0      # 经过该节点的数字数量
        self.indices = []   # 存储经过该节点的数字索引

class XorQueries:
    """Xor查询类"""
    
    def __init__(self):
        """初始化数据结构"""
        self.root = TrieNode()
        self.numbers = []   # 存储所有数字
        self.index = 0      # 当前数字索引
    
    def insert(self, num, idx):
        """
        向Trie树中插入一个数字
        :param num: 要插入的数字
        :param idx: 数字的索引
        """
        node = self.root
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            if bit not in node.children:
                node.children[bit] = TrieNode()
            node = node.children[bit]
            node.count += 1  # 增加经过该节点的数字数量
            node.indices.append(idx)  # 记录数字索引
    
    def delete(self, num, idx):
        """
        从Trie树中删除一个数字
        :param num: 要删除的数字
        :param idx: 数字的索引
        """
        node = self.root
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            if bit in node.children:
                node = node.children[bit]
                node.count -= 1  # 减少经过该节点的数字数量
                if idx in node.indices:
                    node.indices.remove(idx)  # 移除数字索引
            else:
                break
    
    def max_xor(self, num):
        """
        查询与给定数字异或值最大的数字
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
            if opposite_bit in node.children and node.children[opposite_bit].count > 0:
                result |= (1 << i)  # 设置结果的第i位为1
                node = node.children[opposite_bit]
            else:
                node = node.children[bit]
        
        return result
    
    def min_xor(self, num):
        """
        查询与给定数字异或值最小的数字
        :param num: 给定的数字
        :return: 最小异或值
        """
        node = self.root
        result = 0
        
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            
            # 贪心策略：尽可能选择与当前位相同的位
            if bit in node.children and node.children[bit].count > 0:
                node = node.children[bit]
            else:
                result |= (1 << i)  # 设置结果的第i位为1
                node = node.children[1 - bit]
        
        return result
    
    def count_xor_less_than_k(self, num, k):
        """
        查询与给定数字异或值小于等于k的数字数量
        :param num: 给定的数字
        :param k: 比较值
        :return: 满足条件的数字数量
        """
        node = self.root
        result = 0
        
        # 从最高位开始处理
        for i in range(31, -1, -1):
            if not node:
                break
            
            num_bit = (num >> i) & 1  # num的第i位
            k_bit = (k >> i) & 1      # k的第i位
            
            if k_bit == 1:
                # 如果k的第i位是1，那么异或值为0的子树都满足条件
                if num_bit in node.children:
                    result += node.children[num_bit].count
                # 继续处理异或值为1的子树
                node = node.children.get(1 - num_bit)
            else:
                # 如果k的第i位是0，只能继续处理异或值为0的子树
                node = node.children.get(num_bit)
        
        return result

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    q = int(input_lines[0])  # 查询数量
    
    xor_queries = XorQueries()
    
    for i in range(1, q + 1):
        query = list(map(int, input_lines[i].split()))
        type_ = query[0]
        
        if type_ == 0:  # 添加数字
            x = query[1]
            xor_queries.numbers.append(x)
            xor_queries.insert(x, xor_queries.index)
            xor_queries.index += 1
            
        elif type_ == 1:  # 查询最大异或值
            y = query[1]
            max_result = xor_queries.max_xor(y)
            print(max_result)
            
        elif type_ == 2:  # 查询最小异或值
            z = query[1]
            min_result = xor_queries.min_xor(z)
            print(min_result)
            
        elif type_ == 3:  # 查询异或值小于等于k的数字数量
            a = query[1]
            k = query[2]
            count_result = xor_queries.count_xor_less_than_k(a, k)
            print(count_result)
            
        elif type_ == 4:  # 删除指定位置插入的数字
            p = query[1]
            num_to_delete = xor_queries.numbers[p]
            xor_queries.delete(num_to_delete, p)

if __name__ == "__main__":
    main()
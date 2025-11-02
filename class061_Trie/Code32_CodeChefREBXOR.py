"""
CodeChef REBXOR - Nikitosh and xor

题目描述：
给定一个长度为N的数组A，要求将数组分成两个非空的连续子数组，使得这两个子数组的异或和的异或值最大。

解题思路：
这是一个经典的01Trie应用问题。我们可以使用以下方法：
1. 预处理前缀异或数组
2. 对于每个分割点，计算左边子数组的最大异或值和右边子数组的最大异或值
3. 使用01Trie来高效计算最大异或值

具体步骤：
1. 计算前缀异或数组prefix_xor，其中prefix_xor[i]表示A[0]到A[i-1]的异或和
2. 对于每个位置i，计算从A[0]到A[i-1]中某个子数组的最大异或值，存储在left_max数组中
3. 对于每个位置i，计算从A[i]到A[n-1]中某个子数组的最大异或值，存储在right_max数组中
4. 枚举所有分割点，计算left_max[i] XOR right_max[i]的最大值

时间复杂度：O(N * log(max_value))
空间复杂度：O(N * log(max_value))
"""

import sys

class TrieNode:
    """01Trie树节点类"""
    def __init__(self):
        self.children = [None, None]  # 01Trie只有0和1两个子节点
        self.count = 0  # 经过该节点的数字数量

class Trie:
    """01Trie树类"""
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, num):
        """
        向01Trie中插入一个数字
        :param num: 要插入的数字
        """
        node = self.root
        # 从最高位开始处理（假设数字不超过30位）
        for i in range(30, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值（0或1）
            if node.children[bit] is None:
                node.children[bit] = TrieNode()
            node = node.children[bit]
            node.count += 1
    
    def query_max_xor(self, num):
        """
        查询与给定数字异或值最大的数字的异或结果
        :param num: 给定的数字
        :return: 最大异或值
        """
        if self.root.children[0] is None and self.root.children[1] is None:
            return 0
        
        node = self.root
        result = 0
        
        # 从最高位开始处理，贪心地选择能使异或结果最大的路径
        for i in range(30, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            # 贪心策略：优先选择与当前位相反的路径（使异或结果为1）
            if node.children[1 - bit] is not None and node.children[1 - bit].count > 0:
                result |= (1 << i)  # 设置第i位为1
                node = node.children[1 - bit]
            else:
                node = node.children[bit]
        
        return result
    
    def delete(self, num):
        """
        从01Trie中删除一个数字
        :param num: 要删除的数字
        """
        node = self.root
        # 从最高位开始处理
        for i in range(30, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            if node.children[bit] is None:
                return  # 数字不存在
            node = node.children[bit]
            node.count -= 1

def solve_rebxor(arr):
    """
    解决REBXOR问题
    :param arr: 输入数组
    :return: 最大异或值
    """
    n = len(arr)
    if n < 2:
        return 0
    
    # 计算前缀异或数组
    prefix_xor = [0] * (n + 1)
    for i in range(n):
        prefix_xor[i + 1] = prefix_xor[i] ^ arr[i]
    
    # 计算left_max数组：left_max[i]表示前i个元素中某个子数组的最大异或值
    left_max = [0] * (n + 1)
    trie = Trie()
    trie.insert(0)  # 插入0，处理从第一个元素开始的子数组
    
    for i in range(1, n):
        # 查询以第i个元素结尾的子数组的最大异或值
        left_max[i] = max(left_max[i - 1], trie.query_max_xor(prefix_xor[i]))
        # 将prefix_xor[i]插入Trie
        trie.insert(prefix_xor[i])
    
    # 计算right_max数组：right_max[i]表示从第i个元素到最后一个元素中某个子数组的最大异或值
    right_max = [0] * (n + 1)
    trie = Trie()
    trie.insert(0)  # 插入0，处理以最后一个元素结尾的子数组
    
    for i in range(n - 1, 0, -1):
        # 查询以第i个元素开头的子数组的最大异或值
        right_max[i] = max(right_max[i + 1], trie.query_max_xor(prefix_xor[i]))
        # 将prefix_xor[i]插入Trie
        trie.insert(prefix_xor[i])
    
    # 枚举所有分割点，计算最大异或值
    max_xor = 0
    for i in range(1, n):
        max_xor = max(max_xor, left_max[i] + right_max[i + 1])
    
    return max_xor

def main():
    """主函数"""
    # 读取输入
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    if not input_lines:
        return
    
    # 解析输入
    n = int(input_lines[0])
    arr = list(map(int, input_lines[1].split()))
    
    # 求解并输出结果
    result = solve_rebxor(arr)
    print(result)

if __name__ == "__main__":
    main()
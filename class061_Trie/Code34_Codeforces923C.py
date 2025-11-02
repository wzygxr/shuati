"""
Codeforces 923C Perfect Security

题目描述：
Alice有一个重要的消息M，由一些非负整数组成，她想对Eve保密。
Alice知道唯一理论上安全的密码是一次性密码本。
Alice生成一个与消息长度相同的随机密钥K。
Alice计算消息和密钥的按位异或（A[i] = M[i] XOR K[i]）并存储这个加密消息A。
Alice将密钥K发送给Bob并删除自己的副本。
Bob随机排列了密钥后存储。
一年后，Alice想要解密她的消息。由于Bob随机排列了密钥，消息永远丢失了。
Bob想从消息中挽救至少一些信息。他要求你帮助。
给定加密消息A和排列后的密钥P，找出字典序最小的消息O，使得存在一个排列π，使得O[i] = A[i] XOR P[π[i]]。

解题思路：
这是一个经典的01Trie应用问题。我们可以使用贪心策略：
1. 对于消息中的每个元素，我们希望找到一个密钥元素，使得它们的异或值尽可能小
2. 为了得到字典序最小的消息，我们应该从左到右依次确定每个位置的值
3. 对于每个位置，我们在剩余的密钥中选择一个与当前加密值异或结果最小的密钥
4. 使用01Trie来高效地找到异或结果最小的密钥

具体步骤：
1. 将所有密钥插入到01Trie中，每个节点记录经过该节点的密钥数量
2. 对于每个加密值A[i]，在01Trie中查找与它异或值最小的密钥
3. 从Trie中删除选中的密钥
4. 计算异或值作为结果

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
    
    def query_min_xor(self, num):
        """
        查询与给定数字异或值最小的数字的异或结果
        :param num: 给定的数字
        :return: 最小异或值
        """
        if self.root.children[0] is None and self.root.children[1] is None:
            return 0
        
        node = self.root
        result = 0
        
        # 从最高位开始处理，贪心地选择能使异或结果最小的路径
        for i in range(30, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            # 贪心策略：优先选择与当前位相同的路径（使异或结果为0）
            if node.children[bit] is not None and node.children[bit].count > 0:
                node = node.children[bit]
            else:
                result |= (1 << i)  # 设置第i位为1
                node = node.children[1 - bit]
        
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

def solve_perfect_security(encrypted, permuted_key):
    """
    解决Perfect Security问题
    :param encrypted: 加密消息数组
    :param permuted_key: 排列后的密钥数组
    :return: 字典序最小的消息数组
    """
    n = len(encrypted)
    if n == 0:
        return []
    
    # 构建01Trie
    trie = Trie()
    for key in permuted_key:
        trie.insert(key)
    
    # 计算字典序最小的消息
    result = []
    for i in range(n):
        # 查找与当前加密值异或结果最小的密钥
        min_xor = trie.query_min_xor(encrypted[i])
        result.append(min_xor)
        
        # 从Trie中删除使用的密钥
        # 为了找到使用的密钥，我们需要反向计算
        # key = encrypted[i] XOR min_xor
        key = encrypted[i] ^ min_xor
        trie.delete(key)
    
    return result

def main():
    """主函数"""
    # 读取输入
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    if len(input_lines) < 3:
        return
    
    # 解析输入
    n = int(input_lines[0])
    encrypted = list(map(int, input_lines[1].split()))
    permuted_key = list(map(int, input_lines[2].split()))
    
    # 求解并输出结果
    result = solve_perfect_security(encrypted, permuted_key)
    print(' '.join(map(str, result)))

if __name__ == "__main__":
    main()
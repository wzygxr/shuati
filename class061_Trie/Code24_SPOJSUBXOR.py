"""
SPOJ SUBXOR

题目描述：
给定一个数组和一个值k，统计有多少个子数组的异或值小于k。

解题思路：
1. 使用前缀异或和将问题转化为：对于每个位置i，统计有多少个j<i满足prefix[i] ^ prefix[j] < k
2. 使用Trie树存储所有prefix[j]的二进制表示
3. 对于每个prefix[i]，在Trie树中查找有多少个prefix[j]满足prefix[i] ^ prefix[j] < k
4. 贪心策略：从最高位开始比较，如果当前位异或值小于k的对应位，则加上该子树的所有节点数

时间复杂度：O(N*32) = O(N)，其中N是数组长度，32是整数的位数
空间复杂度：O(N*32) = O(N)
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典，对应0和1
        self.count = 0      # 经过该节点的数字数量

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
            node.count += 1  # 增加经过该节点的数字数量
    
    def count_less_than_k(self, num, k):
        """
        查找有多少个数字与给定数字异或值小于k
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

def count_subarrays_with_xor_less_than_k(nums, k):
    """
    统计有多少个子数组的异或值小于k
    :param nums: 输入数组
    :param k: 比较值
    :return: 满足条件的子数组数量
    """
    trie = Trie()
    
    result = 0
    prefix_xor = 0
    
    # 插入前缀异或和0（表示空前缀）
    trie.insert(0)
    
    # 遍历数组
    for num in nums:
        prefix_xor ^= num  # 计算前缀异或和
        
        # 查找有多少个之前的前缀异或和与当前前缀异或和的异或值小于k
        result += trie.count_less_than_k(prefix_xor, k)
        
        # 将当前前缀异或和插入Trie树
        trie.insert(prefix_xor)
    
    return result

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    idx = 0
    t = int(input_lines[idx])  # 测试用例数量
    idx += 1
    
    for _ in range(t):
        n, k = map(int, input_lines[idx].split())  # 数组长度和比较值
        idx += 1
        nums = list(map(int, input_lines[idx].split()))  # 数组元素
        idx += 1
        
        # 计算并输出结果
        result = count_subarrays_with_xor_less_than_k(nums, k)
        print(result)

if __name__ == "__main__":
    main()
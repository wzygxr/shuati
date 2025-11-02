# 数组中两个数的最大异或值
# 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0<=i<=j<=n
# 1 <= nums.length <= 2 * 10^5
# 0 <= nums[i] <= 2^31 - 1
# 测试链接 : https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
#
# 相关题目扩展：
# 1. LeetCode 421. 数组中两个数的最大异或值 (本题)
# 2. LeetCode 1310. 子数组异或查询
# 3. LeetCode 1707. 与数组中元素的最大异或值
# 4. LeetCode 1803. 统计异或值在范围内的数对有多少
# 5. LintCode 1490. 最大异或值
# 6. 牛客网 NC152. 数组中两个数的最大异或值
# 7. HackerRank - XOR Maximization
# 8. CodeChef - MAXXOR
# 9. SPOJ - XORX
# 10. AtCoder - Maximum XOR

class TrieNode:
    """
    前缀树节点类
    """
    def __init__(self):
        # 子节点字典，键为0或1，值为TrieNode
        self.children = {}
        
class Trie:
    """
    前缀树类
    """
    def __init__(self):
        # 根节点
        self.root = TrieNode()
    
    def insert(self, num):
        """
        向前缀树中插入数字
        
        时间复杂度：O(log(max))，其中max是数组中的最大值
        空间复杂度：O(log(max))，最坏情况下需要创建新节点
        
        :param num: 待插入的数字
        """
        node = self.root
        # 从最高位开始插入
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            if bit not in node.children:
                node.children[bit] = TrieNode()
            node = node.children[bit]
    
    def max_xor(self, num):
        """
        查找与num异或能得到最大值的数字
        
        时间复杂度：O(log(max))，其中max是数组中的最大值
        空间复杂度：O(1)
        
        :param num: 待查询的数字
        :return: 最大异或值
        """
        node = self.root
        result = 0
        
        # 从最高位开始查找
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            # 尝试选择相反的位以获得最大异或值
            opposite_bit = 1 - bit
            if opposite_bit in node.children:
                result |= (1 << i)
                node = node.children[opposite_bit]
            else:
                node = node.children[bit]
        
        return result

def find_maximum_xor1(nums):
    """
    使用前缀树查找最大异或值
    
    算法思路：
    1. 构建前缀树，将所有数字的二进制表示插入前缀树
    2. 对于每个数字，在前缀树中查找能产生最大异或值的数字
    
    时间复杂度分析：
    - 构建前缀树：O(n * log(max))，其中n是数组长度，max是数组中的最大值
    - 查询过程：O(n * log(max))
    - 总体时间复杂度：O(n * log(max))
    
    空间复杂度分析：
    - 前缀树空间：O(n * log(max))，用于存储所有数字的二进制表示
    - 总体空间复杂度：O(n * log(max))
    
    是否最优解：是
    理由：使用前缀树可以在线性时间内查找最大异或值，避免了暴力枚举
    
    工程化考虑：
    1. 异常处理：输入为空或数组长度小于2的情况
    2. 边界情况：数组中所有数字相同的情况
    3. 极端输入：大量数字或数字很大的情况
    4. 鲁棒性：处理负数和0的情况
    
    语言特性差异：
    Java：使用二维数组实现前缀树，利用位运算提高效率
    C++：可使用指针实现前缀树节点，更节省空间
    Python：可使用字典实现前缀树，代码更简洁
    
    :param nums: 整数数组
    :return: 最大异或值
    """
    trie = Trie()
    
    # 将所有数字插入前缀树
    for num in nums:
        trie.insert(num)
    
    # 查找最大异或值
    max_result = 0
    for num in nums:
        max_result = max(max_result, trie.max_xor(num) ^ num)
    
    return max_result

def find_maximum_xor2(nums):
    """
    使用哈希表查找最大异或值
    
    算法思路：
    1. 从最高位开始，逐位尝试构建最大异或值
    2. 对于每一位，尝试将其设为1，检查是否存在两个数字异或能得到该值
    
    时间复杂度分析：
    - 查询过程：O(n * log(max))，其中n是数组长度，max是数组中的最大值
    - 总体时间复杂度：O(n * log(max))
    
    空间复杂度分析：
    - 哈希表空间：O(n)，用于存储数字
    - 总体空间复杂度：O(n)
    
    是否最优解：是
    理由：使用哈希表可以在线性时间内查找最大异或值，避免了构建前缀树
    
    工程化考虑：
    1. 异常处理：输入为空或数组长度小于2的情况
    2. 边界情况：数组中所有数字相同的情况
    3. 极端输入：大量数字或数字很大的情况
    4. 鲁棒性：处理负数和0的情况
    
    语言特性差异：
    Java：使用HashSet存储数字，利用位运算提高效率
    C++：可使用unordered_set存储数字，性能更优
    Python：可使用set存储数字，代码更简洁
    
    :param nums: 整数数组
    :return: 最大异或值
    """
    max_val = max(nums)
    max_result = 0
    num_set = set()
    
    # 从最高位开始尝试
    for i in range(31 - max_val.bit_length(), -1, -1):
        # 当前目标异或值
        target = max_result | (1 << i)
        num_set.clear()
        
        for num in nums:
            # 保留高位，低位清零
            masked_num = num >> i << i
            num_set.add(masked_num)
            
            # 检查是否存在两个数字异或能得到target
            if target ^ masked_num in num_set:
                max_result = target
                break
    
    return max_result
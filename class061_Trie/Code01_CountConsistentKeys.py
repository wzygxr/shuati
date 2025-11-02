"""
牛客网接头密钥系统

题目描述：
牛牛和他的朋友们约定了一套接头密匙系统，用于确认彼此身份。
密匙由一组数字序列表示，两个密钥被认为是一致的，如果满足以下条件：
1. 密匙 b 的长度不超过密钥 a 的长度。
2. 对于任意 0 <= i < length(b)，有b[i+1] - b[i] == a[i+1] - a[i]

现在给定了m个密钥 b 的数组，以及n个密钥 a 的数组，
请你返回一个长度为 m 的结果数组 ans，表示每个密钥b都有多少一致的密钥。

约束条件：
- 数组 a 和数组 b 中的元素个数均不超过 10^5
- 1 <= m, n <= 1000

测试链接：https://www.nowcoder.com/practice/c552d3b4dfda49ccb883a6371d9a6932

算法思路：
1. 将数组a中每个密钥转换为差值序列字符串，并存储在前缀树中
2. 对于数组b中每个密钥，计算其差值序列字符串，然后在前缀树中查找匹配的数量

时间复杂度分析：
- 构建前缀树：O(∑len(a[i]))，其中∑len(a[i])是数组a中所有密钥的长度之和
- 查询过程：O(∑len(b[i]))，其中∑len(b[i])是数组b中所有密钥的长度之和
- 总体时间复杂度：O(∑len(a[i]) + ∑len(b[i]))

空间复杂度分析：
- 前缀树空间：O(∑len(a[i]))，用于存储所有差值序列
- 字符串空间：O(max(len(a[i]), len(b[i])))，用于构建差值序列字符串
- 总体空间复杂度：O(∑len(a[i]) + max(len(a[i]), len(b[i])))

是否最优解：是
理由：使用前缀树可以高效地存储和查询字符串前缀，避免了重复计算

工程化考虑：
1. 异常处理：输入为空或密钥长度小于2的情况
2. 边界情况：密钥长度为1时，差值序列为空
3. 极端输入：大量密钥或密钥很长的情况
4. 鲁棒性：处理负数差值和特殊字符

语言特性差异：
Java：使用StringBuilder提高字符串拼接效率
C++：可使用string和vector实现类似功能
Python：可使用list和join方法实现字符串拼接

相关题目扩展：
1. LeetCode 208. 实现 Trie (前缀树)
2. LeetCode 212. 单词搜索 II
3. LintCode 1320. 包含重复值 II
4. LeetCode 438. 找到字符串中所有字母异位词
5. LeetCode 567. 字符串的排列
6. 牛客网 NC105. 二分查找-II
7. 牛客网 NC138. 字符串匹配
8. HackerRank - Strings: Making Anagrams
9. CodeChef - ANAGRAMS
10. SPOJ - ANGRAM
"""

class TrieNode:
    """
    前缀树节点类
    
    节点属性：
    - children: 子节点字典，键为字符，值为TrieNode对象
    - count: 经过该节点的字符串数量
    
    设计思路：
    1. 使用字典存储子节点，支持动态扩展字符集
    2. 维护count计数器，用于快速查询前缀匹配数量
    """
    def __init__(self):
        # 子节点字典
        self.children = {}
        # 经过该节点的字符串数量
        self.count = 0

class Trie:
    """
    前缀树类
    
    核心功能：
    1. 插入字符串到前缀树
    2. 查询指定前缀的匹配字符串数量
    3. 清空前缀树结构
    
    设计特点：
    1. 使用TrieNode构建树形结构
    2. 支持动态字符集（通过字典存储子节点）
    3. 维护每个节点的计数器，支持快速前缀匹配查询
    """
    def __init__(self):
        # 根节点
        self.root = TrieNode()
    
    def insert(self, word):
        """
        向前缀树中插入字符串
        
        算法步骤：
        1. 从根节点开始遍历字符串
        2. 对于每个字符：
           a. 如果子节点不存在，则创建新节点
           b. 移动到子节点
           c. 增加节点的计数器
        3. 遍历完成后，字符串插入完成
        
        时间复杂度：O(len(word))，其中len(word)是字符串长度
        空间复杂度：O(len(word))，最坏情况下需要创建新节点
        
        :param word: 待插入的字符串
        """
        node = self.root
        node.count += 1
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.count += 1
    
    def count_prefix(self, prefix):
        """
        查询前缀树中以prefix为前缀的字符串数量
        
        算法步骤：
        1. 从根节点开始遍历前缀字符串
        2. 对于每个字符：
           a. 如果子节点不存在，返回0（无匹配）
           b. 移动到子节点
        3. 遍历完成后，返回当前节点的计数器值
        
        时间复杂度：O(len(prefix))，其中len(prefix)是前缀长度
        空间复杂度：O(1)
        
        :param prefix: 前缀字符串
        :return: 匹配的字符串数量
        """
        node = self.root
        for char in prefix:
            if char not in node.children:
                return 0
            node = node.children[char]
        return node.count
    
    def clear(self):
        """
        清空前缀树
        
        算法步骤：
        1. 创建新的根节点
        2. 原有节点会被Python垃圾回收机制自动回收
        
        时间复杂度：O(1)，创建新节点的时间
        空间复杂度：O(1)
        """
        self.root = TrieNode()

def count_consistent_keys(b, a):
    """
    计算一致密钥的数量
    
    算法步骤详解：
    1. 创建前缀树实例
    2. 遍历数组a中的每个密钥：
       a. 计算密钥的差值序列（相邻元素的差值）
       b. 将差值序列转换为字符串形式（用#分隔，处理负数）
       c. 将差值序列字符串插入前缀树
    3. 遍历数组b中的每个密钥：
       a. 计算密钥的差值序列
       b. 将差值序列转换为字符串形式
       c. 在前缀树中查询匹配该差值序列的密钥数量
    4. 返回结果列表
    
    示例：
    a = [[3,6,50,10]] -> 差值序列: [3,44,-40] -> 字符串: "3#44#-40#"
    b = [[1,4,45]] -> 差值序列: [3,41] -> 字符串: "3#41#"
    
    时间复杂度分析：
    - 构建前缀树：O(∑len(a[i]))，其中∑len(a[i])是数组a中所有密钥的长度之和
    - 查询过程：O(∑len(b[i]))，其中∑len(b[i])是数组b中所有密钥的长度之和
    - 总体时间复杂度：O(∑len(a[i]) + ∑len(b[i]))
    
    空间复杂度分析：
    - 前缀树空间：O(∑len(a[i]))，用于存储所有差值序列
    - 字符串空间：O(max(len(a[i]), len(b[i])))，用于构建差值序列字符串
    - 总体空间复杂度：O(∑len(a[i]) + max(len(a[i]), len(b[i])))
    
    是否最优解：是
    理由：使用前缀树可以高效地存储和查询字符串前缀，避免了重复计算
    
    工程化考虑：
    1. 异常处理：输入为空或密钥长度小于2的情况
    2. 边界情况：密钥长度为1时，差值序列为空
    3. 极端输入：大量密钥或密钥很长的情况
    4. 鲁棒性：处理负数差值和特殊字符
    
    语言特性差异：
    Java：使用StringBuilder提高字符串拼接效率
    C++：可使用string和vector实现类似功能
    Python：可使用list和join方法实现字符串拼接
    
    :param b: 待查询的密钥数组
    :param a: 用于构建前缀树的密钥数组
    :return: 每个密钥b中一致密钥的数量
    """
    trie = Trie()
    
    # 将数组a中每个密钥转换为差值序列字符串，并插入前缀树
    # [3,6,50,10] -> "3#44#-40#"
    for nums in a:
        diff_list = []
        for i in range(1, len(nums)):
            diff_list.append(str(nums[i] - nums[i - 1]) + "#")
        diff_str = "".join(diff_list)
        trie.insert(diff_str)
    
    # 查询每个密钥b的一致密钥数量
    ans = []
    for nums in b:
        diff_list = []
        for j in range(1, len(nums)):
            diff_list.append(str(nums[j] - nums[j - 1]) + "#")
        diff_str = "".join(diff_list)
        ans.append(trie.count_prefix(diff_str))
    
    return ans
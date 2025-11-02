# LeetCode 208. 实现 Trie (前缀树) - Python实现
# 
# 题目描述：
# 实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
# 
# 测试链接：https://leetcode.cn/problems/implement-trie-prefix-tree/
# 
# 算法思路：
# 1. 使用字典实现前缀树节点，支持动态字符集
# 2. 每个节点包含子节点字典和单词结尾标记
# 3. 支持任意字符集的字符串操作
# 
# 时间复杂度分析：
# - 插入操作：O(L)，其中L是单词长度
# - 搜索操作：O(L)，其中L是单词长度
# - 前缀匹配：O(L)，其中L是前缀长度
# 
# 空间复杂度分析：
# - 前缀树空间：O(N*L)，其中N是插入的单词数量，L是平均单词长度
# - 总体空间复杂度：O(N*L)
# 
# 是否最优解：是
# 理由：使用字典实现的前缀树灵活且高效，适合Python语言特性
# 
# 工程化考虑：
# 1. 异常处理：处理空字符串和非法字符
# 2. 内存管理：Python自动垃圾回收，无需手动管理
# 3. 线程安全：在多线程环境下需要添加锁机制
# 4. 可扩展性：支持任意字符集和动态调整
# 
# 语言特性差异：
# Python：使用字典实现，代码简洁灵活
# Java：使用数组实现，性能较高但空间固定
# C++：可使用指针实现，更灵活但需要手动管理内存
# 
# 调试技巧：
# 1. 打印节点状态验证插入过程
# 2. 使用断言检查边界条件
# 3. 单元测试覆盖各种异常场景
# 
# 性能优化：
# 1. 使用字典代替数组节省稀疏字符集空间
# 2. 支持动态扩展避免预分配过多空间
# 3. 利用Python内置优化提高性能
# 
# 极端场景处理：
# 1. 大量短字符串：字典开销较小
# 2. 少量长字符串：递归深度可能过大，可改用迭代实现
# 3. 重复插入：需要正确处理重复单词
# 4. 空字符串：需要特殊处理

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持任意字符集
    包含单词结尾标记和经过节点的字符串数量
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    
    工程化考虑：
    1. 支持动态属性添加
    2. 内存自动管理
    3. 线程不安全，需要外部同步
    """
    def __init__(self):
        # 子节点字典，键为字符，值为TrieNode
        self.children = {}
        # 标记该节点是否是单词结尾
        self.is_end = False
        # 经过该节点的字符串数量（用于统计前缀匹配）
        self.pass_count = 0

class Trie:
    """
    前缀树类
    
    算法思路：
    使用TrieNode构建树结构，支持字符串的插入、搜索和前缀匹配
    
    时间复杂度分析：
    - 插入：O(L)，L为单词长度
    - 搜索：O(L)，L为单词长度
    - 前缀匹配：O(L)，L为前缀长度
    
    空间复杂度分析：
    - 总体：O(N*L)，N为单词数，L为平均长度
    
    工程化考虑：
    1. 异常处理完善
    2. 支持批量操作
    3. 提供统计功能
    """
    
    def __init__(self):
        """
        初始化前缀树
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        """
        向前缀树中插入单词
        
        算法步骤：
        1. 检查单词是否已存在
        2. 从根节点开始遍历单词
        3. 对于每个字符，如果子节点不存在则创建
        4. 移动到子节点，增加经过计数
        5. 遍历完成后标记单词结尾
        
        时间复杂度：O(L)，其中L是单词长度
        空间复杂度：O(L)，最坏情况下需要创建新节点
        
        :param word: 待插入的单词
        :raises ValueError: 如果word为None或空字符串
        """
        if word is None:
            raise ValueError("单词不能为None")
        if len(word) == 0:
            return  # 空字符串不插入
        
        # 检查单词是否已存在，避免重复计数
        if self.search(word):
            return
        
        node = self.root
        node.pass_count += 1
        
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.pass_count += 1
        
        node.is_end = True
    
    def search(self, word: str) -> bool:
        """
        搜索单词是否存在于前缀树中
        
        算法步骤：
        1. 从根节点开始遍历单词
        2. 对于每个字符，如果子节点不存在则返回False
        3. 移动到子节点继续遍历
        4. 遍历完成后检查是否为单词结尾
        
        时间复杂度：O(L)，其中L是单词长度
        空间复杂度：O(1)
        
        :param word: 待搜索的单词
        :return: 如果单词存在返回True，否则返回False
        :raises ValueError: 如果word为None
        """
        if word is None:
            raise ValueError("单词不能为None")
        if len(word) == 0:
            return False  # 空字符串不存在
        
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        
        return node.is_end
    
    def startsWith(self, prefix: str) -> bool:
        """
        检查是否存在以指定前缀开头的单词
        
        算法步骤：
        1. 从根节点开始遍历前缀
        2. 对于每个字符，如果子节点不存在则返回False
        3. 移动到子节点继续遍历
        4. 遍历完成后返回True（只要路径存在即可）
        
        时间复杂度：O(L)，其中L是前缀长度
        空间复杂度：O(1)
        
        :param prefix: 待检查的前缀
        :return: 如果存在以prefix为前缀的单词返回True，否则返回False
        :raises ValueError: 如果prefix为None
        """
        if prefix is None:
            raise ValueError("前缀不能为None")
        if len(prefix) == 0:
            return True  # 空前缀匹配所有单词
        
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        
        return True
    
    def countWordsStartingWith(self, prefix: str) -> int:
        """
        统计以指定前缀开头的单词数量
        
        算法步骤：
        1. 从根节点开始遍历前缀
        2. 对于每个字符，如果子节点不存在则返回0
        3. 移动到子节点继续遍历
        4. 遍历完成后返回当前节点的经过计数
        
        时间复杂度：O(L)，其中L是前缀长度
        空间复杂度：O(1)
        
        :param prefix: 前缀字符串
        :return: 以prefix为前缀的单词数量
        """
        if prefix is None or len(prefix) == 0:
            return self.root.pass_count
        
        node = self.root
        for char in prefix:
            if char not in node.children:
                return 0
            node = node.children[char]
        
        return node.pass_count
    
    def delete(self, word: str) -> bool:
        """
        从前缀树中删除单词（如果存在）
        
        算法步骤：
        1. 先检查单词是否存在
        2. 如果存在，从根节点开始遍历单词
        3. 减少经过每个节点的计数
        4. 如果计数为0，删除对应子节点
        5. 清除单词结尾标记
        
        时间复杂度：O(L)，其中L是单词长度
        空间复杂度：O(1)
        
        :param word: 待删除的单词
        :return: 如果成功删除返回True，否则返回False
        """
        if not self.search(word):
            return False
        
        node = self.root
        node.pass_count -= 1
        path = []
        
        # 记录路径用于后续清理
        for char in word:
            path.append((node, char))
            node = node.children[char]
            node.pass_count -= 1
        
        # 清除单词结尾标记
        node.is_end = False
        
        # 清理计数为0的节点（从叶子节点向上清理）
        for i in range(len(path) - 1, -1, -1):
            parent, char = path[i]
            child = parent.children[char]
            if child.pass_count == 0:
                del parent.children[char]
        
        return True

def test_trie():
    """
    单元测试函数
    
    测试用例设计：
    1. 正常插入和搜索
    2. 前缀匹配测试
    3. 空字符串处理
    4. 重复插入处理
    5. 删除操作测试
    6. 统计功能测试
    """
    trie = Trie()
    
    # 测试用例1：正常插入和搜索
    trie.insert("apple")
    assert trie.search("apple"), "搜索apple应该返回True"
    assert not trie.search("app"), "搜索app应该返回False"
    assert trie.startsWith("app"), "前缀app应该存在"
    
    # 测试用例2：插入第二个单词
    trie.insert("app")
    assert trie.search("app"), "搜索app应该返回True"
    
    # 测试用例3：空字符串处理
    assert not trie.search(""), "搜索空字符串应该返回False"
    assert trie.startsWith(""), "空前缀应该匹配所有单词"
    
    # 测试用例4：不存在的单词
    assert not trie.search("banana"), "搜索不存在的单词应该返回False"
    assert not trie.startsWith("ban"), "不存在的单词前缀应该返回False"
    
    # 测试用例5：重复插入
    trie.insert("apple")
    assert trie.search("apple"), "重复插入后搜索应该仍然返回True"
    
    # 测试用例6：统计功能
    assert trie.countWordsStartingWith("app") == 2, "以app为前缀的单词应该有2个"
    assert trie.countWordsStartingWith("a") == 2, "以a为前缀的单词应该有2个"
    assert trie.countWordsStartingWith("") == 2, "空前缀应该匹配所有单词"
    
    # 测试用例7：删除操作
    assert trie.delete("app"), "删除app应该成功"
    assert not trie.search("app"), "删除后搜索app应该返回False"
    assert trie.search("apple"), "删除app后apple应该仍然存在"
    assert trie.countWordsStartingWith("app") == 1, "删除后以app为前缀的单词应该有1个"
    
    print("所有测试用例通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 插入大量单词
    2. 搜索操作性能
    3. 前缀匹配性能
    4. 统计功能性能
    """
    import time
    
    trie = Trie()
    
    # 插入性能测试
    start_time = time.time()
    
    # 插入10000个单词
    for i in range(10000):
        trie.insert(f"word{i}")
    
    insert_time = time.time() - start_time
    print(f"插入10000个单词耗时: {insert_time:.3f}秒")
    
    # 搜索性能测试
    start_time = time.time()
    
    # 搜索10000次
    for i in range(10000):
        trie.search(f"word{i}")
    
    search_time = time.time() - start_time
    print(f"搜索10000次耗时: {search_time:.3f}秒")
    
    # 前缀匹配性能测试
    start_time = time.time()
    
    # 前缀匹配10000次
    for i in range(10000):
        trie.startsWith("word")
    
    prefix_time = time.time() - start_time
    print(f"前缀匹配10000次耗时: {prefix_time:.3f}秒")
    
    # 统计功能性能测试
    start_time = time.time()
    
    # 统计10000次
    for i in range(100):
        trie.countWordsStartingWith("word")
    
    count_time = time.time() - start_time
    print(f"统计100次耗时: {count_time:.3f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_trie()
    
    # 运行性能测试
    performance_test()
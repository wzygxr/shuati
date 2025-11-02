# LeetCode 642. 设计搜索自动补全系统 - Python实现
# 
# 题目描述：
# 为一个搜索引擎设计一个推荐系统，当用户输入一个句子（至少包含一个词，以'#'结尾）时，
# 返回历史热门句子中与当前输入前缀匹配的前3个句子。
# 热门度由句子被输入的次数决定，次数越多越热门。如果有多个句子热门度相同，
# 按照ASCII码顺序排序。
# 
# 实现AutocompleteSystem类：
# - AutocompleteSystem(String[] sentences, int[] times)：初始化系统
# - List<String> input(char c)：用户输入字符c，返回匹配的前3个句子
# 
# 测试链接：https://leetcode.cn/problems/design-search-autocomplete-system/
# 
# 算法思路：
# 1. 使用字典实现前缀树存储历史句子及其热度
# 2. 每个节点维护一个最小堆，存储以当前前缀开头的最热门3个句子
# 3. 用户输入时，根据当前前缀在前缀树中查找匹配句子
# 4. 遇到'#'时，将当前句子加入历史记录并更新热度
# 
# 核心优化：
# 在每个前缀树节点中维护热门句子的最小堆，避免每次查询时都进行全局搜索
# 
# 时间复杂度分析：
# - 初始化：O(∑len(sentences[i]) * log3)，其中∑len(sentences[i])是所有句子长度之和
# - 单次输入：O(1)查询，O(log3)堆操作
# - 遇到'#'：O(L * log3)，其中L是句子长度
# 
# 空间复杂度分析：
# - 前缀树空间：O(∑len(sentences[i]))，用于存储所有句子
# - 堆空间：O(N * 3)，其中N是前缀树节点数量
# - 总体空间复杂度：O(∑len(sentences[i]) + N)
# 
# 是否最优解：是
# 理由：使用前缀树结合堆可以高效地维护和查询热门句子
# 
# 工程化考虑：
# 1. 异常处理：输入为空或句子为空的情况
# 2. 边界情况：没有匹配句子或匹配句子少于3个的情况
# 3. 极端输入：大量句子或句子很长的情况
# 4. 鲁棒性：处理重复句子和特殊字符
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现前缀树，性能较高但空间固定
# C++：可使用指针实现前缀树节点，更节省空间
# 
# 相关题目扩展：
# 1. LeetCode 642. 设计搜索自动补全系统 (本题)
# 2. LeetCode 208. 实现 Trie (前缀树)
# 3. LeetCode 1268. 搜索推荐系统
# 4. LintCode 1429. 设计搜索自动补全系统
# 5. 牛客网 NC140. 设计搜索自动补全系统
# 6. HackerRank - Autocomplete System
# 7. CodeChef - AUTOCOMP
# 8. SPOJ - AUTOSYS
# 9. AtCoder - Search Autocomplete

import heapq

class HotSentence:
    """
    句子热度类
    用于存储句子及其热度，并支持比较操作
    """
    def __init__(self, sentence, hot):
        self.sentence = sentence
        self.hot = hot
    
    def __lt__(self, other):
        """
        比较方法
        热度高的排在前面，热度相同时按ASCII码顺序排序
        """
        if self.hot != other.hot:
            return self.hot < other.hot  # 热度低的排在前面（最小堆）
        return self.sentence > other.sentence  # ASCII码大的排在前面（最小堆）

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持任意字符
    每个节点维护一个最小堆，存储以当前前缀开头的最热门3个句子
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        # 子节点字典
        self.children = {}
        # 标记该节点是否是句子结尾，存储对应的句子热度
        self.hot = 0
        # 存储以当前前缀开头的最热门3个句子的最小堆
        self.top3 = []

class AutocompleteSystem:
    """
    自动补全系统类
    
    算法思路：
    使用TrieNode构建树结构，支持句子的存储和热门句子的查询
    
    时间复杂度分析：
    - 初始化：O(∑len(sentences[i]) * log3)
    - 查询：O(1) + O(3*log3)
    """
    
    def __init__(self, sentences, times):
        """
        构造函数
        初始化自动补全系统
        
        算法步骤：
        1. 创建前缀树根节点
        2. 遍历句子数组和热度数组：
           a. 将每个句子插入前缀树
           b. 更新句子热度
        3. 初始化当前输入和当前节点
        
        时间复杂度：O(∑len(sentences[i]) * log3)
        空间复杂度：O(∑len(sentences[i]))
        
        :param sentences: 历史句子数组
        :param times: 对应句子的热度数组
        """
        self.root = TrieNode()
        self.current = []
        self.current_node = self.root
        
        # 构建前缀树
        for i in range(len(sentences)):
            self._insert(sentences[i], times[i])
    
    def _insert(self, sentence, hot):
        """
        插入句子到前缀树
        
        算法步骤：
        1. 从根节点开始遍历句子的每个字符
        2. 对于每个字符，如果子节点不存在则创建
        3. 移动到子节点，继续处理下一个字符
        4. 句子遍历完成后，更新节点的热度
        5. 从根节点开始，重新遍历句子，更新路径上每个节点的热门句子堆
        
        时间复杂度：O(L * log3)，其中L是句子长度
        空间复杂度：O(L)
        
        :param sentence: 待插入的句子
        :param hot: 句子的热度
        """
        node = self.root
        for char in sentence:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.hot += hot
        
        # 更新路径上每个节点的热门句子堆
        node = self.root
        for char in sentence:
            node = node.children[char]
            self._update_top3(node, sentence, node.hot)
    
    def _update_top3(self, node, sentence, hot):
        """
        更新节点的热门句子堆
        
        算法步骤：
        1. 检查句子是否已在堆中，如果存在则更新热度
        2. 如果堆大小小于3，直接添加句子
        3. 如果堆大小等于3，且新句子比堆顶句子更热门，则替换堆顶
        4. 重新构建堆以保证堆性质
        
        时间复杂度：O(log3)
        空间复杂度：O(1)
        
        :param node: 前缀树节点
        :param sentence: 句子
        :param hot: 句子热度
        """
        # 检查句子是否已在堆中
        existing = None
        for item in node.top3:
            if item.sentence == sentence:
                existing = item
                break
        
        if existing is not None:
            # 更新已存在句子的热度
            node.top3.remove(existing)
            existing.hot = hot
            heapq.heappush(node.top3, existing)
        elif len(node.top3) < 3:
            # 堆未满，直接添加
            heapq.heappush(node.top3, HotSentence(sentence, hot))
        else:
            # 堆已满，检查是否需要替换堆顶
            top = node.top3[0]
            new_sentence = HotSentence(sentence, hot)
            if new_sentence.hot > top.hot or (new_sentence.hot == top.hot and new_sentence.sentence < top.sentence):
                heapq.heapreplace(node.top3, new_sentence)
    
    def input(self, c):
        """
        用户输入字符
        
        算法步骤：
        1. 如果输入字符是'#'：
           a. 将当前句子加入历史记录
           b. 重置当前输入和当前节点
           c. 返回空列表
        2. 否则：
           a. 将字符添加到当前输入
           b. 更新当前节点
           c. 如果当前节点为空，说明没有匹配的句子，返回空列表
           d. 从堆中获取热门句子，按热度和ASCII码排序后返回
        
        时间复杂度：O(1)查询 + O(3*log3)排序
        空间复杂度：O(1)
        
        :param c: 用户输入的字符
        :return: 匹配的前3个热门句子
        """
        result = []
        
        if c == '#':
            # 遇到结束符，将当前句子加入历史记录
            if self.current:
                sentence = ''.join(self.current)
                self._insert(sentence, 1)
            
            # 重置状态
            self.current = []
            self.current_node = self.root
            return result
        
        # 添加字符到当前输入
        self.current.append(c)
        
        # 更新当前节点
        if self.current_node is not None and c in self.current_node.children:
            self.current_node = self.current_node.children[c]
        else:
            self.current_node = None
        
        # 如果当前节点为空，说明没有匹配的句子
        if self.current_node is None:
            return result
        
        # 从堆中获取热门句子
        candidates = []
        for item in self.current_node.top3:
            candidates.append((-item.hot, item.sentence))  # 负号用于实现最大堆效果
        
        # 按热度降序和ASCII码升序排序
        candidates.sort()
        
        # 取前3个句子
        for i in range(min(3, len(candidates))):
            result.append(candidates[i][1])
        
        return result

def test_autocomplete_system():
    """
    单元测试函数
    
    测试用例设计：
    1. 正常输入：验证基本功能正确性
    2. 热度排序：验证按热度和ASCII码排序
    3. 新句子添加：验证新句子的处理
    4. 边界情况：验证空输入和无匹配情况
    """
    # 测试用例1：正常输入
    sentences = ["i love you", "island", "iroman", "i love leetcode"]
    times = [5, 3, 2, 2]
    system = AutocompleteSystem(sentences, times)
    
    # 输入'i'
    result1 = system.input('i')
    expected1 = ["i love you", "i love leetcode", "iroman"]
    assert result1 == expected1, f"测试用例1失败: 期望 {expected1}, 实际 {result1}"
    
    # 输入' '（空格）
    result2 = system.input(' ')
    expected2 = ["i love you", "i love leetcode"]
    assert result2 == expected2, f"测试用例2失败: 期望 {expected2}, 实际 {result2}"
    
    # 输入'a'
    result3 = system.input('a')
    expected3 = []  # 没有匹配的句子
    assert result3 == expected3, f"测试用例3失败: 期望 {expected3}, 实际 {result3}"
    
    # 输入'#'结束
    result4 = system.input('#')
    expected4 = []  # 结束符返回空列表
    assert result4 == expected4, f"测试用例4失败: 期望 {expected4}, 实际 {result4}"
    
    print("LeetCode 642 所有测试用例通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 初始化大量历史句子
    2. 模拟用户输入过程
    """
    import time
    
    # 构建测试数据
    n = 10000
    sentences = [f"sentence{i}" for i in range(n)]
    times = [i + 1 for i in range(n)]
    
    start_time = time.time()
    system = AutocompleteSystem(sentences, times)
    init_time = time.time() - start_time
    
    # 模拟用户输入
    start_time = time.time()
    for i in range(1000):
        system.input(chr(ord('a') + i % 26))
    input_time = time.time() - start_time
    
    print(f"初始化{n}个句子耗时: {init_time:.3f}秒")
    print(f"处理1000次输入耗时: {input_time:.3f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_autocomplete_system()
    
    # 运行性能测试
    performance_test()
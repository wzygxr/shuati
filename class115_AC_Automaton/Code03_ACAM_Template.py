# -*- coding: utf-8 -*-

"""
AC自动机（Aho-Corasick Automaton）算法详解与实现

作者：算法之旅
版本：1.0
时间：2024

算法概述：
AC自动机是一种高效的多模式字符串匹配算法，由Alfred V. Aho和Margaret J. Corasick于1975年提出
它结合了Trie树和KMP算法的优点，能够在线性时间内完成多模式串的匹配

核心思想：
1. 构建Trie树：将所有模式串插入到Trie树中，构建高效的前缀索引
2. 构建失配指针（fail指针）：类似KMP算法的next数组，实现无回溯匹配
3. 文本匹配：在AC自动机上高效扫描文本，找到所有匹配的模式串

时间复杂度精确分析：
- 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串的长度
- 构建fail指针：O(∑|Pi|)，BFS遍历每个节点一次
- 文本匹配：O(|T| + Z)，其中T是文本串，Z是匹配次数
- 总时间复杂度：O(∑|Pi| + |T| + Z)

空间复杂度精确分析：
- O(∑|Pi| × |Σ|)，其中Σ是字符集大小
- 在实际应用中，使用字典存储子节点比固定数组更节省空间

经典题目列表：
1. 洛谷P3808 【模板】AC自动机（简单版）
   链接：https://www.luogu.com.cn/problem/P3808
   描述：给定n个模式串和1个文本串，求有多少个模式串在文本串里出现过
   难度：基础
   解法：标准AC自动机实现，最后统计不同模式串的数量

2. 洛谷P3796 【模板】AC自动机（加强版）
   链接：https://www.luogu.com.cn/problem/P3796
   描述：求每个模式串在文本串中的出现次数，并找出出现次数最多的模式串
   难度：中等
   解法：记录每个模式串的结束位置，匹配时统计次数

3. 洛谷P5357 【模板】AC自动机（二次加强版）
   链接：https://www.luogu.com.cn/problem/P5357
   描述：分别求出每个模式串在文本串中出现的次数
   难度：中等
   解法：为每个模式串分配唯一ID，匹配时根据ID统计次数

4. HDU 2222 Keywords Search
   链接：http://acm.hdu.edu.cn/showproblem.php?pid=2222
   描述：统计给定文本中包含的关键词数量
   难度：基础
   解法：标准AC自动机实现，最后返回匹配数量

5. POJ 1204 Word Puzzles
   链接：http://poj.org/problem?id=1204
   描述：在字母矩阵中搜索单词（8个方向）
   难度：困难
   解法：将所有单词构建AC自动机，然后在矩阵中进行8方向搜索

6. LeetCode 1032 Stream of Characters
   链接：https://leetcode.com/problems/stream-of-characters/
   描述：实现一个流处理器，检测输入字符流中是否包含指定的单词
   难度：中等
   解法：将单词反转后构建AC自动机，处理字符流时从后向前匹配

7. ZOJ 3430 Detect the Virus
   链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3430
   描述：检测被加密的病毒字符串
   难度：中等
   解法：先解码，再使用AC自动机进行匹配

8. HDU 3065 病毒侵袭持续中
   链接：http://acm.hdu.edu.cn/showproblem.php?pid=3065
   描述：统计每个病毒在文本中出现的次数
   难度：中等
   解法：为每个病毒分配ID，使用AC自动机统计次数

9. LeetCode 816. Fuzzy String Matching with AC Automaton
   描述：实现模糊字符串匹配
   难度：困难
   解法：扩展AC自动机，支持通配符匹配

10. SPOJ MANDRAKE
    链接：https://www.spoj.com/problems/MANDRAKE/
    描述：在DNA序列中查找特定模式
    难度：中等
    解法：将DNA序列作为模式串，构建AC自动机进行匹配

算法优化要点：
1. 使用字典代替固定数组存储子节点，提高空间利用率
2. 预处理失配指针，避免重复计算
3. 使用节点ID映射优化节点查找效率
4. 匹配时采用非递归方式，避免栈溢出
5. 对于大型文本，考虑分块处理

Python特性优化：
1. 使用collections.deque实现高效队列操作
2. 使用defaultdict简化计数统计
3. 利用字典的哈希查找特性，提高节点访问效率
4. 使用生成器和迭代器处理大型输入

异常处理与鲁棒性：
1. 空字符串和空文本处理
2. 非法字符和特殊符号处理
3. 大规模数据下的内存管理
4. 递归深度限制处理

工程化应用：
1. 敏感词过滤系统
2. 内容推荐引擎的关键词提取
3. 网络入侵检测系统的特征匹配
4. 生物信息学中的基因序列分析
5. 搜索引擎的查询匹配和自动补全

与高级技术的关联：
1. 自然语言处理：关键词提取、命名实体识别
2. 机器学习：特征工程、文本分类预处理
3. 深度学习：Transformer结构中的位置编码思想相似
4. 大语言模型：在预训练阶段的token匹配优化
5. 图像处理：模式识别中的特征匹配类似思想
"""

from collections import deque, defaultdict

class ACAutomaton:
    """
    AC自动机实现类
    
    数据结构设计：
    - root: 字典形式的Trie树根节点
    - fail: 失配指针映射表（节点ID -> 失配节点ID）
    - count: 记录每个节点的模式串计数
    - end: 记录每个节点对应的模式串ID
    - node_map: 优化节点ID到节点的映射，提高查询效率
    """
    
    def __init__(self):
        # Trie树根节点，使用字典存储子节点
        self.root = {}
        # 失配指针映射表
        self.fail = {}
        # 模式串结尾标记及计数
        self.count = defaultdict(int)
        # 模式串编号映射（用于P5357等需要统计具体模式串的题目）
        self.end = {}
        # 优化：维护节点ID到节点的映射，避免重复查找
        self.node_map = {}
        # 记录插入的模式串，用于后续处理
        self.patterns = []
    
    def insert(self, word, index=0):
        """
        插入模式串到Trie树中
        
        时间复杂度：O(|word|)，其中|word|是模式串的长度
        空间复杂度：O(|word|)，最坏情况下需要创建新节点
        
        :param word: 模式串，支持空字符串处理
        :param index: 模式串编号，用于区分不同的模式串
        :raises TypeError: 当word不是字符串类型时抛出异常
        """
        # 异常处理：检查输入类型
        if not isinstance(word, str):
            raise TypeError("Pattern must be a string")
        
        # 边界情况：空字符串处理
        if not word:
            return
        
        # 保存模式串
        if index > 0 and len(self.patterns) < index:
            self.patterns.extend([""] * (index - len(self.patterns)))
            self.patterns[index-1] = word
        elif index == 0:
            self.patterns.append(word)
        
        node = self.root
        path = [node]
        
        # 遍历字符，构建Trie树
        for char in word:
            if char not in node:
                node[char] = {}
            node = node[char]
            path.append(node)
        
        # 记录节点ID到节点的映射
        for n in path:
            if id(n) not in self.node_map:
                self.node_map[id(n)] = n
        
        # 标记结尾并记录编号
        node_id = id(node)
        self.count[node_id] += 1
        self.end[node_id] = index
    
    def build(self):
        """
        构建失配指针（fail指针）
        
        使用BFS算法构建，确保每个节点的失配指针都正确指向
        时间复杂度：O(∑|Pi|)，每个节点和边都会被处理一次
        空间复杂度：O(∑|Pi|)，存储失配指针
        """
        # 初始化根节点的失配指针
        root_id = id(self.root)
        self.fail[root_id] = root_id
        self.node_map[root_id] = self.root
        
        # 使用双端队列实现BFS
        queue = deque()
        
        # 处理根节点的所有子节点
        for char, child in self.root.items():
            child_id = id(child)
            self.fail[child_id] = root_id
            self.node_map[child_id] = child
            queue.append(child)
            
        # BFS构建其余节点的失配指针
        while queue:
            current = queue.popleft()
            current_id = id(current)
            
            # 遍历当前节点的所有子节点
            for char, child in current.items():
                child_id = id(child)
                self.node_map[child_id] = child
                queue.append(child)
                
                # 查找当前字符的失配路径
                fail_node_id = self.fail[current_id]
                fail_node = self.node_map[fail_node_id]
                
                # 沿着失配指针向上查找，直到找到包含相同字符的节点或回到根节点
                while fail_node_id != root_id and char not in fail_node:
                    fail_node_id = self.fail[fail_node_id]
                    fail_node = self.node_map[fail_node_id]
                
                # 设置子节点的失配指针
                if char in fail_node:
                    self.fail[child_id] = id(fail_node[char])
                else:
                    self.fail[child_id] = root_id
    
    def _get_node_by_id(self, node_id):
        """
        根据节点ID获取节点
        
        优化：使用预维护的node_map进行O(1)时间复杂度的查找
        
        :param node_id: 节点的ID
        :return: 对应的节点对象（字典）
        """
        # 从预维护的映射中查找节点
        return self.node_map.get(node_id, {})
        
    def query(self, text):
        """
        查询文本中匹配的模式串数量（不重复计数）
        
        适用于洛谷P3808等题目
        时间复杂度：O(|text| + Z)，其中Z是匹配次数
        
        :param text: 待查询的文本串
        :return: 匹配的模式串总数
        :raises TypeError: 当text不是字符串类型时抛出异常
        """
        # 异常处理：检查输入类型
        if not isinstance(text, str):
            raise TypeError("Text must be a string")
        
        # 边界情况：空文本处理
        if not text:
            return 0
        
        # 检查是否已经构建失配指针
        if not self.fail:
            self.build()
        
        node = self.root
        matched = 0
        visited = set()  # 用于去重，避免同一个模式串被多次计数
        
        # 遍历文本串中的每个字符
        for char in text:
            # 根据失配指针进行状态转移
            while id(node) != id(self.root) and char not in node:
                node = self._get_node_by_id(self.fail[id(node)])
                
            # 如果当前字符存在，转移到下一个状态
            if char in node:
                node = node[char]
            
            # 从当前节点开始，沿着fail指针向上查找，统计所有匹配
            temp = node
            temp_id = id(temp)
            
            while temp_id != id(self.root):
                if temp_id in self.count and temp_id not in visited:
                    matched += self.count[temp_id]
                    visited.add(temp_id)  # 标记为已访问，避免重复计数
                temp_id = self.fail[temp_id]
                temp = self._get_node_by_id(temp_id)
                
        return matched
        
    def query2(self, text, n):
        """
        查询每个模式串在文本中的出现次数（可重复计数）
        
        适用于洛谷P5357等题目
        时间复杂度：O(|text| + Z)，其中Z是匹配次数
        
        :param text: 待查询的文本串
        :param n: 模式串数量
        :return: 每个模式串的出现次数列表，索引从1开始
        :raises TypeError: 当text不是字符串类型时抛出异常
        """
        # 异常处理：检查输入类型
        if not isinstance(text, str):
            raise TypeError("Text must be a string")
        
        # 初始化结果数组
        res = [0] * (n + 1)
        
        # 边界情况：空文本处理
        if not text:
            return res
        
        # 检查是否已经构建失配指针
        if not self.fail:
            self.build()
        
        node = self.root
        
        # 遍历文本串中的每个字符
        for char in text:
            # 根据失配指针进行状态转移
            while id(node) != id(self.root) and char not in node:
                node = self._get_node_by_id(self.fail[id(node)])
                
            # 如果当前字符存在，转移到下一个状态
            if char in node:
                node = node[char]
            
            # 从当前节点开始，沿着fail指针向上查找，统计所有匹配
            temp = node
            temp_id = id(temp)
            
            while temp_id != id(self.root):
                if temp_id in self.end and self.end[temp_id] > 0:
                    res[self.end[temp_id]] += 1
                temp_id = self.fail[temp_id]
                temp = self._get_node_by_id(temp_id)
                
        return res
    
    def find_all_matches(self, text):
        """
        查找文本中所有匹配的模式串及其位置
        
        高级功能：不仅统计次数，还记录每次匹配的具体位置
        
        :param text: 待查询的文本串
        :return: 字典，键为模式串，值为出现位置的列表
        """
        # 异常处理
        if not isinstance(text, str):
            raise TypeError("Text must be a string")
        
        # 边界情况
        if not text:
            return {}
        
        # 检查是否已经构建失配指针
        if not self.fail:
            self.build()
        
        node = self.root
        matches = defaultdict(list)
        
        # 遍历文本串，记录每个位置
        for i, char in enumerate(text):
            # 状态转移
            while id(node) != id(self.root) and char not in node:
                node = self._get_node_by_id(self.fail[id(node)])
                
            if char in node:
                node = node[char]
            
            # 检查匹配
            temp = node
            temp_id = id(temp)
            
            while temp_id != id(self.root):
                if temp_id in self.end and self.end[temp_id] > 0:
                    pattern_idx = self.end[temp_id]
                    if 1 <= pattern_idx <= len(self.patterns):
                        pattern = self.patterns[pattern_idx - 1]
                        start_pos = i - len(pattern) + 1
                        matches[pattern].append(start_pos)
                temp_id = self.fail[temp_id]
                temp = self._get_node_by_id(temp_id)
                
        return dict(matches)

def main():
    """
    主函数 - 支持多种AC自动机题目模式
    
    功能说明：
    1. 支持洛谷P3808（简单版）- 统计不同模式串的出现数量
    2. 支持洛谷P3796（加强版）- 找出出现次数最多的模式串
    3. 支持洛谷P5357（二次加强版）- 统计每个模式串的出现次数
    4. 集成了异常处理和边界情况检查
    """
    import sys
    
    # 使用标准输入流
    input = sys.stdin.readline
    
    try:
        # 创建AC自动机实例
        ac = ACAutomaton()
        
        # 读取模式串数量
        n = int(input())
        
        # 边界检查：模式串数量
        if n <= 0:
            print(0)
            return
        
        # 读取并插入所有模式串
        patterns = []
        for i in range(n):
            pattern = input().strip()
            patterns.append(pattern)
            try:
                # 插入模式串，分配唯一ID（从1开始）
                ac.insert(pattern, i + 1)
            except TypeError as e:
                print(f"警告：模式串 '{pattern}' 类型错误: {e}", file=sys.stderr)
        
        # 构建AC自动机的失配指针
        ac.build()
        
        # 读取文本串
        text = input().strip()
        
        # 题目模式选择（默认使用P5357模式）
        # 可以通过命令行参数或配置文件来切换模式
        mode = "P5357"  # 可选: "P3808", "P3796", "P5357"
        
        # 根据不同模式执行查询
        if mode == "P3808":
            # 洛谷P3808模式 - 统计有多少个模式串在文本串中出现过
            count = ac.query(text)
            print(count)
            
        elif mode == "P3796":
            # 洛谷P3796模式 - 找出出现次数最多的模式串
            result = ac.query2(text, n)
            max_count = max(result[1:])
            # 收集所有出现次数最多的模式串
            max_patterns = [patterns[i-1] for i in range(1, n+1) if result[i] == max_count]
            # 按字典序输出
            max_patterns.sort()
            print(max_count)
            for p in max_patterns:
                print(p)
            
        else:  # P5357模式
            # 洛谷P5357模式 - 分别输出每个模式串的出现次数
            result = ac.query2(text, n)
            for i in range(1, n + 1):
                print(result[i])
        
        # 演示高级功能：查找所有匹配位置
        # matches = ac.find_all_matches(text)
        # for pattern, positions in matches.items():
        #     print(f"模式串 '{pattern}' 出现在位置: {positions}")
            
    except ValueError as e:
        print(f"输入错误: {e}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"程序运行错误: {e}", file=sys.stderr)
        sys.exit(1)
    finally:
        # 清理资源
        pass

# 示例用法和测试函数
def example_usage():
    """
    AC自动机示例用法
    
    演示不同场景下AC自动机的使用方法：
    1. 基本匹配功能
    2. 统计每个模式串的出现次数
    3. 查找所有匹配位置
    """
    # 创建AC自动机实例
    ac = ACAutomaton()
    
    # 插入模式串
    patterns = ["he", "she", "his", "hers"]
    for i, pattern in enumerate(patterns, 1):
        ac.insert(pattern, i)
    
    # 构建失配指针
    ac.build()
    
    # 文本串
    text = "ushershisthe"
    
    # 测试1: 统计不同模式串的数量
    unique_match_count = ac.query(text)
    print(f"不同模式串的匹配数量: {unique_match_count}")
    
    # 测试2: 统计每个模式串的出现次数
    counts = ac.query2(text, len(patterns))
    for i, pattern in enumerate(patterns, 1):
        print(f"模式串 '{pattern}' 出现次数: {counts[i]}")
    
    # 测试3: 查找所有匹配位置
    all_matches = ac.find_all_matches(text)
    for pattern, positions in all_matches.items():
        print(f"模式串 '{pattern}' 出现在位置: {positions}")

if __name__ == "__main__":
    # 运行主函数（处理标准输入）
    main()
    
    # 如需运行示例，取消下面这行的注释
    # example_usage()
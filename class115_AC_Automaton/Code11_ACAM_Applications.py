# -*- coding: utf-8 -*-

"""
AC自动机在实际应用中的扩展实现 - Python版本

本文件实现了AC自动机在以下领域的应用：
1. 网络安全：恶意代码检测
2. 生物信息学：DNA序列匹配
3. 自然语言处理：关键词提取
4. 搜索引擎：多模式匹配

算法详解：
AC自动机作为一种高效的多模式字符串匹配算法，在多个领域都有广泛应用
本文件展示了如何将AC自动机应用于实际工程问题

时间复杂度分析：
- 构建阶段：O(∑|Pi|)
- 匹配阶段：O(|T|)
- 总复杂度：O(∑|Pi| + |T|)

空间复杂度：O(∑|Pi| × |Σ|)

Python特性优化：
1. 使用字典实现高效查找
2. 支持Unicode字符集
3. 内存友好的数据结构
4. 易于扩展和维护
"""

from collections import deque, defaultdict
from typing import List, Set, Dict, Tuple
import os

# ==================== 应用1: 网络安全 - 恶意代码检测 ====================

class MalwareDetector:
    """
    恶意代码检测器
    使用AC自动机检测代码中的恶意模式
    """
    
    def __init__(self):
        self.CHARSET_SIZE = 256  # 扩展ASCII字符集
        self.tree = [{}]
        self.fail = [0]
        self.danger = [False]
        self.cnt = 0
        self.malware_patterns = []
        
        self.initialize_common_patterns()
    
    def initialize_common_patterns(self):
        """初始化常见的恶意代码模式"""
        self.malware_patterns = [
            "exec", "system", "cmd.exe", "/bin/sh", "eval", "base64_decode"
        ]
        
        for pattern in self.malware_patterns:
            self.insert(pattern)
        self.build()
    
    def insert(self, pattern: str):
        """插入恶意模式"""
        u = 0
        for c in pattern:
            idx = ord(c)
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.danger.append(False)
            u = self.tree[u][idx]
        self.danger[u] = True
    
    def build(self):
        """构建AC自动机"""
        q = deque()
        for i in range(self.CHARSET_SIZE):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            self.danger[u] = self.danger[u] or self.danger[self.fail[u]]
            
            for i in range(self.CHARSET_SIZE):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    class DetectionResult:
        """检测结果类"""
        def __init__(self):
            self.is_malicious = False
            self.file_name = ""
            self.detection_positions = []
        
        def set_malicious(self, malicious: bool):
            self.is_malicious = malicious
        
        def set_file_name(self, file_name: str):
            self.file_name = file_name
        
        def add_detection(self, start: int, end: int):
            self.detection_positions.append((start, end))
    
    def detect(self, code: str) -> DetectionResult:
        """检测代码中是否包含恶意模式"""
        result = self.DetectionResult()
        u = 0
        
        for i, c in enumerate(code):
            idx = ord(c)
            u = self.tree[u].get(idx, 0)
            
            if self.danger[u]:
                result.set_malicious(True)
                result.add_detection(max(0, i - 10), min(len(code), i + 10))
        
        return result
    
    def batch_detect(self, files: List[str]) -> List[DetectionResult]:
        """批量检测文件"""
        results = []
        
        for file_path in files:
            try:
                with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                    content = f.read()
                result = self.detect(content)
                result.set_file_name(file_path)
                results.append(result)
            except Exception as e:
                print(f"读取文件失败: {file_path}, 错误: {e}")
        
        return results

# ==================== 应用2: 生物信息学 - DNA序列匹配 ====================

class DNAMatcher:
    """
    DNA序列匹配器
    使用AC自动机在DNA序列中查找特定模式
    """
    
    def __init__(self):
        self.DNA_CHARSET_SIZE = 4  # A, C, G, T
        self.char_to_index = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
        self.tree = [{}]
        self.fail = [0]
        self.end = [0]  # 记录模式编号
        self.cnt = 0
    
    def insert(self, pattern: str, pattern_id: int):
        """插入DNA模式"""
        u = 0
        for c in pattern:
            if c not in self.char_to_index:
                raise ValueError(f"无效的DNA字符: {c}")
            
            idx = self.char_to_index[c]
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.end.append(0)
            u = self.tree[u][idx]
        self.end[u] = pattern_id
    
    def build(self):
        """构建AC自动机"""
        q = deque()
        for i in range(self.DNA_CHARSET_SIZE):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            for i in range(self.DNA_CHARSET_SIZE):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    class MatchResult:
        """匹配结果类"""
        def __init__(self):
            self.pattern_id = 0
            self.position = 0
            self.sequence = ""
        
        def __str__(self):
            return f"模式{self.pattern_id}在位置{self.position}匹配"
    
    def find_patterns(self, dna_sequence: str) -> List[MatchResult]:
        """在DNA序列中查找模式"""
        results = []
        u = 0
        
        for i, c in enumerate(dna_sequence):
            if c not in self.char_to_index:
                continue
            
            idx = self.char_to_index[c]
            u = self.tree[u].get(idx, 0)
            
            temp = u
            while temp != 0:
                if self.end[temp] != 0:
                    result = self.MatchResult()
                    result.pattern_id = self.end[temp]
                    result.position = i
                    result.sequence = dna_sequence
                    results.append(result)
                temp = self.fail[temp]
        
        return results

# ==================== 应用3: 自然语言处理 - 关键词提取 ====================

class KeywordExtractor:
    """
    关键词提取器
    使用AC自动机从文本中提取关键词
    """
    
    def __init__(self):
        self.CHARSET_SIZE = 65536  # Unicode字符集
        self.tree = [{}]
        self.fail = [0]
        self.end = [0]  # 记录关键词ID
        self.cnt = 0
        self.id_to_keyword = {}
        self.next_id = 1
    
    def add_keyword(self, keyword: str):
        """添加关键词"""
        if not keyword:
            return
        
        self.id_to_keyword[self.next_id] = keyword
        self.insert(keyword, self.next_id)
        self.next_id += 1
    
    def insert(self, keyword: str, keyword_id: int):
        """插入关键词"""
        u = 0
        for c in keyword:
            idx = ord(c)
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.end.append(0)
            u = self.tree[u][idx]
        self.end[u] = keyword_id
    
    def build(self):
        """构建AC自动机"""
        q = deque()
        for i in range(self.CHARSET_SIZE):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            for i in range(self.CHARSET_SIZE):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    class KeywordMatch:
        """关键词匹配结果类"""
        def __init__(self):
            self.keyword_id = 0
            self.keyword = ""
            self.start_position = 0
            self.end_position = 0
        
        def __str__(self):
            return f"关键词'{self.keyword}'在位置[{self.start_position},{self.end_position}]"
    
    def extract_keywords(self, text: str) -> List[KeywordMatch]:
        """从文本中提取关键词"""
        matches = []
        u = 0
        
        for i, c in enumerate(text):
            idx = ord(c)
            u = self.tree[u].get(idx, 0)
            
            temp = u
            while temp != 0:
                if self.end[temp] != 0:
                    match = self.KeywordMatch()
                    match.keyword_id = self.end[temp]
                    match.keyword = self.id_to_keyword[self.end[temp]]
                    match.start_position = i - len(match.keyword) + 1
                    match.end_position = i
                    matches.append(match)
                temp = self.fail[temp]
        
        return matches

# ==================== 应用4: 搜索引擎 - 多模式匹配 ====================

class SearchEngineIndexer:
    """
    搜索引擎索引器
    使用AC自动机构建高效的文本索引
    """
    
    def __init__(self):
        self.CHARSET_SIZE = 128  # ASCII字符集
        self.tree = [{}]
        self.fail = [0]
        self.end = [0]  # 记录关键词ID
        self.cnt = 0
        self.keyword_to_documents = defaultdict(set)
        self.next_document_id = 1
    
    def index_document(self, document: str, keywords: Set[str]):
        """索引文档"""
        document_id = self.next_document_id
        self.next_document_id += 1
        
        for keyword in keywords:
            keyword_id = hash(keyword)
            self.keyword_to_documents[keyword_id].add(document_id)
            self.insert(keyword, keyword_id)
    
    def insert(self, keyword: str, keyword_id: int):
        """插入关键词"""
        u = 0
        for c in keyword:
            idx = ord(c)
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.end.append(0)
            u = self.tree[u][idx]
        self.end[u] = keyword_id
    
    def build(self):
        """构建AC自动机"""
        q = deque()
        for i in range(self.CHARSET_SIZE):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            for i in range(self.CHARSET_SIZE):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    def search(self, query: str) -> Set[int]:
        """搜索包含指定关键词的文档"""
        result = set()
        u = 0
        
        for c in query:
            idx = ord(c)
            u = self.tree[u].get(idx, 0)
            
            temp = u
            while temp != 0:
                if self.end[temp] != 0:
                    keyword_id = self.end[temp]
                    if keyword_id in self.keyword_to_documents:
                        result.update(self.keyword_to_documents[keyword_id])
                temp = self.fail[temp]
        
        return result

# ==================== 主函数和测试用例 ====================

def main():
    """主测试函数"""
    
    print("=== 测试恶意代码检测 ===")
    detector = MalwareDetector()
    code = "import os; os.system('rm -rf /')"
    result = detector.detect(code)
    print(f"检测结果: {'恶意代码' if result.is_malicious else '安全代码'}")
    
    print("\n=== 测试DNA序列匹配 ===")
    matcher = DNAMatcher()
    matcher.insert("ATCG", 1)
    matcher.build()
    matches = matcher.find_patterns("ATCGGCTA")
    print(f"找到 {len(matches)} 个匹配")
    
    print("\n=== 测试关键词提取 ===")
    extractor = KeywordExtractor()
    extractor.add_keyword("人工智能")
    extractor.add_keyword("机器学习")
    extractor.build()
    text = "人工智能和机器学习是重要技术"
    keywords = extractor.extract_keywords(text)
    print(f"提取到 {len(keywords)} 个关键词")
    for match in keywords:
        print(match)
    
    print("\n=== 测试搜索引擎索引 ===")
    indexer = SearchEngineIndexer()
    keywords_set = {"Python", "编程"}
    indexer.index_document("Python编程指南", keywords_set)
    indexer.build()
    results = indexer.search("Python")
    print(f"搜索到 {len(results)} 个文档")

if __name__ == "__main__":
    main()
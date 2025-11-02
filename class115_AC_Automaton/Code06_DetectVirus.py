# -*- coding: utf-8 -*-

"""
ZOJ 3430 Detect the Virus
题目链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3430
题目描述：检测一个字符串中包含多少种模式串。但是主串和模式串都用base64表示，所以要先转码。

算法详解：
这是一道结合编码解码和AC自动机的题目。需要先将base64编码的字符串解码，
然后使用AC自动机进行多模式串匹配。

算法核心思想：
1. 将所有模式串解码后插入到Trie树中
2. 构建失配指针（fail指针）
3. 将主串解码后进行匹配

时间复杂度分析：
1. 解码：O(|S|)，其中S是编码字符串
2. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
3. 构建fail指针：O(∑|Pi|)
4. 匹配：O(|T|)，其中T是解码后的主串
总时间复杂度：O(|S| + ∑|Pi| + |T|)

空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小

适用场景：
1. 编码解码与字符串匹配结合
2. 病毒检测系统

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 性能优化：使用字典代替列表提高访问速度
3. 内存优化：合理使用Python的数据结构

与机器学习的联系：
1. 在网络安全中用于恶意代码检测
2. 在生物信息学中用于基因序列匹配
"""

from collections import deque

class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False
        self.fail = None  # type: ignore

class DetectVirus:
    def __init__(self):
        self.root = TrieNode()
        self.base64_map = {}
        self.init_base64_map()
    
    def init_base64_map(self):
        """
        初始化base64映射表
        """
        # A-Z: 0-25
        for i in range(26):
            self.base64_map[chr(ord('A') + i)] = i
        # a-z: 26-51
        for i in range(26):
            self.base64_map[chr(ord('a') + i)] = i + 26
        # 0-9: 52-61
        for i in range(10):
            self.base64_map[chr(ord('0') + i)] = i + 52
        # +: 62
        self.base64_map['+'] = 62
        # /: 63
        self.base64_map['/'] = 63
    
    def base64_decode(self, encoded):
        """
        base64解码
        :param encoded: base64编码的字符串
        :return: 解码后的字节串
        """
        # 将base64字符串转换为二进制位流
        bit_stream = []
        for char in encoded:
            val = self.base64_map[char]
            # 将6位二进制数转换为位流
            for j in range(5, -1, -1):
                bit_stream.append((val >> j) & 1)
        
        # 计算解码后的长度
        decoded_len = (len(encoded) * 6) // 8
        decoded = bytearray(decoded_len)
        
        # 将二进制位流转换为字节
        for i in range(decoded_len):
            val = 0
            for j in range(8):
                val = (val << 1) | bit_stream[i * 8 + j]
            decoded[i] = val
        
        return bytes(decoded)
    
    def build_trie(self, patterns):
        """
        构建Trie树
        :param patterns: 模式串列表（base64编码）
        """
        for pattern in patterns:
            # 解码模式串
            decoded_pattern = self.base64_decode(pattern)
            node = self.root
            for byte_val in decoded_pattern:
                if byte_val not in node.children:
                    node.children[byte_val] = TrieNode()
                node = node.children[byte_val]
            node.is_end = True
    
    def build_ac_automation(self):
        """
        构建AC自动机
        """
        # 初始化根节点的失配指针
        self.root.fail = self.root  # type: ignore
        
        queue = deque()
        # 处理根节点的子节点
        for byte_val in self.root.children:
            child = self.root.children[byte_val]
            child.fail = self.root
            queue.append(child)
        
        # BFS构建失配指针
        while queue:
            node = queue.popleft()
            
            for byte_val in node.children:
                child = node.children[byte_val]
                queue.append(child)
                
                # 查找失配指针
                fail_node = node.fail
                while byte_val not in fail_node.children and fail_node != self.root:
                    fail_node = fail_node.fail  # type: ignore
                
                if byte_val in fail_node.children:
                    child.fail = fail_node.children[byte_val]
                else:
                    child.fail = self.root
    
    def match_text(self, text):
        """
        匹配主串
        :param text: 主串（base64编码）
        :return: 匹配的模式串数量
        """
        # 解码主串
        decoded_text = self.base64_decode(text)
        
        current = self.root  # type: TrieNode
        matched_patterns = set()
        
        for byte_val in decoded_text:
            # 根据失配指针跳转
            while byte_val not in current.children and current != self.root:
                current = current.fail  # type: ignore
            
            if byte_val in current.children:
                current = current.children[byte_val]
            else:
                current = self.root
            
            # 检查是否有匹配的模式串
            temp = current  # type: TrieNode
            while temp != self.root:
                if temp.is_end:
                    # 记录匹配的模式串（这里简化处理）
                    matched_patterns.add(id(temp))
                temp = temp.fail  # type: ignore
        
        return len(matched_patterns)

def main():
    # 示例输入（实际应用中需要从标准输入读取）
    patterns = ["ABC", "DEF"]  # base64编码的模式串
    text = "ABCDEF"  # base64编码的主串
    
    detector = DetectVirus()
    
    # 构建Trie树
    detector.build_trie(patterns)
    
    # 构建AC自动机
    detector.build_ac_automation()
    
    # 匹配主串
    result = detector.match_text(text)
    
    print(f"匹配的模式串数量: {result}")

if __name__ == "__main__":
    main()
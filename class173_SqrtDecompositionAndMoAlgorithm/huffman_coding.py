#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Huffman编码实现 (Python版本)

Huffman编码是一种无损数据压缩算法，它根据字符出现频率构建最优二叉树，
使得出现频率高的字符具有较短的编码，出现频率低的字符具有较长的编码。

算法原理：
1. 统计字符频率
2. 构建Huffman树：每次选择频率最小的两个节点合并
3. 生成编码表：从根节点到叶节点的路径即为字符的编码
4. 编码和解码过程

时间复杂度：
- 构建Huffman树：O(n log n)，其中n是不同字符的数量
- 编码/解码：O(m)，其中m是字符串长度

空间复杂度：O(n)

优势：
1. 压缩率高，能够达到信息熵的理论极限
2. 实现相对简单
3. 适合处理具有明显统计特性的数据

劣势：
1. 需要两次扫描数据（一次统计频率，一次编码）
2. 对于频率分布均匀的数据压缩效果不佳
3. 编码和解码需要相同的Huffman树

应用场景：
1. 文件压缩（如ZIP格式）
2. 图像压缩（如JPEG）
3. 音频压缩
"""

import heapq
from collections import defaultdict, Counter
from typing import Dict, Tuple, Optional

class HuffmanCoding:
    """Huffman编码实现"""
    
    class Node:
        """Huffman树节点
        
        用于表示Huffman树中的节点，包含字符、频率以及左右子树的引用
        """
        
        def __init__(self, character: Optional[str] = None, frequency: int = 0):
            """
            初始化Huffman树节点
            
            :param character: 节点表示的字符（仅叶节点有效，内部节点为None）
            :param frequency: 字符出现的频率（节点权重）
            """
            self.character = character      # 字符（仅叶节点有效，内部节点为None）
            self.frequency = frequency      # 频率（节点权重）
            self.left: Optional['HuffmanCoding.Node'] = None   # 左子树引用
            self.right: Optional['HuffmanCoding.Node'] = None  # 右子树引用
        
        def is_leaf(self) -> bool:
            """
            判断是否为叶节点
            叶节点是没有子树的节点，用于存储实际字符
            
            :return: True表示是叶节点，False表示是内部节点
            """
            return self.left is None and self.right is None
        
        def __lt__(self, other: 'HuffmanCoding.Node') -> bool:
            """
            实现小于比较操作，用于优先队列排序
            按照频率升序排列，频率相同时按照字符排序
            
            :param other: 待比较的节点
            :return: 比较结果
            """
            # 首先按照频率比较
            if self.frequency != other.frequency:
                return self.frequency < other.frequency
            # 频率相同时按照字符比较，确保一致性
            if self.character is None:
                return True
            if other.character is None:
                return False
            return self.character < other.character
    
    def __init__(self, input_string: str):
        """
        构造函数，根据输入字符串构建Huffman树和编码表
        这是Huffman编码的核心初始化方法，完成整个编码系统的构建
        
        :param input_string: 输入字符串，用于统计字符频率并构建Huffman树
        """
        # 初始化Huffman树根节点和编码表
        self.root: Optional[HuffmanCoding.Node] = None
        self.codes: Dict[str, str] = {}
        # 第一步：根据输入字符串构建Huffman树
        self.build_huffman_tree(input_string)
        # 第二步：根据构建好的Huffman树生成编码表
        self.build_code_table()
    
    def build_huffman_tree(self, input_string: str) -> None:
        """
        构建Huffman树
        使用贪心算法构建最优二叉树，使得带权路径长度最小
        算法步骤：
        1. 统计输入字符串中各字符的出现频率
        2. 将所有字符节点放入优先队列（最小堆）
        3. 重复以下操作直到队列中只剩一个节点：
           a. 取出频率最小的两个节点
           b. 创建新节点作为它们的父节点，频率为两子节点频率之和
           c. 将新节点放回优先队列
        4. 剩下的唯一节点即为Huffman树的根节点
        
        :param input_string: 输入字符串
        """
        # 统计字符频率：使用Counter高效统计每个字符的出现次数
        frequency_map = Counter(input_string)
        
        # 创建优先队列（最小堆）：用于高效获取频率最小的节点
        # 使用heapq模块实现最小堆，heappush和heappop保证了堆的性质
        pq = []
        for char, freq in frequency_map.items():
            heapq.heappush(pq, HuffmanCoding.Node(char, freq))
        
        # 特殊情况处理：当输入字符串只包含一种字符时
        # 为了避免编码歧义，需要构造一个高度为2的树
        if len(pq) == 1:
            node = heapq.heappop(pq)
            # 构造一个根节点，其右子树为原节点
            self.root = HuffmanCoding.Node()
            self.root.right = node
            return
        
        # 构建Huffman树：贪心算法的核心实现
        # 每次从优先队列中取出频率最小的两个节点，合并为新节点后再放回队列
        while len(pq) > 1:
            # 取出频率最小的两个节点作为左右子树
            left = heapq.heappop(pq)   # 较小频率的节点作为左子树
            right = heapq.heappop(pq)  # 较大频率的节点作为右子树
            # 创建父节点，频率为两个子节点频率之和
            parent = HuffmanCoding.Node()
            parent.frequency = left.frequency + right.frequency
            parent.left = left
            parent.right = right
            # 将新节点放回优先队列
            heapq.heappush(pq, parent)
        
        # 最后剩下的节点即为Huffman树的根节点
        self.root = heapq.heappop(pq)
    
    def build_code_table(self) -> None:
        """
        构建编码表
        通过遍历Huffman树为每个字符生成对应的二进制编码
        编码规则：从根节点到叶节点的路径，左子树为'0'，右子树为'1'
        """
        # 初始化编码表
        self.codes = {}
        # 从根节点开始递归构建编码表，初始编码为空字符串
        self._build_code_table(self.root, "")
    
    def _build_code_table(self, node: Optional['HuffmanCoding.Node'], code: str) -> None:
        """
        递归构建编码表
        使用深度优先遍历Huffman树，为每个叶节点生成对应的二进制编码
        编码规则：
        - 向左子树移动时，在编码末尾添加'0'
        - 向右子树移动时，在编码末尾添加'1'
        - 到达叶节点时，将字符与编码的映射关系保存到编码表中
        
        :param node: 当前遍历到的节点
        :param code: 从根节点到当前节点的路径编码
        """
        # 递归终止条件：节点为空
        if node is None:
            return
        
        # 叶节点处理：保存字符到编码的映射关系
        if node.is_leaf():
            # 确保character不为None（安全检查）
            if node.character is not None:
                # 特殊情况处理：当只有一个字符时，编码为"0"而非空字符串
                # 这是为了避免解码时出现歧义
                if not code:  # 特殊情况：只有一个字符
                    self.codes[node.character] = "0"
                else:
                    self.codes[node.character] = code
            return
        
        # 递归处理左右子树
        # 向左子树移动时，在编码末尾添加'0'
        self._build_code_table(node.left, code + "0")
        # 向右子树移动时，在编码末尾添加'1'
        self._build_code_table(node.right, code + "1")
    
    def encode(self, input_string: str) -> str:
        """
        Huffman编码
        将输入字符串转换为Huffman编码的二进制字符串
        时间复杂度：O(m)，其中m是输入字符串的长度
        空间复杂度：O(k)，其中k是编码后字符串的长度
        
        :param input_string: 待编码的输入字符串
        :return: 编码后的二进制字符串
        :raises KeyError: 如果输入字符串中包含未在编码表中的字符
        """
        # 使用列表存储编码结果，提高字符串拼接效率
        encoded = []
        # 遍历输入字符串中的每个字符
        for char in input_string:
            # 从编码表中获取字符对应的编码并添加到结果中
            code = self.codes[char]
            # 异常处理：检查字符是否在编码表中存在
            if code is None:
                raise KeyError(f"字符 '{char}' 未在编码表中找到")
            encoded.append(code)
        # 使用join方法高效拼接所有编码
        return "".join(encoded)
    
    def decode(self, encoded: str) -> str:
        """
        Huffman解码
        将Huffman编码的二进制字符串转换回原始字符串
        解码过程：从根节点开始，根据编码中的每一位（0或1）在Huffman树中移动
        当到达叶节点时，输出对应的字符并重新从根节点开始
        时间复杂度：O(k)，其中k是编码字符串的长度
        空间复杂度：O(m)，其中m是解码后字符串的长度
        
        :param encoded: Huffman编码的二进制字符串
        :return: 解码后的原始字符串
        :raises ValueError: 如果编码字符串包含非法字符（非0/1）
        :raises RuntimeError: 如果Huffman树结构错误或编码不完整
        """
        # 使用列表存储解码结果，提高字符串拼接效率
        decoded = []
        # 当前在Huffman树中的位置，初始为根节点
        current = self.root
        
        # 遍历编码字符串中的每一位
        for bit in encoded:
            # 安全检查：确保当前节点不为空
            if current is None:
                raise RuntimeError("Huffman树结构错误或编码不完整")
                
            # 根据当前位的值在Huffman树中移动
            if bit == "0":
                # 遇到'0'，向左子树移动
                current = current.left
            elif bit == "1":
                # 遇到'1'，向右子树移动
                current = current.right
            else:
                # 异常处理：编码字符串包含非法字符
                raise ValueError(f"非法字符 '{bit}' 在编码中")
            
            # 到达叶节点：输出字符并重新从根节点开始
            if current is not None and current.is_leaf():
                # 确保character不为None（安全检查）
                if current.character is not None:
                    decoded.append(current.character)
                current = self.root
        
        # 返回解码结果
        return "".join(decoded)
    
    def get_code_table(self) -> Dict[str, str]:
        """
        获取编码表
        :return: 编码表
        """
        return self.codes.copy()
    
    @staticmethod
    def calculate_compression_ratio(original: int, compressed: int) -> float:
        """
        计算压缩率
        :param original: 原始数据大小（位）
        :param compressed: 压缩后数据大小（位）
        :return: 压缩率（百分比）
        """
        if original == 0:
            return 0
        return (1.0 - compressed / original) * 100
    
    @staticmethod
    def get_frequency_map(input_string: str) -> Dict[str, int]:
        """
        获取字符频率映射
        :param input_string: 输入字符串
        :return: 字符频率映射
        """
        return dict(Counter(input_string))


def main():
    """测试方法"""
    # 测试用例1：包含重复字符的字符串
    test1 = "ABRACADABRA"
    print(f"原始字符串: {test1}")
    print(f"字符频率: {HuffmanCoding.get_frequency_map(test1)}")
    
    hc1 = HuffmanCoding(test1)
    encoded1 = hc1.encode(test1)
    decoded1 = hc1.decode(encoded1)
    
    print(f"编码表: {hc1.get_code_table()}")
    print(f"编码结果: {encoded1}")
    print(f"解码结果: {decoded1}")
    print(f"编码解码是否正确: {test1 == decoded1}")
    print(f"原始大小: {len(test1) * 8} 位")
    print(f"压缩后大小: {len(encoded1)} 位")
    print(f"压缩率: {HuffmanCoding.calculate_compression_ratio(len(test1) * 8, len(encoded1)):.2f}%")
    print()
    
    # 测试用例2：更复杂的字符串
    test2 = "MISSISSIPPI"
    print(f"原始字符串: {test2}")
    print(f"字符频率: {HuffmanCoding.get_frequency_map(test2)}")
    
    hc2 = HuffmanCoding(test2)
    encoded2 = hc2.encode(test2)
    decoded2 = hc2.decode(encoded2)
    
    print(f"编码表: {hc2.get_code_table()}")
    print(f"编码结果: {encoded2}")
    print(f"解码结果: {decoded2}")
    print(f"编码解码是否正确: {test2 == decoded2}")
    print(f"原始大小: {len(test2) * 8} 位")
    print(f"压缩后大小: {len(encoded2)} 位")
    print(f"压缩率: {HuffmanCoding.calculate_compression_ratio(len(test2) * 8, len(encoded2)):.2f}%")
    print()
    
    # 测试用例3：均匀分布的字符串
    test3 = "ABCDEFGH"
    print(f"原始字符串: {test3}")
    print(f"字符频率: {HuffmanCoding.get_frequency_map(test3)}")
    
    hc3 = HuffmanCoding(test3)
    encoded3 = hc3.encode(test3)
    decoded3 = hc3.decode(encoded3)
    
    print(f"编码表: {hc3.get_code_table()}")
    print(f"编码结果: {encoded3}")
    print(f"解码结果: {decoded3}")
    print(f"编码解码是否正确: {test3 == decoded3}")
    print(f"原始大小: {len(test3) * 8} 位")
    print(f"压缩后大小: {len(encoded3)} 位")
    print(f"压缩率: {HuffmanCoding.calculate_compression_ratio(len(test3) * 8, len(encoded3)):.2f}%")
    print()
    
    # 测试用例4：边界情况 - 空字符串
    test4 = ""
    print(f"原始字符串: '{test4}'")
    try:
        hc4 = HuffmanCoding(test4)
        print("空字符串测试: 通过")
    except Exception as e:
        print(f"空字符串测试: 异常 - {e}")
    print()
    
    # 测试用例5：边界情况 - 单字符重复
    test5 = "AAAAAAA"
    print(f"原始字符串: {test5}")
    print(f"字符频率: {HuffmanCoding.get_frequency_map(test5)}")
    
    hc5 = HuffmanCoding(test5)
    encoded5 = hc5.encode(test5)
    decoded5 = hc5.decode(encoded5)
    
    print(f"编码表: {hc5.get_code_table()}")
    print(f"编码结果: {encoded5}")
    print(f"解码结果: {decoded5}")
    print(f"编码解码是否正确: {test5 == decoded5}")
    print(f"原始大小: {len(test5) * 8} 位")
    print(f"压缩后大小: {len(encoded5)} 位")
    print(f"压缩率: {HuffmanCoding.calculate_compression_ratio(len(test5) * 8, len(encoded5)):.2f}%")
    print()
    
    # 测试用例6：边界情况 - 两个不同字符
    test6 = "AB"
    print(f"原始字符串: {test6}")
    print(f"字符频率: {HuffmanCoding.get_frequency_map(test6)}")
    
    hc6 = HuffmanCoding(test6)
    encoded6 = hc6.encode(test6)
    decoded6 = hc6.decode(encoded6)
    
    print(f"编码表: {hc6.get_code_table()}")
    print(f"编码结果: {encoded6}")
    print(f"解码结果: {decoded6}")
    print(f"编码解码是否正确: {test6 == decoded6}")
    print(f"原始大小: {len(test6) * 8} 位")
    print(f"压缩后大小: {len(encoded6)} 位")
    print(f"压缩率: {HuffmanCoding.calculate_compression_ratio(len(test6) * 8, len(encoded6)):.2f}%")
    print()
    
    # 测试用例7：中文字符串
    test7 = "你好世界你好"
    print(f"原始字符串: {test7}")
    print(f"字符频率: {HuffmanCoding.get_frequency_map(test7)}")
    
    hc7 = HuffmanCoding(test7)
    encoded7 = hc7.encode(test7)
    decoded7 = hc7.decode(encoded7)
    
    print(f"编码表: {hc7.get_code_table()}")
    print(f"编码结果: {encoded7}")
    print(f"解码结果: {decoded7}")
    print(f"编码解码是否正确: {test7 == decoded7}")
    print(f"原始大小: {len(test7) * 16} 位")  # 中文字符通常占用16位
    print(f"压缩后大小: {len(encoded7)} 位")
    print(f"压缩率: {HuffmanCoding.calculate_compression_ratio(len(test7) * 16, len(encoded7)):.2f}%")


if __name__ == "__main__":
    main()
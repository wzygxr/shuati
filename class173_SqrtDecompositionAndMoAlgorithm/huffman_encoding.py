#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Huffman编码实现 (Python版本)

Huffman编码是一种无损数据压缩算法，它根据字符出现的频率为每个字符分配不同长度的编码，
频率高的字符分配较短的编码，频率低的字符分配较长的编码，从而实现数据压缩。

算法原理：
1. 统计输入数据中每个字符的频率
2. 构建Huffman树（最优二叉树）
3. 根据Huffman树生成每个字符的编码
4. 使用生成的编码对原始数据进行编码
5. 解码时根据Huffman树和编码还原原始数据

时间复杂度：
- 构建Huffman树：O(n log n)，其中n是不同字符的数量
- 编码：O(m)，其中m是输入数据的长度
- 解码：O(m)，其中m是编码后数据的长度

空间复杂度：O(n)，其中n是不同字符的数量

优势：
1. 压缩率高，特别是对于字符频率差异较大的数据
2. 实现相对简单
3. 解码过程确定且无歧义
4. 前缀编码特性保证了解码的唯一性

劣势：
1. 需要传输或存储Huffman树信息
2. 对于字符频率分布均匀的数据压缩效果不佳
3. 需要两次遍历数据（统计频率和编码）

应用场景：
1. 文件压缩（如ZIP格式）
2. 图像压缩（JPEG中的部分应用）
3. 网络传输数据压缩
"""

import heapq
from collections import defaultdict


class Node:
    """
    Huffman树节点
    """
    
    def __init__(self, frequency, character=None, left=None, right=None):
        """
        构造函数
        :param frequency: 频率
        :param character: 字符（仅叶节点有值）
        :param left: 左子树
        :param right: 右子树
        """
        self.frequency = frequency
        self.character = character
        self.left = left
        self.right = right
    
    def is_leaf(self):
        """
        判断是否为叶节点
        :return: 是否为叶节点
        """
        return self.left is None and self.right is None
    
    def __lt__(self, other):
        """
        比较方法，用于优先队列
        :param other: 另一个节点
        :return: 比较结果
        """
        return self.frequency < other.frequency
    
    def __le__(self, other):
        """
        比较方法，用于优先队列
        :param other: 另一个节点
        :return: 比较结果
        """
        return self.frequency <= other.frequency
    
    def __gt__(self, other):
        """
        比较方法，用于优先队列
        :param other: 另一个节点
        :return: 比较结果
        """
        return self.frequency > other.frequency
    
    def __ge__(self, other):
        """
        比较方法，用于优先队列
        :param other: 另一个节点
        :return: 比较结果
        """
        return self.frequency >= other.frequency


def build_decode_tree(huffman_codes):
    """
    根据编码表构建用于解码的Huffman树
    :param huffman_codes: Huffman编码表
    :return: Huffman树根节点
    """
    root = Node(0)
    
    for character, code in huffman_codes.items():
        current = root
        for bit in code:
            if bit == '0':
                if current.left is None:
                    current.left = Node(0)
                current = current.left
            else:
                if current.right is None:
                    current.right = Node(0)
                current = current.right
        # 设置叶节点的字符
        current.character = character
    
    return root


def decode(encoded_data, huffman_codes):
    """
    Huffman解码
    :param encoded_data: 编码后的数据
    :param huffman_codes: Huffman编码表
    :return: 解码后的原始数据
    """
    if not encoded_data:
        return ""
    
    # 特殊情况：只有一个字符
    if len(huffman_codes) == 1:
        single_char = next(iter(huffman_codes.keys()))
        return single_char * len(encoded_data)
    
    # 构建Huffman树用于解码
    root = build_decode_tree(huffman_codes)
    
    # 解码
    result = []
    current = root
    
    for bit in encoded_data:
        # 根据比特位移动到相应的子节点
        if bit == '0':
            if current.left is not None:
                current = current.left
        else:
            if current.right is not None:
                current = current.right
        
        # 如果到达叶节点，输出字符并回到根节点
        if current is not None and current.is_leaf():
            result.append(current.character)
            current = root
    
    return "".join(result)


class EncodeResult:
    """
    编码结果类
    """
    
    def __init__(self, encoded_data, huffman_codes):
        """
        构造函数
        :param encoded_data: 编码后的数据
        :param huffman_codes: Huffman编码表
        """
        self.encoded_data = encoded_data
        self.huffman_codes = huffman_codes


def build_huffman_tree(frequency_map):
    """
    构建Huffman树
    :param frequency_map: 字符频率映射
    :return: Huffman树的根节点
    """
    # 创建优先队列（最小堆）
    pq = []
    
    # 将所有字符节点加入优先队列
    for character, frequency in frequency_map.items():
        heapq.heappush(pq, Node(frequency, character))
    
    # 特殊情况：只有一个字符
    if len(pq) == 1:
        node = heapq.heappop(pq)
        return Node(node.frequency, None, node, None)
    
    # 构建Huffman树
    while len(pq) > 1:
        # 取出频率最小的两个节点
        left = heapq.heappop(pq)
        right = heapq.heappop(pq)
        
        # 创建新的内部节点
        parent = Node(left.frequency + right.frequency, None, left, right)
        
        # 将新节点加入优先队列
        heapq.heappush(pq, parent)
    
    # 返回根节点
    return heapq.heappop(pq)


def generate_huffman_codes(root):
    """
    生成Huffman编码表
    :param root: Huffman树根节点
    :return: 字符到编码的映射
    """
    huffman_codes = {}
    generate_codes(root, "", huffman_codes)
    return huffman_codes


def generate_codes(node, code, huffman_codes):
    """
    递归生成编码
    :param node: 当前节点
    :param code: 当前路径的编码
    :param huffman_codes: 编码表
    """
    if node is None:
        return
    
    # 如果是叶节点，保存编码
    if node.is_leaf():
        # 特殊情况：只有一个字符
        if not code:
            huffman_codes[node.character] = "0"
        else:
            huffman_codes[node.character] = code
    else:
        # 递归处理左右子树
        generate_codes(node.left, code + "0", huffman_codes)
        generate_codes(node.right, code + "1", huffman_codes)


def encode(input_string):
    """
    Huffman编码
    :param input_string: 输入字符串
    :return: 编码结果
    """
    if not input_string:
        return EncodeResult("", {})
    
    # 统计字符频率
    frequency_map = defaultdict(int)
    for char in input_string:
        frequency_map[char] += 1
    
    # 构建Huffman树
    root = build_huffman_tree(frequency_map)
    
    # 生成Huffman编码表
    huffman_codes = generate_huffman_codes(root)
    
    # 编码输入数据
    encoded_data = "".join(huffman_codes[char] for char in input_string)
    
    return EncodeResult(encoded_data, huffman_codes)


def calculate_compression_ratio(original, compressed):
    """
    计算压缩率
    :param original: 原始数据大小（字节）
    :param compressed: 压缩后数据大小（字节）
    :return: 压缩率（百分比）
    """
    if original == 0:
        return 0
    return (1.0 - compressed / original) * 100


def main():
    """
    测试方法
    """
    # 测试用例1：包含重复字符的字符串
    test1 = "ABRACADABRA"
    print(f"原始字符串: {test1}")
    print(f"原始长度: {len(test1)} 字符")
    
    encoded1 = encode(test1)
    print("Huffman编码表:")
    for char, code in encoded1.huffman_codes.items():
        if char == '\0':
            print(f"EOF: {code}")
        else:
            print(f"{char}: {code}")
    print(f"编码结果: {encoded1.encoded_data}")
    print(f"编码长度: {len(encoded1.encoded_data)} 位")
    
    decoded1 = decode(encoded1.encoded_data, encoded1.huffman_codes)
    print(f"解码结果: {decoded1}")
    print(f"编码解码是否正确: {test1 == decoded1}")
    print(f"压缩率: {calculate_compression_ratio(len(test1) * 8, len(encoded1.encoded_data)):.2f}%")
    print()
    
    # 测试用例2：包含重复模式的字符串
    test2 = "AAAAABBBBBCCCCCDDDDDEEEEE"
    print(f"原始字符串: {test2}")
    print(f"原始长度: {len(test2)} 字符")
    
    encoded2 = encode(test2)
    print("Huffman编码表:")
    for char, code in encoded2.huffman_codes.items():
        print(f"{char}: {code}")
    print(f"编码结果: {encoded2.encoded_data}")
    print(f"编码长度: {len(encoded2.encoded_data)} 位")
    
    decoded2 = decode(encoded2.encoded_data, encoded2.huffman_codes)
    print(f"解码结果: {decoded2}")
    print(f"编码解码是否正确: {test2 == decoded2}")
    print(f"压缩率: {calculate_compression_ratio(len(test2) * 8, len(encoded2.encoded_data)):.2f}%")
    print()
    
    # 测试用例3：随机字符串
    test3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    print(f"原始字符串: {test3}")
    print(f"原始长度: {len(test3)} 字符")
    
    encoded3 = encode(test3)
    print("Huffman编码表:")
    for char, code in encoded3.huffman_codes.items():
        print(f"{char}: {code}")
    print(f"编码结果: {encoded3.encoded_data}")
    print(f"编码长度: {len(encoded3.encoded_data)} 位")
    
    decoded3 = decode(encoded3.encoded_data, encoded3.huffman_codes)
    print(f"解码结果: {decoded3}")
    print(f"编码解码是否正确: {test3 == decoded3}")
    print(f"压缩率: {calculate_compression_ratio(len(test3) * 8, len(encoded3.encoded_data)):.2f}%")


if __name__ == "__main__":
    main()
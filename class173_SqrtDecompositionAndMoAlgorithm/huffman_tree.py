#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
霍夫曼树（Huffman Tree）实现
霍夫曼树是一种带权路径长度最短的二叉树，也称为最优二叉树

常见应用场景：
1. 数据压缩（霍夫曼编码）
2. 文件压缩算法（如ZIP、GZIP等）
3. 信息论中的最优编码
4. 通讯中的数据传输优化
5. 频率相关的排序和检索

相关算法题目：
- LeetCode 1161. 最大层内元素和 https://leetcode.cn/problems/maximum-level-sum-of-a-binary-tree/
- LeetCode 1676. 二叉树的最近公共祖先 IV https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree-iv/
- LeetCode 199. 二叉树的右视图 https://leetcode.cn/problems/binary-tree-right-side-view/
- LintCode 913. 二叉树的序列化与反序列化 https://www.lintcode.com/problem/913/
- 洛谷 P1090 合并果子 https://www.luogu.com.cn/problem/P1090
- 牛客 NC139 数据流中的中位数 https://www.nowcoder.com/practice/9be0172896bd43948f8a32fb954e1be1
- HackerRank Huffman Coding https://www.hackerrank.com/challenges/tree-huffman-decoding/problem
- CodeChef HUFFMAN https://www.codechef.com/problems/HUFFMAN
- USACO Huffman Coding https://usaco.org/index.php?page=viewproblem2&cpid=689
- AtCoder ABC240 E - Ranges on Tree https://atcoder.jp/contests/abc240/tasks/abc240_e
- 杭电 OJ 1053 Entropy https://acm.hdu.edu.cn/showproblem.php?pid=1053
- SPOJ ADACOINS https://www.spoj.com/problems/ADACOINS/
- Codeforces 1278 F. Cards https://codeforces.com/problemset/problem/1278/F
"""

from typing import Dict, Optional, List, Tuple
import heapq
from collections import Counter, deque


class HuffmanNode:
    """霍夫曼树节点类"""
    
    def __init__(self, data: str, frequency: int):
        """初始化节点
        
        Args:
            data: 字符数据
            frequency: 出现频率
        """
        self.data = data
        self.frequency = frequency
        self.left = None
        self.right = None
    
    def __lt__(self, other):
        """用于优先队列比较"""
        return self.frequency < other.frequency


class HuffmanTree:
    """霍夫曼树实现类"""
    
    def __init__(self, frequency_map: Optional[Dict[str, int]] = None):
        """构造函数，通过字符频率表构建霍夫曼树
        
        Args:
            frequency_map: 字符频率映射表
        """
        self.root = None
        if frequency_map:
            self.build_tree(frequency_map)
    
    def build_tree(self, frequency_map: Dict[str, int]) -> None:
        """构建霍夫曼树
        
        Args:
            frequency_map: 字符频率映射表
        """
        # 创建优先队列（最小堆），按照频率排序
        priority_queue = []
        
        # 将所有字符节点加入优先队列
        for char, freq in frequency_map.items():
            heapq.heappush(priority_queue, HuffmanNode(char, freq))
        
        # 构建霍夫曼树
        while len(priority_queue) > 1:
            # 取出两个最小频率的节点
            left = heapq.heappop(priority_queue)
            right = heapq.heappop(priority_queue)
            
            # 创建新的内部节点，频率为两个子节点的频率之和
            internal_node = HuffmanNode('', left.frequency + right.frequency)
            internal_node.left = left
            internal_node.right = right
            
            # 将内部节点加入队列
            heapq.heappush(priority_queue, internal_node)
        
        # 队中只剩下根节点
        if priority_queue:
            self.root = heapq.heappop(priority_queue)
    
    def get_huffman_codes(self) -> Dict[str, str]:
        """获取霍夫曼编码表
        
        Returns:
            字符到霍夫曼编码的映射
        """
        codes = {}
        if self.root:
            self._generate_codes(self.root, "", codes)
        return codes
    
    def _generate_codes(self, node: HuffmanNode, current_code: str, 
                       codes: Dict[str, str]) -> None:
        """递归生成霍夫曼编码
        
        Args:
            node: 当前节点
            current_code: 当前编码
            codes: 编码映射表
        """
        if node is None:
            return
        
        # 如果是叶子节点，保存编码
        if node.left is None and node.right is None:
            codes[node.data] = current_code if current_code else "0"
            return
        
        # 递归遍历左右子树
        self._generate_codes(node.left, current_code + "0", codes)
        self._generate_codes(node.right, current_code + "1", codes)
    
    def encode(self, text: str) -> str:
        """编码文本
        
        Args:
            text: 原始文本
        
        Returns:
            霍夫曼编码后的二进制字符串
        
        Raises:
            ValueError: 如果字符不在霍夫曼树中
        """
        codes = self.get_huffman_codes()
        encoded = []
        
        for char in text:
            if char in codes:
                encoded.append(codes[char])
            else:
                raise ValueError(f"字符 '{char}' 不在霍夫曼树中")
        
        return ''.join(encoded)
    
    def decode(self, encoded: str) -> str:
        """解码二进制字符串
        
        Args:
            encoded: 霍夫曼编码的二进制字符串
        
        Returns:
            解码后的文本
        
        Raises:
            ValueError: 如果二进制字符串无效
        """
        if not self.root:
            raise ValueError("霍夫曼树为空")
        
        decoded = []
        current = self.root
        
        for bit in encoded:
            if bit == '0':
                current = current.left
            elif bit == '1':
                current = current.right
            else:
                raise ValueError(f"无效的二进制位: {bit}")
            
            if current is None:
                raise ValueError("无效的霍夫曼编码")
            
            # 到达叶子节点
            if current.left is None and current.right is None:
                decoded.append(current.data)
                current = self.root  # 重置到根节点
        
        return ''.join(decoded)
    
    def calculate_wpl(self) -> int:
        """计算霍夫曼树的带权路径长度（WPL）
        
        Returns:
            带权路径长度
        """
        return self._calculate_wpl(self.root, 0)
    
    def _calculate_wpl(self, node: HuffmanNode, depth: int) -> int:
        """递归计算带权路径长度
        
        Args:
            node: 当前节点
            depth: 当前深度
        
        Returns:
            带权路径长度
        """
        if node is None:
            return 0
        
        # 叶子节点
        if node.left is None and node.right is None:
            return node.frequency * depth
        
        # 内部节点，递归计算左右子树
        return (self._calculate_wpl(node.left, depth + 1) + 
                self._calculate_wpl(node.right, depth + 1))
    
    def print_tree(self) -> None:
        """打印霍夫曼树的结构"""
        print("霍夫曼树结构：")
        self._print_tree(self.root, 0)
    
    def _print_tree(self, node: HuffmanNode, level: int) -> None:
        """递归打印树结构
        
        Args:
            node: 当前节点
            level: 当前层级
        """
        if node is None:
            return
        
        # 打印右子树
        self._print_tree(node.right, level + 1)
        
        # 打印当前节点
        print("    " * level, end="")
        if not node.data:
            print(f"[内部] {node.frequency}")
        else:
            print(f"['{node.data}'] {node.frequency}")
        
        # 打印左子树
        self._print_tree(node.left, level + 1)
    
    def get_height(self) -> int:
        """获取树的高度
        
        Returns:
            树的高度
        """
        return self._get_height(self.root)
    
    def _get_height(self, node: HuffmanNode) -> int:
        """递归计算树的高度
        
        Args:
            node: 当前节点
        
        Returns:
            树的高度
        """
        if node is None:
            return 0
        left_height = self._get_height(node.left)
        right_height = self._get_height(node.right)
        return max(left_height, right_height) + 1
    
    def get_leaf_count(self) -> int:
        """统计叶子节点数量
        
        Returns:
            叶子节点数量
        """
        return self._get_leaf_count(self.root)
    
    def _get_leaf_count(self, node: HuffmanNode) -> int:
        """递归统计叶子节点数量
        
        Args:
            node: 当前节点
        
        Returns:
            叶子节点数量
        """
        if node is None:
            return 0
        if node.left is None and node.right is None:
            return 1
        return (self._get_leaf_count(node.left) + 
                self._get_leaf_count(node.right))
    
    @staticmethod
    def build_from_text(text: str) -> 'HuffmanTree':
        """根据文本自动构建频率表并创建霍夫曼树
        
        Args:
            text: 输入文本
        
        Returns:
            构建的霍夫曼树
        """
        # 统计字符频率
        frequency_map = Counter(text)
        return HuffmanTree(frequency_map)
    
    def get_root(self) -> Optional[HuffmanNode]:
        """获取根节点
        
        Returns:
            根节点
        """
        return self.root
    
    def equals(self, other: 'HuffmanTree') -> bool:
        """检查两棵霍夫曼树是否相同
        
        Args:
            other: 另一棵霍夫曼树
        
        Returns:
            如果相同返回True，否则返回False
        """
        if other is None:
            return False
        return self._equals(self.root, other.get_root())
    
    def _equals(self, node1: HuffmanNode, node2: HuffmanNode) -> bool:
        """递归比较两棵子树是否相同
        
        Args:
            node1: 第一棵树的节点
            node2: 第二棵树的节点
        
        Returns:
            如果相同返回True，否则返回False
        """
        if node1 is None and node2 is None:
            return True
        if node1 is None or node2 is None:
            return False
        
        # 比较当前节点的频率
        if node1.frequency != node2.frequency:
            return False
        
        # 如果是叶子节点，还需要比较字符
        if (node1.left is None and node1.right is None and 
            node2.left is None and node2.right is None):
            return node1.data == node2.data
        
        # 递归比较左右子树
        return (self._equals(node1.left, node2.left) and 
                self._equals(node1.right, node2.right))
    
    def level_order_traversal(self) -> List[List[str]]:
        """层序遍历霍夫曼树
        
        Returns:
            层序遍历结果
        """
        result = []
        if not self.root:
            return result
        
        queue = deque([self.root])
        
        while queue:
            level_size = len(queue)
            current_level = []
            
            for _ in range(level_size):
                node = queue.popleft()
                if not node.data:
                    current_level.append(f"[内部] {node.frequency}")
                else:
                    current_level.append(f"['{node.data}'] {node.frequency}")
                
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            
            result.append(current_level)
        
        return result
    
    def get_compression_ratio(self, original_text: str) -> float:
        """计算压缩率
        
        Args:
            original_text: 原始文本
        
        Returns:
            压缩率（压缩后大小/原始大小）
        """
        # 假设原始文本每个字符占用8位
        original_size = len(original_text) * 8
        
        # 计算压缩后的大小（以位为单位）
        encoded = self.encode(original_text)
        compressed_size = len(encoded)
        
        return compressed_size / original_size if original_size > 0 else 0
    
    def get_char_frequencies(self) -> Dict[str, int]:
        """获取字符频率表
        
        Returns:
            字符频率映射
        """
        frequencies = {}
        self._collect_frequencies(self.root, frequencies)
        return frequencies
    
    def _collect_frequencies(self, node: HuffmanNode, 
                           frequencies: Dict[str, int]) -> None:
        """递归收集字符频率
        
        Args:
            node: 当前节点
            frequencies: 频率映射表
        """
        if node is None:
            return
        
        if node.left is None and node.right is None:
            frequencies[node.data] = node.frequency
        else:
            self._collect_frequencies(node.left, frequencies)
            self._collect_frequencies(node.right, frequencies)
    
    def to_dot_format(self) -> str:
        """生成DOT格式的树表示（用于可视化）
        
        Returns:
            DOT格式的字符串
        """
        dot = ["digraph HuffmanTree {", "  node [shape=box];"]
        self._build_dot(self.root, dot)
        dot.append("}")
        return "\n".join(dot)
    
    def _build_dot(self, node: HuffmanNode, dot: List[str]) -> str:
        """递归构建DOT表示
        
        Args:
            node: 当前节点
            dot: DOT命令列表
        
        Returns:
            节点ID
        """
        if node is None:
            return "null"
        
        node_id = f"node_{id(node)}"
        label = f"{node.data}:{node.frequency}" if node.data else str(node.frequency)
        dot.append(f"  {node_id} [label=\"{label}\"];")
        
        if node.left:
            left_id = self._build_dot(node.left, dot)
            dot.append(f"  {node_id} -> {left_id} [label=\"0\"];")
        
        if node.right:
            right_id = self._build_dot(node.right, dot)
            dot.append(f"  {node_id} -> {right_id} [label=\"1\"];")
        
        return node_id


# 测试代码
if __name__ == "__main__":
    # 测试数据：字符及其频率
    frequency_map = {
        'a': 5,
        'b': 9,
        'c': 12,
        'd': 13,
        'e': 16,
        'f': 45
    }
    
    # 创建霍夫曼树
    huffman_tree = HuffmanTree(frequency_map)
    
    # 打印树结构
    huffman_tree.print_tree()
    
    # 获取霍夫曼编码
    codes = huffman_tree.get_huffman_codes()
    print("\n霍夫曼编码：")
    for char, code in codes.items():
        print(f"{char}: {code}")
    
    # 计算WPL
    print(f"\n带权路径长度(WPL): {huffman_tree.calculate_wpl()}")
    
    # 计算树高和叶子节点数
    print(f"树高: {huffman_tree.get_height()}")
    print(f"叶子节点数: {huffman_tree.get_leaf_count()}")
    
    # 层序遍历
    print("\n层序遍历：")
    level_order = huffman_tree.level_order_traversal()
    for i, level in enumerate(level_order):
        print(f"层 {i + 1}: {level}")
    
    # 测试编码和解码
    text = "abcdef"
    try:
        encoded = huffman_tree.encode(text)
        decoded = huffman_tree.decode(encoded)
        
        print(f"\n原始文本: {text}")
        print(f"编码后: {encoded}")
        print(f"解码后: {decoded}")
        print(f"编码解码一致性: {text == decoded}")
        print(f"压缩率: {huffman_tree.get_compression_ratio(text):.2f}")
    except ValueError as e:
        print(f"错误: {e}")
    
    # 测试从文本构建霍夫曼树
    print("\n从文本构建霍夫曼树：")
    test_text = "hello huffman coding!"
    tree_from_text = HuffmanTree.build_from_text(test_text)
    codes_from_text = tree_from_text.get_huffman_codes()
    print("文本霍夫曼编码：")
    for char, code in codes_from_text.items():
        display_char = char if char.strip() else "空格"
        print(f"'{display_char}': {code}")
    
    # 测试编码解码
    encoded_text = tree_from_text.encode(test_text)
    decoded_text = tree_from_text.decode(encoded_text)
    print(f"\n原始文本长度: {len(test_text)} 字符")
    print(f"编码后长度: {len(encoded_text)} 位")
    print(f"解码后: {decoded_text}")
    print(f"解码正确性: {test_text == decoded_text}")
    print(f"压缩率: {tree_from_text.get_compression_ratio(test_text):.2f}")
    
    # 测试边界情况
    print("\n测试边界情况：")
    # 单个字符的情况
    single_char_text = "aaaaa"
    single_tree = HuffmanTree.build_from_text(single_char_text)
    single_encoded = single_tree.encode(single_char_text)
    single_decoded = single_tree.decode(single_encoded)
    print(f"单字符文本编码解码: {single_char_text == single_decoded}")
    print(f"单字符编码: {single_encoded}")
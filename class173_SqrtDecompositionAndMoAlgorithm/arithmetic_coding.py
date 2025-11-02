#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
算术编码实现 (Python版本)

算术编码是一种无损数据压缩方法，它将整个输入消息编码为一个位于[0,1)区间内的实数。

算法原理：
1. 统计字符频率，构建概率模型
2. 根据概率模型构建累积分布函数(CDF)
3. 对输入字符串进行编码，将整个字符串映射到[0,1)区间的一个子区间
4. 解码时根据相同的概率模型和编码值还原原始字符串

时间复杂度：
- 编码：O(n)，其中n是输入字符串长度
- 解码：O(n)，其中n是输出字符串长度

空间复杂度：O(k)，其中k是不同字符的数量

优势：
1. 压缩率高，可以达到信息熵的理论极限
2. 可以处理任意精度的概率
3. 适合处理具有明显统计特性的数据

劣势：
1. 实现复杂，需要处理浮点数精度问题
2. 编码和解码必须使用相同的概率模型
3. 对于短字符串，可能不如其他简单编码方法高效

应用场景：
1. 图像压缩（JPEG）
2. 音频压缩
3. 数据压缩标准
"""


class ArithmeticCoding:
    """
    算术编码类
    """
    
    def __init__(self, input_string):
        """
        构造函数，根据输入字符串构建概率模型
        :param input_string: 输入字符串
        """
        self.frequency_map = {}
        self.cumulative_freq = {}
        self.total_freq = 0
        self._build_frequency_map(input_string)
        self._build_cumulative_frequency()
    
    def _build_frequency_map(self, input_string):
        """
        统计字符频率
        :param input_string: 输入字符串
        """
        for char in input_string:
            self.frequency_map[char] = self.frequency_map.get(char, 0) + 1
        # 添加EOF字符，用于解码时确定结束位置
        self.frequency_map['\0'] = 1  # 使用空字符作为EOF
    
    def _build_cumulative_frequency(self):
        """
        构建累积分布函数
        """
        # 按字符排序，确保编码和解码使用相同的顺序
        sorted_chars = sorted(self.frequency_map.keys())
        self.total_freq = 0
        
        for char in sorted_chars:
            self.cumulative_freq[char] = self.total_freq
            self.total_freq += self.frequency_map[char]
    
    def encode(self, input_string):
        """
        算术编码
        :param input_string: 输入字符串
        :return: 编码结果（低值和高值）
        """
        # 初始化区间为[0, 1)
        low = 0.0
        high = 1.0
        
        # 为输入添加EOF字符
        input_string += '\0'
        
        # 对每个字符进行编码
        for char in input_string:
            # 计算当前区间的范围
            range_val = high - low
            
            # 获取字符的概率区间
            symbol_low = self.cumulative_freq[char]
            symbol_high = symbol_low + self.frequency_map[char]
            
            # 缩小区间
            high = low + range_val * symbol_high / self.total_freq
            low = low + range_val * symbol_low / self.total_freq
        
        return CodeResult(low, high)
    
    def decode(self, code_result, max_length):
        """
        算术解码
        :param code_result: 编码结果
        :param max_length: 最大解码长度（防止无限循环）
        :return: 解码结果
        """
        result = []
        # 使用区间的中点作为解码值
        value = (code_result.low + code_result.high) / 2
        
        low = 0.0
        high = 1.0
        
        while len(result) < max_length:
            range_val = high - low
            
            # 查找对应的字符
            found_char = None
            
            for char, symbol_low in self.cumulative_freq.items():
                symbol_high = symbol_low + self.frequency_map[char]
                
                symbol_low_value = low + range_val * symbol_low / self.total_freq
                symbol_high_value = low + range_val * symbol_high / self.total_freq
                
                if symbol_low_value <= value < symbol_high_value:
                    found_char = char
                    low = symbol_low_value
                    high = symbol_high_value
                    break
            
            if found_char is None:
                break
            
            # 如果是EOF字符，结束解码
            if found_char == '\0':
                break
            
            result.append(found_char)
        
        return ''.join(result)
    
    def get_frequency_map(self):
        """
        获取字符频率映射（用于调试和分析）
        :return: 字符频率映射
        """
        return self.frequency_map.copy()


class CodeResult:
    """
    编码结果类
    """
    
    def __init__(self, low, high):
        self.low = low
        self.high = high
    
    def __str__(self):
        return f"[{self.low:.10f}, {self.high:.10f})"


def main():
    """
    测试方法
    """
    # 测试用例1：简单字符串
    test1 = "ABRACADABRA"
    print(f"原始字符串: {test1}")
    
    ac = ArithmeticCoding(test1)
    encoded = ac.encode(test1)
    print(f"编码结果: {encoded}")
    
    decoded = ac.decode(encoded, len(test1) + 1)
    print(f"解码结果: {decoded}")
    print(f"编码解码是否正确: {test1 == decoded}")
    print()
    
    # 测试用例2：包含重复字符的字符串
    test2 = "AAAAABBBBBCCCCC"
    print(f"原始字符串: {test2}")
    
    ac2 = ArithmeticCoding(test2)
    encoded2 = ac2.encode(test2)
    print(f"编码结果: {encoded2}")
    
    decoded2 = ac2.decode(encoded2, len(test2) + 1)
    print(f"解码结果: {decoded2}")
    print(f"编码解码是否正确: {test2 == decoded2}")
    print()
    
    # 显示字符频率
    print("字符频率:")
    freq_map = ac2.get_frequency_map()
    for char, freq in sorted(freq_map.items()):
        if char == '\0':
            print(f"EOF: {freq}")
        else:
            print(f"{char}: {freq}")


if __name__ == "__main__":
    main()
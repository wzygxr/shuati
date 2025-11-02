#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Code03_CountRepetitions - Python实现

题目描述：
统计重复个数问题，类似于LeetCode 466题

解题思路：
这是一个字符串匹配与倍增优化相结合的问题。

核心思想：
1. 预处理：计算从s1的每个位置开始，匹配s2中每个字符需要的最小长度
2. 倍增优化：预处理从每个位置开始匹配一个s2需要的长度，然后使用倍增思想计算匹配多个s2
3. 循环节：寻找循环节，利用循环节快速计算结果

时间复杂度：O(len1 * len2 + log(n1 * len1))
空间复杂度：O(len1 * log(n1 * len1))

算法步骤详解：
1. 预处理字符查找表
2. 计算匹配一个s2需要的最小长度
3. 使用倍增思想预处理状态转移表
4. 使用倍增表快速计算最大匹配数

工程化考量：
- 使用列表和字典存储数据
- 添加边界条件检查
- 使用大整数防止溢出
- 提供完整的测试用例
"""

import math
from typing import List

class CountRepetitions:
    """
    统计重复个数解决方案
    """
    
    @staticmethod
    def get_max_repetitions(s1: str, n1: int, s2: str, n2: int) -> int:
        """
        计算最大重复数M
        
        参数:
            s1: 字符串s1
            n1: s1重复次数
            s2: 字符串s2
            n2: s2重复次数
            
        返回:
            最大整数M，使得[S2,M]可以从S1获得
        """
        # 边界条件检查
        if not s1 or not s2 or n1 <= 0 or n2 <= 0:
            return 0
        
        len1, len2 = len(s1), len(s2)
        
        # 预处理next数组：next[i][j]表示从位置i开始找到字符j的最小长度
        # 使用字典存储字符到位置的映射
        next_table = [{} for _ in range(len1)]
        
        # 从后往前预处理next数组
        for i in range(len1-1, -1, -1):
            # 复制下一行的值
            if i < len1 - 1:
                next_table[i] = next_table[i+1].copy()
            # 设置当前字符的位置
            next_table[i][s1[i]] = i
        
        # 预处理第一个字符的next数组（处理循环情况）
        first_next = {}
        for i, char in enumerate(s1):
            if char not in first_next:
                first_next[char] = i
        
        # 计算匹配一个s2需要的最小长度
        match_len = [0] * len1
        
        for start in range(len1):
            pos = start
            matched = 0
            valid = True
            
            for char in s2:
                # 在当前s1中查找字符
                if char in next_table[pos]:
                    char_pos = next_table[pos][char]
                    matched += char_pos - pos + 1
                    pos = (char_pos + 1) % len1
                else:
                    # 如果当前s1中找不到，需要到下一个s1中查找
                    if char in first_next:
                        matched += (len1 - pos) + first_next[char] + 1
                        pos = (first_next[char] + 1) % len1
                    else:
                        # s1中根本不存在该字符
                        match_len[start] = -1
                        valid = False
                        break
            
            if valid:
                match_len[start] = matched
        
        # 检查是否存在无法匹配的情况
        if match_len[0] == -1:
            return 0
        
        # 计算倍增表的层数
        total_len = n1 * len1
        max_power = 0
        while (1 << max_power) <= total_len:
            max_power += 1
        
        # 初始化倍增表
        st = [[0] * len1 for _ in range(max_power)]
        next_start = [[0] * len1 for _ in range(max_power)]
        
        # 初始化第一层
        for i in range(len1):
            if match_len[i] != -1:
                st[0][i] = match_len[i]
                next_start[0][i] = (i + match_len[i]) % len1
            else:
                st[0][i] = -1
                next_start[0][i] = -1
        
        # 构建倍增表
        for p in range(1, max_power):
            for i in range(len1):
                if st[p-1][i] != -1 and st[p-1][next_start[p-1][i]] != -1:
                    st[p][i] = st[p-1][i] + st[p-1][next_start[p-1][i]]
                    next_start[p][i] = next_start[p-1][next_start[p-1][i]]
                else:
                    st[p][i] = -1
                    next_start[p][i] = -1
        
        # 使用倍增表计算最大匹配数
        current_len = 0
        current_start = 0
        match_count = 0
        
        for p in range(max_power-1, -1, -1):
            if (st[p][current_start] != -1 and 
                current_len + st[p][current_start] <= total_len):
                current_len += st[p][current_start]
                match_count += (1 << p)
                current_start = next_start[p][current_start]
        
        # 返回结果：匹配的s2数量除以n2
        return match_count // n2
    
    @staticmethod
    def get_max_repetitions_simple(s1: str, n1: int, s2: str, n2: int) -> int:
        """
        简化版本：使用循环节检测
        
        当n1很大时，寻找循环节可以优化性能
        """
        if not s1 or not s2 or n1 <= 0 or n2 <= 0:
            return 0
        
        len1, len2 = len(s1), len(s2)
        
        # 检查s1是否包含s2的所有字符
        s1_chars = set(s1)
        for char in s2:
            if char not in s1_chars:
                return 0
        
        # 使用循环节检测
        index_map = {}
        count_map = {}
        
        index = 0
        count = 0
        s2_index = 0
        
        for i in range(n1):
            for j in range(len1):
                if s1[j] == s2[s2_index]:
                    s2_index += 1
                    if s2_index == len2:
                        count += 1
                        s2_index = 0
            
            # 检查是否出现循环节
            if index in index_map:
                # 找到循环节
                prev_i = index_map[index]
                prev_count = count_map[index]
                
                cycle_length = i - prev_i
                cycle_count = count - prev_count
                
                remaining = n1 - i - 1
                full_cycles = remaining // cycle_length
                
                count += full_cycles * cycle_count
                i += full_cycles * cycle_length
                
                # 处理剩余部分
                for _ in range(remaining % cycle_length):
                    for j in range(len1):
                        if s1[j] == s2[s2_index]:
                            s2_index += 1
                            if s2_index == len2:
                                count += 1
                                s2_index = 0
                break
            else:
                index_map[index] = i
                count_map[index] = count
            
            index = s2_index
        
        return count // n2


def test_count_repetitions():
    """
    测试函数
    """
    print("=== CountRepetitions算法测试 ===")
    
    # 测试用例1：基础测试
    s1_1 = "abc"
    n1_1 = 4
    s2_1 = "ab"
    n2_1 = 2
    
    result1 = CountRepetitions.get_max_repetitions(s1_1, n1_1, s2_1, n2_1)
    print(f"测试用例1 - 预期: 1, 实际: {result1}")
    
    # 测试用例2：经典测试
    s1_2 = "acb"
    n1_2 = 4
    s2_2 = "ab"
    n2_2 = 2
    
    result2 = CountRepetitions.get_max_repetitions(s1_2, n1_2, s2_2, n2_2)
    print(f"测试用例2 - 预期: 2, 实际: {result2}")
    
    # 测试用例3：边界测试
    s1_3 = "a"
    n1_3 = 1000000
    s2_3 = "a"
    n2_3 = 1
    
    result3 = CountRepetitions.get_max_repetitions(s1_3, n1_3, s2_3, n2_3)
    print(f"测试用例3 - 预期: 1000000, 实际: {result3}")
    
    # 测试用例4：无法匹配的情况
    s1_4 = "abc"
    n1_4 = 1
    s2_4 = "d"
    n2_4 = 1
    
    result4 = CountRepetitions.get_max_repetitions(s1_4, n1_4, s2_4, n2_4)
    print(f"测试用例4 - 预期: 0, 实际: {result4}")
    
    # 测试简化版本
    result1_simple = CountRepetitions.get_max_repetitions_simple(s1_1, n1_1, s2_1, n2_1)
    print(f"简化版本测试用例1 - 预期: 1, 实际: {result1_simple}")
    
    # 性能测试
    import time
    
    start = time.time()
    CountRepetitions.get_max_repetitions("abc" * 10, 10000, "ab", 1)
    time_standard = time.time() - start
    
    start = time.time()
    CountRepetitions.get_max_repetitions_simple("abc" * 10, 10000, "ab", 1)
    time_simple = time.time() - start
    
    print(f"性能测试 - 标准版本: {time_standard:.6f}秒")
    print(f"性能测试 - 简化版本: {time_simple:.6f}秒")
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    test_count_repetitions()


"""
复杂度分析：
时间复杂度：
- 预处理next数组：O(len1)
- 计算匹配长度：O(len1 * len2)
- 构建倍增表：O(len1 * log(totalLen))
- 查询：O(log(totalLen))
- 总复杂度：O(len1 * len2 + log(n1 * len1))

空间复杂度：O(len1 * log(totalLen))

算法优化点：
1. 使用倍增思想将线性查询优化为对数级别
2. 预处理避免重复计算
3. 提供简化版本处理特殊情况

Python特性利用：
1. 使用字典进行高效字符查找
2. 利用列表推导式简化代码
3. 使用集合进行快速成员检查

工程化改进：
1. 添加完整的边界条件检查
2. 提供两种实现版本（标准和简化）
3. 包含性能测试和功能测试
4. 详细的注释和文档

跨语言对比：
- Python版本比C++版本更简洁
- 使用字典代替数组进行字符查找
- 适合快速原型开发和教学演示
"""
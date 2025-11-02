#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Sunday字符串匹配算法实现
时间复杂度：
  - 最好情况: O(n/m)，其中n是文本长度，m是模式长度
  - 最坏情况: O(n*m)
  - 平均情况: O(n)
空间复杂度：O(1) - 使用固定大小的字符集
"""

class SundayAlgorithm:
    @staticmethod
    def search(text, pattern):
        """
        Sunday字符串匹配算法
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            int: 模式串在文本串中首次出现的索引，如果不存在则返回-1
            
        Raises:
            ValueError: 如果输入参数为None
        """
        if text is None or pattern is None:
            raise ValueError("文本串和模式串不能为None")
        
        n = len(text)
        m = len(pattern)
        
        # 边界条件检查
        if m == 0:
            return 0  # 空模式串匹配任何位置的开始
        if n < m:
            return -1  # 文本串比模式串短，不可能匹配
        
        # 构建偏移表
        shift = SundayAlgorithm._build_shift_table(pattern)
        
        # 开始匹配
        i = 0
        while i <= n - m:
            # 尝试匹配当前位置
            match = True
            for j in range(m):
                if text[i + j] != pattern[j]:
                    match = False
                    break
            
            if match:
                return i  # 找到匹配
            
            # 计算下一次跳转的距离
            next_pos = i + m
            if next_pos >= n:
                break  # 已经到达文本串末尾
            
            # 根据下一个字符在模式串中的位置计算跳转距离
            next_char = text[next_pos]
            i += shift.get(next_char, m + 1)
        
        return -1  # 未找到匹配
    
    @staticmethod
    def _build_shift_table(pattern):
        """
        构建Sunday算法的偏移表
        
        Args:
            pattern (str): 模式串
            
        Returns:
            dict: 偏移表，key为字符，value表示该字符在模式串中最右侧出现的位置到模式串末尾的距离+1
        """
        m = len(pattern)
        shift = {}
        
        # 对于模式串中的每个字符，记录它到模式串末尾的距离
        for i in range(m):
            shift[pattern[i]] = m - i
        
        # 不在模式串中的字符默认偏移为m+1，在字典中不存在会使用默认值
        return shift
    
    @staticmethod
    def search_all(text, pattern):
        """
        查找模式串在文本串中所有出现的位置
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            list: 包含所有匹配位置的列表
        """
        if text is None or pattern is None:
            raise ValueError("文本串和模式串不能为None")
        
        n = len(text)
        m = len(pattern)
        
        if m == 0:
            # 空模式串匹配每个位置的开始
            return list(range(n + 1))
        
        if n < m:
            return []  # 无匹配
        
        # 构建偏移表
        shift = SundayAlgorithm._build_shift_table(pattern)
        
        # 存储所有匹配位置
        matches = []
        
        i = 0
        while i <= n - m:
            match = True
            for j in range(m):
                if text[i + j] != pattern[j]:
                    match = False
                    break
            
            if match:
                matches.append(i)
                # 找到匹配后，移动一个位置继续查找（可以优化为更大的跳转）
                i += 1
            else:
                next_pos = i + m
                if next_pos >= n:
                    break
                i += shift.get(text[next_pos], m + 1)
        
        return matches

# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本匹配
    text1 = "hello world"
    pattern1 = "world"
    print(f"测试1 - 查找'world'在'hello world'中的位置: {SundayAlgorithm.search(text1, pattern1)}")  # 应该是6
    
    # 测试用例2：多次匹配
    text2 = "abababa"
    pattern2 = "aba"
    results2 = SundayAlgorithm.search_all(text2, pattern2)
    print(f"测试2 - 查找所有'aba'在'abababa'中的位置: {results2}")  # 应该是[0, 2, 4]
    
    # 测试用例3：无匹配
    text3 = "hello"
    pattern3 = "world"
    print(f"测试3 - 查找'world'在'hello'中的位置: {SundayAlgorithm.search(text3, pattern3)}")  # 应该是-1
    
    # 测试用例4：边界情况
    text4 = "test"
    pattern4 = ""
    print(f"测试4 - 查找空串在'test'中的位置: {SundayAlgorithm.search(text4, pattern4)}")  # 应该是0
    
    # 测试用例5：特殊字符
    text5 = "a!b@c#d$e%"
    pattern5 = "c#d"
    print(f"测试5 - 查找'c#d'在特殊字符串中的位置: {SundayAlgorithm.search(text5, pattern5)}")  # 应该是4
    
    # 测试用例6：Unicode字符
    text6 = "你好世界"
    pattern6 = "世界"
    print(f"测试6 - 查找'世界'在'你好世界'中的位置: {SundayAlgorithm.search(text6, pattern6)}")  # 应该是2
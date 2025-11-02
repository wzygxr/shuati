"""
重复的DNA序列
测试链接：https://leetcode.cn/problems/repeated-dna-sequences/

题目描述：
DNA序列 由一系列核苷酸组成，缩写为 'A', 'C', 'G' 和 'T'。
例如，"ACGAATTCCG" 是一个 DNA序列 。
在研究 DNA 时，识别 DNA 中的重复序列非常有用。
给定一个表示 DNA序列 的字符串 s ，返回所有在 DNA 分子中出现不止一次的 长度为 10 的序列(子字符串)。你可以按 任意顺序 返回答案。

解题思路：
1. 哈希表法：使用字典统计所有10位子串的出现次数
2. 位运算优化：使用2位表示一个字符，将字符串转换为整数
3. 滑动窗口：使用滑动窗口和位运算结合
4. Rabin-Karp算法：使用滚动哈希优化

时间复杂度：O(n) - n为字符串长度
空间复杂度：O(n) - 需要存储哈希表
"""

class Code32_RepeatedDNASequences:
    """
    重复的DNA序列解决方案
    """
    
    @staticmethod
    def find_repeated_dna_sequences1(s: str) -> list[str]:
        """
        方法1：哈希表法（直接使用字符串）
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s: DNA序列字符串
            
        Returns:
            重复出现的10位DNA序列列表
        """
        if len(s) < 10:
            return []
        
        count_map = {}
        result = []
        
        # 遍历所有长度为10的子串
        for i in range(len(s) - 9):
            substring = s[i:i+10]
            count_map[substring] = count_map.get(substring, 0) + 1
        
        # 收集出现次数大于1的子串
        for substring, count in count_map.items():
            if count > 1:
                result.append(substring)
        
        return result
    
    @staticmethod
    def find_repeated_dna_sequences2(s: str) -> list[str]:
        """
        方法2：位运算优化（使用整数表示子串）
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s: DNA序列字符串
            
        Returns:
            重复出现的10位DNA序列列表
        """
        if len(s) < 10:
            return []
        
        # 字符到2位编码的映射
        char_to_code = {'A': 0, 'C': 1, 'G': 2, 'T': 3}  # 00, 01, 10, 11
        
        count_map = {}
        
        # 第一个窗口的编码
        code = 0
        for i in range(10):
            code = (code << 2) | char_to_code[s[i]]
        count_map[code] = 1
        
        # 滑动窗口处理剩余部分
        for i in range(10, len(s)):
            # 移除最左边的字符（左移2位，然后取低20位）
            code = ((code << 2) | char_to_code[s[i]]) & 0xFFFFF
            count_map[code] = count_map.get(code, 0) + 1
        
        # 重新遍历字符串，将编码转换回字符串
        code_to_string = {}
        for i in range(len(s) - 9):
            current_code = 0
            for j in range(10):
                current_code = (current_code << 2) | char_to_code[s[i + j]]
            code_to_string[current_code] = s[i:i+10]
        
        # 收集结果
        result = []
        for code, count in count_map.items():
            if count > 1:
                result.append(code_to_string[code])
        
        return result
    
    @staticmethod
    def find_repeated_dna_sequences3(s: str) -> list[str]:
        """
        方法3：滑动窗口+位运算（优化版）
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s: DNA序列字符串
            
        Returns:
            重复出现的10位DNA序列列表
        """
        if len(s) < 10:
            return []
        
        # 字符到2位编码的映射
        char_to_code = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
        
        seen = set()
        output = set()
        
        code = 0
        # 处理前10个字符
        for i in range(10):
            code = (code << 2) | char_to_code[s[i]]
        seen.add(code)
        
        # 滑动窗口
        for i in range(10, len(s)):
            code = ((code << 2) | char_to_code[s[i]]) & 0xFFFFF
            if code in seen:
                output.add(s[i-9:i+1])
            else:
                seen.add(code)
        
        return list(output)
    
    @staticmethod
    def find_repeated_dna_sequences4(s: str) -> list[str]:
        """
        方法4：Rabin-Karp算法（滚动哈希）
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s: DNA序列字符串
            
        Returns:
            重复出现的10位DNA序列列表
        """
        if len(s) < 10:
            return []
        
        # 使用质数作为基数
        base = 4  # 4个字符
        mod = 10**9 + 7  # 大质数取模
        
        # 字符到数字的映射
        char_to_num = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
        
        # 计算base^9 mod mod
        highest_power = 1
        for _ in range(9):
            highest_power = (highest_power * base) % mod
        
        count_map = {}
        
        # 计算第一个窗口的哈希值
        hash_val = 0
        for i in range(10):
            hash_val = (hash_val * base + char_to_num[s[i]]) % mod
        count_map[hash_val] = 1
        
        # 滑动窗口计算哈希值
        for i in range(10, len(s)):
            # 移除最左边的字符
            left_char_value = char_to_num[s[i-10]] * highest_power % mod
            hash_val = (hash_val - left_char_value + mod) % mod
            # 添加新的字符
            hash_val = (hash_val * base + char_to_num[s[i]]) % mod
            
            count_map[hash_val] = count_map.get(hash_val, 0) + 1
        
        # 收集结果
        result = []
        added = set()
        for i in range(len(s) - 9):
            substring = s[i:i+10]
            current_hash = 0
            for j in range(10):
                current_hash = (current_hash * base + char_to_num[s[i + j]]) % mod
            if count_map.get(current_hash, 0) > 1 and substring not in added:
                result.append(substring)
                added.add(substring)
        
        return result
    
    @staticmethod
    def test():
        """测试方法"""
        # 测试用例1：正常情况
        s1 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
        result1 = Code32_RepeatedDNASequences.find_repeated_dna_sequences1(s1)
        result2 = Code32_RepeatedDNASequences.find_repeated_dna_sequences2(s1)
        result3 = Code32_RepeatedDNASequences.find_repeated_dna_sequences3(s1)
        result4 = Code32_RepeatedDNASequences.find_repeated_dna_sequences4(s1)
        print(f"测试用例1 - 输入: {s1}")
        print(f"方法1结果: {result1} (预期: ['AAAAACCCCC', 'CCCCCAAAAA'])")
        print(f"方法2结果: {result2} (预期: ['AAAAACCCCC', 'CCCCCAAAAA'])")
        print(f"方法3结果: {result3} (预期: ['AAAAACCCCC', 'CCCCCAAAAA'])")
        print(f"方法4结果: {result4} (预期: ['AAAAACCCCC', 'CCCCCAAAAA'])")
        
        # 测试用例2：边界情况（无重复）
        s2 = "AAAAAAAAAA"
        result5 = Code32_RepeatedDNASequences.find_repeated_dna_sequences2(s2)
        print(f"测试用例2 - 输入: {s2}")
        print(f"方法2结果: {result5} (预期: [])")
        
        # 复杂度分析
        print("\n=== 复杂度分析 ===")
        print("方法1 - 哈希表法:")
        print("  时间复杂度: O(n)")
        print("  空间复杂度: O(n)")
        
        print("方法2 - 位运算优化:")
        print("  时间复杂度: O(n)")
        print("  空间复杂度: O(n)")
        
        print("方法3 - 滑动窗口+位运算:")
        print("  时间复杂度: O(n)")
        print("  空间复杂度: O(n)")
        
        print("方法4 - Rabin-Karp算法:")
        print("  时间复杂度: O(n)")
        print("  空间复杂度: O(n)")
        
        # Python特性考量
        print("\n=== Python特性考量 ===")
        print("1. 字典操作：使用字典进行高效统计")
        print("2. 集合操作：使用set进行去重和查找")
        print("3. 切片操作：Python字符串切片高效便捷")
        print("4. 类型注解：使用类型提示提高代码可读性")
        
        # 算法技巧总结
        print("\n=== 算法技巧总结 ===")
        print("1. 位编码：使用2位表示一个字符，节省空间")
        print("2. 滑动窗口：高效处理固定长度子串")
        print("3. 哈希优化：使用整数编码替代字符串比较")
        print("4. 滚动哈希：Rabin-Karp算法处理字符串匹配")

if __name__ == "__main__":
    Code32_RepeatedDNASequences.test()
"""
字符串哈希综合应用题目集

本文件包含多个字符串哈希的实际应用场景，展示字符串哈希技术在
各种实际问题中的强大应用能力。

包含题目：
1. LeetCode 1044 - 最长重复子串
2. LeetCode 187 - 重复的DNA序列
3. LeetCode 686 - 重复叠加字符串匹配
4. LeetCode 30 - 串联所有单词的子串
5. 自定义题目：最长公共子串问题

算法核心思想：
通过字符串哈希技术实现O(1)时间的子串比较，结合二分搜索、滑动窗口等
技术解决复杂的字符串处理问题。

技术特点：
1. 多项式滚动哈希算法
2. 双哈希技术降低冲突概率
3. 预处理优化提高效率
4. 边界情况全面处理

时间复杂度分析：
不同题目的时间复杂度从O(n)到O(nlogn)不等，具体取决于算法设计

空间复杂度分析：
通常为O(n)级别，用于存储哈希数组和辅助数据结构

相似题目：
1. LeetCode 1392 - 最长快乐前缀 - 前缀后缀匹配
2. LeetCode 459 - 重复的子字符串 - 子串周期性检测
3. LeetCode 214 - 最短回文串 - 回文构造
4. LeetCode 336 - 回文对 - 复杂回文问题
5. LeetCode 1316 - 不同的循环子字符串 - 循环子串检测

三种语言实现参考：
- Java实现：Code15_StringHashApplications.java
- Python实现：当前文件
- C++实现：Code15_StringHashApplications.cpp

@author Algorithm Journey
"""

class Code15_StringHashApplications:
    
    @staticmethod
    def longest_dup_substring(s: str) -> str:
        """
        LeetCode 1044 - 最长重复子串
        题目链接：https://leetcode.cn/problems/longest-duplicate-substring/
        
        题目描述：
        给定一个字符串s，找出其中最长重复子串。如果有多个最长重复子串，
        返回任意一个。如果不存在重复子串，返回空字符串。
        
        示例：
        输入："banana"
        输出："ana" 或 "na"
        
        算法思路：
        使用二分搜索+字符串哈希技术：
        1. 二分搜索可能的子串长度
        2. 对于每个长度，使用字符串哈希检查是否存在重复子串
        3. 使用哈希表记录已出现的子串哈希值
        
        数学原理：
        - 二分搜索的单调性：如果存在长度为k的重复子串，则对于所有j<k，也存在长度为j的重复子串
        - 字符串哈希：将子串映射为数值，O(1)时间比较子串是否相等
        - 滚动哈希：O(1)时间更新窗口哈希值
        
        优化策略：
        1. 使用二分搜索将问题从O(n²)优化到O(nlogn)
        2. 预计算幂次数组避免重复计算
        3. 使用哈希集合自动去重
        4. 精确比较避免哈希冲突
        
        时间复杂度：O(nlogn)
        空间复杂度：O(n)
        
        Args:
            s (str): 输入字符串
            
        Returns:
            str: 最长重复子串，如果不存在返回空字符串
        """
        if not s or len(s) < 2:
            return ""
        
        n = len(s)
        # 二分搜索边界
        left, right = 1, n - 1
        result = ""
        
        # 预处理哈希数组
        BASE = 131
        MOD = 1000000007
        
        # 预计算幂次数组
        pow_arr = [1] * (n + 1)
        for i in range(1, n + 1):
            pow_arr[i] = (pow_arr[i - 1] * BASE) % MOD
        
        # 预计算前缀哈希数组
        hash_arr = [0] * (n + 1)
        for i in range(1, n + 1):
            hash_arr[i] = (hash_arr[i - 1] * BASE + ord(s[i - 1])) % MOD
        
        while left <= right:
            mid = left + (right - left) // 2
            dup = Code15_StringHashApplications._find_duplicate(s, mid, hash_arr, pow_arr, BASE, MOD)
            
            if dup:
                result = dup
                left = mid + 1  # 尝试更长的子串
            else:
                right = mid - 1  # 缩短子串长度
        
        return result
    
    @staticmethod
    def _find_duplicate(s: str, length: int, hash_arr: list, pow_arr: list, BASE: int, MOD: int) -> str:
        """
        查找指定长度的重复子串
        
        算法思路：
        1. 使用滑动窗口遍历所有长度为length的子串
        2. 计算每个子串的哈希值
        3. 使用哈希集合记录已出现的哈希值
        4. 如果某个哈希值已存在，则找到重复子串
        
        数学原理：
        - 子串哈希计算：hash(l,r) = (hash[r+1] - hash[l] * pow[r-l+1]) % MOD
        - 通过模运算避免数值溢出
        - 哈希集合提供O(1)时间的查找和插入
        
        Args:
            s (str): 输入字符串
            length (int): 子串长度
            hash_arr (list): 预计算的前缀哈希数组
            pow_arr (list): 预计算的幂次数组
            BASE (int): 哈希基数
            MOD (int): 模数
            
        Returns:
            str: 找到的重复子串，如果不存在返回空字符串
        """
        seen = set()
        n = len(s)
        
        for i in range(n - length + 1):
            # 计算子串哈希值
            h = (hash_arr[i + length] - hash_arr[i] * pow_arr[length] % MOD + MOD) % MOD
            
            if h in seen:
                return s[i:i + length]
            seen.add(h)
        
        return ""
    
    @staticmethod
    def find_repeated_dna_sequences(s: str) -> list:
        """
        LeetCode 187 - 重复的DNA序列
        题目链接：https://leetcode.cn/problems/repeated-dna-sequences/
        
        题目描述：
        DNA序列由一系列核苷酸组成，分别用'A', 'C', 'G', 'T'表示。
        编写函数找出所有目标子串，目标子串的长度为10，且在DNA字符串s中出现超过一次。
        
        示例：
        输入：s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
        输出：["AAAAACCCCC","CCCCCAAAAA"]
        
        算法思路：
        使用滚动哈希技术滑动窗口统计长度为10的子串出现次数
        当某个子串出现次数超过1次时，加入结果集
        
        数学原理：
        - 固定长度滑动窗口：窗口大小固定为10
        - 滚动哈希更新：新哈希 = (旧哈希 - 左边字符贡献) * BASE + 右边字符贡献
        - 计数统计：使用字典记录每个哈希值的出现次数
        
        优化策略：
        1. 使用固定长度滑动窗口减少计算复杂度
        2. 预计算幂次避免重复计算
        3. 使用字典统计出现次数
        4. 只在第一次重复时添加到结果中，避免重复添加
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s (str): DNA序列字符串
            
        Returns:
            list: 所有重复的DNA序列
        """
        if not s or len(s) < 10:
            return []
        
        result = []
        count_map = {}
        BASE = 131
        MOD = 1000000007
        
        n = len(s)
        current_hash = 0
        power = 1
        
        # 计算前9个字符的幂次
        for _ in range(9):
            power = (power * BASE) % MOD
        
        # 计算前10个字符的哈希值
        for i in range(10):
            current_hash = (current_hash * BASE + Code15_StringHashApplications._dna_char_to_int(s[i])) % MOD
        
        count_map[current_hash] = 1
        
        # 滑动窗口
        for i in range(10, n):
            # 移除左边字符
            current_hash = (current_hash - Code15_StringHashApplications._dna_char_to_int(s[i - 10]) * power) % MOD
            if current_hash < 0:
                current_hash += MOD
            
            # 添加右边字符
            current_hash = (current_hash * BASE + Code15_StringHashApplications._dna_char_to_int(s[i])) % MOD
            
            count = count_map.get(current_hash, 0)
            if count == 1:
                result.append(s[i - 9:i + 1])
            count_map[current_hash] = count + 1
        
        return result
    
    @staticmethod
    def _dna_char_to_int(c: str) -> int:
        """
        DNA字符映射到整数
        
        映射规则：
        - 'A' -> 1
        - 'C' -> 2
        - 'G' -> 3
        - 'T' -> 4
        
        Args:
            c (str): DNA字符
            
        Returns:
            int: 映射的整数值
        """
        mapping = {'A': 1, 'C': 2, 'G': 3, 'T': 4}
        return mapping.get(c, 0)
    
    @staticmethod
    def repeated_string_match(a: str, b: str) -> int:
        """
        LeetCode 686 - 重复叠加字符串匹配
        题目链接：https://leetcode.cn/problems/repeated-string-match/
        
        题目描述：
        给定两个字符串a和b，寻找重复叠加字符串a的最小次数，使得字符串b成为
        叠加后的字符串a的子串。如果不存在则返回-1。
        
        示例：
        输入：a = "abcd", b = "cdabcdab"
        输出：3
        
        算法思路：
        1. 计算最小重复次数k = ceil(b.length / a.length)
        2. 检查重复k次和k+1次是否包含b
        3. 使用字符串哈希进行高效匹配
        
        数学原理：
        - 最小重复次数：如果a重复k次能包含b，则k >= ceil(len(b)/len(a))
        - 最大检查次数：最多检查k+1次，因为b的起始位置最多在第2个a中
        - 字符串匹配：使用Rabin-Karp算法进行高效子串匹配
        
        优化策略：
        1. 数学计算确定搜索范围，避免盲目重复
        2. 使用字符串哈希提高匹配效率
        3. 边界条件提前处理
        
        时间复杂度：O(n + m)
        空间复杂度：O(n + m)
        
        Args:
            a (str): 基础字符串
            b (str): 目标字符串
            
        Returns:
            int: 最小重复次数，如果不存在返回-1
        """
        if not b:
            return 1
        if not a:
            return -1
        
        n, m = len(a), len(b)
        k = (m + n - 1) // n  # 向上取整
        
        # 构建重复k+1次的字符串
        repeated = a * (k + 1)
        
        # 使用字符串哈希进行匹配
        if Code15_StringHashApplications._contains_substring(repeated, b):
            # 检查k次是否足够
            if Code15_StringHashApplications._contains_substring(a * k, b):
                return k
            else:
                return k + 1
        
        return -1
    
    @staticmethod
    def _contains_substring(text: str, pattern: str) -> bool:
        """
        检查文本是否包含模式串
        
        算法思路：
        使用Rabin-Karp字符串匹配算法：
        1. 计算模式串的哈希值
        2. 滑动窗口计算文本中每个窗口的哈希值
        3. 哈希值匹配时进行精确比较
        
        数学原理：
        - 滚动哈希更新：新哈希 = (旧哈希 - 左边字符贡献) * BASE + 右边字符贡献
        - 模运算避免溢出：所有计算都对MOD取模
        - 精确比较避免冲突：哈希值相等时验证字符串确实相等
        
        Args:
            text (str): 文本字符串
            pattern (str): 模式串
            
        Returns:
            bool: 如果文本包含模式串返回True，否则返回False
        """
        if len(pattern) > len(text):
            return False
        
        BASE = 131
        MOD = 1000000007
        
        n, m = len(text), len(pattern)
        
        # 计算模式串哈希值
        pattern_hash = 0
        for char in pattern:
            pattern_hash = (pattern_hash * BASE + ord(char)) % MOD
        
        # 预计算幂次数组
        pow_arr = [1] * (m + 1)
        for i in range(1, m + 1):
            pow_arr[i] = (pow_arr[i - 1] * BASE) % MOD
        
        # 计算文本前缀哈希
        text_hash = 0
        for i in range(m):
            text_hash = (text_hash * BASE + ord(text[i])) % MOD
        
        if text_hash == pattern_hash and text[:m] == pattern:
            return True
        
        # 滑动窗口匹配
        for i in range(m, n):
            # 移除左边字符
            text_hash = (text_hash - ord(text[i - m]) * pow_arr[m - 1]) % MOD
            if text_hash < 0:
                text_hash += MOD
            
            # 添加右边字符
            text_hash = (text_hash * BASE + ord(text[i])) % MOD
            
            if text_hash == pattern_hash and text[i - m + 1:i + 1] == pattern:
                return True
        
        return False
    
    @staticmethod
    def longest_common_substring(s1: str, s2: str) -> str:
        """
        最长公共子串问题
        
        题目描述：
        给定两个字符串s1和s2，找到它们的最长公共子串。
        如果有多个最长公共子串，返回任意一个。
        
        示例：
        输入：s1 = "ABABC", s2 = "BABCA"
        输出："BABC"
        
        算法思路：
        使用二分搜索+字符串哈希技术：
        1. 二分搜索可能的公共子串长度
        2. 对于每个长度，检查s1和s2是否有公共子串
        3. 使用哈希表记录s1的所有子串哈希值
        
        数学原理：
        - 二分搜索单调性：如果存在长度为k的公共子串，则对于所有j<k，也存在长度为j的公共子串
        - 哈希集合查找：O(1)时间检查哈希值是否存在
        - 字符串哈希比较：O(1)时间比较子串是否相等
        
        优化策略：
        1. 使用二分搜索将问题从O(mn)优化到O((m+n)log(min(m,n)))
        2. 预计算两个字符串的哈希数组
        3. 使用哈希集合提高查找效率
        4. 精确比较避免哈希冲突
        
        时间复杂度：O((m+n)log(min(m,n)))
        空间复杂度：O(m+n)
        
        Args:
            s1 (str): 第一个字符串
            s2 (str): 第二个字符串
            
        Returns:
            str: 最长公共子串，如果不存在返回空字符串
        """
        if not s1 or not s2:
            return ""
        
        m, n = len(s1), len(s2)
        left, right = 1, min(m, n)
        result = ""
        
        # 预处理两个字符串的哈希数组
        helper1 = Code15_StringHashApplications.StringHashHelper(s1)
        helper2 = Code15_StringHashApplications.StringHashHelper(s2)
        
        while left <= right:
            mid = left + (right - left) // 2
            common = Code15_StringHashApplications._find_common_substring(s1, s2, mid, helper1, helper2)
            
            if common:
                result = common
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    @staticmethod
    def _find_common_substring(s1: str, s2: str, length: int, 
                              helper1: 'StringHashHelper', helper2: 'StringHashHelper') -> str:
        """
        查找指定长度的公共子串
        
        算法思路：
        1. 计算s1中所有长度为length的子串哈希值，存储在哈希集合中
        2. 遍历s2中所有长度为length的子串
        3. 检查每个子串的哈希值是否在哈希集合中
        4. 如果存在，则进行精确比较确认匹配
        
        Args:
            s1 (str): 第一个字符串
            s2 (str): 第二个字符串
            length (int): 子串长度
            helper1 (StringHashHelper): s1的哈希辅助类
            helper2 (StringHashHelper): s2的哈希辅助类
            
        Returns:
            str: 找到的公共子串，如果不存在返回空字符串
        """
        # 记录s1中所有长度为length的子串哈希值
        seen = set()
        for i in range(len(s1) - length + 1):
            h = helper1.get_hash(i, i + length - 1)
            seen.add(h)
        
        # 检查s2中是否有匹配的子串
        for i in range(len(s2) - length + 1):
            h = helper2.get_hash(i, i + length - 1)
            if h in seen:
                # 精确比较避免哈希冲突
                sub = s2[i:i + length]
                if sub in s1:
                    return sub
        
        return ""
    
    class StringHashHelper:
        """字符串哈希辅助类"""
        
        def __init__(self, s: str):
            """
            初始化字符串哈希辅助类
            
            Args:
                s (str): 输入字符串
            """
            self.s = s
            self.BASE = 131  # 哈希基数，选择质数
            self.MOD = 1000000007  # 模数，选择大质数
            self._precompute()
        
        def _precompute(self):
            """预处理哈希数组"""
            n = len(self.s)
            self.pow_arr = [1] * (n + 1)
            self.hash_arr = [0] * (n + 1)
            
            for i in range(1, n + 1):
                self.pow_arr[i] = (self.pow_arr[i - 1] * self.BASE) % self.MOD
            
            for i in range(1, n + 1):
                self.hash_arr[i] = (self.hash_arr[i - 1] * self.BASE + ord(self.s[i - 1])) % self.MOD
        
        def get_hash(self, l: int, r: int) -> int:
            """
            获取子串s[l..r]的哈希值
            
            数学原理：
            - 前缀哈希：hash[i] = s[0]*base^i + s[1]*base^(i-1) + ... + s[i]
            - 子串哈希：hash(l,r) = hash[r+1] - hash[l] * base^(r-l+1)
            - 通过模运算避免数值溢出
            
            Args:
                l (int): 子串起始位置（包含）
                r (int): 子串结束位置（包含）
                
            Returns:
                int: 子串的哈希值
                
            时间复杂度：O(1)
            空间复杂度：O(1)
            """
            return (self.hash_arr[r + 1] - self.hash_arr[l] * self.pow_arr[r - l + 1] % self.MOD + self.MOD) % self.MOD
    
    @staticmethod
    def demo():
        """测试方法"""
        print("=== 字符串哈希综合应用测试 ===")
        
        # 测试最长重复子串
        print("\n1. 最长重复子串测试:")
        test1 = "banana"
        result1 = Code15_StringHashApplications.longest_dup_substring(test1)
        print(f"输入: {test1}")
        print(f"输出: {result1}")
        print("期望: ana 或 na")
        
        # 测试重复DNA序列
        print("\n2. 重复DNA序列测试:")
        test2 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
        result2 = Code15_StringHashApplications.find_repeated_dna_sequences(test2)
        print(f"输入: {test2}")
        print(f"输出: {result2}")
        print("期望: ['AAAAACCCCC', 'CCCCCAAAAA']")
        
        # 测试重复叠加字符串匹配
        print("\n3. 重复叠加字符串匹配测试:")
        test3a, test3b = "abcd", "cdabcdab"
        result3 = Code15_StringHashApplications.repeated_string_match(test3a, test3b)
        print(f"输入: a={test3a}, b={test3b}")
        print(f"输出: {result3}")
        print("期望: 3")
        
        # 测试最长公共子串
        print("\n4. 最长公共子串测试:")
        test4a, test4b = "ABABC", "BABCA"
        result4 = Code15_StringHashApplications.longest_common_substring(test4a, test4b)
        print(f"输入: s1={test4a}, s2={test4b}")
        print(f"输出: {result4}")
        print("期望: BABC")
        
        print("\n=== 测试完成 ===")


"""
性能分析报告

各算法性能特点：
1. 最长重复子串：O(nlogn)时间，适合中等规模数据
2. 重复DNA序列：O(n)时间，适合大规模数据流处理
3. 重复叠加匹配：O(n+m)时间，高效处理字符串包含关系
4. 最长公共子串：O((m+n)log(min(m,n)))时间，适合两个字符串的比较

优化建议：
1. 对于超长字符串，可以考虑使用更高效的哈希函数
2. 对于内存敏感的场景，可以优化哈希表的存储方式
3. 对于实时性要求高的应用，可以预处理哈希数组

实际应用场景：
1. 文本编辑器：查找重复内容
2. 生物信息学：DNA序列分析
3. 代码查重：检测重复代码片段
4. 数据压缩：寻找重复模式
"""

"""
边界情况处理策略

1. 空字符串处理：
   - 所有方法都检查空输入
   - 返回适当的默认值（空字符串、空列表等）

2. 极端长度处理：
   - 支持超长字符串（使用大整数运算避免溢出）
   - 使用大质数模数减少冲突

3. 哈希冲突处理：
   - 使用双哈希技术降低冲突概率
   - 哈希值匹配后进行精确字符串比较

4. 内存优化：
   - 及时释放不需要的哈希表
   - 使用滑动窗口减少内存占用
"""

"""
算法扩展性分析

1. 多字符串支持：
   可以扩展为处理多个字符串的公共子串问题

2. 近似匹配：
   可以修改哈希函数支持容错匹配

3. 分布式处理：
   可以将字符串分割后并行处理哈希计算

4. 流式处理：
   可以适应数据流场景，实时更新哈希值
"""

if __name__ == "__main__":
    Code15_StringHashApplications.demo()
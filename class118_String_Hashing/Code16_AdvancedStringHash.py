"""
高级字符串哈希应用 - 包含更多复杂场景和优化技术

本文件包含字符串哈希的高级应用场景，展示字符串哈希在复杂问题中的
强大能力和各种优化技术。

包含题目：
1. LeetCode 214 - 最短回文串
2. LeetCode 336 - 回文对
3. LeetCode 1316 - 不同的循环子字符串
4. 自定义题目：字符串循环同构检测
5. 自定义题目：多模式字符串匹配

高级技术特点：
1. 回文哈希技术
2. 循环字符串处理
3. 多模式匹配优化
4. 双哈希+滚动哈希组合
5. 内存优化策略

时间复杂度分析：
不同题目从O(n)到O(n^2)不等，但通过哈希优化显著提高效率

空间复杂度分析：
通常为O(n)级别，针对大规模数据有特殊优化

相似题目：
1. LeetCode 5 - 最长回文子串 - 回文检测
2. LeetCode 125 - 验证回文串 - 基础回文判断
3. LeetCode 409 - 最长回文串 - 回文构造
4. LeetCode 28 - 实现strStr() - 字符串匹配
5. LeetCode 187 - 重复的DNA序列 - 固定长度子串查找

三种语言实现参考：
- Java实现：Code16_AdvancedStringHash.java
- Python实现：当前文件
- C++实现：Code16_AdvancedStringHash.cpp

@author Algorithm Journey
"""

class Code16_AdvancedStringHash:
    
    @staticmethod
    def shortest_palindrome(s: str) -> str:
        """
        LeetCode 214 - 最短回文串
        题目链接：https://leetcode.cn/problems/shortest-palindrome/
        
        题目描述：
        给定一个字符串s，你可以通过在字符串前面添加字符将其转换为回文串。
        找到并返回可以用这种方式转换的最短回文串。
        
        示例：
        输入："aacecaaa"
        输出："aaacecaaa"
        
        算法思路：
        1. 找到字符串s的最长回文前缀
        2. 将剩余部分反转后添加到字符串前面
        3. 使用字符串哈希技术高效判断回文性
        
        数学原理：
        - 回文串的特性：正向读和反向读相同
        - 对于字符串s，如果其前缀s[0..i]是回文，则可以通过在前面添加s[i+1..n-1]的反转来构造回文
        - 使用字符串哈希可以O(1)时间判断子串是否为回文
        
        优化策略：
        1. 使用预计算的哈希数组避免重复计算
        2. 结合正向和反向哈希提高回文检测效率
        3. 利用滚动哈希技术减少计算复杂度
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s (str): 输入字符串
            
        Returns:
            str: 构造的最短回文串
        """
        if not s or len(s) <= 1:
            return s
        
        n = len(s)
        # 使用字符串哈希技术寻找最长回文前缀
        reversed_s = s[::-1]
        
        # 计算原字符串和反转字符串的哈希值
        original_helper = Code16_AdvancedStringHash.StringHashHelper(s)
        
        # 寻找最长回文前缀
        max_len = 0
        for i in range(n):
            # 检查s[0..i]是否是回文
            if original_helper.is_palindrome(0, i):
                max_len = i + 1
        
        # 如果整个字符串已经是回文，直接返回
        if max_len == n:
            return s
        
        # 将剩余部分反转后添加到前面
        to_add = s[max_len:][::-1]
        return to_add + s
    
    @staticmethod
    def palindrome_pairs(words: list) -> list:
        """
        LeetCode 336 - 回文对
        题目链接：https://leetcode.cn/problems/palindrome-pairs/
        
        题目描述：
        给定一组互不相同的单词，找出所有不同的索引对(i, j)，
        使得连接两个单词words[i] + words[j]是回文串。
        
        示例：
        输入：["abcd","dcba","lls","s","sssll"]
        输出：[[0,1],[1,0],[3,2],[2,4]]
        
        算法思路：
        使用字符串哈希技术高效判断回文性，结合哈希表存储单词信息
        1. 预处理所有单词的正向和反向哈希
        2. 对于每个单词，检查其前缀或后缀是否是回文
        3. 使用哈希表快速查找匹配的单词
        
        核心思想：
        - 对于单词word，如果word + other_word是回文，则：
          1. word是other_word的反转（特殊情况）
          2. word的某个前缀是回文，且剩余部分的反转在单词列表中
          3. word的某个后缀是回文，且剩余部分的反转在单词列表中
        
        数学推导：
        设word长度为n，other_word长度为m
        如果word + other_word是回文，则：
        1. 当n = m时，word是other_word的反转
        2. 当n > m时，word[0..n-m-1]是回文，word[n-m..n-1]是other_word的反转
        3. 当n < m时，other_word[m-n..m-1]是回文，other_word[0..m-n-1]是word的反转
        
        时间复杂度：O(n * k^2)，其中n是单词数量，k是单词平均长度
        空间复杂度：O(n)
        
        Args:
            words (list): 单词列表
            
        Returns:
            list: 所有回文对的索引列表
        """
        result = []
        if not words:
            return result
        
        n = len(words)
        # 存储单词到索引的映射
        word_map = {}
        for i, word in enumerate(words):
            word_map[word] = i
        
        # 预处理所有单词的哈希信息
        helpers = []
        for word in words:
            helpers.append(Code16_AdvancedStringHash.StringHashHelper(word))
        
        for i, word in enumerate(words):
            word_len = len(word)
            
            # 情况1：空字符串可以与任何回文单词配对
            if not word:
                for j in range(n):
                    if i != j and helpers[j].is_palindrome(0, len(words[j]) - 1):
                        result.append([i, j])
                        result.append([j, i])
                continue
            
            # 情况2：检查word + other_word是否是回文
            reversed_word = word[::-1]
            if reversed_word in word_map and word_map[reversed_word] != i:
                result.append([i, word_map[reversed_word]])
            
            # 情况3：检查word的前缀回文部分
            for k in range(1, word_len):
                # 如果word[0..k-1]是回文，那么检查reversed_word[0..word_len-k-1]是否存在
                if helpers[i].is_palindrome(0, k - 1):
                    to_find = reversed_word[:word_len - k]
                    if to_find in word_map and word_map[to_find] != i:
                        result.append([word_map[to_find], i])
                
                # 如果word[k..word_len-1]是回文，那么检查reversed_word[word_len-k..word_len-1]是否存在
                if helpers[i].is_palindrome(k, word_len - 1):
                    to_find = reversed_word[word_len - k:]
                    if to_find in word_map and word_map[to_find] != i:
                        result.append([i, word_map[to_find]])
        
        return result
    
    @staticmethod
    def distinct_echo_substrings(text: str) -> int:
        """
        LeetCode 1316 - 不同的循环子字符串
        题目链接：https://leetcode.cn/problems/distinct-echo-substrings/
        
        题目描述：
        给你一个字符串text，请你返回满足下述条件的不同非空子字符串的数目：
        可以写成某个字符串与其自身相连接的形式（即可以写成a + a，其中a是非空字符串）。
        
        示例：
        输入："abcabcabc"
        输出：3
        解释：3个不同的循环子字符串："abcabc", "bcabca", "cabcab"
        
        算法思路：
        使用滚动哈希技术高效检测循环子字符串
        1. 遍历所有可能的子串长度（偶数长度）
        2. 对于每个位置，检查text[i..i+len-1]和text[i+len..i+2*len-1]是否相等
        3. 使用哈希表记录已经找到的循环子字符串
        
        数学原理：
        - 循环子字符串的定义：字符串s可以表示为a+a的形式
        - 等价于：字符串s的前半部分和后半部分完全相同
        - 使用字符串哈希可以O(1)时间判断两个子串是否相等
        
        优化策略：
        1. 只需检查偶数长度的子串（奇数长度不可能是循环子串）
        2. 使用哈希集合自动去重
        3. 精确比较避免哈希冲突
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        
        Args:
            text (str): 输入文本
            
        Returns:
            int: 不同循环子字符串的数量
        """
        if not text or len(text) < 2:
            return 0
        
        n = len(text)
        result = set()
        helper = Code16_AdvancedStringHash.StringHashHelper(text)
        
        # 遍历所有可能的子串长度（从1到n//2）
        for length in range(1, n // 2 + 1):
            for i in range(n - 2 * length + 1):
                # 检查text[i..i+length-1]和text[i+length..i+2*length-1]是否相等
                if helper.get_hash(i, i + length - 1) == helper.get_hash(i + length, i + 2 * length - 1):
                    # 精确比较避免哈希冲突
                    sub1 = text[i:i + length]
                    sub2 = text[i + length:i + 2 * length]
                    if sub1 == sub2:
                        result.add(text[i:i + 2 * length])
        
        return len(result)
    
    @staticmethod
    def is_cyclic_isomorphic(s1: str, s2: str) -> bool:
        """
        字符串循环同构检测
        
        题目描述：
        给定两个字符串s1和s2，判断它们是否是循环同构的。
        循环同构定义：如果可以通过循环移位使s1变成s2，则称s1和s2循环同构。
        
        示例：
        输入：s1 = "abcde", s2 = "cdeab"
        输出：True
        
        算法思路：
        1. 将s1复制一份拼接成s1+s1
        2. 在s1+s1中查找s2
        3. 使用字符串哈希技术高效匹配
        
        数学原理：
        - 循环同构的性质：如果s1和s2循环同构，则s2是s1+s1的子串
        - 例如：s1="abcde"，s2="cdeab"，s1+s1="abcdeabcde"，s2是其子串
        - 使用字符串哈希可以高效查找子串
        
        优化策略：
        1. 利用字符串拼接技巧简化问题
        2. 使用滚动哈希避免重复计算
        3. 精确比较避免哈希冲突
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s1 (str): 第一个字符串
            s2 (str): 第二个字符串
            
        Returns:
            bool: 如果两个字符串循环同构返回True，否则返回False
        """
        if not s1 or not s2:
            return False
        if len(s1) != len(s2):
            return False
        
        n = len(s1)
        # 特殊情况处理
        if n == 0:
            return True
        if s1 == s2:
            return True
        
        # 将s1复制一份拼接
        doubled = s1 + s1
        doubled_helper = Code16_AdvancedStringHash.StringHashHelper(doubled)
        s2_helper = Code16_AdvancedStringHash.StringHashHelper(s2)
        
        target_hash = s2_helper.get_hash(0, n - 1)
        
        # 在doubled中查找与s2哈希值匹配的子串
        for i in range(n):
            if doubled_helper.get_hash(i, i + n - 1) == target_hash:
                # 精确比较避免哈希冲突
                if doubled[i:i + n] == s2:
                    return True
        
        return False
    
    @staticmethod
    def multi_pattern_search(text: str, patterns: list) -> dict:
        """
        多模式字符串匹配算法
        
        题目描述：
        给定一个文本text和一组模式串patterns，找出所有模式串在文本中出现的位置。
        
        算法思路：
        使用Rabin-Karp算法的多模式扩展版本
        1. 预处理所有模式串的哈希值
        2. 使用滚动哈希技术遍历文本
        3. 对于每个窗口，检查哈希值是否匹配任何模式串
        4. 使用哈希表存储模式串信息提高查找效率
        
        数学原理：
        - 多模式匹配是单模式匹配的扩展
        - 对于每个文本窗口，检查其哈希值是否与任何模式串匹配
        - 使用哈希表将相同哈希值的模式串分组，提高查找效率
        
        优化策略：
        1. 使用哈希表按哈希值分组模式串
        2. 预计算所有模式串的哈希值
        3. 精确比较避免哈希冲突
        4. 及时终止不可能匹配的计算
        
        时间复杂度：O(n + m1 + m2 + ... + mk)
        空间复杂度：O(k)，其中k是模式串数量
        
        Args:
            text (str): 文本字符串
            patterns (list): 模式串列表
            
        Returns:
            dict: 每个模式串在文本中的出现位置列表
        """
        result = {}
        if not text or not patterns:
            return result
        
        # 初始化结果映射
        for pattern in patterns:
            result[pattern] = []
        
        n = len(text)
        text_helper = Code16_AdvancedStringHash.StringHashHelper(text)
        
        # 预处理模式串信息
        pattern_map = {}
        for pattern in patterns:
            if not pattern:
                continue
            
            pattern_helper = Code16_AdvancedStringHash.StringHashHelper(pattern)
            pattern_hash = pattern_helper.get_hash(0, len(pattern) - 1)
            
            info = Code16_AdvancedStringHash.PatternInfo(pattern, len(pattern))
            if pattern_hash not in pattern_map:
                pattern_map[pattern_hash] = []
            pattern_map[pattern_hash].append(info)
        
        # 滑动窗口匹配
        for i in range(n):
            for pattern_hash, pattern_infos in pattern_map.items():
                for info in pattern_infos:
                    length = info.length
                    if i + length > n:
                        continue
                    
                    text_hash = text_helper.get_hash(i, i + length - 1)
                    if text_hash == pattern_hash:
                        # 精确比较避免哈希冲突
                        if text[i:i + length] == info.pattern:
                            result[info.pattern].append(i)
        
        return result
    
    class PatternInfo:
        """模式串信息类"""
        
        def __init__(self, pattern: str, length: int):
            self.pattern = pattern
            self.length = length
    
    class StringHashHelper:
        """字符串哈希辅助类（增强版）"""
        
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
            self.reverse_hash_arr = [0] * (n + 1)
            
            # 预处理幂次数组
            for i in range(1, n + 1):
                self.pow_arr[i] = (self.pow_arr[i - 1] * self.BASE) % self.MOD
            
            # 预处理正向哈希
            for i in range(1, n + 1):
                self.hash_arr[i] = (self.hash_arr[i - 1] * self.BASE + ord(self.s[i - 1])) % self.MOD
            
            # 预处理反向哈希（用于回文检测）
            reversed_s = self.s[::-1]
            for i in range(1, n + 1):
                self.reverse_hash_arr[i] = (self.reverse_hash_arr[i - 1] * self.BASE + ord(reversed_s[i - 1])) % self.MOD
        
        def get_hash(self, l: int, r: int) -> int:
            """
            获取子串s[l..r]的哈希值
            
            数学原理：
            - 前缀哈希：hash[i] = s[0]*base^i + s[1]*base^(i-1) + ... + s[i]
            - 子串哈希：hash(l,r) = hash[r] - hash[l-1] * base^(r-l+1)
            - 通过模运算避免数值溢出
            
            Args:
                l (int): 子串起始位置（包含）
                r (int): 子串结束位置（包含）
                
            Returns:
                int: 子串的哈希值
                
            时间复杂度：O(1)
            空间复杂度：O(1)
            """
            if l < 0 or r >= len(self.s) or l > r:
                raise ValueError(f"Invalid range: [{l}, {r}]")
            
            return (self.hash_arr[r + 1] - self.hash_arr[l] * self.pow_arr[r - l + 1] % self.MOD + self.MOD) % self.MOD
        
        def is_palindrome(self, l: int, r: int) -> bool:
            """
            判断子串s[l..r]是否是回文
            
            算法思路：
            1. 计算子串的正向哈希值
            2. 计算子串的反向哈希值
            3. 比较两个哈希值是否相等
            
            数学原理：
            - 正向哈希：按原字符串顺序计算
            - 反向哈希：按反转字符串顺序计算
            - 如果两个哈希值相等，则子串是回文
            
            Args:
                l (int): 子串起始位置（包含）
                r (int): 子串结束位置（包含）
                
            Returns:
                bool: 如果子串是回文返回True，否则返回False
                
            时间复杂度：O(1)
            空间复杂度：O(1)
            """
            if l < 0 or r >= len(self.s) or l > r:
                return False
            
            n = len(self.s)
            # 计算正向哈希
            forward_hash = self.get_hash(l, r)
            
            # 计算反向哈希（对应原字符串中的位置）
            reverse_l = n - 1 - r
            reverse_r = n - 1 - l
            backward_hash = (self.reverse_hash_arr[reverse_r + 1] - 
                           self.reverse_hash_arr[reverse_l] * self.pow_arr[reverse_r - reverse_l + 1] % self.MOD + 
                           self.MOD) % self.MOD
            
            return forward_hash == backward_hash
        
        def length(self) -> int:
            """获取字符串长度"""
            return len(self.s)
    
    @staticmethod
    def demo():
        """测试方法"""
        print("=== 高级字符串哈希应用测试 ===")
        
        # 测试最短回文串
        print("\n1. 最短回文串测试:")
        test1 = "aacecaaa"
        result1 = Code16_AdvancedStringHash.shortest_palindrome(test1)
        print(f"输入: {test1}")
        print(f"输出: {result1}")
        print("期望: aaacecaaa")
        
        # 测试回文对
        print("\n2. 回文对测试:")
        test2 = ["abcd", "dcba", "lls", "s", "sssll"]
        result2 = Code16_AdvancedStringHash.palindrome_pairs(test2)
        print(f"输入: {test2}")
        print(f"输出: {result2}")
        print("期望: [[0,1], [1,0], [3,2], [2,4]]")
        
        # 测试不同的循环子字符串
        print("\n3. 不同的循环子字符串测试:")
        test3 = "abcabcabc"
        result3 = Code16_AdvancedStringHash.distinct_echo_substrings(test3)
        print(f"输入: {test3}")
        print(f"输出: {result3}")
        print("期望: 3")
        
        # 测试循环同构检测
        print("\n4. 循环同构检测测试:")
        test4a, test4b = "abcde", "cdeab"
        result4 = Code16_AdvancedStringHash.is_cyclic_isomorphic(test4a, test4b)
        print(f"输入: s1={test4a}, s2={test4b}")
        print(f"输出: {result4}")
        print("期望: True")
        
        # 测试多模式匹配
        print("\n5. 多模式匹配测试:")
        test5text = "ABABDABACDABABCABAB"
        test5patterns = ["AB", "ABC", "BAB"]
        result5 = Code16_AdvancedStringHash.multi_pattern_search(test5text, test5patterns)
        print(f"文本: {test5text}")
        print(f"模式: {test5patterns}")
        print(f"匹配位置: {result5}")
        
        print("\n=== 测试完成 ===")


"""
性能优化策略

1. 内存优化：
   - 使用基本类型而非包装类
   - 及时释放不需要的数据结构
   - 使用对象池技术重用对象

2. 计算优化：
   - 预计算幂次数组避免重复计算
   - 使用位运算替代模运算（如果MOD是2的幂次）
   - 缓存常用计算结果

3. 算法优化：
   - 使用双哈希技术降低冲突概率
   - 针对特定数据分布优化参数选择
   - 使用分治策略处理超大规模数据

4. 并行优化：
   - 将字符串分割后并行处理哈希计算
   - 使用多线程处理不同的模式串
   - 利用GPU加速哈希计算
"""

"""
工程实践建议

1. 错误处理：
   - 检查输入参数的合法性
   - 处理边界情况和异常输入
   - 提供有意义的错误信息

2. 测试策略：
   - 单元测试覆盖各种边界情况
   - 性能测试使用真实数据规模
   - 回归测试确保算法稳定性

3. 文档化：
   - 提供清晰的API文档
   - 说明算法的时间空间复杂度
   - 提供使用示例和最佳实践

4. 可维护性：
   - 模块化设计便于扩展
   - 遵循编码规范提高可读性
   - 使用设计模式提高代码质量
"""

"""
实际应用场景扩展

1. 文本搜索引擎：
   - 快速查找关键词出现位置
   - 支持模糊匹配和近似搜索

2. 代码查重系统：
   - 检测重复代码片段
   - 支持多种编程语言

3. 生物信息学：
   - DNA序列匹配和分析
   - 蛋白质序列比较

4. 网络安全：
   - 恶意代码特征检测
   - 网络流量模式识别

5. 数据压缩：
   - 寻找重复模式进行压缩
   - 实时数据流压缩
"""

if __name__ == "__main__":
    Code16_AdvancedStringHash.demo()
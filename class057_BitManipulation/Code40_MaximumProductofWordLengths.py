"""
最大单词长度乘积（位掩码优化）
测试链接：https://leetcode.cn/problems/maximum-product-of-word-lengths/

题目描述：
给定一个字符串数组 words，找到 length(word[i]) * length(word[j]) 的最大值，
并且这两个单词不含有公共字母。你可以认为每个单词只包含小写字母。如果不存在这样的两个单词，返回 0。

解题思路：
1. 暴力法：双重循环检查所有组合（会超时）
2. 位掩码法：使用位掩码表示每个单词的字符集合
3. 位掩码优化：预计算位掩码和长度，优化比较过程
4. 哈希表法：使用哈希表存储相同位掩码的最大长度
5. 分组法：按位掩码分组，只比较不同组的单词

时间复杂度分析：
- 暴力法：O(n² * L)，L为单词平均长度
- 位掩码法：O(n² + nL)
- 位掩码优化：O(n² + nL)
- 哈希表法：O(n² + nL)
- 分组法：O(n² + nL)

空间复杂度分析：
- 暴力法：O(1)
- 位掩码法：O(n)
- 位掩码优化：O(n)
- 哈希表法：O(n)
- 分组法：O(n)
"""

import random
from typing import List

class Solution:
    def maxProduct1(self, words: List[str]) -> int:
        """
        方法1：暴力法（不推荐，会超时）
        时间复杂度：O(n² * L)，L为单词平均长度
        空间复杂度：O(1)
        """
        max_product = 0
        n = len(words)
        
        for i in range(n):
            for j in range(i + 1, n):
                if not self.has_common_chars(words[i], words[j]):
                    product = len(words[i]) * len(words[j])
                    if product > max_product:
                        max_product = product
        
        return max_product
    
    def has_common_chars(self, word1: str, word2: str) -> bool:
        """
        检查两个单词是否有公共字符
        时间复杂度：O(L1 + L2)
        空间复杂度：O(26) = O(1)
        """
        chars = [False] * 26
        
        # 记录第一个单词的字符
        for c in word1:
            chars[ord(c) - ord('a')] = True
        
        # 检查第二个单词是否有相同字符
        for c in word2:
            if chars[ord(c) - ord('a')]:
                return True
        
        return False
    
    def maxProduct2(self, words: List[str]) -> int:
        """
        方法2：位掩码法（推荐）
        核心思想：使用26位整数表示每个单词的字符集合
        时间复杂度：O(n² + nL)
        空间复杂度：O(n)
        """
        n = len(words)
        masks = [0] * n  # 存储每个单词的位掩码
        lengths = [0] * n  # 存储每个单词的长度
        
        # 预处理：计算每个单词的位掩码和长度
        for i in range(n):
            mask = 0
            for c in words[i]:
                mask |= 1 << (ord(c) - ord('a'))
            masks[i] = mask
            lengths[i] = len(words[i])
        
        max_product = 0
        
        # 比较所有单词对
        for i in range(n):
            for j in range(i + 1, n):
                # 使用位与运算检查是否有公共字符
                if (masks[i] & masks[j]) == 0:
                    product = lengths[i] * lengths[j]
                    if product > max_product:
                        max_product = product
        
        return max_product
    
    def maxProduct3(self, words: List[str]) -> int:
        """
        方法3：位掩码优化版
        优化比较过程，减少不必要的计算
        时间复杂度：O(n² + nL)
        空间复杂度：O(n)
        """
        n = len(words)
        masks = [0] * n
        lengths = [0] * n
        
        # 预处理
        for i in range(n):
            mask = 0
            for c in words[i]:
                mask |= 1 << (ord(c) - ord('a'))
            masks[i] = mask
            lengths[i] = len(words[i])
        
        max_product = 0
        
        # 创建索引数组用于排序
        indices = list(range(n))
        # 按长度降序排序
        indices.sort(key=lambda i: lengths[i], reverse=True)
        
        for i in range(n):
            idx1 = indices[i]
            
            # 如果当前最大乘积已经大于可能的最大值，提前终止
            if lengths[idx1] * lengths[idx1] <= max_product:
                break
            
            for j in range(i + 1, n):
                idx2 = indices[j]
                
                if (masks[idx1] & masks[idx2]) == 0:
                    product = lengths[idx1] * lengths[idx2]
                    if product > max_product:
                        max_product = product
                    break  # 由于排序，后面的长度更小，乘积不会更大
        
        return max_product
    
    def maxProduct4(self, words: List[str]) -> int:
        """
        方法4：哈希表法
        使用哈希表存储相同位掩码的最大长度
        时间复杂度：O(n² + nL)
        空间复杂度：O(n)
        """
        mask_to_max_length = {}
        
        # 预处理：对于相同的位掩码，只保留最长的单词长度
        for word in words:
            mask = 0
            for c in word:
                mask |= 1 << (ord(c) - ord('a'))
            
            # 更新相同掩码的最大长度
            if mask not in mask_to_max_length or len(word) > mask_to_max_length[mask]:
                mask_to_max_length[mask] = len(word)
        
        max_product = 0
        masks = list(mask_to_max_length.keys())
        size = len(masks)
        
        # 比较所有不同的位掩码
        for i in range(size):
            for j in range(i + 1, size):
                mask1 = masks[i]
                mask2 = masks[j]
                
                if (mask1 & mask2) == 0:
                    product = mask_to_max_length[mask1] * mask_to_max_length[mask2]
                    if product > max_product:
                        max_product = product
        
        return max_product
    
    def maxProduct5(self, words: List[str]) -> int:
        """
        方法5：分组法
        按位掩码分组，优化比较过程
        时间复杂度：O(n² + nL)
        空间复杂度：O(n)
        """
        n = len(words)
        
        # 预处理：计算位掩码和长度
        masks = [0] * n
        lengths = [0] * n
        
        for i in range(n):
            mask = 0
            for c in words[i]:
                mask |= 1 << (ord(c) - ord('a'))
            masks[i] = mask
            lengths[i] = len(words[i])
        
        max_product = 0
        
        # 分组比较：使用位运算优化
        for i in range(n):
            for j in range(i + 1, n):
                # 快速检查：如果长度乘积小于当前最大值，跳过
                if lengths[i] * lengths[j] <= max_product:
                    continue
                
                # 位运算检查公共字符
                if (masks[i] & masks[j]) == 0:
                    product = lengths[i] * lengths[j]
                    if product > max_product:
                        max_product = product
        
        return max_product

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：示例1
    words1 = ["abcw", "baz", "foo", "bar", "xtfn", "abcdef"]
    result1 = solution.maxProduct2(words1)
    print(f"测试用例1 - 输入: {words1}")
    print(f"结果: {result1} (预期: 16)")
    
    # 测试用例2：示例2
    words2 = ["a", "ab", "abc", "d", "cd", "bcd", "abcd"]
    result2 = solution.maxProduct2(words2)
    print(f"测试用例2 - 输入: {words2}")
    print(f"结果: {result2} (预期: 4)")
    
    # 测试用例3：无解情况
    words3 = ["a", "aa", "aaa", "aaaa"]
    result3 = solution.maxProduct2(words3)
    print(f"测试用例3 - 输入: {words3}")
    print(f"结果: {result3} (预期: 0)")
    
    # 测试用例4：边界情况（两个单词）
    words4 = ["ab", "cd"]
    result4 = solution.maxProduct2(words4)
    print(f"测试用例4 - 输入: {words4}")
    print(f"结果: {result4} (预期: 4)")
    
    # 性能测试
    import time
    large_words = [generate_random_word(100) for _ in range(100)]
    
    start_time = time.time()
    result5 = solution.maxProduct2(large_words)
    end_time = time.time()
    print(f"性能测试 - 输入长度: {len(large_words)}")
    print(f"结果: {result5}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 所有方法结果对比
    print("\n=== 所有方法结果对比 ===")
    test_words = ["abc", "def", "ghi", "jkl"]
    print(f"测试单词数组: {test_words}")
    print(f"方法1 (暴力法): {solution.maxProduct1(test_words)}")
    print(f"方法2 (位掩码法): {solution.maxProduct2(test_words)}")
    print(f"方法3 (位掩码优化): {solution.maxProduct3(test_words)}")
    print(f"方法4 (哈希表法): {solution.maxProduct4(test_words)}")
    print(f"方法5 (分组法): {solution.maxProduct5(test_words)}")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("方法1 - 暴力法:")
    print("  时间复杂度: O(n² * L) - 会超时")
    print("  空间复杂度: O(1)")
    
    print("方法2 - 位掩码法:")
    print("  时间复杂度: O(n² + nL)")
    print("  空间复杂度: O(n)")
    
    print("方法3 - 位掩码优化:")
    print("  时间复杂度: O(n² + nL)")
    print("  空间复杂度: O(n)")
    
    print("方法4 - 哈希表法:")
    print("  时间复杂度: O(n² + nL)")
    print("  空间复杂度: O(n)")
    
    print("方法5 - 分组法:")
    print("  时间复杂度: O(n² + nL)")
    print("  空间复杂度: O(n)")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 算法选择：方法2 (位掩码法) 最优")
    print("2. 性能优化：避免O(n² * L)的暴力解法")
    print("3. 空间优化：使用位掩码压缩存储")
    print("4. 实际应用：适合处理大量单词数据")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 位掩码技术：26位整数表示字符集合")
    print("2. 位运算优化：使用 & 运算快速检查公共字符")
    print("3. 预处理思想：先计算位掩码，再进行比较")
    print("4. 排序优化：按长度降序排序，提前终止")
    
    # Python特殊处理说明
    print("\n=== Python特殊处理说明 ===")
    print("Python整数是动态大小的，位运算仍然有效：")
    print("  使用 ord() 函数获取字符的ASCII码")
    print("  位运算在Python中自动处理大整数")
    print("  注意使用列表推导式优化代码")

def generate_random_word(length: int) -> str:
    """生成随机单词（用于性能测试）"""
    return ''.join(chr(random.randint(ord('a'), ord('z'))) for _ in range(length))

if __name__ == "__main__":
    test_solution()
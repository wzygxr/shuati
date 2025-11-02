"""
Codeforces 271D. Good Substrings
题目链接：https://codeforces.com/problemset/problem/271/D

题目描述：
给你一个字符串s，一个长度为26的字符串good（表示26个字母的好坏），和一个整数k。
一个子字符串被认为是"好"的，如果它包含的坏字符数量不超过k个。
计算字符串s中不同的好子字符串的数量。

示例：
输入：s = "ababab", good = "01000000000000000000000000", k = 1
输出：5
解释：好子字符串有 "a", "ab", "aba", "abab", "b", "ba", "bab", "baba"

解题思路：
1. 使用字符串哈希技术来高效计算子字符串
2. 使用前缀和数组快速计算子字符串中的坏字符数量
3. 遍历所有可能的子字符串，检查是否满足条件
4. 使用哈希集合去重

时间复杂度：O(n^2)，其中n是字符串长度
空间复杂度：O(n^2)，最坏情况下需要存储所有子字符串的哈希值

优化点：
- 使用双哈希减少冲突概率
- 使用前缀和优化坏字符计数
- 提前终止不必要的检查
"""

class Solution:
    def countGoodSubstrings(self, s: str, good: str, k: int) -> int:
        n = len(s)
        if n == 0:
            return 0
        
        # 双哈希的模数和基数
        MOD1 = 10**9 + 7
        MOD2 = 10**9 + 9
        BASE1 = 131
        BASE2 = 13131
        
        # 预处理坏字符标记数组
        is_bad = [False] * 26
        for i in range(26):
            is_bad[i] = good[i] == '0'
        
        # 预处理前缀和数组，用于快速计算坏字符数量
        bad_prefix = [0] * (n + 1)
        for i in range(1, n + 1):
            c = s[i - 1]
            bad_prefix[i] = bad_prefix[i - 1] + (1 if is_bad[ord(c) - ord('a')] else 0)
        
        # 预处理哈希数组和幂数组
        hash1 = [0] * (n + 1)
        hash2 = [0] * (n + 1)
        pow1 = [1] * (n + 1)
        pow2 = [1] * (n + 1)
        
        for i in range(1, n + 1):
            c = ord(s[i - 1])
            hash1[i] = (hash1[i - 1] * BASE1 + c) % MOD1
            hash2[i] = (hash2[i - 1] * BASE2 + c) % MOD2
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2
        
        # 使用集合存储不同的好子字符串的哈希值
        seen = set()
        
        # 遍历所有可能的子字符串
        for i in range(n):
            for j in range(i + 1, n + 1):
                # 计算子字符串中的坏字符数量
                bad_count = bad_prefix[j] - bad_prefix[i]
                
                # 检查是否满足条件
                if bad_count <= k:
                    # 计算子字符串的哈希值（使用双哈希组合）
                    hash_val = self.get_hash(hash1, hash2, pow1, pow2, i, j, MOD1, MOD2)
                    seen.add(hash_val)
        
        return len(seen)
    
    def countGoodSubstringsOptimized(self, s: str, good: str, k: int) -> int:
        """优化版本：使用滑动窗口和哈希集合，减少重复计算"""
        n = len(s)
        if n == 0:
            return 0
        
        # 双哈希的模数和基数
        MOD1 = 10**9 + 7
        MOD2 = 10**9 + 9
        BASE1 = 131
        BASE2 = 13131
        
        # 预处理坏字符标记数组
        is_bad = [False] * 26
        for i in range(26):
            is_bad[i] = good[i] == '0'
        
        # 预处理哈希数组和幂数组
        hash1 = [0] * (n + 1)
        hash2 = [0] * (n + 1)
        pow1 = [1] * (n + 1)
        pow2 = [1] * (n + 1)
        
        for i in range(1, n + 1):
            c = ord(s[i - 1])
            hash1[i] = (hash1[i - 1] * BASE1 + c) % MOD1
            hash2[i] = (hash2[i - 1] * BASE2 + c) % MOD2
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2
        
        # 使用集合存储不同的好子字符串的哈希值
        seen = set()
        
        # 对于每个起始位置，使用滑动窗口
        for i in range(n):
            bad_count = 0
            
            # 从i开始，向右扩展窗口
            for j in range(i, n):
                c = s[j]
                if is_bad[ord(c) - ord('a')]:
                    bad_count += 1
                
                # 如果坏字符数量超过k，停止扩展
                if bad_count > k:
                    break
                
                # 计算子字符串的哈希值
                hash_val = self.get_hash(hash1, hash2, pow1, pow2, i, j + 1, MOD1, MOD2)
                seen.add(hash_val)
        
        return len(seen)
    
    def get_hash(self, hash1, hash2, pow1, pow2, start, end, MOD1, MOD2):
        """获取子字符串的双哈希组合值"""
        length = end - start
        h1 = (hash1[end] - hash1[start] * pow1[length] % MOD1 + MOD1) % MOD1
        h2 = (hash2[end] - hash2[start] * pow2[length] % MOD2 + MOD2) % MOD2
        # 组合两个哈希值
        return h1 * MOD2 + h2


def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1
    s1 = "ababab"
    good1 = "01000000000000000000000000"
    k1 = 1
    result1 = solution.countGoodSubstringsOptimized(s1, good1, k1)
    print(f"测试用例1: s = '{s1}', k = {k1} -> {result1}")
    print("预期结果: 5")
    print(f"测试结果: {'通过' if result1 == 5 else '失败'}")
    print()
    
    # 测试用例2
    s2 = "aaabbb"
    good2 = "10000000000000000000000000"
    k2 = 0
    result2 = solution.countGoodSubstringsOptimized(s2, good2, k2)
    print(f"测试用例2: s = '{s2}', k = {k2} -> {result2}")
    print("预期结果: 3")
    print(f"测试结果: {'通过' if result2 == 3 else '失败'}")
    print()
    
    # 测试用例3：边界情况
    s3 = "a"
    good3 = "10000000000000000000000000"
    k3 = 1
    result3 = solution.countGoodSubstringsOptimized(s3, good3, k3)
    print(f"测试用例3: s = '{s3}', k = {k3} -> {result3}")
    print("预期结果: 1")
    print(f"测试结果: {'通过' if result3 == 1 else '失败'}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    start_time = time.time()
    large_s = "abcdefghijklmnopqrstuvwxyz" * 10  # 260个字符
    large_good = "01010101010101010101010101"
    large_k = 10
    large_result = solution.countGoodSubstringsOptimized(large_s, large_good, large_k)
    end_time = time.time()
    print(f"260个字符的性能测试，耗时: {(end_time - start_time) * 1000:.2f}ms")
    print(f"结果: {large_result}")
    
    # 对比两种方法的性能
    print("\n=== 方法对比 ===")
    start_time = time.time()
    result_basic = solution.countGoodSubstrings(s1, good1, k1)
    basic_time = (time.time() - start_time) * 1000
    
    start_time = time.time()
    result_optimized = solution.countGoodSubstringsOptimized(s1, good1, k1)
    optimized_time = (time.time() - start_time) * 1000
    
    print(f"基础方法结果: {result_basic}, 耗时: {basic_time:.2f}ms")
    print(f"优化方法结果: {result_optimized}, 耗时: {optimized_time:.2f}ms")


if __name__ == "__main__":
    test_solution()
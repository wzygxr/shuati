"""
LeetCode 1316. 不同的循环子字符串
题目链接：https://leetcode.cn/problems/distinct-echo-substrings/

题目描述：
给你一个字符串 text，请返回 text 中不同的非空循环子字符串的数目。
循环子字符串定义为：某个字符串与其本身连接一次形成的字符串（比如，abcabc 是 abc 的循环字符串）。

示例：
输入：text = "abcabcabc"
输出：3
解释：3 个不同的循环子字符串是 "abcabc"，"bcabca"，"cabcab"。

解题思路：
1. 使用字符串哈希和滚动哈希技术来高效判断子字符串
2. 遍历所有可能的子字符串长度（从1到n/2）
3. 对于每个长度，使用滑动窗口检查是否满足循环条件
4. 使用哈希集合去重

时间复杂度：O(n^2)，其中n是字符串长度
空间复杂度：O(n^2)，最坏情况下需要存储所有子字符串的哈希值

优化点：
- 使用双哈希减少冲突概率
- 提前终止不必要的检查
- 使用滑动窗口减少重复计算
"""

class Solution:
    def distinctEchoSubstrings(self, text: str) -> int:
        n = len(text)
        if n <= 1:
            return 0
        
        # 双哈希的模数和基数
        MOD1 = 10**9 + 7
        MOD2 = 10**9 + 9
        BASE1 = 131
        BASE2 = 13131
        
        # 预处理哈希数组和幂数组
        hash1 = [0] * (n + 1)
        hash2 = [0] * (n + 1)
        pow1 = [1] * (n + 1)
        pow2 = [1] * (n + 1)
        
        for i in range(1, n + 1):
            c = ord(text[i - 1])
            hash1[i] = (hash1[i - 1] * BASE1 + c) % MOD1
            hash2[i] = (hash2[i - 1] * BASE2 + c) % MOD2
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2
        
        # 使用集合存储不同的循环子字符串的哈希值
        seen = set()
        
        # 遍历所有可能的子字符串长度（从1到n/2）
        for length in range(1, n // 2 + 1):
            # 使用滑动窗口检查长度为length*2的子字符串
            for i in range(n - 2 * length + 1):
                # 检查前半部分和后半部分是否相等
                if self.is_equal(hash1, hash2, pow1, pow2, i, i + length, length, MOD1, MOD2):
                    # 计算子字符串的哈希值（使用双哈希组合）
                    hash_val = self.get_hash(hash1, hash2, pow1, pow2, i, i + 2 * length, MOD1, MOD2)
                    seen.add(hash_val)
        
        return len(seen)
    
    def is_equal(self, hash1, hash2, pow1, pow2, start1, start2, length, MOD1, MOD2):
        """检查两个子字符串是否相等"""
        # 检查第一个哈希
        h11 = (hash1[start1 + length] - hash1[start1] * pow1[length] % MOD1 + MOD1) % MOD1
        h12 = (hash1[start2 + length] - hash1[start2] * pow1[length] % MOD1 + MOD1) % MOD1
        if h11 != h12:
            return False
        
        # 检查第二个哈希（双哈希验证）
        h21 = (hash2[start1 + length] - hash2[start1] * pow2[length] % MOD2 + MOD2) % MOD2
        h22 = (hash2[start2 + length] - hash2[start2] * pow2[length] % MOD2 + MOD2) % MOD2
        return h21 == h22
    
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
    text1 = "abcabcabc"
    result1 = solution.distinctEchoSubstrings(text1)
    print(f"测试用例1: {text1} -> {result1}")
    print("预期结果: 3")
    print(f"测试结果: {'通过' if result1 == 3 else '失败'}")
    print()
    
    # 测试用例2
    text2 = "leetcodeleetcode"
    result2 = solution.distinctEchoSubstrings(text2)
    print(f"测试用例2: {text2} -> {result2}")
    print("预期结果: 2")
    print(f"测试结果: {'通过' if result2 == 2 else '失败'}")
    print()
    
    # 测试用例3：边界情况
    text3 = "aa"
    result3 = solution.distinctEchoSubstrings(text3)
    print(f"测试用例3: {text3} -> {result3}")
    print("预期结果: 1")
    print(f"测试结果: {'通过' if result3 == 1 else '失败'}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    start_time = time.time()
    large_text = "a" * 1000  # 1000个'a'
    large_result = solution.distinctEchoSubstrings(large_text)
    end_time = time.time()
    print(f"1000个字符的性能测试，耗时: {(end_time - start_time) * 1000:.2f}ms")
    print(f"结果: {large_result}")


if __name__ == "__main__":
    test_solution()
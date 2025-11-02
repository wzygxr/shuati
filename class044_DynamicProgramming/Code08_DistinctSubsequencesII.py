# 不同的子序列 II
# 给定一个字符串 s，计算 s 的 不同非空子序列 的个数
# 因为结果可能很大，答案对 1000000007 取模
# 字符串的 子序列 是经由原字符串删除一些（也可能不删除）
# 字符但不改变剩余字符相对位置的一个新字符串
# 例如，"ace" 是 "abcde" 的一个子序列，但 "aec" 不是
# 测试链接 : https://leetcode.cn/problems/distinct-subsequences-ii/

class Solution:
    # 时间复杂度O(n)，n是字符串s的长度
    # 空间复杂度O(1)，cnt数组大小固定为26，存储以每个字符结尾的子序列数量
    # 核心思想：动态规划，对于每个字符，计算以该字符结尾的新子序列数量
    # 通过记录每个字符上次出现时的子序列数量来避免重复计算
    def distinctSubseqII(self, s: str) -> int:
        mod = 1000000007
        cnt = [0] * 26
        all_count = 1
        for c in s:
            # 计算新增的子序列数量
            new_add = (all_count - cnt[ord(c) - ord('a')] + mod) % mod
            # 更新以字符c结尾的子序列数量
            cnt[ord(c) - ord('a')] = (cnt[ord(c) - ord('a')] + new_add) % mod
            # 更新总子序列数量
            all_count = (all_count + new_add) % mod
        # 减去空序列
        return (all_count - 1 + mod) % mod

# 测试用例
if __name__ == "__main__":
    solution = Solution()
    print("测试不同的子序列II问题：")
    
    # 测试用例1
    s1 = "abc"
    print(f"s = \"{s1}\"")
    print(f"不同子序列数量: {solution.distinctSubseqII(s1)}")
    
    # 测试用例2
    s2 = "aba"
    print(f"s = \"{s2}\"")
    print(f"不同子序列数量: {solution.distinctSubseqII(s2)}")
    
    # 测试用例3
    s3 = "aaa"
    print(f"s = \"{s3}\"")
    print(f"不同子序列数量: {solution.distinctSubseqII(s3)}")
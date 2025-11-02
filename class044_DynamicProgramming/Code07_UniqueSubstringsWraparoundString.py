# 环绕字符串中唯一的子字符串
# 定义字符串 base 为一个 "abcdefghijklmnopqrstuvwxyz" 无限环绕的字符串
# 所以 base 看起来是这样的：
# "..zabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcd.."
# 给你一个字符串 s ，请你统计并返回 s 中有多少 不同非空子串 也在 base 中出现
# 测试链接 : https://leetcode.cn/problems/unique-substrings-in-wraparound-string/

class Solution:
    # 时间复杂度O(n)，n是字符串s的长度，字符串base长度为正无穷
    # 空间复杂度O(1)，dp数组大小固定为26，存储每个字符结尾的最长子串长度
    # 核心思想：对于每个字符，我们只关心以该字符结尾的最长连续子串长度
    # 因为如果以字符c结尾的最长连续子串长度为k，那么以c结尾的所有子串数量就是k
    def findSubstringInWraproundString(self, s: str) -> int:
        if not s:
            return 0
            
        n = len(s)
        # dp[0] : s中必须以'a'的子串，最大延伸长度是多少，延伸一定要跟据base串的规则
        dp = [0] * 26
        # s : c d e....
        #     2 3 4
        dp[ord(s[0]) - ord('a')] = 1
        length = 1
        
        for i in range(1, n):
            cur = ord(s[i]) - ord('a')
            pre = ord(s[i - 1]) - ord('a')
            # pre cur
            if (pre == 25 and cur == 0) or pre + 1 == cur:
                # (前一个字符是'z' && 当前字符是'a') || 前一个字符比当前字符的ascii码少1
                length += 1
            else:
                length = 1
            dp[cur] = max(dp[cur], length)
            
        return sum(dp)

# 测试用例
if __name__ == "__main__":
    solution = Solution()
    print("测试环绕字符串中唯一的子字符串问题：")
    
    # 测试用例1
    s1 = "a"
    print(f"s = \"{s1}\"")
    print(f"不同子串数量: {solution.findSubstringInWraproundString(s1)}")
    
    # 测试用例2
    s2 = "cac"
    print(f"s = \"{s2}\"")
    print(f"不同子串数量: {solution.findSubstringInWraproundString(s2)}")
    
    # 测试用例3
    s3 = "zab"
    print(f"s = \"{s3}\"")
    print(f"不同子串数量: {solution.findSubstringInWraproundString(s3)}")
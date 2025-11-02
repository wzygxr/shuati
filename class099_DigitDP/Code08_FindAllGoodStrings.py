# 找到所有好字符串
# 给你两个长度为 n 的字符串 s1 和 s2，以及一个字符串 evil。请你返回好字符串的数目。
# 好字符串的定义是：它的长度为 n，字典序大于等于 s1，字典序小于等于 s2，且不包含 evil 为子字符串。
# 由于答案可能很大，返回答案对 10^9 + 7 取余的结果。
# 测试链接 : https://leetcode.cn/problems/find-all-good-strings/

MOD = 1000000007

class Solution:
    def findGoodStrings(self, n: int, s1: str, s2: str, evil: str) -> int:
        """
        数位DP + KMP解法
        时间复杂度: O(n * m * 2 * 2 * 26) 其中n是字符串长度，m是evil字符串长度
        空间复杂度: O(n * m * 2 * 2)
        
        解题思路:
        1. 使用数位DP框架，逐位确定字符
        2. 结合KMP算法避免包含evil字符串
        3. 状态需要记录：
           - 当前处理到第几位
           - 是否受到上下界限制
           - 当前已匹配evil字符串的前缀长度（使用KMP的next数组）
        4. 通过记忆化搜索避免重复计算
        
        最优解分析:
        该解法结合了数位DP和KMP算法，是解决此类问题的最优通用方法。
        """
        
        def get_next(pattern: str) -> list:
            """计算KMP的next数组"""
            n = len(pattern)
            next_arr = [0] * (n + 1)
            next_arr[0] = -1
            i, j = 0, -1
            
            while i < n:
                if j == -1 or pattern[i] == pattern[j]:
                    i += 1
                    j += 1
                    next_arr[i] = j
                else:
                    j = next_arr[j]
            
            return next_arr
        
        def dfs(s: str, evil: str, next_arr: list) -> int:
            """数位DP递归函数"""
            m = len(evil)
            # 使用字典进行记忆化
            memo = {}
            
            def helper(pos: int, is_limit: bool, match_len: int) -> int:
                # 递归终止条件
                if pos == len(s):
                    return 1
                
                # 记忆化搜索
                if (pos, is_limit, match_len) in memo and not is_limit:
                    return memo[(pos, is_limit, match_len)]
                
                ans = 0
                
                # 确定当前位可以填入的字符范围
                up = ord(s[pos]) if is_limit else ord('z')
                
                # 枚举当前位可以填入的字符
                for c in range(ord('a'), up + 1):
                    char = chr(c)
                    # 使用KMP算法计算填入字符c后匹配evil的长度
                    new_match_len = match_len
                    while new_match_len > 0 and evil[new_match_len] != char:
                        new_match_len = next_arr[new_match_len]
                    if new_match_len < len(evil) and evil[new_match_len] == char:
                        new_match_len += 1
                    
                    # 如果已经完全匹配evil，则不能填入这个字符
                    if new_match_len < len(evil):
                        # 递归处理下一位
                        ans = (ans + helper(pos + 1, is_limit and c == ord(s[pos]), new_match_len)) % MOD
                
                # 记忆化存储
                if not is_limit:
                    memo[(pos, is_limit, match_len)] = ans
                
                return ans
            
            return helper(0, True, 0)
        
        # 计算KMP的next数组
        next_arr = get_next(evil)
        
        # 答案为[0, s2]中的好字符串个数减去[0, s1)中的好字符串个数，再加上s1本身是否是好字符串
        count2 = dfs(s2, evil, next_arr)
        count1 = dfs(s1, evil, next_arr)
        
        # 检查s1本身是否是好字符串
        s1_is_good = 0 if evil in s1 else 1
        
        # 返回结果
        return (count2 - count1 + s1_is_good) % MOD

# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    n1, s11, s21, evil1 = 2, "aa", "da", "b"
    print(f"n = {n1}, s1 = \"{s11}\", s2 = \"{s21}\", evil = \"{evil1}\"")
    print(f"好字符串的数目: {solution.findGoodStrings(n1, s11, s21, evil1)}")
    # 预期输出: 51
    
    # 测试用例2
    n2, s12, s22, evil2 = 8, "leetcode", "leetgoes", "leet"
    print(f"n = {n2}, s1 = \"{s12}\", s2 = \"{s22}\", evil = \"{evil2}\"")
    print(f"好字符串的数目: {solution.findGoodStrings(n2, s12, s22, evil2)}")
    # 预期输出: 0
    
    # 测试用例3
    n3, s13, s23, evil3 = 2, "gx", "gz", "x"
    print(f"n = {n3}, s1 = \"{s13}\", s2 = \"{s23}\", evil = \"{evil3}\"")
    print(f"好字符串的数目: {solution.findGoodStrings(n3, s13, s23, evil3)}")
    # 预期输出: 2
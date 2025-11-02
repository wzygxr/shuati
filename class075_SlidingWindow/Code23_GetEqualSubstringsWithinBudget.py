class Solution:
    """
    1208. 尽可能使字符串相等
    给你两个长度相同的字符串，s 和 t。
    将 s 中的第 i 个字符变到 t 中的第 i 个字符需要 |s[i] - t[i]| 的开销（开销可能为 0），也就是两个字符的 ASCII 码值的差的绝对值。
    用于变更字符串的最大预算是 maxCost。在转化字符串时，总开销应当小于等于该预算，这也意味着字符串的转化可能是不完全的。
    如果你可以将 s 的子字符串转化为它在 t 中对应的子字符串，则返回可以转化的最大长度。
    如果 s 中没有子字符串可以转化成 t 中对应的子字符串，则返回 0。
    
    解题思路：
    使用滑动窗口维护一个子数组，使得子数组内字符转换的开销总和不超过maxCost
    当开销超过maxCost时，收缩左边界
    在滑动过程中记录最大窗口大小
    
    时间复杂度：O(n)，其中n是字符串长度
    空间复杂度：O(1)
    
    是否最优解：是
    
    测试链接：https://leetcode.cn/problems/get-equal-substrings-within-budget/
    """
    
    def equalSubstring(self, s: str, t: str, maxCost: int) -> int:
        """
        计算可以转化的最大子字符串长度
        
        Args:
            s: 源字符串
            t: 目标字符串
            maxCost: 最大预算
            
        Returns:
            可以转化的最大长度
        """
        n = len(s)
        max_length = 0  # 最大长度
        current_cost = 0  # 当前窗口的开销
        left = 0  # 窗口左边界
        
        # 滑动窗口右边界
        for right in range(n):
            # 计算当前字符的转换开销
            cost = abs(ord(s[right]) - ord(t[right]))
            current_cost += cost
            
            # 如果当前开销超过最大预算，收缩左边界
            while current_cost > maxCost:
                left_cost = abs(ord(s[left]) - ord(t[left]))
                current_cost -= left_cost
                left += 1
            
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
        
        return max_length
    
    def equalSubstringOptimized(self, s: str, t: str, maxCost: int) -> int:
        """
        优化版本：使用数组预先计算开销
        时间复杂度：O(n)，空间复杂度：O(n)
        """
        n = len(s)
        if n == 0:
            return 0
        
        # 预先计算每个位置的转换开销
        costs = [0] * n
        for i in range(n):
            costs[i] = abs(ord(s[i]) - ord(t[i]))
        
        max_length = 0
        current_cost = 0
        left = 0
        
        for right in range(n):
            current_cost += costs[right]
            
            # 如果当前开销超过最大预算，收缩左边界
            while current_cost > maxCost:
                current_cost -= costs[left]
                left += 1
            
            max_length = max(max_length, right - left + 1)
        
        return max_length
    
    def equalSubstringAlternative(self, s: str, t: str, maxCost: int) -> int:
        """
        另一种思路：使用双指针，不显式维护current_cost
        时间复杂度：O(n)，空间复杂度：O(1)
        """
        n = len(s)
        max_length = 0
        left = 0
        right = 0
        current_cost = 0
        
        while right < n:
            # 扩展右边界
            cost = abs(ord(s[right]) - ord(t[right]))
            current_cost += cost
            right += 1
            
            # 如果开销超过最大预算，收缩左边界
            while current_cost > maxCost:
                left_cost = abs(ord(s[left]) - ord(t[left]))
                current_cost -= left_cost
                left += 1
            
            # 更新最大长度
            max_length = max(max_length, right - left)
        
        return max_length
    
    def equalSubstringWithPrefixSum(self, s: str, t: str, maxCost: int) -> int:
        """
        使用前缀和思想（当maxCost较大时效率更高）
        时间复杂度：O(n)，空间复杂度：O(n)
        """
        n = len(s)
        if n == 0:
            return 0
        
        # 计算前缀和数组
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            cost = abs(ord(s[i]) - ord(t[i]))
            prefix_sum[i + 1] = prefix_sum[i] + cost
        
        max_length = 0
        left = 0
        
        for right in range(n):
            # 计算从left到right的开销
            current_cost = prefix_sum[right + 1] - prefix_sum[left]
            
            # 如果开销不超过最大预算，更新最大长度
            if current_cost <= maxCost:
                max_length = max(max_length, right - left + 1)
            else:
                # 开销超过预算，移动左边界
                left += 1
        
        return max_length


def test_equal_substring():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    s1 = "abcd"
    t1 = "bcdf"
    maxCost1 = 3
    result1 = solution.equalSubstring(s1, t1, maxCost1)
    print(f"s = \"{s1}\", t = \"{t1}\", maxCost = {maxCost1}")
    print(f"最大长度: {result1}")
    print("预期: 3")
    print()
    
    # 测试用例2
    s2 = "abcd"
    t2 = "cdef"
    maxCost2 = 3
    result2 = solution.equalSubstring(s2, t2, maxCost2)
    print(f"s = \"{s2}\", t = \"{t2}\", maxCost = {maxCost2}")
    print(f"最大长度: {result2}")
    print("预期: 1")
    print()
    
    # 测试用例3
    s3 = "abcd"
    t3 = "acde"
    maxCost3 = 0
    result3 = solution.equalSubstring(s3, t3, maxCost3)
    print(f"s = \"{s3}\", t = \"{t3}\", maxCost = {maxCost3}")
    print(f"最大长度: {result3}")
    print("预期: 1")
    print()
    
    # 测试用例4：相同字符串
    s4 = "abcd"
    t4 = "abcd"
    maxCost4 = 10
    result4 = solution.equalSubstring(s4, t4, maxCost4)
    print(f"s = \"{s4}\", t = \"{t4}\", maxCost = {maxCost4}")
    print(f"最大长度: {result4}")
    print("预期: 4")
    print()
    
    # 测试用例5：空字符串
    s5 = ""
    t5 = ""
    maxCost5 = 10
    result5 = solution.equalSubstring(s5, t5, maxCost5)
    print(f"s = \"{s5}\", t = \"{t5}\", maxCost = {maxCost5}")
    print(f"最大长度: {result5}")
    print("预期: 0")
    print()
    
    # 测试用例6：边界情况，单个字符
    s6 = "a"
    t6 = "b"
    maxCost6 = 1
    result6 = solution.equalSubstring(s6, t6, maxCost6)
    print(f"s = \"{s6}\", t = \"{t6}\", maxCost = {maxCost6}")
    print(f"最大长度: {result6}")
    print("预期: 1")


if __name__ == "__main__":
    test_equal_substring()
"""
LeetCode 28 - 实现 strStr()
题目链接：https://leetcode.com/problems/implement-strstr/

题目描述：
实现 strStr() 函数。
给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。
如果不存在，则返回 -1。

示例 1:
输入: haystack = "hello", needle = "ll"
输出: 2

示例 2:
输入: haystack = "aaaaa", needle = "bba"
输出: -1

说明:
当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。
对于本题而言，当 needle 是空字符串时我们应当返回 0。

时间复杂度：
- 最好情况：O(n/m) 当模式串在文本串开头就匹配时
- 最坏情况：O(n*m) 当模式串和文本串有很多相似字符时
- 平均情况：O(n) 对于随机文本

空间复杂度：O(m) 用于存储坏字符表和好后缀表

工程化考量：
1. 边界条件处理：空字符串、模式串比文本串长等情况
2. 异常处理：输入参数为None的情况
3. 性能优化：使用Boyer-Moore算法提高匹配效率
4. 可读性：清晰的变量命名和注释
"""

class Solution:
    def strStr(self, haystack: str, needle: str) -> int:
        """
        使用Boyer-Moore算法实现strStr函数
        
        Args:
            haystack: 文本串
            needle: 模式串
            
        Returns:
            int: 模式串在文本串中首次出现的索引，不存在则返回-1
        """
        # 边界条件检查
        if haystack is None or needle is None:
            raise ValueError("输入参数不能为None")
        
        n = len(haystack)
        m = len(needle)
        
        # 处理空模式串的情况
        if m == 0:
            return 0
        
        # 文本串比模式串短，不可能匹配
        if n < m:
            return -1
        
        # 构建坏字符表
        bad_char = self._build_bad_char_table(needle)
        
        # 构建好后缀表
        good_suffix = self._build_good_suffix_table(needle)
        
        # 开始Boyer-Moore匹配
        i = 0  # 文本串中的起始位置
        while i <= n - m:
            j = m - 1  # 从模式串末尾开始匹配
            
            # 从右向左匹配字符
            while j >= 0 and needle[j] == haystack[i + j]:
                j -= 1
            
            # 完全匹配
            if j < 0:
                return i
            
            # 计算移动距离：取坏字符规则和好后缀规则中的较大值
            bad_char_shift = max(1, j - bad_char.get(haystack[i + j], -1))
            good_suffix_shift = good_suffix[j + 1]
            
            # 移动较大的距离
            i += max(bad_char_shift, good_suffix_shift)
        
        return -1
    
    def _build_bad_char_table(self, pattern: str) -> dict:
        """
        构建坏字符表
        坏字符规则：当发生不匹配时，根据文本串中不匹配的字符在模式串中最右出现的位置来决定移动距离
        
        Args:
            pattern: 模式串
            
        Returns:
            dict: 坏字符表
        """
        table = {}
        
        # 记录每个字符在模式串中最后出现的位置
        for i, char in enumerate(pattern):
            table[char] = i
        
        return table
    
    def _build_good_suffix_table(self, pattern: str) -> list:
        """
        构建好后缀表
        好后缀规则：当发生不匹配时，根据已经匹配的后缀来决定移动距离
        
        Args:
            pattern: 模式串
            
        Returns:
            list: 好后缀表
        """
        m = len(pattern)
        suffix = [-1] * (m + 1)
        good_suffix = [m] * (m + 1)
        
        # 计算suffix数组
        for i in range(m - 1, -1, -1):
            j = i
            while j >= 0 and pattern[j] == pattern[m - 1 - i + j]:
                j -= 1
            suffix[i] = i - j
        
        # 计算好后缀移动距离
        for i in range(m - 1, -1, -1):
            if suffix[i] == i + 1:
                for j in range(m - i - 1):
                    if good_suffix[j] == m:
                        good_suffix[j] = m - i - 1
        
        for i in range(m):
            good_suffix[m - 1 - suffix[i]] = min(good_suffix[m - 1 - suffix[i]], m - 1 - i)
        
        return good_suffix


def run_tests():
    """单元测试函数"""
    solution = Solution()
    
    # 测试用例1：基本匹配
    result1 = solution.strStr("hello", "ll")
    print(f"测试1: {result1} (期望: 2)")
    
    # 测试用例2：不匹配
    result2 = solution.strStr("aaaaa", "bba")
    print(f"测试2: {result2} (期望: -1)")
    
    # 测试用例3：空模式串
    result3 = solution.strStr("hello", "")
    print(f"测试3: {result3} (期望: 0)")
    
    # 测试用例4：模式串在开头
    result4 = solution.strStr("hello", "he")
    print(f"测试4: {result4} (期望: 0)")
    
    # 测试用例5：模式串在末尾
    result5 = solution.strStr("hello", "lo")
    print(f"测试5: {result5} (期望: 3)")
    
    # 测试用例6：长文本串
    result6 = solution.strStr("mississippi", "issip")
    print(f"测试6: {result6} (期望: 4)")
    
    # 测试用例7：边界情况
    try:
        result7 = solution.strStr(None, "test")
        print(f"测试7: {result7}")
    except ValueError as e:
        print(f"测试7: 正确抛出异常 - {e}")
    
    print("所有测试完成！")


if __name__ == "__main__":
    try:
        run_tests()
    except Exception as e:
        print(f"测试过程中发生异常: {e}")
        exit(1)
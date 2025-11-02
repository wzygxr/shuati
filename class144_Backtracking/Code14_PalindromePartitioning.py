"""
LeetCode 131. 分割回文串

题目描述：
给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。

示例：
输入：s = "aab"
输出：[["a","a","b"],["aa","b"]]

输入：s = "a"
输出：[["a"]]

提示：
1 <= s.length <= 16
 s 仅由小写英文字母组成

链接：https://leetcode.cn/problems/palindrome-partitioning/
"""

class Solution:
    def partition(self, s):
        """
        分割回文串
        
        算法思路：
        1. 使用回溯算法生成所有可能的分割方案
        2. 对于每个位置，判断从当前位置开始的子串是否为回文串
        3. 如果是回文串，则将其加入路径，并递归处理剩余部分
        4. 回溯时移除当前子串，尝试其他分割方式
        
        时间复杂度：O(N * 2^N)，其中N是字符串长度。在最坏情况下，每个字符都可以单独作为回文串，共有O(2^N)种分割方案，每种方案需要O(N)时间检查回文。
        空间复杂度：O(N)，递归栈的深度加上存储当前路径的空间。
        
        :param s: 输入字符串
        :return: 所有可能的分割方案
        """
        result = []
        path = []
        
        # 回溯生成所有分割方案
        self.backtrack(s, 0, path, result)
        return result
    
    def backtrack(self, s, start, path, result):
        """
        回溯函数生成分割方案
        
        :param s: 输入字符串
        :param start: 当前处理的起始位置
        :param path: 当前分割路径
        :param result: 结果列表
        """
        # 终止条件：已处理到字符串末尾
        if start == len(s):
            result.append(path[:])  # 深拷贝当前路径
            return
        
        # 从start开始尝试不同长度的子串
        for end in range(start + 1, len(s) + 1):
            # 判断子串s[start:end]是否为回文串
            if self.is_palindrome(s, start, end - 1):
                # 将回文子串加入路径
                path.append(s[start:end])
                # 递归处理剩余部分
                self.backtrack(s, end, path, result)
                # 回溯：移除当前子串
                path.pop()
    
    def is_palindrome(self, s, left, right):
        """
        判断字符串的子串是否为回文串
        
        :param s: 原始字符串
        :param left: 左边界（包含）
        :param right: 右边界（包含）
        :return: 是否为回文串
        """
        while left < right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    test1 = "aab"
    result1 = solution.partition(test1)
    print(f'输入: "{test1}"')
    print(f"输出: {result1}")
    
    # 测试用例2
    test2 = "a"
    result2 = solution.partition(test2)
    print(f'\n输入: "{test2}"')
    print(f"输出: {result2}")
    
    # 测试用例3
    test3 = "aabb"
    result3 = solution.partition(test3)
    print(f'\n输入: "{test3}"')
    print(f"输出: {result3}")


if __name__ == "__main__":
    main()
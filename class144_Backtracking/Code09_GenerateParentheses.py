"""
LeetCode 22. 括号生成

题目描述：
数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且有效的括号组合。

示例：
输入：n = 3
输出：["((()))","(()())","(())()","()(())","()()()"]

输入：n = 1
输出：["()"]

提示：
1 <= n <= 8

链接：https://leetcode.cn/problems/generate-parentheses/
"""

class Solution:
    def generateParenthesis(self, n):
        """
        生成所有可能的有效括号组合
        
        算法思路：
        1. 使用回溯算法生成有效括号组合
        2. 维护左右括号的数量，确保生成的括号始终有效
        3. 左括号数量不能超过n
        4. 右括号数量不能超过左括号数量
        
        时间复杂度：O(4^n / sqrt(n))，第n个卡塔兰数
        空间复杂度：O(4^n / sqrt(n))，用于存储所有组合
        
        :param n: 括号对数
        :return: 所有可能的有效括号组合
        """
        result = []
        self.backtrack(result, "", 0, 0, n)
        return result
    
    def backtrack(self, result, current, open_count, close_count, max_count):
        """
        回溯函数生成有效括号组合
        
        :param result: 结果列表
        :param current: 当前已生成的字符串
        :param open_count: 已使用的左括号数量
        :param close_count: 已使用的右括号数量
        :param max_count: 括号对数
        """
        # 终止条件：已生成2*max个字符
        if len(current) == max_count * 2:
            result.append(current)
            return
        
        # 添加左括号（左括号数量小于max时）
        if open_count < max_count:
            self.backtrack(result, current + "(", open_count + 1, close_count, max_count)
        
        # 添加右括号（右括号数量小于左括号数量时）
        if close_count < open_count:
            self.backtrack(result, current + ")", open_count, close_count + 1, max_count)


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    n1 = 3
    result1 = solution.generateParenthesis(n1)
    print(f"输入: n = {n1}")
    print(f"输出: {result1}")
    
    # 测试用例2
    n2 = 1
    result2 = solution.generateParenthesis(n2)
    print(f"\n输入: n = {n2}")
    print(f"输出: {result2}")
    
    # 测试用例3
    n3 = 2
    result3 = solution.generateParenthesis(n3)
    print(f"\n输入: n = {n3}")
    print(f"输出: {result3}")


if __name__ == "__main__":
    main()
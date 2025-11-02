"""
LeetCode 22. 括号生成 (增强版)

数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且有效的括号组合。
增强版：除了生成n对括号的所有组合外，还要求计算每个组合中连续括号的最大长度。

算法思路：
使用回溯算法生成所有有效的括号组合，在生成过程中同时计算连续括号的最大长度。

时间复杂度：O(4^n / sqrt(n))，第n个卡塔兰数
空间复杂度：O(4^n / sqrt(n))
"""

class Solution:
    def generateParenthesisWithMaxConsecutive(self, n):
        """
        生成所有可能的并且有效的括号组合，并计算每个组合中连续括号的最大长度
        :param n: int 括号对数
        :return: List[Tuple[str, int]] 包含括号组合和对应最大连续长度的列表
        """
        result = []
        self.backtrack(n, n, "", 0, 0, result)
        return result
    
    def backtrack(self, left, right, current, consecutive, max_consecutive, result):
        """
        回溯函数
        :param left: int 剩余左括号数量
        :param right: int 剩余右括号数量
        :param current: str 当前生成的括号字符串
        :param consecutive: int 当前连续括号长度
        :param max_consecutive: int 当前最大连续括号长度
        :param result: List[Tuple[str, int]] 结果列表
        """
        # 终止条件：所有括号都已使用完
        if left == 0 and right == 0:
            result.append((current, max_consecutive))
            return
        
        # 剪枝：右括号不能比左括号多
        if left > right:
            return
        
        # 添加左括号
        if left > 0:
            self.backtrack(left - 1, right, current + "(", consecutive + 1, max(max_consecutive, consecutive + 1), result)
        
        # 添加右括号
        if right > 0:
            new_consecutive = consecutive - 1 if consecutive > 0 else 0
            self.backtrack(left, right - 1, current + ")", new_consecutive, max_consecutive, result)

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    print("n = 3:")
    result1 = solution.generateParenthesisWithMaxConsecutive(3)
    for combo, max_consecutive in result1:
        print(f"{combo} -> Max consecutive: {max_consecutive}")
    
    # 测试用例2
    print("\nn = 2:")
    result2 = solution.generateParenthesisWithMaxConsecutive(2)
    for combo, max_consecutive in result2:
        print(f"{combo} -> Max consecutive: {max_consecutive}")
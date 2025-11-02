"""
LeetCode 17. 电话号码的字母组合

题目描述：
给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
答案可以按任意顺序返回。

示例：
输入：digits = "23"
输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]

输入：digits = ""
输出：[]

输入：digits = "2"
输出：["a","b","c"]

提示：
0 <= digits.length <= 4
digits[i] 是范围 ['2', '9'] 的一个数字。

链接：https://leetcode.cn/problems/letter-combinations-of-a-phone-number/
"""

class Solution:
    def letterCombinations(self, digits):
        """
        生成电话号码的字母组合
        
        算法思路：
        1. 使用回溯算法生成所有可能的字母组合
        2. 建立数字到字母的映射关系
        3. 对于每个数字，遍历其对应的所有字母
        4. 递归处理下一个数字，直到处理完所有数字
        
        时间复杂度：O(3^m * 4^n)，其中m是对应3个字母的数字个数，n是对应4个字母的数字个数
        空间复杂度：O(3^m * 4^n)，用于存储所有组合
        
        :param digits: 输入的数字字符串
        :return: 所有可能的字母组合
        """
        # 边界条件：空字符串
        if not digits:
            return []
        
        # 数字到字母的映射
        mapping = ["0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"]
        
        result = []
        # 回溯生成所有组合
        self.backtrack(digits, mapping, result, "", 0)
        return result
    
    def backtrack(self, digits, mapping, result, current, index):
        """
        回溯函数生成字母组合
        
        :param digits: 输入的数字字符串
        :param mapping: 数字到字母的映射数组
        :param result: 结果列表
        :param current: 当前已生成的字符串
        :param index: 当前处理的数字索引
        """
        # 终止条件：已处理完所有数字
        if index == len(digits):
            result.append(current)
            return
        
        # 获取当前数字对应的字母
        digit = int(digits[index])
        letters = mapping[digit]
        
        # 遍历所有可能的字母
        for letter in letters:
            # 递归处理下一个数字
            self.backtrack(digits, mapping, result, current + letter, index + 1)


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    test1 = "23"
    result1 = solution.letterCombinations(test1)
    print(f'输入: "{test1}"')
    print(f"输出: {result1}")
    
    # 测试用例2
    test2 = ""
    result2 = solution.letterCombinations(test2)
    print(f'\n输入: "{test2}"')
    print(f"输出: {result2}")
    
    # 测试用例3
    test3 = "2"
    result3 = solution.letterCombinations(test3)
    print(f'\n输入: "{test3}"')
    print(f"输出: {result3}")
    
    # 测试用例4
    test4 = "234"
    result4 = solution.letterCombinations(test4)
    print(f'\n输入: "{test4}"')
    print(f"输出: {result4}")


if __name__ == "__main__":
    main()
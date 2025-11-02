"""
LeetCode 784. 字母大小写全排列

给定一个字符串S，通过将字符串S中的每个字母转变大小写，我们可以获得一个新的字符串。
返回所有可能得到的字符串集合。以任意顺序返回输出。

算法思路：
使用回溯算法遍历字符串中的每个字符，对于字母字符，分别尝试大写和小写两种情况。

时间复杂度：O(2^n * n)，其中n是字符串中字母的个数
空间复杂度：O(2^n * n)，用于存储所有可能的字符串
"""

class Solution:
    def letterCasePermutation(self, s):
        """
        返回所有可能得到的字符串集合
        :param s: str 输入字符串
        :return: List[str] 所有可能的字符串集合
        """
        result = []
        self.backtrack(list(s), 0, result)
        return result
    
    def backtrack(self, chars, index, result):
        """
        回溯函数
        :param chars: List[str] 字符列表
        :param index: int 当前处理的字符位置
        :param result: List[str] 结果列表
        """
        # 终止条件：处理完所有字符
        if index == len(chars):
            result.append("".join(chars))
            return
        
        ch = chars[index]
        
        # 如果是字母，则分别尝试大写和小写
        if ch.isalpha():
            # 尝试小写
            chars[index] = ch.lower()
            self.backtrack(chars, index + 1, result)
            
            # 尝试大写
            chars[index] = ch.upper()
            self.backtrack(chars, index + 1, result)
        else:
            # 如果不是字母，直接处理下一个字符
            self.backtrack(chars, index + 1, result)

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    print('Input: "a1b2"')
    print(solution.letterCasePermutation("a1b2"))
    
    # 测试用例2
    print('\nInput: "3z4"')
    print(solution.letterCasePermutation("3z4"))
    
    # 测试用例3
    print('\nInput: "12345"')
    print(solution.letterCasePermutation("12345"))
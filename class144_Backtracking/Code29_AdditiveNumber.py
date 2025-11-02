class Solution:
    """
    LeetCode 306. 累加数
    
    题目描述：
    累加数是一个字符串，组成它的数字可以形成累加序列。
    一个有效的累加序列必须至少包含 3 个数。除了最开始的两个数以外，字符串中的其他数都等于它之前两个数相加的和。
    
    示例：
    输入："112358"
    输出：true
    解释：累加序列为: 1, 1, 2, 3, 5, 8。1 + 1 = 2, 1 + 2 = 3, 2 + 3 = 5, 3 + 5 = 8
    
    输入："199100199"
    输出：true
    解释：累加序列为: 1, 99, 100, 199。1 + 99 = 100, 99 + 100 = 199
    
    提示：
    1 <= num.length <= 35
    num 仅由数字（0 - 9）组成
    
    链接：https://leetcode.cn/problems/additive-number/
    
    算法思路：
    1. 使用回溯算法尝试所有可能的前两个数字分割
    2. 对于每个可能的前两个数字，验证剩余部分是否满足累加关系
    3. 注意处理大数问题（字符串可能很长）
    4. 注意处理前导0的情况（数字不能有前导0，除非数字本身就是0）
    
    时间复杂度：O(n^3)，需要尝试所有可能的前两个数字分割
    空间复杂度：O(n)，递归栈深度
    """
    
    def isAdditiveNumber(self, num: str) -> bool:
        n = len(num)
        
        # 尝试所有可能的前两个数字分割
        for i in range(1, n):  # 第一个数字的结束位置
            # 第一个数字不能有前导0（除非是0本身）
            if num[0] == '0' and i > 1:
                continue
            
            for j in range(i + 1, n):  # 第二个数字的结束位置
                # 第二个数字不能有前导0（除非是0本身）
                if num[i] == '0' and j > i + 1:
                    continue
                
                # 获取前两个数字
                num1 = num[:i]
                num2 = num[i:j]
                
                # 验证剩余部分
                if self.validate(num, j, num1, num2):
                    return True
        
        return False
    
    def validate(self, num: str, start: int, num1: str, num2: str) -> bool:
        """验证从start位置开始，剩余部分是否满足累加关系"""
        if start == len(num):
            return True
        
        # 计算下一个期望的数字
        expected = self.add_strings(num1, num2)
        
        # 检查剩余字符串是否以期望数字开头
        if num.startswith(expected, start):
            # 递归验证剩余部分
            return self.validate(num, start + len(expected), num2, expected)
        
        return False
    
    def add_strings(self, num1: str, num2: str) -> str:
        """字符串加法，处理大数问题"""
        i, j = len(num1) - 1, len(num2) - 1
        carry = 0
        result = []
        
        while i >= 0 or j >= 0 or carry:
            # 获取当前位的数字
            digit1 = int(num1[i]) if i >= 0 else 0
            digit2 = int(num2[j]) if j >= 0 else 0
            
            # 计算当前位的和
            total = digit1 + digit2 + carry
            carry = total // 10
            digit = total % 10
            
            result.append(str(digit))
            
            i -= 1
            j -= 1
        
        # 反转结果
        return ''.join(result[::-1])

def test_additive_number():
    solution = Solution()
    
    # 测试用例1
    num1 = "112358"
    result1 = solution.isAdditiveNumber(num1)
    print(f'输入: num = "{num1}"')
    print("输出:", result1)
    
    # 测试用例2
    num2 = "199100199"
    result2 = solution.isAdditiveNumber(num2)
    print(f'\n输入: num = "{num2}"')
    print("输出:", result2)
    
    # 测试用例3
    num3 = "1023"
    result3 = solution.isAdditiveNumber(num3)
    print(f'\n输入: num = "{num3}"')
    print("输出:", result3)
    
    # 测试用例4
    num4 = "101"
    result4 = solution.isAdditiveNumber(num4)
    print(f'\n输入: num = "{num4}"')
    print("输出:", result4)

if __name__ == "__main__":
    test_additive_number()
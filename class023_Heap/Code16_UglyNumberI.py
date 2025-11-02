class Solution:
    """
    相关题目8: LeetCode 263. 丑数
    题目链接: https://leetcode.cn/problems/ugly-number/
    题目描述: 丑数 就是只包含质因数 2、3 和 5 的正整数。判断一个数是否是丑数。
    解题思路: 不断将数字除以2、3、5，直到无法整除，如果最终结果为1，则是丑数
    时间复杂度: O(log n)，因为每次除以至少2，数字减小的速度是对数级别的
    空间复杂度: O(1)，只使用常量额外空间
    是否最优解: 是，这是判断丑数的最优解法
    
    本题属于数学问题的一种，虽然不直接使用堆，但可以作为堆相关问题(如UglyNumberII)的基础
    """
    
    def isUgly(self, n):
        """
        判断一个数是否是丑数
        
        Args:
            n: 需要判断的整数
            
        Returns:
            bool: 如果n是丑数返回True，否则返回False
        """
        # 根据题目要求，丑数是正整数
        if n <= 0:
            return False  # 题目明确指出丑数是正整数，所以负数和0都不是丑数
        
        # 调试信息：打印当前处理的数
        # print(f"判断是否是丑数: {n}")
        
        # 不断除以2，直到不能再整除
        while n % 2 == 0:
            n = n // 2  # 使用整除运算符，避免浮点数
        
        # 不断除以3，直到不能再整除
        while n % 3 == 0:
            n = n // 3
        
        # 不断除以5，直到不能再整除
        while n % 5 == 0:
            n = n // 5
        
        # 如果最终结果为1，则说明n的所有质因数只有2、3、5，是丑数
        return n == 1

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：基本情况 - 是丑数
    print("测试用例1：")
    test1 = solution.isUgly(6)
    print(f"6是否是丑数: {test1}")  # 6 = 2 * 3，应该返回 True
    assert test1 == True, "测试失败：6应该是丑数"
    
    test2 = solution.isUgly(1)
    print(f"1是否是丑数: {test2}")  # 1 没有质因数，所以是丑数，应该返回 True
    assert test2 == True, "测试失败：1应该是丑数"
    
    test3 = solution.isUgly(12)
    print(f"12是否是丑数: {test3}")  # 12 = 2^2 * 3，应该返回 True
    assert test3 == True, "测试失败：12应该是丑数"
    
    # 测试用例2：基本情况 - 不是丑数
    print("\n测试用例2：")
    test4 = solution.isUgly(14)
    print(f"14是否是丑数: {test4}")  # 14 = 2 * 7，包含质因数7，不是丑数，应该返回 False
    assert test4 == False, "测试失败：14不应该是丑数"
    
    test5 = solution.isUgly(21)
    print(f"21是否是丑数: {test5}")  # 21 = 3 * 7，包含质因数7，不是丑数，应该返回 False
    assert test5 == False, "测试失败：21不应该是丑数"
    
    # 测试用例3：边界情况
    print("\n测试用例3：")
    test6 = solution.isUgly(0)
    print(f"0是否是丑数: {test6}")  # 0 不是正整数，所以不是丑数，应该返回 False
    assert test6 == False, "测试失败：0不应该是丑数"
    
    test7 = solution.isUgly(-6)
    print(f"-6是否是丑数: {test7}")  # -6 是负数，所以不是丑数，应该返回 False
    assert test7 == False, "测试失败：-6不应该是丑数"
    
    test8 = solution.isUgly(1073741824)  # 2^30
    print(f"2^30是否是丑数: {test8}")  # 应该返回 True
    assert test8 == True, "测试失败：2^30应该是丑数"
    
    # 测试用例4：特殊情况
    print("\n测试用例4：")
    test9 = solution.isUgly(5)
    print(f"5是否是丑数: {test9}")  # 5 是质因数之一，应该返回 True
    assert test9 == True, "测试失败：5应该是丑数"
    
    test10 = solution.isUgly(100)  # 100 = 2^2 * 5^2
    print(f"100是否是丑数: {test10}")  # 应该返回 True
    assert test10 == True, "测试失败：100应该是丑数"
    
    print("\n所有测试用例通过！")

# 运行测试
if __name__ == "__main__":
    test_solution()
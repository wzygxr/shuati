# 单调递增的数字
# 当且仅当每个相邻位数上的数字 x 和 y 满足 x <= y 时，我们称这个整数是单调递增的
# 给定一个整数 n ，返回 小于或等于 n 的最大数字，且数字呈单调递增
# 测试链接: https://leetcode.cn/problems/monotone-increasing-digits/

class Solution:
    def monotoneIncreasingDigits(self, n: int) -> int:
        """
        单调递增的数字问题的贪心解法
        
        解题思路：
        1. 从右向左遍历数字，找到第一个不满足单调递增的位置
        2. 将该位置减1，并将后面的所有数字都设为9
        3. 重复这个过程直到整个数字满足单调递增
        
        贪心策略的正确性：
        局部最优：遇到strNum[i - 1] > strNum[i]的情况，让strNum[i - 1]减一，strNum[i]设为9
        全局最优：得到小于等于N的最大单调递增整数
        
        时间复杂度：O(d)，其中d是数字的位数
        空间复杂度：O(d)，需要将数字转换为字符数组
        
        Args:
            n: 输入数字
            
        Returns:
            小于或等于n的最大单调递增数字
        """
        # 边界条件处理
        if n < 10:
            return n
        
        # 将数字转换为字符列表便于处理
        digits = list(str(n))
        length = len(digits)
        
        # 标记需要修改的位置
        flag = length
        
        # 从右向左遍历，找到第一个不满足单调递增的位置
        for i in range(length - 1, 0, -1):
            if digits[i] < digits[i - 1]:
                # 当前位置需要减1
                digits[i - 1] = str(int(digits[i - 1]) - 1)
                # 标记从当前位置开始需要设为9
                flag = i
        
        # 将标记位置及之后的所有数字设为9
        for i in range(flag, length):
            digits[i] = '9'
        
        # 将字符列表转换回数字
        return int(''.join(digits))
    
    def monotoneIncreasingDigits2(self, n: int) -> int:
        """
        单调递增的数字问题的另一种解法（更直观）
        
        解题思路：
        1. 从左向右遍历，找到第一个不满足单调递增的位置
        2. 从该位置开始向前回溯，找到需要减1的位置
        3. 将该位置减1，后面的所有位置设为9
        
        时间复杂度：O(d)
        空间复杂度：O(d)
        """
        if n < 10:
            return n
        
        digits = list(str(n))
        length = len(digits)
        
        # 从左向右找到第一个不满足单调递增的位置
        i = 1
        while i < length and digits[i] >= digits[i - 1]:
            i += 1
        
        # 如果整个数字已经单调递增，直接返回
        if i == length:
            return n
        
        # 向前回溯，找到需要减1的位置
        while i > 0 and digits[i] < digits[i - 1]:
            digits[i - 1] = str(int(digits[i - 1]) - 1)
            i -= 1
        
        # 将后面的所有数字设为9
        for j in range(i + 1, length):
            digits[j] = '9'
        
        return int(''.join(digits))

# 测试代码
def test_monotone_increasing_digits():
    solution = Solution()
    
    # 测试用例1
    # 输入: n = 10
    # 输出: 9
    # 解释: 10不是单调递增数字，最大单调递增数字是9
    print(f"测试用例1结果: {solution.monotoneIncreasingDigits(10)}")  # 期望输出: 9
    
    # 测试用例2
    # 输入: n = 1234
    # 输出: 1234
    # 解释: 1234本身就是单调递增数字
    print(f"测试用例2结果: {solution.monotoneIncreasingDigits(1234)}")  # 期望输出: 1234
    
    # 测试用例3
    # 输入: n = 332
    # 输出: 299
    # 解释: 332不是单调递增，最大单调递增数字是299
    print(f"测试用例3结果: {solution.monotoneIncreasingDigits(332)}")  # 期望输出: 299
    
    # 测试用例4：边界情况
    # 输入: n = 1
    # 输出: 1
    print(f"测试用例4结果: {solution.monotoneIncreasingDigits(1)}")  # 期望输出: 1
    
    # 测试用例5：复杂情况
    # 输入: n = 100
    # 输出: 99
    print(f"测试用例5结果: {solution.monotoneIncreasingDigits(100)}")  # 期望输出: 99
    
    # 测试用例6
    # 输入: n = 1234321
    # 输出: 1233999
    print(f"测试用例6结果: {solution.monotoneIncreasingDigits(1234321)}")  # 期望输出: 1233999

if __name__ == "__main__":
    test_monotone_increasing_digits()
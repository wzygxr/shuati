import time

class Code32_MonotoneIncreasingDigits:
    """
    单调递增的数字
    
    题目描述：
    给定一个非负整数 N，找出小于或等于 N 的最大的整数，同时这个整数需要满足其各个位数上的数字是单调递增。
    （当且仅当每个相邻位数上的数字 x 和 y 满足 x <= y 时，我们称这个整数是单调递增的。）
    
    来源：LeetCode 738
    链接：https://leetcode.cn/problems/monotone-increasing-digits/
    
    算法思路：
    使用贪心算法：
    1. 将数字转换为字符数组方便处理
    2. 从右向左遍历，找到第一个不满足单调递增的位置
    3. 将该位置数字减1，并将后面所有数字设为9
    4. 继续向左检查，确保整个数字单调递增
    
    时间复杂度：O(logN) - 数字的位数
    空间复杂度：O(logN) - 字符数组的空间
    
    关键点分析：
    - 贪心策略：找到第一个不满足条件的位置进行调整
    - 数字处理：字符数组操作和转换
    - 边界处理：处理0和边界情况
    
    工程化考量：
    - 输入验证：检查数字是否非负
    - 性能优化：避免不必要的转换
    - 可读性：清晰的变量命名和注释
    """
    
    @staticmethod
    def monotone_increasing_digits(N: int) -> int:
        """
        找到小于等于N的最大单调递增数字
        
        Args:
            N: 输入数字
            
        Returns:
            int: 最大的单调递增数字
        """
        # 输入验证
        if N < 0:
            raise ValueError("输入数字必须是非负整数")
        if N < 10:
            return N  # 单个数字总是单调递增的
        
        # 将数字转换为字符数组
        digits = list(str(N))
        n = len(digits)
        
        # 标记需要修改的位置
        mark = n
        
        # 从右向左遍历，找到第一个不满足单调递增的位置
        for i in range(n - 1, 0, -1):
            if digits[i] < digits[i - 1]:
                mark = i
                # 前一位数字减1
                digits[i - 1] = str(int(digits[i - 1]) - 1)
        
        # 将mark位置及后面的所有数字设为9
        for i in range(mark, n):
            digits[i] = '9'
        
        # 转换回数字
        return int(''.join(digits))
    
    @staticmethod
    def monotone_increasing_digits_math(N: int) -> int:
        """
        另一种实现：使用数学运算而非字符数组
        时间复杂度：O(logN)
        空间复杂度：O(1)
        
        正确实现思路：
        1. 从右向左遍历数字的每一位
        2. 找到第一个不满足单调递增的位置
        3. 将该位置减1，后面所有位置设为9
        """
        if N < 0:
            raise ValueError("输入数字必须是非负整数")
        if N < 10:
            return N
        
        digits = []
        n = N
        
        # 将数字分解为各位数字
        while n > 0:
            digits.append(n % 10)
            n //= 10
        digits.reverse()
        
        n = len(digits)
        mark = n
        
        # 从右向左找到第一个不满足条件的位置
        for i in range(n - 1, 0, -1):
            if digits[i] < digits[i - 1]:
                mark = i
                digits[i - 1] -= 1
        
        # 将mark及后面的数字设为9
        for i in range(mark, n):
            digits[i] = 9
        
        # 重新组合数字
        result = 0
        for digit in digits:
            result = result * 10 + digit
        
        return result
    
    @staticmethod
    def monotone_increasing_digits_recursive(N: int) -> int:
        """
        递归解法
        时间复杂度：O(logN)
        空间复杂度：O(logN) - 递归栈深度
        """
        if N < 0:
            raise ValueError("输入数字必须是非负整数")
        if N < 10:
            return N
        
        digits = str(N)
        result_str = Code32_MonotoneIncreasingDigits._helper(digits, 0)
        return int(result_str)
    
    @staticmethod
    def _helper(digits: str, index: int) -> str:
        """递归辅助函数"""
        if index == len(digits) - 1:
            return digits[index]
        
        # 递归处理后面的数字
        rest = Code32_MonotoneIncreasingDigits._helper(digits, index + 1)
        
        # 如果当前数字大于后面数字的首位，需要调整
        if digits[index] > rest[0]:
            # 当前数字减1，后面全部设为9
            if digits[index] > '1':
                # 当前数字可以减1
                result = str(int(digits[index]) - 1)
                result += '9' * (len(digits) - index - 1)
                return result
            else:
                # 当前数字是1，不能减1，需要特殊处理
                return '9' * (len(digits) - index - 1)
        else:
            # 当前数字可以保持不变
            return digits[index] + rest
    
    @staticmethod
    def is_monotone_increasing(num: int) -> bool:
        """
        验证数字是否单调递增
        
        Args:
            num: 要验证的数字
            
        Returns:
            bool: 是否单调递增
        """
        if num < 10:
            return True
        
        s = str(num)
        for i in range(1, len(s)):
            if s[i] < s[i - 1]:
                return False
        return True
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 单调递增的数字测试 ===")
        
        # 测试用例1: N = 10 -> 9
        N1 = 10
        print(f"测试用例1: N = {N1}")
        result1 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits(N1)
        result2 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_math(N1)
        result3 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_recursive(N1)
        print(f"方法1结果: {result1}")  # 9
        print(f"方法2结果: {result2}")  # 9
        print(f"方法3结果: {result3}")  # 9
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result1)}")  # True
        
        # 测试用例2: N = 1234 -> 1234
        N2 = 1234
        print(f"\n测试用例2: N = {N2}")
        result1 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits(N2)
        result2 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_math(N2)
        result3 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_recursive(N2)
        print(f"方法1结果: {result1}")  # 1234
        print(f"方法2结果: {result2}")  # 1234
        print(f"方法3结果: {result3}")  # 1234
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result1)}")  # True
        
        # 测试用例3: N = 332 -> 299
        N3 = 332
        print(f"\n测试用例3: N = {N3}")
        result1 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits(N3)
        result2 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_math(N3)
        result3 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_recursive(N3)
        print(f"方法1结果: {result1}")  # 299
        print(f"方法2结果: {result2}")  # 299
        print(f"方法3结果: {result3}")  # 299
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result1)}")  # True
        
        # 测试用例4: N = 100 -> 99
        N4 = 100
        print(f"\n测试用例4: N = {N4}")
        result1 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits(N4)
        result2 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_math(N4)
        result3 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_recursive(N4)
        print(f"方法1结果: {result1}")  # 99
        print(f"方法2结果: {result2}")  # 99
        print(f"方法3结果: {result3}")  # 99
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result1)}")  # True
        
        # 测试用例5: N = 9 -> 9
        N5 = 9
        print(f"\n测试用例5: N = {N5}")
        result1 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits(N5)
        result2 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_math(N5)
        result3 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_recursive(N5)
        print(f"方法1结果: {result1}")  # 9
        print(f"方法2结果: {result2}")  # 9
        print(f"方法3结果: {result3}")  # 9
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result1)}")  # True
        
        # 边界测试：N = 0
        N6 = 0
        print(f"\n测试用例6: N = {N6}")
        result1 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits(N6)
        result2 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_math(N6)
        result3 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_recursive(N6)
        print(f"方法1结果: {result1}")  # 0
        print(f"方法2结果: {result2}")  # 0
        print(f"方法3结果: {result3}")  # 0
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result1)}")  # True
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        large_N = 1000000000  # 10亿
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits(large_N)
        end_time1 = time.time()
        print(f"方法1执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"结果: {result1}")
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result1)}")
        
        start_time2 = time.time()
        result2 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_math(large_N)
        end_time2 = time.time()
        print(f"方法2执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"结果: {result2}")
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result2)}")
        
        start_time3 = time.time()
        result3 = Code32_MonotoneIncreasingDigits.monotone_increasing_digits_recursive(large_N)
        end_time3 = time.time()
        print(f"方法3执行时间: {(end_time3 - start_time3) * 1000:.2f} 毫秒")
        print(f"结果: {result3}")
        print(f"验证: {Code32_MonotoneIncreasingDigits.is_monotone_increasing(result3)}")
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("方法1（字符数组）:")
        print("- 时间复杂度: O(logN)")
        print("  - 数字位数: O(logN)")
        print("  - 遍历处理: O(logN)")
        print("- 空间复杂度: O(logN)")
        print("  - 字符数组: O(logN)")
        
        print("\n方法2（数学运算）:")
        print("- 时间复杂度: O(logN)")
        print("  - 数字位数: O(logN)")
        print("  - 数学运算: O(logN)")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n方法3（递归）:")
        print("- 时间复杂度: O(logN)")
        print("  - 递归深度: O(logN)")
        print("  - 每次递归操作: O(1)")
        print("- 空间复杂度: O(logN)")
        print("  - 递归栈深度: O(logN)")
        
        print("\n贪心策略证明:")
        print("1. 找到第一个不满足条件的位置是最优调整点")
        print("2. 将该位置减1，后面设为9可以保证得到最大可能值")
        print("3. 数学归纳法证明贪心选择性质")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理负数和边界情况")
        print("2. 性能优化：选择合适的数字处理方法")
        print("3. 可读性：清晰的算法逻辑和注释")
        print("4. 测试覆盖：全面的测试用例设计")

def main():
    """主函数"""
    Code32_MonotoneIncreasingDigits.run_tests()
    Code32_MonotoneIncreasingDigits.performance_test()
    Code32_MonotoneIncreasingDigits.analyze_complexity()

if __name__ == "__main__":
    main()
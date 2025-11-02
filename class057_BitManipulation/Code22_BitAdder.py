import sys

class BitAdder:
    """
    位运算实现加法器
    测试链接：自定义题目，展示位运算在计算机底层运算中的应用
    
    题目描述：
    使用位运算实现整数的加法、减法、乘法和除法运算。
    不使用+、-、*、/等算术运算符。
    
    解题思路：
    1. 加法：使用异或运算实现不考虑进位的加法，使用与运算和左移实现进位处理
    2. 减法：利用补码原理，a - b = a + (-b)，通过位运算求补码
    3. 乘法：使用移位和加法实现，类似于手算乘法
    4. 除法：使用移位和减法实现，类似于手算除法
    
    时间复杂度：
    - 加法：O(1) - 常数次循环（最多32次）
    - 减法：O(1) - 基于加法实现
    - 乘法：O(1) - 最多32次循环
    - 除法：O(1) - 最多32次循环
    
    空间复杂度：O(1) - 只使用常数个变量
    """
    
    @staticmethod
    def add(a: int, b: int) -> int:
        """
        使用位运算实现整数加法
        
        Args:
            a: 第一个加数
            b: 第二个加数
            
        Returns:
            a + b 的结果
        """
        # 由于Python整数没有固定位数，需要限制在32位范围内
        MASK = 0xFFFFFFFF
        MAX_INT = 0x7FFFFFFF
        
        # 思路：加法可以分解为不考虑进位的加法（异或运算）和进位（与运算并左移）
        # 重复这个过程直到进位为0
        
        a = a & MASK
        b = b & MASK
        
        while b != 0:
            # 计算不考虑进位的加法结果
            sum_val = a ^ b
            # 计算进位（需要左移一位）
            carry = (a & b) << 1
            # 限制在32位范围内
            carry = carry & MASK
            # 更新a和b，继续处理进位
            a = sum_val
            b = carry
        
        # 处理负数（Python使用补码表示）
        if a > MAX_INT:
            a = ~(a ^ MASK)
        
        return a
    
    @staticmethod
    def add_recursive(a: int, b: int) -> int:
        """
        递归实现加法
        
        Args:
            a: 第一个加数
            b: 第二个加数
            
        Returns:
            a + b 的结果
        """
        if b == 0:
            return a
        # 递归计算：a + b = (a ^ b) + ((a & b) << 1)
        return BitAdder.add_recursive(a ^ b, (a & b) << 1)
    
    @staticmethod
    def subtract(a: int, b: int) -> int:
        """
        使用位运算实现整数减法
        
        Args:
            a: 被减数
            b: 减数
            
        Returns:
            a - b 的结果
        """
        # 利用补码原理：a - b = a + (-b)
        # 求b的补码（按位取反再加1）
        return BitAdder.add(a, BitAdder.add(~b, 1))
    
    @staticmethod
    def multiply(a: int, b: int) -> int:
        """
        使用位运算实现整数乘法
        
        Args:
            a: 被乘数
            b: 乘数
            
        Returns:
            a * b 的结果
        """
        # 处理特殊情况
        if a == 0 or b == 0:
            return 0
        if a == 1:
            return b
        if b == 1:
            return a
        
        # 记录结果的符号
        negative = False
        if (a < 0 and b > 0) or (a > 0 and b < 0):
            negative = True
        
        # 取绝对值
        a = abs(a)
        b = abs(b)
        
        result = 0
        
        # 类似于手算乘法：将b的每一位与a相乘，然后左移相应的位数
        while b != 0:
            # 如果b的最低位是1，则加上a左移相应的位数
            if b & 1:
                result = BitAdder.add(result, a)
            # a左移一位，相当于乘以2
            a <<= 1
            # b右移一位，处理下一位
            b >>= 1
        
        return -result if negative else result
    
    @staticmethod
    def divide(dividend: int, divisor: int) -> int:
        """
        使用位运算实现整数除法（向下取整）
        
        Args:
            dividend: 被除数
            divisor: 除数
            
        Returns:
            dividend / divisor 的结果
            
        Raises:
            ZeroDivisionError: 当除数为0时
        """
        # 处理特殊情况
        if divisor == 0:
            raise ZeroDivisionError("Division by zero")
        if dividend == 0:
            return 0
        if divisor == 1:
            return dividend
        if divisor == -1:
            # 处理整数溢出情况
            if dividend == -sys.maxsize - 1:
                return sys.maxsize
            return -dividend
        
        # 记录结果的符号
        negative = (dividend < 0) ^ (divisor < 0)
        
        # 取绝对值
        a = abs(dividend)
        b = abs(divisor)
        
        result = 0
        
        # 从最高位开始尝试
        for i in range(31, -1, -1):
            if (a >> i) >= b:
                result = BitAdder.add(result, 1 << i)
                a = BitAdder.subtract(a, b << i)
        
        return -result if negative else result
    
    @staticmethod
    def divide_robust(dividend: int, divisor: int) -> int:
        """
        更稳健的除法实现
        
        Args:
            dividend: 被除数
            divisor: 除数
            
        Returns:
            dividend / divisor 的结果
        """
        # 处理除数为0的情况
        if divisor == 0:
            return sys.maxsize if dividend >= 0 else -sys.maxsize - 1
        
        # 处理被除数为最小值且除数为-1的情况（会溢出）
        if dividend == -sys.maxsize - 1 and divisor == -1:
            return sys.maxsize
        
        # 确定符号
        negative = (dividend < 0) ^ (divisor < 0)
        
        # 取绝对值
        a = abs(dividend)
        b = abs(divisor)
        
        result = 0
        
        # 使用减法实现除法
        while a >= b:
            temp = b
            multiple = 1
            
            # 快速倍增：每次将除数翻倍，直到接近被除数
            while a >= (temp << 1):
                temp <<= 1
                multiple <<= 1
            
            a -= temp
            result += multiple
        
        return -result if negative else result

# 测试代码
if __name__ == "__main__":
    # 测试加法
    print("=== 加法测试 ===")
    print(f"5 + 3 = {BitAdder.add(5, 3)}")  # 8
    print(f"-5 + 3 = {BitAdder.add(-5, 3)}")  # -2
    print(f"5 + (-3) = {BitAdder.add(5, -3)}")  # 2
    print(f"-5 + (-3) = {BitAdder.add(-5, -3)}")  # -8
    
    # 测试减法
    print("\n=== 减法测试 ===")
    print(f"5 - 3 = {BitAdder.subtract(5, 3)}")  # 2
    print(f"3 - 5 = {BitAdder.subtract(3, 5)}")  # -2
    print(f"-5 - 3 = {BitAdder.subtract(-5, 3)}")  # -8
    print(f"5 - (-3) = {BitAdder.subtract(5, -3)}")  # 8
    
    # 测试乘法
    print("\n=== 乘法测试 ===")
    print(f"5 * 3 = {BitAdder.multiply(5, 3)}")  # 15
    print(f"5 * (-3) = {BitAdder.multiply(5, -3)}")  # -15
    print(f"-5 * 3 = {BitAdder.multiply(-5, 3)}")  # -15
    print(f"-5 * (-3) = {BitAdder.multiply(-5, -3)}")  # 15
    
    # 测试除法
    print("\n=== 除法测试 ===")
    print(f"15 / 3 = {BitAdder.divide(15, 3)}")  # 5
    print(f"15 / (-3) = {BitAdder.divide(15, -3)}")  # -5
    print(f"-15 / 3 = {BitAdder.divide(-15, 3)}")  # -5
    print(f"-15 / (-3) = {BitAdder.divide(-15, -3)}")  # 5
    print(f"16 / 3 = {BitAdder.divide(16, 3)}")  # 5（向下取整）
    
    # 测试边界情况
    print("\n=== 边界测试 ===")
    print(f"0 + 5 = {BitAdder.add(0, 5)}")  # 5
    print(f"5 * 0 = {BitAdder.multiply(5, 0)}")  # 0
    print(f"0 / 5 = {BitAdder.divide(0, 5)}")  # 0
    
    # 验证递归加法
    print("\n=== 递归加法测试 ===")
    print(f"5 + 3 = {BitAdder.add_recursive(5, 3)}")  # 8
    print(f"-5 + 3 = {BitAdder.add_recursive(-5, 3)}")  # -2
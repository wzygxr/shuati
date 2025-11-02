import math
import time
import random
from typing import List
import numpy as np

class Complex:
    """
    复数类，用于FFT计算
    也可以使用Python内置的complex类，但这里为了清晰展示计算过程，我们自己实现
    """
    def __init__(self, real: float, imag: float):
        self.re = real  # 实部
        self.im = imag  # 虚部
    
    def add(self, other):
        """复数加法"""
        return Complex(self.re + other.re, self.im + other.im)
    
    def subtract(self, other):
        """复数减法"""
        return Complex(self.re - other.re, self.im - other.im)
    
    def multiply(self, other):
        """复数乘法"""
        return Complex(
            self.re * other.re - self.im * other.im,
            self.re * other.im + self.im * other.re
        )
    
    def __str__(self):
        if self.im == 0:
            return f"{self.re}"
        elif self.re == 0:
            return f"{self.im}i"
        else:
            op = "+" if self.im > 0 else "-"
            return f"{self.re} {op} {abs(self.im)}i"


def fft_multiply(x: str, y: str) -> str:
    """
    使用快速傅里叶变换进行大整数乘法
    时间复杂度：O(n log n)
    
    Args:
        x: 第一个整数的字符串表示
        y: 第二个整数的字符串表示
    
    Returns:
        乘积的字符串表示
    """
    # 处理特殊情况
    if x == "0" or y == "0":
        return "0"
    if x == "1":
        return y
    if y == "1":
        return x

    # 将字符串转换为数字列表（低位在前）
    x_digits = string_to_digits(x)
    y_digits = string_to_digits(y)

    # 调用FFT乘法算法
    product = fft_multiply_digits(x_digits, y_digits)

    # 移除前导零并转换为字符串
    return digits_to_string(product)


def fft_multiply_digits(a: List[int], b: List[int]) -> List[int]:
    """
    使用FFT算法对两个数字列表进行乘法
    
    Args:
        a: 第一个整数的数字列表（低位在前）
        b: 第二个整数的数字列表（低位在前）
    
    Returns:
        乘积的数字列表（低位在前）
    """
    n = 1
    m = len(a) + len(b) - 1

    # 计算大于等于len(a) + len(b) - 1的最小的2的幂次
    while n < m:
        n <<= 1

    # 转换为复数列表
    fa = [Complex(a[i], 0) if i < len(a) else Complex(0, 0) for i in range(n)]
    fb = [Complex(b[i], 0) if i < len(b) else Complex(0, 0) for i in range(n)]

    # 执行FFT
    fa = fft(fa, False)
    fb = fft(fb, False)

    # 点乘得到频域乘积
    fc = [fa[i].multiply(fb[i]) for i in range(n)]

    # 执行逆FFT得到时域结果
    fc = fft(fc, True)

    # 将复数结果转换为整数，并处理进位
    result = [round(fc[i].re) for i in range(m)]

    # 处理进位
    process_carries(result)

    return result


def fft(x: List[Complex], inverse: bool) -> List[Complex]:
    """
    快速傅里叶变换实现
    使用Cooley-Tukey算法进行递归实现
    
    Args:
        x: 输入复数列表
        inverse: 是否为逆FFT
    
    Returns:
        变换后的复数列表
    """
    n = len(x)

    # 递归终止条件
    if n == 1:
        return [x[0]]

    # 确保n是2的幂次
    if (n & (n - 1)) != 0:
        raise ValueError("n必须是2的幂次")

    # 分割偶数索引和奇数索引
    even = [x[2 * i] for i in range(n // 2)]
    odd = [x[2 * i + 1] for i in range(n // 2)]

    # 递归FFT
    even = fft(even, inverse)
    odd = fft(odd, inverse)

    # 合并结果
    result = [Complex(0, 0)] * n
    angle = 2 * math.pi / n * (-1 if inverse else 1)
    w = Complex(1, 0)
    wn = Complex(math.cos(angle), math.sin(angle))

    for i in range(n // 2):
        t = w.multiply(odd[i])
        result[i] = even[i].add(t)
        result[i + n // 2] = even[i].subtract(t)
        w = w.multiply(wn)

    # 如果是逆FFT，需要除以n
    if inverse:
        for i in range(n):
            result[i] = Complex(result[i].re / n, result[i].im / n)

    return result


def process_carries(result: List[int]):
    """
    处理大整数乘法结果中的进位
    
    Args:
        result: 乘法结果数字列表（将被原地修改）
    """
    carry = 0
    for i in range(len(result)):
        sum_val = result[i] + carry
        result[i] = sum_val % 10
        carry = sum_val // 10

    # 如果还有进位，需要扩展列表
    while carry > 0:
        result.append(carry % 10)
        carry = carry // 10


def naive_multiply(x: str, y: str) -> str:
    """
    使用传统方法进行大整数乘法（用于比较性能）
    时间复杂度：O(n²)
    
    Args:
        x: 第一个整数的字符串表示
        y: 第二个整数的字符串表示
    
    Returns:
        乘积的字符串表示
    """
    x_digits = string_to_digits(x)
    y_digits = string_to_digits(y)
    result = [0] * (len(x_digits) + len(y_digits))

    for i in range(len(x_digits)):
        for j in range(len(y_digits)):
            result[i + j] += x_digits[i] * y_digits[j]
            result[i + j + 1] += result[i + j] // 10
            result[i + j] %= 10

    # 移除前导零
    while len(result) > 1 and result[-1] == 0:
        result.pop()

    return digits_to_string(result)


def string_to_digits(s: str) -> List[int]:
    """
    将字符串转换为数字列表（低位在前）
    
    Args:
        s: 数字字符串
    
    Returns:
        数字列表（低位在前）
    """
    return [int(s[len(s) - 1 - i]) for i in range(len(s))]


def digits_to_string(digits: List[int]) -> str:
    """
    将数字列表转换为字符串（低位在前转换为正常表示）
    
    Args:
        digits: 数字列表（低位在前）
    
    Returns:
        数字字符串
    """
    return ''.join(str(digits[i]) for i in range(len(digits) - 1, -1, -1))


def benchmark(size: int):
    """
    性能测试方法，比较FFT算法与其他算法
    
    Args:
        size: 测试数字的位数
    """
    # 生成测试用的大整数
    num1 = generate_random_number(size)
    num2 = generate_random_number(size)

    # 测试FFT算法
    start_time = time.time()
    result_fft = fft_multiply(num1, num2)
    fft_time = (time.time() - start_time) * 1000  # 转换为毫秒

    try:
        # 测试numpy FFT实现（更快的版本）
        start_time = time.time()
        result_numpy = numpy_fft_multiply(num1, num2)
        numpy_time = (time.time() - start_time) * 1000  # 转换为毫秒
        print(f"numpy FFT算法耗时: {numpy_time:.2f} ms")
    except:
        result_numpy = result_fft
        numpy_time = float('inf')

    # 测试传统算法（对于较小的数字）
    result_naive = ""
    naive_time = 0
    if size <= 500:  # 对于大数字，传统算法可能太慢
        start_time = time.time()
        result_naive = naive_multiply(num1, num2)
        naive_time = (time.time() - start_time) * 1000  # 转换为毫秒
        print(f"传统算法耗时: {naive_time:.2f} ms")
        if size <= 500:
            print(f"算法加速比 (传统/FFT): {naive_time / fft_time:.2f}x")
            print(f"结果一致 (FFT vs 传统): {result_fft == result_naive}")

    print(f"数字位数: {size}")
    print(f"FFT算法耗时: {fft_time:.2f} ms")
    if numpy_time < float('inf'):
        print(f"算法加速比 (手动FFT/numpy FFT): {fft_time / numpy_time:.2f}x")
        print(f"结果一致 (手动FFT vs numpy FFT): {result_fft == result_numpy}")
    print(f"乘积位数: {len(result_fft)}")


def generate_random_number(length: int) -> str:
    """
    生成指定长度的随机数字字符串
    
    Args:
        length: 字符串长度
    
    Returns:
        随机数字字符串
    """
    # 第一位不能是0
    first_digit = str(random.randint(1, 9))
    # 生成剩余位
    other_digits = ''.join(str(random.randint(0, 9)) for _ in range(length - 1))
    return first_digit + other_digits


def numpy_fft_multiply(x: str, y: str) -> str:
    """
    使用numpy的FFT实现进行大整数乘法
    这是一个更快的版本，仅在numpy可用时使用
    
    Args:
        x: 第一个整数的字符串表示
        y: 第二个整数的字符串表示
    
    Returns:
        乘积的字符串表示
    """
    # 将字符串转换为数字列表（低位在前）
    x_digits = np.array(string_to_digits(x), dtype=np.float64)
    y_digits = np.array(string_to_digits(y), dtype=np.float64)
    
    n = 1
    m = len(x_digits) + len(y_digits) - 1
    while n < m:
        n <<= 1
    
    # 补零到2的幂次长度
    x_padded = np.zeros(n, dtype=np.complex128)
    y_padded = np.zeros(n, dtype=np.complex128)
    x_padded[:len(x_digits)] = x_digits
    y_padded[:len(y_digits)] = y_digits
    
    # FFT
    x_fft = np.fft.fft(x_padded)
    y_fft = np.fft.fft(y_padded)
    
    # 点乘
    z_fft = x_fft * y_fft
    
    # 逆FFT
    z = np.fft.ifft(z_fft)
    
    # 取实部并四舍五入
    result = np.round(np.real(z[:m])).astype(int)
    
    # 处理进位
    result_list = result.tolist()
    process_carries(result_list)
    
    return digits_to_string(result_list)


def verify_correctness():
    """
    验证算法正确性
    """
    test_cases = [
        ("1234", "5678"),
        ("9999", "9999"),
        ("0", "12345"),
        ("1", "98765"),
        ("999999", "999999")
    ]

    for x, y in test_cases:
        # 使用FFT算法
        result = fft_multiply(x, y)
        
        # 对于小数字，使用Python内置的大整数类验证
        if len(x) <= 18 and len(y) <= 18:  # 确保可以放入Python的int
            num1 = int(x)
            num2 = int(y)
            expected = str(num1 * num2)
            print(f"{x} * {y} = {result} (正确: {result == expected})")
        else:
            print(f"{x} * {y} = {result}")
            print(f"乘积位数: {len(result)}")


def interactive_mode():
    """
    交互式测试函数
    """
    print("请输入两个大整数进行乘法计算（输入exit退出）:")
    while True:
        num1 = input("第一个数: ")
        if num1.lower() == "exit":
            break
        
        num2 = input("第二个数: ")
        if num2.lower() == "exit":
            break
        
        # 验证输入是否为有效数字
        if not num1.isdigit() or not num2.isdigit():
            print("错误: 请输入有效的正整数")
            continue
        
        try:
            start_time = time.time()
            result = fft_multiply(num1, num2)
            end_time = time.time()
            time_taken = (end_time - start_time) * 1000  # 转换为毫秒
            
            print(f"结果: {result}")
            print(f"计算耗时: {time_taken:.2f} ms")
        except Exception as e:
            print(f"计算过程中发生错误: {e}")


if __name__ == "__main__":
    print("验证算法正确性:")
    verify_correctness()
    
    print("\n性能测试:")
    benchmark(100)
    benchmark(1000)
    
    print("\n交互式测试:")
    try:
        interactive_mode()
    except KeyboardInterrupt:
        print("\n程序已中断")
    except Exception as e:
        print(f"发生错误: {e}")
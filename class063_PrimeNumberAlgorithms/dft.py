# 离散傅里叶变换（DFT）的Python实现
# 时间复杂度：O(n²)
# 空间复杂度：O(n)

import math
import time
import bisect

class Complex:
    """
    复数类，支持基本的复数运算
    时间复杂度：所有操作O(1)
    空间复杂度：O(1)
    """
    def __init__(self, real=0.0, imag=0.0):
        self.real = real
        self.imag = imag
    
    def __add__(self, other):
        """复数加法"""
        return Complex(self.real + other.real, self.imag + other.imag)
    
    def __sub__(self, other):
        """复数减法"""
        return Complex(self.real - other.real, self.imag - other.imag)
    
    def __mul__(self, other):
        """复数乘法"""
        return Complex(
            self.real * other.real - self.imag * other.imag,
            self.real * other.imag + self.imag * other.real
        )
    
    def __truediv__(self, divisor):
        """复数除法（除以标量）"""
        return Complex(self.real / divisor, self.imag / divisor)
    
    def __repr__(self):
        """官方字符串表示"""
        return f"({self.real}, {self.imag})"

# 离散傅里叶变换（DFT）
def dft(signal, invert=False):
    """
    计算离散傅里叶变换
    时间复杂度：O(n²)
    空间复杂度：O(n)
    
    参数：
    signal -- 复数列表，表示输入信号
    invert -- 是否计算逆变换
    
    返回：
    变换后的复数列表
    """
    n = len(signal)
    result = [Complex() for _ in range(n)]
    
    for k in range(n):
        for j in range(n):
            # 计算旋转因子 W_n^(kj) = e^(-2πikj/n) 或 W_n^(-kj) = e^(2πikj/n)
            angle = 2 * math.pi * k * j / n
            if invert:
                angle = -angle  # 逆变换时角度取反
            w = Complex(math.cos(angle), math.sin(angle))
            result[k] = result[k] + signal[j] * w
    
    # 逆变换需要除以n
    if invert:
        for i in range(n):
            result[i] = result[i] / n
    
    return result

# 快速傅里叶变换（FFT）- 用于对比
def fft(signal, invert=False):
    """
    计算快速傅里叶变换（迭代实现）
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    
    参数：
    signal -- 复数列表，表示输入信号
    invert -- 是否计算逆变换
    
    返回：
    变换后的复数列表
    """
    # 创建信号的副本，避免修改原信号
    n = len(signal)
    result = signal.copy()
    
    # 位反转置换
    j = 0
    for i in range(1, n):
        bit = n >> 1
        while j >= bit:
            j -= bit
            bit >>= 1
        j += bit
        if i < j:
            result[i], result[j] = result[j], result[i]
    
    # 迭代实现的FFT
    len_factor = 2
    while len_factor <= n:
        ang = 2 * math.pi / len_factor * (-1 if invert else 1)
        wlen = Complex(math.cos(ang), math.sin(ang))
        for i in range(0, n, len_factor):
            w = Complex(1, 0)
            half_len = len_factor // 2
            for j in range(half_len):
                u = result[i + j]
                v = result[i + j + half_len] * w
                result[i + j] = u + v
                result[i + j + half_len] = u - v
                w = w * wlen
        len_factor <<= 1
    
    # 逆变换需要除以n
    if invert:
        for i in range(n):
            result[i] = result[i] / n
    
    return result

# 多项式乘法 - 使用DFT
def multiply_polynomials_dft(poly1, poly2):
    """
    使用DFT进行多项式乘法
    时间复杂度：O(n²)
    空间复杂度：O(n)
    
    参数：
    poly1 -- 第一个多项式的系数列表
    poly2 -- 第二个多项式的系数列表
    
    返回：
    乘积多项式的系数列表
    """
    # 确定结果长度，向上取到2的幂次
    n = 1
    result_length = len(poly1) + len(poly2) - 1
    while n < result_length:
        n <<= 1
    
    # 转换为复数
    fa = [Complex(poly1[i] if i < len(poly1) else 0, 0) for i in range(n)]
    fb = [Complex(poly2[i] if i < len(poly2) else 0, 0) for i in range(n)]
    
    # 进行DFT
    fa_dft = dft(fa)
    fb_dft = dft(fb)
    
    # 点乘
    fc_dft = [fa_dft[i] * fb_dft[i] for i in range(n)]
    
    # 逆DFT得到结果
    fc = dft(fc_dft, True)
    
    # 转换回整数并截断到实际长度
    result = [round(fc[i].real) for i in range(result_length)]
    
    return result

# 多项式乘法 - 使用FFT
def multiply_polynomials_fft(poly1, poly2):
    """
    使用FFT进行多项式乘法
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    
    参数：
    poly1 -- 第一个多项式的系数列表
    poly2 -- 第二个多项式的系数列表
    
    返回：
    乘积多项式的系数列表
    """
    # 确定结果长度，向上取到2的幂次
    n = 1
    result_length = len(poly1) + len(poly2) - 1
    while n < result_length:
        n <<= 1
    
    # 转换为复数
    fa = [Complex(poly1[i] if i < len(poly1) else 0, 0) for i in range(n)]
    fb = [Complex(poly2[i] if i < len(poly2) else 0, 0) for i in range(n)]
    
    # 进行FFT
    fa_fft = fft(fa)
    fb_fft = fft(fb)
    
    # 点乘
    fc_fft = [fa_fft[i] * fb_fft[i] for i in range(n)]
    
    # 逆FFT得到结果
    fc = fft(fc_fft, True)
    
    # 转换回整数并截断到实际长度
    result = [round(fc[i].real) for i in range(result_length)]
    
    return result

# 力扣第43题：字符串相乘 - 大数乘法
def multiply_strings(num1, num2):
    """
    字符串相乘 - 大数乘法
    时间复杂度：O(m*n)
    空间复杂度：O(m+n)
    
    参数：
    num1 -- 第一个数字字符串
    num2 -- 第二个数字字符串
    
    返回：
    乘积的字符串表示
    """
    if num1 == "0" or num2 == "0":
        return "0"
    
    m, n = len(num1), len(num2)
    # 结果的最大长度是m+n
    result = [0] * (m + n)
    
    # 从后向前逐位相乘
    for i in range(m-1, -1, -1):
        for j in range(n-1, -1, -1):
            # 计算乘积
            product = int(num1[i]) * int(num2[j])
            # 加上当前位原有的值
            sum_val = product + result[i + j + 1]
            # 更新高位和当前位
            result[i + j] += sum_val // 10
            result[i + j + 1] = sum_val % 10
    
    # 转换为字符串，跳过前导零
    result_str = "".join(map(str, result))
    # 去除前导零
    return result_str.lstrip("0")

# 力扣第363题：矩形区域不超过 K 的最大数值和
def max_sum_submatrix(matrix, k):
    """
    计算矩形区域不超过 K 的最大数值和
    时间复杂度：O(m²nlogn)
    空间复杂度：O(n)
    
    参数：
    matrix -- 二维整数矩阵
    k -- 目标值
    
    返回：
    不超过k的最大矩形和
    """
    if not matrix or not matrix[0]:
        return 0
    
    m, n = len(matrix), len(matrix[0])
    max_sum = -float('inf')
    
    # 枚举左右边界
    for left in range(n):
        row_sum = [0] * m  # 记录每一行在当前左右边界内的元素和
        for right in range(left, n):
            # 更新每行的和
            for i in range(m):
                row_sum[i] += matrix[i][right]
            
            # 计算前缀和
            prefix_sum = [0]
            curr_sum = 0
            for num in row_sum:
                curr_sum += num
                # 查找前缀和中是否存在curr_sum - k
                idx = bisect.bisect_left(prefix_sum, curr_sum - k)
                if idx < len(prefix_sum):
                    max_sum = max(max_sum, curr_sum - prefix_sum[idx])
                # 将当前前缀和加入列表
                bisect.insort(prefix_sum, curr_sum)
    
    return max_sum

# 打印复数列表
def print_complex_list(lst, name):
    print(f"{name}:")
    for c in lst:
        print(c)

# 打印整数列表
def print_list(lst, name):
    print(f"{name}:")
    print(" ".join(map(str, lst)))

# 测量执行时间
def measure_time(func, *args, **kwargs):
    start = time.time()
    result = func(*args, **kwargs)
    end = time.time()
    return result, (end - start) * 1000  # 转换为毫秒

# 主函数 - 测试代码
def main():
    # 导入bisect模块（用于力扣第363题）
    import bisect
    
    # 测试DFT
    print("=== DFT测试 ===")
    a = [Complex(1, 0), Complex(2, 0), Complex(3, 0), Complex(4, 0)]
    
    a_dft, dft_time = measure_time(dft, a)
    print_complex_list(a_dft, "DFT结果")
    print(f"DFT执行时间: {dft_time:.6f} ms")
    
    a_idft, idft_time = measure_time(dft, a_dft, True)
    print_complex_list(a_idft, "逆DFT结果")
    print(f"逆DFT执行时间: {idft_time:.6f} ms")
    
    # 测试多项式乘法
    print("\n=== 多项式乘法测试 ===")
    poly1 = [1, 2, 3]
    poly2 = [4, 5, 6]
    
    result_dft, dft_mul_time = measure_time(multiply_polynomials_dft, poly1, poly2)
    print_list(result_dft, "DFT多项式乘法结果")
    print(f"DFT多项式乘法执行时间: {dft_mul_time:.6f} ms")
    
    result_fft, fft_mul_time = measure_time(multiply_polynomials_fft, poly1, poly2)
    print_list(result_fft, "FFT多项式乘法结果")
    print(f"FFT多项式乘法执行时间: {fft_mul_time:.6f} ms")
    
    # 测试大数乘法（力扣第43题）
    print("\n=== 力扣第43题测试 ===")
    test_cases = [
        ("123", "456", "56088"),
        ("2", "3", "6"),
        ("0", "12345", "0")
    ]
    
    for num1, num2, expected in test_cases:
        result = multiply_strings(num1, num2)
        print(f"{num1} * {num2} = {result} (期望: {expected}, {'✓' if result == expected else '✗'})")
    
    # 性能对比（针对较大数据）
    print("\n=== 性能对比测试 ===")
    size = 1024
    large_signal = [Complex(math.sin(i), math.cos(i)) for i in range(size)]
    
    # DFT性能
    _, dft_time_large = measure_time(dft, large_signal)
    print(f"DFT时间 (n={size}): {dft_time_large:.6f} ms")
    
    # FFT性能
    _, fft_time_large = measure_time(fft, large_signal)
    print(f"FFT时间 (n={size}): {fft_time_large:.6f} ms")
    
    if fft_time_large > 0:
        speedup = dft_time_large / fft_time_large
        print(f"FFT比DFT快约 {speedup:.2f} 倍")
    else:
        print("FFT速度太快，无法计算准确的加速比")
    
    # 测试numpy的FFT（用于参考）
    try:
        import numpy as np
        print("\n=== NumPy FFT参考 ===")
        # 转换为numpy复数数组
        np_signal = np.array([complex(c.real, c.imag) for c in large_signal])
        start = time.time()
        np_fft_result = np.fft.fft(np_signal)
        np_fft_time = (time.time() - start) * 1000
        print(f"NumPy FFT时间 (n={size}): {np_fft_time:.6f} ms")
    except ImportError:
        print("\n=== NumPy FFT参考 ===")
        print("未安装NumPy库，跳过NumPy FFT测试")
    
    """
    离散傅里叶变换（DFT）算法总结：
    
    1. 算法原理：
       - DFT将时域信号转换为频域表示
       - 基本公式：X[k] = Σ(x[n] * e^(-2πikn/N)) ，n = 0,1,...,N-1
       - 逆变换：x[n] = (1/N) * Σ(X[k] * e^(2πikn/N)) ，k = 0,1,...,N-1
    
    2. 时间复杂度：
       - 直接DFT：O(n²)，需要计算n个频率点，每个点需要n次复数乘法和加法
       - FFT：O(n log n)，利用旋转因子的周期性和对称性，避免重复计算
    
    3. 空间复杂度：
       - 所有实现均为O(n)
    
    4. 应用场景：
       - 信号处理：音频分析、降噪、滤波
       - 图像处理：卷积、边缘检测、频域滤波
       - 多项式乘法：如本题所示
       - 密码学：某些加密算法的基础
       - 量子计算：量子傅里叶变换
    
    5. 相关题目：
       - 力扣第43题：字符串相乘 - 大数乘法，可以使用FFT优化
       - 力扣第363题：矩形区域不超过 K 的最大数值和 - 二维前缀和的应用
       - Codeforces 954I：Yet Another String Matching Problem - 字符串匹配问题
       - Codeforces 914G：Sum the Fibonacci - 斐波那契数列相关的卷积问题
    
    6. 优化方向：
       - 使用迭代版本的FFT而非递归版本，避免栈溢出
       - 针对特定硬件进行向量化优化
       - 对于非常大的数据，可以考虑分块处理
    """

if __name__ == "__main__":
    main()
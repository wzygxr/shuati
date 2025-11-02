# FFT/NTT 算法的Python实现
# 时间复杂度: O(n log n)
# 空间复杂度: O(n)

import math
from typing import List

# FFT相关常数
PI = math.acos(-1.0)

# NTT相关常数
MOD = 998244353
ROOT = 3

# FFT (快速傅里叶变换)
def fft(a: List[complex], invert: bool) -> None:
    """
    将多项式转换为点值表示（或逆变换）
    
    Args:
        a: 复数列表，表示多项式系数
        invert: 是否执行逆变换
    """
    n = len(a)
    
    # 位反转置换
    j = 0
    for i in range(1, n):
        bit = n >> 1
        while j >= bit:
            j -= bit
            bit >>= 1
        j += bit
        
        if i < j:
            a[i], a[j] = a[j], a[i]
    
    # 蝴蝶操作
    while n > 1:
        half = n >> 1
        ang = 2 * PI / n * (-1 if invert else 1)
        wlen = complex(math.cos(ang), math.sin(ang))
        
        # 内层循环实现不同长度的FFT
        for i in range(0, n, half << 1):
            w = complex(1, 0)
            for j in range(half):
                u = a[i + j]
                v = a[i + j + half] * w
                a[i + j] = u + v
                a[i + j + half] = u - v
                w *= wlen
        n = half
    
    # 逆变换时需要除以n
    if invert:
        n = len(a)
        for i in range(n):
            a[i] /= n

# 多项式乘法 (FFT实现)
def multiply_fft(a: List[int], b: List[int]) -> List[int]:
    """
    使用FFT实现多项式乘法
    
    Args:
        a: 第一个多项式的系数列表
        b: 第二个多项式的系数列表
    
    Returns:
        乘积多项式的系数列表
    """
    # 转换为复数列表
    fa = [complex(x, 0) for x in a]
    fb = [complex(x, 0) for x in b]
    
    # 计算需要的最小长度（2的幂次）
    n = 1
    while n < len(a) + len(b) - 1:
        n <<= 1
    
    # 填充到足够长度
    fa += [complex(0, 0)] * (n - len(fa))
    fb += [complex(0, 0)] * (n - len(fb))
    
    # 执行FFT
    fft(fa, False)
    fft(fb, False)
    
    # 点值相乘
    for i in range(n):
        fa[i] *= fb[i]
    
    # 执行逆FFT
    fft(fa, True)
    
    # 转换为整数结果
    result = [round(x.real) for x in fa]
    
    # 移除末尾的零
    while len(result) > 1 and result[-1] == 0:
        result.pop()
    
    return result

# 快速幂取模
def pow_mod(a: int, b: int, mod: int) -> int:
    """
    计算 (a^b) mod mod
    
    Args:
        a: 底数
        b: 指数
        mod: 模数
    
    Returns:
        计算结果
    """
    res = 1
    a %= mod
    while b > 0:
        if b % 2 == 1:
            res = res * a % mod
        a = a * a % mod
        b //= 2
    return res

# 数论逆元
def inv_mod(a: int, mod: int) -> int:
    """
    计算a在模mod下的乘法逆元
    
    Args:
        a: 要计算逆元的数
        mod: 模数
    
    Returns:
        a的模mod逆元
    """
    return pow_mod(a, mod - 2, mod)

# NTT (数论变换)
def ntt(a: List[int], invert: bool) -> None:
    """
    在模运算下进行数论变换
    
    Args:
        a: 系数列表
        invert: 是否执行逆变换
    """
    n = len(a)
    
    # 位反转置换
    j = 0
    for i in range(1, n):
        bit = n >> 1
        while j >= bit:
            j -= bit
            bit >>= 1
        j += bit
        
        if i < j:
            a[i], a[j] = a[j], a[i]
    
    # 蝴蝶操作
    while n > 1:
        half = n >> 1
        wlen = pow_mod(ROOT, (MOD - 1) // n, MOD)
        if invert:
            wlen = inv_mod(wlen, MOD)
        
        for i in range(0, n, half << 1):
            w = 1
            for j in range(half):
                u = a[i + j]
                v = a[i + j + half] * w % MOD
                a[i + j] = (u + v) % MOD
                a[i + j + half] = (u - v + MOD) % MOD
                w = w * wlen % MOD
        n = half
    
    # 逆变换时需要处理
    if invert:
        n = len(a)
        inv_n = inv_mod(n, MOD)
        for i in range(n):
            a[i] = a[i] * inv_n % MOD

# 多项式乘法 (NTT实现)
def multiply_ntt(a: List[int], b: List[int]) -> List[int]:
    """
    使用NTT实现多项式乘法
    
    Args:
        a: 第一个多项式的系数列表
        b: 第二个多项式的系数列表
    
    Returns:
        乘积多项式的系数列表（模MOD）
    """
    # 复制并转换为整数列表
    fa = list(a)
    fb = list(b)
    
    # 计算需要的最小长度（2的幂次）
    n = 1
    while n < len(a) + len(b) - 1:
        n <<= 1
    
    # 填充到足够长度
    fa += [0] * (n - len(fa))
    fb += [0] * (n - len(fb))
    
    # 执行NTT
    ntt(fa, False)
    ntt(fb, False)
    
    # 点值相乘
    for i in range(n):
        fa[i] = fa[i] * fb[i] % MOD
    
    # 执行逆NTT
    ntt(fa, True)
    
    # 移除末尾的零
    while len(fa) > 1 and fa[-1] == 0:
        fa.pop()
    
    return fa

# 力扣第43题：字符串相乘的FFT解法
def multiply_strings(num1: str, num2: str) -> str:
    """
    使用FFT解决大数乘法问题
    
    Args:
        num1: 第一个数字字符串
        num2: 第二个数字字符串
    
    Returns:
        乘积的字符串表示
    """
    if num1 == "0" or num2 == "0":
        return "0"
    
    # 转换为系数列表
    a = [int(c) for c in reversed(num1)]
    b = [int(c) for c in reversed(num2)]
    
    # 使用FFT计算乘积
    res = multiply_fft(a, b)
    
    # 处理进位
    carry = 0
    digits = []
    for x in res:
        x += carry
        carry = x // 10
        digits.append(x % 10)
    
    # 处理剩余进位
    while carry > 0:
        digits.append(carry % 10)
        carry //= 10
    
    # 转换为字符串
    return ''.join(map(str, reversed(digits)))

# 测试代码
if __name__ == "__main__":
    # FFT测试
    a = [1, 2, 3]
    b = [4, 5, 6]
    res_fft = multiply_fft(a, b)
    print("FFT乘法结果:", res_fft)
    
    # NTT测试
    c = [1, 2, 3]
    d = [4, 5, 6]
    res_ntt = multiply_ntt(c, d)
    print("NTT乘法结果:", res_ntt)
    
    # 大数乘法测试
    num1 = "123"
    num2 = "456"
    res_str = multiply_strings(num1, num2)
    print(f"{num1} * {num2} = {res_str}")
    
    # 算法分析与优化说明
    '''
    Python实现的注意事项：
    1. 使用复数类型直接支持FFT的复数运算
    2. 由于Python的递归深度限制，使用迭代版本的FFT
    3. 针对大数乘法，可以进一步优化进位处理
    4. 对于非常大的多项式，可以考虑使用NumPy进行性能优化
    
    常见优化方向：
    1. 常数优化：减少不必要的复制和操作
    2. 内存优化：原地算法实现
    3. 精度控制：在FFT中处理浮点误差
    4. 并行计算：利用多线程加速计算过程
    
    算法应用场景：
    - 多项式乘法：将多项式系数转换为点值表示，O(n log n)时间计算
    - 大数乘法：如力扣43题，将大数视为多项式系数
    - 卷积计算：信号处理、图像处理中的卷积操作
    - 字符串匹配：通过FFT加速字符串匹配算法
    '''
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Toom-Cook乘法算法实现
是Karatsuba算法的一般化，通过更高阶的分治策略进一步降低时间复杂度
传统乘法: O(n²)，Karatsuba: O(n^1.585)，Toom-Cook (k=3): O(n^1.465)
适用于高精度大整数乘法计算
"""

import time
import random


def naive_multiply(x, y):
    """
    使用传统方法进行大整数乘法
    时间复杂度：O(n²)
    作为Toom-Cook算法的基础情况
    
    Args:
        x: 第一个整数的数字列表表示（低位在前）
        y: 第二个整数的数字列表表示（低位在前）
    
    Returns:
        list: 乘积的数字列表表示（低位在前）
    """
    n = len(x)
    m = len(y)
    result = [0] * (n + m)

    # 逐位相乘并累加
    for i in range(n):
        for j in range(m):
            result[i + j] += x[i] * y[j]
            # 处理进位
            result[i + j + 1] += result[i + j] // 10
            result[i + j] %= 10

    # 移除前导零（实际上是列表末尾的零）
    last_non_zero = n + m - 1
    while last_non_zero > 0 and result[last_non_zero] == 0:
        last_non_zero -= 1

    return result[:last_non_zero + 1]


def toom_cook_multiply(x, y):
    """
    使用Toom-Cook乘法算法进行大整数乘法
    这里实现了Toom-3算法，是Toom-Cook的三阶版本
    时间复杂度：O(n^log₃5) ≈ O(n^1.465)
    
    Args:
        x: 第一个整数的字符串表示
        y: 第二个整数的字符串表示
    
    Returns:
        str: 乘积的字符串表示
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

    # 调用递归Toom-Cook算法
    product = toom_cook_multiply_recursive(x_digits, y_digits)

    # 移除前导零并转换为字符串
    return digits_to_string(product)


def toom_cook_multiply_recursive(x, y):
    """
    Toom-Cook (Toom-3) 递归乘法实现
    
    Args:
        x: 第一个整数的数字列表表示（低位在前）
        y: 第二个整数的数字列表表示（低位在前）
    
    Returns:
        list: 乘积的数字列表表示（低位在前）
    """
    n = max(len(x), len(y))
    
    # 基础情况：当列表长度较小时，使用传统乘法
    if n <= 128:  # 阈值可以根据实际情况调整
        return naive_multiply(x, y)

    # 计算分割点，将数字分成三个部分
    m = (n + 2) // 3  # 向上取整到3

    # 分割x为三个部分: x = x2*10^(2*m) + x1*10^m + x0
    x0 = x[:m] if len(x) > 0 else []
    x1 = x[m:2*m] if len(x) > m else []
    x2 = x[2*m:] if len(x) > 2*m else []

    # 分割y为三个部分: y = y2*10^(2*m) + y1*10^m + y0
    y0 = y[:m] if len(y) > 0 else []
    y1 = y[m:2*m] if len(y) > m else []
    y2 = y[2*m:] if len(y) > 2*m else []

    # 计算f(k)和g(k)在k=-1,0,1,2,∞处的值
    # f(0) = x0, f(1) = x0+x1+x2, f(-1) = x0-x1+x2, f(2) = x0+2x1+4x2, f(∞) = x2
    # g(0) = y0, g(1) = y0+y1+y2, g(-1) = y0-y1+y2, g(2) = y0+2y1+4y2, g(∞) = y2
    f0 = x0
    f1 = add_lists(add_lists(x0, x1), x2)
    f_neg1 = add_lists(subtract_lists(x0, x1), x2)
    f2 = add_lists(add_lists(x0, multiply_by_power_of_two(x1, 1)), multiply_by_power_of_two(x2, 2))
    f_infty = x2

    g0 = y0
    g1 = add_lists(add_lists(y0, y1), y2)
    g_neg1 = add_lists(subtract_lists(y0, y1), y2)
    g2 = add_lists(add_lists(y0, multiply_by_power_of_two(y1, 1)), multiply_by_power_of_two(y2, 2))
    g_infty = y2

    # 计算乘积h(k) = f(k)*g(k) 在各点的值
    h0 = toom_cook_multiply_recursive(f0, g0)  # h(0) = x0*y0
    h1 = toom_cook_multiply_recursive(f1, g1)  # h(1) = (x0+x1+x2)*(y0+y1+y2)
    h_neg1 = toom_cook_multiply_recursive(f_neg1, g_neg1)  # h(-1) = (x0-x1+x2)*(y0-y1+y2)
    h2 = toom_cook_multiply_recursive(f2, g2)  # h(2) = (x0+2x1+4x2)*(y0+2y1+4y2)
    h_infty = toom_cook_multiply_recursive(f_infty, g_infty)  # h(∞) = x2*y2

    # 使用拉格朗日插值法求解多项式系数
    # h(z) = z^4*h_4 + z^3*h_3 + z^2*h_2 + z*h_1 + h_0
    # 其中h_4 = hInfty

    # 计算中间值
    a = h1  # h(1)
    b = h_neg1  # h(-1)
    c = h2  # h(2)
    d = h0  # h(0)

    # 通过插值公式计算h3, h2, h1
    # h3 = (c - 8a + 6b - d) / 6
    term1 = subtract_lists(c, multiply_by_power_of_two(a, 3))  # c - 8a
    term2 = add_lists(multiply_by_power_of_two(b, 2), multiply_by_power_of_two(b, 1))  # 6b
    numerator = subtract_lists(add_lists(term1, term2), d)
    h3 = divide_by_six(numerator)

    # h2 = (a + b - 2d - 6h3 - 2h4) / 2
    h4 = h_infty
    term3 = add_lists(a, b)
    term4 = add_lists(multiply_by_power_of_two(d, 1), 
                     add_lists(multiply_by_power_of_two(h3, 2), multiply_by_power_of_two(h3, 1)))  # 2d + 6h3
    term5 = multiply_by_power_of_two(h4, 1)  # 2h4
    numerator = subtract_lists(subtract_lists(term3, term4), term5)
    h2 = divide_by_two(numerator)

    # h1 = (a - b) / 2 - 2h3 - 3h4
    term1 = divide_by_two(subtract_lists(a, b))
    term2 = add_lists(multiply_by_power_of_two(h3, 1), 
                     add_lists(multiply_by_power_of_two(h4, 1), h4))  # 2h3 + 3h4
    h1_coeff = subtract_lists(term1, term2)

    # 现在我们有了所有系数：h4, h3, h2, h1, h0
    # 组合结果: h4*10^(4*m) + h3*10^(3*m) + h2*10^(2*m) + h1*10^m + h0
    result = [0] * (5 * m)  # 可能需要调整大小

    # 添加各部分到结果中
    add_to_result(result, h0, 0)
    add_to_result(result, h1_coeff, m)
    add_to_result(result, h2, 2 * m)
    add_to_result(result, h3, 3 * m)
    add_to_result(result, h4, 4 * m)

    # 移除前导零
    last_non_zero = len(result) - 1
    while last_non_zero > 0 and result[last_non_zero] == 0:
        last_non_zero -= 1

    return result[:last_non_zero + 1]


def add_to_result(result, addend, offset):
    """
    将数字列表添加到结果列表的指定位置
    
    Args:
        result: 结果列表
        addend: 要添加的数字列表
        offset: 起始位置
    """
    new_length = max(len(result), len(addend) + offset)
    if new_length > len(result):
        result.extend([0] * (new_length - len(result)))

    for i in range(len(addend)):
        if i + offset < len(result):
            result[i + offset] += addend[i]
            # 处理进位
            propagate_carry(result, i + offset)


def propagate_carry(arr, pos):
    """
    处理进位传播
    
    Args:
        arr: 数字列表
        pos: 起始处理位置
    """
    while pos < len(arr) - 1 and arr[pos] >= 10:
        arr[pos + 1] += arr[pos] // 10
        arr[pos] %= 10
        pos += 1
    # 处理最高位的进位（如果需要）
    if pos == len(arr) - 1 and arr[pos] >= 10:
        arr.append(arr[pos] // 10)
        arr[pos] %= 10


def add_lists(a, b):
    """
    对两个数字列表进行加法操作
    
    Args:
        a: 第一个数字列表（低位在前）
        b: 第二个数字列表（低位在前）
    
    Returns:
        list: 和的数字列表（低位在前）
    """
    max_length = max(len(a), len(b))
    result = [0] * (max_length + 1)  # 预留进位空间

    for i in range(max_length):
        digit_a = a[i] if i < len(a) else 0
        digit_b = b[i] if i < len(b) else 0
        result[i] = digit_a + digit_b

        # 处理进位
        result[i + 1] += result[i] // 10
        result[i] %= 10

    # 移除前导零
    last_non_zero = max_length
    while last_non_zero > 0 and result[last_non_zero] == 0:
        last_non_zero -= 1

    return result[:last_non_zero + 1]


def subtract_lists(a, b):
    """
    对两个数字列表进行减法操作
    假设 a >= b
    
    Args:
        a: 被减数的数字列表（低位在前）
        b: 减数的数字列表（低位在前）
    
    Returns:
        list: 差的数字列表（低位在前）
    """
    result = a.copy()

    for i in range(len(b)):
        result[i] -= b[i]

    # 处理借位
    for i in range(len(result) - 1):
        while result[i] < 0:
            result[i] += 10
            result[i + 1] -= 1

    # 移除前导零
    last_non_zero = len(result) - 1
    while last_non_zero > 0 and result[last_non_zero] == 0:
        last_non_zero -= 1

    return result[:last_non_zero + 1]


def multiply_by_power_of_two(arr, power):
    """
    将数字列表乘以2的幂（乘以2^power）
    
    Args:
        arr: 数字列表（低位在前）
        power: 2的幂次
    
    Returns:
        list: 结果列表（低位在前）
    """
    result = arr.copy()
    
    for p in range(power):
        carry = 0
        for i in range(len(result)):
            product = result[i] * 2 + carry
            result[i] = product % 10
            carry = product // 10
        if carry > 0:
            result.append(carry)
    
    return result


def divide_by_two(arr):
    """
    将数字列表除以2
    
    Args:
        arr: 数字列表（低位在前）
    
    Returns:
        list: 结果列表（低位在前）
    """
    result = [0] * len(arr)
    remainder = 0
    
    # 从高位开始除（列表的末尾）
    for i in range(len(arr) - 1, -1, -1):
        current = arr[i] + remainder * 10
        result[i] = current // 2
        remainder = current % 2
    
    # 移除前导零
    last_non_zero = len(result) - 1
    while last_non_zero > 0 and result[last_non_zero] == 0:
        last_non_zero -= 1
    
    return result[:last_non_zero + 1]


def divide_by_six(arr):
    """
    将数字列表除以6
    
    Args:
        arr: 数字列表（低位在前）
    
    Returns:
        list: 结果列表（低位在前）
    """
    result = [0] * len(arr)
    remainder = 0
    
    # 从高位开始除（列表的末尾）
    for i in range(len(arr) - 1, -1, -1):
        current = arr[i] + remainder * 10
        result[i] = current // 6
        remainder = current % 6
    
    # 移除前导零
    last_non_zero = len(result) - 1
    while last_non_zero > 0 and result[last_non_zero] == 0:
        last_non_zero -= 1
    
    return result[:last_non_zero + 1]


def string_to_digits(s):
    """
    将字符串转换为数字列表（低位在前）
    
    Args:
        s: 数字字符串
    
    Returns:
        list: 数字列表（低位在前）
    """
    return [int(s[len(s) - 1 - i]) for i in range(len(s))]


def digits_to_string(digits):
    """
    将数字列表转换为字符串（低位在前转换为正常表示）
    
    Args:
        digits: 数字列表（低位在前）
    
    Returns:
        str: 数字字符串
    """
    return ''.join(str(digits[i]) for i in range(len(digits) - 1, -1, -1))

# 导入Karatsuba算法用于性能比较
def karatsuba_multiply(x, y):
    """
    简化版Karatsuba乘法（仅用于性能测试对比）
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

    # 调整列表长度为相等且为2的幂
    max_length = max(len(x_digits), len(y_digits))
    n = 1
    while n < max_length:
        n <<= 1

    x_digits.extend([0] * (n - len(x_digits)))
    y_digits.extend([0] * (n - len(y_digits)))

    # 递归实现Karatsuba
    def karatsuba_recursive(a, b):
        n = len(a)
        if n <= 64:
            return naive_multiply(a, b)

        m = n // 2
        a_high, a_low = a[m:], a[:m]
        b_high, b_low = b[m:], b[:m]

        z1 = karatsuba_recursive(a_high, b_high)
        z3 = karatsuba_recursive(a_low, b_low)
        
        a_sum = add_lists(a_high, a_low)
        b_sum = add_lists(b_high, b_low)
        z2 = karatsuba_recursive(a_sum, b_sum)
        
        z2 = subtract_lists(z2, add_lists(z1, z3))

        result = [0] * (2 * n)
        add_to_result(result, z3, 0)
        add_to_result(result, z2, m)
        add_to_result(result, z1, 2 * m)

        last_non_zero = len(result) - 1
        while last_non_zero > 0 and result[last_non_zero] == 0:
            last_non_zero -= 1
        return result[:last_non_zero + 1]

    product = karatsuba_recursive(x_digits, y_digits)
    return digits_to_string(product)


def benchmark(size):
    """
    性能测试方法，比较Toom-Cook算法与其他算法
    
    Args:
        size: 测试数字的位数
    """
    # 生成测试用的大整数
    num1 = generate_random_number(size)
    num2 = generate_random_number(size)

    # 测试Toom-Cook算法
    start_time = time.time()
    result_toom_cook = toom_cook_multiply(num1, num2)
    toom_cook_time = (time.time() - start_time) * 1000  # 转换为毫秒

    # 测试Karatsuba算法
    start_time = time.time()
    result_karatsuba = karatsuba_multiply(num1, num2)
    karatsuba_time = (time.time() - start_time) * 1000  # 转换为毫秒

    # 测试传统算法（对于较小的数字）
    result_naive = ""
    naive_time = 0
    if size <= 500:  # 对于大数字，传统算法可能太慢
        digits1 = string_to_digits(num1)
        digits2 = string_to_digits(num2)
        start_time = time.time()
        naive_result = naive_multiply(digits1, digits2)
        result_naive = digits_to_string(naive_result)
        naive_time = (time.time() - start_time) * 1000  # 转换为毫秒

    print(f"数字位数: {size}")
    print(f"Toom-Cook算法耗时: {toom_cook_time:.3f} ms")
    print(f"Karatsuba算法耗时: {karatsuba_time:.3f} ms")
    print(f"算法加速比 (Karatsuba/Toom-Cook): {karatsuba_time / toom_cook_time:.2f}x")
    
    if size <= 500:
        print(f"传统算法耗时: {naive_time:.3f} ms")
        print(f"算法加速比 (传统/Toom-Cook): {naive_time / toom_cook_time:.2f}x")
        print(f"结果一致 (Toom-Cook vs 传统): {result_toom_cook == result_naive}")
    print(f"结果一致 (Toom-Cook vs Karatsuba): {result_toom_cook == result_karatsuba}")
    print(f"乘积位数: {len(result_toom_cook)}")


def generate_random_number(length):
    """
    生成指定长度的随机数字字符串
    
    Args:
        length: 字符串长度
    
    Returns:
        str: 随机数字字符串
    """
    # 第一位不能是0
    first_digit = str(random.randint(1, 9))
    # 生成剩余位
    remaining_digits = ''.join(str(random.randint(0, 9)) for _ in range(length - 1))
    return first_digit + remaining_digits


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
        # 使用Toom-Cook算法
        result = toom_cook_multiply(x, y)
        
        # 对于小数字，使用Python内置的大整数类验证
        try:
            num1 = int(x)
            num2 = int(y)
            expected = str(num1 * num2)
            print(f"{x} * {y} = {result} (正确: {result == expected})")
        except ValueError:
            # 处理非常大的数字
            print(f"{x} * {y} = {result}")
            print(f"乘积位数: {len(result)}")


def interactive_mode():
    """
    交互式测试模式
    """
    print("请输入两个大整数进行乘法计算（输入exit退出）:")
    while True:
        try:
            print("第一个数: ")
            num1 = input().strip()
            if num1.lower() == "exit":
                break
            
            print("第二个数: ")
            num2 = input().strip()
            if num2.lower() == "exit":
                break
            
            # 验证输入是否为有效数字
            if not (num1.isdigit() and num2.isdigit()):
                print("错误: 请输入有效的正整数")
                continue
            
            start_time = time.time()
            result = toom_cook_multiply(num1, num2)
            end_time = time.time()
            
            print(f"结果: {result}")
            print(f"计算耗时: {(end_time - start_time) * 1000:.3f} ms")
        except Exception as e:
            print(f"发生错误: {e}")


if __name__ == "__main__":
    print("验证算法正确性:")
    verify_correctness()
    
    print("\n性能测试:")
    benchmark(100)
    benchmark(1000)
    benchmark(5000)
    
    try:
        interactive_mode()
    except (KeyboardInterrupt, EOFError):
        print("\n程序已退出")
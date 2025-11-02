#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Karatsuba乘法算法实现
传统的乘法算法时间复杂度为O(n²)，而Karatsuba算法将其优化至约O(n^1.585)
适用于高精度大整数乘法计算
"""

import time
import random


def naive_multiply(x, y):
    """
    使用传统方法进行大整数乘法
    时间复杂度：O(n²)
    作为Karatsuba算法的基础情况
    
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

    # 移除前导零（实际上是数组末尾的零）
    last_non_zero = n + m - 1
    while last_non_zero > 0 and result[last_non_zero] == 0:
        last_non_zero -= 1

    return result[:last_non_zero + 1]


def karatsuba_multiply(x, y):
    """
    使用Karatsuba算法进行大整数乘法，支持负数
    时间复杂度：O(n^log₂3) ≈ O(n^1.585)
    
    Args:
        x: 第一个整数的字符串表示，可以是负数
        y: 第二个整数的字符串表示，可以是负数
    
    Returns:
        str: 乘积的字符串表示
    """
    # 处理负数情况
    is_negative = False
    if x.startswith('-'):
        is_negative = not is_negative
        x = x[1:]
    if y.startswith('-'):
        is_negative = not is_negative
        y = y[1:]

    # 处理特殊情况
    if x == "0" or y == "0":
        return "0"
    if x == "1":
        return '-' + y if is_negative else y
    if y == "1":
        return '-' + x if is_negative else x

    # 将字符串转换为数字列表（低位在前）
    x_digits = string_to_digits(x)
    y_digits = string_to_digits(y)

    # 调整列表长度为相等且为2的幂（以优化分治过程）
    max_length = max(len(x_digits), len(y_digits))
    n = 1
    while n < max_length:
        n <<= 1  # 向上取最近的2的幂

    x_digits = pad_list(x_digits, n)
    y_digits = pad_list(y_digits, n)

    # 调用递归Karatsuba算法
    product = karatsuba_multiply_recursive(x_digits, y_digits)

    # 移除前导零并转换为字符串
    result = digits_to_string(product)
    
    # 添加负号（如果需要）
    return '-' + result if is_negative else result


def karatsuba_multiply_recursive(x, y):
    """
    Karatsuba递归乘法实现
    
    Args:
        x: 第一个整数的数字列表表示（低位在前）
        y: 第二个整数的数字列表表示（低位在前）
    
    Returns:
        list: 乘积的数字列表表示（低位在前）
    """
    n = len(x)
    
    # 基础情况：当列表长度较小时，使用传统乘法
    if n <= 64:  # 阈值可以根据实际情况调整
        return naive_multiply(x, y)

    # 计算中点
    m = n // 2

    # 分割列表
    a = x[m:]
    b = x[:m]
    c = y[m:]
    d = y[:m]

    # 计算三个子乘积
    # z1 = a * c
    z1 = karatsuba_multiply_recursive(a, c)
    
    # z3 = b * d
    z3 = karatsuba_multiply_recursive(b, d)
    
    # 计算 (a + b) * (c + d)
    sum_a_b = add_lists(a, b)
    sum_c_d = add_lists(c, d)
    z2 = karatsuba_multiply_recursive(sum_a_b, sum_c_d)
    
    # z2 = (a + b) * (c + d) - z1 - z3
    z2 = subtract_lists(z2, add_lists(z1, z3))

    # 组合结果: z1 * 10^(2*m) + z2 * 10^m + z3
    result = [0] * (2 * n)
    
    # 添加z3到结果
    add_to_result(result, z3, 0)
    
    # 添加z2 * 10^m到结果
    add_to_result(result, z2, m)
    
    # 添加z1 * 10^(2*m)到结果
    add_to_result(result, z1, 2 * m)

    # 移除前导零（实际上是列表末尾的零）
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
    # 在这个方法中，我们假设arr足够大，不需要扩展


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


def pad_list(arr, length):
    """
    填充列表到指定长度
    
    Args:
        arr: 原始列表
        length: 目标长度
    
    Returns:
        list: 填充后的列表
    """
    if len(arr) >= length:
        return arr
    return arr + [0] * (length - len(arr))


def benchmark(size):
    """
    性能测试方法，比较Karatsuba算法与Python内置乘法
    
    Args:
        size: 测试数字的位数
    """
    # 生成测试用的大整数
    num1 = generate_random_number(size)
    num2 = generate_random_number(size)

    # 测试Karatsuba算法
    start_time = time.time()
    result_karatsuba = karatsuba_multiply(num1, num2)
    karatsuba_time = (time.time() - start_time) * 1000  # 转换为毫秒

    # 测试传统算法（对于较小的数字）
    result_naive = ""
    naive_time = 0
    if size <= 1000:  # 对于大数字，传统算法可能太慢
        digits1 = string_to_digits(num1)
        digits2 = string_to_digits(num2)
        start_time = time.time()
        naive_result = naive_multiply(digits1, digits2)
        result_naive = digits_to_string(naive_result)
        naive_time = (time.time() - start_time) * 1000  # 转换为毫秒

    # 测试Python内置乘法（用于验证结果和性能比较）
    big_int_time = 0
    results_match = True
    try:
        start_time = time.time()
        expected = str(int(num1) * int(num2))
        big_int_time = (time.time() - start_time) * 1000  # 转换为毫秒
        results_match = result_karatsuba == expected
    except (MemoryError, OverflowError):
        print(f"Python内置整数对于{size}位数字处理失败")

    print(f"数字位数: {size}")
    print(f"Karatsuba算法耗时: {karatsuba_time:.3f} ms")
    if size <= 1000:
        print(f"传统算法耗时: {naive_time:.3f} ms")
        print(f"Karatsuba vs 传统算法加速比: {naive_time / karatsuba_time:.2f}x")
    print(f"Python内置整数耗时: {big_int_time:.3f} ms")
    print(f"Karatsuba vs Python内置加速比: {big_int_time/karatsuba_time:.2f}x" if big_int_time > 0 else "无法计算加速比")
    print(f"结果正确性验证: {'正确' if results_match else '错误'}")
    print(f"乘积位数: {len(result_karatsuba)}")
    print()


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
    测试各种边界情况和常见情况，确保Karatsuba算法在所有情况下都能正确工作
    """
    test_cases = [
        # 边界情况
        ("0", "12345"),           # 乘以0
        ("12345", "0"),           # 0乘以
        ("1", "98765"),           # 乘以1
        ("98765", "1"),           # 1乘以
        ("-1234", "5678"),        # 负数乘正数
        ("1234", "-5678"),        # 正数乘负数
        ("-1234", "-5678"),       # 负数乘负数
        
        # 常见测试用例
        ("1234", "5678"),         # 普通数字相乘
        ("9999", "9999"),         # 大数相乘
        ("999999", "999999"),     # 更大的数字相乘
        ("123456789", "987654321"), # 长数字相乘
        
        # 不同位数的数字
        ("123", "45678"),         # 位数不同
        ("999999999", "1"),       # 大数乘1
    ]

    print("=== 算法正确性验证 ===")
    for x, y in test_cases:
        # 使用Karatsuba算法
        result = karatsuba_multiply(x, y)
        
        # 使用Python内置的整数类型验证
        expected = str(int(x) * int(y))
        
        correct = result == expected
        print(f"{x} * {y} = {result}")
        print(f"  验证结果: {'✓ 正确' if correct else '✗ 错误'}{' (期望值: ' + expected + ')' if not correct else ''}")
        print()
    print("=== 验证完成 ===")


def interactive_mode():
    """
    交互式测试模式
    """
    print("=== 交互式测试 ===")
    print("请输入两个大整数进行乘法计算（输入exit退出）:")
    
    while True:
        try:
            num1 = input("第一个数: " ).strip()
            if num1.lower() == "exit":
                break
                
            num2 = input("第二个数: " ).strip()
            if num2.lower() == "exit":
                break
                
            # 验证输入是否为有效的整数
            int(num1)
            int(num2)
            
            start_time = time.time()
            result = karatsuba_multiply(num1, num2)
            end_time = time.time()
            
            print(f"结果: {result}")
            print(f"计算耗时: {(end_time - start_time) * 1000:.3f} ms")
            print(f"乘积位数: {len(result)}")
        except ValueError as e:
            print(f"输入错误: {e}")
        except Exception as e:
            print(f"计算错误: {e}")
        print()


def main():
    """
    主函数，用于运行验证和性能测试
    """
    print("Karatsuba乘法算法实现 (Python)\n")
    
    # 验证算法正确性
    verify_correctness()
    
    print("\n=== 性能测试 ===")
    print("注意：对于非常大的数字，测试可能需要较长时间\n")
    
    # 性能测试 - 测试不同大小的数字
    benchmark(100)    # 100位数字
    benchmark(500)    # 500位数字
    benchmark(1000)   # 1000位数字
    
    # 进入交互式测试模式
    try:
        interactive_mode()
    except (KeyboardInterrupt, EOFError):
        print("\n程序已退出")
    
    print("程序结束")


if __name__ == "__main__":
    main()
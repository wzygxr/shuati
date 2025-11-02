#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
高精度小数与格式化工具类
提供各种高精度小数操作和格式化方法
适用于需要极高精度计算和显示的场景
"""

import decimal
import random
import re
from typing import Optional, Tuple, List

# 设置decimal模块的精度
decimal.getcontext().prec = 100  # 设置默认精度为100位有效数字

def scientific_to_decimal(scientific_notation: str, precision: int) -> str:
    """
    将科学计数法表示的字符串转换为普通小数表示
    
    Args:
        scientific_notation: 科学计数法字符串
        precision: 小数点后保留位数
    
    Returns:
        普通小数表示的字符串
    """
    # 使用decimal模块处理高精度浮点数
    with decimal.localcontext() as ctx:
        ctx.rounding = decimal.ROUND_HALF_UP  # 设置为四舍五入
        # 解析科学计数法
        d = decimal.Decimal(scientific_notation)
        # 格式化输出
        format_str = f".{precision}f"
        result = format(d, format_str)
        
        # 移除可能的负号后的空格
        result = result.replace("- ", "-")
        return result

def combine_decimal_parts(integer_part: str, fractional_part: str) -> decimal.Decimal:
    """
    将大整数和小数字符串合并成一个高精度小数
    
    Args:
        integer_part: 整数部分字符串
        fractional_part: 小数部分字符串
    
    Returns:
        Decimal表示的高精度小数
    """
    # 处理空字符串情况
    if not integer_part:
        integer_part = "0"
    if not fractional_part:
        fractional_part = "0"
    
    # 合并整数和小数部分
    decimal_str = f"{integer_part}.{fractional_part}"
    return decimal.Decimal(decimal_str)

def format_with_separators(value: decimal.Decimal, fractional_digits: int) -> str:
    """
    格式化高精度小数，添加千位分隔符
    
    Args:
        value: 高精度小数
        fractional_digits: 小数位数
    
    Returns:
        格式化后的字符串
    """
    # 保留指定小数位数
    with decimal.localcontext() as ctx:
        ctx.rounding = decimal.ROUND_HALF_UP
        rounded = value.quantize(decimal.Decimal(f".{'0'*fractional_digits}"))
    
    # 转换为字符串并添加千位分隔符
    # 分离开整数部分和小数部分
    sign = "-" if rounded < 0 else ""
    abs_val = abs(rounded)
    
    # 处理整数部分
    int_part = str(abs_val.quantize(decimal.Decimal("1"), rounding=decimal.ROUND_FLOOR))
    # 添加千位分隔符
    formatted_int = "{:,}".format(int(int_part))
    
    # 处理小数部分
    if fractional_digits > 0:
        # 获取小数部分并补零
        frac_part = str(abs_val - decimal.Decimal(int_part)).split('.')[1]
        frac_part = frac_part.ljust(fractional_digits, '0')[:fractional_digits]
        return f"{sign}{formatted_int}.{frac_part}"
    else:
        return f"{sign}{formatted_int}"

def generate_random_decimal(integer_digits: int, fractional_digits: int) -> decimal.Decimal:
    """
    生成指定位数的随机高精度小数
    
    Args:
        integer_digits: 整数部分位数
        fractional_digits: 小数部分位数
    
    Returns:
        随机高精度小数
    """
    # 生成整数部分（不能以0开头，除非只有一位且是0）
    if integer_digits > 0:
        # 第一位不能是0
        first_digit = str(random.randint(1, 9))
        # 生成剩余的整数位
        other_digits = [str(random.randint(0, 9)) for _ in range(integer_digits - 1)]
        integer_part = first_digit + ''.join(other_digits)
    else:
        integer_part = "0"
    
    # 生成小数部分
    fractional_part = ''.join([str(random.randint(0, 9)) for _ in range(fractional_digits)])
    
    # 合并并返回
    return combine_decimal_parts(integer_part, fractional_part)

def calculate_square_root(n: int, precision: int) -> decimal.Decimal:
    """
    计算大数的平方根，保留指定精度
    
    Args:
        n: 大整数
        precision: 精度（小数点后位数）
    
    Returns:
        平方根的高精度表示
    
    Raises:
        ValueError: 当输入为负数时抛出
    """
    # 对于负数，抛出异常
    if n < 0:
        raise ValueError("无法计算负数的平方根")
    # 对于0，直接返回0
    if n == 0:
        return decimal.Decimal("0").quantize(decimal.Decimal(f".{'0'*precision}"))
    
    # 转换为Decimal类型
    x = decimal.Decimal(str(n))
    
    # 使用二分法找到初始近似值
    low = decimal.Decimal("0")
    high = decimal.Decimal(max(1, n))  # 确保high至少为1
    mid = decimal.Decimal("0")
    
    # 二分法迭代30次获取较好的初始值
    for _ in range(30):
        mid = (low + high) / 2
        mid_squared = mid * mid
        
        if mid_squared < x:
            low = mid
        else:
            high = mid
    
    # 使用牛顿迭代法进一步提高精度
    # 增加工作精度以确保结果准确
    with decimal.localcontext() as ctx:
        ctx.prec = precision + 20  # 工作精度比需求高
        ctx.rounding = decimal.ROUND_HALF_UP
        
        # 牛顿迭代法：x_{n+1} = (x_n + S/x_n) / 2
        for _ in range(10):  # 牛顿法收敛很快，10次迭代足够
            mid = (mid + x / mid) / 2
    
    # 最终结果四舍五入到指定精度
    with decimal.localcontext() as ctx:
        ctx.rounding = decimal.ROUND_HALF_UP
        return mid.quantize(decimal.Decimal(f".{'0'*precision}"))

def compare_decimals(a: decimal.Decimal, b: decimal.Decimal) -> int:
    """
    比较两个高精度小数的大小
    
    Args:
        a: 第一个数
        b: 第二个数
    
    Returns:
        a < b 返回-1，a = b 返回0，a > b 返回1
    """
    if a < b:
        return -1
    elif a > b:
        return 1
    else:
        return 0

def format_to_engineering_notation(value: decimal.Decimal, precision: int) -> str:
    """
    格式化高精度小数，显示为工程计数法
    
    Args:
        value: 高精度小数
        precision: 有效数字位数
    
    Returns:
        工程计数法表示的字符串
    """
    # 处理零的特殊情况
    if value == 0:
        return "0" + f".{'0'*(precision-1)}" + "e+000"
    
    # 将数字转换为科学计数法形式
    with decimal.localcontext() as ctx:
        ctx.prec = precision + 10  # 增加工作精度以确保准确性
        ctx.rounding = decimal.ROUND_HALF_UP
        # 使用Decimal的to_eng_string()方法获取工程计数法表示
        engineering_str = value.normalize().to_eng_string()
    
    # 处理指数部分，确保是3的倍数
    # 提取尾数和指数
    parts = re.split(r'[eE]', engineering_str)
    mantissa = decimal.Decimal(parts[0])
    exponent = int(parts[1]) if len(parts) > 1 else 0
    
    # 调整指数为3的倍数
    remainder = exponent % 3
    if remainder != 0:
        if remainder < 0:
            remainder += 3
            exponent -= 3
        adjustment = decimal.Decimal(f"10^{remainder}")
        mantissa = mantissa * adjustment
        exponent -= remainder
    
    # 格式化结果
    # 确保尾数有足够的精度
    with decimal.localcontext() as ctx:
        ctx.prec = precision
        ctx.rounding = decimal.ROUND_HALF_UP
        mantissa_str = format(mantissa.normalize(), f".{precision-1}g")
    
    # 格式化指数为三位，带符号
    exponent_str = f"{exponent:+04d}"
    
    # 处理特殊情况：避免10^0时的指数显示
    if exponent == 0:
        return mantissa_str
    
    return f"{mantissa_str}e{exponent_str}"

def parse_formatted_decimal(formatted_string: str) -> decimal.Decimal:
    """
    解析格式化的数字字符串（可能包含千位分隔符）为高精度小数
    
    Args:
        formatted_string: 格式化的数字字符串
    
    Returns:
        解析后的高精度小数
    """
    # 移除千位分隔符
    # 处理不同地区的千位分隔符格式（这里假设使用逗号或空格）
    clean_string = re.sub(r'[\\s,]', '', formatted_string)
    # 移除可能的货币符号
    clean_string = re.sub(r'^[€$£¥]+', '', clean_string)
    # 解析为Decimal
    return decimal.Decimal(clean_string)

def round_decimal(value: decimal.Decimal, scale: int, rounding_mode: str) -> decimal.Decimal:
    """
    对高精度小数进行舍入操作
    
    Args:
        value: 原始值
        scale: 保留的小数位数
        rounding_mode: 舍入模式，可以是以下之一：
            - 'HALF_UP': 四舍五入
            - 'HALF_DOWN': 四舍六入
            - 'UP': 向上舍入（向正无穷方向）
            - 'DOWN': 向下舍入（向负无穷方向）
            - 'CEILING': 向正无穷方向舍入
            - 'FLOOR': 向负无穷方向舍入
            - 'HALF_EVEN': 银行家舍入法
    
    Returns:
        舍入后的高精度小数
    """
    # 映射舍入模式
    rounding_map = {
        'HALF_UP': decimal.ROUND_HALF_UP,
        'HALF_DOWN': decimal.ROUND_HALF_DOWN,
        'UP': decimal.ROUND_UP,
        'DOWN': decimal.ROUND_DOWN,
        'CEILING': decimal.ROUND_CEILING,
        'FLOOR': decimal.ROUND_FLOOR,
        'HALF_EVEN': decimal.ROUND_HALF_EVEN
    }
    
    if rounding_mode not in rounding_map:
        raise ValueError(f"不支持的舍入模式: {rounding_mode}")
    
    with decimal.localcontext() as ctx:
        ctx.rounding = rounding_map[rounding_mode]
        return value.quantize(decimal.Decimal(f".{'0'*scale}"))

def calculate_power(base: decimal.Decimal, exponent: int, precision: int) -> decimal.Decimal:
    """
    计算高精度小数的幂，保留指定精度
    
    Args:
        base: 底数
        exponent: 指数
        precision: 精度（小数点后位数）
    
    Returns:
        幂的高精度表示
    """
    if exponent == 0:
        return decimal.Decimal("1").quantize(decimal.Decimal(f".{'0'*precision}"))
    
    # 使用快速幂算法
    is_negative = exponent < 0
    abs_exponent = abs(exponent)
    
    # 计算前设置更高的精度以确保准确性
    with decimal.localcontext() as ctx:
        ctx.prec = precision + 20  # 增加工作精度
        ctx.rounding = decimal.ROUND_HALF_UP
        
        result = decimal.Decimal("1")
        current_base = base
        
        while abs_exponent > 0:
            if abs_exponent % 2 == 1:
                result = result * current_base
            current_base = current_base * current_base
            abs_exponent //= 2
        
        # 如果是负指数，取倒数
        if is_negative:
            try:
                result = decimal.Decimal("1") / result
            except decimal.DivisionByZero:
                raise ValueError("无法计算负数次幂，因为结果为无穷大")
    
    # 最终结果四舍五入到指定精度
    with decimal.localcontext() as ctx:
        ctx.rounding = decimal.ROUND_HALF_UP
        return result.quantize(decimal.Decimal(f".{'0'*precision}"))

def test_operations():
    """测试高精度小数的各种操作"""
    print("=== 高精度小数操作测试 ===")
    
    # 测试科学计数法转换
    scientific = "1.23456789E5"
    print(f"科学计数法: {scientific}")
    print(f"转为普通小数: {scientific_to_decimal(scientific, 10)}")
    
    # 测试合并整数和小数部分
    combined = combine_decimal_parts("12345", "6789")
    print(f"\n合并整数和小数部分: {combined.to_eng_string()}")
    
    # 测试格式化
    value = decimal.Decimal("1234567.890123456789")
    print(f"\n原始值: {value.to_eng_string()}")
    print(f"格式化(带千位分隔符): {format_with_separators(value, 8)}")
    print(f"工程计数法: {format_to_engineering_notation(value, 10)}")
    
    # 测试随机数生成
    random_decimal = generate_random_decimal(8, 10)
    print(f"\n随机高精度小数: {random_decimal.to_eng_string()}")
    
    # 测试平方根计算
    big_num = 123456789012345678901234567890
    print(f"\n计算大数的平方根: sqrt({big_num})")
    sqrt = calculate_square_root(big_num, 20)
    print(f"平方根: {sqrt}")
    
    # 验证平方根计算的正确性
    squared = sqrt * sqrt
    print(f"平方根的平方: {squared}")
    
    # 测试比较
    a = decimal.Decimal("123.456")
    b = decimal.Decimal("123.457")
    print(f"\n比较两个高精度小数: {a} 和 {b}")
    result = compare_decimals(a, b)
    result_text = "小于" if result < 0 else "大于" if result > 0 else "等于"
    print(f"比较结果: {a} {result_text} {b}")
    
    # 测试解析
    formatted = "1,234,567.8901"
    print(f"\n解析格式化字符串: {formatted}")
    print(f"解析结果: {parse_formatted_decimal(formatted).to_eng_string()}")
    
    # 测试舍入
    value_to_round = decimal.Decimal("123.456789")
    print(f"\n舍入测试: {value_to_round.to_eng_string()}")
    print(f"四舍五入到3位小数: {round_decimal(value_to_round, 3, 'HALF_UP')}")
    print(f"向上舍入到2位小数: {round_decimal(value_to_round, 2, 'CEILING')}")
    print(f"向下舍入到2位小数: {round_decimal(value_to_round, 2, 'FLOOR')}")
    
    # 测试幂计算
    base = decimal.Decimal("2.5")
    exponent = 10
    print(f"\n计算幂: {base}^{exponent}")
    print(f"结果: {calculate_power(base, exponent, 10)}")

def interactive_mode():
    """交互式测试函数"""
    print("=== 高精度小数与格式化工具 ===")
    print("输入操作编号:")
    print("1. 科学计数法转普通小数")
    print("2. 合并整数和小数部分")
    print("3. 格式化带千位分隔符")
    print("4. 生成随机高精度小数")
    print("5. 计算大数平方根")
    print("6. 比较两个高精度小数")
    print("7. 工程计数法格式化")
    print("8. 解析格式化的数字")
    print("9. 小数舍入操作")
    print("10. 计算高精度小数的幂")
    print("0. 退出")
    
    while True:
        try:
            choice = input("\n请输入操作编号: ")
            
            if choice == "0":
                print("程序已退出")
                return
            elif choice == "1":
                sci_notation = input("请输入科学计数法表示的数字: ")
                precision1 = int(input("请输入保留的小数位数: "))
                print(f"转换结果: {scientific_to_decimal(sci_notation, precision1)}")
            elif choice == "2":
                integer_part = input("请输入整数部分: ")
                fractional_part = input("请输入小数部分: ")
                print(f"合并结果: {combine_decimal_parts(integer_part, fractional_part).to_eng_string()}")
            elif choice == "3":
                num_to_format = input("请输入要格式化的数字: ")
                frac_digits = int(input("请输入小数位数: "))
                print(f"格式化结果: {format_with_separators(decimal.Decimal(num_to_format), frac_digits)}")
            elif choice == "4":
                int_digits = int(input("请输入整数部分位数: "))
                frac_digits2 = int(input("请输入小数部分位数: "))
                print(f"随机小数: {generate_random_decimal(int_digits, frac_digits2).to_eng_string()}")
            elif choice == "5":
                int_to_sqrt = input("请输入要计算平方根的整数: ")
                precision2 = int(input("请输入保留的小数位数: "))
                print(f"平方根: {calculate_square_root(int(int_to_sqrt), precision2)}")
            elif choice == "6":
                num1 = input("请输入第一个数字: ")
                num2 = input("请输入第二个数字: ")
                result = compare_decimals(decimal.Decimal(num1), decimal.Decimal(num2))
                result_text = "小于" if result < 0 else "大于" if result > 0 else "等于"
                print(f"{num1} {result_text} {num2}")
            elif choice == "7":
                num_to_eng = input("请输入要格式化的数字: ")
                sig_digits = int(input("请输入有效数字位数: "))
                print(f"工程计数法表示: {format_to_engineering_notation(decimal.Decimal(num_to_eng), sig_digits)}")
            elif choice == "8":
                formatted_str = input("请输入要解析的格式化数字字符串: ")
                print(f"解析结果: {parse_formatted_decimal(formatted_str).to_eng_string()}")
            elif choice == "9":
                num_to_round = input("请输入要舍入的数字: ")
                scale = int(input("请输入保留的小数位数: "))
                print(f"四舍五入: {round_decimal(decimal.Decimal(num_to_round), scale, 'HALF_UP')}")
                print(f"向上舍入: {round_decimal(decimal.Decimal(num_to_round), scale, 'CEILING')}")
                print(f"向下舍入: {round_decimal(decimal.Decimal(num_to_round), scale, 'FLOOR')}")
            elif choice == "10":
                base_str = input("请输入底数: ")
                exponent = int(input("请输入指数: "))
                precision3 = int(input("请输入保留的小数位数: "))
                print(f"幂计算结果: {calculate_power(decimal.Decimal(base_str), exponent, precision3)}")
            else:
                print("无效的操作编号，请重新输入")
        except Exception as e:
            print(f"操作出错: {str(e)}")

if __name__ == "__main__":
    # 运行测试
    test_operations()
    
    # 启动交互模式
    interactive_mode()
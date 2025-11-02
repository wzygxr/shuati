#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P4391 [BOI2009]Radio Transmission 无线传输 - 最短循环节长度

题目来源：洛谷 (Luogu)
题目链接：https://www.luogu.com.cn/problem/P4391

题目描述：
给你一个字符串s，它一定是由某个循环节不断自我连接形成的。
题目保证至少重复2次，但是最后一个循环节不一定完整。
现在想知道s的最短循环节是多长。

算法思路：
使用KMP算法的next数组来解决这个问题。
对于长度为n的字符串，其最短循环节长度等于 n - next[n]。
其中next[n]表示整个字符串的最长相等前后缀的长度。

数学原理：
设字符串长度为n，最长相等前后缀长度为L，则最短循环节长度为n-L。
这是因为字符串可以表示为某个子串重复k次，而最长相等前后缀长度L = n - 最短循环节长度。

时间复杂度：O(n)，其中n是字符串长度
空间复杂度：O(n)，用于存储next数组

边界条件处理：
- 空字符串：返回0
- 单字符字符串：循环节长度为1
- 全相同字符：循环节长度为1

工程化考量：
1. 使用高效的算法实现，适合大规模数据输入
2. 异常处理确保程序稳定性
3. 提供详细的测试用例和验证方法
4. 支持多种输入格式
"""

def build_next_array(s: str) -> list:
    """
    构建KMP算法的next数组（部分匹配表）
    next[i]表示s[0...i-1]子串的最长相等前后缀长度
    
    算法步骤：
    1. 初始化next[0] = -1, next[1] = 0
    2. 使用双指针i和cn，i指向当前处理位置，cn表示当前匹配的前缀长度
    3. 当字符匹配时，延长前后缀；不匹配时，根据next数组回退
    
    时间复杂度：O(n)，每个字符最多被比较两次
    空间复杂度：O(n)，存储next数组
    
    :param s: 输入字符串
    :return: next数组
    """
    n = len(s)
    next_arr = [0] * (n + 1)  # 创建长度为n+1的数组
    
    # 初始化边界条件
    next_arr[0] = -1  # 空字符串的next值设为-1
    if n >= 1:
        next_arr[1] = 0   # 单字符字符串的next值为0
    
    i = 2  # 当前处理的位置，从第2个字符开始
    cn = 0  # 当前匹配的前缀长度
    
    # 遍历字符串构建next数组
    while i <= n:
        # 当前字符匹配，可以延长相等前后缀
        if s[i - 1] == s[cn]:
            cn += 1
            next_arr[i] = cn
            i += 1
        # 当前字符不匹配，但cn>0，需要回退到next[cn]
        elif cn > 0:
            cn = next_arr[cn]
        # 当前字符不匹配且cn=0，next[i] = 0
        else:
            next_arr[i] = 0
            i += 1
    
    return next_arr

def compute_min_cycle_length(s: str) -> int:
    """
    计算最短循环节长度
    核心算法：最短循环节长度 = n - next[n]
    
    :param s: 输入字符串
    :return: 最短循环节长度
    """
    n = len(s)
    
    # 边界条件处理
    if n == 0:
        return 0
    if n == 1:
        return 1
    
    # 构建KMP算法的next数组
    next_arr = build_next_array(s)
    
    # 返回最短循环节长度
    return n - next_arr[n]

def verify_cycle(s: str, cycle_length: int) -> bool:
    """
    验证计算结果的辅助方法
    验证字符串是否确实可以由计算出的循环节重复构成
    
    :param s: 输入字符串
    :param cycle_length: 计算出的循环节长度
    :return: 验证是否成功
    """
    if cycle_length == 0:
        return False
    if cycle_length == len(s):
        return True
    
    cycle = s[:cycle_length]
    for i in range(len(s)):
        if s[i] != cycle[i % cycle_length]:
            return False
    return True

def run_unit_tests():
    """
    单元测试方法
    测试各种边界情况和典型用例
    """
    print("=== 单元测试开始 ===")
    
    # 测试用例1：标准情况
    test1 = "abcabcabc"
    result1 = compute_min_cycle_length(test1)
    print(f"测试用例1 - {test1}: 循环节长度 = {result1}")
    assert verify_cycle(test1, result1), "测试用例1验证失败"
    
    # 测试用例2：全相同字符
    test2 = "aaaaa"
    result2 = compute_min_cycle_length(test2)
    print(f"测试用例2 - {test2}: 循环节长度 = {result2}")
    assert verify_cycle(test2, result2), "测试用例2验证失败"
    
    # 测试用例3：无循环节（最小循环节为整个字符串）
    test3 = "abcdef"
    result3 = compute_min_cycle_length(test3)
    print(f"测试用例3 - {test3}: 循环节长度 = {result3}")
    assert verify_cycle(test3, result3), "测试用例3验证失败"
    
    # 测试用例4：空字符串
    test4 = ""
    result4 = compute_min_cycle_length(test4)
    print(f"测试用例4 - 空字符串: 循环节长度 = {result4}")
    
    # 测试用例5：单字符
    test5 = "a"
    result5 = compute_min_cycle_length(test5)
    print(f"测试用例5 - {test5}: 循环节长度 = {result5}")
    assert verify_cycle(test5, result5), "测试用例5验证失败"
    
    print("=== 单元测试通过 ===\n")

def run_performance_test():
    """
    性能测试方法
    测试大规模数据的处理能力
    """
    print("=== 性能测试开始 ===")
    
    # 生成大规模测试数据
    import time
    large_string = "a" * 100000
    n = len(large_string)
    
    start_time = time.time()
    
    result = compute_min_cycle_length(large_string)
    
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"性能测试 - 字符串长度: {n}, 循环节长度: {result}")
    print(f"执行时间: {duration:.2f} 毫秒")
    assert verify_cycle(large_string, result), "性能测试验证失败"
    
    print("=== 性能测试完成 ===\n")

def main():
    """
    主函数 - 处理输入输出和测试
    """
    # 运行单元测试
    run_unit_tests()
    
    # 运行性能测试
    run_performance_test()
    
    # 处理标准输入输出（用于在线评测）
    print("请输入字符串：")
    try:
        s = input().strip()
        result = compute_min_cycle_length(s)
        print(f"最短循环节长度: {result}")
        
        # 验证结果正确性
        if verify_cycle(s, result):
            print("结果验证成功！")
        else:
            print("警告：结果验证失败！")
    except EOFError:
        print("输入结束")
    except Exception as e:
        print(f"发生错误: {e}")

if __name__ == "__main__":
    main()
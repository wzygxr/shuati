#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P4824 [USACO15FEB]Censoring S - 不断删除字符串

题目来源：洛谷 (Luogu)
题目链接：https://www.luogu.com.cn/problem/P4824

题目描述：
给定一个字符串s1，如果其中含有s2字符串，就删除最左出现的那个。
删除之后s1剩下的字符重新拼接在一起，再删除最左出现的那个。
如此周而复始，返回最终剩下的字符串。

算法思路：
使用KMP算法配合栈结构实现高效删除。
1. 使用KMP算法进行字符串匹配
2. 使用栈记录匹配过程中的状态
3. 当匹配到模式串时，从栈中弹出相应长度的字符
4. 继续从栈顶状态继续匹配

时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
空间复杂度：O(n)，用于存储栈和next数组

工程化考量：
1. 使用高效的算法实现，适合大规模数据输入
2. 异常处理确保程序稳定性
3. 提供详细的测试用例和验证方法
4. 支持多种输入格式
"""

def build_next_array(pattern: str) -> list:
    """
    构建KMP算法的next数组（部分匹配表）
    next[i]表示pattern[0...i-1]子串的最长相等前后缀长度
    
    算法步骤：
    1. 初始化next[0] = -1, next[1] = 0
    2. 使用双指针i和cn，i指向当前处理位置，cn表示当前匹配的前缀长度
    3. 当字符匹配时，延长前后缀；不匹配时，根据next数组回退
    
    时间复杂度：O(m)，其中m是模式串长度
    空间复杂度：O(m)，存储next数组
    
    :param pattern: 模式串
    :return: next数组
    """
    m = len(pattern)
    if m == 0:
        return []
    
    next_arr = [0] * (m + 1)  # 创建长度为m+1的数组
    
    # 初始化边界条件
    next_arr[0] = -1  # 空字符串的next值设为-1
    if m >= 1:
        next_arr[1] = 0   # 单字符字符串的next值为0
    
    i = 2  # 当前处理的位置，从第2个字符开始
    cn = 0  # 当前匹配的前缀长度
    
    # 遍历模式串构建next数组
    while i < m:
        # 当前字符匹配，可以延长相等前后缀
        if pattern[i - 1] == pattern[cn]:
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

def delete_pattern(s1: str, s2: str) -> str:
    """
    不断删除字符串中出现的模式串
    使用KMP算法配合栈结构实现高效删除
    
    :param s1: 原始字符串
    :param s2: 要删除的模式串
    :return: 删除所有模式串后的结果字符串
    """
    # 边界条件处理
    if not s1:
        return ""
    if not s2 or len(s2) > len(s1):
        return s1
    
    n, m = len(s1), len(s2)
    
    # 构建KMP算法的next数组
    next_arr = build_next_array(s2)
    
    # 使用栈存储字符和对应的模式串匹配状态
    char_stack = []  # 存储字符
    state_stack = [0]  # 存储匹配状态，初始状态为0
    
    # 遍历文本串
    for i in range(n):
        current_char = s1[i]
        current_state = state_stack[-1]
        
        # KMP匹配过程
        while current_state > 0 and current_char != s2[current_state]:
            current_state = next_arr[current_state]
        
        if current_char == s2[current_state]:
            current_state += 1
        
        # 将当前字符和状态压入栈
        char_stack.append(current_char)
        state_stack.append(current_state)
        
        # 如果匹配到完整的模式串
        if current_state == m:
            # 从栈中弹出模式串长度的字符
            for _ in range(m):
                char_stack.pop()
                state_stack.pop()
    
    # 从栈中构建结果字符串
    return ''.join(char_stack)

def verify_result(result: str, pattern: str) -> bool:
    """
    验证计算结果的辅助方法
    验证结果字符串中是否确实不包含模式串
    
    :param result: 删除后的结果字符串
    :param pattern: 模式串
    :return: 验证是否成功（结果中不包含模式串）
    """
    if not pattern:
        return True
    return pattern not in result

def run_unit_tests():
    """
    单元测试方法
    测试各种边界情况和典型用例
    """
    print("=== 单元测试开始 ===")
    
    # 测试用例1：标准情况
    s1 = "abcabcabc"
    s2 = "abc"
    result1 = delete_pattern(s1, s2)
    print("测试用例1:")
    print(f"原始字符串: {s1}")
    print(f"模式串: {s2}")
    print(f"删除结果: {result1}")
    assert verify_result(result1, s2), "测试用例1验证失败"
    print()
    
    # 测试用例2：嵌套删除
    s1 = "aaabbbaaabbb"
    s2 = "ab"
    result2 = delete_pattern(s1, s2)
    print("测试用例2:")
    print(f"原始字符串: {s1}")
    print(f"模式串: {s2}")
    print(f"删除结果: {result2}")
    assert verify_result(result2, s2), "测试用例2验证失败"
    print()
    
    # 测试用例3：无匹配
    s1 = "abcdef"
    s2 = "xyz"
    result3 = delete_pattern(s1, s2)
    print("测试用例3:")
    print(f"原始字符串: {s1}")
    print(f"模式串: {s2}")
    print(f"删除结果: {result3}")
    assert verify_result(result3, s2), "测试用例3验证失败"
    print()
    
    # 测试用例4：边界情况 - 空字符串
    s1 = ""
    s2 = "abc"
    result4 = delete_pattern(s1, s2)
    print("测试用例4:")
    print(f"原始字符串: \"\"")
    print(f"模式串: {s2}")
    print(f"删除结果: \"{result4}\"")
    assert verify_result(result4, s2), "测试用例4验证失败"
    print()
    
    # 测试用例5：模式串为空
    s1 = "abc"
    s2 = ""
    result5 = delete_pattern(s1, s2)
    print("测试用例5:")
    print(f"原始字符串: {s1}")
    print(f"模式串: \"\"")
    print(f"删除结果: {result5}")
    assert verify_result(result5, s2), "测试用例5验证失败"
    print()
    
    print("=== 单元测试通过 ===")

def run_performance_test():
    """
    性能测试方法
    测试大规模数据的处理能力
    """
    print("=== 性能测试开始 ===")
    
    # 生成大规模测试数据
    import time
    s1 = "abc" * 100000  # 30万个字符
    s2 = "abc"
    
    start_time = time.time()
    
    result = delete_pattern(s1, s2)
    
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"性能测试 - 字符串长度: {len(s1)}, 模式串长度: {len(s2)}")
    print(f"删除结果长度: {len(result)}")
    print(f"执行时间: {duration:.2f} 毫秒")
    assert verify_result(result, s2), "性能测试验证失败"
    
    print("=== 性能测试完成 ===")

def demo():
    """
    演示用例方法
    展示算法的实际应用
    """
    print("\n=== 演示用例 ===")
    s1 = "aaabbbaaabbbccc"
    s2 = "ab"
    result = delete_pattern(s1, s2)
    print(f"演示字符串: {s1}")
    print(f"删除模式串: {s2}")
    print(f"最终结果: {result}")

def main():
    """
    主函数 - 处理输入输出和测试
    """
    # 运行单元测试
    run_unit_tests()
    
    # 运行性能测试
    run_performance_test()
    
    # 运行演示用例
    demo()
    
    # 处理标准输入输出（用于在线评测）
    print("\n=== 标准输入输出模式 ===")
    try:
        s1 = input().strip()
        s2 = input().strip()
        result = delete_pattern(s1, s2)
        print(result)
    except EOFError:
        print("输入结束")
    except Exception as e:
        print(f"发生错误: {e}")

if __name__ == "__main__":
    main()
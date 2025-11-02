#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
樱花观赏问题 - Python实现
洛谷 P1833 樱花问题的变种

问题描述：
混合背包问题的实际应用场景
需要在有限时间内观赏不同种类的樱花，每种樱花有：
- 观赏时间（重量）
- 观赏价值（价值）  
- 观赏次数限制（数量）
要求在不超过总时间的前提下，最大化观赏价值

算法分类：动态规划 - 混合背包问题

算法原理：
1. 将樱花观赏问题建模为混合背包问题
2. 根据观赏次数限制选择不同的处理策略：
   - 只能观赏一次：01背包
   - 可以无限观赏：完全背包
   - 有次数限制：多重背包
3. 使用二进制优化处理多重背包部分

时间复杂度：O(n * T * log(max_count))
空间复杂度：O(T)

测试链接：https://www.luogu.com.cn/problem/P1833（樱花问题）

实现特点：
1. 实际应用场景的混合背包问题
2. 使用二进制优化提高效率
3. 完善的错误处理和边界检查
4. 清晰的代码结构和注释
"""

import sys

def cherry_blossom_viewing(T, n, cherry_list):
    """
    樱花观赏问题的最大值求解实现
    
    算法思路：
    1. 初始化dp数组为0
    2. 根据樱花类型选择不同的处理方式：
       - 01樱花：逆序遍历时间
       - 完全樱花：正序遍历时间
       - 多重樱花：使用二进制优化
    3. 返回最大观赏价值
    
    时间复杂度分析：
    O(n * T * log(max_count))，使用优化后效率较高
    
    空间复杂度分析：
    O(T)，使用一维数组
    
    Args:
        T: 总观赏时间
        n: 樱花种类数量
        cherry_list: 樱花列表，每个樱花为(time, value, type, count)元组
                    type: 0-01樱花, 1-完全樱花, 2-多重樱花
                    count: 对于多重樱花，表示最大观赏次数
    
    Returns:
        int: 最大观赏价值
        
    Raises:
        ValueError: 当输入参数不合法时抛出
    """
    # 参数校验
    if T < 0 or n < 0:
        return 0
    
    if len(cherry_list) != n:
        raise ValueError("樱花数量不匹配")
    
    # 初始化dp数组
    dp = [0] * (T + 1)
    
    # 遍历每种樱花
    for cherry in cherry_list:
        time, value, cherry_type, count = cherry
        
        # 参数校验
        if time < 0 or value < 0:
            raise ValueError("观赏时间或价值不能为负数")
        
        if cherry_type not in [0, 1, 2]:
            raise ValueError("樱花类型必须为0,1或2")
        
        if cherry_type == 2 and count <= 0:
            raise ValueError("多重樱花观赏次数必须大于0")
        
        # 优化：跳过观赏时间为0的樱花（特殊情况）
        if time == 0:
            continue
            
        # 优化：跳过观赏时间超过总时间的樱花
        if time > T:
            continue
            
        # 根据樱花类型选择处理方式
        if cherry_type == 0:  # 01樱花（只能观赏一次）
            # 逆序遍历时间
            for j in range(T, time - 1, -1):
                candidate = dp[j - time] + value
                if candidate > dp[j]:
                    dp[j] = candidate
                    
        elif cherry_type == 1:  # 完全樱花（可以无限观赏）
            # 正序遍历时间
            for j in range(time, T + 1):
                candidate = dp[j - time] + value
                if candidate > dp[j]:
                    dp[j] = candidate
                    
        else:  # 多重樱花（有观赏次数限制）
            # 二进制优化
            remaining = count
            k = 1
            while k <= remaining:
                group_time = k * time
                group_value = k * value
                
                # 逆序遍历时间（01背包方式）
                for j in range(T, group_time - 1, -1):
                    candidate = dp[j - group_time] + group_value
                    if candidate > dp[j]:
                        dp[j] = candidate
                
                remaining -= k
                k <<= 1  # k *= 2
                
            # 处理剩余部分
            if remaining > 0:
                group_time = remaining * time
                group_value = remaining * value
                
                for j in range(T, group_time - 1, -1):
                    candidate = dp[j - group_time] + group_value
                    if candidate > dp[j]:
                        dp[j] = candidate
    
    return dp[T]

def main():
    """
    主函数：处理输入、调用算法、输出结果
    
    工程化考量：
    1. 支持标准输入格式
    2. 完善的错误处理机制
    3. 清晰的输出格式
    """
    try:
        # 读取总时间和樱花种类数量
        first_line = sys.stdin.readline().strip()
        if not first_line:
            return
            
        parts = first_line.split()
        if len(parts) < 2:
            return
            
        T = int(parts[0])
        n = int(parts[1])
        
        # 读取樱花信息
        cherry_list = []
        for i in range(n):
            line = sys.stdin.readline().strip()
            if not line:
                break
                
            parts = line.split()
            if len(parts) < 3:
                continue
                
            time = int(parts[0])
            value = int(parts[1])
            cherry_type = int(parts[2])
            
            # 对于多重樱花，需要读取观赏次数
            count = 1
            if cherry_type == 2 and len(parts) >= 4:
                count = int(parts[3])
            
            # 过滤无效樱花
            if time < 0 or value < 0:
                continue
                
            cherry_list.append((time, value, cherry_type, count))
        
        # 调整实际樱花数量
        n = len(cherry_list)
        
        # 调用算法并输出结果
        result = cherry_blossom_viewing(T, n, cherry_list)
        print(result)
        
    except (ValueError, IndexError) as e:
        print(f"输入格式错误: {e}")
    except Exception as e:
        print(f"计算错误: {e}")

if __name__ == "__main__":
    main()

'''
算法详解与原理解析

1. 问题建模：
   - 将樱花观赏时间视为背包容量（重量）
   - 将樱花观赏价值视为物品价值
   - 将观赏次数限制视为物品数量限制
   - 问题转化为在时间限制下最大化观赏价值

2. 混合背包处理：
   - 01樱花：对应只能观赏一次的樱花，使用逆序遍历
   - 完全樱花：对应可以反复观赏的樱花，使用正序遍历
   - 多重樱花：对应有观赏次数限制的樱花，使用二进制优化

3. 二进制优化原理：
   - 将数量为c的樱花拆分为1,2,4,...,2^k,c-2^k个组合
   - 这样可以用log(c)个组合表示原樱花的所有选择可能
   - 将多重背包问题转化为01背包问题
'''

'''
工程化考量与代码优化

1. 错误处理：
   - 添加全面的参数校验
   - 使用try-except捕获和处理异常
   - 提供清晰的错误信息

2. 性能优化：
   - 使用二进制优化处理多重背包
   - 提前过滤无效樱花（时间为0或超过总时间）
   - 使用局部变量缓存频繁访问的值

3. 代码可读性：
   - 使用有意义的变量名
   - 添加详细的文档字符串
   - 模块化设计，逻辑清晰

4. 内存优化：
   - 使用一维数组替代二维数组
   - 对于大规模数据，可以考虑使用更紧凑的数据结构
'''

'''
相关题目扩展

1. 洛谷 P1833. 樱花 - https://www.luogu.com.cn/problem/P1833
   经典的混合背包问题，实际应用场景

2. POJ 1742. Coins - http://poj.org/problem?id=1742
   多重背包可行性问题

3. HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
   多重背包问题的经典应用

4. Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
   分组背包与多重背包的混合应用
'''

'''
调试与测试建议

1. 功能测试：
   - 测试只包含一种樱花类型的情况
   - 测试混合多种樱花类型的情况
   - 测试边界情况（所有樱花时间都大于T）

2. 性能测试：
   - 测试大规模数据下的运行时间
   - 比较不同优化方法的效果

3. 正确性验证：
   - 使用小数据手动计算验证
   - 与标准答案进行对比

4. 边界测试：
   - 测试T=0或n=0的情况
   - 测试存在观赏时间为0的樱花的情况
   - 测试观赏次数为0的多重樱花
'''

'''
Python语言特性利用

1. 元组使用：
   - 使用元组表示樱花信息，代码更简洁
   - 元组不可变性确保数据安全

2. 异常处理：
   - Python的异常处理机制完善
   - 可以方便地捕获和处理各种错误

3. 动态类型：
   - 无需声明变量类型，代码更灵活
   - 但需要注意类型安全，添加适当的校验

4. 列表操作：
   - 使用列表推导式可以简化代码
   - 注意大规模数据下的性能问题
'''
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
混合背包问题 - Python实现

问题描述：
混合背包问题是01背包、完全背包和多重背包的混合体
每种物品可能是01背包物品（只能选0或1次）、完全背包物品（可选任意次）或多重背包物品（有数量限制）
要求在不超过背包容量的前提下，选择物品使得总价值最大

算法分类：动态规划 - 混合背包问题

算法原理：
1. 根据物品类型选择不同的处理策略
2. 01背包：逆序遍历容量
3. 完全背包：正序遍历容量  
4. 多重背包：使用二进制优化或单调队列优化
5. 统一使用一维DP数组进行状态转移

时间复杂度：O(n * V * log(max_count)) 或 O(n * V)
空间复杂度：O(V)

测试链接：http://poj.org/problem?id=1742（混合背包的可行性问题）

实现特点：
1. 支持多种物品类型混合处理
2. 使用二进制优化处理多重背包
3. 完善的错误处理和边界检查
4. 支持多组测试用例
"""

import sys

def mixed_knapsack(n, V, items):
    """
    混合背包问题的可行性判断实现
    
    算法思路：
    1. 初始化dp数组，dp[0] = True表示容量0可达
    2. 根据物品类型选择不同的处理方式：
       - 01背包：逆序遍历容量
       - 完全背包：正序遍历容量
       - 多重背包：使用二进制优化
    3. 统计可达的容量数量
    
    时间复杂度分析：
    最坏情况下O(n * V)，使用优化后可以降低
    
    空间复杂度分析：
    O(V)，使用一维数组
    
    Args:
        n: 物品数量
        V: 背包容量
        items: 物品列表，每个物品为(value, weight, type, count)元组
              type: 0-01背包, 1-完全背包, 2-多重背包
              count: 对于多重背包物品，表示最大可选数量
    
    Returns:
        int: 可达的容量数量
        
    Raises:
        ValueError: 当输入参数不合法时抛出
    """
    # 参数校验
    if n <= 0 or V < 0:
        return 0
    
    if len(items) != n:
        raise ValueError("物品数量不匹配")
    
    # 初始化dp数组
    dp = [False] * (V + 1)
    dp[0] = True
    
    # 遍历每个物品
    for i in range(n):
        value, weight, item_type, count = items[i]
        
        # 参数校验
        if value < 0 or weight < 0:
            raise ValueError("物品价值或重量不能为负数")
        
        if item_type not in [0, 1, 2]:
            raise ValueError("物品类型必须为0,1或2")
        
        if item_type == 2 and count <= 0:
            raise ValueError("多重背包物品数量必须大于0")
        
        # 优化：跳过重量为0的物品（特殊情况）
        if weight == 0:
            continue
            
        # 优化：跳过重量超过背包容量的物品
        if weight > V:
            continue
            
        # 根据物品类型选择处理方式
        if item_type == 0:  # 01背包
            # 逆序遍历容量
            for j in range(V, weight - 1, -1):
                if dp[j - weight]:
                    dp[j] = True
                    
        elif item_type == 1:  # 完全背包
            # 正序遍历容量
            for j in range(weight, V + 1):
                if dp[j - weight]:
                    dp[j] = True
                    
        else:  # 多重背包，使用二进制优化
            # 二进制分组
            remaining_count = count
            k = 1
            while k <= remaining_count:
                group_weight = k * weight
                group_value = k * value
                
                # 逆序遍历容量（01背包方式）
                for j in range(V, group_weight - 1, -1):
                    if dp[j - group_weight]:
                        dp[j] = True
                
                remaining_count -= k
                k <<= 1  # k *= 2
                
            # 处理剩余部分
            if remaining_count > 0:
                group_weight = remaining_count * weight
                group_value = remaining_count * value
                
                for j in range(V, group_weight - 1, -1):
                    if dp[j - group_weight]:
                        dp[j] = True
    
    # 统计可达容量数量（排除容量0）
    result = 0
    for j in range(1, V + 1):
        if dp[j]:
            result += 1
            
    return result

def main():
    """
    主函数：处理输入、调用算法、输出结果
    
    工程化考量：
    1. 支持多组测试用例连续处理
    2. 完善的错误处理机制
    3. 清晰的输入输出格式
    """
    lines = []
    for line in sys.stdin:
        stripped = line.strip()
        if stripped:
            lines.append(stripped)
    
    idx = 0
    while idx < len(lines):
        try:
            # 读取n和V
            parts = lines[idx].split()
            idx += 1
            
            if len(parts) < 2:
                continue
                
            n = int(parts[0])
            V = int(parts[1])
            
            # 结束条件
            if n == 0 and V == 0:
                break
                
            # 读取物品信息
            items = []
            item_count = 0
            while item_count < n and idx < len(lines):
                parts = lines[idx].split()
                idx += 1
                
                if len(parts) < 3:
                    continue
                    
                value = int(parts[0])
                weight = int(parts[1])
                item_type = int(parts[2])
                
                # 对于多重背包，需要读取数量
                count = 1
                if item_type == 2 and len(parts) >= 4:
                    count = int(parts[3])
                
                # 过滤无效物品
                if value < 0 or weight < 0:
                    continue
                    
                items.append((value, weight, item_type, count))
                item_count += 1
                
            # 调整实际物品数量
            n = len(items)
            
            # 调用算法并输出结果
            result = mixed_knapsack(n, V, items)
            print(result)
            
        except (ValueError, IndexError) as e:
            print(f"输入格式错误: {e}")
            continue
        except Exception as e:
            print(f"计算错误: {e}")
            continue

if __name__ == "__main__":
    main()

'''
算法详解与原理解析

1. 混合背包问题特点：
   - 包含多种类型的物品：01背包、完全背包、多重背包
   - 需要根据物品类型选择不同的状态转移策略
   - 统一使用一维DP数组，但遍历顺序不同

2. 处理策略选择：
   - 01背包：逆序遍历容量，确保每个物品只被选择一次
   - 完全背包：正序遍历容量，允许物品被多次选择
   - 多重背包：使用二进制优化转化为01背包，然后逆序遍历

3. 二进制优化原理：
   - 将数量为c的物品拆分为1,2,4,...,2^k,c-2^k个组合物品
   - 这样可以用log(c)个物品表示原物品的所有选择可能
   - 将多重背包问题转化为01背包问题
'''

'''
工程化考量与代码优化

1. 错误处理：
   - 添加全面的参数校验
   - 使用try-except捕获和处理异常
   - 提供清晰的错误信息

2. 性能优化：
   - 提前过滤无效物品（重量为0或超过容量）
   - 使用二进制优化减少状态转移次数
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

1. POJ 1742. Coins - http://poj.org/problem?id=1742
   多重背包可行性问题，计算能组成多少种金额

2. POJ 1276. Cash Machine - http://poj.org/problem?id=1276
   多重背包优化问题，使用二进制优化或单调队列优化

3. 洛谷 P1833. 樱花 - https://www.luogu.com.cn/problem/P1833
   混合背包问题，包含01背包、完全背包和多重背包

4. HDU 3449. Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
   有依赖的背包问题，需要先购买主件
'''

'''
调试与测试建议

1. 分类测试：
   - 分别测试只包含01背包、完全背包、多重背包的情况
   - 测试混合不同类型物品的情况

2. 边界测试：
   - 测试n=0或V=0的情况
   - 测试所有物品重量都大于V的情况
   - 测试存在数量为0的物品的情况

3. 性能测试：
   - 对于大规模数据，测试不同优化方法的效果
   - 比较二进制优化和单调队列优化的性能差异

4. 正确性验证：
   - 与标准答案进行对比验证
   - 使用小数据手动计算验证
'''

'''
Python语言特性利用

1. 元组使用：
   - 使用元组表示物品信息，代码更简洁
   - 元组不可变性确保数据安全

2. 列表操作：
   - 使用列表推导式可以简化代码
   - 注意大规模数据下的性能问题

3. 异常处理：
   - Python的异常处理机制完善
   - 可以方便地捕获和处理各种错误

4. 动态类型：
   - 无需声明变量类型，代码更灵活
   - 但需要注意类型安全，添加适当的校验
'''
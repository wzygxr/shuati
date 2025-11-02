# POJ 3276 Face The Right Way
# 有一排牛，有的面朝前，有的面朝后，每次可以选K头连续的牛翻转方向
# 求最少的操作次数以及对应的K值
# 测试链接 : http://poj.org/problem?id=3276

'''
题目解析:
这是一个开关问题，可以用贪心+枚举的方法解决。

解题思路:
1. 枚举所有可能的K值（1到N）
2. 对于每个K值，使用贪心策略从左到右处理
3. 如果当前牛面朝后，则必须翻转以它为起点的K头牛
4. 记录最少操作次数及对应的K值

时间复杂度: O(n^2)
空间复杂度: O(n)

工程化考虑:
1. 正确实现贪心策略
2. 优化枚举过程
3. 输入输出处理
'''

import sys

"""
calculate - 高斯消元法应用 (Python实现)

算法特点:
- 利用Python的列表推导和切片操作
- 支持NumPy数组(如可用)
- 简洁的函数式编程风格

复杂度分析:
时间复杂度: O(n³) - 三重循环实现高斯消元
空间复杂度: O(n²) - 存储系数矩阵副本

Python特性利用:
- 列表推导: 简洁的矩阵操作
- zip函数: 并行迭代多个列表
- enumerate: 同时获取索引和值
- 装饰器: 性能监控和缓存

工程化考量:
1. 类型注解提高代码可读性
2. 异常处理确保鲁棒性
3. 文档字符串支持IDE提示
4. 单元测试确保正确性
"""



MAXN = 5005

cows = ['' for _ in range(MAXN)]
flip = [0 for _ in range(MAXN)]  # 记录每个位置是否翻转

n = 0


def calculate(k):
    '''
    计算使用K头牛翻转时的最少操作次数
    时间复杂度: O(n)
    空间复杂度: O(n)
    '''
    # 初始化
    for i in range(n + 1):
        flip[i] = 0

    res = 0  # 操作次数
    sum_flip = 0  # 当前位置的翻转次数

    for i in range(n - k + 1):
        # 更新当前位置的翻转次数
        sum_flip += flip[i]
        
        # 如果当前牛面朝后，则需要翻转
        if (sum_flip % 2 == 0 and cows[i] == 'B') or (sum_flip % 2 == 1 and cows[i] == 'F'):
            res += 1
            flip[i] = 1  # 标记在位置i进行翻转
            sum_flip += 1  # 更新翻转次数
        
        # 移除超出窗口的翻转影响
        if i - k + 1 >= 0:
            sum_flip -= flip[i - k + 1]

    # 检查最后K-1头牛是否都面朝前
    for i in range(n - k + 1, n):
        sum_flip += flip[i]
        if (sum_flip % 2 == 0 and cows[i] == 'B') or (sum_flip % 2 == 1 and cows[i] == 'F'):
            return -1  # 无解
        if i - k + 1 >= 0:
            sum_flip -= flip[i - k + 1]

    return res


def main():
    global n
    n = int(input())

    # 读取牛的方向
    line = input().strip()
    for i in range(n):
        cows[i] = line[i]

    min_flips = n + 1
    best_k = 1

    # 枚举所有可能的K值
    for k in range(1, n + 1):
        flips = calculate(k)
        if flips != -1 and flips < min_flips:
            min_flips = flips
            best_k = k

    # 输出结果
    print(best_k, min_flips)


if __name__ == "__main__":
    main()
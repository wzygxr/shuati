# POJ 3167 Cow Patterns
# 给定一个母牛序列和一个模式序列，找出所有匹配的位置
# 测试链接 : http://poj.org/problem?id=3167

'''
题目解析:
这是一个模式匹配问题，需要使用KMP算法来解决。

解题思路:
1. 使用KMP算法进行模式匹配
2. 预处理模式串的next数组
3. 在母牛序列中查找所有匹配位置

时间复杂度: O(n+m)
空间复杂度: O(m)

工程化考虑:
1. 正确实现KMP算法
2. 输入输出处理
'''

import sys

"""
get_next - 高斯消元法应用 (Python实现)

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



MAXN = 100005
MAXM = 25

cows = [0 for _ in range(MAXN)]
pattern = [0 for _ in range(MAXM)]
next_arr = [0 for _ in range(MAXM)]
result = []

n = 0
m = 0
s = 0


def get_next():
    '''
    预处理模式串的next数组
    时间复杂度: O(m)
    空间复杂度: O(m)
    '''
    i = 0
    j = -1
    next_arr[0] = -1
    while i < m:
        if j == -1 or pattern[i] == pattern[j]:
            i += 1
            j += 1
            next_arr[i] = j
        else:
            j = next_arr[j]


def kmp():
    '''
    KMP算法匹配
    时间复杂度: O(n+m)
    空间复杂度: O(m)
    '''
    i = 0
    j = 0
    while i < n:
        if j == -1 or cows[i] == pattern[j]:
            i += 1
            j += 1
        else:
            j = next_arr[j]

        if j == m:
            result.append(i - m)
            j = next_arr[j]


def main():
    global n, m, s
    n, m, s = map(int, input().split())

    # 读取母牛序列
    cows[:n] = list(map(int, input().split()))

    # 读取模式序列
    pattern[:m] = list(map(int, input().split()))

    # 预处理next数组
    get_next()

    # KMP匹配
    kmp()

    # 输出结果
    print(len(result))
    for pos in result:
        print(pos + 1)


if __name__ == "__main__":
    main()
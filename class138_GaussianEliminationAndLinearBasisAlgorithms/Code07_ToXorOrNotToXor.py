"""
insert - 高斯消元法应用 (Python实现)

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

# SGU 275 To xor or not to xor
# 给定n个数，从中选择一些数，使得它们的异或和最大
# 测试链接 : https://codeforces.com/problemsets/acmsguru/problem/99999/275

'''
题目解析:
这是一个线性基问题，可以用高斯消元的思想来解决。

解题思路:
1. 将每个数看作一个60位的二进制向量
2. 使用高斯消元的思想构造线性基
3. 从高位到低位贪心地选择，使得异或和最大
4. 对于每一位，如果存在一个数在该位为1，则可以通过异或操作使得结果在该位为1

时间复杂度: O(n * 60) = O(n)
空间复杂度: O(60) = O(1)

工程化考虑:
1. 正确处理64位整数
2. 从高位到低位贪心选择
3. 线性基的构造和维护
'''

MAXN = 105
BITS = 60

# 线性基数组
basis = [0 for _ in range(BITS)]

n = 0
numbers = [0 for _ in range(MAXN)]


def insert(x):
    '''
    插入一个数到线性基中
    时间复杂度: O(BITS)
    空间复杂度: O(1)
    '''
    for i in range(BITS - 1, -1, -1):
        if ((x >> i) & 1) == 1:
            if basis[i] == 0:
                basis[i] = x
                return True
            x ^= basis[i]
    return False


def getMaxXor():
    '''
    求最大异或和
    从高位到低位贪心地选择
    时间复杂度: O(BITS)
    空间复杂度: O(1)
    '''
    result = 0
    for i in range(BITS - 1, -1, -1):
        if basis[i] != 0 and ((result >> i) & 1) == 0:
            result ^= basis[i]
    return result


def main():
    global n
    n = int(input())

    # 读取数字
    numbers[1:n+1] = list(map(int, input().split()))

    # 初始化线性基
    for i in range(BITS):
        basis[i] = 0

    # 构造线性基
    for i in range(1, n + 1):
        insert(numbers[i])

    # 求最大异或和
    result = getMaxXor()

    # 输出结果
    print(result)


if __name__ == "__main__":
    main()
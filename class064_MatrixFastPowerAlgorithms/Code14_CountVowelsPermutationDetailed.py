# -*- coding: utf-8 -*-

"""
LeetCode 1220. 统计元音字母序列的数目
题目链接: https://leetcode.cn/problems/count-vowels-permutation/
题目大意: 给你一个整数 n，请你帮忙统计一下我们可以按下述规则形成多少个长度为 n 的字符串：
         字符串中的每个字符都应当是小写元音字母（'a', 'e', 'i', 'o', 'u'）
         每个元音 'a' 后面都只能跟着 'e'
         每个元音 'e' 后面只能跟着 'a' 或者是 'i'
         每个元音 'i' 后面不能跟着另一个 'i'
         每个元音 'o' 后面只能跟着 'i' 或者是 'u'
         每个元音 'u' 后面只能跟着 'a'
解法: 使用矩阵快速幂求解
时间复杂度: O(logn)
空间复杂度: O(1)

数学分析:
1. 状态转移关系:
   a -> e
   e -> a, i
   i -> a, e, o, u
   o -> i, u
   u -> a
2. 转换为矩阵形式:
   [a']   [0 1 1 0 1] [a]
   [e']   [1 0 1 0 0] [e]
   [i'] = [0 1 0 1 0] [i]
   [o']   [0 0 1 0 0] [o]
   [u']   [0 0 1 1 0] [u]

优化思路:
1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
2. 注意模运算防止溢出

工程化考虑:
1. 边界条件处理: n=1的特殊情况
2. 输入验证: 检查n的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
2. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)
3. 最优性: 当n较大时，矩阵快速幂明显优于动态规划
"""

MOD = 1000000007


class Matrix:
    """
    矩阵类
    """
    def __init__(self):
        self.m = [[0 for _ in range(5)] for _ in range(5)]
    
    def __mul__(self, other):
        """
        矩阵乘法
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        res = Matrix()
        for i in range(5):
            for j in range(5):
                for k in range(5):
                    res.m[i][j] = (res.m[i][j] + self.m[i][k] * other.m[k][j]) % MOD
        return res


def identity_matrix():
    """
    构造单位矩阵
    时间复杂度: O(1)
    空间复杂度: O(1)
    """
    res = Matrix()
    for i in range(5):
        res.m[i][i] = 1
    return res


def matrix_power(base, exp):
    """
    矩阵快速幂
    时间复杂度: O(logn)
    空间复杂度: O(1)
    """
    res = identity_matrix()
    while exp > 0:
        if exp & 1:
            res = res * base
        base = base * base
        exp >>= 1
    return res


def count_vowel_permutation(n):
    """
    计算长度为n的元音字母序列数目
    时间复杂度: O(logn)
    空间复杂度: O(1)
    """
    # 特殊情况处理
    if n == 1:
        return 5
    
    # 转移矩阵
    base = Matrix()
    base.m[0][0] = 0
    base.m[0][1] = 1
    base.m[0][2] = 1
    base.m[0][3] = 0
    base.m[0][4] = 1  # a -> e, i, u
    
    base.m[1][0] = 1
    base.m[1][1] = 0
    base.m[1][2] = 1
    base.m[1][3] = 0
    base.m[1][4] = 0  # e -> a, i
    
    base.m[2][0] = 0
    base.m[2][1] = 1
    base.m[2][2] = 0
    base.m[2][3] = 1
    base.m[2][4] = 0  # i -> e, o
    
    base.m[3][0] = 0
    base.m[3][1] = 0
    base.m[3][2] = 1
    base.m[3][3] = 0
    base.m[3][4] = 0  # o -> i
    
    base.m[4][0] = 0
    base.m[4][1] = 0
    base.m[4][2] = 1
    base.m[4][3] = 1
    base.m[4][4] = 0  # u -> i, o
    
    # 计算转移矩阵的(n-1)次幂
    result = matrix_power(base, n - 1)
    
    # 初始状态向量 [1, 1, 1, 1, 1] (长度为1的序列)
    # 结果为 result * [1, 1, 1, 1, 1]^T 的所有元素之和
    sum_val = 0
    for i in range(5):
        for j in range(5):
            sum_val = (sum_val + result.m[i][j]) % MOD
    
    return sum_val


def main():
    """
    主函数
    """
    # 测试用例
    print(count_vowel_permutation(1))  # 5
    print(count_vowel_permutation(2))  # 10
    print(count_vowel_permutation(5))  # 68


# 为了兼容不同的运行环境，只有在直接运行此文件时才执行main函数
if __name__ == "__main__":
    main()
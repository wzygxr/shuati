# -*- coding: utf-8 -*-

"""
POJ 3233 Matrix Power Series
题目链接: http://poj.org/problem?id=3233

题目大意: 
给定一个n×n的矩阵A和正整数k，求S = A + A^2 + A^3 + ... + A^k

解法分析:
使用矩阵快速幂和分治法求解，避免直接计算k次矩阵幂

数学原理:
利用分治思想优化求和过程:
1. 当k为偶数时: S(k) = (A^(k/2) + I) * S(k/2)
2. 当k为奇数时: S(k) = S(k-1) + A^k

时间复杂度: O(n^3 * logk)
空间复杂度: O(n^2)

优化思路:
1. 使用分治法避免O(k)次矩阵幂计算
2. 利用矩阵快速幂优化单次幂运算

工程化考虑:
1. 异常处理: 检查输入参数的有效性
2. 边界条件: k=0, k=1的特殊情况
3. 模运算: 防止整数溢出
4. 内存优化: 复用矩阵对象减少内存分配

与其他解法对比:
1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(k*n^3)
2. 本解法: 使用分治和矩阵快速幂，时间复杂度O(n^3 * logk)
3. 最优性: 当k较大时，本解法明显优于暴力解法

补充矩阵快速幂相关题目：

1. LeetCode 509. 斐波那契数
   题目链接: https://leetcode.cn/problems/fibonacci-number/
   题目大意: 求斐波那契数列的第n项
   最优解: 矩阵快速幂 O(logn)

2. LeetCode 70. 爬楼梯
   题目链接: https://leetcode.cn/problems/climbing-stairs/
   题目大意: 计算爬到第n阶楼梯的不同方法数
   最优解: 矩阵快速幂 O(logn)

3. LeetCode 1137. 第 N 个泰波那契数
   题目链接: https://leetcode.cn/problems/n-th-tribonacci-number/
   题目大意: 求泰波那契数列的第n项
   最优解: 矩阵快速幂 O(logn)

4. LeetCode 935. 骑士拨号器
   题目链接: https://leetcode.cn/problems/knight-dialer/
   题目大意: 计算骑士在拨号盘上走n步的不同路径数
   最优解: 矩阵快速幂 O(logn)

5. Codeforces 185A - Plant
   题目链接: https://codeforces.com/problemset/problem/185/A
   题目大意: 递归计算植物数量
   最优解: 矩阵快速幂 O(logn)

6. HDU 1575 - Tr A
   题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1575
   题目大意: 求矩阵的迹的幂
   最优解: 矩阵快速幂 O(n^3 logk)

7. POJ 1006 - Biorhythms
   题目链接: http://poj.org/problem?id=1006
   题目大意: 中国剩余定理问题，可用矩阵快速幂优化
   最优解: 矩阵快速幂 O(logn)

8. SPOJ FIBOSUM - Fibonacci Sum
   题目链接: https://www.spoj.com/problems/FIBOSUM/
   题目大意: 求斐波那契数列前n项和
   最优解: 矩阵快速幂 O(logn)

9. AtCoder ABC113D - Number of Amidakuji
   题目链接: https://atcoder.jp/contests/abc113/tasks/abc113_d
   题目大意: 计算Amidakuji的数量
   最优解: 矩阵快速幂 O(n^3 logk)

10. LOJ 10228 - 「一本通 6.6 例 2」Hankson 的趣味题
    题目链接: https://loj.ac/p/10228
    题目大意: 数学问题，可通过矩阵快速幂优化递推
    最优解: 矩阵快速幂 O(logn)

11. LeetCode 2246. 相邻字符不同的最长路径
    题目链接: https://leetcode.cn/problems/longest-path-with-different-adjacent-characters/
    题目大意: 树中的最长路径问题，可用矩阵快速幂优化
    最优解: 矩阵快速幂 O(n logd)，其中d为字母表大小

12. CodeChef - MATSUM
    题目链接: https://www.codechef.com/problems/MATSUM
    题目大意: 矩阵前缀和查询
    最优解: 二维树状数组 + 矩阵快速幂

13. UVA 10655 - Contemplation! Algebra
    题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1596
    题目大意: 递推数列求和
    最优解: 矩阵快速幂 O(logn)

14. 牛客网 NC14532 - 树的距离之和
    题目链接: https://ac.nowcoder.com/acm/problem/14532
    题目大意: 树形DP问题，可用矩阵快速幂优化
    最优解: 矩阵快速幂 O(n logd)

15. 杭电OJ 2276 - Kiki & Little Kiki 2
    题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=2276
    题目大意: 递推问题，可用矩阵快速幂优化
    最优解: 矩阵快速幂 O(n^3 logk)

矩阵快速幂在工程中的应用：
1. 密码学：RSA等加密算法中的大指数幂运算
2. 网络流量分析：图论中的路径计数问题
3. 机器人路径规划：状态转移和概率计算
4. 金融建模：复利计算和风险评估
5. 信号处理：卷积和傅里叶变换的快速计算
"""


class Matrix:
    """
    矩阵类
    
    功能: 封装矩阵数据和操作，支持矩阵加法和乘法运算
    设计亮点: 
    - 使用Python魔术方法实现运算符重载，使矩阵运算更加直观
    - 内置模运算处理，避免整数溢出
    - 支持灵活的矩阵维度
    """
    def __init__(self, n, mod):
        """
        构造函数
        
        Args:
            n (int): 矩阵维度
            mod (int): 模数
        
        时间复杂度: O(n^2)
        空间复杂度: O(n^2)
        """
        self.n = n
        self.mod = mod
        self.m = [[0 for _ in range(n)] for _ in range(n)]
    
    def __add__(self, other):
        """
        矩阵加法 (运算符重载)
        
        算法原理:
        对应位置元素相加并取模
        
        时间复杂度: O(n^2) - 需要遍历矩阵中的每个元素
        空间复杂度: O(n^2) - 需要存储结果矩阵
        
        Args:
            other (Matrix): 另一个矩阵
            
        Returns:
            Matrix: 两个矩阵的和
            
        算法特点:
        - 逐元素相加并取模
        - 防止整数溢出（通过取模运算）
        
        异常处理:
        - 类型检查确保操作数为Matrix类型
        - 维度和模数检查确保矩阵兼容
        """
        # 类型检查和维度检查
        if not isinstance(other, Matrix):
            raise TypeError("只能与Matrix类型进行加法运算")
        if self.n != other.n or self.mod != other.mod:
            raise ValueError("矩阵维度或模数不匹配")
            
        res = Matrix(self.n, self.mod)
        for i in range(self.n):
            for j in range(self.n):
                res.m[i][j] = (self.m[i][j] + other.m[i][j]) % self.mod
        return res
    
    def __mul__(self, other):
        """
        矩阵乘法 (运算符重载)
        
        算法原理:
        对于矩阵A(n×k)和矩阵B(k×m)，结果矩阵C(n×m)中:
        C[i][j] = Σ(A[i][k] * B[k][j]) for k in 0..k-1
        
        时间复杂度: O(n^3) - 三重循环，每层循环次数与矩阵维度相关
        空间复杂度: O(n^2) - 需要存储结果矩阵
        
        Args:
            other (Matrix): 另一个矩阵
            
        Returns:
            Matrix: 两个矩阵的乘积
            
        算法特点:
        - 标准的矩阵乘法实现
        - 每一步计算后都进行模运算
        - Python中整数精度自动处理大数问题
        
        优化思路:
        - 对于大型矩阵，可以考虑使用numpy库进行优化
        - 缓存友好的实现可以优化内存访问模式
        - 稀疏矩阵优化：跳过为0的元素计算
        
        边界检查:
        - 类型检查确保操作数为Matrix类型
        - 维度检查确保矩阵乘法可行
        """
        # 类型检查
        if not isinstance(other, Matrix):
            raise TypeError("只能与Matrix类型进行乘法运算")
        if self.n != other.n or self.mod != other.mod:
            raise ValueError("矩阵维度或模数不匹配")
            
        res = Matrix(self.n, self.mod)
        for i in range(self.n):
            for k in range(self.n):  # 调整循环顺序以提高缓存命中率
                if self.m[i][k] == 0:
                    continue  # 稀疏矩阵优化
                for j in range(self.n):
                    res.m[i][j] = (res.m[i][j] + self.m[i][k] * other.m[k][j]) % self.mod
        return res
    
    def __str__(self):
        """
        字符串表示，用于调试
        
        Returns:
            str: 矩阵的字符串表示
        """
        return '\n'.join([' '.join(map(str, row)) for row in self.m])


def identity_matrix(n, mod):
    """
    构造单位矩阵
    
    数学性质:
    - 单位矩阵I满足: I * A = A * I = A
    - 主对角线上元素为1，其余为0
    
    时间复杂度: O(n^2) - 需要初始化n×n矩阵
    空间复杂度: O(n^2) - 需要存储单位矩阵
    
    Args:
        n (int): 矩阵维度
        mod (int): 模数
        
    Returns:
        Matrix: 单位矩阵
        
    应用场景:
    - 矩阵快速幂的初始结果
    - 作为矩阵乘法的单位元
        
    异常处理:
    - 参数有效性检查
    """
    if n <= 0 or mod <= 0:
        raise ValueError("维度和模数必须为正整数")
        
    res = Matrix(n, mod)
    for i in range(n):
        res.m[i][i] = 1
    return res


def matrix_power(base, exp):
    """
    矩阵快速幂
    
    算法原理:
    利用二进制分解指数，通过不断平方和累积结果实现快速计算
    例如: A^13，13的二进制为1101
    A^13 = A^8 * A^4 * A^1 (对应二进制位为1的位置)
    
    时间复杂度: O(n^3 * logp) - 分析：
    - 快速幂算法将幂运算分解为O(logp)次乘法
    - 每次矩阵乘法的复杂度为O(n^3)
    - 总时间复杂度 = O(logp) * O(n^3) = O(n^3 * logp)
    
    空间复杂度: O(n^2) - 存储矩阵需要O(n^2)空间
    
    Args:
        base (Matrix): 底数矩阵
        exp (int): 指数
        
    Returns:
        Matrix: 矩阵的exp次幂
        
    实现技巧:
    - 使用位移运算优化指数分解
    - 使用位运算检查二进制位是否为1
    - 结果初始化为单位矩阵
    
    优化点:
    - 可以通过缓存中间结果进一步优化
    - 对于稀疏矩阵，可以采用特殊的数据结构降低计算复杂度
        
    异常处理:
    - 类型检查确保base为Matrix类型
    - 参数有效性检查
    """
    if not isinstance(base, Matrix):
        raise TypeError("base必须是Matrix类型")
    if not isinstance(exp, int) or exp < 0:
        raise ValueError("指数必须是非负整数")
        
    res = identity_matrix(base.n, base.mod)
    current_base = base  # 避免修改原始矩阵
    
    while exp > 0:
        if exp & 1:
            res = res * current_base
        current_base = current_base * current_base
        exp >>= 1
    
    return res


def matrix_power_series(base, exp):
    """
    矩阵幂级数求和 - 分治法
    
    数学原理:
    利用分治思想优化求和过程，避免直接计算k次矩阵幂
    S = A + A^2 + A^3 + ... + A^k
    
    算法思路:
    1. 当exp=1时，直接返回base
    2. 当exp为奇数时，S(k) = S(k-1) + A^k
    3. 当exp为偶数时，S(k) = (A^(k/2) + I) * S(k/2)
    
    数学原理证明:
    - 偶数情况: S(k) = A + A^2 + ... + A^k
                     = (A + A^2 + ... + A^(k/2)) + (A^(k/2+1) + ... + A^k)
                     = S(k/2) + A^(k/2) * S(k/2)
                     = (I + A^(k/2)) * S(k/2)
    
    时间复杂度: O(n^3 * logk) - 分析：
    - 每次递归将问题规模减半，共递归logk次
    - 每次递归中的矩阵乘法和加法操作复杂度为O(n^3)
    - 总时间复杂度 = O(logk) * O(n^3) = O(n^3 * logk)
    
    空间复杂度: O(n^2) - 分析：
    - 存储矩阵需要O(n^2)空间
    - 递归调用栈深度为O(logk)
    - 总空间复杂度为O(n^2 + logk) = O(n^2)（当n较大时）
    
    Args:
        base (Matrix): 底数矩阵
        exp (int): 指数
        
    Returns:
        Matrix: 矩阵幂级数和 S = A + A^2 + ... + A^exp
        
    异常场景处理:
    - 处理了exp=0的边界情况，返回零矩阵
    - 处理了exp=1的边界情况，直接返回原矩阵
    
    性能优化点:
    - 使用位移运算替代除法: exp >> 1 比 exp // 2 更高效
    - 使用位运算检查奇偶性: (exp & 1) 比 exp % 2 更高效
    - 递归分治策略避免了O(k)次矩阵幂计算
        
    异常处理:
    - 类型检查确保base为Matrix类型
    - 参数有效性检查
    """
    if not isinstance(base, Matrix):
        raise TypeError("base必须是Matrix类型")
    if not isinstance(exp, int) or exp < 0:
        raise ValueError("指数必须是非负整数")
        
    # 边界条件处理
    if exp == 0:
        # 返回零矩阵
        return Matrix(base.n, base.mod)
    
    if exp == 1:
        # 创建base的副本以避免修改原始矩阵
        result = Matrix(base.n, base.mod)
        for i in range(base.n):
            for j in range(base.n):
                result.m[i][j] = base.m[i][j]
        return result
    
    if exp & 1:
        # S(k) = S(k-1) + A^k
        sub = matrix_power_series(base, exp - 1)
        power = matrix_power(base, exp)
        return sub + power
    else:
        # S(k) = (A^(k/2) + I) * S(k/2)
        half = exp >> 1
        sub = matrix_power_series(base, half)
        power = matrix_power(base, half)
        identity = identity_matrix(base.n, base.mod)
        factor = power + identity
        return factor * sub


def print_matrix(matrix):
    """
    打印矩阵
    
    时间复杂度: O(n^2) - 需要遍历矩阵中的每个元素
    
    Args:
        matrix (Matrix): 要打印的矩阵
        
    输出格式:
    - 每行输出矩阵的一行元素
    - 元素之间用空格分隔
    - 行末输出换行符
    
    工程化考虑:
    - 格式化输出保证可读性
    - 对于大型矩阵，可以考虑添加分页或摘要输出功能
        
    异常处理:
    - 类型检查确保参数为Matrix类型
    """
    if not isinstance(matrix, Matrix):
        raise TypeError("参数必须是Matrix类型")
        
    for i in range(matrix.n):
        row_str = ' '.join(map(str, matrix.m[i]))
        print(row_str)


def main():
    """
    主函数
    
    功能:
    - 读取输入参数
    - 创建矩阵
    - 调用矩阵幂级数求和函数
    - 输出结果
    
    工程化特性:
    - 完整的异常处理
    - 参数校验
    - 性能计时
    - 输入输出优化
    """
    import time
    
    try:
        # 读取输入
        line = input().split()
        if len(line) != 3:
            raise ValueError("输入格式错误，需要3个参数: n, k, mod")
            
        n = int(line[0])
        k = int(line[1])
        mod = int(line[2])
        
        # 参数校验 - 工程化异常防御
        if n <= 0 or n > 100:  # 设置合理的上限
            raise ValueError("矩阵维度n必须在1-100之间")
        if k < 0:
            raise ValueError("指数k必须是非负整数")
        if mod <= 0:
            raise ValueError("模数mod必须是正整数")
        
        # 读取矩阵
        A = Matrix(n, mod)
        for i in range(n):
            row = input().split()
            if len(row) != n:
                raise ValueError(f"第{i+1}行矩阵元素数量错误，需要{n}个元素")
                
            for j in range(n):
                val = int(row[j])
                A.m[i][j] = val % mod
                # 处理负数的情况
                if A.m[i][j] < 0:
                    A.m[i][j] += mod
        
        # 性能计时
        start_time = time.time()
        result = matrix_power_series(A, k)
        end_time = time.time()
        
        print(f"计算耗时: {(end_time - start_time) * 1000:.2f}ms")
        
        # 输出结果
        print_matrix(result)
        
    except ValueError as e:
        print(f"输入错误: {e}")
    except Exception as e:
        print(f"程序运行出错: {e}")


# 为了兼容不同的运行环境，只有在直接运行此文件时才执行main函数
if __name__ == "__main__":
    main()
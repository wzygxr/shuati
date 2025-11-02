#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
POJ 2947 Widget Factory - 工厂产品生产天数计算 (Python版本)
题目链接：http://poj.org/problem?id=2947

算法功能：
- 工厂生产n种产品，每种产品需要固定天数完成
- 工作日按周一到周日循环
- 给出m条生产记录，每条记录包含生产的各种产品数量和起止日期
- 求每种产品需要的天数（3-9天）

数学建模：
- 每条记录可以表示为一个模线性方程
- 例如：生产2个产品1和3个产品2，从周一到周三，表示为 2*x1 + 3*x2 ≡ 3 (mod 7)
- 这样就得到了一个模7线性方程组，可以用高斯消元法求解

时间复杂度分析：
- 时间复杂度：O(n³)
  - 高斯消元：O(n³)
  - 回代求解：O(n²)

空间复杂度分析：
- 空间复杂度：O(n²)，用于存储n×(n+1)的增广矩阵

解题要点：
1. 模线性方程组：处理周期性问题（7天一周）
2. 扩展欧几里得算法：求解模线性方程
3. 解的约束：每种产品需要3-9天
4. 解的情况判断：无解、多解、唯一解
"""

import sys
import math

# 最大支持的产品数和记录数 + 5，防止越界
MAXN = 305

# 增广矩阵，用于高斯消元求解模线性方程组
# mat[i][j]表示第i个方程中第j种产品的系数，mat[i][n+1]表示方程右边的常数项
mat = [[0 for _ in range(MAXN)] for _ in range(MAXN)]

# 结果数组，存储每种产品需要的天数
result = [0 for _ in range(MAXN)]

# 星期字符串到数字的映射
def get_day(day):
    """
    将星期字符串转换为数字
    :param day: 星期字符串（如"MON"）
    :return: 对应的数字（1-7）
    """
    day_map = {
        "MON": 1,
        "TUE": 2,
        "WED": 3,
        "THU": 4,
        "FRI": 5,
        "SAT": 6,
        "SUN": 7
    }
    return day_map.get(day, 0)


def exgcd(a, b):
    """
    扩展欧几里得算法
    求解 ax + by = gcd(a, b) 的整数解
    :param a: 系数a
    :param b: 系数b
    :return: 包含gcd和解的数组 [gcd, x, y]
    
    算法原理：
    1. 递归终止条件：当b=0时，gcd(a,0)=a，x=1，y=0
    2. 递归求解：gcd(b, a%b) = bx' + (a%b)y'
    3. 回代得到：ax + by = gcd(a,b)，其中x=y'，y=x'-(a/b)y'
    """
    if b == 0:
        return [a, 1, 0]  # gcd, x, y
    res = exgcd(b, a % b)
    gcd, x, y = res[0], res[2], res[1] - (a // b) * res[2]
    return [gcd, x, y]


def mod_linear_equation(a, b, n):
    """
    求解模线性方程 ax ≡ b (mod n)
    :param a: 系数a
    :param b: 等式右边
    :param n: 模数
    :return: 解，无解返回-1
    
    算法步骤：
    1. 使用扩展欧几里得算法求解 ax + ny = gcd(a,n)
    2. 检查方程是否有解：b必须能被gcd(a,n)整除
    3. 计算解：x = x' * (b/gcd) mod (n/gcd)
    """
    # 使用扩展欧几里得算法求解
    res = exgcd(a, n)
    gcd, x = res[0], res[1]
    
    # 检查方程是否有解
    if b % gcd != 0:
        return -1  # 无解
    
    # 计算解空间的模数
    mod = n // gcd
    # 计算最小正整数解
    sol = ((x * (b // gcd)) % mod + mod) % mod
    return sol


def gcd(a, b):
    """
    求两个数的最大公约数（欧几里得算法）
    :param a: 第一个数
    :param b: 第二个数
    :return: a和b的最大公约数
    """
    return a if b == 0 else gcd(b, a % b)


def gauss(n, m):
    """
    高斯消元法求解模线性方程组
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    
    模线性方程组形式：
    a11*x1 + a12*x2 + ... + a1n*xn ≡ b1 (mod 7)
    a21*x1 + a22*x2 + ... + a2n*xn ≡ b2 (mod 7)
    ...
    an1*x1 + an2*x2 + ... + ann*xn ≡ bn (mod 7)
    
    其中：
    - xi表示第i种产品需要的天数
    - aij表示第j个记录中第i种产品的数量
    - bi表示第j个记录的起止日期差+1（因为包含起止两天）
    
    :param n: 产品数
    :param m: 记录数
    :return: -1表示无解，0表示多解，1表示唯一解
    """
    row = 1
    
    # 前向消元过程
    # 对每一列进行处理
    for col in range(1, n + 1):
        if row > m:
            break
            
        # 寻找第col列中系数不为0的行，将其交换到第row行
        pivot_row = row
        for i in range(row, m + 1):
            if mat[i][col] != 0:
                pivot_row = i
                break
        
        # 如果找不到系数不为0的行，则继续处理下一列
        if mat[pivot_row][col] == 0:
            continue
        
        # 将找到的行与第row行交换
        if pivot_row != row:
            for j in range(1, n + 2):
                mat[row][j], mat[pivot_row][j] = mat[pivot_row][j], mat[row][j]
        
        # 用第row行消除其他行的第col列系数
        for i in range(1, m + 1):
            if i != row and mat[i][col] != 0:
                # 计算最小公倍数，用于消元
                lcm = mat[row][col] * mat[i][col] // gcd(abs(mat[row][col]), abs(mat[i][col]))
                rate1 = lcm // mat[row][col]
                rate2 = lcm // mat[i][col]
                
                # 对整行进行消元操作
                for j in range(1, n + 2):
                    # 执行行减法，然后取模
                    mat[i][j] = (mat[i][j] * rate2 - mat[row][j] * rate1) % 7
                    # 确保结果非负
                    if mat[i][j] < 0:
                        mat[i][j] += 7
        
        row += 1
    
    # 检查是否有矛盾方程（无解情况）
    for i in range(row, m + 1):
        if mat[i][n + 1] != 0:
            return -1  # 无解
    
    # 检查是否有无穷多解
    if row - 1 < n:
        return 0  # 有无穷多解
    
    # 回代求解过程
    for i in range(n, 0, -1):
        # 计算当前方程左边已知部分的和
        sum_val = mat[i][n + 1]
        for j in range(i + 1, n + 1):
            sum_val = (sum_val - mat[i][j] * result[j] % 7 + 7) % 7
        
        # 求解 mat[i][i] * result[i] ≡ sum (mod 7)
        sol = mod_linear_equation(mat[i][i], sum_val, 7)
        if sol == -1:
            return -1  # 无解
        result[i] = sol
        
        # 根据题目要求，解必须在[3, 9]范围内
        if result[i] < 3 or result[i] > 9:
            return -1  # 无解
    
    return 1  # 有唯一解


def main():
    """
    主函数：读取输入，构造方程组，求解并输出结果
    """
    global n, m
    
    while True:
        # 读取产品数n和记录数m
        line = sys.stdin.readline().strip()
        if not line:
            break
            
        n, m = map(int, line.split())
        
        # 当n和m都为0时结束程序
        if n == 0 and m == 0:
            break
        
        # 初始化矩阵为0
        for i in range(1, m + 1):
            for j in range(1, n + 2):
                mat[i][j] = 0
        
        # 读取m条生产记录
        for i in range(1, m + 1):
            # 读取本条记录中涉及的产品种类数
            k = int(sys.stdin.readline().strip())
            
            # 读取起止日期
            start_day = sys.stdin.readline().strip()
            end_day = sys.stdin.readline().strip()
            
            # 将星期转换为数字
            start = get_day(start_day)
            end = get_day(end_day)
            
            # 计算生产天数（包含起止两天）
            days = (end - start + 1 + 7) % 7
            if days == 0:
                days = 7
            # 设置方程右边的常数项
            mat[i][n + 1] = days
            
            # 读取涉及的产品种类
            products = list(map(int, sys.stdin.readline().split()))
            for product in products:
                # 累加该产品在本条记录中的数量（作为系数）
                mat[i][product] += 1
        
        # 使用高斯消元法求解模线性方程组
        res = gauss(n, m)
        
        # 输出结果
        if res == -1:
            # 无解情况
            print("Inconsistent data.")
        elif res == 0:
            # 多解情况
            print("Multiple solutions.")
        else:
            # 唯一解情况，输出每种产品需要的天数
            print(" ".join(str(result[i]) for i in range(1, n + 1)))


"""
注意事项与工程化考量：
1. 模运算处理：
   - 模7运算处理周期性问题
   - 负数取模时需要调整为正数
2. 解的约束检查：
   - 根据题目要求，每种产品需要3-9天
3. 多种解的情况处理：
   - 无解：存在矛盾方程
   - 多解：方程数少于未知数个数
   - 唯一解：方程组有唯一解且满足约束
4. 数值稳定性：
   - 使用整数运算避免浮点误差
   - 扩展欧几里得算法保证计算精度
5. 可扩展性改进：
   - 可以处理不同的周期（不是7天一周）
   - 可以增加更多的约束条件
6. 性能优化：
   - 对于稀疏矩阵可以使用特殊存储结构
   - 可以使用更高效的模运算算法
7. Python特性：
   - 使用列表推导式简化矩阵初始化
   - 使用字典映射简化星期转换
   - 使用生成器表达式优化字符串拼接
"""

if __name__ == "__main__":
    main()
#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
POJ 1222 EXTENDED LIGHTS OUT - 扩展版关灯问题
题目链接：http://poj.org/problem?id=1222

题目大意：
有一个5*6的灯阵，每个灯有一个开关，按下开关会改变自己和上下左右相邻灯的状态（开变关，关变开）
给出初始状态，求一种按开关的方案使得所有灯都关闭

算法思路：
该问题可以建模为异或线性方程组问题：
1. 每个灯的状态可以表示为一个变量（0或1）
2. 按下某个开关相当于在对应的方程中加入异或操作
3. 通过高斯消元法求解这个异或方程组

时间复杂度：O(n³)，其中n=30（5*6个灯泡）
空间复杂度：O(n²)

解题要点：
- 使用异或运算代替普通的加减乘除操作
- 方程组的构建需要考虑每个开关对周围灯的影响
- 异或方程组具有特殊性质，可以简化消元过程
"""

import sys

# 常量定义
MAXN = 35  # 最大变量数+1（5*6 + 1）
N_ROWS = 5  # 灯阵行数
N_COLS = 6  # 灯阵列数
N_LIGHTS = 30  # 灯的总数（5*6）

# 全局变量定义
# 增广矩阵，用于高斯消元求解异或方程组
# mat[i][j] 表示第i个方程中第j个变量的系数，mat[i][n+1]表示方程右边的常数项
mat = [[0 for _ in range(MAXN)] for _ in range(MAXN)]

# 结果数组，存储每个变量的解（是否按下开关）
result = [0 for _ in range(MAXN)]

n = 30  # 变量数量（灯泡数量）


def gauss():
    """
    高斯消元法求解异或方程组
    
    算法步骤：
    1. 前向消元：将增广矩阵转换为上三角矩阵
       - 对每一列寻找主元（系数为1的行）
       - 如果找到主元，交换到当前处理行
       - 用主元行消除其他行在该列的系数
    2. 回代求解：从最后一行开始，计算每个变量的值
    
    时间复杂度：O(n³)
    空间复杂度：O(n²)
    
    异或方程组形式：
    a11*x1 XOR a12*x2 XOR ... XOR a1n*xn = b1
    a21*x1 XOR a22*x2 XOR ... XOR a2n*xn = b2
    ...
    an1*x1 XOR an2*x2 XOR ... XOR ann*xn = bn
    
    其中：
    - xi表示第i个灯是否需要按下（1表示按下，0表示不按下）
    - aij表示按下第j个灯对第i个灯的影响（1表示有影响，0表示无影响）
    - bi表示第i个灯的期望状态变化（1表示需要改变，0表示不需要改变）
    """
    # 前向消元过程
    # 对每一列进行处理（从1到n）
    for i in range(1, n + 1):
        # 寻找第i列中系数为1的行，将其作为主元行
        row = i
        for j in range(i + 1, n + 1):
            if mat[j][i] == 1:
                row = j
                break

        # 如果找不到系数为1的行，则当前变量为自由变量，继续处理下一列
        if mat[row][i] == 0:
            continue

        # 将找到的主元行与当前处理行交换
        if row != i:
            for j in range(1, n + 2):  # 注意包括增广部分
                mat[i][j], mat[row][j] = mat[row][j], mat[i][j]

        # 用主元行消除其他所有行在第i列的系数
        for j in range(1, n + 1):
            # 跳过主元行本身，只处理那些在第i列系数为1的行
            if i != j and mat[j][i] == 1:
                # 异或操作实现行减法（在GF(2)中加法即异或）
                for k in range(1, n + 2):
                    mat[j][k] ^= mat[i][k]

    # 回代求解过程
    # 从最后一行开始往上计算每个变量的值
    for i in range(n, 0, -1):
        # 初始值为方程右边的常数项
        result[i] = mat[i][n + 1]
        # 减去（异或）已知变量的影响
        for j in range(i + 1, n + 1):
            # 只有当系数不为0时才需要异或
            result[i] ^= (mat[i][j] & result[j])



def get_id(x, y):
    """
    将二维坐标转换为一维编号
    
    Args:
        x: 行号（1-5）
        y: 列号（1-6）
    
    Returns:
        int: 对应的一维编号（1-30）
    """
    return (x - 1) * 6 + y


def main():
    """
    主函数：处理输入、构建方程组、求解并输出结果
    
    处理流程：
    1. 读取测试用例数量
    2. 对于每个测试用例：
       a. 初始化增广矩阵
       b. 读取初始灯的状态
       c. 构建系数矩阵，描述每个开关对其他灯的影响
       d. 使用高斯消元法求解异或方程组
       e. 输出结果
    """
    # 读取测试用例数量
    cases = int(sys.stdin.readline())
    
    # 处理每个测试用例
    for t in range(1, cases + 1):
        # 初始化矩阵，防止前一个测试用例的残留影响
        for i in range(1, n + 1):
            for j in range(1, n + 2):
                mat[i][j] = 0

        # 读取初始状态
        for i in range(1, N_ROWS + 1):
            line = list(map(int, sys.stdin.readline().split()))
            for j in range(1, N_COLS + 1):
                id = get_id(i, j)
                # 设置方程右边的常数项，表示灯的初始状态
                # 由于我们希望最终所有灯都关闭，所以初始为1的灯需要被改变状态
                mat[id][n + 1] = line[j - 1]

        # 构建系数矩阵
        # 对于每个灯，按下它会影响自己和相邻的灯
        for i in range(1, N_ROWS + 1):
            for j in range(1, N_COLS + 1):
                id = get_id(i, j)
                # 按下当前灯会影响自己
                mat[id][id] = 1

                # 按下当前灯会影响上方的灯
                if i > 1:
                    up_id = get_id(i - 1, j)
                    mat[up_id][id] = 1

                # 按下当前灯会影响下方的灯
                if i < N_ROWS:
                    down_id = get_id(i + 1, j)
                    mat[down_id][id] = 1

                # 按下当前灯会影响左方的灯
                if j > 1:
                    left_id = get_id(i, j - 1)
                    mat[left_id][id] = 1

                # 按下当前灯会影响右方的灯
                if j < N_COLS:
                    right_id = get_id(i, j + 1)
                    mat[right_id][id] = 1

        # 使用高斯消元法求解异或方程组
        gauss()

        # 输出结果
        print(f"PUZZLE #{t}")
        for i in range(1, N_ROWS + 1):
            line_output = []
            for j in range(1, N_COLS + 1):
                id = get_id(i, j)
                line_output.append(str(result[id]))
            print(" ".join(line_output))


"""
注意事项与工程化考量：

1. 位运算优化：
   - 异或操作(^)在Python中效率较高，适合处理这种0-1方程组
   - 对于更大规模的问题，可以考虑使用位掩码（如整数或bitset）来表示行，进一步提高效率

2. 内存优化：
   - 当前实现使用二维数组存储矩阵，空间复杂度为O(n²)
   - 对于稀疏矩阵，可以使用字典或稀疏矩阵库来减少内存占用

3. 数值稳定性：
   - 异或方程组不存在浮点数精度问题，数值稳定性很好
   - 不需要处理浮点数比较中的精度误差

4. 解的存在性：
   - 本题保证存在解，但在实际应用中需要处理无解或多解的情况
   - 当存在自由变量时，需要枚举自由变量的取值来找到所有可能的解

5. 代码优化建议：
   - 将全局变量改为函数内部变量或类成员变量，提高代码可维护性
   - 添加异常处理，应对非法输入
   - 使用更现代的Python语法（如列表推导式）简化矩阵初始化
   - 考虑使用numpy库进行更高效的矩阵运算

6. 测试与验证：
   - 添加单元测试，验证get_id函数和gauss函数的正确性
   - 测试边界情况，如全亮或全灭的初始状态

7. 可扩展性改进：
   - 将代码重构为可处理任意大小灯阵的函数
   - 添加参数控制输出格式和精度要求

该实现是解决开关问题的经典方法，适用于各类可以建模为异或方程组的场景，如灯光控制、电路设计、密码学等领域。
"""

if __name__ == "__main__":
    main()
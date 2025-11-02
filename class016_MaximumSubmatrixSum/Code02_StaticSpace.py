# ==================================================================================
# 题目2：子矩阵的最大累加和（ACM风格 - Python版本）
# ==================================================================================
# 题目来源：牛客网 (NowCoder)
# 题目链接：https://www.nowcoder.com/practice/cb82a97dcd0d48a7b1f4ee917e2c0409
#
# ACM风格 - 标准输入输出版本
#
# Python IO优化技巧：
# 1. 使用sys.stdin.readline代替input()
# 2. 使用sys.stdout.write代替print()（可选）
# 3. 一次性读取所有输入（适合小数据）
#
# ==================================================================================

import sys

# Python快速IO优化：使用sys.stdin.readline替代input()
input = sys.stdin.readline


def kadane(arr):
    """
    Kadane算法：求一维数组的最大子数组和
    
    参数:
        arr: List[int] - 一维数组
        
    返回:
        int/float - 最大子数组和
        
    时间复杂度: O(n) 其中n是数组长度
    空间复杂度: O(1)
    """
    max_sum = float('-inf')
    cur = 0
    
    for num in arr:
        cur += num
        max_sum = max(max_sum, cur)
        if cur < 0:
            cur = 0
    
    return max_sum


def solve(mat, n, m):
    """
    求最大子矩阵和
    
    参数:
        mat: List[List[int]] - 输入矩阵
        n: int - 行数
        m: int - 列数
        
    返回:
        int/float - 最大子矩阵和
        
    时间复杂度: O(n² × m)
    空间复杂度: O(m)
    """
    max_sum = float('-inf')
    
    # 枚举上边界
    for i in range(n):
        # 辅助数组，存储列压缩结果
        arr = [0] * m
        
        # 枚举下边界
        for j in range(i, n):
            # 将第j行压缩到arr
            for k in range(m):
                arr[k] += mat[j][k]
            
            # 对压缩数组应用Kadane算法
            max_sum = max(max_sum, kadane(arr))
    
    return int(max_sum) if max_sum != float('-inf') else 0


def main():
    """
    主函数：处理ACM风格的输入输出
    
    输入格式：
    - 多组测试数据，直到EOF
    - 每组第一行：n m（行数 列数）
    - 接下来n行，每行m个整数
    
    输出格式：
    - 对于每组测试数据，输出一行：最大子矩阵和
    """
    while True:
        try:
            # 读取n和m
            line = input().strip()
            if not line:
                break
            
            n, m = map(int, line.split())
            
            # 读取矩阵
            mat = []
            for _ in range(n):
                row = list(map(int, input().split()))
                mat.append(row)
            
            # 求解并输出
            result = solve(mat, n, m)
            print(result)
            
        except EOFError:
            break
        except:
            break


if __name__ == "__main__":
    main()


# ==================================================================================
# Python ACM输入输出模板总结
# ==================================================================================
#
# 模板1：单组数据
# ```python
# n, m = map(int, input().split())
# arr = list(map(int, input().split()))
# print(result)
# ```
#
# 模板2：多组数据（给定组数）
# ```python
# t = int(input())
# for _ in range(t):
#     n = int(input())
#     # 处理...
#     print(result)
# ```
#
# 模板3：多组数据（直到EOF）- 本题使用
# ```python
# while True:
#     try:
#         line = input().strip()
#         if not line:
#             break
#         # 处理...
#     except EOFError:
#         break
# ```
#
# 模板4：快速IO（极限优化）
# ```python
# import sys
# input = sys.stdin.readline  # 替换input函数
# 
# # 读取
# n = int(input())
# arr = list(map(int, input().split()))
# 
# # 输出
# sys.stdout.write(str(result) + '\n')
# ```
#
# ==================================================================================
# Python性能优化技巧
# ==================================================================================
#
# 1. 使用PyPy解释器
#    - PyPy比CPython快2-5倍
#    - 提交代码时选择PyPy3
#
# 2. 使用sys.stdin.readline
#    - 比input()快很多
#    - input = sys.stdin.readline
#
# 3. 使用列表推导式
#    - 比for循环快
#    - arr = [int(x) for x in input().split()]
#
# 4. 使用局部变量
#    - 局部变量比全局变量访问快
#    - 将频繁使用的函数赋值给局部变量
#
# 5. 避免重复计算
#    - 使用变量缓存结果
#    - 避免在循环内重复调用函数
#
# ==================================================================================
# Python vs Java vs C++ 性能对比
# ==================================================================================
#
# | 语言 | 运行速度 | 代码长度 | 学习难度 | 推荐指数 |
# |------|---------|---------|---------|---------|
# | C++ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
# | Java | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
# | Python | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
# | PyPy | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
#
# 注：使用PyPy可以大幅提升Python性能，使其接近Java
#
# ==================================================================================
# 测试用例
# ==================================================================================
#
# 输入：
# 3 3
# 1 2 3
# -4 5 -6
# 7 8 9
#
# 输出：
# 27
#
# 解释：选择整个矩阵，和为1+2+3-4+5-6+7+8+9=27
#
# ==================================================================================

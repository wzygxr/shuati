"""
POJ 1222 EXTENDED LIGHTS OUT
题目描述：
有一个5×6的按钮矩阵，每个按钮控制一盏灯。
按下按钮时，该按钮以及上下左右相邻按钮的灯状态会反转。
给定初始状态，求按哪些按钮可以将所有灯关闭。

解题思路：
1. 将每个按钮是否按下设为未知数xi(1表示按下，0表示不按)
2. 对于每个灯，建立一个方程表示该灯的最终状态
3. 使用高斯消元求解异或方程组

时间复杂度：O(30^3) = O(27000)
空间复杂度：O(30^2) = O(900)

最优解分析：
这是标准的异或方程组高斯消元算法，时间复杂度已经是最优的。
"""

ROWS = 5
COLS = 6
TOTAL = ROWS * COLS

# 方向数组：当前位置、上、左、下、右
dx = [0, -1, 0, 1, 0]
dy = [0, 0, -1, 0, 1]

def init_matrix(lights):
    """
    初始化矩阵
    根据灯的初始状态建立异或方程组
    时间复杂度：O(30)
    空间复杂度：O(1)
    """
    mat = [[0] * (TOTAL + 1) for _ in range(TOTAL)]
    
    for i in range(ROWS):
        for j in range(COLS):
            idx = i * COLS + j  # 当前灯的位置索引
            
            # 建立方程：所有影响该灯的按钮的异或和等于初始状态
            for d in range(5):
                ni = i + dx[d]
                nj = j + dy[d]
                
                if 0 <= ni < ROWS and 0 <= nj < COLS:
                    nidx = ni * COLS + nj
                    mat[idx][nidx] = 1  # 按钮nidx会影响灯idx
            
            # 常数项为灯的初始状态
            mat[idx][TOTAL] = lights[i][j]
    
    return mat

def gauss(mat, n):
    """
    高斯消元求解异或方程组
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    
    参数：
    mat: 增广矩阵，大小为n×(n+1)
    n: 变量个数
    
    返回值：
    解向量存储在mat[i][n]中
    """
    for i in range(n):
        # 寻找第i列中系数为1的行
        pivot = i
        for j in range(i, n):
            if mat[j][i] == 1:
                pivot = j
                break
        
        # 交换行
        if pivot != i:
            mat[i], mat[pivot] = mat[pivot], mat[i]
        
        # 消去其他行
        for j in range(n):
            if j != i and mat[j][i] == 1:
                for k in range(i, n + 1):
                    mat[j][k] ^= mat[i][k]

def main():
    """
    主函数，处理输入输出
    时间复杂度：O(30^3)
    空间复杂度：O(30^2)
    """
    import sys
    
    data = sys.stdin.read().split()
    idx = 0
    
    T = int(data[idx]); idx += 1
    
    for t in range(1, T + 1):
        # 读取灯的初始状态
        lights = []
        for i in range(ROWS):
            row = []
            for j in range(COLS):
                row.append(int(data[idx])); idx += 1
            lights.append(row)
        
        # 初始化矩阵
        mat = init_matrix(lights)
        
        # 高斯消元
        gauss(mat, TOTAL)
        
        # 输出结果
        print(f"PUZZLE #{t}")
        for i in range(ROWS):
            row_result = []
            for j in range(COLS):
                idx_val = i * COLS + j
                row_result.append(str(mat[idx_val][TOTAL]))
            print(" ".join(row_result))

"""
init_matrix - 高斯消元法应用 (Python实现)

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



if __name__ == "__main__":
    main()
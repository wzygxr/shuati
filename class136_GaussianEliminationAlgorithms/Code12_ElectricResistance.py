# HDU 3976 Electric resistance
# 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3976
# 题目大意：给定一个电路图，求节点1和节点n之间的等效电阻
# 电路中每个电阻都是1欧姆，根据基尔霍夫定律建立线性方程组求解
#
# 解题思路：
# 1. 根据基尔霍夫电流定律建立线性方程组
# 2. 对于每个节点，流入的电流等于流出的电流
# 3. 对于每个电阻，根据欧姆定律计算电流
# 4. 通过高斯消元法求解线性方程组，得到各节点电势
# 5. 根据欧姆定律计算等效电阻

import sys

MAXN = 105

# 增广矩阵，用于高斯消元求解线性方程组
# mat[i][j] 表示第i个方程中第j个变量的系数
# mat[i][n+1] 表示第i个方程的常数项
mat = [[0.0 for _ in range(MAXN)] for _ in range(MAXN)]

# 节点数量和电阻数量
n = 0
m = 0

# 0.0000001 == 1e-7
# 因为double类型有精度问题，所以认为
# 如果一个数字绝对值 <  sml，则认为该数字是0
# 如果一个数字绝对值 >= sml，则认为该数字不是0
sml = 1e-7


def gauss(n):
    """
    高斯消元法求解线性方程组
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    
    数学原理：
    线性方程组形式：
    a11*x1 + a12*x2 + ... + a1n*xn = b1
    a21*x1 + a22*x2 + ... + a2n*xn = b2
    ...
    an1*x1 + an2*x2 + ... + ann*xn = bn
    
    其中：
    - xi 表示第i个节点的电势
    - aij 和 bi 根据基尔霍夫电流定律建立
    
    算法步骤：
    1. 对于每一行i，选择主元（绝对值最大的元素）
    2. 将主元所在的行与第i行交换
    3. 将第i行的主元系数化为1
    4. 用第i行消除其他所有行的第i列系数
    
    :param n: 节点数量
    """
    for i in range(1, n + 1):
        # 选择主元，找到第i列中绝对值最大的元素所在的行
        max_row = i
        for j in range(i + 1, n + 1):
            if abs(mat[j][i]) > abs(mat[max_row][i]):
                max_row = j
        
        # 交换行，将主元所在的行与第i行交换
        mat[i], mat[max_row] = mat[max_row], mat[i]
        
        # 如果主元的绝对值小于sml，认为是0，继续处理下一行
        if abs(mat[i][i]) < sml:
            continue
        
        # 将第i行的主元系数化为1
        tmp = mat[i][i]
        for j in range(i, n + 2):
            mat[i][j] /= tmp
        
        # 用第i行消除其他所有行的第i列系数
        for j in range(1, n + 1):
            if i != j:
                rate = mat[j][i] / mat[i][i]
                for k in range(i, n + 2):
                    mat[j][k] -= mat[i][k] * rate


def main():
    """
    主函数
    读取输入数据，构建系数矩阵，调用高斯消元法求解，输出结果
    
    算法流程：
    1. 读取测试用例数量
    2. 对于每个测试用例：
       a. 读取节点数量n和电阻数量m
       b. 初始化增广矩阵
       c. 根据基尔霍夫电流定律建立方程组
       d. 设置边界条件（节点1电势为1，节点n电势为0）
       e. 使用高斯消元法求解
       f. 计算等效电阻并输出结果
    """
    global n, m
    
    cases = int(sys.stdin.readline().strip())
    
    for t in range(1, cases + 1):
        line = sys.stdin.readline().strip().split()
        n = int(line[0])
        m = int(line[1])
        
        # 初始化矩阵，将所有元素置为0
        for i in range(1, n + 1):
            for j in range(1, n + 2):
                mat[i][j] = 0.0
        
        # 建立方程组
        # 根据基尔霍夫电流定律：流入节点的电流等于流出节点的电流
        # 对于每个电阻，假设电流从电势高的节点流向电势低的节点
        for i in range(1, m + 1):
            line = sys.stdin.readline().strip().split()
            u = int(line[0])
            v = int(line[1])
            
            # 在节点u和v之间增加一个1欧姆的电阻
            # 根据欧姆定律：I = (Vu - Vv) / R = Vu - Vv（R=1欧姆）
            # 对于节点u：增加流入电流 (Vu - Vv)
            # 对于节点v：增加流出电流 (Vu - Vv)
            mat[u][u] += 1.0  # 节点u的自电导增加
            mat[u][v] -= 1.0  # 节点u到v的互电导
            mat[v][v] += 1.0  # 节点v的自电导增加
            mat[v][u] -= 1.0  # 节点v到u的互电导
        
        # 边界条件
        # 假设节点1的电势为1，节点n的电势为0
        # 这样电流就是1安培，等效电阻数值上等于电压差
        mat[1][n + 1] = 1.0  # 节点1的电势为1
        mat[n][n + 1] = 0.0  # 节点n的电势为0
        
        # 修改矩阵使边界条件生效
        # 将节点1和n的方程替换为边界条件
        for i in range(1, n + 1):
            mat[1][i] = 0.0
            mat[n][i] = 0.0
        mat[1][1] = 1.0
        mat[n][n] = 1.0
        
        # 使用高斯消元法求解线性方程组
        gauss(n)
        
        # 计算等效电阻
        # 根据欧姆定律：R = V / I
        # 我们设定电流为1安培，所以等效电阻数值上等于电压差
        voltage1 = mat[1][n + 1]
        voltagen = mat[n][n + 1]
        resistance = voltage1 - voltagen
        
        # 输出结果，保留两位小数
        print(f"{resistance:.2f}")


if __name__ == "__main__":
    main()
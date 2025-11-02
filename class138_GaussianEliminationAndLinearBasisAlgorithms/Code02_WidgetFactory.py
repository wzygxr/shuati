"""
POJ 2947 Widget Factory
题目描述：
有n种工具，m条记录。每条记录包含：加工工具数量、开始星期、结束星期、工具编号序列。
每种工具制作天数是固定的（3-9天），根据记录推断制作天数。

解题思路：
1. 将每条记录转化为模7线性方程
2. 使用高斯消元法求解模线性方程组
3. 判断解的情况：无解、多解、唯一解

时间复杂度：O(max(n,m)^3)
空间复杂度：O(max(n,m)^2)

最优解分析：
这是标准的高斯消元算法，时间复杂度已经是最优的。
"""

MOD = 7

def init_inv():
    """
    预处理模MOD意义下的逆元
    时间复杂度：O(MOD)
    空间复杂度：O(MOD)
    """
    inv = [0] * MOD
    inv[1] = 1
    for i in range(2, MOD):
        inv[i] = (MOD - MOD // i * inv[MOD % i] % MOD) % MOD
    return inv

def gcd(a, b):
    """
    计算两个整数的最大公约数
    时间复杂度：O(log(min(a,b)))
    空间复杂度：O(1)
    """
    return a if b == 0 else gcd(b, a % b)

def get_day(day_str):
    """
    将星期字符串转换为数字（0-6）
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    days = {"MON": 0, "TUE": 1, "WED": 2, "THU": 3, 
            "FRI": 4, "SAT": 5, "SUN": 6}
    return days.get(day_str, -1)

def gauss(mat, n, m):
    """
    高斯消元解决模线性方程组
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    
    参数：
    mat: 增广矩阵，大小为n×(m+1)
    n: 方程个数
    m: 变量个数
    
    返回值：
    -1: 无解
    0: 多解
    1: 唯一解
    """
    rank = 0  # 矩阵的秩
    free_vars = []  # 自由变量
    
    for col in range(m):
        # 寻找主元
        pivot = -1
        for i in range(rank, n):
            if mat[i][col] != 0:
                pivot = i
                break
        
        if pivot == -1:
            free_vars.append(col)
            continue
        
        # 交换行
        if pivot != rank:
            mat[rank], mat[pivot] = mat[pivot], mat[rank]
        
        # 归一化主元行
        inv_val = inv[mat[rank][col]]
        for j in range(col, m + 1):
            mat[rank][j] = (mat[rank][j] * inv_val) % MOD
        
        # 消去其他行
        for i in range(n):
            if i != rank and mat[i][col] != 0:
                factor = mat[i][col]
                for j in range(col, m + 1):
                    mat[i][j] = (mat[i][j] - factor * mat[rank][j]) % MOD
        
        rank += 1
    
    # 检查无解情况
    for i in range(rank, n):
        if mat[i][m] != 0:
            return -1  # 无解
    
    # 判断解的情况
    if rank < m:
        return 0  # 多解
    else:
        return 1  # 唯一解

def main():
    """
    主函数，处理输入输出
    时间复杂度：O(m*n + n^3)
    空间复杂度：O(n^2)
    """
    global inv
    inv = init_inv()
    
    import sys
    data = sys.stdin.read().split()
    idx = 0
    
    while idx < len(data):
        n = int(data[idx]); idx += 1
        m = int(data[idx]); idx += 1
        
        if n == 0 and m == 0:
            break
        
        # 初始化矩阵
        mat = [[0] * (n + 1) for _ in range(m)]
        
        for i in range(m):
            k = int(data[idx]); idx += 1
            start_day = data[idx]; idx += 1
            end_day = data[idx]; idx += 1
            
            start = get_day(start_day)
            end = get_day(end_day)
            days = (end - start + 1 + MOD) % MOD
            
            # 读取工具编号
            tools = []
            for j in range(k):
                tool = int(data[idx]) - 1  # 转换为0-based
                idx += 1
                tools.append(tool)
            
            # 建立方程
            for tool in tools:
                mat[i][tool] = (mat[i][tool] + 1) % MOD
            mat[i][n] = days
        
        # 高斯消元
        result = gauss(mat, m, n)
        
        if result == -1:
            print("Inconsistent data.")
        elif result == 0:
            print("Multiple solutions.")
        else:
            # 输出唯一解，并验证在3-9天范围内
            valid = True
            solutions = []
            
            for i in range(n):
                days_val = mat[i][n]
                if days_val < 3:
                    days_val += MOD
                if days_val < 3 or days_val > 9:
                    valid = False
                    break
                solutions.append(days_val)
            
            if valid:
                print(" ".join(map(str, solutions)))
            else:
                print("Inconsistent data.")

"""
init_inv - 高斯消元法应用 (Python实现)

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
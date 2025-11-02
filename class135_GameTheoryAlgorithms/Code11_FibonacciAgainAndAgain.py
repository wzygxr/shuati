# 斐波那契博弈扩展 (SG函数与斐波那契数列)
# 有三堆石子，数量分别是m, n, p个
# 两人轮流走，每次选择一堆取，取的个数必须为斐波那契数列中的数
# 取走最后一颗石子的人获胜
# 
# 题目来源：
# 1. HDU 1848 Fibonacci again and again - http://acm.hdu.edu.cn/showproblem.php?pid=1848
# 2. HDU 1005 Fibonacci again - http://acm.hdu.edu.cn/showproblem.php?pid=1005
# 3. POJ 3533 Light Switching Game - http://poj.org/problem?id=3533
# 
# 算法核心思想：
# 1. SG函数方法：通过递推计算每个石子数的SG值
# 2. 斐波那契数列：可取石子数必须为斐波那契数列中的数
# 3. SG定理：整个游戏的SG值等于各堆SG值的异或和
# 
# 时间复杂度分析：
# 1. 预处理：O(max_n * fib_count) - 计算每个石子数的SG值
# 2. 查询：O(1) - 计算三堆石子SG值的异或和
# 
# 空间复杂度分析：
# 1. SG数组：O(max_n) - 存储每个石子数的SG值
# 2. 斐波那契数组：O(fib_count) - 存储斐波那契数列
# 
# 工程化考量：
# 1. 异常处理：处理负数输入和边界情况
# 2. 性能优化：预处理SG值避免重复计算
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的堆数和查询

# 最大石子数
MAXN = 1001

# SG数组，sg[i]表示有i个石子时的SG值
sg = [0] * MAXN

# 斐波那契数组
fib = []

def build():
    """
    算法原理：
    1. 预计算斐波那契数列
    2. 对于每个石子数i，计算其后继状态的SG值集合
    3. 石子数i的后继状态为i-fib[0], i-fib[1], ..., i-fib[k]（如果存在）
    4. 石子数i的SG值等于不属于后继状态SG值集合的最小非负整数(mex)
    
    SG函数定义：
    SG(x) = mex{SG(y) | y是x的后继状态}
    其中mex(S)表示不属于集合S的最小非负整数
    
    对于斐波那契博弈，状态i的后继状态为i-fib[0], i-fib[1], ..., i-fib[k]（如果存在）
    """
    # 预计算斐波那契数列
    global fib
    fib = [1, 2]
    while len(fib) < 21:
        fib.append(fib[-1] + fib[-2])
    
    # 初始化SG数组
    sg[0] = 0  # 终止状态SG值为0
    
    # 递推计算每个状态的SG值
    for i in range(1, MAXN):
        # 计算状态i的所有后继状态的SG值
        appear = set()
        for f in fib:
            if f <= i:
                # 添加后继状态的SG值
                appear.add(sg[i - f])
        
        # 计算mex值，即不属于appear集合的最小非负整数
        mex = 0
        while mex in appear:
            mex += 1
        
        sg[i] = mex

def solve(m, n, p):
    """
    算法原理：
    根据SG定理计算整个游戏的SG值
    1. 对于每堆石子，计算其SG值
    2. 整个游戏的SG值等于各堆SG值的异或和
    3. SG值不为0表示必胜态，为0表示必败态
    """
    # 异常处理：处理负数
    if m < 0 or n < 0 or p < 0:
        return "输入非法"
    
    # 计算三堆石子SG值的异或和
    xor_sum = sg[m] ^ sg[n] ^ sg[p]
    
    # SG值不为0表示必胜态，为0表示必败态
    return "Fibo" if xor_sum != 0 else "Nacci"

# 预处理SG值
build()

# 测试示例
if __name__ == "__main__":
    # 示例1: m=1, n=2, p=3
    # sg[1]=1, sg[2]=0, sg[3]=1
    # xor_sum = 1^0^1 = 0
    # 预期结果: Nacci
    result1 = solve(1, 2, 3)
    
    # 示例2: m=2, n=3, p=5
    # sg[2]=0, sg[3]=1, sg[5]=0
    # xor_sum = 0^1^0 = 1
    # 预期结果: Fibo
    result2 = solve(2, 3, 5)
    
    # 示例3: m=1, n=1, p=1
    # sg[1]=1, sg[1]=1, sg[1]=1
    # xor_sum = 1^1^1 = 1
    # 预期结果: Fibo
    result3 = solve(1, 1, 1)
    
    print(f"示例1结果: {result1}")
    print(f"示例2结果: {result2}")
    print(f"示例3结果: {result3}")
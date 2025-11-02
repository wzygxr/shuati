# -*- coding: utf-8 -*-

# 使用Dinkelbach算法解决01分数规划问题
# 给定n个物品，每个物品有两个属性a[i]和b[i]
# 选择一些物品使得选中物品的a值和与b值和的比值最大
# 1 <= n <= 100000
# 1 <= a[i], b[i] <= 100

import sys

# 常量定义
MAXN = 100001

# a[i]表示选取i的收益
a = [0] * MAXN

# b[i]表示选取i的代价
b = [0] * MAXN

# d[i] = a[i] - L * b[i]，其中L为当前比率值
d = [0.0] * MAXN

n = 0

# 使用Dinkelbach算法求解01分数规划
def dinkelbach():
    """使用Dinkelbach算法求解01分数规划"""
    L = 0.0  # 初始比率值
    epsilon = 1e-9  # 精度要求
    
    while True:
        # 根据当前比率值L计算d数组
        for i in range(1, n + 1):
            d[i] = a[i] - L * b[i]
        
        # 贪心选择d[i] > 0的物品，使得sum(d[i] * x[i])最大
        sumD = 0.0
        sumA = 0.0
        sumB = 0.0
        
        for i in range(1, n + 1):
            if d[i] > 0:  # 选择d[i] > 0的物品
                sumD += d[i]
                sumA += a[i]
                sumB += b[i]
        
        # 如果sumD <= 0，说明已经找到最优解
        if sumD <= 0:
            return L
        
        # 更新比率值
        newL = sumA / sumB if sumB > 0 else 0
        
        # 如果新旧比率值差小于精度要求，则停止迭代
        if abs(newL - L) < epsilon:
            return newL
        
        L = newL

def main():
    """主函数"""
    global n
    
    # 读取输入
    line = sys.stdin.readline().split()
    n = int(line[0])
    
    line = sys.stdin.readline().split()
    for i in range(1, n + 1):
        a[i] = int(line[i - 1])
    
    line = sys.stdin.readline().split()
    for i in range(1, n + 1):
        b[i] = int(line[i - 1])
    
    result = dinkelbach()
    print("%.6f" % result)

if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 1006 Biorhythms
链接：http://poj.org/problem?id=1006
题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，
          求下一次三个指标同时达到峰值的天数

算法思路：
这是一个标准的中国剩余定理应用题。三个生理周期分别为23、28、33天，它们两两互质，
可以直接应用中国剩余定理。

设从出生开始的第x天，三个指标分别为p、e、i，要求从第d天开始，下一个三个指标同时达到峰值的天数。
体力周期：x ≡ p (mod 23)
情感周期：x ≡ e (mod 28)
智力周期：x ≡ i (mod 33)

解法步骤：
1. 使用中国剩余定理求解同余方程组
2. 计算结果与d的差值，确保是下一个峰值

算法原理：
这是一个经典的中国剩余定理应用题，展示了CRT在实际问题中的应用。
由于23、28、33两两互质，可以直接使用CRT求解。

时间复杂度：O(1)，因为模数固定
空间复杂度：O(1)

适用场景：
1. 生物节律计算
2. 周期性事件预测
3. 调度问题

注意事项：
1. 23、28、33两两互质，可以直接使用CRT
2. 需要处理特殊情况，如当天就是峰值
3. 注意结果必须大于d

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理无解的情况
3. 边界处理：处理d=0等特殊情况

与其他算法的关联：
1. 中国剩余定理：核心算法
2. 最小公倍数：用于验证周期

实际应用：
1. 生物节律分析
2. 周期性事件预测
3. 资源调度

相关题目：
1. UVA 756 Biorhythms
   链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
   题目大意：与POJ 1006相同

2. 51Nod 1079 - 中国剩余定理
   链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
   题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K

3. 洛谷 P1495 - 曹冲养猪
   链接：https://www.luogu.com.cn/problem/P1495
   题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
"""

# 生理周期
PHYSICAL_CYCLE = 23
EMOTIONAL_CYCLE = 28
INTELLECTUAL_CYCLE = 33

# 周期的最小公倍数
LCM = 21252  # lcm(23, 28, 33) = 21252

# 扩展欧几里得算法
def exgcd(a, b):
    if b == 0:
        return a, 1, 0
    else:
        d, x, y = exgcd(b, a % b)
        return d, y, x - (a // b) * y

# 求逆元
def mod_inverse(a, m):
    d, x, y = exgcd(a, m)
    if d != 1:
        return None  # 逆元不存在
    else:
        return (x % m + m) % m

# 中国剩余定理求解
def crt(p, e, i):
    """
    中国剩余定理求解生物节律问题
    :param p: 体力峰值日
    :param e: 情感峰值日
    :param i: 智力峰值日
    :return: 三个指标同时达到峰值的日期
    """
    # M = 23 * 28 * 33 = 21252
    M = LCM
    
    # M1 = M / 23 = 924
    M1 = M // PHYSICAL_CYCLE
    # M2 = M / 28 = 759
    M2 = M // EMOTIONAL_CYCLE
    # M3 = M / 33 = 644
    M3 = M // INTELLECTUAL_CYCLE
    
    # 求逆元
    inv_M1 = mod_inverse(M1, PHYSICAL_CYCLE)
    inv_M2 = mod_inverse(M2, EMOTIONAL_CYCLE)
    inv_M3 = mod_inverse(M3, INTELLECTUAL_CYCLE)
    
    if inv_M1 is None or inv_M2 is None or inv_M3 is None:
        return -1  # 无解
    
    # 计算解
    x = (p * M1 * inv_M1 + e * M2 * inv_M2 + i * M3 * inv_M3) % M
    
    return x

def main():
    case_num = 1
    while True:
        line = input().strip()
        p, e, i, d = map(int, line.split())
        
        # 结束条件
        if p == -1 and e == -1 and i == -1 and d == -1:
            break
        
        # 使用中国剩余定理求解
        x = crt(p, e, i)
        
        # 计算下一个峰值日期
        days = (x - d + LCM) % LCM
        if days == 0:
            days = LCM
        
        print(f"Case {case_num}: the next triple peak occurs in {days} days.")
        case_num += 1

if __name__ == "__main__":
    main()
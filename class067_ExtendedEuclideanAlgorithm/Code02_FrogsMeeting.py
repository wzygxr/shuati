# 青蛙的约会
# 有一个周长为l的环，从环的0位置开始，规定只能沿着顺时针方向不停转圈
# 青蛙A在环的x1位置，每秒跳m个单位，青蛙B在x2位置，每秒跳n个单位
# 只有在某时刻，青蛙A和青蛙B来到环的同一个位置，才算相遇
# 如果两只青蛙相遇不了，打印"Impossible"
# 如果可以相遇，打印两只青蛙至少多久才能相遇
# 1 <= l <= 3 * 10^9
# 1 <= x1、x2、m、n <= 2 * 10^9
# x1 != x2
# 测试链接 : https://www.luogu.com.cn/problem/P1516

import sys
import math

# 全局变量
d, x, y, px, py = 0, 0, 0, 0, 0

def exgcd(a, b):
    """
    扩展欧几里得算法
    求解方程ax + by = gcd(a,b)的一组特解
    
    算法原理：
    当b=0时，gcd(a,b)=a，此时x=1,y=0
    当b≠0时，递归计算gcd(b,a%b)的解，然后根据推导公式得到原方程的解
    
    时间复杂度：O(log(min(a,b)))
    空间复杂度：O(log(min(a,b)))，递归调用栈
    
    Args:
        a: 系数a
        b: 系数b
    
    Returns:
        (d, x, y): d=gcd(a,b), x和y是方程ax + by = gcd(a,b)的一组特解
    """
    global d, x, y, px, py
    if b == 0:
        d = a
        x = 1
        y = 0
    else:
        exgcd(b, a % b)
        px = x
        py = y
        x = py
        y = px - py * (a // b)

def main():
    """
    主函数
    
    问题描述：
    有两只青蛙A和B在一个圆环上，给定它们的初始位置和跳跃速度，求它们何时能相遇
    
    解题思路：
    1. 建立方程：设t秒后相遇，则有 (x1 + m*t) ≡ (x2 + n*t) (mod l)
    2. 化简方程：(m-n)*t ≡ (x2-x1) (mod l)
    3. 转换为线性丢番图方程：(m-n)*t + l*k = (x2-x1)
    4. 使用扩展欧几里得算法求解
    
    数学原理：
    1. 同余方程：ax ≡ b (mod m) 等价于 ax + my = b
    2. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
    3. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
    
    时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
    空间复杂度：O(1)
    
    相关题目：
    1. 洛谷 P1516 青蛙的约会
       链接：https://www.luogu.com.cn/problem/P1516
       这是本题的来源，是一道经典题
    
    2. POJ 1061 青蛙的约会
       链接：http://poj.org/problem?id=1061
       与本题完全相同，是POJ上的经典题目
    
    3. HDU 5512 Pagodas
       链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
       本题涉及数论知识，与最大公约数有关
    
    4. POJ 2115 C Looooops
       链接：http://poj.org/problem?id=2115
       本题需要求解模线性方程，可以转化为线性丢番图方程
    
    5. Codeforces 1244C. The Football Stage
       链接：https://codeforces.com/problemset/problem/1244/C
       本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量
    
    工程化考虑：
    1. 异常处理：需要处理输入非法、方程无解等情况
    2. 边界条件：需要考虑各参数为边界值的情况
    3. 性能优化：对于大数据，要注意算法的时间复杂度
    4. 可读性：添加详细注释，变量命名清晰
    
    算法要点：
    1. 同余方程的转化是解决此类问题的关键
    2. 扩展欧几里得算法是解决线性丢番图方程的核心
    3. 裴蜀定理是判断方程是否有解的依据
    4. 对于最小正整数解，需要通过调整特解来找到满足条件的解
    """
    # 读取输入
    line = sys.stdin.readline().strip().split()
    x1 = int(line[0])
    x2 = int(line[1])
    m = int(line[2])
    n = int(line[3])
    l = int(line[4])
    
    # 计算参数
    a, c = 0, 0
    if x1 < x2:
        a = m - n
        c = x2 - x1
    else:
        a = n - m
        c = x1 - x2
    
    if a < 0:
        a = -a
        c = l - c
    
    # 使用扩展欧几里得算法求解
    exgcd(a, l)
    
    # 判断方程是否有解
    if c % d != 0:
        print("Impossible")
    else:
        # 解出的特解
        x0 = x * c // d
        # 单次幅度
        xd = l // d
        # x0调整成>=1的最小正整数，处理办法和上一题一样
        if x0 < 0:
            x0 += ((1 - x0 + xd - 1) // xd) * xd
        else:
            x0 -= ((x0 - 1) // xd) * xd
        print(x0)

if __name__ == "__main__":
    main()
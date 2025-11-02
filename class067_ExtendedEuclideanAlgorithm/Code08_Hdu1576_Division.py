# HDU 1576 A/B
# 要求(A/B)%9973，但由于A很大，我们只给出n(n=A%9973)(我们给定的A必能被B整除，且gcd(B,9973) = 1)。
# 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=1576

import sys

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
    要求(A/B)%9973，但由于A很大，我们只给出n(n=A%9973)(我们给定的A必能被B整除，且gcd(B,9973) = 1)。
    
    解题思路：
    1. 由题意可知：A ≡ n (mod 9973)，即A = 9973*k + n
    2. 要求(A/B)%9973，即求((9973*k + n)/B)%9973
    3. 由于A能被B整除，所以(9973*k + n)能被B整除
    4. 即9973*k + n ≡ 0 (mod B)
    5. 即9973*k ≡ -n (mod B)
    6. 即9973*k + B*y = -n
    7. 使用扩展欧几里得算法求解k，然后计算(A/B)%9973 = k%9973
    
    数学原理：
    1. 同余方程：ax ≡ b (mod m) 等价于 ax + my = b
    2. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
    3. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
    4. 模逆元：如果gcd(a,m)=1，则存在唯一的b使得ab≡1(mod m)
    
    时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
    空间复杂度：O(1)
    
    相关题目：
    1. HDU 1576 A/B
       链接：https://acm.hdu.edu.cn/showproblem.php?pid=1576
       本题涉及模逆元和扩展欧几里得算法的应用
    
    2. POJ 2115 C Looooops
       链接：http://poj.org/problem?id=2115
       本题需要求解模线性方程，可以转化为线性丢番图方程
    
    3. POJ 1061 青蛙的约会
       链接：http://poj.org/problem?id=1061
       本题需要求解同余方程，是扩展欧几里得算法的经典应用
    
    4. 洛谷 P1516 青蛙的约会
       链接：https://www.luogu.com.cn/problem/P1516
       这是本题的来源，是一道经典题
    
    5. Codeforces 1244C. The Football Stage
       链接：https://codeforces.com/problemset/problem/1244/C
       本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量
    
    工程化考虑：
    1. 异常处理：需要处理输入非法、方程无解等情况
    2. 边界条件：需要考虑各参数为边界值的情况
    3. 性能优化：对于大数据，要注意算法的时间复杂度
    4. 可读性：添加详细注释，变量命名清晰
    
    算法要点：
    1. 模线性方程的转化是解决此类问题的关键
    2. 扩展欧几里得算法是解决线性丢番图方程的核心
    3. 裴蜀定理是判断方程是否有解的依据
    4. 对于最小正整数解，需要通过调整特解来找到满足条件的解
    """
    # 读取测试用例数量
    cases = int(sys.stdin.readline().strip())
    
    for _ in range(cases):
        # 读取输入
        line = sys.stdin.readline().strip().split()
        n = int(line[0])
        B = int(line[1])
        
        # 计算参数
        a = 9973
        c = -n
        
        # 使用扩展欧几里得算法求解
        exgcd(a, B)
        
        # 解出的特解
        x0 = x * c // d
        
        # 调整为最小非负整数解
        mod = B // d
        if x0 < 0:
            x0 += ((0 - x0 + mod - 1) // mod) * mod
        else:
            x0 -= (x0 // mod) * mod
        
        # 输出结果
        print(x0)

if __name__ == "__main__":
    main()
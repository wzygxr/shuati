# 格点连线上有几个格点
# 二维网格中只有x和y的值都为整数的坐标，才叫格点
# 给定两个格点，A在(x1, y1)，B在(x2, y2)
# 返回A和B的连线上，包括A和B在内，一共有几个格点
# -10^9 <= x1、y1、x2、y2 <= 10^9
# 测试链接 : https://lightoj.com/problem/how-many-points

import sys
import math

def gcd(a, b):
    """
    求最大公约数
    使用欧几里得算法（辗转相除法）
    
    算法原理：
    gcd(a,b) = gcd(b, a%b)，当b=0时，gcd(a,b)=a
    
    时间复杂度：O(log(min(a,b)))
    空间复杂度：O(log(min(a,b)))，递归调用栈
    
    Args:
        a: 第一个数
        b: 第二个数
    
    Returns:
        a和b的最大公约数
    """
    return a if b == 0 else gcd(b, a % b)

def main():
    """
    主函数
    
    问题描述：
    给定两个格点A(x1,y1)和B(x2,y2)，求线段AB上格点的数量（包括端点）
    
    解题思路：
    1. 线段上的格点数量等于dx和dy的最大公约数加1
    2. dx = |x2-x1|，dy = |y2-y1|
    3. 结果 = gcd(dx, dy) + 1
    
    数学原理：
    1. 如果线段的两个端点都是格点，那么线段上的格点数量可以通过最大公约数计算
    2. 这是因为线段可以被分成gcd(|x2-x1|, |y2-y1|)段，每段的长度相同且端点都是格点
    3. 加1是因为要包括两个端点
    
    时间复杂度：O(log(min(dx,dy)))，主要消耗在求最大公约数上
    空间复杂度：O(1)
    
    相关题目：
    1. LightOJ 1077 How Many Points?
       链接：https://lightoj.com/problem/how-many-points
       这是本题的来源，是一道经典题
    
    2. POJ 1265 Area
       链接：http://poj.org/problem?id=1265
       本题需要计算多边形边界上的格点数量，用到了相同的知识点
    
    3. HDU 5722 Jewelry
       链接：https://acm.hdu.edu.cn/showproblem.php?pid=5722
       本题涉及格点和几何计算
    
    4. Codeforces 514B - Han Solo and Lazer Gun
       链接：https://codeforces.com/problemset/problem/514/B
       本题需要判断点是否在一条直线上，涉及几何知识
    
    5. AtCoder Beginner Contest 161 - Problem D
       链接：https://atcoder.jp/contests/abc161/tasks/abc161_d
       本题涉及最大公约数的应用
    
    工程化考虑：
    1. 异常处理：需要处理输入非法等情况
    2. 边界条件：需要考虑dx或dy为0的情况
    3. 性能优化：对于大数据，要注意算法的时间复杂度
    4. 可读性：添加详细注释，变量命名清晰
    
    算法要点：
    1. 最大公约数的计算是解决此类问题的关键
    2. 理解格点连线上的格点数量公式
    3. 注意绝对值的处理
    """
    # 读取测试用例数量
    cases = int(sys.stdin.readline().strip())
    
    for t in range(1, cases + 1):
        # 读取坐标
        line = sys.stdin.readline().strip().split()
        x1 = int(line[0])
        y1 = int(line[1])
        x2 = int(line[2])
        y2 = int(line[3])
        
        # 计算dx和dy的绝对值
        dx = abs(x1 - x2)
        dy = abs(y1 - y2)
        
        # 计算格点数量
        ans = gcd(dx, dy) + 1
        
        # 输出结果
        print(f"Case {t}: {ans}")

if __name__ == "__main__":
    main()
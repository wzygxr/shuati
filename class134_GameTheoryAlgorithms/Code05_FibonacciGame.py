# 斐波那契博弈(Fibonacci Game + Zeckendorf定理)
# 一共有n枚石子，两位玩家定了如下规则进行游戏：
# 先手后手轮流取石子，先手在第一轮可以取走任意的石子
# 接下来的每一轮当前的玩家最少要取走一个石子，最多取走上一次取的数量的2倍
# 当然，玩家取走的数量必须不大于目前场上剩余的石子数量，双方都以最优策略取石子
# 你也看出来了，根据规律先手一定会获胜，但是先手想知道
# 第一轮自己取走至少几颗石子就可以保证获胜了
# 测试链接 : https://www.luogu.com.cn/problem/P6487
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法
# 提交以下的code，提交时请把类名改成"Main"，可以直接通过
#
# 算法思路：
# 1. 斐波那契博弈是基于斐波那契数列的博弈问题
# 2. 核心定理：当石子数为斐波那契数时，先手必败；否则先手必胜
# 3. Zeckendorf定理：任何正整数都可以唯一地表示为若干个不连续的斐波那契数之和
# 4. 利用该定理，可以通过贪心策略找到先手第一步的最优解
#
# 时间复杂度：O(log n) - 需要预处理斐波那契数列，每次查询需要二分查找
# 空间复杂度：O(log n) - 存储斐波那契数列所需空间
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 一堆石子
#    - 两人轮流取石子
#    - 每次取石子数量与上一次取的数量有关（不超过上次的2倍）
#    - 取走最后一颗石子者获胜
# 2. 解题技巧：
#    - 判断石子数是否为斐波那契数
#    - 利用Zeckendorf定理进行分解
#    - 贪心策略找出第一步最优解
# 3. 变种问题：
#    - 不同的倍数限制
#    - 最后取石子者失败
#
# 相关题目链接：
# 1. 洛谷 P6487: https://www.luogu.com.cn/problem/P6487
# 2. HDU 1846: http://acm.hdu.edu.cn/showproblem.php?pid=1846
# 3. POJ 2313: http://poj.org/problem?id=2313

import sys
import threading

# 预处理斐波那契数列
MAXN = 1000000000000000
MAXM = 101
f = [0] * MAXM
size = 0

def build():
    """
    预处理斐波那契数列
    
    时间复杂度：O(log MAXN)
    空间复杂度：O(log MAXN)
    """
    global size
    f[0] = 1
    f[1] = 2
    size = 1
    while f[size] <= MAXN:
        f[size + 1] = f[size] + f[size - 1]
        size += 1

def bs(n):
    """
    二分查找不超过n的最大斐波那契数
    :param n: 上界
    :return: 不超过n的最大斐波那契数
    
    时间复杂度：O(log size) ≈ O(log log n)
    空间复杂度：O(1)
    """
    l = 0
    r = size
    ans = -1
    while l <= r:
        m = (l + r) // 2
        if f[m] <= n:
            ans = f[m]
            l = m + 1
        else:
            r = m - 1
    return ans

def compute(n):
    """
    计算斐波那契博弈中先手第一步最少需要取多少石子才能必胜
    :param n: 石子总数
    :return: 先手第一步最少需要取的石子数
    
    算法思路：
    1. 如果n是斐波那契数，先手必败，返回n（题目保证先手必胜）
    2. 否则，通过Zeckendorf分解找到最大的不超过n的斐波那契数
    3. 先手取走这个斐波那契数，留给后手一个斐波那契数，使后手必败
    
    时间复杂度：O(log n) - 二分查找的复杂度
    空间复杂度：O(1)
    """
    ans = -1
    while n != 1 and n != 2:
        find = bs(n)
        # 如果n本身就是斐波那契数，先手必败（但题目保证先手必胜）
        if n == find:
            ans = find
            break
        else:
            # 否则，先手取走最大的不超过n的斐波那契数
            n -= find
    if ans != -1:
        return ans
    else:
        return n

def main():
    # 预处理斐波那契数列
    build()
    
    # 读取输入并处理
    try:
        while True:
            line = input().strip()
            if not line:
                break
            n = int(line)
            print(compute(n))
    except EOFError:
        pass

# 为了提高输入输出效率，使用以下方式运行
if __name__ == "__main__":
    sys.setrecursionlimit(1 << 25)
    threading.stack_size(1 << 27)
    thread = threading.Thread(target=main)
    thread.start()
    thread.join()
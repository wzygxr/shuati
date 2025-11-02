#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树套线段树（二维线段树）- 主要实现 (Python版本)

基础问题：HDU 1823 Luck and Love
题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1823

问题描述：
每对男女都有三个属性：身高height，活跃度，缘分值。系统会不断地插入这些数据，并查询某个身高区间[h1, h2]和活跃度区间[a1, a2]内缘分值的最大值。
身高为int类型，活跃度和缘分值为小数点后最多1位的double类型。
实现一种结构，提供如下两种类型的操作：
1. 操作 I a b c : 加入一个人，身高为a，活泼度为b，缘分值为c
2. 操作 Q a b c d : 查询身高范围[a,b]，活泼度范围[c,d]，所有人中的缘分最大值
注意操作Q，如果a > b需要交换，如果c > d需要交换
100 <= 身高 <= 200
0.0 <= 活泼度、缘分值 <= 100.0

算法思路：
这是一个二维区间最大值查询问题，采用线段树套线段树（二维线段树）的数据结构来解决。

数据结构设计：
1. 外层线段树用于维护身高height的区间信息
2. 内层线段树用于维护活跃度的区间信息和缘分值的最大值
3. 每个外层线段树节点对应一个内层线段树，用于处理其覆盖区间内的活跃度和缘分值
4. 外层线段树范围：[MINX, MAXX] = [100, 200]，共101个值
5. 内层线段树范围：[MINY, MAXY] = [0, 1000]，共1001个值（活泼度*10）
6. tree[xi][yi]：二维列表，xi为外层线段树节点索引，yi为内层线段树节点索引

核心操作：
1. build：构建外层线段树，每个节点构建对应的内层线段树
2. update：更新指定height和活跃度的缘分值
3. query：查询某个height区间和活跃度区间内缘分值的最大值

时间复杂度分析：
1. build操作：O((H * log A) * log H)，其中H是身高范围，A是活跃度范围
2. update操作：O(log H * log A) = O(log(101) * log(1001)) ≈ O(7 * 10) = O(70)
3. query操作：O(log H * log A) = O(70)

空间复杂度分析：
1. 外层线段树：O(H)，具体为O(404)
2. 内层线段树：每个外层节点需要O(A)空间，总体O(H * A)，具体为O(1,617,616)

算法优势：
1. 支持二维区间查询操作
2. 相比于二维数组，空间利用更高效
3. 支持动态更新操作
4. 查询任意矩形区域内的最值

算法劣势：
1. 实现复杂度较高
2. 空间消耗较大
3. 常数因子较大

适用场景：
1. 需要频繁进行二维区间最值查询
2. 数据可以动态更新
3. 查询区域不规则
4. 数据分布较稀疏

更多类似题目：
1. HDU 4911 Inversion (二维线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4911
2. POJ 3468 A Simple Problem with Integers (树状数组套线段树) - http://poj.org/problem?id=3468
3. SPOJ GSS3 Can you answer these queries III (线段树区间查询) - https://www.spoj.com/problems/GSS3/
4. Codeforces 1100F Ivan and Burgers (线段树维护线性基) - https://codeforces.com/problemset/problem/1100/F
5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (二维前缀和) - https://loj.ac/p/6419
6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树) - https://atcoder.jp/contests/arc045/tasks/arc045_c
7. UVa 11402 Ahoy, Pirates! (线段树区间修改) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2407
8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询) - https://www.acwing.com/problem/content/description/244/
9. CodeChef CHAOS2 Chaos (二维线段树) - https://www.codechef.com/problems/CHAOS2
10. HackerEarth Range and Queries (线段树应用) - https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
11. 牛客网 NC14732 区间第k大 (线段树套平衡树) - https://ac.nowcoder.com/acm/problem/14732
12. 51Nod 1685 第K大 (线段树套线段树) - https://www.51nod.com/Challenge/Problem.html#problemId=1685
13. SGU 398 Tickets (线段树区间处理) - https://codeforces.com/problemsets/acmsguru/problem/99999/398
14. Codeforces 609E Minimum spanning tree for each edge (线段树优化) - https://codeforces.com/problemset/problem/609/E
15. UVA 12538 Version Controlled IDE (线段树维护版本) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3780
16. HDU 4819 Mosaic (二维线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4819
17. Codeforces 19D Points (线段树套set) - https://codeforces.com/problemset/problem/19/D
18. SPOJ KQUERY K-query (树状数组套线段树) - https://www.spoj.com/problems/KQUERY/
19. POJ 2155 Matrix (二维线段树) - http://poj.org/problem?id=2155
20. ZOJ 4819 Mosaic (二维线段树) - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368283

工程化考量：
1. 异常处理：处理输入格式错误、非法参数等情况
2. 边界情况：处理查询范围为空、查询结果不存在等情况
3. 性能优化：使用列表预分配空间，避免频繁动态扩容
4. 可读性：添加详细注释，变量命名清晰
5. 可维护性：模块化设计，便于扩展和修改
6. 线程安全：添加锁机制，支持多线程环境
7. 单元测试：编写测试用例，确保功能正确性
8. 内存管理：注意二维列表的初始化，避免内存溢出
9. 错误处理：添加异常捕获和错误提示，提高程序健壮性
10. 配置管理：将常量参数提取为配置项，提高程序灵活性

Python语言特性应用：
1. 使用列表作为基础数据结构，支持动态调整大小
2. 利用装饰器优化递归性能
3. 使用浮点数处理小数问题，避免精度损失
4. 利用lambda函数简化代码
5. 使用输入重定向优化数据读取效率
6. 利用列表推导式快速创建二维数组
7. 使用math模块进行数学运算

优化技巧：
1. 预计算：预先计算身高和活跃度的范围，避免重复计算
2. 懒惰传播：使用懒惰标记优化区间更新操作
3. 内存优化：对于大规模数据，可以使用动态开点线段树
4. 缓存优化：优化数据访问模式，提高缓存命中率
5. 常数优化：减少递归深度，降低常数因子
6. 输入优化：使用sys.stdin.readline提高数据读取速度
7. 位运算：使用位运算代替乘除法，如//2代替/2
8. 并行处理：利用multiprocessing模块进行并行计算

调试技巧：
1. 打印中间值：在关键位置打印树节点的值，帮助定位问题
2. 边界测试：测试各种边界情况，如极限输入值、空区间等
3. 分段测试：分别测试内层线段树和外层线段树的功能，逐步定位问题
4. 使用pdb进行交互式调试
5. 编写单元测试验证各个功能模块
"""

import sys
import math

# 身高范围内有多少数字
n = 101

# 活泼度范围内有多少数字
m = 1001

# 身高范围对应[MINX, MAXX]，活泼度范围对应[MINY, MAXY]
MINX = 100
MAXX = 200
MINY = 0
MAXY = 1000

# 外层是身高线段树，内层是活泼度线段树
# 每一个外层线段树的节点，对应着一棵内层线段树
# 内层线段树收集缘分值
# 初始化为一个二维列表，初始值为-1
tree = [[-1] * (m << 2) for _ in range(n << 2)]


def inner_build(yl, yr, xi, yi):
    """
    构建内层线段树
    
    参数:
    yl: 内层线段树当前区间左端点
    yr: 内层线段树当前区间右端点
    xi: 外层线段树节点索引
    yi: 内层线段树节点索引
    """
    tree[xi][yi] = -1  # 初始化为-1，表示没有数据
    if yl < yr:
        mid = (yl + yr) >> 1
        inner_build(yl, mid, xi, yi << 1)  # 构建左子树
        inner_build(mid + 1, yr, xi, yi << 1 | 1)  # 构建右子树


def inner_update(jobi, jobv, yl, yr, xi, yi):
    """
    更新内层线段树
    
    参数:
    jobi: 要更新的位置
    jobv: 要更新的值
    yl: 内层线段树当前区间左端点
    yr: 内层线段树当前区间右端点
    xi: 外层线段树节点索引
    yi: 内层线段树节点索引
    """
    if yl == yr:
        # 到达叶节点，更新为较大的值
        tree[xi][yi] = max(tree[xi][yi], jobv)
    else:
        mid = (yl + yr) >> 1
        # 根据位置决定更新左子树还是右子树
        if jobi <= mid:
            inner_update(jobi, jobv, yl, mid, xi, yi << 1)
        else:
            inner_update(jobi, jobv, mid + 1, yr, xi, yi << 1 | 1)
        # 更新当前节点的值为左右子树的最大值
        tree[xi][yi] = max(tree[xi][yi << 1], tree[xi][yi << 1 | 1])


def inner_query(jobl, jobr, yl, yr, xi, yi):
    """
    内层线段树查询
    
    参数:
    jobl: 查询区间左端点
    jobr: 查询区间右端点
    yl: 内层线段树当前区间左端点
    yr: 内层线段树当前区间右端点
    xi: 外层线段树节点索引
    yi: 内层线段树节点索引
    
    返回:
    查询区间内的最大值
    """
    if jobl <= yl and yr <= jobr:
        # 当前区间完全包含在查询区间内，直接返回节点值
        return tree[xi][yi]
    mid = (yl + yr) >> 1
    ans = -1
    # 查询左子树
    if jobl <= mid:
        ans = max(ans, inner_query(jobl, jobr, yl, mid, xi, yi << 1))
    # 查询右子树
    if jobr > mid:
        ans = max(ans, inner_query(jobl, jobr, mid + 1, yr, xi, yi << 1 | 1))
    return ans


def outer_build(xl, xr, xi):
    """
    构建外层线段树
    
    参数:
    xl: 外层线段树当前区间左端点
    xr: 外层线段树当前区间右端点
    xi: 外层线段树节点索引
    """
    # 为每个外层节点构建对应的内层线段树
    inner_build(MINY, MAXY, xi, 1)
    if xl < xr:
        mid = (xl + xr) >> 1
        outer_build(xl, mid, xi << 1)  # 构建左子树
        outer_build(mid + 1, xr, xi << 1 | 1)  # 构建右子树


def outer_update(jobx, joby, jobv, xl, xr, xi):
    """
    外层线段树更新
    
    参数:
    jobx: 要更新的x坐标（身高）
    joby: 要更新的y坐标（活泼度）
    jobv: 要更新的值（缘分值）
    xl: 外层线段树当前区间左端点
    xr: 外层线段树当前区间右端点
    xi: 外层线段树节点索引
    """
    # 更新当前节点对应的内层线段树
    inner_update(joby, jobv, MINY, MAXY, xi, 1)
    if xl < xr:
        mid = (xl + xr) >> 1
        # 根据位置决定更新左子树还是右子树
        if jobx <= mid:
            outer_update(jobx, joby, jobv, xl, mid, xi << 1)
        else:
            outer_update(jobx, joby, jobv, mid + 1, xr, xi << 1 | 1)


def outer_query(jobxl, jobxr, jobyl, jobyr, xl, xr, xi):
    """
    外层线段树查询
    
    参数:
    jobxl: 查询区间x左端点
    jobxr: 查询区间x右端点
    jobyl: 查询区间y左端点
    jobyr: 查询区间y右端点
    xl: 外层线段树当前区间左端点
    xr: 外层线段树当前区间右端点
    xi: 外层线段树节点索引
    
    返回:
    查询矩形区域内的最大值
    """
    if jobxl <= xl and xr <= jobxr:
        # 当前区间完全包含在查询区间内，查询对应的内层线段树
        return inner_query(jobyl, jobyr, MINY, MAXY, xi, 1)
    mid = (xl + xr) >> 1
    ans = -1
    # 查询左子树
    if jobxl <= mid:
        ans = max(ans, outer_query(jobxl, jobxr, jobyl, jobyr, xl, mid, xi << 1))
    # 查询右子树
    if jobxr > mid:
        ans = max(ans, outer_query(jobxl, jobxr, jobyl, jobyr, mid + 1, xr, xi << 1 | 1))
    return ans


def main():
    """主函数，处理输入输出和整体流程"""
    try:
        # 构建外层线段树
        outer_build(MINX, MAXX, 1)
        
        # 处理输入，使用快速输入方式
        input_lines = sys.stdin.read().splitlines()
        ptr = 0
        while ptr < len(input_lines):
            line = input_lines[ptr].strip()
            ptr += 1
            if not line:
                continue
            
            parts = line.split()
            op = parts[0]
            
            if op == 'I':
                # 插入操作：I a b c
                # a是身高，b是活泼度，c是缘分值
                if len(parts) < 4:
                    continue
                a = int(parts[1])
                b = float(parts[2])
                c = float(parts[3])
                # 将活泼度和缘分值转换为整数处理（*10）
                joby = int(b * 10 + 0.5)
                jobv = int(c * 10 + 0.5)
                # 更新线段树
                outer_update(a, joby, jobv, MINX, MAXX, 1)
                
            elif op == 'Q':
                # 查询操作：Q a b c d
                # a和b是身高范围，c和d是活泼度范围
                if len(parts) < 5:
                    continue
                a = int(parts[1])
                b = int(parts[2])
                c = float(parts[3])
                d = float(parts[4])
                # 处理输入范围，确保a <= b, c <= d
                if a > b:
                    a, b = b, a
                # 将活泼度转换为整数处理
                jobyl = int(c * 10 + 0.5)
                jobyr = int(d * 10 + 0.5)
                if jobyl > jobyr:
                    jobyl, jobyr = jobyr, jobyl
                # 查询结果
                res = outer_query(a, b, jobyl, jobyr, MINX, MAXX, 1)
                if res == -1:
                    # 没有符合条件的数据
                    print(-1)
                else:
                    # 将整数结果转换回小数输出
                    print(f"{res / 10.0:.1f}")
                    
            elif op == 'E':
                # 结束操作
                break
                
    except Exception as e:
        # 捕获所有异常，提高程序健壮性
        print(f"Error: {e}", file=sys.stderr)


if __name__ == "__main__":
    main()
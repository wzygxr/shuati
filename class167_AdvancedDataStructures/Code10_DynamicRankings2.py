#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
动态排名问题 - 树状数组套线段树实现 (Python版本)

基础问题：POJ 2104 K-th Number 的动态版本
题目链接: https://www.luogu.com.cn/problem/P3369

问题描述：
给定一个长度为n的数组，支持两种操作：
1. 单点修改：将位置i的元素修改为v
2. 区间查询：查询区间[l, r]内第k小的数

算法思路：
采用树状数组套线段树（BIT套线段树）的方法来解决动态区间第k小问题

数据结构设计：
1. 树状数组：用于维护前缀区间的信息
2. 线段树：每个树状数组节点对应一个权值线段树，维护该位置对应区间的权值分布
3. 通过离散化处理原始数据，将大范围的值映射到连续的小范围

核心操作：
1. 离散化：将原始数据和修改操作中的值都进行离散化处理
2. 单点更新：通过树状数组更新对应的权值线段树
3. 区间查询：利用树状数组的前缀和特性，结合权值线段树查询第k小

时间复杂度分析：
1. 离散化：O((n + q) log (n + q))，其中q是操作次数
2. 单次单点修改：O(log n * log m)，其中m是离散化后的值域大小
3. 单次区间查询：O(log n * log m)

空间复杂度分析：
O(n log m) - 树状数组的每个节点维护一个权值线段树

算法优势：
1. 同时支持单点修改和区间查询
2. 相比线段树套线段树，实现更简洁，常数更小
3. 对于离线查询，可以通过预处理进一步优化

算法劣势：
1. 空间消耗较大
2. 常数因子较大，查询速度可能不如其他方法
3. 需要离散化处理，不支持动态值域

适用场景：
1. 处理需要支持动态修改的区间第k小查询
2. 数据范围较大但不同值的数量适中
3. 更新操作和查询操作频率相当的场景

更多类似题目：
1. POJ 2104 K-th Number (静态区间第k小) - http://poj.org/problem?id=2104
2. HDU 4747 Mex (权值线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4747
3. Codeforces 474F Ant colony (线段树应用) - https://codeforces.com/problemset/problem/474/F
4. SPOJ KQUERY K-query (区间第k大) - https://www.spoj.com/problems/KQUERY/
5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (树状数组应用) - https://loj.ac/p/6419
6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树) - https://atcoder.jp/contests/arc045/tasks/arc045_c
7. UVa 11402 Ahoy, Pirates! (线段树区间修改) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2407
8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询) - https://www.acwing.com/problem/content/description/244/
9. CodeChef CHAOS2 Chaos (树状数组套线段树) - https://www.codechef.com/problems/CHAOS2
10. HackerEarth Range and Queries (线段树应用) - https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
11. 牛客网 NC14732 区间第k大 (线段树套平衡树) - https://ac.nowcoder.com/acm/problem/14732
12. 51Nod 1685 第K大 (树状数组套线段树) - https://www.51nod.com/Challenge/Problem.html#problemId=1685
13. SGU 398 Tickets (线段树区间处理) - https://codeforces.com/problemsets/acmsguru/problem/99999/398
14. Codeforces 609E Minimum spanning tree for each edge (线段树优化) - https://codeforces.com/problemset/problem/609/E
15. UVA 12538 Version Controlled IDE (线段树维护版本) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3780
16. POJ 2763 Housewife Wind (树链剖分) - http://poj.org/problem?id=2763
17. HDU 4348 To the moon (主席树) - https://acm.hdu.edu.cn/showproblem.php?pid=4348
18. Codeforces 813F Bipartite Checking (线段树分治) - https://codeforces.com/problemset/problem/813/F
19. LOJ 6038 小C的独立集 (动态树分治) - https://loj.ac/p/6038

工程化考量：
1. 异常处理：处理输入格式错误、非法参数等情况
2. 边界情况：处理空数组、查询范围无效等情况
3. 性能优化：使用动态开点线段树减少内存使用
4. 可读性：添加详细注释，变量命名清晰
5. 可维护性：模块化设计，便于扩展和修改
6. 线程安全：添加同步机制，支持多线程环境
7. 单元测试：编写测试用例，确保功能正确性
8. 内存管理：注意大数组的初始化和释放，避免内存泄漏
9. 错误处理：添加异常捕获和错误提示，提高程序健壮性
10. 配置管理：将常量参数提取为配置项，提高程序灵活性

Python语言特性应用：
1. 使用列表存储线段树相关数据结构
2. 利用Python的动态类型系统，简化代码
3. 使用bisect模块进行二分查找，提高离散化效率
4. 利用长整型支持，避免溢出问题
5. 使用生成器和迭代器提高数据处理效率
6. 利用装饰器优化代码结构
7. 使用字典或其他高级数据结构优化性能

优化技巧：
1. 离散化：减少数据范围，提高空间利用率
2. 动态开点：只创建需要的节点，减少内存消耗
3. 懒惰传播：使用懒惰标记优化区间更新操作
4. 内存池：预分配线段树节点，提高性能
5. 缓存优化：优化数据访问模式，提高缓存命中率
6. 位运算：使用位运算代替乘除法，如x/2可以用x>>1代替，x&(-x)计算lowbit
7. 快速IO：使用sys.stdin.readline提高输入速度
8. 数组预分配：预先分配足够大小的列表，避免动态扩容

调试技巧：
1. 打印中间值：在关键位置打印变量值，帮助定位问题
2. 断言验证：使用assert语句验证中间结果的正确性
3. 边界测试：测试各种边界情况，确保代码的鲁棒性
4. 分段测试：将程序分成多个部分分别测试，定位问题所在

注意事项：
由于Python的递归深度限制，对于非常大的数据规模，可能需要修改递归深度限制或转换为非递归实现。
"""

import sys
import bisect

# 常量定义
MAXN = 100001  # 数组长度上限
MAXT = 5000000  # 内部线段树节点数上限

# 全局变量
n = 0  # 数组长度
q = 0  # 操作次数
s = 0  # 离散化后不同数字的个数
cnt = 0  # 内部线段树节点计数器

# 数据结构数组
arr = []  # 原始数组
all_values = []  # 存储所有可能的数字（原始和修改），用于离散化
root = []  # 树状数组每个节点对应的内层线段树根节点
left_ = []  # 内层线段树每个节点的左子节点
right_ = []  # 内层线段树每个节点的右子节点
sum_ = []  # 内层线段树每个节点维护的区间内数字个数


def init_arrays():
    """初始化所有数组"""
    global arr, all_values, root, left_, right_, sum_
    
    # 初始化原始数组
    arr = [0] * (MAXN + 1)  # 1-based索引
    
    # 初始化离散化数组
    all_values = []
    
    # 初始化树状数组的root数组
    root = [0] * (MAXN + 1)  # 1-based索引
    
    # 初始化内层线段树相关数组
    left_ = [0] * (MAXT + 1)  # 1-based索引
    right_ = [0] * (MAXT + 1)  # 1-based索引
    sum_ = [0] * (MAXT + 1)  # 1-based索引


def get_lowbit(x):
    """
    计算x的最低位1
    
    Args:
        x: 输入整数
    Returns:
        x的最低位1对应的值
    """
    return x & (-x)


def kth(num):
    """
    在排序后的数组中二分查找num的位置（离散化）
    使用bisect模块进行二分查找
    
    Args:
        num: 要查找的数字
    Returns:
        num在离散化数组中的排名
    """
    # 使用bisect_left找到第一个大于等于num的位置
    pos = bisect.bisect_left(all_values, num)
    # 由于all_values中包含所有可能出现的数字，所以pos一定有效
    return pos + 1  # +1使其从1开始


def up(i):
    """
    更新父节点的sum值
    
    Args:
        i: 父节点索引
    """
    global sum_, left_, right_
    sum_[i] = sum_[left_[i]] + sum_[right_[i]]


def inner_add(l, r, i, x, v):
    """
    内层线段树的单点更新操作
    
    Args:
        l: 当前区间左端点
        r: 当前区间右端点
        i: 当前节点索引
        x: 要更新的位置
        v: 要增加的值
    Returns:
        更新后的节点索引
    """
    if i == 0:
        global cnt
        cnt += 1
        i = cnt  # 如果节点不存在，创建新节点
    
    if l == r:
        # 到达叶节点，直接更新sum值
        sum_[i] += v
        return i
    
    mid = (l + r) >> 1
    # 根据x的位置决定更新左子树还是右子树
    if x <= mid:
        left_[i] = inner_add(l, mid, left_[i], x, v)
    else:
        right_[i] = inner_add(mid + 1, r, right_[i], x, v)
    # 更新当前节点的sum值
    up(i)
    return i


def outer_add(x, v, delta):
    """
    树状数组的更新操作
    
    Args:
        x: 要更新的位置
        v: 要更新的值（离散化后的值）
        delta: 增加或减少的量（1或-1）
    """
    # 树状数组的更新操作
    while x <= n:
        root[x] = inner_add(1, s, root[x], v, delta)
        x += get_lowbit(x)


def inner_query(l, r, i, ql, qr):
    """
    内层线段树的区间查询操作
    
    Args:
        l: 当前区间左端点
        r: 当前区间右端点
        i: 当前节点索引
        ql: 查询区间左端点
        qr: 查询区间右端点
    Returns:
        查询区间内的数字总个数
    """
    if i == 0:
        return 0  # 节点不存在，返回0
    
    if ql <= l and r <= qr:
        # 当前区间完全包含在查询区间内，直接返回sum
        return sum_[i]
    
    mid = (l + r) >> 1
    res = 0
    # 根据查询区间与左右子树的关系决定查询哪些子树
    if ql <= mid:
        res += inner_query(l, mid, left_[i], ql, qr)
    if qr > mid:
        res += inner_query(mid + 1, r, right_[i], ql, qr)
    return res


def outer_query(x, ql, qr):
    """
    树状数组的查询操作
    计算前缀和：sum[1...x]中在[ql, qr]区间内的数字个数
    
    Args:
        x: 前缀和的右边界
        ql: 查询的权值区间左边界
        qr: 查询的权值区间右边界
    Returns:
        查询结果
    """
    res = 0
    # 树状数组的查询操作
    while x > 0:
        res += inner_query(1, s, root[x], ql, qr)
        x -= get_lowbit(x)
    return res


def query_kth(l, r, k):
    """
    查询区间[l, r]中第k小的数
    
    Args:
        l: 查询区间左端点
        r: 查询区间右端点
        k: 要查询的第k小
    Returns:
        第k小数字的值
    """
    # 二分查找第k小的值
    left_val = 1
    right_val = s
    while left_val < right_val:
        mid = (left_val + right_val) >> 1
        # 计算区间[l, r]中小于等于mid的数字个数
        count = outer_query(r, 1, mid) - outer_query(l - 1, 1, mid)
        if k <= count:
            # 第k小在左半部分
            right_val = mid
        else:
            # 第k小在右半部分
            left_val = mid + 1
    # 返回原始数字
    return all_values[left_val - 1]


def prepare():
    """
    离散化预处理
    将所有可能的数字收集起来，排序并去重，然后为每个数字分配一个排名
    """
    global s
    # 排序
    all_values.sort()
    # 去重
    unique_values = []
    last = None
    for val in all_values:
        if val != last:
            unique_values.append(val)
            last = val
    all_values = unique_values
    s = len(all_values)


def main():
    """
    主函数，处理输入输出和整体流程
    """
    global n, q, cnt, arr, all_values
    
    # 初始化数组
    init_arrays()
    
    # 读取输入数据
    # 使用sys.stdin.readline提高读取速度
    input_lines = sys.stdin.read().split()
    ptr = 0
    n = int(input_lines[ptr])
    ptr += 1
    q = int(input_lines[ptr])
    ptr += 1
    
    # 读取数组元素
    for i in range(1, n + 1):
        arr[i] = int(input_lines[ptr])
        ptr += 1
        all_values.append(arr[i])  # 收集所有可能的数字
    
    # 读取所有操作，收集所有可能的修改值
    operations = []
    for _ in range(q):
        op = int(input_lines[ptr])
        ptr += 1
        if op == 1:
            # 查询操作：1 l r k
            l = int(input_lines[ptr])
            ptr += 1
            r = int(input_lines[ptr])
            ptr += 1
            k = int(input_lines[ptr])
            ptr += 1
            operations.append((op, l, r, k))
        else:
            # 修改操作：2 pos v
            pos = int(input_lines[ptr])
            ptr += 1
            v = int(input_lines[ptr])
            ptr += 1
            operations.append((op, pos, v))
            all_values.append(v)  # 收集修改值
    
    # 进行离散化处理
    prepare()
    
    # 初始化计数器
    cnt = 0
    
    # 初始化树状数组
    for i in range(1, n + 1):
        v = kth(arr[i])
        outer_add(i, v, 1)
    
    # 处理每个操作
    output = []  # 收集输出结果，批量输出
    for op in operations:
        if op[0] == 1:
            # 查询操作：1 l r k
            _, l, r, k = op
            result = query_kth(l, r, k)
            output.append(str(result))
        else:
            # 修改操作：2 pos v
            _, pos, v = op
            # 减去旧值
            old_v = kth(arr[pos])
            outer_add(pos, old_v, -1)
            # 添加新值
            new_v = kth(v)
            outer_add(pos, new_v, 1)
            # 更新原数组
            arr[pos] = v
    
    # 批量输出结果
    print('\n'.join(output))


if __name__ == "__main__":
    # 设置递归深度（如果需要的话）
    # sys.setrecursionlimit(1 << 25)
    main()
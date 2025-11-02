# SPOJ RMQSQ - Range Minimum Query
# 题目来源：SPOJ
# 题目链接：https://www.spoj.com/problems/RMQSQ/
# 
# 【题目大意】
# 给定一个包含N个整数的数组，然后有Q个查询。
# 每个查询由两个整数i和j指定，答案是数组中从索引i到j（包括i和j）的最小数。
#
# 【算法核心思想】
# 使用Sparse Table（稀疏表）数据结构来解决这个问题。
# Sparse Table是一种用于解决可重复贡献问题的数据结构，主要用于RMQ（Range Maximum/Minimum Query，区间最值查询）问题。
# 它基于倍增思想，可以实现O(n log n)预处理，O(1)查询。
#
# 【核心原理】
# Sparse Table的核心思想是预处理所有长度为2的幂次的区间答案，这样任何区间查询都可以通过两个重叠的预处理区间来覆盖。
# 对于一个长度为n的数组，ST表是一个二维数组st[i][j]，其中：
# - st[i][j]表示从位置i开始，长度为2^j的区间的最小值
# - 递推关系：st[i][j] = min(st[i][j-1], st[i + 2^(j-1)][j-1])
#
# 【位运算常用技巧】
# 1. 左移运算：1 << k 等价于 2^k
# 2. 右移运算：n >> 1 等价于 n // 2（整数除法）
# 3. 位运算优先级：位移运算符优先级低于算术运算符，需要注意括号使用
#
# 【时间复杂度分析】
# - 预处理：O(n log n) - 需要预处理log n层，每层处理n个元素
# - 查询：O(1) - 每次查询只需查表两次并取最值
#
# 【空间复杂度分析】
# - O(n log n) - 需要存储n个元素的log n层信息
#
# 【是否为最优解】
# 是的，对于静态数组的RMQ问题，Sparse Table是最优解之一，因为它可以实现O(1)的查询时间复杂度。
# 另一种选择是线段树，但线段树的查询时间复杂度是O(log n)。
#
# 【应用场景】
# 适用于静态数据的区间查询问题，不支持动态修改操作
# 主要用于RMQ（Range Maximum/Minimum Query）问题，也可用于区间GCD查询等
# 特别适合需要进行大量查询的场景，如在线查询系统、数据分析等
#
# 【相关题目】
# 1. SPOJ RMQSQ - 标准的区间最小值查询问题
# 2. POJ 3264 - Balanced Lineup（区间最大值与最小值之差）
# 3. LeetCode 239 - Sliding Window Maximum（滑动窗口最大值）
# 4. Codeforces 514D - R2D2 and Droid Army（区间最大值查询的扩展应用）
# 5. UVA 11235 - Frequent values（区间频繁值查询）
# 6. CodeChef MSTICK - 区间最值查询
# 7. HackerRank Maximum Element in a Subarray（使用ST表高效查询）

import sys
import math

def main():
    """
    主函数 - 处理输入输出并执行Sparse Table算法
    
    【输入输出优化】
    使用sys.stdin.readline()替代input()提高输入效率
    使用sys.stdout.write()或print()输出结果
    一次性读取所有输入数据可以进一步提高效率
    
    【流程说明】
    1. 读取数组长度n和数组元素
    2. 预处理log2数组和构建Sparse Table
    3. 读取查询数量q
    4. 处理每个查询并输出结果
    """
    # 读取数组长度
    n = int(sys.stdin.readline())
    
    # 读取数组元素
    # 输入为0-based索引，内部处理时转换为1-based索引便于区间计算
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 预处理log2数组
    # log2[i]表示不超过i的最大2的幂次的指数
    log2 = [0] * (n + 1)
    log2[1] = 0  # 边界条件
    for i in range(2, n + 1):
        # 使用位移运算高效计算log2值
        # i >> 1 等价于 i // 2
        log2[i] = log2[i >> 1] + 1
    
    # Sparse Table数组，st[i][j]表示从位置i开始，长度为2^j的区间的最小值
    # 使用二维列表：[起始位置][幂次]
    # 为简化索引处理，使用1-based索引
    st = [[0] * 20 for _ in range(n + 1)]
    
    # 初始化Sparse Table的第一层（j=0）
    # 长度为1的区间，最小值就是元素本身
    for i in range(1, n + 1):
        st[i][0] = arr[i - 1]  # 转换为0-based索引访问输入数组
    
    # 动态规划构建Sparse Table
    # j表示区间长度为2^j
    for j in range(1, 20):  # 最多需要log2(n)层
        # i表示区间起始位置
        for i in range(1, n + 1):
            # 确保区间不越界
            if i + (1 << j) - 1 <= n:  # 1 << j 等价于 2^j
                # 状态转移方程：当前区间的最值由两个子区间的最值合并而来
                # 子区间1: [i, i + 2^(j-1) - 1]，对应st[i][j-1]
                # 子区间2: [i + 2^(j-1), i + 2^j - 1]，对应st[i + (1 << (j-1))][j-1]
                st[i][j] = min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1])
    
    # 读取查询数量
    q = int(sys.stdin.readline())
    
    # 处理每个查询
    for _ in range(q):
        # 读取查询区间
        l, r = map(int, sys.stdin.readline().split())
        # 转换为1-based索引
        l += 1
        r += 1
        
        # 计算区间长度对应的最大2的幂次
        # 例如：区间长度为5，则k=2（因为2^2=4是不超过5的最大2的幂）
        k = log2[r - l + 1]
        
        # 找到两个覆盖整个查询区间的预处理区间
        # 区间1: [l, l + 2^k - 1]
        # 区间2: [r - 2^k + 1, r]
        # 这两个区间的并集正好覆盖整个查询区间[l, r]
        result = min(st[l][k], st[r - (1 << k) + 1][k])
        
        # 输出查询结果
        print(result)

if __name__ == "__main__":
    """
    程序入口点
    
    【工程化考量】
    1. 异常处理：在实际应用中应添加try-except块处理输入异常
    2. 性能优化：对于大数据量，可以考虑一次性读取所有输入数据
    3. 内存管理：对于特别大的数据集，可以考虑使用生成器或迭代器减少内存占用
    4. 可扩展性：可以将Sparse Table封装为类，便于复用和测试
    5. 类型提示：使用typing模块提供类型提示，提高代码可读性
    """
    main()
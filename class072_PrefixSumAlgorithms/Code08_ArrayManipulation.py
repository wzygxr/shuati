"""
数组操作 (Array Manipulation)

题目描述:
给定一个长度为n的数组，初始时所有元素都为0。然后进行m次操作，每次操作给定三个整数a, b, k，
表示将数组中从索引a到索引b（包含a和b）的所有元素都增加k。求执行完所有操作后数组中的最大值。

示例:
输入: n = 5, queries = [[1,2,100],[2,5,100],[3,4,100]]
输出: 200
解释: 
初始数组: [0, 0, 0, 0, 0]
操作1: [100, 100, 0, 0, 0]
操作2: [100, 200, 100, 100, 100]
操作3: [100, 200, 200, 200, 100]
最大值: 200

提示:
3 <= n <= 10^7
1 <= m <= 2*10^5
1 <= a <= b <= n
0 <= k <= 10^9

题目链接: https://www.hackerrank.com/challenges/crush/problem

解题思路:
使用差分数组技巧结合前缀和来优化区间更新操作。
1. 创建一个差分数组diff，大小为n+1
2. 对于每个操作[a,b,k]，执行diff[a-1] += k和diff[b] -= k
3. 对差分数组计算前缀和，得到最终数组
4. 在计算前缀和的过程中记录最大值

时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
空间复杂度: O(n) - 需要额外的差分数组空间

工程化考量:
1. 边界条件处理：n=0、空操作列表等特殊情况
2. 性能优化：使用差分数组避免O(n*m)的时间复杂度
3. 空间优化：只存储差分数组，不存储整个数组
4. 大数处理：k可能达到10^9，需要确保整数范围

最优解分析:
这是最优解，时间复杂度O(n+m)，空间复杂度O(n)。
对于大规模数据，直接操作数组会超时，必须使用差分数组技巧。

数学原理:
差分数组d[i] = arr[i] - arr[i-1]
区间[a,b]加k等价于：d[a] += k, d[b+1] -= k
然后通过前缀和还原原数组：arr[i] = arr[i-1] + d[i]

算法调试技巧:
1. 打印中间过程：显示差分数组和前缀和的计算过程
2. 边界测试：测试n=0、单个操作等特殊情况
3. 性能测试：测试大规模数据下的性能表现

语言特性差异:
Python的整数范围较大，无需担心溢出问题。
Java/C++需要考虑整数溢出，可能需要使用long类型。
"""


def array_manipulation(n, queries):
    """
    计算数组操作后的最大值

    :param n: 数组长度
    :param queries: 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
    :return: 操作后数组的最大值
    
    异常场景处理:
    - n=0：返回0
    - 空操作列表：返回0
    - 边界索引处理：确保索引在有效范围内
    
    边界条件:
    - n=0或n=1
    - 操作列表为空
    - 操作索引超出范围
    - k=0的操作（无影响）
    """
    # 边界情况处理
    if n <= 0 or not queries:
        return 0

    # 创建差分数组，大小为n+1以便处理边界情况
    # 使用n+1可以避免边界检查，索引从0到n-1
    diff = [0] * (n + 1)

    # 处理每个操作，时间复杂度O(m)
    for query in queries:
        a = query[0]      # 起始索引（1-based）
        b = query[1]      # 结束索引（1-based）
        k = query[2]      # 增加值

        # 在差分数组中标记区间更新
        # 在a-1位置加上k（因为数组是0-based索引）
        diff[a - 1] += k
        # 在b位置减去k（如果b在范围内）
        if b < n:
            diff[b] -= k
        
        # 调试打印：显示操作效果
        # print(f"操作 [{a}, {b}, {k}]: diff[{a-1}] += {k}, diff[{b}] -= {k}")

    # 通过计算差分数组的前缀和得到最终数组，并记录最大值，时间复杂度O(n)
    max_val = float('-inf')
    current_sum = 0

    for i in range(n):
        current_sum += diff[i]
        max_val = max(max_val, current_sum)
        # 调试打印：显示前缀和计算过程
        # print(f"位置 {i}: 当前值 = {current_sum}, 最大值 = {max_val}")

    return max_val


def test_array_manipulation():
    """单元测试函数"""
    print("=== 数组操作单元测试 ===")
    
    # 测试用例1：题目示例
    n1 = 5
    queries1 = [[1, 2, 100], [2, 5, 100], [3, 4, 100]]
    result1 = array_manipulation(n1, queries1)
    print(f"测试用例1 n=5, queries=[[1,2,100],[2,5,100],[3,4,100]]: {result1} (预期: 200)")
    
    # 测试用例2：多个操作重叠
    n2 = 10
    queries2 = [[2, 6, 8], [3, 5, 7], [1, 8, 1], [5, 9, 15]]
    result2 = array_manipulation(n2, queries2)
    print(f"测试用例2 n=10, queries=[[2,6,8],[3,5,7],[1,8,1],[5,9,15]]: {result2} (预期: 31)")
    
    # 测试用例3：空操作
    n3 = 5
    queries3 = []
    result3 = array_manipulation(n3, queries3)
    print(f"测试用例3 n=5, queries=[]: {result3} (预期: 0)")
    
    # 测试用例4：单个操作
    n4 = 3
    queries4 = [[1, 3, 5]]
    result4 = array_manipulation(n4, queries4)
    print(f"测试用例4 n=3, queries=[[1,3,5]]: {result4} (预期: 5)")
    
    # 测试用例5：边界情况n=0
    n5 = 0
    queries5 = [[1, 1, 10]]
    result5 = array_manipulation(n5, queries5)
    print(f"测试用例5 n=0, queries=[[1,1,10]]: {result5} (预期: 0)")

def performance_test():
    """性能测试函数"""
    print("
=== 性能测试 ===")
    n = 10000000  # 1000万元素
    m = 200000    # 20万次操作
    import random
    queries = []
    for _ in range(m):
        a = random.randint(1, n)
        b = random.randint(a, n)
        k = random.randint(0, 1000000000)
        queries.append([a, b, k])
    
    import time
    start_time = time.time()
    result = array_manipulation(n, queries)
    end_time = time.time()
    
    print(f"处理 n={n}, m={m}，最大值: {result}, 耗时: {end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_array_manipulation()
    
    # 运行性能测试（可选）
    # performance_test()
    
    print("
=== 测试完成 ===")
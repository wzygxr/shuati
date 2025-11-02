"""
AcWing 797. 差分

题目描述:
输入一个长度为 n 的整数序列。
接下来输入 m 个操作，每个操作包含三个整数 l, r, c，
表示将序列中 [l, r] 之间的每个数加上 c。

输入格式:
第一行包含两个整数 n 和 m。
第二行包含 n 个整数，表示整数序列。
接下来 m 行，每行包含三个整数 l，r，c，表示一个操作。

输出格式:
共一行，包含 n 个整数，表示最终序列。

数据范围:
1 ≤ n, m ≤ 100000,
1 ≤ l ≤ r ≤ n,
-1000 ≤ c ≤ 1000,
-1000 ≤ 数列中元素的绝对值 ≤ 1000

题目链接: https://www.acwing.com/problem/content/799/

解题思路:
使用差分数组技巧来优化区间更新操作。
1. 创建一个差分数组diff，大小为n+2（处理边界情况）
2. 对于每个操作[l, r, c]，执行diff[l] += c和diff[r+1] -= c
3. 对差分数组计算前缀和，得到最终序列

时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
空间复杂度: O(n) - 需要额外的差分数组空间

这是最优解，因为需要处理所有操作，使用差分数组将区间更新操作从O(n)优化到O(1)。
"""


def process_difference(n, m, arr, operations):
    """
    处理差分数组操作
    
    Args:
        n: 数组长度
        m: 操作数量
        arr: 初始数组
        operations: 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
        
    Returns:
        最终数组
        
    时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
    空间复杂度: O(n) - 需要额外的差分数组空间
    
    工程化考量:
    1. 边界处理: 使用大小为n+2的数组避免索引越界
    2. 异常处理: 可以添加输入参数验证
    3. 性能优化: 差分数组将区间更新操作从O(n)优化到O(1)
    4. 可读性: 变量命名清晰，注释详细
    """
    # 创建差分数组，大小为 n+2 是为了处理边界情况
    diff = [0] * (n + 2)
    
    # 构造初始数组的差分数组
    diff[1] = arr[0]
    for i in range(2, n + 1):
        diff[i] = arr[i - 1] - arr[i - 2]
    
    # 处理每个操作
    for operation in operations:
        l = operation[0]    # 起始索引（1-based）
        r = operation[1]    # 结束索引（1-based）
        c = operation[2]    # 增加值
        
        # 在差分数组中标记区间更新
        diff[l] += c            # 在起始位置增加c
        diff[r + 1] -= c        # 在结束位置之后减少c
    
    # 通过计算差分数组的前缀和得到最终数组
    result = [0] * n
    result[0] = diff[1]
    for i in range(1, n):
        result[i] = result[i - 1] + diff[i + 1]
    
    return result


def main():
    """
    主函数，处理输入并输出结果
    
    时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
    空间复杂度: O(n) - 需要额外的差分数组空间
    
    工程化考量:
    1. 输入处理: 使用高效的输入处理方式
    2. 边界处理: 确保数组索引正确
    3. 性能优化: 使用差分数组避免重复计算
    4. 输出格式: 按照题目要求输出结果
    """
    # 读取输入
    n, m = map(int, input().split())
    
    arr = list(map(int, input().split()))
    
    operations = []
    for _ in range(m):
        l, r, c = map(int, input().split())
        operations.append([l, r, c])
    
    result = process_difference(n, m, arr, operations)
    
    # 输出结果
    print(' '.join(map(str, result)))


if __name__ == "__main__":
    main()
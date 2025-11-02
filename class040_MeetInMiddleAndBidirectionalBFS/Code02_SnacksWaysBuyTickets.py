# 牛牛的背包问题 & 世界冰球锦标赛
# 牛牛准备参加学校组织的春游, 出发前牛牛准备往背包里装入一些零食, 牛牛的背包容量为w。
# 牛牛家里一共有n袋零食, 第i袋零食体积为v[i]。
# 牛牛想知道在总体积不超过背包容量的情况下,他一共有多少种零食放法(总体积为0也算一种放法)。
# 输入描述：
# 输入包括两行
# 第一行为两个正整数n和w(1 <= n <= 30, 1 <= w <= 2 * 10^9),表示零食的数量和背包的容量
# 第二行n个正整数v[i](0 <= v[i] <= 10^9),表示每袋零食的体积
# 输出描述：
# 输出一个正整数, 表示牛牛一共有多少种零食放法。
# 测试链接 : https://www.luogu.com.cn/problem/P4799
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
# 然后通过双指针技术合并结果
# 时间复杂度：O(2^(n/2) * log(2^(n/2))) = O(n * 2^(n/2))
# 空间复杂度：O(2^(n/2))

import sys
from typing import List

def f(arr: List[int], start: int, end: int, s: int, w: int, ans: List[int], j: int) -> int:
    """
    递归计算数组指定范围内所有可能的和
    
    Args:
        arr: 输入数组
        start: 起始索引
        end: 结束索引
        s: 当前累积和
        w: 背包容量上限
        ans: 存储结果的数组
        j: 当前在结果数组中的位置
    
    Returns:
        结果数组的新位置
    """
    # 剪枝：如果当前和已经超过背包容量，直接返回
    if s > w:
        return j
    
    if start == end:
        # 到达边界，将当前和加入结果数组
        ans[j] = s
        return j + 1
    else:
        # 不要arr[start]位置的数
        j = f(arr, start + 1, end, s, w, ans, j)
        # 要arr[start]位置的数
        j = f(arr, start + 1, end, s + arr[start], w, ans, j)
        return j

def compute(arr: List[int], n: int, w: int) -> int:
    """
    计算满足条件的零食放法数量
    使用折半搜索算法，将数组分为两部分分别处理
    
    Args:
        arr: 零食体积数组
        n: 零食数量
        w: 背包容量
    
    Returns:
        满足条件的方案数
    """
    # 初始化结果数组
    MAXM = 1 << 20
    lsum = [0] * MAXM
    rsum = [0] * MAXM
    
    # 分别计算前半部分和后半部分的所有可能和
    lsize = f(arr, 0, n // 2, 0, w, lsum, 0)
    rsize = f(arr, n // 2, n, 0, w, rsum, 0)
    
    # 对两个数组进行排序，为双指针合并做准备
    lsum[:lsize] = sorted(lsum[:lsize])
    rsum[:rsize] = sorted(rsum[:rsize])
    
    # 使用双指针技术计算满足条件的组合数
    ans = 0
    j = 0
    for i in range(lsize - 1, -1, -1):
        # 移动右指针，找到所有满足条件的组合
        while j < rsize and lsum[i] + rsum[j] <= w:
            j += 1
        # 累加满足条件的组合数
        ans += j
    
    return ans

# 测试方法
def test():
    # 测试用例：n=5, w=1000, arr=[100, 1500, 500, 500, 1000]
    # 预期输出：8
    n = 5
    w = 1000
    arr = [100, 1500, 500, 500, 1000]
    
    result = compute(arr, n, w)
    print("测试用例:")
    print("n=5, w=1000")
    print("arr=[100, 1500, 500, 500, 1000]")
    print("预期输出: 8")
    print("实际输出:", result)

# 主函数（如果需要处理输入输出）
def main():
    # 由于Python的输入处理与原题的StreamTokenizer不完全一致，这里只提供测试
    test()

if __name__ == "__main__":
    main()
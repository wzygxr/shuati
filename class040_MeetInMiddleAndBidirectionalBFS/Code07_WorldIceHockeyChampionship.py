# 世界冰球锦标赛
# 题目来源：洛谷 P4799 (CEOI2015 Day2)
# 题目描述：
# 有 n 场比赛，第 i 场比赛的门票价格为 a_i。Bobek 有 m 元钱，问他有多少种不同的观赛方案。
# 方案可以是空方案，但不能超过他的钱数。
# 测试链接 : https://www.luogu.com.cn/problem/P4799
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
# 然后对其中一半进行排序，通过二分查找找到符合条件的组合数目
# 时间复杂度：O(2^(n/2) * log(2^(n/2))) = O(2^(n/2) * n)
# 空间复杂度：O(2^(n/2))
# 
# 工程化考量：
# 1. 异常处理：检查输入是否合法
# 2. 性能优化：使用折半搜索减少搜索空间
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用列表存储子集和，使用sort方法进行排序，使用bisect模块进行二分查找

from bisect import bisect_left
from typing import List

def countWays(prices: List[int], money: int) -> int:
    """
    计算Bobek可以选择的不同观赛方案数目
    
    Args:
        prices: 各场比赛的门票价格数组
        money: Bobek拥有的钱数
    
    Returns:
        不同的观赛方案数目
    """
    # 边界条件检查
    if not prices:
        return 1  # 只有空方案一种
    
    n = len(prices)
    mid = n // 2
    
    # 分别存储左右两部分的所有可能子集和
    left_sums = []
    right_sums = []
    
    # 计算左半部分的所有可能子集和
    generateSubsetSums(prices, 0, mid - 1, 0, left_sums, money)
    
    # 计算右半部分的所有可能子集和
    generateSubsetSums(prices, mid, n - 1, 0, right_sums, money)
    
    # 对右半部分的子集和进行排序，以便进行二分查找
    right_sums.sort()
    
    # 统计符合条件的组合数目
    count = 0
    for left_sum in left_sums:
        # 查找右半部分中不超过(money - left_sum)的最大子集和的位置
        remaining = money - left_sum
        if remaining < 0:
            continue
        
        # 二分查找找到第一个大于remaining的位置
        index = bisect_left(right_sums, remaining + 1)
        
        # 所有小于等于remaining的子集和都符合条件
        count += index
    
    return count

def generateSubsetSums(prices: List[int], start: int, end: int, current_sum: int, sums: List[int], money: int) -> None:
    """
    递归生成指定范围内所有可能的子集和
    
    Args:
        prices: 门票价格数组
        start: 起始索引
        end: 结束索引
        current_sum: 当前累积和
        sums: 存储结果的列表
        money: 最大允许的钱数
    """
    # 递归终止条件
    if start > end:
        sums.append(current_sum)
        return
    
    # 剪枝：如果当前和已经超过了money，就不再继续搜索
    if current_sum > money:
        return
    
    # 不选择当前比赛
    generateSubsetSums(prices, start + 1, end, current_sum, sums, money)
    
    # 剪枝：如果选择当前比赛后会超过money，就不再选择
    if current_sum + prices[start] <= money:
        # 选择当前比赛
        generateSubsetSums(prices, start + 1, end, current_sum + prices[start], sums, money)

# 测试方法
def main():
    # 读取输入
    print("请输入比赛场次n和Bobek的钱数m：")
    n, m = map(int, input().split())
    
    print("请输入每场比赛的门票价格：")
    prices = list(map(int, input().split()))
    
    # 计算结果
    result = countWays(prices, m)
    print(f"不同的观赛方案数目：{result}")
    
    # 测试用例1
    print("\n测试用例1：")
    prices1 = [1, 2, 3, 4]
    money1 = 5
    print(f"比赛门票价格：{prices1}")
    print(f"Bobek的钱数：{money1}")
    print("期望输出：7")  # 空方案, {1}, {2}, {3}, {4}, {1,2}, {1,3}
    print(f"实际输出：{countWays(prices1, money1)}")
    
    # 测试用例2
    print("\n测试用例2：")
    prices2 = [1000000000, 1000000000, 1000000000]
    money2 = 1000000000
    print(f"比赛门票价格：{prices2}")
    print(f"Bobek的钱数：{money2}")
    print("期望输出：4")  # 空方案, {1000000000}, {1000000000}, {1000000000}
    print(f"实际输出：{countWays(prices2, money2)}")

if __name__ == "__main__":
    main()
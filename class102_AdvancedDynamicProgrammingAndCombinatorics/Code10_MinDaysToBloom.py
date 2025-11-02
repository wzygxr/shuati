# 制作 m 束花所需的最少天数
# 给你一个整数数组 bloomDay，以及两个整数 m 和 k 。
# 现需要制作 m 束花。制作花束时，需要使用花园中相邻的 k 朵花 。
# 花园中有 n 朵花，第 i 朵花会在 bloomDay[i] 时盛开，恰好可以用于一束花中。
# 请你返回从花园中摘 m 束花需要等待的最少的天数。如果不能摘到 m 束花则返回 -1 。

# 算法思路：
# 这是一个典型的二分答案问题。
# 1. 答案具有单调性：等待天数越多，盛开的花朵越多，能制作的花束也越多
# 2. 二分搜索答案的范围：左边界是数组中的最小值，右边界是数组中的最大值
# 3. 对于每个中间值，使用贪心算法检查是否能在该天数内制作出 m 束花
# 
# 时间复杂度：O(n * log(max-min))
# 空间复杂度：O(1)
# 
# 测试链接 : https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/

def can_make_bouquets(bloom_day, m, k, days):
    """
    检查是否能在给定天数内制作出指定数量的花束
    使用贪心算法实现
    
    Args:
        bloom_day (List[int]): 每朵花盛开的天数
        m (int): 需要制作的花束数量
        k (int): 每束花需要的相邻花朵数量
        days (int): 给定的天数
    
    Returns:
        bool: 是否能在给定天数内制作出指定数量的花束
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    bouquets = 0   # 已制作的花束数量
    consecutive = 0 # 当前连续盛开的花朵数量
    
    for day in bloom_day:
        if day <= days:
            # 如果当前花朵在给定天数内盛开
            consecutive += 1
            
            # 如果连续盛开的花朵数量达到了 k 朵，可以制作一束花
            if consecutive == k:
                bouquets += 1
                consecutive = 0  # 重置连续计数
        else:
            # 如果当前花朵在给定天数内未盛开，重置连续计数
            consecutive = 0
    
    # 检查是否能制作出至少 m 束花
    return bouquets >= m

def min_days_to_bloom(bloom_day, m, k):
    """
    计算制作 m 束花所需的最少天数
    
    Args:
        bloom_day (List[int]): 每朵花盛开的天数
        m (int): 需要制作的花束数量
        k (int): 每束花需要的相邻花朵数量
    
    Returns:
        int: 制作 m 束花所需的最少天数，如果不能制作则返回 -1
    
    时间复杂度：O(n * log(max-min))
    空间复杂度：O(1)
    """
    n = len(bloom_day)
    
    # 如果需要的花朵总数超过了花园中的花朵数，无法完成任务
    if m * k > n:
        return -1
    
    # 确定二分搜索的边界
    # 左边界：数组中的最小值
    # 右边界：数组中的最大值
    left = min(bloom_day)
    right = max(bloom_day)
    
    result = -1
    
    # 二分搜索答案
    while left <= right:
        mid = left + (right - left) // 2
        
        # 检查是否能在 mid 天内制作出 m 束花
        if can_make_bouquets(bloom_day, m, k, mid):
            result = mid
            right = mid - 1  # 尝试寻找更少的天数
        else:
            left = mid + 1   # 需要更多的天数
    
    return result

# 为了测试
if __name__ == "__main__":
    # 测试用例1
    bloom_day1 = [1, 10, 3, 10, 2]
    m1, k1 = 3, 1
    print(f"bloomDay: {bloom_day1}, m = {m1}, k = {k1}, 结果 = {min_days_to_bloom(bloom_day1, m1, k1)}")  # 输出: 3
    
    # 测试用例2
    bloom_day2 = [1, 10, 3, 10, 2]
    m2, k2 = 3, 2
    print(f"bloomDay: {bloom_day2}, m = {m2}, k = {k2}, 结果 = {min_days_to_bloom(bloom_day2, m2, k2)}")  # 输出: -1
    
    # 测试用例3
    bloom_day3 = [7, 7, 7, 7, 12, 7, 7]
    m3, k3 = 2, 3
    print(f"bloomDay: {bloom_day3}, m = {m3}, k = {k3}, 结果 = {min_days_to_bloom(bloom_day3, m3, k3)}")  # 输出: 12
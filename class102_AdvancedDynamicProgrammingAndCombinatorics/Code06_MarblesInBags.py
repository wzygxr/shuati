# 将珠子放进背包中
# 给定一个长度为n的数组weights，背包一共有k个
# 其中weights[i]是第i个珠子的重量
# 请你按照如下规则将所有的珠子放进k个背包
# 1，没有背包是空的
# 2，如果第i个珠子和第j个珠子在同一个背包里，那么i到j所有珠子都要在这个背包里
# 一个背包如果包含i到j的所有珠子，这个背包的价格是weights[i]+weights[j]
# 一个珠子分配方案的分数，是所有k个背包的价格之和
# 请返回所有分配方案中，最大分数与最小分数的差值为多少
# 1 <= n, k <= 10^5
# 
# 算法思路：
# 这是一个贪心算法问题
# 1. 分析问题：将n个珠子分成k个连续子数组，每个子数组的价值是首尾元素之和
# 2. 关键观察：相邻两个分割点之间会形成一个子数组，其价值是这两个分割点对应元素之和
# 3. 转化问题：选择k-1个分割点，使得最大分数和最小分数的差值最小
# 4. 贪心策略：计算所有相邻元素之和，排序后取最大的k-1个和最小的k-1个计算差值
# 
# 时间复杂度：O(n*log(n))
# 空间复杂度：O(n)
# 
# 测试链接 : https://leetcode.cn/problems/put-marbles-in-bags/

def putMarbles(weights, k):
    """
    计算最大分数与最小分数的差值
    
    算法思路：
    1. 计算所有相邻元素之和，这些和代表了可能的分割点价值
    2. 将这些和排序
    3. 取最大的k-1个和最小的k-1个计算差值
    
    时间复杂度：O(n*log(n))
    空间复杂度：O(n)
    
    Args:
        weights (List[int]): 珠子重量数组
        k (int): 背包数量
    
    Returns:
        int: 最大分数与最小分数的差值
    """
    n = len(weights)
    
    # 计算所有相邻元素之和
    split = []
    for i in range(1, n):
        split.append(weights[i - 1] + weights[i])
    
    # 排序
    split.sort()
    
    # 计算最大分数和最小分数的差值
    small = 0
    big = 0
    for i in range(k - 1):
        small += split[i]
        big += split[n - 2 - i]
    return big - small

# 测试代码
if __name__ == "__main__":
    # 示例测试
    weights = [1, 3, 5, 1]
    k = 2
    result = putMarbles(weights, k)
    print(f"weights = {weights}, k = {k}")
    print(f"结果: {result}")
    
    # 另一个示例
    weights = [1, 3]
    k = 2
    result = putMarbles(weights, k)
    print(f"weights = {weights}, k = {k}")
    print(f"结果: {result}")
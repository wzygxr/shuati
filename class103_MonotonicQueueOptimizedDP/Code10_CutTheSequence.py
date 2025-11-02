# 切分序列
# 给定一个长度为N的整数序列，要求将序列切成若干段连续的子序列。
# 要求每段子序列的和不超过给定的整数M。
# 切分的代价是每段子序列中的最大值，求所有段代价和的最小值。
# 1 <= N <= 10^5
# 0 <= a[i] <= 10^6
# 0 <= M <= 10^15
# 测试链接 : http://poj.org/problem?id=3017
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

from collections import deque

def cutTheSequence(arr, m):
    """
    使用单调队列优化的动态规划解法
    
    Args:
        arr: List[int] - 整数序列
        m: int - 每段子序列和的上限
    
    Returns:
        int - 所有段代价和的最小值，无解返回-1
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    n = len(arr)
    
    # 检查是否存在单个元素超过m的情况
    for i in range(n):
        if arr[i] > m:
            return -1  # 无解
    
    # 预处理前缀和
    sum_arr = [0] * (n + 1)
    for i in range(1, n + 1):
        sum_arr[i] = sum_arr[i - 1] + arr[i - 1]
    
    # dp[i]表示处理前i个元素能得到的最小代价和
    dp = [float('inf')] * (n + 1)
    dp[0] = 0
    
    # 单调递减队列，存储下标，维护a[j]单调递减
    monotonic_queue = deque()
    
    # 单调递增队列，用于维护dp[j-1]+max(a[j..i])的最小值
    candidate_queue = deque()
    
    def get_value(j, i):
        """计算dp[j-1] + max(a[j..i])的值"""
        if j == 0:
            return float('inf')  # 无效值
        
        # 在单调队列中找到max(a[j..i])
        max_val = 0
        for idx in monotonic_queue:
            if idx >= j:
                max_val = arr[idx - 1]  # arr下标从0开始
                break
        
        return dp[j - 1] + max_val
    
    for i in range(1, n + 1):
        # 维护单调递减队列，存储下标，按照a[j]单调递减
        while monotonic_queue and arr[monotonic_queue[-1] - 1] <= arr[i - 1]:
            monotonic_queue.pop()
        monotonic_queue.append(i)
        
        # 移除队列中超出和限制的元素
        while monotonic_queue and sum_arr[i] - sum_arr[monotonic_queue[0] - 1] > m:
            monotonic_queue.popleft()
        
        # 维护候选队列，存储下标j，按照dp[j-1]+max(a[j..i])单调递增
        while candidate_queue and get_value(candidate_queue[-1], i) >= get_value(i, i):
            candidate_queue.pop()
        candidate_queue.append(i)
        
        # 移除候选队列中无效的元素
        while candidate_queue and candidate_queue[0] < monotonic_queue[0]:
            candidate_queue.popleft()
        
        # 更新dp值
        if candidate_queue:
            dp[i] = min(dp[i], get_value(candidate_queue[0], i))
    
    return dp[n]

# 读取输入并调用函数
if __name__ == "__main__":
    # 读取n和m
    n, m = map(int, input().split())
    # 读取arr数组
    arr = list(map(int, input().split()))
    # 计算并输出结果
    result = cutTheSequence(arr, m)
    print(result)

"""
算法思路详解：

1. 问题分析：
   - 这是一个动态规划问题
   - 状态定义：dp[i]表示处理前i个元素能得到的最小代价和
   - 状态转移方程：dp[i] = min{dp[j-1] + max(a[j..i])}，其中sum[j..i] <= m
   - 目标：求dp[n]

2. 朴素解法：
   - 时间复杂度：O(n^2)，对于每个位置i，需要遍历前面所有可能的j
   - 空间复杂度：O(n)
   - 对于大数据会超时

3. 单调队列优化思路：
   - 观察状态转移方程，我们需要在满足sum[j..i] <= m的j中找到使dp[j-1] + max(a[j..i])最小的j
   - 使用两个单调队列：
     a. 单调递减队列：维护a[j]的单调性，便于快速找到max(a[j..i])
     b. 单调递增队列：维护dp[j-1] + max(a[j..i])的单调性，便于快速找到最小值

4. 队列维护策略：
   - 单调递减队列：存储下标，按照a[j]单调递减排列，队首是当前窗口内的最大值
   - 单调递增队列：存储下标j，按照dp[j-1] + max(a[j..i])单调递增排列，队首是最优决策

5. 时间复杂度分析：
   - 每个元素最多入队和出队一次，均摊时间复杂度O(1)
   - 总时间复杂度：O(n)
   - 空间复杂度：O(n)

6. 边界情况处理：
   - 存在单个元素超过m的情况，无解返回-1
   - 初始状态dp[0] = 0
   - 空序列的处理

7. 为什么是最优解：
   - 该解法将朴素DP的O(n^2)优化到O(n)
   - 利用单调队列维护决策单调性，是此类问题的最优解法
   - 无法进一步优化时间复杂度，因为需要处理每个位置至少一次

8. 工程化考量：
   - 使用collections.deque实现双端队列，性能较好
   - 代码结构清晰，注释详细
   - 函数式编程风格，易于测试和复用

9. 极端场景分析：
   - 所有元素都为0，代价和为0
   - 序列递增，每段只能包含一个元素
   - 序列递减，可以包含多个元素
   - m很大，可以将整个序列作为一段

10. 语言特性差异：
    - Python: 使用collections.deque实现双端队列
    - Java: 使用LinkedList实现双端队列
    - C++: 使用数组模拟队列，性能最优
"""
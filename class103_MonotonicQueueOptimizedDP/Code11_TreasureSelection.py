# 宝物筛选
# 小FF有一个最大载重为W的采集车，洞穴里总共有n种宝物，
# 每种宝物的价值为v[i]，重量为w[i]，每种宝物有m[i]件。
# 每件宝物都只能使用一次，求采集车能装下的宝物的最大价值总和。
# 这是经典的多重背包问题，使用单调队列优化。
# 1 <= n <= 100
# 1 <= W <= 40000
# 1 <= v[i], w[i], m[i] <= 100
# 测试链接 : https://www.luogu.com.cn/problem/P1776
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

from collections import deque

def treasureSelection(v, w, m, W):
    """
    使用单调队列优化的多重背包解法
    
    Args:
        v: List[int] - 物品价值列表
        w: List[int] - 物品重量列表
        m: List[int] - 物品数量列表
        W: int - 背包容量
    
    Returns:
        int - 能装下的宝物的最大价值总和
    
    时间复杂度：O(n*W)
    空间复杂度：O(W)
    """
    n = len(v)
    
    # dp[j]表示重量不超过j时能获得的最大价值
    dp = [0] * (W + 1)
    
    # 对每种物品进行处理
    for i in range(n):
        # 按照重量w[i]的余数进行分组处理
        for r in range(w[i]):
            # 使用单调队列优化
            queue = deque()  # 存储项数k
            
            # 对于余数为r的组，处理所有重量为k*w[i]+r的形式
            k = 0
            while k * w[i] + r <= W:
                j = k * w[i] + r  # 当前重量
                value = dp[j] - k * v[i]  # 价值调整
                
                # 维护单调递减队列
                while queue and dp[queue[-1]] - (queue[-1] - r) // w[i] * v[i] <= value:
                    queue.pop()
                queue.append(k)
                
                # 移除超出数量限制的队首元素
                while queue and queue[0] < k - m[i]:
                    queue.popleft()
                
                # 更新dp值
                if queue:
                    front = queue[0]
                    dp[j] = max(dp[j], dp[front * w[i] + r] + (k - front) * v[i])
                
                k += 1
    
    return dp[W]

# 读取输入并调用函数
if __name__ == "__main__":
    # 读取n和W
    n, W = map(int, input().split())
    # 读取物品信息
    v, w, m = [], [], []
    for _ in range(n):
        vi, wi, mi = map(int, input().split())
        v.append(vi)
        w.append(wi)
        m.append(mi)
    # 计算并输出结果
    result = treasureSelection(v, w, m, W)
    print(result)

"""
算法思路详解：

1. 问题分析：
   - 这是经典的多重背包问题
   - 状态定义：dp[j]表示重量不超过j时能获得的最大价值
   - 状态转移方程：dp[j] = max{dp[j-k*w[i]] + k*v[i]}，其中0 <= k <= min(m[i], j/w[i])
   - 目标：求dp[W]

2. 朴素解法：
   - 时间复杂度：O(n*W*max(m[i]))，对于每个物品需要枚举选择数量
   - 空间复杂度：O(W)
   - 对于m[i]较大时会超时

3. 二进制优化解法：
   - 将m[i]件物品拆分成O(log m[i])件物品
   - 时间复杂度：O(n*W*log(max(m[i])))
   - 空间复杂度：O(W)

4. 单调队列优化思路：
   - 观察状态转移方程，我们可以按w[i]的余数进行分组
   - 对于余数相同的重量，可以看作一个等差数列
   - 使用单调队列维护决策单调性

5. 优化策略：
   - 按照w[i]的余数将重量分为w[i]组
   - 对每组使用单调队列优化
   - 队列中存储的是等差数列的项数

6. 队列维护策略：
   - 队列存储项数k，按照dp[k*w[i]+r]-k*v[i]单调递减排列
   - 队首元素：当前窗口内的最优决策
   - 队尾维护：移除所有价值小于等于当前价值的元素
   - 数量限制维护：移除超出m[i]数量限制的队首元素

7. 时间复杂度分析：
   - 每个重量最多入队和出队一次，均摊时间复杂度O(1)
   - 总时间复杂度：O(n*W)
   - 空间复杂度：O(W)

8. 为什么是最优解：
   - 该解法将朴素DP的O(n*W*max(m[i]))优化到O(n*W)
   - 利用单调队列维护决策单调性，是多重背包的最优解法之一
   - 比二进制优化更优，因为没有log因子

9. 工程化考量：
   - 使用collections.deque实现双端队列，性能较好
   - 代码结构清晰，注释详细
   - 函数式编程风格，易于测试和复用

10. 极端场景分析：
    - W=1时，只能选择重量为1的物品
    - m[i]=1时，退化为01背包
    - m[i]*w[i]>=W时，可以看作完全背包

11. 语言特性差异：
    - Python: 使用collections.deque实现双端队列
    - Java: 使用LinkedList实现双端队列
    - C++: 使用数组模拟队列，性能最优
"""
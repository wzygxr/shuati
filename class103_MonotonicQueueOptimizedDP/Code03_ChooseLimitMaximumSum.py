# 不超过连续k个元素的最大累加和
# 给定一个长度为n的数组arr，你可以随意选择数字
# 要求选择的方案中，连续选择的个数不能超过k个
# 返回能得到的最大累加和
# 1 <= n、k <= 10^5
# 0 <= arr[i] <= 10^9
# 测试链接 : https://www.luogu.com.cn/problem/P2627
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import sys
from collections import deque

class Code03_ChooseLimitMaximumSum:
    
    def __init__(self):
        # 最大数组长度常量
        self.MAXN = 100001
        
        # 输入数组
        self.arr = [0] * self.MAXN
        
        # 前缀和数组，sum[i]表示前i个元素的和
        self.sum = [0] * self.MAXN
        
        # dp数组，dp[i]表示前i个元素能得到的最大累加和
        self.dp = [0] * self.MAXN
        
        # 输入参数
        self.n = 0
        self.k = 0
    
    def value(self, i):
        """
        计算位置i对应的指标值
        指标值用于比较不同决策点的优劣
        
        Args:
            i: 位置下标
        
        Returns:
            long: 位置i对应的指标值
        """
        # 当i=0时，指标值为0（初始状态）
        # 当i>0时，指标值为dp[i-1] - sum[i]（表示从位置i开始选择的收益）
        if i == 0:
            return 0
        else:
            return self.dp[i - 1] - self.sum[i]
    
    def compute(self):
        """
        计算不超过连续k个元素的最大累加和
        使用单调队列优化的动态规划解法
        时间复杂度：O(n)，每个元素最多入队和出队一次
        空间复杂度：O(n)，dp数组、前缀和数组和单调队列的空间
        
        Returns:
            long: 能得到的最大累加和
        """
        # 预处理前缀和数组
        for i in range(1, self.n + 1):
            self.sum[i] = self.sum[i - 1] + self.arr[i]
        
        # 初始化队列，0位置作为初始决策点
        dq = deque()
        dq.append(0)
        
        # 动态规划过程
        for i in range(1, self.n + 1):
            # 维护队列单调性（递减）
            # 移除所有value值小于等于当前value(i)的队尾元素
            while dq and self.value(dq[-1]) <= self.value(i):
                dq.pop()
            
            # 将位置i加入队列
            dq.append(i)
            
            # 移除过期的决策点（超出k个连续元素限制）
            if dq and dq[0] == i - self.k - 1:
                dq.popleft()
            
            # 状态转移：dp[i] = max{dp[j-1] + sum[i] - sum[j]} for j in [max(1, i-k), i]
            # 等价于：dp[i] = max{value(j) + sum[i]} for j in [max(0, i-k-1), i-1]
            self.dp[i] = self.value(dq[0]) + self.sum[i]
        
        return self.dp[self.n]
    
    def main(self):
        """主函数，处理输入输出"""
        # 读取输入参数
        data = sys.stdin.read().split()
        idx = 0
        
        self.n = int(data[idx]); idx += 1
        self.k = int(data[idx]); idx += 1
        
        # 读取数组元素
        for i in range(1, self.n + 1):
            self.arr[i] = int(data[idx]); idx += 1
        
        # 输出计算结果
        print(self.compute())

# 如果直接运行此文件，则执行主函数
if __name__ == "__main__":
    solution = Code03_ChooseLimitMaximumSum()
    solution.main()

"""
算法思路详解：

1. 问题分析：
   - 这是一个动态规划问题，带有约束条件
   - 状态定义：dp[i]表示前i个元素能得到的最大累加和
   - 状态转移方程：dp[i] = max{dp[j-1] + sum[i] - sum[j]}，其中j ∈ [max(1, i-k), i]
   - 目标：求dp[n]

2. 朴素解法：
   - 时间复杂度：O(n*k)，对于每个位置i，需要遍历前面k个位置找最大值
   - 空间复杂度：O(n)
   - 对于大数据会超时

3. 数学变换优化：
   - 将状态转移方程变形：dp[i] = max{dp[j-1] - sum[j]} + sum[i]
   - 令value(j) = dp[j-1] - sum[j]，则dp[i] = max{value(j)} + sum[i]
   - 这样就将问题转化为在滑动窗口内找value的最大值

4. 单调队列优化：
   - 观察优化后的状态转移方程，我们需要在滑动窗口[max(0, i-k-1), i-1]中找到value的最大值
   - 这正是单调队列的经典应用场景
   - 使用单调递减队列，队首始终是窗口内的最大value值

5. 队列维护策略：
   - 队列存储下标，按照value值单调递减排列
   - 队首元素：窗口内的最大value值对应的下标
   - 队尾维护：移除所有value值小于等于当前value[i]的元素
   - 有效性维护：移除超出k个连续元素限制的队首元素

6. 时间复杂度分析：
   - 每个元素最多入队和出队一次，均摊时间复杂度O(1)
   - 总时间复杂度：O(n)
   - 空间复杂度：O(n)

7. 边界情况处理：
   - 初始状态：dp[0] = 0，value(0) = 0
   - 空数组：n=0时，返回0
   - 全负数数组：仍能正确找到最大累加和（可能为0）

8. 为什么是最优解：
   - 该解法将朴素DP的O(n*k)优化到O(n)
   - 利用单调队列维护滑动窗口最值，是此类问题的最优解法
   - 无法进一步优化时间复杂度，因为需要处理每个位置至少一次

9. 工程化考量：
   - 使用前缀和优化区间和计算
   - 输入输出使用高效IO，避免超时
   - 使用Python的int类型自动处理大数
   - 使用collections.deque实现双端队列

10. 极端场景分析：
    - k=1时，不能连续选择元素，退化为选择最大非负元素
    - k=n时，可以任意选择元素，退化为选择所有非负元素
    - 全正数数组：选择所有元素
    - 全负数数组：选择空集，和为0

11. 语言特性差异：
    - Python: 使用collections.deque实现双端队列
    - Java: 使用数组模拟队列
    - C++: 使用deque或数组模拟队列

12. 性能优化技巧：
    - 使用局部变量减少属性访问次数
    - 避免不必要的函数调用
    - 使用列表预分配空间

13. 调试技巧：
    - 打印value数组验证计算正确性
    - 检查队列维护的单调性
    - 验证边界情况的处理
    - 使用小规模数据测试算法正确性

14. 常见错误：
    - 忘记处理k=0或n=0的边界情况
    - 队列维护时下标越界
    - 大数溢出问题
    - 队列单调性维护错误

15. 扩展应用：
    - 类似思路可以用于其他带约束的DP优化问题
    - 可以扩展到二维或更高维度的DP问题
    - 可以结合其他优化技巧如斜率优化
"""
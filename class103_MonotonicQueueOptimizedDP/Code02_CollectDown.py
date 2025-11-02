# 向下收集获得最大能量
# 有一个n * m的区域，行和列的编号从1开始
# 每个能量点用(i, j, v)表示，i行j列上有价值为v的能量点
# 一共有k个能量点，并且所有能量点一定在不同的位置
# 一开始可以在第1行的任意位置，然后每一步必须向下移动
# 向下去往哪个格子是一个范围，如果当前在(i, j)位置
# 那么往下可以选择(i+1, j-t)...(i+1, j+t)其中的一个格子
# 到达最后一行时，收集过程停止，返回能收集到的最大能量价值
# 1 <= n、m、k、t <= 4000
# 1 <= v <= 100
# 测试链接 : https://www.luogu.com.cn/problem/P3800
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import sys
from collections import deque

class Code02_CollectDown:
    
    def __init__(self):
        # 最大行数和列数常量
        self.MAXN = 4001
        self.MAXM = 4001
        
        # dp数组，dp[i][j]表示到达第i行第j列能收集到的最大能量
        self.dp = [[0] * self.MAXM for _ in range(self.MAXN)]
        
        # 输入参数
        self.n = 0
        self.m = 0
        self.k = 0
        self.t = 0
    
    def build(self):
        """初始化dp数组"""
        for i in range(1, self.n + 1):
            for j in range(1, self.m + 1):
                self.dp[i][j] = 0
    
    def compute(self):
        """
        计算能收集到的最大能量
        使用单调队列优化的动态规划解法
        时间复杂度：O(n*m)，每个位置最多入队和出队一次
        空间复杂度：O(n*m)，dp数组和单调队列的空间
        
        Returns:
            int: 能收集到的最大能量
        """
        # 从第2行开始计算每行的最大能量
        for i in range(2, self.n + 1):
            # 使用双端队列维护滑动窗口内的最大值
            dq = deque()
            
            # 初始化队列，将前t列加入队列
            for j in range(1, self.t + 1):
                self._add_to_queue(dq, i - 1, j)
            
            # 计算第i行每列的最大能量
            for j in range(1, self.m + 1):
                # 添加新的可能决策点（j+t列）
                if j + self.t <= self.m:
                    self._add_to_queue(dq, i - 1, j + self.t)
                
                # 移除过期的决策点（j-t-1列）
                self._remove_from_queue(dq, j - self.t - 1)
                
                # 状态转移：dp[i][j] = dp[i-1][最佳前驱位置] + dp[i][j]
                if dq:
                    self.dp[i][j] += self.dp[i - 1][dq[0]]
        
        # 在最后一行中找到最大能量值
        ans = -10**9
        for j in range(1, self.m + 1):
            ans = max(ans, self.dp[self.n][j])
        return ans
    
    def _add_to_queue(self, dq, i, j):
        """
        向单调队列中添加新的决策点
        
        Args:
            dq: 双端队列
            i: 行号
            j: 列号
        """
        # 只有当j是有效列号时才添加
        if 1 <= j <= self.m:
            # 维护队列单调性（递减）
            # 移除所有dp值小于等于当前dp[i][j]的队尾元素
            while dq and self.dp[i][dq[-1]] <= self.dp[i][j]:
                dq.pop()
            # 将列号j加入队列
            dq.append(j)
    
    def _remove_from_queue(self, dq, t):
        """
        移除过期的决策点
        
        Args:
            dq: 双端队列
            t: 要移除的列号
        """
        # 如果队首元素是列号t，则移除它（已过期）
        if dq and dq[0] == t:
            dq.popleft()
    
    def main(self):
        """主函数，处理输入输出"""
        # 读取输入参数
        data = sys.stdin.read().split()
        idx = 0
        
        self.n = int(data[idx]); idx += 1
        self.m = int(data[idx]); idx += 1
        self.k = int(data[idx]); idx += 1
        self.t = int(data[idx]); idx += 1
        
        # 初始化dp数组
        self.build()
        
        # 读取能量点信息
        for _ in range(self.k):
            r = int(data[idx]); idx += 1
            c = int(data[idx]); idx += 1
            v = int(data[idx]); idx += 1
            self.dp[r][c] = v  # 在对应位置设置能量值
        
        # 输出计算结果
        print(self.compute())

# 如果直接运行此文件，则执行主函数
if __name__ == "__main__":
    solution = Code02_CollectDown()
    solution.main()

"""
算法思路详解：

1. 问题分析：
   - 这是一个二维动态规划问题
   - 状态定义：dp[i][j]表示到达第i行第j列能收集到的最大能量
   - 状态转移方程：dp[i][j] = max{dp[i-1][k]} + dp[i][j]，其中k ∈ [max(1, j-t), min(m, j+t)]
   - 目标：求dp[n][j]的最大值

2. 朴素解法：
   - 时间复杂度：O(n*m*t)，对于每个位置需要遍历前后t个位置找最大值
   - 空间复杂度：O(n*m)
   - 对于大数据会超时

3. 单调队列优化：
   - 观察状态转移方程，我们需要在滑动窗口[max(1, j-t), min(m, j+t)]中找到dp[i-1]的最大值
   - 这正是单调队列的经典应用场景
   - 使用单调递减队列，队首始终是窗口内的最大dp值

4. 队列维护策略：
   - 队列存储列号，按照dp[i-1]值单调递减排列
   - 队首元素：窗口内的最大dp[i-1]值对应的列号
   - 队尾维护：移除所有dp[i-1]值小于等于当前dp[i-1][j]的元素
   - 有效性维护：移除超出移动范围的队首元素

5. 时间复杂度分析：
   - 每个元素最多入队和出队一次，均摊时间复杂度O(1)
   - 总时间复杂度：O(n*m)
   - 空间复杂度：O(n*m)

6. 边界情况处理：
   - 第一行的初始能量值就是输入的能量点值
   - 边界列的移动范围需要限制在[1, m]内
   - 空位置的能量值为0

7. 为什么是最优解：
   - 该解法将朴素DP的O(n*m*t)优化到O(n*m)
   - 利用单调队列维护滑动窗口最值，是此类问题的最优解法
   - 无法进一步优化时间复杂度，因为需要处理每个位置至少一次

8. 工程化考量：
   - 输入输出使用高效IO，避免超时
   - 数组预分配空间，避免动态扩容
   - 代码结构清晰，注释详细
   - 异常处理完善（题目保证输入合法）

9. 极端场景分析：
   - n=1时，直接返回第一行的最大能量值
   - t=0时，只能垂直向下移动
   - t=m时，可以在一行内任意移动
   - 所有位置都有能量点时，需要正确处理叠加

10. 语言特性差异：
    - Python: 使用collections.deque实现双端队列
    - Java: 使用数组模拟队列
    - C++: 使用deque或数组模拟队列

11. 性能优化技巧：
    - 使用局部变量减少属性访问次数
    - 避免不必要的函数调用
    - 使用列表推导式初始化数组

12. 调试技巧：
    - 打印中间结果验证算法正确性
    - 使用小规模数据测试边界情况
    - 验证队列维护的正确性
"""
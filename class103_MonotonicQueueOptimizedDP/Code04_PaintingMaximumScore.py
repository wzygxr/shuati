# 粉刷木板的最大收益
# 一共有n个木板，每个木板长度为1，最多粉刷一次，也可以不刷
# 一共有m个工人，每个工人用(li, pi, si)表示：
# 该工人必须刷连续区域的木板，并且连续的长度不超过li
# 该工人每刷一块木板可以得到pi的钱
# 该工人刷的连续区域必须包含si位置的木板
# 返回所有工人最多能获得多少钱
# 1 <= n <= 16000
# 1 <= m <= 100
# 1 <= pi <= 10000
# 测试链接 : http://poj.org/problem?id=1821
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import sys
from collections import deque

class Code04_PaintingMaximumScore:
    
    def __init__(self):
        # 最大木板数常量
        self.MAXN = 16001
        
        # 最大工人数常量
        self.MAXM = 101
        
        # 工人信息数组，workers[i] = [li, pi, si]
        self.workers = [[0, 0, 0] for _ in range(self.MAXM)]
        
        # dp数组，dp[i][j]表示前i个工人刷前j块木板能获得的最大收益
        self.dp = [[0] * self.MAXN for _ in range(self.MAXM)]
        
        # 输入参数
        self.n = 0
        self.m = 0
    
    def value(self, i, pi, j):
        """
        计算工人i从位置j开始刷木板时的指标值
        指标值用于比较不同起始位置的优劣
        
        Args:
            i: 工人编号
            pi: 工人i每刷一块木板的收益
            j: 起始位置
        
        Returns:
            int: 位置j对应的指标值
        """
        # 指标值为：前i-1个工人刷前j块木板的最大收益 - pi * j
        # 这个值越大，说明从位置j开始刷越有利
        return self.dp[i - 1][j] - pi * j
    
    def compute(self):
        """
        计算粉刷木板的最大收益
        使用单调队列优化的动态规划解法
        时间复杂度：O(m*n)，每个位置最多入队和出队一次
        空间复杂度：O(m*n)，dp数组和单调队列的空间
        
        Returns:
            int: 所有工人最多能获得的钱数
        """
        # 按照工人必须刷到的木板位置si排序
        # 这样可以确保在处理工人i时，前面的工人已经处理完毕
        self.workers[1:self.m+1] = sorted(self.workers[1:self.m+1], key=lambda x: x[2])
        
        # 动态规划过程
        for i in range(1, self.m + 1):
            li = self.workers[i][0]  # 工人i能刷的最大连续长度
            pi = self.workers[i][1]  # 工人i每刷一块木板的收益
            si = self.workers[i][2]  # 工人i必须刷到的木板位置
            
            # 使用双端队列维护滑动窗口内的最优决策点
            dq = deque()
            
            # 初始化单调队列，将工人i可以刷到的起始位置加入队列
            # 起始位置范围：[max(0, si-li), si-1]
            start_range = max(0, si - li)
            for j in range(start_range, si):
                # 维护队列单调性（递增）
                # 移除所有value值大于等于当前value(i, pi, j)的队尾元素
                while dq and self.value(i, pi, dq[-1]) <= self.value(i, pi, j):
                    dq.pop()
                # 将位置j加入队列
                dq.append(j)
            
            # 计算前i个工人刷前j块木板的最大收益
            for j in range(1, self.n + 1):
                # 不选择工人i的情况：继承前i-1个工人的结果或前j-1块木板的结果
                self.dp[i][j] = max(self.dp[i - 1][j], self.dp[i][j - 1])
                
                # 如果当前木板位置j >= 工人i必须刷到的位置si
                if j >= si:
                    # 移除过期的决策点（超出工人i能刷的最大长度）
                    if dq and dq[0] == j - li - 1:
                        dq.popleft()
                    
                    # 如果队列不为空，尝试选择工人i来刷木板
                    if dq:
                        # 选择工人i的收益：value(最优起始位置) + pi * j
                        self.dp[i][j] = max(self.dp[i][j], self.value(i, pi, dq[0]) + pi * j)
        
        return self.dp[self.m][self.n]
    
    def main(self):
        """主函数，处理输入输出"""
        # 读取输入参数（可能有多组测试数据）
        data = sys.stdin.read().split()
        idx = 0
        
        while idx < len(data):
            self.n = int(data[idx]); idx += 1
            self.m = int(data[idx]); idx += 1
            
            # 读取工人信息
            for i in range(1, self.m + 1):
                li = int(data[idx]); idx += 1
                pi = int(data[idx]); idx += 1
                si = int(data[idx]); idx += 1
                self.workers[i] = [li, pi, si]
            
            # 输出计算结果
            print(self.compute())

# 如果直接运行此文件，则执行主函数
if __name__ == "__main__":
    solution = Code04_PaintingMaximumScore()
    solution.main()

"""
算法思路详解：

1. 问题分析：
   - 这是一个二维动态规划问题，涉及工人选择和木板粉刷
   - 状态定义：dp[i][j]表示前i个工人刷前j块木板能获得的最大收益
   - 状态转移方程较为复杂，需要考虑工人是否参与粉刷
   - 目标：求dp[m][n]

2. 朴素解法：
   - 时间复杂度：O(m*n^2)，对于每个工人和每块木板，需要遍历可能的起始位置
   - 空间复杂度：O(m*n)
   - 对于大数据会超时

3. 优化思路：
   - 按照工人必须刷到的木板位置排序，确保处理顺序正确
   - 对于每个工人，使用单调队列优化起始位置的选择
   - 将问题转化为在滑动窗口内找最优起始位置

4. 单调队列优化：
   - 对于工人i，我们需要在起始位置范围[max(0, si-li), si-1]内找到最优起始位置
   - 使用单调递增队列，队首始终是窗口内的最优起始位置
   - 通过value函数比较不同起始位置的优劣

5. 队列维护策略：
   - 队列存储起始位置下标，按照value值单调递增排列
   - 队首元素：窗口内的最优起始位置
   - 队尾维护：移除所有value值大于等于当前value的元素
   - 有效性维护：移除超出工人能力范围的队首元素

6. 时间复杂度分析：
   - 每个起始位置最多入队和出队一次，均摊时间复杂度O(1)
   - 总时间复杂度：O(m*n)
   - 空间复杂度：O(m*n)

7. 边界情况处理：
   - 没有工人参与：收益为0
   - 没有木板可刷：收益为0
   - 工人能力不足：无法刷到必须刷到的位置

8. 为什么是最优解：
   - 该解法将朴素DP的O(m*n^2)优化到O(m*n)
   - 利用单调队列维护最优决策点，是此类问题的最优解法
   - 无法进一步优化时间复杂度，因为需要处理每个工人和每块木板

9. 工程化考量：
   - 按照工人必须刷到的位置排序，确保处理顺序正确
   - 输入输出使用高效IO，避免超时
   - 使用列表存储工人信息，提高代码可读性
   - 处理多组测试数据的情况

10. 极端场景分析：
    - m=1时，只有一个工人，退化为单工人问题
    - n=1时，只有一块木板，工人能力足够就能刷
    - 工人能力很强：可以刷很多木板
    - 工人能力很弱：可能无法完成任务

11. 语言特性差异：
    - Python: 使用collections.deque实现双端队列
    - Java: 使用数组模拟队列
    - C++: 使用deque或数组模拟队列

12. 性能优化技巧：
    - 使用局部变量减少属性访问次数
    - 避免不必要的函数调用
    - 使用列表预分配空间

13. 调试技巧：
    - 打印dp数组验证计算正确性
    - 检查队列维护的单调性
    - 验证边界情况的处理
    - 使用小规模数据测试算法正确性

14. 常见错误：
    - 忘记处理排序后的工人索引
    - 队列维护时下标越界
    - 多组测试数据未重置状态
    - 队列单调性维护错误

15. 扩展应用：
    - 类似思路可以用于其他带约束的DP优化问题
    - 可以扩展到二维或更高维度的DP问题
    - 可以结合其他优化技巧如斜率优化
"""
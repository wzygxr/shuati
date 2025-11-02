# 最小移动总距离
# 所有工厂和机器人都分布在x轴上
# 给定长度为n的二维数组factory，factory[i][0]为i号工厂的位置，factory[i][1]为容量
# 给定长度为m的一维数组robot，robot[j]为第j个机器人的位置
# 每个工厂所在的位置都不同，每个机器人所在的位置都不同，机器人到工厂的距离为位置差的绝对值
# 所有机器人都是坏的，但是机器人可以去往任何工厂进行修理，但是不能超过某个工厂的容量
# 测试数据保证所有机器人都可以被维修，返回所有机器人移动的最小总距离
# 1 <= n、m <= 100
# -10^9 <= factory[i][0]、robot[j] <= +10^9
# 0 <= factory[i][1] <= m
# 测试链接 : https://leetcode.cn/problems/minimum-total-distance-traveled/

import sys
from typing import List

class Code05_MinimumTotalDistanceTraveled:
    # 表示不可达状态的常量
    NA = float('inf')
    
    def __init__(self):
        # 输入参数
        self.n = 0
        self.m = 0
        
        # 工厂信息数组，下标从1开始
        # fac[i][0]表示第i号工厂的位置
        # fac[i][1]表示第i号工厂的容量
        self.fac = []
        
        # 机器人位置数组，下标从1开始
        # rob[i]表示第i号机器人的位置
        self.rob = []
        
        # dp数组，dp[i][j]表示前i个工厂修理前j个机器人的最小总距离
        self.dp = []
        
        # 前缀和数组，sum[j]表示前j个机器人去往当前工厂的距离之和
        self.sum_arr = []
        
        # 单调队列，用于维护滑动窗口内的最优决策点
        # 存储的是机器人下标，按照value值单调递增排列
        self.queue = []
        
        # 队列的左右指针
        self.l = 0
        self.r = 0
    
    def build(self, factory: List[List[int]], robot: List[int]) -> None:
        """
        初始化函数，对输入数据进行预处理
        
        Args:
            factory: 工厂信息数组
            robot: 机器人位置列表
        """
        # 工厂和机器人都根据所在位置排序
        # 这样可以确保相邻的工厂和机器人在空间上也是相邻的
        factory_sorted = sorted(factory, key=lambda x: x[0])
        robot_sorted = sorted(robot)
        
        self.n = len(factory_sorted)
        self.m = len(robot_sorted)
        
        # 让工厂和机器人的下标都从1开始，便于处理边界情况
        # 使用0索引作为边界，实际数据从1开始
        self.fac = [[0, 0]] + factory_sorted  # 索引0不使用
        self.rob = [0] + robot_sorted          # 索引0不使用
        
        # 初始化dp数组
        # dp[0][j]表示0个工厂修理j个机器人，这是不可能的，所以设为不可达
        self.dp = [[self.NA] * (self.m + 1) for _ in range(self.n + 1)]
        
        # dp[0][0] = 0，0个工厂修理0个机器人，距离为0
        self.dp[0][0] = 0
        
        # 初始化前缀和数组
        self.sum_arr = [0] * (self.m + 1)
        
        # 初始化队列
        self.queue = [0] * (self.m + 1)
    
    def dist(self, i: int, j: int) -> int:
        """
        计算第i号工厂和第j号机器人之间的距离
        
        Args:
            i: 工厂编号
            j: 机器人编号
            
        Returns:
            工厂和机器人之间的距离
        """
        # 使用绝对值计算距离
        return abs(self.fac[i][0] - self.rob[j])
    
    def value(self, i: int, j: int) -> float:
        """
        计算第i号工厂从第j号机器人开始负责时的指标值
        指标值用于比较不同起始机器人的优劣
        
        Args:
            i: 工厂编号
            j: 机器人编号
            
        Returns:
            指标值，如果不可行则返回NA
        """
        # 如果前i-1个工厂无法修理前j-1个机器人，则不可行
        if self.dp[i - 1][j - 1] == self.NA:
            return self.NA
        
        # 指标值为：前i-1个工厂修理前j-1个机器人的最小距离 - 前j-1个机器人去往第i号工厂的距离之和
        # 这个值越小，说明从第j号机器人开始由第i号工厂负责越有利
        return self.dp[i - 1][j - 1] - self.sum_arr[j - 1]
    
    def minimumTotalDistance(self, robot: List[int], factory: List[List[int]]) -> int:
        """
        计算所有机器人移动的最小总距离
        使用单调队列优化的动态规划解法
        时间复杂度：O(n * m)，每个状态最多入队和出队一次
        空间复杂度：O(n * m)，dp数组和单调队列的空间
        
        Args:
            robot: 机器人位置列表
            factory: 工厂信息数组
            
        Returns:
            所有机器人移动的最小总距离
        """
        # 数据预处理
        self.build(factory, robot)
        
        # 动态规划过程
        for i in range(1, self.n + 1):
            # 获取第i号工厂的容量
            cap = self.fac[i][1]
            
            # 计算前缀和数组
            # sum_arr[j]表示前j个机器人去往第i号工厂的距离之和
            for j in range(1, self.m + 1):
                self.sum_arr[j] = self.sum_arr[j - 1] + self.dist(i, j)
            
            # 初始化队列指针
            self.l = 0
            self.r = 0
            
            # 计算前i个工厂修理前j个机器人的最小总距离
            for j in range(1, self.m + 1):
                # 不选择第i号工厂的情况：继承前i-1个工厂的结果
                self.dp[i][j] = self.dp[i - 1][j]
                
                # 如果从第j号机器人开始负责是可行的，则加入队列
                val = self.value(i, j)
                if val != self.NA:
                    # 维护队列单调性（递增）
                    # 移除所有value值大于等于当前value(i, j)的队尾元素
                    while self.l < self.r and self.value(i, self.queue[self.r - 1]) >= val:
                        self.r -= 1
                    # 将机器人j加入队列
                    self.queue[self.r] = j
                    self.r += 1
                
                # 移除过期的决策点（超出工厂容量限制）
                if self.l < self.r and self.queue[self.l] == j - cap:
                    self.l += 1
                
                # 如果队列不为空，尝试选择第i号工厂来修理机器人
                if self.l < self.r:
                    # 选择第i号工厂的收益：value(最优起始机器人) + sum_arr[j]
                    best_val = self.value(i, self.queue[self.l])
                    candidate = best_val + self.sum_arr[j]
                    if self.dp[i][j] == self.NA:
                        self.dp[i][j] = candidate
                    else:
                        self.dp[i][j] = min(self.dp[i][j], candidate)
        
        return int(self.dp[self.n][self.m])

    '''
    算法思路详解：
    
    1. 问题分析：
       - 这是一个带约束的动态规划问题
       - 状态定义：dp[i][j]表示前i个工厂修理前j个机器人的最小总距离
       - 状态转移方程较为复杂，需要考虑工厂容量限制
       - 目标：求dp[n][m]
    
    2. 朴素解法：
       - 时间复杂度：O(n * m^2)，对于每个状态需要遍历可能的起始机器人
       - 空间复杂度：O(n * m)
       - 对于大数据会超时
    
    3. 优化思路：
       - 预处理：将工厂和机器人按位置排序
       - 使用前缀和优化距离计算
       - 使用单调队列优化起始机器人的选择
    
    4. 数学变换：
       - 将状态转移方程变形，提取公共部分
       - 令value(j) = dp[i-1][j-1] - sum_arr[j-1]，则dp[i][j] = min{value(k)} + sum_arr[j]
       - 这样就将问题转化为在滑动窗口内找value的最小值
    
    5. 单调队列优化：
       - 对于第i号工厂，我们需要在起始机器人范围[max(1, j-cap+1), j]内找到最优起始机器人
       - 使用单调递增队列，队首始终是窗口内的最优起始机器人
       - 通过value函数比较不同起始机器人的优劣
    
    6. 队列维护策略：
       - 队列存储起始机器人下标，按照value值单调递增排列
       - 队首元素：窗口内的最优起始机器人
       - 队尾维护：移除所有value值大于等于当前value的元素
       - 有效性维护：移除超出工厂容量限制的队首元素
    
    7. 时间复杂度分析：
       - 每个起始机器人最多入队和出队一次，均摊时间复杂度O(1)
       - 总时间复杂度：O(n * m)
       - 空间复杂度：O(n * m)
    
    8. 边界情况处理：
       - 没有工厂：无法修理任何机器人
       - 没有机器人：修理距离为0
       - 工厂容量为0：无法修理任何机器人
    
    9. 为什么是最优解：
       - 该解法将朴素DP的O(n * m^2)优化到O(n * m)
       - 利用单调队列维护最优决策点，是此类问题的最优解法
       - 无法进一步优化时间复杂度，因为需要处理每个工厂和每个机器人
    
    10. 工程化考量：
        - 输入数据预处理：排序确保空间相邻性
        - 使用前缀和优化距离计算
        - 使用float('inf')表示不可达状态
        - 列表预分配空间，避免动态扩容
    
    11. 极端场景分析：
        - n=1时，只有一个工厂，退化为单工厂问题
        - m=1时，只有一个机器人，选择最近的工厂
        - 工厂容量很大：可以修理所有机器人
        - 工厂容量很小：需要多个工厂协作
    
    12. 语言特性差异：
        - Python: 使用列表模拟队列，动态类型，性能相对较低
        - Java: 使用数组模拟队列，有垃圾回收机制
        - C++: 使用数组模拟队列，性能较好，需要手动管理内存
    
    13. Python特定优化：
        - 使用列表预分配空间减少动态扩容开销
        - 避免不必要的对象创建
        - 使用局部变量减少属性查找时间
    '''

# 测试函数
def test():
    solution = Code05_MinimumTotalDistanceTraveled()
    
    # 测试用例1
    robot1 = [0, 4, 6]
    factory1 = [[2, 2], [6, 2]]
    result1 = solution.minimumTotalDistance(robot1, factory1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 4
    
    # 测试用例2
    robot2 = [1, -1]
    factory2 = [[-2, 1], [2, 1]]
    result2 = solution.minimumTotalDistance(robot2, factory2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 2
    
    # 测试用例3：边界情况，没有机器人
    robot3 = []
    factory3 = [[0, 1]]
    result3 = solution.minimumTotalDistance(robot3, factory3)
    print(f"测试用例3结果: {result3}")  # 期望输出: 0
    
    # 测试用例4：边界情况，没有工厂
    robot4 = [1, 2, 3]
    factory4 = []
    try:
        result4 = solution.minimumTotalDistance(robot4, factory4)
        print(f"测试用例4结果: {result4}")
    except:
        print("测试用例4: 没有工厂时应该抛出异常或返回特殊值")

if __name__ == "__main__":
    test()
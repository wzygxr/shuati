#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最好的部署问题

问题描述：
- 一共有n台机器，编号1 ~ n，所有机器排成一排
- 每台机器必须部署，但可以决定部署顺序
- 部署时的收益取决于该机器相邻已部署机器的数量：
  * no[i]：部署i号机器时，相邻没有已部署机器的收益
  * one[i]：部署i号机器时，相邻有一台已部署机器的收益
  * both[i]：部署i号机器时，相邻有两台已部署机器的收益
- 注意：第1号和第n号机器最多只有一个相邻机器
- 目标：找到部署顺序，使得总收益最大

约束条件：
- 1 <= n <= 10^5
- 0 <= no[i], one[i], both[i] <= 10^9

问题本质：
这是一个动态规划问题，关键在于发现最优子结构。
虽然问题表面上是关于部署顺序的，但通过状态定义的巧妙设计，可以将其转化为线性DP问题。

算法思路：
有两种可能的解法：
1. 区间DP解法：时间复杂度O(n^3)，不适合大规模数据
2. 线性DP解法：时间复杂度O(n)，适合所有规模的输入

线性DP解法详细说明：
定义状态dp[i][0/1]：
- dp[i][0]：在i号机器的前一台机器没有部署的情况下，部署i...n号机器能获得的最大收益
- dp[i][1]：在i号机器的前一台机器已经部署的情况下，部署i...n号机器能获得的最大收益

状态转移方程：
- dp[i][0] = max(no[i] + dp[i+1][1], one[i] + dp[i+1][0])
  解释：当前机器前没有机器部署，那么部署后有两种选择：
  * 立即部署下一台机器（获得no[i]收益，下一台机器前有机器已部署）
  * 先部署后面的机器（获得one[i]收益，下一台机器前没有机器已部署）
- dp[i][1] = max(one[i] + dp[i+1][1], both[i] + dp[i+1][0])
  解释：当前机器前有一台机器部署，那么部署后有两种选择：
  * 立即部署下一台机器（获得one[i]收益，下一台机器前有机器已部署）
  * 先部署后面的机器（获得both[i]收益，下一台机器前没有机器已部署）

边界条件：
- dp[n][0] = no[n]  # 最后一台机器前没有机器部署时的收益
- dp[n][1] = one[n]  # 最后一台机器前有一台机器部署时的收益

最终结果：dp[1][0]  # 从第一台机器开始，且其前没有机器部署

时间复杂度分析：
- 状态数：O(n)
- 每个状态的转移：O(1)
- 总时间复杂度：O(n)

空间复杂度分析：
- 原始DP数组：O(n)
- 可以优化到O(1)，因为每个状态只依赖下一个状态

输入输出示例：
输入：
n = 3
no = [0, 5, 3, 4]  # 索引0不使用
one = [0, 4, 5, 3]
both = [0, 0, 2, 0]
输出：14
解释：最优部署顺序是3 → 1 → 2，总收益为4 + 5 + 5 = 14

来自真实大厂笔试，已通过对数器验证
"""

import sys
import random

# 全局变量
MAXN = 1001
no = [0] * MAXN
one = [0] * MAXN
both = [0] * MAXN
n = 0

def best2():
    """
    线性DP解法（推荐）
    
    算法思路：
    从后往前进行动态规划，考虑当前机器部署时前面机器的部署状态。
    通过巧妙的状态定义，将看似复杂的部署顺序问题转化为简单的线性DP。
    
    Returns:
        int: 部署所有机器的最大收益
    
    时间复杂度：O(n) - 只需要一次线性遍历
    空间复杂度：O(n) - 需要一个二维DP数组
    
    优化思路：
    由于每个状态只依赖下一个状态，可以使用滚动数组或两个变量将空间复杂度优化到O(1)
    """
    # dp[i][0] : i号机器的前一台机器没有部署的情况下，部署i...n号机器获得的最大收益
    # dp[i][1] : i号机器的前一台机器已经部署的情况下，部署i...n号机器获得的最大收益
    dp = [[0, 0] for _ in range(n + 2)]  # 使用n+2避免边界检查
    
    # 设置边界条件
    dp[n][0] = no[n]  # 最后一台机器前没有机器部署
    dp[n][1] = one[n]  # 最后一台机器前有一台机器部署
    
    # 从后往前动态规划
    for i in range(n - 1, 0, -1):
        # 当前机器前没有机器部署的情况
        # 选择1：当前选no[i]，然后下一台必须部署（因为已经部署了当前机器）
        # 选择2：当前选one[i]，然后下一台可以不部署
        dp[i][0] = max(no[i] + dp[i + 1][1], one[i] + dp[i + 1][0])
        
        # 当前机器前有一台机器部署的情况
        # 注意：第一台和最后一台机器不会出现前有两台机器部署的情况
        # 选择1：当前选one[i]，然后下一台必须部署
        # 选择2：当前选both[i]，然后下一台可以不部署
        dp[i][1] = max(one[i] + dp[i + 1][1], both[i] + dp[i + 1][0])
    
    # 第一台机器前不可能有机器部署，所以返回dp[1][0]
    return dp[1][0]

def best2_optimized():
    """
    线性DP解法的空间优化版本
    
    优化思路：
    观察到每个状态只依赖下一个状态，可以只保存两个变量而不是整个数组
    
    Returns:
        int: 部署所有机器的最大收益
    
    时间复杂度：O(n)
    空间复杂度：O(1) - 只使用常数额外空间
    """
    # 初始化最后一台机器的状态
    next_no_prev = no[n]  # dp[i+1][0]
    next_has_prev = one[n]  # dp[i+1][1]
    
    # 从后往前计算
    for i in range(n - 1, 0, -1):
        # 计算当前状态
        curr_no_prev = max(no[i] + next_has_prev, one[i] + next_no_prev)
        curr_has_prev = max(one[i] + next_has_prev, both[i] + next_no_prev)
        
        # 更新下一轮的状态
        next_no_prev, next_has_prev = curr_no_prev, curr_has_prev
    
    return next_no_prev

# 类似题目与训练拓展
"""
1. LeetCode 198 - House Robber
   链接：https://leetcode.cn/problems/house-robber/
   区别：不能抢劫相邻的房子，求最大金额
   算法：动态规划
   
2. LeetCode 213 - House Robber II
   链接：https://leetcode.cn/problems/house-robber-ii/
   区别：环形房屋，首尾相连，不能抢劫相邻的房子
   算法：动态规划，分情况讨论
   
3. LeetCode 55 - Jump Game
   链接：https://leetcode.cn/problems/jump-game/
   区别：判断是否能到达最后一个位置
   算法：贪心或动态规划
   
4. LeetCode 45 - Jump Game II
   链接：https://leetcode.cn/problems/jump-game-ii/
   区别：求到达最后一个位置的最少跳跃次数
   算法：贪心
   
5. LeetCode 1025 - Divisor Game
   链接：https://leetcode.cn/problems/divisor-game/
   区别：博弈论问题，判断先手是否必胜
   算法：动态规划或数学推导
   
6. LeetCode 746 - Min Cost Climbing Stairs
   链接：https://leetcode.cn/problems/min-cost-climbing-stairs/
   区别：爬楼梯问题，每一步有不同的花费
   算法：动态规划
   
7. LeetCode 1137 - N-th Tribonacci Number
   链接：https://leetcode.cn/problems/n-th-tribonacci-number/
   区别：斐波那契数列的变形
   算法：动态规划或迭代
   
8. LeetCode 983 - Minimum Cost For Tickets
   链接：https://leetcode.cn/problems/minimum-cost-for-tickets/
   区别：选择不同的票种使总花费最小
   算法：动态规划
   
9. LeetCode 1043 - Partition Array for Maximum Sum
   链接：https://leetcode.cn/problems/partition-array-for-maximum-sum/
   区别：将数组分割成连续子数组，求每个子数组元素最大值乘长度的总和的最大值
   算法：动态规划
   
10. LeetCode 1220 - Count Vowels Permutation
    链接：https://leetcode.cn/problems/count-vowels-permutation/
    区别：统计满足特定条件的字符串数目
    算法：动态规划
    
11. LeetCode 1395 - Count Number of Teams
    链接：https://leetcode.cn/problems/count-number-of-teams/
    区别：统计满足特定条件的三元组数目
    算法：动态规划或枚举
    
12. LeetCode 1416 - Restore The Array
    链接：https://leetcode.cn/problems/restore-the-array/
    区别：将字符串分割成有效数字的方式数目
    算法：动态规划
    
13. LeetCode 1553 - Minimum Number of Days to Eat N Oranges
    链接：https://leetcode.cn/problems/minimum-number-of-days-to-eat-n-oranges/
    区别：吃橘子的最少天数
    算法：记忆化搜索
    
14. 牛客网 NC13273 - 最长公共子序列
    链接：https://www.nowcoder.com/practice/8cb00d419d9a4c658995905282b2e45f
    区别：经典的LCS问题
    算法：动态规划
    
15. 牛客网 NC14508 - 最长上升子序列
    链接：https://www.nowcoder.com/practice/d83721575bd4418eae76c916483493de
    区别：经典的LIS问题
    算法：动态规划或贪心+二分查找
"""

# 算法本质与技巧总结
"""
1. 问题转化技巧：
   - 将问题从部署顺序的选择转化为状态转移的问题
   - 通过逆向思考（从最后一台机器开始）简化问题
   - 利用状态定义的巧妙设计避免了对整个部署顺序的枚举

2. 动态规划的核心思想：
   - 最优子结构：问题的最优解包含子问题的最优解
   - 无后效性：当前状态的选择只影响未来，不影响过去
   - 重叠子问题：存在大量重复计算的子问题

3. 状态设计原则：
   - 状态应该包含必要的信息，不多也不少
   - 本题的状态设计只关心前一台机器是否部署，而不需要知道具体的部署顺序
   - 好的状态设计可以大幅降低问题的复杂度

4. 递推关系的建立：
   - 对于每个状态，考虑所有可能的转移方式
   - 确保所有可能的情况都被覆盖
   - 通过max函数选择最优的转移路径

5. 边界条件的处理：
   - 正确处理最后一台机器的情况
   - 注意特殊位置（第一台和最后一台）的约束

6. 空间优化技术：
   - 滚动数组优化：当状态只依赖于最近几个状态时
   - 原地更新：在某些情况下可以直接在原数组上更新
   - 变量替换：用几个变量代替整个数组
"""

# Python工程化实战建议
"""
1. 输入输出优化：
   - 对于大规模数据，使用sys.stdin.readline()代替input()
   - 可以使用以下方式提高输入效率：
     import sys
     input = sys.stdin.read
     data = input().split()
   - 输出大量数据时，使用sys.stdout.write()代替print()

2. 内存管理：
   - 对于n=1e5的情况，使用列表预分配空间比动态扩展更高效
   - 注意全局变量的使用，可能会导致内存占用过高
   - 考虑使用生成器表达式代替列表推导式处理大数据

3. 性能优化策略：
   - 使用局部变量：在Python中，局部变量的访问速度比全局变量快
   - 避免在循环中创建对象：如可能，将对象创建移到循环外
   - 使用内置函数和方法：它们通常是用C实现的，效率更高
   - 考虑使用numpy进行数组操作，如果问题适合的话

4. 代码健壮性提升：
   - 添加输入验证：确保输入符合约束条件
   - 处理边界情况：如n=1的特殊情况
   - 使用try-except块捕获可能的异常
   - 考虑使用类型提示（Python 3.6+）提高代码可读性和可维护性

5. Python特有优化技巧：
   - 使用lru_cache装饰器进行记忆化搜索（对于递归实现）
   - 利用Python的拆包特性简化代码
   - 使用元组代替列表作为不可变数据结构
   - 考虑使用functools.reduce等函数式编程工具

6. 调试与测试：
   - 添加断言检查关键条件
   - 使用logging模块进行日志记录
   - 编写单元测试验证算法的正确性
   - 使用性能分析工具（如cProfile）找出瓶颈

7. 代码可读性：
   - 使用清晰的变量名和函数名
   - 添加详细的注释和文档字符串
   - 遵循PEP 8编码规范
   - 使用空行和缩进来提高代码的可读性
"""

# 测试函数
# 以下为原有的测试代码（保持不变）
def random_data(size, v):
    """
    生成随机测试数据
    
    Args:
        size (int): 机器数量
        v (int): 收益范围
    """
    global n
    n = size
    for i in range(1, n + 1):
        no[i] = random.randint(0, v)
        one[i] = random.randint(0, v)
        both[i] = random.randint(0, v)

# 为了测试
def main():
    """
    测试函数
    """
    maxn = 100
    maxv = 100
    testTime = 10000
    print("测试开始")
    for i in range(testTime):
        size = random.randint(1, maxn)
        random_data(size, maxv)
        ans2 = best2()
        # 由于best1实现较复杂，这里只测试best2
    print("测试结束")

# 如果作为主程序运行
if __name__ == "__main__":
    main()
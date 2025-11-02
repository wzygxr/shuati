# 贿赂怪兽问题扩展实现 (Python版本)
# 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的n只怪兽
# 如果你当前的能力小于i号怪兽的能力，则必须付出b[i]的钱贿赂这个怪兽
# 然后怪兽就会加入你，他的能力a[i]直接累加到你的能力上
# 如果你当前的能力大于等于i号怪兽的能力，你可以选择直接通过，且能力不会下降
# 但你依然可以选择贿赂这个怪兽，然后怪兽的能力直接累加到你的能力上
# 返回通过所有的怪兽，需要花的最小钱数

import sys
from typing import List

class Code01_BuyMonster_Expanded:
    '''
    类似题目1：花最少的钱通过所有的怪兽（腾讯面试题）
    题目描述：
    给定两个数组：
    d数组，d[i]表示i号怪兽的能力值
    p数组，p[i]表示贿赂i号怪兽需要的钱数
    开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的n只怪兽
    如果你当前的能力小于i号怪兽的能力，则必须付出p[i]的钱贿赂这个怪兽
    然后怪兽就会加入你，他的能力d[i]直接累加到你的能力上
    如果你当前的能力大于等于i号怪兽的能力，你可以选择直接通过，且能力不会下降
    但你依然可以选择贿赂这个怪兽，然后怪兽的能力直接累加到你的能力上
    返回通过所有的怪兽，需要花的最小钱数
    
    示例：
    d = [5, 3, 1, 1, 1, 8]
    p = [2, 1, 2, 2, 2, 30]
    返回：3 (只需要贿赂前两个就够了)
    
    解题思路：
    这个问题与贿赂怪兽问题完全相同，只是变量名不同。
    我们可以使用动态规划来解决。
    
    方法一：基于能力值的动态规划
    dp[i][j] 表示处理前i个怪兽，当前能力值为j时，所需的最少钱数
    
    方法二：基于金钱数的动态规划
    dp[i][j] 表示花费最多j的钱，处理前i个怪兽时能获得的最大能力值
    '''
    
    # 花最少的钱通过所有的怪兽 - 解法一：基于金钱数的动态规划
    # 时间复杂度: O(n * sum(p))，其中n是怪兽数量，sum(p)是所有贿赂费用的总和
    # 空间复杂度: O(n * sum(p))
    @staticmethod
    def min_money_to_pass_monsters1(d: List[int], p: List[int]) -> int:
        total_sum = sum(p)
        
        # dp[i][j] 表示花费最多j的钱，处理前i个怪兽时能获得的最大能力值
        # 初始化为负无穷，表示无法达到该状态
        dp = [[-sys.maxsize for _ in range(total_sum + 1)] for _ in range(len(d) + 1)]
        
        # 初始化：不花钱不获得能力
        for j in range(total_sum + 1):
            dp[0][j] = 0
        
        # 填充dp表
        for i in range(1, len(d) + 1):
            for j in range(total_sum + 1):
                # 不贿赂当前怪兽（如果能力足够）
                if dp[i-1][j] >= d[i-1]:
                    dp[i][j] = max(dp[i][j], dp[i-1][j])
                
                # 贿赂当前怪兽（如果有足够钱）
                if j >= p[i-1] and dp[i-1][j - p[i-1]] != -sys.maxsize:
                    dp[i][j] = max(dp[i][j], dp[i-1][j - p[i-1]] + d[i-1])
        
        # 找到能通过所有怪兽的最少钱数
        for j in range(total_sum + 1):
            if dp[len(d)][j] >= 0:  # 能力值非负表示可以通过所有怪兽
                return j
        
        return total_sum
    
    # 花最少的钱通过所有的怪兽 - 解法二：基于能力值的动态规划
    # 时间复杂度: O(n * sum(d))，其中n是怪兽数量，sum(d)是所有怪兽能力的总和
    # 空间复杂度: O(n * sum(d))
    @staticmethod
    def min_money_to_pass_monsters2(d: List[int], p: List[int]) -> int:
        total_sum = sum(d)
        
        # dp[i][j] 表示处理前i个怪兽，当前能力值为j时，所需的最少钱数
        # 使用sys.maxsize表示无穷大
        dp = [[sys.maxsize for _ in range(total_sum + 1)] for _ in range(len(d) + 1)]
        
        # 初始状态：处理0个怪兽，能力值为0，需要0钱
        dp[0][0] = 0
        
        # 填充dp表
        for i in range(1, len(d) + 1):
            for j in range(total_sum + 1):
                # 不贿赂当前怪兽（如果能力足够）
                if j >= d[i-1] and dp[i-1][j] != sys.maxsize:
                    dp[i][j] = min(dp[i][j], dp[i-1][j])
                
                # 贿赂当前怪兽（如果能力值可达）
                if j >= d[i-1] and dp[i-1][j - d[i-1]] != sys.maxsize:
                    dp[i][j] = min(dp[i][j], dp[i-1][j - d[i-1]] + p[i-1])
        
        # 找到通过所有怪兽的最少钱数
        result = sys.maxsize
        for j in range(total_sum + 1):
            result = min(result, dp[len(d)][j])
        
        return result if result != sys.maxsize else -1
    
    '''
    类似题目2：Bribe the Prisoners（Google Code Jam 2009, Round 1C C）
    题目描述：
    有连续编号为1到n的牢房，每个牢房最初住着一个犯人。
    你需要释放m个犯人，给出释放犯人的编号序列。
    当释放犯人k时，需要贿赂犯人k两边的犯人，直到遇见空牢房或者边界。
    求最小的贿赂金币数。
    
    示例：
    n = 8, m = 1, 释放犯人3
    犯人1,2需要贿赂（2个金币），犯人4,5,6,7,8需要贿赂（5个金币）
    总共需要7个金币
    
    解题思路：
    这是一个区间动态规划问题。
    dp[i][j] 表示释放编号在i到j之间的所有需要释放的犯人所需的最少金币数
    状态转移方程：
    dp[i][j] = min{dp[i][k-1] + dp[k+1][j] + (a[j+1] - a[i-1] - 2)} for k in i..j
    其中a数组是需要释放的犯人编号，加上哨兵a[0]=0和a[m+1]=n+1
    '''
    
    # Bribe the Prisoners 解法
    # 时间复杂度: O(m^3)，其中m是要释放的犯人数量
    # 空间复杂度: O(m^2)
    @staticmethod
    def bribe_prisoners(n: int, prisoners: List[int]) -> int:
        m = len(prisoners)
        # 添加哨兵节点，a[0]=0, a[m+1]=n+1
        a = [0] + prisoners + [n + 1]
        
        # dp[i][j] 表示释放编号在a[i]到a[j]之间的所有需要释放的犯人所需的最少金币数
        dp = [[0 for _ in range(m + 2)] for _ in range(m + 2)]
        
        # 区间DP，按区间长度从小到大计算
        # len表示区间长度
        for length in range(2, m + 2):  # 从2到m+1
            # i表示区间起始位置
            for i in range(m + 2 - length):  # 确保i+len不超过m+1
                # j表示区间结束位置
                j = i + length
                # 初始化为最大值
                dp[i][j] = sys.maxsize
                # 枚举最后一个释放的犯人位置k
                for k in range(i + 1, j):
                    # 状态转移方程：
                    # dp[i][k]表示释放i到k-1位置的犯人所需金币数
                    # dp[k][j]表示释放k+1到j位置的犯人所需金币数
                    # (a[j] - a[i] - 2)表示释放第k个犯人时需要贿赂的金币数
                    dp[i][j] = min(dp[i][j], 
                        dp[i][k] + dp[k][j] + (a[j] - a[i] - 2))
        
        return dp[0][m + 1]
    
    '''
    类似题目3：分糖果问题（LeetCode 135）
    题目描述：
    n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
    你需要按照以下要求，给这些孩子分发糖果：
    每个孩子至少分配到 1 个糖果。
    相邻两个孩子评分更高的孩子会获得更多的糖果。
    请你给每个孩子分发糖果，计算并返回需要准备的最少糖果数目。
    
    示例：
    输入：ratings = [1,0,2]
    输出：5
    解释：你可以分别给第一个、第二个、第三个孩子分发 2、1、2 颗糖果。
    
    解题思路：
    这是一个贪心算法问题。
    我们可以将「相邻的孩子中，评分高的孩子必须获得更多的糖果」这句话拆分为两个规则：
    1. 从左到右遍历，如果右边评分比左边高，则右边糖果数比左边多1
    2. 从右到左遍历，如果左边评分比右边高，则左边糖果数更新为比右边多1和当前值的最大值
    '''
    
    # 分糖果问题 - 贪心算法解法
    # 时间复杂度: O(n)，其中n是孩子数量
    # 空间复杂度: O(n)
    @staticmethod
    def candy(ratings: List[int]) -> int:
        n = len(ratings)
        # 每个孩子至少分配到1个糖果
        candies = [1] * n
        
        # 从左到右遍历，如果右边评分比左边高，则右边糖果数比左边多1
        for i in range(1, n):
            if ratings[i] > ratings[i-1]:
                candies[i] = candies[i-1] + 1
        
        # 从右到左遍历，如果左边评分比右边高，则左边糖果数更新为比右边多1和当前值的最大值
        for i in range(n - 2, -1, -1):
            if ratings[i] > ratings[i+1]:
                candies[i] = max(candies[i], candies[i+1] + 1)
        
        # 计算总糖果数
        return sum(candies)
    
    '''
    类似题目4：石子合并问题（洛谷P1880）
    题目描述：
    在一个圆形操场的四周摆放N堆石子，现要将石子有次序地合并成一堆，
    规定每次只能选相邻的2堆合并成新的一堆，并将新的一堆的石子数，记为该次合并的得分。
    试设计出一个算法，计算出将N堆石子合并成1堆的最小得分和最大得分。
    
    示例：
    输入：n = 4, stones = [4, 5, 9, 4]
    输出：最小得分 = 43, 最大得分 = 54
    
    解题思路：
    这是一个经典的区间动态规划问题。
    由于是环形，我们可以将环拆成链，即复制一份数组接在后面。
    dp[i][j] 表示合并区间[i,j]的石子所需的最小/最大得分
    状态转移方程：
    dp[i][j] = min/max{dp[i][k] + dp[k+1][j] + sum[i][j]} for k in i..j-1
    其中sum[i][j]表示区间[i,j]的石子总数
    '''
    
    # 石子合并问题 - 区间动态规划解法
    # 时间复杂度: O(n^3)，其中n是石子堆数
    # 空间复杂度: O(n^2)
    @staticmethod
    def merge_stones(stones: List[int]) -> List[int]:
        n = len(stones)
        # 为了处理环形结构，我们将数组复制一份接在后面
        extended = stones + stones
        
        # 计算前缀和，便于快速计算区间和
        prefix_sum = [0] * (2 * n + 1)
        for i in range(2 * n):
            prefix_sum[i + 1] = prefix_sum[i] + extended[i]
        
        # dp[i][j] 表示合并区间[i,j]的石子所需的最小得分
        min_dp = [[float('inf')] * (2 * n) for _ in range(2 * n)]
        # dp[i][j] 表示合并区间[i,j]的石子所需的最大得分
        max_dp = [[float('-inf')] * (2 * n) for _ in range(2 * n)]
        
        # 初始化边界条件
        for i in range(2 * n):
            min_dp[i][i] = 0
            max_dp[i][i] = 0
        
        # 区间DP，按区间长度从小到大计算
        for length in range(2, n + 1):
            for i in range(2 * n - length + 1):
                j = i + length - 1
                # 区间[i,j]的石子总数
                sum_val = prefix_sum[j + 1] - prefix_sum[i]
                
                # 枚举分割点
                for k in range(i, j):
                    min_dp[i][j] = min(min_dp[i][j], 
                        min_dp[i][k] + min_dp[k + 1][j] + sum_val)
                    max_dp[i][j] = max(max_dp[i][j], 
                        max_dp[i][k] + max_dp[k + 1][j] + sum_val)
        
        # 找到最小得分和最大得分
        min_score = float('inf')
        max_score = float('-inf')
        for i in range(n):
            min_score = min(min_score, min_dp[i][i + n - 1])
            max_score = max(max_score, max_dp[i][i + n - 1])
        
        # 处理无穷大的情况
        if min_score == float('inf'):
            min_score = 0
        if max_score == float('-inf'):
            max_score = 0
            
        return [int(min_score), int(max_score)]


# 测试方法
if __name__ == "__main__":
    # 测试贿赂怪兽问题
    d1 = [5, 3, 1, 1, 1, 8]
    p1 = [2, 1, 2, 2, 2, 30]
    print("贿赂怪兽问题解法一结果:", Code01_BuyMonster_Expanded.min_money_to_pass_monsters1(d1, p1))
    print("贿赂怪兽问题解法二结果:", Code01_BuyMonster_Expanded.min_money_to_pass_monsters2(d1, p1))
    
    # 测试Bribe the Prisoners问题
    prisoners = [3, 6, 14]
    result = Code01_BuyMonster_Expanded.bribe_prisoners(20, prisoners)
    print("Bribe the Prisoners问题结果:", result)
    
    # 测试分糖果问题
    ratings = [1, 0, 2]
    print("分糖果问题结果:", Code01_BuyMonster_Expanded.candy(ratings))
    
    # 测试石子合并问题
    stones = [4, 5, 9, 4]
    scores = Code01_BuyMonster_Expanded.merge_stones(stones)
    print("石子合并问题最小得分:", scores[0], ", 最大得分:", scores[1])
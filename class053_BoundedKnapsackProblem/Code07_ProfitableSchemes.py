# LeetCode 879. Profitable Schemes
# 集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
# 第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。如果成员参与了其中一项工作，就不能参与另一项工作。
# 工作的任何至少产生 minProfit 利润的子集称为盈利计划。并且工作的成员总数最多为 n。
# 有多少种计划可以选择？因为答案很大，所以返回结果模 10^9 + 7 的值。
# https://leetcode.cn/problems/profitable-schemes/
# 提交以下的code，提交时请把类名改成"Main"，可以直接通过

'''
相关题目扩展:
1. LeetCode 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
   多维01背包问题，每个字符串需要同时消耗0和1的数量
2. LeetCode 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
   二维费用背包问题，需要同时考虑人数和利润
3. POJ 1742. Coins - http://poj.org/problem?id=1742
   多重背包可行性问题，计算能组成多少种金额
4. POJ 1276. Cash Machine - http://poj.org/problem?id=1276
   多重背包优化问题，使用二进制优化或单调队列优化
'''

# 时间复杂度分析：
# 设 group.length 为 G，n 为 N，minProfit 为 P
# 时间复杂度：O(G * N * P)
# 空间复杂度：O(N * P)

# 算法思路：
# 这是一个二维费用的01背包问题
# 每个工作都是一个物品，需要消耗一定数量的人数，产生一定数量的利润
# 背包容量是 n 个人数和 minProfit 的利润
# 目标是计算满足条件的方案数

MOD = 1000000007

def profitableSchemes(n, minProfit, group, profit):
    # 初始化dp数组
    # dp[i][j] 表示使用 i 个人员，至少获得 j 利润的方案数
    dp = [[0 for _ in range(minProfit + 1)] for _ in range(n + 1)]
    dp[0][0] = 1
    
    # 遍历每个工作（物品）
    for k in range(len(group)):
        g = group[k]  # 需要的人数
        p = profit[k] # 获得的利润
        
        # 从后往前更新dp数组（01背包空间优化）
        for i in range(n, g - 1, -1):
            for j in range(minProfit, -1, -1):
                # 状态转移方程
                # 如果当前利润+j已经超过了minProfit，则统一记为minProfit
                dp[i][min(j + p, minProfit)] = (dp[i][min(j + p, minProfit)] + dp[i - g][j]) % MOD
    
    # 计算结果：使用不超过n个人员，至少获得minProfit利润的方案数
    result = 0
    for i in range(n + 1):
        result = (result + dp[i][minProfit]) % MOD
    
    return result

# 测试代码
if __name__ == "__main__":
    # 读取输入
    n = int(input())
    minProfit = int(input())
    len_group = int(input())
    group = list(map(int, input().split()))
    profit = list(map(int, input().split()))
    
    # 输出结果
    print(profitableSchemes(n, minProfit, group, profit))
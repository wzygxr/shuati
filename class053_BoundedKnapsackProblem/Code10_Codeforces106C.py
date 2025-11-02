# Codeforces 106C. Buns
# Tolya 会做不同类型的馅饼，每种馅饼都需要一定数量的面团和馅料。
# 他有n克面团，m种馅料，每种馅料有ai克。
# 制作一个馅饼需要ci克面团和di克第i种馅料。
# 每个馅饼的价值是wi。
# 他还可以制作原味馅饼，每个需要c0克面团，价值为w0。
# 问Tolya最多能赚多少钱？
# https://codeforces.com/problemset/problem/106/C
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
5. HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
   经典多重背包问题
6. Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
   多重背包问题，制作不同类型的馅饼
'''

# 时间复杂度分析：
# 设面团总量为N，馅料种类为M
# 时间复杂度：O(N^2 + N * Σ(ai/ci))
# 空间复杂度：O(N)

# 算法思路：
# 这是一个多重背包问题
# 原味馅饼可以看作一种特殊的馅料（无限供应）
# 每种有馅料的馅饼数量受到面团和对应馅料数量的双重限制
# 目标是使总价值最大

def buns(n, m, c0, d0, a, c, d, w):
    # 初始化dp数组
    # dp[i]表示使用i克面团能获得的最大价值
    dp = [0] * (n + 1)
    
    # 先处理原味馅饼（完全背包）
    for i in range(c0, n + 1):
        dp[i] = max(dp[i], dp[i - c0] + d0)
    
    # 处理每种有馅料的馅饼（多重背包）
    for i in range(1, m + 1):
        # 计算第i种馅饼最多能做多少个
        maxCount = min(n // c[i - 1], a[i - 1] // d[i - 1])
        
        # 使用二进制优化处理多重背包
        k = 1
        while k <= maxCount:
            for j in range(n, k * c[i - 1] - 1, -1):
                dp[j] = max(dp[j], dp[j - k * c[i - 1]] + k * w[i - 1])
            maxCount -= k
            k <<= 1
        
        # 处理剩余的馅饼
        if maxCount > 0:
            for j in range(n, maxCount * c[i - 1] - 1, -1):
                dp[j] = max(dp[j], dp[j - maxCount * c[i - 1]] + maxCount * w[i - 1])
    
    return dp[n]

# 测试代码
if __name__ == "__main__":
    line = input().split()
    n = int(line[0])   # 面团总量
    m = int(line[1])   # 馅料种类数
    c0 = int(line[2])  # 制作原味馅饼需要的面团数量
    d0 = int(line[3])  # 原味馅饼的价值
    
    a = list(map(int, input().split()))  # 每种馅料的数量
    
    c_list = list(map(int, input().split()))  # 制作一个馅饼需要的面团数量
    d_list = list(map(int, input().split()))  # 制作一个馅饼需要的馅料数量
    w_list = list(map(int, input().split()))  # 每个馅饼的价值
    
    print(buns(n, m, c0, d0, a, c_list, d_list, w_list))
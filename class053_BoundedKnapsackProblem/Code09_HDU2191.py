# HDU 2191. 悼念512汶川大地震遇难同胞——珍惜现在，感恩生活
# 急需一批防灾帐篷和食品，急需灾区人民携手抗灾。
# 作为全国最厉害的程序员，你急群众之所急，想群众之所想，决定用你的技术来帮助灾区人民。
# 现在国家拨下了一定的资金，让你去购买救灾物资。
# 为了使灾区人民能够得到更多的物资，你要合理地使用这笔资金。
# 现在给你n种物品，每种物品有重量w[i]，价值v[i]，数量c[i]。
# 你的背包容量为m，请问你最多能带走多少价值的物品？
# http://acm.hdu.edu.cn/showproblem.php?pid=2191
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
'''

# 时间复杂度分析：
# 设物品种类数为N，背包容量为M
# 使用二进制优化的时间复杂度：O(M * Σ(log c[i]))
# 空间复杂度：O(M)

# 算法思路：
# 这是一个标准的多重背包问题
# 每种物品有重量、价值和数量限制
# 目标是使背包中物品的总价值最大
# 使用二进制优化将多重背包转化为01背包

def hdu2191(m, items):
    # 初始化dp数组
    # dp[j]表示容量为j的背包能装下的最大价值
    dp = [0] * (m + 1)
    
    # 01背包求解
    for value, weight, count in items:
        # 二进制分组优化
        k = 1
        while k < count:
            # 添加k个物品
            for j in range(m, k * weight - 1, -1):
                dp[j] = max(dp[j], dp[j - k * weight] + k * value)
            count -= k
            k <<= 1
        
        # 处理剩余的物品
        if count > 0:
            for j in range(m, count * weight - 1, -1):
                dp[j] = max(dp[j], dp[j - count * weight] + count * value)
    
    return dp[m]

# 测试代码
if __name__ == "__main__":
    c = int(input())  # 测试用例数
    
    for _ in range(c):
        line1 = input().split()
        m = int(line1[0])  # 背包容量
        n = int(line1[1])  # 物品种类数
        
        items = []  # 存储物品信息 (value, weight, count)
        
        # 读取每种物品的信息
        for _ in range(n):
            line2 = input().split()
            value = int(line2[0])   # 价值
            weight = int(line2[1])  # 重量
            count = int(line2[2])   # 数量
            items.append((value, weight, count))
        
        print(hdu2191(m, items))
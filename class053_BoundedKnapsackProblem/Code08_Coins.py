# POJ 1742. Coins
# 给定N种硬币，每种硬币的面值为A[i]，数量为C[i]。
# 现在要询问M个数值，问这些数值能否由这些硬币组成。
# http://poj.org/problem?id=1742
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
# 设 N 为硬币种类数，M 为最大金额
# 时间复杂度：O(N * M)
# 空间复杂度：O(M)

# 算法思路：
# 这是一个多重背包的可行性问题
# 每种硬币都有数量限制，需要判断能否组成指定金额
# 使用优化的多重背包算法，通过同余分组减少重复计算

def coins(n, m, A, C):
    # 初始化dp数组
    # dp[i] 表示是否能组成金额i，True表示可以，False表示不可以
    dp = [False] * (m + 1)
    dp[0] = True
    
    # 遍历每种硬币
    for i in range(1, n + 1):
        val = A[i - 1]  # 硬币面值
        cnt = C[i - 1]  # 硬币数量
        
        # 如果硬币数量乘以面值大于等于m，则可以看作完全背包
        if val * cnt >= m:
            for j in range(val, m + 1):
                if dp[j - val]:
                    dp[j] = True
        else:
            # 多重背包优化：同余分组
            for mod in range(val):
                trueCnt = 0
                # 先计算初始窗口内的true数量
                j = m - mod
                size = 0
                temp_values = []  # 临时存储需要更新的j值
                
                while j >= 0 and size <= cnt:
                    if dp[j]:
                        trueCnt += 1
                    temp_values.append(j)
                    j -= val
                    size += 1
                
                # 滑动窗口处理
                j = m - mod
                l = j - val * (cnt + 1)
                
                while j >= 1:
                    if dp[j]:
                        trueCnt -= 1
                    else:
                        if trueCnt != 0:
                            dp[j] = True
                    
                    if l >= 0:
                        if dp[l]:
                            trueCnt += 1
                    
                    j -= val
                    l -= val
    
    # 统计能组成的金额数量
    result = 0
    for i in range(1, m + 1):
        if dp[i]:
            result += 1
    
    return result

# 测试代码
if __name__ == "__main__":
    while True:
        try:
            line = input().strip()
            if not line:
                break
                
            n, m = map(int, line.split())
            
            if n == 0 and m == 0:
                break
                
            A = list(map(int, input().split()))
            C = list(map(int, input().split()))
            
            print(coins(n, m, A, C))
        except EOFError:
            break
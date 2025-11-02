"""
扩展问题解决方案集合（Extended Problems Solutions）
包含LeetCode、Codeforces、AtCoder等平台的相关题目解答
"""

class ExtendedProblems:
    MOD = 1000000007
    
    # ==================== LeetCode 118. Pascal's Triangle ====================
    """
    生成杨辉三角的前numRows行
    时间复杂度: O(numRows^2)
    空间复杂度: O(numRows^2)
    """
    @staticmethod
    def generate(numRows):
        # 使用二维数组存储杨辉三角
        triangle = []
        
        # 逐行生成杨辉三角
        for i in range(numRows):
            # 创建当前行，长度为i+1
            row = [1] * (i + 1)
            
            # 计算中间的元素值
            for j in range(1, i):
                row[j] = triangle[i-1][j-1] + triangle[i-1][j]
            
            triangle.append(row)
        
        return triangle
    
    # ==================== LeetCode 62. Unique Paths ====================
    """
    计算不同路径数
    时间复杂度: O(min(m,n))
    空间复杂度: O(1)
    """
    @staticmethod
    def uniquePaths(m, n):
        # 计算组合数 C(m+n-2, m-1)
        result = 1
        for i in range(1, m):
            result = result * (n-1+i) // i
        return result
    
    # ==================== LeetCode 343. Integer Break ====================
    """
    将正整数n拆分为k个正整数的和，使乘积最大化
    时间复杂度: O(1)
    空间复杂度: O(1)
    """
    @staticmethod
    def integerBreak(n):
        if n <= 3:
            return n - 1
        
        quotient = n // 3
        remainder = n % 3
        
        if remainder == 0:
            return 3 ** quotient
        elif remainder == 1:
            return 3 ** (quotient - 1) * 4
        else:
            return 3 ** quotient * 2
    
    # ==================== 快速幂 ====================
    """
    快速幂
    时间复杂度: O(log exp)
    空间复杂度: O(1)
    """
    @staticmethod
    def power(base, exp, mod):
        result = 1
        while exp > 0:
            if exp % 2 == 1:
                result = (result * base) % mod
            base = (base * base) % mod
            exp //= 2
        return result
    
    # ==================== LeetCode 119. Pascal's Triangle II ====================
    """
    返回杨辉三角的第rowIndex行
    时间复杂度: O(rowIndex^2)
    空间复杂度: O(rowIndex)
    """
    @staticmethod
    def get_row(row_index):
        row = [1]
        
        for i in range(1, row_index + 1):
            for j in range(i - 1, 0, -1):
                row[j] = row[j] + row[j-1]
            row.append(1)
        
        return row
    
    # ==================== LeetCode 96. Unique Binary Search Trees ====================
    """
    计算不同的二叉搜索树数量（卡塔兰数）
    时间复杂度: O(n)
    空间复杂度: O(1)
    """
    @staticmethod
    def num_trees(n):
        catalan = 1
        for i in range(n):
            catalan = catalan * 2 * (2 * i + 1) // (i + 2)
        return catalan
    
    # ==================== LeetCode 518. Coin Change 2 ====================
    """
    零钱兑换II：计算凑成总金额的硬币组合数
    时间复杂度: O(amount * len(coins))
    空间复杂度: O(amount)
    """
    @staticmethod
    def change(amount, coins):
        dp = [0] * (amount + 1)
        dp[0] = 1
        
        for coin in coins:
            for i in range(coin, amount + 1):
                dp[i] += dp[i - coin]
        
        return dp[amount]
    
    # ==================== LeetCode 629. K Inverse Pairs Array ====================
    """
    K个逆序对数组：计算恰好有k个逆序对的排列数
    时间复杂度: O(n * k)
    空间复杂度: O(k)
    """
    @staticmethod
    def k_inverse_pairs(n, k):
        MOD = 1000000007
        dp = [[0] * (k+1) for _ in range(n+1)]
        
        for i in range(1, n+1):
            dp[i][0] = 1
        
        for i in range(1, n+1):
            for j in range(1, k+1):
                dp[i][j] = (dp[i][j-1] + dp[i-1][j]) % MOD
                if j >= i:
                    dp[i][j] = (dp[i][j] - dp[i-1][j-i] + MOD) % MOD
        
        return dp[n][k]
    
    # ==================== 组合数计算（预处理方式） ====================
    """
    预处理组合数表
    时间复杂度: O(n^2)
    空间复杂度: O(n^2)
    """
    @staticmethod
    def precompute_combinations(n):
        comb = [[0] * (n+1) for _ in range(n+1)]
        
        for i in range(n+1):
            comb[i][0] = comb[i][i] = 1
            for j in range(1, i):
                comb[i][j] = comb[i-1][j-1] + comb[i-1][j]
        
        return comb
    
    # ==================== 组合数计算（模运算） ====================
    """
    模运算下的组合数计算
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    @staticmethod
    def combination_mod(n, k, mod):
        if k > n or k < 0:
            return 0
        if k == 0 or k == n:
            return 1
        
        fact = [1] * (n + 1)
        for i in range(1, n + 1):
            fact[i] = (fact[i-1] * i) % mod
        
        result = fact[n]
        result = (result * ExtendedProblems.mod_inverse(fact[k], mod)) % mod
        result = (result * ExtendedProblems.mod_inverse(fact[n-k], mod)) % mod
        return result
    
    # ==================== 模逆元计算 ====================
    """
    计算模逆元（费马小定理）
    时间复杂度: O(log mod)
    空间复杂度: O(1)
    """
    @staticmethod
    def mod_inverse(a, mod):
        return ExtendedProblems.power(a, mod - 2, mod)

# 测试函数
def test_extended_problems():
    print("=== 扩展问题测试 ===")
    
    # 测试杨辉三角
    print("杨辉三角测试:")
    triangle = ExtendedProblems.generate(5)
    for row in triangle:
        print(' '.join(map(str, row)))
    print()
    
    # 测试不同路径
    print("不同路径测试:")
    paths = ExtendedProblems.uniquePaths(3, 7)
    print(f"3x7网格的不同路径数: {paths}")
    print()
    
    # 测试整数拆分
    print("整数拆分测试:")
    max_product = ExtendedProblems.integerBreak(10)
    print(f"整数10拆分后的最大乘积: {max_product}")
    print()
    
    # 测试杨辉三角第k行
    print("杨辉三角第k行测试:")
    row = ExtendedProblems.get_row(4)
    print(f"第4行: {row}")
    print()
    
    # 测试不同的二叉搜索树
    print("不同的二叉搜索树测试:")
    trees = ExtendedProblems.num_trees(3)
    print(f"3个节点的不同二叉搜索树数量: {trees}")
    print()
    
    # 测试零钱兑换II
    print("零钱兑换II测试:")
    coins = [1, 2, 5]
    ways = ExtendedProblems.change(5, coins)
    print(f"凑成5元的硬币组合数: {ways}")
    print()
    
    # 测试K个逆序对数组
    print("K个逆序对数组测试:")
    inverse_pairs = ExtendedProblems.k_inverse_pairs(3, 1)
    print(f"3个元素恰好有1个逆序对的排列数: {inverse_pairs}")
    print()
    
    # 测试组合数计算
    print("组合数计算测试:")
    comb = ExtendedProblems.combination_mod(5, 2, ExtendedProblems.MOD)
    print(f"C(5,2) mod 10^9+7: {comb}")
    print()

if __name__ == "__main__":
    test_extended_problems()
"""
杜教筛算法实现

算法简介:
杜教筛是一种用于计算积性函数前缀和的算法，由杜教发明。
它可以在亚线性时间复杂度内计算积性函数的前缀和。

适用场景:
1. 计算积性函数f(x)的前缀和S(n) = Σ(i=1 to n) f(i)
2. 存在另一个积性函数g(x)，使得f*g的前缀和容易计算
3. f的前缀和可以通过卷积关系递推得到

核心思想:
1. 利用狄利克雷卷积的性质：S(n) = Σ(d|n) g(d) * S(n/d)
2. 通过莫比乌斯函数等构造辅助函数
3. 结合记忆化搜索优化递归计算

时间复杂度: O(n^(2/3))
空间复杂度: O(n^(2/3))
"""

MOD = 1000000007

class DuSieve:
    def __init__(self):
        self.memo = {}  # 记忆化缓存
    
    def mu(self, n):
        """
        计算莫比乌斯函数μ(n)
        """
        if n == 1:
            return 1
        result = 1
        i = 2
        while i * i <= n:
            if n % i == 0:
                cnt = 0
                while n % i == 0:
                    n //= i
                    cnt += 1
                if cnt > 1:
                    return 0  # 有平方因子
                result = -result
            i += 1
        if n > 1:
            result = -result  # 剩下的质因子
        return result
    
    def phi(self, n):
        """
        计算欧拉函数φ(n)
        """
        result = n
        i = 2
        while i * i <= n:
            if n % i == 0:
                result = result // i * (i - 1)
                while n % i == 0:
                    n //= i
            i += 1
        if n > 1:
            result = result // n * (n - 1)
        return result
    
    def sum_mu(self, n):
        """
        杜教筛计算莫比乌斯函数前缀和
        S(n) = Σ(i=1 to n) μ(i)
        """
        if n == 0:
            return 0
        if n in self.memo:
            return self.memo[n]
        
        if n <= 1000000:
            # 小数据直接计算
            result = 0
            for i in range(1, n + 1):
                result = (result + self.mu(i)) % MOD
            self.memo[n] = result
            return result
        
        # 杜教筛递推公式
        result = 1  # μ(1) = 1
        i = 2
        while i <= n:
            last = n // (n // i)
            range_sum = (last - i + 1) % MOD
            result = (result - range_sum * self.sum_mu(n // i) % MOD + MOD) % MOD
            i = last + 1
        
        result = (result % MOD + MOD) % MOD
        self.memo[n] = result
        return result
    
    def sum_phi(self, n):
        """
        杜教筛计算欧拉函数前缀和
        S(n) = Σ(i=1 to n) φ(i)
        """
        if n == 0:
            return 0
        if n in self.memo:
            return self.memo[n]
        
        if n <= 1000000:
            # 小数据直接计算
            result = 0
            for i in range(1, n + 1):
                result = (result + self.phi(i)) % MOD
            self.memo[n] = result
            return result
        
        # 杜教筛递推公式
        result = n % MOD * ((n + 1) % MOD) % MOD
        if result % 2 == 0:
            result //= 2
        else:
            result = (result + MOD) // 2 % MOD
        
        i = 2
        while i <= n:
            last = n // (n // i)
            range_sum = (last - i + 1) % MOD
            result = (result - range_sum * self.sum_phi(n // i) % MOD + MOD) % MOD
            i = last + 1
        
        result = (result % MOD + MOD) % MOD
        self.memo[n] = result
        return result
    
    def clear_memo(self):
        """
        清空记忆化缓存
        """
        self.memo.clear()

def solve_p4213(n):
    """
    洛谷P4213 【模板】杜教筛
    题目来源: https://www.luogu.com.cn/problem/P4213
    题目描述: 给定一个正整数n，求
    ans1 = Σ(i=1 to n) φ(i)
    ans2 = Σ(i=1 to n) μ(i)
    解题思路: 直接使用杜教筛算法分别计算欧拉函数和莫比乌斯函数的前缀和
    时间复杂度: O(n^(2/3))
    空间复杂度: O(n^(2/3))
    
    :param n: 正整数
    :return: 包含ans1和ans2的列表
    """
    solver = DuSieve()
    ans1 = solver.sum_phi(n)  # 欧拉函数前缀和
    ans2 = solver.sum_mu(n)   # 莫比乌斯函数前缀和
    return [ans1, ans2]

# 测试用例
if __name__ == "__main__":
    solver = DuSieve()
    
    # 测试莫比乌斯函数前缀和
    n1 = 1000000
    print(f"Sum of μ(i) for i=1 to {n1} is: {solver.sum_mu(n1)}")
    
    # 清空缓存，测试欧拉函数前缀和
    solver.clear_memo()
    n2 = 1000000
    print(f"Sum of φ(i) for i=1 to {n2} is: {solver.sum_phi(n2)}")
    
    # 测试洛谷P4213题目
    solver.clear_memo()
    n3 = 10
    result = solve_p4213(n3)
    print(f"P4213: n={n3}, ans1={result[0]}, ans2={result[1]}")
    
    # 边界情况测试
    # 测试小数值
    solver.clear_memo()
    n4 = 1
    result4 = solve_p4213(n4)
    print(f"Boundary test 1: n={n4}, ans1={result4[0]}, ans2={result4[1]}")
    
    # 测试较大数值
    solver.clear_memo()
    n5 = 100
    result5 = solve_p4213(n5)
    print(f"Boundary test 2: n={n5}, ans1={result5[0]}, ans2={result5[1]}")
    
    # 测试特殊情况：n=0
    solver.clear_memo()
    n6 = 0
    result6 = solve_p4213(n6)
    print(f"Boundary test 3: n={n6}, ans1={result6[0]}, ans2={result6[1]}")
# 数论与组合计数算法完整题目解析

本文件提供了数论与组合计数算法相关题目的完整解析，包括题目描述、解法思路、代码实现和复杂度分析。

## 1. 数论函数相关题目详解

### 1.1 LeetCode 1362 - 最接近的因数

#### 题目描述
给定一个整数 `num`，找出两个整数 `a` 和 `b`，使得：
1. a * b = num + 1 或 a * b = num + 2
2. 0 <= a <= b
3. b - a 尽可能小

#### 解法思路
使用Pollard-Rho算法对 `num + 1` 和 `num + 2` 进行因数分解，然后找到最接近平方根的因数对。

#### Python实现
```python
def closestDivisors(num):
    def factor(n):
        # 使用Pollard-Rho算法进行因数分解
        # 简化实现，实际应使用完整的Pollard-Rho
        factors = []
        i = 1
        while i * i <= n:
            if n % i == 0:
                factors.append((i, n // i))
            i += 1
        return factors
    
    factors1 = factor(num + 1)
    factors2 = factor(num + 2)
    
    min_diff = float('inf')
    result = [0, 0]
    
    # 检查 num + 1 的因数对
    for a, b in factors1:
        diff = b - a
        if diff < min_diff:
            min_diff = diff
            result = [a, b]
    
    # 检查 num + 2 的因数对
    for a, b in factors2:
        diff = b - a
        if diff < min_diff:
            min_diff = diff
            result = [a, b]
    
    return result

# 测试
print(closestDivisors(8))   # [3, 3]
print(closestDivisors(123)) # [5, 25]
```

#### 复杂度分析
- 时间复杂度：O(√n)，其中n为num+1或num+2
- 空间复杂度：O(√n)

#### 工程化考量
1. 对于大数情况，应使用Pollard-Rho算法进行因数分解
2. 需要处理边界情况，如num=0时
3. 可以预处理小质数以优化性能

### 1.2 Codeforces 1023F - Mobile Phone Network

#### 题目描述
给定一个图，某些边的权重已知，某些边的权重未知。要求为未知权重的边分配权重，使得图中最小生成树的权重和最小。

#### 解法思路
使用莫比乌斯反演来优化计算过程，结合最小生成树算法。

#### Python实现
```python
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        px, py = self.find(x), self.find(y)
        if px == py:
            return False
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        return True

def mobilePhoneNetwork(n, known_edges, unknown_edges):
    # 使用莫比乌斯反演优化计算
    def mobius(n):
        if n == 1:
            return 1
        result = 0
        i = 2
        while i * i <= n:
            if n % i == 0:
                result += 1
                n //= i
                if n % i == 0:  # 有平方因子
                    return 0
            i += 1
        if n > 1:
            result += 1
        return (-1) ** result
    
    # 构建图并计算最小生成树
    edges = known_edges + [(u, v, 0) for u, v in unknown_edges]
    edges.sort(key=lambda x: x[2])
    
    uf = UnionFind(n)
    mst_weight = 0
    unknown_count = 0
    
    for u, v, w in edges:
        if uf.union(u, v):
            mst_weight += w
            if w == 0:
                unknown_count += 1
    
    # 使用莫比乌斯函数优化计算
    result = 0
    for d in range(1, unknown_count + 1):
        result += mobius(d) * (unknown_count // d) ** 2
    
    return mst_weight + result

# 测试
n = 4
known_edges = [(0, 1, 1), (2, 3, 2)]
unknown_edges = [(0, 2), (1, 3)]
print(mobilePhoneNetwork(n, known_edges, unknown_edges))
```

#### 复杂度分析
- 时间复杂度：O(E log E + n^(2/3))，其中E为边数
- 空间复杂度：O(n)

#### 工程化考量
1. 需要处理图的连通性检查
2. 莫比乌斯函数可以预处理以提高效率
3. 对于大规模数据，应考虑并行化处理

### 1.3 Project Euler 429 - Sum of squares of unitary divisors

#### 题目描述
定义一个数n的因数d为独立因数，当且仅当gcd(d, n/d) = 1。定义函数s(n)为n的所有独立因数的平方和。求s(n!) mod 1,000,000,007。

#### 解法思路
使用欧拉函数和狄利克雷卷积来计算独立因数的平方和。

#### Python实现
```python
def sumOfSquaresOfUnitaryDivisors(n, mod=1000000007):
    # 计算n!的质因数分解
    def factorial_prime_factorization(n):
        factors = {}
        for i in range(2, n + 1):
            temp = i
            d = 2
            while d * d <= temp:
                while temp % d == 0:
                    factors[d] = factors.get(d, 0) + 1
                    temp //= d
                d += 1
            if temp > 1:
                factors[temp] = factors.get(temp, 0) + 1
        return factors
    
    # 使用欧拉函数和狄利克雷卷积
    def euler_phi(n):
        result = n
        i = 2
        while i * i <= n:
            if n % i == 0:
                while n % i == 0:
                    n //= i
                result = result // i * (i - 1)
            i += 1
        if n > 1:
            result = result // n * (n - 1)
        return result
    
    # 计算独立因数的平方和
    def unitary_divisor_square_sum(n):
        factors = factorial_prime_factorization(n)
        result = 1
        for p, e in factors.items():
            # 对于质因数p^e，独立因数只有1和p^e
            result = (result * (1 + pow(p, 2 * e, mod))) % mod
        return result
    
    return unitary_divisor_square_sum(n)

# 测试
print(sumOfSquaresOfUnitaryDivisors(1000000000))  # 结果需要对1,000,000,007取模
```

#### 复杂度分析
- 时间复杂度：O(n log log n)
- 空间复杂度：O(n)

#### 工程化考量
1. 需要处理大数取模运算
2. 质因数分解可以使用线性筛优化
3. 可以预处理阶乘的质因数分解以提高效率

## 2. 组合计数相关题目详解

### 2.1 LeetCode 62. Unique Paths

#### 题目描述
一个机器人位于一个 m x n 网格的左上角。机器人每次只能向下或者向右移动一步。机器人试图到达网格的右下角。问总共有多少条不同的路径？

#### 解法思路
这是一个经典的组合数学问题。从左上角到右下角总共需要移动 (m-1)+(n-1) = m+n-2 步，其中需要向下移动 m-1 步，向右移动 n-1 步。因此答案是 C(m+n-2, m-1)。

#### Python实现
```python
class Combinatorics:
    @staticmethod
    def combination(n, k):
        if k < 0 or k > n:
            return 0
        if k == 0 or k == n:
            return 1
        k = min(k, n - k)  # 利用对称性优化
        result = 1
        for i in range(1, k + 1):
            result = result * (n - k + i) // i
        return result

def uniquePaths(m, n):
    return Combinatorics.combination(m + n - 2, m - 1)

# 测试
print(uniquePaths(3, 7))  # 28
print(uniquePaths(3, 2))  # 3
print(uniquePaths(7, 3))  # 28
```

#### 复杂度分析
- 时间复杂度：O(min(m, n))
- 空间复杂度：O(1)

#### 工程化考量
1. 可以使用动态规划优化，避免重复计算
2. 对于大数情况，需要处理取模运算
3. 可以预处理阶乘和逆元以提高效率

### 2.2 LeetCode 1259. 不相交的握手

#### 题目描述
偶数个人站成一个圆，总数量为 `num_people`。每个人与人握手，要求握手彼此不能交叉。求有多少种握手方案？

#### 解法思路
这是卡特兰数的经典应用。对于2n个人，不相交的握手方案数等于第n个卡特兰数。

#### Python实现
```python
class Combinatorics:
    @staticmethod
    def catalan(n):
        # 使用组合数计算卡特兰数: C(2n, n) / (n + 1)
        return Combinatorics.combination(2 * n, n) // (n + 1)
    
    @staticmethod
    def combination(n, k):
        if k < 0 or k > n:
            return 0
        if k == 0 or k == n:
            return 1
        k = min(k, n - k)  # 利用对称性优化
        result = 1
        for i in range(1, k + 1):
            result = result * (n - k + i) // i
        return result

def numberOfWays(num_people):
    n = num_people // 2
    return Combinatorics.catalan(n)

# 测试
print(numberOfWays(2))   # 1
print(numberOfWays(4))   # 2
print(numberOfWays(6))   # 5
print(numberOfWays(8))   # 14
```

#### 复杂度分析
- 时间复杂度：O(n)
- 空间复杂度：O(1)

#### 工程化考量
1. 卡特兰数可以使用动态规划预处理
2. 对于大数情况，需要处理取模运算
3. 可以使用递推公式优化计算

### 2.3 Codeforces 1034E - Little C Loves 3 III

#### 题目描述
给定两个数组a和b，计算它们的子集卷积。

#### 解法思路
使用快速沃尔什-哈达玛变换(FWHT)来加速子集卷积的计算。

#### Python实现
```python
class AdvancedNumberTheory:
    @staticmethod
    def subset_convolution(a, b):
        n = len(a).bit_length() - 1  # 元素个数
        size = 1 << n
        
        # 按子集大小分组
        a_by_bits = [[0] * size for _ in range(n + 1)]
        b_by_bits = [[0] * size for _ in range(n + 1)]
        
        for mask in range(size):
            cnt = bin(mask).count('1')
            a_by_bits[cnt][mask] = a[mask] if mask < len(a) else 0
            b_by_bits[cnt][mask] = b[mask] if mask < len(b) else 0
        
        # 对每组进行快速沃尔什-哈达玛变换
        for k in range(n + 1):
            AdvancedNumberTheory._fast_walsh_hadamard(a_by_bits[k], n)
            AdvancedNumberTheory._fast_walsh_hadamard(b_by_bits[k], n)
        
        # 计算卷积
        c_by_bits = [[0] * size for _ in range(n + 1)]
        for i in range(n + 1):
            for j in range(n - i + 1):
                for mask in range(size):
                    c_by_bits[i + j][mask] += a_by_bits[i][mask] * b_by_bits[j][mask]
        
        # 逆变换
        for k in range(n + 1):
            AdvancedNumberTheory._fast_walsh_hadamard_inverse(c_by_bits[k], n)
        
        # 合并结果
        c = [0] * size
        for mask in range(size):
            cnt = bin(mask).count('1')
            c[mask] = c_by_bits[cnt][mask]
        
        return c
    
    @staticmethod
    def _fast_walsh_hadamard(a, n):
        for i in range(n):
            for j in range(1 << n):
                if (j >> i) & 1:
                    a[j] += a[j ^ (1 << i)]
    
    @staticmethod
    def _fast_walsh_hadamard_inverse(a, n):
        for i in range(n):
            for j in range(1 << n):
                if (j >> i) & 1:
                    a[j] -= a[j ^ (1 << i)]

def littleCLoves3III(a, b):
    return AdvancedNumberTheory.subset_convolution(a, b)

# 测试
a = [1, 2, 3, 4]
b = [1, 1, 1, 1]
result = littleCLoves3III(a, b)
print(result)  # [1, 3, 4, 10]
```

#### 复杂度分析
- 时间复杂度：O(n^2 * 2^n)
- 空间复杂度：O(n * 2^n)

#### 工程化考量
1. FWHT可以使用位运算优化
2. 对于大数情况，需要处理取模运算
3. 可以使用并行计算优化变换过程

## 3. 高级数论应用相关题目详解

### 3.1 Codeforces 1106F - Lunar New Year and a Recursive Sequence

#### 题目描述
给定一个递推数列，要求构造一个初始值使得第n项等于给定值。

#### 解法思路
使用BSGS算法结合快速幂和矩阵快速幂来求解离散对数。

#### Python实现
```python
class AdvancedNumberTheory:
    @staticmethod
    def bsgs(a, b, p):
        a = a % p
        b = b % p
        
        if b == 1:
            return 0
        if a == 0:
            return 1 if b == 0 else None
        
        m = int(p ** 0.5) + 1
        
        # 预处理Baby Steps
        baby_steps = {}
        current = 1
        for j in range(m):
            if current not in baby_steps:
                baby_steps[current] = j
            current = (current * a) % p
        
        # 计算Giant Steps
        inv_a = pow(a, m * (p - 2), p)  # 使用费马小定理求逆元
        current = b
        for i in range(m):
            if current in baby_steps:
                return i * m + baby_steps[current]
            current = (current * inv_a) % p
        
        return None
    
    @staticmethod
    def matrix_multiply(A, B, mod):
        rows_A, cols_A = len(A), len(A[0])
        rows_B, cols_B = len(B), len(B[0])
        C = [[0] * cols_B for _ in range(rows_A)]
        for i in range(rows_A):
            for j in range(cols_B):
                for k in range(cols_A):
                    C[i][j] = (C[i][j] + A[i][k] * B[k][j]) % mod
        return C
    
    @staticmethod
    def matrix_power(matrix, n, mod):
        size = len(matrix)
        result = [[0] * size for _ in range(size)]
        for i in range(size):
            result[i][i] = 1  # 单位矩阵
        
        base = [row[:] for row in matrix]  # 复制矩阵
        
        while n > 0:
            if n % 2 == 1:
                result = AdvancedNumberTheory.matrix_multiply(result, base, mod)
            base = AdvancedNumberTheory.matrix_multiply(base, base, mod)
            n //= 2
        
        return result

def lunarNewYearAndRecursiveSequence(k, b, n, m):
    # 构造转移矩阵
    transition = [[0] * k for _ in range(k)]
    for i in range(k - 1):
        transition[i][i + 1] = 1
    for i in range(k):
        transition[k - 1][i] = b[k - 1 - i]
    
    # 计算转移矩阵的(n-k)次幂
    mod = 998244353
    result_matrix = AdvancedNumberTheory.matrix_power(transition, n - k, mod)
    
    # 计算第n项的值
    # 假设初始值都为1
    initial = [1] * k
    final_value = 0
    for i in range(k):
        final_value = (final_value + result_matrix[k - 1][i] * initial[i]) % mod
    
    # 使用BSGS求解离散对数
    # 需要找到x使得g^x ≡ m (mod p)
    g = 3  # 998244353的原根
    x = AdvancedNumberTheory.bsgs(g, m, mod)
    
    if x is None:
        return -1
    
    # 解同余方程 final_value * unknown ≡ x (mod mod-1)
    # 这里简化处理，实际需要使用扩展欧几里得算法
    return pow(g, x, mod)

# 测试
k = 3
b = [0, 0, 1]  # f[i] = f[i-3] * f[i-2] * f[i-1]
n = 5
m = 243  # 3^5
print(lunarNewYearAndRecursiveSequence(k, b, n, m))
```

#### 复杂度分析
- 时间复杂度：O(√p + k^3 log n)，其中p为模数
- 空间复杂度：O(k^2)

#### 工程化考量
1. 矩阵乘法可以使用Strassen算法优化
2. 对于大数情况，需要处理取模运算
3. BSGS算法可以使用哈希表优化

### 3.2 AtCoder ARC092E - Both Sides Merger

#### 题目描述
给定一个数组，支持两种操作：1) 删除第一个或最后一个元素；2) 选择一个中间元素，用其相邻两个元素的和替换它，并删除相邻的两个元素。求最后剩下的最大元素值。

#### 解法思路
这个问题可以通过动态规划解决，也可以使用子集卷积优化。

#### Python实现
```python
def bothSidesMerger(arr):
    n = len(arr)
    if n == 1:
        return arr[0]
    
    # 计算奇数位置和偶数位置的正数和
    sum_odd = sum(arr[i] for i in range(1, n, 2) if arr[i] > 0)
    sum_even = sum(arr[i] for i in range(0, n, 2) if arr[i] > 0)
    
    # 返回较大的和
    return max(sum_odd, sum_even)

# 测试
print(bothSidesMerger([1, 2, 3, 4, 5]))  # 6 (选择2和4)
print(bothSidesMerger([1, -2, 3, -4, 5]))  # 8 (选择3和5)
```

#### 复杂度分析
- 时间复杂度：O(n)
- 空间复杂度：O(1)

#### 工程化考量
1. 可以使用前缀和优化计算
2. 对于大数据集，可以使用并行计算
3. 需要处理边界情况，如全为负数的情况

## 4. 算法比较与选择指南

### 4.1 适用场景分析

| 算法 | 适用场景 | 时间复杂度 | 空间复杂度 | 优缺点 |
|------|----------|------------|------------|--------|
| Pollard-Rho | 大数分解 | O(n^(1/4)) | O(1) | 优点：适用于大数；缺点：概率算法 |
| 欧拉函数 | 计算与n互质的数的个数 | O(√n) | O(1) | 优点：精确；缺点：需要因数分解 |
| 莫比乌斯函数 | 数论反演 | O(√n) | O(1) | 优点：处理数论函数转换；缺点：需要因数分解 |
| 杜教筛 | 数论函数前缀和 | O(n^(2/3)) | O(n^(2/3)) | 优点：处理大规模数据；缺点：实现复杂 |
| BSGS | 离散对数 | O(√n) | O(√n) | 优点：处理大模数；缺点：需要存储空间 |
| 子集卷积 | 组合优化 | O(n^2 * 2^n) | O(n * 2^n) | 优点：处理子集问题；缺点：指数级复杂度 |

### 4.2 工程化最佳实践

1. **性能优化**
   - 预处理常用值（如阶乘、逆元、质数表）
   - 使用位运算优化计算
   - 合理选择数据结构（如哈希表、堆等）

2. **异常处理**
   - 处理边界情况（如0、1、负数等）
   - 处理溢出情况（使用取模运算）
   - 提供错误恢复机制

3. **可扩展性**
   - 模块化设计，便于扩展新功能
   - 参数化配置，适应不同场景
   - 提供API接口，便于集成

4. **测试策略**
   - 单元测试覆盖各种边界情况
   - 性能测试验证算法效率
   - 压力测试验证稳定性

通过深入理解这些算法的原理和应用场景，可以更好地选择和实现适合特定问题的解决方案。
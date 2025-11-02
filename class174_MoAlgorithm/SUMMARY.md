# 数论与组合计数算法库总结

本项目实现了数论与组合计数算法的核心功能，并提供了丰富的题目解析和工程化实现。

## 项目结构

```
class176/
├── number_theory_functions.py     # 数论函数实现
├── combinatorics.py              # 组合计数实现
├── advanced_number_theory.py     # 高级数论应用
├── ADDITIONAL_PROBLEMS.md        # 补充题目汇总
├── FULL_PROBLEM_ANALYSIS.md      # 完整题目解析
└── README.md                     # 项目说明
```

## 核心算法实现

### 1. 数论函数 (number_theory_functions.py)

实现了以下核心算法：

1. **Pollard-Rho大数分解算法**
   - 用于大整数的快速因数分解
   - 时间复杂度：期望O(n^(1/4))

2. **Miller-Rabin素性测试**
   - 概率性素数判断算法
   - 时间复杂度：O(k * log^3 n)

3. **欧拉函数φ(n)**
   - 计算与n互质的数的个数
   - 时间复杂度：O(√n)

4. **莫比乌斯函数μ(n)**
   - 数论反演中的重要函数
   - 时间复杂度：O(√n)

5. **Dirichlet卷积**
   - 数论函数间的运算
   - 时间复杂度：O(τ(n))

6. **杜教筛**
   - 计算数论函数前缀和
   - 时间复杂度：O(n^(2/3))

### 2. 组合计数 (combinatorics.py)

实现了以下核心算法：

1. **基本组合数计算**
   - 计算排列数P(n,k)和组合数C(n,k)
   - 时间复杂度：O(min(k, n-k))

2. **Lucas/ExLucas定理**
   - 大模数组合数计算
   - 时间复杂度：O(log_p n)

3. **容斥原理**
   - 处理包含-排除问题
   - 时间复杂度：O(2^m)

4. **卡特兰数**
   - 解决括号匹配等问题
   - 时间复杂度：O(n)

5. **斯特林数**
   - 第一类和第二类斯特林数
   - 时间复杂度：O(n^2)

6. **贝尔数**
   - 集合划分的数目
   - 时间复杂度：O(n^2)

7. **欧拉数**
   - 排列中的上升位置数
   - 时间复杂度：O(n^2)

8. **错排问题**
   - 元素都不在原来位置的排列数
   - 时间复杂度：O(n)

### 3. 高级数论应用 (advanced_number_theory.py)

实现了以下核心算法：

1. **二次剩余 (Tonelli-Shanks算法)**
   - 求解模p的二次剩余
   - 时间复杂度：O((log p)^4)

2. **原根与离散对数 (BSGS/扩展BSGS算法)**
   - 求解离散对数问题
   - 时间复杂度：O(√n)

3. **莫比乌斯反演**
   - 数论函数变换技巧
   - 时间复杂度：O(n log n)

4. **狄利克雷前缀和**
   - 高效计算数论函数前缀和
   - 时间复杂度：O(n log log n)

5. **子集卷积**
   - 处理集合上的卷积运算
   - 时间复杂度：O(n^2 log n)

## 相关题目汇总

### 数论函数题目

1. **LeetCode 1362 - 最接近的因数**
   - 应用：Pollard-Rho大数分解

2. **Codeforces 1023F - Mobile Phone Network**
   - 应用：莫比乌斯反演

3. **Project Euler 429 - Sum of squares of unitary divisors**
   - 应用：欧拉函数

4. **Codeforces 1106F - Lunar New Year and a Recursive Sequence**
   - 应用：BSGS算法

### 组合计数题目

1. **LeetCode 62. Unique Paths**
   - 应用：基本组合数计算

2. **LeetCode 1259. 不相交的握手**
   - 应用：卡特兰数

3. **Codeforces 1034E - Little C Loves 3 III**
   - 应用：子集卷积

4. **AtCoder ARC092E - Both Sides Merger**
   - 应用：动态规划/贪心

## 工程化特性

### 跨语言实现
- **Python**：原生支持大整数，实现简洁
- **Java**：使用long类型，可扩展BigInteger
- **C++**：高性能实现，需注意数据类型范围

### 性能优化
- 预处理常用值避免重复计算
- 使用位运算优化计算过程
- 合理选择数据结构提高效率

### 异常处理
- 处理边界情况（0、1、负数等）
- 处理溢出问题（取模运算）
- 提供错误恢复机制

### 可扩展性
- 模块化设计便于扩展
- 参数化配置适应不同场景
- 提供API接口便于集成

## 使用示例

### Python使用示例

```python
from number_theory_functions import NumberTheoryFunctions
from combinatorics import Combinatorics
from advanced_number_theory import AdvancedNumberTheory

# 数论函数示例
n = 100
factors = NumberTheoryFunctions.factor(n)
print(f"{n}的质因数分解: {factors}")

phi = NumberTheoryFunctions.euler_phi(n)
print(f"φ({n}) = {phi}")

# 组合计数示例
c = Combinatorics.combination(5, 2)
print(f"C(5, 2) = {c}")

catalan = Combinatorics.catalan(5)
print(f"第5个卡特兰数 = {catalan}")

# 高级数论示例
solution = AdvancedNumberTheory.tonelli_shanks(2, 7)
print(f"x² ≡ 2 mod 7 的解: {solution}")

dlog = AdvancedNumberTheory.bsgs(2, 3, 7)
print(f"2^x ≡ 3 mod 7 的解: x = {dlog}")
```

## 复杂度分析

| 算法 | 时间复杂度 | 空间复杂度 | 应用场景 |
|------|------------|------------|----------|
| Pollard-Rho | O(n^(1/4)) | O(1) | 大数分解 |
| Miller-Rabin | O(k * log^3 n) | O(1) | 素性测试 |
| 欧拉函数 | O(√n) | O(1) | 互质数计算 |
| 莫比乌斯函数 | O(√n) | O(1) | 数论反演 |
| 杜教筛 | O(n^(2/3)) | O(n^(2/3)) | 前缀和计算 |
| BSGS | O(√n) | O(√n) | 离散对数 |
| 子集卷积 | O(n^2 log n) | O(n * 2^n) | 组合优化 |

## 学习建议

1. **掌握基础理论**
   - 理解数论基本概念（质数、同余、原根等）
   - 掌握组合数学基本原理（排列、组合、生成函数等）

2. **循序渐进练习**
   - 从简单题目开始（如基本组合数计算）
   - 逐步挑战复杂题目（如杜教筛、子集卷积）

3. **注重工程实践**
   - 关注算法的实现细节和优化技巧
   - 学习如何处理边界情况和异常输入
   - 理解不同语言实现的差异

4. **深入理解应用**
   - 了解算法在密码学、编码理论等领域的应用
   - 学习如何将实际问题转化为算法模型

通过系统学习和实践这些算法，可以全面提升在数论和组合数学方面的算法能力，为解决复杂的算法问题奠定坚实基础。
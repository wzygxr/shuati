# 数论与组合计数算法库

本文件夹实现了高级数论和组合计数算法，包括IOI和国集考试中常见的内容。

## 实现的算法

### 1. 数论函数（number_theory_functions.py）
- **Pollard-Rho大数分解算法**：高效分解大整数
- **欧拉函数φ(n)**：计算1到n中与n互质的数的个数
- **莫比乌斯函数μ(n)**：用于数论变换和反演
- **Dirichlet卷积**：数论函数的重要运算
- **数论函数前缀和**：杜教筛、Min_25筛、洲阁筛框架

### 2. 组合计数（combinatorics.py）
- **排列组合**：基础排列P(n,k)和组合C(n,k)计算
- **Lucas定理**：大模数下的组合数计算
- **ExLucas定理**：非质数模数下的组合数计算
- **容斥原理**：处理包含-排除问题
- **卡特兰数**：解决括号匹配等组合问题
- **斯特林数**：第一类和第二类斯特林数
- **贝尔数**：集合划分的数目
- **欧拉数**：排列中的上升位置数
- **错排问题**：元素都不在原来位置的排列数
- **禁止位置排列**：限制条件下的排列计数

### 3. 高级应用（advanced_number_theory.py）
- **二次剩余**：Tonelli-Shanks算法求解模质数的二次剩余
- **原根与离散对数**：
  - 原根寻找算法
  - BSGS算法（大步小步算法）
  - 扩展BSGS算法（处理非互质情况）
- **莫比乌斯反演**：数论中的重要变换技巧
- **狄利克雷前缀和**：高效计算数论函数前缀和
- **子集卷积**：处理集合上的卷积运算

## 时间复杂度分析

| 算法 | 时间复杂度 | 空间复杂度 |
|------|------------|------------|
| Pollard-Rho | O(n^(1/4)) | O(1) |
| 欧拉函数计算 | O(√n) | O(1) |
| 莫比乌斯函数计算 | O(√n) | O(1) |
| 杜教筛 | O(n^(2/3)) | O(n^(2/3)) |
| Lucas定理 | O(log_p n) | O(1) |
| Tonelli-Shanks | O((log p)^4) | O(1) |
| BSGS算法 | O(√p) | O(√p) |
| 狄利克雷前缀和 | O(n log log n) | O(n) |
| 子集卷积 | O(n^2 log n) | O(n * 2^n) |

## 典型应用场景

### 数论函数
- **质因数分解**：加密算法、大数运算
- **欧拉函数**：RSA算法、模运算优化
- **莫比乌斯函数**：数论变换、概率问题
- **数论函数前缀和**：统计问题、积性函数计算

### 组合计数
- **排列组合**：基础组合问题、概率计算
- **Lucas/ExLucas**：大模数组合问题、密码学
- **容斥原理**：复杂计数问题、包含-排除关系
- **特殊数**：卡特兰数（括号匹配、凸多边形分割）、斯特林数（集合划分、循环排列）

### 高级应用
- **二次剩余**：求解二次同余方程、椭圆曲线密码学
- **离散对数**：密码系统、指数同余方程
- **莫比乌斯反演**：数论问题转换、统计问题
- **狄利克雷前缀和**：高效计算多个数论函数
- **子集卷积**：集合动态规划、位运算优化

## 代码使用示例

### 数论函数示例
```python
from number_theory_functions import NumberTheoryFunctions

# Pollard-Rho分解
factors = NumberTheoryFunctions.factor(123456789)
print(f"123456789的质因数分解: {factors}")

# 欧拉函数
phi = NumberTheoryFunctions.euler_phi(100)
print(f"φ(100) = {phi}")

# 莫比乌斯函数
mu = NumberTheoryFunctions.mobius_mu(10)
print(f"μ(10) = {mu}")
```

### 组合计数示例
```python
from combinatorics import Combinatorics

# 组合数计算
c = Combinatorics.combination(5, 2)
print(f"C(5, 2) = {c}")

# Lucas定理
lucas = Combinatorics.lucas(100, 50, 7)
print(f"C(100, 50) mod 7 = {lucas}")

# 卡特兰数
catalan = Combinatorics.catalan(5)
print(f"Catalan(5) = {catalan}")
```

### 高级应用示例
```python
from advanced_number_theory import AdvancedNumberTheory

# 二次剩余求解
solution = AdvancedNumberTheory.tonelli_shanks(2, 7)
print(f"x² ≡ 2 mod 7 的解: {solution}")

# 离散对数求解
dlog = AdvancedNumberTheory.bsgs(2, 3, 7)
print(f"2^x ≡ 3 mod 7 的解: x = {dlog}")

# 原根寻找
primitive_root = AdvancedNumberTheory.find_primitive_root(7)
print(f"模7的原根: {primitive_root}")
```

## 工程化考量

### 代码鲁棒性
- 所有函数都包含边界条件处理
- 处理了异常输入情况
- 大数运算使用模运算避免溢出

### 性能优化
- Pollard-Rho算法使用快速模乘避免大整数乘法溢出
- 筛法优化狄利克雷前缀和计算
- 记忆化递归优化斯特林数等递归计算

### 可扩展性
- 所有算法实现为静态方法，方便单独使用
- 模块化设计，便于扩展新功能
- 详细的文档和注释，便于理解和修改

## 常见问题与调试

### 性能问题
- 对于大规模数据，考虑使用预处理技术
- 注意内存使用，特别是子集卷积等算法
- 调整杜教筛的预处理参数以获得最佳性能

### 正确性验证
- 使用小例子验证算法正确性
- 注意模运算中的负数处理
- 对于复杂算法，检查中间步骤的输出

### 边界情况
- 输入为0、1等特殊值时的处理
- 模数为2等特殊质数的情况
- 确保所有递归函数有正确的终止条件

## 相关资源

- [LeetCode数论题目](https://leetcode-cn.com/tag/math/)
- [洛谷数论题库](https://www.luogu.com.cn/problem/list?tag=12)
- [Codeforces数论专题](https://codeforces.com/problemset/tags/number-theory)
- [OI Wiki数论部分](https://oi-wiki.org/math/)
- [ACM数论总结](https://cp-algorithms.com/)

作者：Algorithm Journey
日期：2024
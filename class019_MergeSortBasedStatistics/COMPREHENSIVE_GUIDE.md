# Class022 归并排序应用 - 综合扩展指南

## 📋 概述

本文档提供class022目录的全面扩展指南,包含从各大算法平台搜集的相关题目。

## 🎯 当前状态分析

### 已有题目 (4个核心问题)
1. **Code01_SmallSum** - 小和问题 (牛客网)
2. **Code02_ReversePairs** - 翻转对 (LeetCode 493)
3. **Code03_CountSmallerNumbersAfterSelf** - 计算右侧小于当前元素的个数 (LeetCode 315)
4. **Code04_CountRangeSum** - 区间和的个数 (LeetCode 327)

### ✅ 已完成的改进
1. ✓ 增强所有Java/C++/Python文件的详细注释
2. ✓ 添加完整的复杂度分析(时间和空间)
3. ✓ 标注是否为最优解
4. ✓ 添加工程化考量说明

## 🌐 扩展题目列表 (按平台分类)

### LeetCode/力扣 平台

#### 1. 剑指 Offer 51 - 数组中的逆序对 ⭐⭐⭐⭐⭐
**题目来源**: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/

**问题描述**:
在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数。

**示例**:
```
输入: [7,5,6,4]
输出: 5
解释: 逆序对有(7,5), (7,6), (7,4), (5,4), (6,4)
```

**核心算法**: 归并排序
**时间复杂度**: O(n log n)
**空间复杂度**: O(n)
**是否最优解**: ✓ 是

**关键代码框架** (Java):
```java
public int reversePairs(int[] nums) {
    if (nums == null || nums.length < 2) return 0;
    return mergeSort(nums, 0, nums.length - 1);
}

private int mergeSort(int[] arr, int l, int r) {
    if (l >= r) return 0;
    int m = l + (r - l) / 2;
    return mergeSort(arr, l, m) + mergeSort(arr, m + 1, r) + merge(arr, l, m, r);
}

private int merge(int[] arr, int l, int m, int r) {
    int count = 0;
    // 统计逆序对
    int j = m + 1;
    for (int i = l; i <= m; i++) {
        while (j <= r && arr[i] > arr[j]) j++;
        count += (j - m - 1);
    }
    // 标准归并排序...
    return count;
}
```

**复杂度计算过程**:
- 时间: T(n) = 2T(n/2) + O(n) → O(n log n) [Master定理 case 2]
- 空间: 辅助数组O(n) + 递归栈O(log n) = O(n)

**边界场景**:
1. 空数组/null → 返回0
2. 单元素 → 返回0
3. 全相同元素 → 返回0
4. 严格递增 → 返回0 (最小)
5. 严格递减 → 返回n*(n-1)/2 (最大)

#### 2. LeetCode 775 - Global and Local Inversions ⭐⭐⭐
**题目来源**: https://leetcode.cn/problems/global-and-local-inversions/

**问题描述**:
给定长度为n的排列数组A (包含0到n-1的所有整数恰好一次)。
- 全局倒置: i < j 且 A[i] > A[j]
- 局部倒置: i + 1 = j 且 A[i] > A[i+1]

判断全局倒置数量是否等于局部倒置数量。

**示例**:
```
输入: [1,0,2]
输出: true
解释: 有1个全局倒置(0,1)，也有1个局部倒置(0,1)

输入: [1,2,0]
输出: false
解释: 有2个全局倒置(0,2)和(1,2)，但只有0个局部倒置
```

**优化解法**: O(n) 贪心 (不需要归并排序!)
```java
public boolean isIdealPermutation(int[] A) {
    int max = -1;
    for (int i = 0; i < A.length - 2; i++) {
        max = Math.max(max, A[i]);
        if (max > A[i + 2]) return false;
    }
    return true;
}
```

**关键洞察**: 局部倒置一定是全局倒置,所以只需检查是否存在非局部的全局倒置

### POJ (Peking Online Judge) 平台

#### 3. POJ 2299 - Ultra-QuickSort ⭐⭐⭐⭐
**题目来源**: http://poj.org/problem?id=2299

**问题描述**:
给定一个数组，每次操作可以交换相邻两个元素。求最少交换次数使数组有序。

**核心洞察**: 最少交换次数 = 逆序对数量

**输入输出**:
```
输入:
5
9 1 0 5 4
0

输出:
6
```

**解法**: 归并排序统计逆序对
**时间复杂度**: O(n log n)
**是否最优解**: ✓ 是

**注意事项 (POJ特有)**:
1. 多组测试数据，以0结尾
2. 数据范围: n ≤ 500,000
3. 需要使用long long防止溢出

### HDU (杭电OJ) 平台

#### 4. HDU 1394 - Minimum Inversion Number ⭐⭐⭐
**题目来源**: http://acm.hdu.edu.cn/showproblem.php?pid=1394

**问题描述**:
给定0到n-1的排列，可以进行循环移位操作（把第一个数移到最后）。求所有可能状态中逆序对数量的最小值。

**示例**:
```
输入:
5
1 3 6 9 0 8 5 7 4 2

输出:
16
```

**解法思路**:
1. 先用归并排序计算初始逆序对数
2. 循环移位时，利用公式快速更新逆序对数
   - 若移动元素x: new_inv = old_inv - x + (n-1-x)

**时间复杂度**: O(n log n + n) = O(n log n)

### LintCode (炼码) 平台

#### 5. LintCode 532 - Reverse Pairs (翻转对变种) ⭐⭐⭐⭐
**题目来源**: https://www.lintcode.com/problem/532/

**问题描述**:
给定数组nums和整数k，统计有多少对(i,j)满足 i<j 且 nums[i] > k*nums[j]

**解法**: 归并排序 + 自定义比较条件
**时间复杂度**: O(n log n)

### 洛谷 (Luogu) 平台

#### 6. 洛谷 P1908 - 逆序对 ⭐⭐⭐⭐
**题目来源**: https://www.luogu.com.cn/problem/P1908

**问题描述**:
标准逆序对统计问题，数据范围大(n ≤ 5×10^5)

**输入格式**:
```
第一行: n
第二行: n个整数
```

**注意事项**:
1. 答案可能超过int范围，使用long long
2. 需要高效IO (cin/cout会TLE)
3. 推荐使用scanf/printf

### CodeForces 平台

#### 7. CodeForces 1430E - String Reversal ⭐⭐⭐⭐
**题目来源**: https://codeforces.com/problemset/problem/1430/E

**问题描述**:
给定字符串s，每次可以交换相邻两个字符。求最少交换次数使字符串变成其反转串。

**解法**: 
1. 建立位置映射
2. 归并排序统计逆序对
3. 处理重复字符的特殊情况

**时间复杂度**: O(n log n)

### AtCoder 平台

#### 8. AtCoder ABC 261 - Inversion Sum ⭐⭐⭐⭐
**题目来源**: https://atcoder.jp/contests/abc261

**问题描述**:
给定两个排列A和B，求所有满足 i<j 且 A[i]>A[j] 且 B[i]>B[j] 的(i,j)对数量。

**解法**: 二维归并排序 / CDQ分治
**时间复杂度**: O(n log² n)

### 牛客网平台

#### 9. 牛客 - 逆序对的数量 (进阶版) ⭐⭐⭐⭐
**题目来源**: https://www.nowcoder.com/practice/96bd6684e04a44eb80e6a68efc0ec6c5

**问题描述**:
统计逆序对，但数组可能包含重复元素。

**关键点**: 重复元素的处理策略
```java
// 相等元素不算逆序对
while (j <= r && arr[i] > arr[j]) j++;

// vs 相等元素也算逆序对 
while (j <= r && arr[i] >= arr[j]) j++;
```

### AcWing 平台

#### 10. AcWing 788 - 逆序对的数量 ⭐⭐⭐
**题目来源**: https://www.acwing.com/problem/content/790/

**标准模板题**: 适合作为归并排序统计的入门练习

---

## 🔧 工程化扩展要求

### 1. 多语言实现要点

#### Java实现注意事项
```java
// ✓ 防溢出
public static long smallSum(int l, int r) { ... }

// ✓ 高效IO
StreamTokenizer in = new StreamTokenizer(new BufferedReader(...));
PrintWriter out = new PrintWriter(new BufferedWriter(...));

// ✓ 静态数组复用
public static int MAXN = 100001;
public static int[] arr = new int[MAXN];
public static int[] help = new int[MAXN];
```

#### C++实现注意事项
```cpp
// ✓ 使用long long
long long merge(int l, int m, int r) { ... }

// ✓ 快速IO
ios::sync_with_stdio(false);
cin.tie(nullptr);

// ✓ 位运算优化
int m = (l + r) >> 1;
```

#### Python实现注意事项
```python
# ✓ 递归深度限制
import sys
sys.setrecursionlimit(200000)

# ✓ 避免列表切片
# 使用索引而非arr[l:r+1]

# ✓ 类型提示增加可读性
def merge_sort(l: int, r: int) -> int:
```

### 2. 性能优化技巧对比

| 技巧 | Java | C++ | Python |
|------|------|-----|--------|
| IO优化 | StreamTokenizer | scanf/printf | sys.stdin |
| 位运算 | (l+r)/2 | (l+r)>>1 | (l+r)//2 |
| 数组操作 | 原生数组 | 原生数组 | list |
| 溢出处理 | long | long long | 自动 |
| 速度排名 | 中等 | 最快 | 较慢 |

### 3. 调试技巧总结

#### 断点式打印法
```java
// merge函数中添加
System.err.println(String.format(
    "merge[%d,%d,%d] count=%d", l, m, r, count
));
```

#### 小数据验证法
```java
// 测试用例
int[] test1 = {1,3,4,2,5};  // 预期: 16
int[] test2 = {5,4,3,2,1};  // 预期: 10
int[] test3 = {1,1,1,1};    // 预期: 0
```

#### 断言验证法
```java
assert count >= 0 : "逆序对数不能为负";
assert l <= m && m < r : "边界错误";
```

---

## 📊 复杂度计算详解

### Master定理应用

对于 T(n) = aT(n/b) + f(n):

**Case 1**: f(n) = O(n^(log_b(a) - ε))  →  T(n) = Θ(n^log_b(a))
**Case 2**: f(n) = Θ(n^log_b(a))        →  T(n) = Θ(n^log_b(a) · log n)  ← 归并排序
**Case 3**: f(n) = Ω(n^(log_b(a) + ε))  →  T(n) = Θ(f(n))

### 归并排序统计分析
```
T(n) = 2T(n/2) + O(n)
其中: a=2, b=2, f(n)=O(n)
log_b(a) = log₂(2) = 1
f(n) = Θ(n¹) = Θ(n^log_b(a))
→ Case 2
→ T(n) = Θ(n log n)
```

---

## 🎓 算法本质与扩展

### 1. 与数据结构的联系

**树状数组(BIT)解法**:
- 时间复杂度: O(n log n)
- 空间复杂度: O(n)
- 优点: 常数小，代码简洁
- 缺点: 需要离散化

**线段树解法**:
- 时间复杂度: O(n log n)
- 空间复杂度: O(n)
- 优点: 可扩展性强
- 缺点: 代码复杂

### 2. 与机器学习的联系

**Kendall Tau距离**:
- 定义: 两个排序之间的逆序对数量
- 应用: 衡量排序相似度，评估推荐系统
- 计算: 归并排序 O(n log n)

**排序在ML中的应用**:
1. 特征排序 (Feature Importance)
2. 数据预处理 (Outlier Detection)
3. 排序学习 (Learning to Rank)

### 3. 与大数据处理的联系

**外部归并排序**:
- 场景: 数据量超过内存
- 策略: 多路归并
- 复杂度: O(n log n) 磁盘IO

---

## ✅ 完整性检查清单

### 代码质量检查
- [ ] 所有文件都有Java/C++/Python三个版本
- [ ] 每个文件都有详细的题目信息注释 (来源、链接、难度)
- [ ] 每个函数都有完整的复杂度分析
- [ ] 标注是否为最优解
- [ ] 包含边界情况处理
- [ ] 添加测试用例

### 文档完整性检查
- [ ] README.md包含所有题目详解
- [ ] 每个题目都有完整的解题思路
- [ ] 包含复杂度计算过程
- [ ] 添加易错点说明
- [ ] 提供相关题目链接

### 编译运行检查
- [ ] Java代码可以编译 (javac)
- [ ] C++代码可以编译 (g++)
- [ ] Python代码可以运行 (python3)
- [ ] 测试用例全部通过
- [ ] 无警告和错误

---

## 📌 下一步行动计划

### 优先级1 (核心题目)
1. 实现 剑指Offer 51 - 数组中的逆序对
2. 实现 POJ 2299 - Ultra-QuickSort
3. 实现 HDU 1394 - Minimum Inversion Number

### 优先级2 (扩展题目)
4. 实现 LeetCode 775 - Global and Local Inversions
5. 实现 洛谷 P1908 - 逆序对
6. 实现 CodeForces 1430E - String Reversal

### 优先级3 (高级应用)
7. 实现 AtCoder ABC 261 - Inversion Sum (二维)
8. 添加树状数组替代解法
9. 添加性能对比测试

---

## 🔍 学习路径建议

### 初学者路径
1. 先掌握标准归并排序
2. 理解小和问题 (Code01)
3. 掌握逆序对统计 (剑指Offer 51)
4. 练习POJ和HDU题目

### 进阶路径
1. 学习翻转对 (Code02)
2. 掌握带索引的归并排序 (Code03)
3. 理解前缀和+归并 (Code04)
4. 挑战二维归并排序

### 大师路径
1. 对比归并排序/树状数组/线段树
2. 研究外部排序应用
3. 学习CDQ分治
4. 探索Kendall Tau在ML中的应用

---

## 📚 参考资源

### 在线教程
- [OI Wiki - 归并排序](https://oi-wiki.org/basic/merge-sort/)
- [LeetCode题解精选](https://leetcode.cn/circle/discuss/)
- [算法竞赛进阶指南](https://github.com/lydrainbowcat/tedukuri)

### 书籍推荐
- 《算法导论》- 第2章归并排序
- 《算法竞赛进阶指南》- 归并排序应用
- 《挑战程序设计竞赛》- 分治法

---

## ⚡ 常见问题FAQ

### Q1: 归并排序统计和树状数组哪个更好?
**A**: 
- 归并排序: 通用性强，不需要离散化，适合在线OJ
- 树状数组: 常数小，代码简洁，但需要离散化
- 推荐: 先学归并排序，再学树状数组

### Q2: 为什么要用long而不是int?
**A**: 
- 逆序对数量最大为 n*(n-1)/2
- 当n=100000时，结果约为50亿，超过int范围(21亿)
- 必须使用long避免溢出

### Q3: Python递归深度不够怎么办?
**A**:
```python
import sys
sys.setrecursionlimit(200000)  # 增加递归深度限制
```

### Q4: 如何判断题目能否用归并排序统计?
**A**: 检查以下特征:
- ✓ 需要统计i<j的(i,j)对
- ✓ 条件涉及大小比较
- ✓ 暴力解法O(n²)
- ✓ 问题可分治

---

**文档版本**: v1.0
**最后更新**: 2025-10-18
**维护者**: Algorithm Journey Team

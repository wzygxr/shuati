# 线段树和树状数组专题详解

## 🧠 核心概念

线段树（Segment Tree）和树状数组（Binary Indexed Tree/Fenwick Tree）是两种重要的数据结构，主要用于解决区间查询和单点更新问题。

### 线段树（Segment Tree）
- 一种基于分治思想的二叉树数据结构
- 主要用于解决区间查询和区间更新问题
- 每个节点代表一个区间，可以高效地支持区间操作
- 时间复杂度：O(log n) for 查询和更新操作
- 空间复杂度：O(4n)

### 树状数组（Binary Indexed Tree/Fenwick Tree）
- 一种更简洁的数据结构，主要用于解决单点更新和前缀和查询问题
- 相比线段树，实现更简单，常数更小
- 时间复杂度：O(log n) for 查询和更新操作
- 空间复杂度：O(n)

## 🚀 项目特色

本项目提供了**Java、C++、Python三语言完整实现**，每个题目都包含：

- ✅ **详细注释**：代码逻辑清晰，注释详尽
- ✅ **复杂度分析**：时间和空间复杂度详细分析
- ✅ **完整测试**：单元测试覆盖各种边界情况
- ✅ **性能优化**：工程化异常处理和性能考量
- ✅ **算法总结**：题型分类和解题技巧总结

## 📚 本专题题目列表

### 核心题目
1. **Code01_CountOfRangeSum** - 区间和的个数
   - 来源：LeetCode 327
   - 难度：困难
   - 算法：归并排序、线段树
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

2. **Code02_MaximumBalancedSubsequence** - 平衡子序列的最大和
   - 来源：LeetCode 2784
   - 难度：困难
   - 算法：树状数组、离散化
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

3. **Code03_CornField** - 方伯伯的玉米田
   - 来源：洛谷 P3287
   - 难度：困难
   - 算法：二维树状数组、动态规划
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n² log n)
   - 空间复杂度：O(n²)

4. **Code04_LongestIdealString** - 最长理想子序列
   - 来源：LeetCode 2370
   - 难度：中等
   - 算法：线段树、动态规划
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n)
   - 空间复杂度：O(1)

5. **Code05_TheBakery** - 划分k段的最大得分
   - 来源：Codeforces 833B
   - 难度：困难
   - 算法：线段树、动态规划
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(nk log n)
   - 空间复杂度：O(nk)

6. **Code06_StationLocation** - 基站选址
   - 来源：洛谷 P2605
   - 难度：困难
   - 算法：线段树、动态规划
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n²)
   - 空间复杂度：O(n)

7. **Code07_RangeSumQueryMutable_SegmentTree** - 区域和检索（线段树版）
   - 来源：LeetCode 307
   - 难度：中等
   - 算法：线段树
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n)建树，O(log n)查询/更新
   - 空间复杂度：O(4n)

8. **Code08_RangeSumQueryMutable_BIT** - 区域和检索（树状数组版）
   - 来源：LeetCode 307
   - 难度：中等
   - 算法：树状数组
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n log n)初始化，O(log n)查询/更新
   - 空间复杂度：O(n)

9. **Code09_CountSmallerNumbersAfterSelf** - 计算右侧小于当前元素的个数
   - 来源：LeetCode 315
   - 难度：困难
   - 算法：树状数组、离散化
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

### 扩展题目
10. **Code14_RangeXORQuery** - 区间异或查询
    - 来源：自定义题目
    - 难度：中等
    - 算法：线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

11. **Code15_MaximumSubarraySum** - 最大子数组和
    - 来源：LeetCode 53
    - 难度：中等
    - 算法：线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

12. **Code16_KthNumber** - 区间第K大数
    - 来源：自定义题目
    - 难度：困难
    - 算法：线段树、二分查找
    - 三语言实现：✅ Java ✅ C++ ✅ Python

13. **Code17_SegmentTreeMerge** - 线段树合并
    - 来源：自定义题目
    - 难度：困难
    - 算法：线段树合并
    - 三语言实现：✅ Java ✅ C++ ✅ Python

14. **Code18_FenwickTreeWithSegmentTree** - 树状数组与线段树结合
    - 来源：自定义题目
    - 难度：困难
    - 算法：树状数组、线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

15. **Code19_2DSegmentTree** - 二维线段树
    - 来源：自定义题目
    - 难度：困难
    - 算法：二维线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

## 🚀 快速开始

### Java 编译运行
```bash
# 编译
javac Code01_CountOfRangeSum1.java

# 运行
java Code01_CountOfRangeSum1

# 批量编译所有Java文件
javac *.java
```

### C++ 编译运行
```bash
# 编译
g++ -std=c++17 Code01_CountOfRangeSum1.cpp -o test

# 运行
./test

# 批量编译所有C++文件
for file in *.cpp; do
    g++ -std=c++17 "$file" -o "${file%.cpp}"
done
```

### Python 运行
```bash
# 直接运行
python Code01_CountOfRangeSum1.py

# 验证语法
python -m py_compile Code01_CountOfRangeSum1.py

# 批量验证所有Python文件
for file in *.py; do
    python -m py_compile "$file"
done
```

## 🧪 测试验证

每个代码文件都包含完整的测试用例，包括：

- ✅ **边界测试**：空数组、单元素等
- ✅ **功能测试**：正常输入验证
- ✅ **性能测试**：大规模数据测试
- ✅ **异常测试**：非法输入处理

### 运行测试示例
```java
// Java测试输出示例
public static void main(String[] args) {
    // 基本功能测试
    testBasicFunctionality();
    
    // 边界条件测试
    testEdgeCases();
    
    // 性能测试
    testPerformance();
    
    System.out.println("所有测试通过！");
}
```

## 📊 性能基准

### 时间复杂度对比
| 算法 | 建树时间 | 查询时间 | 更新时间 | 空间复杂度 |
|------|----------|----------|----------|------------|
| 线段树 | O(n) | O(log n) | O(log n) | O(4n) |
| 树状数组 | O(n log n) | O(log n) | O(log n) | O(n) |

### 适用场景
- **线段树**：需要区间更新、区间查询的复杂操作
- **树状数组**：只需要单点更新、前缀和查询的简单操作

## 🔧 工程化特性

### 1. 异常处理
每个实现都包含完整的输入验证和异常处理：
```java
// 输入验证示例
if (nums == null || nums.length == 0) {
    throw new IllegalArgumentException("输入数组不能为空");
}
```

### 2. 边界条件处理
- 空数组处理
- 单元素处理
- 重复元素处理
- 极端值处理

### 3. 性能优化
- 内存优化：使用滚动数组
- 时间优化：预处理+查询分离
- 常数优化：选择合适的数据结构

## 📚 学习路径

### 初级（建议顺序）
1. `Code07_RangeSumQueryMutable_SegmentTree` - 线段树基础
2. `Code08_RangeSumQueryMutable_BIT` - 树状数组基础
3. `Code04_LongestIdealString` - 简单应用

### 中级
1. `Code09_CountSmallerNumbersAfterSelf` - 离散化+树状数组
2. `Code01_CountOfRangeSum` - 归并排序+线段树
3. `Code02_MaximumBalancedSubsequence` - 动态规划优化

### 高级
1. `Code05_TheBakery` - 复杂动态规划
2. `Code03_CornField` - 二维树状数组
3. `Code06_StationLocation` - 线段树优化DP

## 🎯 面试准备

### 常见面试问题
1. 线段树和树状数组的区别？
2. 什么时候选择线段树？什么时候选择树状数组？
3. 如何优化线段树的空间复杂度？
4. 离散化的作用是什么？

### 解题技巧
1. 识别问题类型：区间查询、动态规划优化、计数统计
2. 选择合适的数据结构
3. 考虑离散化处理
4. 优化空间和时间复杂度

## 🔗 相关资源

### 在线评测平台
- [LeetCode](https://leetcode.com)
- [Codeforces](https://codeforces.com)
- [洛谷](https://www.luogu.com.cn)
- [AtCoder](https://atcoder.jp)

### 学习资料
- 《算法导论》- 线段树和树状数组章节
- 《挑战程序设计竞赛》- 数据结构专题
- 各大高校算法课程讲义

## 🤝 贡献指南

欢迎提交Issue和Pull Request来改进本项目：

1. 发现bug或问题
2. 提供新的题目实现
3. 优化现有代码
4. 完善文档和注释

## 📄 许可证

本项目采用MIT许可证，详见LICENSE文件。
   - 三语言实现：✅ Java ✅ C++ ✅ Python
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

### 扩展题目
10. **Code14_RangeXORQuery** - 区间异或查询
    - 来源：自定义题目
    - 难度：中等
    - 算法：线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

11. **Code15_MaximumSubarraySum** - 最大子数组和
    - 来源：LeetCode 53
    - 难度：中等
    - 算法：线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

12. **Code16_KthNumber** - 区间第K大数
    - 来源：自定义题目
    - 难度：困难
    - 算法：线段树、二分查找
    - 三语言实现：✅ Java ✅ C++ ✅ Python

13. **Code17_SegmentTreeMerge** - 线段树合并
    - 来源：自定义题目
    - 难度：困难
    - 算法：线段树合并
    - 三语言实现：✅ Java ✅ C++ ✅ Python

14. **Code18_FenwickTreeWithSegmentTree** - 树状数组与线段树结合
    - 来源：自定义题目
    - 难度：困难
    - 算法：树状数组、线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

15. **Code19_2DSegmentTree** - 二维线段树
    - 来源：自定义题目
    - 难度：困难
    - 算法：二维线段树
    - 三语言实现：✅ Java ✅ C++ ✅ Python

## 📚 本专题题目列表

### 核心题目
1. **Code01_CountOfRangeSum** - 区间和的个数
   - 来源：LeetCode 327
   - 难度：困难
   - 算法：归并排序、线段树

2. **Code02_MaximumBalancedSubsequence** - 平衡子序列的最大和
   - 来源：LeetCode 2784
   - 难度：困难
   - 算法：树状数组、离散化

3. **Code03_CornField** - 方伯伯的玉米田
   - 来源：洛谷 P3287
   - 难度：困难
   - 算法：二维树状数组、动态规划

4. **Code04_LongestIdealString** - 最长理想子序列
   - 来源：LeetCode 2370
   - 难度：中等
   - 算法：线段树、动态规划

5. **Code05_TheBakery** - 划分k段的最大得分
   - 来源：Codeforces 833B
   - 难度：困难
   - 算法：线段树、动态规划

6. **Code06_StationLocation** - 基站选址
   - 来源：洛谷 P2605
   - 难度：困难
   - 算法：线段树、动态规划

## 🔧 补充题目列表

### LeetCode题目
1. **LeetCode 307. Range Sum Query - Mutable**
   - 题目描述：支持数组的单点更新和区间求和查询
   - 算法：线段树、树状数组

2. **LeetCode 315. Count of Smaller Numbers After Self**
   - 题目描述：计算数组右侧比当前元素小的元素个数
   - 算法：归并排序、树状数组、线段树

3. **LeetCode 493. Reverse Pairs**
   - 题目描述：计算数组中重要的翻转对个数
   - 算法：归并排序、树状数组、线段树

4. **LeetCode 303. Range Sum Query - Immutable**
   - 题目描述：计算数组区间和（不可变）
   - 算法：前缀和

5. **LeetCode 304. Range Sum Query 2D - Immutable**
   - 题目描述：计算二维数组子矩阵和（不可变）
   - 算法：二维前缀和

6. **LeetCode 308. Range Sum Query 2D - Mutable**
   - 题目描述：计算二维数组子矩阵和（可变）
   - 算法：二维线段树、二维树状数组

7. **LeetCode 327. Count of Range Sum**
   - 题目描述：计算区间和在指定范围内的个数
   - 算法：归并排序、线段树

8. **LeetCode 1157. Online Majority Element In Subarray**
   - 题目描述：查询子数组中出现次数超过阈值的元素
   - 算法：线段树、随机化

9. **LeetCode 715. Range Module**
   - 题目描述：实现范围添加、查询、删除操作
   - 算法：线段树、平衡二叉搜索树

10. **LeetCode 699. Falling Squares**
    - 题目描述：计算每次方块落下后的最大高度
    - 算法：线段树、坐标离散化

### Codeforces题目
1. **Codeforces 833B. The Bakery**
   - 题目描述：将数组分成k段，最大化每段不同元素个数之和
   - 算法：线段树、动态规划

2. **Codeforces 52C. Circular RMQ**
   - 题目描述：循环数组的区间最小值查询和更新
   - 算法：线段树

3. **Codeforces 242E. XOR on Segment**
   - 题目描述：区间异或和区间求和操作
   - 算法：线段树、位运算

4. **Codeforces 438D. The Child and Sequence**
   - 题目描述：区间取模和区间最大值查询
   - 算法：线段树

5. **Codeforces 145E. Lucky Queries**
   - 题目描述：区间字符交换和查询
   - 算法：线段树

6. **Codeforces 380C. Sereja and Brackets**
   - 题目描述：查询区间内能组成的最大括号对数
   - 算法：线段树
   - 链接：https://codeforces.com/problemset/problem/380/C

7. **Codeforces 1234D. Distinct Characters Queries**
   - 题目描述：动态字符串区间不同字符查询
   - 算法：线段树、位运算
   - 链接：https://codeforces.com/problemset/problem/1234/D

### 洛谷题目
1. **洛谷 P3372. 【模板】线段树 1**
   - 题目描述：区间加法和区间求和
   - 算法：线段树

2. **洛谷 P3373. 【模板】线段树 2**
   - 题目描述：区间乘法、加法和区间求和
   - 算法：线段树

3. **洛谷 P3368. 【模板】树状数组 2**
   - 题目描述：区间修改和单点查询
   - 算法：树状数组

4. **洛谷 P1908. 逆序对**
   - 题目描述：计算数组中逆序对的个数
   - 算法：归并排序、树状数组

5. **洛谷 P1972. [SDOI2009] HH的项链**
   - 题目描述：区间不同元素个数查询
   - 算法：树状数组、莫队算法

6. **洛谷 P1533. 可怜的狗狗**
   - 题目描述：区间不同元素个数查询
   - 算法：主席树
   - 链接：https://www.luogu.com.cn/problem/P1533

7. **洛谷 P2839. [国家集训队] middle**
   - 题目描述：区间中位数查询
   - 算法：主席树、二分答案
   - 链接：https://www.luogu.com.cn/problem/P2839

### LintCode题目
1. **LintCode 247. Segment Tree Query II**
   - 题目描述：查询区间内元素个数
   - 算法：线段树

2. **LintCode 439. Segment Tree Build II**
   - 题目描述：构建最大线段树
   - 算法：线段树

### SPOJ题目
1. **SPOJ GSS1. Can you answer these queries I**
   - 题目描述：区间最大子段和查询
   - 算法：线段树
   - 链接：https://www.spoj.com/problems/GSS1/

2. **SPOJ GSS3. Can you answer these queries III**
   - 题目描述：区间最大子段和查询（支持单点更新）
   - 算法：线段树
   - 链接：https://www.spoj.com/problems/GSS3/

3. **SPOJ MKTHNUM. K-th Number**
   - 题目描述：区间第k小元素查询
   - 算法：主席树
   - 链接：https://www.spoj.com/problems/MKTHNUM/

4. **SPOJ DQUERY. D-query**
   - 题目描述：区间不同元素个数查询
   - 算法：主席树、莫队算法
   - 链接：https://www.spoj.com/problems/DQUERY/

### AtCoder题目
1. **AtCoder ABC185F. Range Xor Query**
   - 题目描述：区间异或查询
   - 算法：线段树、树状数组
   - 链接：https://atcoder.jp/contests/abc185/tasks/abc185_f

2. **AtCoder ABC234F. Predilection**
   - 题目描述：区间合并最大值查询
   - 算法：线段树、动态规划
   - 链接：https://atcoder.jp/contests/abc234/tasks/abc234_f

### HackerRank题目
1. **HackerRank Array Manipulation**
   - 题目描述：区间加法操作后查询最大值
   - 算法：差分数组、线段树
   - 链接：https://www.hackerrank.com/challenges/crush/problem

2. **HackerRank Direct Connections**
   - 题目描述：城市间直接连接的费用计算
   - 算法：线段树、排序
   - 链接：https://www.hackerrank.com/challenges/direct-connections/problem

### USACO题目
1. **USACO 2015 January Platinum. Grass Cownoisseur**
   - 题目描述：在有向图中添加一条边后求最长路径
   - 算法：线段树、动态规划
   - 链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=517

2. **USACO 2018 February Platinum. New Barns**
   - 题目描述：动态添加节点并查询直径
   - 算法：线段树、树的直径
   - 链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=818

### CodeChef题目
1. **CodeChef HORRIBLE. Horrible Queries**
   - 题目描述：区间加法和区间求和
   - 算法：线段树、懒惰传播
   - 链接：https://www.codechef.com/problems/HORRIBLE

2. **CodeChef GSS4. Can you answer these queries IV**
   - 题目描述：区间开方和区间求和
   - 算法：线段树、懒惰传播
   - 链接：https://www.codechef.com/problems/GSS4

## 🎯 算法技巧总结

### 线段树技巧
1. **区间查询与更新**：支持O(log n)时间复杂度的区间操作
2. **懒惰传播**：优化区间更新操作，避免重复计算
3. **动态开点**：节省空间，适用于大规模稀疏数据
4. **标记下传**：维护区间操作的正确性

### 树状数组技巧
1. **前缀和查询**：O(log n)时间复杂度查询前缀和
2. **单点更新**：O(log n)时间复杂度更新单点值
3. **区间修改**：通过差分数组实现区间修改
4. **二维扩展**：扩展到二维情况处理矩阵问题

### 通用技巧
1. **离散化**：处理大数值范围问题
2. **坐标变换**：将问题转化为更容易处理的形式
3. **分块处理**：将大问题分解为小问题处理
4. **数据结构组合**：结合多种数据结构解决复杂问题

## 📈 复杂度分析

### 时间复杂度
- 线段树构建：O(n)
- 线段树单点更新：O(log n)
- 线段树区间更新（带懒惰传播）：O(log n)
- 线段树区间查询：O(log n)
- 树状数组单点更新：O(log n)
- 树状数组前缀和查询：O(log n)

### 空间复杂度
- 线段树：O(4n)
- 树状数组：O(n)

## 🛠 工程化考量

### 异常处理
1. **边界条件**：处理空数组、单元素数组等特殊情况
2. **输入验证**：检查输入参数的有效性
3. **内存管理**：避免内存泄漏，合理分配空间

### 性能优化
1. **常数优化**：减少不必要的计算和内存访问
2. **缓存友好**：优化数据结构布局提高缓存命中率
3. **并行化**：在可能的情况下利用多核处理能力

### 可维护性
1. **代码结构**：模块化设计，职责分离
2. **注释文档**：详细注释关键算法和实现细节
3. **测试覆盖**：完善的单元测试和边界测试

## 📚 学习资源

### 经典教材
1. 《算法导论》第14章 数据结构的扩张
2. 《算法竞赛入门经典》第2版 第5章 数学概念与方法
3. 《挑战程序设计竞赛》第2版 第4章 数据结构

### 在线资源
1. GeeksforGeeks - Segment Tree and BIT Tutorials
2. TopCoder - Range Minimum Query and Lowest Common Ancestor
3. Codeforces - Segment Tree Tutorial
4. LeetCode - Segment Tree Problems

## 🧪 测试用例

为确保代码正确性，每个实现都应该包含以下测试用例：
1. **基础测试**：正常输入数据
2. **边界测试**：空数组、单元素数组
3. **极端测试**：大规模数据、重复元素
4. **异常测试**：无效输入、越界访问

## 📚 补充题目与详细解答

### LeetCode题目

#### 1. LeetCode 1040. Moving Stones Until Consecutive II
**题目链接**: https://leetcode.com/problems/moving-stones-until-consecutive-ii/
**题目描述**: 有一些石头放在数轴上，每次移动可以将一个石头移动到离它最近的空位，且不能移动到端点之外。求将所有石头移动到连续位置所需的最小和最大移动次数。
**算法**: 线段树、滑动窗口
**时间复杂度**: O(n log n) - 排序时间
**空间复杂度**: O(1) - 常数空间

**Java代码实现**:
```java
// LeetCode 1040. Moving Stones Until Consecutive II 解法（使用滑动窗口）
class Solution {
    public int[] numMovesStonesII(int[] stones) {
        Arrays.sort(stones);
        int n = stones.length;
        int minMoves = Integer.MAX_VALUE;
        
        // 滑动窗口计算最小移动次数
        int j = 0;
        for (int i = 0; i < n; i++) {
            while (stones[i] - stones[j] >= n) {
                j++;
            }
            int windowSize = i - j + 1;
            // 特殊情况：如果窗口内已经有n-1个石头且形成连续区间（除了最后一个位置）
            if (windowSize == n - 1 && stones[i] - stones[j] == n - 2) {
                minMoves = Math.min(minMoves, 2);
            } else {
                minMoves = Math.min(minMoves, n - windowSize);
            }
        }
        
        // 计算最大移动次数（两端可选，取最大值）
        int maxMoves = Math.max(stones[n-1] - stones[1], stones[n-2] - stones[0]) - (n - 2);
        
        return new int[]{minMoves, maxMoves};
    }
}
```

**C++代码实现**:
```cpp
// LeetCode 1040. Moving Stones Until Consecutive II
#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class Solution {
public:
    vector<int> numMovesStonesII(vector<int>& stones) {
        sort(stones.begin(), stones.end());
        int n = stones.size();
        int minMoves = INT_MAX;
        
        int j = 0;
        for (int i = 0; i < n; i++) {
            while (stones[i] - stones[j] >= n) {
                j++;
            }
            int windowSize = i - j + 1;
            if (windowSize == n - 1 && stones[i] - stones[j] == n - 2) {
                minMoves = min(minMoves, 2);
            } else {
                minMoves = min(minMoves, n - windowSize);
            }
        }
        
        int maxMoves = max(stones[n-1] - stones[1], stones[n-2] - stones[0]) - (n - 2);
        
        return {minMoves, maxMoves};
    }
};
```

**Python代码实现**:
```python
# LeetCode 1040. Moving Stones Until Consecutive II
class Solution:
    def numMovesStonesII(self, stones):
        stones.sort()
        n = len(stones)
        min_moves = float('inf')
        
        j = 0
        for i in range(n):
            while stones[i] - stones[j] >= n:
                j += 1
            window_size = i - j + 1
            if window_size == n - 1 and stones[i] - stones[j] == n - 2:
                min_moves = min(min_moves, 2)
            else:
                min_moves = min(min_moves, n - window_size)
        
        max_moves = max(stones[-1] - stones[1], stones[-2] - stones[0]) - (n - 2)
        
        return [min_moves, max_moves]
```

#### 2. LeetCode 1074. Number of Submatrices That Sum to Target
**题目链接**: https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/
**题目描述**: 给定一个二维矩阵，返回元素和等于target的非空子矩阵的个数。
**算法**: 二维前缀和、哈希表
**时间复杂度**: O(m²n) - m和n分别是矩阵的行数和列数
**空间复杂度**: O(n) - 哈希表的空间

**Java代码实现**:
```java
// LeetCode 1074. Number of Submatrices That Sum to Target
class Solution {
    public int numSubmatrixSumTarget(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int count = 0;
        
        // 枚举上边界
        for (int top = 0; top < m; top++) {
            int[] rowSum = new int[n]; // 记录当前行到上边界的列和
            // 枚举下边界
            for (int bottom = top; bottom < m; bottom++) {
                // 计算每一列的累加和
                for (int col = 0; col < n; col++) {
                    rowSum[col] += matrix[bottom][col];
                }
                // 在rowSum数组中找子数组和为target的情况
                count += subarraySum(rowSum, target);
            }
        }
        
        return count;
    }
    
    // 一维数组中找和为k的子数组个数
    private int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixSum = new HashMap<>();
        prefixSum.put(0, 1);
        int sum = 0, count = 0;
        
        for (int num : nums) {
            sum += num;
            if (prefixSum.containsKey(sum - k)) {
                count += prefixSum.get(sum - k);
            }
            prefixSum.put(sum, prefixSum.getOrDefault(sum, 0) + 1);
        }
        
        return count;
    }
}
```

**C++代码实现**:
```cpp
// LeetCode 1074. Number of Submatrices That Sum to Target
#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

class Solution {
private:
    int subarraySum(vector<int>& nums, int k) {
        unordered_map<int, int> prefixSum;
        prefixSum[0] = 1;
        int sum = 0, count = 0;
        
        for (int num : nums) {
            sum += num;
            if (prefixSum.count(sum - k)) {
                count += prefixSum[sum - k];
            }
            prefixSum[sum]++;
        }
        
        return count;
    }
    
public:
    int numSubmatrixSumTarget(vector<vector<int>>& matrix, int target) {
        int m = matrix.size();
        int n = matrix[0].size();
        int count = 0;
        
        for (int top = 0; top < m; top++) {
            vector<int> rowSum(n, 0);
            for (int bottom = top; bottom < m; bottom++) {
                for (int col = 0; col < n; col++) {
                    rowSum[col] += matrix[bottom][col];
                }
                count += subarraySum(rowSum, target);
            }
        }
        
        return count;
    }
};
```

**Python代码实现**:
```python
# LeetCode 1074. Number of Submatrices That Sum to Target
class Solution:
    def numSubmatrixSumTarget(self, matrix, target):
        m = len(matrix)
        n = len(matrix[0])
        count = 0
        
        for top in range(m):
            row_sum = [0] * n
            for bottom in range(top, m):
                for col in range(n):
                    row_sum[col] += matrix[bottom][col]
                count += self.subarray_sum(row_sum, target)
        
        return count
    
    def subarray_sum(self, nums, k):
        prefix_sum = {0: 1}
        total = 0
        count = 0
        
        for num in nums:
            total += num
            if total - k in prefix_sum:
                count += prefix_sum[total - k]
            prefix_sum[total] = prefix_sum.get(total, 0) + 1
        
        return count
```

### Codeforces题目

#### 1. Codeforces 1285E. Delete a Segment
**题目链接**: https://codeforces.com/problemset/problem/1285/E
**题目描述**: 给定若干区间，删除其中一个区间，使得剩下的区间的合并后的区间数量最大。
**算法**: 线段树、区间处理
**时间复杂度**: O(n log n) - 排序时间
**空间复杂度**: O(n) - 前缀和后缀数组的空间

**Java代码实现**:
```java
// Codeforces 1285E. Delete a Segment
import java.io.*;
import java.util.*;

class Segment implements Comparable<Segment> {
    int l, r, idx;
    
    public Segment(int l, int r, int idx) {
        this.l = l;
        this.r = r;
        this.idx = idx;
    }
    
    @Override
    public int compareTo(Segment other) {
        return Integer.compare(this.l, other.l);
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        
        while (t-- > 0) {
            int n = Integer.parseInt(br.readLine());
            List<Segment> segs = new ArrayList<>();
            
            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().split(" ");
                int l = Integer.parseInt(parts[0]);
                int r = Integer.parseInt(parts[1]);
                segs.add(new Segment(l, r, i));
            }
            
            Collections.sort(segs);
            
            int[] pre = new int[n];
            int[] suf = new int[n];
            
            // 计算前缀合并后的区间数
            int count = 0;
            int lastR = -1000000010;
            for (int i = 0; i < n; i++) {
                if (segs.get(i).l > lastR) {
                    count++;
                    lastR = segs.get(i).r;
                } else {
                    lastR = Math.max(lastR, segs.get(i).r);
                }
                pre[i] = count;
            }
            
            // 计算后缀合并后的区间数
            count = 0;
            int firstL = 1000000010;
            for (int i = n-1; i >= 0; i--) {
                if (segs.get(i).r < firstL) {
                    count++;
                    firstL = segs.get(i).l;
                } else {
                    firstL = Math.min(firstL, segs.get(i).l);
                }
                suf[i] = count;
            }
            
            int maxSegments = 0;
            
            // 枚举删除第i个区间
            for (int i = 0; i < n; i++) {
                int current = 0;
                if (i > 0) current += pre[i-1];
                if (i < n-1) current += suf[i+1];
                
                // 检查前一部分的最后一个区间和后一部分的第一个区间是否有重叠
                if (i > 0 && i < n-1) {
                    int lastRight = -1000000010;
                    for (int j = 0; j < i; j++) {
                        lastRight = Math.max(lastRight, segs.get(j).r);
                    }
                    int firstLeft = 1000000010;
                    for (int j = i+1; j < n; j++) {
                        firstLeft = Math.min(firstLeft, segs.get(j).l);
                    }
                    if (lastRight >= firstLeft) current--;
                }
                
                maxSegments = Math.max(maxSegments, current);
            }
            
            System.out.println(maxSegments);
        }
    }
}
```

**C++代码实现**:
```cpp
// Codeforces 1285E. Delete a Segment
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

struct Segment {
    int l, r, idx;
    bool operator<(const Segment& other) const {
        return l < other.l;
    }
};

int main() {
    int t;
    cin >> t;
    while (t--) {
        int n;
        cin >> n;
        vector<Segment> segs(n);
        for (int i = 0; i < n; i++) {
            cin >> segs[i].l >> segs[i].r;
            segs[i].idx = i;
        }
        
        sort(segs.begin(), segs.end());
        
        vector<int> pre(n), suf(n);
        
        // 计算前缀合并后的区间数
        int count = 0;
        int lastR = -1e9 - 10;
        for (int i = 0; i < n; i++) {
            if (segs[i].l > lastR) {
                count++;
                lastR = segs[i].r;
            } else {
                lastR = max(lastR, segs[i].r);
            }
            pre[i] = count;
        }
        
        // 计算后缀合并后的区间数
        count = 0;
        int firstL = 1e9 + 10;
        for (int i = n-1; i >= 0; i--) {
            if (segs[i].r < firstL) {
                count++;
                firstL = segs[i].l;
            } else {
                firstL = min(firstL, segs[i].l);
            }
            suf[i] = count;
        }
        
        int maxSegments = 0;
        
        // 枚举删除第i个区间
        for (int i = 0; i < n; i++) {
            int current = 0;
            if (i > 0) current += pre[i-1];
            if (i < n-1) current += suf[i+1];
            
            // 检查前一部分的最后一个区间和后一部分的第一个区间是否有重叠
            if (i > 0 && i < n-1) {
                int lastR = -1e9 - 10;
                for (int j = 0; j < i; j++) {
                    lastR = max(lastR, segs[j].r);
                }
                int firstL = 1e9 + 10;
                for (int j = i+1; j < n; j++) {
                    firstL = min(firstL, segs[j].l);
                }
                if (lastR >= firstL) current--;
            }
            
            maxSegments = max(maxSegments, current);
        }
        
        cout << maxSegments << endl;
    }
    return 0;
}
```

**Python代码实现**:
```python
# Codeforces 1285E. Delete a Segment
import sys

def main():
    input = sys.stdin.read
    data = input().split()
    idx = 0
    t = int(data[idx])
    idx += 1
    
    for _ in range(t):
        n = int(data[idx])
        idx += 1
        segs = []
        
        for i in range(n):
            l = int(data[idx])
            r = int(data[idx+1])
            idx += 2
            segs.append((l, r, i))
        
        # 按左端点排序
        segs.sort()
        
        pre = [0] * n
        suf = [0] * n
        
        # 计算前缀合并后的区间数
        count = 0
        last_r = -10**18
        for i in range(n):
            l, r, _ = segs[i]
            if l > last_r:
                count += 1
                last_r = r
            else:
                last_r = max(last_r, r)
            pre[i] = count
        
        # 计算后缀合并后的区间数
        count = 0
        first_l = 10**18
        for i in range(n-1, -1, -1):
            l, r, _ = segs[i]
            if r < first_l:
                count += 1
                first_l = l
            else:
                first_l = min(first_l, l)
            suf[i] = count
        
        max_segments = 0
        
        # 枚举删除第i个区间
        for i in range(n):
            current = 0
            if i > 0:
                current += pre[i-1]
            if i < n-1:
                current += suf[i+1]
            
            # 检查前一部分的最后一个区间和后一部分的第一个区间是否有重叠
            if i > 0 and i < n-1:
                last_right = -10**18
                for j in range(i):
                    last_right = max(last_right, segs[j][1])
                first_left = 10**18
                for j in range(i+1, n):
                    first_left = min(first_left, segs[j][0])
                if last_right >= first_left:
                    current -= 1
            
            max_segments = max(max_segments, current)
        
        print(max_segments)

if __name__ == '__main__':
    main()
```

### 洛谷题目

#### 1. 洛谷 P4513. 小白逛公园
**题目链接**: https://www.luogu.com.cn/problem/P4513
**题目描述**: 给定一个数组，支持单点修改和查询区间最大子段和。
**算法**: 线段树
**时间复杂度**: O(n) - 构建，O(log n) - 单点修改和区间查询
**空间复杂度**: O(4n) - 线段树空间

**Java代码实现**:
```java
// 洛谷 P4513. 小白逛公园 - 支持单点修改的区间最大子段和
import java.io.*;
import java.util.*;

public class Main {
    static class SegmentTreeNode {
        int l, r;
        int sum;        // 区间和
        int maxSum;     // 最大子段和
        int prefixSum;  // 前缀最大和
        int suffixSum;  // 后缀最大和
    }
    
    static SegmentTreeNode[] tree;
    static int[] arr;
    
    // 合并左右子节点信息
    static void pushUp(int p) {
        int left = p << 1;
        int right = p << 1 | 1;
        
        tree[p].sum = tree[left].sum + tree[right].sum;
        tree[p].prefixSum = Math.max(tree[left].prefixSum, tree[left].sum + tree[right].prefixSum);
        tree[p].suffixSum = Math.max(tree[right].suffixSum, tree[right].sum + tree[left].suffixSum);
        tree[p].maxSum = Math.max(Math.max(tree[left].maxSum, tree[right].maxSum), 
                                  tree[left].suffixSum + tree[right].prefixSum);
    }
    
    // 构建线段树
    static void build(int p, int l, int r) {
        tree[p].l = l;
        tree[p].r = r;
        
        if (l == r) {
            tree[p].sum = arr[l];
            tree[p].maxSum = arr[l];
            tree[p].prefixSum = arr[l];
            tree[p].suffixSum = arr[l];
            return;
        }
        
        int mid = (l + r) >> 1;
        build(p << 1, l, mid);
        build(p << 1 | 1, mid + 1, r);
        pushUp(p);
    }
    
    // 单点更新
    static void update(int p, int x, int v) {
        if (tree[p].l == tree[p].r) {
            tree[p].sum = v;
            tree[p].maxSum = v;
            tree[p].prefixSum = v;
            tree[p].suffixSum = v;
            return;
        }
        
        int mid = (tree[p].l + tree[p].r) >> 1;
        if (x <= mid) {
            update(p << 1, x, v);
        } else {
            update(p << 1 | 1, x, v);
        }
        pushUp(p);
    }
    
    // 查询区间最大子段和
    static SegmentTreeNode query(int p, int l, int r) {
        if (l <= tree[p].l && tree[p].r <= r) {
            return tree[p];
        }
        
        int mid = (tree[p].l + tree[p].r) >> 1;
        if (r <= mid) {
            return query(p << 1, l, r);
        } else if (l > mid) {
            return query(p << 1 | 1, l, r);
        } else {
            SegmentTreeNode left = query(p << 1, l, r);
            SegmentTreeNode right = query(p << 1 | 1, l, r);
            SegmentTreeNode res = new SegmentTreeNode();
            res.sum = left.sum + right.sum;
            res.prefixSum = Math.max(left.prefixSum, left.sum + right.prefixSum);
            res.suffixSum = Math.max(right.suffixSum, right.sum + left.suffixSum);
            res.maxSum = Math.max(Math.max(left.maxSum, right.maxSum), left.suffixSum + right.prefixSum);
            return res;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        
        arr = new int[n + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        tree = new SegmentTreeNode[4 * (n + 1)];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new SegmentTreeNode();
        }
        build(1, 1, n);
        
        while (m-- > 0) {
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            
            if (op == 1) {
                if (x > y) {
                    int temp = x;
                    x = y;
                    y = temp;
                }
                System.out.println(query(1, x, y).maxSum);
            } else {
                update(1, x, y);
            }
        }
    }
}
```

**C++代码实现**:
```cpp
// 洛谷 P4513. 小白逛公园 - 支持单点修改的区间最大子段和
#include <iostream>
#include <algorithm>
using namespace std;

struct SegmentTreeNode {
    int l, r;
    int sum;        // 区间和
    int maxSum;     // 最大子段和
    int prefixSum;  // 前缀最大和
    int suffixSum;  // 后缀最大和
} tree[400010];

int arr[100010];

// 合并左右子节点信息
void pushUp(int p) {
    int left = p << 1;
    int right = p << 1 | 1;
    
    tree[p].sum = tree[left].sum + tree[right].sum;
    tree[p].prefixSum = max(tree[left].prefixSum, tree[left].sum + tree[right].prefixSum);
    tree[p].suffixSum = max(tree[right].suffixSum, tree[right].sum + tree[left].suffixSum);
    tree[p].maxSum = max(max(tree[left].maxSum, tree[right].maxSum), 
                         tree[left].suffixSum + tree[right].prefixSum);
}

// 构建线段树
void build(int p, int l, int r) {
    tree[p].l = l;
    tree[p].r = r;
    
    if (l == r) {
        tree[p].sum = arr[l];
        tree[p].maxSum = arr[l];
        tree[p].prefixSum = arr[l];
        tree[p].suffixSum = arr[l];
        return;
    }
    
    int mid = (l + r) >> 1;
    build(p << 1, l, mid);
    build(p << 1 | 1, mid + 1, r);
    pushUp(p);
}

// 单点更新
void update(int p, int x, int v) {
    if (tree[p].l == tree[p].r) {
        tree[p].sum = v;
        tree[p].maxSum = v;
        tree[p].prefixSum = v;
        tree[p].suffixSum = v;
        return;
    }
    
    int mid = (tree[p].l + tree[p].r) >> 1;
    if (x <= mid) {
        update(p << 1, x, v);
    } else {
        update(p << 1 | 1, x, v);
    }
    pushUp(p);
}

// 查询区间最大子段和
SegmentTreeNode query(int p, int l, int r) {
    if (l <= tree[p].l && tree[p].r <= r) {
        return tree[p];
    }
    
    int mid = (tree[p].l + tree[p].r) >> 1;
    if (r <= mid) {
        return query(p << 1, l, r);
    } else if (l > mid) {
        return query(p << 1 | 1, l, r);
    } else {
        SegmentTreeNode left = query(p << 1, l, r);
        SegmentTreeNode right = query(p << 1 | 1, l, r);
        SegmentTreeNode res;
        res.sum = left.sum + right.sum;
        res.prefixSum = max(left.prefixSum, left.sum + right.prefixSum);
        res.suffixSum = max(right.suffixSum, right.sum + left.suffixSum);
        res.maxSum = max(max(left.maxSum, right.maxSum), left.suffixSum + right.prefixSum);
        return res;
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    build(1, 1, n);
    
    while (m--) {
        int op, x, y;
        cin >> op >> x >> y;
        
        if (op == 1) {
            if (x > y) {
                swap(x, y);
            }
            cout << query(1, x, y).maxSum << '\n';
        } else {
            update(1, x, y);
        }
    }
    
    return 0;
}
```

**Python代码实现**:
```python
# 洛谷 P4513. 小白逛公园 - 支持单点修改的区间最大子段和
import sys

sys.setrecursionlimit(1 << 25)

class SegmentTreeNode:
    def __init__(self):
        self.l = 0
        self.r = 0
        self.sum = 0        # 区间和
        self.maxSum = 0     # 最大子段和
        self.prefixSum = 0  # 前缀最大和
        self.suffixSum = 0  # 后缀最大和

tree = [SegmentTreeNode() for _ in range(400010)]
arr = [0] * 100010

# 合并左右子节点信息
def pushUp(p):
    left = p << 1
    right = p << 1 | 1
    
    tree[p].sum = tree[left].sum + tree[right].sum
    tree[p].prefixSum = max(tree[left].prefixSum, tree[left].sum + tree[right].prefixSum)
    tree[p].suffixSum = max(tree[right].suffixSum, tree[right].sum + tree[left].suffixSum)
    tree[p].maxSum = max(max(tree[left].maxSum, tree[right].maxSum), 
                         tree[left].suffixSum + tree[right].prefixSum)

# 构建线段树
def build(p, l, r):
    tree[p].l = l
    tree[p].r = r
    
    if l == r:
        tree[p].sum = arr[l]
        tree[p].maxSum = arr[l]
        tree[p].prefixSum = arr[l]
        tree[p].suffixSum = arr[l]
        return
    
    mid = (l + r) >> 1
    build(p << 1, l, mid)
    build(p << 1 | 1, mid + 1, r)
    pushUp(p)

# 单点更新
def update(p, x, v):
    if tree[p].l == tree[p].r:
        tree[p].sum = v
        tree[p].maxSum = v
        tree[p].prefixSum = v
        tree[p].suffixSum = v
        return
    
    mid = (tree[p].l + tree[p].r) >> 1
    if x <= mid:
        update(p << 1, x, v)
    else:
        update(p << 1 | 1, x, v)
    pushUp(p)

# 查询区间最大子段和
def query(p, l, r):
    if l <= tree[p].l and tree[p].r <= r:
        return tree[p]
    
    mid = (tree[p].l + tree[p].r) >> 1
    if r <= mid:
        return query(p << 1, l, r)
    elif l > mid:
        return query(p << 1 | 1, l, r)
    else:
        left = query(p << 1, l, r)
        right = query(p << 1 | 1, l, r)
        res = SegmentTreeNode()
        res.sum = left.sum + right.sum
        res.prefixSum = max(left.prefixSum, left.sum + right.prefixSum)
        res.suffixSum = max(right.suffixSum, right.sum + left.suffixSum)
        res.maxSum = max(max(left.maxSum, right.maxSum), left.suffixSum + right.prefixSum)
        return res

def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    for i in range(1, n + 1):
        arr[i] = int(input[ptr])
        ptr += 1
    
    build(1, 1, n)
    
    for _ in range(m):
        op = int(input[ptr])
        ptr += 1
        x = int(input[ptr])
        ptr += 1
        y = int(input[ptr])
        ptr += 1
        
        if op == 1:
            if x > y:
                x, y = y, x
            res = query(1, x, y)
            print(res.maxSum)
        else:
            update(1, x, y)

if __name__ == '__main__':
    main()
```

### 其他平台题目（更多题目请参考extended_problems.md文件）

由于篇幅限制，这里只列出了部分代表性题目。更多详细题目和解答请参考：
- [extended_problems.md](extended_problems.md) - 完整的扩展题目列表

## 📂 相关文件

- [extended_problems.md](extended_problems.md) - 完整的扩展题目列表和详细解答
- [SUMMARY.md](SUMMARY.md) - 专题总结文档
- [Code14_RangeXORQuery.java](Code14_RangeXORQuery.java) - 区间异或查询(Java)
- [Code14_RangeXORQuery.py](Code14_RangeXORQuery.py) - 区间异或查询(Python)
- [Code15_MaximumSubarraySum.java](Code15_MaximumSubarraySum.java) - 区间最大子段和(Java)
- [Code15_MaximumSubarraySum.py](Code15_MaximumSubarraySum.py) - 区间最大子段和(Python)
- [Code16_KthNumber.java](Code16_KthNumber.java) - 区间第k小元素(Java)
- [Code16_KthNumber.py](Code16_KthNumber.py) - 区间第k小元素(Python)
# Class 109 - 线段树扩展题目与实现

本目录在原有基础上扩展了线段树相关题目的完整实现，包括LeetCode、洛谷等平台的经典题目，每道题目都提供了Java、Python两种语言的实现（C++版本因编译器问题暂未包含），并附有详细的注释和复杂度分析。

## 新增内容概览

### 1. 线段树基础实现
- `SegmentTreeBasic.java` - Java版本线段树基础实现
- `segment_tree_basic.py` - Python版本线段树基础实现

### 2. LeetCode题目实现

#### LeetCode 307. 区域和检索 - 数组可修改
- `LeetCode307_SegmentTree.java` - Java实现
- `leetcode307_segment_tree.py` - Python实现
- 题目链接：https://leetcode.cn/problems/range-sum-query-mutable

#### LeetCode 315. 计算右侧小于当前元素的个数
- `LeetCode315_SegmentTree.java` - Java实现
- `leetcode315_segment_tree.py` - Python实现
- 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self

#### LeetCode 493. 翻转对
- `LeetCode493_SegmentTree.java` - Java实现
- `leetcode493_segment_tree.py` - Python实现
- 题目链接：https://leetcode.cn/problems/reverse-pairs

#### LeetCode 327. 区间和的个数
- `LeetCode327_SegmentTree.java` - Java实现
- `leetcode327_segment_tree.py` - Python实现
- 题目链接：https://leetcode.cn/problems/count-of-range-sum

### 3. 测试文件
- `test_segment_tree_problems.py` - 所有题目实现的测试文件

### 4. 总结文档
- `segment_tree_summary.md` - 线段树完全指南，包含应用场景、解题技巧等

## 原有内容

### 1. 基础题目实现
- `Code01_NumberOfReversePair1.java`, `Code01_NumberOfReversePair2.java` - 逆序对问题
- `Code02_IncreasingTriples.java` - 升序三元组数量
- `Code03_NumberOfLIS.java` - 最长递增子序列的个数
- `Code04_DifferentColors.java` - HH的项链（区间不同元素个数查询）
- `Code05_MinimumNumberOfMovesToMakePalindrome.java` - 得到回文串的最少操作次数

### 2. 扩展题目实现
- `Code06_ReversePairs.*` - 翻转对（Java/C++/Python）
- `Code07_LIS_BIT.*` - 最长递增子序列（Java/C++/Python）
- `Code08_NumberOfLISAdvanced.*` - 最长递增子序列的个数（进阶）（Java/C++/Python）
- `Code09_GoodTriplets.*` - 统计数组中好三元组数目（Java/C++/Python）

### 3. 哈希相关题目
- `Code10_LeetCode1044_LongestDuplicateSubstring.*` - 最长重复子串（Java/C++/Python）
- `Code11_LeetCode1316_DistinctEchoSubstrings.*` - 不同的循环子字符串（Java/C++/Python）
- `Code12_Codeforces271D_GoodSubstrings.*` - Good Substrings（Java/C++/Python）
- `Code13_HashSetDesign.java` - 哈希集合设计实现
- `Code14_ConsistentHashing.*` - 一致性哈希算法实现（Java/C++/Python）
- `Code15_BloomFilter.java` - 布隆过滤器实现

## 技术要点总结

### 线段树核心概念
1. **基本结构**：二叉树结构，每个节点代表一个区间
2. **核心操作**：建树、单点更新、区间查询、区间更新
3. **时间复杂度**：所有操作均为O(log n)
4. **空间复杂度**：O(n)

### 线段树应用场景
1. **区间最值查询**：RMQ问题
2. **区间和查询**：数组可修改的区间和问题
3. **区间统计问题**：计算满足条件的元素个数
4. **动态维护序列**：支持动态插入、删除、查询操作

### 权值线段树
1. **基本思想**：将元素值作为线段树的索引
2. **应用场景**：统计比某值大/小的元素个数
3. **实现要点**：离散化 + 单点更新 + 区间查询

### 懒标记技术
1. **基本思想**：延迟更新，提高区间更新效率
2. **核心操作**：下推操作、合并操作
3. **应用场景**：区间加法、区间乘法等区间更新操作

## 复杂度分析

### 时间复杂度
- **建树**：O(n)
- **单点更新**：O(log n)
- **区间查询**：O(log n)
- **区间更新**：O(log n)

### 空间复杂度
- **线段树**：O(n)
- **懒标记数组**：O(n)

## 工程化考量

### 1. 异常处理
- 空输入处理：检查数组是否为空
- 边界条件：处理数组长度为1或2的情况
- 数值溢出：使用long类型处理大数运算

### 2. 性能优化
- IO优化：使用快速IO读写
- 内存复用：复用数组空间，减少内存分配
- 常数优化：减少不必要的计算和比较

### 3. 代码可读性
- 命名规范：变量命名见名知意
- 注释完整：详细解释算法思路和关键步骤
- 模块化：将功能拆分为独立的方法

## 语言特性差异

### Java
- 静态数组：预分配固定大小数组提高性能
- IO优化：使用StreamTokenizer和BufferedReader

### Python
- 列表操作：列表推导式和内置函数
- 动态类型：灵活但需注意类型转换

## 面试技巧

### 解题思路
1. 问题分析：理解题目要求，提取关键约束
2. 算法选择：根据数据规模和时间要求选择合适算法
3. 边界处理：考虑特殊情况和边界条件
4. 复杂度分析：准确计算时间和空间复杂度

### 代码实现
1. 模板复用：准备常用算法模板
2. 调试技巧：使用打印语句跟踪变量变化
3. 测试用例：准备典型和边界测试用例

## 扩展学习

### 相关算法
1. 树状数组：更简洁的区间数据结构
2. 平衡二叉搜索树：动态维护有序序列
3. 分块：平衡时间复杂度和实现复杂度

### 应用领域
1. 机器学习：特征选择和排序算法
2. 图像处理：像素排序和滤波
3. 自然语言处理：文本排序和匹配

## 使用说明

### 编译和运行

**Java**:
```bash
javac LeetCode307_SegmentTree.java
java LeetCode307_SegmentTree
```

**Python**:
```bash
python leetcode307_segment_tree.py
```

### 测试用例
每个文件都包含完整的测试用例，包括：
- 基本功能测试
- 边界情况测试
- 性能测试
- 异常情况测试

## 贡献指南

欢迎提交新的线段树题目和优化方案！请确保：
1. 提供多种语言实现（至少Java、Python）
2. 包含详细的注释和复杂度分析
3. 添加完整的测试用例
4. 遵循统一的代码风格

## 许可证

本项目采用MIT许可证，详见LICENSE文件。
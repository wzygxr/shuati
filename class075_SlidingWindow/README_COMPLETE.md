# 滑动窗口算法完全指南

## 项目概述

本项目提供了滑动窗口算法的完整实现，包含25+个经典题目的Java、C++、Python三种语言实现。每个实现都包含详细的注释、时间复杂度分析、空间复杂度分析和完整的测试用例。

## 目录结构

```
class049/
├── SLIDING_WINDOW_GUIDE.md          # 滑动窗口算法详解指南
├── README_COMPLETE.md               # 完整项目说明（本文件）
├── Code01_MinimumSizeSubarraySum.java
├── Code02_LongestSubstringWithoutRepeatingCharacters.java
├── Code03_MinimumWindowSubstring.java
├── Code04_GasStation.java
├── Code05_ReplaceTheSubstringForBalancedString.java
├── Code06_SubarraysWithKDifferentIntegers.java
├── Code07_LongestSubstringWithAtLeastKRepeating.java
├── Code08_SlidingWindowMaximum.cpp
├── Code08_SlidingWindowMaximum.java
├── Code08_SlidingWindowMaximum.py
├── Code09_PermutationInString.cpp
├── Code09_PermutationInString.java
├── Code09_PermutationInString.py
├── Code10_FindAllAnagrams.cpp
├── Code10_FindAllAnagrams.java
├── Code10_FindAllAnagrams.py
├── Code11_MaxConsecutiveOnes.cpp
├── Code11_MaxConsecutiveOnes.java
├── Code11_MaxConsecutiveOnes.py
├── Code12_LongestSubarrayWithLimitedDifference.class
├── Code12_LongestSubarrayWithLimitedDifference.cpp
├── Code12_LongestSubarrayWithLimitedDifference.java
├── Code12_LongestSubarrayWithLimitedDifference.py
├── Code13_GetEqualSubstringsWithinBudget.class
├── Code13_GetEqualSubstringsWithinBudget.cpp
├── Code13_GetEqualSubstringsWithinBudget.java
├── Code13_GetEqualSubstringsWithinBudget.py
├── Code14_SlidingWindowMinMax.class
├── Code14_SlidingWindowMinMax.cpp
├── Code14_SlidingWindowMinMax.java
├── Code14_SlidingWindowMinMax.py
├── Code15_GrumpyBookstoreOwner.class
├── Code15_GrumpyBookstoreOwner.java
├── Code15_GrumpyBookstoreOwner.py
├── Code16_MaximumPointsYouCanObtain.class
├── Code16_MaximumPointsYouCanObtain.java
├── Code16_MaximumPointsYouCanObtain.py
├── Code17_LongestRepeatingCharacterReplacement.java
├── Code17_LongestRepeatingCharacterReplacement.cpp
├── Code17_LongestRepeatingCharacterReplacement.py
├── Code18_SlidingWindowMedian.java
├── Code18_SlidingWindowMedian.cpp
├── Code18_SlidingWindowMedian.py
├── Code19_SubarraysWithKDifferentIntegers.java
├── Code19_SubarraysWithKDifferentIntegers.cpp
├── Code19_SubarraysWithKDifferentIntegers.py
├── Code20_LongestSubarrayOf1sAfterDeletingOneElement.java
├── Code20_LongestSubarrayOf1sAfterDeletingOneElement.cpp
├── Code20_LongestSubarrayOf1sAfterDeletingOneElement.py
├── Code21_MaximumErasureValue.java
├── Code21_MaximumErasureValue.cpp
├── Code21_MaximumErasureValue.py
├── Code22_MaxConsecutiveOnesIII.java
├── Code22_MaxConsecutiveOnesIII.cpp
├── Code22_MaxConsecutiveOnesIII.py
├── Code23_GetEqualSubstringsWithinBudget.java
├── Code23_GetEqualSubstringsWithinBudget.cpp
├── Code23_GetEqualSubstringsWithinBudget.py
└── Code19_FindDuplicateSubtrees.cpp
└── Code19_FindDuplicateSubtrees.java
└── Code19_FindDuplicateSubtrees.py
```

## 题目列表

### 基础题目（1-16）
1. **209. 长度最小的子数组** - 固定窗口大小问题
2. **3. 无重复字符的最长子串** - 字符计数滑动窗口
3. **76. 最小覆盖子串** - 复杂约束滑动窗口
4. **134. 加油站** - 环形数组滑动窗口
5. **1234. 替换子串得到平衡字符串** - 字符替换滑动窗口
6. **992. K 个不同整数的子数组** - 计数滑动窗口
7. **395. 至少有 K 个重复字符的最长子串** - 分治+滑动窗口
8. **239. 滑动窗口最大值** - 单调队列优化
9. **567. 字符串的排列** - 字符匹配滑动窗口
10. **438. 找到字符串中所有字母异位词** - 多窗口匹配
11. **1004. 最大连续1的个数 III** - 0计数滑动窗口
12. **1438. 绝对差不超过限制的最长连续子数组** - 最值维护滑动窗口
13. **1208. 尽可能使字符串相等** - 开销控制滑动窗口
14. **滑动窗口最值问题** - 通用最值维护
15. **1052. 爱生气的书店老板** - 状态转换滑动窗口
16. **1423. 可获得的最大点数** - 环形数组滑动窗口

### 新增题目（17-23）
17. **424. 替换后的最长重复字符** - 字符替换计数
18. **480. 滑动窗口中位数** - 双堆维护中位数
19. **992. K 个不同整数的子数组** - 恰好K个不同计数
20. **1493. 删掉一个元素以后全为 1 的最长子数组** - 0计数优化
21. **1695. 删除子数组的最大得分** - 无重复元素滑动窗口
22. **1004. 最大连续1的个数 III** - 0翻转计数
23. **1208. 尽可能使字符串相等** - 字符转换开销控制

## 算法特点

### 时间复杂度分析
- **基本滑动窗口**：O(n) - 每个元素最多被访问两次
- **单调队列优化**：O(n) - 每个元素最多入队出队一次
- **哈希表辅助**：O(n) - 哈希表操作平均O(1)
- **双堆维护**：O(n*log k) - 堆操作的时间复杂度

### 空间复杂度分析
- **固定窗口**：O(1)
- **哈希表辅助**：O(k) - k为字符集大小
- **单调队列**：O(k) - k为窗口大小
- **双堆维护**：O(k) - 存储窗口内的元素

## 使用说明

### 编译运行

#### Java
```bash
javac CodeXX_ProblemName.java
java CodeXX_ProblemName
```

#### C++
```bash
g++ -std=c++11 CodeXX_ProblemName.cpp -o CodeXX_ProblemName
./CodeXX_ProblemName
```

#### Python
```bash
python CodeXX_ProblemName.py
```

### 测试用例

每个代码文件都包含完整的测试用例，覆盖以下场景：
- 正常输入测试
- 边界条件测试（空数组、单元素等）
- 极端值测试
- 性能测试

## 工程化考量

### 1. 异常处理
- 空输入检查
- 参数合法性验证
- 边界条件处理

### 2. 性能优化
- 避免不必要的计算
- 合理选择数据结构
- 减少内存分配

### 3. 代码可读性
- 清晰的变量命名
- 详细的注释说明
- 模块化的代码结构

### 4. 多语言支持
- Java：面向对象，丰富的集合框架
- C++：高性能，灵活的内存管理
- Python：简洁语法，快速开发

## 学习路径

### 初学者路径
1. 先学习基础滑动窗口概念
2. 从简单题目开始（如209、3题）
3. 逐步增加难度（如76、239题）
4. 掌握不同变种的应用场景

### 进阶学习
1. 理解算法的时间复杂度分析
2. 学习不同数据结构的优化
3. 掌握复杂约束的处理
4. 实践实际项目应用

## 贡献指南

欢迎贡献新的滑动窗口题目实现！请遵循以下规范：

1. **代码规范**：遵循现有代码的注释和格式规范
2. **测试用例**：为每个实现提供完整的测试用例
3. **多语言实现**：尽量提供Java、C++、Python三种语言的实现
4. **文档更新**：更新相关的文档说明

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱：algorithm-journey@example.com
- GitHub：https://github.com/algorithm-journey

## 更新日志

### v1.0.0 (2024-01-23)
- 初始版本发布
- 包含25+个滑动窗口题目实现
- 提供Java、C++、Python三种语言实现
- 完整的文档和测试用例

### v1.1.0 (2024-01-23)
- 新增7个高级滑动窗口题目
- 优化现有代码的注释和文档
- 完善工程化考量内容
- 增加多语言特性对比分析

---

**注意**：本项目持续更新中，欢迎关注和贡献！
# Class068 - 字符串动态规划专题

本专题涵盖了字符串相关的动态规划问题，包含Java、Python等多种编程语言的实现。

## 📚 目录概览

本专题包含以下核心文件，每个文件都提供了Java、C++、Python三种语言的实现：

### 1. Code01_DistinctSubsequences - 不同的子序列
**题目来源**: LeetCode 115. 不同的子序列  
**链接**: https://leetcode.cn/problems/distinct-subsequences/  
**难度**: 困难

**核心功能**: 统计字符串s的子序列中字符串t出现的个数，结果对1000000007取模。

**算法特点**:
- 基础DP：二维数组存储状态，O(n*m)时间空间复杂度
- 空间优化：滚动数组技术，O(m)空间复杂度
- 工业级：带取模运算，防止整数溢出

**工程化考量**:
- 异常处理：输入验证和边界条件检查
- 性能优化：空间压缩和常数项优化
- 测试覆盖：全面的单元测试和性能测试

### 2. Code02_EditDistance - 编辑距离
**题目来源**: LeetCode 72. 编辑距离  
**链接**: https://leetcode.cn/problems/edit-distance/  
**难度**: 中等

**核心功能**: 计算将word1转换成word2所使用的最少操作数（插入、删除、替换）。

**算法特点**:
- 经典DP：三种操作的最小代价选择
- 空间优化：一维数组+临时变量保存历史值
- 扩展功能：带权重的编辑距离计算

### 3. Code03_InterleavingString - 交错字符串
**题目来源**: LeetCode 97. 交错字符串  
**链接**: https://leetcode.cn/problems/interleaving-string/  
**难度**: 中等

**核心功能**: 判断s3是否由s1和s2交错组成。

### 4. Code04_FillCellsUseAllColorsWays - 有效涂色问题
**核心功能**: 计算n个格子使用m种颜色的有效涂色方法数。

### 5. Code05_MinimumDeleteBecomeSubstring - 删除字符变子串
**核心功能**: 计算s1至少删除多少字符可以成为s2的子串。

### 6. Code06_RegularExpressionMatching - 正则表达式匹配
**题目来源**: LeetCode 10. 正则表达式匹配  
**链接**: https://leetcode.cn/problems/regular-expression-matching/  
**难度**: 困难

**核心功能**: 实现支持 '.' 和 '*' 的正则表达式匹配。

### 7. Code07_WildcardMatching - 通配符匹配
**题目来源**: LeetCode 44. 通配符匹配  
**链接**: https://leetcode.cn/problems/wildcard-matching/  
**难度**: 困难

**核心功能**: 实现支持 '?' 和 '*' 匹配规则的通配符匹配。

### 8. Code08_DeleteOperationForTwoStrings - 两个字符串的删除操作
**题目来源**: LeetCode 583. 两个字符串的删除操作  
**链接**: https://leetcode.cn/problems/delete-operation-for-two-strings/  
**难度**: 中等

**核心功能**: 计算使两个字符串相同所需的最小删除步数。

### 9. Code09_MinimumASCIIDeleteSumForTwoStrings - 最小ASCII删除和
**题目来源**: LeetCode 712. 两个字符串的最小ASCII删除和  
**链接**: https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/  
**难度**: 中等

**核心功能**: 计算使两个字符串相等所需删除字符的ASCII值的最小和。

### 10. Code10_UncrossedLines - 不相交的线
**题目来源**: LeetCode 1035. 不相交的线  
**链接**: https://leetcode.cn/problems/uncrossed-lines/  
**难度**: 中等

**核心功能**: 计算可以绘制的最大不相交连线数。

### 11. Code11_LongestPalindromicSubsequence - 最长回文子序列
**题目来源**: LeetCode 516. 最长回文子序列  
**链接**: https://leetcode.cn/problems/longest-palindromic-subsequence/  
**难度**: 中等

**核心功能**: 计算给定字符串的最长回文子序列长度。

### 12. Code12_LongestCommonSubsequence - 最长公共子序列
**题目来源**: LeetCode 1143. 最长公共子序列  
**链接**: https://leetcode.cn/problems/longest-common-subsequence/  
**难度**: 中等

**核心功能**: 计算两个字符串的最长公共子序列长度。

**算法特点**:
- 经典LCS问题：动态规划标准解法
- 空间优化：O(min(n,m))空间复杂度
- 扩展功能：重构LCS字符串

### 13. Code13_LongestPalindromicSubstring - 最长回文子串
**题目来源**: LeetCode 5. 最长回文子串  
**链接**: https://leetcode.cn/problems/longest-palindromic-substring/  
**难度**: 中等

**核心功能**: 找出给定字符串中的最长回文子串。

### 14. Code14_EditDistance - 编辑距离
**题目来源**: LeetCode 72. 编辑距离  
**链接**: https://leetcode.cn/problems/edit-distance/  
**难度**: 中等

**核心功能**: 计算将word1转换成word2所使用的最少操作数。

**算法特点**:
- 工业级实现：完整的异常处理和边界条件
- 扩展功能：重构编辑操作序列
- 性能优化：空间复杂度优化到O(min(m,n))

### 15. Code15_LongestValidParentheses - 最长有效括号
**题目来源**: LeetCode 32. 最长有效括号  
**链接**: https://leetcode.cn/problems/longest-valid-parentheses/  
**难度**: 困难

**核心功能**: 找出只包含括号的字符串中最长的有效括号子串。

### 16. Code16_MinimumWindowSubsequence - 最小窗口子序列
**题目来源**: LeetCode 727. 最小窗口子序列  
**链接**: https://leetcode.cn/problems/minimum-window-subsequence/  
**难度**: 困难

**核心功能**: 在S中寻找最短的子串，使得T是该子串的子序列。

**算法特点**:
- 多种解法：DP预处理、双指针、滑动窗口
- 性能优化：O(n*m)时间复杂度
- 应用广泛：文本搜索、基因序列分析

### 17. Code17_LongestChunkedPalindrome - 段式回文
**题目来源**: LeetCode 1147. 段式回文  
**链接**: https://leetcode.cn/problems/longest-chunked-palindrome-decomposition/  
**难度**: 困难

**核心功能**: 将字符串分成k个不相交的子串，满足第i个子串与第k-i+1个子串相同，找出最大的k值。

**算法特点**:
- 最优解：贪心+双指针，O(n)时间复杂度
- 多种实现：递归、DP、字符串哈希
- 工程应用：文本压缩、数据分块存储

---

## 🆕 新增题目扩展

基于对各大算法平台的广泛搜索，本专题新增了以下重要题目：

### 扩展题目列表

1. **最小窗口子序列** (LeetCode 727) - 已实现
   - 应用：文本搜索、模式匹配
   - 算法：动态规划预处理 + 双指针优化

2. **段式回文** (LeetCode 1147) - 已实现  
   - 应用：数据压缩、分布式存储
   - 算法：贪心+双指针最优解

3. **掷骰子等于目标和的方法数** (LeetCode 1155)
   - 应用：概率计算、游戏设计
   - 算法：动态规划计数问题

4. **最长有效括号** (LeetCode 32) - 已存在
   - 应用：语法分析、编译器设计
   - 算法：栈法/DP法多种解法

5. **正则表达式匹配** (LeetCode 10) - 已存在
   - 应用：文本处理、搜索引擎
   - 算法：复杂的动态规划状态转移

---

## 🔍 搜索到的其他相关题目

通过搜索LeetCode、LintCode、Codeforces、AtCoder、洛谷、牛客、POJ等平台，发现以下字符串动态规划相关题目：

### LeetCode平台
- 115. 不同的子序列 ✓
- 72. 编辑距离 ✓  
- 97. 交错字符串 ✓
- 1143. 最长公共子序列 ✓
- 10. 正则表达式匹配 ✓
- 44. 通配符匹配 ✓
- 583. 两个字符串的删除操作 ✓
- 712. 两个字符串的最小ASCII删除和 ✓
- 1035. 不相交的线 ✓
- 5. 最长回文子串 ✓
- 516. 最长回文子序列 ✓
- 32. 最长有效括号 ✓
- 727. 最小窗口子序列 ✓
- 1147. 段式回文 ✓
- 1155. 掷骰子等于目标和的方法数

### 其他平台
- LintCode 79. 最长公共子序列
- Codeforces 455A. Boredom
- AtCoder dp_a. Frog 1
- 洛谷 P1435. 回文字串
- 牛客 NC127. 最长公共子串
- POJ 1458. Common Subsequence

---

## 🧠 深度技术分析

### 字符串动态规划核心模式

#### 1. 双字符串DP模板
```java
// 状态定义：dp[i][j] 表示字符串1前i个字符和字符串2前j个字符的关系
int[][] dp = new int[n+1][m+1];

// 边界初始化
for (int i = 0; i <= n; i++) dp[i][0] = ...;
for (int j = 0; j <= m; j++) dp[0][j] = ...;

// 状态转移
for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= m; j++) {
        if (s1[i-1] == s2[j-1]) {
            dp[i][j] = ...; // 字符匹配的情况
        } else {
            dp[i][j] = ...; // 字符不匹配的情况
        }
    }
}
```

#### 2. 单字符串DP模板（回文类问题）
```java
// 状态定义：dp[i][j] 表示子串s[i..j]的属性
boolean[][] dp = new boolean[n][n];

// 按长度递增处理
for (int len = 1; len <= n; len++) {
    for (int i = 0; i <= n-len; i++) {
        int j = i + len - 1;
        if (s[i] == s[j]) {
            dp[i][j] = (len <= 2) || dp[i+1][j-1];
        }
    }
}
```

### 时间复杂度优化策略

#### 1. 空间压缩技术
- **滚动数组**：当状态转移只依赖前一行时使用
- **一维数组**：通过交换循环顺序优化空间
- **临时变量**：保存必要的历史值

#### 2. 剪枝优化
- **提前终止**：当不可能得到更优解时提前返回
- **边界剪枝**：处理特殊情况减少计算量
- **记忆化搜索**：避免重复计算子问题

### 工程化最佳实践

#### 1. 异常处理框架
```java
public static int solve(String s, String t) {
    // 输入验证
    if (s == null || t == null) {
        throw new IllegalArgumentException("输入不能为null");
    }
    if (s.length() == 0 || t.length() == 0) {
        return handleEmptyCase(s, t);
    }
    // 主逻辑...
}
```

#### 2. 测试策略
- **边界测试**：空字符串、单字符、极端长度
- **性能测试**：大规模数据验证时间复杂度
- **对比测试**：多种解法验证正确性

#### 3. 调试技巧
```java
// 打印DP表用于调试
public static void printDPTable(int[][] dp) {
    for (int i = 0; i < dp.length; i++) {
        for (int j = 0; j < dp[i].length; j++) {
            System.out.printf("%3d", dp[i][j]);
        }
        System.out.println();
    }
}
```

## 🎯 面试与笔试技巧

### 笔试核心策略

#### 1. 代码模板准备
- 提前准备好常用DP模板
- 熟悉空间优化技巧
- 掌握边界条件处理

#### 2. 时间管理
- 先写暴力解法确保正确性
- 再优化到最优解
- 留时间检查边界情况

#### 3. 调试方法
```java
// 笔试中的调试打印
System.out.println("i=" + i + ", j=" + j + ", dp=" + dp[i][j]);
```

### 面试深度表达

#### 1. 问题分析
- 明确问题约束条件
- 分析时间空间复杂度要求
- 考虑极端情况和边界条件

#### 2. 解法演进
- 从暴力解法开始分析
- 逐步优化到动态规划
- 讨论空间优化可能性

#### 3. 工程化考量
- 异常处理策略
- 性能优化空间
- 实际应用场景

## 📊 性能对比分析

### 各算法时间复杂度对比

| 算法 | 时间复杂度 | 空间复杂度 | 适用场景 |
|------|------------|------------|----------|
| 不同的子序列 | O(n*m) | O(min(n,m)) | 子序列计数 |
| 编辑距离 | O(n*m) | O(min(n,m)) | 字符串相似度 |
| 最长公共子序列 | O(n*m) | O(min(n,m)) | 序列比对 |
| 正则表达式匹配 | O(n*m) | O(n*m) | 模式匹配 |
| 段式回文 | O(n) | O(1) | 文本压缩 |

### 空间优化效果

通过空间优化技术，大多数字符串DP问题的空间复杂度可以从O(n*m)优化到O(min(n,m))，在实际应用中能显著减少内存使用。

## 🔗 跨领域应用

### 自然语言处理
- **编辑距离**：拼写检查、文本相似度
- **最长公共子序列**：文档去重、版本对比
- **正则表达式匹配**：文本搜索、模式识别

### 生物信息学  
- **序列比对**：DNA/蛋白质序列分析
- **基因匹配**：生物信息数据处理

### 计算机系统
- **文件差异**：版本控制系统
- **数据压缩**：段式回文应用于压缩算法

## 🚀 进阶学习路径

### 第一阶段：基础掌握
1. 理解动态规划基本原理
2. 掌握经典字符串DP问题
3. 熟练编写基础DP代码

### 第二阶段：优化进阶  
1. 学习空间优化技巧
2. 掌握多种DP状态定义
3. 理解时间复杂度分析

### 第三阶段：工程实践
1. 实现工业级代码规范
2. 掌握测试和调试技巧
3. 了解实际应用场景

### 第四阶段：创新应用
1. 解决复杂字符串问题
2. 优化算法性能
3. 探索新的应用领域

## 📈 学习效果评估

### 掌握程度检查清单

- [ ] 能够独立实现基础字符串DP算法
- [ ] 理解各种优化技术的原理和应用
- [ ] 能够分析算法的时间空间复杂度
- [ ] 掌握工程化编码规范和测试方法
- [ ] 了解算法在实际中的应用场景

### 实战能力提升

通过本专题的学习，你将能够：
1. 快速识别字符串动态规划问题
2. 设计高效的DP状态转移方程
3. 实现空间优化的工业级代码
4. 应对技术面试中的复杂字符串问题
5. 将算法知识应用到实际工程项目中

---

**最后更新时间**：2025-10-24  
**专题状态**：✅ 已完成核心题目实现和文档完善  
**下一步计划**：继续添加更多平台题目和优化实现

---

## 🧠 算法技巧总结

### 1. 字符串动态规划通用思路
1. **状态定义**: 通常使用dp[i][j]表示第一个字符串前i个字符和第二个字符串前j个字符的关系
2. **状态转移**: 根据字符是否相等进行不同的状态转移
3. **边界处理**: 处理空字符串的情况
4. **空间优化**: 利用滚动数组优化空间复杂度

### 2. 常见题型分类
- **子序列计数**: 不同的子序列、不同的子序列II等
- **字符串编辑**: 编辑距离、删除操作等
- **字符串匹配**: 交错字符串、正则表达式匹配、通配符匹配等
- **字符串构造**: 有效涂色问题等
- **字符串比较**: 两个字符串的删除操作、最小ASCII删除和等
- **字符串连线**: 不相交的线、最小窗口子序列等
- **字符串分割**: 段式回文等
- **字符串与数学**: 掷骰子等于目标和的方法数等

### 3. 优化技巧
- **空间压缩**: 从二维数组优化到一维数组
- **边界剪枝**: 提前终止不必要的计算
- **滚动数组**: 减少空间使用

### 4. 字符串动态规划解题模板

#### 4.1 双字符串DP模板
```java
// dp[i][j] 表示字符串s1前i个字符和字符串s2前j个字符的关系
int[][] dp = new int[n + 1][m + 1];

// 边界条件处理
for (int i = 0; i <= n; i++) {
    dp[i][0] = ...; // 处理s1前i个字符与空字符串的关系
}
for (int j = 0; j <= m; j++) {
    dp[0][j] = ...; // 处理空字符串与s2前j个字符的关系
}

// 状态转移
for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= m; j++) {
        if (s1[i-1] == s2[j-1]) {
            dp[i][j] = ...; // 字符相等时的状态转移
        } else {
            dp[i][j] = ...; // 字符不相等时的状态转移
        }
    }
}
```

#### 4.2 单字符串DP模板
```java
// dp[i][j] 表示字符串s从索引i到j的子串的某种属性
int[][] dp = new int[n][n];

// 边界条件处理
for (int i = 0; i < n; i++) {
    dp[i][i] = ...; // 单个字符的情况
}

// 状态转移（按长度递增）
for (int len = 2; len <= n; len++) {
    for (int i = 0; i <= n - len; i++) {
        int j = i + len - 1;
        if (s[i] == s[j]) {
            dp[i][j] = ...; // 首尾字符相等时的状态转移
        } else {
            dp[i][j] = ...; // 首尾字符不相等时的状态转移
        }
    }
}
```

### 5. 字符串动态规划常见模式

#### 5.1 子序列问题
- **最长公共子序列(LCS)**: 两个字符串的最长公共子序列
- **最长回文子序列(LPS)**: 字符串中最长的回文子序列
- **不同子序列计数**: 统计一个字符串中有多少个给定子序列
- **最长递增子序列(LIS)**: 在字符串上下文中的应用

#### 5.2 编辑距离问题
- **标准编辑距离**: 插入、删除、替换操作的最小代价
- **特殊编辑操作**: 只允许删除、只允许插入等
- **带权重编辑距离**: 不同操作有不同的代价

#### 5.3 字符串匹配问题
- **正则表达式匹配**: 支持'.'和'*'的匹配
- **通配符匹配**: 支持'?'和'*'的匹配
- **模式匹配**: 字符串与模式的匹配
- **交错字符串匹配**: 判断字符串是否由两个字符串交错组成

#### 5.4 字符串构造问题
- **有效构造方案数**: 满足特定条件的构造方案计数
- **最小/最大构造代价**: 构造满足条件的字符串的最小/最大代价

#### 5.5 回文问题
- **最长回文子串**: 字符串中最长的连续回文子串
- **最长回文子序列**: 字符串中最长的回文子序列
- **段式回文**: 字符串分割为回文子串的最优分割

#### 5.6 括号匹配问题
- **最长有效括号**: 只包含括号的字符串中最长的有效子串
- **有效括号判断**: 判断字符串中的括号是否有效匹配

### 6. 字符串动态规划优化策略

#### 6.1 空间优化
- **滚动数组**: 当状态转移只依赖于前一行时，可以使用滚动数组优化空间
- **一维数组**: 当状态转移只依赖于当前位置左侧和上方的值时，可以优化到一维数组

#### 6.2 时间优化
- **剪枝**: 提前终止不必要的计算
- **预处理**: 预先计算一些值以减少重复计算
- **记忆化**: 对于递归解法，使用记忆化避免重复计算

#### 6.3 特殊技巧
- **字符串哈希**: 在某些情况下使用字符串哈希优化比较操作
- **KMP算法**: 在需要频繁匹配字符串时使用KMP算法预处理
- **后缀数组**: 在处理复杂字符串问题时使用后缀数组

## 🎯 工程化考量

### 1. 异常处理
- 检查输入参数合法性
- 处理边界条件
- 防止整数溢出（使用取模运算）
- 处理空指针异常
- 验证输入字符串的有效性

### 2. 性能优化
- 空间优化：使用滚动数组减少内存占用
- 时间优化：避免重复计算
- 边界优化：提前终止不必要的计算
- 缓存优化：合理利用CPU缓存局部性原理
- 并行优化：在可能的情况下使用并行计算

### 3. 代码可读性
- 添加详细注释
- 使用有意义的变量名
- 保持代码结构清晰
- 遵循编码规范
- 模块化设计

### 4. 调试技巧

#### 4.1 打印调试
- 在关键位置打印变量值
- 使用格式化输出展示状态转移过程
- 记录中间结果用于问题定位

#### 4.2 断言验证
- 在关键步骤添加断言验证中间结果
- 验证边界条件是否正确处理
- 检查状态转移是否符合预期

#### 4.3 性能分析
- 使用性能分析工具定位瓶颈
- 分析时间和空间复杂度
- 优化热点代码

### 5. 测试策略

#### 5.1 边界测试
- 空字符串测试
- 单字符字符串测试
- 极端长度字符串测试
- 特殊字符测试

#### 5.2 性能测试
- 大数据量测试
- 时间复杂度验证
- 空间复杂度验证

#### 5.3 对比测试
- 与暴力解法对比验证正确性
- 与标准库实现对比
- 跨语言实现对比

## 📈 复杂度分析

### 时间复杂度
- 大多数字符串DP问题：O(n*m)
- 其中n和m分别为两个字符串的长度

### 空间复杂度
- 基础版本：O(n*m)
- 优化版本：O(min(n,m))

## 🔗 与其他领域的联系与应用

### 1. 自然语言处理(NLP)
- **文本相似度计算**: 编辑距离用于计算文本相似度
- **拼写检查**: 编辑距离用于检测和纠正拼写错误
- **机器翻译**: 序列到序列模型中的注意力机制与字符串匹配有关
- **文本摘要**: 最长公共子序列用于提取文本中的关键信息

### 2. 生物信息学
- **DNA序列比对**: 最长公共子序列用于DNA序列比对
- **蛋白质结构预测**: 字符串匹配算法用于蛋白质结构分析
- **基因组组装**: 字符串算法用于基因组数据处理

### 3. 信息检索
- **搜索引擎**: 编辑距离用于查询纠错
- **文档相似度**: 最长公共子序列用于文档去重
- **推荐系统**: 字符串相似度用于内容推荐

### 4. 计算机视觉
- **OCR识别**: 字符串匹配用于文字识别后处理
- **图像描述生成**: 序列生成模型与字符串构造问题相关

### 5. 机器学习
- **特征工程**: 字符串特征提取
- **序列模型**: RNN、LSTM等序列模型与字符串DP有相似思想
- **强化学习**: 序列决策问题与字符串构造问题相关

## 📚 相关题目扩展

### LeetCode题目
1. **LeetCode 115. 不同的子序列** - https://leetcode.cn/problems/distinct-subsequences/
2. **LeetCode 72. 编辑距离** - https://leetcode.cn/problems/edit-distance/
3. **LeetCode 97. 交错字符串** - https://leetcode.cn/problems/interleaving-string/
4. **LeetCode 1143. 最长公共子序列** - https://leetcode.cn/problems/longest-common-subsequence/
5. **LeetCode 583. 两个字符串的删除操作** - https://leetcode.cn/problems/delete-operation-for-two-strings/
6. **LeetCode 10. 正则表达式匹配** - https://leetcode.cn/problems/regular-expression-matching/
7. **LeetCode 44. 通配符匹配** - https://leetcode.cn/problems/wildcard-matching/
8. **LeetCode 712. 两个字符串的最小ASCII删除和** - https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
9. **LeetCode 1035. 不相交的线** - https://leetcode.cn/problems/uncrossed-lines/
10. **LeetCode 727. 最小窗口子序列** - https://leetcode.cn/problems/minimum-window-subsequence/
11. **LeetCode 1147. 段式回文** - https://leetcode.cn/problems/longest-chunked-palindrome-decomposition/
12. **LeetCode 1155. 掷骰子等于目标和的方法数** - https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
13. **LeetCode 5. 最长回文子串** - https://leetcode.cn/problems/longest-palindromic-substring/
14. **LeetCode 516. 最长回文子序列** - https://leetcode.cn/problems/longest-palindromic-subsequence/
15. **LeetCode 32. 最长有效括号** - https://leetcode.cn/problems/longest-valid-parentheses/

### 其他平台题目
1. **LintCode 79. 最长公共子序列** - https://www.lintcode.com/problem/longest-common-subsequence/
2. **HackerRank - Dynamic Programming Problems** - https://www.hackerrank.com/domains/tutorials/10-days-of-dynamic-programming
3. **Codeforces - String DP Problems** - https://codeforces.com/problemset?tags=dp,strings
4. **AtCoder - Dynamic Programming Problems** - https://atcoder.jp/contests/dp
5. **洛谷 - 字符串动态规划** - https://www.luogu.com.cn/problem/list?tag=动态规划&content=字符串
6. **牛客网 - 字符串动态规划** - https://www.nowcoder.com/exam/oj?page=1&tab=算法篇&topicId=291
7. **POJ - 字符串动态规划** - http://poj.org/problemlist?volume=10

---
**最后更新时间**：2025-10-19  
**作者**：AI Assistant
# 滑动窗口算法详解

## 1. 核心思想

滑动窗口算法是一种基于双指针技术的算法模式，主要用于解决数组和字符串中的子序列问题。其核心思想是：

1. **维护一个动态窗口**：通过左右两个指针维护一个窗口区间
2. **窗口滑动**：根据问题条件动态扩展或收缩窗口
3. **优化计算**：避免重复计算，将时间复杂度从O(n²)降低到O(n)

## 2. 适用场景

滑动窗口算法适用于以下特征的问题：

### 2.1 数据结构要求
- 输入是线性数据结构（数组、字符串等）
- 需要处理连续的子序列或子串

### 2.2 问题类型
1. **固定窗口大小问题**
   - 例：大小为k的子数组的最大和
   - 特点：窗口大小固定，左右指针同步移动

2. **可变窗口大小问题**
   - **最大化窗口**：寻找满足条件的最大窗口
     - 例：至多包含k个不同字符的最长子串
   - **最小化窗口**：寻找满足条件的最小窗口
     - 例：和大于等于target的最短子数组

## 3. 算法模板

### 3.1 固定窗口大小模板
```java
public static void fixedWindow(int[] arr, int k) {
    // 初始化窗口
    for (int i = 0; i < k; i++) {
        // 处理窗口内元素
    }
    
    // 滑动窗口
    for (int i = k; i < arr.length; i++) {
        // 移除窗口左边的元素
        // 添加窗口右边的元素
        // 处理当前窗口
    }
}
```

### 3.2 可变窗口大小模板
```java
public static void variableWindow(int[] arr) {
    int left = 0;
    for (int right = 0; right < arr.length; right++) {
        // 扩展窗口右边界，处理新元素
        
        // 收缩窗口左边界，直到满足条件
        while (/* 不满足条件 */) {
            // 移除左边界元素
            left++;
        }
        
        // 更新结果
    }
}
```

## 4. 经典问题类型

### 4.1 最值问题
- 滑动窗口最大值/最小值
- 使用单调队列优化

### 4.2 计数问题
- 无重复字符的最长子串
- 至多包含K个不同字符的子串

### 4.3 匹配问题
- 字符串排列匹配
- 字母异位词查找

### 4.4 优化问题
- 长度最小的子数组
- 最大连续1的个数
- 绝对差不超过限制的最长连续子数组
- 字符串转换最大长度
- 滑动窗口最值问题

## 5. 时间与空间复杂度分析

### 5.1 时间复杂度
- **基本滑动窗口**：O(n)，每个元素最多被访问两次
- **单调队列优化**：O(n)，每个元素最多入队出队一次
- **哈希表辅助**：O(n)，哈希表操作平均O(1)

### 5.2 空间复杂度
- **固定窗口**：O(1)
- **哈希表辅助**：O(k)，k为字符集大小
- **单调队列**：O(k)，k为窗口大小

## 6. 工程化考量

### 6.1 异常处理
- 空输入检查
- 边界条件处理
- 参数合法性验证

### 6.2 性能优化
- 避免不必要的计算
- 合理选择数据结构
- 减少内存分配

### 6.3 可读性
- 变量命名清晰
- 添加详细注释
- 模块化设计

## 7. 语言特性差异

### 7.1 Java
- Deque接口及其实现类(ArrayDeque)
- 数组操作和系统类库丰富

### 7.2 C++
- STL容器(deque, vector)
- 内存管理更灵活

### 7.3 Python
- 列表和collections模块
- 语法简洁但性能相对较低

## 8. 常见题目列表

### 8.1 LeetCode题目
1. 239. 滑动窗口最大值 - https://leetcode.cn/problems/sliding-window-maximum/
2. 76. 最小覆盖子串 - https://leetcode.cn/problems/minimum-window-substring/
3. 567. 字符串的排列 - https://leetcode.cn/problems/permutation-in-string/
4. 438. 找到字符串中所有字母异位词 - https://leetcode.cn/problems/find-all-anagrams-in-a-string/
5. 3. 无重复字符的最长子串 - https://leetcode.cn/problems/longest-substring-without-repeating-characters/
6. 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
7. 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
8. 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
9. 1208. 尽可能使字符串相等 - https://leetcode.cn/problems/get-equal-substrings-within-budget/
10. 1052. 爱生气的书店老板 - https://leetcode.cn/problems/grumpy-bookstore-owner/
11. 1423. 可获得的最大点数 - https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
12. 904. 水果成篮 - https://leetcode.cn/problems/fruit-into-baskets/
13. 1456. 定长子串中元音的最大数目 - https://leetcode.cn/problems/maximum-number-of-vowels-in-a-substring-of-given-length/
14. 1493. 删掉一个元素以后全为 1 的最长子数组 - https://leetcode.cn/problems/longest-subarray-of-1s-after-deleting-one-element/
15. 1695. 删除子数组的最大得分 - https://leetcode.cn/problems/maximum-erasure-value/
16. 1499. 满足不等式的最大值 - https://leetcode.cn/problems/max-value-of-equation/
17. 1610. 可见点的最大数目 - https://leetcode.cn/problems/maximum-number-of-visible-points/
18. 424. 替换后的最长重复字符 - https://leetcode.cn/problems/longest-repeating-character-replacement/
19. 480. 滑动窗口中位数 - https://leetcode.cn/problems/sliding-window-median/
20. 992. K 个不同整数的子数组 - https://leetcode.cn/problems/subarrays-with-k-different-integers/
21. 930. 和相同的二元子数组 - https://leetcode.cn/problems/binary-subarrays-with-sum/
22. 1248. 统计「优美子数组」 - https://leetcode.cn/problems/count-number-of-nice-subarrays/
23. 1358. 包含所有三种字符的子字符串数目 - https://leetcode.cn/problems/number-of-substrings-containing-all-three-characters/
24. 1838. 最高频元素的频数 - https://leetcode.cn/problems/frequency-of-the-most-frequent-element/
25. 2024. 考试的最大困扰度 - https://leetcode.cn/problems/maximize-the-confusion-of-an-exam/

### 8.2 其他平台题目
1. POJ 2823. Sliding Window - http://poj.org/problem?id=2823
2. Luogu P1886. 滑动窗口 - https://www.luogu.com.cn/problem/P1886
3. HackerRank - Sliding Window Maximum - https://www.hackerrank.com/challenges/deque-stl/problem
4. Codeforces - Sliding Window - https://codeforces.com/problemset/problem/940/E
5. AtCoder - Sliding Window - https://atcoder.jp/contests/abc146/tasks/abc146_d
6. 牛客网 - 滑动窗口最大值 - https://www.nowcoder.com/practice/1624bc35a45c42c0bc17d17fa0cba788
7. 杭电OJ - 滑动窗口 - http://acm.hdu.edu.cn/showproblem.php?pid=4193
8. UVa OJ - Sliding Window - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4193
9. SPOJ - Sliding Window - https://www.spoj.com/problems/SLIDINGW/
10. CodeChef - Sliding Window - https://www.codechef.com/problems/SLIDINGW

## 9. 调试技巧

### 9.1 打印调试
- 打印左右指针位置
- 打印窗口状态
- 打印中间结果

### 9.2 边界测试
- 空数组/字符串
- 单元素数组
- 极端输入数据
- 窗口大小等于数组长度
- 窗口大小为1

### 9.3 调试建议
- 打印左右指针位置变化
- 打印窗口内元素状态
- 验证窗口边界条件
- 检查收缩条件是否正确
- 验证结果更新时机

## 10. 新增题目详解

### 10.1 LeetCode 424. 替换后的最长重复字符
- **题目描述**：给你一个字符串 s 和一个整数 k 。你可以选择字符串中的任一字符，并将其更改为任何其他大写英文字符。该操作最多可执行 k 次。在执行上述操作后，返回包含相同字母的最长子字符串的长度。
- **解法**：使用滑动窗口维护一个窗口，窗口内最多有k个字符可以被替换成其他字符。核心思想：窗口大小 - 窗口内出现次数最多的字符数量 <= k
- **时间复杂度**：O(n)
- **空间复杂度**：O(1)
- **代码文件**：Code17_LongestRepeatingCharacterReplacement.java/.cpp/.py

### 10.2 LeetCode 480. 滑动窗口中位数
- **题目描述**：中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
- **解法**：使用两个堆（最大堆和最小堆）来维护滑动窗口的中位数。最大堆存储窗口左半部分（较小的一半），最小堆存储窗口右半部分（较大的一半）。保持两个堆的大小平衡，最大堆的大小等于最小堆的大小或比最小堆大1。
- **时间复杂度**：O(n*log k)
- **空间复杂度**：O(k)
- **代码文件**：Code18_SlidingWindowMedian.java/.cpp/.py

### 10.3 LeetCode 992. K 个不同整数的子数组
- **题目描述**：给定一个正整数数组 nums 和一个整数 k，返回 nums 中「好子数组」的数目。如果某个子数组中不同整数的个数恰好为 k，则称其为「好子数组」。
- **解法**：使用滑动窗口的变种：恰好K个不同整数的子数组数量 = 最多K个不同整数的子数组数量 - 最多K-1个不同整数的子数组数量
- **时间复杂度**：O(n)
- **空间复杂度**：O(k)
- **代码文件**：Code19_SubarraysWithKDifferentIntegers.java/.cpp/.py

### 10.4 LeetCode 1493. 删掉一个元素以后全为 1 的最长子数组
- **题目描述**：给你一个二进制数组 nums ，你需要从中删掉一个元素。请你在删掉元素的结果数组中，返回最长的且只包含 1 的非空子数组的长度。如果不存在这样的子数组，请返回 0 。
- **解法**：使用滑动窗口维护一个最多包含1个0的窗口。当窗口内0的个数超过1时，收缩左边界。最终结果是窗口大小减1（因为要删除一个元素）
- **时间复杂度**：O(n)
- **空间复杂度**：O(1)
- **代码文件**：Code20_LongestSubarrayOf1sAfterDeletingOneElement.java/.cpp/.py

### 10.5 LeetCode 1695. 删除子数组的最大得分
- **题目描述**：给你一个正整数数组 nums ，请你从中删除一个含有 若干不同元素 的子数组。删除子数组的 得分 就是子数组各元素之 和 。返回 只删除一个 子数组可获得的 最大得分 。如果数组为空，返回 0 。
- **解法**：使用滑动窗口维护一个不含重复元素的子数组。当遇到重复元素时，收缩左边界直到没有重复元素。在滑动过程中记录最大和。
- **时间复杂度**：O(n)
- **空间复杂度**：O(k)
- **代码文件**：Code21_MaximumErasureValue.java/.cpp/.py

### 10.6 LeetCode 1004. 最大连续1的个数 III
- **题目描述**：给定一个二进制数组 nums 和一个整数 k，如果可以翻转最多 k 个 0 ，则返回 数组中连续 1 的最大个数 。
- **解法**：使用滑动窗口维护一个最多包含k个0的窗口。当窗口内0的个数超过k时，收缩左边界。在滑动过程中记录最大窗口大小。
- **时间复杂度**：O(n)
- **空间复杂度**：O(1)
- **代码文件**：Code22_MaxConsecutiveOnesIII.java/.cpp/.py

### 10.7 LeetCode 1208. 尽可能使字符串相等
- **题目描述**：给你两个长度相同的字符串，s 和 t。将 s 中的第 i 个字符变到 t 中的第 i 个字符需要 |s[i] - t[i]| 的开销（开销可能为 0），也就是两个字符的 ASCII 码值的差的绝对值。用于变更字符串的最大预算是 maxCost。在转化字符串时，总开销应当小于等于该预算，这也意味着字符串的转化可能是不完全的。如果你可以将 s 的子字符串转化为它在 t 中对应的子字符串，则返回可以转化的最大长度。如果 s 中没有子字符串可以转化成 t 中对应的子字符串，则返回 0。
- **解法**：使用滑动窗口维护一个子数组，使得子数组内字符转换的开销总和不超过maxCost。当开销超过maxCost时，收缩左边界。在滑动过程中记录最大窗口大小。
- **时间复杂度**：O(n)
- **空间复杂度**：O(1)
- **代码文件**：Code23_GetEqualSubstringsWithinBudget.java/.cpp/.py

### 10.8 POJ 2823/Luogu P1886 滑动窗口最大值和最小值
- **题目描述**：给定一个数组和窗口大小，求出每个滑动窗口内的最大值和最小值
- **解法**：使用单调队列维护窗口内的最值
- **时间复杂度**：O(n)
- **空间复杂度**：O(k)

## 11. 滑动窗口技巧总结

### 11.1 核心技巧
1. **双指针技术**：使用左右指针维护一个动态窗口
2. **窗口扩展与收缩**：根据问题条件动态调整窗口大小
3. **避免重复计算**：每个元素最多被访问两次

### 11.2 常见变种
1. **固定窗口大小**：窗口大小不变，左右指针同步移动
2. **可变窗口大小**：根据条件动态调整窗口大小
3. **转换思路问题**：将问题转换为滑动窗口模型

### 11.3 数据结构选择
1. **基本滑动窗口**：使用左右指针
2. **最值问题**：使用单调队列
3. **计数问题**：使用哈希表
4. **复杂约束**：使用TreeMap等高级数据结构

## 12. 新增题目代码实现总结

### 12.1 已实现的题目列表

| 题目编号 | 题目名称 | 难度 | 核心算法 | 时间复杂度 | 空间复杂度 |
|---------|---------|------|----------|-----------|-----------|
| 424 | 替换后的最长重复字符 | 中等 | 滑动窗口+字符计数 | O(n) | O(1) |
| 480 | 滑动窗口中位数 | 困难 | 滑动窗口+双堆 | O(n*log k) | O(k) |
| 992 | K 个不同整数的子数组 | 困难 | 滑动窗口+哈希表 | O(n) | O(k) |
| 1493 | 删掉一个元素以后全为 1 的最长子数组 | 中等 | 滑动窗口+0计数 | O(n) | O(1) |
| 1695 | 删除子数组的最大得分 | 中等 | 滑动窗口+无重复元素 | O(n) | O(k) |
| 1004 | 最大连续1的个数 III | 中等 | 滑动窗口+0计数 | O(n) | O(1) |
| 1208 | 尽可能使字符串相等 | 中等 | 滑动窗口+开销控制 | O(n) | O(1) |

### 12.2 工程化考量总结

#### 12.2.1 异常处理
- 空输入检查：所有实现都包含对空数组/字符串的检查
- 边界条件处理：处理窗口大小为0、数组长度为0等边界情况
- 参数合法性验证：验证k值、maxCost等参数的合法性

#### 12.2.2 性能优化
- **避免重复计算**：滑动窗口算法天然避免了暴力解法的重复计算
- **合理选择数据结构**：根据问题特点选择哈希表、堆、数组等数据结构
- **减少内存分配**：尽量使用原地操作，减少不必要的内存分配

#### 12.2.3 可读性优化
- **变量命名清晰**：使用left、right、maxLength等直观的变量名
- **详细注释**：每个方法都有详细的注释说明功能、参数和返回值
- **模块化设计**：将复杂逻辑拆分为多个小方法，提高可读性和可维护性

### 12.3 语言特性差异总结

#### 12.3.1 Java
- **优势**：丰富的集合框架（HashMap、HashSet、PriorityQueue）
- **特点**：强类型、面向对象、垃圾回收
- **适用场景**：需要复杂数据结构和面向对象设计的场景

#### 12.3.2 C++
- **优势**：STL容器性能优秀、内存管理灵活
- **特点**：零成本抽象、手动内存管理
- **适用场景**：对性能要求极高的场景

#### 12.3.3 Python
- **优势**：语法简洁、开发效率高
- **特点**：动态类型、解释执行
- **适用场景**：快速原型开发、算法验证

### 12.4 调试技巧总结

#### 12.4.1 打印调试
- **打印指针位置**：跟踪左右指针的移动
- **打印窗口状态**：显示窗口内的元素和统计信息
- **打印中间结果**：验证算法的中间步骤

#### 12.4.2 边界测试
- **空输入测试**：验证空数组/字符串的处理
- **单元素测试**：测试数组长度为1的情况
- **极端值测试**：测试k=0、k=数组长度等极端情况

#### 12.4.3 调试建议
- **逐步验证**：先验证简单情况，再逐步增加复杂度
- **对比不同解法**：实现多种解法进行对比验证
- **单元测试**：为每个方法编写完整的测试用例

## 13. 总结

滑动窗口算法是解决连续子序列问题的高效方法，通过双指针技术避免了暴力解法的重复计算。掌握该算法需要：

1. **理解算法核心思想**：动态维护一个窗口，根据条件扩展或收缩窗口
2. **熟练掌握模板代码**：掌握固定窗口和可变窗口两种模板
3. **针对不同问题类型灵活应用**：根据具体问题特点选择合适的变种
4. **注意工程化实现细节**：异常处理、性能优化、代码可读性

### 13.1 学习建议

1. **从简单题目开始**：先掌握基本的滑动窗口应用
2. **逐步增加难度**：从固定窗口到可变窗口，从简单约束到复杂约束
3. **多语言实现**：用不同语言实现同一算法，理解语言特性差异
4. **实际项目应用**：将滑动窗口算法应用到实际项目中

### 13.2 进阶方向

1. **复杂约束问题**：学习处理多个约束条件的滑动窗口问题
2. **数据结构优化**：探索使用更高效的数据结构优化滑动窗口
3. **分布式滑动窗口**：研究分布式环境下的滑动窗口算法
4. **实时流处理**：将滑动窗口应用于实时数据流处理

通过系统学习和实践，滑动窗口算法将成为解决数组和字符串问题的强大工具。
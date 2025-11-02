# KMP算法扩展题目集

## 竞赛平台题目分类

### LeetCode (力扣)

#### 基础题目
1. **28. 实现 strStr()**
   - 难度：简单
   - 链接：https://leetcode.cn/problems/implement-strstr/
   - 描述：KMP算法最经典的应用

2. **1392. 最长快乐前缀**
   - 难度：困难
   - 链接：https://leetcode.cn/problems/longest-happy-prefix/
   - 描述：利用next数组求最长相等前后缀

3. **1367. 二叉树中的链表**
   - 难度：中等
   - 链接：https://leetcode.cn/problems/linked-list-in-binary-tree/
   - 描述：KMP算法与树遍历结合

4. **1397. 找到所有好字符串**
   - 难度：困难
   - 链接：https://leetcode.cn/problems/find-all-good-strings/
   - 描述：数位DP与KMP算法结合

#### 进阶题目
5. **214. 最短回文串**
   - 难度：困难
   - 链接：https://leetcode.cn/problems/shortest-palindrome/
   - 描述：KMP算法在回文串中的应用

6. **459. 重复的子字符串**
   - 难度：简单
   - 链接：https://leetcode.cn/problems/repeated-substring-pattern/
   - 描述：周期判断的经典问题

### 洛谷 (Luogu)

#### 模板题目
7. **P3375 【模板】KMP字符串匹配**
   - 难度：普及/提高-
   - 链接：https://www.luogu.com.cn/problem/P3375
   - 描述：KMP算法标准模板题

8. **P4391 [BOI2009]Radio Transmission 无线传输**
   - 难度：普及/提高-
   - 链接：https://www.luogu.com.cn/problem/P4391
   - 描述：最短循环节计算

9. **P4824 [USACO15FEB]Censoring S**
   - 难度：普及/提高-
   - 链接：https://www.luogu.com.cn/problem/P4824
   - 描述：字符串删除操作

#### 进阶题目
10. **P3435 [POI2006]OKR-Periods of Words**
    - 难度：提高+/省选-
    - 链接：https://www.luogu.com.cn/problem/P3435
    - 描述：周期总和计算

11. **P2375 [NOI2014] 动物园**
    - 难度：提高+/省选-
    - 链接：https://www.luogu.com.cn/problem/P2375
    - 描述：KMP算法的变形应用

### Codeforces

#### 经典题目
12. **126B Password**
    - 难度：1500
    - 链接：https://codeforces.com/problemset/problem/126/B
    - 描述：寻找既是前缀又是后缀且在中间出现的子串

13. **432D Prefixes and Suffixes**
    - 难度：1900
    - 链接：https://codeforces.com/problemset/problem/432/D
    - 描述：统计所有既是前缀又是后缀的子串

14. **471D MUH and Cube Walls**
    - 难度：1800
    - 链接：https://codeforces.com/problemset/problem/471/D
    - 描述：KMP算法在差值匹配中的应用

#### 挑战题目
15. **535D Tavas and Malekas**
    - 难度：2000
    - 链接：https://codeforces.com/problemset/problem/535/D
    - 描述：KMP算法与组合数学结合

16. **1137B Camp Schedule**
    - 难度：1700
    - 链接：https://codeforces.com/problemset/problem/1137/B
    - 描述：利用next数组优化字符串构造

### SPOJ (Sphere Online Judge)

#### 基础训练
17. **PERIOD - Period**
    - 难度：经典
    - 链接：https://www.spoj.com/problems/PERIOD/
    - 描述：周期判断的标准题目

18. **NHAY - A Needle in the Haystack**
    - 难度：经典
    - 链接：https://www.spoj.com/problems/NHAY/
    - 描述：多测试用例的KMP匹配

19. **BEADS - Glass Beads**
    - 难度：中等
    - 链接：https://www.spoj.com/problems/BEADS/
    - 描述：最小表示法与KMP结合

#### 进阶挑战
20. **MINMOVE - Minimum Rotations**
    - 难度：困难
    - 链接：https://www.spoj.com/problems/MINMOVE/
    - 描述：循环移位的最小表示

### POJ (北京大学在线评测)

#### 经典题目
21. **2752 Seek the Name, Seek the Fame**
    - 难度：中等
    - 链接：http://poj.org/problem?id=2752
    - 描述：寻找所有既是前缀又是后缀的子串

22. **2406 Power Strings**
    - 难度：中等
    - 链接：http://poj.org/problem?id=2406
    - 描述：计算字符串的最大幂次

23. **1961 Period**
    - 难度：中等
    - 链接：http://poj.org/problem?id=1961
    - 描述：与SPOJ PERIOD类似

### HDU (杭州电子科技大学)

#### 训练题目
24. **2594 Simpsons' Hidden Talents**
    - 难度：简单
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=2594
    - 描述：两个字符串的最长公共前后缀

25. **2087 剪花布条**
    - 难度：简单
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=2087
    - 描述：不重叠的模式匹配

26. **3746 Cyclic Nacklace**
    - 难度：中等
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=3746
    - 描述：循环项链问题

### 其他平台

#### 牛客网
27. **NC109 环形字符串的最小表示**
    - 难度：中等
    - 链接：https://www.nowcoder.com/practice/...
    - 描述：最小表示法应用

#### 计蒜客
28. **T3229 字符串周期**
    - 难度：中等
    - 描述：周期判断的变种

#### AcWing
29. **157. 树形地铁系统**
    - 难度：中等
    - 链接：https://www.acwing.com/problem/content/159/
    - 描述：树的最小表示与KMP

#### 洛谷 (Luogu)

#### 经典题目
30. **P2375 [NOI2014] 动物园**
    - 难度：提高+/省选-
    - 链接：https://www.luogu.com.cn/problem/P2375
    - 描述：KMP算法的变形应用，需要计算每个前缀的不相交的相同前后缀数量

31. **P4069 [SDOI2016] 游戏**
    - 难度：省选/NOI-
    - 链接：https://www.luogu.com.cn/problem/P4069
    - 描述：KMP算法与数学函数结合

#### Codeforces

#### 挑战题目
32. **1137B. Camp Schedule**
    - 难度：1700
    - 链接：https://codeforces.com/problemset/problem/1137/B
    - 描述：利用next数组优化字符串构造

33. **535D. Tavas and Malekas**
    - 难度：2000
    - 链接：https://codeforces.com/problemset/problem/535/D
    - 描述：KMP算法与组合数学结合

#### AtCoder
34. **ABC150E - Change a Little Bit**
    - 难度：1380
    - 链接：https://atcoder.jp/contests/abc150/tasks/abc150_e
    - 描述：KMP算法在排列问题中的应用

#### LeetCode (力扣)
35. **214. 最短回文串**
    - 难度：困难
    - 链接：https://leetcode.cn/problems/shortest-palindrome/
    - 描述：KMP算法在回文串中的应用

36. **459. 重复的子字符串**
    - 难度：简单
    - 链接：https://leetcode.cn/problems/repeated-substring-pattern/
    - 描述：周期判断的经典问题

#### SPOJ (Sphere Online Judge)
37. **BEADS - Glass Beads**
    - 难度：中等
    - 链接：https://www.spoj.com/problems/BEADS/
    - 描述：最小表示法与KMP结合

38. **MINMOVE - Minimum Rotations**
    - 难度：困难
    - 链接：https://www.spoj.com/problems/MINMOVE/
    - 描述：循环移位的最小表示

#### POJ (北京大学在线评测)
39. **2406 Power Strings**
    - 难度：中等
    - 链接：http://poj.org/problem?id=2406
    - 描述：计算字符串的最大幂次

40. **1961 Period**
    - 难度：中等
    - 链接：http://poj.org/problem?id=1961
    - 描述：与SPOJ PERIOD类似

#### HDU (杭州电子科技大学)
41. **3746 Cyclic Nacklace**
    - 难度：中等
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=3746
    - 描述：循环项链问题

42. **2087 剪花布条**
    - 难度：简单
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=2087
    - 描述：不重叠的模式匹配

#### 牛客网
43. **NC109 环形字符串的最小表示**
    - 难度：中等
    - 链接：https://www.nowcoder.com/practice/...
    - 描述：最小表示法应用

#### 计蒜客
44. **T3229 字符串周期**
    - 难度：中等
    - 描述：周期判断的变种

## 题目难度分级

### 入门级 (适合初学者)
- LeetCode 28, 459
- 洛谷 P3375
- HDU 2594, 2087

### 进阶级 (需要深入理解)
- LeetCode 1392, 1367
- 洛谷 P4391, P4824
- Codeforces 126B
- SPOJ PERIOD, NHAY

### 高手级 (挑战思维)
- LeetCode 1397, 214
- 洛谷 P3435, P2375
- Codeforces 432D, 535D
- POJ 2752, 2406

### 专家级 (综合应用)
- Codeforces 471D, 1137B
- SPOJ BEADS, MINMOVE
- 复杂的组合数学问题

## 解题技巧总结

### 1. 模式识别技巧
- **看到周期判断** → 考虑 n - next[n] 公式
- **看到字符串删除** → 考虑栈+KMP组合
- **看到树/图匹配** → 考虑状态机思想
- **看到统计计数** → 考虑数位DP+KMP

### 2. 优化策略
- **空间优化**：使用滚动数组压缩next数组
- **时间优化**：预处理+记忆化搜索
- **代码优化**：模板化常用操作

### 3. 调试方法
- **打印next数组**：验证构建过程
- **单步跟踪**：观察匹配过程
- **边界测试**：测试极端情况

## 训练计划建议

### 第一阶段：基础掌握 (1-2周)
1. 理解KMP算法原理
2. 实现标准KMP匹配
3. 完成LeetCode 28, 459

### 第二阶段：应用拓展 (2-3周)
1. 掌握周期判断技巧
2. 学习栈+KMP组合
3. 完成洛谷P3375, P4391, P4824

### 第三阶段：综合应用 (3-4周)
1. 学习树结构匹配
2. 掌握数位DP结合
3. 完成LeetCode 1367, 1397

### 第四阶段：高手进阶 (4周+)
1. 研究复杂变种问题
2. 参与竞赛实战
3. 完成Codeforces, SPOJ难题

## 学习资源推荐

### 在线教程
- [KMP算法详解 - 算法竞赛入门经典]
- [字符串匹配算法专题 - OI Wiki]
- [KMP算法可视化 - USFCA]

### 书籍推荐
- 《算法导论》 - 字符串匹配章节
- 《算法竞赛入门经典》 - 字符串算法
- 《挑战程序设计竞赛》 - 字符串处理

### 视频课程
- [KMP算法原理与实现 - B站]
- [字符串匹配算法专题 - Coursera]
- [算法竞赛字符串专题 - 牛客网]

通过系统学习这些题目，您将全面掌握KMP算法及其在各种场景下的应用，为算法竞赛和工程开发打下坚实基础。
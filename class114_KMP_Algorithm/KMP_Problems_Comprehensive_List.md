# KMP算法综合题目列表

## 经典题目

### 1. LeetCode 28. 实现 strStr()
- **题目链接**: https://leetcode.cn/problems/implement-strstr/
- **难度**: 简单
- **文件**: Code09_LeetCode28_StrStr.java, Code09_LeetCode28_StrStr.cpp, Code09_LeetCode28_StrStr.py
- **描述**: 在文本串中查找模式串第一次出现的位置
- **算法**: 基础KMP算法

### 2. 洛谷 P3375 【模板】KMP字符串匹配
- **题目链接**: https://www.luogu.com.cn/problem/P3375
- **难度**: 模板题
- **文件**: LuoguP3375_KMP.java (在extended目录中)
- **描述**: 输出模式串在文本串中所有出现的位置，并输出next数组
- **算法**: 标准KMP算法

### 3. LeetCode 1392. 最长快乐前缀
- **题目链接**: https://leetcode.cn/problems/longest-happy-prefix/
- **难度**: 困难
- **文件**: Code08_LongestHappyPrefix.java, Code08_LongestHappyPrefix.cpp, Code08_LongestHappyPrefix.py
- **描述**: 找到字符串的最长快乐前缀（既是前缀又是后缀的最长子串）
- **算法**: 利用KMP的next数组

### 4. 洛谷 P4391 [BOI2009]Radio Transmission 无线传输
- **题目链接**: https://www.luogu.com.cn/problem/P4391
- **难度**: 普及/提高-
- **文件**: Code01_RepeatMinimumLength.java, Code01_RepeatMinimumLength.cpp, Code01_RepeatMinimumLength.py
- **描述**: 计算字符串的最短循环节长度
- **算法**: 利用KMP计算周期长度

### 5. 洛谷 P4824 [USACO15FEB]Censoring S
- **题目链接**: https://www.luogu.com.cn/problem/P4824
- **难度**: 普及/提高-
- **文件**: Code02_DeleteAgainAndAgain.java, Code02_DeleteAgainAndAgain.cpp, Code02_DeleteAgainAndAgain.py
- **描述**: 不断删除字符串中的模式串
- **算法**: KMP算法配合栈结构

## 进阶题目

### 6. Codeforces 126B Password
- **题目链接**: https://codeforces.com/problemset/problem/126/B
- **难度**: 1500
- **文件**: Code10_Codeforces126B_Password.java, Code10_Codeforces126B_Password.cpp, Code10_Codeforces126B_Password.py
- **描述**: 找到既是前缀又是后缀且在中间出现的子串
- **算法**: KMP的next数组应用

### 7. POJ 2752 Seek the Name, Seek the Fame
- **题目链接**: http://poj.org/problem?id=2752
- **难度**: 中等
- **文件**: Code11_POJ2752_SeekName.java, Code11_POJ2752_SeekName.cpp, Code11_POJ2752_SeekName.py
- **描述**: 找到所有既是前缀又是后缀的子串
- **算法**: 递归应用KMP的next数组

### 8. HDU 2594 Simpsons' Hidden Talents
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2594
- **难度**: 简单
- **文件**: Code12_HDU2594_SimpsonsTalents.java, Code12_HDU2594_SimpsonsTalents.cpp, Code12_HDU2594_SimpsonsTalents.py
- **描述**: 找到第一个字符串的后缀和第二个字符串的前缀的最长公共部分
- **算法**: KMP算法的变种应用

### 9. SPOJ PERIOD - Period
- **题目链接**: https://www.spoj.com/problems/PERIOD/
- **难度**: 经典
- **文件**: Code13_SPOJ_PERIOD.java, Code13_SPOJ_PERIOD.cpp, Code13_SPOJ_PERIOD.py
- **描述**: 计算字符串各前缀的周期数
- **算法**: KMP算法在周期判断中的应用

### 10. LeetCode 1367. 二叉树中的链表
- **题目链接**: https://leetcode.cn/problems/linked-list-in-binary-tree/
- **难度**: 中等
- **文件**: Code03_LinkedListInBinaryTree.java, Code03_LinkedListInBinaryTree.cpp, Code03_LinkedListInBinaryTree.py
- **描述**: 在二叉树中查找与链表匹配的路径
- **算法**: KMP状态机与树遍历结合

## 高级题目

### 11. LeetCode 1397. 找到所有好字符串
- **题目链接**: https://leetcode.cn/problems/find-all-good-strings/
- **难度**: 困难
- **文件**: Code04_FindAllGoodStrings.java, Code04_FindAllGoodStrings.cpp, Code04_FindAllGoodStrings.py
- **描述**: 数位DP结合KMP算法
- **算法**: 数位DP与KMP状态机结合

### 12. SPOJ NHAY - A Needle in the Haystack
- **题目链接**: https://www.spoj.com/problems/NHAY/
- **难度**: 经典
- **文件**: Code06_NeedleInHaystack.java, Code06_NeedleInHaystack.cpp, Code06_NeedleInHaystack.py
- **描述**: 在干草堆中找针（查找所有匹配位置）
- **算法**: 标准KMP算法

### 13. POI 2006 - Periods of Words
- **题目链接**: https://www.luogu.com.cn/problem/P3435
- **难度**: 提高+/省选-
- **文件**: Code07_PeriodsOfWords.java, Code07_PeriodsOfWords.cpp, Code07_PeriodsOfWords.py
- **描述**: 计算字符串所有周期的总和
- **算法**: KMP算法在周期计算中的应用

### 14. LeetCode 459. 重复的子字符串
- **题目链接**: https://leetcode.cn/problems/repeated-substring-pattern/
- **难度**: 简单
- **描述**: 判断字符串是否可以由其子串重复构成
- **算法**: KMP算法或简单枚举

### 15. Codeforces 432D Prefixes and Suffixes
- **题目链接**: https://codeforces.com/problemset/problem/432/D
- **难度**: 1900
- **描述**: 统计所有既是前缀又是后缀的子串
- **算法**: KMP算法的next数组应用

## 挑战题目

### 16. Codeforces 471D MUH and Cube Walls
- **题目链接**: https://codeforces.com/problemset/problem/471/D
- **难度**: 1800
- **描述**: KMP算法在差值匹配中的应用
- **算法**: 差值数组+KMP

### 17. Codeforces 535D Tavas and Malekas
- **题目链接**: https://codeforces.com/problemset/problem/535/D
- **难度**: 2000
- **描述**: KMP算法与组合数学结合
- **算法**: KMP算法与数学推理

### 18. SPOJ BEADS - Glass Beads
- **题目链接**: https://www.spoj.com/problems/BEADS/
- **难度**: 中等
- **描述**: 最小表示法与KMP结合
- **算法**: 最小表示法

### 19. SPOJ MINMOVE - Minimum Rotations
- **题目链接**: https://www.spoj.com/problems/MINMOVE/
- **难度**: 困难
- **描述**: 循环移位的最小表示
- **算法**: 最小表示法

### 20. HDU 3746 Cyclic Nacklace
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=3746
- **难度**: 中等
- **描述**: 循环项链问题
- **算法**: KMP算法在周期问题中的应用

## 算法复杂度总结

| 算法 | 时间复杂度 | 空间复杂度 | 应用场景 |
|------|------------|------------|----------|
| 基础KMP | O(n + m) | O(m) | 字符串匹配 |
| 周期计算 | O(n) | O(n) | 周期判断 |
| 最长公共前后缀 | O(n) | O(n) | 前后缀匹配 |
| 数位DP+KMP | O(n × m) | O(n × m) | 计数问题 |

其中n是文本串长度，m是模式串长度。

## 学习路径建议

### 入门阶段
1. 理解KMP算法基本原理
2. 实现标准KMP字符串匹配 (LeetCode 28)
3. 掌握next数组的构建方法

### 进阶阶段
1. 学习周期判断技巧 (SPOJ PERIOD)
2. 掌握栈与KMP的结合应用 (洛谷 P4824)
3. 理解前后缀匹配问题 (Codeforces 126B)

### 高级阶段
1. 研究树结构匹配问题 (LeetCode 1367)
2. 学习数位DP与KMP的结合 (LeetCode 1397)
3. 掌握复杂变种问题

### 专家阶段
1. 参与实际竞赛题目
2. 研究算法优化技巧
3. 扩展到AC自动机等高级算法

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
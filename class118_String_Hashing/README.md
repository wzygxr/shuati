# 字符串哈希（String Hashing）专题

## 概述
字符串哈希是一种将字符串映射为整数的技术，通过将字符串转换为哈希值，可以在O(1)时间内比较两个字符串是否相等。这是处理字符串相关问题的一种重要技巧。

## 核心思想
将字符串看作一个以某个质数为基数的多进制数，然后对一个大质数取模得到哈希值。

## 哈希函数
```
hash(s) = (s[0] * base^(n-1) + s[1] * base^(n-2) + ... + s[n-1] * base^0) mod MOD
```

其中base通常选择一个质数（如131, 499等），MOD通常选择一个大质数。

## 应用场景
1. 快速判断两个字符串是否相等
2. 查找字符串中是否有某个子串
3. 统计不同子串的数量
4. 查找最长重复子串
5. 字符串匹配问题

## 时间复杂度
- 预处理：O(n)
- 查询子串哈希值：O(1)
- 空间复杂度：O(n)

## 注意事项
1. 哈希冲突：不同的字符串可能有相同的哈希值
2. 选择合适的base和MOD可以减少冲突概率
3. 在实际应用中可能需要使用双哈希来进一步减少冲突

## 题目列表

### 1. 洛谷 P3370 【模板】字符串哈希
- **题目链接**: https://www.luogu.com.cn/problem/P3370
- **题目大意**: 给定N个字符串，请求出N个字符串中共有多少个不同的字符串
- **解法**: 使用字符串哈希技术，将每个字符串映射为一个整数，然后统计不同整数的个数
- **文件**: Code01_DifferentStrings.java, Code01_DifferentStrings.py

### 2. LeetCode 187. 重复的DNA序列
- **题目链接**: https://leetcode.cn/problems/repeated-dna-sequences/
- **题目大意**: 找到所有长度为10的重复DNA序列
- **解法**: 使用字符串哈希技术，计算所有长度为10的子串的哈希值，统计出现次数
- **文件**: Code07_GoodSubstrings.java, Code07_GoodSubstrings.py

### 3. LeetCode 1044. 最长重复子串
- **题目链接**: https://leetcode.cn/problems/longest-duplicate-substring/
- **题目大意**: 找到字符串中最长的重复子串
- **解法**: 使用二分搜索+字符串哈希，二分答案长度，用哈希验证是否存在重复子串
- **文件**: Code08_LongestDuplicateSubstring.java, Code08_LongestDuplicateSubstring.py

### 4. LeetCode 28. 找到字符串中第一个匹配项
- **题目链接**: https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
- **题目大意**: 在字符串haystack中查找needle第一次出现的位置
- **解法**: 使用字符串哈希技术，计算模式串的哈希值，在文本中查找匹配的哈希值
- **文件**: Code03_SubstringHash.java

### 5. POJ 1200 Crazy Search
- **题目链接**: http://poj.org/problem?id=1200
- **题目大意**: 给定子串长度N，字符中不同字符数量NC，以及一个字符串，求不同子串数量
- **解法**: 使用滚动哈希技术，计算所有长度为N的子串的哈希值，然后统计不同哈希值的个数
- **文件**: Code10_CrazySearch.java, Code10_CrazySearch.cpp, Code10_CrazySearch.py

### 6. CodeForces 835D Palindromic characteristics
- **题目链接**: https://codeforces.com/problemset/problem/835/D
- **题目大意**: 定义k回文串，求字符串中各个级别回文子串的数量
- **解法**: 使用字符串哈希和动态规划，预处理回文信息，然后计算各级回文数量
- **文件**: Code11_PalindromicCharacteristics.java, Code11_PalindromicCharacteristics.cpp, Code11_PalindromicCharacteristics.py

### 7. SPOJ NAJPF Pattern Find
- **题目链接**: https://www.spoj.com/problems/NAJPF/
- **题目大意**: 给定一个字符串和一个模式串，找到模式串在字符串中所有出现的位置
- **解法**: 使用字符串哈希技术，计算模式串的哈希值，然后在文本中查找匹配的哈希值
- **文件**: Code12_PatternFind.java, Code12_PatternFind.cpp, Code12_PatternFind.py

### 8. 牛客网字符串哈希题
- **题目链接**: https://www.nowcoder.com/practice/dadbd37fee7c43f0ae407db11b16b4bf
- **题目大意**: 给定N个字符串，计算其中不同字符串的个数
- **解法**: 使用字符串哈希技术，将每个字符串映射为一个整数，然后统计不同整数的个数
- **文件**: Code13_NowcoderStringHash.java, Code13_NowcoderStringHash.cpp, Code13_NowcoderStringHash.py

### 9. Rabin-Karp算法实现
- **题目来源**: 算法导论经典算法
- **题目大意**: 实现Rabin-Karp字符串匹配算法，用于高效模式匹配
- **解法**: 滚动哈希+多项式哈希
- **文件**: Code14_RabinKarpAlgorithm.java, Code14_RabinKarpAlgorithm.cpp, Code14_RabinKarpAlgorithm.py
- **时间复杂度**: 平均O(n+m)，最坏O(n*m)
- **空间复杂度**: O(1)

### 10. 字符串哈希综合应用
- **题目来源**: 多平台综合题目
- **题目大意**: 包含多个字符串哈希的实际应用场景
- **解法**: 多种高级字符串哈希技术
- **文件**: Code15_StringHashApplications.java, Code15_StringHashApplications.cpp, Code15_StringHashApplications.py
- **包含题目**:
  - LeetCode 1044 - 最长重复子串
  - LeetCode 187 - 重复的DNA序列
  - LeetCode 686 - 重复叠加字符串匹配
  - 最长公共子串问题

### 11. 高级字符串哈希应用
- **题目来源**: 高级算法题目
- **题目大意**: 包含字符串哈希的高级应用场景和优化技术
- **解法**: 高级字符串哈希技术
- **文件**: Code16_AdvancedStringHash.java, Code16_AdvancedStringHash.cpp, Code16_AdvancedStringHash.py
- **包含题目**:
  - LeetCode 214 - 最短回文串
  - LeetCode 336 - 回文对
  - LeetCode 1316 - 不同的循环子字符串
  - 字符串循环同构检测
  - 多模式字符串匹配

## 更多详细分析
请查看 [SUMMARY.md](SUMMARY.md) 文件，其中包含了详细的思路技巧、题型分析、边界场景处理、工程化考量等内容。
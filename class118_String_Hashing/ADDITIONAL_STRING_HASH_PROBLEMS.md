# 字符串哈希补充题目列表

## LeetCode题目

### 1. LeetCode 187. 重复的DNA序列
- **题目链接**: https://leetcode.cn/problems/repeated-dna-sequences/
- **题目大意**: 找到所有长度为10的重复DNA序列
- **解法**: 使用字符串哈希技术，计算所有长度为10的子串的哈希值，统计出现次数
- **相关文件**: Code07_GoodSubstrings.java, Code07_GoodSubstrings.py

### 2. LeetCode 1044. 最长重复子串
- **题目链接**: https://leetcode.cn/problems/longest-duplicate-substring/
- **题目大意**: 找到字符串中最长的重复子串
- **解法**: 使用二分搜索+字符串哈希，二分答案长度，用哈希验证是否存在重复子串
- **相关文件**: Code08_LongestDuplicateSubstring.java, Code08_LongestDuplicateSubstring.py

### 3. LeetCode 28. 找到字符串中第一个匹配项
- **题目链接**: https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
- **题目大意**: 在字符串haystack中查找needle第一次出现的位置
- **解法**: 使用字符串哈希技术，计算模式串的哈希值，在文本中查找匹配的哈希值

### 4. LeetCode 214. 最短回文串
- **题目链接**: https://leetcode.cn/problems/shortest-palindrome/
- **题目大意**: 通过在字符串前面添加字符来构造最短回文串
- **解法**: 使用字符串哈希技术，快速判断前缀是否为回文

### 5. LeetCode 336. 回文对
- **题目链接**: https://leetcode.cn/problems/palindrome-pairs/
- **题目大意**: 找到所有回文对
- **解法**: 使用字符串哈希技术，快速判断两个字符串拼接后是否为回文

### 6. LeetCode 1316. 不同的循环子字符串
- **题目链接**: https://leetcode.cn/problems/distinct-echo-substrings/
- **题目大意**: 找到所有不同的循环子字符串
- **解法**: 使用字符串哈希技术，判断子串是否由两个相同的字符串拼接而成

### 7. LeetCode 686. 重复叠加字符串匹配
- **题目链接**: https://leetcode.cn/problems/repeated-string-match/
- **题目大意**: 重复叠加字符串匹配
- **解法**: 使用字符串哈希技术，快速判断叠加后的字符串是否包含目标字符串

## Codeforces题目

### 1. Codeforces 271D - Good Substrings
- **题目链接**: https://codeforces.com/contest/271/problem/D
- **题目大意**: 找到字符串中不同好子串的数量
- **解法**: 使用字符串哈希技术，结合滑动窗口和剪枝优化
- **相关文件**: Code07_GoodSubstrings.java, Code07_GoodSubstrings.py

### 2. Codeforces 985F - Isomorphic Strings
- **题目链接**: https://codeforces.com/contest/985/problem/F
- **题目大意**: 判断两个字符串是否同构
- **解法**: 使用字符串哈希技术，通过字符映射判断字符串是否同构

### 3. Codeforces 25E - Test
- **题目链接**: https://codeforces.com/contest/25/problem/E
- **题目大意**: 字符串匹配问题
- **解法**: 使用滚动哈希法快速预处理字符串的哈希值

### 4. Codeforces 578E - Compress Words
- **题目链接**: https://codeforces.com/contest/578/problem/E
- **题目大意**: 压缩单词
- **解法**: 使用字符串哈希技术，合并相邻单词

### 5. Codeforces 4C - Registration system
- **题目链接**: https://codeforces.com/contest/4/problem/C
- **题目大意**: 注册系统用户名处理
- **解法**: 使用字符串哈希技术，快速判断用户名是否已存在

## 洛谷题目

### 1. 洛谷 P3370 【模板】字符串哈希
- **题目链接**: https://www.luogu.com.cn/problem/P3370
- **题目大意**: 给定N个字符串，请求出N个字符串中共有多少个不同的字符串
- **解法**: 使用字符串哈希技术，将每个字符串映射为一个整数，然后统计不同整数的个数
- **相关文件**: Code01_DifferentStrings.java, Code01_DifferentStrings.py

### 2. 洛谷 P4503 [CTSC2014] 企鹅QQ
- **题目链接**: https://www.luogu.com.cn/problem/P4503
- **题目大意**: 判断两个字符串是否相似
- **解法**: 使用字符串哈希技术，通过删除一个字符后的哈希值判断相似性

### 3. 洛谷 P3538 [POI2012] OKR-A Horrible Poem
- **题目链接**: https://www.luogu.com.cn/problem/P3538
- **题目大意**: 判断字符串是否由某个子串重复构成
- **解法**: 使用字符串哈希技术，结合数论知识判断周期性

### 4. 洛谷 P6456 [COCI2006-2007#5] DVAPUT
- **题目链接**: https://www.luogu.com.cn/problem/P6456
- **题目大意**: 找到最长的重复子串
- **解法**: 使用二分搜索+字符串哈希，二分答案长度，用哈希验证是否存在重复子串

### 5. 洛谷 P1200 [USACO1.1] 你的飞碟在这儿
- **题目链接**: https://www.luogu.com.cn/problem/P1200
- **题目大意**: 计算字符串的哈希值
- **解法**: 使用字符串哈希技术，计算字符串的乘积哈希值

## AtCoder题目

### 1. AtCoder ABC331 F - Palindrome Query
- **题目链接**: https://atcoder.jp/contests/abc331/tasks/abc331_f
- **题目大意**: 回文串查询
- **解法**: 使用线段树+字符串哈希，快速判断区间是否为回文

### 2. AtCoder ABC284 F - ABCBAC
- **题目链接**: https://atcoder.jp/contests/abc284/tasks/abc284_f
- **题目大意**: 字符串分割问题
- **解法**: 使用字符串哈希技术，快速判断前后缀是否相同

## POJ题目

### 1. POJ 1200 Crazy Search
- **题目链接**: http://poj.org/problem?id=1200
- **题目大意**: 给定子串长度N，字符中不同字符数量NC，以及一个字符串，求不同子串数量
- **解法**: 使用滚动哈希技术，计算所有长度为N的子串的哈希值，然后统计不同哈希值的个数
- **相关文件**: Code10_CrazySearch.java, Code10_CrazySearch.cpp, Code10_CrazySearch.py

### 2. POJ 3349 Snowflake Snow Snowflakes
- **题目链接**: http://poj.org/problem?id=3349
- **题目大意**: 判断雪花是否相同
- **解法**: 使用字符串哈希技术，通过旋转和翻转判断雪花是否相同

## SPOJ题目

### 1. SPOJ NAJPF Pattern Find
- **题目链接**: https://www.spoj.com/problems/NAJPF/
- **题目大意**: 给定一个字符串和一个模式串，找到模式串在字符串中所有出现的位置
- **解法**: 使用字符串哈希技术，计算模式串的哈希值，然后在文本中查找匹配的哈希值
- **相关文件**: Code12_PatternFind.java, Code12_PatternFind.cpp, Code12_PatternFind.py

### 2. SPOJ DICT Dictionary
- **题目链接**: https://www.spoj.com/problems/DICT/
- **题目大意**: 字典查询
- **解法**: 使用字符串哈希技术，快速查找字典中的单词

## 牛客网题目

### 1. 牛客网字符串哈希题
- **题目链接**: https://www.nowcoder.com/practice/dadbd37fee7c43f0ae407db11b16b4bf
- **题目大意**: 给定N个字符串，计算其中不同字符串的个数
- **解法**: 使用字符串哈希技术，将每个字符串映射为一个整数，然后统计不同整数的个数
- **相关文件**: Code13_NowcoderStringHash.java, Code13_NowcoderStringHash.cpp, Code13_NowcoderStringHash.py

## 其他经典题目

### 1. Rabin-Karp算法实现
- **题目来源**: 算法导论经典算法
- **题目大意**: 实现Rabin-Karp字符串匹配算法，用于高效模式匹配
- **解法**: 滚动哈希+多项式哈希
- **相关文件**: Code14_RabinKarpAlgorithm.java, Code14_RabinKarpAlgorithm.cpp, Code14_RabinKarpAlgorithm.py

### 2. 字符串哈希综合应用
- **题目来源**: 多平台综合题目
- **题目大意**: 包含多个字符串哈希的实际应用场景
- **解法**: 多种高级字符串哈希技术
- **相关文件**: Code15_StringHashApplications.java, Code15_StringHashApplications.cpp, Code15_StringHashApplications.py

### 3. 高级字符串哈希应用
- **题目来源**: 高级算法题目
- **题目大意**: 包含字符串哈希的高级应用场景和优化技术
- **解法**: 高级字符串哈希技术
- **相关文件**: Code16_AdvancedStringHash.java, Code16_AdvancedStringHash.cpp, Code16_AdvancedStringHash.py

## 三种语言实现参考

对于每道题目，都应该提供以下三种语言的实现：
- Java实现
- Python实现
- C++实现

## 哈希冲突处理建议

1. **双哈希法**：同时使用两个不同的哈希函数，只有当两个哈希值都相同时才认为字符串相同
2. **模数选择**：使用大质数作为模数，如10^9+7或10^9+9
3. **基数选择**：使用较大的质数作为基数，如911、131或13331
4. **链式地址法**：在实际哈希表实现中，当发生冲突时，可以使用链表存储多个元素
# Trie (前缀树) 算法题目汇总

## 目录
1. [LeetCode 题目](#leetcode-题目)
2. [LintCode 题目](#lintcode-题目)
3. [HackerRank 题目](#hackerrank-题目)
4. [SPOJ 题目](#spoj-题目)
5. [CodeChef 题目](#codechef-题目)
6. [AtCoder 题目](#atcoder-题目)
7. [UVa OJ 题目](#uva-oj-题目)
8. [POJ 题目](#poj-题目)
9. [HDU 题目](#hdu-题目)
10. [ZOJ 题目](#zoj-题目)

---

## LeetCode 题目

### 1. 208. Implement Trie (Prefix Tree) - 实现 Trie (前缀树)
- **题目链接**: https://leetcode.com/problems/implement-trie-prefix-tree/
- **题目描述**: 实现一个 Trie 类，包含插入、搜索和前缀检查功能
- **难度**: Medium
- **标签**: 设计、Trie

### 2. 211. Design Add and Search Words Data Structure - 添加与搜索单词 - 数据结构设计
- **题目链接**: https://leetcode.com/problems/design-add-and-search-words-data-structure/
- **题目描述**: 设计一个数据结构，支持添加单词和搜索单词（支持通配符 '.'）
- **难度**: Medium
- **标签**: 深度优先搜索、设计、Trie

### 3. 212. Word Search II - 单词搜索 II
- **题目链接**: https://leetcode.com/problems/word-search-ii/
- **题目描述**: 在二维字符网格中查找所有存在的单词
- **难度**: Hard
- **标签**: 字典树、数组、回溯、矩阵

### 4. 1707. Maximum XOR With an Element From Array - 与数组中元素的最大异或值
- **题目链接**: https://leetcode.com/problems/maximum-xor-with-an-element-from-array/
- **题目描述**: 给定一个数组和查询列表，对每个查询找出与指定元素异或的最大值
- **难度**: Hard
- **标签**: 位运算、字典树、数组

### 5. 1803. Count Pairs With XOR in a Range - 统计异或值在范围内的数对
- **题目链接**: https://leetcode.com/problems/count-pairs-with-xor-in-a-range/
- **题目描述**: 统计数组中异或值在指定范围内的数对数量
- **难度**: Hard
- **标签**: 字典树、数组

### 6. 677. Map Sum Pairs - 键值映射
- **题目链接**: https://leetcode.com/problems/map-sum-pairs/
- **题目描述**: 实现一个键值映射类，支持插入键值对和查询指定前缀的所有键值之和
- **难度**: Medium
- **标签**: 设计、字典树、哈希表、字符串

### 7. 1268. Search Suggestions System - 搜索推荐系统
- **题目链接**: https://leetcode.com/problems/search-suggestions-system/
- **题目描述**: 设计一个搜索推荐系统，根据输入的每个字母推荐产品
- **难度**: Medium
- **标签**: 字符串、字典树、数组

### 8. 421. Maximum XOR of Two Numbers in an Array - 数组中两个数的最大异或值
- **题目链接**: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
- **题目描述**: 找出数组中任意两个数的最大异或值
- **难度**: Medium
- **标签**: 位运算、字典树、数组

### 9. 648. Replace Words - 单词替换
- **题目链接**: https://leetcode.com/problems/replace-words/
- **题目描述**: 给定一个词根字典和一个句子，将句子中的继承词替换为最短的词根
- **难度**: Medium
- **标签**: 字典树、数组、哈希表、字符串

### 10. 642. Design Search Autocomplete System - 设计搜索自动补全系统
- **题目链接**: https://leetcode.com/problems/design-search-autocomplete-system/
- **题目描述**: 设计一个搜索自动补全系统，根据历史搜索记录推荐搜索结果
- **难度**: Hard
- **标签**: 设计、字典树、堆

---

## LintCode 题目

### 1. 442. Implement Trie - 实现 Trie 结构
- **题目链接**: https://www.lintcode.com/problem/442/
- **题目描述**: 实现一个 Trie 类，包含插入、搜索和前缀检查功能
- **难度**: Medium

### 2. 3729. Implement Trie II - 实现 Trie II
- **题目链接**: https://www.lintcode.com/problem/3729/
- **题目描述**: 实现一个增强版的 Trie 类，支持统计单词出现次数
- **难度**: Medium

---

## HackerRank 题目

### 1. Tries: Contacts - 联系人
- **题目链接**: https://www.hackerrank.com/challenges/ctci-contacts/problem
- **题目描述**: 实现一个联系人管理系统，支持添加联系人和查询指定前缀的联系人数量
- **难度**: Medium
- **标签**: Trie

---

## SPOJ 题目

### 1. PHONELST - Phone List - 电话列表
- **题目链接**: https://www.spoj.com/problems/PHONELST/
- **题目描述**: 判断给定的电话号码列表是否一致（没有号码是另一个号码的前缀）
- **难度**: Classical
- **标签**: Trie

### 2. DICT - Search in the dictionary! - 在字典中搜索
- **题目链接**: https://www.spoj.com/problems/DICT/
- **题目描述**: 在字典中查找具有指定前缀的所有单词
- **难度**: Easy
- **标签**: Trie

### 3. ADAINDEX - Ada and Indexing - Ada和索引
- **题目链接**: https://www.spoj.com/problems/ADAINDEX/
- **题目描述**: 统计字典中以指定字符串为前缀的单词数量
- **难度**: Medium
- **标签**: Trie

### 4. SUBXOR - SubXor - 子数组异或
- **题目链接**: https://www.spoj.com/problems/SUBXOR/
- **题目描述**: 统计数组中异或值小于指定值的子数组数量
- **难度**: Medium
- **标签**: Trie, Bit Manipulation

### 5. TRYCOMP - Try to complete - 尝试完成
- **题目链接**: https://www.spoj.com/problems/TRYCOMP/
- **题目描述**: 查找字典中具有指定前缀且出现频率最高的单词
- **难度**: Medium
- **标签**: Trie

---

## CodeChef 题目

### 1. Tries with XOR - 异或前缀树
- **题目链接**: https://www.codechef.com/tags/problems/tries-xor
- **题目描述**: 使用 Trie 解决异或相关问题
- **难度**: Medium to Hard
- **标签**: Trie, Bit Manipulation

---

## AtCoder 题目

### 1. ABC353E - Greatest Convex - 最大凸包
- **题目链接**: https://atcoder.jp/contests/abc353/tasks/abc353_e
- **题目描述**: 使用 Trie 解决字符串前缀匹配问题
- **难度**: Medium
- **标签**: Trie

### 2. ABC403E - Longest Prefix - 最长前缀
- **题目链接**: https://atcoder.jp/contests/abc403/tasks/abc403_e
- **题目描述**: 找出字符串列表中任意两个字符串的最长公共前缀
- **难度**: Hard
- **标签**: Trie

---

## UVa OJ 题目

### 1. 11362 - Phone List - 电话列表
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2347
- **题目描述**: 判断给定的电话号码列表是否一致（没有号码是另一个号码的前缀）
- **难度**: Medium
- **标签**: Trie

### 2. 10226 - Hardwood Species - 硬木种类
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1167
- **题目描述**: 统计森林中各种硬木的数量百分比
- **难度**: Medium
- **标签**: Trie

---

## POJ 题目

### 1. 1056 - IMMEDIATE DECODABILITY - 即时可解码性
- **题目链接**: http://poj.org/problem?id=1056
- **题目描述**: 判断给定的二进制编码列表是否具有即时可解码性
- **难度**: Easy
- **标签**: Trie

### 2. 2001 - Shortest Prefixes - 最短前缀
- **题目链接**: http://poj.org/problem?id=2001
- **题目描述**: 为每个单词找出最短的唯一前缀
- **难度**: Medium
- **标签**: Trie

---

## HDU 题目

### 1. 1671 - Phone List - 电话列表
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1671
- **题目描述**: 判断给定的电话号码列表是否一致（没有号码是另一个号码的前缀）
- **难度**: Medium
- **标签**: Trie

---

## ZOJ 题目

### 1. 3430 - Detect the Virus - 检测病毒
- **题目链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827369499
- **题目描述**: 使用 Trie 检测文件中的病毒代码
- **难度**: Hard
- **标签**: Trie
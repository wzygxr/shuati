# 第045节：前缀树（Trie）题目总结

## 实现代码

### Java实现
1. [Code01_CountConsistentKeys.java](Code01_CountConsistentKeys.java)
2. [Code02_TwoNumbersMaximumXor.java](Code02_TwoNumbersMaximumXor.java)
3. [Code03_WordSearchII.java](Code03_WordSearchII.java)
4. [Code04_Contacts.java](Code04_Contacts.java)
5. [Code05_Dict.java](Code05_Dict.java)
6. [Code06_PhoneList.java](Code06_PhoneList.java)
7. [Code07_ImplementTrie.java](Code07_ImplementTrie.java)

### Python实现
1. [Code01_CountConsistentKeys.py](Code01_CountConsistentKeys.py)
2. [Code02_TwoNumbersMaximumXor.py](Code02_TwoNumbersMaximumXor.py)
3. [Code03_WordSearchII.py](Code03_WordSearchII.py)
4. [Code04_Contacts.py](Code04_Contacts.py)
5. [Code05_Dict.py](Code05_Dict.py)
6. [Code06_PhoneList.py](Code06_PhoneList.py)
7. [Code07_ImplementTrie.py](Code07_ImplementTrie.py)

### C++实现
1. [Code01_CountConsistentKeys.cpp](Code01_CountConsistentKeys.cpp)
2. [Code02_TwoNumbersMaximumXor.cpp](Code02_TwoNumbersMaximumXor.cpp)
3. [Code03_WordSearchII.cpp](Code03_WordSearchII.cpp)
4. [Code04_Contacts.cpp](Code04_Contacts.cpp)
5. [Code05_Dict.cpp](Code05_Dict.cpp)
6. [Code06_PhoneList.cpp](Code06_PhoneList.cpp)
7. [Code07_ImplementTrie.cpp](Code07_ImplementTrie.cpp)

## 已实现题目列表

### 1. 牛客网接头密钥系统
- **文件**: Code01_CountConsistentKeys.java, Code01_CountConsistentKeys.py, Code01_CountConsistentKeys.cpp
- **题目描述**: 密钥由一组数字序列表示，两个密钥被认为是一致的，如果满足特定条件。
- **测试链接**: https://www.nowcoder.com/practice/c552d3b4dfda49ccb883a6371d9a6932
- **算法思路**: 使用前缀树存储差值序列，通过匹配差值序列来判断密钥是否一致。

### 2. LeetCode 421. 数组中两个数的最大异或值
- **文件**: Code02_TwoNumbersMaximumXor.java, Code02_TwoNumbersMaximumXor.py, Code02_TwoNumbersMaximumXor.cpp
- **题目描述**: 给定一个整数数组，返回数组中两个数的最大异或值。
- **测试链接**: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
- **算法思路**: 使用前缀树存储数字的二进制表示，通过贪心策略查找最大异或值。

### 3. LeetCode 212. 单词搜索 II
- **文件**: Code03_WordSearchII.java, Code03_WordSearchII.py, Code03_WordSearchII.cpp
- **题目描述**: 在二维字符网格中查找所有单词。
- **测试链接**: https://leetcode.cn/problems/word-search-ii/
- **算法思路**: 使用前缀树存储单词列表，在网格中进行深度优先搜索。

### 4. HackerRank Contacts
- **文件**: Code04_Contacts.java, Code04_Contacts.py, Code04_Contacts.cpp
- **题目描述**: 实现联系人管理系统，支持添加联系人和查找联系人。
- **测试链接**: https://www.hackerrank.com/challenges/ctci-contacts/problem
- **算法思路**: 使用前缀树存储联系人姓名，维护每个节点的计数器以快速查询匹配数量。

### 5. SPOJ DICT
- **文件**: Code05_Dict.java, Code05_Dict.py, Code05_Dict.cpp
- **题目描述**: 给定一个字典和一个前缀，找出字典中所有以该前缀开头的单词。
- **测试链接**: https://www.spoj.com/problems/DICT/
- **算法思路**: 使用前缀树存储字典单词，通过深度优先搜索查找所有匹配的单词。

### 6. SPOJ PHONELST
- **文件**: Code06_PhoneList.java, Code06_PhoneList.py, Code06_PhoneList.cpp
- **题目描述**: 给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
- **测试链接**: https://www.spoj.com/problems/PHONELST/
- **算法思路**: 使用前缀树存储电话号码，在插入过程中检测前缀关系。

### 7. LintCode 442. 实现 Trie (前缀树)
- **文件**: Code07_ImplementTrie.java, Code07_ImplementTrie.py, Code07_ImplementTrie.cpp
- **题目描述**: 实现一个Trie（前缀树），包含insert, search, 和startsWith这三个操作。
- **测试链接**: https://www.lintcode.com/problem/442/
- **算法思路**: 标准前缀树实现，包含插入、搜索和前缀匹配功能。

## 相关题目扩展

### LeetCode系列
1. LeetCode 208. 实现 Trie (前缀树)
2. LeetCode 211. 添加与搜索单词 - 数据结构设计
3. LeetCode 438. 找到字符串中所有字母异位词
4. LeetCode 567. 字符串的排列
5. LeetCode 1310. 子数组异或查询
6. LeetCode 1707. 与数组中元素的最大异或值
7. LeetCode 1803. 统计异或值在范围内的数对有多少

### HackerRank系列
1. HackerRank - Strings: Making Anagrams
2. HackerRank - Word Search
3. HackerRank - XOR Maximization

### LintCode系列
1. LintCode 1320. 包含重复值 II
2. LintCode 1490. 最大异或值

### 牛客网系列
1. 牛客网 NC105. 二分查找-II
2. 牛客网 NC138. 字符串匹配

### CodeChef系列
1. CodeChef - ANAGRAMS
2. CodeChef - MAXXOR

### SPOJ系列
1. SPOJ - ANGRAM
2. SPOJ - XORX
3. SPOJ - MORSE

### AtCoder系列
1. AtCoder - Maximum XOR
2. AtCoder - Grid 1

### 其他平台
1. USACO - 相关字符串处理问题
2. POJ - 相关数据结构问题
3. 洛谷 - 相关算法问题

## 算法复杂度分析

### 时间复杂度
- **插入操作**: O(m)，其中m是字符串的长度
- **搜索操作**: O(m)，其中m是字符串的长度
- **前缀搜索**: O(m)，其中m是前缀的长度
- **最大异或值**: O(n * log(max))，其中n是数组长度，max是数组中的最大值

### 空间复杂度
- **基本操作**: O(ALPHABET_SIZE * N * M)，其中ALPHABET_SIZE是字符集大小，N是字符串数量，M是平均字符串长度
- **最大异或值**: O(n * log(max))，用于存储所有数字的二进制表示

## 工程化考虑

### 异常处理
1. **空输入处理**: 处理空字符串或空数组的边界情况
2. **非法字符处理**: 验证输入字符是否符合预期字符集
3. **内存溢出处理**: 在大规模数据场景下监控内存使用情况

### 性能优化
1. **路径压缩**: 对于只有单个子节点的路径进行压缩
2. **内存优化**: 使用哈希表代替数组以节省稀疏字符集的空间
3. **批量操作**: 支持批量插入和查询以提高效率

### 线程安全
1. **读写锁**: 在多线程环境下使用读写锁保护共享数据
2. **不可变设计**: 提供不可变视图以支持并发读取

### 可扩展性
1. **字符集扩展**: 支持Unicode等大字符集
2. **持久化存储**: 支持将前缀树结构持久化到磁盘
3. **分布式实现**: 支持分布式环境下的前缀树实现

## 语言特性差异

### Java实现
- 使用二维数组实现前缀树结构
- 利用字符减法计算路径索引
- 通过静态方法提供功能接口

### Python实现
- 使用字典实现前缀树节点
- 代码更简洁，易于理解
- 支持动态属性添加

### C++实现
- 使用指针实现前缀树节点
- 更节省空间，性能更优
- 支持模板以提高通用性

## 应用场景

### 搜索引擎
- **自动补全**: 根据用户输入的前缀提供候选词
- **拼写检查**: 快速查找字典中是否存在某个单词
- **关键词提取**: 从文本中提取重要关键词

### 网络路由
- **IP路由**: 进行最长前缀匹配以确定数据包转发路径
- **域名解析**: 快速查找域名对应的IP地址

### 字符串处理
- **敏感词过滤**: 快速检测文本中的敏感词汇
- **单词游戏**: 在填字游戏等应用中快速查找有效单词
- **文本分析**: 分析文本中的词汇分布和频率

## 总结

前缀树是一种非常实用的数据结构，在字符串处理领域有着广泛的应用。通过本次实现，我们掌握了前缀树的基本操作和多种变体，包括：

1. **基础实现**: 插入、搜索、前缀匹配
2. **高级应用**: 异或运算优化、二维网格搜索
3. **实际问题**: 联系人管理、字典查询、电话号码验证

通过多语言实现和详尽的测试，我们验证了算法的正确性和鲁棒性，为实际应用打下了坚实的基础。
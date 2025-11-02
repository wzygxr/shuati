# Class 109 - 线段树题目扩展

本目录包含了从各大算法平台收集的线段树相关题目，每个题目都有Java、C++、Python三语言实现，包含详细的注释和复杂度分析。

## 新增题目列表

### 1. LeetCode 1044. 最长重复子串
- **题目链接**: https://leetcode.cn/problems/longest-duplicate-substring/
- **题目描述**: 给定一个字符串 s，找出其中最长的重复子串。
- **解题思路**: 使用字符串哈希和二分查找技术
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

**文件**:
- `Code10_LeetCode1044_LongestDuplicateSubstring.java`
- `Code10_LeetCode1044_LongestDuplicateSubstring.cpp`
- `Code10_LeetCode1044_LongestDuplicateSubstring.py`

### 2. LeetCode 1316. 不同的循环子字符串
- **题目链接**: https://leetcode.cn/problems/distinct-echo-substrings/
- **题目描述**: 计算字符串中不同的非空循环子字符串的数目
- **解题思路**: 使用字符串哈希和滚动哈希技术
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n²)

**文件**:
- `Code11_LeetCode1316_DistinctEchoSubstrings.java`
- `Code11_LeetCode1316_DistinctEchoSubstrings.cpp`
- `Code11_LeetCode1316_DistinctEchoSubstrings.py`

### 3. Codeforces 271D. Good Substrings
- **题目链接**: https://codeforces.com/problemset/problem/271/D
- **题目描述**: 计算字符串中不同的好子字符串的数量（坏字符不超过k个）
- **解题思路**: 使用字符串哈希和前缀和数组
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n²)

**文件**:
- `Code12_Codeforces271D_GoodSubstrings.java`
- `Code12_Codeforces271D_GoodSubstrings.cpp`
- `Code12_Codeforces271D_GoodSubstrings.py`

### 4. 哈希集合设计实现
- **题目来源**: LeetCode 705. 设计哈希集合
- **题目链接**: https://leetcode.cn/problems/design-hashset/
- **题目描述**: 不使用任何内建的哈希表库设计一个哈希集合
- **解题思路**: 使用链地址法解决哈希冲突，实现动态扩容
- **时间复杂度**: 平均O(1)，最坏O(n)
- **空间复杂度**: O(n + m)

**文件**:
- `Code13_HashSetDesign.java`

### 5. 一致性哈希算法实现
- **题目来源**: 分布式系统设计面试题
- **应用场景**: 负载均衡、分布式缓存、分布式存储系统
- **题目描述**: 实现一致性哈希算法，支持节点的动态增删和虚拟节点技术
- **解题思路**: 使用哈希环和虚拟节点技术解决数据分布不均问题
- **时间复杂度**: 添加节点O(k)，删除节点O(k)，查找节点O(log n)
- **空间复杂度**: O(n*k) n为物理节点数，k为虚拟节点数

**文件**:
- `Code14_ConsistentHashing.java`
- `Code14_ConsistentHashing.cpp`
- `Code14_ConsistentHashing.py`

### 6. 布隆过滤器实现
- **题目来源**: 大数据处理、缓存系统、网络爬虫去重
- **应用场景**: 网页去重、垃圾邮件过滤、缓存穿透防护
- **题目描述**: 实现布隆过滤器，支持元素添加和存在性检查
- **解题思路**: 使用多个哈希函数将元素映射到位数组的不同位置
- **时间复杂度**: 插入O(k)，查询O(k) k为哈希函数数量
- **空间复杂度**: O(m) m为位数组大小

**文件**:
- `Code15_BloomFilter.java`

### 7. POJ 3468 A Simple Problem with Integers
- **题目来源**: POJ (Peking University Online Judge)
- **题目链接**: http://poj.org/problem?id=3468
- **题目描述**: 区间更新和区间查询，将某区间每个数加上x，查询某区间每个数的和
- **解题思路**: 使用线段树配合懒标记(Lazy Propagation)解决区间更新问题
- **时间复杂度**: 区间更新O(log n)，区间查询O(log n)
- **空间复杂度**: O(n)

**文件**:
- `POJ3468_SegmentTree.java`
- `POJ3468_SegmentTree.cpp`
- `poj3468_segment_tree.py`

### 8. HDU 1166 敌兵布阵
- **题目来源**: HDU (Hangzhou Dianzi University Online Judge)
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1166
- **题目描述**: 单点更新和区间查询，营地增加士兵，查询某区间士兵总数
- **解题思路**: 使用线段树解决单点更新和区间查询问题
- **时间复杂度**: 单点更新O(log n)，区间查询O(log n)
- **空间复杂度**: O(n)

**文件**:
- `HDU1166_SegmentTree.java`
- `hdu1166_segment_tree.py`

### 9. 洛谷P3372 【模板】线段树1
- **题目来源**: 洛谷 (Luogu)
- **题目链接**: https://www.luogu.com.cn/problem/P3372
- **题目描述**: 区间更新和区间查询模板题
- **解题思路**: 使用线段树配合懒标记解决区间更新问题
- **时间复杂度**: 区间更新O(log n)，区间查询O(log n)
- **空间复杂度**: O(n)

**文件**:
- `LuoguP3372_SegmentTree.java`
- `luogu_p3372_segment_tree.py`

### 10. Codeforces 52C Circular RMQ
- **题目来源**: Codeforces
- **题目链接**: https://codeforces.com/contest/52/problem/C
- **题目描述**: 环形数组的区间更新和区间最小值查询
- **解题思路**: 使用线段树配合懒标记解决环形区间更新和查询问题
- **时间复杂度**: 区间更新O(log n)，区间查询O(log n)
- **空间复杂度**: O(n)

**文件**:
- `Codeforces52C_SegmentTree.java`
- `codeforces_52c_segment_tree.py`

## 技术要点总结

### 1. 字符串哈希技术
- **双哈希法**: 使用两个不同的哈希函数减少冲突概率
- **滚动哈希**: 支持O(1)时间复杂度的子字符串哈希计算
- **模数选择**: 使用大质数作为模数减少冲突

### 2. 哈希冲突解决
- **链地址法**: 每个桶使用链表存储冲突元素
- **开放地址法**: 线性探测、二次探测等
- **再哈希法**: 使用多个哈希函数

### 3. 性能优化策略
- **动态扩容**: 根据负载因子自动调整哈希表大小
- **缓存友好**: 优化内存访问模式
- **提前终止**: 在适当条件下提前结束循环

## 复杂度分析

### 时间复杂度
- **平均情况**: O(1) 对于哈希表的插入、删除、查找操作
- **最坏情况**: O(n) 当所有元素都哈希到同一个桶时
- **字符串哈希**: O(n) 预处理，O(1) 查询子字符串

### 空间复杂度
- **哈希表**: O(n + m)，其中n是元素数量，m是桶的数量
- **字符串哈希**: O(n) 存储哈希数组和幂数组

## 工程化考量

### 1. 异常处理
- 边界值检查（空字符串、极端输入）
- 内存溢出防护
- 输入验证和错误处理

### 2. 测试策略
- 单元测试覆盖各种边界情况
- 性能测试验证算法效率
- 压力测试检验大规模数据处理能力

### 3. 可维护性
- 清晰的代码结构和注释
- 模块化设计便于扩展
- 统一的编码规范

## 使用说明

### 编译和运行

**Java**:
```bash
javac Code10_LeetCode1044_LongestDuplicateSubstring.java
java Code10_LeetCode1044_LongestDuplicateSubstring
```

**C++**:
```bash
g++ -std=c++11 Code10_LeetCode1044_LongestDuplicateSubstring.cpp -o test
./test
```

**Python**:
```bash
python Code10_LeetCode1044_LongestDuplicateSubstring.py
```

### 测试用例
每个文件都包含完整的测试用例，包括：
- 基本功能测试
- 边界情况测试
- 性能测试
- 异常情况测试

## 扩展学习

### 1. 高级哈希应用
- 布隆过滤器（Bloom Filter）
- 一致性哈希（Consistent Hashing）
- 完美哈希（Perfect Hashing）

### 2. 相关算法
- Rabin-Karp字符串匹配算法
- KMP算法
- 后缀数组和后缀树

### 3. 实际应用场景
- 数据库索引
- 缓存系统
- 分布式系统
- 网络安全

## 贡献指南

欢迎提交新的哈希算法题目和优化方案！请确保：
1. 提供三语言实现（Java、C++、Python）
2. 包含详细的注释和复杂度分析
3. 添加完整的测试用例
4. 遵循统一的代码风格

## 许可证

本项目采用MIT许可证，详见LICENSE文件。
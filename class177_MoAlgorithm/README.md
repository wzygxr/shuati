# 莫队算法专题 (Mo's Algorithm)

莫队算法是一种基于分块思想的离线算法，用于解决区间查询问题。它通过巧妙地排序查询和维护区间信息，能够在较优的时间复杂度内处理大量区间查询。

## 算法特点

- **离线算法**：需要预先知道所有查询
- **适用范围**：适用于可以O(1)时间扩展或收缩区间边界的问题
- **时间复杂度**：普通莫队O(n√n)，带修莫队O(n^(5/3))，回滚莫队O(n√m)

## 算法分类

### 1. 普通莫队 (Standard Mo's Algorithm)
适用于只有查询没有修改的问题。

**核心思想**：
1. 对查询进行特殊排序
2. 通过移动左右端点维护区间信息
3. 利用分块思想优化时间复杂度

**时间复杂度**：O(n√m)

**代表题目**：
- SP3267 DQUERY - D-query (Code14)
- P1494 [国家集训队] 小 Z 的袜子
- CF617E XOR and Favorite Number (Code19)

### 2. 带修莫队 (Mo's Algorithm with Modification)
适用于带单点修改的区间查询问题。

**核心思想**：
1. 在普通莫队基础上增加时间维度
2. 对查询按左端点块号、右端点块号、时间排序
3. 维护修改操作的时间顺序

**时间复杂度**：O(n^(5/3))

**代表题目**：
- P1903 [国家集训队] 数颜色 / 维护队列 (Code16)

### 3. 回滚莫队 (Rollback Mo's Algorithm)
适用于删除操作难以维护的问题。

**核心思想**：
1. 只使用增加操作或只使用删除操作
2. 对于增加操作，通过回滚技术处理
3. 对于删除操作，通过预处理技术处理

**时间复杂度**：O(n√m)

**代表题目**：
- AT_joisc2014_c 歴史の研究 (Code15)
- P5906 【模板】回滚莫队&不删除莫队

### 4. 树上莫队 (Mo's Algorithm on Tree)
适用于树上路径查询问题。

**核心思想**：
1. 将树转化为括号序
2. 利用括号序上的莫队算法处理
3. 特别处理LCA节点

**时间复杂度**：O(n√m)

**代表题目**：
- SP10707 COT2 - Count on a tree II (Code17)

### 5. 二次离线莫队 (Secondary Offline Mo's Algorithm)
适用于转移操作复杂度较高的问题。

**核心思想**：
1. 将转移操作离线处理
2. 通过扫描线等技术批量处理
3. 结合其他数据结构优化

**时间复杂度**：O(n√m + n√n)

**代表题目**：
- P4887 【模板】莫队二次离线（第十四分块(前体)）(Code18)

## 文件命名规范

所有代码文件采用`CodeXX_XXX.java/.cpp/.py`的命名格式：
- `XX`为两位数字序号
- `XXX`为题目英文名称或核心功能描述
- 每个题目包含Java、C++、Python三种语言实现

## 题目列表

### 普通莫队
1. **Code14_DQuery** - SP3267 DQUERY - D-query
   - 题目大意：查询区间不同元素个数
   - Java: Code14_DQuery1.java
   - C++: Code14_DQuery2.cpp
   - Python: Code14_DQuery3.py

2. **Code19_XorFavorite** - CF617E XOR and Favorite Number
   - 题目大意：查询区间内异或值等于k的子区间个数
   - Java: Code19_XorFavorite1.java
   - C++: Code19_XorFavorite2.cpp
   - Python: Code19_XorFavorite3.py

3. **Code20_CF1000F_OneOccurrence** - Codeforces 1000F One Occurrence
   - 题目大意：查询区间只出现一次的元素中的最左元素
   - Java: Code20_CF1000F_OneOccurrence1.java
   - C++: Code20_CF1000F_OneOccurrence2.cpp
   - Python: Code20_CF1000F_OneOccurrence3.py

### 回滚莫队
4. **Code15_HistoryResearch** - AT_joisc2014_c 歴史の研究
   - 题目大意：查询区间内重要度最大的数字
   - Java: Code15_HistoryResearch1.java
   - C++: Code15_HistoryResearch2.cpp
   - Python: Code15_HistoryResearch3.py

5. **Code22_MaxFrequency** - 洛谷 P5906 【模板】回滚莫队&不删除莫队 / Codeforces 86D Powerful array / 洛谷 P4137 Rmq Problem / mex
   - 题目大意：查询区间内元素出现次数的平方和 / 查询区间mex值
   - Java: Code22_MaxFrequency1.java
   - C++: Code22_MaxFrequency2.cpp
   - Python: Code22_MaxFrequency3.py

### 带修莫队
6. **Code16_ColorMaintenance** - P1903 [国家集训队] 数颜色 / 维护队列
   - 题目大意：维护序列，支持查询区间不同颜色数和单点修改
   - Java: Code16_ColorMaintenance1.java
   - C++: Code16_ColorMaintenance2.cpp
   - Python: Code16_ColorMaintenance3.py

7. **Code21_TimeTravelQueries** - Codeforces 246E Blood Cousins Return / HDU 6629 string matching
   - 题目大意：支持时间旅行的区间查询问题 / 字符串匹配与修改
   - Java: Code21_TimeTravelQueries1.java
   - C++: Code21_TimeTravelQueries2.cpp
   - Python: Code21_TimeTravelQueries3.py

### 树上莫队
8. **Code17_TreeMo** - SP10707 COT2 - Count on a tree II
   - 题目大意：查询树上两点间路径不同权值个数
   - Java: Code17_TreeMo1.java
   - C++: Code17_TreeMo2.cpp
   - Python: Code17_TreeMo3.py

9. **Code23_TreePathQueries** - Codeforces 375D Tree and Queries / HDU 6604 Blow up the city
   - 题目大意：查询树上路径中出现次数大于等于k的颜色数 / 图的连通性查询
   - Java: Code23_TreePathQueries1.java
   - C++: Code23_TreePathQueries2.cpp
   - Python: Code23_TreePathQueries3.py

### 二次离线莫队
10. **Code18_SecondaryOffline** - P4887 【模板】莫队二次离线
    - 题目大意：查询区间内满足异或条件的二元组个数
    - Java: Code18_SecondaryOffline1.java
    - C++: Code18_SecondaryOffline2.cpp
    - Python: Code18_SecondaryOffline3.py

## 算法技巧总结

### 1. 排序策略
- 普通莫队：按左端点块号为第一关键字，右端点为第二关键字
- 左端点块号相同时，奇数块按右端点升序，偶数块按右端点降序

### 2. 指针移动顺序
```
// 扩展右端点
while (winr < jobr) add(arr[++winr]);

// 扩展左端点  
while (winl > jobl) add(arr[--winl]);

// 收缩左端点
while (winl < jobl) del(arr[winl++]);

// 收缩右端点
while (winr > jobr) del(arr[winr--]);
```

### 3. 块大小选择
- 普通莫队：√n
- 带修莫队：n^(2/3)
- 根据具体题目调整以获得最优性能

## 应用场景

莫队算法适用于以下场景：
1. 区间查询问题
2. 可以O(1)时间扩展或收缩区间边界
3. 允许离线处理
4. 数据规模适中（通常n,m ≤ 10^5）

## 与其他算法的比较

| 算法 | 时间复杂度 | 空间复杂度 | 优点 | 缺点 |
|------|------------|------------|------|------|
| 线段树 | O(nlogn) | O(n) | 在线、功能强大 | 实现复杂 |
| 树状数组 | O(nlogn) | O(n) | 实现简单、常数小 | 功能有限 |
| 莫队算法 | O(n√n) | O(n) | 实现简单、适用范围广 | 离线、常数较大 |

## 学习建议

1. **掌握基础**：先熟练掌握普通莫队算法
2. **理解本质**：理解分块思想和指针移动的原理
3. **练习变种**：逐步学习带修莫队、回滚莫队等变种
4. **实战应用**：通过大量题目加深理解
5. **优化技巧**：学习各种优化技巧，如奇偶优化等

## 常见优化技巧

1. **奇偶优化**：同一块内奇数行升序，偶数行降序
2. **块大小调整**：根据题目特点调整块大小
3. **IO优化**：使用快速输入输出
4. **常数优化**：减少不必要的计算和内存访问

## 注意事项

1. **指针顺序**：严格按照扩展-收缩的顺序移动指针
2. **边界处理**：注意数组边界和初始化
3. **离散化**：对于权值范围大的题目考虑离散化
4. **数据结构**：根据题目特点选择合适的计数数据结构
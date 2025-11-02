# 算法与数据结构题目分类整理

## 1. 回文自动机 (Palindromic Automaton / Eertree)

### 相关题目
1. **Codeforces 835D - Palindromic characteristics**
   - 链接: https://codeforces.com/problemset/problem/835/D
   - 类型: 回文子串计数
   - 描述: 给定一个字符串，计算不同回文等级的子串数量

2. **SPOJ LPS - Longest Palindromic Substring**
   - 链接: https://www.spoj.com/problems/LPS/
   - 类型: 最长回文子串
   - 描述: 找到字符串中的最长回文子串

3. **Codeforces Gym 100952/C - Transform to Palindrome**
   - 链接: https://codeforces.com/gym/100952/problem/C
   - 类型: 字符串变换
   - 描述: 通过最少的操作将字符串转换为回文串

4. **SPOJ JUSTAPAL - Just a Palindrome**
   - 链接: https://www.spoj.com/problems/JUSTAPAL/
   - 类型: 回文串操作
   - 描述: 对回文串进行各种操作的综合题

5. **Codeforces 17E - Palisection**
   - 链接: https://codeforces.com/problemset/problem/17/E
   - 类型: 回文串交集
   - 描述: 计算相交的回文子串对数

## 2. Manacher算法

### 相关题目
1. **LeetCode 5 - Longest Palindromic Substring**
   - 链接: https://leetcode.com/problems/longest-palindromic-substring/
   - 类型: 最长回文子串
   - 描述: 找到字符串中的最长回文子串

2. **Codeforces 137D - Palindromes**
   - 链接: https://codeforces.com/problemset/problem/137/D
   - 类型: 回文分割
   - 描述: 将字符串分割成最少的回文子串

3. **SPOJ NUMOFPAL - Number of Palindromes**
   - 链接: https://www.spoj.com/problems/NUMOFPAL/
   - 类型: 回文子串计数
   - 描述: 计算字符串中所有回文子串的数量

4. **Codeforces 1080E - Sonya and Matrix Beauty**
   - 链接: https://codeforces.com/problemset/problem/1080/E
   - 类型: 二维回文子矩阵
   - 描述: 在矩阵中查找美丽的子矩阵（行和列都是回文）

5. **Codeforces 81E - Pairs**
   - 链接: https://codeforces.com/problemset/problem/81/E
   - 类型: 回文配对
   - 描述: 在字符串中查找满足条件的回文配对

## 3. 高级并查集 (Advanced Union-Find)

### 相关题目
1. **Codeforces 722C - Destroying Array**
   - 链接: https://codeforces.com/problemset/problem/722/C
   - 类型: 离线处理 + 回滚并查集
   - 描述: 通过删除元素最大化剩余元素的和

2. **Codeforces 813F - Bipartite Checking**
   - 链接: https://codeforces.com/problemset/problem/813/F
   - 类型: 二分图判定
   - 描述: 动态添加边并检查图是否为二分图

3. **Codeforces 1156E - Special Segments of Permutation**
   - 链接: https://codeforces.com/problemset/problem/1156/E
   - 类型: 时间轴并查集
   - 描述: 在排列中查找满足特定条件的特殊段

4. **Codeforces 1215F - Radio Stations**
   - 链接: https://codeforces.com/problemset/problem/1215/F
   - 类型: 2-SAT + 并查集
   - 描述: 电台频率分配问题

5. **Codeforces 1721F - Matching Reduction**
   - 链接: https://codeforces.com/problemset/problem/1721/F
   - 类型: 二分图匹配 + 并查集
   - 描述: 二分图最大匹配的动态维护

6. **Codeforces 277E - Binary Tree on Plane**
   - 链接: https://codeforces.com/problemset/problem/277/E
   - 类型: 几何 + 并查集
   - 描述: 平面上点的二叉树构建问题

## 4. K-D Tree 和 Octree

### 相关题目
1. **SPOJ NNS - Nearest Neighbor Search**
   - 链接: https://www.spoj.com/problems/NNS/
   - 类型: 最近邻搜索
   - 描述: 在二维平面上查找点的最近邻

2. **Codeforces 1045G - AI robots**
   - 链接: https://codeforces.com/problemset/problem/1045/G
   - 类型: 范围查询
   - 描述: 机器人之间的通信问题

3. **SPOJ - Closest Point Pair**
   - 链接: https://www.spoj.com/problems/CLOPPAIR/
   - 类型: 最近点对
   - 描述: 找到平面上最近的点对

4. **Codeforces 958F3 - Lightsabers (hard)**
   - 链接: https://codeforces.com/problemset/problem/958/F3
   - 类型: 多维范围查询
   - 描述: 光剑分配问题

5. **SPOJ GANNHAT - Closest distance**
   - 链接: https://www.spoj.com/problems/GANNHAT/
   - 类型: 最近距离查询
   - 描述: 在平面上查找点对之间的最近距离

6. **Codeforces 762F - Tree nesting**
   - 链接: https://codeforces.com/problemset/problem/762/F
   - 类型: 树嵌套 + K-D Tree
   - 描述: 树的嵌套问题

## 5. 字符串匹配算法

### Rabin-Karp 算法
1. **LeetCode 187 - Repeated DNA Sequences**
   - 链接: https://leetcode.com/problems/repeated-dna-sequences/
   - 类型: 滚动哈希
   - 描述: 找到DNA序列中重复的10字符子串

2. **Codeforces 963D - Frequency of String**
   - 链接: https://codeforces.com/problemset/problem/963/D
   - 类型: 多模式串匹配
   - 描述: 查找包含指定模式串且出现频率最高的子串

3. **SPOJ NAJPF - Pattern Find**
   - 链接: https://www.spoj.com/problems/NAJPF/
   - 类型: 模式匹配
   - 描述: 在文本中查找所有模式串出现的位置

4. **Codeforces 126B - Password**
   - 链接: https://codeforces.com/problemset/problem/126/B
   - 类型: Border查找
   - 描述: 找到既是前缀又是后缀的最长子串

5. **SPOJ EPALIN - Extend to Palindrome**
   - 链接: https://www.spoj.com/problems/EPALIN/
   - 类型: 回文扩展
   - 描述: 通过在字符串末尾添加最少字符使其成为回文串

### Boyer-Moore 算法
1. **LeetCode 28 - Implement strStr()**
   - 链接: https://leetcode.com/problems/implement-strstr/
   - 类型: 子串查找
   - 描述: 实现字符串查找函数

2. **Codeforces 126B - Password**
   - 链接: https://codeforces.com/problemset/problem/126/B
   - 类型: Border查找
   - 描述: 找到既是前缀又是后缀的最长子串

3. **SPOJ NAJPF - Pattern Find**
   - 链接: https://www.spoj.com/problems/NAJPF/
   - 类型: 模式匹配
   - 描述: 在文本中查找所有模式串出现的位置

4. **Codeforces 914F - Substrings in a String**
   - 链接: https://codeforces.com/problemset/problem/914/F
   - 类型: 子串查询
   - 描述: 动态字符串的子串查询问题

### Sunday 算法
1. **SPOJ PLD - Palindromes**
   - 链接: https://www.spoj.com/problems/PLD/
   - 类型: 回文子串查找
   - 描述: 找到指定长度的所有回文子串

2. **Codeforces 127D - Password**
   - 链接: https://codeforces.com/problemset/problem/127/D
   - 类型: 字符串匹配
   - 描述: 密码破解问题

3. **Codeforces 808G - Anthem of Berland**
   - 链接: https://codeforces.com/problemset/problem/808/G
   - 类型: 字符串匹配 + DP
   - 描述: 在字符串中插入字符以最大化模式串出现次数

## 6. 其他相关数据结构

### Link-Cut Tree
1. **Codeforces 117E - Tree or not Tree**
   - 链接: https://codeforces.com/problemset/problem/117/E
   - 类型: 动态树
   - 描述: 树上路径查询和修改

2. **Codeforces 886F - Symmetric Projections**
   - 链接: https://codeforces.com/problemset/problem/886/F
   - 类型: 几何 + 动态树
   - 描述: 对称投影问题

3. **Codeforces 1416D - Graph and Queries**
   - 链接: https://codeforces.com/problemset/problem/1416/D
   - 类型: 图论 + 动态树
   - 描述: 图的动态查询问题

4. **Codeforces 614A - Link/Cut Tree**
   - 链接: https://codeforces.com/problemset/problem/614/A
   - 类型: 数学 + 动态树
   - 描述: 链式切割树问题

### Suffix Array & Suffix Automaton
1. **Codeforces 235C - Cyclical Quest**
   - 链接: https://codeforces.com/problemset/problem/235/C
   - 类型: 循环同构匹配
   - 描述: 在文本中查找循环同构的模式串

2. **Codeforces 452E - Three strings**
   - 链接: https://codeforces.com/problemset/problem/452/E
   - 类型: 多串公共子串
   - 描述: 计算三个字符串公共子串的数量

3. **SPOJ LCS - Longest Common Substring**
   - 链接: https://www.spoj.com/problems/LCS/
   - 类型: 最长公共子串
   - 描述: 找到两个字符串的最长公共子串

4. **Codeforces 271D - Good Substrings**
   - 链接: https://codeforces.com/problemset/problem/271/D
   - 类型: 后缀数组 + 计数
   - 描述: 计算满足条件的好子串数量

5. **SPOJ SARRAY - Suffix Array**
   - 链接: https://www.spoj.com/problems/SARRAY/
   - 类型: 后缀数组构建
   - 描述: 构建字符串的后缀数组

6. **SPOJ SUBST1 - New Distinct Substrings**
   - 链接: https://www.spoj.com/problems/SUBST1/
   - 类型: 不同子串计数
   - 描述: 使用后缀数组计算字符串中不同子串的数量

7. **洛谷 P3804 【模板】后缀自动机**
   - 链接: https://www.luogu.com.cn/problem/P3804
   - 类型: 后缀自动机应用
   - 描述: 使用后缀自动机计算出现次数不为1的子串的出现次数乘长度的最大值

### FHQ Treap
1. **Codeforces 356A - Knight Tournament**
   - 链接: https://codeforces.com/problemset/problem/356/A
   - 类型: FHQ Treap
   - 描述: 骑士锦标赛问题

2. **Codeforces 863D - Yet Another Array Queries Problem**
   - 链接: https://codeforces.com/problemset/problem/863/D
   - 类型: FHQ Treap + 离线处理
   - 描述: 数组查询问题

3. **Codeforces 785E - Anton and Permutation**
   - 链接: https://codeforces.com/problemset/problem/785/E
   - 类型: FHQ Treap + 逆序对
   - 描述: 计算排列的逆序对数量

4. **Codeforces 960F - Pathwalks**
   - 链接: https://codeforces.com/problemset/problem/960/F
   - 类型: FHQ Treap + 最长路径
   - 描述: 在有向图中查找最长路径

## 总结

以上整理了各种高级数据结构和算法相关的经典题目，涵盖了：
- 回文相关算法（回文自动机、Manacher算法）
- 高级并查集（回滚、时间轴、二分图）
- 空间分割数据结构（K-D Tree、Octree）
- 字符串匹配算法（Rabin-Karp、Boyer-Moore、Sunday）
- 其他高级数据结构（Link-Cut Tree、后缀数组/自动机、FHQ Treap）

这些题目来自各大在线评测平台，包括Codeforces、LeetCode、SPOJ等，适合用于深入学习和掌握这些高级算法和数据结构。
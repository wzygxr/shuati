# 整体二分算法补充题目汇总

## 题目分类与详细信息

### 1. 静态区间第K小问题

#### 1.1 POJ 2104 K-th Number
- **题目链接**: http://poj.org/problem?id=2104
- **题目描述**: 给定一个长度为N的数组，有M个查询，每个查询要求在指定区间内找到第K小的数
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 2000
- **解题思路**: 使用整体二分处理静态区间第K小问题，将所有查询一起处理，二分答案的值域，利用树状数组维护区间内小于等于mid的元素个数

#### 1.2 HDU 2665 Kth Number
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2665
- **题目描述**: 与POJ 2104相同题目，静态区间第K小查询
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 2000

#### 1.3 BZOJ 2738 矩阵乘法
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=2738
- **题目描述**: 给定一个N*N的矩阵，多次求某个子矩阵中的第K小
- **算法类型**: 整体二分 + 二维树状数组
- **难度等级**: 省选/NOI-

### 2. 带修改的区间第K小问题

#### 2.1 HDU 5412 CRB and Queries
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=5412
- **题目描述**: 带修改区间第K小查询
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 2500

#### 2.2 洛谷 P2617 Dynamic Rankings
- **题目链接**: https://www.luogu.com.cn/problem/P2617
- **题目描述**: 带修改区间第K小值
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 省选/NOI-

#### 2.3 ZOJ 2112 Dynamic Rankings
- **题目链接**: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=2112
- **题目描述**: 求动态区间第K大
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 2500

### 3. 带修改的区间第K大问题

#### 3.1 洛谷 P3332 [ZJOI2013]K大数查询
- **题目链接**: https://www.luogu.com.cn/problem/P3332
- **题目描述**: 给定一个长度为N的数组，支持区间加元素和查询区间第K大
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 提高+/省选-

### 4. 混合果汁问题

#### 4.1 洛谷 P4602 [CTSC2018]混合果汁
- **题目链接**: https://www.luogu.com.cn/problem/P4602
- **题目描述**: 多种果汁按不同比例混合，满足小朋友的预算和容量要求
- **算法类型**: 整体二分 + 线段树
- **难度等级**: 省选/NOI-

### 5. 矩阵相关问题

#### 5.1 洛谷 P1527 [国家集训队]矩阵乘法
- **题目链接**: https://www.luogu.com.cn/problem/P1527
- **题目描述**: 查询子矩阵中第K小的元素
- **算法类型**: 整体二分 + 二维树状数组
- **难度等级**: 省选/NOI-

### 6. 树上问题

#### 6.1 洛谷 P3242 [HNOI2015]接水果
- **题目链接**: https://www.luogu.com.cn/problem/P3242
- **题目描述**: 树上路径包含关系的查询问题
- **算法类型**: 整体二分 + 扫描线 + 树状数组
- **难度等级**: 省选/NOI-

#### 6.2 洛谷 P3250 [HNOI2016]网络
- **题目链接**: https://www.luogu.com.cn/problem/P3250
- **题目描述**: 树上路径修改和查询问题
- **算法类型**: 整体二分 + 树上差分 + 树状数组
- **难度等级**: 省选/NOI-

### 7. 国家收集陨石问题

#### 7.1 洛谷 P3527 [POI2011]MET-Meteors
- **题目链接**: https://www.luogu.com.cn/problem/P3527
- **题目描述**: 区间加法和查询满足条件的时间点
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 省选/NOI-

#### 7.2 SPOJ METEORS
- **题目链接**: https://www.spoj.com/problems/METEORS/
- **题目描述**: 国家收集陨石问题
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 2500

### 8. 动态图问题

#### 8.1 洛谷 P5163 WD与地图
- **题目链接**: https://www.luogu.com.cn/problem/P5163
- **题目描述**: 动态图连通性问题
- **算法类型**: 整体二分 + 可撤销并查集
- **难度等级**: NOI/NOI+/CTSC

### 9. 区间最大异或和问题

#### 9.1 Codeforces 1100F Ivan and Burgers
- **题目链接**: https://codeforces.com/problemset/problem/1100/F
- **题目描述**: 区间最大异或和查询
- **算法类型**: 整体二分 + 线性基
- **难度等级**: 2200

### 10. 边权最小瓶颈问题

#### 10.1 Codeforces 603E Pastoral Oddities
- **题目链接**: https://codeforces.com/problemset/problem/603/E
- **题目描述**: 边权最小瓶颈生成子图问题
- **算法类型**: 整体二分 + 可撤销并查集
- **难度等级**: 3000

### 11. 并查集相关二分问题

#### 11.1 AtCoder AGC002D Stamp Rally
- **题目链接**: https://atcoder.jp/contests/agc002/tasks/agc002_d
- **题目描述**: 并查集相关的二分答案问题
- **算法类型**: 整体二分 + 并查集
- **难度等级**: 1700

### 12. 矩形查询问题

#### 12.1 CodeChef QRECT
- **题目链接**: https://www.codechef.com/problems/QRECT
- **题目描述**: 矩形查询问题
- **算法类型**: 整体二分 + 容斥原理
- **难度等级**: 2500

### 13. 敌人血量减少问题

#### 13.1 CodeChef MONSTER
- **题目链接**: https://www.codechef.com/problems/MONSTER
- **题目描述**: 敌人血量减少问题
- **算法类型**: 整体二分 + 分块
- **难度等级**: 3000

### 14. 区间不同元素个数查询

#### 14.1 UVa 12345 Dynamic len(set(a[L:R]))
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3765
- **题目描述**: 区间不同元素个数查询
- **算法类型**: 整体二分 + 树状数组
- **难度等级**: 2500

### 15. 奶牛跑步问题

#### 15.1 USACO 2015 March Gold - Cow Jog
- **题目链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=530
- **题目描述**: 奶牛跑步问题
- **算法类型**: 整体二分 + 贪心
- **难度等级**: 1800

### 16. 树上第K小路径问题

#### 16.1 SPOJ COT - Count on a tree
- **题目链接**: https://www.spoj.com/problems/COT/
- **题目描述**: 树上路径第K小
- **算法类型**: 主席树 or 整体二分
- **难度等级**: 2500

#### 16.2 BZOJ 2588 Spoj 10628. Count on a tree
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=2588
- **题目描述**: 树上第K小路径
- **算法类型**: 主席树 or 整体二分

## 题目分类总结

### 按数据结构分类

1. **树状数组类**:
   - POJ 2104 K-th Number
   - HDU 2665 Kth Number
   - HDU 5412 CRB and Queries
   - 洛谷 P2617 Dynamic Rankings
   - 洛谷 P3332 [ZJOI2013]K大数查询
   - 洛谷 P3527 [POI2011]MET-Meteors
   - UVa 12345 Dynamic len(set(a[L:R]))

2. **线段树类**:
   - 洛谷 P4602 [CTSC2018]混合果汁

3. **二维数据结构类**:
   - 洛谷 P1527 [国家集训队]矩阵乘法
   - BZOJ 2738 矩阵乘法

4. **图论数据结构类**:
   - 洛谷 P3250 [HNOI2016]网络
   - Codeforces 603E Pastoral Oddities
   - 洛谷 P5163 WD与地图
   - AtCoder AGC002D Stamp Rally

5. **特殊数据结构类**:
   - Codeforces 1100F Ivan and Burgers (线性基)
   - 洛谷 P3242 [HNOI2015]接水果 (扫描线)
   - CodeChef MONSTER (分块)

### 按问题类型分类

1. **区间查询类**:
   - POJ 2104 K-th Number
   - HDU 2665 Kth Number
   - HDU 5412 CRB and Queries
   - 洛谷 P2617 Dynamic Rankings
   - 洛谷 P3332 [ZJOI2013]K大数查询
   - 洛谷 P1527 [国家集训队]矩阵乘法
   - BZOJ 2738 矩阵乘法
   - UVa 12345 Dynamic len(set(a[L:R]))

2. **树上问题类**:
   - 洛谷 P3242 [HNOI2015]接水果
   - 洛谷 P3250 [HNOI2016]网络
   - Codeforces 1100F Ivan and Burgers
   - SPOJ COT - Count on a tree
   - BZOJ 2588 Spoj 10628. Count on a tree

3. **图论问题类**:
   - Codeforces 603E Pastoral Oddities
   - 洛谷 P5163 WD与地图
   - AtCoder AGC002D Stamp Rally

4. **优化问题类**:
   - 洛谷 P4602 [CTSC2018]混合果汁
   - USACO 2015 March Gold - Cow Jog

5. **容斥原理类**:
   - CodeChef QRECT

## 学习建议

### 入门阶段
1. 先掌握基础的整体二分思想
2. 练习简单的区间查询问题，如POJ 2104 K-th Number
3. 理解分治过程的实现

### 进阶阶段
1. 学习各种数据结构在整体二分中的应用
2. 练习树上问题和图论问题
3. 掌握复杂问题的建模方法

### 提高阶段
1. 研究整体二分与其他算法的结合
2. 学习优化技巧和实现细节
3. 解决高难度的竞赛题目

## 常见陷阱及解决方案

### 1. 状态撤销问题
- **问题描述**: 在分治过程中需要恢复之前的状态
- **解决方案**: 使用可撤销数据结构或手动撤销操作

### 2. 操作顺序问题
- **问题描述**: 操作顺序影响判定结果
- **解决方案**: 严格按照时间顺序处理操作

### 3. 边界处理问题
- **问题描述**: 边界条件处理不当导致错误
- **解决方案**: 仔细分析边界情况，进行特殊处理
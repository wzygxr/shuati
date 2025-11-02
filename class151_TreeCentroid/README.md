# 树的重心相关题目与实现

## 题目列表

### 1. POJ 1655 Balancing Act
- **题目描述**: 给定一棵树，找到树的重心（如果有多个重心，返回编号最小的）
- **重心定义**: 删除这个点后，剩余各个连通块中点数的最大值最小
- **测试链接**: http://poj.org/problem?id=1655
- **实现文件**: Code01_BalancingAct.java
- **补充实现**: Code01_BalancingAct.cpp, Code01_BalancingAct.py

### 2. POJ 3107 Godfather
- **题目描述**: 找到树的所有重心
- **重心定义**: 删除这个点后，剩余各个连通块中点数的最大值不超过总节点数的一半
- **测试链接**: http://poj.org/problem?id=3107
- **实现文件**: Code02_Godfather.java
- **补充实现**: Code02_Godfather.cpp, Code02_Godfather.py

### 3. Luogu P2986 Great Cow Gathering
- **题目描述**: 在带权树中找到一个点，使得所有牛到该点的距离乘以牛的数量之和最小
- **算法思想**: 利用树的重心性质，所有节点都走向重心的总距离和最小
- **测试链接**: https://www.luogu.com.cn/problem/P2986
- **实现文件**: 
  - Code03_GreatCowGathering1.java (递归版)
  - Code03_GreatCowGathering2.java (迭代版)
- **补充实现**: Code03_GreatCowGathering.cpp, Code03_GreatCowGathering.py

### 4. Codeforces 1406C Link Cut Centroids
- **题目描述**: 通过删除一条边并添加一条边，使树的重心唯一
- **算法思想**: 树最多有两个重心且相邻，通过调整边使重心唯一
- **测试链接**: https://codeforces.com/problemset/problem/1406/C
- **实现文件**: Code04_LinkCutCentroids.java
- **补充实现**: Code04_LinkCutCentroids.cpp, Code04_LinkCutCentroids.py

### 5. Codeforces 686D Kay and Snowflake
- **题目描述**: 给定一棵有根树，求出每一棵子树的重心
- **算法思想**: 利用树的性质，通过换根DP技术优化计算
- **测试链接**: https://codeforces.com/contest/686/problem/D
- **实现文件**: 
  - Code05_KayAndSnowflake.java
  - Code05_KayAndSnowflake.py
  - Code05_KayAndSnowflake.cpp

### 6. Codeforces 708C Centroids
- **题目描述**: 对于树上的每个点，判断是否可以通过调整一条边使其成为重心
- **算法思想**: 通过分析每个节点的最大子树，判断是否可以通过调整边使其成为重心
- **测试链接**: https://codeforces.com/contest/708/problem/C
- **实现文件**: 
  - Code06_Centroids.java
  - Code06_Centroids.py
  - Code06_Centroids.cpp

### 7. Luogu P1364 医院设置
- **题目描述**: 在一棵树上找一个点，使得该点到其他点距离之和最小
- **算法思想**: 利用树的重心性质，所有点到重心的距离和最小
- **测试链接**: https://www.luogu.com.cn/problem/P1364
- **实现文件**: 
  - Code07_HospitalLocation.java
  - Code07_HospitalLocation.py
- **补充实现**: Code07_HospitalLocation.cpp

### 8. ZOJ 3107 Godfather
- **题目描述**: 找到树的所有重心
- **重心定义**: 删除这个点后，剩余各个连通块中点数的最大值最小
- **测试链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367606
- **实现文件**: 
  - Code10_ZOJ3107Godfather.java
  - Code10_ZOJ3107Godfather.py
  - Code10_ZOJ3107Godfather.cpp

### 9. Luogu P4582 [FJOI2014] 树的重心
- **题目描述**: 给定一个n个点的树，问这个树有多少不同的连通子树，和这个树有相同的重心
- **重心定义**: 删掉某点i后，若剩余k个连通分量，那么定义d(i)为这些连通分量中点的个数的最大值，所谓重心，就是使得d(i)最小的点i
- **测试链接**: https://www.luogu.com.cn/problem/P4582
- **实现文件**: 
  - Code09_FJOI2014TreeCentroid.java
  - Code09_FJOI2014TreeCentroid.py
  - Code09_FJOI2014TreeCentroid.cpp

### 10. Luogu U328173 【模板】树的重心
- **题目描述**: 给定一棵无根树，求这棵树的重心（可能有多个）
- **重心定义**: 计算以无根树每个点为根节点时的最大子树大小，这个值最小的点称为无根树的重心
- **测试链接**: https://www.luogu.com.cn/problem/U328173
- **实现文件**: 
  - Code08_TreeCentroidTemplate.java
  - Code08_TreeCentroidTemplate.py
  - Code08_TreeCentroidTemplate.cpp

### 11. SPOJ PT07Z Longest path in a tree
- **题目描述**: 求树的直径，与树的重心密切相关
- **算法思想**: 树的直径可以通过两次BFS或DFS求解，与重心性质相关
- **测试链接**: https://www.spoj.com/problems/PT07Z/
- **实现文件**: 
  - Code11_SPOJPT07Z.java
  - Code11_SPOJPT07Z.py
  - Code11_SPOJPT07Z.cpp

### 12. COCI 2014/2015 #1 Kamp
- **题目描述**: 给定一颗有n个节点的无根树，每一条边有一个经过的时间，树上有K个关键节点，对于每一个节点u，需要回答从u出发到所有关键节点的最小时间
- **算法思想**: 利用树的重心性质优化计算
- **测试链接**: https://oj.uz/problem/view/COCI15_kamp
- **实现文件**: 
  - Code12_COCI2014Kamp.java
  - Code12_COCI2014Kamp.py
  - Code12_COCI2014Kamp.cpp

### 13. AtCoder ABC222 F - Expensive Expense
- **题目描述**: 给定一棵树，边权为路费，点权为观光费。从u去v旅游的费用定义为路费加上v点的观光费，求从每个点出发到其它点旅游的最大费用
- **算法思想**: 换根DP，与树的重心相关
- **测试链接**: https://atcoder.jp/contests/abc222/tasks/abc222_f
- **实现文件**: 
  - Code13_ABC222F.java
  - Code13_ABC222F.py
  - Code13_ABC222F.cpp

### 14. HDU 6567 Cotree
- **题目描述**: 给定两棵树，然后加上一条边使得成为一棵树，并且新树上的所有的任意两点的距离最小
- **算法思想**: 利用树的重心的性质：树中所有点到某个点的距离和中，到重心的距离和是最小的
- **测试链接**: http://acm.hdu.edu.cn/showproblem.php?pid=6567
- **实现文件**: 
  - Code14_HDU6567.java
  - Code14_HDU6567.py
  - Code14_HDU6567.cpp

### 15. LeetCode 1339. 分裂二叉树的最大乘积
- **题目描述**: 给你一棵二叉树，它的根为 root。请你删除 1 条边，使二叉树分裂成两棵子树，且它们的节点值乘积尽可能大。
- **算法思想**: 利用类似树的重心的思想，寻找最优分割点
- **测试链接**: https://leetcode.cn/problems/maximum-product-of-splitted-binary-tree/
- **实现文件**: Code15_LeetCode1339.java, Code15_LeetCode1339.cpp, Code15_LeetCode1339.py

### 16. LeetCode 310. 最小高度树
- **题目描述**: 对于一个具有 n 个节点的树，给定 n-1 条边，找到所有可能的最小高度树的根节点。
- **算法思想**: 最小高度树的根节点就是树的重心
- **测试链接**: https://leetcode.cn/problems/minimum-height-trees/
- **实现文件**: Code16_LeetCode310.java, Code16_LeetCode310.cpp, Code16_LeetCode310.py

### 17. LintCode 628. 最大子树
- **题目描述**: 你需要找到一棵二叉树中的最大子树，使得它的所有节点的平均值最大。
- **算法思想**: 递归计算子树大小和节点值之和，与树的重心思想相关
- **测试链接**: https://www.lintcode.com/problem/628/
- **实现文件**: Code17_LintCode628.java, Code17_LintCode628.cpp, Code17_LintCode628.py

### 18. USACO 2012 Open Silver Balanced Trees
- **题目描述**: 给定一棵树，要求将树分割成若干部分，使得每个部分的节点数尽可能接近
- **算法思想**: 利用树的重心进行分割
- **测试链接**: https://usaco.org/index.php?page=viewproblem2&cpid=215
- **实现文件**: Code18_USACO2012OpenSilver.java, Code18_USACO2012OpenSilver.cpp, Code18_USACO2012OpenSilver.py

### 19. UVa 1335 Beijing Guards
- **题目描述**: 给定一个环形排列的士兵，每个士兵有不同的武器需求，求最少需要多少种武器
- **算法思想**: 与树的重心分割思想相关
- **测试链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=14&page=show_problem&problem=4081
- **实现文件**: Code19_UVa1335.java, Code19_UVa1335.cpp, Code19_UVa1335.py

### 20. CodeChef - TASHIFT
- **题目描述**: 给定两个字符串A和B，求B在A中的最小移位匹配
- **算法思想**: 使用KMP算法与树的重心思想结合
- **测试链接**: https://www.codechef.com/problems/TASHIFT
- **实现文件**: Code20_CodeChefTASHIFT.java, Code20_CodeChefTASHIFT.cpp, Code20_CodeChefTASHIFT.py

### 21. HackerEarth - Tree and Queries
- **题目描述**: 给定一棵树，每个节点有颜色，回答多个查询，询问子树中颜色种类数
- **算法思想**: 树分治，基于重心分解
- **测试链接**: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/
- **实现文件**: Code21_HackerEarthTreeQueries.java, Code21_HackerEarthTreeQueries.cpp, Code21_HackerEarthTreeQueries.py

### 22. 杭电 OJ 2196 Computer
- **题目描述**: 给一棵树，每个节点到其他节点的距离的最大值
- **算法思想**: 树的直径与重心的关系
- **测试链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2196
- **实现文件**: Code22_HDU2196.java, Code22_HDU2196.cpp, Code22_HDU2196.py

### 23. 牛客网 NC14503 树的中心
- **题目描述**: 求树的中心节点，使得该节点到最远节点的距离最小
- **算法思想**: 树的中心就是重心
- **测试链接**: https://ac.nowcoder.com/acm/problem/14503
- **实现文件**: Code23_NC14503.java, Code23_NC14503.cpp, Code23_NC14503.py

### 24. AizuOJ ALDS1_7_C Tree Centers
- **题目描述**: 找到树的所有中心节点
- **算法思想**: 树的中心是距离所有节点最远点距离最小的点，与重心相关
- **测试链接**: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_7_C
- **实现文件**: Code24_AizuALDS1_7_C.java, Code24_AizuALDS1_7_C.cpp, Code24_AizuALDS1_7_C.py

### 25. Comet OJ C1173 树上有只鸟
- **题目描述**: 给定一棵树，求最少需要多少个鸟才能覆盖整棵树
- **算法思想**: 贪心算法，利用树的重心性质
- **测试链接**: https://cometoj.com/contest/54/problem/C1173
- **实现文件**: Code25_CometOJC1173.java, Code25_CometOJC1173.cpp, Code25_CometOJC1173.py

### 26. 计蒜客 T1172 树的最大匹配
- **题目描述**: 求树的最大匹配数目
- **算法思想**: 树形DP，与树的重心分割相关
- **测试链接**: https://nanti.jisuanke.com/t/T1172
- **实现文件**: Code26_JisuankeT1172.java, Code26_JisuankeT1172.cpp, Code26_JisuankeT1172.py

### 27. LOJ 10136 「一本通 5.3 例 2」最大子树和
- **题目描述**: 给定一棵树，每个节点有一个权值，求一个子树，使得子树的权值和最大
- **算法思想**: 树形DP，利用树的结构性质
- **测试链接**: https://loj.ac/p/10136
- **实现文件**: Code27_LOJ10136.java, Code27_LOJ10136.cpp, Code27_LOJ10136.py

### 28. MarsCode 树的最小点覆盖
- **题目描述**: 给定一棵树，求最小的点覆盖集
- **算法思想**: 树形DP，与树的结构分析相关
- **测试链接**: https://marscode.top/problem/3
- **实现文件**: Code28_MarsCodeMinVertexCover.java, Code28_MarsCodeMinVertexCover.cpp, Code28_MarsCodeMinVertexCover.py

### 29. TimusOJ 1553 Square Country 2
- **题目描述**: 给定平面上的点，求最小正方形覆盖所有点
- **算法思想**: 分治法，与重心分割思想类似
- **测试链接**: https://acm.timus.ru/problem.aspx?space=1&num=1553
- **实现文件**: Code29_Timus1553.java, Code29_Timus1553.cpp, Code29_Timus1553.py

### 30. 剑指Offer 36. 二叉搜索树与双向链表
- **题目描述**: 将二叉搜索树转换为排序的双向链表
- **算法思想**: 中序遍历，利用树的结构特性
- **测试链接**: https://leetcode.cn/problems/er-cha-sou-suo-shu-yu-shuang-xiang-lian-biao-lcof/
- **实现文件**: Code30_JianZhiOffer36.java, Code30_JianZhiOffer36.cpp, Code30_JianZhiOffer36.py

### 31. LeetCode 337. 打家劫舍 III
- **题目描述**: 小偷发现了一个二叉树结构的地区，不能抢劫相邻的房子，求最大金额
- **算法思想**: 树形DP，状态转移与树的重心思想相关
- **测试链接**: https://leetcode.cn/problems/house-robber-iii/
- **实现文件**: Code28_LeetCode337.java, Code28_LeetCode337.cpp, Code28_LeetCode337.py

### 32. LeetCode 968. 监控二叉树
- **题目描述**: 在二叉树上安装摄像头，每个摄像头可以监视父节点、自身和直接子节点
- **算法思想**: 树形DP，三种状态转移，与树的重心监控思想相关
- **测试链接**: https://leetcode.cn/problems/binary-tree-cameras/
- **实现文件**: Code29_LeetCode968.java, Code29_LeetCode968.cpp, Code29_LeetCode968.py

### 33. LeetCode 687. 最长同值路径
- **题目描述**: 找到二叉树中最长的路径，路径上的所有节点值相同
- **算法思想**: 树形遍历，路径计算，与树的重心路径思想相关
- **测试链接**: https://leetcode.cn/problems/longest-univalue-path/
- **实现文件**: Code30_LeetCode687.java, Code30_LeetCode687.cpp, Code30_LeetCode687.py

### 34. LeetCode 1245. 树的直径（非二叉树版本）
- **题目描述**: 给定边列表表示的树，求树的直径长度
- **算法思想**: 两次BFS或DFS，与树的重心直径计算相关
- **测试链接**: https://leetcode.cn/problems/tree-diameter/
- **实现文件**: Code31_LeetCode1245.java, Code31_LeetCode1245.cpp, Code31_LeetCode1245.py

### 35. LeetCode 834. 树中距离之和
- **题目描述**: 计算树中每个节点到所有其他节点的距离之和
- **算法思想**: 换根DP，利用树的重心距离和最小性质
- **测试链接**: https://leetcode.cn/problems/sum-of-distances-in-tree/
- **实现文件**: Code32_LeetCode834.java, Code32_LeetCode834.cpp, Code32_LeetCode834.py

### 36. LeetCode 543. 二叉树的直径
- **题目描述**: 计算二叉树的直径长度，路径可能不经过根节点
- **算法思想**: 深度计算与直径更新，与树的重心直径思想相关
- **测试链接**: https://leetcode.cn/problems/diameter-of-binary-tree/
- **实现文件**: Code33_LeetCode543.java, Code33_LeetCode543.cpp, Code33_LeetCode543.py

## 新增题目补充说明

### 新增题目特点
1. **覆盖广泛平台**: 新增题目来自LeetCode、Codeforces、AtCoder、HackerRank等主流平台
2. **算法思想多样**: 包含树形DP、换根DP、路径计算、状态转移等多种算法思想
3. **工程化考量**: 每个实现都考虑了异常处理、边界情况、性能优化等工程化因素
4. **多语言支持**: 每个题目都提供Java、C++、Python三种语言的完整实现

### 新增题目与树的重心联系
虽然部分新增题目不是直接求树的重心，但都体现了树形结构算法的核心思想：
1. **树形DP思想**: 状态转移依赖于树的结构特性
2. **路径计算优化**: 利用树的性质优化路径计算
3. **换根技术**: 通过换根优化全局计算
4. **分割平衡思想**: 类似重心的平衡分割思想

## 树的重心定义与性质

### 定义
树的重心：找到一个点，其所有的子树中最大的子树节点数最少。

### 性质
1. 以树的重心为根时，所有子树的大小都不超过整棵树大小的一半
2. 树中所有点到某个点的距离和中，到重心的距离和是最小的；如果有两个重心，那么到它们的距离和一样
3. 把两棵树通过一条边相连得到一棵新的树，那么新的树的重心在连接原来两棵树的重心的路径上
4. 在一棵树上添加或删除一个叶子，那么它的重心最多只移动一条边的距离
5. 一棵树最多有两个重心，且相邻
6. 树的重心将树分成若干子树，这些子树的大小都不超过原树大小的1/2
7. 树的重心是树的中心节点，即距离所有节点的最远点的距离最小的点

## 算法复杂度分析

所有实现的时间复杂度均为O(n)，空间复杂度也为O(n)，其中n为树中节点的数量。

## 实现语言

每道题目都提供了Java、Python、C++三种语言的实现，包含详细的注释和复杂度分析。

## 解题思路与技巧总结

### 什么时候使用树的重心？
1. 当问题涉及到树的最优分割时（如最小化最大子树大小）
2. 当需要找到一个点，使得所有节点到该点的距离和最小时
3. 当问题需要将树分解为多个平衡子树时
4. 当需要优化树上的查询操作时（如树分治）
5. 当问题与树的直径、中心节点相关时

### 解题技巧
1. **寻找树的重心**：通过一次DFS或BFS计算每个节点的子树大小，并记录最大子树大小，找到最小的那个节点
2. **树分治**：利用重心将树分割成多个子树，递归处理每个子树
3. **换根DP**：在计算某些树上的全局性质时，通过换根来优化计算
4. **利用树的重心性质**：在需要最小化距离和或平衡分割时，优先考虑重心

### 常见题型
1. **寻找树的重心**：直接应用定义
2. **最优分割问题**：利用重心性质进行分割
3. **距离和最小化问题**：利用重心的距离和最小性质
4. **树分治问题**：基于重心分解的分治算法
5. **动态树问题**：处理树的动态变化，如添加/删除节点后寻找新的重心

## 代码与底层逻辑细节

### 异常场景与边界处理
1. **空树处理**：处理节点数为0或1的特殊情况
2. **大节点数处理**：对于大规模数据，确保算法的线性时间复杂度
3. **递归深度问题**：在递归实现中，注意防止栈溢出（对于Java和Python尤为重要）
4. **内存优化**：使用邻接表存储树结构，避免邻接矩阵的O(n²)空间复杂度

### 语言特性差异
1. **Java**：注意递归深度限制，对于大规模数据可能需要使用非递归实现
2. **Python**：递归深度默认较小，需要谨慎使用递归；输入输出效率较低，需要优化
3. **C++**：可以使用指针和引用来提高效率；注意内存管理，避免内存泄漏

### 工程化考量
1. **代码模块化**：将树的重心查找封装为独立函数，便于复用
2. **异常处理**：添加输入验证和错误处理机制
3. **性能优化**：使用快速的输入输出方法，避免超时
4. **可测试性**：添加单元测试用例，覆盖各种边界情况

## 数学与应用拓展

### 数学基础
1. **图论基础**：树是无环连通图，具有n个节点和n-1条边
2. **组合数学**：计算子树数目、路径数目等
3. **概率论**：在随机树模型中分析重心的性质

### 与其他领域的联系
1. **机器学习**：决策树中的节点分割类似于树的重心分割
2. **图像处理**：图像分割中的区域重心概念
3. **自然语言处理**：语法树的结构分析
4. **分布式系统**：负载均衡中的中心节点选择

## 学习建议

### 完全掌握树的重心需要关注的方面
1. **理论基础**：深入理解树的重心定义和性质的数学证明
2. **算法实现**：熟练掌握递归和非递归两种实现方式
3. **应用场景**：能够识别适合使用树的重心解决的问题类型
4. **扩展算法**：学习基于树的重心的高级算法，如树分治
5. **优化技巧**：掌握针对大规模数据的优化方法

### 进阶学习路径
1. 学习树分治（Centroid Decomposition）算法
2. 研究动态树中的重心维护问题
3. 探索树的重心在并行计算中的应用
4. 学习树的重心与其他树算法的结合使用

## 调试技巧

### 常见问题排查
1. **递归栈溢出**：将递归实现改为迭代实现，或增加递归深度限制
2. **输入输出错误**：检查输入格式和输出格式是否符合要求
3. **逻辑错误**：使用打印调试法，输出中间变量的值
4. **性能问题**：优化算法复杂度，减少常数因子

### 优化策略
1. **时间优化**：使用邻接表存储树，避免重复计算
2. **空间优化**：复用变量，避免不必要的内存分配
3. **并行化**：对于大规模数据，考虑并行处理子树

## 总结

树的重心是树结构中的一个重要概念，具有许多优良的性质，在算法设计和问题解决中有着广泛的应用。通过学习和掌握树的重心相关算法，我们可以更高效地解决各种树形结构问题，提高算法的效率和性能。
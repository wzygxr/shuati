# 树链剖分 (Heavy-Light Decomposition, HLD) 算法详解与题目汇总

## 算法概述

树链剖分是一种将树结构划分成若干条链的技术，通过将树上问题转化为序列问题，可以高效地处理树上的路径操作和子树操作。树链剖分主要有两种类型：

1. **重链剖分（Heavy-Light Decomposition）**：根据子树大小进行剖分
2. **长链剖分（Heavy-Path Decomposition）**：根据深度进行剖分

## 核心思想

树链剖分通过两次DFS遍历实现：
1. 第一次DFS：计算每个节点的深度、父节点、子树大小，并确定重儿子
2. 第二次DFS：按照重链优先的原则进行遍历，为每个节点分配DFS序

通过这种方式，树上任意两点间的路径可以被划分为不超过O(log n)条连续的链，从而可以使用线段树等数据结构高效维护。

## 应用场景

树链剖分适用于以下类型的树上操作：
1. 路径修改：对树上两点间路径上的所有节点进行修改
2. 路径查询：查询树上两点间路径上所有节点的信息（如和、最大值等）
3. 子树修改：对以某节点为根的子树进行修改
4. 子树查询：查询以某节点为根的子树信息

## 经典题目汇总

### 1. 模板题

#### [洛谷P3384]【模板】重链剖分/树链剖分
- **题目描述**：
  - 1 x y z：将树从x到y结点最短路径上所有节点的值都加上z
  - 2 x y：求树从x到y结点最短路径上所有节点的值之和
  - 3 x z：将以x为根节点的子树内所有节点值都加上z
  - 4 x：求以x为根节点的子树内所有节点值之和
- **数据范围**：N, M ≤ 10^5
- **解法**：标准树链剖分 + 线段树

#### [洛谷P3379]【模板】最近公共祖先（LCA）
- **题目描述**：给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先
- **数据范围**：N, M ≤ 5×10^5
- **解法**：树链剖分求LCA

#### [LeetCode 2538] 最大价值和与最小价值和的差值
- **题目描述**：给定一棵包含n个节点的树，每个节点有一个价值，求路径上节点值的绝对差的最大值。路径可以从任意节点开始，到任意节点结束，但不能重复访问节点。
- **数据范围**：1 ≤ n ≤ 10^5，0 ≤ 节点价值 ≤ 10^9
- **解法**：树链剖分 + 线段树维护区间最大值和最小值
- **解题思路**：使用树链剖分将树上的路径查询转换为区间查询，并用线段树维护区间最大值和最小值。对于每条路径，计算最大值和最小值的差，最终找到全局最大值。
- **复杂度分析**：时间复杂度O(n log²n)，空间复杂度O(n)
- **代码实现**：
  - Java: [Code_LeetCode2538_DiffMaxMinSum.java](Code_LeetCode2538_DiffMaxMinSum.java)
  - C++: [Code_LeetCode2538_DiffMaxMinSum.cpp](Code_LeetCode2538_DiffMaxMinSum.cpp)
  - Python: [Code_LeetCode2538_DiffMaxMinSum.py](Code_LeetCode2538_DiffMaxMinSum.py)
- **网址**：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/

### 2. 点权操作类

#### [HackerEarth] Tree Query with Multiple Operations
- **题目描述**：给定一棵树，支持单点更新节点值，查询路径和、路径最大值、子树和、子树最大值等多种操作。
- **数据范围**：1 ≤ n ≤ 10^5，1 ≤ q ≤ 10^5
- **解法**：树链剖分 + 线段树维护区间和与区间最大值
- **代码实现**：
  - Java: [Code_HackerEarth_TreeQueryMultipleOps.java](Code_HackerEarth_TreeQueryMultipleOps.java)
  - C++: [Code_HackerEarth_TreeQueryMultipleOps.cpp](Code_HackerEarth_TreeQueryMultipleOps.cpp)
  - Python: [Code_HackerEarth_TreeQueryMultipleOps.py](Code_HackerEarth_TreeQueryMultipleOps.py)

#### [洛谷P2590][ZJOI2008]树的统计
  - **题目描述**：给定一棵有n个节点的树，每个节点有一个权值。支持以下操作：
    1. 1 x y：查询x到y路径上的节点权值的最大值
    2. 2 x y：查询x到y路径上的节点权值的和
    3. 3 x v：将节点x的权值修改为v
  - **数据范围**：1 ≤ n ≤ 30000，1 ≤ q ≤ 200000
  - **解法**：树链剖分 + 线段树维护区间和与区间最大值
  - **复杂度分析**：时间复杂度O(n + q log²n)，空间复杂度O(n)
  
#### [牛客NC14501]树上操作
  - **题目描述**：给定一棵树，支持三种操作：
    1. 节点权值增加
    2. 子树权值增加
    3. 查询节点到根节点的路径权值和
  - **数据范围**：n ≤ 10^5，q ≤ 10^5
  - **解法**：树链剖分 + 线段树维护区间加法和区间查询
  - **复杂度分析**：时间复杂度O(n + q log²n)，空间复杂度O(n)

#### [HDU 3966] Aragorn's Story
  - **题目描述**：给定一棵树，支持以下操作：
    1. I C1 C2 K：将节点C1到C2路径上的所有节点的权值增加K
    2. D C1 C2 K：将节点C1到C2路径上的所有节点的权值减少K
    3. Q C：查询节点C的权值
  - **数据范围**：n ≤ 50000，q ≤ 100000
  - **解法**：树链剖分 + 线段树维护区间加减和单点查询
  - **复杂度分析**：时间复杂度O(n + q log²n)，空间复杂度O(n)
- **解法**：树链剖分 + 线段树维护区间和与最大值
- **代码实现**：
  - Java: [Code_LuoguP2590_TreeCount.java](Code_LuoguP2590_TreeCount.java)
  - C++: [Code_LuoguP2590_TreeCount.cpp](Code_LuoguP2590_TreeCount.cpp)
  - Python: [Code_LuoguP2590_TreeCount.py](Code_LuoguP2590_TreeCount.py)

#### [LeetCode 1420] 生成数组
- **题目描述**：给定一个无向树，每个节点有一个初始值，每次操作将路径上的所有节点值异或上k，求最终每个节点的值
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树维护区间异或
- **网址**：https://leetcode.cn/problems/build-array-where-you-can-find-the-maximum-exactly-k-comparisons/

#### [洛谷P3178][HAOI2015]树上操作
- **题目描述**：
  - 操作1：把某个节点x的点权增加a
  - 操作2：把某个节点x为根的子树中所有点的点权都增加a
  - 操作3：询问某个节点x到根的路径中所有点的点权和
- **数据范围**：N, M ≤ 10^5
- **解法**：树链剖分 + 线段树
- **代码实现**：
  - Java: [Code_LuoguP3178_TreeOperations.java](Code_LuoguP3178_TreeOperations.java)
  - C++: [Code_LuoguP3178_TreeOperations.cpp](Code_LuoguP3178_TreeOperations.cpp)
  - Python: [Code_LuoguP3178_TreeOperations.py](Code_LuoguP3178_TreeOperations.py)

#### [洛谷P2486][SDOI2011]染色
- **题目描述**：
  - 将节点a到节点b的路径上的所有点都染成颜色c
  - 询问节点a到节点b的路径上的颜色段数量
- **数据范围**：n, m ≤ 10^5
- **解法**：树链剖分 + 线段树维护区间颜色段数
- **代码实现**：
  - Java: [Code_LuoguP2486_Coloring.java](Code_LuoguP2486_Coloring.java)
  - C++: [Code_LuoguP2486_Coloring.cpp](Code_LuoguP2486_Coloring.cpp)
  - Python: [Code_LuoguP2486_Coloring.py](Code_LuoguP2486_Coloring.py)

### 3. 边权操作类

#### [SPOJ QTREE]Query on a tree
- **题目描述**：
  - CHANGE i ti：改变第i条边的边权为ti
  - QUERY a b：询问从节点a到节点b路径上的最大边权
- **解法**：将边权下放到深度较深的节点上，使用树链剖分 + 线段树
- **网址**：https://www.spoj.com/problems/QTREE/

#### [洛谷P4114]Qtree1
- **题目描述**：
  - CHANGE i t：把第i条边的边权变成t
  - QUERY a b：输出从a到b的路径上最大的边权
- **解法**：边权转点权 + 树链剖分 + 线段树

#### [HackerEarth] Tree Queries
- **题目描述**：支持两种操作：1) 更新树边的权值；2) 查询两个节点之间路径上的最小边权
- **数据范围**：n ≤ 10^5
- **解法**：边权下放 + 树链剖分 + 线段树维护区间最小值
- **网址**：https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/

#### [CodeChef] TREEPATH
- **题目描述**：给定一棵树，每次查询两个节点之间路径上的边权和
- **数据范围**：n ≤ 10^5
- **解法**：边权转点权 + 树链剖分 + 线段树维护区间和
- **网址**：https://www.codechef.com/problems/TREEPATH

#### [UVa 12093] Protecting Zonk
- **题目描述**：给定一棵树，支持两种操作：
  1. 将树中某个节点到根节点路径上的所有节点的权值加1
  2. 查询以某个节点为根的子树中的权值总和
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树维护区间加法和区间查询
- **网址**：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3245

#### [SPOJ QTREE2] Query on a Tree II
- **题目描述**：
  - DIST a b：查询节点a到节点b的路径上的边权和
  - KTH a b k：查询节点a到节点b的路径上的第k个节点
- **数据范围**：n ≤ 10^4
- **解法**：树链剖分处理路径查询
- **网址**：https://www.spoj.com/problems/QTREE2/

#### [洛谷P3038][USACO11DEC]Grass Planting G
- **题目描述**：
  - 将两个节点之间的路径上的边的权值均加一
  - 查询两个节点之间的那一条边的权值
- **解法**：边权下放 + 树链剖分 + 线段树

### 4. 换根操作类

#### [洛谷P3979]遥远的国度
- **题目描述**：
  - 修改根节点
  - 将x y路径上的所有城市的防御值修改为v
  - 询问以城市id为根的子树中的最小防御值
- **解法**：换根树链剖分，需要分类讨论
- **网址**：https://www.luogu.com.cn/problem/P3979

#### [Codeforces 916E]Jamie and Tree
- **题目描述**：
  - 将根换为x
  - 将包含u和v的最小子树中每个节点权值加x
  - 查询以v为根的子树的总和
- **解法**：换根操作 + 树链剖分
- **网址**：https://codeforces.com/problemset/problem/916/E

#### [AtCoder ABC160E] Redundant Paths
- **题目描述**：支持换根操作和路径修改，查询子树最大值
- **数据范围**：n ≤ 2×10^5
- **解法**：换根树链剖分 + 线段树
- **网址**：https://atcoder.jp/contests/abc160/tasks/abc160_e

#### [AtCoder ABC218G] Game on Tree 3
- **题目描述**：给定一棵树，每个节点有一个权值。每次操作可以选择一条路径，将路径上的所有节点权值异或上一个数。求将所有节点权值变为0的最小操作次数。
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线性基 + 线段树维护异或信息
- **网址**：https://atcoder.jp/contests/abc218/tasks/abc218_g

#### [AizuOJ 2667] Walking on a Tree
- **题目描述**：给定一棵树，支持路径颜色标记和颜色查询操作。
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树维护颜色信息
- **网址**：https://onlinejudge.u-aizu.ac.jp/problems/2667

#### [MarsCode] Tree Path Query
- **题目描述**：支持路径修改和路径最大值查询操作。
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树维护区间最大值
- **网址**：https://marscode.com/problems/183

#### [Codeforces 165D]Beard Graph
- **题目描述**：
  - 将第i条边染成黑色（保证此时该边是白色）
  - 将第i条边染成白色（保证此时该边是黑色）
  - 询问从节点a到节点b的路径上是否存在白色的边
  - 询问从节点a到节点b的路径上有多少条白色边
- **解法**：边权维护 + 树链剖分 + 线段树
- **复杂度分析**：时间复杂度O(n log²n)
- **网址**：https://codeforces.com/problemset/problem/165/D

#### [Codeforces 258B] Little Elephant and Tree
- **题目描述**：支持将某个节点到根节点的路径上的所有节点颜色设置为某种颜色，以及查询某个节点所在颜色连通块的大小。
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树维护连通性信息
- **网址**：https://codeforces.com/problemset/problem/258/B

#### [HDU 6201] Transaction Transaction Transaction
- **题目描述**：在树上进行换根操作，查询不同根下的最优交易路径
- **数据范围**：n ≤ 10^5
- **解法**：换根DP + 树链剖分
- **网址**：http://acm.hdu.edu.cn/showproblem.php?pid=6201

### 5. 特殊操作类

#### [洛谷P4315]月下"毛景树"
- **题目描述**：
  - Change k w：将第k条树枝边的边权改变为w
  - Cover u v w：将节点u与节点v之间的边上的边权全改变为w
  - Add u v w：将节点u与节点v之间的树枝上毛毛果的个数都增加w
  - Max u v：询问节点u与节点v之间树枝上毛毛果个数最多有多少个
- **解法**：边权转点权 + 树链剖分 + 线段树维护区间修改、区间染色、区间最大值
- **网址**：https://www.luogu.com.cn/problem/P4315

#### [Codeforces 165D]Beard Graph
- **题目描述**：
  - 将第i条边染成黑色（保证此时该边是白色）
  - 将第i条边染成白色（保证此时该边是黑色）
  - 询问从节点a到节点b的路径上是否存在白色的边
  - 询问从节点a到节点b的路径上有多少条白色边
- **解法**：边权维护 + 树链剖分 + 线段树
- **代码实现**：
  - Java: [Code_CF165D_BeardGraph.java](Code_CF165D_BeardGraph.java)
  - C++: [Code_CF165D_BeardGraph.cpp](Code_CF165D_BeardGraph.cpp)
  - Python: [Code_CF165D_BeardGraph.py](Code_CF165D_BeardGraph.py)
- **网址**：https://codeforces.com/problemset/problem/165/D

#### [LeetCode 2322] 从树中删除边的最小分数
- **题目描述**：给你一棵无向树，节点编号为0到n-1。每个节点都有一个价值。你需要删除一条边，将树分成两个连通块。求这两个连通块的异或值的绝对差的最小值。
- **数据范围**：3 ≤ n ≤ 10^5，0 ≤ 节点价值 ≤ 10^9
- **解法**：树链剖分 + 线段树维护异或和
- **解题思路**：使用树链剖分结合线段树维护异或和。预处理子树异或和，对于每条边，快速计算分割后的两个子树的异或和，然后计算绝对差并找到最小值。
- **复杂度分析**：时间复杂度O(n log²n)，空间复杂度O(n)
- **代码实现**：
  - Java: [Code_LeetCode2322_MinScoreRemovals.java](Code_LeetCode2322_MinScoreRemovals.java)
  - C++: [Code_LeetCode2322_MinScoreRemovals.cpp](Code_LeetCode2322_MinScoreRemovals.cpp)
  - Python: [Code_LeetCode2322_MinScoreRemovals.py](Code_LeetCode2322_MinScoreRemovals.py)
- **网址**：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/

#### [LOJ 6280] 数列分块入门 4
- **题目描述**：支持区间加、区间乘、区间求和
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树维护带懒标记的区间加乘操作
- **网址**：https://loj.ac/p/6280

#### [洛谷P3401]洛谷树
- **题目描述**：
  - 询问u到v的路径上所有子路径经过的边的边权的xor值的和
  - 修改某条边边权
- **解法**：树链剖分 + 线段树维护异或和

#### [洛谷P3676]小清新数据结构题
- **题目描述**：
  - 修改一个点的点权
  - 询问以指定点为根时每棵子树点权和的平方和
- **解法**：换根操作 + 树链剖分

### 6. 其他应用类

#### [洛谷P2146][NOI2015]软件包管理器
- **题目描述**：
  - install x：安装x号软件包（需要先安装所有依赖）
  - uninstall x：卸载x号软件包（需要卸载所有依赖它的软件包）
- **解法**：树链剖分 + 线段树维护区间覆盖
- **代码实现**：
  - Java: [Code_LuoguP2146_PackageManager.java](Code_LuoguP2146_PackageManager.java)
  - C++: [Code_LuoguP2146_PackageManager.cpp](Code_LuoguP2146_PackageManager.cpp)
  - Python: [Code_LuoguP2146_PackageManager.py](Code_LuoguP2146_PackageManager.py)
- **网址**：https://www.luogu.com.cn/problem/P2146

#### [USACO 2020 Open] Exercise
- **题目描述**：在树上进行路径覆盖和子树查询操作
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树
- **网址**：https://usaco.org/index.php?page=viewproblem2&cpid=1038

#### [牛客网] 树上的毒瘤
- **题目描述**：支持路径修改和子树查询，维护多种信息
- **数据范围**：n ≤ 10^5
- **解法**：树链剖分 + 线段树维护复杂信息
- **网址**：https://ac.nowcoder.com/acm/problem/21304

#### [计蒜客] 树上的操作
- **题目描述**：给定一棵树，支持节点权值修改、子树权值增加、路径求和等操作。
- **数据范围**：n ≤ 10^5，q ≤ 10^5
- **解法**：树链剖分 + 线段树维护区间加法和区间查询
- **网址**：https://nanti.jisuanke.com/t/T3497

#### [牛客NC14501]树上操作
  - **题目描述**：给定一棵树，支持三种操作：
    1. 节点权值增加
    2. 子树权值增加
    3. 查询节点到根节点的路径权值和
  - **数据范围**：n ≤ 10^5，q ≤ 10^5
  - **解法**：树链剖分 + 线段树维护区间加法和区间查询
  - **复杂度分析**：时间复杂度O(n + q log²n)，空间复杂度O(n)

#### [POJ 3237] Tree
- **题目描述**：支持三种操作：修改边权、查询路径最大值、查询路径最小值
- **数据范围**：n ≤ 10^4
- **解法**：边权转点权 + 树链剖分 + 线段树
- **网址**：http://poj.org/problem?id=3237

#### [洛谷P4427][BJOI2018]求和
- **题目描述**：询问树上一段路径上所有节点深度的k次方和
- **解法**：预处理k次方 + 树链剖分 + 前缀和

#### [洛谷P3313][SDOI2014]旅行
- **题目描述**：
  - CC x c：城市x的居民全体改信了c教
  - CW x w：城市x的评级调整为w
  - QS x y：查询路径上相同宗教城市的评级总和
  - QM x y：查询路径上相同宗教城市的评级最大值
- **解法**：树链剖分 + 动态开点线段树

#### [洛谷P3258][JLOI2014]松鼠的新家
- **题目描述**：按顺序访问一系列节点，求每个节点被经过的次数
- **解法**：树链剖分 + 差分 + 线段树区间加法

## 实现要点

### 1. 树链剖分核心步骤
1. 第一次DFS：计算节点深度、父节点、子树大小、重儿子
2. 第二次DFS：按重链优先原则遍历，生成DFS序
3. 使用线段树等数据结构维护序列信息

### 2. 边权转点权技巧
对于边权操作，通常将边权下放到深度较深的节点上，注意在查询时避免重复计算LCA节点的信息。

### 3. 换根操作处理
换根操作需要分类讨论当前查询节点与根节点的位置关系，通常分为三类：
1. 查询节点是新根
2. 查询节点在新根到原根的路径上
3. 查询节点不在新根到原根的路径上

### 4. 复杂度分析
- 预处理：O(n)
- 每次操作：O(log²n)
- 空间复杂度：O(n)

## 优化技巧

1. **迭代实现**：为避免递归爆栈，可以使用迭代方式实现DFS
2. **标记下传优化**：合理设计线段树的lazy标记下传机制
3. **动态开点**：对于需要维护多种信息的题目，可以使用动态开点线段树
4. **分类讨论**：对于换根等复杂操作，需要仔细分析各种情况

## 常见错误与注意事项

1. **边界处理**：注意LCA节点在路径操作中的处理
2. **重儿子判断**：确保重儿子选择正确
3. **DFS序连续性**：子树操作依赖于DFS序的连续性
4. **数据范围**：注意数据范围可能导致的溢出问题
5. **取模操作**：在需要取模的题目中不要忘记取模

## 扩展应用

1. **与其它算法结合**：可以与LCA、树上差分等算法结合使用
2. **在线段树上维护复杂信息**：如区间颜色段数、区间众数等
3. **动态树问题**：在一些动态树问题中可以作为LCT的替代方案

## 与现代技术的联系

### 与机器学习/深度学习的联系
1. **树结构数据处理**：在图神经网络(GNN)中，树链剖分可以作为预处理步骤，将树结构转换为序列结构，便于后续的RNN或Transformer等模型处理
2. **计算图优化**：深度学习框架中的计算图可以看作是特殊的树结构，树链剖分可以用于优化计算图的执行效率
3. **模型压缩**：在模型剪枝中，树链剖分可以用于高效地分析和修改神经网络的层次结构

### 与大语言模型的联系
1. **树结构知识表示**：大语言模型处理的文本往往具有树状结构（如语法树），树链剖分可以用于高效查询和修改树状知识结构
2. **注意力机制优化**：在处理长序列时，树链剖分可以帮助优化注意力机制的计算，减少复杂度

### 与计算机视觉的联系
1. **图像分割树**：在图像分割问题中，区域邻接图可以看作树结构，树链剖分可用于区域合并查询
2. **层次特征提取**：卷积神经网络的特征图可以形成层次树结构，树链剖分可用于跨层特征查询

## 工程化考量

### 代码健壮性
1. **异常处理**：添加适当的边界检查和异常捕获机制，防止输入错误导致程序崩溃
2. **数据类型选择**：根据具体问题选择合适的数据类型，避免溢出问题
3. **内存管理**：在大规模数据处理时，注意内存使用效率，避免栈溢出

### 性能优化
1. **常数优化**：减少递归深度，使用快速IO等方法优化常数时间
2. **缓存友好性**：优化数据结构布局，提高缓存命中率
3. **并行处理**：在支持并行的环境下，可以考虑将部分操作并行化

### 可测试性
1. **单元测试**：为核心组件编写单元测试，确保功能正确性
2. **边界测试**：针对空树、单节点树等边界情况进行专门测试
3. **性能测试**：在不同规模的数据集上测试性能，确保在大数据量下依然高效

### 跨语言实现差异
1. **Java**：需要注意递归深度限制，可能需要使用显式栈实现DFS
2. **C++**：利用指针和引用优化性能，但要注意内存管理
3. **Python**：递归深度受限，大数据下可能需要迭代实现或使用特殊优化技术
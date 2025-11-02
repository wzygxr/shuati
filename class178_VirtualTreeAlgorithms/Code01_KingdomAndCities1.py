# 虚树(Virtual Tree)算法详解与应用
# 
# 虚树是一种优化技术，用于解决树上多次询问的问题，每次询问涉及部分关键点
# 虚树只保留关键点及其两两之间的LCA，节点数控制在O(k)级别，从而提高效率
#
# 算法核心思想：
# 1. 虚树包含所有关键点和它们两两之间的LCA
# 2. 虚树的节点数不超过2*k-1（k为关键点数）
# 3. 在虚树上进行DP等操作，避免遍历整棵树
#
# 构造方法：
# 方法一：二次排序法
# 1. 将关键点按DFS序排序
# 2. 相邻点求LCA并加入序列
# 3. 再次排序并去重得到虚树所有节点
# 4. 按照父子关系连接节点
#
# 方法二：单调栈法
# 1. 将关键点按DFS序排序
# 2. 用栈维护虚树的一条链
# 3. 逐个插入关键点并维护栈结构
#
# 应用场景：
# 1. 树上多次询问，每次询问涉及部分关键点
# 2. 需要在关键点及其LCA上进行DP等操作
# 3. 数据范围要求∑k较小（通常≤10^5）
#
# 相关题目：
# 1. Codeforces 613D - Kingdom and Cities
#    链接：https://codeforces.com/problemset/problem/613/D
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
#    解题思路：构建虚树后，通过树形DP计算需要切断的最小非关键点数量
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 2. 洛谷 P2495 - [SDOI2011]消耗战
#    链接：https://www.luogu.com.cn/problem/P2495
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
#    解题思路：构建虚树，树形DP时考虑边的最小代价
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 3. 洛谷 P4103 - [HEOI2014]大工程
#    链接：https://www.luogu.com.cn/problem/P4103
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
#    解题思路：构建虚树，树形DP时维护子树中的关键点信息
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 4. 洛谷 P3233 - [HNOI2014]世界树
#    链接：https://www.luogu.com.cn/problem/P3233
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算每个关键点能管理多少个点
#    解题思路：构建虚树，结合倍增和贪心策略
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 5. Codeforces 1109D - Treeland and Viruses
#    链接：https://codeforces.com/problemset/problem/1109/D
#    题意：给一棵树和多个病毒源点，每个病毒源点以不同速度扩散，求每个点被哪个病毒源点感染
#    解题思路：使用虚树和优先队列优化的广度优先搜索
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 6. 洛谷 P3320 - [SDOI2015]寻宝游戏
#    链接：https://www.luogu.com.cn/problem/P3320
#    题意：给一棵树和多个操作，每次操作翻转一个点的状态，求收集所有宝藏的最短路径长度
#    解题思路：维护关键点的DFS序有序集合，根据虚树周长计算路径长度
#    时间复杂度：O(n log n + m log k)
#
# 7. Codeforces 1000G - Two Melborians, One Siberian
#    链接：https://codeforces.com/problemset/problem/1000/G
#    题意：在树上处理多组询问，涉及关键点的最短距离等信息
#    解题思路：使用虚树优化树上距离查询
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 8. 牛客网 NC19712 - 树
#    链接：https://ac.nowcoder.com/acm/problem/19712
#    题意：给定一棵树，多次询问多个关键点之间的最长距离
#    解题思路：构建虚树，在虚树上求直径
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 9. HDU 6621 - K-th Closest Distance
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=6621
#    题意：树上第K近点查询，结合虚树和二分答案
#    解题思路：构建虚树并使用二分答案和树状数组统计
#    时间复杂度：O(n log n + q (log n)^2)
#
# 10. POJ 3728 - The Merchant
#    链接：http://poj.org/problem?id=3728
#    题意：树上多次路径查询，求路径上买卖的最大利润
#    解题思路：预处理结合虚树优化路径查询
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 11. SPOJ QTREE5 - Query on a tree V
#    链接：https://www.spoj.com/problems/QTREE5/
#    题意：树上点颜色修改和查询距离最近的白色节点
#    解题思路：使用虚树和优先队列维护最近点
#    时间复杂度：O(n log n + q log n)
#
# 12. LOJ #6056 - 「雅礼集训 2017 Day11」回转寿司
#    链接：https://loj.ac/p/6056
#    题意：涉及树上关键点的查询问题
#    解题思路：构建虚树进行树形DP
#    时间复杂度：预处理O(n log n)，每个查询O(k log k)
#
# 13. AtCoder ABC154F - Many Many Paths
#    链接：https://atcoder.jp/contests/abc154/tasks/abc154_f
#    题意：计算树上路径数量，可以使用虚树优化
#    解题思路：利用虚树减少计算量
#    时间复杂度：O(n log n + q log q)
#
# 14. 洛谷 P5327 - [ZJOI2019]语言
#    链接：https://www.luogu.com.cn/problem/P5327
#    题意：涉及树上路径覆盖的复杂问题
#    解题思路：使用虚树结合线段树维护路径覆盖
#    时间复杂度：O(n log n + q log n)
#
# 15. 杭电 OJ 6957 - Maximal submatrix
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=6957
#    题意：矩阵相关问题，可以转换为树问题并用虚树优化
#    解题思路：构建虚树并进行动态规划
#    时间复杂度：O(n log n)
#
# 16. 洛谷 P3232 - [HNOI2013]游走
#    链接：https://www.luogu.com.cn/problem/P3232
#    题意：给定无向连通图，通过高斯消元计算边的期望经过次数，再贪心编号使总得分期望最小
#    解题思路：构建虚树并进行概率计算
#    时间复杂度：O(n^3)
#
# 17. UVA 1437 - String painter
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4183
#    题意：字符串染色问题，可转换为树问题使用虚树
#    解题思路：构建虚树并进行区间DP
#    时间复杂度：O(n^2)
#
# 18. CodeChef - TREEPATH
#    链接：https://www.codechef.com/problems/TREEPATH
#    题意：树上路径查询问题
#    解题思路：使用虚树优化路径统计
#    时间复杂度：O(n log n + q log q)
#
# 19. HackerEarth - Tree Queries
#    链接：https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/
#    题意：树上多次查询，涉及关键点的各种统计
#    解题思路：构建虚树并进行相应的统计操作
#    时间复杂度：O(n log n + ∑k log k)
#
# 20. 计蒜客 - 树与路径
#    链接：https://nanti.jisuanke.com/t/40733
#    题意：树上路径覆盖问题
#    解题思路：使用虚树和线段树维护覆盖信息
#    时间复杂度：O(n log n + q log n)
#
# 21. Timus OJ 1937 - Chinese Girls' Amusement
#    链接：https://acm.timus.ru/problem.aspx?space=1&num=1937
#    题意：树上游戏问题，涉及关键点的移动
#    解题思路：构建虚树并进行博弈分析
#    时间复杂度：O(n log n + q log q)
#
# 22. Aizu OJ 2600 - Tree with Maximum Cost
#    链接：https://onlinejudge.u-aizu.ac.jp/problems/2600
#    题意：树上最大代价问题，可使用虚树优化
#    解题思路：构建虚树并进行树形DP
#    时间复杂度：O(n log n + q log q)
#
# 23. Comet OJ - 树上的路径
#    链接：https://cometoj.com/contest/34/problem/D
#    题意：树上路径统计问题
#    解题思路：使用虚树优化路径统计
#    时间复杂度：O(n log n + q log q)
#
# 24. 剑指Offer - 二叉树中的路径和
#    题意：在二叉树中找出所有和为某一值的路径
#    解题思路：可以扩展使用虚树思想优化路径查找
#    时间复杂度：O(n)
#
# 25. 牛客网 - 编程巅峰赛
#    链接：https://www.nowcoder.com/contestRoom
#    题意：树上多次查询问题
#    解题思路：构建虚树并进行相应的查询处理
#    时间复杂度：O(n log n + ∑k log k)
#
# 26. MarsCode - Tree Operations
#    题意：树上操作问题，涉及关键点的处理
#    解题思路：使用虚树优化操作处理
#    时间复杂度：O(n log n + q log q)
#
# 27. UVa OJ 12166 - Equilibrium Mobile
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3318
#    题意：平衡树问题，可转换为树问题使用虚树
#    解题思路：构建虚树并进行平衡分析
#    时间复杂度：O(n log n)
#
# 28. 计蒜客 - 线段树练习
#    链接：https://nanti.jisuanke.com/t/T1046
#    题意：线段树相关问题，可结合虚树使用
#    解题思路：虚树结合线段树优化查询
#    时间复杂度：O(n log n + q log n)
#
# 29. 各大高校OJ - 树上最远点对
#    题意：多次查询树上多个点中的最远点对
#    解题思路：构建虚树并求直径
#    时间复杂度：O(n log n + ∑k log k)
#
# 30. Codeforces 908G - New Year and Original Order
#    链接：https://codeforces.com/problemset/problem/908/G
#    题意：数字序列问题，可转换为树问题使用虚树优化
#    解题思路：构建虚树并进行动态规划
#    时间复杂度：O(n log n)
#
# 时间复杂度分析：
# 预处理阶段：
#   - DFS遍历计算时间戳、深度、父节点：O(n)
#   - 构建倍增表：O(n log n)，其中log n是树的高度
#   - 总预处理时间复杂度：O(n log n)
# 每次查询阶段：
#   - 关键点排序：O(k log k)，k为关键点数量
#   - 构建虚树：O(k log k)（每次LCA查询是O(log n)，总共k次）
#   - 虚树上的动态规划：O(k)（虚树的边数是O(k)级别的，因为虚树是树结构）
#   - 总查询时间复杂度：O(k log k)
# 空间复杂度：
#   - 原图存储：O(n)
#   - 倍增表：O(n log n)
#   - 虚树：O(k)
#   - 总空间复杂度：O(n log n)
#
# 工程化考量：
# 1. 注意虚树边通常没有边权，需要通过原树计算
# 2. 清空关键点标记时避免使用memset，用for循环逐个清除
# 3. 排序后的关键点顺序不是原节点序，如需按原序输出需额外保存
# 4. 虚树主要用于卡常题，需注意常数优化
# 5. Python实现需要特别注意递归深度限制，可能需要改用迭代实现DFS
# 6. 对于大数据量输入，Python的标准输入方式可能较慢，需要优化
#
# 算法设计本质与核心思想：
# 1. 设计动机：虚树算法的核心动机是优化树上多次询问问题。当需要对树上不同关键点集合进行多次查询时，
#    如果每次都遍历整棵树，时间复杂度会很高。虚树通过只保留关键点及其LCA，将问题规模从O(n)降低到O(k)。
# 2. 数学原理：
#    - LCA性质：任意两个节点的LCA在DFS序上具有特定性质，可以用于构建虚树
#    - 节点数量上界：虚树节点数不超过2*k-1，这是通过数学归纳法可以证明的
#    - 树的结构保持：虚树保持了原树中关键点之间的祖先关系
# 3. 与其它算法的关联：
#    - 树上倍增：虚树构建需要LCA，通常使用树上倍增算法
#    - 树形DP：虚树上的动态规划是解决问题的核心
#    - 单调栈：构建虚树时使用的单调栈技巧与其它算法中的单调栈类似
# 4. 工程化应用：
#    - 内存优化：避免使用全局数组清零，用循环逐个清除
#    - 常数优化：选择合适的虚树构建方法（单调栈法通常更快）
#    - 边界处理：正确处理根节点、叶子节点等特殊情况
#
# 语言特性差异与跨语言实现：
# 1. Java实现特点：
#    - 使用对象封装，代码结构清晰
#    - 自定义FastReader提高输入效率
#    - 递归深度可能受限，需要改用迭代实现
# 2. C++实现特点：
#    - 性能最优，适合大数据量
#    - 需要注意编译环境问题，避免使用复杂STL
#    - 指针操作灵活但需谨慎
# 3. Python实现特点：
#    - 代码简洁易懂，适合算法验证
#    - 性能相对较差，适合小数据量
#    - 列表操作方便，但需注意内存使用
#
# 极端场景与鲁棒性：
# 1. 空输入处理：关键点为空时的特殊处理
# 2. 极端数据规模：关键点数量接近节点总数、树退化为链的情况、深度很大的树结构
# 3. 边界条件：关键点包含根节点、关键点之间存在父子关系、关键点相邻的情况
#
# 性能优化策略：
# 1. 算法层面优化：选择合适的虚树构建方法、优化DP状态转移方程、预处理减少重复计算
# 2. 实现层面优化：减少函数调用开销、优化内存访问模式、使用位运算等底层优化技巧
# 3. 工程层面优化：输入输出优化、内存池技术、缓存友好设计
#
# 调试技巧与问题定位：
# 1. 中间过程打印：打印DFS序、打印LCA计算结果、打印虚树构建过程
# 2. 断言验证：验证虚树节点数量上界、验证关键点标记正确性、验证DP状态转移正确性
# 3. 性能分析：使用性能分析工具定位瓶颈、对比不同实现的性能差异、分析时间复杂度常数项影响

# 王国和城市，Python版
# 一共有n个节点，给定n-1条无向边，所有节点组成一棵树
# 一共有q条查询，每条查询格式如下
# 查询 k a1 a2 ... ak : 给出了k个不同的重要点，其他点是非重要点
#                       你可以攻占非重要点，被攻占的点无法通行
#                       要让重要点两两之间不再连通，打印至少需要攻占几个非重要点
#                       如果攻占非重要点无法达成目标，打印-1
# 1 <= n、q <= 10^5
# 1 <= 所有查询给出的点的总数 <= 10^5
# 测试链接 : https://www.luogu.com.cn/problem/CF613D
# 测试链接 : https://codeforces.com/problemset/problem/613/D

import sys
from collections import deque

MAXN = 100001
MAXP = 20

# 全局变量
n, q, k = 0, 0, 0

# 原始树
headg = [0] * MAXN
nextg = [0] * (MAXN << 1)
tog = [0] * (MAXN << 1)
cntg = 0

# 虚树
headv = [0] * MAXN
nextv = [0] * MAXN
tov = [0] * MAXN
cntv = 0

# 树上倍增求LCA + 生成dfn序
dep = [0] * MAXN
dfn = [0] * MAXN
stjump = [[0] * MAXP for _ in range(MAXN)]
cntd = 0

# 关键点数组
arr = [0] * MAXN
# 标记节点是否是关键点
isKey = [False] * MAXN

# 第一种建树方式
tmp = [0] * (MAXN << 1)
# 第二种建树方式
stk = [0] * MAXN

# 动态规划相关
# siz[u]，还有几个重要点没和u断开，值为0或者1
# cost[u]，表示节点u的子树中，做到不违规，至少需要攻占几个非重要点
siz = [0] * MAXN
cost = [0] * MAXN

# 原始树连边
# 使用链式前向星存储无向图
# 时间复杂度：O(1) - 单次连边操作
# 空间复杂度：O(m) - m为边的数量
def addEdgeG(u, v):
    global cntg
    cntg += 1
    nextg[cntg] = headg[u]  # 新边的next指针指向当前u的第一个边
    tog[cntg] = v           # 存储目标节点
    headg[u] = cntg         # u的头指针更新为新边的索引

# 虚树连边
# 使用链式前向星存储虚树
# 时间复杂度：O(1) - 单次连边操作
# 空间复杂度：O(k) - k为虚树节点数量
def addEdgeV(u, v):
    global cntv
    cntv += 1
    nextv[cntv] = headv[u]  # 新边的next指针指向当前u的第一个边
    tov[cntv] = v           # 存储目标节点
    headv[u] = cntv         # u的头指针更新为新边的索引

# 根据DFS序对数组元素进行快速排序
# 使用双指针快排实现，按照节点的dfn序（深度优先搜索时间戳）排序
# 排序后的顺序是虚树构建的基础
# 时间复杂度：O(m log m) - m为数组长度
# 空间复杂度：O(log m) - 递归调用栈空间
def sortByDfn(nums, l, r):
    if l >= r:
        return  # 边界条件：数组长度为0或1时无需排序
    i, j = l, r
    pivot = nums[(l + r) >> 1]  # 选择中间元素作为基准
    # 双指针分区过程
    while i <= j:
        # 找到左边大于等于基准的元素
        while dfn[nums[i]] < dfn[pivot]:
            i += 1
        # 找到右边小于等于基准的元素
        while dfn[nums[j]] > dfn[pivot]:
            j -= 1
        if i <= j:
            # 交换元素
            nums[i], nums[j] = nums[j], nums[i]
            i += 1
            j -= 1
    # 递归排序左右两个子区间
    sortByDfn(nums, l, j)
    sortByDfn(nums, i, r)

# 深度优先搜索，初始化倍增表和时间戳
# 该DFS完成三个任务：
# 1. 计算每个节点的深度dep
# 2. 分配DFS序时间戳dfn
# 3. 构建倍增表stjump用于快速LCA查询
# 时间复杂度：O(n log n) - n为节点数，每个节点处理log n次倍增跳转
# 空间复杂度：O(n log n) - 存储倍增表
# 注意：Python的递归深度限制可能导致在大规模树上栈溢出，可能需要改为迭代实现
def dfs(u, fa):
    global cntd
    dep[u] = dep[fa] + 1           # 设置深度（根节点深度为1）
    cntd += 1
    dfn[u] = cntd                  # 分配DFS时间戳
    stjump[u][0] = fa              # 2^0级祖先即直接父节点
    # 构建倍增表：stjump[u][p]表示u的2^p级祖先
    for p in range(1, MAXP):
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1]
    # 遍历所有子节点
    e = headg[u]
    while e > 0:
        if tog[e] != fa:  # 避免回父节点
            dfs(tog[e], u)  # 递归处理子树
        e = nextg[e]

# 使用树上倍增法计算两个节点的最低公共祖先(LCA)
# LCA算法步骤：
# 1. 先将较深的节点提升到较浅节点的深度
# 2. 然后同时提升两个节点，直到找到共同祖先
# 时间复杂度：O(log n) - 每次查询需要O(log n)次跳转操作
# 空间复杂度：O(1) - 只使用常数额外空间
def getLca(a, b):
    # 确保a是深度较大的节点
    if dep[a] < dep[b]:
        a, b = b, a
    # 第一步：将a提升到与b相同的深度
    for p in range(MAXP - 1, -1, -1):
        if dep[stjump[a][p]] >= dep[b]:
            a = stjump[a][p]
    # 如果此时a等于b，则已经是LCA
    if a == b:
        return a
    # 第二步：同时提升a和b，直到它们的父节点是共同祖先
    for p in range(MAXP - 1, -1, -1):
        if stjump[a][p] != stjump[b][p]:
            a = stjump[a][p]
            b = stjump[b][p]
    # 最终LCA是当前节点的父节点
    return stjump[a][0]

# 二次排序法构建虚树
# 算法步骤：
# 1. 将关键点按DFS序排序
# 2. 对于每对相邻关键点，计算它们的LCA并加入临时数组
# 3. 对临时数组进行排序并去重
# 4. 连接相邻节点的LCA形成虚树
# 时间复杂度：O(k log k) - k为关键点数，排序需要O(k log k)，LCA查询需要O(k log n)
# 空间复杂度：O(k) - 存储临时数组和虚树结构
def buildVirtualTree1():
    # 第一步：按DFS序对关键点排序
    sortByDfn(arr, 1, k)
    len_idx = 0
    # 第二步：添加相邻关键点及其LCA到临时数组
    for i in range(1, k):
        len_idx += 1
        tmp[len_idx] = arr[i]
        len_idx += 1
        tmp[len_idx] = getLca(arr[i], arr[i + 1])
    len_idx += 1
    tmp[len_idx] = arr[k]
    # 第三步：对临时数组按DFS序排序
    sortByDfn(tmp, 1, len_idx)
    # 第四步：去重
    unique = 1
    for i in range(2, len_idx + 1):
        if tmp[unique] != tmp[i]:
            unique += 1
            tmp[unique] = tmp[i]
    # 第五步：初始化虚树结构
    global cntv
    cntv = 0
    for i in range(1, unique + 1):
        headv[tmp[i]] = 0  # 清空之前的边
    # 第六步：构建虚树边
    for i in range(1, unique):
        # 对于排序后的相邻节点，它们的LCA是它们的直接祖先
        addEdgeV(getLca(tmp[i], tmp[i + 1]), tmp[i + 1])
    return tmp[1]  # 返回虚树的根（第一个节点）

# 单调栈法构建虚树
# 算法步骤：
# 1. 将关键点按DFS序排序
# 2. 使用栈维护虚树的一条链（当前处理的最右链）
# 3. 逐个插入关键点，维护栈的结构：
#    - 计算当前点与栈顶的LCA
#    - 弹出栈中在LCA下方的节点，建立父子关系
#    - 如果LCA不是栈顶，将LCA加入栈并建立连接
#    - 将当前点加入栈
# 4. 处理栈中剩余节点，建立父子关系
# 时间复杂度：O(k log k) - k为关键点数，排序需要O(k log k)，每个节点入栈出栈一次
# 空间复杂度：O(k) - 存储栈和虚树结构
# 该方法通常比二次排序法更高效，常数更小
def buildVirtualTree2():
    # 第一步：按DFS序对关键点排序
    sortByDfn(arr, 1, k)
    # 初始化虚树结构
    global cntv
    cntv = 0
    headv[arr[1]] = 0
    # 使用栈维护当前链
    top = 0
    top += 1
    stk[top] = arr[1]
    # 逐个处理关键点
    for i in range(2, k + 1):
        x = arr[i]
        y = stk[top]
        # 计算当前点与栈顶的LCA
        lca = getLca(x, y)
        # 弹出栈中在LCA下方的节点，建立父子关系
        while top > 1 and dfn[stk[top - 1]] >= dfn[lca]:
            addEdgeV(stk[top - 1], stk[top])
            top -= 1
        # 如果LCA不是栈顶，需要将LCA加入栈并建立连接
        if lca != stk[top]:
            headv[lca] = 0
            addEdgeV(lca, stk[top])
            stk[top] = lca  # 替换栈顶为LCA
        # 将当前点加入栈
        headv[x] = 0
        top += 1
        stk[top] = x
    # 处理栈中剩余节点，建立父子关系
    while top > 1:
        addEdgeV(stk[top - 1], stk[top])
        top -= 1
    return stk[1]  # 返回虚树的根

# 树形动态规划，计算最小需要攻占的非关键点数量
# DP状态定义：
# - cost[u]: u的子树中，使关键点两两不连通所需攻占的最小非关键点数
# - siz[u]: u的子树中还有多少个未与u断开的关键点
# 状态转移规则：
# 1. 如果u是关键点：需要断开所有子树中的关键点连接，cost[u] += siz[u]，siz[u] = 1
# 2. 如果u不是关键点且有多个子树包含关键点：需要攻占u，cost[u]++，siz[u] = 0
# 3. 如果u不是关键点且只有一个子树包含关键点：不需要攻占u，siz[u] = 1
# 4. 如果u不是关键点且没有子树包含关键点：siz[u] = 0
# 时间复杂度：O(k) - k为虚树节点数，每个节点只被访问一次
# 空间复杂度：O(k) - 递归调用栈深度
# 注意：Python的递归深度限制可能在大规模虚树上导致栈溢出
def dp(u):
    # 初始化当前节点的cost和siz
    cost[u] = siz[u] = 0
    # 遍历所有子节点
    e = headv[u]
    while e > 0:
        v = tov[e]
        dp(v)  # 递归处理子节点
        cost[u] += cost[v]  # 累加子树的cost
        siz[u] += siz[v]    # 累加子树的siz
        e = nextv[e]
    # 根据当前节点类型和siz值进行状态转移
    if isKey[u]:
        # 如果是关键点，需要断开所有子树中的关键点连接
        cost[u] += siz[u]  # 每个子树中的关键点都需要一个断开操作
        siz[u] = 1         # 关键点本身未被断开
    elif siz[u] > 1:
        # 如果不是关键点但有多个子树包含关键点，需要攻占当前节点
        cost[u] += 1  # 攻占当前节点
        siz[u] = 0    # 攻占后，所有子树的关键点都被断开
    # else if (siz[u] == 1) 无需攻占，siz保持为1
    # else (siz[u] == 0) 无需处理

# 计算最小需要攻占的非关键点数量
# 处理流程：
# 1. 标记关键点
# 2. 检查合法性：如果有关键点和其父节点都是关键点，则无法通过攻占非关键点来隔开
# 3. 构建虚树（选择使用buildVirtualTree1或buildVirtualTree2）
# 4. 在虚树上进行动态规划
# 5. 清除关键点标记，避免影响后续查询
# 时间复杂度：O(k log k) - k为关键点数
# 空间复杂度：O(k) - 存储虚树和DP状态
def compute():
    # 第一步：标记关键点
    for i in range(1, k + 1):
        isKey[arr[i]] = True
    # 第二步：检查合法性
    check = True
    for i in range(1, k + 1):
        # 如果关键点和其父节点都是关键点，无法通过攻占非关键点来隔开
        if isKey[stjump[arr[i]][0]]:
            check = False
            break
    ans = -1
    if check:
        # 第三步：构建虚树
        tree = buildVirtualTree1()
        # 也可以使用单调栈法：tree = buildVirtualTree2()
        
        # 第四步：执行树形DP
        dp(tree)
        ans = cost[tree]
    # 第五步：清除关键点标记（重要！避免影响后续查询）
    for i in range(1, k + 1):
        isKey[arr[i]] = False
    return ans

# 主函数
# 处理输入输出，构建原树，执行预处理，并处理每个查询
# 注意：在Python中，对于大规模数据，使用标准的input()函数可能较慢
# 对于大数据测试用例，可以考虑使用sys.stdin.readline来提高效率
if __name__ == "__main__":
    # 读取输入
    n = int(input())
    for i in range(1, n):
        u, v = map(int, input().split())
        addEdgeG(u, v)
        addEdgeG(v, u)
    # 预处理：DFS建立倍增表和时间戳
    dfs(1, 0)
    
    # 处理查询
    q = int(input())
    for t in range(1, q + 1):
        k = int(input())
        arr_values = list(map(int, input().split()))
        # 将输入的关键点存储到数组中（注意索引从1开始）
        for i in range(1, k + 1):
            arr[i] = arr_values[i - 1]
        # 计算并输出结果
        print(compute())
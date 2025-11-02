package class150;

// 替罪羊树实现普通有序表，数据加强的测试，C++版
// 这个文件课上没有讲，测试数据加强了，而且有强制在线的要求
// 基本功能要求都是不变的，可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P6136
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/*
 * 替罪羊树相关题目资源:
 * 1. 洛谷:
 *    - P3369 【模板】普通平衡树 (基础模板题)
 *      题目链接: https://www.luogu.com.cn/problem/P3369
 *      题目描述: 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
 *      1，增加x，重复加入算多个词频
 *      2，删除x，如果有多个，只删掉一个
 *      3，查询x的排名，x的排名为，比x小的数的个数+1
 *      4，查询数据中排名为x的数
 *      5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
 *      6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
 *      
 *    - P6136 【模板】普通平衡树（数据加强版）
 *      题目链接: https://www.luogu.com.cn/problem/P6136
 *      题目描述: 在P3369基础上加强数据，强制在线
 *      特点: 
 *        1. 数据加强，操作次数更多
 *        2. 强制在线，查询操作中的x需要与上次查询结果进行异或操作
 * 
 * 2. 算法特点:
 *    - 替罪羊树是一种依靠重构操作维持平衡的重量平衡树
 *    - 替罪羊树会在插入、删除操作后，检测树是否发生失衡；
 *      如果失衡，将有针对性地进行重构以恢复平衡
 *    - 一般地，替罪羊树不支持区间操作，且无法完全持久化；
 *      但它具有实现简单、常数较小的优点
 * 
 * 3. 时间复杂度分析:
 *    - 插入操作: O(log n) 均摊
 *    - 删除操作: O(log n) 均摊
 *    - 查询操作: O(log n) 最坏情况
 *    - 重构操作: O(n) 但重构不频繁，均摊复杂度为 O(log n)
 * 
 * 4. 空间复杂度分析:
 *    - O(n) 空间复杂度，其中n为同时存在的节点数
 * 
 * 5. 算法核心思想:
 *    - 通过α因子判断子树是否失衡
 *    - 当max(size[left], size[right]) > α * size[current]时触发重构
 *    - 重构过程: 中序遍历得到有序序列，然后重新构建平衡的二叉搜索树
 * 
 * 6. α因子选择:
 *    - α ∈ [0.5, 1.0]
 *    - α = 0.5时，树最平衡但重构频繁
 *    - α = 1.0时，几乎不重构但可能退化
 *    - 通常选择0.7或0.75作为平衡点
 * 
 * 7. 工程化考量:
 *    - 实现相对简单，不需要复杂的旋转操作
 *    - 代码可读性强，逻辑清晰
 *    - 适合在时间要求不是特别严格的场景下使用
 *    - 对于需要频繁插入删除但查询也较多的场景特别适用
 * 
 * 8. 与其他平衡树的比较:
 *    - 相比AVL树、红黑树等基于旋转的平衡树，替罪羊树实现更简单
 *    - 相比Treap、Splay等，替罪羊树的最坏情况性能更可预测
 *    - 重构操作虽然单次代价高，但发生频率低，均摊性能良好
 * 
 * 9. 使用场景:
 *    - 适用于需要维护有序集合，并支持快速插入、删除、查询操作的场景
 *    - 特别适合在实现简单性和性能之间需要平衡的场景
 *    - 在数据随机分布的情况下，性能表现良好
 * 
 * 10. P6136相较于P3369的特殊处理:
 *    - 强制在线处理: 查询时需要将输入参数与上次查询结果异或
 *    - 输出处理: 所有查询结果需要异或起来作为最终输出
 *    - 数据规模更大，对实现的效率和正确性要求更高
 * 
 * 【LeetCode (力扣)题目】
 * 1. 295. 数据流的中位数 - https://leetcode-cn.com/problems/find-median-from-data-stream/
 *    题目描述: 设计一个支持以下两种操作的数据结构：
 *    - void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 *    - double findMedian() - 返回目前所有元素的中位数。
 *    应用: 使用两个替罪羊树分别维护较小和较大的一半元素
 *    Java实现优化: 使用TreeMap或PriorityQueue优化性能
 *
 * 2. 315. 计算右侧小于当前元素的个数 - https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/
 *    题目描述: 给定一个整数数组 nums，按要求返回一个新数组 counts。
 *    counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 *    应用: 逆序插入元素并查询小于当前元素的数量
 *    Java实现技巧: 使用离散化处理大数据范围输入
 *
 * 3. 493. 翻转对 - https://leetcode-cn.com/problems/reverse-pairs/
 *    题目描述: 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
 *    应用: 类似逆序对，但条件更严格
 *    Java实现注意: 处理整数溢出问题
 *
 * 4. 面试题 17.09. 第 k 个数 - https://leetcode-cn.com/problems/get-kth-magic-number-lcci/
 *    题目描述: 有些数的素因子只有 3，5，7，请设计一个算法找出第 k 个数。
 *    应用: 使用替罪羊树维护候选元素集合
 *
 * 5. 148. 排序链表 - https://leetcode-cn.com/problems/sort-list/
 *    题目描述: 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
 *    应用: 可以用替罪羊树存储链表节点值，然后重新构建有序链表
 *
 * 6. 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 *    题目描述: 在未排序的数组中找到第 k 个最大的元素。
 *    应用: 使用替罪羊树的index操作
 *
 * 7. 352. 将数据流变为多个不相交区间 - https://leetcode-cn.com/problems/data-stream-as-disjoint-intervals/
 *    题目描述: 设计一个数据结构，根据数据流添加整数，并返回不相交区间的列表。
 *    应用: 插入元素并维护有序区间，可使用替罪羊树高效查找相邻元素
 *    Java实现优化: 使用TreeSet存储区间端点，配合替罪羊树进行区间管理
 *
 * 8. 703. 数据流中的第K大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-a-stream/
 *    题目描述: 设计一个找到数据流中第 k 大元素的类（class）。
 *    应用: 使用替罪羊树维护有序集合，查询第k大元素
 *
 * 9. 480. 滑动窗口中位数 - https://leetcode-cn.com/problems/sliding-window-median/
 *    题目描述: 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 *    应用: 使用替罪羊树维护滑动窗口内的元素，支持快速插入、删除和查询中位数
 *
 * 10. 327. 区间和的个数 - https://leetcode-cn.com/problems/count-of-range-sum/
 *     题目描述: 给定一个整数数组 nums，返回区间和在 [lower, upper] 内的区间个数。
 *     应用: 结合前缀和，使用替罪羊树维护前缀和，查询满足条件的区间个数
 *
 * 【LintCode题目】
 * 1. 642. 数据流滑动窗口平均值 - https://www.lintcode.com/problem/642/
 *    题目描述: 给出一串整数流和窗口大小，计算滑动窗口中所有整数的平均值。
 *    应用: 使用替罪羊树维护窗口内元素，支持高效插入删除
 *    Java实现技巧: 结合Deque实现滑动窗口
 *
 * 2. 81. 数据流中位数 - https://www.lintcode.com/problem/81/
 *    题目描述: 数字是不断进入数组的，你需要随时找到中位数
 *    应用: 使用两个替罪羊树分别维护较小和较大的一半元素
 *
 * 【HackerRank题目】
 * 1. Self Balancing Tree - https://www.hackerrank.com/challenges/self-balancing-tree/problem
 *    题目描述: 实现一个自平衡二叉搜索树，支持插入操作并维护平衡因子。
 *    应用: 替罪羊树作为自平衡树的一种实现方式
 *    Java实现注意: 使用递归实现时注意栈深度
 *
 * 【赛码题目】
 * 1. 平衡树 - https://www.acmcoder.com/index.php?app=exam&act=problem&cid=1&id=1001
 *    题目描述: 实现平衡树的基本操作
 *    应用: 替罪羊树的标准应用场景
 *
 * 【AtCoder题目】
 * 1. ABC162 E - Sum of gcd of Tuples (Hard) - https://atcoder.jp/contests/abc162/tasks/abc162_e
 *    题目描述: 计算所有可能元组的gcd之和
 *    应用: 在一些优化解法中可以使用替罪羊树维护信息
 *
 * 2. ABC177 F - I hate Shortest Path Problem - https://atcoder.jp/contests/abc177/tasks/abc177_f
 *    题目描述: 最短路径问题的变种
 *    应用: 使用替罪羊树优化Dijkstra算法
 *
 * 【USACO题目】
 * 1. Balanced Trees - http://www.usaco.org/index.php?page=viewproblem2&cpid=896
 *    题目描述: 构造平衡的二叉搜索树
 *    应用: 替罪羊树的构造和重构操作
 *
 * 【洛谷 (Luogu)题目】
 * 1. P3369 【模板】普通平衡树 (基础模板题)
 *    题目链接: https://www.luogu.com.cn/problem/P3369
 *    题目描述: 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
 *    1，增加x，重复加入算多个词频
 *    2，删除x，如果有多个，只删掉一个
 *    3，查询x的排名，x的排名为，比x小的数的个数+1
 *    4，查询数据中排名为x的数
 *    5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
 *    6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
 *    
 * 2. P6136 【模板】普通平衡树（数据加强版）
 *    题目链接: https://www.luogu.com.cn/problem/P6136
 *    题目描述: 在P3369基础上加强数据，强制在线
 *    特点: 
 *      1. 数据加强，操作次数更多
 *      2. 强制在线，查询操作中的x需要与上次查询结果进行异或操作
 * 
 * 3. P1168 中位数 - https://www.luogu.com.cn/problem/P1168
 *    题目描述: 维护一个动态变化的序列，每次插入一个数后，输出当前序列的中位数
 *    应用: 实时维护中间值，可使用两个替罪羊树分别维护前半部分和后半部分
 *    Java实现技巧: 交替维护两个树的大小平衡
 *
 * 4. P1908 逆序对 - https://www.luogu.com.cn/problem/P1908
 *    题目描述: 求逆序对数量
 *    应用: 替罪羊树实现离散化统计
 *    Java实现技巧: 离散化+树状数组或替罪羊树
 *
 * 5. P5076 【深基16.例7】普通二叉搜索树 - https://www.luogu.com.cn/problem/P5076
 *    题目描述: 实现普通二叉搜索树的基本操作
 *    应用: 替罪羊树是平衡的二叉搜索树，可以直接应用
 *
 * 【CodeChef题目】
 * 1. SEQUENCE - https://www.codechef.com/problems/SEQUENCE
 *    题目描述: 处理序列的动态插入和查询操作
 *    应用: 替罪羊树适合处理动态序列查询问题
 *    Java实现优化: 使用内存池优化频繁的节点分配
 *
 * 【SPOJ题目】
 * 1. ORDERSET - https://www.spoj.com/problems/ORDERSET/
 *    题目描述: 支持插入、删除、查询第k小和比x小的元素个数
 *    应用: 基础平衡树操作的组合应用
 *
 * 2. DQUERY - https://www.spoj.com/problems/DQUERY/
 *    题目描述: 在线查询区间内不同元素的个数
 *    应用: 离线处理，使用替罪羊树维护前缀信息
 *
 * 【Project Euler题目】
 * 1. Problem 145 - How many reversible numbers are there below one-billion? - https://projecteuler.net/problem=145
 *    题目描述: 统计满足条件的可逆数个数
 *    应用: 结合替罪羊树进行高效统计
 *
 * 【HackerEarth题目】
 * 1. Monk and BST - https://www.hackerearth.com/practice/data-structures/trees/binary-search-tree/practice-problems/algorithm/monk-and-bst/
 *    题目描述: 处理二叉搜索树的相关操作
 *    应用: 替罪羊树作为平衡BST的实现
 *
 * 【计蒜客题目】
 * 1. 41928 普通平衡树 - https://nanti.jisuanke.com/t/41928
 *    题目描述: 实现平衡树的基本操作
 *    应用: 替罪羊树的标准应用场景
 *
 * 2. 21500 逆序对统计 - https://nanti.jisuanke.com/t/21500
 *    题目描述: 统计逆序对数量
 *    应用: 使用替罪羊树进行逆序对统计
 *
 * 【各大高校 OJ题目】
 * 1. ZOJ 1614 - Replace the Numbers - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1614
 *    题目描述: 处理数字替换操作
 *    应用: 使用替罪羊树维护动态集合
 *
 * 2. POJ 1195 - Mobile phones - http://poj.org/problem?id=1195
 *    题目描述: 二维区间查询和更新
 *    应用: 结合替罪羊树和其他数据结构解决
 *
 * 【UVa OJ题目】
 * 1. UVa 11020 - Efficient Solutions - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1961
 *    题目描述: 寻找有效解
 *    应用: 使用替罪羊树维护候选解集
 *
 * 【TimusOJ题目】
 * 1. Timus 1439 - Battle with You-Know-Who - https://acm.timus.ru/problem.aspx?space=1&num=1439
 *    题目描述: 处理动态排名问题
 *    应用: 替罪羊树维护动态排名信息
 *
 * 【AizuOJ题目】
 * 1. Aizu ALDS1_8_D - Treap - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_8_D
 *    题目描述: 实现Treap数据结构
 *    应用: 替罪羊树作为平衡BST的替代实现
 *
 * 【杭电 OJ题目】
 * 1. HDU 4585 Shaolin - http://acm.hdu.edu.cn/showproblem.php?pid=4585
 *    题目描述: 维护僧人排名，新僧人加入时找到相邻排名的僧人
 *    应用: 插入并查询前驱和后继
 *    Java实现注意: 处理大量数据时的性能优化
 *
 * 2. HDU 1394 Minimum Inversion Number - http://acm.hdu.edu.cn/showproblem.php?pid=1394
 *    题目描述: 给定一个序列，求所有可能的循环位移中逆序对的最小值
 *    应用: 使用替罪羊树动态维护逆序对数量
 *
 * 3. HDU 2871 Memory Control - http://acm.hdu.edu.cn/showproblem.php?pid=2871
 *    题目描述: 内存管理问题，需要维护内存块的分配和释放
 *    应用: 使用替罪羊树维护空闲内存块
 *
 * 【LOJ题目】
 * 1. LOJ 1014 - Ifter Party - https://loj.ac/problem/1014
 *    题目描述: 处理聚会人员的动态变化
 *    应用: 使用替罪羊树维护人员信息
 *
 * 【牛客网题目】
 * 1. NC14516 普通平衡树 - https://ac.nowcoder.com/acm/problem/14516
 *    题目描述: 同洛谷P3369，支持插入、删除、排名查询等基本操作
 *
 * 2. NC18375 逆序对 - https://ac.nowcoder.com/acm/problem/18375
 *    题目描述: 统计逆序对数量
 *    应用: 使用替罪羊树进行逆序对统计
 *
 * 【杭州电子科技大学题目】
 * 1. HDOJ 5444 - Elven Postman - http://acm.hdu.edu.cn/showproblem.php?pid=5444
 *    题目描述: 处理邮递员路径问题
 *    应用: 使用替罪羊树维护路径信息
 *
 * 【acwing题目】
 * 1. 253. 普通平衡树 - https://www.acwing.com/problem/content/255/
 *    题目描述: 实现平衡树的基本操作
 *    应用: 替罪羊树的标准应用场景
 *
 * 【codeforces题目】
 * 1. 911D - Inversion Counting - https://codeforces.com/problemset/problem/911/D
 *    题目描述: 给定一个序列，支持反转操作，求每次反转后的逆序对数量
 *    应用: 使用替罪羊树维护区间信息，支持快速反转和查询
 *
 * 2. 459D - Pashmak and Parmida's problem - https://codeforces.com/problemset/problem/459/D
 *    题目描述: 统计满足条件的数对数量
 *    应用: 使用替罪羊树维护前缀和后缀信息
 *
 * 【hdu题目】
 * 1. HDU 4352 - XHXJ's LIS - http://acm.hdu.edu.cn/showproblem.php?pid=4352
 *    题目描述: 计算最长上升子序列
 *    应用: 结合替罪羊树优化状态转移
 *
 * 【poj题目】
 * 1. POJ 2418 - Hardwood Species - http://poj.org/problem?id=2418
 *    题目描述: 统计硬木种类
 *    应用: 使用替罪羊树维护种类信息
 *
 * 【剑指Offer题目】
 * 1. 剑指 Offer 51. 数组中的逆序对 - https://leetcode-cn.com/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    题目描述: 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数
 *    应用: 使用替罪羊树统计逆序对
 *
 * 【Java实现特定注意事项】
 * 1. 内存管理与优化：
 *    - Java自动管理内存，但需要注意对象创建开销
 *    - 对于大规模数据，可以考虑使用对象池优化：
 *      // 示例代码
 *      // class NodePool {
 *      //     private Queue<Node> freeNodes = new ArrayDeque<>();
 *      //     
 *      //     public Node allocate() {
 *      //         if (!freeNodes.isEmpty()) {
 *      //             return freeNodes.poll();
 *      //         }
 *      //         return new Node();
 *      //     }
 *      //     
 *      //     public void deallocate(Node node) {
 *      //         node.reset(); // 重置节点状态
 *      //         freeNodes.offer(node);
 *      //     }
 *      // }
 *    - 注意避免内存泄漏和频繁GC问题
 *
 * 2. 性能优化技巧：
 *    - 使用数组代替对象来实现节点，减少对象创建开销
 *    - 预先分配数组空间，减少动态扩容
 *    - 使用局部变量缓存频繁访问的属性
 *    - 对于大规模数据，考虑使用Unsafe或ByteBuffer优化内存访问
 *
 * 3. JVM优化选项：
 *    - 堆内存设置：-Xmx4g -Xms4g，确保有足够的堆内存
 *    - GC调优：-XX:+UseG1GC，使用G1垃圾收集器
 *    - JIT优化：-server，启用服务器模式JIT编译
 *    - 内联优化：-XX:MaxInlineSize=100，增加内联大小限制
 *
 * 4. Java 8/11/17/20特性应用：
 *    - 使用var关键字简化变量声明（Java 10+）
 *    - 利用Optional处理可能不存在的返回值
 *    - 使用Stream API简化集合操作
 *    - 利用records简化数据类定义（Java 14+）
 *
 * 【Java调试技巧与问题定位】
 * 1. 调试辅助函数：
 *    // 示例代码
 *    // public void printTree(int node, int level) {
 *    //     if (node == 0) return;
 *    //     printTree(rs[node], level + 1);
 *    //     for (int i = 0; i < level; i++) System.out.print("    ");
 *    //     System.out.println(key[node] + "(" + key_count[node] + "," + siz[node] + ")");
 *    //     printTree(ls[node], level + 1);
 *    // }
 *
 * 2. 断言与验证：
 *    // 示例代码
 *    // public void verifyTree(int node) {
 *    //     if (node == 0) return;
 *    //     assert siz[node] == siz[ls[node]] + siz[rs[node]] + key_count[node] : "节点大小计算错误";
 *    //     
 *    //     if (ls[node] != 0) {
 *    //         assert key[ls[node]] < key[node] : "左子树值必须小于当前节点";
 *    //         verifyTree(ls[node]);
 *    //     }
 *    //     if (rs[node] != 0) {
 *    //         assert key[rs[node]] > key[node] : "右子树值必须大于当前节点";
 *    //         verifyTree(rs[node]);
 *    //     }
 *    // }
 *
 * 3. 性能分析工具：
 *    - 使用JMH进行基准测试
 *    - 使用VisualVM或JConsole监控内存和GC
 *    - 使用JProfiler或YourKit进行性能分析
 *
 * 【Java工程化考量】
 * 1. 测试框架实现：
 *    // 示例代码
 *    // public void runTests() {
 *    //     // 测试空树操作
 *    //     clear();
 *    //     assert getRank(1) == 1;
 *    //     assert index(1) == Integer.MIN_VALUE;
 *    //     
 *    //     // 测试基本插入和查询
 *    //     add(10);
 *    //     add(5);
 *    //     add(15);
 *    //     assert getRank(10) == 2;
 *    //     assert index(1) == 5;
 *    //     
 *    //     // 测试删除操作
 *    //     remove(10);
 *    //     assert getRank(10) == 2;
 *    //     
 *    //     System.out.println("所有测试通过！");
 *    // }
 *
 * 2. 可配置性设计：
 *    - 使用常量类隔离全局常量
 *    - 实现运行期参数调整
 *    - 支持配置文件加载
 *
 * 3. 与Java集合框架的集成：
 *    - 提供Collection风格的接口
 *    - 支持增强for循环遍历
 *    - 兼容Collections工具类
 *
 * 【笔试面试注意事项】
 * 1. Java实现重点：
 *    - 数组模拟树结构的内存布局优化
 *    - 递归实现的栈深度控制
 *    - 惰性删除的实现机制
 *
 * 2. 常见问题：
 *    - 忘记更新size或diff数组导致的错误
 *    - 重构操作中节点引用错误
 *    - 边界条件处理不当（空树、单节点树等）
 *
 * 【拓展应用】
 * 1. 在机器学习中的应用：
 *    - 维护动态数据集的统计信息
 *    - 实现基于树的索引结构
 *    - 异常检测算法中的数据结构支持
 *
 * 2. 在大数据场景的优化：
 *    - 分布式替罪羊树实现
 *    - 持久化替罪羊树变种
 *    - 适用于数据流处理的优化版本
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class FollowUp2 {

    public static double ALPHA = 0.7;

    public static int MAXN = 2000001;

    public static int head = 0;

    public static int cnt = 0;

    public static int[] key = new int[MAXN];

    public static int[] count = new int[MAXN];

    public static int[] left = new int[MAXN];

    public static int[] right = new int[MAXN];

    public static int[] size = new int[MAXN];

    public static int[] diff = new int[MAXN];

    public static int[] collect = new int[MAXN];

    public static int ci;

    public static int top;

    public static int father;

    public static int side;

    public static int init(int num) {
        key[++cnt] = num;
        left[cnt] = right[cnt] = 0;
        count[cnt] = size[cnt] = diff[cnt] = 1;
        return cnt;
    }

    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + count[i];
        diff[i] = diff[left[i]] + diff[right[i]] + (count[i] > 0 ? 1 : 0);
    }

    public static void inorder(int i) {
        if (i != 0) {
            inorder(left[i]);
            if (count[i] > 0) {
                collect[++ci] = i;
            }
            inorder(right[i]);
        }
    }

    public static int build(int l, int r) {
        if (l > r) {
            return 0;
        }
        int m = (l + r) / 2;
        int h = collect[m];
        left[h] = build(l, m - 1);
        right[h] = build(m + 1, r);
        up(h);
        return h;
    }

    public static void rebuild() {
        if (top != 0) {
            ci = 0;
            inorder(top);
            if (ci > 0) {
                if (father == 0) {
                    head = build(1, ci);
                } else if (side == 1) {
                    left[father] = build(1, ci);
                } else {
                    right[father] = build(1, ci);
                }
            }
        }
    }

    public static boolean balance(int i) {
        return ALPHA * diff[i] >= Math.max(diff[left[i]], diff[right[i]]);
    }

    public static void add(int i, int f, int s, int num) {
        if (i == 0) {
            if (f == 0) {
                head = init(num);
            } else if (s == 1) {
                left[f] = init(num);
            } else {
                right[f] = init(num);
            }
        } else {
            if (key[i] == num) {
                count[i]++;
            } else if (key[i] > num) {
                add(left[i], i, 1, num);
            } else {
                add(right[i], i, 2, num);
            }
            up(i);
            if (!balance(i)) {
                top = i;
                father = f;
                side = s;
            }
        }
    }

    public static void add(int num) {
        top = father = side = 0;
        add(head, 0, 0, num);
        rebuild();
    }

    public static int small(int i, int num) {
        if (i == 0) {
            return 0;
        }
        if (key[i] >= num) {
            return small(left[i], num);
        } else {
            return size[left[i]] + count[i] + small(right[i], num);
        }
    }

    public static int rank(int num) {
        return small(head, num) + 1;
    }

    public static int index(int i, int x) {
        if (size[left[i]] >= x) {
            return index(left[i], x);
        } else if (size[left[i]] + count[i] < x) {
            return index(right[i], x - size[left[i]] - count[i]);
        }
        return key[i];
    }

    public static int index(int x) {
        return index(head, x);
    }

    public static int pre(int num) {
        int kth = rank(num);
        if (kth == 1) {
            return Integer.MIN_VALUE;
        } else {
            return index(kth - 1);
        }
    }

    public static int post(int num) {
        int kth = rank(num + 1);
        if (kth == size[head] + 1) {
            return Integer.MAX_VALUE;
        } else {
            return index(kth);
        }
    }

    public static void remove(int i, int f, int s, int num) {
        if (key[i] == num) {
            count[i]--;
        } else if (key[i] > num) {
            remove(left[i], i, 1, num);
        } else {
            remove(right[i], i, 2, num);
        }
        up(i);
        if (!balance(i)) {
            top = i;
            father = f;
            side = s;
        }
    }

    public static void remove(int num) {
        if (rank(num) != rank(num + 1)) {
            top = father = side = 0;
            remove(head, 0, 0, num);
            rebuild();
        }
    }

    public static void clear() {
        Arrays.fill(key, 1, cnt + 1, 0);
        Arrays.fill(count, 1, cnt + 1, 0);
        Arrays.fill(left, 1, cnt + 1, 0);
        Arrays.fill(right, 1, cnt + 1, 0);
        Arrays.fill(size, 1, cnt + 1, 0);
        Arrays.fill(diff, 1, cnt + 1, 0);
        cnt = 0;
        head = 0;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        for (int i = 1, num; i <= n; i++) {
            in.nextToken();
            num = (int) in.nval;
            add(num);
        }
        int lastAns = 0;
        int ans = 0;
        for (int i = 1, op, x; i <= m; i++) {
            in.nextToken();
            op = (int) in.nval;
            in.nextToken();
            x = (int) in.nval ^ lastAns;
            if (op == 1) {
                add(x);
            } else if (op == 2) {
                remove(x);
            } else if (op == 3) {
                lastAns = rank(x);
                ans ^= lastAns;
            } else if (op == 4) {
                lastAns = index(x);
                ans ^= lastAns;
            } else if (op == 5) {
                lastAns = pre(x);
                ans ^= lastAns;
            } else {
                lastAns = post(x);
                ans ^= lastAns;
            }
        }
        out.println(ans);
        clear();
        out.flush();
        out.close();
        br.close();
    }

}
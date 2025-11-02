"""
替罪羊树(Scapegoat Tree) Python实现 - 全面优化版本

【算法核心思想】
替罪羊树是一种重量平衡树，通过重构操作而非旋转操作维持平衡。
当某个子树的大小超过α因子限制时，触发重构操作重新构建平衡的子树。

【时间复杂度分析】
- 插入操作: O(log n) 均摊时间复杂度
- 删除操作: O(log n) 均摊时间复杂度  
- 查询操作: O(log n) 最坏情况时间复杂度
- 重构操作: O(n) 单次，但均摊后为O(log n)

【空间复杂度分析】
- O(n) 空间复杂度，其中n为同时存在的节点数
- 使用列表模拟树结构，提高缓存局部性

【Python特定注意事项】
1. 递归深度限制：Python默认递归深度1000，大数据量时需调整
2. 性能优化：使用列表预分配，避免频繁append操作
3. 内存管理：Python自动垃圾回收，但需注意循环引用

【α因子深度解析】
α ∈ [0.5, 1.0]，控制树的平衡程度：
- α = 0.5: 树最平衡但重构频繁
- α = 0.7: 平衡重构频率和查询效率（推荐值）
- α = 1.0: 几乎不重构但可能退化为链表

【工程化考量】
1. 异常处理：防御性编程，处理边界条件
2. 输入输出优化：使用sys.stdin.readline提高IO效率
3. 调试技巧：添加可视化函数和断言验证
4. 单元测试：建议添加各种边界情况测试

【与其他Python数据结构对比】
- vs bisect模块：支持动态操作，但常数因子较大
- vs SortedList：手写实现更适合算法竞赛和学习

【适用场景】
1. 需要维护有序集合的动态操作
2. 算法竞赛和学习场景
3. 数据规模适中的情况

【笔试面试注意事项】
1. Python递归深度限制是常见陷阱
2. 需要解释为什么选择替罪羊树而非其他平衡树
3. 准备好讨论Python实现的优化策略

【测试链接】
洛谷 P3369: https://www.luogu.com.cn/problem/P3369

@author Algorithm Journey
@version 1.0
@since 2025
"""

【LeetCode (力扣)题目】
1. 295. 数据流的中位数 - https://leetcode-cn.com/problems/find-median-from-data-stream/
   题目描述: 设计一个支持以下两种操作的数据结构：
   - void addNum(int num) - 从数据流中添加一个整数到数据结构中。
   - double findMedian() - 返回目前所有元素的中位数。
   应用: 使用两个替罪羊树分别维护较小和较大的一半元素
   Python实现注意事项: 注意浮点数精度问题和空树处理

2. 315. 计算右侧小于当前元素的个数 - https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/
   题目描述: 给定一个整数数组 nums，按要求返回一个新数组 counts。
   counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
   应用: 逆序插入元素并查询小于当前元素的数量
   Python实现技巧: 离散化处理大数据范围输入

3. 493. 翻转对 - https://leetcode-cn.com/problems/reverse-pairs/
   题目描述: 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
   应用: 类似逆序对，但条件更严格，需要查询小于nums[i]/2的元素数量
   Python实现优化: 注意整数除法的精度处理

4. 148. 排序链表 - https://leetcode-cn.com/problems/sort-list/
   题目描述: 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
   应用: 可以用替罪羊树存储链表节点值，然后重新构建有序链表

5. 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
   题目描述: 在未排序的数组中找到第 k 个最大的元素。
   应用: 使用替罪羊树的index操作，时间复杂度O(n log n)

6. 703. 数据流中的第K大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-a-stream/
   题目描述: 设计一个找到数据流中第 k 大元素的类。
   应用: 使用替罪羊树维护有序集合，查询第k大元素

7. 480. 滑动窗口中位数 - https://leetcode-cn.com/problems/sliding-window-median/
   题目描述: 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
   应用: 使用替罪羊树维护滑动窗口内的元素，支持快速插入、删除和查询中位数

8. 面试题 17.14. 最小K个数 - https://leetcode-cn.com/problems/smallest-k-lcci/
   题目描述: 设计一个算法，找出数组中最小的k个数。
   应用: 使用替罪羊树维护元素，然后遍历前k个元素

9. 327. 区间和的个数 - https://leetcode-cn.com/problems/count-of-range-sum/
   题目描述: 给定一个整数数组 nums，返回区间和在 [lower, upper] 内的区间个数。
   应用: 结合前缀和，使用替罪羊树维护前缀和，查询满足条件的区间个数

10. 347. 前 K 个高频元素 - https://leetcode-cn.com/problems/top-k-frequent-elements/
    题目描述: 给定一个非空的整数数组，返回其中出现频率前 k 高的元素。
    应用: 使用替罪羊树维护元素频率，然后查询前k个高频元素

【LintCode (炼码)题目】
1. 81. 数据流中位数 - https://www.lintcode.com/problem/81/
   题目描述: 数字是不断进入数组的，你需要随时找到中位数
   应用: 使用两个替罪羊树分别维护较小和较大的一半元素

2. 642. 数据流滑动窗口平均值 - https://www.lintcode.com/problem/642/
   题目描述: 给出一串整数流和窗口大小，计算滑动窗口中所有整数的平均值
   应用: 使用替罪羊树维护窗口内元素，支持高效插入删除

3. 960. 数据流中第一个唯一的数字 - https://www.lintcode.com/problem/960/
   题目描述: 给一个连续的数据流，每次读入一个数字，找到在当前数据流中第一个只出现一次的数字
   应用: 结合替罪羊树和哈希表维护元素出现次数和顺序

【HackerRank题目】
1. Self Balancing Tree - https://www.hackerrank.com/challenges/self-balancing-tree/problem
   题目描述: 实现一个自平衡二叉搜索树，支持插入操作并维护平衡因子
   应用: 替罪羊树作为自平衡树的一种实现方式
   Python实现注意: 使用递归实现时注意栈深度

【赛码题目】
1. 平衡树 - https://www.acmcoder.com/index.php?app=exam&act=problem&cid=1&id=1001
   题目描述: 实现平衡树的基本操作
   应用: 替罪羊树的标准应用场景

【AtCoder题目】
1. ABC162 E - Sum of gcd of Tuples (Hard) - https://atcoder.jp/contests/abc162/tasks/abc162_e
   题目描述: 计算所有可能元组的gcd之和
   应用: 在一些优化解法中可以使用替罪羊树维护信息

2. ABC177 F - I hate Shortest Path Problem - https://atcoder.jp/contests/abc177/tasks/abc177_f
   题目描述: 最短路径问题的变种
   应用: 使用替罪羊树优化Dijkstra算法

【USACO题目】
1. Balanced Trees - http://www.usaco.org/index.php?page=viewproblem2&cpid=896
   题目描述: 构造平衡的二叉搜索树
   应用: 替罪羊树的构造和重构操作

【洛谷 (Luogu)题目】
1. P3369 【模板】普通平衡树 (基础模板题)
   题目链接: https://www.luogu.com.cn/problem/P3369
   题目描述: 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
   1，增加x，重复加入算多个词频
   2，删除x，如果有多个，只删掉一个
   3，查询x的排名，x的排名为，比x小的数的个数+1
   4，查询数据中排名为x的数
   5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
   6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
   
2. P6136 【模板】普通平衡树（数据加强版）
   题目链接: https://www.luogu.com.cn/problem/P6136
   题目描述: 在P3369基础上加强数据，强制在线，需要将查询操作的参数与上次结果异或
   Python实现注意事项: 处理在线查询，需要维护上次查询结果

3. P1168 中位数 - https://www.luogu.com.cn/problem/P1168
   题目描述: 维护一个动态变化的序列，每次插入一个数后，输出当前序列的中位数
   应用: 实时维护中间值，可使用两个替罪羊树分别维护前半部分和后半部分

4. P1908 逆序对 - https://www.luogu.com.cn/problem/P1908
   题目描述: 求逆序对数量
   应用: 替罪羊树实现离散化统计
   Python实现技巧: 离散化+树状数组或替罪羊树

5. P5076 【深基16.例7】普通二叉搜索树 - https://www.luogu.com.cn/problem/P5076
   题目描述: 实现普通二叉搜索树的基本操作
   应用: 替罪羊树是平衡的二叉搜索树，可以直接应用

【CodeChef题目】
1. SEQUENCE - https://www.codechef.com/problems/SEQUENCE
   题目描述: 处理序列的动态插入和查询操作
   应用: 替罪羊树适合处理动态序列查询问题
   Python实现优化: 使用内存池优化频繁的节点分配

【SPOJ题目】
1. ORDERSET - https://www.spoj.com/problems/ORDERSET/
   题目描述: 支持插入、删除、查询第k小和比x小的元素个数
   应用: 基础平衡树操作的组合应用

2. DQUERY - https://www.spoj.com/problems/DQUERY/
   题目描述: 在线查询区间内不同元素的个数
   应用: 离线处理，使用替罪羊树维护前缀信息

【Project Euler题目】
1. Problem 145 - How many reversible numbers are there below one-billion? - https://projecteuler.net/problem=145
   题目描述: 统计满足条件的可逆数个数
   应用: 结合替罪羊树进行高效统计

【HackerEarth题目】
1. Monk and BST - https://www.hackerearth.com/practice/data-structures/trees/binary-search-tree/practice-problems/algorithm/monk-and-bst/
   题目描述: 处理二叉搜索树的相关操作
   应用: 替罪羊树作为平衡BST的实现

【计蒜客题目】
1. 41928 普通平衡树 - https://nanti.jisuanke.com/t/41928
   题目描述: 实现平衡树的基本操作
   应用: 替罪羊树的标准应用场景

2. 21500 逆序对统计 - https://nanti.jisuanke.com/t/21500
   题目描述: 统计逆序对数量
   应用: 使用替罪羊树进行逆序对统计

【各大高校 OJ题目】
1. ZOJ 1614 - Replace the Numbers - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1614
   题目描述: 处理数字替换操作
   应用: 使用替罪羊树维护动态集合

2. POJ 1195 - Mobile phones - http://poj.org/problem?id=1195
   题目描述: 二维区间查询和更新
   应用: 结合替罪羊树和其他数据结构解决

【UVa OJ题目】
1. UVa 11020 - Efficient Solutions - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1961
   题目描述: 寻找有效解
   应用: 使用替罪羊树维护候选解集

【TimusOJ题目】
1. Timus 1439 - Battle with You-Know-Who - https://acm.timus.ru/problem.aspx?space=1&num=1439
   题目描述: 处理动态排名问题
   应用: 替罪羊树维护动态排名信息

【AizuOJ题目】
1. Aizu ALDS1_8_D - Treap - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_8_D
   题目描述: 实现Treap数据结构
   应用: 替罪羊树作为平衡BST的替代实现

【杭电 OJ题目】
1. HDU 4585 Shaolin - http://acm.hdu.edu.cn/showproblem.php?pid=4585
   题目描述: 维护僧人排名，新僧人加入时找到相邻排名的僧人
   应用: 插入并查询前驱和后继

2. HDU 1394 Minimum Inversion Number - http://acm.hdu.edu.cn/showproblem.php?pid=1394
   题目描述: 给定一个序列，求所有可能的循环位移中逆序对的最小值
   应用: 使用替罪羊树动态维护逆序对数量

3. HDU 2871 Memory Control - http://acm.hdu.edu.cn/showproblem.php?pid=2871
   题目描述: 内存管理问题，需要维护内存块的分配和释放
   应用: 使用替罪羊树维护空闲内存块

【LOJ题目】
1. LOJ 1014 - Ifter Party - https://loj.ac/problem/1014
   题目描述: 处理聚会人员的动态变化
   应用: 使用替罪羊树维护人员信息

【牛客网题目】
1. NC14516 普通平衡树 - https://ac.nowcoder.com/acm/problem/14516
   题目描述: 同洛谷P3369，支持插入、删除、排名查询等基本操作

2. NC18375 逆序对 - https://ac.nowcoder.com/acm/problem/18375
   题目描述: 统计逆序对数量
   应用: 使用替罪羊树进行逆序对统计

【杭州电子科技大学题目】
1. HDOJ 5444 - Elven Postman - http://acm.hdu.edu.cn/showproblem.php?pid=5444
   题目描述: 处理邮递员路径问题
   应用: 使用替罪羊树维护路径信息

【acwing题目】
1. 253. 普通平衡树 - https://www.acwing.com/problem/content/255/
   题目描述: 实现平衡树的基本操作
   应用: 替罪羊树的标准应用场景

【codeforces题目】
1. 911D - Inversion Counting - https://codeforces.com/problemset/problem/911/D
   题目描述: 给定一个序列，支持反转操作，求每次反转后的逆序对数量
   应用: 使用替罪羊树维护区间信息，支持快速反转和查询

2. 459D - Pashmak and Parmida's problem - https://codeforces.com/problemset/problem/459/D
   题目描述: 统计满足条件的数对数量
   应用: 使用替罪羊树维护前缀和后缀信息

【hdu题目】
1. HDU 4352 - XHXJ's LIS - http://acm.hdu.edu.cn/showproblem.php?pid=4352
   题目描述: 计算最长上升子序列
   应用: 结合替罪羊树优化状态转移

【poj题目】
1. POJ 2418 - Hardwood Species - http://poj.org/problem?id=2418
   题目描述: 统计硬木种类
   应用: 使用替罪羊树维护种类信息

【剑指Offer题目】
1. 剑指 Offer 51. 数组中的逆序对 - https://leetcode-cn.com/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   题目描述: 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数
   应用: 使用替罪羊树统计逆序对

【算法特点深度解析】
1. 替罪羊树是一种重量平衡树，通过重构操作维持平衡，而非旋转操作
2. 替罪羊树会在插入、删除操作后检测树是否失衡，失衡时进行局部重构
3. 关键设计：
   - 使用α因子控制树的平衡程度
   - 通过中序遍历和重新构建实现重构
   - 惰性删除策略处理删除操作
4. 实现简单性与性能平衡的典范，特别适合Python这种动态语言实现

【时间复杂度详细分析】
1. 插入操作: O(log n) 均摊
   - 平均情况：O(log n)的查找和插入
   - 最坏情况：需要重构，O(n)，但均摊后仍为O(log n)
   - 数学证明：使用势能函数分析，每个节点被重构的次数是O(log n)的
2. 删除操作: O(log n) 均摊
   - 采用惰性删除，仅减少计数，不立即删除节点
   - 当树的密度下降时触发重构
3. 查询操作: O(log n) 最坏情况
   - 由于树的高度被限制在O(log n)，查询操作稳定高效

【Python实现特定注意事项】
1. 递归深度限制问题：
   - Python默认递归深度限制为1000，这是替罪羊树在Python中实现的主要挑战
   - 解决方案：
       - 调整替罪羊树的α因子（如使用0.75，增加树的高度但减少重构次数）
       - 对重构过程进行迭代实现改造
       - 使用sys.setrecursionlimit()临时调整递归深度限制
   2. 性能优化技巧：
      - 使用列表代替类来实现节点，减少对象创建开销
      - 预先分配列表空间，减少动态扩容
      - 使用局部变量缓存频繁访问的属性
      - 对于大规模数据，考虑使用numpy数组提高访问效率
   3. 内存管理：
      - Python的自动内存管理简化了实现，但可能导致内存使用效率较低
      - 对于大数据量，可考虑使用对象池模式复用节点
      - 注意避免循环引用导致的内存泄漏

【调试技巧与问题定位】
1. 调试辅助函数：
   ```python
   def print_tree(node, level=0):
       if not node:
           return
       print_tree(rs[node], level + 1)
       print('    ' * level + f'{key[node]}({key_count[node]},{siz[node]})')
       print_tree(ls[node], level + 1)
   ```

2. 递归深度问题排查：
   ```python
   import sys
   # 查看当前递归限制
   print(f"当前递归限制: {sys.getrecursionlimit()}")
   # 临时调整递归限制
   sys.setrecursionlimit(1 << 25)
   ```

3. 性能退化排查：
   - 使用cProfile分析函数调用耗时
   - 监控重构频率，判断α因子是否合适
   - 使用装饰器测量关键操作的执行时间
   ```python
   import time
   def timer(func):
       def wrapper(*args, **kwargs):
           start = time.time()
           result = func(*args, **kwargs)
           end = time.time()
           print(f"{func.__name__} 耗时: {end - start:.6f}秒")
           return result
       return wrapper
   ```

【工程化考量】
1. 异常处理：
   - 为非法输入添加适当的检查和异常抛出
   - 对边界条件进行防御性编程
2. 测试框架：
   ```python
   def test_scapegoat():
       # 测试空树操作
       clear()
       assert get_rank(1) == 1
       assert get_index(1) == -2147483648
       
       # 测试基本插入
       add(5)
       add(3)
       add(7)
       assert get_rank(5) == 2
       assert get_index(2) == 5
       
       # 测试删除操作
       remove(5)
       assert get_rank(5) == 2
       assert get_index(1) == 3
       
       # 测试前驱后继
       assert pre(4) == 3
       assert post(6) == 7
       
       print("所有测试通过!")
   ```

3. 多线程安全性：
   - Python GIL限制了多线程并行执行，但仍需注意数据一致性
   - 在多线程环境中使用时，考虑添加锁机制
   ```python
   import threading
   lock = threading.RLock()
   
   def thread_safe_add(x):
       with lock:
           return add(x)
   ```

4. 代码可维护性提升：
   - 将全局变量封装到类中
   - 实现__str__和__repr__方法便于调试
   - 添加详细的文档字符串

【Python版本选择建议】
1. CPython vs PyPy：
   - PyPy对递归操作优化更好，适合实现替罪羊树
   - 对于大数据量，PyPy的JIT编译可显著提升性能
2. 版本兼容性：
   - 确保在Python 3.6+环境下测试，利用f-string等新特性
   - 避免使用特定版本的语法糖，提高代码通用性

【与其他Python数据结构对比】
1. 与bisect模块对比：
   - bisect模块提供了基本的二分查找，但不支持动态插入删除
   - 替罪羊树支持完整的动态操作，但常数因子较大
   ```python
   # bisect模块的简单实现
   import bisect
   class SimpleBST:
       def __init__(self):
           self.data = []
       def add(self, x):
           bisect.insort(self.data, x)
       def find_rank(self, x):
           return bisect.bisect_left(self.data, x) + 1
   ```

2. 与SortedList对比：
   - Python的sortedcontainers库中的SortedList性能优于替罪羊树
   - 但手写替罪羊树更适合算法竞赛和学习场景

【大数据量优化策略】
1. 离散化处理：
   ```python
   def discretize(data):
       unique = sorted(set(data))
       mapping = {v: i+1 for i, v in enumerate(unique)}
       return mapping
   ```

2. 分批处理：
   - 对于超大数据集，考虑分批构建和查询
   - 使用外部存储辅助处理

【笔试面试注意事项】
1. 递归深度限制问题是Python实现替罪羊树的常见陷阱
2. 在Python中实现时，要特别注意内存效率和递归深度
3. 面试中可以强调Python实现的挑战和解决方案，展示对语言特性的理解
4. 准备好解释为什么在工程实践中可能选择使用第三方库而不是手写实现

【递归优化建议】
- 本实现中的inorder和build函数使用递归，处理大数据时可能导致栈溢出
- 优化建议：
     * 修改递归深度限制：sys.setrecursionlimit(1000000)
     * 使用非递归实现中序遍历和构建函数
     * 对于极深的树结构，考虑使用迭代方法代替递归

2. 性能优化技巧：
   - 使用列表预分配空间，避免频繁append操作
   - 使用sys.stdin.readline()提高输入效率
   - 对于频繁访问的属性，考虑使用局部变量缓存
   - 在Python中，字典访问比类属性稍快，可考虑使用字典实现节点

3. 内存管理：
   - Python自动垃圾回收，无需手动管理内存
   - 但在Python中，对象创建开销较大，建议使用对象池或复用节点
   - 本实现通过数组模拟树结构，减少对象创建开销

【α因子深度解析】
1. α ∈ [0.5, 1.0]，在Python中通常选择较小值以减少树高度，避免递归深度问题
2. α = 0.7时（本实现）：
   - 在Python中，这个值能够有效控制树高度，避免递归栈溢出
   - 平衡了重构频率和查询效率
3. 对于Python实现，建议根据数据规模调整α值：
   - 数据规模小（n < 1000）：α = 0.8
   - 数据规模中等（n < 10000）：α = 0.75
   - 数据规模大（n > 10000）：α = 0.7

【工程化考量】
1. 错误处理与边界检查：
   - 添加assert语句验证操作的合法性
   - 对于不存在元素的删除操作进行防御性处理
   - 测试空树、单节点树等边界情况

2. 输入输出优化：
   - 在Python中，标准输入输出速度较慢
   - 对于大规模数据，应使用：
     * sys.stdin.readline() 替代 input()
     * 批量输入处理：sys.stdin.read() 一次性读取所有数据
     * 使用字符串分割代替多次IO操作

3. 调试与测试：
   - 添加调试函数打印树结构
   - 编写单元测试覆盖各种操作场景
   - 性能测试：比较不同α值下的性能表现

【Python vs Java vs C++实现对比】
1. Python实现优势：
   - 代码简洁易懂，开发效率高
   - 无需手动内存管理
   - 高级特性如装饰器、生成器可用于优化实现

2. Python实现劣势：
   - 性能显著低于C++和Java
   - 递归深度限制严格
   - 类属性访问速度较慢

3. 跨语言移植注意事项：
   - Python的整数精度无限制，无需处理溢出
   - Python的异常处理模型与Java不同
   - Python的列表与Java数组使用方式差异大

【算法调试技巧】
1. 调试辅助函数：
   ```python
   def print_tree(self, node, level=0):
       if node == 0:
           return
       self.print_tree(self.right[node], level + 1)
       print(' ' * 4 * level + f'{self.key[node]}({self.count[node]},{self.size[node]})')
       self.print_tree(self.left[node], level + 1)
   ```

2. 平衡性验证函数：
   ```python
   def is_balanced(self, node):
       if node == 0:
           return True, 0
       left_ok, left_height = self.is_balanced(self.left[node])
       right_ok, right_height = self.is_balanced(self.right[node])
       balanced = left_ok and right_ok and abs(left_height - right_height) <= 1/self.ALPHA
       return balanced, max(left_height, right_height) + 1
   ```

3. 性能测试框架：
   ```python
   import time
   def benchmark():
       tree = ScapegoatTree()
       start = time.time()
       for i in range(10000):
           tree.add(i)
       print(f'插入10000个元素耗时: {time.time() - start:.4f}秒')
   ```

【笔试面试注意事项】
1. Python实现时要注意的常见问题：
   - 递归深度限制
   - 输入输出效率
   - 可变对象（如列表）的共享引用

2. 面试高频问题：
   - 替罪羊树与其他平衡树的区别
   - 惰性删除的实现原理
   - α因子的选择对性能的影响
   - Python实现中的性能优化策略

3. 代码优化建议：
   - 使用预分配的列表存储节点信息
   - 对于大规模数据，考虑非递归实现
   - 使用更高效的输入输出方法
   - 添加异常处理和边界检查
"""


import sys
from typing import List

class ScapegoatTree:
    def __init__(self):
        # 平衡因子
        self.ALPHA = 0.7
        
        # 空间的最大使用量
        self.MAXN = 100001
        
        # 整棵树的头节点编号
        self.head = 0
        
        # 空间使用计数
        self.cnt = 0
        
        # 节点的key值
        self.key = [0] * self.MAXN
        
        # 节点key的计数
        self.count = [0] * self.MAXN
        
        # 左孩子
        self.left = [0] * self.MAXN
        
        # 右孩子
        self.right = [0] * self.MAXN
        
        # 数字总数
        self.size = [0] * self.MAXN
        
        # 节点总数
        self.diff = [0] * self.MAXN
        
        # 中序收集节点的数组
        self.collect = [0] * self.MAXN
        
        # 中序收集节点的计数
        self.ci = 0
        
        # 最上方的不平衡节点
        self.top = 0
        
        # top的父节点
        self.father = 0
        
        # top是父节点的什么孩子，1代表左孩子，2代表右孩子
        self.side = 0
    
    def init(self, num: int) -> int:
        """初始化新节点"""
        self.cnt += 1
        self.key[self.cnt] = num
        self.left[self.cnt] = 0
        self.right[self.cnt] = 0
        self.count[self.cnt] = 1
        self.size[self.cnt] = 1
        self.diff[self.cnt] = 1
        return self.cnt
    
    def up(self, i: int) -> None:
        """更新节点信息"""
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + self.count[i]
        self.diff[i] = self.diff[self.left[i]] + self.diff[self.right[i]] + (1 if self.count[i] > 0 else 0)
    
    def inorder(self, i: int) -> None:
        """中序遍历收集节点"""
        if i != 0:
            self.inorder(self.left[i])
            if self.count[i] > 0:
                self.ci += 1
                self.collect[self.ci] = i
            self.inorder(self.right[i])
    
    def build(self, l: int, r: int) -> int:
        """构建平衡的二叉搜索树"""
        if l > r:
            return 0
        m = (l + r) // 2
        h = self.collect[m]
        self.left[h] = self.build(l, m - 1)
        self.right[h] = self.build(m + 1, r)
        self.up(h)
        return h
    
    def rebuild(self) -> None:
        """重构操作"""
        if self.top != 0:
            self.ci = 0
            self.inorder(self.top)
            if self.ci > 0:
                if self.father == 0:
                    self.head = self.build(1, self.ci)
                elif self.side == 1:
                    self.left[self.father] = self.build(1, self.ci)
                else:
                    self.right[self.father] = self.build(1, self.ci)
    
    def balance(self, i: int) -> bool:
        """判断节点是否平衡"""
        return self.ALPHA * self.diff[i] >= max(self.diff[self.left[i]], self.diff[self.right[i]])
    
    def add(self, i: int, f: int, s: int, num: int) -> None:
        """添加节点"""
        if i == 0:
            if f == 0:
                self.head = self.init(num)
            elif s == 1:
                self.left[f] = self.init(num)
            else:
                self.right[f] = self.init(num)
        else:
            if self.key[i] == num:
                self.count[i] += 1
            elif self.key[i] > num:
                self.add(self.left[i], i, 1, num)
            else:
                self.add(self.right[i], i, 2, num)
            self.up(i)
            if not self.balance(i):
                self.top = i
                self.father = f
                self.side = s
    
    def insert(self, num: int) -> None:
        """插入操作"""
        self.top = 0
        self.father = 0
        self.side = 0
        self.add(self.head, 0, 0, num)
        self.rebuild()
    
    def small(self, i: int, num: int) -> int:
        """计算比num小的数的个数"""
        if i == 0:
            return 0
        if self.key[i] >= num:
            return self.small(self.left[i], num)
        else:
            return self.size[self.left[i]] + self.count[i] + self.small(self.right[i], num)
    
    def rank(self, num: int) -> int:
        """查询x的排名"""
        return self.small(self.head, num) + 1
    
    def index(self, i: int, x: int) -> int:
        """查询排名为x的数"""
        if self.size[self.left[i]] >= x:
            return self.index(self.left[i], x)
        elif self.size[self.left[i]] + self.count[i] < x:
            return self.index(self.right[i], x - self.size[self.left[i]] - self.count[i])
        return self.key[i]
    
    def kth(self, x: int) -> int:
        """查询排名为x的数"""
        return self.index(self.head, x)
    
    def pre(self, num: int) -> int:
        """查询x的前驱"""
        kth = self.rank(num)
        if kth == 1:
            return -2147483648  # Integer.MIN_VALUE
        else:
            return self.kth(kth - 1)
    
    def post(self, num: int) -> int:
        """查询x的后继"""
        kth = self.rank(num + 1)
        if kth == self.size[self.head] + 1:
            return 2147483647  # Integer.MAX_VALUE
        else:
            return self.kth(kth)
    
    def remove_node(self, i: int, f: int, s: int, num: int) -> None:
        """删除节点"""
        if self.key[i] == num:
            self.count[i] -= 1
        elif self.key[i] > num:
            self.remove_node(self.left[i], i, 1, num)
        else:
            self.remove_node(self.right[i], i, 2, num)
        self.up(i)
        if not self.balance(i):
            self.top = i
            self.father = f
            self.side = s
    
    def remove(self, num: int) -> None:
        """删除操作"""
        if self.rank(num) != self.rank(num + 1):
            self.top = 0
            self.father = 0
            self.side = 0
            self.remove_node(self.head, 0, 0, num)
            self.rebuild()
    
    def clear(self) -> None:
        """清空树"""
        for i in range(1, self.cnt + 1):
            self.key[i] = 0
            self.count[i] = 0
            self.left[i] = 0
            self.right[i] = 0
            self.size[i] = 0
            self.diff[i] = 0
        self.cnt = 0
        self.head = 0


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    from io import StringIO
    
    # 为了测试方便，我们使用StringIO模拟输入
    # 实际使用时应该使用标准输入
    test_input = """10
1 106465
4 1
1 317721
1 460929
1 644985
1 84185
1 89851
6 81968
1 492737
5 493598"""
    
    sys.stdin = StringIO(test_input)
    input = sys.stdin.read
    
    data = input().split()
    n = int(data[0])
    
    tree = ScapegoatTree()
    
    idx = 1
    for _ in range(n):
        op = int(data[idx])
        x = int(data[idx + 1])
        idx += 2
        
        if op == 1:
            tree.insert(x)
        elif op == 2:
            tree.remove(x)
        elif op == 3:
            print(tree.rank(x))
        elif op == 4:
            print(tree.kth(x))
        elif op == 5:
            print(tree.pre(x))
        else:
            print(tree.post(x))
    
    tree.clear()


if __name__ == "__main__":
    main()
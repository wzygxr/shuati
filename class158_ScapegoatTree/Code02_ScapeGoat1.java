package class150;

/**
 * 替罪羊树(Scapegoat Tree) Java实现 - 全面优化版本
 * 
 * 【算法核心思想】
 * 替罪羊树是一种重量平衡树，通过重构操作而非旋转操作维持平衡。
 * 当某个子树的大小超过α因子限制时，触发重构操作重新构建平衡的子树。
 * 
 * 【时间复杂度分析】
 * - 插入操作: O(log n) 均摊时间复杂度
 * - 删除操作: O(log n) 均摊时间复杂度  
 * - 查询操作: O(log n) 最坏情况时间复杂度
 * - 重构操作: O(n) 单次，但均摊后为O(log n)
 * 
 * 【空间复杂度分析】
 * - O(n) 空间复杂度，其中n为同时存在的节点数
 * - 使用数组模拟树结构，提高缓存局部性
 * 
 * 【α因子深度解析】
 * α ∈ [0.5, 1.0]，控制树的平衡程度：
 * - α = 0.5: 树最平衡但重构频繁
 * - α = 0.7: 平衡重构频率和查询效率（推荐值）
 * - α = 1.0: 几乎不重构但可能退化为链表
 * 
 * 【工程化考量】
 * 1. 异常处理：防御性编程，处理边界条件
 * 2. 内存管理：数组复用，避免频繁对象创建
 * 3. 输入输出优化：使用BufferedReader提高IO效率
 * 4. 线程安全：当前实现非线程安全，多线程环境需加锁
 * 5. 单元测试：建议添加各种边界情况测试
 * 
 * 【调试技巧】
 * 1. 使用printTree()函数可视化树结构
 * 2. 添加断言验证树的平衡性
 * 3. 性能监控：测量关键操作耗时
 * 
 * 【与其他平衡树对比】
 * - vs AVL树：实现更简单，无需旋转操作
 * - vs 红黑树：代码量小，但最坏情况性能略差
 * - vs Treap：确定性算法，不受随机因素影响
 * 
 * 【适用场景】
 * 1. 需要维护有序集合的动态操作
 * 2. 实现简单性和性能需要平衡的场景
 * 3. 数据随机分布的情况
 * 
 * 【笔试面试高频问题】
 * 1. 替罪羊树的核心思想是什么？
 * 2. 为什么插入操作均摊复杂度是O(log n)？
 * 3. α因子的选择对性能有什么影响？
 * 4. 惰性删除的实现原理是什么？
 * 
 * 【测试链接】
 * 洛谷 P3369: https://www.luogu.com.cn/problem/P3369
 * 提交时请把类名改成"Main"
 * 
 * @author Algorithm Journey
 * @version 1.0
 * @since 2025
 */

/*
 * 替罪羊树相关题目资源:
 * 
 * 【LeetCode (力扣)】
 * 1. 295. 数据流的中位数 - https://leetcode-cn.com/problems/find-median-from-data-stream/
 *    题目描述: 设计一个支持以下两种操作的数据结构：
 *    - void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 *    - double findMedian() - 返回目前所有元素的中位数。
 *    应用: 使用两个替罪羊树分别维护较小和较大的一半元素
 *    Java实现优化: 使用LinkedList优化重构过程中的内存使用，注意线程安全问题
 * 
 * 2. 315. 计算右侧小于当前元素的个数 - https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/
 *    题目描述: 给定一个整数数组 nums，按要求返回一个新数组 counts。
 *    counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 *    应用: 逆序插入元素并查询小于当前元素的数量
 *    Java实现技巧: 使用Integer对象缓存频繁使用的整数值
 * 
 * 3. 493. 翻转对 - https://leetcode-cn.com/problems/reverse-pairs/
 *    题目描述: 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
 *    应用: 类似逆序对，但条件更严格，需要查询小于nums[i]/2的元素数量
 *    Java实现注意: 使用long类型避免整数溢出
 * 
 * 4. 148. 排序链表 - https://leetcode-cn.com/problems/sort-list/
 *    题目描述: 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
 *    应用: 可以用替罪羊树存储链表节点值，然后重新构建有序链表
 * 
 * 5. 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 *    题目描述: 在未排序的数组中找到第 k 个最大的元素。
 *    应用: 使用替罪羊树的index操作
 *    Java实现优化: 使用优先级队列可能更简单，但替罪羊树支持更多扩展功能
 * 
 * 6. 面试题 17.09. 第 k 个数 - https://leetcode-cn.com/problems/get-kth-magic-number-lcci/
 *    题目描述: 有些数的素因子只有 3，5，7，请设计一个算法找出第 k 个数。
 *    应用: 使用替罪羊树维护候选元素集合，避免重复计算
 * 
 * 7. 352. 将数据流变为多个不相交区间 - https://leetcode-cn.com/problems/data-stream-as-disjoint-intervals/
 *    题目描述: 设计一个数据结构，根据数据流添加整数，并返回不相交区间的列表。
 *    应用: 插入元素并维护有序区间，可使用替罪羊树高效查找相邻元素
 *    Java实现技巧: 使用TreeSet实现可能更简单，但替罪羊树可提供更灵活的定制化操作
 * 
 * 【洛谷 (Luogu)】
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
 *    题目描述: 在P3369基础上加强数据，强制在线，需要将查询操作的参数与上次结果异或
 * 
 * 3. P1168 中位数 - https://www.luogu.com.cn/problem/P1168
 *    题目描述: 维护一个动态变化的序列，每次插入一个数后，输出当前序列的中位数
 *    应用: 实时维护中间值，可使用两个替罪羊树
 * 
 * 4. P1908 逆序对 - https://www.luogu.com.cn/problem/P1908
 *    题目描述: 求逆序对数量
 *    应用: 替罪羊树实现离散化统计
 * 
 * 【计蒜客】
 * 1. 计蒜客 普通平衡树模板 - https://nanti.jisuanke.com/t/41099
 *    题目描述: 同洛谷P3369
 * 
 * 【HDU】
 * 1. HDU 4585 Shaolin - http://acm.hdu.edu.cn/showproblem.php?pid=4585
 *    题目描述: 维护僧人排名，新僧人加入时找到相邻排名的僧人
 *    应用: 插入并查询前驱和后继
 * 
 * 2. HDU 1394 Minimum Inversion Number - http://acm.hdu.edu.cn/showproblem.php?pid=1394
 *    题目描述: 求循环右移数组的所有逆序对最小值
 *    应用: 逆序对计数的变种
 * 
 * 3. HDU 2871 Memory Control - http://acm.hdu.edu.cn/showproblem.php?pid=2871
 *    题目描述: 内存分配与释放问题
 *    应用: 区间维护和查询
 * 
 * 【Codeforces】
 * 1. Codeforces 911D - Inversion Counting - https://codeforces.com/problemset/problem/911/D
 *    题目描述: 可反转区间的逆序对计数
 *    应用: 灵活运用平衡树进行统计
 * 
 * 2. Codeforces 459D - Pashmak and Parmida's problem - https://codeforces.com/problemset/problem/459/D
 *    题目描述: 统计满足条件的数对数量
 *    应用: 使用替罪羊树维护前缀和后缀信息
 * 
 * 【AtCoder】
 * 1. AtCoder ABC162 E - Sum of gcd of Tuples (Hard) - https://atcoder.jp/contests/abc162/tasks/abc162_e
 *    题目描述: 计算所有可能元组的gcd之和
 *    应用: 在一些优化解法中可以使用替罪羊树维护信息
 * 
 * 2. AtCoder ABC177 F - I hate Shortest Path Problem - https://atcoder.jp/contests/abc177/tasks/abc177_f
 *    题目描述: 最短路径问题的变种
 *    应用: 使用替罪羊树优化Dijkstra算法
 * 
 * 【SPOJ】
 * 1. SPOJ ORDERSET - https://www.spoj.com/problems/ORDERSET/
 *    题目描述: 支持插入、删除、查询第k小和比x小的元素个数
 *    应用: 基础平衡树操作的组合应用
 * 
 * 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
 *    题目描述: 区间不同元素的个数查询
 *    应用: 离线处理+平衡树统计
 * 
 * 【牛客网】
 * 1. 牛客网 NC14516 普通平衡树 - https://ac.nowcoder.com/acm/problem/14516
 *    题目描述: 同洛谷P3369，支持插入、删除、排名查询等基本操作
 * 
 * 2. 牛客网 NC18375 逆序对 - https://ac.nowcoder.com/acm/problem/18375
 *    题目描述: 统计逆序对数量
 *    应用: 使用替罪羊树进行逆序对统计
 * 
 * 【BZOJ】
 * 1. BZOJ 3600 没有人的算术 - https://www.lydsy.com/JudgeOnline/problem.php?id=3600
 *    题目描述: 定义一种新的数对比较方式，支持动态插入和查询
 *    应用: 替罪羊树动态标号+线段树
 * 
 * 2. BZOJ 3224 Tyvj 1728 普通平衡树 - https://www.lydsy.com/JudgeOnline/problem.php?id=3224
 *    题目描述: 同洛谷P3369
 * 
 * 【算法特点深度解析】
 * 1. 替罪羊树是一种重量平衡树，通过重构操作维持平衡，而非旋转操作
 * 2. 替罪羊树会在插入、删除操作后检测树是否失衡，失衡时进行局部重构
 * 3. 关键设计：
 *    - 使用α因子控制树的平衡程度
 *    - 通过中序遍历和重新构建实现重构
 *    - 惰性删除策略处理删除操作
 * 4. 实现简单性与性能平衡的典范，特别适合算法竞赛使用
 * 
 * 【时间复杂度详细分析】
 * 1. 插入操作: O(log n) 均摊
 *    - 平均情况：O(log n)的查找和插入
 *    - 最坏情况：需要重构，O(n)，但均摊后仍为O(log n)
 *    - 数学证明：使用势能函数分析，每个节点被重构的次数是O(log n)的
 * 2. 删除操作: O(log n) 均摊
 *    - 采用惰性删除，仅减少计数，不立即删除节点
 *    - 当树的密度下降时触发重构
 * 3. 查询操作: O(log n) 最坏情况
 *    - 由于树的高度被限制在O(log n)，查询操作稳定高效
 * 4. 重构操作: O(n) 单次，但发生频率低
 *    - 每个节点平均O(1/α^log n)次重构，总均摊复杂度为O(log n)
 * 
 * 【空间复杂度分析】
 * 1. O(n) 总空间复杂度
 * 2. 使用数组模拟树结构，提高缓存局部性
 * 3. 对于重复元素使用计数优化，减少空间消耗
 * 4. 重构过程中使用辅助数组存储中序遍历结果
 * 
 * 【α因子深度解析】
 * 1. α ∈ [0.5, 1.0]
 * 2. α = 0.5时：
 *    - 树最平衡，类似于完全二叉树
 *    - 重构频率极高，插入操作的常数大
 *    - 查询效率最高
 * 3. α = 0.7时（本实现）：
 *    - 重构频率适中
 *    - 插入、删除、查询性能较为平衡
 *    - 工程实践中最常用的选择
 * 4. α = 0.8时：
 *    - 重构次数减少
 *    - 查询性能可能略有下降
 *    - 适合插入删除密集型应用
 * 5. α = 1.0时：
 *    - 退化为普通二叉搜索树
 *    - 可能形成链表，查询退化为O(n)
 *    - 但在完全随机数据下表现尚可
 * 6. 调优建议：根据具体应用场景的查询/修改比例调整α值
 * 
 * 【与其他平衡树的详细比较】
 * 1. 与AVL树比较：
 *    - AVL树：基于旋转，严格平衡（左右子树高度差≤1）
 *    - 替罪羊树：基于重构，松散平衡（α因子控制）
 *    - 优势：代码实现更简单，无需维护高度信息
 *    - 劣势：最坏情况插入可能需要O(n)时间
 * 2. 与红黑树比较：
 *    - 红黑树：通过颜色标记和旋转维持平衡
 *    - 替罪羊树：实现更简单，但均摊常数可能略大
 *    - 标准库（如Java TreeMap）通常使用红黑树
 * 3. 与Treap比较：
 *    - Treap：结合二叉搜索树和堆的特性，使用随机优先级
 *    - 替罪羊树：确定性算法，不受随机因素影响
 *    - 优势：无需随机数生成，行为可预测
 * 4. 与Splay树比较：
 *    - Splay树：通过旋转将访问过的节点移到根，利用局部性原理
 *    - 替罪羊树：结构更稳定，但不具备自调整能力
 *    - 适用场景：Splay树适合有访问局部性的场景
 * 
 * 【工程化考量深度解析】
 * 1. 异常处理：
 *    - 本实现使用StreamTokenizer进行输入，效率高于Scanner
 *    - 对不存在元素的删除操作进行了防御性检查
 *    - 建议扩展：添加更多边界检查和异常抛出机制
 * 2. 输入输出优化：
 *    - 使用BufferedReader和PrintWriter进行高效IO
 *    - 在处理大数据量输入时尤为重要
 * 3. 内存管理：
 *    - 使用数组模拟树结构，避免频繁对象创建
 *    - clear()方法可重用内存空间
 *    - 建议扩展：实现内存池管理
 * 4. 线程安全：
 *    - 当前实现非线程安全
 *    - 多线程环境下需要添加锁机制或使用并发版本
 * 5. 代码模块化：
 *    - 各功能函数职责清晰，易于维护
 *    - 建议扩展：封装为类，支持泛型
 * 6. 单元测试：
 *    - 建议添加各种边界情况的测试用例
 *    - 如空树操作、大量重复元素、极端数据等
 * 
 * 【Java调试技巧与问题定位】
 * 1. 调试辅助函数：
 *    ```java
 *    public void printTree() {
 *        if (root == null) {
 *            System.out.println("空树");
 *            return;
 *        }
 *        printTree(root, 0);
 *    }
 *    
 *    private void printTree(Node node, int level) {
 *        if (node == null) return;
 *        printTree(node.right, level + 1);
 *        StringBuilder indent = new StringBuilder();
 *        for (int i = 0; i < level; i++) {
 *            indent.append("    ");
 *        }
 *        System.out.println(indent + node.key + "(size=" + size(node) + ")");
 *        printTree(node.left, level + 1);
 *    }
 *    ```
 * 
 * 2. 断言与验证：
 *    ```java
 *    private void verifyTree(Node node) {
 *        assert node != null : "节点不能为空";
 *        assert size(node) == size(node.left) + size(node.right) + 1 : "节点大小计算错误";
 *        
 *        if (node.left != null) {
 *            assert node.left.key < node.key : "左子树键值必须小于当前节点";
 *            verifyTree(node.left);
 *        }
 *        if (node.right != null) {
 *            assert node.right.key > node.key : "右子树键值必须大于当前节点";
 *            verifyTree(node.right);
 *        }
 *    }
 *    ```
 * 
 * 3. 性能监控与日志：
 *    - 使用System.nanoTime()测量关键操作耗时
 *    - 集成SLF4J等日志框架记录运行状态
 *    - 使用JProfiler等工具进行性能分析
 * 
 * 【Java工程化考量】
 * 1. 测试框架实现：
 *    ```java
 *    public static void runTests() {
 *        ScapeGoatTree tree = new ScapeGoatTree();
 *        
 *        // 测试空树操作
 *        testEmptyTree(tree);
 *        
 *        // 测试基本插入和查询
 *        testBasicOperations(tree);
 *        
 *        // 测试删除操作
 *        testDeleteOperations(tree);
 *        
 *        // 测试重构触发
 *        testRebuildTrigger(tree);
 *        
 *        // 测试极端情况
 *        testEdgeCases(tree);
 *        
 *        System.out.println("所有测试通过！");
 *    }
 *    
 *    private static void testBasicOperations(ScapeGoatTree tree) {
 *        tree.clear();
 *        tree.add(10);
 *        tree.add(5);
 *        tree.add(15);
 *        
 *        assert tree.getRank(10) == 2 : "Rank计算错误";
 *        assert tree.getByIndex(1) == 5 : "按索引查询错误";
 *        assert tree.getByIndex(2) == 10 : "按索引查询错误";
 *        assert tree.getByIndex(3) == 15 : "按索引查询错误";
 *    }
 *    ```
 * 
 * 2. 可配置性设计：
 *    - 将α因子设为可配置参数
 *    - 支持自定义比较器
 *    - 提供统计信息收集功能
 *    
 * 3. 序列化支持：
 *    ```java
 *    implements Serializable {
 *        private static final long serialVersionUID = 1L;
 *        
 *        private void writeObject(ObjectOutputStream out) throws IOException {
 *            // 自定义序列化逻辑
 *            out.writeDouble(alpha);
 *            out.writeInt(size);
 *            // 序列化树结构
 *            serializeNode(root, out);
 *        }
 *        
 *        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
 *            // 自定义反序列化逻辑
 *            alpha = in.readDouble();
 *            size = in.readInt();
 *            // 反序列化树结构
 *            root = deserializeNode(in);
 *        }
 *    }
 *    ```
 * 
 * 4. 泛型支持：
 *    ```java
 *    public class ScapeGoatTree<K extends Comparable<K>> {
 *        // 实现泛型版本的替罪羊树
 *    }
 *    ```
 * 
 * 【Java与标准库对比】
 * 1. 与TreeSet/TreeMap对比：
 *    - Java标准库中的TreeSet/TreeMap基于红黑树实现
 *    - 替罪羊树在插入删除操作上有均摊O(log n)的复杂度
 *    - 标准库实现经过高度优化，性能通常更好
 *    - 替罪羊树适合学习和定制化场景
 * 
 * 2. 与ArrayList+二分查找对比：
 *    - ArrayList+bisect在静态数据上查询高效
 *    - 替罪羊树在动态更新场景下更优
 *    - ArrayList插入操作需要O(n)时间
 * 
 * 【大数据量Java实现优化策略】
 * 1. 内存优化：
 *    - 使用数组代替链表存储节点信息
 *    - 实现对象池减少GC压力
 *    - 使用Off-Heap内存存储大量数据
 * 
 * 2. 并行处理：
 *    - 数据分片处理
 *    - 使用ForkJoinPool并行构建树
 *    - 批量操作优化
 * 
 * 3. 性能调优参数：
 *    - 调整JVM参数：-XX:+UseG1GC -XX:MaxGCPauseMillis=200
 *    - 预热JIT：在正式使用前进行预热操作

【跨语言实现差异详解】
 * 1. Java vs C++：
 *    - Java:
 *      - 使用数组模拟树结构，易于实现和调试
 *      - 使用StreamTokenizer和BufferedReader提高IO效率
 *      - 自动内存管理，无需手动释放空间
 *    - C++:
 *      - 可使用指针实现，内存占用更小
 *      - 更快的执行速度，尤其是在大数据量场景
 *      - 需注意内存泄漏问题
 * 2. Java vs Python：
 *    - Java:
 *      - 数组访问速度远快于Python的列表
 *      - 递归深度限制更宽松（默认10000左右）
 *      - 执行效率高，适合大数据量处理
 *    - Python:
 *      - 代码更简洁，可读性更好
 *      - 递归深度受限（默认1000），需注意栈溢出
 *      - 性能较低，但实现更简单
 * 3. Java vs Go：
 *    - Java:
 *      - 面向对象特性使代码更结构化
 *      - 丰富的标准库支持
 *    - Go:
 *      - 值传递特性可优化树节点复制操作
 *      - goroutine支持并发处理
 *      - 更简洁的语法和错误处理机制
 * 
 * 【数学与算法理论联系】
 * 1. 均摊分析：
 *    - 替罪羊树的时间复杂度证明基于势能函数
 *    - 每个节点的势能定义为其到最近重构祖先的距离
 *    - 每次重构操作会将势能降低，从而保证均摊复杂度
 * 2. 概率统计：
 *    - α因子的选择涉及概率分布和期望分析
 *    - 可以通过数学方法找到最优的α值
 * 3. 信息论：
 *    - 平衡树的结构可以看作是一种高效的信息编码方式
 *    - 树的高度与信息熵有关
 * 
 * 【与机器学习/AI领域的联系】
 * 1. 决策树构建优化：
 *    - 替罪羊树的重构思想可以应用于决策树剪枝
 *    - 当决策树过拟合时，可以通过重构优化树结构
 * 2. 强化学习状态空间管理：
 *    - 在大型状态空间中，使用平衡树高效管理状态
 *    - 支持快速查询相似状态和状态转移
 * 3. 数据流处理算法：
 *    - 在线学习算法中，需要高效维护数据统计信息
 *    - 替罪羊树可用于实时维护分位数等统计量
 * 4. 计算机视觉中的应用：
 *    - 图像特征的高效存储和查询
 *    - 基于内容的图像检索系统
 * 5. 自然语言处理：
 *    - 词汇表管理和词频统计
 *    - 文本索引的构建和查询
 * 
 * 【调试技巧与性能优化】
 * 1. 调试技巧：
 *    - 添加打印中间状态的辅助函数，如printTree()
 *    - 使用断言验证树的平衡性：assert balance(head)
 *    - 边界测试：空树、单节点树、大量重复元素
 *    - 性能瓶颈定位：使用profiler分析热点函数
 * 2. 性能优化策略：
 *    - 调整α因子以适应具体应用场景
 *    - 使用非递归实现避免栈溢出
 *    - 对于特定问题，可优化数据结构（如离散化）
 *    - 批量操作时，可延迟重构以提高效率
 * 3. 内存优化：
 *    - 实现内存池管理节点分配
 *    - 对于大数据量，考虑使用动态数组扩展
 *    - 压缩存储重复元素信息
 * 
 * 【代码健壮性保障】
 * 1. 防御性编程：
 *    - 删除不存在元素的检查
 *    - 前驱后继不存在的边界处理
 *    - 查询排名超出范围的处理
 * 2. 异常场景处理：
 *    - 空树操作的特殊处理
 *    - 单节点树删除后的状态
 *    - 大量重复元素的插入和删除
 * 3. 鲁棒性测试：
 *    - 随机数据测试
 *    - 压力测试（大数据量）
 *    - 特殊模式数据（如完全有序、逆序、交替数据）
 * 
 * 【笔试面试高频问题解析】
 * 1. 替罪羊树的核心思想是什么？
 *    答：通过α因子判断子树是否失衡，失衡时进行局部重构，而非通过旋转操作维护平衡。
 * 2. 为什么替罪羊树的插入操作均摊复杂度是O(log n)？
 *    答：虽然单次重构是O(n)时间，但每个节点被重构的次数是O(log n)均摊的，总操作均摊下来是O(log n)。
 * 3. α因子的选择对性能有什么影响？
 *    答：α越小，树越平衡但重构越频繁；α越大，重构越少但查询可能变慢。通常选择0.7作为平衡点。
 * 4. 替罪羊树与红黑树的区别是什么？
 *    答：红黑树通过颜色标记和旋转维持平衡，而替罪羊树通过重构；红黑树实现更复杂但最坏情况更稳定，替罪羊树实现简单但均摊性能接近。
 * 5. 惰性删除的实现原理是什么？
 *    答：删除时不立即移除节点，仅减少计数，当树的密度下降到一定程度时触发重构。
 * 6. 替罪羊树的优势和劣势是什么？
 *    答：优势是实现简单，无需旋转操作；劣势是单次最坏情况性能较差，不适合实时性要求极高的场景。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code02_ScapeGoat1 {

	/**
	 * α平衡因子 - 控制树的平衡程度
	 * 取值范围: [0.5, 1.0]
	 * 推荐值: 0.7 (平衡重构频率和查询效率)
	 * 数学原理: 当max(size[left], size[right]) > α * size[current]时触发重构
	 */
	public static double ALPHA = 0.7;

	/**
	 * 最大节点容量 - 预分配数组大小
	 * 根据题目约束设置，避免动态扩容开销
	 * 工程优化: 可根据实际数据规模动态调整
	 */
	public static int MAXN = 100001;

	/**
	 * 树的根节点索引
	 * 值为0表示空树
	 * 工程考量: 使用索引而非指针，提高缓存友好性
	 */
	public static int head = 0;

	/**
	 * 节点计数器 - 记录已使用的节点数量
	 * 用于管理预分配数组的分配和回收
	 * 内存管理: 避免频繁的对象创建和垃圾回收
	 */
	public static int cnt = 0;

	/**
	 * 节点键值数组 - 存储每个节点的值
	 * 索引从1开始，0表示空节点
	 * 性能优化: 数组访问比对象访问更快
	 */
	public static int[] key = new int[MAXN];

	/**
	 * 节点计数数组 - 存储重复键值的出现次数
	 * 支持重复元素的插入和删除
	 * 空间优化: 避免为重复元素创建多个节点
	 */
	public static int[] count = new int[MAXN];

	/**
	 * 左子树索引数组 - 存储左子节点的索引
	 * 值为0表示没有左子树
	 * 数据结构: 使用数组模拟树结构
	 */
	public static int[] left = new int[MAXN];

	/**
	 * 右子树索引数组 - 存储右子节点的索引
	 * 值为0表示没有右子树
	 * 内存布局: 连续内存访问，提高缓存命中率
	 */
	public static int[] right = new int[MAXN];

	/**
	 * 子树大小数组 - 存储以该节点为根的子树中元素总数
	 * 包括重复元素的计数
	 * 时间复杂度: O(1)获取子树大小，支持快速排名查询
	 */
	public static int[] size = new int[MAXN];

	/**
	 * 有效节点数数组 - 存储子树中不同键值的节点数量
	 * 用于平衡性判断，忽略被惰性删除的节点
	 * 算法核心: 基于有效节点数判断是否需要重构
	 */
	public static int[] diff = new int[MAXN];

	/**
	 * 重构收集数组 - 用于存储中序遍历结果
	 * 在重构操作时临时使用
	 * 空间复用: 避免每次重构都创建新数组
	 */
	public static int[] collect = new int[MAXN];

	/**
	 * 收集计数器 - 记录当前收集的节点数量
	 * 与collect数组配合使用
	 * 线程安全: 当前实现非线程安全
	 */
	public static int ci;

	/**
	 * 不平衡节点 - 记录需要重构的子树的根节点
	 * 在插入/删除过程中动态更新
	 * 重构策略: 只重构不平衡的子树，而非整棵树
	 */
	public static int top;

	/**
	 * 父节点索引 - 不平衡节点的父节点
	 * 用于重构后重新连接子树
	 * 工程实现: 记录父子关系，支持局部重构
	 */
	public static int father;

	/**
	 * 子树方向 - 标识不平衡节点是父节点的左子树还是右子树
	 * 1: 左子树, 2: 右子树
	 * 重构连接: 确保重构后正确连接到父节点
	 */
	public static int side;

	/**
	 * 初始化新节点
	 * 
	 * @param num 节点的键值
	 * @return 新节点的索引
	 * 
	 * 【时间复杂度】O(1)
	 * 【空间复杂度】O(1)
	 * 【算法步骤】
	 * 1. 分配新节点索引
	 * 2. 设置节点键值和初始计数
	 * 3. 初始化左右子树为空
	 * 4. 设置子树大小和有效节点数为1
	 * 
	 * 【工程化考量】
	 * - 使用预分配数组，避免动态内存分配
	 * - 节点索引从1开始，0表示空节点
	 * - 支持重复元素的计数机制
	 */
	public static int init(int num) {
		// 分配新节点，cnt从1开始递增
		key[++cnt] = num;
		// 初始化左右子树为空
		left[cnt] = right[cnt] = 0;
		// 设置初始计数和大小信息
		count[cnt] = size[cnt] = diff[cnt] = 1;
		return cnt;
	}

	/**
	 * 更新节点信息（子树大小和有效节点数）
	 * 
	 * @param i 要更新的节点索引
	 * 
	 * 【时间复杂度】O(1)
	 * 【空间复杂度】O(1)
	 * 【算法原理】
	 * 子树大小 = 左子树大小 + 右子树大小 + 当前节点计数
	 * 有效节点数 = 左子树有效节点数 + 右子树有效节点数 + (当前节点是否有效)
	 * 
	 * 【工程化考量】
	 * - 在插入、删除操作后必须调用此函数
	 * - 支持惰性删除：count为0时节点无效但仍存在
	 * - 为平衡性判断提供基础数据
	 */
	public static void up(int i) {
		// 更新子树大小：左子树 + 右子树 + 当前节点计数
		size[i] = size[left[i]] + size[right[i]] + count[i];
		// 更新有效节点数：左子树有效节点 + 右子树有效节点 + 当前节点是否有效
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
		for (int i = 1, op, x; i <= n; i++) {
			in.nextToken();
			op = (int) in.nval;
			in.nextToken();
			x = (int) in.nval;
			if (op == 1) {
				add(x);
			} else if (op == 2) {
				remove(x);
			} else if (op == 3) {
				out.println(rank(x));
			} else if (op == 4) {
				out.println(index(x));
			} else if (op == 5) {
				out.println(pre(x));
			} else {
				out.println(post(x));
			}
		}
		clear();
		out.flush();
		out.close();
		br.close();
	}

}
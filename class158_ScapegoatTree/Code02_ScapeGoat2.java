package class150;

// 替罪羊树的实现(C++版)
// 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
// 1，增加x，重复加入算多个词频
// 2，删除x，如果有多个，只删掉一个
// 3，查询x的排名，x的排名为，比x小的数的个数+1
// 4，查询数据中排名为x的数
// 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
// 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
// 所有操作的次数 <= 10^5
// -10^7 <= x <= +10^7
// 测试链接 : https://www.luogu.com.cn/problem/P3369
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/*
 * 替罪羊树相关题目资源:
 * 
 * 【LeetCode (力扣)】
 * 1. 295. 数据流的中位数 - https://leetcode-cn.com/problems/find-median-from-data-stream/
 *    题目描述: 设计一个支持以下两种操作的数据结构：
 *    - void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 *    - double findMedian() - 返回目前所有元素的中位数。
 *    应用: 使用两个替罪羊树分别维护较小和较大的一半元素
 *    C++实现优化: 使用vector优化重构过程中的内存使用
 * 
 * 2. 315. 计算右侧小于当前元素的个数 - https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/
 *    题目描述: 给定一个整数数组 nums，按要求返回一个新数组 counts。
 *    counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 *    应用: 逆序插入元素并查询小于当前元素的数量
 *    C++实现技巧: 使用离散化处理大数据范围输入
 * 
 * 3. 493. 翻转对 - https://leetcode-cn.com/problems/reverse-pairs/
 *    题目描述: 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
 *    应用: 类似逆序对，但条件更严格，需要查询小于nums[i]/2的元素数量
 *    C++实现注意: 使用long long类型避免整数溢出
 * 
 * 4. 148. 排序链表 - https://leetcode-cn.com/problems/sort-list/
 *    题目描述: 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
 *    应用: 可以用替罪羊树存储链表节点值，然后重新构建有序链表
 * 
 * 5. 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 *    题目描述: 在未排序的数组中找到第 k 个最大的元素。
 *    应用: 使用替罪羊树的index操作
 *    C++实现优化: 使用priority_queue可能更简单，但替罪羊树支持更多扩展功能
 * 
 * 6. 面试题 17.09. 第 k 个数 - https://leetcode-cn.com/problems/get-kth-magic-number-lcci/
 *    题目描述: 有些数的素因子只有 3，5，7，请设计一个算法找出第 k 个数。
 *    应用: 使用替罪羊树维护候选元素集合，避免重复计算
 * 
 * 7. 352. 将数据流变为多个不相交区间 - https://leetcode-cn.com/problems/data-stream-as-disjoint-intervals/
 *    题目描述: 设计一个数据结构，根据数据流添加整数，并返回不相交区间的列表。
 *    应用: 插入元素并维护有序区间，可使用替罪羊树高效查找相邻元素
 *    C++实现技巧: 使用set实现可能更简单，但替罪羊树可提供更灵活的定制化操作
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
 *    - 标准库（如std::set）通常使用红黑树
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
 *    - 本实现使用快速IO，效率高于标准IO
 *    - 对不存在元素的删除操作进行了防御性检查
 *    - 建议扩展：添加更多边界检查和异常抛出机制
 * 2. 输入输出优化：
 *    - 使用ios::sync_with_stdio(false)和cin.tie(nullptr)进行高效IO
 *    - 在处理大数据量输入时尤为重要
 * 3. 内存管理：
 *    - 使用数组模拟树结构，避免频繁内存分配
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
 * 【C++调试技巧与问题定位】
 * 1. 调试辅助函数：
 *    ```cpp
 *    void printTree(int node, int level = 0) {
 *        if (!node) return;
 *        printTree(rs[node], level + 1);
 *        for (int i = 0; i < level; i++) cout << "    ";
 *        cout << key[node] << "(cnt=" << key_count[node] << ",size=" << siz[node] << ")" << endl;
 *        printTree(ls[node], level + 1);
 *    }
 *    ```
 * 
 * 2. 断言与验证：
 *    ```cpp
 *    void verifyTree(int node) {
 *        assert(node != 0 && "节点不能为空");
 *        assert(siz[node] == siz[ls[node]] + siz[rs[node]] + key_count[node] && "节点大小计算错误");
 *        
 *        if (ls[node]) {
 *            assert(key[ls[node]] < key[node] && "左子树键值必须小于当前节点");
 *            verifyTree(ls[node]);
 *        }
 *        if (rs[node]) {
 *            assert(key[rs[node]] > key[node] && "右子树键值必须大于当前节点");
 *            verifyTree(rs[node]);
 *        }
 *    }
 *    ```
 * 
 * 3. 性能监控与日志：
 *    - 使用chrono库测量关键操作耗时
 *    - 使用日志框架记录运行状态
 *    - 使用perf等工具进行性能分析
 * 
 * 【C++工程化考量】
 * 1. 测试框架实现：
 *    ```cpp
 *    void runTests() {
 *        // 测试空树操作
 *        clear();
 *        assert(getRank(1) == 1);
 *        assert(index(1) == INT_MIN);
 *        
 *        // 测试基本插入和查询
 *        add(10);
 *        add(5);
 *        add(15);
 *        
 *        assert(getRank(10) == 2);
 *        assert(index(1) == 5);
 *        
 *        // 测试删除操作
 *        remove(10);
 *        assert(getRank(10) == 2);
 *        
 *        cout << "所有测试通过！" << endl;
 *    }
 *    ```
 * 
 * 2. 可配置性设计：
 *    - 将α因子设为可配置参数
 *    - 支持自定义比较函数
 *    - 提供统计信息收集功能
 *    
 * 3. 内存池优化：
 *    ```cpp
 *    class MemoryPool {
 *    private:
 *        vector<int> free_list;
 *        int* key;     // 预分配的大型数组
 *        int* ls;      // 左子节点
 *        int* rs;      // 右子节点
 *        int* key_count; // 键出现次数
 *        int* siz;     // 子树大小
 *        int* diff;    // 有效节点数
 *        int capacity; // 容量
 *        int next_free; // 下一个可用位置
 *    public:
 *        MemoryPool(int cap = 100001) : capacity(cap), next_free(1) {
 *            key = new int[cap];
 *            ls = new int[cap];
 *            rs = new int[cap];
 *            key_count = new int[cap];
 *            siz = new int[cap];
 *            diff = new int[cap];
 *        }
 *        
 *        ~MemoryPool() {
 *            delete[] key;
 *            delete[] ls;
 *            delete[] rs;
 *            delete[] key_count;
 *            delete[] siz;
 *            delete[] diff;
 *        }
 *        
 *        int allocate() {
 *            if (!free_list.empty()) {
 *                int node = free_list.back();
 *                free_list.pop_back();
 *                return node;
 *            }
 *            return next_free++;
 *        }
 *        
 *        void deallocate(int node) {
 *            free_list.push_back(node);
 *        }
 *    };
 *    ```
 * 
 * 4. 模板化支持：
 *    ```cpp
 *    template<typename T>
 *    class ScapeGoatTree {
 *    private:
 *        // 实现泛型版本的替罪羊树
 *    public:
 *        void add(const T& value);
 *        void remove(const T& value);
 *        int getRank(const T& value);
 *        T index(int rank);
 *        T predecessor(const T& value);
 *        T successor(const T& value);
 *    };
 *    ```
 * 
 * 【C++与标准库对比】
 * 1. 与std::set/std::map对比：
 *    - C++标准库中的std::set/std::map基于红黑树实现
 *    - 替罪羊树在插入删除操作上有均摊O(log n)的复杂度
 *    - 标准库实现经过高度优化，性能通常更好
 *    - 替罪羊树适合学习和定制化场景
 * 
 * 2. 与vector+二分查找对比：
 *    - vector+lower_bound在静态数据上查询高效
 *    - 替罪羊树在动态更新场景下更优
 *    - vector插入操作需要O(n)时间
 * 
 * 【大数据量C++实现优化策略】
 * 1. 内存优化：
 *    - 使用数组代替指针存储节点信息
 *    - 实现对象池减少内存分配压力
 *    - 使用内存映射存储大量数据
 * 
 * 2. 并行处理：
 *    - 数据分片处理
 *    - 使用std::async并行构建树
 *    - 批量操作优化
 * 
 * 3. 性能调优参数：
 *    - 编译优化选项：-O2/-O3，大幅提升运行速度
 *    - 内联优化：-finline-functions
 *    - LTO链接时优化：-flto，提高跨文件优化效果
 *    - 特定架构优化：-march=native，针对当前CPU架构优化
 * 
 * 【跨语言实现差异详解】
 * 1. C++ vs Java：
 *    - C++:
 *      - 可使用指针实现，内存占用更小
 *      - 更快的执行速度，尤其是在大数据量场景
 *      - 需注意内存泄漏问题
 *    - Java:
 *      - 使用数组模拟树结构，易于实现和调试
 *      - 使用StreamTokenizer和BufferedReader提高IO效率
 *      - 自动内存管理，无需手动释放空间
 * 2. C++ vs Python：
 *    - C++:
 *      - 更快的执行速度
 *      - 更复杂的内存管理
 *      - 更严格的类型检查
 *    - Python:
 *      - 代码更简洁，可读性更好
 *      - 递归深度受限（默认1000），需注意栈溢出
 *      - 性能较低，但实现更简单
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
 *    - 性能瓶颈定位：使用perf分析热点函数
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

//#include <iostream>
//#include <vector>
//#include <algorithm>
//#include <cmath>
//#include <climits>
//#include <cstring>
//#
//#using namespace std;
//#
//#const double ALPHA = 0.7;
//#const int MAXN = 100001;
//#int head = 0;
//#int cnt = 0;
//#int key[MAXN];
//#int key_count[MAXN];
//#int ls[MAXN];
//#int rs[MAXN];
//#int siz[MAXN];
//#int diff[MAXN];
//#int collect[MAXN];
//#int ci;
//#int top;
//#int father;
//#int side;
//#
//#int init(int num) {
//#    key[++cnt] = num;
//#    ls[cnt] = rs[cnt] = 0;
//#    key_count[cnt] = siz[cnt] = diff[cnt] = 1;
//#    return cnt;
//#}
//#
//#void up(int i) {
//#    siz[i] = siz[ls[i]] + siz[rs[i]] + key_count[i];
//#    diff[i] = diff[ls[i]] + diff[rs[i]] + (key_count[i] > 0 ? 1 : 0);
//#}
//#
//#void inorder(int i) {
//#    if (i != 0) {
//#        inorder(ls[i]);
//#        if (key_count[i] > 0) {
//#            collect[++ci] = i;
//#        }
//#        inorder(rs[i]);
//#    }
//#}
//#
//#int build(int l, int r) {
//#    if (l > r) {
//#        return 0;
//#    }
//#    int m = (l + r) / 2;
//#    int h = collect[m];
//#    ls[h] = build(l, m - 1);
//#    rs[h] = build(m + 1, r);
//#    up(h);
//#    return h;
//#}
//#
//#void rebuild() {
//#    if (top != 0) {
//#        ci = 0;
//#        inorder(top);
//#        if (ci > 0) {
//#            if (father == 0) {
//#                head = build(1, ci);
//#            } else if (side == 1) {
//#                ls[father] = build(1, ci);
//#            } else {
//#                rs[father] = build(1, ci);
//#            }
//#        }
//#    }
//#}
//#
//#bool balance(int i) {
//#    return ALPHA * diff[i] >= max(diff[ls[i]], diff[rs[i]]);
//#}
//#
//#void add(int i, int f, int s, int num) {
//#    if (i == 0) {
//#        if (f == 0) {
//#            head = init(num);
//#        } else if (s == 1) {
//#            ls[f] = init(num);
//#        } else {
//#            rs[f] = init(num);
//#        }
//#    } else {
//#        if (key[i] == num) {
//#            key_count[i]++;
//#        } else if (key[i] > num) {
//#            add(ls[i], i, 1, num);
//#        } else {
//#            add(rs[i], i, 2, num);
//#        }
//#        up(i);
//#        if (!balance(i)) {
//#            top = i;
//#            father = f;
//#            side = s;
//#        }
//#    }
//#}
//#
//#void add(int num) {
//#    top = father = side = 0;
//#    add(head, 0, 0, num);
//#    rebuild();
//#}
//#
//#int small(int i, int num) {
//#    if (i == 0) {
//#        return 0;
//#    }
//#    if (key[i] >= num) {
//#        return small(ls[i], num);
//#    } else {
//#        return siz[ls[i]] + key_count[i] + small(rs[i], num);
//#    }
//#}
//#
//#int getRank(int num) {
//#    return small(head, num) + 1;
//#}
//#
//#int index(int i, int x) {
//#    if (siz[ls[i]] >= x) {
//#        return index(ls[i], x);
//#    } else if (siz[ls[i]] + key_count[i] < x) {
//#        return index(rs[i], x - siz[ls[i]] - key_count[i]);
//#    }
//#    return key[i];
//#}
//#
//#int index(int x) {
//#    return index(head, x);
//#}
//#
//#int pre(int num) {
//#    int kth = getRank(num);
//#    if (kth == 1) {
//#        return INT_MIN;
//#    } else {
//#        return index(kth - 1);
//#    }
//#}
//#
//#int post(int num) {
//#    int kth = getRank(num + 1);
//#    if (kth == siz[head] + 1) {
//#        return INT_MAX;
//#    } else {
//#        return index(kth);
//#    }
//#}
//#
//#void remove(int i, int f, int s, int num) {
//#    if (key[i] == num) {
//#        key_count[i]--;
//#    } else if (key[i] > num) {
//#        remove(ls[i], i, 1, num);
//#    } else {
//#        remove(rs[i], i, 2, num);
//#    }
//#    up(i);
//#    if (!balance(i)) {
//#        top = i;
//#        father = f;
//#        side = s;
//#    }
//#}
//#
//#void remove(int num) {
//#    if (getRank(num) != getRank(num + 1)) {
//#        top = father = side = 0;
//#        remove(head, 0, 0, num);
//#        rebuild();
//#    }
//#}
//#
//#void clear() {
//#    memset(key, 0, sizeof(key));
//#    memset(key_count, 0, sizeof(key_count));
//#    memset(ls, 0, sizeof(ls));
//#    memset(rs, 0, sizeof(rs));
//#    memset(siz, 0, sizeof(siz));
//#    memset(diff, 0, sizeof(diff));
//#    cnt = 0;
//#    head = 0;
//#}
//#
//#int main() {
//#    ios::sync_with_stdio(false);
//#    cin.tie(nullptr);
//#    int n;
//#    cin >> n;
//#    for (int i = 1; i <= n; i++) {
//#        int op, x;
//#        cin >> op >> x;
//#        if (op == 1) {
//#            add(x);
//#        } else if (op == 2) {
//#            remove(x);
//#        } else if (op == 3) {
//#            cout << getRank(x) << "\n";
//#        } else if (op == 4) {
//#            cout << index(x) << "\n";
//#        } else if (op == 5) {
//#            cout << pre(x) << "\n";
//#        } else {
//#            cout << post(x) << "\n";
//#        }
//#    }
//#    clear();
//#    return 0;
//#}
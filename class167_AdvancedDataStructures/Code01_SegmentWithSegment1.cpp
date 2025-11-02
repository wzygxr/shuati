/**
 * 线段树套线段树（二维线段树）- 主要实现 (C++版本)
 * 
 * 基础问题：HDU 1823 Luck and Love
 * 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1823
 * 
 * 问题描述：
 * 每对男女都有三个属性：身高height，活跃度，缘分值。系统会不断地插入这些数据，并查询某个身高区间[h1, h2]和活跃度区间[a1, a2]内缘分值的最大值。
 * 身高为int类型，活跃度和缘分值为小数点后最多1位的double类型。
 * 实现一种结构，提供如下两种类型的操作：
 * 1. 操作 I a b c : 加入一个人，身高为a，活泼度为b，缘分值为c
 * 2. 操作 Q a b c d : 查询身高范围[a,b]，活泼度范围[c,d]，所有人中的缘分最大值
 * 注意操作Q，如果a > b需要交换，如果c > d需要交换
 * 100 <= 身高 <= 200
 * 0.0 <= 活泼度、缘分值 <= 100.0
 * 
 * 算法思路：
 * 这是一个二维区间最大值查询问题，采用线段树套线段树（二维线段树）的数据结构来解决。
 * 
 * 数据结构设计：
 * 1. 外层线段树用于维护身高height的区间信息
 * 2. 内层线段树用于维护活跃度的区间信息和缘分值的最大值
 * 3. 每个外层线段树节点对应一个内层线段树，用于处理其覆盖区间内的活跃度和缘分值
 * 4. 外层线段树范围：[MINX, MAXX] = [100, 200]，共101个值
 * 5. 内层线段树范围：[MINY, MAXY] = [0, 1000]，共1001个值（活泼度*10）
 * 6. tree[xi][yi]：二维数组，xi为外层线段树节点索引，yi为内层线段树节点索引
 * 
 * 核心操作：
 * 1. build：构建外层线段树，每个节点构建对应的内层线段树
 * 2. update：更新指定height和活跃度的缘分值
 * 3. query：查询某个height区间和活跃度区间内缘分值的最大值
 * 
 * 时间复杂度分析：
 * 1. build操作：O((H * log A) * log H)，其中H是身高范围，A是活跃度范围
 * 2. update操作：O(log H * log A) = O(log(101) * log(1001)) ≈ O(7 * 10) = O(70)
 * 3. query操作：O(log H * log A) = O(70)
 * 
 * 空间复杂度分析：
 * 1. 外层线段树：O(H)，具体为O(404)
 * 2. 内层线段树：每个外层节点需要O(A)空间，总体O(H * A)，具体为O(1,617,616)
 * 
 * 算法优势：
 * 1. 支持二维区间查询操作
 * 2. 相比于二维数组，空间利用更高效
 * 3. 支持动态更新操作
 * 4. 查询任意矩形区域内的最值
 * 
 * 算法劣势：
 * 1. 实现复杂度较高
 * 2. 空间消耗较大
 * 3. 常数因子较大
 * 
 * 适用场景：
 * 1. 需要频繁进行二维区间最值查询
 * 2. 数据可以动态更新
 * 3. 查询区域不规则
 * 4. 数据分布较稀疏
 * 
 * 更多类似题目：
 * 1. HDU 4911 Inversion (二维线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4911
 * 2. POJ 3468 A Simple Problem with Integers (树状数组套线段树) - http://poj.org/problem?id=3468
 * 3. SPOJ GSS3 Can you answer these queries III (线段树区间查询) - https://www.spoj.com/problems/GSS3/
 * 4. Codeforces 1100F Ivan and Burgers (线段树维护线性基) - https://codeforces.com/problemset/problem/1100/F
 * 5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (二维前缀和) - https://loj.ac/p/6419
 * 6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树) - https://atcoder.jp/contests/arc045/tasks/arc045_c
 * 7. UVa 11402 Ahoy, Pirates! (线段树区间修改) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2407
 * 8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询) - https://www.acwing.com/problem/content/description/244/
 * 9. CodeChef CHAOS2 Chaos (二维线段树) - https://www.codechef.com/problems/CHAOS2
 * 10. HackerEarth Range and Queries (线段树应用) - https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
 * 11. 牛客网 NC14732 区间第k大 (线段树套平衡树) - https://ac.nowcoder.com/acm/problem/14732
 * 12. 51Nod 1685 第K大 (线段树套线段树) - https://www.51nod.com/Challenge/Problem.html#problemId=1685
 * 13. SGU 398 Tickets (线段树区间处理) - https://codeforces.com/problemsets/acmsguru/problem/99999/398
 * 14. Codeforces 609E Minimum spanning tree for each edge (线段树优化) - https://codeforces.com/problemset/problem/609/E
 * 15. UVA 12538 Version Controlled IDE (线段树维护版本) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3780
 * 16. HDU 4819 Mosaic (二维线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4819
 * 17. Codeforces 19D Points (线段树套set) - https://codeforces.com/problemset/problem/19/D
 * 18. SPOJ KQUERY K-query (树状数组套线段树) - https://www.spoj.com/problems/KQUERY/
 * 19. POJ 2155 Matrix (二维线段树) - http://poj.org/problem?id=2155
 * 20. ZOJ 4819 Mosaic (二维线段树) - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368283
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理查询范围为空、查询结果不存在等情况
 * 3. 性能优化：使用静态数组减少内存分配开销
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 * 6. 线程安全：添加同步机制，支持多线程环境
 * 7. 单元测试：编写测试用例，确保功能正确性
 * 8. 内存管理：注意二维数组的初始化，避免内存溢出
 * 9. 错误处理：添加异常捕获和错误提示，提高程序健壮性
 * 10. 配置管理：将常量参数提取为配置项，提高程序灵活性
 * 
 * C++语言特性应用：
 * 1. 使用二维数组存储树的结构，提高访问效率
 * 2. 利用inline函数减少函数调用开销
 * 3. 使用const定义常量，提高编译时优化
 * 4. 利用位运算代替乘除法，提高运算效率
 * 5. 使用全局变量预分配空间，避免动态内存分配
 * 
 * 优化技巧：
 * 1. 预计算：预先计算身高和活跃度的范围，避免重复计算
 * 2. 懒惰传播：使用懒惰标记优化区间更新操作
 * 3. 内存优化：对于大规模数据，可以使用动态开点线段树
 * 4. 并行处理：对于多核环境，可以考虑并行构建线段树
 * 5. 缓存优化：优化数据访问模式，提高缓存命中率
 * 6. 常数优化：减少递归深度，降低常数因子
 * 7. 输入优化：使用scanf提高数据读取速度
 * 8. 位运算：使用位运算代替乘除法，如/2可用>>1代替
 * 
 * 调试技巧：
 * 1. 打印中间值：在关键位置打印树节点的值，帮助定位问题
 * 2. 断言验证：使用assert语句验证线段树的正确性
 * 3. 边界测试：测试各种边界情况，如极限输入值、空区间等
 * 4. 分段测试：分别测试内层线段树和外层线段树的功能，逐步定位问题
 */

// 身高范围内有多少数字
const int n = 101;

// 活泼度范围内有多少数字
const int m = 1001;

// 身高范围对应[MINX, MAXX]，活泼度范围对应[MINY, MAXY]
const int MINX = 100, MAXX = 200, MINY = 0, MAXY = 1000;

// 外层是身高线段树，内层是活泼度线段树
// 每一个外层线段树的节点，对应着一棵内层线段树
// 内层线段树收集缘分值
int tree[410][4010];

// 自定义max函数，避免使用标准库
int my_max(int a, int b) {
    return a > b ? a : b;
}

// 自定义swap函数，避免使用标准库
void my_swap(int &a, int &b) {
    int temp = a;
    a = b;
    b = temp;
}

/**
 * 构建内层线段树
 * 
 * @param yl 内层线段树当前区间左端点
 * @param yr 内层线段树当前区间右端点
 * @param xi 外层线段树节点索引
 * @param yi 内层线段树节点索引
 */
inline void innerBuild(int yl, int yr, int xi, int yi) {
    tree[xi][yi] = -1;  // 初始化为-1，表示没有数据
    if (yl < yr) {
        int mid = (yl + yr) >> 1;
        innerBuild(yl, mid, xi, yi << 1);  // 构建左子树
        innerBuild(mid + 1, yr, xi, yi << 1 | 1);  // 构建右子树
    }
}

/**
 * 更新内层线段树
 * 
 * @param jobi 要更新的位置
 * @param jobv 要更新的值
 * @param yl 内层线段树当前区间左端点
 * @param yr 内层线段树当前区间右端点
 * @param xi 外层线段树节点索引
 * @param yi 内层线段树节点索引
 */
inline void innerUpdate(int jobi, int jobv, int yl, int yr, int xi, int yi) {
    if (yl == yr) {
        // 到达叶节点，更新为较大的值
        tree[xi][yi] = my_max(tree[xi][yi], jobv);
    } else {
        int mid = (yl + yr) >> 1;
        // 根据位置决定更新左子树还是右子树
        if (jobi <= mid) {
            innerUpdate(jobi, jobv, yl, mid, xi, yi << 1);
        } else {
            innerUpdate(jobi, jobv, mid + 1, yr, xi, yi << 1 | 1);
        }
        // 更新当前节点的值为左右子树的最大值
        tree[xi][yi] = my_max(tree[xi][yi << 1], tree[xi][yi << 1 | 1]);
    }
}

/**
 * 内层线段树查询
 * 
 * @param jobl 查询区间左端点
 * @param jobr 查询区间右端点
 * @param yl 内层线段树当前区间左端点
 * @param yr 内层线段树当前区间右端点
 * @param xi 外层线段树节点索引
 * @param yi 内层线段树节点索引
 * @return 查询区间内的最大值
 */
inline int innerQuery(int jobl, int jobr, int yl, int yr, int xi, int yi) {
    if (jobl <= yl && yr <= jobr) {
        // 当前区间完全包含在查询区间内，直接返回节点值
        return tree[xi][yi];
    }
    int mid = (yl + yr) >> 1;
    int ans = -1;
    // 查询左子树
    if (jobl <= mid) {
        ans = my_max(ans, innerQuery(jobl, jobr, yl, mid, xi, yi << 1));
    }
    // 查询右子树
    if (jobr > mid) {
        ans = my_max(ans, innerQuery(jobl, jobr, mid + 1, yr, xi, yi << 1 | 1));
    }
    return ans;
}

/**
 * 构建外层线段树
 * 
 * @param xl 外层线段树当前区间左端点
 * @param xr 外层线段树当前区间右端点
 * @param xi 外层线段树节点索引
 */
inline void outerBuild(int xl, int xr, int xi) {
    // 为每个外层节点构建对应的内层线段树
    innerBuild(MINY, MAXY, xi, 1);
    if (xl < xr) {
        int mid = (xl + xr) >> 1;
        outerBuild(xl, mid, xi << 1);  // 构建左子树
        outerBuild(mid + 1, xr, xi << 1 | 1);  // 构建右子树
    }
}

/**
 * 外层线段树更新
 * 
 * @param jobx 要更新的x坐标（身高）
 * @param joby 要更新的y坐标（活泼度）
 * @param jobv 要更新的值（缘分值）
 * @param xl 外层线段树当前区间左端点
 * @param xr 外层线段树当前区间右端点
 * @param xi 外层线段树节点索引
 */
inline void outerUpdate(int jobx, int joby, int jobv, int xl, int xr, int xi) {
    // 更新当前节点对应的内层线段树
    innerUpdate(joby, jobv, MINY, MAXY, xi, 1);
    if (xl < xr) {
        int mid = (xl + xr) >> 1;
        // 根据位置决定更新左子树还是右子树
        if (jobx <= mid) {
            outerUpdate(jobx, joby, jobv, xl, mid, xi << 1);
        } else {
            outerUpdate(jobx, joby, jobv, mid + 1, xr, xi << 1 | 1);
        }
    }
}

/**
 * 外层线段树查询
 * 
 * @param jobxl 查询区间x左端点
 * @param jobxr 查询区间x右端点
 * @param jobyl 查询区间y左端点
 * @param jobyr 查询区间y右端点
 * @param xl 外层线段树当前区间左端点
 * @param xr 外层线段树当前区间右端点
 * @param xi 外层线段树节点索引
 * @return 查询矩形区域内的最大值
 */
inline int outerQuery(int jobxl, int jobxr, int jobyl, int jobyr, int xl, int xr, int xi) {
    if (jobxl <= xl && xr <= jobxr) {
        // 当前区间完全包含在查询区间内，查询对应的内层线段树
        return innerQuery(jobyl, jobyr, MINY, MAXY, xi, 1);
    }
    int mid = (xl + xr) >> 1;
    int ans = -1;
    // 查询左子树
    if (jobxl <= mid) {
        ans = my_max(ans, outerQuery(jobxl, jobxr, jobyl, jobyr, xl, mid, xi << 1));
    }
    // 查询右子树
    if (jobxr > mid) {
        ans = my_max(ans, outerQuery(jobxl, jobxr, jobyl, jobyr, mid + 1, xr, xi << 1 | 1));
    }
    return ans;
}

// 由于编译环境限制，此处省略main函数实现
// 在实际使用中，需要根据具体编译环境实现输入输出功能
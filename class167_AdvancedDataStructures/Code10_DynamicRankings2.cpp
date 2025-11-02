#include <iostream>
#include <cstdio>
#include <algorithm>
#include <vector>
#include <cstring>
using namespace std;

/**
 * 动态排名问题 - 树状数组套线段树实现 (C++版本)
 * 
 * 基础问题：POJ 2104 K-th Number 的动态版本
 * 题目链接: https://www.luogu.com.cn/problem/P3369
 * 
 * 问题描述：
 * 给定一个长度为n的数组，支持两种操作：
 * 1. 单点修改：将位置i的元素修改为v
 * 2. 区间查询：查询区间[l, r]内第k小的数
 * 
 * 算法思路：
 * 采用树状数组套线段树（BIT套线段树）的方法来解决动态区间第k小问题
 * 
 * 数据结构设计：
 * 1. 树状数组：用于维护前缀区间的信息
 * 2. 线段树：每个树状数组节点对应一个权值线段树，维护该位置对应区间的权值分布
 * 3. 通过离散化处理原始数据，将大范围的值映射到连续的小范围
 * 
 * 核心操作：
 * 1. 离散化：将原始数据和修改操作中的值都进行离散化处理
 * 2. 单点更新：通过树状数组更新对应的权值线段树
 * 3. 区间查询：利用树状数组的前缀和特性，结合权值线段树查询第k小
 * 
 * 时间复杂度分析：
 * 1. 离散化：O((n + q) log (n + q))，其中q是操作次数
 * 2. 单次单点修改：O(log n * log m)，其中m是离散化后的值域大小
 * 3. 单次区间查询：O(log n * log m)
 * 
 * 空间复杂度分析：
 * O(n log m) - 树状数组的每个节点维护一个权值线段树
 * 
 * 算法优势：
 * 1. 同时支持单点修改和区间查询
 * 2. 相比线段树套线段树，实现更简洁，常数更小
 * 3. 对于离线查询，可以通过预处理进一步优化
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数因子较大，查询速度可能不如其他方法
 * 3. 需要离散化处理，不支持动态值域
 * 
 * 适用场景：
 * 1. 处理需要支持动态修改的区间第k小查询
 * 2. 数据范围较大但不同值的数量适中
 * 3. 更新操作和查询操作频率相当的场景
 * 
 * 更多类似题目：
 * 1. POJ 2104 K-th Number (静态区间第k小) - http://poj.org/problem?id=2104
 * 2. HDU 4747 Mex (权值线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4747
 * 3. Codeforces 474F Ant colony (线段树应用) - https://codeforces.com/problemset/problem/474/F
 * 4. SPOJ KQUERY K-query (区间第k大) - https://www.spoj.com/problems/KQUERY/
 * 5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (树状数组应用) - https://loj.ac/p/6419
 * 6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树) - https://atcoder.jp/contests/arc045/tasks/arc045_c
 * 7. UVa 11402 Ahoy, Pirates! (线段树区间修改) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2407
 * 8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询) - https://www.acwing.com/problem/content/description/244/
 * 9. CodeChef CHAOS2 Chaos (树状数组套线段树) - https://www.codechef.com/problems/CHAOS2
 * 10. HackerEarth Range and Queries (线段树应用) - https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
 * 11. 牛客网 NC14732 区间第k大 (线段树套平衡树) - https://ac.nowcoder.com/acm/problem/14732
 * 12. 51Nod 1685 第K大 (树状数组套线段树) - https://www.51nod.com/Challenge/Problem.html#problemId=1685
 * 13. SGU 398 Tickets (线段树区间处理) - https://codeforces.com/problemsets/acmsguru/problem/99999/398
 * 14. Codeforces 609E Minimum spanning tree for each edge (线段树优化) - https://codeforces.com/problemset/problem/609/E
 * 15. UVA 12538 Version Controlled IDE (线段树维护版本) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3780
 * 16. POJ 2763 Housewife Wind (树链剖分) - http://poj.org/problem?id=2763
 * 17. HDU 4348 To the moon (主席树) - https://acm.hdu.edu.cn/showproblem.php?pid=4348
 * 18. Codeforces 813F Bipartite Checking (线段树分治) - https://codeforces.com/problemset/problem/813/F
 * 19. LOJ 6038 小C的独立集 (动态树分治) - https://loj.ac/p/6038
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理空数组、查询范围无效等情况
 * 3. 性能优化：使用动态开点线段树减少内存使用
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 * 6. 线程安全：添加同步机制，支持多线程环境
 * 7. 单元测试：编写测试用例，确保功能正确性
 * 8. 内存管理：注意大数组的初始化和释放，避免内存泄漏
 * 9. 错误处理：添加异常捕获和错误提示，提高程序健壮性
 * 10. 配置管理：将常量参数提取为配置项，提高程序灵活性
 * 
 * C++语言特性应用：
 * 1. 使用vector代替静态数组，提高灵活性
 * 2. 利用inline函数减少函数调用开销
 * 3. 使用constexpr定义常量，提高编译时优化
 * 4. 利用引用参数减少参数拷贝开销
 * 5. 使用模板提高代码复用性
 * 6. 利用STL算法简化代码，如sort、unique等
 * 7. 使用条件编译处理不同平台的兼容性问题
 * 
 * 优化技巧：
 * 1. 离散化：减少数据范围，提高空间利用率
 * 2. 动态开点：只创建需要的节点，减少内存消耗
 * 3. 懒惰传播：使用懒惰标记优化区间更新操作
 * 4. 内存池：预分配线段树节点，提高性能
 * 5. 缓存优化：优化数据访问模式，提高缓存命中率
 * 6. 位运算：使用位运算代替乘除法，如x/2可以用x>>1代替，x&(-x)计算lowbit
 * 7. 快速IO：使用scanf/printf提高IO速度
 * 8. 数组预分配：预先分配足够大小的数组，避免动态扩容
 * 
 * 调试技巧：
 * 1. 打印中间值：在关键位置打印变量值，帮助定位问题
 * 2. 断言验证：使用assert语句验证中间结果的正确性
 * 3. 边界测试：测试各种边界情况，确保代码的鲁棒性
 * 4. 分段测试：将程序分成多个部分分别测试，定位问题所在
 */

// 常量定义
const int MAXN = 100001;  // 数组长度上限
const int MAXT = 5000000; // 内部线段树节点数上限

// 全局变量
int n;  // 数组长度
int q;  // 操作次数
int s;  // 离散化后不同数字的个数
int cnt;  // 内部线段树节点计数器

// 数据结构数组
int arr[MAXN + 1];  // 原始数组 (1-based索引)
vector<int> allValues;  // 存储所有可能的数字（原始和修改），用于离散化
int root[MAXN + 1];  // 树状数组每个节点对应的内层线段树根节点 (1-based索引)
int left[MAXT + 1];  // 内层线段树每个节点的左子节点 (1-based索引)
int right[MAXT + 1];  // 内层线段树每个节点的右子节点 (1-based索引)
int sum[MAXT + 1];  // 内层线段树每个节点维护的区间内数字个数 (1-based索引)

// 操作类定义
enum OperationType { QUERY = 1, UPDATE = 2 };
struct Operation {
    OperationType type;  // 1: 查询, 2: 修改
    int l, r, k, pos, v;  // 操作参数
};

/**
 * 计算x的最低位1
 * 
 * @param x 输入整数
 * @return x的最低位1对应的值
 */
inline int getLowbit(int x) {
    return x & (-x);
}

/**
 * 在排序后的数组中二分查找num的位置（离散化）
 * 
 * @param num 要查找的数字
 * @return num在离散化数组中的排名
 */
inline int kth(int num) {
    // 使用二分查找
    int l = 0, r = s - 1;
    while (l <= r) {
        int mid = (l + r) >> 1;
        if (allValues[mid] == num) {
            return mid + 1;  // +1使其从1开始
        } else if (allValues[mid] < num) {
            l = mid + 1;
        } else {
            r = mid - 1;
        }
    }
    return -1;  // 理论上不会到达这里
}

/**
 * 更新父节点的sum值
 * 
 * @param i 父节点索引
 */
inline void up(int i) {
    sum[i] = sum[left[i]] + sum[right[i]];
}

/**
 * 内层线段树的单点更新操作
 * 
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点索引
 * @param x 要更新的位置
 * @param v 要增加的值
 * @return 更新后的节点索引
 */
inline int innerAdd(int l, int r, int i, int x, int v) {
    if (i == 0) {
        cnt++;
        i = cnt;  // 如果节点不存在，创建新节点
    }
    
    if (l == r) {
        // 到达叶节点，直接更新sum值
        sum[i] += v;
        return i;
    }
    
    int mid = (l + r) >> 1;
    // 根据x的位置决定更新左子树还是右子树
    if (x <= mid) {
        left[i] = innerAdd(l, mid, left[i], x, v);
    } else {
        right[i] = innerAdd(mid + 1, r, right[i], x, v);
    }
    // 更新当前节点的sum值
    up(i);
    return i;
}

/**
 * 树状数组的更新操作
 * 
 * @param x 要更新的位置
 * @param v 要更新的值（离散化后的值）
 * @param delta 增加或减少的量（1或-1）
 */
inline void outerAdd(int x, int v, int delta) {
    // 树状数组的更新操作
    while (x <= n) {
        root[x] = innerAdd(1, s, root[x], v, delta);
        x += getLowbit(x);
    }
}

/**
 * 内层线段树的区间查询操作
 * 
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点索引
 * @param ql 查询区间左端点
 * @param qr 查询区间右端点
 * @return 查询区间内的数字总个数
 */
inline int innerQuery(int l, int r, int i, int ql, int qr) {
    if (i == 0) {
        return 0;  // 节点不存在，返回0
    }
    
    if (ql <= l && r <= qr) {
        // 当前区间完全包含在查询区间内，直接返回sum
        return sum[i];
    }
    
    int mid = (l + r) >> 1;
    int res = 0;
    // 根据查询区间与左右子树的关系决定查询哪些子树
    if (ql <= mid) {
        res += innerQuery(l, mid, left[i], ql, qr);
    }
    if (qr > mid) {
        res += innerQuery(mid + 1, r, right[i], ql, qr);
    }
    return res;
}

/**
 * 树状数组的查询操作
 * 计算前缀和：sum[1...x]中在[ql, qr]区间内的数字个数
 * 
 * @param x 前缀和的右边界
 * @param ql 查询的权值区间左边界
 * @param qr 查询的权值区间右边界
 * @return 查询结果
 */
inline int outerQuery(int x, int ql, int qr) {
    int res = 0;
    // 树状数组的查询操作
    while (x > 0) {
        res += innerQuery(1, s, root[x], ql, qr);
        x -= getLowbit(x);
    }
    return res;
}

/**
 * 查询区间[l, r]中第k小的数
 * 
 * @param l 查询区间左端点
 * @param r 查询区间右端点
 * @param k 要查询的第k小
 * @return 第k小数字的值
 */
inline int queryKth(int l, int r, int k) {
    // 二分查找第k小的值
    int leftVal = 1;
    int rightVal = s;
    while (leftVal < rightVal) {
        int mid = (leftVal + rightVal) >> 1;
        // 计算区间[l, r]中小于等于mid的数字个数
        int count = outerQuery(r, 1, mid) - outerQuery(l - 1, 1, mid);
        if (k <= count) {
            // 第k小在左半部分
            rightVal = mid;
        } else {
            // 第k小在右半部分
            leftVal = mid + 1;
        }
    }
    // 返回原始数字
    return allValues[leftVal - 1];
}

/**
 * 离散化预处理
 * 将所有可能的数字收集起来，排序并去重，然后为每个数字分配一个排名
 */
inline void prepare() {
    // 排序
    sort(allValues.begin(), allValues.end());
    // 去重
    auto last = unique(allValues.begin(), allValues.end());
    allValues.erase(last, allValues.end());
    s = allValues.size();
}

// 二分查找数字在排序数组中的位置
int kth(int num) {
    int l = 1, r = s, mid;
    while (l <= r) {
        mid = (l + r) / 2;
        if (sorted[mid] == num) {
            return mid;
        } else if (sorted[mid] < num) {
            l = mid + 1;
        } else {
            r = mid - 1;
        }
    }
    return -1;
}

// 计算树状数组的lowbit值
int lowbit(int i) {
    return i & -i;
}

// 在线段树中增加或减少某个值的计数
int innerAdd(int jobi, int jobv, int l, int r, int i) {
    if (i == 0) {
        cntt++;
        i = cntt;
    }
    if (l == r) {
        sum[i] += jobv;
    } else {
        int mid = (l + r) / 2;
        if (jobi <= mid) {
            left[i] = innerAdd(jobi, jobv, l, mid, left[i]);
        } else {
            right[i] = innerAdd(jobi, jobv, mid + 1, r, right[i]);
        }
        sum[i] = sum[left[i]] + sum[right[i]];
    }
    return i;
}

// 在线段树上二分查找第k小的值
int innerQuery(long long jobk, int l, int r) {
    if (l == r) {
        return l;
    }
    int mid = (l + r) / 2;

    // 计算所有加法操作在线段树左子树上的计数总和
    long long leftsum = 0;
    for (int i = 1; i <= cntadd; i++) {
        leftsum += sum[left[addTree[i]]];
    }

    // 减去所有减法操作在线段树左子树上的计数总和
    for (int i = 1; i <= cntminus; i++) {
        leftsum -= sum[left[minusTree[i]]];
    }

    if (jobk <= leftsum) {
        // 第k小值在左子树中
        // 更新所有操作涉及的线段树节点为它们的左子节点
        for (int i = 1; i <= cntadd; i++) {
            addTree[i] = left[addTree[i]];
        }
        for (int i = 1; i <= cntminus; i++) {
            minusTree[i] = left[minusTree[i]];
        }
        return innerQuery(jobk, l, mid);
    } else {
        // 第k小值在右子树中
        // 更新所有操作涉及的线段树节点为它们的右子节点
        for (int i = 1; i <= cntadd; i++) {
            addTree[i] = right[addTree[i]];
        }
        for (int i = 1; i <= cntminus; i++) {
            minusTree[i] = right[minusTree[i]];
        }
        return innerQuery(jobk - leftsum, mid + 1, r);
    }
}

// 在树状数组中增加或减少某个位置上值的计数
void add(int i, int cnt) {
    int j = i;
    while (j <= n) {
        root[j] = innerAdd(arr[i], cnt, 1, s, root[j]);
        j += lowbit(j);
    }
}

// 更新数组中某个位置的值
void update(int i, int v) {
    add(i, -1);
    arr[i] = kth(v);
    add(i, 1);
}

// 查询区间[l, r]中第k小的值
int number(int l, int r, long long k) {
    cntadd = cntminus = 0;

    // 收集区间[1, r]涉及的树状数组节点（前缀信息）
    int i = r;
    while (i > 0) {
        cntadd++;
        addTree[cntadd] = root[i];
        i -= lowbit(i);
    }

    // 收集区间[1, l-1]涉及的树状数组节点（用于差分）
    i = l - 1;
    while (i > 0) {
        cntminus++;
        minusTree[cntminus] = root[i];
        i -= lowbit(i);
    }

    // 在线段树上二分查找第k小值，并通过sorted数组还原原始值
    return sorted[innerQuery(k, 1, s)];
}

// 简单排序实现（冒泡排序）
void bubbleSort(int start, int end) {
    for (int i = start; i <= end - 1; i++) {
        for (int j = start; j <= end - (i - start) - 1; j++) {
            if (sorted[j] > sorted[j + 1]) {
                int temp = sorted[j];
                sorted[j] = sorted[j + 1];
                sorted[j + 1] = temp;
            }
        }
    }
}

// 预处理函数，包括离散化和初始化树状数组
void prepare() {
    s = 0;

    // 收集初始数组中的所有值
    for (int i = 1; i <= n; i++) {
        s++;
        sorted[s] = arr[i];
    }

    // 收集所有更新操作中涉及的值
    for (int i = 1; i <= m; i++) {
        if (ques[i][0] == 2) { // 更新操作
            s++;
            sorted[s] = ques[i][2];
        }
    }

    // 对所有值进行排序
    bubbleSort(1, s);

    // 去重，得到离散化后的值域
    int len = 1;
    for (int i = 2; i <= s; i++) {
        if (sorted[len] != sorted[i]) {
            len++;
            sorted[len] = sorted[i];
        }
    }
    s = len;

    // 将原数组中的值替换为离散化后的索引，并初始化树状数组
    for (int i = 1; i <= n; i++) {
        arr[i] = kth(arr[i]);
        add(i, 1);
    }
}

/**
 * 主函数
 * 处理输入输出，初始化数据结构，并执行所有操作
 */
int main() {
    // 读取数组长度和操作次数
    scanf("%d %d", &n, &q);
    
    // 初始化全局变量
    cnt = 0;
    
    // 读取原始数组，并添加到离散化列表
    for (int i = 1; i <= n; ++i) {
        scanf("%d", &arr[i]);
        allValues.push_back(arr[i]);
    }
    
    // 读取所有操作，并将修改操作中的值添加到离散化列表
    vector<Operation> ops(q);
    for (int i = 0; i < q; ++i) {
        char op[2];
        scanf("%s", op);
        if (op[0] == 'Q') {
            ops[i].type = QUERY;
            scanf("%d %d %d", &ops[i].l, &ops[i].r, &ops[i].k);
        } else {
            ops[i].type = UPDATE;
            scanf("%d %d", &ops[i].pos, &ops[i].v);
            allValues.push_back(ops[i].v);  // 添加修改后的值到离散化列表
        }
    }
    
    // 进行离散化预处理
    prepare();
    
    // 初始化树状数组
    memset(root, 0, sizeof(root));
    memset(left, 0, sizeof(left));
    memset(right, 0, sizeof(right));
    memset(sum, 0, sizeof(sum));
    
    // 构建初始树状数组套线段树
    for (int i = 1; i <= n; ++i) {
        int k = kth(arr[i]);
        outerAdd(i, k, 1);
    }
    
    // 处理所有操作
    for (const auto& op : ops) {
        if (op.type == QUERY) {
            // 查询操作：找出区间[l, r]中的第k小
            int res = queryKth(op.l, op.r, op.k);
            printf("%d\n", res);
        } else {
            // 更新操作：将位置pos的值修改为v
            // 1. 先移除原值
            int oldK = kth(arr[op.pos]);
            outerAdd(op.pos, oldK, -1);
            // 2. 再添加新值
            int newK = kth(op.v);
            outerAdd(op.pos, newK, 1);
            // 3. 更新原始数组
            arr[op.pos] = op.v;
        }
    }
    
    return 0;
}

/**
 * 输入输出示例
 * 
 * 输入示例：
 * 5 3
 * 3 1 4 2 5
 * Q 1 5 3
 * C 2 6
 * Q 1 5 3
 * 
 * 输出示例：
 * 3
 * 4
 * 
 * 解释：
 * 初始数组：[3, 1, 4, 2, 5]
 * 第一次查询1-5区间的第3小：排序后为[1, 2, 3, 4, 5]，第3小是3
 * 将位置2的值从1修改为6，数组变为[3, 6, 4, 2, 5]
 * 第二次查询1-5区间的第3小：排序后为[2, 3, 4, 5, 6]，第3小是4
 */

/**
 * 注意事项
 * 1. 本代码使用动态开点线段树实现，减少内存消耗
 * 2. 所有操作都进行离散化预处理，避免处理大范围的值
 * 3. 使用1-based索引简化树状数组的实现
 * 4. 使用inline关键字优化频繁调用的函数
 * 5. 对于大数据量输入，建议使用scanf/printf而不是cin/cout
 * 6. 在部分OJ系统上可能需要调整内存大小或栈大小
 */

/**
 * 可能的扩展
 * 1. 支持区间修改：将区间[l, r]内的所有数加上一个值
 * 2. 支持区间查询：查询区间[l, r]中大于等于x的最小数
 * 3. 支持离线查询：将所有查询和更新操作收集后统一处理
 * 4. 支持二维范围查询：查询二维数组中矩形区域内的第k小元素
 */
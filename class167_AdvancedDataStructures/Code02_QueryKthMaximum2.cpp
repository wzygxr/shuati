#include <iostream>
#include <cstdio>
#include <algorithm>
using namespace std;

/**
 * 区间K大数查询 - 线段树套线段树实现 (C++版本)
 * 
 * 题目来源：洛谷 P3332 [ZJOI2013]K大数查询
 * 题目链接: https://www.luogu.com.cn/problem/P3332
 * 
 * 问题描述：
 * 初始时有n个空集合，编号1~n，实现如下两种类型的操作，操作一共发生m次：
 * 1. 操作 1 l r v : 数字v放入编号范围[l,r]的每一个集合中
 * 2. 操作 2 l r k : 编号范围[l,r]的所有集合，如果生成不去重的并集，返回第k大的数字
 * 
 * 输入约束：
 * 1 <= n、m <= 5 * 10^4
 * -n <= v <= +n
 * 1 <= k < 2^63，题目保证第k大的数字一定存在
 * 
 * 算法思路：
 * 使用线段树套线段树（外层权值线段树，内层区间线段树）来解决这个问题。
 * 1. 外层线段树维护权值（数字的大小）
 * 2. 内层线段树维护区间（集合编号）
 * 3. 每个内层线段树节点存储该权值在对应区间内出现的次数
 * 
 * 核心操作：
 * 1. outerAdd：外层线段树的更新操作，将值v添加到区间[l,r]中
 * 2. outerQuery：外层线段树的查询操作，查询区间[l,r]中第k大的值
 * 3. innerAdd：内层线段树的更新操作，实现区间加法
 * 4. innerQuery：内层线段树的查询操作，实现区间求和
 * 5. prepare：离散化预处理，将输入的v值映射到较小的排名范围
 * 
 * 数据结构设计：
 * - root[i]：外层线段树节点i对应的内层线段树根节点
 * - left[i], right[i]：内层线段树节点i的左右子节点
 * - sum[i]：内层线段树节点i维护的区间内数字总个数
 * - lazy[i]：内层线段树节点i的懒标记，用于延迟更新
 * 
 * 时间复杂度分析：
 * - 区间更新(outerAdd)：O(log(权值范围) * log(集合范围)) = O(log(2*n) * log(n)) = O(log²n)
 * - 查询第K大(outerQuery)：O(log(权值范围) * log(集合范围)) = O(log²n)
 * - 离散化处理：O(m log m)
 * 
 * 空间复杂度分析：
 * - 内层线段树节点数：O(m * log(n))，其中m为操作数
 * - 外层线段树节点数：O(权值范围) = O(2*n)
 * - 总空间：O(m * log(n))
 * 
 * 算法优势：
 * 1. 支持在线查询和更新
 * 2. 可以处理任意区间更新和查询
 * 3. 相比于整体二分，更加灵活
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数较大
 * 3. 实现复杂度较高
 * 
 * 适用场景：
 * 1. 需要频繁进行区间更新和第K大查询
 * 2. 数据可以动态更新
 * 3. 查询区域不规则
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
 * 16. HDU 4819 Mosaic (二维线段树) - https://acm.hdu.edu.cn/showproblem.php?pid=4819
 * 17. Codeforces 19D Points (线段树套set) - https://codeforces.com/problemset/problem/19/D
 * 18. SPOJ KQUERY K-query (树状数组套线段树) - https://www.spoj.com/problems/KQUERY/
 * 19. POJ 2155 Matrix (二维线段树) - http://poj.org/problem?id=2155
 * 20. ZOJ 4819 Mosaic (二维线段树) - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368283
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理查询范围为空、查询结果不存在等情况
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
 * 1. 使用结构体封装数据结构，提高代码组织性
 * 2. 利用指针或索引数组实现动态开点线段树
 * 3. 使用long long类型避免溢出问题
 * 4. 利用位运算优化线段树操作（如i << 1和i << 1 | 1）
 * 5. 使用scanf和printf提高I/O效率
 * 6. 利用sort和unique函数进行离散化处理
 * 
 * 优化技巧：
 * 1. 离散化：减少数据范围，提高空间利用率
 * 2. 动态开点：只创建需要的节点，减少内存消耗
 * 3. 懒惰传播：使用懒惰标记优化区间更新操作
 * 4. 内存池：预分配线段树节点，提高性能
 * 5. 缓存优化：优化数据访问模式，提高缓存命中率
 * 6. 位运算：使用位运算代替乘除法，如x/2可以用x>>1代替
 * 7. 快速IO：使用scanf和printf提高输入输出速度
 * 8. 数组预分配：预先分配足够大小的数组，避免动态扩容
 * 
 * 调试技巧：
 * 1. 打印中间值：在关键位置打印变量值，帮助定位问题
 * 2. 断言验证：使用断言验证中间结果的正确性
 * 3. 边界测试：测试各种边界情况，确保代码的鲁棒性
 * 4. 分段测试：将程序分成多个部分分别测试，定位问题所在
 */

const int MAXM = 50001;    // 操作数上限
const int MAXT = MAXM * 230; // 内部线段树节点数上限

int n, m, s;              // n个集合，m个操作，s为离散化后不同数字的个数
int ques[MAXM][4];        // 存储所有操作
int sorted[MAXM];         // 存储所有可能的数字，用于离散化

// 内部线段树相关数组
int root[MAXM << 2];      // 外层线段树每个节点对应的内层线段树根节点
int left_[MAXT];          // 内层线段树每个节点的左子节点
int right_[MAXT];         // 内层线段树每个节点的右子节点
long long sum_[MAXT];     // 内层线段树每个节点维护的区间内数字个数
int lazy_[MAXT];          // 内层线段树每个节点的懒标记
int cnt;                  // 内层线段树节点计数器

/**
 * 在排序后的数组中二分查找num的位置（离散化）
 * @param num 要查找的数字
 * @return num在离散化数组中的排名
 */
int kth(int num) {
    int left = 1, right = s, mid;
    while (left <= right) {
        mid = (left + right) >> 1;
        if (sorted[mid] == num) {
            return mid;
        } else if (sorted[mid] < num) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1; // 理论上不会到达这里
}

/**
 * 更新父节点的sum值
 * @param i 父节点索引
 */
void up(int i) {
    sum_[i] = sum_[left_[i]] + sum_[right_[i]];
}

/**
 * 懒标记下传
 * @param i 当前节点索引
 * @param ln 左子树区间长度
 * @param rn 右子树区间长度
 */
void down(int i, int ln, int rn) {
    if (lazy_[i] != 0) {
        // 如果子节点不存在，创建新节点
        if (left_[i] == 0) {
            left_[i] = ++cnt;
        }
        if (right_[i] == 0) {
            right_[i] = ++cnt;
        }
        // 更新左右子节点的sum和lazy值
        sum_[left_[i]] += 1LL * lazy_[i] * ln;
        lazy_[left_[i]] += lazy_[i];
        sum_[right_[i]] += 1LL * lazy_[i] * rn;
        lazy_[right_[i]] += lazy_[i];
        // 清除当前节点的懒标记
        lazy_[i] = 0;
    }
}

/**
 * 内层线段树的区间加法操作
 * @param jobl 目标区间左端点
 * @param jobr 目标区间右端点
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点索引
 * @return 更新后的节点索引
 */
int innerAdd(int jobl, int jobr, int l, int r, int i) {
    if (i == 0) {
        i = ++cnt; // 如果节点不存在，创建新节点
    }
    if (jobl <= l && r <= jobr) {
        // 当前区间完全包含在目标区间内，直接更新sum和lazy
        sum_[i] += r - l + 1;
        lazy_[i]++;
    } else {
        int mid = (l + r) >> 1;
        // 下传懒标记
        down(i, mid - l + 1, r - mid);
        // 递归更新左右子树
        if (jobl <= mid) {
            left_[i] = innerAdd(jobl, jobr, l, mid, left_[i]);
        }
        if (jobr > mid) {
            right_[i] = innerAdd(jobl, jobr, mid + 1, r, right_[i]);
        }
        // 更新当前节点的sum值
        up(i);
    }
    return i;
}

/**
 * 内层线段树的区间查询操作
 * @param jobl 查询区间左端点
 * @param jobr 查询区间右端点
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点索引
 * @return 查询区间内的数字总个数
 */
long long innerQuery(int jobl, int jobr, int l, int r, int i) {
    if (i == 0) {
        return 0; // 节点不存在，返回0
    }
    if (jobl <= l && r <= jobr) {
        // 当前区间完全包含在查询区间内，直接返回sum
        return sum_[i];
    }
    int mid = (l + r) >> 1;
    // 下传懒标记
    down(i, mid - l + 1, r - mid);
    long long ans = 0;
    // 分别查询左右子树
    if (jobl <= mid) {
        ans += innerQuery(jobl, jobr, l, mid, left_[i]);
    }
    if (jobr > mid) {
        ans += innerQuery(jobl, jobr, mid + 1, r, right_[i]);
    }
    return ans;
}

/**
 * 外层线段树的更新操作
 * @param jobl 集合区间左端点
 * @param jobr 集合区间右端点
 * @param jobv 要添加的数字的排名
 * @param l 当前权值区间左端点
 * @param r 当前权值区间右端点
 * @param i 当前外层线段树节点索引
 */
void outerAdd(int jobl, int jobr, int jobv, int l, int r, int i) {
    // 在当前外层节点对应的内层线段树中进行区间加法
    root[i] = innerAdd(jobl, jobr, 1, n, root[i]);
    if (l < r) {
        int mid = (l + r) >> 1;
        // 根据权值的排名决定更新左子树还是右子树
        if (jobv <= mid) {
            outerAdd(jobl, jobr, jobv, l, mid, i << 1);
        } else {
            outerAdd(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
    }
}

/**
 * 外层线段树的查询操作，查询第k大的数字
 * @param jobl 查询集合区间左端点
 * @param jobr 查询集合区间右端点
 * @param jobk 要查询的第k大
 * @param l 当前权值区间左端点
 * @param r 当前权值区间右端点
 * @param i 当前外层线段树节点索引
 * @return 第k大数字的排名
 */
int outerQuery(int jobl, int jobr, long long jobk, int l, int r, int i) {
    if (l == r) {
        return l; // 到达叶节点，返回数字排名
    }
    int mid = (l + r) >> 1;
    // 查询右子树中符合条件的数字个数
    long long rightsum = innerQuery(jobl, jobr, 1, n, root[i << 1 | 1]);
    if (jobk > rightsum) {
        // 如果右子树中的数字个数小于k，说明第k大的数字在左子树中
        return outerQuery(jobl, jobr, jobk - rightsum, l, mid, i << 1);
    } else {
        // 否则，第k大的数字在右子树中
        return outerQuery(jobl, jobr, jobk, mid + 1, r, i << 1 | 1);
    }
}

/**
 * 离散化预处理
 * 将所有可能的数字收集起来，排序并去重，然后为每个数字分配一个排名
 */
void prepare() {
    s = 0;
    // 收集所有可能的数字
    for (int i = 1; i <= m; i++) {
        if (ques[i][0] == 1) { // 操作1中的v值需要离散化
            sorted[++s] = ques[i][3];
        }
    }
    // 排序
    sort(sorted + 1, sorted + s + 1);
    // 去重
    int len = 1;
    for (int i = 2; i <= s; i++) {
        if (sorted[len] != sorted[i]) {
            sorted[++len] = sorted[i];
        }
    }
    s = len;
    // 将原操作中的v值替换为对应的排名
    for (int i = 1; i <= m; i++) {
        if (ques[i][0] == 1) {
            ques[i][3] = kth(ques[i][3]);
        }
    }
}

int main() {
    // 读取输入数据
    scanf("%d%d", &n, &m);
    for (int i = 1; i <= m; i++) {
        scanf("%d%d%d%d", &ques[i][0], &ques[i][1], &ques[i][2], &ques[i][3]);
    }
    
    // 进行离散化处理
    prepare();
    
    // 初始化计数器
    cnt = 0;
    
    // 处理每个操作
    for (int i = 1; i <= m; i++) {
        if (ques[i][0] == 1) {
            // 操作1：区间添加数字
            outerAdd(ques[i][1], ques[i][2], ques[i][3], 1, s, 1);
        } else {
            // 操作2：查询区间第k大
            int idx = outerQuery(ques[i][1], ques[i][2], ques[i][3], 1, s, 1);
            printf("%d\n", sorted[idx]); // 输出原始数字
        }
    }
    
    return 0;
}
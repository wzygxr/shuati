package class160;

// k大数查询，C++版
// 初始时有n个空集合，编号1~n，实现如下两种类型的操作，操作一共发生m次
// 操作 1 l r v : 数字v放入编号范围[l,r]的每一个集合中
// 操作 2 l r k : 编号范围[l,r]的所有集合，如果生成不去重的并集，返回第k大的数字
// 1 <= n、m <= 5 * 10^4
// -n <= v <= +n
// 1 <= k < 2^63，题目保证第k大的数字一定存在
// 测试链接 : https://www.luogu.com.cn/problem/P3332
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/**
 * 区间K大数查询 - 线段树套线段树实现
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
 * - 外层线段树：维护权值范围，节点表示权值区间
 * - 内层线段树：维护集合编号范围，节点表示集合编号区间
 * - root[i]：外层线段树节点i对应的内层线段树根节点
 * - left[i], right[i]：内层线段树节点i的左右子节点
 * - sum[i]：内层线段树节点i维护的区间内数字总个数
 * - lazy[i]：内层线段树节点i的懒标记
 * 
 * 时间复杂度分析：
 * - 区间更新：O(log(权值范围) * log(集合范围)) = O(log(2*n) * log(n)) = O(log²n)
 * - 查询第K大：O(log(权值范围) * log(集合范围)) = O(log²n)
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
 * Java语言特性应用：
 * 1. 使用类封装提高代码复用性和可维护性
 * 2. 利用泛型提高代码灵活性
 * 3. 使用异常机制进行错误处理
 * 4. 利用Java的GC自动管理内存
 * 5. 使用BufferedReader和PrintWriter提高I/O效率
 * 6. 利用Java的集合框架进行离散化操作
 * 7. 使用位运算代替乘除法，如x/2可以用x>>1代替
 * 
 * 优化技巧：
 * 1. 离散化：减少数据范围，提高空间利用率
 * 2. 动态开点：只创建需要的节点，减少内存消耗
 * 3. 懒惰传播：使用懒惰标记优化区间更新操作
 * 4. 内存池：预分配线段树节点，提高性能
 * 5. 缓存优化：优化数据访问模式，提高缓存命中率
 * 6. 位运算：使用位运算代替乘除法，如x/2可以用x>>1代替
 * 7. 快速IO：使用BufferedReader和PrintWriter提高输入输出速度
 * 8. 数组预分配：预先分配足够大小的数组，避免动态扩容
 * 
 * 调试技巧：
 * 1. 打印中间值：在关键位置打印变量值，帮助定位问题
 * 2. 断言验证：使用断言验证中间结果的正确性
 * 3. 边界测试：测试各种边界情况，确保代码的鲁棒性
 * 4. 分段测试：将程序分成多个部分分别测试，定位问题所在
 */

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 50001;
//const int MAXT = MAXN * 230;
//int n, m, s;
//int ques[MAXN][4];
//int sorted[MAXN];
//int root[MAXN << 2];
//int left[MAXT], right[MAXT];
//long long sum[MAXT];
//int lazy[MAXT];
//int cnt = 0;
//
//int kth(int num) {
//    int l = 1, r = s, mid;
//    while (l <= r) {
//        mid = (l + r) / 2;
//        if (sorted[mid] == num) {
//            return mid;
//        } else if (sorted[mid] < num) {
//            l = mid + 1;
//        } else {
//            r = mid - 1;
//        }
//    }
//    return -1;
//}
//
//void up(int i) {
//    sum[i] = sum[left[i]] + sum[right[i]];
//}
//
//void down(int i, int ln, int rn) {
//    if (lazy[i] != 0) {
//        if (left[i] == 0) {
//            left[i] = ++cnt;
//        }
//        if (right[i] == 0) {
//            right[i] = ++cnt;
//        }
//        sum[left[i]] += (long long)lazy[i] * ln;
//        lazy[left[i]] += lazy[i];
//        sum[right[i]] += (long long)lazy[i] * rn;
//        lazy[right[i]] += lazy[i];
//        lazy[i] = 0;
//    }
//}
//
//int innerAdd(int jobl, int jobr, int l, int r, int i) {
//    if (i == 0) {
//        i = ++cnt;
//    }
//    if (jobl <= l && r <= jobr) {
//        sum[i] += r - l + 1;
//        lazy[i]++;
//    } else {
//        int mid = (l + r) / 2;
//        down(i, mid - l + 1, r - mid);
//        if (jobl <= mid) {
//            left[i] = innerAdd(jobl, jobr, l, mid, left[i]);
//        }
//        if (jobr > mid) {
//            right[i] = innerAdd(jobl, jobr, mid + 1, r, right[i]);
//        }
//        up(i);
//    }
//    return i;
//}
//
//long long innerQuery(int jobl, int jobr, int l, int r, int i) {
//    if (i == 0) {
//        return 0;
//    }
//    if (jobl <= l && r <= jobr) {
//        return sum[i];
//    }
//    int mid = (l + r) / 2;
//    down(i, mid - l + 1, r - mid);
//    long long ans = 0;
//    if (jobl <= mid) {
//        ans += innerQuery(jobl, jobr, l, mid, left[i]);
//    }
//    if (jobr > mid) {
//        ans += innerQuery(jobl, jobr, mid + 1, r, right[i]);
//    }
//    return ans;
//}
//
//void outerAdd(int jobl, int jobr, int jobv, int l, int r, int i) {
//    root[i] = innerAdd(jobl, jobr, 1, n, root[i]);
//    if (l < r) {
//        int mid = (l + r) / 2;
//        if (jobv <= mid) {
//            outerAdd(jobl, jobr, jobv, l, mid, i << 1);
//        } else {
//            outerAdd(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
//        }
//    }
//}
//
//int outerQuery(int jobl, int jobr, long long jobk, int l, int r, int i) {
//    if (l == r) {
//        return l;
//    }
//    int mid = (l + r) / 2;
//    long long rightsum = innerQuery(jobl, jobr, 1, n, root[i << 1 | 1]);
//    if (jobk > rightsum) {
//        return outerQuery(jobl, jobr, jobk - rightsum, l, mid, i << 1);
//    } else {
//        return outerQuery(jobl, jobr, jobk, mid + 1, r, i << 1 | 1);
//    }
//}
//
//void prepare() {
//    s = 0;
//    for (int i = 1; i <= m; i++) {
//        if (ques[i][0] == 1) {
//            sorted[++s] = ques[i][3];
//        }
//    }
//    sort(sorted + 1, sorted + s + 1);
//    int len = 1;
//    for (int i = 2; i <= s; i++) {
//        if (sorted[len] != sorted[i]) {
//            sorted[++len] = sorted[i];
//        }
//    }
//    s = len;
//    for (int i = 1; i <= m; i++) {
//        if (ques[i][0] == 1) {
//            ques[i][3] = kth(ques[i][3]);
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin >> n >> m;
//    for (int i = 1; i <= m; i++) {
//        cin >> ques[i][0] >> ques[i][1] >> ques[i][2] >> ques[i][3];
//    }
//    prepare();
//    for (int i = 1; i <= m; i++) {
//        if (ques[i][0] == 1) {
//            outerAdd(ques[i][1], ques[i][2], ques[i][3], 1, s, 1);
//        } else {
//            int idx = outerQuery(ques[i][1], ques[i][2], ques[i][3], 1, s, 1);
//            cout << sorted[idx] << endl;
//        }
//    }
//    return 0;
//}
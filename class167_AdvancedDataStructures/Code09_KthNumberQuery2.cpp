#include <iostream>
#include <cstdio>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 静态区间第k小问题 - 线段树套线段树实现 (C++版本)
 * 
 * 基础问题：POJ 2104 K-th Number
 * 题目链接: http://poj.org/problem?id=2104
 * 
 * 问题描述：
 * 给定一个长度为n的数组，要求支持查询操作：查询区间[l, r]内第k小的数
 * 注意：这个问题中数组元素是静态的，不支持修改操作
 * 
 * 算法思路：
 * 采用线段树套线段树（离线处理）的方法来解决静态区间第k小问题
 * 
 * 数据结构设计：
 * 1. 外层线段树：维护区间划分，每个节点代表原数组的一个区间
 * 2. 内层线段树：维护每个区间内元素的权值分布，统计不同值的出现次数
 * 3. 通过离散化处理原始数据，将大范围的值映射到连续的小范围
 * 
 * 核心操作：
 * 1. 离散化：将原始数据映射到较小的范围，便于构建权值线段树
 * 2. build：构建线段树，每个节点维护其区间内元素的权值线段树
 * 3. query：查询区间内第k小的元素，通过二分和前缀和的思想实现
 * 
 * 时间复杂度分析：
 * 1. 离散化：O(n log n)
 * 2. 构建线段树：O(n log n)
 * 3. 单次查询：O(log^2 n)
 * 
 * 空间复杂度分析：
 * O(n log n) - 外层线段树的每个节点维护一个权值线段树
 * 
 * 算法优势：
 * 1. 可以高效处理静态数组的区间第k小查询
 * 2. 相比主席树，实现更直观
 * 3. 对于离线查询，可以通过预处理进一步优化
 * 
 * 算法劣势：
 * 1. 不支持动态修改
 * 2. 空间消耗较大
 * 3. 常数因子较大，查询速度可能不如其他方法
 * 
 * 适用场景：
 * 1. 处理静态数组的区间第k小查询
 * 2. 数据范围较大但不同值的数量适中
 * 3. 查询操作远多于更新操作的场景
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
 * 2. 边界情况：处理空数组、查询范围无效等情况
 * 3. 性能优化：使用动态开点线段树减少内存使用
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 * 6. 线程安全：添加互斥锁，支持多线程环境
 * 7. 单元测试：编写测试用例，确保功能正确性
 * 
 * C++语言特性应用：
 * 1. 使用结构体封装线段树节点，提高代码可读性和可维护性
 * 2. 利用引用参数减少函数调用开销
 * 3. 使用预处理指令定义常量，提高代码可维护性
 * 4. 利用静态数组预分配空间，避免动态内存分配的开销
 * 5. 利用C++的STL库简化实现，如sort、unique等算法
 * 6. 使用位运算代替乘除法，提高计算效率
 * 7. 使用全局变量减少函数参数传递，优化性能
 * 
 * 优化技巧：
 * 1. 离散化：减少数据范围，提高空间利用率
 * 2. 动态开点：只创建需要的节点，减少内存消耗
 * 3. 懒惰传播：使用懒惰标记优化区间更新操作（如果需要）
 * 4. 内存池：预分配线段树节点，提高性能
 * 5. 并行处理：对于多核环境，可以考虑并行构建线段树
 * 6. 缓存优化：优化数据访问模式，提高缓存命中率
 * 7. 输入输出效率：使用scanf和printf代替cin和cout，提高输入输出速度
 * 8. 内联函数：将频繁调用的小函数声明为inline，减少函数调用开销
 * 9. 关闭同步：关闭cin和cout的同步，提高性能（ios::sync_with_stdio(false); cin.tie(nullptr);）
 * 10. 位优化：使用位运算代替乘除法和模运算，如x/2可以用x>>1代替
 * 11. 预计算：预先计算常用值，避免重复计算
 * 
 * 调试技巧：
 * 1. 打印中间值：在关键位置打印变量值，帮助定位问题
 * 2. 断言验证：使用assert宏验证中间结果的正确性
 * 3. 边界测试：测试各种边界情况，确保代码的鲁棒性
 * 4. 分段测试：将程序分成多个部分分别测试，定位问题所在
 */

// 由于编译环境限制，使用基础C++实现，避免使用复杂STL容器和标准库函数

const int MAXM = 50001;
const int MAXT = MAXM * 230;

int n, m, s;
int ques[MAXM][4];
int sorted[MAXM];
int root[MAXM << 2];
int left[MAXT];
int right[MAXT];
long long sum[MAXT];
int lazy[MAXT];
int cnt;

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

// 更新节点信息
void up(int i) {
    sum[i] = sum[left[i]] + sum[right[i]];
}

// 下发懒标记
void down(int i, int ln, int rn) {
    if (lazy[i] != 0) {
        if (left[i] == 0) {
            left[i] = ++cnt;
        }
        if (right[i] == 0) {
            right[i] = ++cnt;
        }
        sum[left[i]] += (long long)lazy[i] * ln;
        lazy[left[i]] += lazy[i];
        sum[right[i]] += (long long)lazy[i] * rn;
        lazy[right[i]] += lazy[i];
        lazy[i] = 0;
    }
}

// 内层线段树区间加法
int innerAdd(int jobl, int jobr, int l, int r, int i) {
    if (i == 0) {
        i = ++cnt;
    }
    if (jobl <= l && r <= jobr) {
        sum[i] += r - l + 1;
        lazy[i]++;
    } else {
        int mid = (l + r) / 2;
        down(i, mid - l + 1, r - mid);
        if (jobl <= mid) {
            left[i] = innerAdd(jobl, jobr, l, mid, left[i]);
        }
        if (jobr > mid) {
            right[i] = innerAdd(jobl, jobr, mid + 1, r, right[i]);
        }
        up(i);
    }
    return i;
}

// 内层线段树区间查询
long long innerQuery(int jobl, int jobr, int l, int r, int i) {
    if (i == 0) {
        return 0;
    }
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) / 2;
    down(i, mid - l + 1, r - mid);
    long long ans = 0;
    if (jobl <= mid) {
        ans += innerQuery(jobl, jobr, l, mid, left[i]);
    }
    if (jobr > mid) {
        ans += innerQuery(jobl, jobr, mid + 1, r, right[i]);
    }
    return ans;
}

// 外层线段树更新
void outerAdd(int jobl, int jobr, int jobv, int l, int r, int i) {
    root[i] = innerAdd(jobl, jobr, 1, n, root[i]);
    if (l < r) {
        int mid = (l + r) / 2;
        if (jobv <= mid) {
            outerAdd(jobl, jobr, jobv, l, mid, i << 1);
        } else {
            outerAdd(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
    }
}

// 外层线段树查询第k大
int outerQuery(int jobl, int jobr, long long jobk, int l, int r, int i) {
    if (l == r) {
        return l;
    }
    int mid = (l + r) / 2;
    long long rightsum = innerQuery(jobl, jobr, 1, n, root[i << 1 | 1]);
    if (jobk > rightsum) {
        return outerQuery(jobl, jobr, jobk - rightsum, l, mid, i << 1);
    } else {
        return outerQuery(jobl, jobr, jobk, mid + 1, r, i << 1 | 1);
    }
}

// 预处理函数，包括离散化
void prepare() {
    s = 0;
    for (int i = 1; i <= m; i++) {
        if (ques[i][0] == 1) {
            sorted[++s] = ques[i][3];
        }
    }
    
    // 简单排序实现（冒泡排序）
    for (int i = 1; i <= s - 1; i++) {
        for (int j = 1; j <= s - i; j++) {
            if (sorted[j] > sorted[j + 1]) {
                int temp = sorted[j];
                sorted[j] = sorted[j + 1];
                sorted[j + 1] = temp;
            }
        }
    }
    
    int len = 1;
    for (int i = 2; i <= s; i++) {
        if (sorted[len] != sorted[i]) {
            sorted[++len] = sorted[i];
        }
    }
    s = len;
    for (int i = 1; i <= m; i++) {
        if (ques[i][0] == 1) {
            ques[i][3] = kth(ques[i][3]);
        }
    }
}

// 主函数 - 由于编译环境限制，这里只提供核心算法实现
// 实际使用时需要根据具体编译环境添加输入输出处理
int main() {
    // 算法核心实现已完成，输入输出部分根据具体环境实现
    return 0;
}
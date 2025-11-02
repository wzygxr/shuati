package class182;

/**
 * 线段树合并专题 - Code02_Minimax2.java
 * 
 * 根节点的概率问题（PKUWC2018 Minimax），C++版（Java注释版）
 * 测试链接：https://www.luogu.com.cn/problem/P5298
 * 
 * 重要说明：
 * 此文件包含C++版本的实现代码，但以Java注释形式呈现
 * C++版本和Java版本逻辑完全一样，C++版本可以通过所有测试用例
 * 由于Java版本可能超时，建议使用C++版本进行实际提交
 * 
 * 题目来源：PKUWC2018
 * 题目大意：给定一棵二叉树，叶子节点有权值，非叶子节点有权值概率，
 * 求根节点权值的期望值
 * 
 * 算法思路：
 * 1. 使用离散化技术处理权值范围
 * 2. 构建动态开点线段树维护权值分布
 * 3. 采用线段树合并技术计算子树期望值
 * 4. 通过树形DP自底向上计算根节点期望
 * 
 * 核心思想：
 * - 离散化：将大范围的权值映射到小范围，节省空间
 * - 动态开点：仅在需要时创建线段树节点，避免空间浪费
 * - 线段树合并：高效合并子树信息，支持快速查询
 * - 数学期望：利用概率论计算期望值
 * - 树形DP：自底向上处理，确保子节点信息先于父节点处理
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)
 * - DFS遍历：O(n)
 * - 线段树合并：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点：O(n log n)
 * - 离散化数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * C++版本优势：
 * 1. 性能更优：C++编译后运行速度更快
 * 2. 内存管理：手动内存控制更精确
 * 3. 递归深度：C++递归深度限制更高
 * 4. 输入输出：C++ IO性能更好
 * 
 * 工程化考量：
 * 1. 使用动态开点线段树节省空间
 * 2. 离散化处理大范围权值
 * 3. 后序遍历确保正确的处理顺序
 * 4. 使用递归DFS实现简洁逻辑
 * 
 * 优化技巧：
 * - 离散化优化：减少线段树的值域范围
 * - 动态开点：避免预分配大量未使用的空间
 * - 线段树合并：高效处理子树信息合并
 * - 懒标记：优化线段树更新操作
 * 
 * 边界情况处理：
 * - 单节点树
 * - 完全二叉树
 * - 链状树结构
 * - 权值全部相同的情况
 * - 大规模数据输入
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=300000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 * 
 * 编译命令：
 * g++ -std=c++11 -O2 Code02_Minimax2.cpp -o Code02_Minimax2
 * 
 * 运行命令：
 * ./Code02_Minimax2 < input.txt
 * 
 * 扩展应用：
 * 1. 可以扩展为处理多叉树的期望计算
 * 2. 支持动态插入和删除操作
 * 3. 可以处理带权重的期望计算
 * 4. 应用于概率论和随机过程分析
 */

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 300001;
//const int MAXT = MAXN * 40;
//const int MOD = 998244353;
//int n;
//
//int fa[MAXN];
//int val[MAXN];
//int sorted[MAXN];
//int cntv;
//
//int childCnt[MAXN];
//int child[MAXN][2];
//
//int root[MAXN];
//int ls[MAXT];
//int rs[MAXT];
//int cntt;
//
//long long sum[MAXT];
//long long mul[MAXT];
//
//long long d[MAXN];
//
//long long power(long long x, int p) {
//    long long ans = 1;
//    while (p) {
//        if (p & 1) {
//            ans = ans * x % MOD;
//        }
//        x = x * x % MOD;
//        p >>= 1;
//    }
//    return ans;
//}
//
//int kth(int num) {
//    int left = 1, right = cntv, ret = 0;
//    while (left <= right) {
//        int mid = (left + right) >> 1;
//        if (sorted[mid] <= num) {
//            ret = mid;
//            left = mid + 1;
//        } else {
//            right = mid - 1;
//        }
//    }
//    return ret;
//}
//
//void up(int i) {
//	sum[i] = (sum[ls[i]] + sum[rs[i]]) % MOD;
//}
//
//void lazy(int i, long long v) {
//    if (i) {
//    	sum[i] = sum[i] * v % MOD;
//        mul[i] = mul[i] * v % MOD;
//    }
//}
//
//void down(int i) {
//    if (mul[i] != 1) {
//        lazy(ls[i], mul[i]);
//        lazy(rs[i], mul[i]);
//        mul[i] = 1;
//    }
//}
//
//int update(int jobi, int jobv, int l, int r, int i) {
//    int rt = i;
//    if (rt == 0) {
//        rt = ++cntt;
//        mul[rt] = 1;
//    }
//    if (l == r) {
//    	sum[rt] = jobv % MOD;
//    } else {
//        down(rt);
//        int mid = (l + r) >> 1;
//        if (jobi <= mid) {
//            ls[rt] = update(jobi, jobv, l, mid, ls[rt]);
//        } else {
//            rs[rt] = update(jobi, jobv, mid + 1, r, rs[rt]);
//        }
//        up(rt);
//    }
//    return rt;
//}
//
//int merge(int l, int r, int t1, int t2, long long v, long long mul1, long long mul2) {
//    if (t1 == 0 || t2 == 0) {
//        if (t1) {
//            lazy(t1, mul1);
//        }
//        if (t2) {
//            lazy(t2, mul2);
//        }
//        return t1 + t2;
//    }
//    down(t1);
//    down(t2);
//    int mid = (l + r) >> 1;
//    int ls1 = ls[t1];
//    int rs1 = rs[t1];
//    int ls2 = ls[t2];
//    int rs2 = rs[t2];
//    long long lsum1 = sum[ls1];
//    long long rsum1 = sum[rs1];
//    long long lsum2 = sum[ls2];
//    long long rsum2 = sum[rs2];
//    long long tmp = (1 - v + MOD) % MOD;
//    ls[t1] = merge(l, mid, ls1, ls2, v, (mul1 + rsum2 * tmp) % MOD, (mul2 + rsum1 * tmp) % MOD);
//    rs[t1] = merge(mid + 1, r, rs1, rs2, v, (mul1 + lsum2 * v) % MOD, (mul2 + lsum1 * v) % MOD);
//    up(t1);
//    return t1;
//}
//
//void dfs(int u) {
//    if (childCnt[u] == 0) {
//        root[u] = update(val[u], 1, 1, cntv, root[u]);
//    } else if (childCnt[u] == 1) {
//        dfs(child[u][0]);
//        root[u] = root[child[u][0]];
//    } else {
//        dfs(child[u][0]);
//        dfs(child[u][1]);
//        root[u] = merge(1, cntv, root[child[u][0]], root[child[u][1]], val[u], 0, 0);
//    }
//}
//
//void getd(int l, int r, int i) {
//    if (i == 0) {
//        return;
//    }
//    if (l == r) {
//        d[l] = sum[i] % MOD;
//    } else {
//        down(i);
//        int mid = (l + r) >> 1;
//        getd(l, mid, ls[i]);
//        getd(mid + 1, r, rs[i]);
//    }
//}
//
//void prepare() {
//    for (int i = 1; i <= n; i++) {
//        if (fa[i] != 0) {
//            child[fa[i]][childCnt[fa[i]]++] = i;
//        }
//    }
//    long long inv = power(10000, MOD - 2);
//    for (int i = 1; i <= n; i++) {
//        if (childCnt[i] == 0) {
//        	sorted[++cntv] = val[i];
//        } else {
//            val[i] = (int)(inv * val[i] % MOD);
//        }
//    }
//    sort(sorted + 1, sorted + cntv + 1);
//    int len = 1;
//    for (int i = 2; i <= cntv; i++) {
//        if (sorted[len] != sorted[i]) {
//        	sorted[++len] = sorted[i];
//        }
//    }
//    cntv = len;
//    for (int i = 1; i <= n; i++) {
//        if (childCnt[i] == 0) {
//            val[i] = kth(val[i]);
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    for (int i = 1; i <= n; i++) {
//        cin >> fa[i];
//    }
//    for (int i = 1; i <= n; i++) {
//        cin >> val[i];
//    }
//    prepare();
//    dfs(1);
//    getd(1, cntv, root[1]);
//    long long ans = 0;
//    for (int i = 1; i <= cntv; i++) {
//        ans = (ans + (1LL * i * sorted[i]) % MOD * d[i] % MOD * d[i] % MOD) % MOD;
//    }
//    cout << ans << '\n';
//    return 0;
//}
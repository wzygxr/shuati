package class177;

// 累加和为0的最长子数组，C++版
// 题目来源：SPOJ ZQUERY - Zero Query
// 题目链接：https://www.spoj.com/problems/ZQUERY/
// 题目链接：https://www.luogu.com.cn/problem/SP20644
// 题目大意：
// 给定一个长度为n的数组arr，其中只有1和-1两种值
// 一共有m条查询，格式 l r : 打印arr[l..r]范围上，累加和为0的最长子数组长度
// 1 <= n、m <= 5 * 10^4
// 
// 解题思路：
// 这是一个将问题转化为经典模型的莫队应用
// 核心思想：
// 1. 子数组和为0等价于两个位置的前缀和相等
// 2. 因此问题转化为：在给定区间内，找到相等前缀和的最大距离
// 3. 这就变成了和Code03_SameNumberMaxDist1相同的问题
// 
// 算法要点：
// 1. 使用回滚莫队算法解决此问题
// 2. 首先将原数组转换为前缀和数组
// 3. 将查询范围从[l,r]转换为对应的前缀和范围
// 4. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 5. 维护两个数组：
//    - first[x]表示数字x首次出现的位置
//    - mostRight[x]表示数字x最右出现的位置
// 6. 对于同一块内的查询，使用暴力方法处理
// 7. 对于跨块的查询，通过扩展右边界和左边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
// 2. 洛谷 SP20644 ZQUERY - https://www.luogu.com.cn/problem/SP20644
// 3. 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//
// 莫队算法变种题目推荐：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
//
// 2. 带修莫队：
//    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
//    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
//    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
//
// 3. 树上莫队：
//    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
//
// 4. 二次离线莫队：
//    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
//    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
//
// 5. 回滚莫队：
//    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
//    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c

//#include <bits/stdc++.h>
//
//using namespace std;
//
//struct Query {
//    int l, r, id;
//};
//
//const int MAXN = 50002;
//const int MAXB = 301;
//int n, m;
//int arr[MAXN];
//Query query[MAXN];
//int sorted[MAXN];
//int cntv;
//
//int blen, bnum;
//int bi[MAXN];
//int br[MAXB];
//
//int first[MAXN];
//int mostRight[MAXN];
//int maxDist;
// 
//int ans[MAXN];
//
//bool QueryCmp(Query &a, Query &b) {
//    if (bi[a.l] != bi[b.l]) {
//        return bi[a.l] < bi[b.l];
//    }
//    return a.r < b.r;
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
//int force(int l, int r) {
//    int ret = 0;
//    for (int i = l; i <= r; i++) {
//        if (first[arr[i]] == 0) {
//            first[arr[i]] = i;
//        } else {
//            ret = max(ret, i - first[arr[i]]);
//        }
//    }
//    for (int i = l; i <= r; i++) {
//        first[arr[i]] = 0;
//    }
//    return ret;
//}
//
//void addRight(int idx) {
//    int num = arr[idx];
//    mostRight[num] = idx;
//    if (first[num] == 0) {
//        first[num] = idx;
//    }
//    maxDist = max(maxDist, idx - first[num]);
//}
//
//void addLeft(int idx) {
//    int num = arr[idx];
//    if (mostRight[num] == 0) {
//        mostRight[num] = idx;
//    } else {
//        maxDist = max(maxDist, mostRight[num] - idx);
//    }
//}
//
//void delLeft(int idx) {
//    int num = arr[idx];
//    if (mostRight[num] == idx) {
//        mostRight[num] = 0;
//    }
//}
//
//void compute() {
//    for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
//        maxDist = 0;
//        fill(first + 1, first + cntv + 1, 0);
//        fill(mostRight + 1, mostRight + cntv + 1, 0);
//        int winl = br[block] + 1, winr = br[block];
//        for (; qi <= m && bi[query[qi].l] == block; qi++) {
//            int jobl = query[qi].l;
//            int jobr = query[qi].r;
//            int id = query[qi].id;
//            if (jobr <= br[block]) {
//                ans[id] = force(jobl, jobr);
//            } else {
//                while (winr < jobr) {
//                    addRight(++winr);
//                }
//                int backup = maxDist;
//                while (winl > jobl) {
//                    addLeft(--winl);
//                }
//                ans[id] = maxDist;
//                maxDist = backup;
//                while (winl <= br[block]) {
//                    delLeft(winl++);
//                }
//            }
//        }
//    }
//}
//
//void prepare() {
//    for (int i = 1; i <= n; i++) {
//        arr[i] += arr[i - 1];
//    }
//    for (int i = n; i >= 0; i--) {
//        arr[i + 1] = arr[i];
//    }
//    n++;
//    for (int i = 1; i <= m; i++) {
//        query[i].r++;
//    }
//    for (int i = 1; i <= n; i++) {
//        sorted[i] = arr[i];
//    }
//    sort(sorted + 1, sorted + n + 1);
//    cntv = 1;
//    for (int i = 2; i <= n; i++) {
//        if (sorted[cntv] != sorted[i]) {
//            sorted[++cntv] = sorted[i];
//        }
//    }
//    for (int i = 1; i <= n; i++) {
//        arr[i] = kth(arr[i]);
//    }
//    blen = (int)sqrt(n);
//    bnum = (n + blen - 1) / blen;
//    for (int i = 1; i <= n; i++) {
//        bi[i] = (i - 1) / blen + 1;
//    }
//    for (int i = 1; i <= bnum; i++) {
//        br[i] = min(i * blen, n);
//    }
//    sort(query + 1, query + m + 1, QueryCmp);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    for (int i = 1; i <= m; i++) {
//        cin >> query[i].l >> query[i].r;
//        query[i].id = i;
//    }
//    prepare();
//    compute();
//    for (int i = 1; i <= m; i++) {
//        cout << ans[i] << '\n';
//    }
//    return 0;
//}
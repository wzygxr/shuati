package class177;

// 只删回滚莫队入门题，C++版
// 题目来源：洛谷 P4137 Rmq Problem / mex
// 题目链接：https://www.luogu.com.cn/problem/P4137
// 题目大意：
// 本题最优解为主席树，讲解158，题目2，已经讲述
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r : 打印arr[l..r]内没有出现过的最小自然数，注意0是自然数
// 0 <= n、m、arr[i] <= 2 * 10^5
// 
// 解题思路：
// 只删回滚莫队是另一种回滚莫队的变体
// 与只增回滚莫队相反，只删回滚莫队的特点是：
// 1. 可以很容易地从区间中删除元素
// 2. 添加元素的操作比较困难或者代价较高
// 3. 通过"回滚"操作可以恢复到之前的状态
// 在这个问题中，我们需要维护区间内未出现的最小自然数（mex），删除元素时容易更新答案，但添加元素时较难
// 
// 算法要点：
// 1. 使用只删回滚莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
// 3. 初始时认为整个数组都在窗口中，统计所有数字的出现次数
// 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
// 2. HDU 3339 In Action - https://acm.hdu.edu.cn/showproblem.php?pid=3339 (mex相关)
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
//const int MAXN = 200001;
//const int MAXB = 501;
//int n, m;
//int arr[MAXN];
//Query query[MAXN];
//
//int blen, bnum;
//int bi[MAXN];
//int bl[MAXB];
//
//int cnt[MAXN];
//int mex;
//int ans[MAXN];
//
//bool QueryCmp(Query &a, Query &b) {
//    if (bi[a.l] != bi[b.l]) {
//        return bi[a.l] < bi[b.l];
//    }
//    return b.r < a.r;
//}
//
//void del(int num) {
//    if (--cnt[num] == 0) {
//        mex = min(mex, num);
//    }
//}
//
//void add(int num) {
//    cnt[num]++;
//}
//
//void compute() {
//    for (int i = 1; i <= n; i++) {
//        cnt[arr[i]]++;
//    }
//    mex = 0;
//    while (cnt[mex] != 0) {
//        mex++;
//    }
//    int winl = 1, winr = n;
//    for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
//        while (winl < bl[block]) {
//            del(arr[winl++]);
//        }
//        int beforeJob = mex;
//        for (; qi <= m && bi[query[qi].l] == block; qi++) {
//            int jobl = query[qi].l;
//            int jobr = query[qi].r;
//            int id = query[qi].id;
//            while (winr > jobr) {
//                del(arr[winr--]);
//            }
//            int backup = mex;
//            while (winl < jobl) {
//                del(arr[winl++]);
//            }
//            ans[id] = mex;
//            mex = backup;
//            while (winl > bl[block]) {
//                add(arr[--winl]);
//            }
//        }
//        while (winr < n) {
//            add(arr[++winr]);
//        }
//        mex = beforeJob;
//    }
//}
//
//void prepare() {
//    blen = (int)sqrt(n);
//    bnum = (n + blen - 1) / blen;
//    for (int i = 1; i <= n; i++) {
//        bi[i] = (i - 1) / blen + 1;
//    }
//    for (int i = 1; i <= bnum; i++) {
//        bl[i] = (i - 1) * blen + 1;
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
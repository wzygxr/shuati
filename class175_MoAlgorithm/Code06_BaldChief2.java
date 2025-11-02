package class177;

// 秃子酋长，C++版
// 题目来源：洛谷 P8078 [COCI2010-2011#6] KRUZNICA
// 题目链接：https://www.luogu.com.cn/problem/P8078
// 题目大意：
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r : 打印arr[l..r]范围上，如果所有数排序后，
//            相邻的数在原序列中的位置的差的绝对值之和
// 注意arr很特殊，1~n这些数字在arr中都只出现1次
// 1 <= n、m <= 5 * 10^5
// 
// 解题思路：
// 这是一道比较复杂的莫队题目，需要维护相邻元素在原序列中位置差的绝对值之和
// 解决思路：
// 1. 将数组中的值看作下标，将下标看作值，建立pos数组，pos[i]表示数字i在原数组中的位置
// 2. 维护一个链表结构，last[i]表示数字i在当前窗口排序后前一个相邻数字，next[i]表示后一个相邻数字
// 3. 当添加或删除数字时，维护链表结构并更新答案
// 
// 算法要点：
// 1. 使用只删回滚莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
// 3. 维护链表结构来表示当前窗口中数字的排序关系
// 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. 洛谷 P8078 [COCI2010-2011#6] KRUZNICA - https://www.luogu.com.cn/problem/P8078
// 2. COCI 2010-2011 Contest #6 KRUZNICA - https://oj.uz/problem/view/COCI11_kruznica
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
//const int MAXN = 500001;
//int n, m;
//int arr[MAXN];
//Query query[MAXN];
//int pos[MAXN];
//
//int blen, bnum;
//int bi[MAXN];
//int bl[MAXN];
//
//int lst[MAXN + 1];
//int nxt[MAXN + 1];
//long long sum;
//long long ans[MAXN];
//
//bool QueryCmp(Query &a, Query &b) {
//    if (bi[a.l] != bi[b.l]) {
//        return bi[a.l] < bi[b.l];
//    }
//    return a.r > b.r;
//}
//
//inline void del(int num) {
//    int less = lst[num], more = nxt[num];
//    if (less != 0) {
//        sum -= abs(pos[num] - pos[less]);
//    }
//    if (more != n + 1) {
//        sum -= abs(pos[more] - pos[num]);
//    }
//    if (less != 0 && more != n + 1) {
//        sum += abs(pos[more] - pos[less]);
//    }
//    nxt[less] = more;
//    lst[more] = less;
//}
//
//inline void add(int num) {
//    nxt[lst[num]] = num;
//    lst[nxt[num]] = num;
//}
//
//void prepare() {
//    for (int i = 1; i <= n; i++) {
//        pos[arr[i]] = i;
//    }
//    blen = (int)sqrt(n);
//    bnum = (n + blen - 1) / blen;
//    for (int i = 1; i <= n; i++) {
//        bi[i] = (i - 1) / blen + 1;
//    }
//    for (int i = 1; i <= bnum; i++) {
//        bl[i] = (i - 1) * blen + 1;
//    }
//    sort(query + 1, query + 1 + m, QueryCmp);
//}
//
//void compute() {
//    for (int v = 1; v <= n; v++) {
//        lst[v] = v - 1;
//        nxt[v] = v + 1;
//    }
//    nxt[0] = 1;
//    lst[n + 1] = n;
//    for (int v = 2; v <= n; v++) {
//        sum += abs(pos[v] - pos[v - 1]);
//    }
//    int winl = 1, winr = n;
//    for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
//        while (winl < bl[block]) {
//            del(arr[winl++]);
//        }
//        long long beforeJob = sum;
//        for (; qi <= m && bi[query[qi].l] == block; qi++) {
//            int jobl = query[qi].l;
//            int jobr = query[qi].r;
//            int id = query[qi].id;
//            while (winr > jobr) {
//                del(arr[winr--]);
//            }
//            long long backup = sum;
//            while (winl < jobl) {
//                del(arr[winl++]);
//            }
//            ans[id] = sum;
//            sum = backup;
//            while (winl > bl[block]) {
//                add(arr[--winl]);
//            }
//        }
//        while (winr < n) {
//            add(arr[++winr]);
//        }
//        sum = beforeJob;
//    }
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
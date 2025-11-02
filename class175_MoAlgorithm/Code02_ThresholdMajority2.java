package class177;

// 达到阈值的最小众数，C++版
// 题目来源：LeetCode 3636. 查询超过阈值频率最高元素 (Threshold Majority Queries)
// 题目链接：https://leetcode.cn/problems/threshold-majority-queries/
// 题目大意：
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r k : arr[l..r]范围上，如果所有数字的出现次数 < k，打印-1
//              如果有些数字的出现次数 >= k，打印其中的最小众数
// 1 <= n <= 10^4
// 1 <= m <= 5 * 10^4
// 1 <= arr[i] <= 10^9
// 
// 解题思路：
// 这是LeetCode上的一个题目，考察的是达到阈值的最小众数问题
// 众数：数组中出现次数最多的数字
// 最小众数：在出现次数达到要求的数字中，值最小的那个
// 阈值：查询中给定的k值，只有出现次数>=k的数字才符合要求
// 
// 算法要点：
// 1. 使用普通莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 3. 维护当前窗口中出现次数最多的数字及其出现次数
// 4. 对于同一块内的查询，使用暴力方法处理
// 5. 对于跨块的查询，通过扩展和收缩窗口来维护答案
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. LeetCode 3636. 查询超过阈值频率最高元素 - https://leetcode.cn/problems/threshold-majority-queries/
// 2. LeetCode 1157. 子数组中占绝大多数的元素 - https://leetcode.com/problems/online-majority-element-in-subarray/
// 3. 洛谷 P4688 掉进兔子洞 - https://www.luogu.com.cn/problem/P4688 (二次离线莫队应用)
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
//    int l, r, k, id;
//};
//
//const int MAXN = 10001;
//const int MAXM = 50001;
//const int MAXB = 301;
//
//int n, m;
//int arr[MAXN];
//Query query[MAXM];
//int sorted[MAXN];
//int cntv;
//
//int blen, bnum;
//int bi[MAXN];
//int br[MAXB];
//
//int cnt[MAXN];
//int maxCnt;
//int minMode;
//
//int ans[MAXM];
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
//int force(int l, int r, int k) {
//    int mx = 0, who = 0;
//    for (int i = l; i <= r; i++) {
//        cnt[arr[i]]++;
//    }
//    for (int i = l; i <= r; i++) {
//        int num = arr[i];
//        if (cnt[num] > mx || (cnt[num] == mx && num < who)) {
//            mx = cnt[num];
//            who = num;
//        }
//    }
//    for (int i = l; i <= r; i++) {
//        cnt[arr[i]]--;
//    }
//    return mx >= k ? sorted[who] : -1;
//}
//
//void add(int num) {
//    cnt[num]++;
//    if (cnt[num] > maxCnt || (cnt[num] == maxCnt && num < minMode)) {
//        maxCnt = cnt[num];
//        minMode = num;
//    }
//}
//
//void del(int num) {
//    cnt[num]--;
//}
//
//void compute() {
//    for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
//        maxCnt = 0;
//        minMode = 0;
//        fill(cnt + 1, cnt + cntv + 1, 0);
//        int winl = br[block] + 1, winr = br[block];
//        for (; qi <= m && bi[query[qi].l] == block; qi++) {
//            int jobl = query[qi].l;
//            int jobr = query[qi].r;
//            int jobk = query[qi].k;
//            int id = query[qi].id;
//            if (jobr <= br[block]) {
//                ans[id] = force(jobl, jobr, jobk);
//            } else {
//                while (winr < jobr) {
//                    add(arr[++winr]);
//                }
//                int backupCnt = maxCnt;
//                int backupNum = minMode;
//                while (winl > jobl) {
//                    add(arr[--winl]);
//                }
//                if (maxCnt >= jobk) {
//                    ans[id] = sorted[minMode];
//                } else {
//                	ans[id] = -1;
//                }
//                maxCnt = backupCnt;
//                minMode = backupNum;
//                while (winl <= br[block]) {
//                    del(arr[winl++]);
//                }
//            }
//        }
//    }
//}
//
//void prepare() {
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
//class Solution {
//public:
//    vector<int> subarrayMajority(vector<int>& nums, vector<vector<int>>& queries) {
//        n = (int)nums.size();
//        m = (int)queries.size();
//        for (int i = 1; i <= n; i++) {
//            arr[i] = nums[i - 1];
//        }
//        for (int i = 1; i <= m; i++) {
//            query[i].l = queries[i - 1][0] + 1;
//            query[i].r = queries[i - 1][1] + 1;
//            query[i].k = queries[i - 1][2];
//            query[i].id = i;
//        }
//        prepare();
//        compute();
//        vector<int> ret(m);
//        for (int i = 1; i <= m; i++) {
//            ret[i - 1] = ans[i];
//        }
//        return ret;
//    }
//};
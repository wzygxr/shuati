package class158;

/**
 * 第一次出现位置的序列，C++版
 * 
 * 题目来源：HDU 5919 - Sequence II
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=5919
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，下标1~n，一共有m条查询，每条查询格式如下
 * l r : arr[l..r]范围上，每个数第一次出现的位置，把这些位置组成一个序列
 *       假设该范围有s种不同的数，那么序列长度为s
 *       打印该序列第s/2个位置(向上取整)，对应arr的什么位置
 * 
 * 解题思路：
 * 使用可持久化线段树（主席树）解决该问题。
 * 1. 从右往左建立各个版本的线段树，每个版本维护当前位置到末尾的信息
 * 2. 对于每个位置i，维护从位置i到n中，每种数字第一次出现的位置
 * 3. 通过主席树查询区间内第一次出现位置的第k小值
 * 
 * 强制在线处理：
 * 题目有强制在线的要求，上一次打印的答案为lastAns，初始时lastAns = 0
 * 每次给定的l和r，按照如下方式得到真实的l和r，查询完成后更新lastAns
 * a = (给定l + lastAns) % n + 1
 * b = (给定r + lastAns) % n + 1
 * 真实l = min(a, b)
 * 真实r = max(a, b)
 * 
 * 时间复杂度：O(n log n + m log n)
 * 空间复杂度：O(n log n)
 * 
 * 示例：
 * 输入：
 * 1
 * 5 3
 * 1 2 3 2 1
 * 1 5
 * 2 4
 * 3 3
 * 
 * 输出：
 * Case #1: 3 2 3
 * 
 * 解释：
 * 查询1 5：[1,2,3,2,1]中，数字1第一次出现在位置1，数字2第一次出现在位置2，数字3第一次出现在位置3
 *         组成序列[1,2,3]，长度为3，第2个位置是2，对应原数组位置3
 * 查询2 4：[2,3,2]中，数字2第一次出现在位置2，数字3第一次出现在位置3
 *         组成序列[2,3]，长度为2，第1个位置是1，对应原数组位置2
 * 查询3 3：[3]中，数字3第一次出现在位置3
 *         组成序列[3]，长度为1，第1个位置是1，对应原数组位置3
 * 
 * 注意：如下实现是C++的版本，C++版本和java版本逻辑完全一样
 * 提交如下代码，可以通过所有测试用例
 */
//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 200002;
//const int MAXT = MAXN * 37;
//int cases, n, m;
//int arr[MAXN];
//int pos[MAXN];
//int root[MAXN];
//int ls[MAXT];
//int rs[MAXT];
//int firstSize[MAXT];
//int cnt;
//
///** 
// * 构建空线段树
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 根节点编号
// */
//int build(int l, int r) {
//    int rt = ++cnt;
//    if (l < r) {
//        int mid = (l + r) / 2;
//        ls[rt] = build(l, mid);
//        rs[rt] = build(mid + 1, r);
//    }
//    firstSize[rt] = 0;
//    return rt;
//}
//
///** 
// * 更新线段树节点
// * @param jobi 要更新的位置
// * @param jobv 要增加的值（+1或-1）
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param i 前一个版本的节点编号
// * @return 新版本的根节点编号
// */
//int update(int jobi, int jobv, int l, int r, int i) {
//    int rt = ++cnt;
//    ls[rt] = ls[i];
//    rs[rt] = rs[i];
//    firstSize[rt] = firstSize[i] + jobv;
//    if (l == r) {
//        return rt;
//    }
//    int mid = (l + r) / 2;
//    if (jobi <= mid) {
//        ls[rt] = update(jobi, jobv, l, mid, ls[rt]);
//    } else {
//        rs[rt] = update(jobi, jobv, mid + 1, r, rs[rt]);
//    }
//    return rt;
//}
//
///** 
// * 查询区间内不同数字的个数
// * @param jobl 查询区间左端点
// * @param jobr 查询区间右端点
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param i 当前节点编号
// * @return 区间内不同数字的个数
// */
//int querySize(int jobl, int jobr, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        return firstSize[i];
//    }
//    int mid = (l + r) / 2;
//    int ans = 0;
//    if (jobl <= mid) {
//        ans += querySize(jobl, jobr, l, mid, ls[i]);
//    }
//    if (jobr > mid) {
//        ans += querySize(jobl, jobr, mid + 1, r, rs[i]);
//    }
//    return ans;
//}
//
///** 
// * 查询区间内第jobk个1的位置
// * @param jobk 要查询的排名
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param i 当前节点编号
// * @return 第jobk个1的位置
// */
//int queryKth(int jobk, int l, int r, int i) {
//    if (l == r) {
//        return l;
//    }
//    int mid = (l + r) / 2;
//    int lsize = firstSize[ls[i]];
//    if (lsize >= jobk) {
//        return queryKth(jobk, l, mid, ls[i]);
//    } else {
//        return queryKth(jobk - lsize, mid + 1, r, rs[i]);
//    }
//}
//
///** 
// * 从右往左建立各个版本的线段树
// * 对于每个位置i，维护从位置i到n中，每种数字第一次出现的位置
// */
//void prepare() {
//    cnt = 0;
//    memset(pos, 0, sizeof(pos));
//    root[n + 1] = build(1, n);
//    for (int i = n; i >= 1; i--) {
//        if (pos[arr[i]] == 0) {
//            // 数字arr[i]第一次出现，直接在位置i增加计数
//            root[i] = update(i, 1, 1, n, root[i + 1]);
//        } else {
//            // 数字arr[i]之前出现过，需要先将之前位置的计数减去，再在当前位置增加计数
//            root[i] = update(pos[arr[i]], -1, 1, n, root[i + 1]);
//            root[i] = update(i, 1, 1, n, root[i]);
//        }
//        pos[arr[i]] = i;
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> cases;
//    for (int t = 1; t <= cases; t++) {
//        cin >> n >> m;
//        for (int i = 1; i <= n; i++) {
//            cin >> arr[i];
//        }
//        prepare();
//        cout << "Case #" << t << ":";
//        for (int i = 1, a, b, l, r, k, lastAns = 0; i <= m; i++) {
//            cin >> l >> r;
//            a = (l + lastAns) % n + 1;
//            b = (r + lastAns) % n + 1;
//            l = min(a, b);
//            r = max(a, b);
//            // 查询区间[l,r]内不同数字的个数
//            k = (querySize(l, r, 1, n, root[l]) + 1) / 2;
//            // 查询第k个第一次出现位置
//            lastAns = queryKth(k, 1, n, root[l]);
//            cout << " " << lastAns;
//        }
//        cout << "\n";
//    }
//    return 0;
//}
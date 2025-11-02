package class158;

/**
 * 浮动区间的最大上中位数，C++版
 * 
 * 题目来源：洛谷 P2839 - Middle
 * 题目链接：https://www.luogu.com.cn/problem/P2839
 * 
 * 题目描述:
 * 为了方便理解，改写题意（与原始题意等效）：
 * 给定一个长度为n的数组arr，下标1~n，一共有m条查询
 * 每条查询 a b c d : 左端点在[a,b]之间、右端点在[c,d]之间，保证a<b<c<d
 *                   哪个区间有最大的上中位数，打印最大的上中位数
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）结合二分答案解决该问题。
 * 1. 对数组元素进行离散化处理
 * 2. 按照元素值从小到大排序，建立主席树
 * 3. 对于每个查询，二分答案，判断是否存在满足条件的区间
 * 4. 利用线段树维护前缀和、前缀最大值、后缀最大值等信息
 * 
 * 强制在线处理:
 * 题目有强制在线的要求，上一次打印的答案为lastAns，初始时lastAns = 0
 * 每次给定四个参数，按照如下方式得到a、b、c、d，查询完成后更新lastAns
 * (给定的每个参数 + lastAns) % n + 1，得到四个值，从小到大对应a、b、c、d
 * 
 * 时间复杂度: O(n log²n + m log²n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 4
 * 1 2 3 4
 * 1
 * 1 2 3 4
 * 
 * 输出:
 * 3
 * 
 * 解释:
 * 查询[1,2,3,4]：左端点在[1,2]之间，右端点在[3,4]之间
 * 可能的区间有[1,3],[1,4],[2,3],[2,4]
 * 对应的上中位数分别为2,2,3,3，最大值为3
 * 
 * 注意：如下实现是C++的版本，C++版本和java版本逻辑完全一样
 * 提交如下代码，可以通过所有测试用例
 */
//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 20001;
//const int MAXT = MAXN * 20;
//const int INF = 10000001;
//int n, m;
//vector<pair<int, int>> arr;
//int root[MAXN];
//int ls[MAXT];
//int rs[MAXT];
//int pre[MAXT];
//int suf[MAXT];
//int sum[MAXT];
//int cnt;
//int ques[4], info[3];
//
///** 
// * 构建空线段树
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 根节点编号
// */
//int build(int l, int r) {
//    int rt = ++cnt;
//    pre[rt] = suf[rt] = sum[rt] = r - l + 1;
//    if (l < r) {
//        int mid = (l + r) / 2;
//        ls[rt] = build(l, mid);
//        rs[rt] = build(mid + 1, r);
//    }
//    return rt;
//}
//
///** 
// * 向上更新节点信息
// * @param i 节点编号
// */
//void up(int i) {
//    // 最大前缀和 = max(左子树最大前缀和, 左子树和 + 右子树最大前缀和)
//    pre[i] = max(pre[ls[i]], sum[ls[i]] + pre[rs[i]]);
//    // 最大后缀和 = max(右子树最大后缀和, 右子树和 + 左子树最大后缀和)
//    suf[i] = max(suf[rs[i]], suf[ls[i]] + sum[rs[i]]);
//    // 区间和 = 左子树和 + 右子树和
//    sum[i] = sum[ls[i]] + sum[rs[i]];
//}
//
///** 
// * 更新线段树节点
// * @param jobi 要更新的位置
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param i 前一个版本的节点编号
// * @return 新版本的根节点编号
// */
//int update(int jobi, int l, int r, int i) {
//    int rt = ++cnt;
//    ls[rt] = ls[i];
//    rs[rt] = rs[i];
//    pre[rt] = pre[i];
//    suf[rt] = suf[i];
//    sum[rt] = sum[i];
//    if (l == r) {
//        // 将位置jobi的值从1改为-1
//        pre[rt] = suf[rt] = sum[rt] = -1;
//    } else {
//        int mid = (l + r) / 2;
//        if (jobi <= mid) {
//            ls[rt] = update(jobi, l, mid, ls[rt]);
//        } else {
//            rs[rt] = update(jobi, mid + 1, r, rs[rt]);
//        }
//        up(rt);
//    }
//    return rt;
//}
//
///** 
// * 初始化info数组
// */
//void initInfo() {
//    info[0] = info[1] = -INF;
//    info[2] = 0;
//}
//
///** 
// * 合并右侧区间信息
// * @param r 右侧区间节点编号
// */
//void mergeRight(int r) {
//    // 更新最大前缀和
//    info[0] = max(info[0], info[2] + pre[r]);
//    // 更新最大后缀和
//    info[1] = max(suf[r], info[1] + sum[r]);
//    // 更新区间和
//    info[2] += sum[r];
//}
//
///** 
// * 查询区间[jobl,jobr]的信息
// * @param jobl 查询区间左端点
// * @param jobr 查询区间右端点
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param i 当前节点编号
// */
//void query(int jobl, int jobr, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        mergeRight(i);
//    } else {
//        int mid = (l + r) / 2;
//        if (jobl <= mid) {
//            query(jobl, jobr, l, mid, ls[i]);
//        }
//        if (jobr > mid) {
//            query(jobl, jobr, mid + 1, r, rs[i]);
//        }
//    }
//}
//
///** 
// * 预处理，建立主席树
// */
//void prepare() {
//    // 按照数值从小到大排序
//    sort(arr.begin() + 1, arr.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
//        return a.second < b.second;
//    });
//    cnt = 0;
//    root[1] = build(1, n);
//    for (int i = 2; i <= n; i++) {
//        // 将位置arr[i-1].first的值从1改为-1
//        root[i] = update(arr[i - 1].first, 1, n, root[i - 1]);
//    }
//}
//
///** 
// * 检查是否存在满足条件的区间，其上中位数大于等于v
// * @param a 左端点下界
// * @param b 左端点上界
// * @param c 右端点下界
// * @param d 右端点上界
// * @param v 要检查的上中位数值
// * @return 是否存在满足条件的区间
// */
//bool check(int a, int b, int c, int d, int v) {
//    initInfo();
//    // 查询[a,b]区间的信息
//    query(a, b, 1, n, root[v]);
//    int best = info[1];
//    initInfo();
//    // 查询[c,d]区间的信息
//    query(c, d, 1, n, root[v]);
//    best += info[0];
//    if (b + 1 <= c - 1) {
//        initInfo();
//        // 查询[b+1,c-1]区间的信息
//        query(b + 1, c - 1, 1, n, root[v]);
//        best += info[2];
//    }
//    // 如果best >= 0，说明存在满足条件的区间
//    return best >= 0;
//}
//
///** 
// * 计算查询[a,b,c,d]的最大上中位数
// * @param a 左端点下界
// * @param b 左端点上界
// * @param c 右端点下界
// * @param d 右端点上界
// * @return 最大上中位数
// */
//int compute(int a, int b, int c, int d) {
//    int left = 1, right = n, mid, ans = 0;
//    // 二分答案
//    while (left <= right) {
//        mid = (left + right) / 2;
//        if (check(a, b, c, d, mid)) {
//            // 如果存在满足条件的区间，更新答案并继续向右查找
//            ans = arr[mid].second;
//            left = mid + 1;
//        } else {
//            // 否则向左查找
//            right = mid - 1;
//        }
//    }
//    return ans;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    arr.resize(n + 1);
//    for (int i = 1; i <= n; i++) {
//        arr[i].first = i;
//        cin >> arr[i].second;
//    }
//    prepare();
//    cin >> m;
//    for (int i = 1, lastAns = 0; i <= m; i++) {
//        for (int j = 0; j < 4; j++) {
//            cin >> ques[j];
//            ques[j] = (ques[j] + lastAns) % n + 1;
//        }
//        // 对四个值进行排序
//        sort(ques, ques + 4);
//        // 计算最大上中位数
//        lastAns = compute(ques[0], ques[1], ques[2], ques[3]);
//        cout << lastAns << '\n';
//    }
//    return 0;
//}
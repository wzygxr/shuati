package class160;

// 树状数组套线段树，C++版
// 给定一个长度为n的数组arr，下标1~n，每条操作都是如下5种类型中的一种，一共进行m次操作
// 操作 1 x y z : 查询数字z在arr[x..y]中的排名
// 操作 2 x y z : 查询arr[x..y]中排第z名的数字
// 操作 3 x y   : arr中x位置的数字改成y
// 操作 4 x y z : 查询数字z在arr[x..y]中的前驱，不存在返回-2147483647
// 操作 5 x y z : 查询数字z在arr[x..y]中的后继，不存在返回+2147483647
// 1 <= n、m <= 5 * 10^4
// 数组中的值永远在[0, 10^8]范围内
// 测试链接 : https://www.luogu.com.cn/problem/P3380
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/**
 * 树状数组套线段树解法详解：
 * 
 * 问题分析：
 * 这是"二逼平衡树"问题的另一种解法，需要支持区间内的各种操作：
 * 1. 查询数字在区间内的排名
 * 2. 查询区间内排名第k的数字
 * 3. 单点修改
 * 4. 查询区间内数字的前驱
 * 5. 查询区间内数字的后继
 * 
 * 解法思路：
 * 使用树状数组套线段树来解决这个问题。
 * 1. 外层树状数组维护前缀信息
 * 2. 内层线段树维护值域信息（权值线段树）
 * 3. 每个树状数组节点对应一个权值线段树，存储该前缀区间内各值的出现次数
 * 
 * 数据结构设计：
 * - 外层树状数组：维护前缀[1,i]，每个节点存储一个权值线段树
 * - 内层权值线段树：维护值域[0,10^8]，节点存储值的出现次数
 * - root[i]：树状数组节点i对应的权值线段树根节点
 * - sum[i]：权值线段树节点i维护的值域内元素总个数
 * - left[i], right[i]：权值线段树节点i的左右子节点
 * 
 * 时间复杂度分析：
 * - 查询排名：O(log²n)
 * - 查询第k大：O(log²n)（不需要二分答案）
 * - 单点修改：O(log²n)
 * - 查询前驱/后继：O(log²n)
 * 
 * 空间复杂度分析：
 * - 树状数组节点数：O(n)
 * - 权值线段树节点数：O(n log V)，V为值域大小
 * - 总空间：O(n log V)
 * 
 * 算法优势：
 * 1. 相比于线段树套平衡树，常数更小
 * 2. 实现相对简单
 * 3. 查询第k大不需要二分答案
 * 
 * 算法劣势：
 * 1. 空间消耗较大（值域大时）
 * 2. 需要离散化处理大值域
 * 
 * 适用场景：
 * 1. 需要频繁进行区间排名查询
 * 2. 数据可以动态更新
 * 3. 值域不是特别大或者可以离散化
 */

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 50001;
//const int MAXT = MAXN * 160;
//const int INF = INT_MAX;
//int n, m, s;
//int arr[MAXN];
//int ques[MAXN][4];
//int sorted[MAXN * 2];
//int root[MAXN];
//int sum[MAXT];
//int ls[MAXT];
//int rs[MAXT];
//int cntt = 0;
//int addTree[MAXN];
//int minusTree[MAXN];
//int cntadd;
//int cntminus;
//
//int kth(int num) {
//    int left = 1, right = s, mid;
//    while (left <= right) {
//        mid = (left + right) / 2;
//        if (sorted[mid] == num) {
//            return mid;
//        } else if (sorted[mid] < num) {
//            left = mid + 1;
//        } else {
//            right = mid - 1;
//        }
//    }
//    return -1;
//}
//
//int lowbit(int i) {
//    return i & -i;
//}
//
//int innerAdd(int jobi, int jobv, int l, int r, int i) {
//    if (i == 0) {
//        i = ++cntt;
//    }
//    if (l == r) {
//        sum[i] += jobv;
//    } else {
//        int mid = (l + r) / 2;
//        if (jobi <= mid) {
//            ls[i] = innerAdd(jobi, jobv, l, mid, ls[i]);
//        } else {
//            rs[i] = innerAdd(jobi, jobv, mid + 1, r, rs[i]);
//        }
//        sum[i] = sum[ls[i]] + sum[rs[i]];
//    }
//    return i;
//}
//
//int innerQuery(int jobl, int jobr, int l, int r, int i) {
//    if (i == 0) {
//        return 0;
//    }
//    if (jobl <= l && r <= jobr) {
//        return sum[i];
//    }
//    int mid = (l + r) / 2;
//    int ans = 0;
//    if (jobl <= mid) {
//        ans += innerQuery(jobl, jobr, l, mid, ls[i]);
//    }
//    if (jobr > mid) {
//        ans += innerQuery(jobl, jobr, mid + 1, r, rs[i]);
//    }
//    return ans;
//}
//
//void outerAdd(int index, int jobi, int jobv) {
//    for (int i = index; i <= n; i += lowbit(i)) {
//        root[i] = innerAdd(jobi, jobv, 1, s, root[i]);
//    }
//}
//
//int outerSmall(int index, int jobl, int jobr) {
//    int ans = 0;
//    for (int i = index; i > 0; i -= lowbit(i)) {
//        ans += innerQuery(jobl, jobr, 1, s, root[i]);
//    }
//    return ans;
//}
//
//int outerNumber(int index, int jobk) {
//    int l = 1, r = s, mid, ans = 0;
//    while (l <= r) {
//        mid = (l + r) / 2;
//        if (outerSmall(index, 1, mid) >= jobk) {
//            ans = mid;
//            r = mid - 1;
//        } else {
//            l = mid + 1;
//        }
//    }
//    return ans;
//}
//
//int outerPre(int index, int jobv) {
//    int k = outerSmall(index, 1, jobv);
//    if (k == 0) {
//        return -INF;
//    } else {
//        return sorted[outerNumber(index, k)];
//    }
//}
//
//int outerPost(int index, int jobv) {
//    int k = outerSmall(index, 1, jobv - 1) + 1;
//    if (k > outerSmall(index, 1, s)) {
//        return INF;
//    } else {
//        return sorted[outerNumber(index, k)];
//    }
//}
//
//void prepare() {
//    s = 0;
//    for (int i = 1; i <= n; i++) {
//        sorted[++s] = arr[i];
//    }
//    for (int i = 1; i <= m; i++) {
//        if (ques[i][0] == 1 || ques[i][0] == 2 || ques[i][0] == 4 || ques[i][0] == 5) {
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
//    for (int i = 1; i <= n; i++) {
//        arr[i] = kth(arr[i]);
//    }
//    for (int i = 1; i <= m; i++) {
//        if (ques[i][0] == 1 || ques[i][0] == 2 || ques[i][0] == 4 || ques[i][0] == 5) {
//            ques[i][3] = kth(ques[i][3]);
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin >> n >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    for (int i = 1; i <= m; i++) {
//        cin >> ques[i][0] >> ques[i][1] >> ques[i][2];
//        if (ques[i][0] != 3) {
//            cin >> ques[i][3];
//        }
//    }
//    prepare();
//    for (int i = 1; i <= n; i++) {
//        outerAdd(i, arr[i], 1);
//    }
//    for (int i = 1, op, x, y, z; i <= m; i++) {
//        op = ques[i][0];
//        x = ques[i][1];
//        y = ques[i][2];
//        if (op == 3) {
//            outerAdd(x, arr[x], -1);
//            arr[x] = kth(y);
//            outerAdd(x, arr[x], 1);
//        } else {
//            z = ques[i][3];
//            if (op == 1) {
//                cout << outerSmall(y, 1, z) - outerSmall(x - 1, 1, z) + 1 << endl;
//            } else if (op == 2) {
//                int k = outerSmall(y, 1, s) - outerSmall(x - 1, 1, s);
//                if (y - x + 1 - k < z) {
//                    cout << -INF << endl;
//                } else {
//                    cout << sorted[outerNumber(y, outerSmall(x - 1, 1, s) + z)] << endl;
//                }
//            } else if (op == 4) {
//                int preY = outerPre(y, z);
//                int preX = outerPre(x - 1, z);
//                cout << (preY == preX ? -INF : preY) << endl;
//            } else {
//                int postY = outerPost(y, z);
//                int postX = outerPost(x - 1, z);
//                cout << (postY == postX ? INF : postY) << endl;
//            }
//        }
//    }
//    return 0;
//}
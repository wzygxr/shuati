package class160;

// 线段树套平衡树，C++版
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
 * 线段树套平衡树解法详解：
 * 
 * 问题分析：
 * 这是一个经典的"二逼平衡树"问题，需要支持区间内的各种平衡树操作：
 * 1. 查询数字在区间内的排名
 * 2. 查询区间内排名第k的数字
 * 3. 单点修改
 * 4. 查询区间内数字的前驱
 * 5. 查询区间内数字的后继
 * 
 * 解法思路：
 * 使用线段树套平衡树来解决这个问题。
 * 1. 外层线段树维护区间信息
 * 2. 内层平衡树维护区间内元素的有序信息
 * 3. 每个线段树节点对应一个平衡树，存储该区间内的所有元素
 * 
 * 数据结构设计：
 * - 外层线段树：维护区间[1,n]，每个节点存储一个平衡树
 * - 内层平衡树：使用替罪羊树实现平衡树，维护区间内元素的有序性
 * - root[i]：线段树节点i对应的平衡树根节点
 * - key[i]：平衡树节点i的键值
 * - cnts[i]：平衡树节点i的重复计数
 * - left[i], right[i]：平衡树节点i的左右子节点
 * - size[i]：平衡树节点i的子树大小
 * - diff[i]：平衡树节点i的子树中不同元素的个数
 * 
 * 时间复杂度分析：
 * - 查询排名：O(log²n)
 * - 查询第k大：O(log³n)（需要二分答案）
 * - 单点修改：O(log²n)
 * - 查询前驱/后继：O(log²n)
 * 
 * 空间复杂度分析：
 * - 线段树节点数：O(n)
 * - 平衡树节点数：O(n log n)
 * - 总空间：O(n log n)
 * 
 * 算法优势：
 * 1. 支持在线查询和更新
 * 2. 可以处理任意区间查询
 * 3. 相比于树状数组套平衡树，可以支持更多操作
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数较大
 * 3. 实现复杂度较高
 * 
 * 适用场景：
 * 1. 需要频繁进行区间平衡树操作
 * 2. 数据可以动态更新
 * 3. 需要支持多种平衡树操作
 */

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 50001;
//const int MAXT = MAXN * 40;
//const int INF = INT_MAX;
//const double ALPHA = 0.7;
//int n, m;
//int arr[MAXN];
//int root[MAXN << 2];
//int key[MAXT], cnts[MAXT], left[MAXT], right[MAXT], size[MAXT], diff[MAXT];
//int cnt = 0;
//int collect[MAXT], ci;
//int top, father, side;
//
//int init(int num) {
//    key[++cnt] = num;
//    left[cnt] = right[cnt] = 0;
//    cnts[cnt] = size[cnt] = diff[cnt] = 1;
//    return cnt;
//}
//
//void up(int i) {
//    size[i] = size[left[i]] + size[right[i]] + cnts[i];
//    diff[i] = diff[left[i]] + diff[right[i]] + (cnts[i] > 0 ? 1 : 0);
//}
//
//bool balance(int i) {
//    return i == 0 || ALPHA * diff[i] >= max(diff[left[i]], diff[right[i]]);
//}
//
//void inorder(int i) {
//    if (i != 0) {
//        inorder(left[i]);
//        if (cnts[i] > 0) {
//            collect[++ci] = i;
//        }
//        inorder(right[i]);
//    }
//}
//
//int innerBuild(int l, int r) {
//    if (l > r) {
//        return 0;
//    }
//    int m = (l + r) >> 1;
//    int h = collect[m];
//    left[h] = innerBuild(l, m - 1);
//    right[h] = innerBuild(m + 1, r);
//    up(h);
//    return h;
//}
//
//int innerRebuild(int h) {
//    if (top != 0) {
//        ci = 0;
//        inorder(top);
//        if (ci > 0) {
//            if (father == 0) {
//                h = innerBuild(1, ci);
//            } else if (side == 1) {
//                left[father] = innerBuild(1, ci);
//            } else {
//                right[father] = innerBuild(1, ci);
//            }
//        }
//    }
//    return h;
//}
//
//int innerInsert(int num, int i, int f, int s) {
//    if (i == 0) {
//        i = init(num);
//    } else {
//        if (key[i] == num) {
//            cnts[i]++;
//        } else if (key[i] > num) {
//            left[i] = innerInsert(num, left[i], i, 1);
//        } else {
//            right[i] = innerInsert(num, right[i], i, 2);
//        }
//        up(i);
//        if (!balance(i)) {
//            top = i;
//            father = f;
//            side = s;
//        }
//    }
//    return i;
//}
//
//int innerInsert(int num, int i) {
//    top = father = side = 0;
//    i = innerInsert(num, i, 0, 0);
//    i = innerRebuild(i);
//    return i;
//}
//
//int innerSmall(int num, int i) {
//    if (i == 0) {
//        return 0;
//    }
//    if (key[i] >= num) {
//        return innerSmall(num, left[i]);
//    } else {
//        return size[left[i]] + cnts[i] + innerSmall(num, right[i]);
//    }
//}
//
//int innerIndex(int index, int i) {
//    int leftsize = size[left[i]];
//    if (leftsize >= index) {
//        return innerIndex(index, left[i]);
//    } else if (leftsize + cnts[i] < index) {
//        return innerIndex(index - leftsize - cnts[i], right[i]);
//    } else {
//        return key[i];
//    }
//}
//
//int innerPre(int num, int i) {
//    int kth = innerSmall(num, i) + 1;
//    if (kth == 1) {
//        return -INF;
//    } else {
//        return innerIndex(kth - 1, i);
//    }
//}
//
//int innerPost(int num, int i) {
//    int k = innerSmall(num + 1, i);
//    if (k == size[i]) {
//        return INF;
//    } else {
//        return innerIndex(k + 1, i);
//    }
//}
//
//void innerRemove(int num, int i, int f, int s) {
//    if (key[i] == num) {
//        cnts[i]--;
//    } else if (key[i] > num) {
//        innerRemove(num, left[i], i, 1);
//    } else {
//        innerRemove(num, right[i], i, 2);
//    }
//    up(i);
//    if (!balance(i)) {
//        top = i;
//        father = f;
//        side = s;
//    }
//}
//
//int innerRemove(int num, int i) {
//    if (innerSmall(num, i) != innerSmall(num + 1, i)) {
//        top = father = side = 0;
//        innerRemove(num, i, 0, 0);
//        i = innerRebuild(i);
//    }
//    return i;
//}
//
//void add(int jobi, int jobv, int l, int r, int i) {
//    root[i] = innerInsert(jobv, root[i]);
//    if (l < r) {
//        int mid = (l + r) >> 1;
//        if (jobi <= mid) {
//            add(jobi, jobv, l, mid, i << 1);
//        } else {
//            add(jobi, jobv, mid + 1, r, i << 1 | 1);
//        }
//    }
//}
//
//void update(int jobi, int jobv, int l, int r, int i) {
//    root[i] = innerRemove(arr[jobi], root[i]);
//    root[i] = innerInsert(jobv, root[i]);
//    if (l < r) {
//        int mid = (l + r) >> 1;
//        if (jobi <= mid) {
//            update(jobi, jobv, l, mid, i << 1);
//        } else {
//            update(jobi, jobv, mid + 1, r, i << 1 | 1);
//        }
//    }
//}
//
//int small(int jobl, int jobr, int jobv, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        return innerSmall(jobv, root[i]);
//    }
//    int mid = (l + r) >> 1;
//    int ans = 0;
//    if (jobl <= mid) {
//        ans += small(jobl, jobr, jobv, l, mid, i << 1);
//    }
//    if (jobr > mid) {
//        ans += small(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
//    }
//    return ans;
//}
//
//int number(int jobl, int jobr, int jobk) {
//    int l = 0, r = 100000000, mid, ans = 0;
//    while (l <= r) {
//        mid = (l + r) >> 1;
//        if (small(jobl, jobr, mid + 1, 1, n, 1) + 1 > jobk) {
//            ans = mid;
//            r = mid - 1;
//        } else {
//            l = mid + 1;
//        }
//    }
//    return ans;
//}
//
//int pre(int jobl, int jobr, int jobv, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        return innerPre(jobv, root[i]);
//    }
//    int mid = (l + r) >> 1;
//    int ans = -INF;
//    if (jobl <= mid) {
//        ans = max(ans, pre(jobl, jobr, jobv, l, mid, i << 1));
//    }
//    if (jobr > mid) {
//        ans = max(ans, pre(jobl, jobr, jobv, mid + 1, r, i << 1 | 1));
//    }
//    return ans;
//}
//
//int post(int jobl, int jobr, int jobv, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        return innerPost(jobv, root[i]);
//    }
//    int mid = (l + r) >> 1;
//    int ans = INF;
//    if (jobl <= mid) {
//        ans = min(ans, post(jobl, jobr, jobv, l, mid, i << 1));
//    }
//    if (jobr > mid) {
//        ans = min(ans, post(jobl, jobr, jobv, mid + 1, r, i << 1 | 1));
//    }
//    return ans;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin >> n >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    for (int i = 1; i <= n; i++) {
//        add(i, arr[i], 1, n, 1);
//    }
//    for (int i = 1, op, x, y, z; i <= m; i++) {
//        cin >> op >> x >> y;
//        if (op == 3) {
//            cin >> z;
//            update(x, z, 1, n, 1);
//            arr[x] = z;
//        } else {
//            cin >> z;
//            if (op == 1) {
//                cout << small(x, y, z, 1, n, 1) + 1 << endl;
//            } else if (op == 2) {
//                cout << number(x, y, z) << endl;
//            } else if (op == 4) {
//                cout << pre(x, y, z, 1, n, 1) << endl;
//            } else {
//                cout << post(x, y, z, 1, n, 1) << endl;
//            }
//        }
//    }
//    return 0;
//}
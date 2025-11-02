package class170;

// 三维偏序问题的C++版本实现注释
// 题目来源: 洛谷P3810 【模板】三维偏序（陌上花开）
// 题目链接: https://www.luogu.com.cn/problem/P3810
// 难度等级: 提高+/省选-
// 标签: CDQ分治, 三维偏序

// 题目描述:
// 一共有n个对象，属性值范围[1, k]，每个对象有a属性、b属性、c属性
// f(i)表示，aj <= ai 且 bj <= bi 且 cj <= ci 且 j != i 的j的数量
// ans(d)表示，f(i) == d 的i的数量
// 打印所有的ans[d]，d的范围[0, n)
// 约束条件: 1 <= n <= 10^5, 1 <= k <= 2 * 10^5

// 解题思路:
// 使用CDQ分治解决三维偏序问题。这是CDQ分治的经典应用。
// 
// 1. 第一维：a属性，通过排序处理
// 2. 第二维：b属性，通过CDQ分治处理
// 3. 第三维：c属性，通过树状数组处理
// 
// 具体步骤：
// 1. 按照a属性排序，相同a的按b排序，相同b的按c排序
// 2. CDQ分治处理b属性
// 3. 在分治的合并过程中，使用双指针处理b属性的大小关系，用树状数组维护c属性的前缀和
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

// 以下是C++版本的实现，逻辑与Java版本完全一致
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//struct Node {
//    int i, a, b, c;
//};
//
//bool CmpAbc(Node x, Node y) {
//    if (x.a != y.a) {
//        return x.a < y.a;
//    }
//    if (x.b != y.b) {
//        return x.b < y.b;
//    }
//    return x.c < y.c;
//}
//
//bool CmpB(Node x, Node y) {
//    return x.b < y.b;
//}
//
//const int MAXN = 100001;
//const int MAXK = 200001;
//int n, k;
//
//Node arr[MAXN];
//int tree[MAXK];
//int f[MAXN];
//int ans[MAXN];
//
//int lowbit(int i) {
//    return i & -i;
//}
//
//void add(int i, int v) {
//    while (i <= k) {
//        tree[i] += v;
//        i += lowbit(i);
//    }
//}
//
//int query(int i) {
//    int ret = 0;
//    while (i > 0) {
//        ret += tree[i];
//        i -= lowbit(i);
//    }
//    return ret;
//}
//
//void merge(int l, int m, int r) {
//    // 利用左、右各自b属性有序
//    // 不回退的找，当前右组对象包括了几个左组的对象
//    int p1, p2;
//    for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
//        while (p1 + 1 <= m && arr[p1 + 1].b <= arr[p2].b) {
//            p1++;
//            add(arr[p1].c, 1);
//        }
//        f[arr[p2].i] += query(arr[p2].c);
//    }
//    // 清空树状数组
//    for (int i = l; i <= p1; i++) {
//        add(arr[i].c, -1);
//    }
//    // 直接根据b属性排序，无需写经典的归并过程
//    sort(arr + l, arr + r + 1, CmpB);
//}
//
//void cdq(int l, int r) {
//    if (l == r) {
//        return;
//    }
//    int mid = (l + r) / 2;
//    cdq(l, mid);
//    cdq(mid + 1, r);
//    merge(l, mid, r);
//}
//
//void prepare() {
//    // 根据a排序，a一样根据b排序，b一样根据c排序
//    // 排序后a、b、c一样的同组内，组前的下标得不到同组后面的统计量
//    // 所以把这部分的贡献，提前补偿给组前的下标，然后再跑CDQ分治
//    sort(arr + 1, arr + n + 1, CmpAbc);
//    for (int l = 1, r = 1; l <= n; l = ++r) {
//        while (r + 1 <= n && arr[l].a == arr[r + 1].a && arr[l].b == arr[r + 1].b && arr[l].c == arr[r + 1].c) {
//            r++;
//        }
//        for (int i = l; i <= r; i++) {
//            f[arr[i].i] = r - i;
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> k;
//    for (int i = 1; i <= n; i++) {
//        arr[i].i = i;
//        cin >> arr[i].a >> arr[i].b >> arr[i].c;
//    }
//    prepare();
//    cdq(1, n);
//    for (int i = 1; i <= n; i++) {
//        ans[f[i]]++;
//    }
//    for (int i = 0; i < n; i++) {
//        cout << ans[i] << '\n';
//    }
//    return 0;
//}
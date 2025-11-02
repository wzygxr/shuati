package class170;

// 动态逆序对问题的C++版本实现注释
// 题目来源: 洛谷P3157 [CQOI2011]动态逆序对
// 题目链接: https://www.luogu.com.cn/problem/P3157
// 难度等级: 省选/NOI-
// 标签: CDQ分治, 动态逆序对

// 题目描述:
// 给定一个长度为n的排列，1~n所有数字都出现一次
// 如果，前面的数 > 后面的数，那么这两个数就组成一个逆序对
// 给定一个长度为m的数组，表示依次删除的数字
// 打印每次删除数字前，排列中一共有多少逆序对，一共m条打印
// 约束条件: 1 <= n <= 10^5, 1 <= m <= 5 * 10^4

// 解题思路:
// 使用CDQ分治解决动态逆序对问题。
// 
// 1. 将删除操作转化为时间维度
// 2. 使用CDQ分治处理时间、位置和数值三个维度
// 3. 分别计算每个删除操作对逆序对数量的影响
// 
// 具体步骤：
// 1. 将初始序列和删除操作都看作事件，分别标记为+1和-1
// 2. 使用CDQ分治处理时间维度
// 3. 在合并过程中，分别计算左侧值大的数量和右侧值小的数量
// 4. 通过树状数组维护数值维度的前缀和
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
//    int v, i, d, q;
//};
//
//bool NodeCmp(Node x, Node y) {
//    return x.i < y.i;
//}
//
//const int MAXN = 100001;
//const int MAXM = 50001;
//int n, m;
//
//int num[MAXN];
//int pos[MAXN];
//int del[MAXM];
//
//Node arr[MAXN + MAXM];
//int cnt = 0;
//
//int tree[MAXN];
//
//long long ans[MAXM];
//
//int lowbit(int i) {
//    return i & -i;
//}
//
//void add(int i, int v) {
//    while (i <= n) {
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
//    // 利用左侧和右侧各自在位置维度上的有序性
//    int p1, p2;
//    // 从左到右统计左侧值大的数量
//    for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
//        // 双指针移动，找到所有位置小于当前右侧元素位置的左侧元素
//        while (p1 + 1 <= m && arr[p1 + 1].i < arr[p2].i) {
//            p1++;
//            // 将左侧元素的数值加入树状数组，权重为其效果值
//            add(arr[p1].v, arr[p1].d);
//        }
//        // 计算当前右侧元素对答案的贡献
//        // arr[p2].d是当前元素的效果值，(query(n) - query(arr[p2].v))是数值大于当前元素的左侧元素数量
//        ans[arr[p2].q] += arr[p2].d * (query(n) - query(arr[p2].v));
//    }
//    // 清除树状数组，为下一次统计做准备
//    for (int i = l; i <= p1; i++) {
//        add(arr[i].v, -arr[i].d);
//    }
//    // 从右到左统计右侧值小的数量
//    for (p1 = m + 1, p2 = r; p2 > m; p2--) {
//        // 双指针移动，找到所有位置大于当前左侧元素位置的右侧元素
//        while (p1 - 1 >= l && arr[p1 - 1].i > arr[p2].i) {
//            p1--;
//            // 将右侧元素的数值加入树状数组，权重为其效果值
//            add(arr[p1].v, arr[p1].d);
//        }
//        // 计算当前左侧元素对答案的贡献
//        // arr[p2].d是当前元素的效果值，query(arr[p2].v - 1)是数值小于当前元素的右侧元素数量
//        ans[arr[p2].q] += arr[p2].d * query(arr[p2].v - 1);
//    }
//    // 清除树状数组
//    for (int i = m; i >= p1; i--) {
//        add(arr[i].v, -arr[i].d);
//    }
//    // 直接排序，按位置维度排序
//    sort(arr + l, arr + r + 1, NodeCmp);
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
//    // 将初始序列元素转化为事件，效果值为+1，问题编号为0
//    for (int i = 1; i <= n; i++) {
//        arr[++cnt].v = num[i];
//        arr[cnt].i = i;
//        arr[cnt].d = 1;
//        arr[cnt].q = 0;
//    }
//    // 将删除操作转化为事件，效果值为-1，问题编号为操作序号
//    for (int i = 1; i <= m; i++) {
//        arr[++cnt].v = del[i];
//        arr[cnt].i = pos[del[i]];
//        arr[cnt].d = -1;
//        arr[cnt].q = i;
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> num[i];
//        pos[num[i]] = i;
//    }
//    for (int i = 1; i <= m; i++) {
//        cin >> del[i];
//    }
//    prepare();
//    cdq(1, cnt);
//    // 计算前缀和，得到每次删除前的逆序对数量
//    for (int i = 1; i < m; i++) {
//        ans[i] += ans[i - 1];
//    }
//    for (int i = 0; i < m; i++) {
//        cout << ans[i] << '\n';
//    }
//    return 0;
//}
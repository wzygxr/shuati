package class170;

// 园丁的烦恼问题的C++版本实现注释
// 题目来源: 洛谷P2163 [SHOI2007]园丁的烦恼
// 题目链接: https://www.luogu.com.cn/problem/P2163
// 难度等级: 省选/NOI-
// 标签: CDQ分治, 二维数点

// 题目描述:
// 有n棵树，每棵树给定位置坐标(x, y)，接下来有m条查询，格式如下
// 查询 a b c d : 打印左上角(a, b)、右下角(c, d)的区域里有几棵树
// 约束条件: 
// 0 <= n <= 5 * 10^5
// 1 <= m <= 5 * 10^5
// 0 <= 坐标值 <= 10^7

// 解题思路:
// 使用CDQ分治解决二维数点问题。
// 
// 1. 将查询操作拆分为前缀和的形式
// 2. 使用CDQ分治处理时间维度
// 3. 在合并过程中使用双指针处理y坐标维度
// 
// 具体步骤：
// 1. 将每棵树的插入操作和查询操作都看作事件
// 2. 将二维区域查询转化为四个前缀和查询的组合
// 3. 按照x坐标排序
// 4. 使用CDQ分治处理时间维度，在合并过程中统计y坐标维度上的数量
// 
// 时间复杂度：O((n+m) log^2 (n+m))
// 空间复杂度：O(n+m)

// 以下是C++版本的实现，逻辑与Java版本完全一致
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//struct Node {
//    int op, x, y, v, q;
//};
//
//bool NodeCmp(Node a, Node b) {
//    if (a.x != b.x) {
//        return a.x < b.x;
//    }
//    return a.op < b.op;
//}
//
//const int MAXN = 500001 * 5;
//int n, m;
//Node arr[MAXN];
//int cnt = 0;
//Node tmp[MAXN];
//int ans[MAXN];
//
//void addTree(int x, int y) {
//    arr[++cnt].op = 1;  // 操作类型：1表示树木
//    arr[cnt].x = x;     // x坐标
//    arr[cnt].y = y;     // y坐标
//}
//
//void addQuery(int x, int y, int v, int q) {
//    arr[++cnt].op = 2;  // 操作类型：2表示查询
//    arr[cnt].x = x;     // x坐标
//    arr[cnt].y = y;     // y坐标
//    arr[cnt].v = v;     // 效果值
//    arr[cnt].q = q;     // 查询编号
//}
//
//void merge(int l, int m, int r) {
//    // 利用左侧和右侧各自在y坐标上的有序性
//    int p1, p2, tree = 0;
//    for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
//        // 双指针移动，找到所有y坐标小于等于当前右侧元素y坐标的左侧元素
//        while (p1 + 1 <= m && arr[p1 + 1].y <= arr[p2].y) {
//            p1++;
//            // 如果是树木插入事件，增加计数
//            if (arr[p1].op == 1) {
//                tree++;
//            }
//        }
//        // 如果是查询事件，累加答案
//        if (arr[p2].op == 2) {
//            // tree表示y坐标小于等于当前查询点y坐标的树木数量
//            // arr[p2].v是效果值，用于处理前缀和
//            ans[arr[p2].q] += tree * arr[p2].v;
//        }
//    }
//    // 经典归并过程
//    p1 = l;
//    p2 = m + 1;
//    int i = l;
//    while (p1 <= m && p2 <= r) {
//        tmp[i++] = arr[p1].y <= arr[p2].y ? arr[p1++] : arr[p2++];
//    }
//    while (p1 <= m) {
//        tmp[i++] = arr[p1++];
//    }
//    while (p2 <= r) {
//        tmp[i++] = arr[p2++];
//    }
//    for (i = l; i <= r; i++) {
//        arr[i] = tmp[i];
//    }
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
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    // 读取所有树木的坐标并添加插入事件
//    for (int i = 1, x, y; i <= n; i++) {
//        cin >> x >> y;
//        addTree(x, y);
//    }
//    // 读取所有查询，将二维区域查询转化为四个前缀和查询的组合
//    for (int i = 1, a, b, c, d; i <= m; i++) {
//        cin >> a >> b >> c >> d;
//        // 使用容斥原理将矩形区域查询转换为四个前缀和查询
//        // 右上角区域加1
//        addQuery(c, d, 1, i);
//        // 左下角区域加1
//        addQuery(a - 1, b - 1, 1, i);
//        // 左上角区域减1
//        addQuery(a - 1, d, -1, i);
//        // 右下角区域减1
//        addQuery(c, b - 1, -1, i);
//    }
//    // 按照x坐标排序，x坐标相同的按照操作类型排序(树木在前)
//    sort(arr + 1, arr + cnt + 1, NodeCmp);
//    // 执行CDQ分治
//    cdq(1, cnt);
//    // 输出所有查询的答案
//    for (int i = 1; i <= m; i++) {
//        cout << ans[i] << '\n';
//    }
//    return 0;
//}
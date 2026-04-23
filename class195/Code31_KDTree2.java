package class195;

// KD 树优化建图基础模板，C++ 版
// 本代码展示 KD 树优化建图的核心模板，用于解决多维空间查询问题
// 测试链接 : https://www.luogu.com.cn/problem/P1429（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== KD 树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// KD 树用于解决多维空间中的最近邻搜索、范围查询等问题
// 通过交替按维度分割空间，构建二叉搜索树
//
// 【核心原理】
// 维度交替：每一层按不同维度分割
// 中位数选择：选择中位数作为分割点
// 空间划分：左子树包含小于分割点的点，右子树包含大于分割点的点
// 剪枝优化：查询时利用距离下界剪枝
//
// 【ML/DL 关联价值】
// 1. KNN 算法中的最近邻搜索
// 2. 聚类分析中的空间划分
// 3. 计算机视觉中的特征匹配

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const double INF = 1e18;

// ===================== 点结构体 =====================
struct Point {
    double x, y;
    int id;
};

// ===================== KD 树节点 =====================
struct Node {
    Point point;
    int left, right;
    double minX, maxX, minY, maxY;
};

// ===================== 全局变量 =====================
Node tree[MAXN];
int nodeCnt;
int root;
int n;
Point points[MAXN];
double queryX, queryY;
double minDist;
int nearestId;

// ===================== 核心函数：计算两点距离平方 =====================
double distSq(const Point& p1, const Point& p2) {
    double dx = p1.x - p2.x;
    double dy = p1.y - p2.y;
    return dx * dx + dy * dy;
}

// ===================== 核心函数：点到矩形盒的最小距离 =====================
double distToBox(int node, double x, double y) {
    Node& nd = tree[node];
    double dx = 0, dy = 0;

    if (x < nd.minX) dx = nd.minX - x;
    else if (x > nd.maxX) dx = x - nd.maxX;

    if (y < nd.minY) dy = nd.minY - y;
    else if (y > nd.maxY) dy = y - nd.maxY;

    return dx * dx + dy * dy;
}

// ===================== 核心函数：更新包围盒 =====================
void updateBox(int node) {
    Node& nd = tree[node];
    if (nd.left) {
        Node& left = tree[nd.left];
        nd.minX = min(nd.minX, left.minX);
        nd.maxX = max(nd.maxX, left.maxX);
        nd.minY = min(nd.minY, left.minY);
        nd.maxY = max(nd.maxY, left.maxY);
    }
    if (nd.right) {
        Node& right = tree[nd.right];
        nd.minX = min(nd.minX, right.minX);
        nd.maxX = max(nd.maxX, right.maxX);
        nd.minY = min(nd.minY, right.minY);
        nd.maxY = max(nd.maxY, right.maxY);
    }
}

// ===================== 核心函数：构建 KD 树 =====================
int build(int l, int r, int depth) {
    if (l > r) return 0;

    int mid = (l + r) >> 1;

    // 按当前维度排序
    if (depth % 2 == 0) {
        nth_element(points + l, points + mid, points + r + 1, 
            [](const Point& a, const Point& b) { return a.x < b.x; });
    } else {
        nth_element(points + l, points + mid, points + r + 1, 
            [](const Point& a, const Point& b) { return a.y < b.y; });
    }

    int node = ++nodeCnt;
    tree[node].point = points[mid];
    tree[node].left = build(l, mid - 1, depth + 1);
    tree[node].right = build(mid + 1, r, depth + 1);

    tree[node].minX = tree[node].maxX = points[mid].x;
    tree[node].minY = tree[node].maxY = points[mid].y;
    updateBox(node);

    return node;
}

// ===================== 核心函数：查询最近邻 =====================
void queryNearest(int node, int depth, double x, double y) {
    if (!node) return;

    Node& nd = tree[node];
    Point p = nd.point;

    double d = distSq(p, {x, y, 0});
    if (d < minDist) {
        minDist = d;
        nearestId = p.id;
    }

    double distLeft = nd.left ? distToBox(nd.left, x, y) : INF;
    double distRight = nd.right ? distToBox(nd.right, x, y) : INF;

    if (distLeft < distRight) {
        if (distLeft < minDist) queryNearest(nd.left, depth + 1, x, y);
        if (distRight < minDist) queryNearest(nd.right, depth + 1, x, y);
    } else {
        if (distRight < minDist) queryNearest(nd.right, depth + 1, x, y);
        if (distLeft < minDist) queryNearest(nd.left, depth + 1, x, y);
    }
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入点数
    cin >> n;

    // 读入所有点
    for (int i = 1; i <= n; i++) {
        cin >> points[i].x >> points[i].y;
        points[i].id = i;
    }

    // 构建 KD 树
    root = build(1, n, 0);

    // 查询示例
    int queryCount;
    cin >> queryCount;
    for (int i = 0; i < queryCount; i++) {
        cin >> queryX >> queryY;

        minDist = INF;
        nearestId = -1;

        queryNearest(root, 0, queryX, queryY);

        cout << "查询点 (" << queryX << ", " << queryY << ") 的最近邻：" << endl;
        cout << "  最近点编号：" << nearestId << endl;
        cout << "  最小距离：" << sqrt(minDist) << endl;
    }

    return 0;
}

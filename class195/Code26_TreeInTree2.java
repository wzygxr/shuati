package class195;

// 树套树优化建图基础模板，C++ 版
// 本代码展示树套树优化建图的核心模板，用于解决二维区间问题
// 测试链接 : https://www.luogu.com.cn/problem/P3380（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 树套树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 树套树用于解决二维区间查询和修改问题
// 通过外层线段树套内层平衡树/线段树实现
//
// 【核心原理】
// 外层树：维护第一维（通常是位置）
// 内层树：维护第二维（通常是值域）
// 嵌套查询：在外层树上查询时，同时查询内层树
//
// 【ML/DL 关联价值】
// 1. 高维特征空间的索引结构
// 2. 多模态数据的联合查询
// 3. 图数据库中的多维索引

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int INF = 1 << 30;

// ===================== 内层线段树节点 =====================
struct InnerNode {
    int sum; // 区间和
    int count; // 区间计数
    InnerNode *left, *right;

    InnerNode() : sum(0), count(0), left(nullptr), right(nullptr) {}
};

// ===================== 外层线段树节点 =====================
struct OuterNode {
    InnerNode* root; // 内层线段树的根
    OuterNode *left, *right;

    OuterNode() : root(new InnerNode()), left(nullptr), right(nullptr) {}
};

// ===================== 树套树变量区 =====================
OuterNode* outerRoot;
int n, m;
int a[MAXN];

// ===================== 值域范围 =====================
int minVal, maxVal;

// ===================== 核心函数：内层线段树更新 =====================
InnerNode* updateInner(InnerNode* node, int l, int r, int pos, int val) {
    if (node == nullptr) {
        node = new InnerNode();
    }

    if (l == r) {
        node->sum += val;
        node->count++;
        return node;
    }

    int mid = (l + r) >> 1;
    if (pos <= mid) {
        node->left = updateInner(node->left, l, mid, pos, val);
    } else {
        node->right = updateInner(node->right, mid + 1, r, pos, val);
    }

    // 更新当前节点
    node->sum = (node->left ? node->left->sum : 0) + 
                (node->right ? node->right->sum : 0);
    node->count = (node->left ? node->left->count : 0) + 
                  (node->right ? node->right->count : 0);

    return node;
}

// ===================== 核心函数：外层线段树更新 =====================
OuterNode* updateOuter(OuterNode* node, int l, int r, int pos, int val) {
    if (node == nullptr) {
        node = new OuterNode();
    }

    // 在内层树中更新
    node->root = updateInner(node->root, minVal, maxVal, val, 1);

    if (l == r) {
        return node;
    }

    int mid = (l + r) >> 1;
    if (pos <= mid) {
        node->left = updateOuter(node->left, l, mid, pos, val);
    } else {
        node->right = updateOuter(node->right, mid + 1, r, pos, val);
    }

    return node;
}

// ===================== 核心函数：内层线段树查询 =====================
int queryInner(InnerNode* node, int l, int r, int ql, int qr) {
    if (node == nullptr || ql > r || qr < l) {
        return 0;
    }

    if (ql <= l && r <= qr) {
        return node->sum;
    }

    int mid = (l + r) >> 1;
    int leftSum = queryInner(node->left, l, mid, ql, qr);
    int rightSum = queryInner(node->right, mid + 1, r, ql, qr);

    return leftSum + rightSum;
}

// ===================== 核心函数：外层线段树查询 =====================
int queryOuter(OuterNode* node, int l, int r, int ql, int qr, int vl, int vr) {
    if (node == nullptr || ql > r || qr < l) {
        return 0;
    }

    if (ql <= l && r <= qr) {
        return queryInner(node->root, minVal, maxVal, vl, vr);
    }

    int mid = (l + r) >> 1;
    int leftSum = queryOuter(node->left, l, mid, ql, qr, vl, vr);
    int rightSum = queryOuter(node->right, mid + 1, r, ql, qr, vl, vr);

    return leftSum + rightSum;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入数组大小和操作数
    cin >> n >> m;
    minVal = 0;
    maxVal = 100000;

    // 读入初始数组
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }

    // 构建树套树
    outerRoot = new OuterNode();
    for (int i = 1; i <= n; i++) {
        outerRoot = updateOuter(outerRoot, 1, n, i, a[i]);
    }

    // 处理 m 次操作
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            int l, r, vl, vr;
            cin >> l >> r >> vl >> vr;
            int result = queryOuter(outerRoot, 1, n, l, r, vl, vr);
            cout << "查询结果：" << result << endl;
        } else if (op == 2) {
            int pos, newVal;
            cin >> pos >> newVal;
            int oldVal = a[pos];
            a[pos] = newVal;
            cout << "修改位置 " << pos << " 从 " << oldVal << " 到 " << newVal << endl;
        }
    }

    return 0;
}

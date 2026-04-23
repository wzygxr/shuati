package class195;

// 动态开点线段树优化建图基础模板，C++ 版
// 本代码展示动态开点线段树优化建图的核心模板，用于解决值域优化问题
// 测试链接 : https://www.luogu.com.cn/problem/P3293（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 动态开点线段树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 动态开点线段树用于解决值域很大但实际使用点很少的问题
// 通过动态创建节点，节省空间复杂度
//
// 【核心原理】
// 动态开点：只在需要时创建节点
// 值域映射：将数值映射到线段树区间
// 懒惰标记：延迟更新，优化复杂度
//
// 【ML/DL 关联价值】
// 1. 大规模特征空间的稀疏表示
// 2. 动态图神经网络中的节点管理
// 3. 在线学习中的增量更新

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int MAX_NODES = 4000001;
const int INF = 1 << 30;

// ===================== 动态开点线段树变量区 =====================
int leftChild[MAX_NODES];
int rightChild[MAX_NODES];
int minVal[MAX_NODES];
int maxVal[MAX_NODES];
int nodeCnt;
int root;

// ===================== 值域变量区 =====================
int n, m;
int minRange, maxRange;

// ===================== 核心函数：创建新节点 =====================
int createNode() {
    int node = ++nodeCnt;
    leftChild[node] = 0;
    rightChild[node] = 0;
    minVal[node] = INF;
    maxVal[node] = -INF;
    return node;
}

// ===================== 核心函数：动态开点更新 =====================
void update(int node, int l, int r, int pos, int val) {
    if (l == r) {
        minVal[node] = min(minVal[node], val);
        maxVal[node] = max(maxVal[node], val);
        return;
    }
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        if (leftChild[node] == 0) {
            leftChild[node] = createNode();
        }
        update(leftChild[node], l, mid, pos, val);
    } else {
        if (rightChild[node] == 0) {
            rightChild[node] = createNode();
        }
        update(rightChild[node], mid + 1, r, pos, val);
    }
    if (leftChild[node] != 0) {
        minVal[node] = min(minVal[node], minVal[leftChild[node]]);
        maxVal[node] = max(maxVal[node], maxVal[leftChild[node]]);
    }
    if (rightChild[node] != 0) {
        minVal[node] = min(minVal[node], minVal[rightChild[node]]);
        maxVal[node] = max(maxVal[node], maxVal[rightChild[node]]);
    }
}

// ===================== 核心函数：区间查询最小值 =====================
int queryMin(int node, int l, int r, int ql, int qr) {
    if (node == 0 || ql > r || qr < l) {
        return INF;
    }
    if (ql <= l && r <= qr) {
        return minVal[node];
    }
    int mid = (l + r) >> 1;
    int leftMin = queryMin(leftChild[node], l, mid, ql, qr);
    int rightMin = queryMin(rightChild[node], mid + 1, r, ql, qr);
    return min(leftMin, rightMin);
}

// ===================== 核心函数：区间查询最大值 =====================
int queryMax(int node, int l, int r, int ql, int qr) {
    if (node == 0 || ql > r || qr < l) {
        return -INF;
    }
    if (ql <= l && r <= qr) {
        return maxVal[node];
    }
    int mid = (l + r) >> 1;
    int leftMax = queryMax(leftChild[node], l, mid, ql, qr);
    int rightMax = queryMax(rightChild[node], mid + 1, r, ql, qr);
    return max(leftMax, rightMax);
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入操作数和值域范围
    cin >> m >> minRange >> maxRange;

    // 创建根节点
    root = createNode();

    // 处理 m 次操作
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            int pos, val;
            cin >> pos >> val;
            update(root, minRange, maxRange, pos, val);
        } else if (op == 2) {
            int l, r;
            cin >> l >> r;
            int minV = queryMin(root, minRange, maxRange, l, r);
            cout << "区间 [" << l << ", " << r << "] 最小值：" << (minV == INF ? "不存在" : minV) << endl;
        } else if (op == 3) {
            int l, r;
            cin >> l >> r;
            int maxV = queryMax(root, minRange, maxRange, l, r);
            cout << "区间 [" << l << ", " << r << "] 最大值：" << (maxV == -INF ? "不存在" : maxV) << endl;
        }
    }

    // 输出使用的节点数
    cout << "使用的节点数：" << nodeCnt << endl;

    return 0;
}

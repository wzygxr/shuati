package class195;

// 笛卡尔树优化建图基础模板，C++ 版
// 本代码展示笛卡尔树优化建图的核心模板，用于解决区间最值问题
// 测试链接 : https://www.luogu.com.cn/problem/P5854（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 笛卡尔树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 笛卡尔树用于解决区间最值、笛卡尔树构建等问题
// 通过维护堆性质和 BST 性质，实现高效的区间最值查询
//
// 【核心原理】
// 堆性质：父节点的值小于等于子节点（小根堆）
// BST 性质：中序遍历为原序列
// 构建方法：单调栈 O(n) 构建
//
// 【ML/DL 关联价值】
// 1. 树结构中的层次化最值索引
// 2. 注意力机制中的稀疏化结构
// 3. 图神经网络中的树形聚合

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 5000001;
const int INF = 1 << 30;

// ===================== 笛卡尔树节点 =====================
struct Node {
    int val; // 节点值
    int left; // 左儿子编号
    int right; // 右儿子编号
    int parent; // 父节点编号

    Node(int v) : val(v), left(0), right(0), parent(0) {}
    Node() : val(0), left(0), right(0), parent(0) {}
};

// ===================== 笛卡尔树变量区 =====================
Node tree[MAXN];
int n;
int root;

// ===================== 数组变量区 =====================
int a[MAXN];
int stk[MAXN];
int top;

// ===================== 核心函数：单调栈构建笛卡尔树 =====================
void buildCartesianTree() {
    top = 0;
    root = 1;

    for (int i = 1; i <= n; i++) {
        tree[i] = Node(i);
        int last = 0;

        // 维护单调递增栈
        while (top > 0 && a[stk[top]] > a[i]) {
            last = stk[top];
            top--;
        }

        if (top > 0) {
            tree[i].parent = stk[top];
            tree[stk[top]].right = i;
        }

        if (last != 0) {
            tree[i].left = last;
            tree[last].parent = i;
        }

        stk[++top] = i;

        if (tree[i].parent == 0) {
            root = i;
        }
    }
}

// ===================== 核心函数：DFS 遍历笛卡尔树 =====================
void dfs(int u, int depth) {
    if (u == 0) {
        return;
    }

    for (int i = 0; i < depth; i++) {
        cout << "  ";
    }
    cout << "节点 " << u << ": 值=" << tree[u].val << endl;

    dfs(tree[u].left, depth + 1);
    dfs(tree[u].right, depth + 1);
}

// ===================== 核心函数：查询区间最值 =====================
int queryRMQ(int l, int r) {
    int minPos = l;
    for (int i = l + 1; i <= r; i++) {
        if (a[i] < a[minPos]) {
            minPos = i;
        }
    }
    return minPos;
}

// ===================== 核心函数：验证笛卡尔树性质 =====================
bool verifyCartesianTree(int u, int minVal, int maxVal) {
    if (u == 0) {
        return true;
    }

    if (tree[u].val < minVal || tree[u].val > maxVal) {
        return false;
    }

    if (tree[u].left != 0 && tree[tree[u].left].val < tree[u].val) {
        return false;
    }
    if (tree[u].right != 0 && tree[tree[u].right].val < tree[u].val) {
        return false;
    }

    return verifyCartesianTree(tree[u].left, minVal, tree[u].val - 1) &&
           verifyCartesianTree(tree[u].right, tree[u].val + 1, maxVal);
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入数组大小
    cin >> n;

    // 读入数组元素
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }

    // 构建笛卡尔树
    buildCartesianTree();

    // 输出笛卡尔树信息
    cout << "笛卡尔树根节点：" << root << endl;
    cout << "笛卡尔树结构：" << endl;
    dfs(root, 0);

    // 验证笛卡尔树性质
    bool valid = verifyCartesianTree(root, -INF, INF);
    cout << "笛卡尔树性质验证：" << (valid ? "通过" : "失败") << endl;

    // RMQ 查询示例
    int queryCount;
    cin >> queryCount;
    for (int i = 0; i < queryCount; i++) {
        int l, r;
        cin >> l >> r;
        int minPos = queryRMQ(l, r);
        cout << "区间 [" << l << ", " << r << "] 最小值位置：" << minPos 
             << ", 最小值：" << a[minPos] << endl;
    }

    return 0;
}

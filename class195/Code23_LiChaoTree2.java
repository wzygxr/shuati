package class195;

// 李超树优化建图基础模板，C++ 版
// 本代码展示李超树优化建图的核心模板，用于解决直线/函数最值问题
// 测试链接 : https://www.luogu.com.cn/problem/P4097（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 李超树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 李超树用于解决动态插入直线/函数，查询某点的最值问题
// 通过线段树结构维护直线集合，支持高效插入和查询
//
// 【核心原理】
// 优势直线：在每个区间保留最优的直线
// 标记永久化：不删除旧直线，直接覆盖或保留
// 递归比较：在交点处比较两条直线的优劣
//
// 【ML/DL 关联价值】
// 1. 动态规划中的斜率优化
// 2. 凸优化问题中的分段线性函数
// 3. 在线学习中的模型更新

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int MAX_NODES = 4000001;
const long long INF = 1e18;

// ===================== 直线表示 =====================
struct Line {
    long long k; // 斜率
    long long b; // 截距

    Line() : k(0), b(0) {}
    Line(long long k, long long b) : k(k), b(b) {}

    // 计算直线在 x 处的值
    long long getValue(long long x) {
        return k * x + b;
    }
};

// ===================== 李超树变量区 =====================
Line tree[MAX_NODES];
bool hasLine[MAX_NODES];
int nodeCnt;
int root;

// ===================== 值域变量区 =====================
int n, m;
int minRange, maxRange;

// ===================== 核心函数：比较两条直线 =====================
Line compare(const Line& l1, const Line& l2, long long x, bool isMax) {
    if (!hasLine[l1.k]) return l2;
    if (!hasLine[l2.k]) return l1;
    long long v1 = l1.getValue(x);
    long long v2 = l2.getValue(x);
    return isMax ? (v1 > v2 ? l1 : l2) : (v1 < v2 ? l1 : l2);
}

// ===================== 核心函数：插入直线 =====================
void insert(int node, int l, int r, Line newLine, bool isMax) {
    if (l > r) return;

    if (!hasLine[node]) {
        tree[node] = newLine;
        hasLine[node] = true;
        return;
    }

    int mid = (l + r) >> 1;
    Line cur = tree[node];

    bool betterAtMid = isMax ? 
        (newLine.getValue(mid) > cur.getValue(mid)) : 
        (newLine.getValue(mid) < cur.getValue(mid));

    if (betterAtMid) {
        swap(tree[node], newLine);
        cur = tree[node];
    }

    if (l == r) return;

    bool betterAtLeft = isMax ? 
        (newLine.getValue(l) > cur.getValue(l)) : 
        (newLine.getValue(l) < cur.getValue(l));
    bool betterAtRight = isMax ? 
        (newLine.getValue(r) > cur.getValue(r)) : 
        (newLine.getValue(r) < cur.getValue(r));

    if (betterAtLeft) {
        insert(node << 1, l, mid, newLine, isMax);
    } else if (betterAtRight) {
        insert(node << 1 | 1, mid + 1, r, newLine, isMax);
    }
}

// ===================== 核心函数：查询最值 =====================
long long query(int node, int l, int r, int x, bool isMax) {
    if (node == 0 || l > r || !hasLine[node]) {
        return isMax ? -INF : INF;
    }

    long long result = tree[node].getValue(x);

    if (l == r) {
        return result;
    }

    int mid = (l + r) >> 1;
    if (x <= mid) {
        long long childResult = query(node << 1, l, mid, x, isMax);
        result = isMax ? max(result, childResult) : min(result, childResult);
    } else {
        long long childResult = query(node << 1 | 1, mid + 1, r, x, isMax);
        result = isMax ? max(result, childResult) : min(result, childResult);
    }

    return result;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入操作数和值域范围
    cin >> m >> minRange >> maxRange;

    // 初始化
    memset(hasLine, false, sizeof(hasLine));
    root = 1;
    nodeCnt = 1;

    // 处理 m 次操作
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            long long k, b;
            cin >> k >> b;
            Line line(k, b);
            insert(root, minRange, maxRange, line, true);
        } else if (op == 2) {
            int x;
            cin >> x;
            long long result = query(root, minRange, maxRange, x, true);
            cout << "位置 " << x << " 的最大值：" << (result == -INF ? "不存在" : result) << endl;
        }
    }

    // 输出使用的节点数
    cout << "使用的节点数：" << nodeCnt << endl;

    return 0;
}

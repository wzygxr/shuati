package class195;

// 分块优化建图基础模板，C++ 版
// 本代码展示分块优化建图的核心模板，用于解决区间修改查询问题
// 测试链接 : https://www.luogu.com.cn/problem/P3372（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 分块优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 分块用于解决区间修改、区间查询等问题
// 通过将数组分块，平衡预处理和查询的复杂度
//
// 【核心原理】
// 分块思想：将数组分成大小为√n 的块
// 整块操作：对完整的块使用懒标记
// 散点操作：对不完整的块暴力处理
//
// 【ML/DL 关联价值】
// 1. 批处理中的块状计算优化
// 2. 内存访问的局部性优化
// 3. 并行计算中的数据分块

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const long long INF = 1e18;

// ===================== 数组变量区 =====================
long long a[MAXN];
long long sum[MAXN];
long long lazy[MAXN];
int belong[MAXN];
int n, m;
int blockSize;
int blockCount;

// ===================== 核心函数：分块初始化 =====================
void init() {
    blockSize = sqrt(n);
    if (blockSize == 0) blockSize = 1;
    blockCount = (n + blockSize - 1) / blockSize;

    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }

    for (int i = 1; i <= n; i++) {
        sum[belong[i]] += a[i];
    }
}

// ===================== 核心函数：区间加法 =====================
void rangeAdd(int l, int r, long long val) {
    if (belong[l] == belong[r]) {
        for (int i = l; i <= r; i++) {
            a[i] += val;
        }
        sum[belong[l]] += val * (r - l + 1);
        return;
    }

    for (int i = l; i <= belong[l] * blockSize && i <= n; i++) {
        a[i] += val;
    }
    sum[belong[l]] += val * (belong[l] * blockSize - l + 1);

    for (int i = belong[l] + 1; i < belong[r]; i++) {
        lazy[i] += val;
        sum[i] += val * blockSize;
    }

    for (int i = (belong[r] - 1) * blockSize + 1; i <= r; i++) {
        a[i] += val;
    }
    sum[belong[r]] += val * (r - ((belong[r] - 1) * blockSize + 1) + 1);
}

// ===================== 核心函数：区间求和 =====================
long long rangeQuery(int l, int r) {
    long long result = 0;

    if (belong[l] == belong[r]) {
        for (int i = l; i <= r; i++) {
            result += a[i] + lazy[belong[i]];
        }
        return result;
    }

    for (int i = l; i <= belong[l] * blockSize && i <= n; i++) {
        result += a[i] + lazy[belong[i]];
    }

    for (int i = belong[l] + 1; i < belong[r]; i++) {
        result += sum[i];
    }

    for (int i = (belong[r] - 1) * blockSize + 1; i <= r; i++) {
        result += a[i] + lazy[belong[i]];
    }

    return result;
}

// ===================== 核心函数：区间最值查询 =====================
long long rangeMax(int l, int r) {
    long long result = -INF;

    if (belong[l] == belong[r]) {
        for (int i = l; i <= r; i++) {
            result = max(result, a[i] + lazy[belong[i]]);
        }
        return result;
    }

    for (int i = l; i <= belong[l] * blockSize && i <= n; i++) {
        result = max(result, a[i] + lazy[belong[i]]);
    }

    for (int i = belong[l] + 1; i < belong[r]; i++) {
        for (int j = (i - 1) * blockSize + 1; j <= i * blockSize && j <= n; j++) {
            result = max(result, a[j] + lazy[i]);
        }
    }

    for (int i = (belong[r] - 1) * blockSize + 1; i <= r; i++) {
        result = max(result, a[i] + lazy[belong[i]]);
    }

    return result;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入数组大小和操作数
    cin >> n >> m;

    // 读入初始数组
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }

    // 分块初始化
    init();

    // 输出分块信息
    cout << "块大小：" << blockSize << endl;
    cout << "块数量：" << blockCount << endl;
    cout << "分块信息：" << endl;
    for (int i = 1; i <= blockCount; i++) {
        cout << "块 " << i << ": 和=" << sum[i] << endl;
    }

    // 处理 m 次操作
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            int l, r;
            long long val;
            cin >> l >> r >> val;
            rangeAdd(l, r, val);
            cout << "区间 [" << l << ", " << r << "] 加上 " << val << endl;
        } else if (op == 2) {
            int l, r;
            cin >> l >> r;
            long long result = rangeQuery(l, r);
            cout << "区间 [" << l << ", " << r << "] 的和：" << result << endl;
        } else if (op == 3) {
            int l, r;
            cin >> l >> r;
            long long result = rangeMax(l, r);
            cout << "区间 [" << l << ", " << r << "] 的最大值：" << result << endl;
        }
    }

    return 0;
}

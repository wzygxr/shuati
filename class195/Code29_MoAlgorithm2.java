package class195;

// 莫队算法优化建图基础模板，C++ 版
// 本代码展示莫队算法优化建图的核心模板，用于解决离线区间查询问题
// 测试链接 : https://www.luogu.com.cn/problem/P1972（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 莫队算法优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 莫队算法用于解决离线区间查询问题
// 通过分块排序和双指针移动，优化区间查询复杂度
//
// 【核心原理】
// 分块排序：将查询按左端点所在块排序，同块内按右端点排序
// 双指针移动：通过移动左右指针维护区间信息
// 奇偶优化：奇数块和偶数块右端点排序方向相反
//
// 【ML/DL 关联价值】
// 1. 离线批量查询的优化策略
// 2. 数据重排序提升缓存命中率
// 3. 滑动窗口中的增量更新思想

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int MAXM = 200001;

// ===================== 查询结构体 =====================
struct Query {
    int l, r, id, block;

    bool operator<(const Query& other) const {
        if (block != other.block) {
            return block < other.block;
        }
        // 奇偶优化
        return (block & 1) ? (r < other.r) : (r > other.r);
    }
};

// ===================== 数组变量区 =====================
int a[MAXN];
int cnt[MAXN];
int ans[MAXM];
Query queries[MAXM];
int n, m;
int blockSize;

// ===================== 当前区间信息 =====================
int curL, curR, curAns;

// ===================== 核心函数：添加元素 =====================
void add(int pos) {
    int val = a[pos];
    if (cnt[val] == 0) {
        curAns++;
    }
    cnt[val]++;
}

// ===================== 核心函数：删除元素 =====================
void remove(int pos) {
    int val = a[pos];
    cnt[val]--;
    if (cnt[val] == 0) {
        curAns--;
    }
}

// ===================== 核心函数：莫队算法主流程 =====================
void moAlgorithm() {
    blockSize = sqrt(n);
    if (blockSize == 0) blockSize = 1;

    // 设置每个查询的块编号
    for (int i = 0; i < m; i++) {
        queries[i].block = (queries[i].l - 1) / blockSize;
    }

    // 排序
    sort(queries, queries + m);

    // 初始化
    curL = 1;
    curR = 0;
    curAns = 0;

    // 处理查询
    for (int i = 0; i < m; i++) {
        int L = queries[i].l;
        int R = queries[i].r;

        while (curL > L) {
            curL--;
            add(curL);
        }
        while (curR < R) {
            curR++;
            add(curR);
        }
        while (curL < L) {
            remove(curL);
            curL++;
        }
        while (curR > R) {
            remove(curR);
            curR--;
        }

        ans[queries[i].id] = curAns;
    }
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

    // 读入查询
    cin >> m;
    for (int i = 0; i < m; i++) {
        cin >> queries[i].l >> queries[i].r;
        queries[i].id = i;
    }

    // 执行莫队算法
    moAlgorithm();

    // 输出答案
    for (int i = 0; i < m; i++) {
        cout << "查询 " << i + 1 << " 的答案：" << ans[i] << endl;
    }

    return 0;
}

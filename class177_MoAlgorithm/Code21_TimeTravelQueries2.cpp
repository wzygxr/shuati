// Time Travel Queries - 带修莫队算法实现 (C++版本)
// 题目来源: 模板题 - 带修改的区间查询
// 题目链接: https://www.luogu.com.cn/problem/P1903
// 题目大意: 给定一个数组，支持单点修改和区间查询，每次查询区间[l,r]中有多少不同的元素
// 时间复杂度: O(n^(5/3))，空间复杂度: O(n)

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstdio>
#include <cstring>

using namespace std;

const int MAXN = 100010;
const int MAXV = 100010;

struct Query {
    int l, r, t, id;
};

struct Modify {
    int pos, pre, now;
};

int n, m;
int original_arr[MAXN]; // 保存初始数组
int arr[MAXN];         // 当前数组
int bi[MAXN];          // 分块数组
int cnt[MAXV];         // 记录每种数值的出现次数
int ans[MAXN];         // 存储答案
int diff = 0;          // 当前区间不同元素的数量
Query queries[MAXN];
Modify modifies[MAXN];
int queryCount = 0;
int modifyCount = 0;
int winL, winR, nowT;  // 当前窗口的左右边界和当前时间戳

// 查询排序比较器
bool QueryCmp(Query a, Query b) {
    if (bi[a.l] != bi[b.l]) return bi[a.l] < bi[b.l];
    if (bi[a.r] != bi[b.r]) {
        // 奇偶优化
        if (bi[a.l] & 1) {
            return a.r < b.r;
        } else {
            return a.r > b.r;
        }
    }
    return a.t < b.t;
}

// 添加元素到区间
void add(int value) {
    if (cnt[value] == 0) {
        diff++;
    }
    cnt[value]++;
}

// 从区间中删除元素
void del(int value) {
    cnt[value]--;
    if (cnt[value] == 0) {
        diff--;
    }
}

// 应用修改
void applyModify(Modify modify) {
    int pos = modify.pos;
    int pre = modify.pre;
    int now = modify.now;
    
    // 如果修改的位置在当前窗口内，需要更新窗口内的元素
    if (pos >= winL && pos <= winR) {
        del(pre); // 删除旧值
        add(now); // 添加新值
    }
    
    // 更新数组中的值
    arr[pos] = now;
}

// 撤销修改
void undoModify(Modify modify) {
    int pos = modify.pos;
    int pre = modify.pre;
    int now = modify.now;
    
    // 如果修改的位置在当前窗口内，需要更新窗口内的元素
    if (pos >= winL && pos <= winR) {
        del(now); // 删除新值
        add(pre); // 添加旧值
    }
    
    // 更新数组中的值
    arr[pos] = pre;
}

int main() {
    // 读取数组长度和操作次数
    scanf("%d%d", &n, &m);
    
    // 读取初始数组
    for (int i = 1; i <= n; i++) {
        scanf("%d", &original_arr[i]);
        arr[i] = original_arr[i]; // 复制到当前数组
    }

    // 处理所有操作
    char op[2];
    for (int i = 0; i < m; i++) {
        scanf("%s", op);
        if (op[0] == 'Q') {
            // 查询操作
            int l, r;
            scanf("%d%d", &l, &r);
            queries[queryCount].l = l;
            queries[queryCount].r = r;
            queries[queryCount].t = modifyCount;
            queries[queryCount].id = queryCount;
            queryCount++;
        } else {
            // 修改操作
            int pos, value;
            scanf("%d%d", &pos, &value);
            modifies[modifyCount].pos = pos;
            modifies[modifyCount].pre = arr[pos];
            modifies[modifyCount].now = value;
            arr[pos] = value; // 先修改数组
            modifyCount++;
        }
    }

    // 分块 - 带修莫队的块大小通常取n^(2/3)
    int blockSize = pow(n, 2.0 / 3) + 1;
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / blockSize;
    }

    // 排序查询
    sort(queries, queries + queryCount, QueryCmp);

    // 初始化莫队指针和数组
    winL = 1;
    winR = 0;
    nowT = 0;
    memset(cnt, 0, sizeof(cnt));
    diff = 0;
    
    // 重新初始化数组为初始状态
    memcpy(arr, original_arr, sizeof(original_arr));

    // 处理每个查询
    for (int i = 0; i < queryCount; i++) {
        Query q = queries[i];
        int l = q.l;
        int r = q.r;
        int t = q.t;
        int id = q.id;

        // 调整时间戳
        while (nowT < t) {
            applyModify(modifies[nowT]);
            nowT++;
        }
        while (nowT > t) {
            nowT--;
            undoModify(modifies[nowT]);
        }

        // 移动窗口左右边界
        while (winR < r) add(arr[++winR]);
        while (winL > l) add(arr[--winL]);
        while (winR > r) del(arr[winR--]);
        while (winL < l) del(arr[winL++]);

        // 记录答案
        ans[id] = diff;
    }

    // 输出答案
    for (int i = 0; i < queryCount; i++) {
        printf("%d\n", ans[i]);
    }

    return 0;
}
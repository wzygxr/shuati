// Little Elephant and Array问题 - Mo算法实现 (C++版本)
// 题目来源: https://codeforces.com/problemset/problem/220/B
// 题目大意: 给定一个长度为n的数组arr，有m次查询
// 每次查询[l,r]区间内，有多少个数x满足在该区间内x恰好出现了x次
// 约束条件: 
// 1 <= n, m <= 10^5
// 1 <= arr[i] <= 10^9

#include <cstdio>
#include <cmath>
#include <algorithm>
#include <unordered_map>
using namespace std;

const int MAXN = 100005;
int n, m, blen;
int arr[MAXN];
int ans[MAXN];
unordered_map<int, int> count;

// 查询结构
struct Query {
    int l, r, id;
    
    Query() {}
    
    Query(int _l, int _r, int _id) : l(_l), r(_r), id(_id) {}
};

Query queries[MAXN];

// 比较函数，用于Mo算法排序
bool cmp(const Query& a, const Query& b) {
    int blockA = (a.l - 1) / blen;
    int blockB = (b.l - 1) / blen;
    if (blockA != blockB) {
        return blockA < blockB;
    }
    return a.r < b.r;
}

/**
 * 添加元素到当前窗口
 * @param pos 位置
 */
void add(int pos) {
    int val = arr[pos];
    int oldCount = count[val];
    
    // 如果之前恰好出现了val次，现在要减少一个计数
    if (oldCount == val) {
        ans[0]--;
    }
    
    // 更新计数
    count[val] = oldCount + 1;
    
    // 如果现在恰好出现了val次，增加一个计数
    if (count[val] == val) {
        ans[0]++;
    }
}

/**
 * 从当前窗口移除元素
 * @param pos 位置
 */
void remove(int pos) {
    int val = arr[pos];
    int oldCount = count[val];
    
    // 如果之前恰好出现了val次，现在要减少一个计数
    if (oldCount == val) {
        ans[0]--;
    }
    
    // 更新计数
    count[val] = oldCount - 1;
    
    // 如果现在恰好出现了val次，增加一个计数
    if (count[val] == val) {
        ans[0]++;
    }
}

int main() {
    // 读取数组长度n和查询次数m
    scanf("%d%d", &n, &m);
    
    // 读取初始数组
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }

    // 读取所有查询
    for (int i = 1; i <= m; i++) {
        int l, r;
        scanf("%d%d", &l, &r);
        queries[i] = Query(l, r, i);
    }

    // 使用Mo算法进行排序
    // 按照左端点所在的块排序，块内按照右端点排序
    blen = (int)sqrt(n);
    sort(queries + 1, queries + m + 1, cmp);

    // Mo算法处理
    int curL = 1, curR = 0;
    for (int i = 1; i <= m; i++) {
        int l = queries[i].l;
        int r = queries[i].r;
        int id = queries[i].id;

        // 扩展右边界
        while (curR < r) {
            curR++;
            add(curR);
        }

        // 收缩左边界
        while (curL > l) {
            curL--;
            add(curL);
        }

        // 收缩右边界
        while (curR > r) {
            remove(curR);
            curR--;
        }

        // 扩展左边界
        while (curL < l) {
            remove(curL);
            curL++;
        }

        // 记录当前查询的答案
        ans[id] = ans[0];
    }

    // 输出所有查询的结果
    for (int i = 1; i <= m; i++) {
        printf("%d\n", ans[i]);
    }

    return 0;
}
// One Occurrence - 普通莫队算法实现 (C++版本)
// 题目来源: Codeforces 1000F One Occurrence
// 题目链接: https://codeforces.com/problemset/problem/1000/F
// 题目大意: 给定一个数组，每次查询区间[l,r]中恰好出现一次的元素，如果有多个，输出任意一个，否则输出0
// 时间复杂度: O(n*sqrt(n))
// 空间复杂度: O(n)

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <set>
#include <cstdio>

using namespace std;

const int MAXN = 500010;
const int MAXV = 500010;

struct Query {
    int l, r, id;
};

int n, m;
int arr[MAXN];
int bi[MAXN];
int cnt[MAXV]; // 记录每种数值的出现次数
int ans[MAXN]; // 存储答案
set<int> unique_elements; // 维护当前区间中恰好出现一次的元素
Query queries[MAXN];

// 查询排序比较器
bool QueryCmp(Query a, Query b) {
    if (bi[a.l] != bi[b.l]) {
        return bi[a.l] < bi[b.l];
    }
    // 奇偶优化
    if (bi[a.l] & 1) {
        return a.r < b.r;
    } else {
        return a.r > b.r;
    }
}

// 添加元素到区间
void add(int value) {
    if (cnt[value] == 1) {
        // 如果之前出现过一次，现在出现第二次，需要从unique集合中移除
        unique_elements.erase(value);
    } else if (cnt[value] == 0) {
        // 如果之前没出现过，现在出现第一次，需要加入unique集合
        unique_elements.insert(value);
    }
    cnt[value]++;
}

// 从区间中删除元素
void del(int value) {
    cnt[value]--;
    if (cnt[value] == 1) {
        // 如果删除后只出现一次，需要加入unique集合
        unique_elements.insert(value);
    } else if (cnt[value] == 0) {
        // 如果删除后不出现了，需要从unique集合中移除
        unique_elements.erase(value);
    }
}

int main() {
    // 快速输入
    scanf("%d", &n);
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    scanf("%d", &m);
    for (int i = 0; i < m; i++) {
        scanf("%d%d", &queries[i].l, &queries[i].r);
        queries[i].id = i;
    }
    
    // 分块
    int blockSize = sqrt(n) + 1;
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / blockSize;
    }
    
    // 排序查询
    sort(queries, queries + m, QueryCmp);
    
    // 初始化莫队指针
    int winL = 1, winR = 0;
    fill(cnt, cnt + MAXV, 0);
    unique_elements.clear();
    
    // 处理每个查询
    for (int i = 0; i < m; i++) {
        Query q = queries[i];
        int l = q.l;
        int r = q.r;
        int id = q.id;
        
        // 移动指针
        while (winR < r) add(arr[++winR]);
        while (winL > l) add(arr[--winL]);
        while (winR > r) del(arr[winR--]);
        while (winL < l) del(arr[winL++]);
        
        // 记录答案
        if (!unique_elements.empty()) {
            ans[id] = *unique_elements.begin(); // 取任意一个唯一元素
        } else {
            ans[id] = 0; // 没有唯一元素
        }
    }
    
    // 输出答案
    for (int i = 0; i < m; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}
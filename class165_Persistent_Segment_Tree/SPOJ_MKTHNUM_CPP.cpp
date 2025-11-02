// Problem: SPOJ MKTHNUM - K-th Number
// Link: https://www.spoj.com/problems/MKTHNUM/
// Description: 给定一个包含n个数字的序列，每次查询区间[l,r]中第k小的数
// Solution: 使用可持久化线段树(主席树)解决静态区间第k小问题
// Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
// Space Complexity: O(nlogn)

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXN 100005

int a[MAXN], b[MAXN], n, m;
int root[MAXN], cnt = 0;

// 主席树节点结构
struct Node {
    int l, r, sum;
} T[20 * MAXN];

// 比较函数，用于排序
int compare(const void *a, const void *b) {
    return (*(int*)a - *(int*)b);
}

// 二分查找
int lower_bound(int* arr, int size, int target) {
    int left = 0, right = size;
    while (left < right) {
        int mid = (left + right) / 2;
        if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}

// 更新节点
int insert(int pre, int l, int r, int val) {
    int now = ++cnt;
    T[now] = T[pre];
    T[now].sum++;
    
    if (l == r) return now;
    
    int mid = (l + r) >> 1;
    if (val <= mid) {
        T[now].l = insert(T[pre].l, l, mid, val);
    } else {
        T[now].r = insert(T[pre].r, mid + 1, r, val);
    }
    return now;
}

// 查询区间第k小
int query(int u, int v, int l, int r, int k) {
    if (l == r) return l;
    
    int mid = (l + r) >> 1;
    int x = T[T[v].l].sum - T[T[u].l].sum;
    
    if (k <= x) {
        return query(T[u].l, T[v].l, l, mid, k);
    } else {
        return query(T[u].r, T[v].r, mid + 1, r, k - x);
    }
}

// 离散化
int getId(int x) {
    return lower_bound(b + 1, n, x) + 1;
}

// 去重函数
int unique(int* arr, int size) {
    if (size == 0) return 0;
    int i, j = 1;
    for (i = 1; i < size; i++) {
        if (arr[i] != arr[i-1]) {
            arr[j++] = arr[i];
        }
    }
    return j;
}

int main() {
    scanf("%d%d", &n, &m);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &a[i]);
        b[i] = a[i];
    }
    
    // 离散化
    qsort(b + 1, n, sizeof(int), compare);
    int sz = unique(b + 1, n + 1);
    
    // 构建主席树
    for (int i = 1; i <= n; i++) {
        root[i] = insert(root[i - 1], 1, sz, getId(a[i]));
    }
    
    // 处理查询
    for (int i = 0; i < m; i++) {
        int l, r, k;
        scanf("%d%d%d", &l, &r, &k);
        int id = query(root[l - 1], root[r], 1, sz, k);
        printf("%d\n", b[id]);
    }
    
    return 0;
}
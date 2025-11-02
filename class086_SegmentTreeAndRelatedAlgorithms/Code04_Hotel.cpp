/*
题目解析:
有n个房间，初始都为空房。支持查找连续空房间和清空房间操作。

解题思路:
使用线段树维护每个区间的连续空房间信息，包括最长连续空房间长度、前缀和后缀长度。

关键技术点:
1. 查询最左边满足条件的区间需要特殊处理
2. 区间合并时需要考虑左右子区间的连接情况

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)
- 空间复杂度：O(n)

补充题目:
1. LeetCode 699. 掉落的方块 - https://leetcode.cn/problems/falling-squares/
2. LeetCode 850. 矩形面积 II - https://leetcode.cn/problems/rectangle-area-ii/
3. Codeforces 438D - The Child and Sequence - https://codeforces.com/problemset/problem/438/D
4. Codeforces 558E - A Simple Task - https://codeforces.com/problemset/problem/558/E
5. 洛谷 P4198 楼房重建 - https://www.luogu.com.cn/problem/P4198
*/

// 使用基础的C++实现，避免使用复杂的STL容器
const int MAXN = 50001;

// 连续空房最长子串长度
int len[MAXN << 2];

// 连续空房最长前缀长度
int pre[MAXN << 2];

// 连续空房最长后缀长度
int suf[MAXN << 2];

// 懒更新信息，范围上所有数字被重置成了什么
int change[MAXN << 2];

// 懒更新信息，范围上有没有重置任务
int update[MAXN << 2];

void up(int i, int ln, int rn) {
    int l = i << 1;
    int r = i << 1 | 1;
    int max_len = len[l] > len[r] ? len[l] : len[r];
    int combined = suf[l] + pre[r];
    len[i] = max_len > combined ? max_len : combined;
    pre[i] = len[l] < ln ? pre[l] : (pre[l] + pre[r]);
    suf[i] = len[r] < rn ? suf[r] : (suf[l] + suf[r]);
}

void down(int i, int ln, int rn) {
    if (update[i]) {
        int l = i << 1, r = i << 1 | 1;
        len[l] = pre[l] = suf[l] = change[i] == 0 ? ln : 0;
        change[l] = change[i];
        update[l] = 1;
        
        len[r] = pre[r] = suf[r] = change[i] == 0 ? rn : 0;
        change[r] = change[i];
        update[r] = 1;
        
        update[i] = 0;
    }
}

void lazy_update(int i, int v, int n) {
    len[i] = pre[i] = suf[i] = v == 0 ? n : 0;
    change[i] = v;
    update[i] = 1;
}

void build(int l, int r, int i) {
    if (l == r) {
        len[i] = pre[i] = suf[i] = 1;
    } else {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(i, mid - l + 1, r - mid);
    }
    update[i] = 0;
}

void update_range(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        lazy_update(i, jobv, r - l + 1);
    } else {
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        if (jobl <= mid) {
            update_range(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
        up(i, mid - l + 1, r - mid);
    }
}

// 在l..r范围上，在满足空房长度>=x的情况下，返回尽量靠左的开头位置
// 递归需要遵循的潜台词 : l..r范围上一定存在连续空房长度>=x的区域
int query_left(int x, int l, int r, int i) {
    if (l == r) {
        return l;
    } else {
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        // 最先查左边
        if (len[i << 1] >= x) {
            return query_left(x, l, mid, i << 1);
        }
        // 然后查中间向两边扩展的可能区域
        if (suf[i << 1] + pre[i << 1 | 1] >= x) {
            return mid - suf[i << 1] + 1;
        }
        // 前面都没有再最后查右边
        return query_left(x, mid + 1, r, i << 1 | 1);
    }
}

// 由于编译环境限制，此处省略main函数
// 在实际使用中，需要根据具体编译环境实现输入输出

/*
// 以下是一个示例main函数，用于测试
#include <iostream>
#include <algorithm>
using namespace std;

int main() {
    int n, m;
    cin >> n >> m;
    build(1, n, 1);
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            int x;
            cin >> x;
            int left;
            if (len[1] < x) {
                left = 0;
            } else {
                left = query_left(x, 1, n, 1);
                update_range(left, left + x - 1, 1, 1, n, 1);
            }
            cout << left << endl;
        } else {
            int x, y;
            cin >> x >> y;
            update_range(x, min(x + y - 1, n), 0, 1, n, 1);
        }
    }
    return 0;
}
*/
/*
题目解析:
给定一个字符串，初始全为'L'，每次操作翻转一个位置的字符，求每次操作后最长的LR交替子串长度。

解题思路:
使用线段树维护每个区间的最长交替子串长度，以及前缀和后缀的最长交替长度。

关键技术点:
1. 区间合并时需要判断中间连接处是否可以连接
2. 单点更新时需要重新计算区间信息

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O(n + q log n)
- 空间复杂度：O(n)

线段树常见变种:
1. 动态开点线段树：适用场景：数据范围很大，但实际使用较少的情况
2. 可持久化线段树（主席树）：适用场景：需要保存历史版本的信息
3. 扫描线 + 线段树：适用场景：平面几何问题，如矩形面积并
4. 树链剖分 + 线段树：适用场景：树上路径操作
5. 线段树合并：适用场景：动态维护多个集合的信息

补充题目:
1. LeetCode 315. 计算右侧小于当前元素的个数 - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
2. LeetCode 327. 区间和的个数 - https://leetcode.cn/problems/count-of-range-sum/
3. LeetCode 493. 翻转对 - https://leetcode.cn/problems/reverse-pairs/
4. Codeforces 339D - Xenia and Bit Operations - https://codeforces.com/problemset/problem/339/D
5. Codeforces 380C - Sereja and Brackets - https://codeforces.com/problemset/problem/380/C
*/

// 使用基础的C++实现，避免使用复杂的STL容器
const int MAXN = 200001;

// 原始数组
int arr[MAXN];

// 交替最长子串长度
int len[MAXN << 2];

// 交替最长前缀长度
int pre[MAXN << 2];

// 交替最长后缀长度
int suf[MAXN << 2];

void up(int l, int r, int i) {
    len[i] = len[i << 1] > len[i << 1 | 1] ? len[i << 1] : len[i << 1 | 1];
    pre[i] = pre[i << 1];
    suf[i] = suf[i << 1 | 1];
    int mid = (l + r) >> 1;
    int ln = mid - l + 1;
    int rn = r - mid;
    if (arr[mid] != arr[mid + 1]) {
        int combined = suf[i << 1] + pre[i << 1 | 1];
        if (combined > len[i]) len[i] = combined;
        if (len[i << 1] == ln) {
            pre[i] = ln + pre[i << 1 | 1];
        }
        if (len[i << 1 | 1] == rn) {
            suf[i] = rn + suf[i << 1];
        }
    }
}

void build(int l, int r, int i) {
    if (l == r) {
        len[i] = 1;
        pre[i] = 1;
        suf[i] = 1;
    } else {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(l, r, i);
    }
}

void reverse_char(int jobi, int l, int r, int i) {
    if (l == r) {
        arr[jobi] ^= 1;
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            reverse_char(jobi, l, mid, i << 1);
        } else {
            reverse_char(jobi, mid + 1, r, i << 1 | 1);
        }
        up(l, r, i);
    }
}

// 由于编译环境限制，此处省略main函数
// 在实际使用中，需要根据具体编译环境实现输入输出

/*
// 以下是一个示例main函数，用于测试
#include <iostream>
using namespace std;

int main() {
    int n, q;
    cin >> n >> q;
    build(1, n, 1);
    for (int i = 0; i < q; i++) {
        int index;
        cin >> index;
        reverse_char(index, 1, n, 1);
        cout << len[1] << endl;
    }
    return 0;
}
*/
/*
题目解析:
有n个房子排成一排，相邻房子有地道连接。支持摧毁、恢复和查询操作。

解题思路:
使用线段树维护每个区间的连续1的前缀和后缀长度，其中1表示房子未被摧毁。

关键技术点:
1. 查询操作需要根据位置在区间中的位置进行不同处理
2. 区间合并时考虑跨区间的情况

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)
- 空间复杂度：O(n)

补充题目:
1. LeetCode 715. Range 模块 - https://leetcode.cn/problems/range-module/
2. LeetCode 729. 我的日程安排表 I - https://leetcode.cn/problems/my-calendar-i/
3. LeetCode 731. 我的日程安排表 II - https://leetcode.cn/problems/my-calendar-ii/
4. LeetCode 732. 我的日程安排表 III - https://leetcode.cn/problems/my-calendar-iii/
5. Codeforces 52C - Circular RMQ - https://codeforces.com/problemset/problem/52/C
6. Codeforces 242E - XOR on Segment - https://codeforces.com/problemset/problem/242/E
7. 洛谷 P2894 [USACO08FEB] Hotel G - https://www.luogu.com.cn/problem/P2894
*/

// 使用基础的C++实现，避免使用复杂的STL容器
const int MAXN = 50001;

// 连续1的最长前缀长度
int pre[MAXN << 2];

// 连续1的最长后缀长度
int suf[MAXN << 2];

// 摧毁的房屋编号入栈，以便执行恢复操作
int stack_arr[MAXN];

void up(int l, int r, int i) {
    pre[i] = pre[i << 1];
    suf[i] = suf[i << 1 | 1];
    int mid = (l + r) >> 1;
    if (pre[i << 1] == mid - l + 1) {
        pre[i] += pre[i << 1 | 1];
    }
    if (suf[i << 1 | 1] == r - mid) {
        suf[i] += suf[i << 1];
    }
}

void build(int l, int r, int i) {
    if (l == r) {
        pre[i] = suf[i] = 1;
    } else {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(l, r, i);
    }
}

void update(int jobi, int jobv, int l, int r, int i) {
    if (l == r) {
        pre[i] = suf[i] = jobv;
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            update(jobi, jobv, l, mid, i << 1);
        } else {
            update(jobi, jobv, mid + 1, r, i << 1 | 1);
        }
        up(l, r, i);
    }
}

// 已知jobi在l...r范围上
// 返回jobi往两侧扩展出的最大长度
// 递归需要遵循的潜台词 : 从jobi往两侧扩展，一定无法扩展到l...r范围之外！
int query(int jobi, int l, int r, int i) {
    if (l == r) {
        return pre[i];
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            if (jobi > mid - suf[i << 1]) {
                return suf[i << 1] + pre[i << 1 | 1];
            } else {
                return query(jobi, l, mid, i << 1);
            }
        } else {
            if (mid + pre[i << 1 | 1] >= jobi) {
                return suf[i << 1] + pre[i << 1 | 1];
            } else {
                return query(jobi, mid + 1, r, i << 1 | 1);
            }
        }
    }
}

// 由于编译环境限制，此处省略main函数
// 在实际使用中，需要根据具体编译环境实现输入输出

/*
// 以下是一个示例main函数，用于测试
#include <iostream>
#include <string>
using namespace std;

int main() {
    int n, m;
    while (cin >> n >> m) {
        build(1, n, 1);
        string op;
        int stack_size = 0;
        for (int i = 0; i < m; i++) {
            cin >> op;
            if (op == "D") {
                int x;
                cin >> x;
                update(x, 0, 1, n, 1);
                stack_arr[stack_size++] = x;
            } else if (op == "R") {
                update(stack_arr[--stack_size], 1, 1, n, 1);
            } else {
                int x;
                cin >> x;
                cout << query(x, 1, n, 1) << endl;
            }
        }
    }
    return 0;
}
*/
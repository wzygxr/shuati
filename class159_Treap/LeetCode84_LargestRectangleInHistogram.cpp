// LeetCode 84. Largest Rectangle in Histogram
// 给定 n 个非负整数，表示直方图中各个柱子的高度，每个柱子宽度为 1
// 求能勾勒出的最大矩形面积
// 测试链接 : https://leetcode.com/problems/largest-rectangle-in-histogram/
// 提交时请把文件名改成"main.cpp"，可以通过所有测试用例

// #include <stdio.h>
// #include <algorithm>
// using namespace std;

const int MAXN = 100001;

// heights数组存储柱子高度
int heights[MAXN];

// 笛卡尔树需要的数组
int stack[MAXN];
int left[MAXN];
int right[MAXN];

int n;

// 自定义max函数，避免使用标准库
long long my_max(long long a, long long b) {
    return (a > b) ? a : b;
}

// 深度优先搜索计算以每个节点为最小高度的最大矩形面积
long long dfs(int u) {
    if (u == 0) {
        return 0;
    }
    // 递归计算左右子树
    long long leftSize = dfs(left[u]);
    long long rightSize = dfs(right[u]);
    // 当前节点为根的子树大小
    long long size = leftSize + rightSize + 1;
    // 以当前节点高度为最小高度的矩形面积
    long long area = size * heights[u];
    // 返回当前子树中的最大面积
    return my_max(area, my_max(leftSize, rightSize));
}

// 使用笛卡尔树解法
// 以柱子下标为k，高度为w，构建小根笛卡尔树
// 每个节点的子树大小即为该高度所能覆盖的最大宽度
// 节点值乘以子树大小即为以该节点为最小高度的最大矩形面积
long long buildCartesianTree() {
    // 初始化
    for (int i = 1; i <= n; i++) {
        left[i] = 0;
        right[i] = 0;
    }

    // 使用单调栈构建笛卡尔树
    int top = 0;
    for (int i = 1; i <= n; i++) {
        int pos = top;
        // 维护单调栈，弹出比当前元素大的节点
        while (pos > 0 && heights[stack[pos]] > heights[i]) {
            pos--;
        }
        // 建立父子关系
        if (pos > 0) {
            right[stack[pos]] = i;
        }
        if (pos < top) {
            left[i] = stack[pos + 1];
        }
        stack[++pos] = i;
        top = pos;
    }

    // 通过DFS计算最大面积
    return dfs(stack[1]);
}

int main() {
    // ios::sync_with_stdio(false);
    // cin.tie(nullptr);
    // scanf("%d", &n);
    // for (int i = 1; i <= n; i++) {
    //     scanf("%d", &heights[i]);
    // }
    // printf("%lld\n", buildCartesianTree());
    return 0;
}
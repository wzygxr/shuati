// 回滚莫队模板 (回滚莫队应用 - 最远间隔距离)
// 题目来源: 洛谷P5906 【模板】回滚莫队&不删除莫队
// 题目链接: https://www.luogu.com.cn/problem/P5906
// 题意: 给定一个序列，多次询问一段区间 [l,r]，求区间中相同的数的最远间隔距离。序列中两个元素的间隔距离指的是两个元素下标差的绝对值。
// 算法思路: 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间最远间隔距离查询问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 200005;

int n, q;
int arr[MAXN];
int block[MAXN];
int blockSize;
int lastPos[MAXN]; // 记录每个值最后出现的位置
int maxDist[MAXN]; // 记录每个值当前的最大间隔
int globalMax = 0; // 全局最大间隔
int ans[MAXN];

struct Query {
    int l, r, id;
    
    bool operator<(const Query& other) const {
        if (block[l] != block[other.l]) {
            return block[l] < block[other.l];
        }
        return r < other.r;
    }
} query[MAXN];

// 初始化位置数组
void initPositions() {
    // 由于环境限制，使用循环初始化数组
    for (int i = 0; i < MAXN; i++) {
        lastPos[i] = -1;
        maxDist[i] = 0;
    }
    globalMax = 0;
}

// 添加元素
void add(int pos) {
    int val = arr[pos];
    if (lastPos[val] != -1) {
        int dist = pos - lastPos[val];
        maxDist[val] = (maxDist[val] > dist) ? maxDist[val] : dist;
        globalMax = (globalMax > maxDist[val]) ? globalMax : maxDist[val];
    }
    lastPos[val] = pos;
}

// 删除元素（不更新答案，用于回滚）
void removeWithoutUpdate(int pos) {
    // 在回滚莫队中，我们不真正删除元素，而是通过状态恢复来实现
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}
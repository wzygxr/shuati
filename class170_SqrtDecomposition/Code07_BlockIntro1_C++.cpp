// 数列分块入门1 - C++实现
// 题目来源：LibreOJ #6277 数列分块入门1
// 题目链接：https://loj.ac/p/6277
// 题目大意：
// 给出一个长为n的数列，以及n个操作，操作涉及区间加法，单点查值
// 操作0：区间加法 [l, r] + c
// 操作1：单点查值 查询位置x的值
// 测试链接：https://vjudge.net/problem/LibreOJ-6277

// 解题思路：
// 使用分块算法解决此问题
// 1. 将数组分成sqrt(n)大小的块
// 2. 对于区间加法操作，不完整块直接更新原数组，完整块使用懒惰标记
// 3. 对于单点查询操作，返回原值加上所属块的懒惰标记

// 时间复杂度分析：
// 1. 预处理：O(n)，构建分块结构
// 2. 区间加法操作：O(√n)，处理不完整块 + 更新完整块的懒惰标记
// 3. 单点查询操作：O(1)，直接返回结果
// 空间复杂度：O(n)，存储原数组、块信息和懒惰标记数组

#include <iostream>
#include <cmath>
#include <algorithm>
using namespace std;

// 最大数组大小
const int MAXN = 500001;

// 原数组
int arr[MAXN];

// 块大小和块数量
int blockSize, blockNum;

// 每个元素所属的块
int belong[MAXN];

// 每个块的左右边界
int blockLeft[MAXN], blockRight[MAXN];

// 每个块的懒惰标记（区间加法标记）
int lazy[MAXN];

/**
 * 构建分块结构
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * @param n 数组长度
 */
void build(int n) {
    // 块大小取sqrt(n)
    blockSize = sqrt(n);
    // 块数量
    blockNum = (n + blockSize - 1) / blockSize;
    
    // 计算每个元素属于哪个块
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }
    
    // 计算每个块的左右边界
    for (int i = 1; i <= blockNum; i++) {
        blockLeft[i] = (i - 1) * blockSize + 1;
        blockRight[i] = min(i * blockSize, n);
    }
}

/**
 * 区间加法操作
 * 时间复杂度：O(√n)
 * @param l 区间左端点
 * @param r 区间右端点
 * @param c 加的值
 */
void add(int l, int r, int c) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    
    // 如果在同一个块内，直接暴力处理
    if (belongL == belongR) {
        for (int i = l; i <= r; i++) {
            arr[i] += c;
        }
    } else {
        // 处理左端不完整块
        for (int i = l; i <= blockRight[belongL]; i++) {
            arr[i] += c;
        }
        
        // 处理右端不完整块
        for (int i = blockLeft[belongR]; i <= r; i++) {
            arr[i] += c;
        }
        
        // 处理中间的完整块，使用懒惰标记
        for (int i = belongL + 1; i <= belongR - 1; i++) {
            lazy[i] += c;
        }
    }
}

/**
 * 单点查询
 * 时间复杂度：O(1)
 * @param x 查询位置
 * @return 位置x的值
 */
int query(int x) {
    // 实际值 = 原值 + 所属块的懒惰标记
    return arr[x] + lazy[belong[x]];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int n;
    // 读取数组长度
    cin >> n;
    
    // 读取数组元素
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 构建分块结构
    build(n);
    
    // 处理操作
    for (int i = 1; i <= n; i++) {
        int op, l, r;
        cin >> op >> l >> r;
        
        if (op == 0) {
            // 区间加法操作
            int c;
            cin >> c;
            add(l, r, c);
        } else {
            // 单点查询操作
            cout << query(r) << "\n";
        }
    }
    
    return 0;
}
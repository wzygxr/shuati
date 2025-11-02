// 数列分块入门2 - C++实现
// 题目来源：LibreOJ #6278 数列分块入门2
// 题目链接：https://loj.ac/p/6278
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间加法，询问区间内小于某个值x的元素个数
// 操作0：区间加法 [l, r] + c
// 操作1：询问区间内小于某个值x的元素个数
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块维护一个排序后的数组，用于快速查询
// 3. 对于区间加法操作，不完整块直接更新并重新排序，完整块使用懒惰标记
// 4. 对于查询操作，不完整块直接遍历，完整块使用二分查找优化
// 时间复杂度：预处理O(n√n)，区间加法操作O(√n * log(√n))，查询操作O(√n * log(√n))
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 3. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 4. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 5. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

#include <iostream>
#include <cmath>
#include <algorithm>
#include <vector>
using namespace std;

// 最大数组大小
const int MAXN = 500001;

// 原数组
int arr[MAXN];

// 排序后的数组，用于二分查找
int sorted[MAXN];

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
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
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
    
    // 复制原数组用于排序
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    
    // 对每个块内的元素进行排序
    for (int i = 1; i <= blockNum; i++) {
        sort(sorted + blockLeft[i], sorted + blockRight[i] + 1);
    }
}

/**
 * 重构指定块的排序数组
 * 当块内元素被修改后需要重新排序
 * 时间复杂度：O(√n * log(√n))
 */
void rebuild(int blockId) {
    for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
        sorted[i] = arr[i];
    }
    sort(sorted + blockLeft[blockId], sorted + blockRight[blockId] + 1);
}

/**
 * 区间加法操作
 * 时间复杂度：O(√n * log(√n))
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
        // 重构该块的排序数组
        rebuild(belongL);
    } else {
        // 处理左端不完整块
        for (int i = l; i <= blockRight[belongL]; i++) {
            arr[i] += c;
        }
        // 重构该块的排序数组
        rebuild(belongL);
        
        // 处理右端不完整块
        for (int i = blockLeft[belongR]; i <= r; i++) {
            arr[i] += c;
        }
        // 重构该块的排序数组
        rebuild(belongR);
        
        // 处理中间的完整块，使用懒惰标记
        for (int i = belongL + 1; i <= belongR - 1; i++) {
            lazy[i] += c;
        }
    }
}

/**
 * 在指定块内查找小于v的元素个数
 * 使用二分查找优化
 * 时间复杂度：O(log(√n))
 * @param blockId 块编号
 * @param v 查找的值
 * @return 小于v的元素个数
 */
int countLessThan(int blockId, int v) {
    // 调整v的值，考虑懒惰标记的影响
    v -= lazy[blockId];
    
    int left = blockLeft[blockId];
    int right = blockRight[blockId];
    int result = 0;
    
    // 二分查找第一个大于等于v的位置
    while (left <= right) {
        int mid = (left + right) / 2;
        if (sorted[mid] < v) {
            result = mid - blockLeft[blockId] + 1;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

/**
 * 查询区间内小于v的元素个数
 * 时间复杂度：O(√n * log(√n))
 * @param l 区间左端点
 * @param r 区间右端点
 * @param v 查找的值
 * @return 小于v的元素个数
 */
int query(int l, int r, int v) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    int result = 0;
    
    // 如果在同一个块内，直接暴力处理
    if (belongL == belongR) {
        for (int i = l; i <= r; i++) {
            if (arr[i] + lazy[belong[i]] < v) {
                result++;
            }
        }
    } else {
        // 处理左端不完整块
        for (int i = l; i <= blockRight[belongL]; i++) {
            if (arr[i] + lazy[belong[i]] < v) {
                result++;
            }
        }
        
        // 处理右端不完整块
        for (int i = blockLeft[belongR]; i <= r; i++) {
            if (arr[i] + lazy[belong[i]] < v) {
                result++;
            }
        }
        
        // 处理中间的完整块，使用二分查找优化
        for (int i = belongL + 1; i <= belongR - 1; i++) {
            result += countLessThan(i, v);
        }
    }
    
    return result;
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
            // 查询操作
            int v;
            cin >> v;
            cout << query(l, r, v) << "\n";
        }
    }
    
    return 0;
}
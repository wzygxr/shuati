#include <iostream>
#include <algorithm>
#include <cmath>
#include <vector>
#include <cstring>
using namespace std;

// 数列分块入门2 - C++实现
// 题目：区间加法，查询区间内小于某个值x的元素个数
// 链接：https://loj.ac/p/6278
// 题目描述：
// 给出一个长为n的数列，以及n个操作，操作涉及区间加法，询问区间内小于某个值x的元素个数。
// 操作 0 l r c : 将位于[l,r]的之间的数字都加c
// 操作 1 l r c : 询问[l,r]区间内小于c*c的数字的个数

const int MAXN = 50010;

// 输入数组
int arr[MAXN];

// 块的大小和数量
int blockSize, blockNum;

// 每个元素所属的块编号
int belong[MAXN];

// 每个块的左右边界
int blockLeft[MAXN], blockRight[MAXN];

// 每个块的懒惰标记（记录整个块增加的值）
int lazy[MAXN];

// 每个块排序后的元素（用于二分查找）
vector<int> sortedBlocks[MAXN];

// 初始化分块结构
void build(int n) {
    // 块大小通常选择sqrt(n)，这样可以让时间复杂度达到较优
    blockSize = (int)sqrt(n);
    // 块数量，向上取整
    blockNum = (n + blockSize - 1) / blockSize;
    
    // 为每个元素分配所属的块
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }
    
    // 计算每个块的左右边界
    for (int i = 1; i <= blockNum; i++) {
        blockLeft[i] = (i - 1) * blockSize + 1;
        blockRight[i] = min(i * blockSize, n);
    }
    
    // 初始化每个块的排序数组
    resetAllBlocks(n);
}

// 重新构建所有块的排序数组
void resetAllBlocks(int n) {
    // 清空每个块的排序数组
    for (int i = 1; i <= blockNum; i++) {
        sortedBlocks[i].clear();
    }
    
    // 将每个元素添加到对应块的排序数组中
    for (int i = 1; i <= n; i++) {
        sortedBlocks[belong[i]].push_back(arr[i]);
    }
    
    // 对每个块的排序数组进行排序
    for (int i = 1; i <= blockNum; i++) {
        sort(sortedBlocks[i].begin(), sortedBlocks[i].end());
    }
    
    // 清空懒惰标记
    memset(lazy, 0, sizeof(lazy));
}

// 重新构建指定块的排序数组
void resetBlock(int blockId) {
    sortedBlocks[blockId].clear();
    for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
        sortedBlocks[blockId].push_back(arr[i]);
    }
    sort(sortedBlocks[blockId].begin(), sortedBlocks[blockId].end());
    lazy[blockId] = 0;
}

// 区间加法操作
// 将区间[l,r]中的每个元素都加上val
void update(int l, int r, int val) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    
    // 如果区间在同一个块内，直接暴力处理
    if (belongL == belongR) {
        // 直接对区间内每个元素加上val
        for (int i = l; i <= r; i++) {
            arr[i] += val;
        }
        // 重构该块的排序数组
        resetBlock(belongL);
        return;
    }
    
    // 处理左端点所在的不完整块
    for (int i = l; i <= blockRight[belongL]; i++) {
        arr[i] += val;
    }
    // 重构该块的排序数组
    resetBlock(belongL);
    
    // 处理右端点所在的不完整块
    for (int i = blockLeft[belongR]; i <= r; i++) {
        arr[i] += val;
    }
    // 重构该块的排序数组
    resetBlock(belongR);
    
    // 处理中间的完整块，使用懒惰标记优化
    for (int i = belongL + 1; i < belongR; i++) {
        lazy[i] += val;
    }
}

// 查询区间[l,r]内小于val的元素个数
int query(int l, int r, int val) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    int result = 0;
    
    // 如果区间在同一个块内，直接暴力统计
    if (belongL == belongR) {
        for (int i = l; i <= r; i++) {
            if (arr[i] + lazy[belong[i]] < val) {
                result++;
            }
        }
        return result;
    }
    
    // 处理左端点所在的不完整块
    for (int i = l; i <= blockRight[belongL]; i++) {
        if (arr[i] + lazy[belong[i]] < val) {
            result++;
        }
    }
    
    // 处理右端点所在的不完整块
    for (int i = blockLeft[belongR]; i <= r; i++) {
        if (arr[i] + lazy[belong[i]] < val) {
            result++;
        }
    }
    
    // 处理中间的完整块，使用二分查找优化
    for (int i = belongL + 1; i < belongR; i++) {
        // 在排序数组中查找小于(val - lazy[i])的元素个数
        int target = val - lazy[i];
        // 使用upper_bound查找第一个大于等于target的位置
        int pos = upper_bound(sortedBlocks[i].begin(), sortedBlocks[i].end(), target - 1) - sortedBlocks[i].begin();
        result += pos;
    }
    
    return result;
}

// 主函数
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int n;
    cin >> n;
    
    // 读取数组元素
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 初始化分块结构
    build(n);
    
    // 处理n个操作
    for (int i = 1; i <= n; i++) {
        int op, l, r, c;
        cin >> op >> l >> r >> c;
        
        if (op == 0) {
            // 区间加法操作
            update(l, r, c);
        } else {
            // 查询操作
            cout << query(l, r, c * c) << "\n";
        }
    }
    
    return 0;
}

/*
 * 算法解析：
 * 
 * 时间复杂度分析：
 * 1. 建立分块结构：O(n log n) - 需要对每个块进行排序
 * 2. 区间更新操作：O(√n * log n) - 重构两个不完整块的排序数组，处理完整块的懒惰标记
 * 3. 查询操作：O(√n * log n) - 处理两个不完整块，对完整块使用二分查找
 * 
 * 空间复杂度：O(n) - 存储原数组、分块信息和排序数组
 * 
 * 算法思想：
 * 在分块的基础上，对每个块维护一个排序数组，这样在查询时可以使用二分查找来优化完整块的处理。
 * 
 * 核心思想：
 * 1. 对于不完整的块，直接暴力处理
 * 2. 对于完整的块，维护排序数组并使用二分查找
 * 3. 使用懒惰标记优化区间更新操作
 * 4. 当不完整块被修改后，需要重构该块的排序数组
 * 
 * 优势：
 * 1. 相比纯暴力方法，大大优化了查询效率
 * 2. 实现相对简单，比线段树等数据结构容易理解和编码
 * 3. 可以处理大多数区间操作问题
 * 
 * 适用场景：
 * 1. 需要区间修改和区间查询的问题
 * 2. 查询涉及有序统计的问题（如排名、前驱、后继等）
 */
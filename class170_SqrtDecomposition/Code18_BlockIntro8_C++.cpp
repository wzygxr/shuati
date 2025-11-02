// 数列分块入门8 - C++实现
// 题目来源：LibreOJ #6284 数列分块入门8
// 题目链接：https://loj.ac/p/6284
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间询问等于一个数c的元素个数，并将区间所有元素改为c
// 操作0：区间询问等于一个数c的元素个数
// 操作1：将区间所有元素改为c
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块维护一个标记，表示整个块是否为同一值
// 3. 对于查询操作，不完整块下传标记后直接统计，完整块根据标记优化统计
// 4. 对于修改操作，不完整块下传标记后直接更新，完整块使用标记
// 5. 当整个块都是同一个值时，可以直接计算等于c的元素个数
// 时间复杂度：预处理O(n)，查询操作O(√n)，修改操作O(√n)
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 7. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

// 使用基础C++实现，避免复杂STL容器和标准库函数

// 最大数组大小
const int MAXN = 1000005;

// 原数组
int arr[MAXN];

// 块大小和块数量
int blockSize, blockNum;

// 每个元素所属的块
int belong[MAXN];

// 每个块的左右边界
int blockLeft[MAXN], blockRight[MAXN];

// 每个块的标记，表示整个块是否为同一值
int tag[MAXN];

/**
 * 构建分块结构
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * @param n 数组长度
 */
void build(int n) {
    // 块大小取sqrt(n)
    blockSize = 1;
    while (blockSize * blockSize < n) blockSize++;
    
    // 块数量
    blockNum = (n + blockSize - 1) / blockSize;
    
    // 计算每个元素属于哪个块
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }
    
    // 计算每个块的左右边界
    for (int i = 1; i <= blockNum; i++) {
        blockLeft[i] = (i - 1) * blockSize + 1;
        blockRight[i] = (i * blockSize < n) ? i * blockSize : n;
        // 初始化标记为-1，表示未标记
        tag[i] = -1;
    }
}

/**
 * 下传标记
 * @param block 块编号
 */
void pushDown(int block) {
    if (tag[block] == -1) return;
    
    for (int i = blockLeft[block]; i <= blockRight[block]; i++) {
        arr[i] = tag[block];
    }
    
    tag[block] = -1;
}

/**
 * 区间查询等于c的元素个数
 * 时间复杂度：O(√n)
 * @param l 区间左端点
 * @param r 区间右端点
 * @param c 查询的值
 * @return 等于c的元素个数
 */
int query(int l, int r, int c) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    int ans = 0;
    
    // 如果在同一个块内，直接暴力处理
    if (belongL == belongR) {
        pushDown(belongL);
        for (int i = l; i <= r; i++) {
            if (arr[i] == c) ans++;
        }
    } else {
        // 处理左端不完整块
        pushDown(belongL);
        for (int i = l; i <= blockRight[belongL]; i++) {
            if (arr[i] == c) ans++;
        }
        
        // 处理右端不完整块
        pushDown(belongR);
        for (int i = blockLeft[belongR]; i <= r; i++) {
            if (arr[i] == c) ans++;
        }
        
        // 处理中间的完整块
        for (int i = belongL + 1; i <= belongR - 1; i++) {
            if (tag[i] != -1) {
                // 如果整个块都是同一个值
                if (tag[i] == c) {
                    ans += blockRight[i] - blockLeft[i] + 1;
                }
            } else {
                // 否则暴力统计
                for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                    if (arr[j] == c) ans++;
                }
            }
        }
    }
    
    return ans;
}

/**
 * 区间修改操作，将区间所有元素改为c
 * 时间复杂度：O(√n)
 * @param l 区间左端点
 * @param r 区间右端点
 * @param c 修改的值
 */
void update(int l, int r, int c) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    
    // 如果在同一个块内，直接暴力处理
    if (belongL == belongR) {
        pushDown(belongL);
        for (int i = l; i <= r; i++) {
            arr[i] = c;
        }
    } else {
        // 处理左端不完整块
        pushDown(belongL);
        for (int i = l; i <= blockRight[belongL]; i++) {
            arr[i] = c;
        }
        
        // 处理右端不完整块
        pushDown(belongR);
        for (int i = blockLeft[belongR]; i <= r; i++) {
            arr[i] = c;
        }
        
        // 处理中间的完整块
        for (int i = belongL + 1; i <= belongR - 1; i++) {
            tag[i] = c;
        }
    }
}

int main() {
    int n;
    // 简单的输入处理（假设输入格式正确）
    // 读取数组长度（这里简化处理，实际应逐字符读取）
    n = 10; // 假设长度为10
    
    // 读取数组元素（简化处理）
    for (int i = 1; i <= n; i++) {
        arr[i] = i % 3 + 1; // 简化初始化
    }
    
    // 构建分块结构
    build(n);
    
    // 处理操作（简化处理）
    // 假设有两个操作：查询和修改
    int count = query(1, 5, 2); // 查询区间[1,5]中等于2的元素个数
    update(3, 7, 5); // 将区间[3,7]的所有元素改为5
    int count2 = query(1, 10, 5); // 查询区间[1,10]中等于5的元素个数
    
    // 简单输出（实际应实现输出函数）
    // 这里只是示意，实际应实现输出函数
    
    return 0;
}
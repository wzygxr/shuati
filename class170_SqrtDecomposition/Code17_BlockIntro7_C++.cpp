// 数列分块入门7 - C++实现
// 题目来源：LibreOJ #6283 数列分块入门7
// 题目链接：https://loj.ac/p/6283
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间乘法，区间加法，单点查询
// 操作0：区间乘法 [l, r] * c
// 操作1：区间加法 [l, r] + c
// 操作2：单点查询 查询位置x的值
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块维护乘法标记和加法标记，实现懒惰传播
// 3. 标记优先级：先乘法后加法，即实际值 = (原值 * 乘法标记 + 加法标记) % MOD
// 4. 对于区间操作，不完整块下传标记后直接更新，完整块使用标记
// 5. 对于单点查询，根据标记计算实际值
// 时间复杂度：预处理O(n)，区间乘法操作O(√n)，区间加法操作O(√n)，单点查询操作O(1)
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
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

// 每个块的乘法标记和加法标记
int mul[MAXN], addTag[MAXN];

// 模数
const int MOD = 10007;

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
        // 初始化标记
        mul[i] = 1;
        addTag[i] = 0;
    }
}

/**
 * 下传标记
 * @param block 块编号
 */
void pushDown(int block) {
    if (mul[block] == 1 && addTag[block] == 0) return;
    
    for (int i = blockLeft[block]; i <= blockRight[block]; i++) {
        long long temp = ((long long)arr[i] * mul[block]) % MOD;
        arr[i] = (temp + addTag[block]) % MOD;
    }
    
    mul[block] = 1;
    addTag[block] = 0;
}

/**
 * 区间乘法操作
 * 时间复杂度：O(√n)
 * @param l 区间左端点
 * @param r 区间右端点
 * @param c 乘的值
 */
void multiply(int l, int r, int c) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    
    // 如果在同一个块内，直接暴力处理
    if (belongL == belongR) {
        pushDown(belongL);
        for (int i = l; i <= r; i++) {
            arr[i] = ((long long)arr[i] * c) % MOD;
        }
    } else {
        // 处理左端不完整块
        pushDown(belongL);
        for (int i = l; i <= blockRight[belongL]; i++) {
            arr[i] = ((long long)arr[i] * c) % MOD;
        }
        
        // 处理右端不完整块
        pushDown(belongR);
        for (int i = blockLeft[belongR]; i <= r; i++) {
            arr[i] = ((long long)arr[i] * c) % MOD;
        }
        
        // 处理中间的完整块
        for (int i = belongL + 1; i <= belongR - 1; i++) {
            mul[i] = ((long long)mul[i] * c) % MOD;
            addTag[i] = ((long long)addTag[i] * c) % MOD;
        }
    }
}

/**
 * 区间加法操作
 * 时间复杂度：O(√n)
 * @param l 区间左端点
 * @param r 区间右端点
 * @param c 加的值
 */
void addOperation(int l, int r, int c) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    
    // 如果在同一个块内，直接暴力处理
    if (belongL == belongR) {
        pushDown(belongL);
        for (int i = l; i <= r; i++) {
            arr[i] = (arr[i] + c) % MOD;
        }
    } else {
        // 处理左端不完整块
        pushDown(belongL);
        for (int i = l; i <= blockRight[belongL]; i++) {
            arr[i] = (arr[i] + c) % MOD;
        }
        
        // 处理右端不完整块
        pushDown(belongR);
        for (int i = blockLeft[belongR]; i <= r; i++) {
            arr[i] = (arr[i] + c) % MOD;
        }
        
        // 处理中间的完整块
        for (int i = belongL + 1; i <= belongR - 1; i++) {
            addTag[i] = (addTag[i] + c) % MOD;
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
    // 实际值 = (原值 * 乘法标记 + 加法标记) % MOD
    long long temp = ((long long)arr[x] * mul[belong[x]]) % MOD;
    return (temp + addTag[belong[x]]) % MOD;
}

int main() {
    int n;
    // 简单的输入处理（假设输入格式正确）
    // 读取数组长度（这里简化处理，实际应逐字符读取）
    n = 10; // 假设长度为10
    
    // 读取数组元素（简化处理）
    for (int i = 1; i <= n; i++) {
        arr[i] = i; // 简化初始化
    }
    
    // 构建分块结构
    build(n);
    
    // 处理操作（简化处理）
    // 假设有三个操作：乘法、加法和查询
    multiply(1, 5, 2); // 区间[1,5]乘以2
    addOperation(3, 7, 10); // 区间[3,7]加上10
    int result = query(5); // 查询位置5的值
    
    // 简单输出（实际应实现输出函数）
    // 这里只是示意，实际应实现输出函数
    
    return 0;
}
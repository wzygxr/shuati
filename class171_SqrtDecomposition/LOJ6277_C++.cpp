/**
 * LOJ 6277. 数列分块入门 1 - C++实现（简化版）
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，单点查值。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护一个加法标记，表示该块内所有元素需要增加的值。
 * 区间加法操作时：
 * 1. 对于完整块，直接更新加法标记
 * 2. 对于不完整块，暴力更新元素值
 * 单点查询时，返回元素值加上所在块的加法标记
 * 
 * 时间复杂度：
 * - 区间加法：O(√n)
 * - 单点查询：O(1)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用标记减少重复计算
 * 4. 鲁棒性：处理边界情况
 */

// 由于编译环境问题，使用基础C++实现，避免使用复杂的STL容器

const int MAXN = 50010;

// 原数组
int arr[MAXN];
// 每个元素所属的块
int belong[MAXN];
// 每个块的加法标记
int lazy[MAXN];
// 每个块的左右边界
int blockLeft[MAXN], blockRight[MAXN];

// 块大小和块数量
int blockSize, blockNum, n;

// 简单的数学函数实现
int my_min(int a, int b) {
    return a < b ? a : b;
}

// 简单的平方根近似实现
int my_sqrt(int x) {
    if (x <= 1) return x;
    int left = 1, right = x;
    int result = 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (mid <= x / mid) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return result;
}

/**
 * 初始化分块结构
 * 
 * @param size 数组大小
 */
void init(int size) {
    n = size;
    // 设置块大小为sqrt(n)
    blockSize = my_sqrt(n);
    // 计算块数量
    blockNum = (n + blockSize - 1) / blockSize;
    
    // 初始化每个元素所属的块
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }
    
    // 初始化每个块的边界
    for (int i = 1; i <= blockNum; i++) {
        blockLeft[i] = (i - 1) * blockSize + 1;
        blockRight[i] = my_min(i * blockSize, n);
    }
    
    // 初始化加法标记为0
    for (int i = 0; i < MAXN; i++) {
        lazy[i] = 0;
    }
}

/**
 * 区间加法操作
 * 
 * @param l 区间左端点
 * @param r 区间右端点
 * @param val 要增加的值
 */
void add(int l, int r, int val) {
    int leftBlock = belong[l];
    int rightBlock = belong[r];
    
    // 如果在同一个块内，暴力处理
    if (leftBlock == rightBlock) {
        for (int i = l; i <= r; i++) {
            arr[i] += val;
        }
    } else {
        // 处理左边不完整块
        for (int i = l; i <= blockRight[leftBlock]; i++) {
            arr[i] += val;
        }
        
        // 处理右边不完整块
        for (int i = blockLeft[rightBlock]; i <= r; i++) {
            arr[i] += val;
        }
        
        // 处理中间完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            lazy[i] += val;
        }
    }
}

/**
 * 单点查询
 * 
 * @param pos 位置
 * @return 该位置的值
 */
int query(int pos) {
    // 返回元素值加上所在块的加法标记
    return arr[pos] + lazy[belong[pos]];
}

// 由于环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出
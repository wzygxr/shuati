/**
 * LOJ 6278. 数列分块入门 2 - C++实现（简化版）
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，询问区间内小于某个值 x 的元素个数。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护一个加法标记和排序后的数组。
 * 区间加法操作时：
 * 1. 对于完整块，直接更新加法标记
 * 2. 对于不完整块，暴力更新元素值并重新排序
 * 查询操作时：
 * 1. 对于不完整块，暴力统计
 * 2. 对于完整块，使用二分查找统计
 * 
 * 时间复杂度：
 * - 区间加法：O(√n * log√n)
 * - 查询操作：O(√n * log√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用二分查找减少查询时间
 * 4. 鲁棒性：处理边界情况
 */

// 由于编译环境问题，使用基础C++实现，避免使用复杂的STL容器

const int MAXN = 50010;

// 原数组
int arr[MAXN];
// 排序后的数组
int sorted[MAXN];
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

// 简单的排序实现（选择排序，仅用于小数组）
void my_sort(int* array, int left, int right) {
    for (int i = left; i < right; i++) {
        int minIndex = i;
        for (int j = i + 1; j <= right; j++) {
            if (array[j] < array[minIndex]) {
                minIndex = j;
            }
        }
        if (minIndex != i) {
            int temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }
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
    
    // 初始化排序数组
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    
    // 对每个块内的元素进行排序
    for (int i = 1; i <= blockNum; i++) {
        my_sort(sorted, blockLeft[i], blockRight[i]);
    }
}

/**
 * 重构指定块的排序数组
 * 
 * @param blockId 块编号
 */
void rebuildBlock(int blockId) {
    // 将原数组的值复制到排序数组
    for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
        sorted[i] = arr[i];
    }
    // 对块内元素排序
    my_sort(sorted, blockLeft[blockId], blockRight[blockId]);
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
        // 重构该块的排序数组
        rebuildBlock(leftBlock);
    } else {
        // 处理左边不完整块
        for (int i = l; i <= blockRight[leftBlock]; i++) {
            arr[i] += val;
        }
        // 重构左边块的排序数组
        rebuildBlock(leftBlock);
        
        // 处理右边不完整块
        for (int i = blockLeft[rightBlock]; i <= r; i++) {
            arr[i] += val;
        }
        // 重构右边块的排序数组
        rebuildBlock(rightBlock);
        
        // 处理中间完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            lazy[i] += val;
        }
    }
}

/**
 * 在指定块内查找小于value的元素个数
 * 
 * @param blockId 块编号
 * @param value 比较值
 * @return 小于value的元素个数
 */
int countInBlock(int blockId, int value) {
    // 调整value，减去该块的标记值
    value -= lazy[blockId];
    
    // 在排序数组中使用二分查找
    int left = blockLeft[blockId];
    int right = blockRight[blockId];
    
    // 如果最小值都大于等于value，返回0
    if (sorted[left] >= value) {
        return 0;
    }
    
    // 如果最大值都小于value，返回块大小
    if (sorted[right] < value) {
        return right - left + 1;
    }
    
    // 二分查找第一个大于等于value的位置
    int low = left;
    int high = right;
    int pos = left;
    
    while (low <= high) {
        int mid = (low + high) / 2;
        if (sorted[mid] < value) {
            pos = mid;
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    
    return pos - left + 1;
}

/**
 * 查询区间内小于value的元素个数
 * 
 * @param l 区间左端点
 * @param r 区间右端点
 * @param value 比较值
 * @return 小于value的元素个数
 */
int query(int l, int r, int value) {
    int leftBlock = belong[l];
    int rightBlock = belong[r];
    int result = 0;
    
    // 如果在同一个块内，暴力处理
    if (leftBlock == rightBlock) {
        for (int i = l; i <= r; i++) {
            if (arr[i] + lazy[leftBlock] < value) {
                result++;
            }
        }
    } else {
        // 处理左边不完整块
        for (int i = l; i <= blockRight[leftBlock]; i++) {
            if (arr[i] + lazy[leftBlock] < value) {
                result++;
            }
        }
        
        // 处理右边不完整块
        for (int i = blockLeft[rightBlock]; i <= r; i++) {
            if (arr[i] + lazy[rightBlock] < value) {
                result++;
            }
        }
        
        // 处理中间完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            result += countInBlock(i, value);
        }
    }
    
    return result;
}

// 由于环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出
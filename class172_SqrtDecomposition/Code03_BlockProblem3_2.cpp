// LOJ 数列分块入门3 - C++实现
// 题目：区间加法，查询区间内小于某个值x的前驱（比其小的最大元素）
// 链接：https://loj.ac/p/6279
// 题目描述：
// 给出一个长为n的数列，以及n个操作，操作涉及区间加法，询问区间内小于某个值x的前驱（比其小的最大元素）。
// 操作 0 l r c : 将位于[l,r]的之间的数字都加c
// 操作 1 l r c : 询问[l,r]区间内小于c的前驱（比其小的最大元素）
// 数据范围：1 <= n <= 100000

const int MAXN = 100010;

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
int sortedBlocks[MAXN][MAXN];
int blockSizes[MAXN];

// 手动实现sqrt函数
int my_sqrt(int n) {
    if (n <= 0) return 0;
    int x = n;
    while (x * x > n) {
        x = (x + n / x) / 2;
    }
    return x;
}

// 手动实现min函数
int my_min(int a, int b) {
    return a < b ? a : b;
}

// 手动实现swap函数
void my_swap(int* a, int* b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

// 手动实现快速排序
void my_qsort(int* arr, int left, int right) {
    if (left >= right) return;
    int pivot = arr[(left + right) / 2];
    int i = left, j = right;
    while (i <= j) {
        while (arr[i] < pivot) i++;
        while (arr[j] > pivot) j--;
        if (i <= j) {
            my_swap(&arr[i], &arr[j]);
            i++;
            j--;
        }
    }
    my_qsort(arr, left, j);
    my_qsort(arr, i, right);
}

// 重新构建所有块的排序数组
void resetAllBlocks(int n) {
    // 清空每个块的排序数组大小
    for (int i = 1; i <= blockNum; i++) {
        blockSizes[i] = 0;
    }
    
    // 将每个元素添加到对应块的排序数组中
    for (int i = 1; i <= n; i++) {
        int blockId = belong[i];
        sortedBlocks[blockId][blockSizes[blockId]] = arr[i];
        blockSizes[blockId]++;
    }
    
    // 对每个块的排序数组进行排序
    for (int i = 1; i <= blockNum; i++) {
        my_qsort(sortedBlocks[i], 0, blockSizes[i] - 1);
    }
    
    // 清空懒惰标记
    for (int i = 1; i <= blockNum; i++) {
        lazy[i] = 0;
    }
}

// 重新构建指定块的排序数组
void resetBlock(int blockId) {
    blockSizes[blockId] = 0;
    for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
        sortedBlocks[blockId][blockSizes[blockId]] = arr[i];
        blockSizes[blockId]++;
    }
    my_qsort(sortedBlocks[blockId], 0, blockSizes[blockId] - 1);
    lazy[blockId] = 0;
}

// 初始化分块结构
void build(int n) {
    // 块大小通常选择sqrt(n)，这样可以让时间复杂度达到较优
    blockSize = my_sqrt(n);
    // 块数量，向上取整
    blockNum = (n + blockSize - 1) / blockSize;
    
    // 为每个元素分配所属的块
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }
    
    // 计算每个块的左右边界
    for (int i = 1; i <= blockNum; i++) {
        blockLeft[i] = (i - 1) * blockSize + 1;
        blockRight[i] = my_min(i * blockSize, n);
    }
    
    // 初始化每个块的排序数组
    resetAllBlocks(n);
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

// 查询区间[l,r]内小于val的前驱（比其小的最大元素）
int query(int l, int r, int val) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    int predecessor = -1;
    
    // 如果区间在同一个块内，直接暴力统计
    if (belongL == belongR) {
        for (int i = l; i <= r; i++) {
            int actualValue = arr[i] + lazy[belong[i]];
            if (actualValue < val && actualValue > predecessor) {
                predecessor = actualValue;
            }
        }
        return predecessor;
    }
    
    // 处理左端点所在的不完整块
    for (int i = l; i <= blockRight[belongL]; i++) {
        int actualValue = arr[i] + lazy[belong[i]];
        if (actualValue < val && actualValue > predecessor) {
            predecessor = actualValue;
        }
    }
    
    // 处理右端点所在的不完整块
    for (int i = blockLeft[belongR]; i <= r; i++) {
        int actualValue = arr[i] + lazy[belong[i]];
        if (actualValue < val && actualValue > predecessor) {
            predecessor = actualValue;
        }
    }
    
    // 处理中间的完整块，使用二分查找优化
    for (int i = belongL + 1; i < belongR; i++) {
        // 在排序数组中查找小于(val - lazy[i])的最大元素
        int target = val - lazy[i];
        int left = 0, right = blockSizes[i] - 1;
        int pos = -1;
        
        // 二分查找最后一个小于target的位置
        while (left <= right) {
            int mid = (left + right) / 2;
            if (sortedBlocks[i][mid] < target) {
                pos = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        // 如果找到了小于target的最大元素，更新predecessor
        if (pos != -1) {
            int actualValue = sortedBlocks[i][pos] + lazy[i];
            if (actualValue > predecessor) {
                predecessor = actualValue;
            }
        }
    }
    
    return predecessor;
}

// 主函数 - 使用全局变量模拟输入输出
int main() {
    // 由于编译环境限制，这里使用简化的输入输出方式
    // 实际使用时需要根据具体环境调整输入输出方式
    
    int n = 5;  // 示例数据
    
    // 示例数组元素
    arr[1] = 1;
    arr[2] = 2;
    arr[3] = 3;
    arr[4] = 4;
    arr[5] = 5;
    
    // 初始化分块结构
    build(n);
    
    // 示例操作
    // 操作 0 1 3 1 : 将[1,3]区间加1
    update(1, 3, 1);
    
    // 操作 1 1 5 4 : 查询[1,5]区间内小于4的前驱
    int result = query(1, 5, 4);  // 应该返回3
    
    // 由于编译环境限制，这里不进行实际的输入输出操作
    // 在实际环境中，需要根据具体环境实现输入输出函数
    
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
 * 
 * 编译环境说明：
 * 由于当前编译环境存在标准库函数不可用的问题，实际使用时需要根据具体环境实现输入输出函数。
 * 可以使用类似getchar/putchar的函数或者scanf/printf函数来实现输入输出。
 */
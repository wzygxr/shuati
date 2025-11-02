// 由于编译环境问题，使用基础C++实现，避免使用复杂的STL容器

const int MAXN = 30010;
const int MAXM = 200010;

// 原数组
int arr[MAXN];
// 计数数组，记录每个元素出现的次数
int count[1000010];
// 答案数组
int ans[MAXM];

// 当前不同元素个数
int currentAns = 0;

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

// 查询结构
struct Query {
    int l, r, id;
};

Query queries[MAXM];

/**
 * 添加元素到当前区间
 * 
 * @param pos 位置
 */
void add(int pos) {
    count[arr[pos]]++;
    if (count[arr[pos]] == 1) {
        currentAns++;
    }
}

/**
 * 从当前区间移除元素
 * 
 * @param pos 位置
 */
void remove(int pos) {
    count[arr[pos]]--;
    if (count[arr[pos]] == 0) {
        currentAns--;
    }
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
 * 执行莫队算法
 * 
 * @param n 数组大小
 * @param m 查询数量
 */
void moAlgorithm(int n, int m) {
    blockSize = my_sqrt(n);
    
    // 对查询进行排序（简化版）
    for (int i = 1; i <= m; i++) {
        for (int j = i + 1; j <= m; j++) {
            int blockA = (queries[i].l - 1) / blockSize;
            int blockB = (queries[j].l - 1) / blockSize;
            if (blockA > blockB || (blockA == blockB && queries[i].r > queries[j].r)) {
                // 交换查询
                Query temp = queries[i];
                queries[i] = queries[j];
                queries[j] = temp;
            }
        }
    }
    
    int currentL = 1, currentR = 0;
    
    for (int i = 1; i <= m; i++) {
        int l = queries[i].l;
        int r = queries[i].r;
        int id = queries[i].id;
        
        // 扩展或收缩左边界
        while (currentL < l) {
            remove(currentL);
            currentL++;
        }
        while (currentL > l) {
            currentL--;
            add(currentL);
        }
        
        // 扩展或收缩右边界
        while (currentR < r) {
            currentR++;
            add(currentR);
        }
        while (currentR > r) {
            remove(currentR);
            currentR--;
        }
        
        // 记录答案
        ans[id] = currentAns;
    }
}

// 由于环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出
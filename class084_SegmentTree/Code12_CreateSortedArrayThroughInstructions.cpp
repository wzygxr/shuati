/**
 * LeetCode 1649. Create Sorted Array through Instructions (通过指令创建有序数组)
 * 题目链接：https://leetcode.com/problems/create-sorted-array-through-instructions/
 * 
 * 题目描述：
 * 给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
 * 一开始数组为空。你需要依次读取 instructions 中的元素，并将它插入到有序数组中的正确位置。
 * 每次插入操作的代价是以下两者的较小值：
 * 1. 有多少个元素严格小于 instructions[i]（左边）
 * 2. 有多少个元素严格大于 instructions[i]（右边）
 * 返回插入所有元素的总最小代价。由于答案可能很大，请返回它对 10^9 + 7 取模的结果。
 * 
 * 示例：
 * 输入：instructions = [1,5,6,2]
 * 输出：1
 * 解释：插入 1 时，数组为空，代价为 0。
 * 插入 5 时，左边有 1 个元素比 5 小，右边没有元素，代价为 min(1, 0) = 0。
 * 插入 6 时，左边有 2 个元素比 6 小，右边没有元素，代价为 min(2, 0) = 0。
 * 插入 2 时，左边有 1 个元素比 2 小，右边有 2 个元素比 2 大，代价为 min(1, 2) = 1。
 * 总代价为 0 + 0 + 0 + 1 = 1
 * 
 * 解题思路：
 * 这道题可以使用离散化线段树来解决。我们需要高效地统计数组中有多少元素小于当前元素，以及有多少元素大于当前元素。
 * 具体步骤：
 * 1. 离散化处理：将指令数组中的所有元素进行排序去重，得到每个元素的排名（离散化值）
 * 2. 构建线段树：维护每个值出现的次数
 * 3. 对于每个指令：
 *    a. 查询当前小于该元素的个数（即离散化后值减1的前缀和）
 *    b. 查询当前大于该元素的个数（即总元素数减去离散化后值的前缀和）
 *    c. 计算当前代价并累加到结果中
 *    d. 更新线段树，将该元素的计数加1
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)，其中n是指令数组的长度
 * - 线段树构建：O(m)，其中m是离散化后的不同元素个数
 * - 每个查询和更新操作：O(log m)
 * - 总时间复杂度：O(n log n + n log m) = O(n log n)，因为m ≤ n
 * 
 * 空间复杂度分析：
 * - 线段树空间：O(m)
 * - 离散化数组：O(m)
 * - 总空间复杂度：O(m) = O(n)
 * 
 * 本题最优解：线段树是本题的最优解之一，另外也可以使用树状数组（Fenwick Tree）实现，时间复杂度相同。
 */

// 由于编译环境限制，使用简单的数组实现

const int MAXN = 100005;
const int MOD = 1000000007;
int tree[2 * MAXN];  // 线段树数组
int n;               // 原始数据长度

/**
 * 初始化线段树
 * @param size 离散化后的值域大小
 */
void initSegmentTree(int size) {
    n = 1;
    // 计算大于等于size的最小2的幂次
    while (n < size) {
        n <<= 1;
    }
    // 初始化线段树数组
    for (int i = 0; i < 2 * n; i++) {
        tree[i] = 0;
    }
}

/**
 * 更新线段树中的某个位置的值
 * @param idx 离散化后的值对应的索引
 * @param delta 要增加的值（这里是1）
 */
void update(int idx, int delta) {
    idx += n; // 转换为线段树叶子节点索引
    tree[idx] += delta;
    // 向上更新父节点
    for (int i = idx >> 1; i >= 1; i >>= 1) {
        tree[i] = tree[2 * i] + tree[2 * i + 1];
    }
}

/**
 * 查询区间[0, idx]的和
 * @param idx 离散化后的值对应的索引
 * @return 区间和
 */
int query(int idx) {
    if (idx < 0) return 0;
    idx = (idx < n - 1) ? idx : n - 1; // 防止越界
    int res = 0;
    int l = n;       // 左边界（线段树叶子节点索引）
    int r = n + idx; // 右边界（线段树叶子节点索引）
    
    // 区间查询
    while (l <= r) {
        // 如果l是右孩子
        if ((l & 1) == 1) {
            res += tree[l];
            l++;
        }
        // 如果r是左孩子
        if ((r & 1) == 0) {
            res += tree[r];
            r--;
        }
        l >>= 1;
        r >>= 1;
    }
    return res;
}

/**
 * 简单排序函数（冒泡排序）
 * @param arr 待排序数组
 * @param size 数组大小
 */
void bubbleSort(int arr[], int size) {
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1 - i; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

/**
 * 去重函数
 * @param arr 已排序数组
 * @param size 数组大小
 * @return 去重后的元素个数
 */
int removeDuplicates(int arr[], int size) {
    if (size == 0) return 0;
    int uniqueSize = 1;
    for (int i = 1; i < size; i++) {
        if (arr[i] != arr[i - 1]) {
            arr[uniqueSize++] = arr[i];
        }
    }
    return uniqueSize;
}

/**
 * 二分查找函数
 * @param arr 已排序数组
 * @param size 数组大小
 * @param target 目标值
 * @return 目标值在数组中的索引，如果不存在则返回应该插入的位置
 */
int binarySearch(int arr[], int size, int target) {
    int left = 0, right = size - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return left;
}

/**
 * 计算两个数中的较小值
 * @param a 第一个数
 * @param b 第二个数
 * @return 较小的数
 */
int min(int a, int b) {
    return (a < b) ? a : b;
}

/**
 * 计算创建有序数组的最小代价
 * @param instructions 指令数组
 * @param size 指令数组大小
 * @return 总最小代价
 */
int createSortedArray(int instructions[], int size) {
    // 离散化处理
    int sortedVals[MAXN];
    for (int i = 0; i < size; i++) {
        sortedVals[i] = instructions[i];
    }
    
    // 排序
    bubbleSort(sortedVals, size);
    
    // 去重
    int uniqueSize = removeDuplicates(sortedVals, size);
    
    // 构建线段树
    initSegmentTree(uniqueSize);
    long long totalCost = 0; // 使用long long避免溢出
    
    // 处理每个指令
    for (int i = 0; i < size; i++) {
        int value = instructions[i];
        int idx = binarySearch(sortedVals, uniqueSize, value);
        
        // 计算左边比当前元素小的个数（即前缀和）
        int smallerCount = query(idx - 1);
        
        // 计算右边比当前元素大的个数（总元素数减去到当前索引的前缀和）
        int largerCount = i - query(idx);
        
        // 取较小值作为当前操作的代价
        totalCost = (totalCost + min(smallerCount, largerCount)) % MOD;
        
        // 更新线段树，将当前元素的计数加1
        update(idx, 1);
    }
    
    return (int)totalCost;
}

// 由于编译环境限制，不提供main函数测试
// 可以通过调用createSortedArray函数来使用
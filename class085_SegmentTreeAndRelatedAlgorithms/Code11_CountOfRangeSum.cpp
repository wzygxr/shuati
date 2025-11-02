// 327. 区间和的个数 - 线段树实现
// 题目来源：LeetCode 327 https://leetcode.cn/problems/count-of-range-sum/
// 
// 题目描述：
// 给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的区间和的个数。
// 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
// 
// 解题思路：
// 使用线段树配合前缀和与离散化来解决区间和个数问题
// 1. 计算前缀和数组，将区间和问题转换为前缀和差值问题
// 2. 对于前缀和prefixSum[i]，需要找到满足lower <= prefixSum[j] - prefixSum[i] <= upper的j个数
// 3. 转换为prefixSum[i] + lower <= prefixSum[j] <= prefixSum[i] + upper
// 4. 从右向左遍历前缀和数组，使用线段树维护已处理的前缀和信息
// 5. 对于当前前缀和prefixSum[i]，在线段树中查询满足条件的前缀和个数
// 6. 将当前前缀和加入线段树，供后续元素查询使用
// 
// 时间复杂度分析：
// - 计算前缀和：O(n)
// - 离散化：O(n log n)
// - 构建线段树：O(n)
// - 处理每个前缀和：O(log n)
// - 总时间复杂度：O(n log n)
// 空间复杂度：O(n)

// 由于编译环境限制，使用基础C++实现，避免复杂STL容器

const int MAXN = 10000;

// 线段树数组
int tree[MAXN * 4];
int n;  // 离散化后的值域大小

/**
 * 更新线段树中的某个位置的值
 * @param node 当前节点索引
 * @param start 当前节点维护的区间左边界
 * @param end 当前节点维护的区间右边界
 * @param index 要更新的位置索引
 * @param val 要增加的值
 * 
 * 时间复杂度: O(log n)
 */
void updateHelper(int node, int start, int end, int index, int val) {
    // 到达叶子节点，直接更新
    if (start == end) {
        tree[node] += val;
    } else {
        int mid = start + (end - start) / 2;
        // 根据位置决定更新左子树还是右子树
        if (index <= mid) {
            // 在左子树中更新
            updateHelper(2 * node, start, mid, index, val);
        } else {
            // 在右子树中更新
            updateHelper(2 * node + 1, mid + 1, end, index, val);
        }
        // 更新当前节点的值（合并子节点）
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }
}

/**
 * 更新线段树中的某个位置的值
 * @param index 要更新的位置索引
 * @param val 要增加的值
 * 
 * 时间复杂度: O(log n)
 */
void update(int index, int val) {
    updateHelper(1, 0, n - 1, index, val);
}

/**
 * 查询辅助函数
 * @param node 当前节点索引
 * @param start 当前节点维护的区间左边界
 * @param end 当前节点维护的区间右边界
 * @param left 查询区间左边界
 * @param right 查询区间右边界
 * @return 区间[left, right]内元素的和
 */
int queryHelper(int node, int start, int end, int left, int right) {
    // 查询区间与当前节点维护区间无交集，返回0
    if (right < start || end < left) {
        return 0;
    }
    // 当前节点维护区间完全包含在查询区间内，返回节点值
    if (left <= start && end <= right) {
        return tree[node];
    }
    // 部分重叠，递归查询左右子树
    int mid = start + (end - start) / 2;
    return queryHelper(2 * node, start, mid, left, right) + 
           queryHelper(2 * node + 1, mid + 1, end, left, right);
}

/**
 * 查询区间和
 * @param left 查询区间左边界
 * @param right 查询区间右边界
 * @return 区间[left, right]内元素的和
 * 
 * 时间复杂度: O(log n)
 */
int query(int left, int right) {
    // 处理边界情况
    if (left < 0) left = 0;
    if (right >= n) right = n - 1;
    if (left > right) return 0;
    
    return queryHelper(1, 0, n - 1, left, right);
}

// 简单排序函数（冒泡排序）
void bubbleSort(long long* arr, int size) {
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1 - i; j++) {
            if (arr[j] > arr[j + 1]) {
                long long temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 二分查找函数
int binarySearch(long long* arr, int size, long long target) {
    int left = 0, right = size - 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}

/**
 * 主函数：计算区间和的个数
 * @param nums 输入数组
 * @param numsSize 输入数组大小
 * @param lower 区间下界
 * @param upper 区间上界
 * @return 区间和在[lower, upper]范围内的个数
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 */
int countRangeSum(int* nums, int numsSize, int lower, int upper) {
    // 处理边界情况
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // 计算前缀和数组，prefixSum[0] = 0, prefixSum[i] = nums[0] + ... + nums[i-1]
    long long prefixSum[MAXN + 1];
    prefixSum[0] = 0;
    for (int i = 0; i < numsSize; ++i) {
        prefixSum[i + 1] = prefixSum[i] + nums[i];
    }
    
    // 离散化处理，收集所有可能需要的值
    long long uniqueValues[MAXN * 3];
    int uniqueCount = 0;
    
    // 收集前缀和
    for (int i = 0; i <= numsSize; ++i) {
        uniqueValues[uniqueCount++] = prefixSum[i];
    }
    
    // 收集用于查询下界的值
    for (int i = 0; i <= numsSize; ++i) {
        uniqueValues[uniqueCount++] = prefixSum[i] - lower;
    }
    
    // 收集用于查询上界的值
    for (int i = 0; i <= numsSize; ++i) {
        uniqueValues[uniqueCount++] = prefixSum[i] - upper;
    }
    
    // 去重
    bubbleSort(uniqueValues, uniqueCount);
    int actualCount = 1;
    for (int i = 1; i < uniqueCount; ++i) {
        if (uniqueValues[i] != uniqueValues[i - 1]) {
            uniqueValues[actualCount++] = uniqueValues[i];
        }
    }
    
    // 创建值到索引的映射（使用线性查找，简化实现）
    n = actualCount;
    
    // 初始化线段树
    for (int i = 0; i < MAXN * 4; ++i) {
        tree[i] = 0;
    }
    
    int count = 0;
    
    // 从右到左遍历前缀和，使用线段树查询符合条件的区间和
    for (int i = numsSize; i >= 0; --i) {
        long long current = prefixSum[i];
        // 查询满足lower <= prefixSum[j] - prefixSum[i] <= upper的j的数量
        // 即查询prefixSum[j]在[current + lower, current + upper]范围内的数量
        // 转换为查询离散化后的索引范围
        int left = binarySearch(uniqueValues, actualCount, current + lower);
        int right = binarySearch(uniqueValues, actualCount, current + upper);
        count += query(left, right);
        
        // 将当前前缀和添加到线段树中，供后续元素查询使用
        int currentIndex = binarySearch(uniqueValues, actualCount, current);
        update(currentIndex, 1);
    }
    
    return count;
}
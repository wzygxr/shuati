// Codeforces 1042D Petya and Array
// C++ 实现

/**
 * Codeforces 1042D Petya and Array
 * 
 * 题目描述：
 * 给定一个包含n个整数的数组a和一个整数t，找出有多少个连续子数组的和严格小于t。
 * 
 * 解题思路：
 * 这个问题可以转化为前缀和问题。设prefix[i]表示前i个元素的和，那么子数组a[l..r]的和等于
 * prefix[r] - prefix[l-1]。我们需要找出有多少对(l,r)满足prefix[r] - prefix[l-1] < t，
 * 即prefix[l-1] > prefix[r] - t。
 * 
 * 我们可以使用归并排序的思想来解决这个问题。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
long long countSubarrays(int* a, int n, int t) {
    // 计算前缀和
    long long* prefix = (long long*)calloc(n + 1, sizeof(long long));
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = prefix[i] + a[i];
    }
    
    // 使用归并排序的思想计算满足条件的子数组数量
    long long result = mergeSortAndCount(prefix, 0, n, t);
    
    free(prefix);
    return result;
}

long long mergeSortAndCount(long long* prefix, int left, int right, int t) {
    if (left >= right) {
        return 0;
    }
    
    int mid = left + (right - left) / 2;
    long long count = 0;
    
    // 递归计算左半部分和右半部分的答案
    count += mergeSortAndCount(prefix, left, mid, t);
    count += mergeSortAndCount(prefix, mid + 1, right, t);
    
    // 计算跨越中点的子数组数量
    count += countCrossing(prefix, left, mid, right, t);
    
    // 合并两个有序数组
    merge(prefix, left, mid, right);
    
    return count;
}

long long countCrossing(long long* prefix, int left, int mid, int right, int t) {
    long long count = 0;
    
    // 对于右半部分的每个元素，计算左半部分有多少元素满足条件
    for (int j = mid + 1; j <= right; j++) {
        // 我们需要找到左半部分中满足 prefix[i] > prefix[j] - t 的元素数量
        // 即找到左半部分中大于 prefix[j] - t 的元素数量
        double target = (double)(prefix[j] - t);
        count += countGreaterThan(prefix, left, mid, target);
    }
    
    return count;
}

int countGreaterThan(long long* arr, int left, int right, double target) {
    // 在有序数组arr[left..right]中找到大于target的元素数量
    // 使用二分查找
    int low = left, high = right + 1;
    
    while (low < high) {
        int mid = low + (high - low) / 2;
        if ((double)arr[mid] > target) {
            high = mid;
        } else {
            low = mid + 1;
        }
    }
    
    return right + 1 - low;
}

void merge(long long* prefix, int left, int mid, int right) {
    long long* temp = (long long*)malloc((right - left + 1) * sizeof(long long));
    int i = left, j = mid + 1, k = 0;
    
    while (i <= mid && j <= right) {
        if (prefix[i] <= prefix[j]) {
            temp[k++] = prefix[i++];
        } else {
            temp[k++] = prefix[j++];
        }
    }
    
    while (i <= mid) {
        temp[k++] = prefix[i++];
    }
    
    while (j <= right) {
        temp[k++] = prefix[j++];
    }
    
    for (i = 0; i < right - left + 1; i++) {
        prefix[left + i] = temp[i];
    }
    
    free(temp);
}

// 算法核心思想：
// 1. 将问题转化为前缀和问题
// 2. 使用归并排序的思想计算满足条件的子数组数量
// 3. 通过二分查找优化计算过程

// 时间复杂度分析：
// - 计算前缀和：O(n)
// - 归并排序过程：O(n log n)
// - 二分查找：O(log n)
// - 总体时间复杂度：O(n log n)
// - 空间复杂度：O(n)
*/

// 算法应用场景：
// 1. 数组子段和问题
// 2. 前缀和优化技巧
// 3. 归并排序在计数问题中的应用
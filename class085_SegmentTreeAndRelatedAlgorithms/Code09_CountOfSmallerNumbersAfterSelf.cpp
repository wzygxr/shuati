// 315. 计算右侧小于当前元素的个数 - 线段树实现
// 题目来源：LeetCode 315 https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
// 
// 题目描述：
// 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
// 
// 解题思路：
// 使用线段树配合离散化来解决这个问题
// 1. 先对数组进行离散化处理，将值域映射到连续的小范围
// 2. 从右向左遍历数组，这样可以确保每次处理的元素右侧元素都已经处理过
// 3. 使用线段树维护离散化后的值域信息，记录每个值出现的次数
// 4. 对于当前元素，查询值域中小于它的元素个数，即为右侧小于当前元素的个数
// 5. 将当前元素加入线段树，供后续元素查询使用
// 
// 时间复杂度分析：
// - 离散化：O(n^2) （使用了简单的冒泡排序）
// - 构建线段树：O(n)
// - 处理每个元素：O(log n)
// - 总时间复杂度：O(n^2)
// 空间复杂度：O(n)

const int MAXN = 100000;

// 线段树数组
int tree[MAXN * 4];
int n;

/**
 * 更新线段树
 * @param node 当前节点索引
 * @param start 当前节点维护的区间左边界
 * @param end 当前节点维护的区间右边界
 * @param idx 要更新的位置
 * @param val 要增加的值
 * 
 * 时间复杂度: O(log n)
 */
void update(int node, int start, int end, int idx, int val) {
    // 到达叶子节点，更新值
    if (start == end) {
        tree[node] += val;
    } else {
        int mid = (start + end) / 2;
        // 根据位置决定更新左子树还是右子树
        if (idx <= mid) {
            update(2 * node, start, mid, idx, val);
        } else {
            update(2 * node + 1, mid + 1, end, idx, val);
        }
        // 更新当前节点的值为左右子节点值之和
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }
}

/**
 * 查询区间和
 * @param node 当前节点索引
 * @param start 当前节点维护的区间左边界
 * @param end 当前节点维护的区间右边界
 * @param l 查询区间左边界
 * @param r 查询区间右边界
 * @return 区间[l,r]内元素的和
 * 
 * 时间复杂度: O(log n)
 */
int query(int node, int start, int end, int l, int r) {
    // 查询区间与当前节点维护区间无交集，返回0
    if (l > end || r < start) {
        return 0;
    }
    // 当前节点维护区间完全包含在查询区间内，返回节点值
    if (l <= start && end <= r) {
        return tree[node];
    }
    // 部分重叠，递归查询左右子树
    int mid = (start + end) / 2;
    return query(2 * node, start, mid, l, r) + 
           query(2 * node + 1, mid + 1, end, l, r);
}

/**
 * 离散化处理
 * @param nums 原始数组
 * @param sorted 用于存储排序后去重数组的数组
 * @param size 原始数组大小
 * @return 去重后数组的大小
 */
int discretize(int* nums, int* sorted, int size) {
    // 复制原始数组
    for (int i = 0; i < size; i++) {
        sorted[i] = nums[i];
    }
    
    // 简单排序（冒泡排序）
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1 - i; j++) {
            if (sorted[j] > sorted[j + 1]) {
                int temp = sorted[j];
                sorted[j] = sorted[j + 1];
                sorted[j + 1] = temp;
            }
        }
    }
    
    // 去重
    int unique_size = 1;
    for (int i = 1; i < size; i++) {
        if (sorted[i] != sorted[i - 1]) {
            sorted[unique_size++] = sorted[i];
        }
    }
    
    return unique_size;
}

/**
 * 二分查找
 * @param arr 已排序的数组
 * @param size 数组大小
 * @param target 要查找的目标值
 * @return 目标值在数组中的索引，未找到返回-1
 */
int binarySearch(int* arr, int size, int target) {
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
 * 主函数：计算右侧小于当前元素的个数
 * @param nums 输入数组
 * @param size 输入数组大小
 * @param result 结果数组
 * 
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n)
 */
void countSmaller(int* nums, int size, int* result) {
    // 处理边界情况
    if (nums == 0 || size == 0) {
        return;
    }
    
    // 离散化处理
    int sorted[MAXN];
    int unique_size = discretize(nums, sorted, size);
    
    // 初始化线段树
    for (int i = 0; i < MAXN * 4; i++) {
        tree[i] = 0;
    }
    n = unique_size;
    
    // 从右向左遍历数组
    for (int i = size - 1; i >= 0; i--) {
        // 查找当前元素在离散化数组中的位置
        int pos = binarySearch(sorted, unique_size, nums[i]);
        // 查询小于当前元素的个数（即右侧小于当前元素的个数）
        result[i] = query(1, 0, unique_size - 1, 0, pos - 1);
        // 将当前元素加入线段树，供后续元素查询使用
        update(1, 0, unique_size - 1, pos, 1);
    }
}
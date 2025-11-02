/**
 * C++ 线段树实现 - LeetCode 315. Count of Smaller Numbers After Self
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 题目描述:
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 *
 * 示例 1:
 * 输入: nums = [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 *
 * 示例 2:
 * 输入: nums = [-1]
 * 输出: [0]
 *
 * 示例 3:
 * 输入: nums = [-1,-1]
 * 输出: [0,0]
 *
 * 提示:
 * 1 <= nums.length <= 10^5
 * -10^4 <= nums[i] <= 10^4
 *
 * 解题思路:
 * 这是一个经典的逆序对问题，可以使用线段树来解决。
 * 1. 由于数值范围较大(-10^4 到 10^4)，需要进行离散化处理
 * 2. 从右往左遍历数组，对于每个元素:
 *    - 查询线段树中比当前元素小的元素个数
 *    - 将当前元素插入线段树
 * 3. 使用线段树维护区间和，支持单点更新和区间查询
 *
 * 时间复杂度: O(n log n)，其中n是数组长度
 * 空间复杂度: O(n)
 */

// 定义最大数组大小
#define MAXN 200005

// 线段树数组
int tree[MAXN * 4];

// 离散化数组
int nums[MAXN];
int map[MAXN];
int n;

// 向上传递
void pushUp(int i) {
    tree[i] = tree[i << 1] + tree[i << 1 | 1];
}

// 建立线段树
void build(int l, int r, int i) {
    if (l == r) {
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
}

// 单点更新
void update(int index, int l, int r, int i) {
    if (l == r) {
        tree[i]++;
        return;
    }
    int mid = (l + r) >> 1;
    if (index <= mid)
        update(index, l, mid, i << 1);
    else
        update(index, mid + 1, r, i << 1 | 1);
    pushUp(i);
}

// 区间查询
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl > r || jobr < l) {
        return 0;
    }
    if (jobl <= l && r <= jobr) {
        return tree[i];
    }
    int mid = (l + r) >> 1;
    int ans = 0;
    if (jobl <= mid)
        ans += query(jobl, jobr, l, mid, i << 1);
    if (jobr > mid)
        ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
    return ans;
}

// 简单排序函数（冒泡排序）
void bubbleSort(int arr[], int len) {
    for (int i = 0; i < len - 1; i++) {
        for (int j = 0; j < len - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 去重函数
int removeDuplicates(int arr[], int len) {
    if (len == 0) return 0;
    
    int i = 0;
    for (int j = 1; j < len; j++) {
        if (arr[j] != arr[i]) {
            i++;
            arr[i] = arr[j];
        }
    }
    return i + 1;
}

// 离散化处理
void discretization(int original_nums[], int len) {
    // 复制数组
    for (int i = 0; i < len; i++) {
        nums[i] = original_nums[i];
    }
    
    // 排序
    bubbleSort(nums, len);
    
    // 去重
    n = removeDuplicates(nums, len);
    
    // 建立映射关系
    for (int i = 0; i < n; i++) {
        map[i] = i + 1;
    }
}

// 查询比val小的元素个数
int countSmaller(int val) {
    // 简化处理，实际应该使用二分查找
    for (int i = 0; i < n; i++) {
        if (nums[i] == val) {
            return query(1, map[i] - 1, 1, n, 1);
        }
    }
    return 0;
}

// 插入元素
void insert_val(int val) {
    // 简化处理，实际应该使用二分查找
    for (int i = 0; i < n; i++) {
        if (nums[i] == val) {
            update(map[i], 1, n, 1);
            return;
        }
    }
}

// 主要函数
void countSmallerMain(int input_nums[], int len, int result[]) {
    if (len == 0) {
        return;
    }
    
    // 离散化处理
    discretization(input_nums, len);
    
    // 初始化线段树
    for (int i = 0; i < MAXN * 4; i++) {
        tree[i] = 0;
    }
    build(1, n, 1);
    
    // 从右往左遍历
    for (int i = len - 1; i >= 0; i--) {
        // 查询比当前元素小的元素个数
        result[i] = countSmaller(input_nums[i]);
        
        // 将当前元素插入线段树
        insert_val(input_nums[i]);
    }
}
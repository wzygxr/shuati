// Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
// 题目来源: LeetCode 315. Count of Smaller Numbers After Self
// 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self
// 题目链接: https://leetcode.com/problems/count-of-smaller-numbers-after-self
// 
// 题目描述:
// 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
// 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
// 示例 1：
// 输入：nums = [5,2,6,1]
// 输出：[2,1,1,0]
// 解释：
// 5 的右侧有 2 个更小的元素 (2 和 1)
// 2 的右侧有 1 个更小的元素 (1)
// 6 的右侧有 1 个更小的元素 (1)
// 1 的右侧有 0 个更小的元素
// 示例 2：
// 输入：nums = [-1]
// 输出：[0]
// 示例 3：
// 输入：nums = [-1,-1]
// 输出：[0,0]
// 提示：
// 1 <= nums.length <= 10^5
// -10^4 <= nums[i] <= 10^4
//
// 解题思路:
// 1. 使用离散化+线段树的方法解决
// 2. 从右向左遍历数组，维护一个值域线段树
// 3. 对于每个元素，查询值域中小于它的元素个数
// 4. 将当前元素插入到线段树中
// 5. 利用离散化处理大范围的值域
//
// 时间复杂度: O(n log n)，其中n为数组长度
// 空间复杂度: O(n)

// 由于编译环境限制，使用简单的数组实现

const int MAXN = 100005;
int nums[MAXN];
int sorted[MAXN];
int tree[4 * MAXN];
int result[MAXN];
int n;

// 更新节点值（单点更新）
void update(int node, int start, int end, int index, int val) {
    // 找到叶子节点，更新值
    if (start == end) {
        tree[node] += val;
        return;
    }

    // 在左右子树中查找需要更新的索引
    int mid = (start + end) / 2;
    if (index <= mid) {
        // 在左子树中
        update(2 * node + 1, start, mid, index, val);
    } else {
        // 在右子树中
        update(2 * node + 2, mid + 1, end, index, val);
    }

    // 更新当前节点的值为左右子节点值的和
    tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
}

// 查询区间和
int query(int node, int start, int end, int left, int right) {
    // 当前区间与查询区间无交集
    if (right < start || left > end) {
        return 0;
    }

    // 当前区间完全包含在查询区间内
    if (left <= start && end <= right) {
        return tree[node];
    }

    // 当前区间与查询区间有部分交集，递归查询左右子树
    int mid = (start + end) / 2;
    int leftSum = query(2 * node + 1, start, mid, left, right);
    int rightSum = query(2 * node + 2, mid + 1, end, left, right);
    return leftSum + rightSum;
}

// 二分查找元素在排序数组中的位置
int binarySearch(int left, int right, int target) {
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (sorted[mid] == target) {
            return mid;
        } else if (sorted[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return left;
}

// 主函数实现
void countSmaller(int nums_arr[], int size) {
    n = size;
    
    // 复制数组
    for (int i = 0; i < size; i++) {
        nums[i] = nums_arr[i];
        sorted[i] = nums_arr[i];
    }
    
    // 排序用于离散化
    // 简单冒泡排序（因为不能使用<algorithm>）
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
    int uniqueSize = 1;
    for (int i = 1; i < size; i++) {
        if (sorted[i] != sorted[i - 1]) {
            sorted[uniqueSize++] = sorted[i];
        }
    }
    
    // 初始化线段树
    for (int i = 0; i < 4 * uniqueSize; i++) {
        tree[i] = 0;
    }
    
    // 从右向左遍历数组
    for (int i = n - 1; i >= 0; i--) {
        // 找到当前元素在离散化数组中的位置
        int pos = binarySearch(0, uniqueSize - 1, nums[i]);
        
        // 查询比当前元素小的元素个数（在值域上查询[0, pos-1]区间和）
        result[i] = query(0, 0, uniqueSize - 1, 0, pos - 1);
        
        // 更新当前元素的计数（在值域上对位置pos进行+1操作）
        update(0, 0, uniqueSize - 1, pos, 1);
    }
}

// ==========================================================================================
// LeetCode 1649. Create Sorted Array through Instructions
// 题目链接：https://leetcode.com/problems/create-sorted-array-through-instructions/
// 题目描述：
// 给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
// 一开始数组为空。你需要依次读取 instructions 中的元素，并将它插入到有序数组中的正确位置。
// 每次插入操作的代价是以下两者的较小值：
// 1. 有多少个元素严格小于 instructions[i]（左边）
// 2. 有多少个元素严格大于 instructions[i]（右边）
// 返回插入所有元素的总最小代价。由于答案可能很大，请返回它对 10^9 + 7 取模的结果。
// ==========================================================================================

const int MOD = 1e9 + 7;

// LeetCode 1649题的实现函数
long long createSortedArray(int instructions[], int size) {
    // 离散化处理
    int* sorted_inst = new int[size];
    for (int i = 0; i < size; i++) {
        sorted_inst[i] = instructions[i];
    }
    
    // 排序用于离散化
    // 简单冒泡排序
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1 - i; j++) {
            if (sorted_inst[j] > sorted_inst[j + 1]) {
                int temp = sorted_inst[j];
                sorted_inst[j] = sorted_inst[j + 1];
                sorted_inst[j + 1] = temp;
            }
        }
    }
    
    // 去重
    int uniqueSize = 1;
    for (int i = 1; i < size; i++) {
        if (sorted_inst[i] != sorted_inst[i - 1]) {
            sorted_inst[uniqueSize++] = sorted_inst[i];
        }
    }
    
    // 初始化线段树
    for (int i = 0; i < 4 * uniqueSize; i++) {
        tree[i] = 0;
    }
    
    long long totalCost = 0;
    
    // 处理每个指令
    for (int i = 0; i < size; i++) {
        int value = instructions[i];
        
        // 找到离散化后的位置
        int pos = binarySearch(0, uniqueSize - 1, value);
        
        // 计算左边比当前元素小的个数
        int smallerCount = query(0, 0, uniqueSize - 1, 0, pos - 1);
        
        // 计算右边比当前元素大的个数（总元素数减去到当前索引的前缀和）
        int largerCount = i - query(0, 0, uniqueSize - 1, 0, pos);
        
        // 取较小值作为当前操作的代价
        totalCost = (totalCost + (smallerCount < largerCount ? smallerCount : largerCount)) % MOD;
        
        // 更新线段树，将当前元素的计数加1
        update(0, 0, uniqueSize - 1, pos, 1);
    }
    
    delete[] sorted_inst;
    return totalCost;
}

// 测试LeetCode 1649题的函数
void testLeetCode1649() {
    // 测试用例1
    int instructions1[] = {1, 5, 6, 2};
    long long result1 = createSortedArray(instructions1, 4);
    // printf("LeetCode 1649 测试用例1结果: %lld\n", result1); // 预期输出: 1
    
    // 测试用例2
    int instructions2[] = {1, 2, 3, 6, 5, 4};
    long long result2 = createSortedArray(instructions2, 6);
    // printf("LeetCode 1649 测试用例2结果: %lld\n", result2); // 预期输出: 3
    
    // 测试用例3
    int instructions3[] = {1, 3, 3, 3, 2, 4, 2, 1, 2};
    long long result3 = createSortedArray(instructions3, 9);
    // printf("LeetCode 1649 测试用例3结果: %lld\n", result3); // 预期输出: 4
}

// 由于编译环境限制，不提供main函数测试
// 可以通过调用countSmaller函数并检查result数组来验证结果
// 也可以调用testLeetCode1649函数来测试LeetCode 1649题的实现
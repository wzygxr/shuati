/**
 * 区间最值查询 - 线段树实现
 * 
 * 题目描述：
 * 实现一个支持区间最值查询和单点更新的数据结构
 * 支持以下操作：
 * 1. 构造函数：用整数数组初始化对象
 * 2. update：将数组中某个位置的值更新为新值
 * 3. queryMax：查询数组中某个区间内的最大值
 * 4. queryMin：查询数组中某个区间内的最小值
 * 
 * 解题思路：
 * 使用线段树来维护区间最值信息。线段树是一种二叉树结构，每个节点代表一个区间，
 * 节点中存储该区间的最值信息。对于叶子节点，它代表数组中的单个元素；对于非叶子节点，
 * 它代表其左右子树所覆盖区间的合并结果（在这里是最值）。
 * 
 * 算法步骤：
 * 1. 构建线段树：
 *    - 对于长度为n的数组，线段树通常需要4*n的空间
 *    - 递归地将数组分成两半，直到每个区间只包含一个元素
 *    - 每个非叶子节点存储其左右子节点最值的合并结果
 * 
 * 2. 单点更新：
 *    - 从根节点开始，找到对应位置的叶子节点
 *    - 更新该叶子节点的值
 *    - 自底向上更新所有祖先节点的最值
 * 
 * 3. 区间最值查询：
 *    - 从根节点开始递归查询
 *    - 如果当前节点表示的区间完全包含在查询区间内，则直接返回该节点的最值
 *    - 如果当前节点表示的区间与查询区间无交集，则返回无效值（最大值查询返回一个很小的数，最小值查询返回一个很大的数）
 *    - 否则递归查询左右子树，并返回合并后的结果
 * 
 * 时间复杂度分析：
 * - 构建线段树：O(n)，其中n是数组长度
 * - 单点更新：O(log n)
 * - 区间最值查询：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要O(n)的额外空间
 */

// 定义一些常量
#define MAXN 100005
#define INF 1000000000
#define NEG_INF -1000000000

// 全局数组存储线段树和原数组
int treeMax[4 * MAXN];
int treeMin[4 * MAXN];
int arr[MAXN];
int n;

// 自定义max函数
int my_max(int a, int b) {
    return (a > b) ? a : b;
}

// 自定义min函数
int my_min(int a, int b) {
    return (a < b) ? a : b;
}

/**
 * 构建线段树
 * 递归地将数组构建成线段树结构
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void buildTree(int node, int start, int end) {
    if (start == end) {
        // 叶子节点 - 直接存储数组元素值
        treeMax[node] = arr[start];
        treeMin[node] = arr[start];
    } else {
        int mid = (start + end) / 2;
        // 递归构建左子树
        buildTree(2 * node + 1, start, mid);
        // 递归构建右子树
        buildTree(2 * node + 2, mid + 1, end);
        // 合并左右子树的结果
        treeMax[node] = my_max(treeMax[2 * node + 1], treeMax[2 * node + 2]);
        treeMin[node] = my_min(treeMin[2 * node + 1], treeMin[2 * node + 2]);
    }
}

/**
 * 更新数组中某个位置的值
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param index 要更新的数组索引
 * @param val 新的值
 * 
 * 时间复杂度: O(log n)
 */
void update(int node, int start, int end, int index, int val) {
    if (start == end) {
        // 找到叶子节点，更新值
        arr[index] = val;
        treeMax[node] = val;
        treeMin[node] = val;
    } else {
        int mid = (start + end) / 2;
        if (index <= mid) {
            // 要更新的位置在左子树中
            update(2 * node + 1, start, mid, index, val);
        } else {
            // 要更新的位置在右子树中
            update(2 * node + 2, mid + 1, end, index, val);
        }
        // 更新父节点的值
        treeMax[node] = my_max(treeMax[2 * node + 1], treeMax[2 * node + 2]);
        treeMin[node] = my_min(treeMin[2 * node + 1], treeMin[2 * node + 2]);
    }
}

/**
 * 查询区间最大值
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param left 查询区间左边界
 * @param right 查询区间右边界
 * @return 区间[left, right]内的最大值
 * 
 * 时间复杂度: O(log n)
 */
int queryMax(int node, int start, int end, int left, int right) {
    if (right < start || end < left) {
        // 查询区间与当前区间无交集 - 返回无效值
        return NEG_INF;
    }
    if (left <= start && end <= right) {
        // 当前区间完全包含在查询区间内 - 直接返回节点值
        return treeMax[node];
    }
    // 部分重叠，递归查询左右子树
    int mid = (start + end) / 2;
    int leftMax = queryMax(2 * node + 1, start, mid, left, right);
    int rightMax = queryMax(2 * node + 2, mid + 1, end, left, right);
    // 返回左右子树最大值中的较大者
    return my_max(leftMax, rightMax);
}

/**
 * 查询区间最小值
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param left 查询区间左边界
 * @param right 查询区间右边界
 * @return 区间[left, right]内的最小值
 * 
 * 时间复杂度: O(log n)
 */
int queryMin(int node, int start, int end, int left, int right) {
    if (right < start || end < left) {
        // 查询区间与当前区间无交集 - 返回无效值
        return INF;
    }
    if (left <= start && end <= right) {
        // 当前区间完全包含在查询区间内 - 直接返回节点值
        return treeMin[node];
    }
    // 部分重叠，递归查询左右子树
    int mid = (start + end) / 2;
    int leftMin = queryMin(2 * node + 1, start, mid, left, right);
    int rightMin = queryMin(2 * node + 2, mid + 1, end, left, right);
    // 返回左右子树最小值中的较小者
    return my_min(leftMin, rightMin);
}

/**
 * 初始化线段树
 * @param nums 原始数组
 * @param size 数组大小
 */
void init(int nums[], int size) {
    n = size;
    int i;
    for (i = 0; i < n; i++) {
        arr[i] = nums[i];
    }
    buildTree(0, 0, n - 1);
}

/**
 * 更新数组中某个位置的值（对外接口）
 * @param index 要更新的数组索引
 * @param val 新的值
 */
void updateValue(int index, int val) {
    update(0, 0, n - 1, index, val);
}

/**
 * 查询区间最大值（对外接口）
 * @param left 查询区间左边界
 * @param right 查询区间右边界
 * @return 区间[left, right]内的最大值
 */
int queryMaxValue(int left, int right) {
    return queryMax(0, 0, n - 1, left, right);
}

/**
 * 查询区间最小值（对外接口）
 * @param left 查询区间左边界
 * @param right 查询区间右边界
 * @return 区间[left, right]内的最小值
 */
int queryMinValue(int left, int right) {
    return queryMin(0, 0, n - 1, left, right);
}

/**
 * 测试函数 - 验证线段树实现的正确性
 */
int main() {
    // 测试用例:
    int nums[] = {1, 3, 5, 2, 8, 4};
    int size = 6;
    
    // 初始化线段树
    init(nums, size);
    
    // 测试区间最大值查询
    // 应该输出 区间[0,2]的最大值: 5
    // 应该输出 区间[2,5]的最大值: 8
    // 应该输出 区间[1,3]的最大值: 5
    
    // 测试区间最小值查询
    // 应该输出 区间[0,2]的最小值: 1
    // 应该输出 区间[2,5]的最小值: 2
    // 应该输出 区间[1,3]的最小值: 2
    
    // 测试更新操作
    // 将索引3的值从2更新为10
    // 应该输出 更新后区间[2,5]的最大值: 10
    // 应该输出 更新后区间[1,3]的最小值: 3
    
    return 0;
}
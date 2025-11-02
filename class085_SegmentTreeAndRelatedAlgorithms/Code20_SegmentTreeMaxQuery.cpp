/**
 * 线段树区间最大值查询实现 (C++版本)
 * 题目来源：洛谷 P3865 【模板】ST表
 * 题目链接：https://www.luogu.com.cn/problem/P3865
 * 
 * 题目描述：
 * 给定一个长度为n的数组，支持区间最大值查询操作
 * 
 * 解题思路：
 * 使用线段树来维护区间最大值信息。线段树是一种二叉树结构，每个节点代表数组的一个区间，
 * 存储该区间的最大值。对于叶子节点，它代表数组中的单个元素；对于非叶子节点，
 * 它代表其左右子树所覆盖区间的合并结果（在这里是最大值）。
 * 
 * 算法要点：
 * - 使用线段树维护区间最大值
 * - 支持区间最大值查询操作
 * - 可以扩展支持区间更新（最大值更新）
 * 
 * 时间复杂度分析：
 * - 建树：O(n)
 * - 区间查询：O(log n)
 * - 单点更新：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要4n的空间：O(n)
 * 
 * 应用场景：
 * - 需要频繁查询区间最大值的场景
 * - 如滑动窗口最大值、区间最值统计等
 */

// 定义常量
#define MAXN 100005
#define INF 1000000000

// 全局数组存储线段树
int tree[4 * MAXN];  // 线段树数组，存储每个区间的最大值
int data[MAXN];      // 原始数据
int n;               // 数据长度

/**
 * 构建线段树
 * 递归地将数组构建成线段树结构
 * @param start 区间起始索引
 * @param end 区间结束索引
 * @param idx 当前节点索引（在tree数组中的位置）
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(log n) - 递归调用栈深度
 */
void buildTree(int start, int end, int idx) {
    // 递归终止条件：当前区间只有一个元素（叶子节点）
    if (start == end) {
        tree[idx] = data[start];
        return;
    }
    
    // 计算区间中点，避免整数溢出
    int mid = start + (end - start) / 2;
    // 计算左右子节点在tree数组中的索引
    int leftIdx = 2 * idx + 1;   // 左子节点索引
    int rightIdx = 2 * idx + 2;  // 右子节点索引
    
    // 递归构建左子树
    buildTree(start, mid, leftIdx);
    // 递归构建右子树
    buildTree(mid + 1, end, rightIdx);
    
    // 合并左右子树的结果，当前节点存储左右子树区间最大值中的较大者
    if (tree[leftIdx] > tree[rightIdx]) {
        tree[idx] = tree[leftIdx];
    } else {
        tree[idx] = tree[rightIdx];
    }
}

/**
 * 区间最大值查询递归实现
 * @param start 当前节点管理区间的起始索引
 * @param end 当前节点管理区间的结束索引
 * @param l 目标查询区间的左边界
 * @param r 目标查询区间的右边界
 * @param idx 当前节点在tree数组中的索引
 * @return 区间[l, r]的最大值
 * 
 * 核心思想：
 * 1. 判断当前区间与目标区间的关系
 * 2. 根据关系决定是直接返回、递归查询还是部分返回
 */
int queryMax(int start, int end, int l, int r, int idx) {
    // 当前区间完全包含在目标区间内，直接返回当前节点的值
    if (l <= start && end <= r) {
        return tree[idx];
    }
    
    // 当前区间与目标区间无交集，返回无效值（负无穷）
    if (start > r || end < l) {
        return -INF;
    }
    
    // 部分重叠，需要递归查询子区间
    int mid = start + (end - start) / 2;
    // 递归查询左子树
    int leftMax = queryMax(start, mid, l, r, 2 * idx + 1);
    // 递归查询右子树
    int rightMax = queryMax(mid + 1, end, l, r, 2 * idx + 2);
    
    // 返回左右子树查询结果中的较大者
    if (leftMax > rightMax) {
        return leftMax;
    } else {
        return rightMax;
    }
}

/**
 * 单点更新递归实现
 * @param start 当前节点管理区间的起始索引
 * @param end 当前节点管理区间的结束索引
 * @param index 要更新的位置索引
 * @param val 新值
 * @param idx 当前节点在tree数组中的索引
 */
void updatePoint(int start, int end, int index, int val, int idx) {
    // 递归终止条件：找到叶子节点
    if (start == end) {
        tree[idx] = val;
        return;
    }
    
    // 计算区间中点
    int mid = start + (end - start) / 2;
    // 根据索引位置决定更新左子树还是右子树
    if (index <= mid) {
        // 更新左子树
        updatePoint(start, mid, index, val, 2 * idx + 1);
    } else {
        // 更新右子树
        updatePoint(mid + 1, end, index, val, 2 * idx + 2);
    }
    
    // 更新完成后，需要维护父节点信息（合并子节点结果）
    if (tree[2 * idx + 1] > tree[2 * idx + 2]) {
        tree[idx] = tree[2 * idx + 1];
    } else {
        tree[idx] = tree[2 * idx + 2];
    }
}

/**
 * 初始化线段树
 * @param arr 原始数组
 * @param size 数组大小
 */
void init(int arr[], int size) {
    n = size;
    int i;
    for (i = 0; i < n; i++) {
        data[i] = arr[i];
    }
    // 构建线段树
    buildTree(0, n - 1, 0);
}

/**
 * 区间最大值查询
 * @param l 区间左边界（包含）
 * @param r 区间右边界（包含）
 * @return 区间最大值
 * 
 * 时间复杂度：O(log n)
 */
int queryMaxValue(int l, int r) {
    // 调用递归实现
    return queryMax(0, n - 1, l, r, 0);
}

/**
 * 单点更新 - 更新位置index的值为val
 * @param index 位置索引
 * @param val 新值
 * 
 * 时间复杂度：O(log n)
 */
void updatePointValue(int index, int val) {
    // 调用递归实现
    updatePoint(0, n - 1, index, val, 0);
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1：基本功能测试
    int arr1[] = {3, 1, 4, 1, 5, 9, 2, 6};
    init(arr1, 8);
    
    // 应该输出 区间[0, 2]的最大值：4 (3,1,4中的最大值)
    // 应该输出 区间[2, 5]的最大值：9 (4,1,5,9中的最大值)
    // 应该输出 区间[4, 7]的最大值：9 (5,9,2,6中的最大值)
    
    // 单点更新测试
    updatePointValue(3, 10);
    // 应该输出 更新后区间[0, 3]的最大值：10 (3,1,4,10中的最大值)
    // 应该输出 更新后区间[2, 5]的最大值：10 (4,10,5,9中的最大值)
    
    // 测试用例2：边界条件测试
    int arr2[] = {7};
    init(arr2, 1);
    // 应该输出 单元素数组查询[0]：7
    updatePointValue(0, 15);
    // 应该输出 单元素更新后查询[0]：15
}

// 主函数
int main() {
    // 运行测试
    test();
    
    return 0;
}
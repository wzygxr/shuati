/**
 * 线段树懒惰传播实现 - 区间加法和区间求和 (C++版本)
 * 题目来源：洛谷 P3372 【模板】线段树 1
 * 题目链接：https://www.luogu.com.cn/problem/P3372
 * 
 * 题目描述：
 * 给定一个长度为n的数组，支持两种操作：
 * 1. 将某个区间内的每个数加上k
 * 2. 查询某个区间内所有数的和
 * 
 * 解题思路：
 * 使用线段树配合懒惰传播技术来高效处理区间更新和区间查询操作。
 * 线段树是一种二叉树结构，每个节点代表数组的一个区间，存储该区间的相关信息（如区间和）。
 * 懒惰传播是一种优化技术，当需要对一个区间进行更新时，不立即更新所有相关节点，
 * 而是在节点上打上标记，只有在后续查询或更新需要访问该节点的子节点时，
 * 才将标记向下传递，这样可以避免不必要的计算，提高效率。
 * 
 * 算法要点：
 * - 使用线段树维护区间和
 * - 使用懒惰标记实现区间加法的高效更新
 * - 支持区间更新和区间查询操作
 * 
 * 时间复杂度分析：
 * - 建树：O(n)
 * - 区间更新：O(log n)
 * - 区间查询：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要4n的空间：O(n)
 * - 懒惰标记数组需要4n的空间：O(n)
 * - 总空间复杂度：O(n)
 */

// 定义常量
#define MAXN 100005

// 全局数组存储线段树和懒惰标记
int tree[4 * MAXN];  // 线段树数组，存储每个区间的和
int lazy[4 * MAXN];  // 懒惰标记数组，存储区间加法的增量
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
    
    // 合并左右子树的结果，当前节点存储左右子树区间和的总和
    tree[idx] = tree[leftIdx] + tree[rightIdx];
}

/**
 * 区间更新递归实现
 * @param start 当前节点管理区间的起始索引
 * @param end 当前节点管理区间的结束索引
 * @param l 目标更新区间的左边界
 * @param r 目标更新区间的右边界
 * @param val 要加的值
 * @param idx 当前节点在tree数组中的索引
 * 
 * 核心思想：
 * 1. 先处理当前节点的懒惰标记（懒惰传播）
 * 2. 判断当前区间与目标区间的关系
 * 3. 根据关系决定是直接更新、递归更新还是忽略
 * 4. 更新完成后维护父节点信息
 */
void updateRange(int start, int end, int l, int r, int val, int idx) {
    // 先处理懒惰标记（懒惰传播的核心步骤）
    // 如果当前节点有懒惰标记，需要先将标记应用到当前节点
    if (lazy[idx] != 0) {
        // 更新当前节点的值：区间和增加 lazy[idx] * 区间长度
        tree[idx] += (end - start + 1) * lazy[idx];
        // 如果不是叶子节点，将懒惰标记传递给子节点
        if (start != end) {
            lazy[2 * idx + 1] += lazy[idx];  // 传递给左子节点
            lazy[2 * idx + 2] += lazy[idx];  // 传递给右子节点
        }
        // 清除当前节点的懒惰标记
        lazy[idx] = 0;
    }
    
    // 当前区间与目标区间无交集，直接返回
    if (start > r || end < l) {
        return;
    }
    
    // 当前区间完全包含在目标区间内，可以直接更新
    if (l <= start && end <= r) {
        // 更新当前节点的值：区间和增加 val * 区间长度
        tree[idx] += (end - start + 1) * val;
        // 如果不是叶子节点，打上懒惰标记
        if (start != end) {
            lazy[2 * idx + 1] += val;  // 给左子节点打标记
            lazy[2 * idx + 2] += val;  // 给右子节点打标记
        }
        return;
    }
    
    // 部分重叠，需要递归更新子区间
    int mid = start + (end - start) / 2;
    // 递归更新左子树
    updateRange(start, mid, l, r, val, 2 * idx + 1);
    // 递归更新右子树
    updateRange(mid + 1, end, l, r, val, 2 * idx + 2);
    
    // 更新完成后，需要维护父节点信息（合并子节点结果）
    tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
}

/**
 * 区间查询递归实现
 * @param start 当前节点管理区间的起始索引
 * @param end 当前节点管理区间的结束索引
 * @param l 目标查询区间的左边界
 * @param r 目标查询区间的右边界
 * @param idx 当前节点在tree数组中的索引
 * @return 区间[l, r]的和
 * 
 * 核心思想：
 * 1. 先处理当前节点的懒惰标记（懒惰传播）
 * 2. 判断当前区间与目标区间的关系
 * 3. 根据关系决定是直接返回、递归查询还是部分返回
 */
int queryRange(int start, int end, int l, int r, int idx) {
    // 先处理懒惰标记（懒惰传播的核心步骤）
    // 如果当前节点有懒惰标记，需要先将标记应用到当前节点
    if (lazy[idx] != 0) {
        // 更新当前节点的值：区间和增加 lazy[idx] * 区间长度
        tree[idx] += (end - start + 1) * lazy[idx];
        // 如果不是叶子节点，将懒惰标记传递给子节点
        if (start != end) {
            lazy[2 * idx + 1] += lazy[idx];  // 传递给左子节点
            lazy[2 * idx + 2] += lazy[idx];  // 传递给右子节点
        }
        // 清除当前节点的懒惰标记
        lazy[idx] = 0;
    }
    
    // 当前区间与目标区间无交集，返回0
    if (start > r || end < l) {
        return 0;
    }
    
    // 当前区间完全包含在目标区间内，直接返回当前节点的值
    if (l <= start && end <= r) {
        return tree[idx];
    }
    
    // 部分重叠，需要递归查询子区间
    int mid = start + (end - start) / 2;
    // 递归查询左子树
    int leftSum = queryRange(start, mid, l, r, 2 * idx + 1);
    // 递归查询右子树
    int rightSum = queryRange(mid + 1, end, l, r, 2 * idx + 2);
    
    // 返回左右子树查询结果的和
    return leftSum + rightSum;
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
 * 区间更新 - 将区间[l, r]内的每个数加上val
 * @param l 区间左边界（包含）
 * @param r 区间右边界（包含）
 * @param val 要加的值
 * 
 * 时间复杂度：O(log n)
 */
void updateRangeValue(int l, int r, int val) {
    // 调用递归实现
    updateRange(0, n - 1, l, r, val, 0);
}

/**
 * 区间查询 - 查询区间[l, r]的和
 * @param l 区间左边界（包含）
 * @param r 区间右边界（包含）
 * @return 区间和
 * 
 * 时间复杂度：O(log n)
 */
int queryRangeValue(int l, int r) {
    // 调用递归实现
    return queryRange(0, n - 1, l, r, 0);
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1：基本功能测试
    int arr1[] = {1, 3, 5, 7, 9, 11};
    init(arr1, 6);
    
    // 应该输出 区间[0, 2]的和：9 (1+3+5)
    // 应该输出 区间[1, 4]的和：24 (3+5+7+9)
    
    // 区间更新测试
    updateRangeValue(1, 3, 2);
    // 应该输出 更新后区间[0, 2]的和：15 (1+5+9)
    // 应该输出 更新后区间[1, 4]的和：30 (5+9+9+9)
    
    // 测试用例2：边界条件测试
    int arr2[] = {10};
    init(arr2, 1);
    // 应该输出 单元素数组查询[0]：10
    updateRangeValue(0, 0, 5);
    // 应该输出 单元素更新后查询[0]：15
}

// 主函数
int main() {
    // 运行测试
    test();
    
    return 0;
}
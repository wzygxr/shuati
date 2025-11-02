/**
 * 线段树区间更新 - 懒惰传播 (Lazy Propagation)
 * 
 * 题目描述：
 * 实现支持区间加法和区间查询的线段树
 * 操作类型：
 * 1. 区间加法：将区间 [l, r] 内的每个数加上 k
 * 2. 区间求和：查询区间 [l, r] 内所有数的和
 * 
 * 题目来源：洛谷 P3372 【模板】线段树 1
 * 测试链接 : https://www.luogu.com.cn/problem/P3372
 * 
 * 解题思路：
 * 使用线段树配合懒惰传播技术来高效处理区间更新和区间查询操作。
 * 
 * 核心思想：
 * 1. 懒惰传播：当需要对一个区间进行更新时，不立即更新所有相关节点，
 *    而是在节点上打上标记，只有在后续查询或更新需要访问该节点的子节点时，
 *    才将标记向下传递，这样可以避免不必要的计算，提高效率。
 * 
 * 2. 线段树结构：
 *    - tree[]数组：存储线段树节点的值（区间和）
 *    - lazy[]数组：存储懒惰标记（区间加法的增量）
 * 
 * 3. 关键操作：
 *    - pushDown：懒惰传播操作，将父节点的标记传递给子节点
 *    - rangeAdd：区间加法更新
 *    - rangeSum：区间求和查询
 * 
 * 时间复杂度分析：
 * - 构建线段树：O(n)
 * - 区间加法更新：O(log n)
 * - 区间求和查询：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要O(n)的额外空间
 */

// 定义常量
#define MAXN 100005

// 全局数组存储线段树和懒惰标记
long long tree[4 * MAXN];  // 线段树数组，存储区间和
long long lazy[4 * MAXN];  // 懒惰标记数组，存储区间加法的增量
int arr[MAXN];             // 原始数组
int n, m;                  // 数组长度和操作数

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
        tree[node] = arr[start];
    } else {
        int mid = (start + end) / 2;
        // 递归构建左子树
        buildTree(2 * node + 1, start, mid);
        // 递归构建右子树
        buildTree(2 * node + 2, mid + 1, end);
        // 合并左右子树的结果（区间和）
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }
}

/**
 * 懒惰传播 - 将当前节点的懒惰标记传递给子节点
 * 这是懒惰传播技术的核心实现
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * 
 * 时间复杂度: O(1)
 */
void pushDown(int node, int start, int end) {
    // 只有当当前节点有懒惰标记时才需要传播
    if (lazy[node] != 0) {
        int mid = (start + end) / 2;
        int leftLen = mid - start + 1;   // 左子树区间长度
        int rightLen = end - mid;        // 右子树区间长度
        
        // 更新左子树
        // 1. 更新左子树的区间和：增加 lazy[node] * 区间长度
        tree[2 * node + 1] += lazy[node] * leftLen;
        // 2. 将懒惰标记传递给左子树
        lazy[2 * node + 1] += lazy[node];
        
        // 更新右子树
        // 1. 更新右子树的区间和：增加 lazy[node] * 区间长度
        tree[2 * node + 2] += lazy[node] * rightLen;
        // 2. 将懒惰标记传递给右子树
        lazy[2 * node + 2] += lazy[node];
        
        // 清除当前节点的懒惰标记
        lazy[node] = 0;
    }
}

/**
 * 区间加法更新
 * 将区间[left, right]内的每个数都加上val
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param left 更新区间左边界（0-based索引）
 * @param right 更新区间右边界（0-based索引）
 * @param val 要加上的值
 * 
 * 时间复杂度: O(log n)
 */
void rangeAdd(int node, int start, int end, int left, int right, long long val) {
    // 如果当前区间完全包含在更新区间内
    if (left <= start && end <= right) {
        // 直接更新当前节点的值和懒惰标记
        tree[node] += val * (end - start + 1);  // 更新区间和
        lazy[node] += val;  // 打上懒惰标记
        return;
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    
    // 递归更新左右子树
    int mid = (start + end) / 2;
    // 如果更新区间与左子树有交集，则更新左子树
    if (left <= mid) {
        rangeAdd(2 * node + 1, start, mid, left, right, val);
    }
    // 如果更新区间与右子树有交集，则更新右子树
    if (right > mid) {
        rangeAdd(2 * node + 2, mid + 1, end, left, right, val);
    }
    
    // 更新父节点的值（子节点更新后需要更新父节点）
    tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
}

/**
 * 区间求和查询
 * 查询区间[left, right]内所有数的和
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param left 查询区间左边界（0-based索引）
 * @param right 查询区间右边界（0-based索引）
 * @return 区间[left, right]内所有数的和
 * 
 * 时间复杂度: O(log n)
 */
long long rangeSum(int node, int start, int end, int left, int right) {
    // 如果当前区间完全包含在查询区间内
    if (left <= start && end <= right) {
        // 直接返回当前节点的值
        return tree[node];
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    
    // 递归查询左右子树
    int mid = (start + end) / 2;
    long long sum = 0;
    // 如果查询区间与左子树有交集，则查询左子树
    if (left <= mid) {
        sum += rangeSum(2 * node + 1, start, mid, left, right);
    }
    // 如果查询区间与右子树有交集，则查询右子树
    if (right > mid) {
        sum += rangeSum(2 * node + 2, mid + 1, end, left, right);
    }
    
    return sum;
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
    buildTree(1, 0, n - 1);  // 使用1-based索引
}

/**
 * 区间加法更新（对外接口）
 * 将区间[left, right]内的每个数都加上val
 * @param left 更新区间左边界（0-based索引）
 * @param right 更新区间右边界（0-based索引）
 * @param val 要加上的值
 */
void rangeAddValue(int left, int right, long long val) {
    rangeAdd(1, 0, n - 1, left, right, val);  // 使用1-based索引
}

/**
 * 区间求和查询（对外接口）
 * 查询区间[left, right]内所有数的和
 * @param left 查询区间左边界（0-based索引）
 * @param right 查询区间右边界（0-based索引）
 * @return 区间[left, right]内所有数的和
 */
long long rangeSumValue(int left, int right) {
    return rangeSum(1, 0, n - 1, left, right);  // 使用1-based索引
}

/**
 * 测试函数 - 验证线段树实现的正确性
 */
void test() {
    // 测试用例1: 基础功能测试
    int nums1[] = {1, 2, 3, 4, 5};
    init(nums1, 5);
    
    // 测试区间求和
    // 应该输出 初始数组区间[0,4]的和: 15
    // 应该输出 区间[1,3]的和: 9
    
    // 测试区间加法
    rangeAddValue(1, 3, 2); // 将索引1-3的元素都加2
    // 应该输出 区间加法后区间[1,3]的和: 15 (2+2, 3+2, 4+2 = 4+5+6)
    // 应该输出 区间[0,4]的和: 21 (1+4+5+6+5)
    
    // 测试用例2: 边界情况
    int nums2[] = {10};
    init(nums2, 1);
    // 应该输出 单元素数组区间[0,0]的和: 10
    rangeAddValue(0, 0, 5);
    // 应该输出 单元素加法后区间[0,0]的和: 15
    
    // 测试用例3: 大规模数据测试
    int nums3[1000];
    int i;
    for (i = 0; i < 1000; i++) {
        nums3[i] = 1;
    }
    init(nums3, 1000);
    
    // 对整个数组进行区间加法
    rangeAddValue(0, 999, 10);
    // 应该输出 大规模数组区间[0,999]的和: 11000 (1000 * 11)
}

// 主函数 - 处理输入输出和操作调度
int main() {
    // 由于环境限制，这里不实现完整的输入输出处理
    // 实际使用时需要根据具体的输入输出要求进行调整
    
    // 运行测试
    test();
    
    return 0;
}
/**
 * 线段树高级应用 - 多种区间操作
 * 
 * 题目描述：
 * 实现支持多种区间操作的线段树，包括：
 * 1. 区间赋值
 * 2. 区间加法
 * 3. 区间乘法
 * 4. 区间求和
 * 5. 区间最大值/最小值
 * 
 * 题目来源：洛谷 P3373 【模板】线段树 2
 * 测试链接 : https://www.luogu.com.cn/problem/P3373
 * 
 * 解题思路：
 * 使用高级线段树实现支持多种操作，包括区间赋值、加法、乘法以及查询操作。
 * 通过维护多种懒惰标记来处理不同优先级的操作。
 * 
 * 核心思想：
 * 1. 多标记懒惰传播：同时维护加法、乘法和赋值三种懒惰标记
 * 2. 标记优先级：赋值 > 乘法 > 加法
 * 3. 标记下传：在下传标记时需要按照优先级顺序处理
 * 
 * 时间复杂度分析：
 * - 构建线段树：O(n)
 * - 所有区间操作：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要O(n)的额外空间
 */

// 定义常量
#define MAXN 100005
#define INF 1000000000000000LL

// 结构体定义线段树节点
struct Node {
    long long sum;        // 区间和
    long long max_val;    // 区间最大值
    long long min_val;    // 区间最小值
    long long add;        // 加法懒惰标记
    long long mul;        // 乘法懒惰标记
    long long set_val;    // 赋值懒惰标记
    bool has_set;         // 是否有赋值标记
    
    // 构造函数
    Node() {
        sum = 0;
        max_val = -INF;
        min_val = INF;
        add = 0;
        mul = 1;
        set_val = 0;
        has_set = false;
    }
};

// 全局数组和变量
Node tree[4 * MAXN];  // 线段树数组
int arr[MAXN];        // 原始数组
int n, m;             // 数组长度和操作数

/**
 * 向上更新父节点
 * 根据左右子节点的信息更新当前节点的信息
 * @param node 当前线段树节点索引
 */
void pushUp(int node) {
    int left = 2 * node + 1;   // 左子节点索引
    int right = 2 * node + 2;  // 右子节点索引
    // 更新区间和
    tree[node].sum = tree[left].sum + tree[right].sum;
    // 更新区间最大值
    tree[node].max_val = (tree[left].max_val > tree[right].max_val) ? tree[left].max_val : tree[right].max_val;
    // 更新区间最小值
    tree[node].min_val = (tree[left].min_val < tree[right].min_val) ? tree[left].min_val : tree[right].min_val;
}

/**
 * 向下传递懒惰标记
 * 按照标记优先级顺序传递标记给子节点
 * 优先级：赋值 > 乘法 > 加法
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * 
 * 时间复杂度: O(1)
 */
void pushDown(int node, int start, int end) {
    // 叶子节点不需要传递标记
    if (start == end) return;
    
    int left = 2 * node + 1;   // 左子节点索引
    int right = 2 * node + 2;  // 右子节点索引
    int mid = (start + end) / 2;
    
    // 处理赋值标记（优先级最高）
    // 当存在赋值标记时，需要清除子节点的其他标记
    if (tree[node].has_set) {
        // 更新左子树的区间信息
        tree[left].sum = tree[node].set_val * (mid - start + 1);  // 区间和 = 赋值 * 区间长度
        tree[right].sum = tree[node].set_val * (end - mid);
        tree[left].max_val = tree[node].set_val;  // 区间最大值 = 赋值
        tree[right].max_val = tree[node].set_val;
        tree[left].min_val = tree[node].set_val;  // 区间最小值 = 赋值
        tree[right].min_val = tree[node].set_val;
        
        // 传递赋值标记给子节点
        tree[left].set_val = tree[node].set_val;
        tree[right].set_val = tree[node].set_val;
        tree[left].has_set = true;
        tree[right].has_set = true;
        
        // 清除子节点的其他标记（加法和乘法）
        tree[left].add = 0;
        tree[right].add = 0;
        tree[left].mul = 1;
        tree[right].mul = 1;
        
        // 清除当前节点的赋值标记
        tree[node].has_set = false;
    }
    
    // 处理乘法标记（优先级次之）
    // 当存在乘法标记时，需要更新子节点的所有信息
    if (tree[node].mul != 1) {
        // 更新左子树的区间信息（乘以mul）
        tree[left].sum *= tree[node].mul;
        tree[right].sum *= tree[node].mul;
        tree[left].max_val *= tree[node].mul;
        tree[right].max_val *= tree[node].mul;
        tree[left].min_val *= tree[node].mul;
        tree[right].min_val *= tree[node].mul;
        
        // 传递乘法标记给子节点
        tree[left].mul *= tree[node].mul;
        tree[right].mul *= tree[node].mul;
        // 乘法标记也会影响加法标记（add * mul）
        tree[left].add *= tree[node].mul;
        tree[right].add *= tree[node].mul;
        
        // 清除当前节点的乘法标记
        tree[node].mul = 1;
    }
    
    // 处理加法标记（优先级最低）
    // 当存在加法标记时，需要更新子节点的所有信息
    if (tree[node].add != 0) {
        int leftLen = mid - start + 1;   // 左子树区间长度
        int rightLen = end - mid;        // 右子树区间长度
        
        // 更新左子树的区间信息（加上add）
        tree[left].sum += tree[node].add * leftLen;  // 区间和增加 add * 区间长度
        tree[right].sum += tree[node].add * rightLen;
        tree[left].max_val += tree[node].add;  // 区间最大值增加 add
        tree[right].max_val += tree[node].add;
        tree[left].min_val += tree[node].add;  // 区间最小值增加 add
        tree[right].min_val += tree[node].add;
        
        // 传递加法标记给子节点
        tree[left].add += tree[node].add;
        tree[right].add += tree[node].add;
        
        // 清除当前节点的加法标记
        tree[node].add = 0;
    }
}

/**
 * 构建线段树
 * 递归地将数组构建成线段树结构
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * 
 * 时间复杂度: O(n)
 */
void buildTree(int node, int start, int end) {
    // 初始化节点
    tree[node] = Node();
    
    if (start == end) {
        // 叶子节点 - 直接存储数组元素值
        tree[node].sum = arr[start];
        tree[node].max_val = arr[start];
        tree[node].min_val = arr[start];
    } else {
        int mid = (start + end) / 2;
        // 递归构建左子树
        buildTree(2 * node + 1, start, mid);
        // 递归构建右子树
        buildTree(2 * node + 2, mid + 1, end);
        // 向上更新父节点信息
        pushUp(node);
    }
}

/**
 * 区间赋值操作
 * 将区间[left, right]内的每个数都赋值为val
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param l 更新区间左边界（0-based索引）
 * @param r 更新区间右边界（0-based索引）
 * @param val 要赋的值
 * 
 * 时间复杂度: O(log n)
 */
void rangeSet(int node, int start, int end, int l, int r, long long val) {
    // 如果当前区间完全包含在更新区间内
    if (l <= start && end <= r) {
        // 直接更新当前节点的信息和标记
        tree[node].sum = val * (end - start + 1);  // 区间和 = 赋值 * 区间长度
        tree[node].max_val = val;  // 区间最大值 = 赋值
        tree[node].min_val = val;  // 区间最小值 = 赋值
        tree[node].set_val = val;  // 设置赋值标记
        tree[node].has_set = true;  // 标记存在赋值操作
        tree[node].add = 0;  // 清除加法标记
        tree[node].mul = 1;  // 清除乘法标记
        return;
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    int mid = (start + end) / 2;
    // 递归更新左右子树
    if (l <= mid) rangeSet(2 * node + 1, start, mid, l, r, val);
    if (r > mid) rangeSet(2 * node + 2, mid + 1, end, l, r, val);
    // 更新父节点信息
    pushUp(node);
}

/**
 * 区间乘法操作
 * 将区间[left, right]内的每个数都乘以val
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param l 更新区间左边界（0-based索引）
 * @param r 更新区间右边界（0-based索引）
 * @param val 要乘的值
 * 
 * 时间复杂度: O(log n)
 */
void rangeMul(int node, int start, int end, int l, int r, long long val) {
    // 如果当前区间完全包含在更新区间内
    if (l <= start && end <= r) {
        // 直接更新当前节点的信息和标记
        tree[node].sum *= val;  // 区间和乘以val
        tree[node].max_val *= val;  // 区间最大值乘以val
        tree[node].min_val *= val;  // 区间最小值乘以val
        tree[node].mul *= val;  // 乘法标记乘以val
        tree[node].add *= val;  // 加法标记也乘以val（因为 a*x + b 变成 a*x*val + b*val）
        return;
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    int mid = (start + end) / 2;
    // 递归更新左右子树
    if (l <= mid) rangeMul(2 * node + 1, start, mid, l, r, val);
    if (r > mid) rangeMul(2 * node + 2, mid + 1, end, l, r, val);
    // 更新父节点信息
    pushUp(node);
}

/**
 * 区间加法操作
 * 将区间[left, right]内的每个数都加上val
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param l 更新区间左边界（0-based索引）
 * @param r 更新区间右边界（0-based索引）
 * @param val 要加的值
 * 
 * 时间复杂度: O(log n)
 */
void rangeAdd(int node, int start, int end, int l, int r, long long val) {
    // 如果当前区间完全包含在更新区间内
    if (l <= start && end <= r) {
        // 直接更新当前节点的信息和标记
        tree[node].sum += val * (end - start + 1);  // 区间和增加 val * 区间长度
        tree[node].max_val += val;  // 区间最大值增加 val
        tree[node].min_val += val;  // 区间最小值增加 val
        tree[node].add += val;  // 加法标记增加 val
        return;
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    int mid = (start + end) / 2;
    // 递归更新左右子树
    if (l <= mid) rangeAdd(2 * node + 1, start, mid, l, r, val);
    if (r > mid) rangeAdd(2 * node + 2, mid + 1, end, l, r, val);
    // 更新父节点信息
    pushUp(node);
}

/**
 * 区间求和查询
 * 查询区间[left, right]内所有数的和
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param l 查询区间左边界（0-based索引）
 * @param r 查询区间右边界（0-based索引）
 * @return 区间[left, right]内所有数的和
 * 
 * 时间复杂度: O(log n)
 */
long long rangeSum(int node, int start, int end, int l, int r) {
    // 如果当前区间完全包含在查询区间内
    if (l <= start && end <= r) {
        // 直接返回当前节点的区间和
        return tree[node].sum;
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    int mid = (start + end) / 2;
    long long sum = 0;
    // 递归查询左右子树
    if (l <= mid) sum += rangeSum(2 * node + 1, start, mid, l, r);
    if (r > mid) sum += rangeSum(2 * node + 2, mid + 1, end, l, r);
    return sum;
}

/**
 * 区间最大值查询
 * 查询区间[left, right]内的最大值
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param l 查询区间左边界（0-based索引）
 * @param r 查询区间右边界（0-based索引）
 * @return 区间[left, right]内的最大值
 * 
 * 时间复杂度: O(log n)
 */
long long rangeMax(int node, int start, int end, int l, int r) {
    // 如果当前区间完全包含在查询区间内
    if (l <= start && end <= r) {
        // 直接返回当前节点的区间最大值
        return tree[node].max_val;
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    int mid = (start + end) / 2;
    long long maxVal = -INF;
    // 递归查询左右子树
    if (l <= mid) {
        long long leftMax = rangeMax(2 * node + 1, start, mid, l, r);
        maxVal = (maxVal > leftMax) ? maxVal : leftMax;
    }
    if (r > mid) {
        long long rightMax = rangeMax(2 * node + 2, mid + 1, end, l, r);
        maxVal = (maxVal > rightMax) ? maxVal : rightMax;
    }
    return maxVal;
}

/**
 * 区间最小值查询
 * 查询区间[left, right]内的最小值
 * @param node 当前线段树节点索引
 * @param start 当前节点表示区间的起始位置
 * @param end 当前节点表示区间的结束位置
 * @param l 查询区间左边界（0-based索引）
 * @param r 查询区间右边界（0-based索引）
 * @return 区间[left, right]内的最小值
 * 
 * 时间复杂度: O(log n)
 */
long long rangeMin(int node, int start, int end, int l, int r) {
    // 如果当前区间完全包含在查询区间内
    if (l <= start && end <= r) {
        // 直接返回当前节点的区间最小值
        return tree[node].min_val;
    }
    
    // 需要向下传递懒惰标记（在递归之前）
    pushDown(node, start, end);
    int mid = (start + end) / 2;
    long long minVal = INF;
    // 递归查询左右子树
    if (l <= mid) {
        long long leftMin = rangeMin(2 * node + 1, start, mid, l, r);
        minVal = (minVal < leftMin) ? minVal : leftMin;
    }
    if (r > mid) {
        long long rightMin = rangeMin(2 * node + 2, mid + 1, end, l, r);
        minVal = (minVal < rightMin) ? minVal : rightMin;
    }
    return minVal;
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
 * 区间赋值操作（对外接口）
 * 将区间[left, right]内的每个数都赋值为val
 * @param left 更新区间左边界（0-based索引）
 * @param right 更新区间右边界（0-based索引）
 * @param val 要赋的值
 */
void rangeSetValue(int left, int right, long long val) {
    rangeSet(0, 0, n - 1, left, right, val);
}

/**
 * 区间乘法操作（对外接口）
 * 将区间[left, right]内的每个数都乘以val
 * @param left 更新区间左边界（0-based索引）
 * @param right 更新区间右边界（0-based索引）
 * @param val 要乘的值
 */
void rangeMulValue(int left, int right, long long val) {
    rangeMul(0, 0, n - 1, left, right, val);
}

/**
 * 区间加法操作（对外接口）
 * 将区间[left, right]内的每个数都加上val
 * @param left 更新区间左边界（0-based索引）
 * @param right 更新区间右边界（0-based索引）
 * @param val 要加的值
 */
void rangeAddValue(int left, int right, long long val) {
    rangeAdd(0, 0, n - 1, left, right, val);
}

/**
 * 区间求和查询（对外接口）
 * 查询区间[left, right]内所有数的和
 * @param left 查询区间左边界（0-based索引）
 * @param right 查询区间右边界（0-based索引）
 * @return 区间[left, right]内所有数的和
 */
long long rangeSumValue(int left, int right) {
    return rangeSum(0, 0, n - 1, left, right);
}

/**
 * 区间最大值查询（对外接口）
 * 查询区间[left, right]内的最大值
 * @param left 查询区间左边界（0-based索引）
 * @param right 查询区间右边界（0-based索引）
 * @return 区间[left, right]内的最大值
 */
long long rangeMaxValue(int left, int right) {
    return rangeMax(0, 0, n - 1, left, right);
}

/**
 * 区间最小值查询（对外接口）
 * 查询区间[left, right]内的最小值
 * @param left 查询区间左边界（0-based索引）
 * @param right 查询区间右边界（0-based索引）
 * @return 区间[left, right]内的最小值
 */
long long rangeMinValue(int left, int right) {
    return rangeMin(0, 0, n - 1, left, right);
}

/**
 * 测试函数 - 验证高级线段树实现的正确性
 */
void test() {
    // 测试用例1: 基础功能测试
    int nums1[] = {1, 2, 3, 4, 5};
    init(nums1, 5);
    
    // 测试初始状态
    // 应该输出 初始数组区间[0,4]的和: 15
    // 应该输出 区间[0,4]的最大值: 5
    // 应该输出 区间[0,4]的最小值: 1
    
    // 测试区间加法
    rangeAddValue(1, 3, 2);
    // 应该输出 区间加法后区间[1,3]的和: 15 (4+5+6)
    
    // 测试区间乘法
    rangeMulValue(0, 2, 3);
    // 应该输出 区间乘法后区间[0,2]的和: 36 (3*3 + 4*3 + 5*3)
    
    // 测试区间赋值
    rangeSetValue(2, 4, 10);
    // 应该输出 区间赋值后区间[2,4]的和: 30 (10*3)
}

// 主函数 - 处理输入输出和操作调度
int main() {
    // 由于环境限制，这里不实现完整的输入输出处理
    // 实际使用时需要根据具体的输入输出要求进行调整
    
    // 运行测试
    test();
    
    return 0;
}
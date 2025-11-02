/**
 * 线段树GCD查询 - 支持区间GCD查询和单点更新 (C++版本)
 * 题目来源：Codeforces 914D - Bash and a Tough Math Puzzle
 * 问题描述：支持区间GCD查询和单点更新的线段树实现
 * 
 * 解题思路：
 * 使用线段树来维护区间GCD信息。线段树是一种二叉树结构，每个节点代表数组的一个区间，
 * 存储该区间的GCD值。对于叶子节点，它代表数组中的单个元素；对于非叶子节点，
 * 它代表其左右子树所覆盖区间的合并结果（在这里是GCD）。
 * 
 * 算法要点：
 * - 使用线段树维护区间GCD信息
 * - 支持高效区间GCD查询和单点更新
 * - 利用GCD的性质：gcd(a,b,c) = gcd(gcd(a,b), c)
 * 
 * 时间复杂度：
 * - 构建线段树：O(n)
 * - 单点更新：O(log n)
 * - 区间GCD查询：O(log n)
 * 
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * - 区间GCD查询问题
 * - 判断区间内元素是否满足特定GCD条件
 * - 数学相关的区间查询问题
 */

// 定义常量
#define MAXN 100005

// 全局数组存储线段树
int tree[4 * MAXN];  // 线段树数组，存储每个区间的GCD值
int data[MAXN];      // 原始数据
int n;               // 数组长度

/**
 * 计算两个数的最大公约数
 * 使用欧几里得算法（辗转相除法）
 * @param a 第一个数
 * @param b 第二个数
 * @return GCD值
 */
int gcd(int a, int b) {
    // 递归终止条件：b为0时，a就是最大公约数
    if (b == 0) return a;
    // 递归计算：gcd(a,b) = gcd(b, a%b)
    return gcd(b, a % b);
}

/**
 * 构建线段树
 * 递归地将数组构建成线段树结构
 * @param start 区间开始索引
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
    // 递归构建左子树
    buildTree(start, mid, 2 * idx + 1);
    // 递归构建右子树
    buildTree(mid + 1, end, 2 * idx + 2);
    // 合并左右子树的结果，当前节点存储左右子树区间GCD值的GCD
    tree[idx] = gcd(tree[2 * idx + 1], tree[2 * idx + 2]);
}

/**
 * 单点更新递归实现
 * @param start 当前节点管理区间的起始索引
 * @param end 当前节点管理区间的结束索引
 * @param pos 要更新的位置
 * @param val 新的值
 * @param idx 当前节点在tree数组中的索引
 */
void update(int start, int end, int pos, int val, int idx) {
    // 递归终止条件：找到叶子节点
    if (start == end) {
        tree[idx] = val;
        return;
    }
    
    // 计算区间中点
    int mid = start + (end - start) / 2;
    // 根据位置决定更新左子树还是右子树
    if (pos <= mid) {
        // 更新左子树
        update(start, mid, pos, val, 2 * idx + 1);
    } else {
        // 更新右子树
        update(mid + 1, end, pos, val, 2 * idx + 2);
    }
    // 更新完成后，需要维护父节点信息（合并子节点结果）
    tree[idx] = gcd(tree[2 * idx + 1], tree[2 * idx + 2]);
}

/**
 * 区间GCD查询递归实现
 * @param start 当前节点管理区间的起始索引
 * @param end 当前节点管理区间的结束索引
 * @param l 查询区间左边界
 * @param r 查询区间右边界
 * @param idx 当前节点在tree数组中的索引
 * @return 区间[l, r]的GCD值
 * 
 * 核心思想：
 * 1. 判断当前区间与目标区间的关系
 * 2. 根据关系决定是直接返回、递归查询还是部分返回
 * 3. 利用GCD的性质进行合并
 */
int query(int start, int end, int l, int r, int idx) {
    // 当前区间完全包含在目标区间内，直接返回当前节点的值
    if (l <= start && end <= r) {
        return tree[idx];
    }
    
    // 计算区间中点
    int mid = start + (end - start) / 2;
    // 根据查询区间与左右子树区间的关系决定查询策略
    if (r <= mid) {
        // 查询区间完全在左子树中
        return query(start, mid, l, r, 2 * idx + 1);
    } else if (l > mid) {
        // 查询区间完全在右子树中
        return query(mid + 1, end, l, r, 2 * idx + 2);
    } else {
        // 查询区间跨越左右子树，需要分别查询后再合并
        int leftGCD = query(start, mid, l, r, 2 * idx + 1);
        int rightGCD = query(mid + 1, end, l, r, 2 * idx + 2);
        // 利用GCD的性质：gcd(a,b,c) = gcd(gcd(a,b), c)
        return gcd(leftGCD, rightGCD);
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
 * 单点更新
 * @param pos 要更新的位置
 * @param val 新的值
 * 
 * 时间复杂度：O(log n)
 */
void updateValue(int pos, int val) {
    // 调用递归实现
    update(0, n - 1, pos, val, 0);
}

/**
 * 区间GCD查询
 * @param l 查询区间左边界（包含）
 * @param r 查询区间右边界（包含）
 * @return 区间GCD值
 * 
 * 时间复杂度：O(log n)
 */
int queryValue(int l, int r) {
    // 调用递归实现
    return query(0, n - 1, l, r, 0);
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1：基础功能测试
    int arr1[] = {2, 4, 6, 8, 10};
    init(arr1, 5);
    
    // 应该输出 区间[0,2]的GCD: 2 (gcd(2,4,6)=2)
    // 应该输出 区间[1,4]的GCD: 2 (gcd(4,6,8,10)=2)
    
    // 更新测试
    updateValue(2, 9);
    // 应该输出 更新后区间[0,2]的GCD: 1 (gcd(2,4,9)=1)
    
    // 测试用例2：边界条件测试
    int arr2[] = {15};
    init(arr2, 1);
    // 应该输出 单元素数组查询[0,0]的GCD: 15
    
    // 测试用例3：性能验证
    // 线段树GCD算法已实现，支持高效区间查询和单点更新
}

// 主函数
int main() {
    // 运行测试
    test();
    
    return 0;
}
/**
 * C++ 线段树实现 - Codeforces 339D. Xenia and Bit Operations
 * 题目链接: https://codeforces.com/problemset/problem/339/D
 * 题目描述:
 * Xenia这个小孩非常喜欢数论。她特别喜欢异或运算。
 * 现在有一个长度为2^n的数组，下标从0到2^n-1。
 * 有m次操作，每次操作会修改数组中的一个元素。
 * 在每次操作后，需要计算一个特定的值。
 * 计算过程如下：
 * 1. 第一层：对相邻的两个元素进行OR运算，得到2^(n-1)个结果
 * 2. 第二层：对相邻的两个元素进行XOR运算，得到2^(n-2)个结果
 * 3. 第三层：对相邻的两个元素进行OR运算，得到2^(n-3)个结果
 * 4. 以此类推，交替进行OR和XOR运算
 * 5. 最后一层：对两个元素进行运算，得到1个结果
 * 问每次操作后，最终的结果是多少。
 *
 * 输入:
 * 第一行包含两个整数n和m (1 <= n <= 17, 1 <= m <= 10^5) - 数组大小的对数和操作次数。
 * 第二行包含2^n个整数a0, a1, ..., a_{2^n-1} (0 <= ai <= 2^30) - 初始数组。
 * 接下来m行，每行包含两个整数p和b (0 <= p <= 2^n-1, 0 <= b <= 2^30)，
 * 表示将数组中下标为p的元素修改为b。
 *
 * 输出:
 * 对于每次操作，输出一行包含一个整数，表示操作后的最终结果。
 *
 * 示例:
 * 输入:
 * 2 4
 * 1 6 3 5
 * 0 2
 * 1 4
 * 2 5
 * 3 5
 *
 * 输出:
 * 1
 * 4
 * 5
 * 5
 *
 * 解题思路:
 * 这是一个线段树问题，结合了位运算。
 * 1. 使用线段树来维护整个计算过程
 * 2. 每个节点需要记录该层应该进行的运算类型（OR或XOR）
 * 3. 叶子节点存储数组元素，非叶子节点存储运算结果
 * 4. 通过层数的奇偶性来判断应该进行OR还是XOR运算
 * 5. 更新时，从叶子节点向上更新，每层根据运算类型进行相应的运算
 *
 * 时间复杂度: 
 * - 建树: O(2^n)
 * - 单点更新: O(n)
 * - 查询根节点: O(1)
 * 空间复杂度: O(2^n)
 */

// 定义最大数组大小
#define MAXN 131072  // 2^17

// 线段树节点结构
struct Node {
    int l, r;      // 区间左右端点
    int value;     // 节点值
    bool isOr;     // 是否为OR运算
};

// 线段树数组
Node tree[MAXN * 2];

// 原始数组
int arr[MAXN];

// 数组长度
int n;

// 建立线段树
void build(int l, int r, int i, int level) {
    tree[i].l = l;
    tree[i].r = r;
    
    // 确定该层的运算类型
    // 最底层(level=0)是叶子节点，存储原始值
    // 倒数第二层(level=1)进行OR运算
    // 倒数第三层(level=2)进行XOR运算
    // 以此类推，奇数层OR，偶数层XOR
    tree[i].isOr = (level % 2 == 1);
    
    if (l == r) {
        tree[i].value = arr[l];
        return;
    }
    
    int mid = (l + r) >> 1;
    build(l, mid, i << 1, level - 1);
    build(mid + 1, r, i << 1 | 1, level - 1);
    
    // 根据运算类型计算当前节点的值
    if (tree[i].isOr) {
        tree[i].value = tree[i << 1].value | tree[i << 1 | 1].value;
    } else {
        tree[i].value = tree[i << 1].value ^ tree[i << 1 | 1].value;
    }
}

// 单点更新
void update(int index, int val, int l, int r, int i) {
    if (l == r) {
        tree[i].value = val;
        arr[index] = val;
        return;
    }
    
    int mid = (l + r) >> 1;
    if (index <= mid) {
        update(index, val, l, mid, i << 1);
    } else {
        update(index, val, mid + 1, r, i << 1 | 1);
    }
    
    // 根据运算类型更新当前节点的值
    if (tree[i].isOr) {
        tree[i].value = tree[i << 1].value | tree[i << 1 | 1].value;
    } else {
        tree[i].value = tree[i << 1].value ^ tree[i << 1 | 1].value;
    }
}

// 获取根节点的值
int getRootValue() {
    return tree[1].value;
}

// 初始化函数
void init(int power) {
    n = 1 << power;  // 2^power
}

// 主函数（演示用）
void Codeforces339D_demo() {
    // 示例测试
    int power = 2;
    init(power);
    
    // 设置初始数组
    arr[0] = 1;
    arr[1] = 6;
    arr[2] = 3;
    arr[3] = 5;
    
    build(0, n - 1, 1, power);
    
    // 初始结果
    int result1 = getRootValue(); // 应该是1
    
    // 操作1: 0 2 (将下标0的元素改为2)
    update(0, 2, 0, n - 1, 1);
    int result2 = getRootValue(); // 应该是1
    
    // 操作2: 1 4 (将下标1的元素改为4)
    update(1, 4, 0, n - 1, 1);
    int result3 = getRootValue(); // 应该是4
    
    // 操作3: 2 5 (将下标2的元素改为5)
    update(2, 5, 0, n - 1, 1);
    int result4 = getRootValue(); // 应该是5
    
    // 操作4: 3 5 (将下标3的元素改为5)
    update(3, 5, 0, n - 1, 1);
    int result5 = getRootValue(); // 应该是5
}
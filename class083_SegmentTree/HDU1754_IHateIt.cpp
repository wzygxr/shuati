/**
 * C++ 线段树实现 - HDU 1754. I Hate It
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1754
 * 题目描述:
 * 很多学校流行一种比较的习惯。老师们很喜欢询问，从某某到某某当中，分数最高的是多少。
 * 这让很多学生很反感。
 * 不管你喜不喜欢，现在需要你做的是，就是按照老师的要求，写一个程序，模拟老师的询问。
 * 当然，老师有时候需要更新某位同学的成绩。
 *
 * 输入:
 * 本题目包含多组测试，请处理到文件结束。
 * 在每个测试的第一行，有两个正整数 N 和 M ( 0<N<=200000,0<M<5000 )，分别代表学生的数目和操作的数目。
 * 学生ID编号从1到N。
 * 第二行包含N个整数，代表这N个学生的初始成绩，接下来有M行。
 * 每一行有一条命令，命令有两种形式：
 * 1. Q A B 代表询问从第A个学生到第B个学生中，成绩最高的是多少。
 * 2. U A B 代表更新第A个学生的成绩为B。
 * 其中A和B均为正整数。
 *
 * 输出:
 * 对于每一次询问，输出一行，表示最高成绩。
 *
 * 示例:
 * 输入:
 * 5 6
 * 1 2 3 4 5
 * Q 1 5
 * U 3 6
 * Q 3 4
 * Q 4 5
 * U 2 9
 * Q 1 5
 *
 * 输出:
 * 5
 * 6
 * 5
 * 9
 *
 * 解题思路:
 * 这是一个经典的线段树问题，支持单点更新和区间查询最大值。
 * 1. 使用线段树维护区间最大值
 * 2. 支持两种操作：
 *    - 单点更新：更新某个学生的学习成绩
 *    - 区间查询：查询某个区间内的最高成绩
 *
 * 时间复杂度: 
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 */

// 定义最大数组大小
#define MAXN 200005

// 线段树结构
struct Node {
    int l, r;    // 区间左右端点
    int max_val; // 区间最大值
};

// 线段树数组
Node tree[MAXN * 4];

// 学生成绩数组
int scores[MAXN];

// 学生数量
int n;

// 向上传递
void pushUp(int i) {
    tree[i].max_val = (tree[i << 1].max_val > tree[i << 1 | 1].max_val) ? 
                      tree[i << 1].max_val : tree[i << 1 | 1].max_val;
}

// 建立线段树
void build(int l, int r, int i) {
    tree[i].l = l;
    tree[i].r = r;
    if (l == r) {
        tree[i].max_val = scores[l];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    pushUp(i);
}

// 单点更新
void update(int index, int val, int l, int r, int i) {
    if (l == r) {
        tree[i].max_val = val;
        scores[index] = val;
        return;
    }
    int mid = (l + r) >> 1;
    if (index <= mid) {
        update(index, val, l, mid, i << 1);
    } else {
        update(index, val, mid + 1, r, i << 1 | 1);
    }
    pushUp(i);
}

// 区间查询最大值
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return tree[i].max_val;
    }
    int mid = (l + r) >> 1;
    int ans = -2147483647; // MIN_INT
    if (jobl <= mid) {
        int temp = query(jobl, jobr, l, mid, i << 1);
        ans = (ans > temp) ? ans : temp;
    }
    if (jobr > mid) {
        int temp = query(jobl, jobr, mid + 1, r, i << 1 | 1);
        ans = (ans > temp) ? ans : temp;
    }
    return ans;
}

// 初始化函数
void init(int num) {
    n = num;
}

// 主函数（演示用）
void HDU1754_demo() {
    // 初始化
    init(5);
    
    // 设置初始成绩
    scores[1] = 1;
    scores[2] = 2;
    scores[3] = 3;
    scores[4] = 4;
    scores[5] = 5;
    
    // 建立线段树
    build(1, 5, 1);
    
    // 演示操作
    // Q 1 5
    int result1 = query(1, 5, 1, 5, 1);
    // 输出结果应为5
    
    // U 3 6
    update(3, 6, 1, 5, 1);
    
    // Q 3 4
    int result2 = query(3, 4, 1, 5, 1);
    // 输出结果应为6
    
    // Q 4 5
    int result3 = query(4, 5, 1, 5, 1);
    // 输出结果应为5
    
    // U 2 9
    update(2, 9, 1, 5, 1);
    
    // Q 1 5
    int result4 = query(1, 5, 1, 5, 1);
    // 输出结果应为9
}
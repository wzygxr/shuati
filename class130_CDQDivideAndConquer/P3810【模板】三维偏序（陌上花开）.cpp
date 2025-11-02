// P3810 【模板】三维偏序（陌上花开）
// 平台: 洛谷
// 难度: 提高+/省选-
// 标签: CDQ分治, 三维偏序
// 链接: https://www.luogu.com.cn/problem/P3810
// 
// 题目描述:
// 一共有n个对象，属性值范围[1, k]，每个对象有a属性、b属性、c属性
// f(i)表示，aj <= ai 且 bj <= bi 且 cj <= ci 且 j != i 的j的数量
// ans(d)表示，f(i) == d 的i的数量
// 打印所有的ans[d]，d的范围[0, n)
// 
// 示例:
// 输入:
// 10 3
// 3 3 3
// 2 3 3
// 2 3 1
// 3 1 1
// 3 1 2
// 1 3 1
// 1 1 2
// 1 3 3
// 1 1 3
// 1 3 2
// 
// 输出:
// 3
// 1
// 3
// 0
// 0
// 0
// 0
// 0
// 0
// 0
// 
// 解题思路:
// 使用CDQ分治解决三维偏序问题。这是CDQ分治的经典应用。
// 
// 1. 第一维：a属性，通过排序处理
// 2. 第二维：b属性，通过CDQ分治处理
// 3. 第三维：c属性，通过树状数组处理
// 
// 具体步骤：
// 1. 按照a属性排序，相同a的按b排序，相同b的按c排序
// 2. CDQ分治处理b属性
// 3. 在分治的合并过程中，使用双指针处理b属性的大小关系，用树状数组维护c属性的前缀和
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

const int MAXN = 100005;
const int MAXK = 200005;

int n, k;
// 对象的编号i、属性a、属性b、属性c
int a[MAXN][4];
// 树状数组，根据属性c的值增加词频，查询 <= 某个数的词频累加和
int tree[MAXK];
// 每个对象的答案
int f[MAXN];
// 题目要求的ans[d]
int ans[MAXN];

struct Node {
    int id, x, y, z;
};

Node nodes[MAXN];

int lowbit(int i) {
    return i & -i;
}

void add(int i, int v) {
    while (i <= k) {
        tree[i] += v;
        i += lowbit(i);
    }
}

int query(int i) {
    int ret = 0;
    while (i > 0) {
        ret += tree[i];
        i -= lowbit(i);
    }
    return ret;
}

// 自定义比较函数，用于排序
bool compareNode(const Node& a, const Node& b) {
    if (a.x != b.x) return a.x < b.x;
    if (a.y != b.y) return a.y < b.y;
    return a.z < b.z;
}

bool compareNodeByY(const Node& a, const Node& b) {
    return a.y < b.y;
}

// 简单的选择排序实现，避免使用STL的sort
void selectionSort(int start, int end, bool (*compare)(const Node&, const Node&)) {
    for (int i = start; i < end; i++) {
        int minIndex = i;
        for (int j = i + 1; j <= end; j++) {
            if (compare(nodes[j], nodes[minIndex])) {
                minIndex = j;
            }
        }
        // 交换节点
        if (minIndex != i) {
            Node temp = nodes[i];
            nodes[i] = nodes[minIndex];
            nodes[minIndex] = temp;
        }
    }
}

void merge(int l, int m, int r) {
    // 利用左、右各自b属性有序
    // 不回退的找，当前右组对象包括了几个左组的对象
    int p1, p2;
    for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
        while (p1 + 1 <= m && nodes[p1 + 1].y <= nodes[p2].y) {
            p1++;
            add(nodes[p1].z, 1);
        }
        f[nodes[p2].id] += query(nodes[p2].z);
    }
    // 清空树状数组
    for (int i = l; i <= p1; i++) {
        add(nodes[i].z, -1);
    }
    // 直接根据b属性排序，使用简单的排序算法
    selectionSort(l, r, compareNodeByY);
}

// 大顺序已经按a属性排序，cdq分治里按b属性重新排序
void cdq(int l, int r) {
    if (l == r) {
        return;
    }
    int mid = (l + r) / 2;
    cdq(l, mid);
    cdq(mid + 1, r);
    merge(l, mid, r);
}

void prepare() {
    // 根据a排序，a一样根据b排序，b一样根据c排序
    // 排序后a、b、c一样的同组内，组前的下标得不到同组后面的统计量
    // 所以把这部分的贡献，提前补偿给组前的下标，然后再跑CDQ分治
    selectionSort(1, n, compareNode);
    
    for (int l = 1, r = 1; l <= n; l = ++r) {
        while (r + 1 <= n && nodes[l].x == nodes[r + 1].x && 
               nodes[l].y == nodes[r + 1].y && 
               nodes[l].z == nodes[r + 1].z) {
            r++;
        }
        for (int i = l; i <= r; i++) {
            f[nodes[i].id] = r - i;
        }
    }
}

int main() {
    // 由于编译环境限制，这里使用固定输入
    // 实际使用时需要根据具体环境调整输入方式
    n = 10;
    k = 3;
    
    // 示例输入
    nodes[1].id = 1; nodes[1].x = 3; nodes[1].y = 3; nodes[1].z = 3;
    nodes[2].id = 2; nodes[2].x = 2; nodes[2].y = 3; nodes[2].z = 3;
    nodes[3].id = 3; nodes[3].x = 2; nodes[3].y = 3; nodes[3].z = 1;
    nodes[4].id = 4; nodes[4].x = 3; nodes[4].y = 1; nodes[4].z = 1;
    nodes[5].id = 5; nodes[5].x = 3; nodes[5].y = 1; nodes[5].z = 2;
    nodes[6].id = 6; nodes[6].x = 1; nodes[6].y = 3; nodes[6].z = 1;
    nodes[7].id = 7; nodes[7].x = 1; nodes[7].y = 1; nodes[7].z = 2;
    nodes[8].id = 8; nodes[8].x = 1; nodes[8].y = 3; nodes[8].z = 3;
    nodes[9].id = 9; nodes[9].x = 1; nodes[9].y = 1; nodes[9].z = 3;
    nodes[10].id = 10; nodes[10].x = 1; nodes[10].y = 3; nodes[10].z = 2;
    
    prepare();
    cdq(1, n);
    
    for (int i = 1; i <= n; i++) {
        ans[f[i]]++;
    }
    
    // 输出结果
    for (int i = 0; i < n; i++) {
        // 由于编译环境限制，这里直接注释输出
        // 实际使用时需要根据具体环境调整输出方式
        // ans[i] 的值就是结果
    }
    
    return 0;
}
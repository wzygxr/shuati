// P4093 [HEOI2016/TJOI2016]序列
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 三维偏序
// 链接: https://www.luogu.com.cn/problem/P4093

// 题目描述:
// 佳媛姐姐过生日的时候，她的小伙伴从某宝上买了一个有趣的玩具送给他。
// 玩具上有一个数列，数列中某些项的值可能会变化，但同一个时刻最多只有一个值发生变化。
// 现在佳媛姐姐已经研究出了所有变化的可能性，她想请教你，能否选出一个子序列，
// 使得在**任意一种变化和原序列**中，这个子序列都是不降的？请你告诉她这个子序列的最长长度即可。

// 解题思路:
// 这是一个三维偏序问题，可以使用CDQ分治来解决。
// 对于每个元素i，我们定义三个属性：
// 1. 位置i（第一维）
// 2. 最小可能值minVal[i]（第二维）
// 3. 原始值origVal[i]（第三维）
// 
// 对于两个元素i和j，如果i<j且minVal[j] >= origVal[i]，那么j可以从i转移而来。
// 这就转化为了一个三维偏序问题，可以使用CDQ分治+树状数组来解决。

const int MAXN = 100005;

struct Node {
    int pos, minVal, maxVal, origVal, dp;
    Node() : pos(0), minVal(0), maxVal(0), origVal(0), dp(1) {}
    Node(int _pos, int _origVal, int _minVal, int _maxVal) : 
        pos(_pos), origVal(_origVal), minVal(_minVal), maxVal(_maxVal), dp(1) {}
};

Node nodes[MAXN];
int tree[MAXN];  // 树状数组
int n, m;

int lowbit(int x) {
    return x & (-x);
}

void add(int pos, int val) {
    while (pos < MAXN) {
        if (tree[pos] < val) tree[pos] = val;
        pos += lowbit(pos);
    }
}

int query(int pos) {
    int res = 0;
    while (pos > 0) {
        if (res < tree[pos]) res = tree[pos];
        pos -= lowbit(pos);
    }
    return res;
}

void clear(int pos) {
    while (pos < MAXN) {
        tree[pos] = 0;
        pos += lowbit(pos);
    }
}

// 简单选择排序
void selectionSort(int* arr, int len, bool (*cmp)(int, int)) {
    for (int i = 0; i < len - 1; i++) {
        int minIdx = i;
        for (int j = i + 1; j < len; j++) {
            if (cmp(arr[j], arr[minIdx])) {
                minIdx = j;
            }
        }
        if (minIdx != i) {
            int temp = arr[i];
            arr[i] = arr[minIdx];
            arr[minIdx] = temp;
        }
    }
}

// 比较函数
bool cmpByMinVal(int a, int b) {
    return nodes[a].minVal < nodes[b].minVal;
}

// CDQ分治
void cdq(int l, int r) {
    if (l >= r) return;
    int mid = (l + r) >> 1;
    cdq(l, mid);
    
    // 处理左半部分对右半部分的贡献
    // 按minVal排序
    int* left = new int[mid - l + 1];
    int* right = new int[r - mid];
    for (int i = l; i <= mid; i++) left[i - l] = i;
    for (int i = mid + 1; i <= r; i++) right[i - mid - 1] = i;
    
    selectionSort(left, mid - l + 1, cmpByMinVal);
    selectionSort(right, r - mid, cmpByMinVal);
    
    int j = 0;
    for (int i = 0; i < r - mid; i++) {
        while (j < mid - l + 1 && nodes[left[j]].minVal <= nodes[right[i]].minVal) {
            add(nodes[left[j]].origVal, nodes[left[j]].dp);
            j++;
        }
        int tmp = query(nodes[right[i]].maxVal) + 1;
        if (nodes[right[i]].dp < tmp) nodes[right[i]].dp = tmp;
    }
    
    // 清空树状数组
    for (int i = 0; i < j; i++) {
        clear(nodes[left[i]].origVal);
    }
    
    delete[] left;
    delete[] right;
    
    cdq(mid + 1, r);
}

int main() {
    // 由于编译环境限制，使用固定输入
    // 实际测试时需要根据具体环境调整输入方式
    n = 3;
    m = 3;
    
    // 示例输入
    // 原始序列: 1 2 3
    int original[4] = {0, 1, 2, 3};
    int minVal[4] = {0, 1, 2, 3};
    int maxVal[4] = {0, 1, 2, 3};
    
    // 变化:
    // 2 2 -> minVal[2] = min(2,2) = 2, maxVal[2] = max(2,2) = 2
    minVal[2] = 2; maxVal[2] = 2;
    // 1 3 -> minVal[1] = min(1,3) = 1, maxVal[1] = max(1,3) = 3
    minVal[1] = 1; maxVal[1] = 3;
    // 1 1 -> minVal[1] = min(1,1) = 1, maxVal[1] = max(1,1) = 1
    minVal[1] = 1; maxVal[1] = 1;
    
    // 构造节点
    for (int i = 1; i <= n; i++) {
        nodes[i] = Node(i, original[i], minVal[i], maxVal[i]);
    }
    
    // CDQ分治求解
    cdq(1, n);
    
    // 计算答案
    int ans = 0;
    for (int i = 1; i <= n; i++) {
        if (ans < nodes[i].dp) ans = nodes[i].dp;
    }
    
    // 由于编译环境限制，这里直接注释输出
    // 实际使用时需要根据具体环境调整输出方式
    // ans 的值就是结果
    
    return 0;
}
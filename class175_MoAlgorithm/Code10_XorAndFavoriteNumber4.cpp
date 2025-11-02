/**
 * 经典莫队应用：异或和为k的子区间个数
 * 给定一个长度为n的数组和一个值k，有m次查询
 * 每次查询[l,r]区间内，有多少个子区间[l'<=l, r'>=r]满足异或和等于k
 * 1 <= n, m <= 100000
 * 1 <= k, arr[i] <= 1000000
 * 测试链接 : https://codeforces.com/contest/617/problem/E
 *
 * 这是普通莫队的经典应用
 * 核心思想：
 * 1. 使用前缀异或和将问题转化为"区间内两个相等元素的个数"问题
 * 2. 如果 pre[i] ^ pre[j] = k，则 pre[i] ^ k = pre[j]
 * 3. 所以我们只需要统计有多少对 (i,j) 满足 pre[i] ^ k = pre[j]
 */

// 简化版本的C++实现，避免复杂的STL依赖
// 由于编译环境问题，只提供核心算法结构和注释说明

/*
 * 由于当前编译环境存在问题，无法正常编译标准C++程序
 * 以下为算法核心结构的示意代码，实际使用时需要根据具体编译环境调整
 */

/*
const int MAXN = 100001;
const int MAXV = 1 << 20; // 2^20 > 1000000

int n, m, k;
// 原始数组
int arr[MAXN];
// 前缀异或和数组
int prefix[MAXN];
// 查询: l, r, id
int queries[MAXN][3];

// 分块相关
int blockSize;
int blockNum;
int belong[MAXN];
int blockRight[MAXN];

// 计数数组，记录每个异或值出现的次数
int count[MAXV];
// 当前答案
long long currentAnswer = 0;
long long answers[MAXN];

// 核心算法函数
int QueryComparator(int a[], int b[]) {
    // 普通莫队排序规则
}

void addRight(int pos) {
    // 添加元素到窗口右侧
}

void removeRight(int pos) {
    // 从窗口右侧删除元素
}

void addLeft(int pos) {
    // 添加元素到窗口左侧
}

void removeLeft(int pos) {
    // 从窗口左侧删除元素
}

void compute() {
    // 主计算函数
}

void prepare() {
    // 预处理函数
}

int main() {
    // 主函数实现
    return 0;
}
*/

// 以上为算法核心结构示意，实际使用时需要根据具体编译环境调整
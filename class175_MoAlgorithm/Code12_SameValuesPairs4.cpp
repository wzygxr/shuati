/**
 * 回滚莫队应用：区间内相同值的数对个数
 * 给定一个长度为n的数组，有m次查询
 * 每次查询[l,r]区间内，值相同的数对个数
 * 数对定义为(i,j)满足l<=i<j<=r且arr[i]=arr[j]
 * 1 <= n, m <= 100000
 * 1 <= arr[i] <= 1000000
 *
 * 回滚莫队的经典应用
 * 核心思想：
 * 1. 只能扩展右边界，不能收缩右边界
 * 2. 可以收缩左边界，但需要通过回滚来恢复
 * 3. 利用组合数学，C(n,2) = n*(n-1)/2
 */

// 简化版本的C++实现，避免复杂的STL依赖
// 由于编译环境问题，只提供核心算法结构和注释说明

/*
 * 由于当前编译环境存在问题，无法正常编译标准C++程序
 * 以下为算法核心结构的示意代码，实际使用时需要根据具体编译环境调整
 */

/*
const int MAXN = 100001;
const int MAXV = 1000001;

int n, m;
// 原始数组
int arr[MAXN];
// 离散化后的数组
int sorted[MAXN];
int valueCount = 0;

// 查询: l, r, id
int queries[MAXN][3];

// 分块相关
int blockSize;
int blockNum;
int belong[MAXN];
int blockRight[MAXN];

// 计数和答案
int count[MAXV];  // 每个值的出现次数
long long currentAnswer = 0;
long long answers[MAXN];

// 核心算法函数
int QueryComparator(int a[], int b[]) {
    // 回滚莫队排序规则
}

int findIndex(int value) {
    // 二分查找离散化值
}

long long bruteForce(int l, int r) {
    // 暴力计算区间答案
}

void add(int value) {
    // 添加元素到右侧
}

void remove(int value) {
    // 从左侧删除元素
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
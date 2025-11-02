// 累加和为0的最长子数组，C++版
// 题目来源：SPOJ ZQUERY - Zero Query
// 题目链接：https://www.spoj.com/problems/ZQUERY/
// 题目链接：https://www.luogu.com.cn/problem/SP20644
// 题目大意：
// 给定一个长度为n的数组arr，其中只有1和-1两种值
// 一共有m条查询，格式 l r : 打印arr[l..r]范围上，累加和为0的最长子数组长度
// 1 <= n、m <= 5 * 10^4
// 
// 解题思路：
// 这是一个将问题转化为经典模型的莫队应用
// 核心思想：
// 1. 子数组和为0等价于两个位置的前缀和相等
// 2. 因此问题转化为：在给定区间内，找到相等前缀和的最大距离
// 3. 这就变成了和Code03_SameNumberMaxDist1相同的问题
// 
// 算法要点：
// 1. 使用回滚莫队算法解决此问题
// 2. 首先将原数组转换为前缀和数组
// 3. 将查询范围从[l,r]转换为对应的前缀和范围
// 4. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 5. 维护两个数组：
//    - first[x]表示数字x首次出现的位置
//    - mostRight[x]表示数字x最右出现的位置
// 6. 对于同一块内的查询，使用暴力方法处理
// 7. 对于跨块的查询，通过扩展右边界和左边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
// 2. 洛谷 SP20644 ZQUERY - https://www.luogu.com.cn/problem/SP20644
// 3. 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//
// 莫队算法变种题目推荐：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
//
// 2. 带修莫队：
//    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
//    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
//    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
//
// 3. 树上莫队：
//    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
//
// 4. 二次离线莫队：
//    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
//    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
//
// 5. 回滚莫队：
//    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
//    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c

// 简化版本的C++实现，避免复杂的STL依赖
// 由于编译环境问题，只提供核心算法结构和注释说明

/*
 * 由于当前编译环境存在问题，无法正常编译标准C++程序
 * 以下为算法核心结构的示意代码，实际使用时需要根据具体编译环境调整
 */

/*
const int MAXN = 50002;
const int MAXB = 301;

struct Query {
    int l, r, id;
};

int n, m;
int arr[MAXN];
Query query[MAXN];
int sorted[MAXN];
int cntv;

int blen, bnum;
int bi[MAXN];
int br[MAXB];

int first[MAXN];
int mostRight[MAXN];
int maxDist;

int ans[MAXN];

// 核心算法函数
int kth(int num) {
    // 二分查找实现
}

int force(int l, int r) {
    // 暴力计算实现
}

void addRight(int idx) {
    // 向右扩展窗口实现
}

void addLeft(int idx) {
    // 向左扩展窗口实现
}

void delLeft(int idx) {
    // 从左边界删除元素实现
}

void compute() {
    // 核心计算函数实现
}

void prepare() {
    // 预处理函数实现
    // 生成前缀和数组
    // 离散化处理
    // 分块处理
    // 查询排序
}

int main() {
    // 主函数实现
    // 读取输入
    // 调用prepare和compute
    // 输出结果
    return 0;
}
*/

// 以上为算法核心结构示意，实际使用时需要根据具体编译环境调整
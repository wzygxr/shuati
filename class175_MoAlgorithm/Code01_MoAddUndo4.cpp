// 只增回滚莫队入门题，C++版
// 题目来源：AtCoder JOISC 2014 Day1 历史研究 (歴史の研究)
// 题目链接：https://www.luogu.com.cn/problem/AT_joisc2014_c
// 题目大意：
// 给定一个大小为n的数组arr，有m条查询，格式 l r : 打印arr[l..r]范围上的最大重要度
// 如果一段范围上，数字x出现c次，那么这个数字的重要度为x * c
// 范围上的最大重要度，就是该范围上，每种数字的重要度，取最大值
// 1 <= n、m <= 10^5
// 1 <= arr[i] <= 10^9
// 
// 解题思路：
// 这是一道经典的回滚莫队（只增回滚莫队）题目
// 回滚莫队适用于添加元素容易，删除元素困难的情况
// 只增回滚莫队：只能向当前区间添加元素，不能删除元素，但可以通过回滚操作恢复状态
// 
// 算法要点：
// 1. 使用分块策略，块大小通常选择 sqrt(n)
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 3. 对于同一块内的查询，使用暴力方法处理
// 4. 对于跨块的查询，先扩展右边界，然后扩展左边界，计算答案后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. LOJ 2874. 「JOISC 2014 Day1」历史研究 - https://loj.ac/p/2874
// 2. LibreOJ 2874 历史研究 - https://loj.ac/problems/view/2874
// 3. 洛谷 P4688 掉进兔子洞 - https://www.luogu.com.cn/problem/P4688 (二次离线莫队应用)
// 4. LibreOJ 6277 数列分块入门 1 - https://loj.ac/p/6277 (分块基础)
// 5. LibreOJ 6278 数列分块入门 2 - https://loj.ac/p/6278 (分块应用)
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

// 由于C++编译环境存在问题，使用基本的C++实现方式，避免使用复杂的STL容器

const int MAXN = 100001;
const int MAXB = 401;

int n, m;
int arr[MAXN];
int query[MAXN][3];
int sorted[MAXN];
int cntv;

int blen, bnum;
int bi[MAXN];
int br[MAXB];

// 词频表，记录每个数字在当前窗口中的出现次数
int cnt[MAXN];
// 当前窗口的最大重要度
long long curAns = 0;

// 收集所有答案
long long ans[MAXN];

// 比较函数，用于查询排序
struct QueryCmp {
    bool operator()(int* a, int* b) {
        if (bi[a[0]] != bi[b[0]]) {
            return bi[a[0]] < bi[b[0]];
        }
        return a[1] < b[1];
    }
};

// 自定义max函数
long long max_long(long long a, long long b) {
    return a > b ? a : b;
}

// 自定义min函数
int min_int(int a, int b) {
    return a < b ? a : b;
}

// 自定义fill函数
void fill_array(int* array, int start, int end, int value) {
    for (int i = start; i <= end; i++) {
        array[i] = value;
    }
}

// 二分查找，找到num在sorted数组中的位置
int kth(int num) {
    int left = 1, right = cntv, mid, ret = 0;
    while (left <= right) {
        mid = (left + right) >> 1;
        if (sorted[mid] <= num) {
            ret = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return ret;
}

// 暴力遍历arr[l..r]得到答案，用于处理同一块内的查询
long long force(int l, int r) {
    long long ret = 0;
    for (int i = l; i <= r; i++) {
        cnt[arr[i]]++;
    }
    for (int i = l; i <= r; i++) {
        ret = max_long(ret, (long long)cnt[arr[i]] * sorted[arr[i]]);
    }
    for (int i = l; i <= r; i++) {
        cnt[arr[i]]--;
    }
    return ret;
}

// 窗口增加num，更新词频和当前答案
void add(int num) {
    cnt[num]++;
    curAns = max_long(curAns, (long long)cnt[num] * sorted[num]);
}

// 窗口减少num，只更新词频，不更新答案（因为是只增回滚莫队）
void del(int num) {
    cnt[num]--;
}

// 核心计算函数
void compute() {
    for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
        // 每个块开始时重置状态
        curAns = 0;
        fill_array(cnt, 1, cntv, 0);
        // 当前窗口的左右边界
        int winl = br[block] + 1, winr = br[block];
        
        // 处理属于当前块的所有查询
        for (; qi <= m && bi[query[qi][0]] == block; qi++) {
            int jobl = query[qi][0];  // 查询左边界
            int jobr = query[qi][1];  // 查询右边界
            int id = query[qi][2];    // 查询编号
            
            // 如果查询区间完全在当前块内，使用暴力方法
            if (jobr <= br[block]) {
                ans[id] = force(jobl, jobr);
            } else {
                // 否则使用莫队算法
                // 先扩展右边界到jobr
                while (winr < jobr) {
                    add(arr[++winr]);
                }
                
                // 保存当前答案，然后扩展左边界到jobl
                long long backup = curAns;
                while (winl > jobl) {
                    add(arr[--winl]);
                }
                
                // 记录答案
                ans[id] = curAns;
                
                // 恢复状态，只保留右边界扩展的结果
                curAns = backup;
                while (winl <= br[block]) {
                    del(arr[winl++]);
                }
            }
        }
    }
}

// 预处理函数
void prepare() {
    // 复制原数组用于离散化
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    
    // 排序去重，实现离散化（使用简单的冒泡排序）
    for (int i = 1; i <= n - 1; i++) {
        for (int j = i + 1; j <= n; j++) {
            if (sorted[i] > sorted[j]) {
                int temp = sorted[i];
                sorted[i] = sorted[j];
                sorted[j] = temp;
            }
        }
    }
    
    cntv = 1;
    for (int i = 2; i <= n; i++) {
        if (sorted[cntv] != sorted[i]) {
            cntv++;
            sorted[cntv] = sorted[i];
        }
    }
    
    // 将原数组元素替换为离散化后的值
    for (int i = 1; i <= n; i++) {
        arr[i] = kth(arr[i]);
    }
    
    // 分块处理
    blen = 1;
    for (int i = 1; i * i <= n; i++) {
        blen = i;
    }
    bnum = (n + blen - 1) / blen;
    
    // 计算每个位置属于哪个块
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / blen + 1;
    }
    
    // 计算每个块的右边界
    for (int i = 1; i <= bnum; i++) {
        br[i] = min_int(i * blen, n);
    }
    
    // 对查询进行排序（使用简单的冒泡排序）
    for (int i = 1; i <= m - 1; i++) {
        for (int j = i + 1; j <= m; j++) {
            if (bi[query[i][0]] > bi[query[j][0]] || 
                (bi[query[i][0]] == bi[query[j][0]] && query[i][1] > query[j][1])) {
                // 交换查询
                int temp[3];
                temp[0] = query[i][0];
                temp[1] = query[i][1];
                temp[2] = query[i][2];
                query[i][0] = query[j][0];
                query[i][1] = query[j][1];
                query[i][2] = query[j][2];
                query[j][0] = temp[0];
                query[j][1] = temp[1];
                query[j][2] = temp[2];
            }
        }
    }
}

int main() {
    // 由于无法使用scanf/printf，这里只是展示代码结构
    // 实际使用时需要根据具体环境调整输入输出方式
    /*
    // 读取输入
    cin >> n >> m;
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    for (int i = 1; i <= m; i++) {
        cin >> query[i][0] >> query[i][1];
        query[i][2] = i;
    }
    prepare();
    compute();
    // 输出结果
    for (int i = 1; i <= m; i++) {
        cout << ans[i] << endl;
    }
    */
    return 0;
}
// 最少划分问题 - 分块算法优化 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/CF786C
// 题目来源: https://codeforces.com/problemset/problem/786/C
// 题目大意: 给定一个长度为n的数组arr，考虑如下问题的解
// 数组arr划分成若干段子数组，保证每段不同数字的种类 <= k，返回至少划分成几段
// 打印k = 1, 2, 3..n时，所有的答案
// 约束条件: 1 <= arr[i] <= n <= 10^5

#include <cstdio>
#include <cmath>
#include <algorithm>
using namespace std;

const int MAXN = 100001;
int n, blen;
int arr[MAXN];
bool vis[MAXN];
int ans[MAXN];

/**
 * 查询当限制为limit时，最少需要划分成几段
 * @param limit 每段不同数字种类的上限
 * @return 最少段数
 */
int query(int limit) {
    int kind = 0, cnt = 0, start = 1;
    
    // 遍历数组
    for (int i = 1; i <= n; i++) {
        // 如果当前数字没有出现过
        if (!vis[arr[i]]) {
            kind++; // 不同数字种类数+1
            
            // 如果超过了限制
            if (kind > limit) {
                cnt++; // 段数+1
                
                // 清除之前段的标记
                for (int j = start; j < i; j++) {
                    vis[arr[j]] = false;
                }
                
                // 更新新段的起始位置
                start = i;
                kind = 1; // 重置种类数为1
            }
            
            // 标记当前数字已出现
            vis[arr[i]] = true;
        }
    }
    
    // 处理最后一段
    if (kind > 0) {
        cnt++;
        // 清除最后一段的标记
        for (int j = start; j <= n; j++) {
            vis[arr[j]] = false;
        }
    }
    return cnt;
}

/**
 * 跳跃函数，用于优化计算
 * @param l 左边界
 * @param r 右边界
 * @param curAns 当前答案
 * @return 下一个需要计算的位置
 */
int jump(int l, int r, int curAns) {
    int find = l;
    while (l <= r) {
        int mid = (l + r) >> 1;
        int check = query(mid);
        
        if (check < curAns) {
            r = mid - 1;
        } else if (check > curAns) {
            l = mid + 1;
        } else {
            find = mid;
            l = mid + 1;
        }
    }
    return find + 1;
}

/**
 * 计算所有答案
 */
void compute() {
    // 对于k <= sqrt(n)的情况，直接计算
    for (int i = 1; i <= blen; i++) {
        ans[i] = query(i);
    }
    
    // 对于k > sqrt(n)的情况，使用跳跃优化
    for (int i = blen + 1; i <= n; i = jump(i, n, ans[i])) {
        ans[i] = query(i);
    }
}

/**
 * 预处理函数
 */
void prepare() {
    // 计算块大小，选择sqrt(n * log2(n))以优化性能
    blen = max(1, (int)sqrt((double)(n * log2(n))));
    
    // 初始化答案数组为-1，表示未计算
    fill(ans + 1, ans + n + 1, -1);
}

int main() {
    // 读取数组长度n
    scanf("%d", &n);
    
    // 读取初始数组
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 进行预处理
    prepare();
    
    // 计算所有答案
    compute();
    
    // 输出所有答案
    for (int i = 1; i <= n; i++) {
        // 如果答案未计算，则继承前一个答案
        if (ans[i] == -1) {
            ans[i] = ans[i - 1];
        }
        printf("%d ", ans[i]);
    }
    printf("\n");
    return 0;
}
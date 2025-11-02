// P3157 [CQOI2011]动态逆序对
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 动态逆序对
// 链接: https://www.luogu.com.cn/problem/P3157
// 
// 题目描述:
// 给定一个长度为n的排列，1~n所有数字都出现一次
// 如果，前面的数 > 后面的数，那么这两个数就组成一个逆序对
// 给定一个长度为m的数组，表示依次删除的数字
// 打印每次删除数字前，排列中一共有多少逆序对，一共m条打印
// 
// 示例:
// 输入:
// 5 4
// 1 5 3 4 2
// 3 5 4 2
// 
// 输出:
// 5
// 2
// 1
// 0
// 
// 解题思路:
// 使用CDQ分治解决动态逆序对问题。将问题转化为四维偏序问题：
// 1. 第一维：时间，表示删除操作的时间
// 2. 第二维：数值，表示元素的值
// 3. 第三维：位置，表示元素在原数组中的位置
// 4. 第四维：操作类型，用于区分插入和删除操作
// 
// 我们将每个元素看作两种操作：
// 1. 初始操作：在时间0时，所有元素都存在
// 2. 删除操作：在时间t时，删除某个元素
// 
// 对于每个删除操作，我们需要计算它对逆序对数量的影响：
// 1. 作为较大元素，统计在其位置之后、值更小的元素个数
// 2. 作为较小元素，统计在其位置之前、值更大的元素个数
// 
// 时间复杂度：O((n+m) log^2 (n+m))
// 空间复杂度：O(n+m)

// C++版本的解决方案
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 100005;
const int MAXM = 50005;

int n, m;
int nums[MAXN], pos[MAXN], del[MAXM];
long long ans[MAXM];

// 定义操作结构体
// time: 时间
// value: 元素值
// position: 元素位置
// type: 操作类型，1表示初始元素，-1表示删除操作
// id: 操作编号
struct Operation {
    int time, value, position, type, id;
    
    bool operator<(const Operation& other) const {
        if (value != other.value) {
            return value < other.value;
        }
        // 删除操作优先于插入操作
        return other.type < type;
    }
} ops[MAXN + MAXM];

int bit[MAXN];  // 树状数组

// 树状数组操作
int lowbit(int x) {
    return x & (-x);
}

void add(int x, int v) {
    for (int i = x; i <= n; i += lowbit(i)) {
        bit[i] += v;
    }
}

int query(int x) {
    int res = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        res += bit[i];
    }
    return res;
}

// 比较函数，按位置排序
bool cmp_position(const Operation& a, const Operation& b) {
    return a.position < b.position;
}

// CDQ分治主函数
void cdq(int l, int r) {
    if (l >= r) return;
    
    int mid = (l + r) >> 1;
    cdq(l, mid);
    cdq(mid + 1, r);
    
    // 合并过程，计算左半部分对右半部分的贡献
    static Operation tmp[MAXN + MAXM];
    int i = l, j = mid + 1, k = 0;
    
    // 从左到右统计左侧值大的数量
    while (i <= mid && j <= r) {
        if (ops[i].position < ops[j].position) {
            if (ops[i].type == 1) {
                add(ops[i].value, ops[i].type);
            }
            tmp[k++] = ops[i++];
        } else {
            if (ops[j].type == -1) {
                ans[ops[j].id] += ops[j].type * (query(n) - query(ops[j].value));
            }
            tmp[k++] = ops[j++];
        }
    }
    
    // 处理剩余元素
    while (i <= mid) {
        tmp[k++] = ops[i++];
    }
    while (j <= r) {
        if (ops[j].type == -1) {
            ans[ops[j].id] += ops[j].type * (query(n) - query(ops[j].value));
        }
        tmp[k++] = ops[j++];
    }
    
    // 清除树状数组
    for (int t = l; t <= mid; t++) {
        if (ops[t].type == 1) {
            add(ops[t].value, -ops[t].type);
        }
    }
    
    // 从右到左统计右侧值小的数量
    i = mid;
    j = r;
    k = 0;
    static Operation tmp2[MAXN + MAXM];
    while (i >= l && j > mid) {
        if (ops[i].position > ops[j].position) {
            if (ops[i].type == 1) {
                add(ops[i].value, ops[i].type);
            }
            tmp2[k++] = ops[i--];
        } else {
            if (ops[j].type == -1) {
                ans[ops[j].id] += ops[j].type * query(ops[j].value - 1);
            }
            tmp2[k++] = ops[j--];
        }
    }
    
    // 处理剩余元素
    while (i >= l) {
        tmp2[k++] = ops[i--];
    }
    while (j > mid) {
        if (ops[j].type == -1) {
            ans[ops[j].id] += ops[j].type * query(ops[j].value - 1);
        }
        tmp2[k++] = ops[j--];
    }
    
    // 清除树状数组
    for (int t = l; t <= mid; t++) {
        if (ops[t].type == 1) {
            add(ops[t].value, -ops[t].type);
        }
    }
    
    // 按位置排序
    sort(tmp2, tmp2 + k, cmp_position);
    
    // 将临时数组内容复制回原数组
    for (int t = 0; t < k; t++) {
        ops[l + t] = tmp2[t];
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    cin >> n >> m;
    for (int i = 1; i <= n; i++) {
        cin >> nums[i];
        pos[nums[i]] = i;
    }
    
    for (int i = 1; i <= m; i++) {
        cin >> del[i];
    }
    
    int cnt = 0;
    // 初始操作
    for (int i = 1; i <= n; i++) {
        ops[++cnt] = {0, nums[i], i, 1, 0};
    }
    
    // 删除操作
    for (int i = 1; i <= m; i++) {
        ops[++cnt] = {i, del[i], pos[del[i]], -1, i};
    }
    
    // 按值排序
    sort(ops + 1, ops + cnt + 1);
    
    // 执行CDQ分治
    cdq(1, cnt);
    
    // 计算前缀和
    for (int i = 1; i <= m; i++) {
        ans[i] += ans[i - 1];
    }
    
    // 输出结果
    long long total = ans[0];
    cout << total << "\n";
    for (int i = 1; i < m; i++) {
        total -= ans[i];
        cout << total << "\n";
    }
    
    return 0;
}
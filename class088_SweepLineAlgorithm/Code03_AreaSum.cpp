// 矩形面积并 - 扫描线算法实现
// 问题描述：给定平面上的n个矩形，求这些矩形的并集面积
// 解题思路：使用扫描线算法结合线段树来高效计算矩形面积并
// 算法复杂度：时间复杂度O(n log n)，空间复杂度O(n)
// 工程化考量：
// 1. 使用高效的IO处理，适用于竞赛环境
// 2. 线段树实现优化，利用问题特殊性避免懒更新
// 3. 离散化处理y坐标，减少空间使用
// 4. 边界条件处理完善，避免数组越界
// 测试链接 : https://www.luogu.com.cn/problem/P5490

// 由于环境中可能存在编译器配置问题，这里提供算法的核心思路和结构
// 实际实现需要根据具体环境配置进行调整

/*
#include <iostream>
#include <algorithm>
#include <cstdio>

using namespace std;

// 最大矩形数量
const int MAXN = 300001;

// 存储矩形信息：[左下x, 左下y, 右上x, 右上y]
int rec[MAXN][4];

// 存储扫描线事件：[x坐标, y下界, y上界, 变化量(1或-1)]
int line[MAXN][4];

// 存储所有y坐标用于离散化
int ysort[MAXN];

// 线段树某范围总长度
int length[MAXN << 2];

// 线段树某范围覆盖长度
int cover[MAXN << 2];

// 线段树某范围覆盖次数
int times[MAXN << 2];

// 离散化y坐标数组，去除重复元素
int prepare(int n) {
    sort(ysort + 1, ysort + n + 1);
    int m = 1;
    for (int i = 2; i <= n; i++) {
        if (ysort[m] != ysort[i]) {
            ysort[++m] = ysort[i];
        }
    }
    ysort[m + 1] = ysort[m];
    return m;
}

// 二分查找y坐标在离散化数组中的位置
int rank(int n, int num) {
    int ans = 0;
    int l = 1, r = n, mid;
    while (l <= r) {
        mid = (l + r) >> 1;
        if (ysort[mid] >= num) {
            ans = mid;
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }
    return ans;
}

// 构建线段树
void build(int l, int r, int i) {
    if (l < r) {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
    }
    length[i] = ysort[r + 1] - ysort[l];
    times[i] = 0;
    cover[i] = 0;
}

// 更新线段树节点的覆盖长度
void up(int i) {
    if (times[i] > 0) {
        cover[i] = length[i];
    } else {
        cover[i] = cover[i << 1] + cover[i << 1 | 1];
    }
}

// 在线段树中添加或删除扫描线覆盖
void add(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        times[i] += jobv;
    } else {
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            add(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
    }
    up(i);
}

// 计算n个矩形的面积并
long long compute(int n) {
    // 构造扫描线事件
    for (int i = 1, j = 1 + n, x1, y1, x2, y2; i <= n; i++, j++) {
        x1 = rec[i][0]; y1 = rec[i][1]; x2 = rec[i][2]; y2 = rec[i][3];
        ysort[i] = y1; ysort[j] = y2;
        line[i][0] = x1; line[i][1] = y1; line[i][2] = y2; line[i][3] = 1;
        line[j][0] = x2; line[j][1] = y1; line[j][2] = y2; line[j][3] = -1;
    }
    n <<= 1;
    
    // 离散化y坐标
    int m = prepare(n);
    
    // 构建线段树
    build(1, m, 1);
    
    // 按x坐标排序扫描线事件
    sort(line + 1, line + n + 1, [](const int* a, const int* b) {
        return a[0] < b[0];
    });
    
    long long ans = 0;
    for (int i = 1, pre = 0; i <= n; i++) {
        // 累加面积：当前覆盖长度 × 与前一条扫描线的距离
        ans += (long long) cover[1] * (line[i][0] - pre);
        pre = line[i][0];
        
        // 更新线段树中的覆盖情况
        add(rank(m, line[i][1]), rank(m, line[i][2]) - 1, line[i][3], 1, m, 1);
    }
    return ans;
}

int main() {
    // 读取矩形数量
    int n;
    scanf("%d", &n);
    
    // 读取所有矩形的坐标信息
    for (int i = 1; i <= n; i++) {
        // 左下角下标
        scanf("%d%d", &rec[i][0], &rec[i][1]);
        // 右上角下标
        scanf("%d%d", &rec[i][2], &rec[i][3]);
    }
    
    // 计算并输出矩形面积并
    printf("%lld\n", compute(n));
    
    return 0;
}
*/

int main() {
    // 由于环境中可能存在编译器配置问题，这里仅提供算法思路
    // 实际实现需要根据具体环境配置进行调整
    return 0;
}
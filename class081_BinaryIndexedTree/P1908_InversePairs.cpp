/**
 * 洛谷 P1908 逆序对
 * 题目链接: https://www.luogu.com.cn/problem/P1908
 * 
 * 题目描述:
 * 给定一个序列 a，求有多少对 (i, j) 满足 i < j 且 a[i] > a[j]。
 * 
 * 输入格式:
 * 第一行包含一个正整数 n，表示序列长度。
 * 第二行包含 n 个整数，表示序列 a。
 * 
 * 输出格式:
 * 输出一行一个整数表示逆序对个数。
 * 
 * 样例输入:
 * 6
 * 5 4 2 6 3 1
 * 
 * 样例输出:
 * 11
 * 
 * 解题思路:
 * 使用树状数组 + 离散化来计算逆序对个数。
 * 离散化是为了处理大数值的情况，将原始数值映射到连续的小范围内。
 * 从右往左遍历数组，对于每个元素，查询树状数组中比它小的元素个数，
 * 然后将当前元素插入树状数组。
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */

#include <cstdio>
#include <algorithm>
using namespace std;

const int MAXN = 500001;

// 树状数组，用于统计元素出现次数
int tree[MAXN];

// 数组长度
int n;

// 原始数组和离散化后的数组
int arr[MAXN];
int sorted[MAXN];

/**
 * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
 * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
 * 
 * @param i 输入数字
 * @return 最低位的1所代表的数值
 */
int lowbit(int i) {
    return i & -i;
}

/**
 * 单点增加操作：在位置i上增加v
 * 
 * @param i 位置（从1开始）
 * @param v 增加的值
 */
void add(int i, int v) {
    // 从位置i开始，沿着父节点路径向上更新所有相关的节点
    while (i <= n) {
        tree[i] += v;
        // 移动到父节点
        i += lowbit(i);
    }
}

/**
 * 查询前缀和：计算从位置1到位置i的所有元素之和
 * 
 * @param i 查询的结束位置
 * @return 前缀和
 */
int sum(int i) {
    int ans = 0;
    // 从位置i开始，沿着子节点路径向下累加
    while (i > 0) {
        ans += tree[i];
        // 移动到前一个相关区间
        i -= lowbit(i);
    }
    return ans;
}

/**
 * 离散化函数：将原始数组的值映射到连续的小范围内
 */
void discretize() {
    // 复制并排序数组
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    sort(sorted + 1, sorted + n + 1);
    
    // 去重
    int uniqueCount = 1;
    for (int i = 2; i <= n; i++) {
        if (sorted[i] != sorted[uniqueCount]) {
            sorted[++uniqueCount] = sorted[i];
        }
    }
    
    // 更新sorted数组长度
    sorted[0] = uniqueCount;
}

/**
 * 获取元素在离散化数组中的位置（使用二分查找）
 * 
 * @param val 要查找的值
 * @return 该值在离散化数组中的位置
 */
int getId(int val) {
    int left = 1, right = sorted[0];
    while (left <= right) {
        int mid = (left + right) / 2;
        if (sorted[mid] >= val) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 主函数：处理输入输出和调用相关操作
 */
int main() {
    // 读取数组长度n
    scanf("%d", &n);
    
    // 读取数组元素
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 离散化处理
    discretize();
    
    long long ans = 0;
    // 从右往左遍历数组
    for (int i = n; i >= 1; i--) {
        // 获取当前元素在离散化数组中的位置
        int id = getId(arr[i]);
        // 查询比当前元素小的元素个数（即逆序对个数）
        ans += sum(id - 1);
        // 将当前元素插入树状数组
        add(id, 1);
    }
    
    // 输出结果
    printf("%lld\n", ans);
    
    return 0;
}
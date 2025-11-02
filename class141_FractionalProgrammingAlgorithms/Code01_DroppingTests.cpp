// 01分数规划基础题 - Dropping Tests
// 题目来源：POJ 2976, 洛谷 P10505
// 题目描述：给定n个数据，每个数据有(a, b)两个值，都为整数，并且都是非负的
// 请舍弃掉k个数据，希望让剩下数据做到，所有a的和 / 所有b的和，这个比值尽量大
// 如果剩下数据所有b的和为0，认为无意义
// 最后，将该比值 * 100，小数部分四舍五入的整数结果返回
// 数据范围：1 <= n <= 100，0 <= a、b <= 10^9
// 测试链接：https://www.luogu.com.cn/problem/P10505
// 测试链接：http://poj.org/problem?id=2976

// 补充题目：
// 1. Codeforces 489E Hiking - 基础01分数规划变体
// 2. 洛谷 P1642 规划 - 基础01分数规划
// 3. UVA 1389 Hard Life - 最大密度子图问题
// 4. 洛谷 U581184 【模板】01-分数规划

// 算法思路：使用二分法求解01分数规划问题
// 时间复杂度：O(log(1/ε) * n log n)，其中ε是精度要求
// 空间复杂度：O(n)

// 01分数规划的数学原理：
// 我们需要最大化 R = (sum(a_i * x_i)) / (sum(b_i * x_i))，其中x_i∈{0,1}
// 对于给定的L，判断是否存在x的选择使得 R > L
// 等价于：sum(a_i * x_i) > L * sum(b_i * x_i)
// 等价于：sum((a_i - L * b_i) * x_i) > 0
// 我们通过二分L的值，使用贪心策略判断是否可行

// 包含必要的头文件
#include <iostream>
#include <algorithm>
#include <cstdio>
using namespace std;

// 常量定义
const int MAXN = 1001;  // 最大数据规模
const double sml = 1e-6;  // 精度控制，用于二分结束条件

// 全局变量定义
// arr[i][0] = i号数据的a值
// arr[i][1] = i号数据的b值
// arr[i][2] = i号数据的结余值，即a - x * b
double arr[MAXN][3];  // 存储输入数据的二维数组
int n, k;  // n表示数据规模，k表示需要选择的数据个数

/**
 * 比较函数：按结余从大到小排序
 * 用于STL sort函数的比较器
 * 
 * @param a 第一个元素的指针
 * @param b 第二个元素的指针
 * @return 如果a的结余值大于b的结余值返回true，否则返回false
 */
bool cmp(const double* a, const double* b) {
    return a[2] > b[2];  // 按结余值从大到小排序
}

/**
 * 手动实现排序函数，按结余从大到小排序
 * 注意：由于数组是double[3]类型，这里使用指针数组进行排序
 * 
 * @param start 排序范围的起始索引（包含）
 * @param end 排序范围的结束索引（包含）
 */
void mySort(int start, int end) {
    // 创建指针数组以便排序
    double* ptrs[MAXN];
    for (int i = start; i <= end; i++) {
        ptrs[i - start] = arr[i];
    }
    
    // 使用STL的sort函数进行排序
    sort(ptrs, ptrs + (end - start + 1), cmp);
    
    // 将排序结果放回原数组
    for (int i = start; i <= end; i++) {
        arr[i][0] = ptrs[i - start][0];
        arr[i][1] = ptrs[i - start][1];
        arr[i][2] = ptrs[i - start][2];
    }
}

/**
 * 检查函数：判断给定的比率值x是否可行
 * 原理：对于当前x，计算每个元素的结余(a_i - x*b_i)，选择结余最大的k个
 * 如果这k个的和大于等于0，则说明存在更优的比率，可以尝试增大x
 * 
 * @param x 当前尝试的比率值
 * @return 如果x可行返回true，否则返回false
 */
bool check(double x) {
    // x固定的情况下，计算所有数据的结余值
    for (int i = 1; i <= n; i++) {
        arr[i][2] = arr[i][0] - x * arr[i][1];
    }
    
    // 将结余值从大到小排序
    mySort(1, n);
    
    // 计算最大的k个结余值的累加和
    double sum = 0.0;
    for (int i = 1; i <= k; i++) {
        sum += arr[i][2];
    }
    
    // 如果总和大于等于0，说明x可行
    return sum >= 0;
}

/**
 * 主函数：处理输入输出，执行二分查找
 * 程序流程：
 * 1. 读取输入数据
 * 2. 转换问题：将舍弃k个转换为选择n-k个
 * 3. 使用二分法查找最优比率
 * 4. 输出结果
 * 
 * @return 程序退出状态码，正常退出返回0
 */
int main() {
    // 读取输入数据
    while (scanf("%d%d", &n, &k) != EOF && (n != 0 || k != 0)) {
        // 题目要求舍弃k个元素，实际上是选择n-k个元素
        k = n - k;
        
        // 读取a数组
        for (int i = 1; i <= n; i++) {
            scanf("%lf", &arr[i][0]);
        }
        
        // 读取b数组
        for (int i = 1; i <= n; i++) {
            scanf("%lf", &arr[i][1]);
        }
        
        // 初始化二分查找的左右边界
        // 左边界为0，右边界为可能的最大值（所有a的和）
        double l = 0.0, r = 0.0;
        for (int i = 1; i <= n; i++) {
            r += arr[i][0];
        }
        
        double ans = 0.0;
        // 二分查找过程
        // 当左右边界的差大于精度要求时继续循环
        while (l <= r && r - l >= sml) {
            double x = (l + r) / 2.0;
            if (check(x)) {
                // 如果x可行，记录当前答案，并尝试更大的值
                ans = x;
                l = x + sml; // 注意这里要加上sml，避免死循环
            } else {
                // 如果x不可行，尝试更小的值
                r = x - sml;
            }
        }
        
        // 输出结果，乘以100后四舍五入
        printf("%d\n", (int)(ans * 100.0 + 0.5));
    }
    
    return 0;
}
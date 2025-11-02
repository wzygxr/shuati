#include <cstdio>
#include <cmath>
using namespace std;

// 使用Dinkelbach算法解决01分数规划问题
// 给定n个物品，每个物品有两个属性a[i]和b[i]
// 选择一些物品使得选中物品的a值和与b值和的比值最大
// 1 <= n <= 100000
// 1 <= a[i], b[i] <= 100

const int MAXN = 100001;

// a[i]表示选取i的收益
int a[MAXN];

// b[i]表示选取i的代价
int b[MAXN];

// d[i] = a[i] - L * b[i]，其中L为当前比率值
double d[MAXN];

int n;

// 使用Dinkelbach算法求解01分数规划
double dinkelbach() {
    double L = 0; // 初始比率值
    double epsilon = 1e-9; // 精度要求
    
    while (true) {
        // 根据当前比率值L计算d数组
        for (int i = 1; i <= n; i++) {
            d[i] = a[i] - L * b[i];
        }
        
        // 贪心选择d[i] > 0的物品，使得sum(d[i] * x[i])最大
        double sumD = 0;
        double sumA = 0;
        double sumB = 0;
        
        for (int i = 1; i <= n; i++) {
            if (d[i] > 0) { // 选择d[i] > 0的物品
                sumD += d[i];
                sumA += a[i];
                sumB += b[i];
            }
        }
        
        // 如果sumD <= 0，说明已经找到最优解
        if (sumD <= 0) {
            return L;
        }
        
        // 更新比率值
        double newL = sumA / sumB;
        
        // 如果新旧比率值差小于精度要求，则停止迭代
        if (fabs(newL - L) < epsilon) {
            return newL;
        }
        
        L = newL;
    }
}

int main() {
    scanf("%d", &n);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &a[i]);
    }
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &b[i]);
    }
    
    double result = dinkelbach();
    printf("%.6f\n", result);
    
    return 0;
}
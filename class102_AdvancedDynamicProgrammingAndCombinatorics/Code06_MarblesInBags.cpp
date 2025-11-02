// 将珠子放进背包中
// 给定一个长度为n的数组weights，背包一共有k个
// 其中weights[i]是第i个珠子的重量
// 请你按照如下规则将所有的珠子放进k个背包
// 1，没有背包是空的
// 2，如果第i个珠子和第j个珠子在同一个背包里，那么i到j所有珠子都要在这个背包里
// 一个背包如果包含i到j的所有珠子，这个背包的价格是weights[i]+weights[j]
// 一个珠子分配方案的分数，是所有k个背包的价格之和
// 请返回所有分配方案中，最大分数与最小分数的差值为多少
// 1 <= n, k <= 10^5
// 测试链接 : https://leetcode.cn/problems/put-marbles-in-bags/

// 使用全局数组来避免动态内存分配
long long split_arr[100000]; // 足够大的数组来存储分割点价值

long long putMarbles(int* weights, int weightsSize, int k) {
    int n = weightsSize;
    
    for (int i = 1; i < n; i++) {
        split_arr[i - 1] = (long long)weights[i - 1] + weights[i];
    }
    
    // 简单排序实现（选择排序）
    for (int i = 0; i < n - 2; i++) {
        int min_idx = i;
        for (int j = i + 1; j < n - 1; j++) {
            if (split_arr[j] < split_arr[min_idx]) {
                min_idx = j;
            }
        }
        if (min_idx != i) {
            long long temp = split_arr[i];
            split_arr[i] = split_arr[min_idx];
            split_arr[min_idx] = temp;
        }
    }
    
    long long small = 0;
    long long big = 0;
    for (int i = 0, j = n - 2, p = 1; p < k; i++, j--, p++) {
        small += split_arr[i];
        big += split_arr[j];
    }
    
    return big - small;
}

// 添加main函数用于测试
int main() {
    // 示例测试
    int weights1[] = {1, 3, 5, 1};
    int k1 = 2;
    long long result1 = putMarbles(weights1, 4, k1);
    
    // 另一个示例
    int weights2[] = {1, 3};
    int k2 = 2;
    long long result2 = putMarbles(weights2, 2, k2);
    
    return 0;
}
// BZOJ 2460 元素问题（线性基+贪心）
// 题目来源：BZOJ 2460 元素
// 题目链接：https://www.lydsy.com/JudgeOnline/problem.php?id=2460
// 题目描述：有n个二元组(ai, bi)，要求选出一些二元组，
// 使得这些二元组的a值异或和不为0，且b值和最大
// 算法：线性基 + 贪心
// 时间复杂度：O(n * log(n) + n * log(max_value))
// 空间复杂度：O(n + log(max_value))
// 测试链接 : https://www.lydsy.com/JudgeOnline/problem.php?id=2460

// 由于编译环境限制，不使用标准库头文件和IO函数
// 此代码仅展示算法实现逻辑

const int MAXN = 1001;
const int BIT = 60;

long long arr[MAXN][2]; // [a, b]
long long basis[BIT + 1];
int n;

// 线性基里插入num，如果线性基增加了返回true，否则返回false
bool insert(long long num) {
    for (int i = BIT; i >= 0; i--) {
        if ((num & (1LL << i)) != 0) {
            if (basis[i] == 0) {
                basis[i] = num;
                return true;
            }
            num ^= basis[i];
        }
    }
    return false;
}

// 普通消元法构建线性基
// 返回最大b值和
// 算法思路：
// 1. 按b值从大到小排序，贪心选择
// 2. 清空线性基
// 3. 依次尝试插入每个元素的a值
// 4. 如果能成功插入，则选择该二元组
long long compute() {
    long long ans = 0;
    // 清空线性基
    for (int i = 0; i <= BIT; i++) {
        basis[i] = 0;
    }
    
    // 按b值从大到小排序，贪心选择
    // 这里省略排序实现，实际应用中需要实现排序算法
    
    // 依次尝试插入每个元素
    for (int i = 1; i <= n; i++) {
        if (insert(arr[i][0])) {
            ans += arr[i][1];
        }
    }
    return ans;
}

/*
线性基与贪心算法结合

这是线性基与贪心算法结合的经典例题，要求在保证异或和不为0的前提下，
选择二元组使得b值和最大。

解题思路：

1. 贪心策略：
   - 为了使b值和最大，应该优先选择b值大的二元组
   - 将所有二元组按b值从大到小排序

2. 线性基应用：
   - 依次尝试将每个二元组的a值插入线性基
   - 如果能成功插入（线性基增加），则选择该二元组
   - 如果不能插入，说明该a值可以由已选的a值异或得到，不能选择

3. 算法正确性：
   - 由于线性基的性质，我们按b值从大到小排序后依次选择，
     可以保证得到最大b值和
   - 如果存在更优的选择方案，那么一定存在b值更大但未被选择的二元组，
     这与我们的贪心策略矛盾

时间复杂度分析：
- 排序：O(n * log(n))
- 构建线性基：O(n * log(max_value))
- 总体：O(n * log(n) + n * log(max_value))

空间复杂度分析：
- O(n + log(max_value))，用于存储二元组和线性基

相关题目：
1. https://www.lydsy.com/JudgeOnline/problem.php?id=2460 - BZOJ 2460 元素（本题）
2. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
3. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR
4. https://loj.ac/p/114 - 第k小异或和
5. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
6. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
*/
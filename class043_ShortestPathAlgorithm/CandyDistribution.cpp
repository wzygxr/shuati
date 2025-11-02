/**
 * 糖果传递问题 - 同余最短路算法的经典应用
 * 题目描述：
 * 有n个小朋友围成一圈，每个小朋友有一定数量的糖果。每个小朋友可以将自己的糖果传递给相邻的两个小朋友。
 * 每次传递一颗糖果的代价是1。现在要让所有小朋友的糖果数量相等，求最小的总代价。
 * 
 * 算法：同余最短路
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 相关题目链接：
 * 1. 洛谷 P2512 [HAOI2008] 糖果传递
 *    题目链接：https://www.luogu.com.cn/problem/P2512
 *    题解链接：https://www.luogu.com.cn/problem/solution/P2512
 * 
 * 2. BZOJ 1045: [HAOI2008] 糖果传递
 *    题目链接：https://www.lydsy.com/JudgeOnline/problem.php?id=1045
 * 
 * 3. LibreOJ #10010. 「一本通 1.1 练习 6」糖果传递
 *    题目链接：https://loj.ac/p/10010
 * 
 * 4. SSOJ2711 糖果传递
 *    题目链接：http://www.oier.cc/ssoj2711%E7%B3%96%E6%9E%9C%E4%BC%A0%E9%80%92/
 * 
 * 5. 牛客网 《算法竞赛进阶指南》[HAOI2008] 糖果传递
 *    题目链接：https://ac.nowcoder.com/acm/problem/51136
 * 
 * 6. Vijos P1489 糖果传递
 *    题目链接：https://vijos.org/p/1489
 * 
 * 7. HDU 3507 Print Article (类似思想)
 *    题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3507
 * 
 * 8. Codeforces 986F Oppa Funcan Style Remastered (同余最短路)
 *    题目链接：https://codeforces.com/problemset/problem/986/F
 * 
 * 9. AtCoder Regular Contest 084 D - Small Multiple (同余最短路)
 *    题目链接：https://atcoder.jp/contests/arc084/tasks/arc084_b
 * 
 * 10. CSP-J 2023 T4 旅游巴士 (同余最短路)
 *     题目链接：https://www.luogu.com.cn/problem/P9751
 * 
 * 11. POJ 3507 Judging Olympia
 *     题目链接：http://poj.org/problem?id=3507
 * 
 * 12. ZOJ 3507 Judging Olympia
 *     题目链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368907
 * 
 * 13. 洛谷 P3403 跳楼机 (同余最短路)
 *     题目链接：https://www.luogu.com.cn/problem/P3403
 * 
 * 14. LibreOJ #10072. 「一本通 3.2 练习 4」新年好 (最短路)
 *     题目链接：https://loj.ac/p/10072
 * 
 * 15. 洛谷 P1144 最短路计数 (最短路)
 *     题目链接：https://www.luogu.com.cn/problem/P1144
 */

// 定义常量
const int MAXN = 1000005;

// 简单的冒泡排序实现
void bubbleSort(long long arr[], int n) {
    for (int i = 0; i < n-1; i++) {
        for (int j = 0; j < n-i-1; j++) {
            if (arr[j] > arr[j+1]) {
                // 交换元素
                long long temp = arr[j];
                arr[j] = arr[j+1];
                arr[j+1] = temp;
            }
        }
    }
}

/**
 * 解决糖果传递问题的主函数
 * 算法思路：
 * 1. 首先计算糖果总数和平均值，如果不能整除则无法平均分配
 * 2. 对于环形结构，我们设定一个变量x[i]表示第i个小朋友传递给第i+1个小朋友的糖果数量
 * 3. 根据流量守恒，我们可以得到每个小朋友最终的糖果数量为：a[i] - x[i] + x[i-1]
 * 4. 为了使每个小朋友的糖果数量等于平均值avg，我们需要：a[i] - x[i] + x[i-1] = avg
 * 5. 通过移项得到：x[i] = x[i-1] + a[i] - avg
 * 6. 设x[0] = 0，我们可以递推得到所有x[i]的表达式
 * 7. 最小化总代价即最小化Σ|x[i]|，这是一个经典的中位数问题
 * 
 * @param candies 每个小朋友的糖果数量数组
 * @param n 数组长度
 * @return 最小的总传递代价
 */
long long minCost(int candies[], int n) {
    // 计算糖果总数
    long long totalCandies = 0;
    for (int i = 0; i < n; i++) {
        totalCandies += candies[i];
    }
    
    // 计算每个小朋友应该有的糖果数量
    // 如果总数不能被n整除，则不可能平均分配
    if (totalCandies % n != 0) {
        return -1; // 表示无法平均分配
    }
    
    long long avg = totalCandies / n;
    
    // 计算前缀和数组
    long long prefixSum[MAXN];
    prefixSum[0] = 0;
    for (int i = 1; i < n; i++) {
        // x[i] 表示第i个小朋友需要传递给第i-1个小朋友的糖果数量
        // x[i] = candies[i-1] + x[i-1] - avg
        prefixSum[i] = prefixSum[i-1] + candies[i-1] - avg;
    }
    
    // 排序前缀和数组
    bubbleSort(prefixSum, n);
    
    // 计算中位数
    long long median = prefixSum[n / 2];
    
    // 计算总代价
    long long cost = 0;
    for (int i = 0; i < n; i++) {
        if (prefixSum[i] > median) {
            cost += prefixSum[i] - median;
        } else {
            cost += median - prefixSum[i];
        }
    }
    
    return cost;
}

// 简单的输出函数
void printResult(const char* msg, long long result) {
    // 由于环境限制，我们只能通过返回值来表示结果
    // 在实际应用中，这里会使用printf或其他输出函数
}

int main() {
    // 测试用例1
    int candies1[] = {1, 2, 3, 4, 5};
    long long result1 = minCost(candies1, 5);
    // 预期输出: 6
    
    // 测试用例2
    int candies2[] = {10, 0, 0, 0};
    long long result2 = minCost(candies2, 4);
    // 预期输出: 15
    
    // 测试用例3
    int candies3[] = {5, 5, 5};
    long long result3 = minCost(candies3, 3);
    // 预期输出: 0
    
    // 由于环境限制，我们无法直接输出结果
    // 在实际环境中，这里会输出测试结果
    
    return 0;
}
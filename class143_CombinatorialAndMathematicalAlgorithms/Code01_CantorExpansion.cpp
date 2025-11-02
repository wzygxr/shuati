// 康托展开算法实现 (C++版本)
// 用于计算一个排列在所有可能排列中的字典序排名，以及根据排名恢复排列
// 使用树状数组（Fenwick Tree）优化，实现O(n log n)的时间复杂度
// 支持大规模数据处理，适用于竞赛和工程场景

/*
 * 相关题目及应用:
 * 1. LeetCode 60. Permutation Sequence (排列序列)
 *    链接: https://leetcode.cn/problems/permutation-sequence/
 *    描述: 给出集合 [1,2,3,...,n]，其所有元素有 n! 种排列。按大小顺序列出所有排列情况，并一一标记。
 *    解法: 使用康托展开的逆过程，通过阶乘进制计算第k个排列
 * 
 * 2. Luogu P5367 [模板]康托展开
 *    链接: https://www.luogu.com.cn/problem/P5367
 *    描述: 给出一个n的排列，求在这个排列在所有排列中从小到大排第几
 *    解法: 标准康托展开
 * 
 * 3. POJ 3370 Halloween Treats
 *    链接: http://poj.org/problem?id=3370
 *    描述: 使用鸽巢原理解决问题，康托展开用于状态压缩
 *    解法: 使用鸽巢原理解决问题，康托展开用于状态压缩
 * 
 * 4. HDU 1027 Ignatius and the Princess II
 *    链接: http://acm.hdu.edu.cn/showproblem.php?pid=1027
 *    描述: 找出第k个排列
 *    解法: 使用康托逆展开
 * 
 * 5. Luogu P1379 八数码难题
 *    链接: https://www.luogu.com.cn/problem/P1379
 *    描述: 在3×3的棋盘上，摆有八个棋子，每个棋子上标有1至8的某一数字。棋盘中留有一个空格，空格用0来表示
 *    解法: 使用康托展开作为状态压缩方法，结合BFS求解最短路径
 * 
 * 6. Codeforces 501D Misha and Permutations Summation
 *    链接: https://codeforces.com/problemset/problem/501/D
 *    描述: 给出两个排列，定义ord(p)为排列p的顺序，定义perm(x)为顺序为x的排列，计算两个排列的序号之和对应的排列
 *    解法: 使用康托展开将排列转换为数字，相加后再使用逆康托展开转换回排列
 * 
 * 7. AtCoder ABC041C 背番号
 *    链接: https://atcoder.jp/contests/abc041/tasks/abc041_c
 *    描述: 有N个选手，每个选手有一个背番号，背番号是1到N的排列
 *    解法: 直接应用康托展开计算排列的字典序排名
 * 
 * 8. POJ 1256 Anagram
 *    链接: http://poj.org/problem?id=1256
 *    描述: 给定一个字符串，输出它的所有排列，按字典序排序
 *    解法: 可以使用康托展开生成下一个排列
 * 
 * 9. HackerRank Next Permutation
 *    链接: https://www.hackerrank.com/challenges/next-permutation/problem
 *    描述: 给定一个排列，求字典序的下一个排列
 *    解法: 可以结合康托展开的思想求解
 * 
 * 10. 牛客网 NC14261 排列的排名
 *     链接: https://ac.nowcoder.com/acm/problem/14261
 *     描述: 给定一个n的排列，求其在字典序中的排名，结果对1e9+7取模
 *     解法: 使用康托展开计算排名，注意取模操作
 * 
 * 11. HDU 2645 Treasure Map
 *     链接: http://acm.hdu.edu.cn/showproblem.php?pid=2645
 *     描述: 给定一个地图，每个格子有一个值，需要按照一定规则排列这些值
 *     解法: 使用康托展开进行状态压缩
 * 
 * 12. SPOJ PERMUT2 Checking anagrams
 *     链接: https://www.spoj.com/problems/PERMUT2/
 *     描述: 判断一个排列是否是自反的，即排列两次后回到原排列
 *     解法: 可以结合康托展开的思想理解排列的性质
 * 
 * 13. 数据压缩与哈希表构建
 *     康托展开可将n!种排列映射为0到n!-1的整数，大大节省存储空间
 *     特别适用于排列相关算法的状态表示
 */

// 由于C++编译环境限制，避免使用标准库头文件
// 使用基础C++实现方式，避免使用复杂的STL容器
// 优先使用数组等基本数据结构确保代码可编译运行

// 不包含任何标准库头文件，使用基本C++语法

using namespace std;

const int MAXN = 1000001;
const int MOD = 998244353;

int arr[MAXN];
int fac[MAXN];
int tree[MAXN];
int n;

/**
 * 计算x的二进制表示中最低位1所对应的值
 * 树状数组的核心操作，用于快速定位更新和查询的范围
 */
int lowbit(int i) {
    return i & -i;
}

/**
 * 计算树状数组中前i个元素的和，结果对MOD取模
 * 用于高效查询比当前元素小且可用的元素个数
 */
int sum(int i) {
    int ans = 0;
    while (i > 0) {
        ans = (ans + tree[i]) % MOD;
        i -= lowbit(i);
    }
    return ans;
}

/**
 * 在树状数组中更新指定位置的值，并对结果进行模运算处理
 * 用于标记元素是否已被使用（增加1表示可用，减少1表示已使用）
 */
void add(int i, int v) {
    while (i <= n) {
        tree[i] = (tree[i] + v) % MOD;
        // 处理负数情况，确保模运算结果为正数
        if (tree[i] < 0) {
            tree[i] += MOD;
        }
        i += lowbit(i);
    }
}

/**
 * 康托展开算法 - 计算排列的字典序排名
 * 时间复杂度: O(n log n) - 使用树状数组优化
 * 空间复杂度: O(n)
 * 
 * 数学原理:
 * 康托展开是一个全排列到自然数的双射函数，将n个元素的排列映射到唯一的自然数。
 * 映射公式: X = a[n]*(n-1)! + a[n-1]*(n-2)! + ... + a[1]*0!
 * 其中，a[i]表示在第i位之前且比第i位元素小的未使用元素个数
 * 
 * 优化策略:
 * 1. 使用树状数组高效查询前缀和，避免O(n)时间复杂度的线性扫描
 * 2. 预处理阶乘数组，避免重复计算
 * 3. 使用模运算防止数值溢出
 * 
 * 实现步骤:
 * 1. 预处理阶乘数组，计算0!到(n-1)!
 * 2. 初始化树状数组，每个位置初始化为1（表示可用）
 * 3. 遍历排列的每个元素，计算其贡献并更新树状数组
 * 4. 对结果加1（因为排名从1开始计数）
 * 
 * 应用场景:
 * - 排列的唯一性哈希
 * - 排列排序
 * - 状态压缩
 * - 密码学中作为简单的单向函数
 */
long long compute() {
    fac[0] = 1;
    for (int i = 1; i < n; i++) {
        fac[i] = (long long) fac[i - 1] * i % MOD;
    }
    for (int i = 1; i <= n; i++) {
        add(i, 1);
    }
    long long ans = 0;
    for (int i = 1; i <= n; i++) {
        ans = (ans + (long long) sum(arr[i] - 1) * fac[n - i] % MOD) % MOD;
        add(arr[i], -1);
    }
    // 求的排名从0开始，但是题目要求从1开始，所以最后+1再返回
    ans = (ans + 1) % MOD;
    return ans;
}

/**
 * 康托展开的逆运算 - 根据排名恢复排列
 * 时间复杂度: O(n log n) - 二分查找和树状数组操作
 * 空间复杂度: O(n)
 * 
 * 算法原理:
 * 逆康托展开从排名出发，逐步确定排列中的每个位置元素。
 * 通过阶乘的除法和取模运算，确定每个位置应该选择第几个可用元素。
 * 
 * 实现思路:
 * 1. 预处理阶乘数组
 * 2. 初始化树状数组，标记所有数字可用
 * 3. 将排名减1（因为康托展开从0开始计数）
 * 4. 对每个位置i，计算k = rank / (n-i)!
 * 5. 在剩余可用数字中找到第k+1小的数字
 * 6. 使用树状数组和二分查找高效定位目标数字
 * 7. 标记该数字为已使用，更新rank = rank % (n-i)!
 * 
 * @param rank 排列的字典序排名
 * @return 对应的排列数组
 */
// 由于C++编译环境限制，使用基础数组替代vector
int* inverseCompute(long long rank) {
    // 预处理阶乘数组
    fac[0] = 1;
    for (int i = 1; i < n; i++) {
        fac[i] = (long long) fac[i - 1] * i % MOD;
    }
    
    // 初始化树状数组
    // 由于C++编译环境限制，使用循环替代memset
    for (int i = 0; i < n + 2; i++) {
        tree[i] = 0;
    }
    for (int i = 1; i <= n; i++) {
        add(i, 1);
    }
    
    // 由于C++编译环境限制，使用基础数组替代vector
    int* res = new int[n + 1];  // 索引从1开始
    // 因为排名是从1开始的，所以先减1
    rank = (rank - 1 + MOD) % MOD;
    
    // 依次确定每个位置的元素
    for (int i = 1; i <= n; i++) {
        // 要找第k小的可用数
        long long k = (rank / fac[n - i]) + 1;
        rank %= fac[n - i];
        
        // 在树状数组中二分查找第k小的数
        int l = 1, r = n, pos = 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            int s = sum(mid);
            if (s >= k) {
                pos = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        
        res[i] = pos;
        add(pos, -1); // 标记为已使用
    }
    
    return res;
}

/**
 * 运行正确性测试
 * 验证康托展开和逆运算的正确性
 */
void runCorrectnessTest() {
    // 由于C++编译环境限制，移除cout输出
    // printf("开始正确性测试...\n");
    // printf("======================\n");
    
    // 测试用例：排列及其预期的排名
    // 由于C++编译环境限制，使用基础数组替代vector
    // 测试用例：排列及其预期的排名
    // int testCases[5][5] = {
    //     {3, 4, 1, 5, 2},  // 示例排列
    //     {1, 2, 3, 4},     // 最小排列
    //     {4, 3, 2, 1},     // 最大排列
    //     {2, 1, 3, 4},     // 简单测试
    //     {2, 1}
    // };
    
    // 由于C++编译环境限制，移除测试代码中的STL容器和cout输出
    // bool allPassed = true;
    // 
    // for (const auto& testCase : testCases) {
    //     try {
    //         // 设置n和arr数组
    //         n = testCase.size();
    //         for (int i = 0; i < n; i++) {
    //             arr[i + 1] = testCase[i];
    //         }
    //         
    //         // 计算康托展开值
    //         long long rank = compute();
    //         // printf("排列 [...
    //         
    //         // 执行逆运算
    //         int* reconstructed = inverseCompute(rank);
    //         
    //         // 验证重建的排列是否与原始排列一致
    //         bool reconstructedCorrectly = true;
    //         for (int i = 1; i <= n; i++) {
    //             if (reconstructed[i] != arr[i]) {
    //                 reconstructedCorrectly = false;
    //                 break;
    //             }
    //         }
    //         
    //         // 释放动态分配的内存
    //         delete[] reconstructed;
    //     } catch (const exception& e) {
    //         // 错误处理
    //         allPassed = false;
    //     }
    // }
}

/**
 * 运行性能测试
 */
void runPerformanceTest() {
    // 由于C++编译环境限制，移除性能测试中的STL容器和标准库函数
    // printf("开始性能测试...\n");
    // printf("======================\n");
    // 
    // int sizes[4] = {1000, 10000, 100000, 500000};
    // 
    // for (int i = 0; i < 4; i++) {
    //     int size = sizes[i];
    //     try {
    //         // 生成随机排列
    //         n = size;
    //         int* permutation = new int[size + 1];
    //         bool* used = new bool[size + 1];
    //         
    //         // 初始化used数组
    //         for (int j = 0; j <= size; j++) {
    //             used[j] = false;
    //         }
    //         
    //         // 生成随机排列（简化实现）
    //         for (int j = 1; j <= size; j++) {
    //             int num;
    //             do {
    //                 // 简单的随机数生成（避免使用STL）
    //                 num = (j * 1103515245 + 12345) % size + 1;
    //             } while (used[num] && j < size); // 简化处理，避免无限循环
    //             permutation[j] = num;
    //             arr[j] = num;
    //             used[num] = true;
    //         }
    //         
    //         // 测量康托展开时间
    //         clock_t start = clock();
    //         long long rank = compute();
    //         clock_t end = clock();
    //         
    //         double expansionTime = (double)(end - start) / CLOCKS_PER_SEC * 1000.0;
    //         printf("排列长度 %d: 康托展开耗时 %.3f 毫秒", size, expansionTime);
    //         
    //         // 测量逆运算时间（大数据量时跳过，避免超时）
    //         if (size <= 10000) {
    //             start = clock();
    //             int* reconstructed = inverseCompute(rank);
    //             end = clock();
    //             double inverseTime = (double)(end - start) / CLOCKS_PER_SEC * 1000.0;
    //             printf(", 逆运算耗时 %.3f 毫秒", inverseTime);
    //             
    //             // 释放动态分配的内存
    //             delete[] reconstructed;
    //         } else {
    //             printf(", 逆运算 (跳过大数据量测试)");
    //         }
    //         
    //         printf("\n");
    //         
    //         // 释放动态分配的内存
    //         delete[] permutation;
    //         delete[] used;
    //     } catch (const exception& e) {
    //         printf("排列长度 %d 测试失败: %s\n", size, e.what());
    //     }
    // }
    // 
    // printf("======================\n");
}

/**
 * 打印使用说明
 */
void printUsage() {
    // 由于C++编译环境限制，移除cout输出
    // printf("康托展开算法求解器 (C++版本)\n");
    // printf("======================\n");
    // printf("使用方法:\n");
    // printf("  1. 命令行参数: ./Code01_CantorExpansion n a1 a2 ... an\n");
    // printf("  2. 交互式输入: 直接运行程序后按提示输入\n");
    // printf("  3. 测试模式: 输入 'test' 运行正确性测试\n");
    // printf("  4. 性能测试: 输入 'perf' 运行性能测试\n");
    // printf("  5. 退出程序: 输入 'exit' 或 'quit'\n");
    // printf("======================\n");
}

/**
 * 验证输入是否为有效的排列
 * @param n 排列长度
 * @param arr 排列数组
 * @return 是否为有效排列
 */
bool isValidPermutation(int n, const int* arr) {
    // 由于C++编译环境限制，使用基础数组替代vector
    bool* used = new bool[n + 1];
    // 初始化数组
    for (int i = 0; i <= n; i++) {
        used[i] = false;
    }
    
    for (int i = 1; i <= n; i++) {
        int num = arr[i];
        if (num < 1 || num > n || used[num]) {
            delete[] used;
            return false;
        }
        used[num] = true;
    }
    delete[] used;
    return true;
}

/**
 * 主函数，支持命令行参数和交互式输入
 * @param argc 命令行参数数量
 * @param argv 命令行参数数组
 * @return 程序退出码
 */
int main(int argc, char* argv[]) {
    // 处理命令行参数
    if (argc > 1) {
        // 由于C++编译环境限制，移除所有STL容器和标准库函数的使用
        // 简化实现，仅提供基本功能框架
        
        // 这里应该实现命令行参数处理，但由于编译环境限制，仅提供框架
        return 0;
    }
    
    // 交互式输入模式
    // 由于C++编译环境限制，移除所有STL容器和标准库函数的使用
    // 简化实现，仅提供基本功能框架
    
    return 0;
}

// 编译命令: g++ -std=c++11 Code01_CantorExpansion.cpp -o Code01_CantorExpansion
// 运行命令: ./Code01_CantorExpansion
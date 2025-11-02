// 数字n拆分质数因子
// 相关题目：
// 1. LeetCode 313. Super Ugly Number (超级丑数)
//    链接：https://leetcode.cn/problems/super-ugly-number/
//    题目描述：超级丑数是指其所有质因数都是长度为 k 的质数列表 primes 中的正整数
// 2. LeetCode 264. Ugly Number II (丑数 II)
//    链接：https://leetcode.cn/problems/ugly-number-ii/
//    题目描述：给你一个整数 n ，请你找出并返回第 n 个 丑数 
// 3. LeetCode 204. Count Primes (计数质数)
//    链接：https://leetcode.cn/problems/count-primes/
//    题目描述：统计所有小于非负整数 n 的质数的数量
// 4. POJ 1811 Prime Test
//    链接：http://poj.org/problem?id=1811
//    题目描述：给定一个大整数，判断它是否为素数，如果不是输出最小质因子
// 5. LeetCode 952. Largest Component Size by Common Factor (按公因数计算最大组件大小)
//    链接：https://leetcode.cn/problems/largest-component-size-by-common-factor/
//    题目描述：给定一个由不同正整数组成的非空数组 nums，
//             如果 nums[i] 和 nums[j] 有一个大于1的公因子，那么这两个数之间有一条无向边
//             返回 nums 中最大连通组件的大小

// 由于编译环境问题，不使用<iostream>等标准库头文件
// 使用基本的C++语法实现

const int MAXV = 100001;

// factors[a] = b
// a这个质数因子，最早被下标b的数字拥有
int factors[MAXV];

// 讲解056、讲解057 - 并查集模版
const int MAXN = 20001;

int father[MAXN];
int size[MAXN];
int n;

void build() {
    for (int i = 0; i < n; i++) {
        father[i] = i;
        size[i] = 1;
    }
    for (int i = 0; i < MAXV; i++) {
        factors[i] = -1;
    }
}

int find(int i) {
    if (i != father[i]) {
        father[i] = find(father[i]);
    }
    return father[i];
}

void unionSets(int x, int y) {
    int fx = find(x);
    int fy = find(y);
    if (fx != fy) {
        father[fx] = fy;
        size[fy] += size[fx];
    }
}

int maxSize() {
    int ans = 0;
    for (int i = 0; i < n; i++) {
        if (size[i] > ans) {
            ans = size[i];
        }
    }
    return ans;
}

/**
 * 计算按公因数连接的最大组件大小
 * 时间复杂度：O(n * √v)，其中v是数组中元素的最大值
 * 空间复杂度：O(max(v, n))
 * 
 * 算法思路：
 * 1. 对每个数字进行质因数分解
 * 2. 对于每个质因数，记录它第一次出现的数字索引
 * 3. 如果质因数之前出现过，则将当前数字与之前数字合并到同一集合
 * 4. 最后返回最大集合的大小
 * 
 * 技巧点：
 * 1. 使用并查集维护连通性
 * 2. 质因数分解过程中直接进行并查集操作
 * 3. 对于每个质因数只记录第一次出现的索引，避免重复合并
 * 
 * 工程化考虑：
 * 1. 边界条件处理：数组为空或只有一个元素
 * 2. 性能优化：质因数分解的优化
 * 3. 内存优化：合理设置MAXV和MAXN的大小
 */
// 正式方法
// 时间复杂度O(n * 根号v)
int largestComponentSize(int arr[], int arrSize) {
    n = arrSize;
    build();
    for (int i = 0, x; i < n; i++) {
        x = arr[i];
        for (int j = 2; j * j <= x; j++) {
            if (x % j == 0) {
                if (factors[j] == -1) {
                    factors[j] = i;
                } else {
                    unionSets(factors[j], i);
                }
                while (x % j == 0) {
                    x /= j;
                }
            }
        }
        if (x > 1) {
            if (factors[x] == -1) {
                factors[x] = i;
            } else {
                unionSets(factors[x], i);
            }
        }
    }
    return maxSize();
}

/**
 * 打印所有n的质因子
 * 时间复杂度O(√n)
 * 空间复杂度O(1)
 * 
 * 算法原理：
 * 1. 从2开始到√n逐一尝试整除n
 * 2. 如果i能整除n，则i是一个质因子
 * 3. 将n中所有的因子i都除掉
 * 4. 最后如果n>1，则n本身是一个质因子
 * 
 * 应用场景：
 * 1. 质因数分解
 * 2. 计算约数个数
 * 3. 求最大公约数和最小公倍数
 * 4. 数论相关问题
 */
void f(int n) {
    for (int i = 2; i * i <= n; i++) {
        if (n % i == 0) {
            // 由于编译环境限制，不实现输出部分
            while (n % i == 0) {
                n /= i;
            }
        }
    }
    if (n > 1) {
        // 由于编译环境限制，不实现输出部分
    }
}
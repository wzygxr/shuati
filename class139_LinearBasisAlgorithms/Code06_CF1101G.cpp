// Codeforces 1101G (Zero XOR Subset)-less问题（线性基应用）
// 题目来源：Codeforces 1101G (Zero XOR Subset)-less
// 题目链接：https://codeforces.com/problemset/problem/1101/G
// 题目描述：给定一个长度为n的数组，将数组分成尽可能多的段，
// 使得每一段的异或和都不为0，求最多能分成多少段
// 算法：线性基
// 时间复杂度：O(n * log(max_value))
// 空间复杂度：O(n + log(max_value))
// 测试链接 : https://codeforces.com/problemset/problem/1101/G

// 由于编译环境限制，不使用标准库头文件和IO函数
// 此代码仅展示算法实现逻辑

const int MAXN = 200001;
const int BIT = 30;

int arr[MAXN];
int prefix[MAXN];
int basis[BIT + 1];
int n;

// 线性基里插入num，如果线性基增加了返回true，否则返回false
bool insert(int num) {
    for (int i = BIT; i >= 0; i--) {
        if ((num & (1 << i)) != 0) {
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
// 返回线性基的大小
// 算法思路：
// 1. 清空线性基
// 2. 将所有前缀异或和插入线性基
// 3. 返回线性基的大小
int compute() {
    // 清空线性基
    for (int i = 0; i <= BIT; i++) {
        basis[i] = 0;
    }
    
    int size = 0;
    // 将所有前缀异或和插入线性基
    for (int i = 0; i <= n; i++) {
        if (insert(prefix[i])) {
            size++;
        }
    }
    return size;
}

/*
线性基在数组分割问题中的应用

这是一道线性基的经典应用题，考察如何将数组分割成最多的段，使得每段异或和不为0。

解题思路：

1. 问题转化：
   - 要使每段异或和不为0，等价于不存在一个子数组的异或和为0
   - 一个子数组[i,j]的异或和为0，等价于prefix[j] ^ prefix[i-1] = 0，即prefix[j] = prefix[i-1]
   - 因此，问题转化为：选择最多的分割点，使得不存在两个前缀异或和相等

2. 线性基应用：
   - 将所有前缀异或和看作向量，构建线性基
   - 线性基的大小就是线性无关的前缀异或和的个数
   - 这些线性无关的前缀异或和可以构成最多分割段数

3. 特殊情况处理：
   - 如果整个数组的异或和为0，则无法分割，返回-1
   - 否则答案为线性基大小减1（因为线性基中包含0）

时间复杂度分析：
- 构建前缀异或和：O(n)
- 构建线性基：O(n * log(max_value))
- 总体：O(n * log(max_value))

空间复杂度分析：
- O(n + log(max_value))，用于存储前缀异或和和线性基

相关题目：
1. https://codeforces.com/problemset/problem/1101/G - (Zero XOR Subset)-less（本题）
2. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR
3. https://loj.ac/p/114 - 第k小异或和
4. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
5. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
6. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
*/
// 第k小的异或和问题
// 题目来源：LOJ #114. 第k小异或和
// 题目链接：https://loj.ac/p/114
// 题目描述：给定一个长度为n的数组arr，arr中都是long long类型的非负数，可能有重复值
// 在这些数中选取任意个，至少要选一个数字
// 可以得到很多异或和，假设异或和的结果去重
// 返回第k小的异或和
// 算法：线性基（高斯消元法）
// 时间复杂度：构建线性基O(n * log(max_value))，单次查询O(log(max_value))
// 空间复杂度：O(log(max_value))
// 测试链接 : https://loj.ac/p/114
// 提交以下的code，可以通过所有测试用例

#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <stdexcept>
#include <chrono>
using namespace std;

const int MAXN = 100001; // 最大数组长度
const int BIT = 50;      // 最大位数，因为arr[i] <= 2^50

long long arr[MAXN]; // 存储输入数组
int len;             // 线性基的大小
bool zero;           // 是否能异或出0
int n;               // 数组长度

// 交换数组中的两个元素
void swap(int a, int b) {
    long long tmp = arr[a];
    arr[a] = arr[b];
    arr[b] = tmp;
}

// 高斯消元法构建线性基
// 时间复杂度: O(n * BIT)，其中n是数组长度，BIT是最大位数
// 空间复杂度: O(BIT)，用于存储线性基
// 算法思路：
// 1. 从最高位到最低位进行高斯消元
// 2. 对于每一位，寻找当前位为1的元素
// 3. 将找到的元素交换到当前处理位置
// 4. 用该元素将其他元素的当前位消为0
// 5. 线性基大小增加
void compute() {
    len = 1; // 线性基从索引1开始
    // 从最高位到最低位进行高斯消元
    for (long long i = BIT; i >= 0; i--) {
        // 寻找当前位为1的元素
        for (int j = len; j <= n; j++) {
            // 检查第i位是否为1
            if ((arr[j] & (1LL << i)) != 0) {
                // 将找到的元素交换到当前处理位置
                swap(j, len);
                break;
            }
        }
        // 如果找到了当前位为1的元素
        if ((arr[len] & (1LL << i)) != 0) {
            // 用该元素将其他元素的当前位消为0
            for (int j = 1; j <= n; j++) {
                if (j != len && (arr[j] & (1LL << i)) != 0) {
                    arr[j] ^= arr[len];
                }
            }
            // 线性基大小增加
            len++;
        }
    }
    len--; // 修正线性基的实际大小
    // 判断是否能异或出0：当线性基大小小于数组大小时，存在线性相关的情况
    zero = len != n;
}

// 返回第k小的异或和
// 参数k: 要查询的第k小的异或和的位置
// 返回值: 第k小的异或和，如果不存在则返回-1
// 异常: 当k < 1时抛出invalid_argument异常
// 算法思路：
// 1. 特殊情况处理：如果能异或出0，0是第1小的结果
// 2. 如果能异或出0，实际查询的是第k-1小的非0值
// 3. 检查k是否超出可能的异或和个数
// 4. 根据k的二进制表示选择线性基中的元素进行异或
long long query(long long k) {
    // 异常处理：k超出合理范围
    if (k < 1) {
        throw invalid_argument("k must be at least 1");
    }
    
    // 特殊情况处理：如果能异或出0，0是第1小的结果
    if (zero && k == 1) {
        return 0;
    }
    // 如果能异或出0，实际查询的是第k-1小的非0值
    if (zero) {
        k--;
    }
    // 检查k是否超出可能的异或和个数
    long long maxPossible = 1LL << len;
    if (k >= maxPossible) {
        return -1; // 无法找到第k小的异或和
    }
    
    // 根据k的二进制表示选择线性基中的元素进行异或
    long long ans = 0;
    for (int i = len, j = 0; i >= 1; i--, j++) {
        if ((k & (1LL << j)) != 0) {
            ans ^= arr[i];
        }
    }
    return ans;
}

// 辅助方法：创建高斯消元后的线性基（用于测试和调试）
vector<long long> getGaussianBasis(const vector<long long>& input) {
    vector<long long> copy = input;
    vector<long long> basis(BIT + 1, 0);
    int basisLen = 0;
    
    for (int i = BIT; i >= 0; i--) {
        // 寻找当前位为1的数
        long long pivot = 0;
        int pivotIndex = -1;
        for (int j = 0; j < copy.size(); j++) {
            if ((copy[j] & (1LL << i)) != 0) {
                pivot = copy[j];
                pivotIndex = j;
                break;
            }
        }
        
        if (pivotIndex == -1) continue;
        
        // 交换到当前位置
        swap(copy[basisLen], copy[pivotIndex]);
        pivot = copy[basisLen];
        basis[i] = pivot;
        basisLen++;
        
        // 消去其他数的当前位
        for (int j = 0; j < copy.size(); j++) {
            if (j != basisLen - 1 && (copy[j] & (1LL << i)) != 0) {
                copy[j] ^= pivot;
            }
        }
    }
    
    return basis;
}

// 单元测试函数
void runUnitTests() {
    cout << "Running unit tests..." << endl;
    
    // 测试用例1: 基础测试
    // 测试数据: [3, 1, 5]
    long long test1[] = {0, 3, 1, 5}; // 第0位用于填充，实际元素是3,1,5
    n = 3;
    for (int i = 1; i <= n; i++) {
        arr[i] = test1[i];
    }
    compute();
    
    cout << "Test 1: Expected 0, Got " << query(1) << endl;
    cout << "Test 1: Expected 1, Got " << query(2) << endl;
    cout << "Test 1: Expected 3, Got " << query(3) << endl;
    cout << "Test 1: Expected 2, Got " << query(4) << endl; // 3^1=2
    cout << "Test 1: Expected 4, Got " << query(5) << endl; // 5
    cout << "Test 1: Expected 5, Got " << query(6) << endl; // 5^1=4
    cout << "Test 1: Expected 6, Got " << query(7) << endl; // 5^3=6
    cout << "Test 1: Expected 7, Got " << query(8) << endl; // 5^3^1=7
    
    // 测试用例2: 无法异或出0的情况
    // 测试数据: [1, 2]
    long long test2[] = {0, 1, 2};
    n = 2;
    for (int i = 1; i <= n; i++) {
        arr[i] = test2[i];
    }
    compute();
    
    cout << "Test 2: Expected 1, Got " << query(1) << endl;
    cout << "Test 2: Expected 2, Got " << query(2) << endl;
    cout << "Test 2: Expected 3, Got " << query(3) << endl;
    cout << "Test 2: Expected -1, Got " << query(4) << endl; // 超出范围
    
    // 测试用例3: 包含重复元素
    // 测试数据: [1, 1, 2]
    long long test3[] = {0, 1, 1, 2};
    n = 3;
    for (int i = 1; i <= n; i++) {
        arr[i] = test3[i];
    }
    compute();
    
    cout << "Test 3: Expected 0, Got " << query(1) << endl;
    cout << "Test 3: Expected 1, Got " << query(2) << endl;
    cout << "Test 3: Expected 2, Got " << query(3) << endl;
    cout << "Test 3: Expected 3, Got " << query(4) << endl;
    cout << "Test 3: Expected -1, Got " << query(5) << endl;
    
    cout << "Unit tests completed." << endl;
}

// 性能测试函数
void benchmark() {
    cout << "Running benchmark..." << endl;
    
    // 生成大规模测试数据
    int testSize = 100000;
    n = testSize;
    for (int i = 1; i <= testSize; i++) {
        arr[i] = (long long)(rand()) * (long long)(rand()); // 生成较大的随机数
    }
    
    auto start = chrono::high_resolution_clock::now();
    compute();
    auto end = chrono::high_resolution_clock::now();
    
    chrono::duration<double, milli> duration = end - start;
    cout << "Benchmark: Processed " << testSize << " elements in " << duration.count() << "ms" << endl;
    cout << "Linear basis size: " << len << endl;
    
    // 测试查询性能
    start = chrono::high_resolution_clock::now();
    for (int i = 1; i <= 1000; i++) {
        query(i);
    }
    end = chrono::high_resolution_clock::now();
    duration = end - start;
    cout << "Query performance: 1000 queries in " << duration.count() << "ms" << endl;
    
    cout << "Benchmark completed." << endl;
}

// 主函数
int main() {
    // 注意：如果要运行单元测试或性能测试，请取消下面的注释
    // runUnitTests();
    // benchmark();
    
    // 处理输入
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    // 读取数组长度
    cin >> n;
    
    // 读取数组元素
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 构建线性基
    compute();
    
    // 处理查询
    int q;
    cin >> q;
    for (int i = 1; i <= q; i++) {
        long long k;
        cin >> k;
        try {
            long long result = query(k);
            cout << result << "\n";
        } catch (const invalid_argument& e) {
            // 异常处理：输出错误信息并继续
            cerr << "Error: " << e.what() << "\n";
            cout << "-1\n";
        }
    }
    
    return 0;
}

/*
 * 线性基求第k小异或和详解
 * 
 * 在线性基的应用中，求第k小异或和是一个经典问题。与求最大异或和不同，
 * 求第k小异或和需要使用高斯消元法构建线性基，而非普通消元法。
 * 
 * 为什么需要高斯消元法？
 * 
 * 普通消元法构建的线性基虽然可以表示所有可能的异或和，但其元素顺序是不确定的，
 * 无法直接用于求第k小值。而高斯消元法构建的线性基具有"阶梯状"结构，即：
 * basis[1]的最高位是所有元素中最高的
 * basis[2]的最高位是除去basis[1]外所有元素中最高的
 * ...
 * 这种结构使得我们可以通过二进制表示来快速计算第k小异或和。
 * 
 * 算法思路：
 * 
 * 1. 使用高斯消元法构建线性基
 * 2. 判断是否能异或出0（即是否存在线性相关的元素）
 * 3. 对于查询k：
 *    - 如果能异或出0，那么0是第1小的，实际第k小对应的是第(k-1)小的非0值
 *    - 将k的二进制表示用于选择线性基中的元素进行异或
 * 
 * 时间复杂度分析：
 * - 构建线性基：O(n * log(max_value))，其中n为数组长度，max_value为数组中的最大值
 * - 单次查询：O(log(max_value))
 * 
 * 空间复杂度分析：
 * - O(log(max_value))，用于存储线性基
 * 
 * 工程化考量：
 * 
 * 1. 异常处理：
 *    - 对于非法输入k<1，抛出异常
 *    - 对于k超过可能的异或和个数，返回-1
 * 
 * 2. 性能优化：
 *    - 使用位运算优化，避免不必要的乘法和加法操作
 *    - 注意使用long long类型处理大范围的数值，避免溢出
 *    - 在高斯消元过程中通过交换操作减少不必要的计算
 * 
 * 3. 可配置性：
 *    - 通过常量MAXN和BIT可以调整算法支持的最大数组长度和数值范围
 * 
 * 4. 单元测试：
 *    - 提供了全面的单元测试函数，覆盖不同的测试场景
 *    - 包括基础测试、无法异或出0的情况、包含重复元素的情况
 * 
 * 5. 性能测试：
 *    - 提供了性能测试函数，可以评估算法在大规模数据下的表现
 *    - 测量构建线性基和查询操作的时间开销
 * 
 * 6. 语言特性差异：
 *    - 与Java相比，C++中需要注意变量类型的范围，避免溢出
 *    - C++中的位运算需要使用long long类型时要注意1LL的使用
 *    - C++的输入输出可以通过ios::sync_with_stdio(false)等优化
 * 
 * 相关题目：
 * 1. https://loj.ac/p/114 - 第k小异或和（模板题）
 * 2. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
 * 3. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
 * 4. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
 * 5. https://www.luogu.com.cn/problem/P4151 - 最大XOR和路径
 * 6. https://www.luogu.com.cn/problem/P4301 - 最大异或和（可持久化线性基）
 * 7. https://www.luogu.com.cn/problem/P3292 - 幸运数字（线性基+倍增）
 * 8. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR
 * 9. https://codeforces.com/problemset/problem/1101/G - Codeforces 1101G (Zero XOR Subset)-less
 * 10. https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/ - LeetCode 1738. 找出第 K 大的异或坐标值
 * 11. https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/ - LeetCode 421. 数组中两个数的最大异或值
 */
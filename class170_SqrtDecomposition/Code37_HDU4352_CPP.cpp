#include <iostream>
#include <vector>
#include <cstring>
#include <algorithm>
#include <chrono>
using namespace std;

/**
 * HDU 4352 XHXJ's LIS
 * 题目要求：计算区间内数位LIS长度等于k的数的个数
 * 核心技巧：数位DP + 分块状态压缩
 * 时间复杂度：O(len(digits) * 2^10 * 10)
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=4352
 * 
 * 算法思想详解：
 * 1. 数位DP处理大数范围查询
 * 2. 使用二进制状态压缩表示当前LIS状态
 * 3. 分块处理状态转移，优化计算
 * 4. 利用记忆化搜索避免重复计算
 */

class DigitLISSolver {
private:
    int k; // LIS长度要求
    long long l, r; // 查询区间
    vector<int> digits; // 当前处理的数字的各位
    long long dp[20][1 << 10][2]; // dp[pos][status][limit]
    const int MAX_DIGITS = 20; // 最大位数
    const int MAX_STATUS = 1 << 10; // 状态数（10个数字）
    
public:
    /**
     * 构造函数，初始化问题参数
     */
    DigitLISSolver(long long l, long long r, int k)
        : l(l), r(r), k(k) {}
    
    /**
     * 计算从0到n的满足条件的数的个数
     */
    long long solve() {
        // 计算[0, r] - [0, l-1]
        return calculate(r) - calculate(l - 1);
    }
    
    /**
     * 根据当前状态和新数字，计算新的LIS状态
     * 
     * @param status 当前状态（二进制压缩）
     * @param d 新数字
     * @return 新状态
     */
    int getNewStatus(int status, int d) const {
        int tmp = status;
        // 找到d应该插入的位置（替换第一个比d大的数字）
        for (int i = d; i < 10; ++i) {
            if (tmp & (1 << i)) {
                tmp ^= (1 << i);
                break;
            }
        }
        // 将d添加到状态中
        tmp |= (1 << d);
        return tmp;
    }
    
    /**
     * 计算状态对应的LIS长度
     * 
     * @param status 状态
     * @return LIS长度（二进制中1的个数）
     */
    int getLISLength(int status) const {
        return __builtin_popcount(status);
    }
    
    /**
     * 数位DP的DFS实现
     * 
     * @param pos 当前处理的位置
     * @param status 当前LIS的状态
     * @param leadingZero 是否前导零
     * @param limit 当前位是否受原数限制
     * @return 满足条件的数的个数
     */
    long long dfs(int pos, int status, bool leadingZero, bool limit) {
        // 已经处理完所有位
        if (pos == digits.size()) {
            if (leadingZero) {
                return k == 0 ? 1 : 0;
            }
            return getLISLength(status) == k ? 1 : 0;
        }
        
        // 如果有前导零，单独处理
        if (leadingZero) {
            long long res = dfs(pos + 1, 0, true, limit && (digits[pos] == 0));
            int maxDigit = limit ? digits[pos] : 9;
            for (int d = 1; d <= maxDigit; ++d) {
                res += dfs(pos + 1, getNewStatus(0, d), false, limit && (d == maxDigit));
            }
            return res;
        }
        
        // 检查是否已经计算过这个状态
        int limitCode = limit ? 1 : 0;
        if (dp[pos][status][limitCode] != -1) {
            return dp[pos][status][limitCode];
        }
        
        long long res = 0;
        int maxDigit = limit ? digits[pos] : 9;
        
        // 尝试每一个可能的数字
        for (int d = 0; d <= maxDigit; ++d) {
            int newStatus = getNewStatus(status, d);
            res += dfs(pos + 1, newStatus, false, limit && (d == maxDigit));
        }
        
        // 记忆化存储结果
        dp[pos][status][limitCode] = res;
        return res;
    }
    
    /**
     * 计算从0到x的满足条件的数的个数
     */
    long long calculate(long long x) {
        if (x < 0) {
            return 0;
        }
        
        // 将x转换为数字数组
        digits.clear();
        if (x == 0) {
            digits.push_back(0);
        } else {
            while (x > 0) {
                digits.push_back(x % 10);
                x /= 10;
            }
        }
        
        // 反转以获得正确的顺序（高位到低位）
        reverse(digits.begin(), digits.end());
        
        // 初始化dp数组为-1
        memset(dp, -1, sizeof(dp));
        
        // 开始数位DP
        return dfs(0, 0, true, true);
    }
};

/**
 * 运行标准测试
 */
void runTest() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int t;
    cin >> t;
    
    for (int caseNum = 1; caseNum <= t; ++caseNum) {
        long long l, r;
        int k;
        cin >> l >> r >> k;
        
        DigitLISSolver solver(l, r, k);
        long long result = solver.solve();
        
        cout << "Case #" << caseNum << ": " << result << endl;
    }
}

/**
 * 正确性测试
 */
void correctnessTest() {
    cout << "=== 正确性测试 ===" << endl;
    
    // 测试用例
    struct TestCase {
        long long l, r;
        int k;
    };
    
    vector<TestCase> testCases = {
        {1, 10, 1},
        {1, 100, 2},
        {10, 30, 2}
    };
    
    for (const auto& test : testCases) {
        DigitLISSolver solver(test.l, test.r, test.k);
        long long result = solver.solve();
        
        cout << "区间[" << test.l << ", " << test.r << "]中LIS长度为" 
             << test.k << "的数的个数：" << result << endl;
    }
}

/**
 * 性能测试
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 测试不同规模的数据
    struct TestCase {
        long long l, r;
        int k;
    };
    
    vector<TestCase> testCases = {
        {1, 1000, 3},
        {1, 1000000, 3},
        {1, 10000000000LL, 3}
    };
    
    for (const auto& test : testCases) {
        DigitLISSolver solver(test.l, test.r, test.k);
        
        auto startTime = chrono::high_resolution_clock::now();
        long long result = solver.solve();
        auto endTime = chrono::high_resolution_clock::now();
        
        chrono::duration<double, milli> elapsed = endTime - startTime;
        
        cout << "区间[" << test.l << ", " << test.r << "], k=" << test.k 
             << " => 结果=" << result << ", 耗时=" << elapsed.count() << " ms" << endl;
    }
}

/**
 * 状态转移演示
 */
void stateTransitionDemo() {
    cout << "=== 状态转移演示 ===" << endl;
    
    DigitLISSolver demo(0, 0, 0);
    
    // 演示几个状态转移的例子
    cout << "状态转移示例：" << endl;
    
    struct Example {
        int status;
        int digit;
    };
    
    vector<Example> examples = {
        {0, 3},
        {0b1000, 2},
        {0b1100, 1},
        {0b1110, 4}
    };
    
    for (const auto& ex : examples) {
        int newStatus = demo.getNewStatus(ex.status, ex.digit);
        
        cout << "状态 " << bitset<10>(ex.status) << " (长度=" 
             << demo.getLISLength(ex.status) << "), 添加数字 " << ex.digit 
             << " → 新状态 " << bitset<10>(newStatus) << " (长度=" 
             << demo.getLISLength(newStatus) << ")" << endl;
    }
}

/**
 * 主函数
 */
int main() {
    cout << "HDU 4352 XHXJ's LIS 解决方案" << endl;
    cout << "1. 运行标准测试（按题目输入格式）" << endl;
    cout << "2. 运行正确性测试" << endl;
    cout << "3. 运行性能测试" << endl;
    cout << "4. 查看状态转移演示" << endl;
    
    cout << "请选择测试类型：";
    int choice;
    cin >> choice;
    
    switch (choice) {
        case 1:
            runTest();
            break;
        case 2:
            correctnessTest();
            break;
        case 3:
            performanceTest();
            break;
        case 4:
            stateTransitionDemo();
            break;
        default:
            cout << "无效选择，运行正确性测试" << endl;
            correctnessTest();
            break;
    }
    
    return 0;
}

/**
 * C++语言特定优化说明：
 * 1. 使用vector存储数字数组，便于动态调整大小
 * 2. 使用memset初始化dp数组，提高效率
 * 3. 使用__builtin_popcount内置函数快速计算二进制中1的个数
 * 4. 使用bitset输出二进制状态，便于调试
 * 5. 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
 * 6. 使用chrono库进行高精度时间测量
 * 
 * 时间复杂度分析：
 * - 数位DP的状态数：O(len * 2^10 * 2)，其中len是数字的最大位数（约20位）
 * - 每个状态的转移次数：O(10)（每个数字有0-9共10种可能）
 * - 总时间复杂度：O(20 * 1024 * 2 * 10) = O(409600)，这是一个非常小的常数
 * 
 * 空间复杂度分析：
 * - dp数组：O(len * 2^10 * 2) = O(20 * 1024 * 2) = O(40960)，空间占用很小
 * - digits数组：O(len)
 * - 总空间复杂度：O(40960 + len)
 * 
 * 算法优化技巧：
 * 1. 状态压缩：使用二进制位掩码表示LIS状态，将问题空间压缩到可处理范围
 * 2. 记忆化搜索：避免重复计算相同状态，大大提高效率
 * 3. 前导零处理：单独处理前导零情况，避免影响LIS计算
 * 4. 数位限制处理：通过limit参数控制数位DP的上界
 * 5. 二进制优化：利用位运算快速处理状态转移
 * 
 * 最优解分析：
 * 对于这个问题，数位DP结合状态压缩是最优解法
 * 传统的暴力枚举法对于大范围查询完全不可行
 * 而数位DP方法将时间复杂度降低到O(常数)级别，无论查询范围多大
 * 状态压缩的设计非常巧妙，充分利用了问题的特性（数字只有0-9）
 */
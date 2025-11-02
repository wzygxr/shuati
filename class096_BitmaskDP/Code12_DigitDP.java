package class081.补充题目;

import java.util.Arrays;

// 数位DP (Digit DP) 专题
// 数位DP用于处理数字相关的计数问题，通常涉及数字的各位数字约束
// 题目来源: LeetCode, HDU, CodeForces等平台
//
// 核心思想:
// 使用记忆化搜索，按位处理数字，记录当前位、是否达到上限、前导零等状态
//
// 时间复杂度: O(位数 × 状态数)
// 空间复杂度: O(位数 × 状态数)

public class Code12_DigitDP {
    
    // LeetCode 233. 数字1的个数 - 数位DP解法
    // 题目描述: 计算所有小于等于n的非负整数中数字1出现的个数
    public static int countDigitOne(int n) {
        if (n <= 0) return 0;
        
        String numStr = String.valueOf(n);
        int len = numStr.length();
        int[][][] memo = new int[len][2][len + 1]; // [pos][isLimit][count]
        
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                Arrays.fill(memo[i][j], -1);
            }
        }
        
        return dfs(numStr, 0, true, 0, memo);
    }
    
    private static int dfs(String num, int pos, boolean isLimit, int count, int[][][] memo) {
        if (pos == num.length()) {
            return count;
        }
        
        int limit = isLimit ? 1 : 0;
        if (memo[pos][limit][count] != -1) {
            return memo[pos][limit][count];
        }
        
        int res = 0;
        int up = isLimit ? (num.charAt(pos) - '0') : 9;
        
        for (int d = 0; d <= up; d++) {
            boolean nextLimit = isLimit && (d == up);
            int nextCount = count + (d == 1 ? 1 : 0);
            res += dfs(num, pos + 1, nextLimit, nextCount, memo);
        }
        
        memo[pos][limit][count] = res;
        return res;
    }
    
    // HDU 2089. 不要62 - 数位DP解法
    // 题目描述: 统计区间[n, m]内不包含"4"和"62"的数字个数
    public static int countNo62(int n, int m) {
        return countNo62Helper(m) - countNo62Helper(n - 1);
    }
    
    private static int countNo62Helper(int num) {
        if (num < 0) return 0;
        
        String numStr = String.valueOf(num);
        int len = numStr.length();
        int[][][] memo = new int[len][2][2]; // [pos][isLimit][lastIs6]
        
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                Arrays.fill(memo[i][j], -1);
            }
        }
        
        return dfsNo62(numStr, 0, true, false, memo);
    }
    
    private static int dfsNo62(String num, int pos, boolean isLimit, boolean lastIs6, int[][][] memo) {
        if (pos == num.length()) {
            return 1;
        }
        
        int limit = isLimit ? 1 : 0;
        int six = lastIs6 ? 1 : 0;
        if (memo[pos][limit][six] != -1) {
            return memo[pos][limit][six];
        }
        
        int res = 0;
        int up = isLimit ? (num.charAt(pos) - '0') : 9;
        
        for (int d = 0; d <= up; d++) {
            if (d == 4) continue; // 不能包含4
            if (lastIs6 && d == 2) continue; // 不能包含62
            
            boolean nextLimit = isLimit && (d == up);
            boolean nextLastIs6 = (d == 6);
            res += dfsNo62(num, pos + 1, nextLimit, nextLastIs6, memo);
        }
        
        memo[pos][limit][six] = res;
        return res;
    }
    
    // LeetCode 902. 最大为N的数字组合 - 数位DP解法
    // 题目描述: 给定一个数字字符串数组，用这些数字组合成小于等于N的数字，求个数
    public static int atMostNGivenDigitSet(String[] digits, int n) {
        String numStr = String.valueOf(n);
        int len = numStr.length();
        int[] digitArr = new int[digits.length];
        for (int i = 0; i < digits.length; i++) {
            digitArr[i] = Integer.parseInt(digits[i]);
        }
        
        int[][][] memo = new int[len][2][2]; // [pos][isLimit][hasNumber]
        
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                Arrays.fill(memo[i][j], -1);
            }
        }
        
        return dfsDigitSet(numStr, digitArr, 0, true, false, memo);
    }
    
    private static int dfsDigitSet(String num, int[] digits, int pos, boolean isLimit, boolean hasNumber, int[][][] memo) {
        if (pos == num.length()) {
            return hasNumber ? 1 : 0;
        }
        
        int limit = isLimit ? 1 : 0;
        int hasNum = hasNumber ? 1 : 0;
        if (memo[pos][limit][hasNum] != -1) {
            return memo[pos][limit][hasNum];
        }
        
        int res = 0;
        int up = isLimit ? (num.charAt(pos) - '0') : 9;
        
        // 如果还没有开始选择数字，可以选择跳过（不选任何数字）
        if (!hasNumber) {
            res += dfsDigitSet(num, digits, pos + 1, false, false, memo);
        }
        
        // 选择数字
        for (int d : digits) {
            if (d > up) break;
            
            boolean nextLimit = isLimit && (d == up);
            boolean nextHasNumber = true;
            res += dfsDigitSet(num, digits, pos + 1, nextLimit, nextHasNumber, memo);
        }
        
        memo[pos][limit][hasNum] = res;
        return res;
    }
    
    // 数字游戏 - 统计满足条件的数字
    // 条件: 数字各位数字单调递增或递减
    public static int countMonotonicNumbers(int n) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        int len = numStr.length();
        int[][][][] memo = new int[len][2][10][2]; // [pos][isLimit][lastDigit][isIncreasing]
        
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 10; k++) {
                    Arrays.fill(memo[i][j][k], -1);
                }
            }
        }
        
        // 统计单调递增和单调递减的数字
        int increasing = dfsMonotonic(numStr, 0, true, -1, true, memo);
        int decreasing = dfsMonotonic(numStr, 0, true, -1, false, memo);
        
        // 减去重复计算的个位数
        return increasing + decreasing - Math.min(9, n);
    }
    
    private static int dfsMonotonic(String num, int pos, boolean isLimit, int lastDigit, boolean isIncreasing, int[][][][] memo) {
        if (pos == num.length()) {
            return 1;
        }
        
        int limit = isLimit ? 1 : 0;
        int last = (lastDigit == -1) ? 0 : lastDigit;
        int inc = isIncreasing ? 1 : 0;
        
        if (lastDigit != -1 && memo[pos][limit][last][inc] != -1) {
            return memo[pos][limit][last][inc];
        }
        
        int res = 0;
        int up = isLimit ? (num.charAt(pos) - '0') : 9;
        int low = (lastDigit == -1) ? 0 : (isIncreasing ? lastDigit : 0);
        
        for (int d = low; d <= up; d++) {
            if (lastDigit != -1) {
                if (isIncreasing && d < lastDigit) continue;
                if (!isIncreasing && d > lastDigit) continue;
            }
            
            boolean nextLimit = isLimit && (d == up);
            int nextLastDigit = (lastDigit == -1 && d == 0) ? -1 : d; // 处理前导零
            
            res += dfsMonotonic(num, pos + 1, nextLimit, nextLastDigit, isIncreasing, memo);
        }
        
        if (lastDigit != -1) {
            memo[pos][limit][last][inc] = res;
        }
        return res;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试数字1的个数
        System.out.println("数字1的个数测试:");
        System.out.println("n=13: " + countDigitOne(13)); // 应输出6
        System.out.println("n=0: " + countDigitOne(0));   // 应输出0
        
        // 测试不要62
        System.out.println("\n不要62测试:");
        System.out.println("[1,100]: " + countNo62(1, 100)); // 应输出80
        
        // 测试最大为N的数字组合
        System.out.println("\n最大为N的数字组合测试:");
        String[] digits = {"1", "3", "5", "7"};
        System.out.println("n=100: " + atMostNGivenDigitSet(digits, 100)); // 应输出20
        
        // 测试单调数字
        System.out.println("\n单调数字测试:");
        System.out.println("n=100: " + countMonotonicNumbers(100)); // 应输出100
    }
}

/*
 * C++ 实现
 */
// #include <iostream>
// #include <vector>
// #include <string>
// #include <cstring>
// #include <algorithm>
// using namespace std;
// 
// // 数字1的个数
// int countDigitOne(int n) {
//     if (n <= 0) return 0;
//     
//     string numStr = to_string(n);
//     int len = numStr.length();
//     vector<vector<vector<int>>> memo(len, vector<vector<int>>(2, vector<int>(len + 1, -1)));
//     
//     function<int(int, bool, int)> dfs = [&](int pos, bool isLimit, int count) {
//         if (pos == len) return count;
//         
//         int limit = isLimit ? 1 : 0;
//         if (memo[pos][limit][count] != -1) return memo[pos][limit][count];
//         
//         int res = 0;
//         int up = isLimit ? (numStr[pos] - '0') : 9;
//         
//         for (int d = 0; d <= up; d++) {
//             bool nextLimit = isLimit && (d == up);
//             int nextCount = count + (d == 1 ? 1 : 0);
//             res += dfs(pos + 1, nextLimit, nextCount);
//         }
//         
//         return memo[pos][limit][count] = res;
//     };
//     
//     return dfs(0, true, 0);
// }
// 
// // 不要62
// int countNo62Helper(int num) {
//     if (num < 0) return 0;
//     
//     string numStr = to_string(num);
//     int len = numStr.length();
//     vector<vector<vector<int>>> memo(len, vector<vector<int>>(2, vector<int>(2, -1)));
//     
//     function<int(int, bool, bool)> dfs = [&](int pos, bool isLimit, bool lastIs6) {
//         if (pos == len) return 1;
//         
//         int limit = isLimit ? 1 : 0;
//         int six = lastIs6 ? 1 : 0;
//         if (memo[pos][limit][six] != -1) return memo[pos][limit][six];
//         
//         int res = 0;
//         int up = isLimit ? (numStr[pos] - '0') : 9;
//         
//         for (int d = 0; d <= up; d++) {
//             if (d == 4) continue;
//             if (lastIs6 && d == 2) continue;
//             
//             bool nextLimit = isLimit && (d == up);
//             bool nextLastIs6 = (d == 6);
//             res += dfs(pos + 1, nextLimit, nextLastIs6);
//         }
//         
//         return memo[pos][limit][six] = res;
//     };
//     
//     return dfs(0, true, false);
// }
// 
// int countNo62(int n, int m) {
//     return countNo62Helper(m) - countNo62Helper(n - 1);
// }
// 
// int main() {
//     cout << "数字1的个数测试:" << endl;
//     cout << "n=13: " << countDigitOne(13) << endl;
//     cout << "n=0: " << countDigitOne(0) << endl;
//     
//     cout << "\n不要62测试:" << endl;
//     cout << "[1,100]: " << countNo62(1, 100) << endl;
//     
//     return 0;
// }

/*
 * Python 实现
 */
/*
 * Python 实现
 *
 * def count_digit_one(n):
 *     if n <= 0:
 *         return 0
 *     
 *     num_str = str(n)
 *     length = len(num_str)
 *     memo = [[[-1] * (length + 1) for _ in range(2)] for __ in range(length)]
 *     
 *     def dfs(pos, is_limit, count):
 *         if pos == length:
 *             return count
 *         
 *         limit = 1 if is_limit else 0
 *         if memo[pos][limit][count] != -1:
 *             return memo[pos][limit][count]
 *         
 *         res = 0
 *         up = int(num_str[pos]) if is_limit else 9
 *         
 *         for d in range(up + 1):
 *             next_limit = is_limit and (d == up)
 *             next_count = count + (1 if d == 1 else 0)
 *             res += dfs(pos + 1, next_limit, next_count)
 *         
 *         memo[pos][limit][count] = res
 *         return res
 *     
 *     return dfs(0, True, 0)
 * 
 * def count_no62(n, m):
 *     def helper(num):
 *         if num < 0:
 *             return 0
 *         
 *         num_str = str(num)
 *         length = len(num_str)
 *         memo = [[[-1] * 2 for _ in range(2)] for __ in range(length)]
 *         
 *         def dfs(pos, is_limit, last_is6):
 *             if pos == length:
 *                 return 1
 *             
 *             limit = 1 if is_limit else 0
 *             six = 1 if last_is6 else 0
 *             if memo[pos][limit][six] != -1:
 *                 return memo[pos][limit][six]
 *             
 *             res = 0
 *             up = int(num_str[pos]) if is_limit else 9
 *             
 *             for d in range(up + 1):
 *                 if d == 4:
 *                     continue
 *                 if last_is6 and d == 2:
 *                     continue
 *                 
 *                 next_limit = is_limit and (d == up)
 *                 next_last_is6 = (d == 6)
 *                 res += dfs(pos + 1, next_limit, next_last_is6)
 *             
 *             memo[pos][limit][six] = res
 *             return res
 *         
 *         return dfs(0, True, False)
 *     
 *     return helper(m) - helper(n - 1)
 * 
 * if __name__ == "__main__":
 *     print("数字1的个数测试:")
 *     print("n=13:", count_digit_one(13))
 *     print("n=0:", count_digit_one(0))
 *     
 *     print("\n不要62测试:")
 *     print("[1,100]:", count_no62(1, 100))
 */